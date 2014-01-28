package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pic on 1/28/14.
 */
public class ConfigPane extends JPanel {

    public ConfigPane() {

        super();
        BoxLayout verticalLayout = new BoxLayout(this,BoxLayout.PAGE_AXIS);
        setLayout(verticalLayout);


        // Line database config
        JLabel label1 = new JLabel("Database :");
        JTextField databaseTextField = new JTextField();
        databaseTextField.setColumns(25);
        JButton changeDatabaseButton = new JButton("Change Database");
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1,BoxLayout.LINE_AXIS));
        panel1.add(label1);
        panel1.add(databaseTextField);
        panel1.add(changeDatabaseButton);
        panel1.setMaximumSize(
                new Dimension(Integer.MAX_VALUE,(int) panel1.getPreferredSize().getHeight()));

        // Line attachment :
        JLabel attachPathLabel = new JLabel("Message table :");
        JTextField attachPathTextField = new JTextField();
        attachPathTextField.setEditable(false);
        attachPathTextField.setColumns(25);
        JButton attachPathButton = new JButton("change directory");
        JPanel attachPathPanel = new JPanel();
        attachPathPanel.setLayout(new BoxLayout(attachPathPanel,BoxLayout.LINE_AXIS));
        attachPathPanel.add(attachPathLabel);
        attachPathPanel.add(attachPathTextField);
        attachPathPanel.add(attachPathButton);
        attachPathPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                attachPathPanel.getPreferredSize().height));

        // Add lines vertically : 
        add(panel1);
        add(attachPathPanel);
    }
}
