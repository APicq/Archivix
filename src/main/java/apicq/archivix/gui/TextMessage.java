package apicq.archivix.gui;

import java.util.ArrayList;

/**
 * a TextMessage class is a message without full attachment content.
 * The only reference to attachments is their name and md5 digest,
 * used to retrieve their original attachment.
 *
 */
public class TextMessage {

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
    private ArrayList<AttachmentSignature> attachmentSignatures;

    // Getters :

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

    /**
     * Constructor
     * @param id
     * @param date
     * @param author
     * @param subject
     * @param recip
     * @param body
     * @param attach
     * @param mailrecip
     * @param cc
     * @param bcc
     * @param username
     * @param insertDate
     * @param tags
     * @param attachmentSignatures
     */
    public TextMessage(
            int id,
            String date,
            String author,
            String subject,
            String recip,
            String body,
            int attach,
            String mailrecip,
            String cc,
            String bcc,
            String username,
            String insertDate,
            ArrayList<String> tags,
            ArrayList<AttachmentSignature> attachmentSignatures){

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
        this.attachmentSignatures = attachmentSignatures ;
    }
}
