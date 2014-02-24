package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Created by pic on 2/24/14.
 */
public class VisibleColumnDialog extends JDialog {

    public static final Logger log = Logger.getLogger("Archivix");
    private final MainFrame mainFrame ;


    /**
     * Constructor
     * @param mainFrame
     */
    public VisibleColumnDialog(final MainFrame mainFrame) {

        super(mainFrame,true);
        this.mainFrame = mainFrame ;
        setTitle("Colonnes visibles");
        setLayout(new MigLayout());
        add(new JLabel("Choisissez les colonnes visibles :"),"wrap");

        // ---------------------
        // All the check boxes :
        // ---------------------
        final JCheckBox idCheckBox = new JCheckBox(
                "clé primaire",
                !isHiddenColumn(mainFrame,MessageTableModel.IDCOL));
        add(idCheckBox,"wrap");

        final JCheckBox dateCheckBox = new JCheckBox(
                "Date et heure",
                !isHiddenColumn(mainFrame,MessageTableModel.DATECOL));
        add(dateCheckBox,"wrap");

        final JCheckBox authorCheckBox = new JCheckBox(
                "Auteur",
                !isHiddenColumn(mainFrame,MessageTableModel.AUTHORCOL));
        add(authorCheckBox,"wrap");

        final JCheckBox subjectCheckBox = new JCheckBox(
                "Sujet du message",
                !isHiddenColumn(mainFrame,MessageTableModel.SUBJECTCOL));
        add(subjectCheckBox,"wrap");

        final JCheckBox recipCheckBox = new JCheckBox(
                "Destinataires",!isHiddenColumn(mainFrame,MessageTableModel.RECIPCOL));
        add(recipCheckBox,"wrap");

        final JCheckBox bodyCheckBox = new JCheckBox(
                "Corps du message",
                !isHiddenColumn(mainFrame,MessageTableModel.BODYCOL));
        add(bodyCheckBox,"wrap");

        final JCheckBox attachCheckBox = new JCheckBox(
                "nombre de pièces jointes",
                !isHiddenColumn(mainFrame,MessageTableModel.ATTACHCOL));
        add(attachCheckBox,"wrap");

        final JCheckBox mailRecipCheckBox = new JCheckBox(
                "Destinataires (liste complète)",
                !isHiddenColumn(mainFrame,MessageTableModel.MAILRECIPCOL));
        add(mailRecipCheckBox,"wrap");

        final JCheckBox ccCheckBox = new JCheckBox(
                "Champs cc",
                !isHiddenColumn(mainFrame,MessageTableModel.CCCOL));
        add(ccCheckBox,"wrap");

        final JCheckBox bccCheckBox = new JCheckBox(
                "Champs bcc",
                !isHiddenColumn(mainFrame,MessageTableModel.BCCCOL));
        add(bccCheckBox,"wrap");

        final JCheckBox usernameCheckBox = new JCheckBox(
                "Nom d'utilisateur",
                !isHiddenColumn(mainFrame,MessageTableModel.USERNAMECOL));
        add(usernameCheckBox,"wrap");

        final JCheckBox insertDateCheckBox = new JCheckBox(
                "Date d'insertion",
                !isHiddenColumn(mainFrame,MessageTableModel.INSERTDATECOL));
        add(insertDateCheckBox,"wrap");

        final JCheckBox tagsCheckBox = new JCheckBox(
                "Tags",
                !isHiddenColumn(mainFrame,MessageTableModel.TAGSCOL));
        add(tagsCheckBox,"wrap");

        //-----------
        // OK Button :
        //-----------
        final JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idCheckBox.isSelected()){
                    showColumn(MessageTableModel.IDCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.IDCOL);
                }
                if(dateCheckBox.isSelected()){
                    showColumn(MessageTableModel.DATECOL);
                }
                else {
                    hideColumn(mainFrame,MessageTableModel.DATECOL);
                }
                if(authorCheckBox.isSelected()){
                    showColumn(MessageTableModel.AUTHORCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.AUTHORCOL);
                }
                if(subjectCheckBox.isSelected()){
                    showColumn(MessageTableModel.SUBJECTCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.SUBJECTCOL);
                }
                if(recipCheckBox.isSelected()){
                    showColumn(MessageTableModel.RECIPCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.RECIPCOL);
                }
                if(bodyCheckBox.isSelected()){
                    showColumn(MessageTableModel.BODYCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.BODYCOL);
                }
                if(attachCheckBox.isSelected()){
                    showColumn(MessageTableModel.ATTACHCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.ATTACHCOL);
                }
                if(mailRecipCheckBox.isSelected()){
                    showColumn(MessageTableModel.MAILRECIPCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.MAILRECIPCOL);
                }
                if(ccCheckBox.isSelected()){
                    showColumn(MessageTableModel.CCCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.CCCOL);
                }
                if(bccCheckBox.isSelected()){
                    showColumn(MessageTableModel.BCCCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.BCCCOL);
                }
                if(usernameCheckBox.isSelected()){
                    showColumn(MessageTableModel.USERNAMECOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.USERNAMECOL);
                }
                if(insertDateCheckBox.isSelected()){
                    showColumn(MessageTableModel.INSERTDATECOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.INSERTDATECOL);
                }
                if(tagsCheckBox.isSelected()){
                    showColumn(MessageTableModel.TAGSCOL);
                }
                else{
                    hideColumn(mainFrame,MessageTableModel.TAGSCOL);
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

    public static void hideColumn(MainFrame mainFrame,int index){
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMinWidth(0);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMaxWidth(0);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setWidth(0);
    }

    private void showColumn(int index){
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMinWidth(10);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMaxWidth(Integer.MAX_VALUE);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setWidth(15);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setPreferredWidth(20);
    }

    public static boolean isHiddenColumn(MainFrame mainFrame,int index){
        int minWidth = mainFrame.getMessageTable().getColumnModel().getColumn(index).getMinWidth();
        int maxWidth = mainFrame.getMessageTable().getColumnModel().getColumn(index).getMaxWidth();
        int width = mainFrame.getMessageTable().getColumnModel().getColumn(index).getWidth();

        if(minWidth==0 && maxWidth==0 && width==0) return true ;
        else return false ;
    }
}
