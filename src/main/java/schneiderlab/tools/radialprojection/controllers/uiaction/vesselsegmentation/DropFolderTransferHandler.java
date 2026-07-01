package schneiderlab.tools.radialprojection.controllers.uiaction.vesselsegmentation;

import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DropFolderTransferHandler extends TransferHandler {
    private final JTable table;
    private final Component parent;
    private MainView mainView;

    public DropFolderTransferHandler(JTable table, Component parent, MainView mainView) {
        this.table = table;
        this.parent = parent;
        this.mainView = mainView;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;  // Essential for drops from external apps
    }

    @Override
    public boolean canImport(TransferSupport support){
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) return false;
        Transferable t = support.getTransferable();
        try {
            java.util.List<File> transferedFiles = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
            File importedFile = transferedFiles.get(0);
            if (importedFile.isDirectory()){
                File dir = importedFile;
                File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".tif"));
                if (files != null) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    for (File file : files) {
                        model.addRow(new Object[]{file.getAbsolutePath()});
                    }
                }
            }
            return true;
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
