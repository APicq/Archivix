package apicq.archivix.gui;

import apicq.archivix.tools.SpecializedWorker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by pic on 2/10/14.
 */
public class MessageTableModel extends AbstractTableModel {

    public static final int IDCOL           = 0;
    public static final int DATECOL         = 1;
    public static final int AUTHORCOL       = 2;
    public static final int SUBJECTCOL      = 3;
    public static final int RECIPCOL        = 4;
    public static final int BODYCOL         = 5;
    public static final int ATTACHCOL       = 6;
    public static final int MAILRECIPCOL    = 7;
    public static final int CCCOL           = 8;
    public static final int BCCCOL          = 9;
    public static final int USERNAMECOL     = 10;
    public static final int INSERTDATECOL   = 11;
    public static final int TAGSCOL         = 12;
    public static final int LINENUMBERCOL = 13;

    public static final int SUMCOL          = 14; // number of columns

    // messageElement container
    private ArrayList<TextMessage> messages ;

    /**
     * Getter
     * @param index
     * @return
     */
    public TextMessage get(int index){
        if(index <0 || index>=messages.size()) return null ;
        return messages.get(index);
    }



    /**
     * Constructor
     */
    public MessageTableModel() {
        messages = new ArrayList<TextMessage>();
    }

    /**
     * Add one element in table
     * @param me
     */
    public void add(TextMessage me){
        if(me!=null) messages.add(me);
    }


    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public int getColumnCount() {
        return SUMCOL;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case IDCOL :
                return messages.get(rowIndex).id();
            case DATECOL :
                return messages.get(rowIndex).date();
            case AUTHORCOL :
                return messages.get(rowIndex).author();
            case SUBJECTCOL :
                return messages.get(rowIndex).subject();
            case RECIPCOL :
                return messages.get(rowIndex).recip();
            case BODYCOL :
                return messages.get(rowIndex).body();
            case ATTACHCOL :
                return messages.get(rowIndex).attach();
            case MAILRECIPCOL :
                return messages.get(rowIndex).mailrecip();
            case CCCOL :
                return messages.get(rowIndex).cc();
            case BCCCOL :
                return messages.get(rowIndex).bcc();
            case USERNAMECOL :
                return messages.get(rowIndex).username();
            case INSERTDATECOL :
                return messages.get(rowIndex).insertDate();
            case TAGSCOL :
                String[] tagArray = new String[messages.get(rowIndex).tags().size()];
                for( int i=0 ; i<messages.get(rowIndex).tags().size() ; i++){
                    tagArray[i]=messages.get(rowIndex).tags().get(i);
                }
                return SpecializedWorker.stringify("", "", " || ", tagArray);
            case LINENUMBERCOL:
                return ""+rowIndex ;

            default :
                return "ERROR" ;
        } //switch
    } // get
} // class
