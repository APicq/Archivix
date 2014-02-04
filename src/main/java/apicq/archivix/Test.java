package apicq.archivix;

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


    public void test(){
        Calendar c = Calendar.getInstance();
        //c.add(Calendar.DATE,1);
        log.info(""+c.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info(""+format.format(c.getTime()));
    }

    public static void main(String[] args){
        new Test().test();
    }
}
