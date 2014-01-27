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

    private static final Logger logger = Logger.getLogger("logger");
    //private GlobalWorkerDialog dialog;

    @Override
    protected void done() {
        super.done();
        //dialog.dispose();
    }

    /**
     * Constructor
     */
    public ReportWorker(){
        // dialog = new GlobalWorkerDialog();
    }


    public void doTheJob(){
        execute();
    //    dialog.pack();
    //    dialog.setVisible(true);
    }


    @Override
    protected Boolean doInBackground() throws Exception {
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        super.process(chunks);
    }
}
