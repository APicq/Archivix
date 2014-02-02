package apicq.archivix.gui;

import javax.swing.*;
import java.awt.*;

/**
 * todo : delete.
 */
public class InsertMessagePane extends JPanel {

    private JButton scanMsgButton ;
    private JButton insertAllMsgButton ;

    public InsertMessagePane() {
        super();
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        scanMsgButton = new JButton("Scan messages");
        insertAllMsgButton = new JButton("Insert all messages");

        // line 1
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1,BoxLayout.LINE_AXIS));
        panel1.add(scanMsgButton);
        panel1.add(Box.createRigidArea(new Dimension(10,0)));
        panel1.add(insertAllMsgButton);
        panel1.add(Box.createHorizontalGlue());

        // Line assembly into column
        add(panel1);
    }
}
