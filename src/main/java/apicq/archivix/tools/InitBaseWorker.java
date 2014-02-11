package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Initialize database, create necessary tables.
 */
public class InitBaseWorker extends SwingWorker<Boolean,String> {


    public static final Logger log = Logger.getLogger("Archivix");

    private final MainFrame mainFrame ;

    // True if everything OK, false otherwise
    private boolean operationOK = false ;
    private String error = "";

    /**
     * Constructor
     * @param mainFrame
     */
    public InitBaseWorker(MainFrame mainFrame) {
        this.mainFrame = mainFrame ;
    }



    @Override
    protected Boolean doInBackground() throws Exception {

        try {
            mainFrame.pConnection().close();
            mainFrame.pConnection().init();
            PreparedStatement pStmt = mainFrame.pConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS messages(" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "date TEXT," +
                            "author TEXT," +
                            "subject TEXT," +
                            "recip TEXT," +
                            "body TEXT," +
                            "attach INTEGER," +
                            "mailrecip TEXT," +
                            "cc TEXT," +
                            "bcc TEXT,"+
                            "username TEXT,"+
                            "insertDate TEXT)");
            pStmt.execute();
            pStmt = mainFrame.pConnection().prepareStatement("CREATE TABLE IF NOT EXISTS attach (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "msgid INTEGER," +
                    "name TEXT," +
                    "size INTEGER," +
                    "md5sum TEXT)");
            pStmt.execute();
            // todo : create table tags
        } catch (SQLException e) {
            log.warning(e.toString());
        }
        operationOK=true ;
        return true ;
    } // doInBackground

    /**
     * Show  dialog and eventually errors
     */
    @Override
    protected void done() {
        if(!operationOK){
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Erreur SQL : db non initialisée !!", "ERREUR", JOptionPane.ERROR_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Base de données "+mainFrame.databaseFile()+" initialisée",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}