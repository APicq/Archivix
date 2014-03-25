package apicq.archivix.gui.table;

import org.apache.poi.hslf.model.Table;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * class aimed to replace MessageTable
 * todo : prevent moving columns
 * store size when refresh
 * implements cell renderer
 */
public class MailTable extends JTable {

    private final ArrayList<TableColumn> columnIdents ;

    public MailTable(){

        setModel(new MailTableModel());

        // store all columns :
        columnIdents = new ArrayList<TableColumn>();
        Enumeration<TableColumn> col = getColumnModel().getColumns();
        while(col.hasMoreElements()) columnIdents.add(col.nextElement());

        //test :
        getColumn(MailTableModel.ATTACHCOL_NAME).setPreferredWidth(200);
    }

    /**
     * Remove every columns, then show up them all.
     * To hide column : first call resetColumns, then call removeColumn(ident) whith ident :
     * a string in IDCOL_NAME,DATECOL_NAME,etc..., then doLayout
     */
    private void resetColumns(){
        for(TableColumn tc : columnIdents) removeColumn(tc);
        for(TableColumn tc : columnIdents) addColumn(tc);
    }
}
