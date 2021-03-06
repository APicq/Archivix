package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by pic on 2/2/14.
 */
public class SearchPanel extends JPanel {

    public static final Logger log = Logger.getLogger("Archivix");
    private final JTextField searchWordsTextField;
    private final JComboBox<String> fieldComboBox;
    private final JComboBox<String> sortComboBox;
    private final MainFrame mainFrame ;
    private final JLabel pageLabel;
    private final JLabel userLabel;

    // if true, only untagged messages are searched
    private boolean onlyUntagged = false ;

    // username for search :
    private String userName = "" ;

    // If true, enable search by users
    private boolean perUserSelection = false ;



    // Page number, used by next/previous
    private static int pageNumber=1;

    // Number of results per page :
    private JTextField maxResultNumberField ;



    // -------------------
    // Getters and setters
    // -------------------

    public static int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        if(pageNumber>=1){
            this.pageNumber = pageNumber;
            pageLabel.setText("page : "+pageNumber);
        }
    }

    public void setuserLabel(String s){
        userLabel.setText(s);
    }

    public JComboBox<String> getSortComboBox() {
        return sortComboBox;
    }

    public JTextField getMaxResultNumberField() {
        return maxResultNumberField;
    }

    public void setMaxResult(int max){
        maxResultNumberField.setText(""+max);
    }

    public JPanel getSelectedTagsPanel() {
        return selectedTagsPanel;
    }

    private JPanel selectedTagsPanel ;

    public JComboBox<String> getFieldComboBox() {
        return fieldComboBox;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPerUserSelection(boolean perUserSelection) {
        this.perUserSelection = perUserSelection;
    }

    public boolean isPerUserSelection() {
        return perUserSelection;
    }

    public String getUserName() {
        return userName;
    }

    public void setOnlyUntagged(boolean onlyUntagged) {
        this.onlyUntagged = onlyUntagged;
    }

    public boolean isOnlyUntagged() {
        return onlyUntagged;
    }

    public JTextField searchWordsTextField(){
        return searchWordsTextField ;
    }


    /**
     * Constructor
     * @param mainFrame
     */
    public SearchPanel(final MainFrame mainFrame){

        this.mainFrame = mainFrame ;
        setLayout(new MigLayout("", "[][][grow][]", ""));
        setBackground(Color.LIGHT_GRAY);

        // Line 1
        JLabel searchLabel = new JLabel("Rechercher dans ");
        add(searchLabel, "");
        searchWordsTextField = new JTextField("");
        fieldComboBox = new JComboBox<String>(
                new String[]{"corps","sujet","destinataires","auteur"});
        add(fieldComboBox);
        add(searchWordsTextField,"grow");

        // Button to find messages
        JButton searchWordsButton = new JButton("Rechercher");
        searchWordsButton.setActionCommand("findMessagesAction");
        searchWordsButton.addActionListener(mainFrame);
        add(searchWordsButton, "");

        // user selection :
        JButton userSelectionButton = new JButton("Utilisateur");
        userSelectionButton.setActionCommand("findUserAction");
        userSelectionButton.addActionListener(mainFrame);
        add(userSelectionButton, "");

        userLabel = new JLabel("Tous");
        add(userLabel,"wrap");

        // Line 2
        // Button to choose tags :
        JButton selectTagsButton = new JButton("Tags");
        selectTagsButton.setActionCommand("selectTagsAction");
        selectTagsButton.addActionListener(mainFrame);
        add(selectTagsButton, "shrink");

        // panel with all the tags :
        selectedTagsPanel = new JPanel();
        selectedTagsPanel.setLayout(new MigLayout());
        selectTagsButton.setBorder(new EmptyBorder(10,10,10,10));
        add(new JScrollPane(selectedTagsPanel), "grow,span");


        // Panel with sort options :
        JPanel sortingPanel = new JPanel(new MigLayout());
        sortingPanel.add(new JLabel("Trier par :"), "");
        sortComboBox = new JComboBox<String>(
                new String[]{"date","sujet","destinataires","auteur","date insertion","id"});
        sortingPanel.add(sortComboBox, "");

        JButton previousPageButton = new JButton("Page précédente");
        previousPageButton.setActionCommand("previousAction");
        previousPageButton.addActionListener(mainFrame);
        sortingPanel.add(previousPageButton, "");

        pageLabel = new JLabel("page 1");
        sortingPanel.add(pageLabel);

        JButton nextPageButton = new JButton("Page suivante");
        nextPageButton.setActionCommand("nextAction");
        nextPageButton.addActionListener(mainFrame);
        sortingPanel.add(nextPageButton, "");

        maxResultNumberField = new JTextField();
        maxResultNumberField.setColumns(6);
        maxResultNumberField.setText("999999");
        sortingPanel.add(maxResultNumberField, "");
        sortingPanel.add(new JLabel("résultats maximum par page"), "");
        add(sortingPanel,"span,grow");



    }


}
