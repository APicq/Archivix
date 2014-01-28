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

        public ReportWorkerDialog(JFrame myFrame, String name){

            super(myFrame);
            setTitle(name);


            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setText("Wait..");
            textArea.setColumns(40);
            textArea.setRows(30);

            JButton OKButton = new JButton("OK");
            JButton CancelButton = new JButton("Cancel");

            JPanel vertPanel = new JPanel();
            vertPanel.setBorder(new EmptyBorder(10,10,10,10));
            vertPanel.setLayout(new BoxLayout(vertPanel, BoxLayout.PAGE_AXIS));
            //vertPanel.add(textArea);

            JScrollPane scrollPane = new JScrollPane(textArea);
            vertPanel.add(scrollPane);

            JPanel panel = new JPanel();
            BoxLayout layout = new BoxLayout(panel,BoxLayout.LINE_AXIS);
            //panel.setLayout(layout);
            panel.add(OKButton);

            panel.add(CancelButton);
            panel.add(Box.createHorizontalGlue());

            panel.setMaximumSize(panel.getPreferredSize());

            vertPanel.add(panel);

            add(vertPanel);

            pack();
        }
    }

    private ReportWorkerDialog reportWorkerDialog ;

    //private GlobalWorkerDialog dialog;

    @Override
    protected void done() {
        super.done();
        //dialog.dispose();
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
        super.process(chunks);
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
