package apicq.archivix.gui;

import apicq.archivix.tools.ProtectedConnection;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by pic on 2/10/14.
 */
public class MessageTableModel extends AbstractTableModel {

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
    private static final int TAGSCOL         = 12;

    private static final int SUMCOL          = 13; // number of columns

    // messageElement container
    private ArrayList<MessageElement> messages ;

    /**
     * Getter
     * @param index
     * @return
     */
    public MessageElement get(int index){
        if(index <0 || index>=messages.size()) return null ;
        return messages.get(index);
    }



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
            case TAGSCOL :
                return "tags" ;
            default:
                return "ERROR" ;
        }
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
                return ProtectedConnection.merge(
                        messages.get(rowIndex).tags(),"||");
            default :
                return "ERROR" ;
            } //switch
    } // get
} // class
