package apicq.archivix.gui;

import javafx.scene.paint.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.Color;

/**
 * Created by pic on 2/13/14.
 */
public class MessageShowerDialog extends JDialog {

    public  MessageShowerDialog(MessageElement me){
        setModal(true);
        setMinimumSize(new Dimension(400,300));
        setLayout(new MigLayout());
        JTextField subjectTextField = new JTextField(me.subject());
        subjectTextField.setEditable(false);
        subjectTextField.setBackground(Color.LIGHT_GRAY);
        add(subjectTextField, "wrap");
        JTextArea bodyTextArea = new JTextArea(me.body());
        JScrollPane bodyScrollPane = new JScrollPane(bodyTextArea);
        add(bodyScrollPane,"wrap");
    }
}
