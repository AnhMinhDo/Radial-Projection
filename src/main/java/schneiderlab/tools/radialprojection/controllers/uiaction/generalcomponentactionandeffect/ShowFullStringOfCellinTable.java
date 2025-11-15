package schneiderlab.tools.radialprojection.controllers.uiaction.generalcomponentactionandeffect;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ShowFullStringOfCellinTable extends MouseMotionAdapter {
    private JTable table;

    public ShowFullStringOfCellinTable(JTable table) {
        this.table = table;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        int col = table.columnAtPoint(e.getPoint());
        if (row > -1 && col > -1) {
            String value = (String) table.getValueAt(row, col);
            table.setToolTipText(value);
        }
    }


}
