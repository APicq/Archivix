package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pic on 2/2/14.
 */
public class MessageJList extends JList<MessageListModelElement> {

    private MessageListModel messageListModel ;

    public MessageJList() {
        messageListModel = new MessageListModel();
        setModel(messageListModel);
        setCellRenderer(new MessageJListRenderer());
        setLayoutOrientation(JList.VERTICAL);

        debug();
    }

    private void debug(){
    messageListModel.add(new MessageListModelElement(
            "2013-28-10","toto","tata"));
    }
}
