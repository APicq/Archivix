package apicq.archivix.tools;

import apicq.archivix.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by pic on 2/19/14.
 */
public class NewFindMessagesWorker extends SpecializedWorker {



    /**
     * Constructor
     *
     * @param mainFrame
     * @param subject
     */
    public NewFindMessagesWorker(MainFrame mainFrame, String subject) {
        super(mainFrame, subject);
    }

    @Override
    protected Void doInBackground() throws Exception {


        // column name for searhing words :

        String fieldToSearch="" ;

        if(mainFrame.getSearchPanel().getFieldComboBox().equals("corps")){
            fieldToSearch = "body";
        }
        if(mainFrame.getSearchPanel().getFieldComboBox().equals("sujet")){
            fieldToSearch = "subjet";
        }
        if(mainFrame.getSearchPanel().getFieldComboBox().equals("destinataires")){
            fieldToSearch = "mailrecip";
        }
        if(mainFrame.getSearchPanel().getFieldComboBox().equals("auteur")){
            fieldToSearch = "author";
        }

        log.warning("fieldToSearch " + fieldToSearch);


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

        try {
            String sqlFindString = buildMessageRequest(
                    mainFrame.getSearchPanel().searchWordsTextField().getText(),//words to search
                    fieldToSearch,// column name
                    mainFrame.getSearchPanel().isOnlyUntagged(),// only tagged message
                    tagArray, // tags to search for
                    mainFrame.getSearchPanel().isPerUserSelection(),
                    mainFrame.getSearchPanel().getUserName());


//where i am
            );
        } catch (SQLException e){

        }
        return null ;
    }


    /**
     * method used to build sql strings.
     * @param prefix  prefix
     * @param suffix suffix
     * @param separator separator
     * @param args args
     * @return a string
     */
    public static String stringify(String prefix,
                                   String suffix,
                                   String separator,
                                   String... args){
        if(args==null) return "";
        StringBuilder sb = new StringBuilder();
        boolean isSecond = false ;
        for(String arg:args){
            String trimmedArg = arg.trim();
            if(trimmedArg.length()>0) {
                if(isSecond) {
                    sb.append(separator+prefix+trimmedArg+suffix);
                }
                else {
                    isSecond = true ;
                    sb.append(prefix+trimmedArg+suffix);
                }
            }
        }
        return sb.toString();
    }


    /**
     *
     * @param wordsTofind   words in message, ex : "meeting London"
     * @param fieldForWords  field to search for words : body, subject, author...
     * @param unTagged if true, search only in non-tagged messages
     * @param tags // String[] of tags
     * @param perUserSelection // true if search for one particular username
     * @param userName // username to search for
     * @return // sql string
     */
    private  static String buildMessageRequest(String wordsTofind, // words in message, ex : "meeting London"
                                               String fieldForWords,// field to seach for words
                                               boolean unTagged,
                                               String[] tags,
                                               boolean perUserSelection,
                                               String userName){
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
        System.out.println("part1 "+part1);

        // -----------------
        // Words selection :
        // -----------------
        String part2 = "";
        if(wordsTofind!=null && wordsTofind.length()>0){
            part2 = stringify(fieldForWords+" like '%","%'"," and ",wordsTofind.split(" +"));
        }
        System.out.println("part2 "+part2);

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
                part3 = " id in (select msgid from tags where tag in("+tagString+")";
            }else {
                part3="";
            }
        }
        System.out.println("part3 "+part3);

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

        return part1234;
    }

}
