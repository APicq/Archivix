package apicq.archivix.tools;

import apicq.archivix.gui.AttachmentSignature;
import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.TextMessage;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by pic on 2/26/14.
 */
public class CreateReportWorker extends SpecializedWorker {

    // todo :

    // if true, cancel doInBackground method
    private boolean actionIsCanceled = false ;

    // selected rows in message table : every messages to be saved
    private final int[] selectedRows ;

    // Base directory to save messages:
    private final String messageDir ;

    // Number of saved messages :
    private int savedMessages = 0 ;


    /**
     * Constructor
     *
     * @param mainFrame
     */

    public CreateReportWorker(MainFrame mainFrame) {
        super(mainFrame, "Sauvegarde des messages");

        // Ask for directory via fileChooser :
        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisissez le répertoire pour la sauvegarde des messages");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = chooser.showOpenDialog(mainFrame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            messageDir = chooser.getSelectedFile().getAbsolutePath();
            selectedRows = mainFrame.getMessageTable().getSelectedRows();
            log.info(""+selectedRows);
        }
        else { // Cancel action
            selectedRows = null ;
            messageDir = "" ;
            actionIsCanceled = true ;
        }
    }



    @Override
    protected void done() {
        super.done();
        JOptionPane.showMessageDialog(mainFrame,
                ""+savedMessages+" message(s) sauvegardé(s)"+SEP+
                        "dans le répertoire : "+messageDir);
    }

    @Override
    protected Void doInBackground() throws Exception {


        setMaximum(selectedRows.length);

//        MessageTableModel mtm = (MessageTableModel) mainFrame.getMessageTable().getModel();

        // for each message :
        for(int index : selectedRows){

            if(actionIsCanceled) return null;

            setProgress(++savedMessages);

//            TextMessage tm = mtm.get(index);
            TextMessage tm = mainFrame.getMessageTable().get(index);
            setString("message "+savedMessages+"/"+selectedRows.length);

            // Create message directory
            File messageDir = newNamedFile(this.messageDir,"message_"+tm.id(),"");
            if(messageDir==null){
                addError("Erreur : message "+tm.id()+" : trop de messages sauvegardés");
                continue;
            }
            try {
                messageDir.mkdir();
            }catch (SecurityException e){
                addError("Erreur : Impossible de créer le répertoire " + messageDir.getAbsolutePath());
                continue ;
            }

            // Save attachments :
            for(AttachmentSignature as : tm.attachmentSignatures()){
                // check messageDir + name
                // retrieve file :
                File[] archivedFiles = new File(mainFrame.attachmentDirectory()).listFiles();
                for(File archivedFile : archivedFiles){

                    String archivedMd5 = "";
                    int extensionIndex  = archivedFile.getName().lastIndexOf(".");
                    if (extensionIndex == -1) {
                        archivedMd5 = archivedFile.getName();
                    } else {
                        archivedMd5 = archivedFile.getName().substring(0, extensionIndex);
                    }
                    if(archivedMd5.equals(as.getMd5())){ // copy message
                        File destinationFile = new File(messageDir.getAbsolutePath(),as.getName());
                        try {
                            Files.copy(archivedFile.toPath(),destinationFile.toPath());
                        } catch (IOException e){
                            addError("Impossible de récuperer la pièce jointe : "+as.getName());
                        }
                        break ;
                    }
                }
            }
            // Save message file
            File textMessagefile = newNamedFile(messageDir.getAbsolutePath(),"message",".txt");
            if(textMessagefile==null){
                addError("Erreur : impossible de sauvegarder le message "+tm.id());
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(textMessagefile));
            try {
                bw.write(tm.toString());
                bw.close();
            } catch (IOException e){
                addError("Erreur : impossible de sauvegarder le message "+tm.id());
                addError(e.getMessage());
            }
        }
        return null;
    }

    /**
     * create a File base on parent and base name, with integer extension.
     * @param parent
     * @param base
     * @param suffix
     * @return
     */
    public static File newNamedFile(String parent,String base,String suffix){
        File f = new File(parent,base+suffix);
        if(!f.exists()) return f;
        // Seek a correct name :100 max, should be enough
        for(int index=0 ; index<100 ; index++ ){
            f = new File(parent,base+"_"+index+suffix);
            if(!f.exists()) return f ;
        }
        return null ; // too many files created, return null
    }

}
