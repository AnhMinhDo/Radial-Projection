package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.FibrilTool;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.bandgapmeasurement.BandAndGapMeasurementByRandomScan;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.CurrentImageStage;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializableUtils;
import schneiderlab.tools.radialprojection.imageprocessor.core.io.SaveAnalysisResultBatchMode;
import schneiderlab.tools.radialprojection.imageprocessor.core.io.SaveVesselResultToCSV;
import schneiderlab.tools.radialprojection.imageprocessor.core.io.Utils;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializableUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeGlobalStateModel;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.SliceCroppedRange;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSliceData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BatchAnalysisWorker extends SwingWorker<Void, Void> {
    // list of ImageData objects
    // batch mode model
//    private BatchModeModel batchModeModel;
    private BatchModeGlobalStateModel batchModeGlobalStateModel;
//    private List<ImageData<UnsignedShortType, FloatType>> imageDataList;
    private Context context;

    public BatchAnalysisWorker(BatchModeGlobalStateModel batchModeGlobalStateModel,
                               Context context) {
//        this.batchModeModel = batchModeModel;
        this.batchModeGlobalStateModel = batchModeGlobalStateModel;
//        this.imageDataList = batchModeModel.getAnalysisBatchList();
        this.context = context;
    }

    @Override
    protected Void doInBackground() throws Exception {
        // perform analysis for each vessel and set the values in the vessel object
        while (!batchModeGlobalStateModel.getAnalysisQueue().isEmpty()){
            IJ.log("start analysis step");
            String serFile = batchModeGlobalStateModel.getFirstAnalysisQueue();
            IJ.log("serialized object path: " + serFile);
            ImageDataSerializable imageDataSerializable = ImageDataSerializableUtils.imageDataDeserializeObject(Paths.get(serFile));
            IJ.log("complete deserialization");
            // load the vesselSerFileFilePathList, no need to convert back to Vessel object
            List<String> vesselFilePathList = imageDataSerializable.getVesselSerFilePathList();
            IJ.log(vesselFilePathList.toString());
            for (int i = 0; i < vesselFilePathList.size(); i++) {
                String vesselFilePath = vesselFilePathList.get(i);
                // deserialize the vesselSerializable object
                VesselSerializable vesselSerializable = VesselSerializableUtils.vesselDeserializeObject(Paths.get(vesselFilePath));
                // convert back to Vessel object
                Vessel vessel = VesselSerializableUtils.convertSerializableToVessel(vesselSerializable, CurrentImageStage.Analysis, context);
                // get the path to Radial Projection
                String pathMultiChannelsRadialProjection = vesselSerializable.getPathMultiChannelsRadialProjection();
                // load the radial projection using the file path
                ImagePlus radialProjectionMultipleChannels = IJ.openImage(pathMultiChannelsRadialProjection);
                // get the crop radialProjection
                ImageStack imageStack = radialProjectionMultipleChannels.getImageStack();
                ShortProcessor hybridProcessor = (ShortProcessor) imageStack.getProcessor(3).duplicate();
                // crop hybridProcessor
                SliceCroppedRange sliceCroppedRange = vesselSerializable.getSliceCroppedRange();
                Roi roi = new Roi(sliceCroppedRange.getStart(), 0, sliceCroppedRange.getEnd() - sliceCroppedRange.getStart(), hybridProcessor.getHeight());
                ImagePlus hybridImp = new ImagePlus("hybrid", hybridProcessor);
                hybridImp.setRoi(roi);
                ImagePlus croppedImagePlus = hybridImp.crop();
                int numberOfRandomLineScan = vesselSerializable.getNoOfRandomLineScan();
                int lineScanLengthInMicroMeter = (int) vesselSerializable.getLengthOfLineScan(); // TODO: trace back to check if the input is micrometer or nanometer
                int pixelSizeInNm = imageDataSerializable.getXyPixelSize();
                BandAndGapMeasurementByRandomScan bagmbrs = new BandAndGapMeasurementByRandomScan(
                        (ShortProcessor) croppedImagePlus.getProcessor(),
                        numberOfRandomLineScan,
                        lineScanLengthInMicroMeter,
                        pixelSizeInNm);
                bagmbrs.process();
                ShortProcessor imageWithOnlyScanBand = bagmbrs.getImageWithOnlyScannedBands();
                ImagePlus imageWithOnlyScanBandImagePlus = new ImagePlus("Detected Bands in vessel " + (i+1), imageWithOnlyScanBand);
                // get the mask of bands
                ImageProcessor imageWithOnlyScanBandProcessor = imageWithOnlyScanBandImagePlus.getProcessor();
                //            binary.setThreshold(1,binary.getMax(),ImageProcessor.BLACK_AND_WHITE_LUT);
                // convert from short type to byte type
                short[] imageWithOnlyScanBandPixels = (short[]) imageWithOnlyScanBandProcessor.getPixels();
                byte[] binaryByteArray = new byte[imageWithOnlyScanBandPixels.length];
                for (int j = 0; j < imageWithOnlyScanBandPixels.length; j++) {
                    if (imageWithOnlyScanBandPixels[j] != 0) {
                        binaryByteArray[j] = (byte) 255;
                    }
                }
                ByteProcessor binaryBandOnlyProcessor = new ByteProcessor(imageWithOnlyScanBandProcessor.getWidth(),
                        imageWithOnlyScanBandProcessor.getHeight(),
                        binaryByteArray);
                ImagePlus binaryImagePlus = new ImagePlus("binary of detected bands in vessel " + (i+1), binaryBandOnlyProcessor);
                // save the binary image of the detected Band using line scan
                FileSaver binaryFileSaver = new FileSaver(binaryImagePlus);
                String imageName = "Vessel " + (i+1) + " detected bands";
                Path outputDirPath = Paths.get(imageDataSerializable.getOutputDirPath());
                Path detectedBandPath = outputDirPath.resolve(imageName);
                binaryFileSaver.saveAsTiff(detectedBandPath.toString());
                // set parameters
                vesselSerializable.setNoOfBands((double) bagmbrs.getTotalNumberOfBand());
                vessel.setNoOfBands((double) bagmbrs.getTotalNumberOfBand());
                vesselSerializable.setMeanBandWidth(bagmbrs.getMeanBandLength());
                vessel.setMeanBandWidth(bagmbrs.getMeanBandLength());
                vesselSerializable.setSdBandWidth(bagmbrs.getStdBandLength());
                vessel.setSdBandWidth(bagmbrs.getStdBandLength());
                vesselSerializable.setNoOfGaps((double) bagmbrs.getTotalNumberOfGap());
                vessel.setNoOfGaps((double) bagmbrs.getTotalNumberOfGap());
                vesselSerializable.setMeanGapWidth(bagmbrs.getMeanGapLength());
                vessel.setMeanGapWidth(bagmbrs.getMeanGapLength());
                vesselSerializable.setSdGapWidth(bagmbrs.getStdGapLength());
                vessel.setSdGapWidth(bagmbrs.getStdGapLength());
                vesselSerializable.setNoOfRandomLineScan(numberOfRandomLineScan);
                vessel.setNoOfRandomLineScan(numberOfRandomLineScan);
                vesselSerializable.setLengthOfLineScan(lineScanLengthInMicroMeter);
                vessel.setLengthOfLineScan(lineScanLengthInMicroMeter);
                // perform anisotropy measurement
                IJ.log("Perform anisotropy measurement");
                int numberOfRandomBox = vesselSerializable.getNoOfRandomBox();
                int windowSize = vesselSerializable.getRandomBoxWidth();
                DescriptiveStatistics statsOrientation = new DescriptiveStatistics();
                DescriptiveStatistics statsAnisotropy = new DescriptiveStatistics();
                for (int k = 0; k < numberOfRandomBox + 1; k++) {
                    ImagePlus randombox = selectRandomBox(croppedImagePlus, windowSize);
                    FibrilTool fibrilTool = new FibrilTool(randombox);
                    fibrilTool.calculate();
                    statsOrientation.addValue(fibrilTool.getOrientation());
                    statsAnisotropy.addValue(fibrilTool.getAnisotropy());
                }
                vesselSerializable.setMeanAnisotropy(statsAnisotropy.getMean());
                vesselSerializable.setMeanBandOrientation(statsOrientation.getMean());
                vesselSerializable.setSdAnisotropy(statsAnisotropy.getStandardDeviation());
                vesselSerializable.setSdBandOrientation(statsOrientation.getStandardDeviation());
                vesselSerializable.setNoOfRandomBox(numberOfRandomBox);
                IJ.log("Mean band width and std: " + vesselSerializable.getMeanBandWidth() + " " + vesselSerializable.getSdBandWidth());
                IJ.log("Mean gap width and std: " + vesselSerializable.getMeanGapWidth() + " " + vesselSerializable.getSdGapWidth());
                IJ.log("Orientation and std: " + vesselSerializable.getMeanBandOrientation() + " " + vesselSerializable.getSdBandOrientation());
                Path batchDirPath = Paths.get(imageDataSerializable.getImagePath()).getParent();
                Path combinedAnalysisCsv = batchDirPath.resolve("analysis.csv");
                Utils.checkAndCreateCombinedAnalysisFile(batchDirPath,"analysis.csv");
                // append the result to the csv file
                SaveAnalysisResultBatchMode sarbm = new SaveAnalysisResultBatchMode(vesselSerializable,
                        combinedAnalysisCsv,
                        imageDataSerializable,
                        i);
                sarbm.flush();
            }
            batchModeGlobalStateModel.removeFirstAnalysisQueue();
            // delete the temp folder
            deleteFolderRecursive(Paths.get(serFile).getParent());
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

    public static void deleteFolderRecursive(Path folderPath) throws IOException {
        if (!Files.exists(folderPath)) {
            return;
        }
        Files.walk(folderPath)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
