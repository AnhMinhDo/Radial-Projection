package schneiderlab.tools.radialprojection.controllers.uiaction;


import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AddFilePathFromDirToTableVesselSegmentation implements ActionListener {
    private final JTable table;
    private final Component parent;
    private MainView mainView;

    public AddFilePathFromDirToTableVesselSegmentation(JTable table,
                                                       Component parent,
                                                       MainView mainView) {
        this.table = table;
        this.parent = parent;
        this.mainView = mainView;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        File dirPathFromCziToTifStep = new File(mainView.getTextFieldConvertCzi2Tif().getText());
        boolean isDirPathFromCziToTifStepValid = dirPathFromCziToTifStep.exists();
        if(isDirPathFromCziToTifStepValid){
            chooser.setCurrentDirectory(dirPathFromCziToTifStep);
        }
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".tif"));
            if (files != null) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                for (File file : files) {
                    model.addRow(new Object[]{file.getAbsolutePath()});
                }
            }
        }
    }
}
