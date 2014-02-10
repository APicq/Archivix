package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pic on 2/2/14.
 */
public class MessageJList extends JList<String> {

    class ListedMessage {

        private String date ;
        public String date() { return date ;}
        private String author ;
        public String author() { return author;}
        private String subject ;
        public String subject() { return subject;}

        // private String body ;

        public ListedMessage(
                String date,
                String author,
                String subject){
            this.date = date ;
            this.author = author ;
            this.subject = subject ;
        }


    } // ListedMessage

    class MessageJListRenderer extends JPanel implements ListCellRenderer<ListedMessage>{

        public MessageJListRenderer() {
            //setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ListedMessage> list, ListedMessage value, int index, boolean isSelected, boolean cellHasFocus) {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            JLabel dateLabel= new JLabel(value.date());
            add(dateLabel);
            JLabel authorLabel = new JLabel(value.author());
            add(authorLabel);
            return this ;
        }
    }

    public MessageJList(String[] listData) {
        super(listData);

    }
}
