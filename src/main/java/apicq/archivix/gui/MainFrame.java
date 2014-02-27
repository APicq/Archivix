package apicq.archivix.gui;

import apicq.archivix.tools.*;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.table.ColumnFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Main Program Window
 */
public class MainFrame extends JFrame implements ActionListener {

    public static final Logger log = Logger.getLogger("Archivix");

    // Full path to sqlite database :
    private String dabataseFile = "";
    public String databaseFile() {return dabataseFile;}

    // Full path to attachment directory
    private String attachmentDirectory = "";
    public String attachmentDirectory() {return attachmentDirectory;}

    // JTable to print messages :
    private final MessageTable messageTable ;
    public MessageTable getMessageTable(){ return messageTable ;}

    // Panel with buttons, to search elements
    private final SearchPanel searchPanel;
    public SearchPanel getSearchPanel(){ return searchPanel ;}

    /**
     * Constructor
     */
    public MainFrame() {

        // Create Column factory :
        ColumnFactory.setInstance(new MessageColumnFactory());

        // Load configuration :
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.txt"));
            dabataseFile = prop.getProperty("database");
            attachmentDirectory = prop.getProperty("attachmentdir");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Aucune base de données configurée. Connectez-vous\n"+
                    "à une base de données et choisissez un répertoire pour les pièces jointes");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load visibility :

        if(prop.getProperty("idcol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.IDCOL,false);
        if(prop.getProperty("datecol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.DATECOL,false);
        if(prop.getProperty("authorcol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.AUTHORCOL,false);
        if(prop.getProperty("subjectcol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.SUBJECTCOL,false);
        if(prop.getProperty("recipcol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.RECIPCOL,false);
        if(prop.getProperty("bodycol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.BODYCOL,false);
        if(prop.getProperty("attachcol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.ATTACHCOL,false);
        if(prop.getProperty("mailrecipcol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.MAILRECIPCOL,false);
        if(prop.getProperty("cccol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.CCCOL,false);
        if(prop.getProperty("bcccol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.BCCCOL,false);
        if(prop.getProperty("usernamecol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.USERNAMECOL,false);
        if(prop.getProperty("insertdatecol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.INSERTDATECOL,false);
        if(prop.getProperty("tagscol","yes").equals("no"))
            MessageColumnFactory.setVisibility(MessageTableModel.TAGSCOL,false);


        // Disable renaming in file chooser :
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);

        // Misc frame configuration
        setLayout(new MigLayout("", "[grow,fill]", "[][grow]"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -----------
        // Setup menu
        // -----------
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");

        JMenuItem insertMessageItem = new JMenuItem("Insérer des messages");
        insertMessageItem.setActionCommand("insertMessagesAction");
        insertMessageItem.addActionListener(this);
        fileMenu.add(insertMessageItem);

        JMenuItem databaseChooseItem = new JMenuItem("Connecter la base de données");
        databaseChooseItem.setActionCommand("connectDatabaseAction");
        databaseChooseItem.addActionListener(this);
        fileMenu.add(databaseChooseItem);

        JMenuItem attachDirItem = new JMenuItem("Définir le répertoire des pièces jointes");
        attachDirItem.setActionCommand("setAttachDirAction");
        attachDirItem.addActionListener(this);
        fileMenu.add(attachDirItem);

        JMenuItem quitItem = new JMenuItem("Sauvegarder la configuration et quitter");
        quitItem.setActionCommand("quitAction");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);

        JMenu configurationMenu = new JMenu("Configuration");
        JMenuItem configColumnItem = new JMenuItem("Configurer les colonnes visibles");
        configColumnItem.setActionCommand("configColumnsAction");
        configColumnItem.addActionListener(this);
        configurationMenu.add(configColumnItem);
        menuBar.add(configurationMenu);

        setJMenuBar(menuBar);

        // ----------
        // Components
        // ----------
        searchPanel = new SearchPanel(this);
        add(searchPanel, "wrap");
        messageTable = new MessageTable(this);


        JScrollPane messageListScroller = new JScrollPane(messageTable);
        add(messageListScroller, "grow");

        // Finalize
        pack();
        setMinimumSize(getPreferredSize());

        setVisible(true);
        setLocationRelativeTo(null);

        new CheckDatabaseWorker(this).start();
        log.info("balise");

    }// constructor



    /**
     * Program entry point
     */
    public static void main(String[] args) {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            log.warning(e.toString());
            return ;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log.warning(e.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
            }
        });
    }



    /**
     * All actions are dispatched here :
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if("deleteMessagesAction".equals(e.getActionCommand())){
            new DeleteMessagesWorker(MainFrame.this).start();
            return ;
        }

        if("addNewTagAction".equals(e.getActionCommand())){
            String newTag = JOptionPane.showInputDialog(
                    MainFrame.this,
                    "Entrez un nouveau tag (pas d'espace) :",
                    "Nouveau tag",
                    JOptionPane.INFORMATION_MESSAGE);
            newTag = newTag.replaceAll(" +",newTag);
            if(newTag!=null && newTag.trim().length()>0){
                new AddNewTagWorker(MainFrame.this,newTag).start();
            }
        }

        if("modifyTagsAction".equals(e.getActionCommand())){
            new FindTagsWorker(MainFrame.this,false).start();
        }

        if("insertMessagesAction".equals(e.getActionCommand())){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choisissez un ou des fichiers messages outlook (.msg)");
            fileChooser.setMultiSelectionEnabled(true);
            if(fileChooser.showOpenDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
                File[] messageFiles = fileChooser.getSelectedFiles();
                new InsertMessageWorker(MainFrame.this,messageFiles).start();

            }
            return ;
        }

        if("connectDatabaseAction".equals(e.getActionCommand())){
            final JFileChooser chooser = new JFileChooser();
            int returnValue = chooser.showOpenDialog(MainFrame.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                dabataseFile = (chooser.getSelectedFile().getAbsolutePath());
                new CheckDatabaseWorker(MainFrame.this).start();
                MainFrame.this.setTitle(chooser.getSelectedFile().getName());
            }
        }

        if("setAttachDirAction".equals(e.getActionCommand())){
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = chooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                attachmentDirectory = chooser.getSelectedFile().getAbsolutePath();
            }
        }

        if("quitAction".equals(e.getActionCommand())){
            Properties prop = new Properties();
            prop.setProperty("database",dabataseFile);
            prop.setProperty("attachmentdir",attachmentDirectory);

            if(MessageColumnFactory.isVisible(MessageTableModel.IDCOL)){
                prop.setProperty("idcol","yes");
            }
            else {
                prop.setProperty("idcol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.DATECOL)){
                prop.setProperty("datecol","yes");
            }
            else {
                prop.setProperty("datecol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.AUTHORCOL)){
                prop.setProperty("authorcol","yes");
            }
            else {
                prop.setProperty("authorcol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.SUBJECTCOL)){
                prop.setProperty("subjectcol","yes");
            }
            else {
                prop.setProperty("subjectcol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.RECIPCOL)){
                prop.setProperty("recipcol","yes");
            }
            else {
                prop.setProperty("recipcol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.BODYCOL)){
                prop.setProperty("bodycol","yes");
            }
            else {
                prop.setProperty("bodycol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.ATTACHCOL)){
                prop.setProperty("attachcol","yes");
            }
            else {
                prop.setProperty("attachcol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.MAILRECIPCOL)){
                prop.setProperty("mailrecipcol","yes");
            }
            else {
                prop.setProperty("mailrecipcol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.CCCOL)){
                prop.setProperty("cccol","yes");
            }
            else {
                prop.setProperty("cccol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.BCCCOL)){
                prop.setProperty("bcccol","yes");
            }
            else {
                prop.setProperty("bcccol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.USERNAMECOL)){
                prop.setProperty("usernamecol","yes");
            }
            else {
                prop.setProperty("usernamecol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.INSERTDATECOL)){
                prop.setProperty("insertdatecol","yes");
            }
            else {
                prop.setProperty("insertdatecol","no");
            }
            if(MessageColumnFactory.isVisible(MessageTableModel.TAGSCOL)){
                prop.setProperty("tagscol","yes");
            }
            else {
                prop.setProperty("tagscol","no");
            }

            try {
                prop.store(new FileOutputStream("config.txt"), "properties for Archivix");
            } catch (IOException except) {
                JOptionPane.showMessageDialog(this,"ERREUR : l'enregistrement des paramètres a échoué.");
                log.warning(except.getMessage());
            }
            System.exit(0);
        }

        if("findMessagesAction".equals(e.getActionCommand())){
            searchPanel.setPageNumber(1);
            new FindMessagesWorker(MainFrame.this).start();
        }

        if("selectTagsAction".equals(e.getActionCommand())){
            new FindTagsWorker(MainFrame.this,true).start();
        }
        if("previousAction".equals(e.getActionCommand())){
            if(getSearchPanel().getPageNumber()>1){
                getSearchPanel().setPageNumber(SearchPanel.getPageNumber()-1);
                new FindMessagesWorker(this).start();
            }
        }
        if("nextAction".equals(e.getActionCommand())){
            // Number of results :
            int resultNumber = getMessageTable().getModel().getRowCount() ;
            int limit = Integer.parseInt(getSearchPanel().getMaxResultNumberField().getText());
            if( resultNumber >= limit ){
                log.info(""+SearchPanel.getPageNumber());
                getSearchPanel().setPageNumber(SearchPanel.getPageNumber()+1);
                new FindMessagesWorker(this).start();
            }
        }
        if("findUserAction".equals(e.getActionCommand())){
            new FindUserWorker(MainFrame.this).start();
        }
        if("deleteMessagesAction".equals(e.getActionCommand())){
            int result = JOptionPane.showConfirmDialog(
                    MainFrame.this,
                    "Etes-vous sûr de vouloir effacer les messages sélectionnés ?",
                    "Effacement des messages",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if( result==JOptionPane.YES_OPTION ){
                new DeleteMessagesWorker(MainFrame.this).start();
            }
        }
        if("configColumnsAction".equals(e.getActionCommand())){
            new VisibleColumnDialog(this).setVisible(true);
        }
        if("createReportAction".equals(e.getActionCommand())){
            new CreateReportWorker(this).start();
        }


    }
}
