package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.SelectTagsDialog;
import apicq.archivix.gui.SelectUserDialog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by pic on 2/23/14.
 */
public class FindTagsWorker extends SpecializedWorker {

    private final ArrayList<String> tagList = new ArrayList<String>();
    // if true : mode is for search panel
    private final boolean searchPanelMode ;

    /**
     * Constructor
     *
     * @param mainFrame
     */
    public FindTagsWorker(MainFrame mainFrame,boolean searchPanelMode) {
        super(mainFrame, "Recherche des tags");
        this.searchPanelMode = searchPanelMode ;
    }


    @Override
    protected Void doInBackground() throws Exception {

        try {
            PreparedStatement findTagNamesStmt = pStatement("SELECT DISTINCT tag from tags order by tag");
            ResultSet rs = findTagNamesStmt.executeQuery();
            while(rs.next()) tagList.add(rs.getString(1));
        } catch (SQLException e){
            addError("Erreur lors de la recherche des tags :");
            addError(e.toString());
        }
        return null ;
    }

    @Override
    protected void done() {
        super.done();
        SelectTagsDialog std = new SelectTagsDialog(mainFrame,tagList,searchPanelMode);
        std.setVisible(true);
    }
}
