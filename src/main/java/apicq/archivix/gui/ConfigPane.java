package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pic on 1/28/14.
 * Panel whith all configuration options.
 */
public class ConfigPane extends JPanel {

    /**
     * Simple line Component with label, textfield and button
     */
    class ConfigLine extends JPanel {

        private JButton button ;
        private JTextField textField;

        public JButton button() {
            return button;
        }

        public JTextField textField() {
            return textField;
        }

        /**
         * Constructor
         * @param buttonString
         * @param labelString
         */
        public ConfigLine(String buttonString,String labelString) {
            super();
            button = new JButton(buttonString);
            textField = new JTextField();
            textField.setColumns(20);
            textField.setEditable(false);
            JLabel label = new JLabel(labelString);
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            add(label);
            add(textField);
            add(button);
            setMaximumSize(
                    new Dimension(Integer.MAX_VALUE,getPreferredSize().height));
        }

    } //class

    private final ConfigLine databaseLine ;
    private final ConfigLine attachLine;
    private final ConfigLine messagePoolLine ;


    /**
     * Constructor
     */
    public ConfigPane() {
        super();
        databaseLine = new ConfigLine("change database","database : ");
        attachLine = new ConfigLine("Change directory","Attachment directory:");
        messagePoolLine = new ConfigLine("Change directory","Message pool : ");
        BoxLayout verticalLayout = new BoxLayout(this,BoxLayout.PAGE_AXIS);
        setLayout(verticalLayout);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(databaseLine);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(attachLine);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(messagePoolLine);
    }
}
