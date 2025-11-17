package schneiderlab.tools.radialprojection.controllers.uiaction.generalcomponentactionandeffect;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ShowContentOfTextFieldInToolTip extends MouseMotionAdapter {
    private final JTextField textField;

    public ShowContentOfTextFieldInToolTip(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        String content = textField.getText();
        textField.setToolTipText(content);
    }
}
