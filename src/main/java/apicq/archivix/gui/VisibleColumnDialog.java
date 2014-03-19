package apicq.archivix.gui;

import apicq.archivix.tools.FindMessagesWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
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
                MessageColumnFactory.isVisible(MessageTableModel.IDCOL));
        add(idCheckBox,"wrap");

        final JCheckBox dateCheckBox = new JCheckBox(
                "Date et heure",
                MessageColumnFactory.isVisible(MessageTableModel.DATECOL));
        add(dateCheckBox,"wrap");

        final JCheckBox authorCheckBox = new JCheckBox(
                "Auteur",
                MessageColumnFactory.isVisible(MessageTableModel.AUTHORCOL));
        add(authorCheckBox,"wrap");

        final JCheckBox subjectCheckBox = new JCheckBox(
                "Sujet du message",
                MessageColumnFactory.isVisible(MessageTableModel.SUBJECTCOL));
        add(subjectCheckBox,"wrap");

        final JCheckBox recipCheckBox = new JCheckBox(
                "Destinataires",
                MessageColumnFactory.isVisible(MessageTableModel.RECIPCOL));
        add(recipCheckBox,"wrap");

        final JCheckBox bodyCheckBox = new JCheckBox(
                "Corps du message",
                MessageColumnFactory.isVisible(MessageTableModel.BODYCOL));
        add(bodyCheckBox,"wrap");

        final JCheckBox attachCheckBox = new JCheckBox(
                "nombre de pièces jointes",
                MessageColumnFactory.isVisible(MessageTableModel.ATTACHCOL));
        add(attachCheckBox,"wrap");

        final JCheckBox mailRecipCheckBox = new JCheckBox(
                "Destinataires (liste complète)",
                MessageColumnFactory.isVisible(MessageTableModel.MAILRECIPCOL));
        add(mailRecipCheckBox,"wrap");

        final JCheckBox ccCheckBox = new JCheckBox(
                "Champs cc",
                MessageColumnFactory.isVisible(MessageTableModel.CCCOL));
        add(ccCheckBox,"wrap");

        final JCheckBox bccCheckBox = new JCheckBox(
                "Champs bcc",
                MessageColumnFactory.isVisible(MessageTableModel.BCCCOL));
        add(bccCheckBox,"wrap");

        final JCheckBox usernameCheckBox = new JCheckBox(
                "Nom d'utilisateur",
                MessageColumnFactory.isVisible(MessageTableModel.USERNAMECOL));
        add(usernameCheckBox,"wrap");

        final JCheckBox insertDateCheckBox = new JCheckBox(
                "Date d'insertion",
                MessageColumnFactory.isVisible(MessageTableModel.INSERTDATECOL));
        add(insertDateCheckBox,"wrap");

        final JCheckBox tagsCheckBox = new JCheckBox(
                "Tags",
                MessageColumnFactory.isVisible(MessageTableModel.TAGSCOL));
        add(tagsCheckBox,"wrap");

        final JCheckBox lineNumberCheckBox = new JCheckBox(
                "Numéro des lignes",
                MessageColumnFactory.isVisible(MessageTableModel.LINENUMBERCOL));
        add(lineNumberCheckBox,"wrap");

        //-----------
        // OK Button :
        //-----------
        final JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.IDCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.IDCOL,false);
                }

                if(dateCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.DATECOL,true);

                }
                else {
                    MessageColumnFactory.setVisibility(MessageTableModel.DATECOL,false);

                }
                if(authorCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.AUTHORCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.AUTHORCOL,false);
                }
                if(subjectCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.SUBJECTCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.SUBJECTCOL,false);
                }
                if(recipCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.RECIPCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.RECIPCOL,false);
                }
                if(bodyCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.BODYCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.BODYCOL,false);
                }
                if(attachCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.ATTACHCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.ATTACHCOL,false);
                }
                if(mailRecipCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.MAILRECIPCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.MAILRECIPCOL,false);
                }
                if(ccCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.CCCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.CCCOL,false);
                }
                if(bccCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.BCCCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.BCCCOL,false);
                }
                if(usernameCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.USERNAMECOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.USERNAMECOL,false);
                }
                if(insertDateCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.INSERTDATECOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.INSERTDATECOL,false);
                }
                if(tagsCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.TAGSCOL,true);
                }
                else{
                    MessageColumnFactory.setVisibility(MessageTableModel.TAGSCOL,false);
                }
                if(lineNumberCheckBox.isSelected()){
                    MessageColumnFactory.setVisibility(MessageTableModel.LINENUMBERCOL,true);
                } else {
                    MessageColumnFactory.setVisibility(MessageTableModel.LINENUMBERCOL,false);
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
    } // constructor
}
