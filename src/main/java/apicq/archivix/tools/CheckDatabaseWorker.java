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
                            "date TEXT," +
                            "author TEXT,"+
                            "subject TEXT,"+
                            "recip TEXT,"+
                            "body TEXT,"+
                            "attach INTEGER,"+
                            "mailrecip TEXT,"+
                            "cc TEXT,"+
                            "bcc TEXT,"+
                            "username TEXT,"+
                            "insertDate TEXT," +
                            "UNIQUE(date,author))");
            messageTableStmt.execute();


            PreparedStatement attachRefStmt = pStatement(
                    "CREATE TABLE IF NOT EXISTS attachref ("+
                            "md5sum TEXT PRIMARY KEY,"+
                            "name TEXT,"+
                            "size INTEGER)");
            attachRefStmt.execute();



            PreparedStatement attachTableStmt = pStatement(
                    "CREATE TABLE IF NOT EXISTS attach("+
                            "md5sum TEXT,"+
                            "msgid INTEGER,"+
                            "PRIMARY KEY(md5sum,msgid)," +
                            "FOREIGN KEY(md5sum) REFERENCES attachref(id)," +
                            "FOREIGN KEY(msgid) REFERENCES messages(id))");
            attachTableStmt.execute();

            log.info("balise");

            PreparedStatement tagsrefStmt = pStatement("CREATE TABLE IF NOT EXISTS tagsref("+
                    "name TEXT PRIMARY KEY)");
            tagsrefStmt.execute();


            PreparedStatement tagTableStmt = pStatement(
                    "CREATE TABLE IF NOT EXISTS tags("+
                            "msgid INTEGER,"+
                            "tag TEXT,"+
                            "FOREIGN KEY(msgid) REFERENCES messages(id),"+
                            "FOREIGN KEY(tag) REFERENCES tagsref(tag))");
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
        mainFrame.updateMainTitle();
    }
}
