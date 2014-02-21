package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import java.sql.PreparedStatement;

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
        PreparedStatement ps = Pst
    }
}
