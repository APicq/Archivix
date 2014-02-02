package apicq.archivix.tools;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by pic on 1/29/14.
 * Makes easier database updates and requests.
 */
public class DatabaseHandler {

    private static Logger log = Logger.getLogger("Archivix");

    // database Path
    private static String databaseFullPathName ;

    // Database connection
    private static Connection connection ;


    // bool to launch init method only once.
    private static boolean initIsDone = false ;

    // initOK == true if database initialised.
    private static boolean initOK = false ;



    /**
     * getter
     * @return
     */
    public String getDatabaseFullPathName() {
        return databaseFullPathName;
    }

    /**
     * Setter
     * @param databaseFullPathName
     */
    public static void setDatabaseFullPathName(String databaseFullPathName) {
        databaseFullPathName = databaseFullPathName;
    }
    /**
     * Load driver
     * @return
     */
    public static boolean init(){

        if( initIsDone == false ){
            initIsDone = true ;
            try {
                Class.forName("org.sqlite.JDBC");
                initOK = true ;
                log.info("org.sqlite.JDBC loaded.");
            } catch (ClassNotFoundException e) {
                log.warning("Class org.sqlite.JDBC not foud.");
                return false ;
            }
        }
        return true ;
    }


    public static PreparedStatement getPStatement(String sql) {

        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            log.warning(e.toString());
            return null;
        }
    }

    public static boolean open() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:"+databaseFullPathName);
            return true ;
        } catch (SQLException e) {
            log.warning(e.toString());
            return false ;
        }
    }

    /**
     * Close database connection
     * @return
     */
    public static boolean close(){
        try {
            connection.close();
            return true ;
        } catch (SQLException e) {
            log.warning(e.toString());
            return false ;
        }
    }


    /**
     * check database schema
     * @return
     */
    public static boolean check(){
        if(init()==false) return false ;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXIST messages(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "from text,"+
                    "date TEXT,"+
                    "to TEXT,"+
                    "subject TEXT,"+
                    "pj INTEGER)");
            stmt.executeUpdate("CREATE TABLE IF NOT EXIST attach(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "msgid INTEGER,"+
                    "name TEXT,"+
                    "md5 TEXT,"+
                    "size INTEGER)");
        } catch (SQLException e) {
            log.warning(e.toString());
            return false ;
        }
        if(close()==false) return false ;
        return true ;
    }


}   //class
