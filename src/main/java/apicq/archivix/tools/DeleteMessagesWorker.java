package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageTableModel;

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


    /**
     * Constructor
     *
     * @param mainFrame
     */

    private int returnValue = JOptionPane.OK_OPTION ;

    public DeleteMessagesWorker(MainFrame mainFrame) {
        super(mainFrame, "Effacement des messages");
        returnValue = JOptionPane.showConfirmDialog(
                null,
                "Etes-vous sûr de vouloir effacer les messages ?",
                "Etes-vous sûr de vouloir effacer les messages ?",
                JOptionPane.YES_NO_OPTION);
    }

    @Override
    protected Void doInBackground() throws Exception {

        if( returnValue != JOptionPane.YES_OPTION) return null ;

        // build an array of message ids :
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

            // find md5 :
            PreparedStatement findMd5Stmt = pStatement("SELECT md5sum from attach where msgid=?");
            findMd5Stmt.setInt(1,messageId);
            ResultSet rs = findMd5Stmt.executeQuery();
            ArrayList<String> md5List = new ArrayList<String>();
            while(rs.next()){
                md5List.add(rs.getString(1));
            }
            // delete attachment files :
            // check if file to be deleted has other references :
            for(String md5str : md5List){
                PreparedStatement findOtherMd5Stmt = pStatement("SELECT COUNT(id) FROM attach WHERE md5sum=?");
                findOtherMd5Stmt.setString(1,md5str);
                ResultSet findOtherMd5Result = findOtherMd5Stmt.executeQuery();//todo tester si ca marche
                if(rs.next() && (rs.getInt(1))==1){ // Only one md5sum found in table : delete
                    String directory = mainFrame.attachmentDirectory();
                    File[] files = new File(directory).listFiles();
                    if(files==null) continue ;
                    for(File file : files){
                        String md5file = "" ;
                        int extensionIndex  = file.getName().lastIndexOf(".");
                        if(extensionIndex==-1) md5file = file.getName();
                        else md5file = file.getName().substring(0, extensionIndex);
                        if(md5str.equals(md5file)){
                            if(!file.delete()){
                                addError("Message id"+messageId+" :");
                                addError("Impossible d'effacer la pièce jointe "+file.getName());
                            }
                        }
                    }
                }
            }
            try {
                // delete attach table rows :
                PreparedStatement removeAttachStmt = pStatement(
                        "DELETE FROM ATTACH WHERE msgid="+messageId);
                removeAttachStmt.execute();
                // delete tags :
                PreparedStatement removeTagStmt = pStatement(
                        "DELETE FROM TAGS WHERE msgid="+messageId);
                removeTagStmt.execute();
                // delete message :
                PreparedStatement removeMessageStmt = pStatement(
                        "DELETE FROM messages WHERE id="+messageId);
                removeMessageStmt.execute();
            } catch (SQLException e){
                addError("Erreur lors de la mise à jour de la base de données");
                addError("Message id : "+messageId);
                addError(e.getMessage());
            }

        } // for each message id
        return null ;
    }

    @Override
    protected void done() {
        super.done();
        // refresh message table :
        new FindMessagesWorker(mainFrame).start();
    }
}
