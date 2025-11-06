package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

public class SaveRadialProjectionOutputWorker extends SwingWorker<Void, Void> {
    private ImageData<UnsignedShortType, FloatType> imageData;

    public SaveRadialProjectionOutputWorker(ImageData<UnsignedShortType, FloatType> imageData) {
        this.imageData = imageData;
    }

    @Override
    protected Void doInBackground() throws Exception {
        List<Vessel> vesselList = imageData.getVesselList();
        Path dirPath = imageData.getOutputDirPath();
        String filename = RadialProjectionUtils.filenameWithoutExtension(imageData.getImagePath().getFileName().toString());
        Path finalOutputDir = dirPath.resolve(filename+"_Out");
        int index = 0;
        for (Vessel vessel : vesselList){
            index+=1;
            ImageStack imageStack = new ImageStack(vessel.getRadialProjectionHybrid().getWidth(), vessel.getRadialProjectionHybrid().getHeight());
            imageStack.addSlice(vessel.getRadialProjectionLignin().getProcessor());
            imageStack.addSlice(vessel.getRadialProjectionCellulose().getProcessor());
            imageStack.addSlice(vessel.getRadialProjectionHybrid().getProcessor());
            ImagePlus imagePlus = new ImagePlus("RadialProjection_Vessel_"+index,imageStack);
            FileSaver radialProjectionHybridSaver = new FileSaver(imagePlus);
            radialProjectionHybridSaver.saveAsTiff(finalOutputDir.resolve("_RadialProjection_Vessel_"+index).toString());
        }

        return null;
    }
}
