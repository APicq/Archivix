package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by a.picquoin on 05/03/14.
 */
public class SaveDatabaseWorker extends SpecializedWorker {


    private boolean isAlreadySaved = false ;
    private String newName ;

    /**
     * Constructor
     *
     * @param mainFrame
     */
    public SaveDatabaseWorker(MainFrame mainFrame) {
        super(mainFrame, "Sauvegarde de la base en cours");
        setIndeterminate(true);
        setString("Sauvegarde en cours..");
    }

    @Override
    protected Void doInBackground() throws Exception {
        //Build new name :
        File databaseFile = new File(mainFrame.databaseFile());
        String databaseName = databaseFile.getName();
        String nameWithoutExt = "" ;
        String extension = "";
        int extensionIndex  = databaseName.lastIndexOf(".");
        if (extensionIndex == -1) {
            nameWithoutExt = databaseName;
        } else {
            nameWithoutExt = databaseName.substring(0, extensionIndex);
            extension = databaseName.substring(extensionIndex);
        }
        String dateStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date());
        newName = nameWithoutExt+"_"+dateStamp+extension;
        log.info(newName);
        File newDatabaseFile = new File(databaseFile.getParent(),newName);
        if(newDatabaseFile.exists()){
            isAlreadySaved = true ;
        } else {
            try {
                Files.copy(databaseFile.toPath(),newDatabaseFile.toPath());
            } catch(IOException e){
                addError("Impossible de copier la base de données");
                return null;
            }

        }
        return null ;
    }

    @Override
    protected void done() {
        super.done();
        if(!isError()){
            if(isAlreadySaved){
                JOptionPane.showMessageDialog(mainFrame,"La base de données a déjà été sauvegardée il y moins d'une minute");
            } else {
                JOptionPane.showMessageDialog(mainFrame,"La base de données a été sauvegardée sous le nom : "+newName);
            }
        }
    }
}
