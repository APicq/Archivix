package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pic on 1/28/14.
 */
public class MainFrame extends JFrame {

    // configuration panel, place into tab.
    private ConfigPane configPane ;
    private InsertMessagePane insertMessagePane ;

    public MainFrame(){

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
