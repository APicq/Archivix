package apicq.archivix.tools;

import java.util.logging.Logger;

/**
 * Created by pic on 1/29/14.
 * Makes easier database updates and requests.
 */
public class DatabaseHandler {


    private String databaseFullPathName ;
    private static Logger log = Logger.getLogger("Archivix");
    private static boolean init = false ;

    public DatabaseHandler(){

    }

    /**
     * Load driver
     * @return
     */
    private static boolean init(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            log.warning("Class org.sqlite.JDBC not foud.");
            return false ;
        }
        init = true;
        return true;
    }

    // todo    Statement -> update
    public boolean update(){
        return false ;
    }


    //todo     Statement -> resultSet
    public void request(){

    }


}   //class
