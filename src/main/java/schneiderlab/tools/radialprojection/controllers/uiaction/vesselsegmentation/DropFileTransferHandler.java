package schneiderlab.tools.radialprojection.controllers.uiaction.vesselsegmentation;

import ij.IJ;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DropFileTransferHandler extends TransferHandler {
    private final JTable table;
    private final Component parent;

    public DropFileTransferHandler(JTable table, Component parent) {
        this.table = table;
        this.parent = parent;
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
            java.util.List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
            File importedFile = files.get(0);
            if (importedFile.isFile()){
                String path = importedFile.getPath();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[]{path});
            }
            return true;
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
