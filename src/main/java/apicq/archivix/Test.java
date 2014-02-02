package apicq.archivix;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by pic on 2/2/14.
 */
public class Test {

    public static Logger log = Logger.getLogger("Tester");
    public Test(){
    }


    public void test(){
        log.info(""+getClass().getClassLoader());
        Locale currentLocale = Locale.getDefault();
        log.info(""+currentLocale);
        //ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle");
        ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
        log.info(messages.getString("message1"));
    }

    public static void main(String[] args){
        new Test().test();
    }
}
