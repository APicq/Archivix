package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pic on 2/2/14.
 */
public class SearchPanel extends JPanel {

    private final JTextField searchWordsTextField;
    public JTextField searchWordsTextField(){ return searchWordsTextField ;}
    private final JButton searchWordsButton ;
    public JButton searchWordsButton() { return searchWordsButton; };

    public SearchPanel(){
        searchWordsButton = new JButton("Rechercher");
        searchWordsTextField = new JTextField("");
        //super();
        setLayout(new MigLayout("", "[][grow,fill][]", ""));
        setBackground(Color.YELLOW);
        JLabel searchLabel = new JLabel("Mots à rechercher :");
        add(searchLabel);
        add(searchWordsTextField, "grow");
        add(searchWordsButton,"wrap");
        JCheckBox bodyCheckBox = new JCheckBox("corps");
        add(bodyCheckBox);
        JCheckBox subjectCheckBox = new JCheckBox("destinataire");
        add(subjectCheckBox);

    }
}
