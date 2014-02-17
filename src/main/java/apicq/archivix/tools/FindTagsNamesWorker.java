package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import sun.applet.Main;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * e
 */
public class FindTagsNamesWorker extends SwingWorker<ArrayList<String>,Void> {

    public static final Logger log = Logger.getLogger("Archivix");

    private final MainFrame mainFrame ;
    private final ProgressMonitor progressMonitor ;

    public FindTagsNamesWorker(MainFrame mainFrame) {
        this.mainFrame = mainFrame ;
        progressMonitor = new ProgressMonitor(mainFrame,"Recherche des tags","Recherche des tags en cours...",0,1);
    }

    @Override
    protected ArrayList<String> doInBackground() throws Exception {
        ArrayList<String> tagList = new ArrayList<String>();
        PreparedStatement findTagNamesStmt =
                mainFrame.pConnection().
                        prepareStatement("SELECT DISTINCT tag from tags order by tag");
        ResultSet rs = findTagNamesStmt.executeQuery();
        while(rs.next()) tagList.add(rs.getString(1));
        return tagList;
    }
}
