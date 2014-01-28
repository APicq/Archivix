package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            }
        });
    }


}
