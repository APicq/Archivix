package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * Created by pic on 2/20/14.
 */
public class SelectUserDialog extends JDialog {

    private MainFrame mainFrame ;
    private JList<String> userList ;

    public JList<String> getUserList() {
        return userList;
    }

    public SelectUserDialog(MainFrame mainFrame){
        super(mainFrame,true);//todo check constructor
        setLayout(new MigLayout());

        add(new JLabel("Seuls les messages de cet utilisateur seront affichés:"),"wrap");

        userList = new JList<String>();
        // todo : start worker,update userList, pack, show, prepapre return value.

    }

}
