package apicq.archivix.gui;

import apicq.archivix.tools.FindMessagesWorker;
import apicq.archivix.tools.InitBaseWorker;
import apicq.archivix.tools.InsertMessageWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * todo delete
 */
public class MainFrame extends JFrame {

    // Full path to sqlite database :
    private String dabataseFile;

    // Full path to attachment directory
    private String attachmentDirectory;

    // JTable to print messages :
    private final MessageTable messageTable ;

    public MessageTable messageTable(){ return messageTable ;}

    // Panel with buttons, to search elements
    private final SearchPanel searchPanel;

    public SearchPanel searchPanel(){ return searchPanel ;}

    /**
     * Getter
     * @return
     */
    public String databaseFile() {
        return dabataseFile;
    }

    /**
     * Getter
     * @return
     */
    public String attachmentDirectory() {
        return attachmentDirectory;
    }

    public MainFrame() {

        // Disable renaming in file chooser :
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        setLayout(new MigLayout("", "[grow,fill]", "[][grow]"));
        setTitle("-- Archivix --");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setup menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem insertMessageItem = new JMenuItem("Insert message files");
        fileMenu.add(insertMessageItem);
        JMenuItem databaseChooseItem = new JMenuItem("Connecter la base de données");
        fileMenu.add(databaseChooseItem);
        JMenuItem attachDirItem = new JMenuItem("Définir le répertoire des pièces jointes");
        fileMenu.add(attachDirItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Components
        searchPanel = new SearchPanel();
        add(searchPanel, "wrap");

        JScrollPane messageListScroller = new JScrollPane();

        //MessageJList messageJList = new MessageJList();
        messageTable = new MessageTable();
        //messageListScroller.add(messageTable);
        //add(messageListScroller, "grow");
        add(messageTable, "grow");

        // Finalize
        pack();
        setMinimumSize(getPreferredSize());

        // -------
        // Actions
        // -------

        // Quit
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionQuit();
            }
        });

        // select outlook message files and insert them into database
        insertMessageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InsertMessageWorker(MainFrame.this);
            }
        });


        // Connect to a sqlite database
        databaseChooseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionConnectDatabase();
            }
        });

        // Select attachment directory
        attachDirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionSelectAttachDir();
            }
        });

        // Find and print messages
        searchPanel.searchWordsButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionSearchInMessages();
            }
        });
       debug();
    }// constructor


    /**
     * Search in messages
     */
    private void actionSearchInMessages() {
        new FindMessagesWorker(this).execute();

    }

    private void actionQuit() {
        // todo : save some datas
        System.exit(0);
    }

    /**
     * Select directory where all attachment files lie.
     */
    private void actionSelectAttachDir() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = chooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            attachmentDirectory = chooser.getSelectedFile().getAbsolutePath();
        }
    }

    /**
     * Connect to SQlite database
     */
    private void actionConnectDatabase() {
        final JFileChooser chooser = new JFileChooser();
        int returnValue = chooser.showOpenDialog(MainFrame.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            dabataseFile = (chooser.getSelectedFile().getAbsolutePath());
            MainFrame.this.setTitle(chooser.getSelectedFile().getName());
            // init database :
            new InitBaseWorker(MainFrame.this).execute();
            //todo update search,show first result, freeze main frame
        }
    }

    /**
     * Program entry point
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }

    // todo : delete after,only for debugging
    public void debug() {
        dabataseFile = "/home/pic/testbase.sqlite";
        attachmentDirectory = "/home/pic/attach/";
        new InitBaseWorker(this).execute();
        //new InsertMessageWorker(this);
    }


}