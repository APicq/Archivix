package apicq.archivix;

import apicq.archivix.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by pic on 1/27/14.
 */
public class Main {


    public static void main(String args[]){
        System.out.println("Start..");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}
