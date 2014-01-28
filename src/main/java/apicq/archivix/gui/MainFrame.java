package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pic on 1/28/14.
 */
public class MainFrame extends JFrame {

    private ConfigPane configPane ;

    public MainFrame(){

        configPane = new ConfigPane();

        setMinimumSize(new Dimension(100,100));
        setTitle("-- Archivix --");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Config",configPane);
        add(tabbedPane);


    }


}
