package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.sqlite.SQLiteErrorCode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by pic on 2/23/14.
 */
public class InsertMessageWorker extends SpecializedWorker {

    public static final Logger log = Logger.getLogger("Archivix");

    // String separator
    private static final String SEP = System.getProperty("line.separator");

    // User name
    private static final String USER = System.getProperty("user.name");


    private final File[] messageFiles ;

    /**
     * Constructor
     *
     * @param mainFrame
     */
    public InsertMessageWorker(MainFrame mainFrame, File[] messageFiles) {
        super(mainFrame,"Insertion des messages en cours");
        this.messageFiles = messageFiles ;
    }

    @Override
    protected void done() {
        super.done();
    }

    @Override
    protected Void doInBackground() throws Exception {

        setMaximum(messageFiles.length);

        int cursor = 0 ;

        for( File messageFile : messageFiles ){

            // Update progress
            setProgress(++cursor);
            setString(messageFile.getName());

            // Parse message file
            MAPIMessage mapiMessage;
            try {
                mapiMessage = new MAPIMessage(messageFile.getAbsolutePath());
            }
            catch (IOException e){
                addError(messageFile.getAbsolutePath() + " n'est pas un message.");
                continue;
            }

            // Insert message, go to next message if already in database.
            try {
                dibInsertMessage(mapiMessage);
            }
            catch (SQLException e){
                if(e.getErrorCode()==SQLiteErrorCode.SQLITE_CONSTRAINT.code ||
                        e.getErrorCode() == SQLiteErrorCode.SQLITE_OK.code){
                    addError("Message déja inséré : "+messageFile.getAbsolutePath());
                }
                else {
                    addError("Erreur d'insertion avec le message : "+messageFile.getAbsoluteFile());
                    addError(e.toString());
                    log.info("e.getErrorCode() : "+e.getErrorCode());
                    log.info("SQLITE_CONSTRAINT.code : "+SQLiteErrorCode.SQLITE_CONSTRAINT.code);
                }
                continue;
            }
            catch (ChunkNotFoundException e){
                addError("Chunk error : "+e.getMessage());
                continue;
            }

            // get message id :
            int messageID = -1 ;
            try {
                PreparedStatement getMessageIdStmt = pStatement("SELECT id FROM messages where date=? and subject=?");
                getMessageIdStmt.setString(1,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                mapiMessage.getMessageDate().getTime()));
                getMessageIdStmt.setString(2,mapiMessage.getSubject());
                ResultSet rs = getMessageIdStmt.executeQuery();
                if(rs.next()) messageID = rs.getInt(1);
            } catch (SQLException e){
                addError("Impossible de retrouver la clé primaire du message");
                addError(e.getMessage());
                continue;
            }
            if(messageID == -1){
                addError("Impossible de retrouver la clé primaire du message : id = -1");
                continue;
            }

            // insert attachments
            AttachmentChunks[] attachmentChunksList = mapiMessage.getAttachmentFiles();

            if (attachmentChunksList == null || attachmentChunksList.length == 0) continue;

            for (AttachmentChunks attach : attachmentChunksList) {

                // ignore embedded outlook messages :
                if (attach.getEmbeddedAttachmentObject() == null) {
                    addError("message outlook ignoré dans : " + messageFile.getName());
                    continue;
                }

                // md5 calculation
                String md5 = "" ;
                try {
                    md5 = dibMd5Sum(attach.getEmbeddedAttachmentObject());
                }
                catch (NoSuchAlgorithmException e){
                    addError("Erreur md5sum "+messageFile.getName());
                    addError(e.getMessage());
                    continue;
                }

                // update attachRef :
                try {
                    PreparedStatement updateAttachRefStmt = pStatement(
                            "INSERT INTO attachref(md5sum,name,size) VALUES(?,?,?)");
                    updateAttachRefStmt.setString(1,md5);
                    updateAttachRefStmt.setString(2,attach.attachLongFileName.toString());
                    updateAttachRefStmt.setInt(3, attach.getEmbeddedAttachmentObject().length);
                    updateAttachRefStmt.execute();

                } catch (SQLException e){
                    if(e.getErrorCode()==SQLiteErrorCode.SQLITE_CONSTRAINT.code ||
                            e.getErrorCode()==SQLiteErrorCode.SQLITE_OK.code){
                        addError("Pièce jointe en doublon : "+attach.attachLongFileName.toString());
                    }
                    else {
                        addError("Impossible de mettre à jour la table attachref");
                        addError(e.getMessage());
                    }
                }

                // update attach :
                try {
                    PreparedStatement updateAttachStmt = pStatement(
                            "INSERT INTO attach(md5sum,msgid) VALUES(?,?)");
                    updateAttachStmt.setString(1,md5);
                    updateAttachStmt.setInt(2, messageID);
                    updateAttachStmt.execute();

                } catch (SQLException e){
                    if(e.getErrorCode()==SQLiteErrorCode.SQLITE_CONSTRAINT.code ||
                            e.getErrorCode()==SQLiteErrorCode.SQLITE_OK.code){
                        addError("Pièce jointe déjà insérée : "+attach.attachLongFileName.toString());
                        log.warning("Erreur SQL : INSERT INTO attach(md5sum,msgid) VALUES(?,?)");
                                log.warning("messageID : "+messageID+" md5 : "+md5);

                    } else {
                        addError("Erreur lors de la mise à jour la table attach");
                        addError(e.getMessage());
                    }

                }
                // Copy file if new :
                try {
                    String finalName = md5 + attach.attachExtension.toString();
                    File inputFile = new File(mainFrame.attachmentDirectory(),finalName);
                    if(!inputFile.exists()){
                        ByteArrayInputStream bais = new ByteArrayInputStream(attach.getEmbeddedAttachmentObject());
                        Files.copy(bais,inputFile.toPath());
                        bais.close();
                    }
                } catch(IOException e){
                    addError("Impossible de sauvegarder la pièce jointe : "+attach.attachLongFileName.toString());
                    addError(e.getMessage());
                }
            }
        }//for each message file
        return null ;
    }

    /**
     * Try to clean strings from outlook CRLF->SEP and
     * remove extra lines.
     * @param inputString
     * @return purged string
     */
    private String dibPurge(final String inputString){
        return inputString.replaceAll(
                "" + (char)(0x0d) , "" ).replaceAll(
                "" + (char) (0x0a) + "+" + ".{0,1}" + (char) (0x0a) + "+", SEP
        );
    }

    /**
     * Insert a message in DB and returns its primary key
     * @param mapiMessage
     * @return
     */
    private void dibInsertMessage(MAPIMessage mapiMessage) throws SQLException, ChunkNotFoundException {
        PreparedStatement pStatement = pStatement(
                "INSERT INTO messages(" +
                        "date," +
                        "author," +
                        "subject," +
                        "recip," +
                        "body," +
                        "attach," +
                        "mailrecip," +
                        "cc," +
                        "bcc," +
                        "username," +
                        "insertdate) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        pStatement.setString(1,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        mapiMessage.getMessageDate().getTime()));
        pStatement.setString(2, mapiMessage.getDisplayFrom());
        pStatement.setString(3, mapiMessage.getSubject());
        pStatement.setString(4, mapiMessage.getDisplayTo());
        pStatement.setString(5, dibPurge(mapiMessage.getTextBody()));
        pStatement.setInt(6, mapiMessage.getAttachmentFiles().length);
        pStatement.setString(7,mapiMessage.getRecipientEmailAddress());
        pStatement.setString(8, mapiMessage.getDisplayCC());
        pStatement.setString(9, mapiMessage.getDisplayBCC());
        pStatement.setString(10, USER);
        pStatement.setString(11,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        pStatement.execute();
    }


    /**
     * Returns a string as a md5 calculation of byte content
     * @param content  byte array
     * @return         md5 string
     * @throws NoSuchAlgorithmException
     */
    private String dibMd5Sum(byte[] content) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(content);
        BigInteger bigInt = new BigInteger(1, digest);
        //todo : zero fill result : so all filenames have same chars number
        return bigInt.toString(16);
    }
}
