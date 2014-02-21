package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pic on 2/20/14.
 */
public class FindUserWorker extends SpecializedWorker {



    /**
     * Constructor
     *
     * @param mainFrame
     * @param subject
     */
    public FindUserWorker(MainFrame mainFrame, String subject) {
        super(mainFrame, "Recherche des noms d'utilisateurs");
        getProgressDialog().getProgressBar().setIndeterminate(true);
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            PreparedStatement ps = pStatement("SELECT DISTINCT user FROM messages ORDER BY user");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                
            }
        } catch (SQLException e){
            addError("Erreur SQL :");
            addError(e.toString());
        }
        return null ;
    }
}
