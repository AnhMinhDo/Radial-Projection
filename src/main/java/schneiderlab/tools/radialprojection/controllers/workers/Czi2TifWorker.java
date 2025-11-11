package schneiderlab.tools.radialprojection.controllers.workers;

import ij.IJ;
import schneiderlab.tools.radialprojection.imageprocessor.core.convertczitotif.CZIProcessor;
import schneiderlab.tools.radialprojection.imageprocessor.core.convertczitotif.RotateDirection;

import javax.swing.*;
import java.io.File;

public class Czi2TifWorker extends SwingWorker <Void, Void>{
    private final String folderPath;
    private final boolean backgroundSubtraction;
    private final int rolling;
    private final int saturated;
    private final boolean isRotate;
    private final RotateDirection rotateDirection;

    public Czi2TifWorker(String folderPath,
                         boolean backgroundSubtraction,
                         int rolling,
                         int saturated,
                         boolean isRotate,
                         RotateDirection rotateDirection) {
        this.folderPath = folderPath;
        this.backgroundSubtraction = backgroundSubtraction;
        this.rolling = rolling;
        this.saturated = saturated;
        this.isRotate = isRotate;
        this.rotateDirection = rotateDirection;
    }

    @Override
    protected Void doInBackground() {
        // perform file checking and validation
        if (folderPath == null || folderPath.isEmpty()) {
            System.err.println("Error: Folder path is empty.");
            IJ.log("Error: Folder path is empty.");
            return null;
        }
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Error: Invalid folder path.");
            IJ.log("Error: Invalid folder path.");
            return null;
        }
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".czi"));
        if (files == null || files.length == 0) {
            System.err.println("Error: No CZI file in this directory");
            IJ.log("Error: No CZI file in this directory");
            return null;
        }
//        IJ.log(String.valueOf(backgroundSubtraction));
        // loop the file list and call the conversion class for each file
        int total = files.length;
        double singlePart = 100.0/total;
        int counter = 0;
        for (File file : files) {
            CZIProcessor.convertingCZItoTIFF(file,
                    backgroundSubtraction,
                    rolling,
                    saturated,
                    isRotate,
                    rotateDirection,
                    false);
            counter++;
            setProgress((int)Math.floor(singlePart*counter));
            IJ.log(counter+"/"+total+" complete");
        }
        return null;
    }
}
