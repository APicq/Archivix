package apicq.archivix.gui;

import apicq.archivix.tools.FindTagsNamesWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by pic on 2/2/14.
 */
public class SearchPanel extends JPanel {

    private final JTextField searchWordsTextField;

    private final JButton selectTagsButton;

    public JTextField searchWordsTextField(){ return searchWordsTextField ;}
    private final JButton searchWordsButton ;
    public JButton searchWordsButton() { return searchWordsButton; };
    private final MainFrame mainFrame ;

    public SearchPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame ;
        searchWordsButton = new JButton("Rechercher");
        searchWordsTextField = new JTextField("");
        //super();
        setLayout(new MigLayout("", "[][][grow,fill][]", ""));
        setBackground(Color.YELLOW);
        JLabel searchLabel = new JLabel("Rechercher dans ");
        add(searchLabel);
        JComboBox<String> fieldComboBox = new JComboBox<String>(
                new String[]{"corps","sujet","destinataires","auteur"});
        add(fieldComboBox);
        add(searchWordsTextField, "grow");
        add(searchWordsButton,"wrap");
        selectTagsButton = new JButton("Tags");
        add(selectTagsButton);

        selectTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<String> result = new FindTagsNamesWorker(SearchPanel.this.mainFrame).get();
                    // where I am
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
