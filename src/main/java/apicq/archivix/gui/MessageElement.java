package apicq.archivix.gui;

/**
 * Created by pic on 2/10/14.
 */
public class MessageElement {

    private String date ;
    public String date() { return date ;}
    private String author ;
    public String author() { return author;}
    private String subject ;
    public String subject() { return subject;}

    // private String body ;

    public MessageElement(
            String date,
            String author,
            String subject){
        this.date = date ;
        this.author = author ;
        this.subject = subject ;
    }

}
