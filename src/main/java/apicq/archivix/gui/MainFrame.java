package apicq.archivix.gui;

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

    // Full path to sqlite database :
    private String dabataseFile;
    public String databaseFile() {return dabataseFile;}

    // Full path to attachment directory
    private String attachmentDirectory;
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

        // Load configuration :
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("config.txt"));
            dabataseFile = prop.getProperty("database");
            setTitle(dabataseFile);
            attachmentDirectory = prop.getProperty("attachmentdir");




        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Aucune base de données configurée. Connectez-vous\n"+
                    "à une base de données et choisissez un répertoire pour les pièces jointes");
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        // Load visibility :
/*
        String visibleColumn = prop.getProperty("idcol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"id");
        }
        visibleColumn = prop.getProperty("datecol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"date");
        }
        visibleColumn = prop.getProperty("authorcol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"auteur");
        }
        visibleColumn = prop.getProperty("subjectcol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"sujet");
        }
        visibleColumn = prop.getProperty("recipcol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"destinataires");
        }
        visibleColumn = prop.getProperty("bodycol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"texte");
        }
        visibleColumn = prop.getProperty("attachcol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"nombre de pj");
        }
        visibleColumn = prop.getProperty("mailrecipcol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"destinataires(complet)");
        }
        visibleColumn = prop.getProperty("cccol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"cc");
        }
        visibleColumn = prop.getProperty("bcccol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"bcc");
        }
        visibleColumn = prop.getProperty("usernamecol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"utilisateur");
        }
        visibleColumn = prop.getProperty("insertdatecol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"date insertion");
        }
        visibleColumn = prop.getProperty("tagscol","yes");
        if(visibleColumn.equals("no")){
            VisibleColumnDialog.hideColumn(this,"tags");
        }
*/
        JScrollPane messageListScroller = new JScrollPane(messageTable);
        add(messageListScroller, "grow");

        // Finalize
        pack();
        setMinimumSize(getPreferredSize());

        setVisible(true);
        setLocationRelativeTo(null);

    }// constructor



    /**
     * Program entry point
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            log.warning(e.toString());
            return ;
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                //mainFrame.setVisible(true);
            }
        });
    }

    // todo : delete after,only for debugging
    public void debug() {
        //dabataseFile = "/home/pic/testbase.sqlite";
 //       attachmentDirectory = "/home/pic/attach/";

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
                    "Entrez un nouveau tag :",
                    "Nouveau tag",
                    JOptionPane.INFORMATION_MESSAGE);
            if(newTag!=null && newTag.trim().length()>0){
                new AddNewTagWorker(MainFrame.this,newTag).execute();
            }
        }
        if("modifyTagsAction".equals(e.getActionCommand())){
            new FindTagsWorker(MainFrame.this,false).execute();
        }
        if("insertMessagesAction".equals(e.getActionCommand())){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choisissez un ou des fichiers messages outlook (.msg)");
            fileChooser.setMultiSelectionEnabled(true);
            if(fileChooser.showOpenDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
                File[] messageFiles = fileChooser.getSelectedFiles();
                new InsertMessageWorker(MainFrame.this,messageFiles);

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

            if(VisibleColumnDialog.isHiddenColumn(this,"id")){
                prop.setProperty("idcol","no");
            }
            else {
                prop.setProperty("idcol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"date")){
                prop.setProperty("datecol","no");
            }
            else {
                prop.setProperty("datecol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"auteur")){
                prop.setProperty("authorcol","no");
            }
            else {
                prop.setProperty("authorcol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"sujet")){
                prop.setProperty("subjectcol","no");
            }
            else {
                prop.setProperty("subjectcol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"destinataires")){
                prop.setProperty("recipcol","no");
            }
            else {
                prop.setProperty("recipcol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"texte")){
                prop.setProperty("bodycol","no");
            }
            else {
                prop.setProperty("bodycol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"nombre de pj")){
                prop.setProperty("attachcol","no");
            }
            else {
                prop.setProperty("attachcol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"destinataires(complet)")){
                prop.setProperty("mailrecipcol","no");
            }
            else {
                prop.setProperty("mailrecipcol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"cc")){
                prop.setProperty("cccol","no");
            }
            else {
                prop.setProperty("cccol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"bcc")){
                prop.setProperty("bcccol","no");
            }
            else {
                prop.setProperty("bcccol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"utilisateur")){
                prop.setProperty("usernamecol","no");
            }
            else {
                prop.setProperty("usernamecol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"date insertion")){
                prop.setProperty("insertdatecol","no");
            }
            else {
                prop.setProperty("insertdatecol","yes");
            }
            if(VisibleColumnDialog.isHiddenColumn(this,"tags")){
                prop.setProperty("tagscol","no");
            }
            else {
                prop.setProperty("tagscol","yes");
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
            FindMessagesWorker nfmw = new FindMessagesWorker(MainFrame.this);
            nfmw.start();
        }

        if("selectTagsAction".equals(e.getActionCommand())){
            FindTagsWorker nftw = new FindTagsWorker(MainFrame.this,true);
            nftw.start();
        }
        if("previousAction".equals(e.getActionCommand())){

        }
        if("nextAction".equals(e.getActionCommand())){

        }
        if("findUserAction".equals(e.getActionCommand())){
            FindUserWorker fuw = new FindUserWorker(MainFrame.this);
            fuw.start();
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

    }
}