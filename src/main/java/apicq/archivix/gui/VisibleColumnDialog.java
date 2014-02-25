package apicq.archivix.gui;

import apicq.archivix.tools.FindMessagesWorker;
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
        final MessageTableModel mtm = (MessageTableModel) mainFrame.getMessageTable().getModel();
        final JCheckBox idCheckBox = new JCheckBox(
                "clé primaire",
                MessageColumnFactory.isVisible(mtm.IDCOL));
        add(idCheckBox,"wrap");

        final JCheckBox dateCheckBox = new JCheckBox(
                "Date et heure",
                MessageColumnFactory.isVisible(mtm.DATECOL));
        add(dateCheckBox,"wrap");

        final JCheckBox authorCheckBox = new JCheckBox(
                "Auteur",
                MessageColumnFactory.isVisible(mtm.AUTHORCOL));
        add(authorCheckBox,"wrap");

        final JCheckBox subjectCheckBox = new JCheckBox(
                "Sujet du message",
                MessageColumnFactory.isVisible(mtm.SUBJECTCOL));
        add(subjectCheckBox,"wrap");

        final JCheckBox recipCheckBox = new JCheckBox(
                "Destinataires",
                MessageColumnFactory.isVisible(mtm.RECIPCOL));
        add(recipCheckBox,"wrap");

        final JCheckBox bodyCheckBox = new JCheckBox(
                "Corps du message",
                MessageColumnFactory.isVisible(mtm.BODYCOL));
        add(bodyCheckBox,"wrap");

        final JCheckBox attachCheckBox = new JCheckBox(
                "nombre de pièces jointes",
                MessageColumnFactory.isVisible(mtm.ATTACHCOL));
        add(attachCheckBox,"wrap");

        final JCheckBox mailRecipCheckBox = new JCheckBox(
                "Destinataires (liste complète)",
                MessageColumnFactory.isVisible(mtm.MAILRECIPCOL));
        add(mailRecipCheckBox,"wrap");

        final JCheckBox ccCheckBox = new JCheckBox(
                "Champs cc",
                MessageColumnFactory.isVisible(mtm.CCCOL));
        add(ccCheckBox,"wrap");

        final JCheckBox bccCheckBox = new JCheckBox(
                "Champs bcc",
                MessageColumnFactory.isVisible(mtm.BCCCOL));
        add(bccCheckBox,"wrap");

        final JCheckBox usernameCheckBox = new JCheckBox(
                "Nom d'utilisateur",
                MessageColumnFactory.isVisible(mtm.USERNAMECOL));
        add(usernameCheckBox,"wrap");

        final JCheckBox insertDateCheckBox = new JCheckBox(
                "Date d'insertion",
                MessageColumnFactory.isVisible(mtm.INSERTDATECOL));
        add(insertDateCheckBox,"wrap");

        final JCheckBox tagsCheckBox = new JCheckBox(
                "Tags",
                MessageColumnFactory.isVisible(mtm.TAGSCOL));
        add(tagsCheckBox,"wrap");

        //-----------
        // OK Button :
        //-----------
        final JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.IDCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.IDCOL,false);
                }

                if(dateCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.DATECOL,true);

                }
                else {
                    MessageColumnFactory.setVisibility(mtm.DATECOL,false);

                }
                if(authorCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.AUTHORCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.AUTHORCOL,false);
                }
                if(subjectCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.SUBJECTCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.SUBJECTCOL,false);
                }
                if(recipCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.RECIPCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.RECIPCOL,false);
                }
                if(bodyCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.BODYCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.BODYCOL,false);
                }
                if(attachCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.ATTACHCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.ATTACHCOL,false);
                }
                if(mailRecipCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.MAILRECIPCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.MAILRECIPCOL,false);
                }
                if(ccCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.CCCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.CCCOL,false);
                }
                if(bccCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.BCCCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.BCCCOL,false);
                }
                if(usernameCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.USERNAMECOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.USERNAMECOL,false);
                }
                if(insertDateCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.INSERTDATECOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.INSERTDATECOL,false);
                }
                if(tagsCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(mtm.TAGSCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(mtm.TAGSCOL,false);
                }
                new FindMessagesWorker(mainFrame).start();
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

    /*
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
*/
}
