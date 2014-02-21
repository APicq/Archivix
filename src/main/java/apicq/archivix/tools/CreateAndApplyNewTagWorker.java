package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageTableModel;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by pic on 2/16/14.
 */
public class CreateAndApplyNewTagWorker extends SwingWorker<Void,Void> {

    public static final Logger log = Logger.getLogger("Archivix");
    private final MainFrame mainFrame;
    private final String newTag ;
    private  final ProgressMonitor progressMonitor ;

    public CreateAndApplyNewTagWorker(MainFrame mainFrame,String newTag){
        this.mainFrame = mainFrame ;
        this.newTag = newTag.trim() ;
        this.progressMonitor = new ProgressMonitor(
                mainFrame,
                "Insertion du nouveau tag",
                "Insertion du tag "+newTag+" pour chaque message",
                0,
                mainFrame.getMessageTable().getSelectedRowCount());
    }
    @Override
    protected Void doInBackground() throws Exception {

        try {
            PreparedStatement insertNewTagMsg = mainFrame.pConnection().prepareStatement(
                    "INSERT INTO tags(msgid,tag) VALUES(?,?)");
            for( int i=0 ; i < mainFrame.getMessageTable().getSelectedRows().length ; i++) {
                progressMonitor.setProgress(i);
                int selectionIndex = mainFrame.getMessageTable().getSelectedRows()[i];
                MessageTableModel mtm = (MessageTableModel) mainFrame.
                        getMessageTable().getModel();
                int messageId = mtm.get(selectionIndex).id();
                progressMonitor.setNote("message "+messageId);
                insertNewTagMsg.setInt(1,messageId);
                insertNewTagMsg.setString(2,newTag);
                insertNewTagMsg.execute();
            }
        } catch (SQLException e){
            log.warning(e.toString());
            JOptionPane.showMessageDialog(mainFrame, "ERREUR : l'insertion a échoué");
            return null ;
        }
        return null;
    }
}

