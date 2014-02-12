package apicq.archivix.gui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * Created by pic on 2/10/14.
 */
public class MessageTable extends JTable {

    public static final Logger log = Logger.getLogger("Archivix");

    private MessageTableModel messageTableModel ;

    public MessageTable() {
        messageTableModel = new MessageTableModel();
        setModel(messageTableModel);


        // todo : right-clikc menu
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == e.BUTTON3){// instanceof MessageTable){
                    JPopupMenu popup = new JPopupMenu("qdsmfkjq");
                    popup.add("dsqfqsdf");
                    popup.add("aAAAAAA");
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
}
