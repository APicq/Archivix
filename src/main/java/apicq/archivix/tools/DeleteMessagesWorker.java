package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageTable;
import apicq.archivix.gui.MessageTableModel;
import apicq.archivix.gui.TextMessage;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by pic on 2/23/14.
 */
public class DeleteMessagesWorker extends SpecializedWorker {


    /**
     * Constructor
     *
     * @param mainFrame
     */
    public DeleteMessagesWorker(MainFrame mainFrame) {
        super(mainFrame, "Effacement des messages");
    }

    @Override
    protected Void doInBackground() throws Exception {

        // build an array of message ids :
        int[] selectedRows = mainFrame.getMessageTable().getSelectedRows();
        int[] messageIds = new int[selectedRows.length];
        MessageTableModel mtm = (MessageTableModel) mainFrame.getMessageTable().getModel();
        for(int x=0 ; x<selectedRows.length ; x++){
            messageIds[x] = mtm.get(selectedRows[x]).id();
        }

        for(int messageId : messageIds){
            // find md5 :
            PreparedStatement findMd5Stmt = pStatement("SELECT md5sum from attach where msgid=?");
            findMd5Stmt.setInt(1,messageId);
            ResultSet rs = findMd5Stmt.executeQuery();
            ArrayList<String> md5List = new ArrayList<String>();
            while(rs.next()){
                md5List.add(rs.getString(1));
            }
            // delete attachment files :
            for(String md5str : md5List){
                String directory = mainFrame.attachmentDirectory();
                File[] files = new File(directory).listFiles();
                for(File file : files){
                    String md5file = "" ;
                    int extensionIndex  = file.getName().toString().lastIndexOf(".");
                    if(extensionIndex==-1) md5file = file.getName().toString();
                    else md5file = file.getName().toString().substring(0, extensionIndex);
                    if(md5str.equals(md5file)){
                        if(!file.delete()){
                            addError("Message id"+messageId+" :");
                            addError("Impossible d'effacer la pièce jointe "+file.getName());
                        }
                    }

                }
            }

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
                    "DELETE FROM TAGS WHERE msgid="+messageId);
            removeMessageStmt.execute();
        } // for each message id
        return null ;
    }

    @Override
    protected void done() {
        // refresh message table :
        new FindUserWorker(mainFrame).start();
    }
}
