package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by pic on 2/24/14.
 */
public class CheckDatabaseWorker extends SpecializedWorker {



    /**
     * Constructor
     *
     * @param mainFrame
     */
    public CheckDatabaseWorker(MainFrame mainFrame) {
        super(mainFrame, "Vérification de la base de données");
        setIndeterminate(true);
    }



    @Override
    protected Void doInBackground() throws Exception {
        try {
            PreparedStatement messageTableStmt = pStatement(
                    "CREATE TABLE IF NOT EXISTS messages("+
                            "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                            "date TEXT,author TEXT,"+
                            "subject TEXT,"+
                            "recip TEXT,"+
                            "body TEXT,"+
                            "attach INTEGER,"+
                            "mailrecip TEXT,"+
                            "cc TEXT,"+
                            "bcc TEXT,"+
                            "username TEXT,"+
                            "insertDate TEXT)");
            messageTableStmt.execute();

            PreparedStatement attachTableStmt = pStatement(
            "CREATE TABLE IF NO EXISTS attach ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "msgid INTEGER,"+
                    "name TEXT,"+
                    "size INTEGER,"+
                    "md5sum TEXT,"+
                    "FOREIGN KEY(msgid) REFERENCES messages(id))");
            attachTableStmt.execute();

            PreparedStatement tagTableStmt = pStatement(
                    "CREATE TABLE IF NOT EXISTS tags("+
                            "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                            "msgid INTEGER,"+
                            "tag TEXT,"+
                            "FOREIGN KEY(msgid) REFERENCES messages(id))");
            tagTableStmt.execute();


        }
        catch (SQLException e){
            addError("Erreur lors de la vérification/création des tables de la base de données");
            addError(e.getMessage());
        }

        return null ;
    }



    @Override
    protected void done() {
        super.done();
    }
}
