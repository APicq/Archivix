package apicq.archivix.gui.table;

import apicq.archivix.gui.TextMessage;
import apicq.archivix.tools.SpecializedWorker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *  TableModel for MailTable
 */
public class MailTableModel extends AbstractTableModel {

    public static final Logger log = Logger.getLogger("Archivix");

    // Name of columns
    public static final String IDCOL_NAME         = "ID" ;
    public static final String DATECOL_NAME       = "Date" ;
    public static final String AUTHORCOL_NAME     = "Auteur" ;
    public static final String SUBJECTCOL_NAME    = "Sujet" ;
    public static final String RECIPCOL_NAME      = "Destinataires" ;
    public static final String BODYCOL_NAME       = "Texte" ;
    public static final String ATTACHCOL_NAME     = "pièces jointes" ;
    public static final String MAILRECIPCOL_NAME  = "Tous les destinataires" ;
    public static final String CCCOL_NAME         = "CC" ;
    public static final String BCCCOL_NAME        = "BCC" ;
    public static final String USERNAMECOL_NAME   = "Utilisateur" ;
    public static final String INSERTDATECOL_NAME = "Date d'insertion" ;
    public static final String TAGSCOL_NAME       = "Tags" ;
    public static final String LINENUMBERCOL_NAME = "Ligne" ;

    public static final String[] COL_NAMES = {
            "ID" ,			// 0
            "Date" ,			// 1
            "Auteur" ,			// 2
            "Sujet" ,			// 3
            "Destinataires" ,		// 4
            "Texte" ,			// 5
            "pièces jointes" ,		// 6
            "Tous les destinataires" ,	// 7
            "CC" ,			// 8
            "BCC" ,			// 9
            "Utilisateur" ,		// 10
            "Date d'insertion",		// 11
            "Tags" ,			// 12
            "Ligne"			// 13
    };

    //order of columns
    public static final int IDCOL_ORDER         = 0;
    public static final int DATECOL_ORDER       = 1;
    public static final int AUTHORCOL_ORDER     = 2;
    public static final int SUBJECTCOL_ORDER    = 3;
    public static final int RECIPCOL_ORDER      = 4;
    public static final int BODYCOL_ORDER       = 5;
    public static final int ATTACHCOL_ORDER     = 6;
    public static final int MAILRECIPCOL_ORDER  = 7;
    public static final int CCCOL_ORDER         = 8;
    public static final int BCCCOL_ORDER        = 9;
    public static final int USERNAMECOL_ORDER   = 10;
    public static final int INSERTDATECOL_ORDER = 11;
    public static final int TAGSCOL_ORDER       = 12;
    public static final int LINENUMBERCOL_ORDER = 13;

    public static final int SUMCOL		= 14;

    // Data container :
    private final ArrayList<TextMessage> messages ;

    /**
     * Getter
     * @return the array of messages
     */
    public ArrayList<TextMessage> getMessages() {
        return messages;
    }

    /**
     * Constructor
     */
    public MailTableModel() {
        messages = new ArrayList<TextMessage>();
    }

    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public int getColumnCount() {
        return SUMCOL ;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        switch (columnIndex){

            case IDCOL_ORDER :
                return messages.get(rowIndex).id();

            case DATECOL_ORDER :
                return messages.get(rowIndex).date();

            case AUTHORCOL_ORDER :
                return messages.get(rowIndex).author();

            case SUBJECTCOL_ORDER :
                return messages.get(rowIndex).subject();

            case RECIPCOL_ORDER :
                return messages.get(rowIndex).recip();

            case BODYCOL_ORDER :
                return messages.get(rowIndex).body();

            case ATTACHCOL_ORDER :
                return messages.get(rowIndex).attach();

            case MAILRECIPCOL_ORDER :
                return messages.get(rowIndex).mailrecip();

            case CCCOL_ORDER :
                return messages.get(rowIndex).cc();

            case BCCCOL_ORDER :
                return messages.get(rowIndex).bcc();

            case USERNAMECOL_ORDER :
                return messages.get(rowIndex).username();

            case INSERTDATECOL_ORDER :
                return messages.get(rowIndex).insertDate();

            case TAGSCOL_ORDER :
                String[] tagArray = new String[messages.get(rowIndex).tags().size()];
                for( int i=0 ; i<messages.get(rowIndex).tags().size() ; i++){
                    tagArray[i]=messages.get(rowIndex).tags().get(i);
                }
                return SpecializedWorker.stringify("", "", " || ", tagArray);

            case LINENUMBERCOL_ORDER:
                return ""+(rowIndex+1) ;

            default :
                return "ERROR" ;
        } //switch
    }// getValueAt

    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
        /*
        switch(column){
            case IDCOL_ORDER		    : return IDCOL_NAME ;
            case DATECOL_ORDER		    : return DATECOL_NAME ;
            case AUTHORCOL_ORDER	    : return AUTHORCOL_NAME ;
            case SUBJECTCOL_ORDER   	: return SUBJECTCOL_NAME ;
            case RECIPCOL_ORDER		    : return RECIPCOL_NAME ;
            case BODYCOL_ORDER	    	: return BODYCOL_NAME ;
            case ATTACHCOL_ORDER	    : return ATTACHCOL_NAME ;
            case MAILRECIPCOL_ORDER	    : return MAILRECIPCOL_NAME ;
            case CCCOL_ORDER	    	: return CCCOL_NAME ;
            case BCCCOL_ORDER	    	: return BCCCOL_NAME ;
            case USERNAMECOL_ORDER	    : return USERNAMECOL_NAME ;
            case INSERTDATECOL_ORDER	: return INSERTDATECOL_NAME ;
            case TAGSCOL_ORDER	    	: return TAGSCOL_NAME ;
            default		            	: return "ERROR";
        }*/
    }


    /**
     * Getter
     * @param index
     * @return the TextMessage at index [index]
     */
   /*
    public TextMessage get(int index){
        if( index <0 || index>=messages.size() ) return null ;
        return messages.get(index);
    }*/

    /**
     * adds a new TextMessage
     * @param me : the TextMessage to add
     */
    /*
    public void add(TextMessage me){
        if(me!=null) messages.add(me);
    }
    */
}
