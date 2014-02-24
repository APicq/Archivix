package apicq.archivix.gui;

import apicq.archivix.tools.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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

        // Disable renaming in file chooser :
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);

        // Misc frame configuration
        setLayout(new MigLayout("", "[grow,fill]", "[][grow]"));
        setTitle("-- Archivix --");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -----------
        // Setup menu
        // -----------
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");

        JMenuItem insertMessageItem = new JMenuItem("Insert message files");
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

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.setActionCommand("quitAction");
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);
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

        //todo : delete after
        debug();
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
        dabataseFile = "/home/pic/testbase.sqlite";
        attachmentDirectory = "/home/pic/attach/";

    }

    /**
     * All actions are dispatched here :
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        log.info(e.getActionCommand());//debug
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
            //todo
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
            // todo : save some datas as properties
            System.exit(0);
        }

        if("findMessagesAction".equals(e.getActionCommand())){
            FindMessagesWorker nfmw = new FindMessagesWorker(MainFrame.this);
            nfmw.start();
        }

        if("selectTagsAction".equals(e.getActionCommand())){
            NewFindTagsWorker nftw = new NewFindTagsWorker(MainFrame.this);
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
        if("".equals(e.getActionCommand())){
        }

    }
}