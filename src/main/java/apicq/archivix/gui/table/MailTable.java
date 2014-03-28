package apicq.archivix.gui.table;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.ShowMessageDialog;
import apicq.archivix.gui.TextMessage;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    // The pop up menu :
    private final JPopupMenu popupMenu ;




    /**
     * Constructor
     * @param mainFrame
     */
    public MailTable(final MainFrame mainFrame){

        this.mainFrame = mainFrame ;
        setModel(new MailTableModel());

        // prevents column moves :
        getTableHeader().setReorderingAllowed(false);

        // store all columns :
        columnIdents = new ArrayList<TableColumn>();
        Enumeration<TableColumn> col = getColumnModel().getColumns();
        while(col.hasMoreElements()) columnIdents.add(col.nextElement());

        // Menu definition and actions :
        JMenuItem addNewTagItem = new JMenuItem("Ajouter un nouveau tag");
        addNewTagItem.setActionCommand("addNewTagAction");
        addNewTagItem.addActionListener(mainFrame);


        JMenuItem modifyTagsItem = new JMenuItem("Modifier les tags");
        modifyTagsItem.setActionCommand("modifyTagsAction");
        modifyTagsItem.addActionListener(mainFrame);

        JMenuItem deleteMessagesItem = new JMenuItem("Effacer le(s) message(s)");
        deleteMessagesItem.setActionCommand("deleteMessagesAction");
        deleteMessagesItem.addActionListener(mainFrame);

        JMenuItem saveMessagesItem = new JMenuItem("Sauvegarder le(s) message(s)");
        saveMessagesItem.setActionCommand("createReportAction");
        saveMessagesItem.addActionListener(mainFrame);

        popupMenu = new JPopupMenu("Choisissez une action sur le(s) message(s)");
        popupMenu.add(addNewTagItem);
        popupMenu.add(modifyTagsItem);
        popupMenu.add(deleteMessagesItem);
        popupMenu.add(saveMessagesItem);

        // Right-click : menu appears
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON3 && getSelectedRow() != -1) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
                if(e.getClickCount()==2){
                    int rowIndex = getSelectedRow();
                    MailTableModel mtm = (MailTableModel) getModel() ;
                    ShowMessageDialog smd = new ShowMessageDialog(
                            mainFrame,
                            mtm.getMessages().get(rowIndex));
                    smd.setLocationRelativeTo(null);
                    smd.setVisible(true);
                }
            }

        });
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

    /**
     * add a textMessage a the end of the textMessage array;
     * @param tm
     */
    public void addMessage(TextMessage tm){
        MailTableModel mtm = (MailTableModel) getModel() ;
        mtm.getMessages().add(tm);
    }

    /**
     * returns the TextMessage at index i
     * @param index
     * @return
     */
    public TextMessage get(int index){
        MailTableModel mtm = (MailTableModel) getModel() ;
        return mtm.getMessages().get(index);
    }
}
