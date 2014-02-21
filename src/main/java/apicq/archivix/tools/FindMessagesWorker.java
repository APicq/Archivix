package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageElement;
import apicq.archivix.gui.MessageTableModel;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
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
            String sqlRequest =  "select "+
                    "id," +
                    "date," +
                    "author," +
                    "subject," +
                    "recip," +
                    "body," +
                    "attach," +
                    "mailrecip," +
                    "cc," +
                    "bcc," +
                    "username," +
                    "insertdate " +
                    "from messages ";
            // rest of string :
            String searchWords = mainFrame.getSearchPanel().searchWordsTextField().getText();
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
                // retrieve tags :
                PreparedStatement tagsStatement = mainFrame.pConnection().
                        prepareStatement("SELECT tag FROM tags where msgid=?");
                tagsStatement.setInt(1,rs.getInt(1));
                ResultSet tagsResultSet = tagsStatement.executeQuery();
                ArrayList<String> tags = new ArrayList<String>();
                while(tagsResultSet.next()) tags.add(tagsResultSet.getString(1));
                MessageElement me = new MessageElement(
                        rs.getInt(1),// id
                        rs.getString(2),// date
                        rs.getString(3),// author
                        rs.getString(4),// subject
                        rs.getString(5),// recip
                        rs.getString(6),// body
                        rs.getInt(7),// attach
                        rs.getString(8),// mailrecip
                        rs.getString(9),// cc
                        rs.getString(10),// bcc
                        rs.getString(11),// username
                        rs.getString(12),// insertdate
                        tags);
                messageTableModel.add(me);
            }
            mainFrame.getMessageTable().setModel(messageTableModel);
            mainFrame.invalidate();
        }
        catch(SQLException e){   // no connection : abort
            log.warning(e.toString());
            return Boolean.FALSE;
        }
        return null;
    }
}
