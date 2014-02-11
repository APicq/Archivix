package apicq.archivix.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by pic on 2/10/14.
 */
public class MessageTableModel extends AbstractTableModel {

    private ArrayList<MessageElement> messages ;

    private static final int IDCOL           = 0;
    private static final int DATECOL         = 1;
    private static final int AUTHORCOL       = 2;
    private static final int SUBJECTCOL      = 3;
    private static final int RECIPCOL        = 4;
    private static final int BODYCOL         = 5;
    private static final int ATTACHCOL       = 6;
    private static final int MAILRECIPCOL    = 7;
    private static final int CCCOL           = 8;
    private static final int BCCCOL          = 9;
    private static final int USERNAMECOL     = 10;
    private static final int INSERTDATECOL   = 11;
    private static final int SUMCOL          = 12; // number of columns

    /**
     * Constructor
     */
    public MessageTableModel() {
        messages = new ArrayList<MessageElement>();
    }

    /**
     * Add one element in table
     * @param me
     */
    public void add(MessageElement me){
        if(me!=null) messages.add(me);
    }


    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return messages.get(rowIndex).date();
        }
        if(columnIndex==1){
            return messages.get(rowIndex).author();
        }
        if(columnIndex==2){
            return messages.get(rowIndex).subject();
        }
        return null ;
    }
}
