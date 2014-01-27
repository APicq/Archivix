package apicq.archivix;

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
                JFrame jFrame = new JFrame("Test");
                jFrame.setMinimumSize(new Dimension(100,100));
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.setVisible(true);
            }
        });
    }
}
