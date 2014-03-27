package apicq.archivix.gui;

import apicq.archivix.gui.table.MailTable;
import apicq.archivix.gui.table.MailTableModel;
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

        // pick up all visible Columns :

        setTitle("Colonnes visibles");
        setLayout(new MigLayout());
        add(new JLabel("Choisissez les colonnes visibles :"),"wrap");

        // ---------------------
        // All the check boxes :
        // ---------------------
        final MailTable mailTable = mainFrame.getMessageTable();

        final JCheckBox idCheckBox = new JCheckBox(
                "clé primaire (ID)",
                mailTable.isVisibleColumn(MailTableModel.IDCOL_NAME));
        log.info("ID visible : "+mailTable.isVisibleColumn(MailTableModel.IDCOL_NAME));
        add(idCheckBox, "wrap");

        final JCheckBox dateCheckBox = new JCheckBox(
                "Date et heure",
                mailTable.isVisibleColumn(MailTableModel.DATECOL_NAME));
        add(dateCheckBox,"wrap");

        final JCheckBox authorCheckBox = new JCheckBox(
                "Auteur",
                mailTable.isVisibleColumn(MailTableModel.AUTHORCOL_NAME));
        add(authorCheckBox,"wrap");

        final JCheckBox subjectCheckBox = new JCheckBox(
                "Sujet du message",
                mailTable.isVisibleColumn(MailTableModel.SUBJECTCOL_NAME));
        add(subjectCheckBox,"wrap");

        final JCheckBox recipCheckBox = new JCheckBox(
                "Destinataires",
                mailTable.isVisibleColumn(MailTableModel.RECIPCOL_NAME) );
        add(recipCheckBox,"wrap");

        final JCheckBox bodyCheckBox = new JCheckBox(
                "Corps du message",
                mailTable.isVisibleColumn(MailTableModel.BODYCOL_NAME));
        add(bodyCheckBox,"wrap");

        final JCheckBox attachCheckBox = new JCheckBox(
                "nombre de pièces jointes",
                mailTable.isVisibleColumn(MailTableModel.ATTACHCOL_NAME) );
        add(attachCheckBox,"wrap");

        final JCheckBox mailRecipCheckBox = new JCheckBox(
                "Destinataires (liste complète)",
                mailTable.isVisibleColumn(MailTableModel.MAILRECIPCOL_NAME));
        add(mailRecipCheckBox,"wrap");

        final JCheckBox ccCheckBox = new JCheckBox(
                "Champs cc",
                mailTable.isVisibleColumn(MailTableModel.CCCOL_NAME));
        add(ccCheckBox,"wrap");

        final JCheckBox bccCheckBox = new JCheckBox(
                "Champs bcc",
                mailTable.isVisibleColumn(MailTableModel.BCCCOL_NAME) );
        add(bccCheckBox,"wrap");

        final JCheckBox usernameCheckBox = new JCheckBox(
                "Nom d'utilisateur",
                mailTable.isVisibleColumn(MailTableModel.USERNAMECOL_NAME) );
        add(usernameCheckBox,"wrap");

        final JCheckBox insertDateCheckBox = new JCheckBox(
                "Date d'insertion",
                mailTable.isVisibleColumn(MailTableModel.INSERTDATECOL_NAME) );
        add(insertDateCheckBox,"wrap");

        final JCheckBox tagsCheckBox = new JCheckBox(
                "Tags",
                mailTable.isVisibleColumn(MailTableModel.TAGSCOL_NAME) );
        add(tagsCheckBox,"wrap");

        final JCheckBox lineNumberCheckBox = new JCheckBox(
                "Numéro des lignes",
                mailTable.isVisibleColumn(MailTableModel.LINENUMBERCOL_NAME) );
        add(lineNumberCheckBox,"wrap");

        //-----------
        // OK Button :
        //-----------
        final JButton OKButton = new JButton("OK");
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                mailTable.resetColumns();

                if(!idCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.IDCOL,true);
                    log.info("idCheckBox non selected");
                    log.info("table id : "+mailTable.getColumn(MailTableModel.IDCOL_NAME).getIdentifier());
//                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.IDCOL_NAME));
                    mailTable.removeColumn(mailTable.getColumn("ID"));
                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.IDCOL,false);
                }

                if(!dateCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.DATECOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.DATECOL_NAME));

                }
                else {
//                    MessageColumnFactory.setVisibility(MessageTableModel.DATECOL,false);

                }
                if(!authorCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.AUTHORCOL,true)
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.AUTHORCOL_NAME));
                    ;
                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.AUTHORCOL,false);
                }
                if(!subjectCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.SUBJECTCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.SUBJECTCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.SUBJECTCOL,false);
                }
                if(!recipCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.RECIPCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.RECIPCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.RECIPCOL,false);
                }
                if(!bodyCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.BODYCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.BODYCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.BODYCOL,false);
                }
                if(!attachCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.ATTACHCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.ATTACHCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.ATTACHCOL,false);
                }
                if(!mailRecipCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.MAILRECIPCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.MAILRECIPCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.MAILRECIPCOL,false);
                }
                if(!ccCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.CCCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.CCCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.CCCOL,false);
                }
                if(!bccCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.BCCCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.BCCCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.BCCCOL,false);
                }
                if(!usernameCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.USERNAMECOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.USERNAMECOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.USERNAMECOL,false);
                }
                if(!insertDateCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.INSERTDATECOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.INSERTDATECOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.INSERTDATECOL,false);
                }
                if(!tagsCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.TAGSCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.TAGSCOL_NAME));

                }
                else{
//                    MessageColumnFactory.setVisibility(MessageTableModel.TAGSCOL,false);
                }
                if(!lineNumberCheckBox.isSelected()){
//                    MessageColumnFactory.setVisibility(MessageTableModel.LINENUMBERCOL,true);
                    mailTable.removeColumn(mailTable.getColumn(MailTableModel.LINENUMBERCOL_NAME));

                } else {
//                    MessageColumnFactory.setVisibility(MessageTableModel.LINENUMBERCOL,false);
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
