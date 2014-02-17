package apicq.archivix.gui;

import apicq.archivix.tools.FindTagsNamesWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public JTextField searchWordsTextField(){
        return searchWordsTextField ;
    }

    public JButton searchWordsButton() {
        return searchWordsButton;
    };

    /**
     * Constructor
     * @param mainFrame
     */
    public SearchPanel(final MainFrame mainFrame){
        this.mainFrame = mainFrame ;
        searchWordsButton = new JButton("Rechercher");
        searchWordsTextField = new JTextField("");
        setLayout(new MigLayout("", "[][][grow,fill][]", ""));
        setBackground(Color.LIGHT_GRAY);
        JLabel searchLabel = new JLabel("Rechercher dans ");
        add(searchLabel);
        JComboBox<String> fieldComboBox = new JComboBox<String>(
                new String[]{"corps","sujet","destinataires","auteur"});
        add(fieldComboBox);
        add(searchWordsTextField, "grow");
        add(searchWordsButton,"wrap");

        // Line 2
        // todo : actions from mainFrame here.
        // Button to choose tags :
        selectTagsButton = new JButton("Tags");
        add(selectTagsButton);
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


    }
}
