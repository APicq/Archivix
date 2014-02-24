package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageTableModel;

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
        super(mainFrame, "Vérification des tags");
        setIndeterminate(true);
        this.tag = tag ;

    }

    @Override
    protected Void doInBackground() throws Exception {

        try {

            // list of all message ids already tagged with tag
            PreparedStatement msgIdForTag = pStatement(
                    " SELECT msgid FROM tags where tag='?' ");
            msgIdForTag.setString(1, tag);
            ResultSet rs = msgIdForTag.executeQuery();
            ArrayList<Integer> msgIdList = new ArrayList<Integer>();
            while(rs.next()){
                msgIdList.add(rs.getInt(1));
            }

            // Apply tag if necessary for each message
            int[] selected  = mainFrame.getMessageTable().getSelectedRows();
            MessageTableModel mtm = (MessageTableModel) mainFrame.getMessageTable().getModel();
            if(selected.length==0) return null;
            for( int i=0 ; i < selected.length ; i++){
                setProgress(i*100/selected.length);
                int id = mtm.get(selected[i]).id();
                boolean isAlreadyTagged = false ;
                for(int x : msgIdList){
                    if(id==x){
                        isAlreadyTagged = true ;
                        break;
                    }
                }
                if(!isAlreadyTagged){
                    PreparedStatement insertTagStmt = pStatement(
                            "INSERT INTO TAGS(msgid,tag) VALUES(?,?) ");
                    insertTagStmt.setInt(1,id);
                    insertTagStmt.setString(2,tag);
                }
            }

        }
        catch (SQLException e){
            addError("Erreur d'accès à la base de données :");
            addError(e.toString());
        }

        return null ;
    }

    @Override
    protected void done() {
        super.done();
        JOptionPane.showMessageDialog(mainFrame,"Le tag "+tag+" a bien été ajouté aux messages sélectionnés");

    }
}
