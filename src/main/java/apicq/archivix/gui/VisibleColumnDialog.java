package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pic on 2/24/14.
 */
public class VisibleColumnDialog extends JDialog {

    private final MainFrame mainFrame ;

    public VisibleColumnDialog(final MainFrame mainFrame) {

        super(mainFrame,true);
        this.mainFrame = mainFrame ;
        setTitle("Colonnes visibles");
        setLayout(new MigLayout());
        add(new JLabel("Choisissez les colonnes visibles :"),"wrap");

        final JCheckBox idCheckBox = new JCheckBox("clé primaire",isHiddenColumn(MessageTableModel.IDCOL));
        add(idCheckBox,"wrap");
        JCheckBox dateCheckBox = new JCheckBox("Date et heure",true);
        add(dateCheckBox,"wrap");
        JCheckBox authorCheckBox = new JCheckBox("Auteur",true);
        add(authorCheckBox,"wrap");
        JCheckBox subjectCheckBox = new JCheckBox("Sujet du message",true);
        add(subjectCheckBox,"wrap");
        JCheckBox recipCheckBox = new JCheckBox("Destinataires",true);
        add(recipCheckBox,"wrap");
        JCheckBox bodyCheckBox = new JCheckBox("Corps du message",true);
        add(bodyCheckBox,"wrap");
        JCheckBox attachCheckBox = new JCheckBox("nombre de pièces jointes",true);
        add(attachCheckBox,"wrap");
        JCheckBox mailRecipCheckBox = new JCheckBox("Destinataires (liste complète)",true);
        add(mailRecipCheckBox,"wrap");
        JCheckBox ccCheckBox = new JCheckBox("Champs cc",true);
        add(ccCheckBox,"wrap");
        JCheckBox bccCheckBox = new JCheckBox("Champs bcc",true);
        add(bccCheckBox,"wrap");
        JCheckBox usernameCheckBox = new JCheckBox("Nom d'utilisateur",true);
        add(usernameCheckBox,"wrap");
        JCheckBox insertDateCheckBox = new JCheckBox("Date d'insertion",true);
        add(insertDateCheckBox,"wrap");
        JCheckBox tagsCheckBox = new JCheckBox("Tags",true);
        add(tagsCheckBox,"wrap");

        //-----------
        // OK Button :
        //-----------
        final JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!idCheckBox.isSelected()){
                    hideColumn(MessageTableModel.IDCOL);
                }
                mainFrame.revalidate();
                mainFrame.repaint();
                setVisible(false);
            }

        });
        add(OKButton, "");


        JButton cancelButton = new JButton("Quitter");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(cancelButton, "");
        pack();
        //setVisible(true);

    } // constructor

    private void hideColumn(int index){
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMinWidth(0);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMaxWidth(0);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setWidth(0);
    }

    private boolean isHiddenColumn(int index){
        int minWidth = mainFrame.getMessageTable().getColumnModel().getColumn(index).getMinWidth();
        int maxWidth = mainFrame.getMessageTable().getColumnModel().getColumn(index).getMaxWidth();
        int width = mainFrame.getMessageTable().getColumnModel().getColumn(index).getWidth();
        if(minWidth==0 && maxWidth==0 && width==0) return true ;
        else return false ;
    }
}
