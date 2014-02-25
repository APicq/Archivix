package apicq.archivix.gui;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Table containing messages, plus events management
 */
public class MessageTable extends JXTable {

    public static final Logger log = Logger.getLogger("Archivix");

    private final JPopupMenu popupMenu ;

    public MessageTable(final MainFrame mainFrame) {

        getTableHeader().setReorderingAllowed(false);

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
                    MessageTableModel mtm = (MessageTableModel) getModel();
                    ShowMessageDialog msd = new ShowMessageDialog(mainFrame,mtm.get(rowIndex));
                    msd.setLocationRelativeTo(null);
                    msd.setVisible(true);
                }
            }

        });
    }
}

