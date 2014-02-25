package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.SelectUserDialog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by pic on 2/20/14.
 */
public class FindUserWorker extends SpecializedWorker {


    private final Vector<String> nameVector ;
    /**
     * Constructor
     *
     * @param mainFrame mainFrame
     */
    public FindUserWorker(MainFrame mainFrame) {
        super(mainFrame, "Recherche des noms d'utilisateurs");
        nameVector = new Vector<String>();
        setIndeterminate(true);
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            PreparedStatement ps = pStatement("SELECT DISTINCT username FROM messages ORDER BY username");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                nameVector.add(rs.getString(1));
            }
            //mainFrame.getSearchPanel().getU

        } catch (SQLException e){
            addError("Erreur SQL :");
            addError(e.toString());
        }
        return null ;
    }

    @Override
    protected void done() {
        super.done();
        SelectUserDialog sud = new SelectUserDialog(mainFrame,nameVector);
    }
}
