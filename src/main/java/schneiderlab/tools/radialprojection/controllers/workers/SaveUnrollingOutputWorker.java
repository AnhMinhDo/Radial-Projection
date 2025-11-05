package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import ij.process.ShortProcessor;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;

public class SaveUnrollingOutputWorker extends SwingWorker<Void, Void> {
    private ImageData<UnsignedShortType, FloatType> imageData;

    public SaveUnrollingOutputWorker(ImageData<UnsignedShortType, FloatType> imageData) {
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
            ImageStack imageStack = new ImageStack(vessel.getUnrolledVesselHybrid().getWidth(), vessel.getUnrolledVesselHybrid().getHeight());
            imageStack.addSlice(vessel.getUnrolledVesselLignin().getProcessor());
            imageStack.addSlice(vessel.getUnrolledVesselCellulose().getProcessor());
            imageStack.addSlice(vessel.getUnrolledVesselHybrid().getProcessor());
            ByteProcessor byteProcessor = (ByteProcessor) vessel.getContour().getProcessor();
            byte[] byteArray = (byte[]) byteProcessor.getPixels();
            ShortProcessor shortProcessor = new ShortProcessor(byteProcessor.getWidth(), byteProcessor.getHeight());
            short[] shortArray = (short[]) shortProcessor.getPixels();
            for (int i = 0; i < byteArray.length; i++) {
                if((byteArray[i]& 0xFF) == 255){
                    shortArray[i] = (short)65535;
                }
            }
            imageStack.addSlice(shortProcessor);
            ImagePlus imagePlus = new ImagePlus("Unrolled_Vessel_"+index,imageStack);
            FileSaver radialProjectionHybridSaver = new FileSaver(imagePlus);
            radialProjectionHybridSaver.saveAsTiff(finalOutputDir.resolve("_Unrolled_Vessel_"+index).toString());
        }
        return null;
    }
}
