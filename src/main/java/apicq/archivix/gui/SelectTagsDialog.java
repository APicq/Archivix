package apicq.archivix.gui;

import apicq.archivix.tools.FindTagsNamesWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Open a dialog, user can select tags from database.
 */
public class SelectTagsDialog extends JDialog {

    public static final Logger log = Logger.getLogger("Archivix");
    private MainFrame mainFrame ;

    private JList tagList ;

    private JPanel selectedTagsPanel ;
    public static final int CANCEL=0;
    public static final int OK=1;
    private int returnValue=CANCEL ;
    /**
     * Return value : OK or CANCEL
     * @return
     */
    public int returnValue(){
        return returnValue;
    }

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

    private TagListModel tagListModel ;

    /**
     * Listener for tagList
     */
    class MySelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectedTagsPanel.removeAll();
            for(Object tag:tagList.getSelectedValuesList()){
                JLabel tagLabel = new JLabel(tag.toString());
                tagLabel.setBorder(new LineBorder(Color.black));
                selectedTagsPanel.add(tagLabel);
            }
            SelectTagsDialog.this.validate();
            SelectTagsDialog.this.repaint();
        }
    }

    /**
     * Constructor
     * @param mainFrame
     */
    public SelectTagsDialog(MainFrame mainFrame) {
        setModal(true);
        setLayout(new MigLayout("","[][grow][]","[][grow 95][grow 5][]")); // layout,column,row
        this.mainFrame = mainFrame ;

        // Line 1
        add(new JLabel("tags disponibles :"), "wrap");

        // Line 2
        // pick up tags from database
        ArrayList<String> tagStringList = null ;
        FindTagsNamesWorker ftnw = new FindTagsNamesWorker(mainFrame);
        ftnw.execute();
        try {
            tagStringList = ftnw.get();
        } catch (Exception e) {
            log.warning(e.toString());
            tagStringList = new ArrayList<String>(); // creates an empty taglist in case of exception
        }

        tagListModel = new TagListModel(tagStringList);
        tagList = new JList<String>(tagListModel);
        tagList.addListSelectionListener(new MySelectionListener());
        tagList.setLayoutOrientation(JList.VERTICAL_WRAP);
        JScrollPane tagListScrollPane = new JScrollPane(tagList);
        add(tagListScrollPane, "grow,span");

        // Line 3
        selectedTagsPanel = new JPanel();
        selectedTagsPanel.setLayout(new MigLayout());
        add(new JLabel("Tags sélectionnés :"),"");
        add(new JScrollPane(selectedTagsPanel), "grow,span");

        // Line 4
        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnValue = OK ;
                setVisible(false);
            }
        });
        add(OKButton);
        JButton QuitButton = new JButton("Quitter");
        QuitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnValue = CANCEL ;
                setVisible(false);
            }
        });
        add(QuitButton, "cell 2 3");
        pack();
        setMinimumSize(getPreferredSize());
    }
}
