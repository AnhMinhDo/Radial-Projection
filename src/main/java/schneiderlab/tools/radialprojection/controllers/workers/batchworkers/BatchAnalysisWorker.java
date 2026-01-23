package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import schneiderlab.tools.radialprojection.imageprocessor.core.FibrilTool;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.bandgapmeasurement.BandAndGapMeasurementByRandomScan;
import schneiderlab.tools.radialprojection.imageprocessor.core.io.SaveVesselResultToCSV;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BatchAnalysisWorker extends SwingWorker<Void, Void> {
    // list of ImageData objects
    // batch mode model
    private BatchModeModel batchModeModel;
    private List<ImageData<UnsignedShortType, FloatType>> imageDataList;

    public BatchAnalysisWorker(BatchModeModel batchModeModel) {
        this.batchModeModel = batchModeModel;
        this.imageDataList = batchModeModel.getAnalysisBatchList();
    }

    @Override
    protected Void doInBackground() throws Exception {
        // perform analysis for each vessel and set the values in the vessel object
        for (ImageData<UnsignedShortType, FloatType> imageData : imageDataList){
            int idx = 0;
            for (Vessel vessel: imageData.getVesselList()){
                idx+=1;
                // perform band and gap measurement
                ImagePlus hybrid =vessel.getRadialProjectionHybrid();
                ShortProcessor hybridShortProcessor = (ShortProcessor) hybrid.getProcessor();
                int numberOfRandomLineScan = batchModeModel.getNumberOfLineScan();
                int lineScanLengthInMicroMeter = batchModeModel.getLinescanLength();
                int pixelSizeInNm = batchModeModel.getXyPixelSize();
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
                // perform anisotropy measurement
                int numberOfRandomBox = batchModeModel.getNumberOfRandomBoxes();
                int windowSize = batchModeModel.getAnalysisWindow();
                DescriptiveStatistics statsOrientation = new DescriptiveStatistics();
                DescriptiveStatistics statsAnisotropy = new DescriptiveStatistics();
                for (int i = 0; i < numberOfRandomBox+1; i++) {
                    ImagePlus randombox = selectRandomBox(vessel.getRadialProjectionHybrid(),windowSize);
                    FibrilTool fibrilTool = new FibrilTool(randombox);
                    fibrilTool.calculate();
                    statsOrientation.addValue(fibrilTool.getOrientation());
                    statsAnisotropy.addValue(fibrilTool.getAnisotropy());
                }
                vessel.setMeanAnisotropy(statsAnisotropy.getMean());
                vessel.setMeanBandOrientation(statsOrientation.getMean());
                vessel.setSdAnisotropy(statsAnisotropy.getStandardDeviation());
                vessel.setSdBandOrientation(statsOrientation.getStandardDeviation());
                vessel.setNoOfRandomBox(numberOfRandomBox);
                // for debugging: show the mask image of detected bands and anisotropy results
                vessel.getBandHybridMaskImagePlus().duplicate().show();
                IJ.log("Mean band width and std: " + vessel.getMeanBandWidth() + " " + vessel.getSdBandWidth());
                IJ.log("Mean gap width and std: " + vessel.getMeanGapWidth() + " " + vessel.getSdGapWidth());
                IJ.log("Orientation and std: " + vessel.getMeanBandOrientation() + " " + vessel.getSdBandOrientation());
            }
        }
        // perform saving of all the analysis to csv file
        boolean combine = true;
        SaveVesselResultToCSV saveVesselResultToCSV = new SaveVesselResultToCSV(imageDataList, combine);
        try {
            saveVesselResultToCSV.flush();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    // select a point in range 0 to width-window size and build an ImagePlus object from that
    private static ImagePlus selectRandomBox(ImagePlus input, int windowSize){
        int min = 0;
        int max = input.getWidth()-windowSize;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max); // inclusive range
        int x = randomNum;      // top-left x coordinate
        int y = 0;      // top-left y coordinate
        int width = windowSize; // crop width
        int height = input.getHeight(); // crop height
        input.setRoi(new Rectangle(x, y, width, height));
        return input.crop();
    }
}
