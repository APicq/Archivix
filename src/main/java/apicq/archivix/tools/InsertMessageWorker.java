package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;

import javax.swing.*;
import javax.swing.text.StringContent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Insert messages into database
 */
public class InsertMessageWorker extends SwingWorker<Integer,String> {

    // log
    public static final Logger log = Logger.getLogger("Archivix");

    // String separator
    private static final String SEP = System.getProperty("line.separator");

    // User name
    private static final String USER = System.getProperty("user.name");

    // Array of outlook msg files to be inserted into database
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

    //private Connection con ;


    /**
     * Constructor
     * @param mainFrame   mainefFrame
     */
    public InsertMessageWorker(MainFrame mainFrame){

        this.mainFrame = mainFrame ;
        notifications = new ArrayList<String>();

        // select files to push into db :
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choisissez un ou des fichiers messages outlook (.msg)");
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


    /**
     * archive message and attachments
     * @return
     * @throws Exception
     */
    //todo ?: block gui while insert
    @Override
    protected Integer doInBackground() throws Exception {

        int index = 0; // only used for showing partial results in progressDialog

        // init connection, or exit in case of error
        try {
            mainFrame.pConnection().init();
        }
        catch(SQLException e){   // no connection : abort
            dibError("SQL open connection error : "+e.getMessage());
            status = resultERROR ;
            return null;
        }


        for(File messageFile:messageFiles) {

            // Show infos on dialog :
            progressMonitor.setProgress(++index);
            progressMonitor.setNote(
                    "" + index + "/" + messageFiles.length + " " + messageFile.getName());
            if(progressMonitor.isCanceled()) {
                dibError("Archiving canceled");
                status = resultCANCEL ;
                return resultCANCEL;
            }

            // parse message
            MAPIMessage mapiMessage;
            try {
                mapiMessage = new MAPIMessage(messageFile.getAbsolutePath());
            }
            catch (IOException e){
                dibError(messageFile.getAbsolutePath()+" not a message file");
                log.warning(messageFile.getAbsolutePath()+" not a message file");
                status = resultERROR ;
                continue;
            }



            // ** Check duplicate messages :
            int messageID = -1 ;
            try {
                messageID = dibCheckDuplicate(mapiMessage);
            }
            catch (SQLException e){//Signal error,next message
                dibError("Duplicate error : "+e.getMessage());
                status=resultERROR;
                continue;
            }
            catch (ChunkNotFoundException e){  //idem
                dibError("Duplicate chunk error : "+e.getMessage());
                status=resultERROR;
                continue;
            }

            // If new message,insert it :
            if( messageID == -1 ){
                try {
                    messageID = dibInsertMessage(mapiMessage);
                }
                catch (SQLException e){
                    dibError("Insert error : "+e.getMessage());
                    status=resultERROR;
                    continue;
                }
                catch (ChunkNotFoundException e){
                    dibError("Chunk error : "+e.getMessage());
                    status=resultERROR;
                    continue;
                }
            }


            // insert attachments
            AttachmentChunks[] attachmentChunksList = mapiMessage.getAttachmentFiles();

            if (attachmentChunksList == null || attachmentChunksList.length == 0) continue;

            for (AttachmentChunks attach : attachmentChunksList) {

                if(progressMonitor.isCanceled()) {
                    dibError("Cancelled");
                    status = resultCANCEL ;
                    return  resultCANCEL ;
                }

                // ignore embedded outlook messages :
                if (attach.getEmbeddedAttachmentObject() == null) {
                    dibError("Embedded outlook message in " + messageFile.getName());
                    // add to error string todo
                    continue;
                }


                // Check if attachment is already saved :
                String md5 = "" ;
                boolean attachAlreadySaved ;
                try {
                    md5 = dibMd5Sum(attach.getEmbeddedAttachmentObject());
                    attachAlreadySaved = dibIsAttachAlreadySaved(attach, md5);
                }
                catch(IOException e){
                    dibError("Erreur attach IO "+messageFile.getName()+" "+e.getMessage());
                    status=resultERROR;
                    continue;
                }
                catch (NoSuchAlgorithmException e){
                    dibError("Erreur md5sum "+messageFile.getName()+" "+e.getMessage());
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
                        dibError("Error copy" + messageFile.getName());
                        status=resultERROR;
                        continue;
                    }
                }
                else {
                    log.warning("Attachment file "+attach.attachLongFileName+" already saved.");
                }


                // update attachment table
                try {
                    dibInsertAttachToDatabase(attach, messageID, md5);
                }
                catch (SQLException e){
                    dibError("Error SQL insert " + messageFile.getName());
                    status=resultERROR;
                    continue;
                }
            }  // for each attachment
        } // for each fileMessage

        status = resultOK ;
        return resultOK ;
    }



    /**
     * do in background : Insert attach parameters into table,if not exists
     * @param attach
     */
    private void dibInsertAttachToDatabase(AttachmentChunks attach, int messageID, String md5)
            throws SQLException {

        // Check if already saved ;
        PreparedStatement pStatement = mainFrame.pConnection().prepareStatement(
                "SELECT id from attach where msgid=? and name=? and md5sum =?");
        pStatement.setInt(1,messageID);
        pStatement.setString(2, attach.attachLongFileName.toString());
        pStatement.setString(3,md5);
        ResultSet rs = pStatement.executeQuery();
        if(rs.next()){
            log.warning("attach "+attach.attachLongFileName+" already in database,msgid="+messageID);
        }
        else {
            pStatement = mainFrame.pConnection().prepareStatement(
                    "INSERT INTO attach(msgid,name,size,md5sum) " +
                            " VALUES(?,?,?,?)");
            pStatement.setInt(1, messageID);
            pStatement.setString(2, attach.attachLongFileName.toString());
            pStatement.setInt(3, attach.getEmbeddedAttachmentObject().length);
            pStatement.setString(4,md5);
            pStatement.execute();
        }
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
     * Insert a message in DB and returns its primary key
     * @param mapiMessage
     * @return
     */
    private int dibInsertMessage(MAPIMessage mapiMessage) throws SQLException, ChunkNotFoundException {
        PreparedStatement pStatement = mainFrame.pConnection().prepareStatement(
                "INSERT INTO messages("+
                        "date," +
                        "author,"+
                        "subject,"+
                        "recip,"+
                        "body,"+
                        "attach,"+
                        "mailrecip,"+
                        "cc,"+
                        "bcc," +
                        "username,"+
                        "insertdate) "+
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        pStatement.setString(1,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        mapiMessage.getMessageDate().getTime()));
        pStatement.setString(2, mapiMessage.getDisplayFrom());
        pStatement.setString(3, mapiMessage.getSubject());
        pStatement.setString(4, mapiMessage.getDisplayTo());
        pStatement.setString(5, purge(mapiMessage.getTextBody()));
        pStatement.setInt(6, mapiMessage.getAttachmentFiles().length);
        pStatement.setString(7,mapiMessage.getRecipientEmailAddress());
        pStatement.setString(8, mapiMessage.getDisplayCC());
        pStatement.setString(9, mapiMessage.getDisplayBCC());
        pStatement.setString(10, USER);
        pStatement.setString(11,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        pStatement.execute();

        // pick up message id :
        pStatement = mainFrame.pConnection().
                prepareStatement("SELECT last_insert_rowid()");
        ResultSet rs = pStatement.executeQuery();
        if (rs.next()) {
            pStatement = mainFrame.pConnection().
                    prepareStatement("SELECT id FROM messages WHERE rowid=?");
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
    private int dibCheckDuplicate(MAPIMessage mapiMessage) throws SQLException,
            ChunkNotFoundException {

        PreparedStatement pStatement = mainFrame.pConnection().prepareStatement(
                "SELECT id FROM messages WHERE "+
                        "author=? AND date=? AND subject=? AND body=? AND recip=?");
        pStatement.setString(1, mapiMessage.getDisplayFrom());
        pStatement.setString(2,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        mapiMessage.getMessageDate().getTime()));
        pStatement.setString(3, mapiMessage.getSubject());
        pStatement.setString(4, purge(mapiMessage.getTextBody()));
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
    private String dibMd5Sum(byte[] content) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] digest = md.digest(content);

        BigInteger bigInt = new BigInteger(1, digest);

        //todo : zero fill result : so all filenames have same chars number

        return bigInt.toString(16);
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
     * Error management :
     * @param s string to print
     */
    private void dibError(String s){
        log.warning(s);
        notifications.add(s+SEP);
    }

    /**
     * Try to clean strings from outlook CRLF->SEP and
     * remove extra lines.
     * @param inputString
     * @return
     */
    private String purge(final String inputString){
        return inputString.replaceAll(
                "" + (char)(0x0d) , "" ).replaceAll(
                "" + (char) (0x0a) + "+" + ".{0,1}" + (char) (0x0a) + "+", SEP
        );
    }

    @Override
    protected void done() {
        log.info("Done");
        //close the progress dialog
        // check errors
        // show dialog if errors
    }
}
