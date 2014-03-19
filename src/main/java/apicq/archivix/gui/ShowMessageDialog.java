package apicq.archivix.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Created by pic on 2/13/14.
 */
public class ShowMessageDialog extends JDialog {

    public static final Logger log = Logger.getLogger("Archivix");
    public static final String SEP = System.getProperty("line.separator");
    private final MainFrame mainFrame ;


    /**
     * JLabel with red color
     */
    class CustomJLabel extends JLabel {
        public CustomJLabel(String name){
            super(name);
            setForeground(Color.RED);
        }
    }

    public ShowMessageDialog(MainFrame mainFrame,TextMessage me){

        this.mainFrame = mainFrame;
        setLayout(new MigLayout());
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Dimension to limit dialog size :
        Dimension screenDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int dimX = (int) (screenDim.getWidth()*0.7);
        int dimY = (int) (screenDim.getHeight()*0.8);


        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new MigLayout("","[right][fill,grow]",""));// layout,column,row

        // Line 1
        messagePanel.add(new CustomJLabel("Date :"),"");
        messagePanel.add(new JLabel(me.date()),"wrap");

        // Line 2
        messagePanel.add(new CustomJLabel("Sujet :"),"");

        JTextArea subjectTextArea = new JTextArea(me.subject());
        subjectTextArea.setEditable(false);
        subjectTextArea.setBorder(new LineBorder(Color.BLACK));
        subjectTextArea.setBackground(Color.YELLOW);
        subjectTextArea.setLineWrap(true);
        messagePanel.add(subjectTextArea,"wmax "+dimX+",grow,wrap");

        // From :
        messagePanel.add(new CustomJLabel("De :"),"");
        messagePanel.add(new JLabel(me.author()),"grow,wrap");

        // Recipients
        messagePanel.add(new CustomJLabel("A :"),"");
        JTextArea recipTextArea = new JTextArea(me.recip());
        recipTextArea.setEditable(false);
        recipTextArea.setBorder(new LineBorder(Color.BLACK));
        recipTextArea.setLineWrap(true);
        messagePanel.add(recipTextArea,"wmax "+dimX+",grow,wrap");


        // All recipients :
        messagePanel.add(new CustomJLabel("Tous les destinataires :"),"");
        JTextArea mailRecipTextArea = new JTextArea(me.mailrecip());
        mailRecipTextArea.setEditable(false);
        mailRecipTextArea.setBorder(new LineBorder(Color.BLACK));
        mailRecipTextArea.setLineWrap(true);
        messagePanel.add(mailRecipTextArea,"wmax "+dimX+",grow,wrap");

        // Attachments :
        messagePanel.add(new CustomJLabel("Pièces jointes :"),"");

        JTextArea attachTextArea = new JTextArea();
        attachTextArea.setBorder(new LineBorder(Color.blue));
        Iterator<AttachmentSignature> asIterator = me.attachmentSignatures().iterator();
        while(asIterator.hasNext()){
            attachTextArea.append(asIterator.next().getName());
            if(asIterator.hasNext()) attachTextArea.append(SEP);
        }
        /*
        for(AttachmentSignature as : me.attachmentSignatures()){
            attachTextArea.append(as.getName()+SEP);
        }
        */
        attachTextArea.setEditable(false);
        attachTextArea.setLineWrap(true);
        messagePanel.add(attachTextArea,"grow,span");

        // ------
        // Body :
        // ------
        JTextArea bodyTextArea = new JTextArea(me.body());
        bodyTextArea.setBorder(new LineBorder(Color.RED));
        bodyTextArea.setEditable(false);
        bodyTextArea.setLineWrap(true);
        bodyTextArea.setWrapStyleWord(true);
        messagePanel.add(bodyTextArea,"grow,span");


        JScrollPane mainScrollPane = new JScrollPane(messagePanel);
        add(mainScrollPane,"width "+dimX+" ,height "+dimY+",span 2,wrap");


        JButton reportButton = new JButton("Sauvegarder le message et les pièces jointes");
        reportButton.setActionCommand("createReportAction");
        reportButton.addActionListener(mainFrame);

        add(reportButton, "left");

        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(quitButton, "right");
        pack();
        setResizable(false);
    }
}
