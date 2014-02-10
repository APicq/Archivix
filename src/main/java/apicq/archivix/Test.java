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

        String SEP = System.getProperty("line.separator");
        MAPIMessage msg= new MAPIMessage("/home/pic/a_effacer.msg");
        BufferedWriter bw = new BufferedWriter(new FileWriter("/home/pic/test2.txt"));
        String s1 = msg.getRtfBody();
        bw.write(s1+SEP+"===================================================================="+SEP);
        //s1 = s1.replaceAll( "" + (char)(0x0d) + (char)(0x0a) + ".{0,1}" + (char)(0x0d) + (char)(0x0a),SEP );

        s1 = s1.replaceAll( "" + (char)(0x0d) , "" );
        s1 = s1.replaceAll( "" + (char)(0x0a)+"+" + ".{0,1}" + (char)(0x0a)+"+" , SEP);
        bw.write(s1);
        bw.close();
    }

    public static void main(String[] args){
        try {
            new Test().test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
