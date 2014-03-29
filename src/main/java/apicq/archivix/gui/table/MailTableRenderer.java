package apicq.archivix.gui.table;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by pic on 3/28/14.
 */
public class MailTableRenderer implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setText(value.toString());
//        label.setHorizontalAlignment(JLabel.CENTER);

        switch(column){
            case MailTableModel.SUBJECTCOL_ORDER :
                label.setBackground(Color.YELLOW);
                label.setText(value.toString());
                break;
            case MailTableModel.DATECOL_ORDER:
                label.setForeground(Color.RED);
            default:
                label.setText(value.toString());
        }
        if(isSelected){
//            label.setBackground(Color.blue.LIGHT_GRAY);
            label.setBorder(new LineBorder(Color.RED));
        }
    return label ;
    }
}
