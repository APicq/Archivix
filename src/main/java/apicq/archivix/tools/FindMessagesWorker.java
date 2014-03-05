package apicq.archivix.tools;

import apicq.archivix.gui.*;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by pic on 2/19/14.
 */
public class FindMessagesWorker extends SpecializedWorker {

    public static final Logger log = Logger.getLogger("Archivix");
    private final MessageTableModel mtm ;

    /**
     * Constructor
     *
     * @param mainFrame
     */
    public FindMessagesWorker(MainFrame mainFrame) {
        super(mainFrame, "Recherche des messages en cours");
        mtm = new MessageTableModel();
    }



    @Override
    protected Void doInBackground() throws Exception {

        //  where to search for words in body, subject, etc...
        String fieldToSearch = (String) mainFrame.getSearchPanel().getFieldComboBox().getSelectedItem();

        if(fieldToSearch.equals("corps")){
            fieldToSearch = "body";
        }
        if(fieldToSearch.equals("sujet")){
            fieldToSearch = "subject";
        }
        if(fieldToSearch.equals("destinataires")){
            fieldToSearch = "mailrecip";
        }
        if(fieldToSearch.equals("auteur")){
            fieldToSearch = "author";
        }

        // Tag request string :
        // search by tags : prepare array of tags
        ArrayList<String> tagArrayList = new ArrayList<String>();
        for(Component c : mainFrame.getSearchPanel().getSelectedTagsPanel().getComponents()){
            JLabel jLabel = (JLabel) c;
            tagArrayList.add(((JLabel) c).getText());
        }

        // ------------------
        // ordering results :
        // ------------------
        String orderInput = (String) mainFrame.getSearchPanel().getSortComboBox().getSelectedItem();
        String order = "date";
        if(orderInput.equals("date")){
            order = "date";
        }
        if(orderInput.equals("sujet")){
            order = "subject";
        }
        if(orderInput.equals("destinataires")){
            order = "recip";
        }
        if(orderInput.equals("auteur")){
            order = "author";
        }
        if(orderInput.equals("date insertion")){
            order = "insertDate";
        }
        order = order + " DESC ";


        //-----------------
        // limit and offset
        //-----------------
        int limit = 10 ;
        try {
            limit = Integer.parseInt(mainFrame.getSearchPanel().getMaxResultNumberField().getText());
            if(limit<=0) limit = 10 ;
        } catch (NumberFormatException nfe ){
            addError("Erreur champs nombre de résultats maximum par page");
            addError(nfe.toString());
            return null;
        }
        int offset = (SearchPanel.getPageNumber()-1) * limit ;

        // ---------------
        // Build request :
        // ---------------
        String sqlFindString = buildMessageRequest(
                mainFrame.getSearchPanel().searchWordsTextField().getText(),//words to search
                fieldToSearch,// column name
                mainFrame.getSearchPanel().isOnlyUntagged(),// only tagged message
                tagArrayList, // tags to search for
                mainFrame.getSearchPanel().isPerUserSelection(),// user selection on/off
                mainFrame.getSearchPanel().getUserName(), // username
                order, // ordering field
                limit, // limit
                offset); // offset
        log.info("sqlFindString "+sqlFindString);

        try {
            PreparedStatement pstmt = pStatement(sqlFindString);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){

                // pick up tags
                PreparedStatement tagsStatement =
                        pStatement("SELECT tag FROM tags where msgid=? ");
                tagsStatement.setInt(1,rs.getInt(1));
                ResultSet tagsResultSet = tagsStatement.executeQuery();
                ArrayList<String> tags = new ArrayList<String>();
                while(tagsResultSet.next()) tags.add(tagsResultSet.getString(1));

                // pick up attachments :
                PreparedStatement attachStatement =
                        pStatement("SELECT attachref.md5sum,attachref.name FROM" +
                                " attachref,attach " +
                                "WHERE attachref.md5sum=attach.md5sum AND attach.msgid=? ");
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
                        rs.getString(11),   // username
                        rs.getString(12),   // insertdate
                        tags,               // tags
                        attachmentSignatures);// attachments
                mtm.add(me);

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

        if(!isError()){
            mainFrame.getMessageTable().setModel(mtm);
            mainFrame.getMessageTable().revalidate();
            mainFrame.getMessageTable().repaint();
        }
    }




    /**
     *
     * @param wordsTofind   words in message, ex : "meeting London"
     * @param fieldForWords  field to search for words : body, subject, author...
     * @param unTagged if true, search only in non-tagged messages
     * @param tagList // String[] of tags
     * @param perUserSelection // true if search for one particular username
     * @param userName // username to search for
     * @param limit
     * @param offset
     * @return // sql string
     */
    private String buildMessageRequest(String wordsTofind, // words in message, ex : "meeting London"
                                       String fieldForWords,// field to seach for words
                                       boolean unTagged,
                                       ArrayList<String> tagList,
                                       boolean perUserSelection,
                                       String userName,
                                       String order,
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
            part3 = " id NOT IN (SELECT DISTINCT msgid FROM tags) ";
        }
        else {
            if( tagList.size() >0 ){

                // SELECT
                part3 = " id IN(SELECT tags0.msgid FROM ";

                // FROM
                boolean isFirst = true ;
                for(int i=0 ; i<tagList.size() ; i++){
                    if(isFirst){
                        isFirst=false;
                        part3 = part3 + "tags as tags"+i ;
                    }
                    else {
                        part3 = part3 + ",tags as tags"+i ;
                    }
                }

                //WHERE  tag part
                part3 = part3 + " WHERE " ;
                isFirst=true;
                for(int i=0 ; i<tagList.size() ; i++){
                    if(isFirst){
                        isFirst=false;
                        part3=part3+" tags"+i+".tag="+"'"+tagList.get(i)+"'";
                    }else {
                        part3=part3+" AND  tags"+i+".tag="+"'"+tagList.get(i)+"'";
                    }
                }

                // WHERE msgid part
                if ( tagList.size() >1 ){
                    part3 = part3 + " AND " ;
                    isFirst=true;
                    for(int i=0 ; i<tagList.size()-1 ; i++){
                        if(isFirst){
                            isFirst=false;
                            part3 = part3 + "tags"+i+".msgid=tags"+(i+1)+".msgid";
                        }
                        else {
                            part3 = part3 + " AND tags"+i+".msgid=tags"+(i+1)+".msgid";
                        }
                    }
                }

                part3=part3+") ";
            }else { // all tags must be visible
                part3=" ";
            }
        }

        // -----------------
        // user management :
        // -----------------
        String part4="";
        if(perUserSelection && userName!=null && userName.length()>0){
            part4=" userName="+"'"+userName+"'";
        }

        String part234 = stringify("",""," AND ",part2,part3,part4);

        String part1234 = stringify("",""," WHERE ",part1,part234);

        // ----------
        // ordering :
        // ----------
        String part5 = " ORDER BY "+order;


        // -------
        // limit :
        // -------
        String part6 = " LIMIT " + limit + " OFFSET " + offset  ;

        return part1234 + part5 + part6 ;
    }

}
