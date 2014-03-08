package apicq.archivix.gui;

import apicq.archivix.tools.FindUserWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by pic on 2/20/14.
 */
public class SelectUserDialog extends JDialog {

    public static final Logger log = Logger.getLogger("Archivix");
    private final MainFrame mainFrame ;
    private JList<String> userList ;

    /**
     * Getter
     * @return the username list
     */
    public JList<String> getUserList() {
        return userList;
    }


    /**
     * Constructor
     */
    public SelectUserDialog(final MainFrame mainFrame,Vector<String> nameVector){
        super(mainFrame,true);//todo check constructor
        this.mainFrame = mainFrame ;
        setLayout(new MigLayout());

        add(new JLabel("Seuls les messages de cet utilisateur seront affichés:"),"wrap");

        userList = new JList<String>(nameVector);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(userList), "span");

        final JCheckBox allUsersBox = new JCheckBox("Afficher tous les utilisateurs");
        add(allUsersBox,"wrap");

        JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(allUsersBox.isSelected()){
                    mainFrame.getSearchPanel().setPerUserSelection(false);
                    mainFrame.getSearchPanel().setuserLabel("Tous");
                }
                else {
                    if(userList.getSelectedValue()!=null){
                        mainFrame.getSearchPanel().setPerUserSelection(true);
                        mainFrame.getSearchPanel().setUserName(userList.getSelectedValue());
                        mainFrame.getSearchPanel().setuserLabel(userList.getSelectedValue());
                    }
                }
                setVisible(false);
                mainFrame.revalidate();
                mainFrame.repaint();
                return;
            }
        });
        add(OKButton,"");

        JButton cancelButton = new JButton("Quitter");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(cancelButton,"");

        pack();
        setVisible(true);
    } // constructor
}

