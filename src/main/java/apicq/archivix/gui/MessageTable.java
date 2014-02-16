package apicq.archivix.gui;

import javax.swing.*;
import java.awt.event.*;
import java.util.logging.Logger;

/**
 * Table containing messages, plus events management
 */
public class MessageTable extends JTable {

    public static final Logger log = Logger.getLogger("Archivix");

    private MessageTableModel messageTableModel ;

    private JPopupMenu popupMenu ;
   // private JMenuItem addNewTagItem ;
   // private JMenuItem modifyTagsItem ;
   // private JMenuItem deleteMessagesItem ;

    public MessageTable(MainFrame mainFrame) {

        messageTableModel = new MessageTableModel();
        setModel(messageTableModel);

        // Menu definition and actions :
        popupMenu = new JPopupMenu("Choisissez une action sur le(s) message(s)");
        JMenuItem addNewTagItem = new JMenuItem("Ajouter un nouveau tag");
        JMenuItem modifyTagsItem = new JMenuItem("Modifier les tags");
        JMenuItem deleteMessagesItem = new JMenuItem("Effacer le(s) message(s)");
        popupMenu.add(addNewTagItem);
        popupMenu.add(modifyTagsItem);
        popupMenu.add(deleteMessagesItem);

        // Actions
        addNewTagItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAddNewTag();
            }
        });

        // Right-click : menu appears
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON3) {// instanceof MessageTable){
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void actionAddNewTag() {
        // todo
    }
}
