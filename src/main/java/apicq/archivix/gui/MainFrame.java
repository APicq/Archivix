package apicq.archivix.gui;

import apicq.archivix.gui.table.MailTable;
import apicq.archivix.gui.table.MailTableModel;
import apicq.archivix.tools.*;
import net.miginfocom.swing.MigLayout;

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
    public static final String archivixVersion = "Test_0328_1";

    // Full path to sqlite database :
    private String dabataseFile = "";

    /**
     * Getter for database file
     * @return
     */
    public String databaseFile() {return dabataseFile;}

    // Full path to attachment directory
    private String attachmentDirectory = "";

    /**
     * Getter for attachment directory
     * @return
     */
    public String attachmentDirectory() {return attachmentDirectory;}

    // Full path to message pool directory :
    private String messagePoolDirectory = "" ;

    // JTable to print messages :
    private final MailTable messageTable ;

    /**
     * getter for message JTable
     * @return MailTable
     */
    public MailTable getMessageTable(){ return messageTable ;}

    // Panel with buttons, to search elements
    private final SearchPanel searchPanel;
    public SearchPanel getSearchPanel(){ return searchPanel ;}

    public void updateMainTitle(){
        setTitle("Archivix_"+archivixVersion+" -- base de donnée : ");
        if(dabataseFile.length()==0){
            setTitle(getTitle()+" Aucune base de données");
        } else {
            setTitle(getTitle()+dabataseFile);
        }
        setTitle(getTitle()+ " || Pièces jointes : ");
        if(attachmentDirectory.length()==0){
            setTitle(getTitle()+"aucun répertoire sélectionné");
        } else {
            setTitle(getTitle()+attachmentDirectory);
        }
        revalidate();
        repaint();
    }

    /**
     * Constructor
     */
    public MainFrame() {

        // Load configuration :
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.txt"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Aucune base de données configurée. Connectez-vous\n"+
                    "à une base de données et choisissez un répertoire pour les pièces jointes");
            updateMainTitle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Apply configuration
        dabataseFile = prop.getProperty("database","");
        attachmentDirectory = prop.getProperty("attachmentdir","");
        messagePoolDirectory = prop.getProperty("messagepooldir","");
        updateMainTitle();


        // enable renaming in file chooser :
        UIManager.put("FileChooser.readOnly", Boolean.FALSE);

        // Close program if window closed :
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout configuration :
        setLayout(new MigLayout("", "[grow,fill]", "[][grow]"));

        // -----------
        // Setup menu
        // -----------
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");

        JMenuItem insertMessageItem = new JMenuItem("Insérer des messages");
        insertMessageItem.setActionCommand("insertMessagesAction");
        insertMessageItem.addActionListener(this);
        fileMenu.add(insertMessageItem);


        JMenuItem saveDatabaseItem = new JMenuItem("Créer un point de sauvegarde de la base de données");
        saveDatabaseItem.setActionCommand("saveDatabaseAction");
        saveDatabaseItem.addActionListener(this);
        fileMenu.add(saveDatabaseItem);

        JMenuItem quitItem = new JMenuItem("Quitter");
        quitItem.setActionCommand("quitAction");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);

        JMenu configurationMenu = new JMenu("Configuration");

        JMenuItem configColumnItem = new JMenuItem("Configurer les colonnes visibles");
        configColumnItem.setActionCommand("configColumnsAction");
        configColumnItem.addActionListener(this);
        configurationMenu.add(configColumnItem);

        JMenuItem databaseChooseItem = new JMenuItem("Connecter une base de données");
        databaseChooseItem.setActionCommand("connectDatabaseAction");
        databaseChooseItem.addActionListener(this);
        configurationMenu.add(databaseChooseItem);

        JMenuItem attachDirItem = new JMenuItem("Définir le répertoire des pièces jointes");
        attachDirItem.setActionCommand("setAttachDirAction");
        attachDirItem.addActionListener(this);
        configurationMenu.add(attachDirItem);

        JMenuItem saveConfigItem = new JMenuItem("Sauvegarder la configuration");
        saveConfigItem.setActionCommand("saveConfigAction");
        saveConfigItem.addActionListener(this);
        configurationMenu.add(saveConfigItem);


        menuBar.add(configurationMenu);

        setJMenuBar(menuBar);

        // ----------
        // Components
        // ----------
        searchPanel = new SearchPanel(this);
        add(searchPanel, "wrap");


        messageTable = new MailTable(this);

        // Load messageTable configuration :
        if(prop.getProperty("idcol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.IDCOL_NAME));
        if(prop.getProperty("datecol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.DATECOL_NAME));
        if(prop.getProperty("authorcol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.AUTHORCOL_NAME));
        if(prop.getProperty("subjectcol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.SUBJECTCOL_NAME));
        if(prop.getProperty("recipcol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.RECIPCOL_NAME));
        if(prop.getProperty("bodycol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.BODYCOL_NAME));
        if(prop.getProperty("attachcol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.ATTACHCOL_NAME));
        if(prop.getProperty("mailrecipcol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.MAILRECIPCOL_NAME));
        if(prop.getProperty("cccol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.CCCOL_NAME));
        if(prop.getProperty("bcccol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.BCCCOL_NAME));
        if(prop.getProperty("usernamecol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.USERNAMECOL_NAME));
        if(prop.getProperty("insertdatecol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.INSERTDATECOL_NAME));
        if(prop.getProperty("tagscol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.TAGSCOL_NAME));
        if(prop.getProperty("linenumbercol","yes").equals("no"))
            messageTable.removeColumn(messageTable.getColumn(MailTableModel.LINENUMBERCOL_NAME));

        JScrollPane messageListScroller = new JScrollPane(messageTable);
        add(messageListScroller, "grow");

        // Finalize
        pack();
        setMinimumSize(getPreferredSize());

        setLocationRelativeTo(null);
        setVisible(true);
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
            JFileChooser fileChooser = new JFileChooser(messagePoolDirectory);
            fileChooser.setDialogTitle("Choisissez un ou des fichiers messages outlook (.msg)");
            fileChooser.setMultiSelectionEnabled(true);
            if(fileChooser.showOpenDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
                messagePoolDirectory = fileChooser.getCurrentDirectory().getAbsolutePath();
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
            }
        }

        if("setAttachDirAction".equals(e.getActionCommand())){
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = chooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                attachmentDirectory = chooser.getSelectedFile().getAbsolutePath();
                new CheckDatabaseWorker(MainFrame.this).start();
            }
        }

        if("saveConfigAction".equals(e.getActionCommand())){
            Properties prop = new Properties();
            prop.setProperty("database",dabataseFile);
            prop.setProperty("attachmentdir",attachmentDirectory);
            prop.setProperty("messagepooldir",messagePoolDirectory);
            if(messageTable.isVisibleColumn(MailTableModel.IDCOL_NAME)){
                prop.setProperty("idcol","yes");
            }
            else {
                prop.setProperty("idcol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.DATECOL_NAME)){
                prop.setProperty("datecol","yes");
            }
            else {
                prop.setProperty("datecol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.AUTHORCOL_NAME)){
                prop.setProperty("authorcol","yes");
            }
            else {
                prop.setProperty("authorcol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.SUBJECTCOL_NAME)){
                prop.setProperty("subjectcol","yes");
            }
            else {
                prop.setProperty("subjectcol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.RECIPCOL_NAME)){
                prop.setProperty("recipcol","yes");
            }
            else {
                prop.setProperty("recipcol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.BODYCOL_NAME)){
                prop.setProperty("bodycol","yes");
            }
            else {
                prop.setProperty("bodycol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.ATTACHCOL_NAME)){
                prop.setProperty("attachcol","yes");
            }
            else {
                prop.setProperty("attachcol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.MAILRECIPCOL_NAME)){
                prop.setProperty("mailrecipcol","yes");
            }
            else {
                prop.setProperty("mailrecipcol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.CCCOL_NAME)){
                prop.setProperty("cccol","yes");
            }
            else {
                prop.setProperty("cccol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.BCCCOL_NAME)){
                prop.setProperty("bcccol","yes");
            }
            else {
                prop.setProperty("bcccol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.USERNAMECOL_NAME)){
                prop.setProperty("usernamecol","yes");
            }
            else {
                prop.setProperty("usernamecol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.INSERTDATECOL_NAME)){
                prop.setProperty("insertdatecol","yes");
            }
            else {
                prop.setProperty("insertdatecol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.TAGSCOL_NAME)){
                prop.setProperty("tagscol","yes");
            }
            else {
                prop.setProperty("tagscol","no");
            }
            if(messageTable.isVisibleColumn(MailTableModel.LINENUMBERCOL_NAME)){
                prop.setProperty("linenumbercol","yes");
            }
            else {
                prop.setProperty("linenumbercol","no");
            }
            try {
                prop.store(new FileOutputStream("config.txt"), "properties for Archivix");
                JOptionPane.showMessageDialog(this,"La configuration est enregistrée sous config.txt");
            } catch (IOException except) {
                JOptionPane.showMessageDialog(this,"ERREUR : l'enregistrement des paramètres a échoué.");
                log.warning(except.getMessage());
            }
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
            try {
                int limit = Integer.parseInt(getSearchPanel().getMaxResultNumberField().getText());
                if( resultNumber >= limit ){
                    getSearchPanel().setPageNumber(SearchPanel.getPageNumber()+1);
                    new FindMessagesWorker(this).start();
                }
            } catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(this,"le champs \"résultats maximum par page\" n'est pas un entier positif !");
            }
            return;
        }
        if("findUserAction".equals(e.getActionCommand())){
            new FindUserWorker(MainFrame.this).start();
        }
        if("deleteMessagesAction".equals(e.getActionCommand())){

            String result = JOptionPane.showInputDialog(
                    MainFrame.this,
                    "Tapez OUI pour effacer les messages sélectionnés ?",
                    "Effacement des messages",
                    JOptionPane.WARNING_MESSAGE);
            if( result.equals("OUI") ){
                new DeleteMessagesWorker(MainFrame.this).start();
            }
        }
        if("configColumnsAction".equals(e.getActionCommand())){
            new VisibleColumnDialog(this).setVisible(true);
            return ;
        }
        if("createReportAction".equals(e.getActionCommand())){
            new CreateReportWorker(this).start();
            return ;
        }
        if( "quitAction".equals(e.getActionCommand())){
            System.exit(0);
        }
        if("saveDatabaseAction".equals(e.getActionCommand())){
            new SaveDatabaseWorker(this).start();
        }
    }
}
/*
 todo : tags table : make(tag,msgid) primary key
 menu : quit & save -> quit
 */