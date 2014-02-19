package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

/**
 * Reimplement findMessagesWorker
 */
public class TestSpecializedWorker extends SpecializedWorker  {


    /**
     * @param mainFrame
     */
    public TestSpecializedWorker(MainFrame mainFrame){//} mainFrame, String subject, int max) {
        super(mainFrame,"Recherche des messages", 10);
        getProgressDialog().getProgressBar().setMaximum(100);
    }

    @Override
    protected Void doInBackground() throws Exception {
        setProgress(0);
        for(int i=0 ; i<5 ; i++){
            setProgress(i+20);
            publish("message "+i);
            //log.info(""+i);
            Thread.sleep(1000);
        }
        addError("erreur volontaire");
        addError("erreur volontaire");
        addError("erreur volontaire");
        addError("erreur volontaire");

        return null ;
    }

}
