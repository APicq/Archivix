package apicq.archivix.gui;

import java.util.ArrayList;

/**
 * Created by pic on 2/10/14.
 */
public class MessageElement {

    class ArchivedMessage {

        private final String name ;
        private final String md5 ;

        public String getName() {
            return name;
        }

        public String getMd5() {
            return md5;
        }

        public ArchivedMessage(String name,String md5){
            this.name = name ;
            this.md5 = md5 ;
        }
    } // class

    //where i am : add list of attach + list of md5sum

    private int id ;
    private String date ;
    private String author ;

    private String subject ;

    private String recip ;

    private String body ;

    private int attach;
    private String mailrecip ;
    private String cc ;

    private String bcc ;
    private String username ;
    private String insertDate ;

    private ArrayList<String> tags ;
    private ArrayList<ArchivedMessage> archivedMessages ;

    public String username(){ return username;}
    public int attach() { return  attach;}
    public String date() { return date ;}
    public int id() { return id;}
    public String author() { return author;}

    public String subject() { return subject;}
    public String recip() { return recip;}
    public String body() { return body;}
    public String mailrecip() {return  mailrecip;}
    public String cc() {return cc;}
    public String bcc() { return bcc;}
    public String insertDate() { return insertDate;}
    public  ArrayList<String> tags() { return tags ;}

    public MessageElement(
            int id,
            String date,
            String author,
            String subject,
            String recip ,
            String body ,
            int attach ,
            String mailrecip ,
            String cc ,
            String bcc ,
            String username ,
            String insertDate,
            ArrayList<String> tags,
            ArrayList<ArchivedMessage> archivedMessages){

        this.id = id ;
        this.date = date ;
        this.author = author ;
        this.subject = subject ;
        this.recip = recip ;
        this.body = body ;
        this.attach = attach ;
        this.mailrecip = mailrecip ;
        this.cc = cc ;
        this.bcc = bcc ;
        this.username = username ;
        this.insertDate = insertDate ;
        this.tags = tags ;
        this.archivedMessages = archivedMessages ;
    }
}
