package apicq.archivix.tools;

import javax.swing.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by pic on 1/27/14.
 * A specialized worker. Prints a dialog with informations.Needs to be subclassed.
 *

 */
public class ReportWorker extends SwingWorker<Boolean,String> {







    private class ReportWorkerDialog extends JDialog {

        public ReportWorkerDialog(JFrame myFrame){

            super(myFrame);


            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setText("Wait..");

            JButton OKButton = new JButton("OK");
            JButton CancelButton = new JButton("Cancel");

            JPanel vertPanel = new JPanel();
            vertPanel.setLayout(new BoxLayout(vertPanel,BoxLayout.PAGE_AXIS));
            vertPanel.add(textArea);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.LINE_AXIS));
            panel.add(OKButton);
            panel.add(CancelButton);

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
    public ReportWorker(JFrame myFrame){
        reportWorkerDialog = new ReportWorkerDialog(myFrame);

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
        ReportWorker worker = new ReportWorker(frame);
        worker.doTheJob();


    }
}
