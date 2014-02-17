package apicq.archivix.gui;

import apicq.archivix.tools.FindTagsNamesWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Created by pic on 2/2/14.
 */
public class SearchPanel extends JPanel {

    public static final Logger log = Logger.getLogger("Archivix");
    private final JTextField searchWordsTextField;
    private final JButton selectTagsButton;
    private final JButton searchWordsButton ;
    private final MainFrame mainFrame ;
    private JPanel selectedTagsPanel ;

    /**
     * Getter
     * @return
     */
    public JTextField searchWordsTextField(){
        return searchWordsTextField ;
    }

    /**
     * Getter
     * @return
     */
    public JButton searchWordsButton() {
        return searchWordsButton;
    };

    /**
     * Constructor
     * @param mainFrame
     */
    public SearchPanel(final MainFrame mainFrame){

        this.mainFrame = mainFrame ;
        setLayout(new MigLayout("", "[][][grow,fill][]", ""));
        setBackground(Color.LIGHT_GRAY);

        // Line 1
        JLabel searchLabel = new JLabel("Rechercher dans ");
        add(searchLabel,"");
        searchWordsTextField = new JTextField("");
        JComboBox<String> fieldComboBox = new JComboBox<String>(
                new String[]{"corps","sujet","destinataires","auteur"});
        add(fieldComboBox);
        add(searchWordsTextField, "grow");
        searchWordsButton = new JButton("Rechercher");
        add(searchWordsButton,"wrap");

        // Line 2
        // todo : actions from mainFrame here.
        // Button to choose tags :
        selectTagsButton = new JButton("Tags");
        add(selectTagsButton,"grow");
        // panel with all the tags :
        selectedTagsPanel = new JPanel();
        selectedTagsPanel.setLayout(new MigLayout());
        add(new JScrollPane(selectedTagsPanel),"grow,span");
        selectTagsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectTagsDialog std = new SelectTagsDialog(SearchPanel.this.mainFrame);
                std.setVisible(true);
                if( std.returnValue()==std.OK ){
                    selectedTagsPanel.removeAll();
                    for(Component c : std.selectedTagsPanel().getComponents()){
                        selectedTagsPanel.add(c);
                    }
                    selectedTagsPanel.revalidate();
                    selectedTagsPanel.repaint();
                }
            }
        });

        // Line 3
        final JCheckBox untaggedMessagesCheckBox = new JCheckBox("Afficher les messages non taggés");
        untaggedMessagesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(untaggedMessagesCheckBox.isSelected()){
                    selectTagsButton.setEnabled(false);
                    selectedTagsPanel.setEnabled(false);
                }
                else {
                    selectTagsButton.setEnabled(true);
                    selectedTagsPanel.setEnabled(true);
                }
            }
        });
        add(untaggedMessagesCheckBox,"span 2");
        add(new JLabel("Trier par :"),"");
        JComboBox<String> sortComboBox = new JComboBox<String>(
                new String[]{"date","sujet","destinataires","auteur"});
        add(sortComboBox,"");


        // Line 4
        JButton previousPageButton = new JButton("Page précédente");
        add(previousPageButton,"");
        JButton nextPageButton = new JButton("Page suivante");
        add(nextPageButton,"");
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(1000);
        formatter.setCommitsOnValidEdit(true);
        JFormattedTextField maxResultNumberField = new JFormattedTextField(formatter);
        maxResultNumberField.setColumns(4);
        maxResultNumberField.setText("100");
        add(maxResultNumberField, "");
        add(new JLabel("résultats maximum par page"), "");

    }
}
