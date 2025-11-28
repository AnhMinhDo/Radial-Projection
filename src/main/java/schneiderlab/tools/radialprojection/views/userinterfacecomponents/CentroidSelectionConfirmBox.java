/*
 * Created by JFormDesigner on Fri Nov 28 17:44:15 CET 2025
 */

package schneiderlab.tools.radialprojection.views.userinterfacecomponents;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author anhminh
 */
public class CentroidSelectionConfirmBox extends JFrame {
	public CentroidSelectionConfirmBox() {
		initComponents();
		Icon refreshIcon = new ImageIcon(getClass().getResource("/icons/refreshArrow.png"));
		Icon checkIcon = new ImageIcon(getClass().getResource("/icons/checkMark.png"));
		buttonRefresh.setIcon(refreshIcon);
		buttonCheckMark.setIcon(checkIcon);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
		// Generated using JFormDesigner Educational license - Anh Minh Do
		buttonRefresh = new JButton();
		buttonCheckMark = new JButton();

		//======== this ========
		setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		//---- buttonRefresh ----
		buttonRefresh.setToolTipText("REFRESH");
		buttonRefresh.setText("Refresh");
		contentPane.add(buttonRefresh);

		//---- buttonCheckMark ----
		buttonCheckMark.setToolTipText("OK");
		buttonCheckMark.setText("Confirm");
		contentPane.add(buttonCheckMark);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
	// Generated using JFormDesigner Educational license - Anh Minh Do
	private JButton buttonRefresh;
	private JButton buttonCheckMark;
	// JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
