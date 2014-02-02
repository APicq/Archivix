package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import org.apache.poi.hsmf.MAPIMessage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

/**
 * Created by pic on 2/2/14.
 */
public class InsertMessageWorker extends SwingWorker<Boolean,String> {

    public static final Logger log = Logger.getLogger("Archivix");

    private File[] messageFiles ;
    private ProgressMonitor progressMonitor ;

    public InsertMessageWorker(MainFrame mainFrame){
    JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        if(fileChooser.showOpenDialog(mainFrame)==JFileChooser.APPROVE_OPTION){
            messageFiles = fileChooser.getSelectedFiles();
            progressMonitor = new ProgressMonitor(mainFrame,
                    "Insertion des messages dans la base de données",
                    "",0,messageFiles.length);
            execute();
        }

    }

    @Override
    protected Boolean doInBackground() throws Exception {

        if(DatabaseHandler.check()==false) return  Boolean.FALSE ;
        int index = 0;
         for(File messageFile:messageFiles) {
             Thread.sleep(500);//todo
             log.info(messageFile.toString());
             progressMonitor.setProgress(++index);
             progressMonitor.setNote(""+index+"/"+messageFiles.length+" "+messageFile.getName());
             // parse message
             MAPIMessage mapiMessage = null ;
             try {
             mapiMessage = new MAPIMessage(messageFile.getAbsolutePath());
             }
             catch (IOException e){
                 log.warning(e.toString());
                 continue;
             }
             // check duplicate
             if(!DatabaseHandler.open()) continue ;
             PreparedStatement pStatement = DatabaseHandler.getPStatement("" +
                     "SELECT id from messages where from=? and date=? and subject=? and text=? and to=?");
             if (pStatement == null) continue;
             pStatement.setString();   //todo

             // insert message
             // insert attachments
             if(!DatabaseHandler.close()) continue ;
         }
        return Boolean.FALSE ;
    }
}
