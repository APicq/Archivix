package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pic on 2/2/14.
 */
public class SearchPanel extends JPanel {

    private JButton searchWordsButton ;
    public JButton searchWordsButton() { return searchWordsButton; };

    public SearchPanel(){
        super();
        setLayout(new MigLayout("", "[][grow,fill][]", ""));
        setBackground(Color.YELLOW);
        JLabel searchLabel = new JLabel("Mots à rechercher :");
        add(searchLabel);
        JTextField searchWordsTextField = new JTextField("hello");
        add(searchWordsTextField, "grow");
        JButton searchWordsButton = new JButton("Rechercher");
        add(searchWordsButton,"wrap");
        JCheckBox bodyCheckBox = new JCheckBox("corps");
        add(bodyCheckBox);
        JCheckBox subjectCheckBox = new JCheckBox("destinataire");
        add(subjectCheckBox);

    }
}
