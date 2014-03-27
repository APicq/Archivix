package apicq.archivix.gui.table;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.TextMessage;
import org.apache.poi.hslf.model.Table;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * class aimed to replace MessageTable
 * todo : prevent moving columns
 * store size when refresh
 * implements cell renderer
 */
public class MailTable extends JTable {

    public static final Logger log = Logger.getLogger("Archivix");

    // A list of columns, used for column visiblity management.
    private final ArrayList<TableColumn> columnIdents ;


    // mainFrame frame, used for event management.
    private final MainFrame mainFrame ;




    /**
     * Constructor
     * @param mainFrame
     */
    public MailTable(MainFrame mainFrame){

        this.mainFrame = mainFrame ;
        setModel(new MailTableModel());
        getTableHeader().setReorderingAllowed(false);

        // store all columns :
        columnIdents = new ArrayList<TableColumn>();
        Enumeration<TableColumn> col = getColumnModel().getColumns();
        while(col.hasMoreElements()) columnIdents.add(col.nextElement());

        //test : OK
        //getColumn(MailTableModel.ATTACHCOL_NAME).setPreferredWidth(500);
    }

    /**
     * Remove every columns, then show up them all.
     * To hide column : first call resetColumns, then call removeColumn(ident) whith ident :
     * a string in IDCOL_NAME,DATECOL_NAME,etc..., then doLayout
     */
    public void resetColumns(){
        for(TableColumn tc : columnIdents) removeColumn(tc);
        for(TableColumn tc : columnIdents) addColumn(tc);
    }

    public boolean isVisibleColumn(String columnIdent){
        try{
            getColumn(columnIdent);
            return  true ;
        }
        catch (IllegalArgumentException iae){
            log.info(columnIdent+" -- " + iae.toString());
            return false ;
        }
    }

    /**
     * Delete all messages from the array in tableModel :
     */
    public void clearMessages(){
        MailTableModel mtm = (MailTableModel) getModel();
        mtm.getMessages().clear();
    }

    public void addMessage(TextMessage tm){
        MailTableModel mtm = (MailTableModel) getModel() ;
        mtm.getMessages().add(tm);
    }


}
