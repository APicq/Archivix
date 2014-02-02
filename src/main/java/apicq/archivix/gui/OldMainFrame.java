package apicq.archivix.gui;

import javax.swing.*;

/**
 * Created by pic on 1/28/14.
 */
public class OldMainFrame extends JFrame {

    // configuration panel, place into tab.
    private ConfigPane configPane ;
    private InsertMessagePane insertMessagePane ;

    public OldMainFrame(){

        configPane = new ConfigPane();
        insertMessagePane = new InsertMessagePane();

        setTitle("-- Archivix --");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Config",configPane);
        tabbedPane.add("Insertion",insertMessagePane);
        add(tabbedPane);
        pack();
        setMinimumSize(getPreferredSize());

    }


}
