package apicq.archivix.gui;

import apicq.archivix.tools.ArchiLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * Created by pic on 1/28/14.
 */
public class MainFrame extends JFrame {

    public MainFrame(){
        setMinimumSize(new Dimension(100,100));
        setTitle("-- Archivix --");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // debug
        JButton OKButton = new JButton("OK");
        getContentPane().add(OKButton);
        OKButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArchiLog.info("OK button");
            }
        });
    }


}
