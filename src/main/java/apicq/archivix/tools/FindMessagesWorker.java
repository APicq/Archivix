package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageElement;
import apicq.archivix.gui.MessageTable;
import apicq.archivix.gui.MessageTableModel;

import javax.swing.*;
import java.sql.*;
import java.util.logging.Logger;

/**
 * Created by pic on 2/10/14.
 */
public class FindMessagesWorker extends SwingWorker<Boolean,String>{

    // log
    public static final Logger log = Logger.getLogger("Archivix");

    // Reference to all components
    private final MainFrame mainFrame;

    public FindMessagesWorker(MainFrame mainFrame) {
        this.mainFrame = mainFrame ;

        //todo create progress dialog
    }

    @Override
    protected Boolean doInBackground() throws Exception {

        try {

            mainFrame.pConnection().init();
            String sqlRequest =  "select id,date,author,subject,body " +
                    "from messages ";
            // rest of string :
            String searchWords = mainFrame.searchPanel().searchWordsTextField().getText();
            String[] wordsTofind = searchWords.split(" +");
            for( int x=0 ; x<wordsTofind.length ; x++ ){
                if(x==0){
                    sqlRequest = sqlRequest.concat("where body like '%");
                }
                sqlRequest = sqlRequest.concat(wordsTofind[x]+"%");
                if(x==wordsTofind.length-1){
                    sqlRequest = sqlRequest.concat("'");
                }
            }
            log.info(sqlRequest);
            PreparedStatement pStatement = mainFrame.pConnection().
                    prepareStatement(sqlRequest);
            ResultSet rs = pStatement.executeQuery();

            MessageTableModel messageTableModel = new MessageTableModel();


            while(rs.next()){
                MessageElement me = new MessageElement(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
                messageTableModel.add(me);
            }

            mainFrame.messageTable().setModel(messageTableModel);
            mainFrame.invalidate();

        }
        catch(SQLException e){   // no connection : abort
            log.warning(e.toString());
            return Boolean.FALSE;
        }

        return null;
    }
}
