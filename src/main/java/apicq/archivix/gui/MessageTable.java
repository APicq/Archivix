package apicq.archivix.gui;

import javax.swing.*;

/**
 * Created by pic on 2/10/14.
 */
public class MessageTable extends JTable {

    private MessageTableModel messageTableModel ;

    public MessageTable() {
        messageTableModel = new MessageTableModel();
        setModel(messageTableModel);
    }
}
