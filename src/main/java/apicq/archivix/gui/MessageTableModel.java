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
        return SUMCOL;
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case IDCOL :
                return "id";
            case DATECOL :
                return "date" ;
            case AUTHORCOL :
                return "auteur" ;
            case SUBJECTCOL :
                return "sujet" ;
            case RECIPCOL :
                return "destinataires";
            case BODYCOL :
                return "texte" ;
            case ATTACHCOL :
                return "nombre de pj" ;
            case MAILRECIPCOL :
                return "destinataires(complet)" ;
            case CCCOL :
                return "cc" ;
            case BCCCOL :
                return "bcc" ;
            case USERNAMECOL :
                return "utilisateur" ;
            case INSERTDATECOL :
                return "date insertion" ;
            default:
                return "ERROR" ;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==IDCOL){
            return messages.get(rowIndex).id();
        }
        if(columnIndex==DATECOL){
            return messages.get(rowIndex).date();
        }
        if(columnIndex==AUTHORCOL){
            return messages.get(rowIndex).author();
        }
        if(columnIndex==SUBJECTCOL){
            return messages.get(rowIndex).subject();
        }
        if(columnIndex==RECIPCOL){
            return messages.get(rowIndex).recip();
        }
        if(columnIndex==BODYCOL){
            return messages.get(rowIndex).body();
        }
        if(columnIndex==ATTACHCOL){
            return messages.get(rowIndex).attach();
        }
        if(columnIndex==MAILRECIPCOL){
            return messages.get(rowIndex).mailrecip();
        }
        if(columnIndex==CCCOL){
            return messages.get(rowIndex).cc();
        }
        if(columnIndex==BCCCOL){
            return messages.get(rowIndex).bcc();
        }
        if(columnIndex==USERNAMECOL){
            return messages.get(rowIndex).username();
        }
        if(columnIndex==INSERTDATECOL){
            return messages.get(rowIndex).insertDate();
        }
        return null ;
    }
}
