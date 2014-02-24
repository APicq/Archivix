package apicq.archivix.gui;

import apicq.archivix.tools.AddNewTagWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Table containing messages, plus events management
 */
public class MessageTable extends JTable {

    public static final Logger log = Logger.getLogger("Archivix");
    private final MainFrame mainFrame ;
    private MessageTableModel messageTableModel ;

    private JPopupMenu popupMenu ;

    public MessageTable(MainFrame mainFrame) {

        this.mainFrame = mainFrame ;
        messageTableModel = new MessageTableModel();
        setModel(messageTableModel);

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

        popupMenu = new JPopupMenu("Choisissez une action sur le(s) message(s)");
        popupMenu.add(addNewTagItem);
        popupMenu.add(modifyTagsItem);
        popupMenu.add(deleteMessagesItem);



        // Right-click : menu appears
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON3 && getSelectedRow() != -1) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
                if(e.getClickCount()==2){
                    int rowIndex = getSelectedRow();
                    log.info("rowIndex : "+rowIndex);
                    MessageTableModel mtm = (MessageTableModel) getModel();
                    MessageShowerDialog msd = new MessageShowerDialog(mtm.get(rowIndex));
                    msd.setVisible(true);
                    msd.setLocationRelativeTo(null);

                }
            }

        });
    }
}

