package apicq.archivix.gui;

import apicq.archivix.tools.CreateAndApplyNewTagWorker;
import apicq.archivix.tools.ProtectedConnection;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Table containing messages, plus events management
 */
public class MessageTable extends JTable {

    public static final Logger log = Logger.getLogger("Archivix");
    private final MainFrame mainFrame ;
    private MessageTableModel messageTableModel ;

    private JPopupMenu popupMenu ;
    // private JMenuItem addNewTagItem ;
    // private JMenuItem modifyTagsItem ;
    // private JMenuItem deleteMessagesItem ;

    public MessageTable(MainFrame mainFrame) {

        this.mainFrame = mainFrame ;
        messageTableModel = new MessageTableModel();
        setModel(messageTableModel);

        // Menu definition and actions :
        popupMenu = new JPopupMenu("Choisissez une action sur le(s) message(s)");
        JMenuItem addNewTagItem = new JMenuItem("Ajouter un nouveau tag");
        JMenuItem modifyTagsItem = new JMenuItem("Modifier les tags");
        JMenuItem deleteMessagesItem = new JMenuItem("Effacer le(s) message(s)");
        popupMenu.add(addNewTagItem);
        popupMenu.add(modifyTagsItem);
        popupMenu.add(deleteMessagesItem);

        // Actions

        // Add a new tag
        addNewTagItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAddNewTag();
            }
        });

        // Modify tags
        modifyTagsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionModifyTags();
            }
        });

        // Delete message
        deleteMessagesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionDeleteMessages();
            }
        });
        // Right-click : menu appears
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == e.BUTTON3 && getSelectedRow()!=-1) {// instanceof MessageTable){
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void actionDeleteMessages() {
        // todo
    }

    private void actionModifyTags() {
        // todo
    }

    private void actionAddNewTag() {
        /*
        JDialog dialog = new JDialog(mainFrame,"Nouveau tag:");
        dialog.setLayout(new MigLayout());
        dialog.add(new JLabel("Entrez un nouveau tag :"),"wrap");
        JTextField newTagField = new JTextField();
        dialog.add(newTagField,"span,grow");
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
        */
        String newTag = JOptionPane.showInputDialog(
                mainFrame,
                "Entrez un nouveau tag :",
                "Nouveau tag",
                JOptionPane.INFORMATION_MESSAGE);
        log.info("new tag : "+newTag);
        if(newTag!=null && newTag.trim().length()>0){
            try {
                PreparedStatement checkNewTagStmt = mainFrame.
                        pConnection().prepareStatement("SELECT id FROM tags where tag=?");
                checkNewTagStmt.setString(1,newTag);
                ResultSet rs = checkNewTagStmt.executeQuery();
                if(rs.next()){
                    JOptionPane.showMessageDialog(mainFrame,"Erreur : le tag existe !!");
                }
                else {
                    new CreateAndApplyNewTagWorker(mainFrame,newTag).execute();
                }
            } catch (SQLException e) {
                log.warning(e.toString());
                JOptionPane.showMessageDialog(mainFrame,"Erreur lors de la création des tags.");
                return ;
            }
        }
    }
}
