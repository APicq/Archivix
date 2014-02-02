package apicq.archivix.gui;

import apicq.archivix.tools.DatabaseHandler;
import apicq.archivix.tools.InsertMessageWorker;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

/**
 * todo delete
 */
public class MainFrame extends JFrame {

    //private File dabataseFile ;
    private File AttachmentDirectory;

    public MainFrame(){

        // Disable renaming in file chooser :
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        setLayout(new MigLayout("", "[grow,fill]", "[][grow]"));
        setTitle("-- Archivix --");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fichier");
        JMenuItem insertMessageItem = new JMenuItem("Insert message files");
        fileMenu.add(insertMessageItem);
        JMenuItem databaseChooseItem = new JMenuItem("Connecter la base de données");
        fileMenu.add(databaseChooseItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Components
        //JLabel databaseNameLabel = new JLabel("No database selected");
        //add(databaseNameLabel,"wrap");
        SearchPanel searchPanel = new SearchPanel();
        add(searchPanel,"wrap");
        String[] someElemStrings = new String[3];
        someElemStrings[0]="aaa";
        someElemStrings[1]="bbb";
        someElemStrings[2]="ccc";
        MessageJList messageJList = new MessageJList(someElemStrings);
        add(messageJList,"grow");
        // Finalize
        pack();
        setMinimumSize(getPreferredSize());

        // Actions
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo : save some datas
                System.exit(0);
            }
        });

        // Choose msg outlook files and insert them into database
        insertMessageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new InsertMessageWorker(MainFrame.this);
            }
        });
        databaseChooseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser chooser = new JFileChooser();
                int returnValue = chooser.showOpenDialog(MainFrame.this);
                if(returnValue==JFileChooser.APPROVE_OPTION){
                    DatabaseHandler.setDatabaseFullPathName(chooser.getSelectedFile().getAbsolutePath());
                    MainFrame.this.setTitle(chooser.getSelectedFile().getName());
                    //todo update search,show first result.
                }
            }
        });


    }

    public static void main(String[] args){
        DatabaseHandler.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }

        });
    }


}