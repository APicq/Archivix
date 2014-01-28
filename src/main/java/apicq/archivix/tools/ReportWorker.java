package apicq.archivix.tools;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;



// TODO add scrollable

/**
 * Created by pic on 1/27/14.
 * A specialized worker. Prints a dialog with informations.Needs to be subclassed.
 *
 */
public class ReportWorker extends SwingWorker<Boolean,String> {







    private class ReportWorkerDialog extends JDialog {

        public JTextArea getTextArea() {
            return textArea;
        }

        JTextArea textArea ;
        JButton   OKButton ;
        JButton   CancelButton ;

        public ReportWorkerDialog(JFrame myFrame, String name){

            super(myFrame);
            setTitle(name);
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setText("Wait..");
            textArea.setColumns(40);
            textArea.setRows(30);
            OKButton = new JButton("OK");
            CancelButton = new JButton("Cancel");
            JPanel verticalPanel = new JPanel();
            verticalPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.PAGE_AXIS));
            JScrollPane scrollPane = new JScrollPane(textArea);
            verticalPanel.add(scrollPane);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
            buttonPanel.setBackground(Color.YELLOW);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(OKButton);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            buttonPanel.add(CancelButton);
            verticalPanel.add(buttonPanel);
            add(verticalPanel);
            pack();
            setMinimumSize(getPreferredSize());
        }
    }   // class

    private ReportWorkerDialog reportWorkerDialog ;

    @Override
    protected void done() {
        reportWorkerDialog.dispose();
    }

    /**
     * Constructor
     */
    public ReportWorker(JFrame myFrame,String name){
        reportWorkerDialog = new ReportWorkerDialog(myFrame,name);

    }


    public void doTheJob(){
        execute();
        reportWorkerDialog.setVisible(true);
    }


    /**
     * Override this for function
     * @return
     * @throws Exception
     */
    @Override
    protected Boolean doInBackground() throws Exception {
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        for(String chunk:chunks) reportWorkerDialog.getTextArea().append(chunk);
    }

    /**
     * Gui test
     * @param args
     */
    public static void main(String[] args){

        JFrame frame = new JFrame("Test ReportWorkerDialog");
        ReportWorker worker = new ReportWorker(frame,"Worker Dialog");
        worker.doTheJob();


    }
}
