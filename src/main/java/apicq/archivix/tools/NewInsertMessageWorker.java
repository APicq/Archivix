package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
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
public class NewInsertMessageWorker extends SpecializedWorker {

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
    public NewInsertMessageWorker(MainFrame mainFrame,File[] messageFiles) {
        super(mainFrame,"Insertion des messages en cours");
        this.messageFiles = messageFiles ;
    }

    @Override
    protected void done() {
        super.done();
    }

    @Override
    protected Void doInBackground() throws Exception {

        getProgressDialog().getProgressBar().setMaximum(messageFiles.length);
        int cursor = 0 ;

        for( File messageFile : messageFiles ){

            // Update progress
            getProgressDialog().getProgressBar().setValue(++cursor);
            getProgressDialog().getProgressBar().setString(messageFile.getName());

            // Parse message file
            MAPIMessage mapiMessage;
            try {
                mapiMessage = new MAPIMessage(messageFile.getAbsolutePath());
            }
            catch (IOException e){
                addError(messageFile.getAbsolutePath() + " not a message file");
                log.warning(messageFile.getAbsolutePath()+" not a message file");
                continue;
            }

            // Check duplicates :
            int messageID = -1 ;
            try {
                messageID = dibCheckDuplicate(mapiMessage);
            }
            catch (SQLException e){//Signal error,next message
                addError("Message déjà présent dans la base de données : "+e.getMessage());
                continue;
            }
            catch (ChunkNotFoundException e){  //idem
                addError("Duplicate chunk error : "+e.getMessage());
                continue;
            }

            // If new message,insert it :
            if( messageID == -1 ){
                try {
                    messageID = dibInsertMessage(mapiMessage);
                }
                catch (SQLException e){
                    addError("Erreur d'insertion : "+e.getMessage());
                    continue;
                }
                catch (ChunkNotFoundException e){
                    addError("Chunk error : "+e.getMessage());
                    continue;
                }
            }

            // insert attachments
            AttachmentChunks[] attachmentChunksList = mapiMessage.getAttachmentFiles();
            if (attachmentChunksList == null || attachmentChunksList.length == 0) continue;

            for (AttachmentChunks attach : attachmentChunksList) {
                // ignore embedded outlook messages :
                if (attach.getEmbeddedAttachmentObject() == null) {
                    addError("message outlook ignoré dans : " + messageFile.getName());
                    // add to error string todo
                    continue;
                }
                // Check if attachment is already saved :
                String md5 = "" ;
                boolean attachAlreadySaved;
                try {
                    md5 = dibMd5Sum(attach.getEmbeddedAttachmentObject());
                    attachAlreadySaved = dibIsAttachAlreadySaved(attach, md5);
                }
                catch(IOException e){
                    addError("Erreur pièce jointe "+messageFile.getName());
                    addError(e.getMessage());
                    continue;
                }
                catch (NoSuchAlgorithmException e){
                    addError("Erreur md5sum "+messageFile.getName());
                    addError(e.getMessage());
                    continue;
                }

                // New attach,copy it :
                if(!attachAlreadySaved){
                    String finalName = md5 + attach.attachExtension.toString();
                    try {
                        File inputFile = new File(mainFrame.attachmentDirectory(),finalName);
                        FileOutputStream fos = new FileOutputStream(inputFile);
                        fos.write(attach.getEmbeddedAttachmentObject());
                        fos.close();
                    }
                    catch (Exception e){
                        addError("Error copie pièce jointe :" + messageFile.getName());
                        continue;
                    }
                }
                else {
                    addError("Attachment file " + attach.attachLongFileName + " already saved.");
                }
                // update attachment table
                try {
                    dibInsertAttachToDatabase(attach, messageID, md5);
                }
                catch (SQLException e){
                    addError("Error mise à jour table pièces jointes :  " + messageFile.getName());
                    continue;
                }
            }
        }//for each message file
        return null ;
    }


    private int dibCheckDuplicate(MAPIMessage mapiMessage) throws
            SQLException,
            ChunkNotFoundException {

        PreparedStatement pStatement = pStatement(
                "SELECT id FROM messages WHERE " +
                        "author=? AND date=? AND subject=? AND body=? AND recip=?");
        pStatement.setString(1, mapiMessage.getDisplayFrom());
        pStatement.setString(2,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        mapiMessage.getMessageDate().getTime()));
        pStatement.setString(3, mapiMessage.getSubject());
        pStatement.setString(4, dibPurge(mapiMessage.getTextBody()));
        pStatement.setString(5, mapiMessage.getDisplayTo());

        ResultSet rs = pStatement.executeQuery();

        if( rs.next() ){ // message already in database : pick up messageID
            return rs.getInt(1);
        }
        else return -1 ;
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
    private int dibInsertMessage(MAPIMessage mapiMessage) throws SQLException, ChunkNotFoundException {
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

        // pick up message id :
        pStatement = pStatement("SELECT last_insert_rowid()");
        ResultSet rs = pStatement.executeQuery();
        if (rs.next()) {
            pStatement = pStatement("SELECT id FROM messages WHERE rowid=?");
            pStatement.setInt(1, rs.getInt(1));
            rs = pStatement.executeQuery();
            rs.next();
        }
        return rs.getInt(1);
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

    /**
     * Check if an attachment is saved in archive directory
     * @param attach
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private boolean dibIsAttachAlreadySaved(AttachmentChunks attach, String md5)
            throws NoSuchAlgorithmException, IOException {
        List<String> listOfNames =
                dibBuildCleanFileNames(mainFrame.attachmentDirectory());
        for (String name : listOfNames) {
            if (md5.equals(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     *  Returns a list of file name, without extension. Retrieve md5sum from attach
     *  directory.
     * @param directory
     * @return
     * @throws IOException
     */
    private List<String> dibBuildCleanFileNames(String directory) throws IOException {

        List<String> listOfNames = new ArrayList<String>();
        File[] files = new File(directory).listFiles();
        String separator = ".";
        for (File oneFile : files) {
            int extensionIndex  = oneFile.getName().toString().lastIndexOf(separator);
            if (extensionIndex == -1) {
                listOfNames.add(oneFile.getName().toString());
            } else {
                listOfNames.add(oneFile.getName().toString().substring(0, extensionIndex));
            }
        } // for
        return listOfNames;
    }


    /**
     * do in background : Insert attach parameters into table,if not exists
     * @param attach
     */
    private void dibInsertAttachToDatabase(AttachmentChunks attach, int messageID, String md5)
            throws SQLException {

        // Check if already saved ;
        PreparedStatement pStatement = pStatement(
                "SELECT id from attach where msgid=? and name=? and md5sum =?");
        pStatement.setInt(1,messageID);
        pStatement.setString(2, attach.attachLongFileName.toString());
        pStatement.setString(3,md5);
        ResultSet rs = pStatement.executeQuery();
        if(rs.next()){
            addError("attach " + attach.attachLongFileName + " already in database,msgid=" + messageID);
        }
        else {
            pStatement = pStatement(
                    "INSERT INTO attach(msgid,name,size,md5sum) " +
                            " VALUES(?,?,?,?)");
            pStatement.setInt(1, messageID);
            pStatement.setString(2, attach.attachLongFileName.toString());
            pStatement.setInt(3, attach.getEmbeddedAttachmentObject().length);
            pStatement.setString(4,md5);
            pStatement.execute();
      }
    }  
}
