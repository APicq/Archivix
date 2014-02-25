package apicq.archivix.gui;

import apicq.archivix.tools.SpecializedWorker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by pic on 2/10/14.
 */
public class NewMessageTableModel extends AbstractTableModel {

    public static final Logger log = Logger.getLogger("Archivix");

    /**
     * Class to manage column visibility
     */
    class VisibleColumn {
        int id;
        boolean isVisible ;

        VisibleColumn(int id, boolean isVisible) {
            this.id = id;
            this.isVisible = isVisible;
        }
    }

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
    public static final int SUMCOL          = 13; // number of columns

    // messageElement container
    private ArrayList<TextMessage> messages ;

    // manages column visibility
    private ArrayList<VisibleColumn> visibility ;




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
    public NewMessageTableModel() {
        visibility = new ArrayList<VisibleColumn>() ;
        for( int x=0 ; x<SUMCOL ; x++){
            visibility.add(new VisibleColumn(x,true));
        }
        messages = new ArrayList<TextMessage>();
    }

    /**
     * Add one element in table
     * @param me
     */
    public void add(TextMessage me){
        if(me!=null) messages.add(me);
    }

    /**
     * Delete all messages in table model
     */
    public void clear(){
        messages.clear();
    }

    public int getIndexFromVisibleColumn(int column){//todo rewrite
        int x=-1 ;
        for(VisibleColumn vc : visibility){
            if(vc.isVisible) x++ ;
            if(x==column) break ;
        }
        return x ;
    }

    public void hideColumn(int columnId){
        visibility.get(columnId).isVisible = false ;
    }

    public void showColumn(int columnId){
        visibility.get(columnId).isVisible = true ;
    }

    public boolean isVisible(int columnId){
        return visibility.get(columnId).isVisible;
    }
    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public int getColumnCount() {//todo rewrite
        int x=0;
        for(VisibleColumn vc : visibility){
            if(vc.isVisible) x++;
        }
        return x;
        //return SUMCOL;
    }

    @Override
    public String getColumnName(int column) {
        int x = getIndexFromVisibleColumn(column);
        switch (x){
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
        int x = getIndexFromVisibleColumn(columnIndex);
        switch (x){
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
            default :
                return "ERROR" ;
        } //switch
    } // get
} // class
