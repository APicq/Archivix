package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;
import sun.applet.Main;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
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
                !isHiddenColumn(mainFrame,"id"));
        add(idCheckBox,"wrap");

        final JCheckBox dateCheckBox = new JCheckBox(
                "Date et heure",
                !isHiddenColumn(mainFrame,"date"));
        add(dateCheckBox,"wrap");

        final JCheckBox authorCheckBox = new JCheckBox(
                "Auteur",
                !isHiddenColumn(mainFrame,"auteur"));
        add(authorCheckBox,"wrap");

        final JCheckBox subjectCheckBox = new JCheckBox(
                "Sujet du message",
                !isHiddenColumn(mainFrame,"sujet"));
        add(subjectCheckBox,"wrap");

        final JCheckBox recipCheckBox = new JCheckBox(
                "Destinataires",
                !isHiddenColumn(mainFrame,"destinataires"));
        add(recipCheckBox,"wrap");

        final JCheckBox bodyCheckBox = new JCheckBox(
                "Corps du message",
                !isHiddenColumn(mainFrame,"texte"));
        add(bodyCheckBox,"wrap");

        final JCheckBox attachCheckBox = new JCheckBox(
                "nombre de pièces jointes",
                !isHiddenColumn(mainFrame,"nombre de pj"));
        add(attachCheckBox,"wrap");

        final JCheckBox mailRecipCheckBox = new JCheckBox(
                "Destinataires (liste complète)",
                !isHiddenColumn(mainFrame,"destinataires(complet)"));
        add(mailRecipCheckBox,"wrap");

        final JCheckBox ccCheckBox = new JCheckBox(
                "Champs cc",
                !isHiddenColumn(mainFrame,"cc"));
        add(ccCheckBox,"wrap");

        final JCheckBox bccCheckBox = new JCheckBox(
                "Champs bcc",
                !isHiddenColumn(mainFrame,"bcc"));
        add(bccCheckBox,"wrap");

        final JCheckBox usernameCheckBox = new JCheckBox(
                "Nom d'utilisateur",
                !isHiddenColumn(mainFrame,"utilisateur"));
        add(usernameCheckBox,"wrap");

        final JCheckBox insertDateCheckBox = new JCheckBox(
                "Date d'insertion",
                !isHiddenColumn(mainFrame,"date insertion"));
        add(insertDateCheckBox,"wrap");

        final JCheckBox tagsCheckBox = new JCheckBox(
                "Tags",
                !isHiddenColumn(mainFrame,"tags"));
        add(tagsCheckBox,"wrap");

        //-----------
        // OK Button :
        //-----------
        final JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idCheckBox.isSelected()){
                    showColumn(mainFrame,"id");
                }
                else{
                    hideColumn(mainFrame,"id");
                }
                if(dateCheckBox.isSelected()){
                    showColumn(mainFrame,"date");
                }
                else {
                    hideColumn(mainFrame,"date");
                }
                if(authorCheckBox.isSelected()){
                    showColumn(mainFrame,"auteur");
                }
                else{
                    hideColumn(mainFrame,"auteur");
                }
                if(subjectCheckBox.isSelected()){
                    showColumn(mainFrame,"sujet");
                }
                else{
                    hideColumn(mainFrame,"sujet");
                }
                if(recipCheckBox.isSelected()){
                    showColumn(mainFrame,"destinataires");
                }
                else{
                    hideColumn(mainFrame,"destinataires");
                }
                if(bodyCheckBox.isSelected()){
                    showColumn(mainFrame,"texte");
                }
                else{
                    hideColumn(mainFrame,"texte");
                }
                if(attachCheckBox.isSelected()){
                    showColumn(mainFrame,"nombre de pj");
                }
                else{
                    hideColumn(mainFrame,"nombre de pj");
                }
                if(mailRecipCheckBox.isSelected()){
                    showColumn(mainFrame,"destinataires(complet)");
                }
                else{
                    hideColumn(mainFrame,"destinataires(complet)");
                }
                if(ccCheckBox.isSelected()){
                    showColumn(mainFrame,"cc");
                }
                else{
                    hideColumn(mainFrame,"cc");
                }
                if(bccCheckBox.isSelected()){
                    showColumn(mainFrame,"bcc");
                }
                else{
                    hideColumn(mainFrame,"bcc");
                }
                if(usernameCheckBox.isSelected()){
                    showColumn(mainFrame,"utilisateur");
                }
                else{
                    hideColumn(mainFrame,"utilisateur");
                }
                if(insertDateCheckBox.isSelected()){
                    showColumn(mainFrame,"date insertion");
                }
                else{
                    hideColumn(mainFrame,"date insertion");
                }
                if(tagsCheckBox.isSelected()){
                    showColumn(mainFrame,"tags");
                }
                else{
                    hideColumn(mainFrame,"tags");
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

    public static void hideColumn(MainFrame mainFrame,String name){
        TableColumnModel tcm = mainFrame.getMessageTable().getColumnModel();
        tcm.getColumn(tcm.getColumnIndex(name)).setMinWidth(0);
        tcm.getColumn(tcm.getColumnIndex(name)).setMaxWidth(0);
        tcm.getColumn(tcm.getColumnIndex(name)).setWidth(0);
    }

    private void showColumn(int index){
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMinWidth(10);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setMaxWidth(Integer.MAX_VALUE);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setWidth(15);
        mainFrame.getMessageTable().getColumnModel().getColumn(index).setPreferredWidth(20);
    }

    private void showColumn(MainFrame mainFrame,String name){
        TableColumnModel tcm = mainFrame.getMessageTable().getColumnModel();
        tcm.getColumn(tcm.getColumnIndex(name)).setMinWidth(15);
        tcm.getColumn(tcm.getColumnIndex(name)).setMaxWidth(Integer.MAX_VALUE);
        tcm.getColumn(tcm.getColumnIndex(name)).setWidth(20);
        tcm.getColumn(tcm.getColumnIndex(name)).setPreferredWidth(20);
    }

    public static boolean isHiddenColumn(MainFrame mainFrame,int index){
        int minWidth = mainFrame.getMessageTable().getColumnModel().getColumn(index).getMinWidth();
        int maxWidth = mainFrame.getMessageTable().getColumnModel().getColumn(index).getMaxWidth();
        int width = mainFrame.getMessageTable().getColumnModel().getColumn(index).getWidth();
        if(minWidth==0 && maxWidth==0 && width==0) return true ;
        else return false ;
    }

    public static boolean isHiddenColumn(MainFrame mainFrame,String name){
        TableColumnModel tcm = mainFrame.getMessageTable().getColumnModel();
        int minWidth = tcm.getColumn(tcm.getColumnIndex(name)).getMinWidth();
        int maxWidth = tcm.getColumn(tcm.getColumnIndex(name)).getMaxWidth();
        int width = tcm.getColumn(tcm.getColumnIndex(name)).getWidth();
        if(minWidth==0 && maxWidth==0 && width==0) return true ;
        else return false ;
    }
}
