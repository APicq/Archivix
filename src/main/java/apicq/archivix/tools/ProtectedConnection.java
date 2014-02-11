package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import sun.applet.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * All connections to the SQLITE database must pass through this class
 */
public class ProtectedConnection {

    public static final Logger log = Logger.getLogger("Archivix");

    private Connection con ;
    private MainFrame mainFrame ;

    /**
     * Constructor
     * @param mainFrame
     */
    public ProtectedConnection(MainFrame mainFrame){
        this.mainFrame = mainFrame ;
    }

    public void init() throws SQLException {
        if(con==null || con.isClosed()){
            con=DriverManager.getConnection("jdbc:sqlite:" +
                    mainFrame.databaseFile());
            log.info("Connected to database "+mainFrame.databaseFile());

        }
    }

    public void close() throws SQLException {
        if(con!=null && !con.isClosed()){
            con.close();
        }
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if(con==null) {
            throw new SQLException("connection null");
        }
        if(con.isClosed()){
            throw new SQLException("connection is closed");
        }
        return  con.prepareStatement(sql);
    }
}
