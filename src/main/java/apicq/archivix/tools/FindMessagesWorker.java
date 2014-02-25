package apicq.archivix.tools;

import apicq.archivix.gui.AttachmentSignature;
import apicq.archivix.gui.MainFrame;
import apicq.archivix.gui.MessageTableModel;
import apicq.archivix.gui.TextMessage;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by pic on 2/19/14.
 */
public class FindMessagesWorker extends SpecializedWorker {

    private final MessageTableModel messageTableModel = new MessageTableModel();//debug

    /**
     * Constructor
     *
     * @param mainFrame
     */
    public FindMessagesWorker(MainFrame mainFrame) {
        super(mainFrame, "Recherche des messages en cours");
    }



    @Override
    protected Void doInBackground() throws Exception {

        // column name for searching words : todo : change

        String fieldToSearch = (String) mainFrame.getSearchPanel().getFieldComboBox().getSelectedItem();

        if(fieldToSearch.equals("corps")){
            fieldToSearch = "body";
        }
        if(fieldToSearch.equals("sujet")){
            fieldToSearch = "subjet";
        }
        if(fieldToSearch.equals("destinataires")){
            fieldToSearch = "mailrecip";
        }
        if(fieldToSearch.equals("auteur")){
            fieldToSearch = "author";
        }

        log.warning("fieldToSearch:getselecteditem :"+mainFrame.getSearchPanel().getFieldComboBox().getSelectedItem());
        log.warning("fieldToSearch =" + fieldToSearch);

        // search by tags : prepare array of tags

        Vector<String> tagVector = new Vector<String>();
        for(Component c : mainFrame.getSearchPanel().getSelectedTagsPanel().getComponents()){
            JLabel jLabel = (JLabel) c;
            tagVector.add(((JLabel) c).getText());
        }
        String[] tagArray = new String[tagVector.size()];
        for( int index=0 ; index <tagVector.size() ; index++ ){
            tagArray[index]=tagVector.get(index);
        }

        int limit = Integer.parseInt(mainFrame.getSearchPanel().getMaxResultNumberField().getText());
        int offset = mainFrame.getSearchPanel().getPageNumber() * limit ;

        // Build request :

        String sqlFindString = buildMessageRequest(
                mainFrame.getSearchPanel().searchWordsTextField().getText(),//words to search
                fieldToSearch,// column name
                mainFrame.getSearchPanel().isOnlyUntagged(),// only tagged message
                tagArray, // tags to search for
                mainFrame.getSearchPanel().isPerUserSelection(),
                mainFrame.getSearchPanel().getUserName(),
                limit, // limit
                offset); // offset
        try {
            PreparedStatement pstmt = pStatement(sqlFindString);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){

                // pick up tags
                PreparedStatement tagsStatement =
                        pStatement("SELECT tag FROM tags where msgid=?");
                tagsStatement.setInt(1,rs.getInt(1));
                ResultSet tagsResultSet = tagsStatement.executeQuery();
                ArrayList<String> tags = new ArrayList<String>();
                while(tagsResultSet.next()) tags.add(tagsResultSet.getString(1));

                // pick up attachments :
                PreparedStatement attachStatement =
                        pStatement("SELECT name,md5sum FROM attach WHERE msgid=?");
                attachStatement.setInt(1,rs.getInt(1));
                ResultSet attachResultSet = attachStatement.executeQuery();
                ArrayList<AttachmentSignature> attachmentSignatures = new ArrayList<AttachmentSignature>();
                while(attachResultSet.next()){
                    attachmentSignatures.add(
                            new AttachmentSignature(
                                    attachResultSet.getString(1),
                                    attachResultSet.getString(2)));
                }


                TextMessage me = new TextMessage(
                        rs.getInt(1),       // id
                        rs.getString(2),    // date
                        rs.getString(3),    // author
                        rs.getString(4),    // subject
                        rs.getString(5),    // recip
                        rs.getString(6),    // body
                        rs.getInt(7),       // attach
                        rs.getString(8),    // mailrecip
                        rs.getString(9),    // cc
                        rs.getString(10),   // bcc
                        rs.getString(10),   // username
                        rs.getString(11),   // insertdate
                        tags,               // tags
                        attachmentSignatures);// attachments

                messageTableModel.add(me);
            }
        }
        catch (SQLException e){
            addError(e.toString());
        }
        return null ;
    }


    @Override
    protected void done() {
        super.done();
        mainFrame.getMessageTable().setModel(messageTableModel);
        mainFrame.invalidate();
    }




    /**
     *
     * @param wordsTofind   words in message, ex : "meeting London"
     * @param fieldForWords  field to search for words : body, subject, author...
     * @param unTagged if true, search only in non-tagged messages
     * @param tags // String[] of tags
     * @param perUserSelection // true if search for one particular username
     * @param userName // username to search for
     * @param limit
     * @param offset
     * @return // sql string
     */
    private String buildMessageRequest(String wordsTofind, // words in message, ex : "meeting London"
                                               String fieldForWords,// field to seach for words
                                               boolean unTagged,
                                               String[] tags,
                                               boolean perUserSelection,
                                               String userName,
                                               int limit,
                                               int offset){
        // -------
        // Header
        // -------
        String part1 =  "SELECT "+
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
                "FROM messages ";

        // -----------------
        // Words selection :
        // -----------------
        String part2 = "";
        if(wordsTofind!=null && wordsTofind.length()>0){
            part2 = stringify(fieldForWords+" like '%","%'"," and ",wordsTofind.split(" +"));
        }

        //----------------
        // tag management :
        //----------------
        String part3 = "";
        if(unTagged) {
            part3 = " id NOT IN (SELECT DISTINCT msgid FROM tags)";
        }
        else {
            String tagString = stringify("'","'",",",tags);
            if(tagString.length()>0){
                part3 = " id in (select msgid from tags where tag in("+tagString+"))";
            }else {
                part3="";
            }
        }

        // -----------------
        // user management :
        // -----------------
        String part4="";
        if(perUserSelection && userName!=null && userName.length()>0){
            part4=" userName="+"'"+userName+"'";
        }
        System.out.println("part4 "+part4);

        String part234 = stringify("",""," AND ",part2,part3,part4);
        System.out.println("part234 : "+part234);

        String part1234 = stringify("",""," WHERE ",part1,part234);
        System.out.println("part1234 : "+part1234);

        String part5 = " LIMIT " + limit + " OFFSET " + offset  ;

        return part1234;
    }

}
