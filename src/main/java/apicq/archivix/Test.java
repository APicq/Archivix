package apicq.archivix;

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


    public void test() throws Exception  {
        MAPIMessage msg = new MAPIMessage("/home/pic/a_effacer.msg");
        System.out.println(msg.getDisplayTo());
        System.out.println(msg.getRecipientEmailAddress());
    }

    public static void main(String[] args){
        try {
            new Test().test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
