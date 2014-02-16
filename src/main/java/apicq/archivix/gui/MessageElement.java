package apicq.archivix.gui;

import java.util.ArrayList;

/**
 * Created by pic on 2/10/14.
 */
public class MessageElement {

    private int id ;
    public int id() { return id;}
    private String date ;
    public String date() { return date ;}
    private String author ;
    public String author() { return author;}
    private String subject ;
    public String subject() { return subject;}
    private String recip ;
    public String recip() { return recip;}
    private String body ;
    public String body() { return body;}
    private int attach ;
    public int attach() { return  attach;}
    private String mailrecip ;
    public String mailrecip() {return  mailrecip;}
    private String cc ;
    public String cc() {return cc;}
    private String bcc ;
    public String bcc() { return bcc;}
    private String username ;
    public String username(){ return username;}
    private String insertDate ;
    public String insertDate() { return insertDate;}
    private ArrayList<String> tags ;
    public  ArrayList<String> tags() { return tags ;}



    // private String body ;

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
            ArrayList<String> tags){

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
    }
}
