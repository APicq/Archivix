package apicq.archivix.tools;

import java.sql.PreparedStatement;
import java.util.logging.Logger;

/**
 * Created by pic on 1/29/14.
 * Makes easier database updates and requests.
 */
public class DatabaseHandler {


    private String databaseFullPathName ;
    private static Logger log = Logger.getLogger("Archivix");

    // bool to launch init method only once.
    private static boolean initIsDone = false ;

    // initOK == true if database initialised.
    private static boolean initOK = false ;


    public DatabaseHandler(){

    }

    /**
     * Load driver
     * @return
     */
    private static boolean init(){

        if( initIsDone == false ){
            initIsDone = true ;
            try {
                Class.forName("org.sqlite.JDBC");
                initOK = true ;
            } catch (ClassNotFoundException e) {
                log.warning("Class org.sqlite.JDBC not foud.");
                return false ;
            }
        }
        return true ;
    }

    // todo
    public PreparedStatement getPreparedStatement(String SQL) {
        return null ;
    }


    //todo     Statement -> resultSet
    public void request(){

    }


}   //class
