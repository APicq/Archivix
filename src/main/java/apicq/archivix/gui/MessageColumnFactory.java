package apicq.archivix.gui;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.swing.table.TableModel;
import java.util.logging.Logger;

/**
 * Created by pic on 2/25/14.
 */
public class MessageColumnFactory extends ColumnFactory {

    public static final Logger log = Logger.getLogger("Archivix");

    private static boolean[] visibility = new boolean[]{
            true, //IDCOL
            true, //DATECOL
            true, //AUTHORCOL
            true, //SUBJECTCOL
            true, //RECIPCOL
            true, //BODYCOL
            true, //ATTACHCOL
            true, //MAILRECIPCOL
            true, //CCCOL
            true, //BCCCOL
            true, //USERNAMECOL
            true, //INSERTDATECOL
            true, //TAGSCOL
    };

    public static void setVisibility(int index,boolean isVisible){
        visibility[index]=isVisible ;
    }

    public static boolean isVisible(int index){
        return visibility[index];
    }


    @Override
    public TableColumnExt createAndConfigureTableColumn(TableModel model, int modelIndex) {
        TableColumnExt tce =  super.createAndConfigureTableColumn(model, modelIndex);
        switch(modelIndex){
            case MessageTableModel.IDCOL :
                tce.setTitle("ID");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.DATECOL :
                tce.setTitle("Date");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.AUTHORCOL :
                tce.setTitle("Auteur");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.SUBJECTCOL :
                tce.setTitle("Sujet");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.RECIPCOL :
                tce.setTitle("Destinataires");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.BODYCOL :
                tce.setTitle("Texte");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.ATTACHCOL :
                tce.setTitle("PJ");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.MAILRECIPCOL :
                tce.setTitle("Destinataires (liste complète)");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.CCCOL :
                tce.setTitle("CC");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.BCCCOL :
                tce.setTitle("BCC");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.USERNAMECOL :
                tce.setTitle("Utilisateur");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.INSERTDATECOL :
                tce.setTitle("Date d'insertion");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            case MessageTableModel.TAGSCOL :
                tce.setTitle("Tags");
                tce.setVisible(visibility[modelIndex]);
                return tce ;
            default:
                return tce ;
        }
      //  log.info(""+tce.getTitle()+" "+tce.isVisible());
    }
}
