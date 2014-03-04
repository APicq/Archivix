package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageTableModel;
import org.sqlite.SQLiteErrorCode;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by pic on 2/23/14.
 */
public class AddNewTagWorker extends SpecializedWorker {


    private final String tag;

    /**
     * Constructor
     *
     * @param mainFrame
     */
    public AddNewTagWorker(MainFrame mainFrame, String tag) {
        super(mainFrame, "Ajout d'un nouveau tag");
        setIndeterminate(true);
        this.tag = tag ;

    }

    @Override
    protected Void doInBackground() throws Exception {

        // Add new tag into tagref :
        // -------------------------
        try {
            PreparedStatement addNewTagRefStmt = pStatement("INSERT INTO tagsref(name) VALUES(?)");
            addNewTagRefStmt.setString(1,tag.trim().replaceAll(" +","_"));
            addNewTagRefStmt.execute();
        }
        catch (SQLException e){
            if(e.getErrorCode()== SQLiteErrorCode.SQLITE_OK.code ||
                    e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code){
                addError("Erreur : le tag "+tag+" existe déjà");
            } else {
                addError(e.toString());
            }
            return null ;
        }

        // Add tag to messages :
        // build an array of message ids : // todo copy code
        publish("Recherche des messages à tagger..");
        int[] selectedRows = mainFrame.getMessageTable().getSelectedRows();
        int[] messageIds = new int[selectedRows.length];
        MessageTableModel mtm = (MessageTableModel) mainFrame.getMessageTable().getModel();
        for(int x=0 ; x<selectedRows.length ; x++){
            messageIds[x] = mtm.get(selectedRows[x]).id();
        }

        setMaximum(messageIds.length);
        int progressCursor = 0 ;
        for(int messageId :messageIds){
            try {
                setProgress(++progressCursor);
                PreparedStatement addNewTagStmt = pStatement("INSERT INTO tags(msgid,tag) VALUES(?,?)");
                addNewTagStmt.setInt(1,messageId);
                addNewTagStmt.setString(2,tag);
                addNewTagStmt.execute();
            } catch (SQLException e){
                if(e.getErrorCode()== SQLiteErrorCode.SQLITE_OK.code ||
                        e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code){
                    addError("Problème de doublon : tag="+tag+" message id="+messageId);
                    log.warning(e.getMessage());
                } else {
                    addError(e.getMessage());
                    return null ;
                }
            }
        } // for
        return null ;
    }

    @Override
    protected void done() {
        super.done();
        if(!isError()){
            JOptionPane.showMessageDialog(mainFrame,"Le tag "+tag+" a bien été ajouté aux messages sélectionnés");
        }

    }
}
