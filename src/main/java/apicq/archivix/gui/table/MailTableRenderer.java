package apicq.archivix.gui.table;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by pic on 3/28/14.
 */
public class MailTableRenderer implements TableCellRenderer{


    public static Color SUBJECT_BACKGROUND_COLOR = new Color(246,245,175);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        if( (row%2)==0 ) {
            label.setBackground(Color.WHITE);
        }
        else {
            label.setBackground(Color.LIGHT_GRAY);
        }
        label.setText(value.toString());
//        label.setHorizontalAlignment(JLabel.CENTER);

        switch(column){
            case MailTableModel.SUBJECTCOL_ORDER :
                label.setBackground(SUBJECT_BACKGROUND_COLOR);
                label.setText(value.toString());
                break;
            case MailTableModel.DATECOL_ORDER:
                label.setForeground(Color.RED);
            default:
                label.setText(value.toString());
        }
        if(isSelected){
//            label.setBackground(Color.blue.LIGHT_GRAY);
//            label.setBorder(new LineBorder(Color.BLACK));
            label.setBackground(new Color(153,153,153));
        }
    return label ;
    }
}
