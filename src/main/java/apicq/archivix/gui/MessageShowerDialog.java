package apicq.archivix.gui;

import javafx.scene.paint.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.DimensionUIResource;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pic on 2/13/14.
 */
public class MessageShowerDialog extends JDialog {

    /**
     * JLabel with red color
     */
    class CustomJLabel extends JLabel {
        public CustomJLabel(String name){
            super(name);
            setForeground(Color.RED);
        }
    }

    public  MessageShowerDialog(MessageElement me){
       // where i am : retrieve attachments
        setModal(true);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new MigLayout("","[right][]",""));// layout,column,row

        // Line 1
        messagePanel.add(new CustomJLabel("Date :"),"");
        messagePanel.add(new JLabel(me.date()),"grow,span");

        // Line 2
        messagePanel.add(new CustomJLabel("Sujet :"),"");
        messagePanel.add(new JLabel(me.subject()),"grow,span");

        // Line 3
        messagePanel.add(new CustomJLabel("De :"),"");
        messagePanel.add(new JLabel(me.author()),"grow,span");

        // Line 4
        messagePanel.add(new CustomJLabel("A :"),"");
        messagePanel.add(new JLabel(me.recip()),"grow,span");

        // Line 5
        messagePanel.add(new CustomJLabel("Tous les destinataires :"),"");
        messagePanel.add(new JLabel(me.mailrecip()),"grow,span");

        // Line 6
        messagePanel.add(new CustomJLabel("Pièces jointes :"),"wrap");
        //messagePanel.add(new JLabel(me.mailrecip()),"grow,span");

        // Line
        JTextArea bodyTextArea = new JTextArea(me.body());
        bodyTextArea.setBorder(new LineBorder(Color.BLACK));
        messagePanel.add(bodyTextArea,"grow,span");

        // Line
        JButton reportButton = new JButton("Sauvegarder le message");
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo
            }
        });
        messagePanel.add(reportButton,"left");

        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        messagePanel.add(quitButton, "left");
        add(new JScrollPane(messagePanel));
        pack();
    }
}
