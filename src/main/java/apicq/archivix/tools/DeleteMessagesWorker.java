package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import javax.swing.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 */
public class DeleteMessagesWorker extends SpecializedWorker {



    private int returnValue = JOptionPane.OK_OPTION ;
    private ArrayList<String> deletedMessageList ;
    private ArrayList<String> deletedAttachList ;


    /**
     *
      * @param mainFrame
     */
    public DeleteMessagesWorker(MainFrame mainFrame) {
        super(mainFrame, "Effacement des messages");
        returnValue = JOptionPane.showConfirmDialog(
                null,
                "Etes-vous sûr de vouloir effacer les messages ?",
                "Etes-vous sûr de vouloir effacer les messages ?",
                JOptionPane.YES_NO_OPTION);
        deletedMessageList = new ArrayList<String>();
        deletedAttachList = new ArrayList<String>();
    }

    @Override
    protected Void doInBackground() throws Exception {

        if( returnValue != JOptionPane.YES_OPTION) return null ;

        // build an array of message ids : //todo copy code
        int[] selectedRows = mainFrame.getMessageTable().getSelectedRows();
        int[] messageIds = new int[selectedRows.length];
        MessageTableModel mtm = (MessageTableModel) mainFrame.getMessageTable().getModel();
        for(int x=0 ; x<selectedRows.length ; x++){
            messageIds[x] = mtm.get(selectedRows[x]).id();
        }

        setMaximum(messageIds.length);
        int progressIndex = 0 ;

        for(int messageId : messageIds){

            setProgress(++progressIndex);
            setString("message "+messageId);

            // --------------------
            // 1 : clean tags table
            // --------------------
            try {
                PreparedStatement cleanTagsStmt = pStatement("DELETE FROM tags WHERE msgid=?");
                cleanTagsStmt.setInt(1,messageId);
                cleanTagsStmt.execute();

            }catch (SQLException e){
                addError("Problème avec la table : tags");
                addError(e.getMessage());
                return null;
            }

            // -----------------------
            // 2 : clean tagsref table
            // -----------------------
            try {
                PreparedStatement cleanTagsRefStmt = pStatement(
                        "DELETE FROM tagsref WHERE name NOT IN (SELECT DISTINCT tag from tags)");
                cleanTagsRefStmt.execute();
            }catch (SQLException e){
                addError("Problème avec la table : tagsref");
                addError(e.getMessage());
                return null;
            }

            // ----------------------
            // 3 : clean attach table
            // ----------------------
            try {
                PreparedStatement cleanattachStmt = pStatement(
                        "DELETE FROM attach WHERE msgid=?");
                cleanattachStmt.setInt(1,messageId);
                cleanattachStmt.execute();
            }catch (SQLException e){
                addError("Problème avec la table : attach");
                addError(e.getMessage());
                return null;
            }

            // -------------------
            // 4 : clean message :
            // -------------------
            try {
                PreparedStatement cleanattachStmt = pStatement(
                        "DELETE FROM messages WHERE id=?");
                cleanattachStmt.setInt(1,messageId);
                cleanattachStmt.execute();
                deletedMessageList.add("message effacé : "+messageId);
            }catch (SQLException e){
                addError("Problème avec la table : messages");
                addError(e.getMessage());
                return null;
            }

        } // for each message



        // -----------------------
        // 5 : delete attach files
        // -----------------------
        publish("Effacement des pièces jointes..");
        ArrayList<String> md5sumList = new ArrayList<String>();//list of md5 to delete :
        try { // look for orphan attach md5 :
            PreparedStatement orphanAttachStmt = pStatement(
                    "SELECT md5sum FROM attachref WHERE md5sum NOT IN (SELECT DISTINCT md5sum FROM attach)");
            ResultSet rs = orphanAttachStmt.executeQuery();
            while(rs.next()){
                md5sumList.add(rs.getString(1));
            }
        }catch (SQLException e){
            addError(e.toString());
            return null ;
        }
        // delete files :
        File[] files = new File(mainFrame.attachmentDirectory()).listFiles();
        int deleteCounter = 0 ;
        setMaximum(files.length);
        for(File file:files){
            setProgress(++deleteCounter);
            String filePrefix = "" ;
            int extensionIndex  = file.getName().lastIndexOf(".");
            if(extensionIndex==-1) filePrefix = file.getName();
            else filePrefix = file.getName().substring(0, extensionIndex);
            log.info(filePrefix);
            for(String md5sum:md5sumList){
                if(md5sum.equals(filePrefix)){
                    if(!file.delete()){
                        addError("Impossible d'effacer la pièce jointe "+file.getName());
                    } else {
                        deletedAttachList.add(md5sum);
                    }
                    break;
                }
            } // for each md5sum
        } // for each file

        // -------------------------
        // 5 : clean attachref table
        // -------------------------
        publish("Nettoyage de la table attachref..");
        try {
            PreparedStatement delOrphanAttachStmt = pStatement(
                    "DELETE FROM attachref WHERE md5sum NOT IN (SELECT DISTINCT md5sum FROM attach)");
            delOrphanAttachStmt.execute();
        }catch (SQLException e){
            addError("Problème avec la table : attachref");
            addError(e.getMessage());
        }
        return null ;
    }



    @Override
    protected void done() {
        super.done();
        // refresh message table :
        new FindMessagesWorker(mainFrame).start();
        //debug
        for(String s:deletedMessageList) System.out.println(s);
        for(String s:deletedAttachList) System.out.println(s);
    }
}
