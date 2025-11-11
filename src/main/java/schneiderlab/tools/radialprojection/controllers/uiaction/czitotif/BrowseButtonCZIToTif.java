package schneiderlab.tools.radialprojection.controllers.uiaction.czitotif;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BrowseButtonCZIToTif implements ActionListener {
    private JTextField textFieldFolderPath;
    private JFrame parentFrame;
    private JTable table;

    public BrowseButtonCZIToTif(JTextField textFieldFolderPath,
                                JFrame parentFrame,
                                JTable table) {
        this.textFieldFolderPath = textFieldFolderPath;
        this.parentFrame = parentFrame;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(parentFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedDir = chooser.getSelectedFile();
            textFieldFolderPath.setText(selectedDir.getAbsolutePath());
            File[] filesList = selectedDir.listFiles(((dir, name) -> name.toLowerCase().endsWith(".czi")));
            if(filesList==null){
                return;
            }
            DefaultTableModel cziToTifTableModel = (DefaultTableModel) table.getModel();
            cziToTifTableModel.setRowCount(0);
            for (int i = 0; i < filesList.length; i++) {
                cziToTifTableModel.addRow(new String[]{filesList[i].getName()});
            }
        }
}

}
