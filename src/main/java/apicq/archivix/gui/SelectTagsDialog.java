package apicq.archivix.gui;

import apicq.archivix.tools.ApplyTagWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Open a dialog, user can select tags from database.
 * This dialog is used by two actions, so it has two modes:
 * searchPanelMode = true : used for tag selection in message research
 * searchPanelMode = false : used to apply tags to selected messages
 */
public class SelectTagsDialog extends JDialog {

    public static final Logger log = Logger.getLogger("Archivix");

    private final MainFrame mainFrame ;
    private final JList tagList ;
    private final TagListModel tagListModel ;
    private final JPanel selectedTagsPanel ;
    private final JCheckBox untaggedMessagesCheckBox ;

    /**
     * Getter for selected tags
     * @return
     */
    public JPanel selectedTagsPanel() {
        return selectedTagsPanel;
    }

    /**
     * listmodel for tagList
     */
    class TagListModel extends AbstractListModel<String>{

        private ArrayList<String> tagNames ;

        TagListModel(ArrayList<String> tagNames) {
            this.tagNames = tagNames;
        }

        @Override
        public int getSize() {
            return tagNames.size();
        }

        @Override
        public String getElementAt(int index) {
            return tagNames.get(index);
        }
    }


    /**
     * Listener for tagList // todo put into tagList
     */
    class MySelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {

            boolean tagIsAlreadyInPanel = false ;
            String clickedTag = (String) tagList.getSelectedValue();
            log.info("clicked tag : "+clickedTag);
            for(Component comp : selectedTagsPanel.getComponents()){
                JLabel selectedTagLabel = (JLabel)comp;
                if(selectedTagLabel.getText().equals(clickedTag)){
                    tagIsAlreadyInPanel = true ;
                    break ;
                }
            }
            if(!tagIsAlreadyInPanel) {
                JLabel newTagLabel = new JLabel(clickedTag);
                newTagLabel.setOpaque(true);
                newTagLabel.setBackground(Color.WHITE);
                newTagLabel.setBorder(new LineBorder(Color.black));
                selectedTagsPanel.add(newTagLabel);
            }
            SelectTagsDialog.this.validate();
            SelectTagsDialog.this.repaint();
        }
    }

    /**
     * Constructor
     * @param mainFrame
     */
    public SelectTagsDialog(final MainFrame mainFrame,
                            final ArrayList<String> tagStringList,
                            final boolean searchPanelMode) {
        setModal(true);
        setLayout(new MigLayout("","[][grow][]","[][grow][][]")); // layout,column,row
        this.mainFrame = mainFrame ;

        // Line 1
        add(new JLabel("tags disponibles :"), "wrap");

        tagListModel = new TagListModel(tagStringList);
        tagList = new JList<String>(tagListModel);
        tagList.addListSelectionListener(new MySelectionListener());
        tagList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tagListScrollPane = new JScrollPane(tagList);
        add(tagListScrollPane, "grow,span");

        // Line 3
        add(new JLabel("Tags sélectionnés :"),"");
        selectedTagsPanel = new JPanel();
        selectedTagsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        selectedTagsPanel.setLayout(new MigLayout());
        add(new JScrollPane(selectedTagsPanel), "grow,span 2");
        JButton delSelectionButton = new JButton("Effacer la sélection");
        delSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTagsPanel.removeAll();
                selectedTagsPanel.revalidate();
                selectedTagsPanel.repaint();
            }
        });
        add(delSelectionButton, "wrap");

        // Line 4  : only tagged messages
        untaggedMessagesCheckBox = new JCheckBox("Afficher seulement les messages non taggés");
        untaggedMessagesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagList.setEnabled(untaggedMessagesCheckBox.isEnabled());
            }
        });
        if(searchPanelMode){
            add(untaggedMessagesCheckBox,"span 2");
        }

        // Line 5
        // OK Button
        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(searchPanelMode){
                    if(untaggedMessagesCheckBox.isSelected()){
                        mainFrame.getSearchPanel().setOnlyUntagged(true);
                        mainFrame.getSearchPanel().getSelectedTagsPanel().removeAll();
                        mainFrame.getSearchPanel().getSelectedTagsPanel().add(new JLabel("Seulement les messages non taggés"));
                    }
                    else {
                        mainFrame.getSearchPanel().setOnlyUntagged(false);
                        mainFrame.getSearchPanel().getSelectedTagsPanel().removeAll();
                        for(Component c : selectedTagsPanel().getComponents()){
                            mainFrame.getSearchPanel().getSelectedTagsPanel().add(c);
                        }
                    }
                    mainFrame.getSearchPanel().getSelectedTagsPanel().revalidate();
                    mainFrame.getSearchPanel().getSelectedTagsPanel().repaint();
                    setVisible(false);
                }
                else {
                    ArrayList<String> newTagsList = new ArrayList<String>();
                    for(Component c: selectedTagsPanel().getComponents()){
                        JLabel tagLabel = (JLabel)c;
                        newTagsList.add(tagLabel.getText());
                    }
                    setVisible(false);
                    log.info("newTagList"+newTagsList);
                    new ApplyTagWorker(mainFrame,newTagsList).start();
                }
            }
        });
        add(OKButton);

        // Quit Button :
        JButton QuitButton = new JButton("Quitter");
        QuitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(QuitButton, "cell 2 3");
        pack();
        setMinimumSize(getPreferredSize());
        setLocationRelativeTo(null);
    }
}
