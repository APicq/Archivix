package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Insert messages into database
 */
public class InsertMessageWorker extends SwingWorker<Integer,String> {

    // log
    public static final Logger log = Logger.getLogger("Archivix");

    // Array of files to be inserted into database
    private File[] messageFiles ;

    // Show a progressbar
    private ProgressMonitor progressMonitor ;

    // reference to main Gui components
    private MainFrame mainFrame ;

    // all result type :
    private static final Integer resultOK = 0 ;
    private static final Integer resultERROR = 1 ;
    private static final Integer resultCANCEL = 2 ;

    // Status value after doInBackground
    private int status = resultOK ;

    // Show information after operation
    private ArrayList<String> notifications ;

    private Connection con ;


    /**
     * Constructor
     * @param mainFrame   mainefFrame
     */
    public InsertMessageWorker(MainFrame mainFrame){

        this.mainFrame = mainFrame ;
        notifications = new ArrayList<String>();

        // select files to push into db :
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        if(fileChooser.showOpenDialog(mainFrame)==JFileChooser.APPROVE_OPTION){
            messageFiles = fileChooser.getSelectedFiles();
            progressMonitor = new ProgressMonitor(
                    mainFrame,
                    "Insertion des messages dans la base de données",
                    "",0,messageFiles.length);
            execute();
        }
    } // constructor





    //todo : block gui while insert
    @Override
    protected Integer doInBackground() throws Exception {

        int index = 0;

        for(File messageFile:messageFiles) {

            // Show informations :
            log.info("parsing "+messageFile.toString());
            progressMonitor.setProgress(++index);
            progressMonitor.setNote(""+index+"/"+messageFiles.length+" "+messageFile.getName());
            if(progressMonitor.isCanceled()) {
                status = resultCANCEL ;
                return resultCANCEL;
            }

            // parse message
            MAPIMessage mapiMessage;
            try {
                mapiMessage = new MAPIMessage(messageFile.getAbsolutePath());
                log.info("parse message OK : "+messageFile.getAbsolutePath());
            }
            catch (IOException e){
                log.warning(e.toString());
                status = resultERROR ;
                continue;
            }

            // ** init connection :
            try {
                con = DriverManager.getConnection(
                        "jdbc:sqlite:" + mainFrame.databaseFile() );
            }
            catch(SQLException e){   // no connection : abort
                notifications.add("Erreur : ouverture connexion impossible "+e.getMessage());
                status = resultERROR ;
                return null;
            }

            // ** Check duplicate messages :
            int messageID = -1 ;
            try {
                messageID = checkDuplicate(mapiMessage);
            }
            catch (SQLException e){//Signal error,next message
                log.warning("Erreur duplicate SQL "+messageFile.getAbsolutePath());
                notifications.add("SQL " + messageFile.getName() + " " + e.getMessage());
                status=resultERROR;
                continue;
            }
            catch (ChunkNotFoundException e){  //idem
                log.warning("Erreur duplicate Chunk "+messageFile.getAbsolutePath());
                notifications.add("Chunk " + messageFile.getName() + " " + e.getMessage());
                status=resultERROR;
                continue;
            }

            // If new message,insert it :
            if( messageID != -1 ){
                try {
                    messageID = insertMessage(mapiMessage);
                }
                catch (SQLException e){
                    log.warning("Erreur insert message SQL "+messageFile.getAbsolutePath());
                    notifications.add("SQL " + messageFile.getName()+" "+e.getMessage());
                    status=resultERROR;
                    continue;
                }
                catch (ChunkNotFoundException e){
                    log.warning("Erreur duplicate chunk "+messageFile.getAbsolutePath());
                    notifications.add("chunk " + messageFile.getName()+" "+e.getMessage());
                    status=resultERROR;
                    continue;
                }
            }


            // insert attachments
            AttachmentChunks[] attachmentChunksList = mapiMessage.getAttachmentFiles();

            if (attachmentChunksList == null || attachmentChunksList.length == 0) continue;

            for (AttachmentChunks attach : attachmentChunksList) {

                if(progressMonitor.isCanceled()) {
                    status = resultCANCEL ;
                    return  resultCANCEL ;
                }

                log.info("Attachment : "+attach.attachFileName);

                // ignore embedded outlook messages :
                if (attach.getEmbeddedAttachmentObject() == null) {
                    log.warning("Embedded outlook message");
                    notifications.add("Embedded outlook message in "+messageFile.getName());
                    continue;
                }


                // Check if attachment is already saved :
                String md5 = "" ;
                boolean attachAlreadySaved ;
                try {
                md5 = md5Sum(attach.getEmbeddedAttachmentObject());
                attachAlreadySaved = isAttachAlreadySaved(attach,md5);
                }
                catch(IOException e){
                    log.warning("Erreur attach IO "+messageFile.getAbsolutePath());
                    notifications.add("attach IO " + messageFile.getName()+" "+e.getMessage());
                    status=resultERROR;
                    continue;
                }
                catch (NoSuchAlgorithmException e){
                    log.warning("Erreur duplicate chunk "+messageFile.getAbsolutePath());
                    notifications.add("chunk " + messageFile.getName()+" "+e.getMessage());
                    status=resultERROR;
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
                        log.warning("Error copy "+messageFile.getAbsolutePath());
                        notifications.add("Error copy " + messageFile.getName()+" "+e.getMessage());
                        status=resultERROR;
                        continue;
                    }
                }


                // update attachment table
                try {
                    insertAttachToDatabase(attach, messageID, md5);
                }
                catch (SQLException e){
                    log.warning("Error SQL insert "+messageFile.getAbsolutePath());
                    notifications.add("Error insert " + messageFile.getName()+" "+e.getMessage());
                    status=resultERROR;
                    continue;
                }
            }  // for each attachment
        } // for each fileMessage

        status = resultOK ;
        return resultOK ;
    }



    /**
     * Insert attach parameters into table
     * @param attach
     */
    private void insertAttachToDatabase(AttachmentChunks attach,int messageID,String md5)
            throws SQLException {
        PreparedStatement pStatement = con.prepareStatement(
                "INSERT INTO attach(msgid,name,size,md5sum) " +
                " VALUES(?,?,?,?)");
        pStatement.setInt(1, messageID);
        pStatement.setString(2, attach.attachLongFileName.toString());
        pStatement.setInt(3, attach.getEmbeddedAttachmentObject().length);
        pStatement.setString(4,md5);
        pStatement.execute();
    }


    /**
     * Check if an attachment is saved in archive directory
     * @param attach
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private boolean isAttachAlreadySaved(AttachmentChunks attach,String md5)
            throws NoSuchAlgorithmException, IOException {
        List<String> listOfNames =
                buildCleanFileNames(mainFrame.attachmentDirectory());
        for (String name : listOfNames) {
            if (md5.equals(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Insert a message in DB and returns its primary key
     * @param mapiMessage
     * @return
     */
    private int insertMessage(MAPIMessage mapiMessage) throws SQLException, ChunkNotFoundException {
        PreparedStatement pStatement = con.prepareStatement(
                "INSERT INTO messages "+      // todo change schema
                        "(AUTEUR,SUJET,CORPS,DATE,DEST,PJ,CC,BCC)" +
                        " VALUES(?,?,?,?,?,?,?,?)");
        pStatement.setString(1, mapiMessage.getDisplayFrom());
        pStatement.setString(2, mapiMessage.getSubject());
        pStatement.setString(3, mapiMessage.getTextBody());
        pStatement.setString(4,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                mapiMessage.getMessageDate().getTime()));
        pStatement.setString(5, mapiMessage.getDisplayTo());
        pStatement.setInt(   6, mapiMessage.getAttachmentFiles().length);
        pStatement.setString(7, mapiMessage.getDisplayCC());
        pStatement.setString(8, mapiMessage.getDisplayBCC());
        pStatement.execute();

        // pick up message id :
        pStatement = con.prepareStatement("SELECT last_insert_rowid()");
        ResultSet rs = pStatement.executeQuery();
        if (rs.next()) {
            pStatement = con.prepareStatement("SELECT id FROM messages WHERE rowid=?");
            pStatement.setInt(1, rs.getInt(1));
            rs = pStatement.executeQuery();
            rs.next();
        }
        return rs.getInt(1);
    }

    /**
     * return primary key of message if it's in the database,-1 otherwise ;
     * @param mapiMessage
     * @return
     */
    private int checkDuplicate(MAPIMessage mapiMessage) throws SQLException,
            ChunkNotFoundException {

        PreparedStatement pStatement = con.prepareStatement(
                "SELECT id FROM messages WHERE from=? AND date=? AND subject=? AND text=? AND to=?");

        pStatement.setString(1, mapiMessage.getDisplayFrom());
        pStatement.setString(2,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        mapiMessage.getMessageDate().getTime()));
        pStatement.setString(3, mapiMessage.getSubject());
        pStatement.setString(4, mapiMessage.getHtmlBody());
        pStatement.setString(5, mapiMessage.getDisplayTo());

        ResultSet rs = pStatement.executeQuery();

        if( rs.next() ){ // message already in database : pick up messageID
            return rs.getInt(1);
        }
        else return -1 ;
    }


    /**
     * Returns a string as a md5 calculation of byte content
     * @param content  byte array
     * @return         md5 string
     * @throws NoSuchAlgorithmException
     */
    private String md5Sum(byte[] content) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] digest = md.digest(content);

        BigInteger bigInt = new BigInteger(1, digest);

        return bigInt.toString(16);
    }

    /**
     *  Returns a list of file name, without extension. Retrieve md5sum from attach
     *  directory.
     * @param directory
     * @return
     * @throws IOException
     */
    private List<String> buildCleanFileNames(String directory) throws IOException {

        List<String> listOfNames = new ArrayList<String>();

        File[] files = new File(directory).listFiles();
        //DirectoryStream<Path> attachDirStream = Files.newDirectoryStream(source);

        String separator = ".";

        for (File oneFile : files) {

            int extensionIndex  = oneFile.getName().toString().lastIndexOf(separator);
            //attachPath.getFileName().toString().lastIndexOf(separator);
            if (extensionIndex == -1) {
                listOfNames.add(oneFile.getName().toString());
            } else {
                listOfNames.add(oneFile.getName().toString().substring(0, extensionIndex));
            }
        } // for
        return listOfNames;
    }

    @Override
    protected void done() {
        log.info("Done");
        //close the progress dialog
        // check errors
        // show dialog if errors
    }
}
