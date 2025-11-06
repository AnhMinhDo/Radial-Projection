package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.bandgapmeasurement.BandAndGapMeasurementByRandomScan;
import schneiderlab.tools.radialprojection.imageprocessor.core.bandgapmeasurement.LineScan;

import javax.swing.*;
import java.util.List;

public class RandomLineScanWorker extends SwingWorker<Void, Void> {
    private int numberOfRandomLineScan;
    private int lineScanLengthInMicroMeter;
    private int pixelSizeInNm;
    private List<Vessel> vesselList;

    public RandomLineScanWorker(int numberOfRandomLineScan,
                                int lineScanLengthInMicroMeter,
                                int pixelSizeInNm,
                                List<Vessel> vesselList) {
        this.numberOfRandomLineScan = numberOfRandomLineScan;
        this.lineScanLengthInMicroMeter = lineScanLengthInMicroMeter;
        this.pixelSizeInNm=pixelSizeInNm;
        this.vesselList = vesselList;
    }

    @Override
    protected Void doInBackground() throws Exception {
        int idx = 0;
        for(Vessel vessel : vesselList){
            idx+=1;
            ImagePlus hybrid =vessel.getRadialProjectionHybrid();
            ShortProcessor  hybridShortProcessor = (ShortProcessor) hybrid.getProcessor();
            BandAndGapMeasurementByRandomScan bagmbrs = new BandAndGapMeasurementByRandomScan(
                    hybridShortProcessor,
                    numberOfRandomLineScan,
                    lineScanLengthInMicroMeter,
                    pixelSizeInNm);
            bagmbrs.process();
            ShortProcessor imageWithOnlyScanBand=bagmbrs.getImageWithOnlyScannedBands();
            ImagePlus imageWithOnlyScanBandImagePlus = new ImagePlus("Detected Bands in vessel "+idx,imageWithOnlyScanBand);
            vessel.setBandHybridImagePlus(imageWithOnlyScanBandImagePlus);
            // get the mask of bands
            ImageProcessor imageWithOnlyScanBandProcessor = imageWithOnlyScanBandImagePlus.getProcessor();
            //            binary.setThreshold(1,binary.getMax(),ImageProcessor.BLACK_AND_WHITE_LUT);
            short[] imageWithOnlyScanBandPixels = (short[]) imageWithOnlyScanBandProcessor.getPixels();
            byte[] binaryByteArray = new byte[imageWithOnlyScanBandPixels.length];
            for (int i = 0; i < imageWithOnlyScanBandPixels.length; i++) {
                if(imageWithOnlyScanBandPixels[i] != 0){
                    binaryByteArray[i] = (byte)255;
                }
            }
            ByteProcessor binaryBandOnlyProcessor = new ByteProcessor(imageWithOnlyScanBandProcessor.getWidth(),
                    imageWithOnlyScanBandProcessor.getHeight(),
                    binaryByteArray);
            ImagePlus binaryImagePlus = new ImagePlus("binary of detected bands in vessel "+idx,binaryBandOnlyProcessor);
            vessel.setBandHybridMaskImagePlus(binaryImagePlus);
            // set parameters
            vessel.setNoOfBands((double) bagmbrs.getTotalNumberOfBand());
            vessel.setMeanBandWidth(bagmbrs.getMeanBandLength());
            vessel.setSdBandWidth(bagmbrs.getStdBandLength());
            vessel.setNoOfGaps((double)bagmbrs.getTotalNumberOfGap());
            vessel.setMeanGapWidth(bagmbrs.getMeanGapLength());
            vessel.setSdGapWidth(bagmbrs.getStdGapLength());
            vessel.setNoOfRandomLineScan(numberOfRandomLineScan);
            vessel.setLengthOfLineScan(lineScanLengthInMicroMeter);

        }

        return null;
    }
}
