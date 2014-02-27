package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageTableModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Apply list of tags to selected messages
 */
public class ApplyTagWorker extends  SpecializedWorker{

    private final ArrayList<String> newTagsList;

    public ApplyTagWorker(MainFrame mainFrame, ArrayList<String> newTagsList) {
        super(mainFrame,"Modification des messages avec les nouveaux tags");
        this.newTagsList = newTagsList;
        setMaximum(mainFrame.getMessageTable().getSelectedRowCount());
    }

    @Override
    protected void done() {
        super.done();
        // update search :
        new FindMessagesWorker(mainFrame).start();
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

        // remove old tags :
        publish("nettoyage des tags");
        int countDeleteMessage=0;
        for(int messageId:messageIds){
            setProgress(++countDeleteMessage);
            try {
            PreparedStatement removeTagStmt = pStatement(
                    "DELETE FROM tags WHERE msgid=?");
            removeTagStmt.setInt(1,messageId);
            removeTagStmt.execute();
            }
            catch (SQLException e){
                addError("Erreur lors du traitement du message "+messageId);
                addError(e.getMessage());
                return  null ;
            }
        }
        // apply new tags :
        for(String newTag : newTagsList){
            publish("création du tag : "+newTag);
            int countApplyMessage = 0 ;
            for(int messageId:messageIds){
                setProgress(++countApplyMessage);
                try{
                    PreparedStatement addNewTagStmt =
                            pStatement("INSERT INTO tags(msgid,tag) VALUES(?,?)");
                    addNewTagStmt.setInt(1,messageId);
                    addNewTagStmt.setString(2,newTag);
                    addNewTagStmt.execute();
                }catch (SQLException e){
                    addError("Problème avec le tag : "+newTag+ " pour le message "+messageId);
                    addError(e.toString());
                }
            }
        }
        return null ;
    }
}
