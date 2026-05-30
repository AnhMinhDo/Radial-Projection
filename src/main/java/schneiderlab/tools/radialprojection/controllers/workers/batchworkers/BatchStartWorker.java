package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.DefaultLinearAxis;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.ui.UIService;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.createsideview.CreateSideView;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializableUtils;
import schneiderlab.tools.radialprojection.imageprocessor.core.segmentation.CreateHybridStack;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeGlobalStateModel;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BatchStartWorker<T extends RealType<T>> extends SwingWorker<Void, Void> {
    private final int targetXYpixelSize;
    private final int targetZpixelSize;
    private final int ligninToCelluloseWeight;
    private final int windowSizeinMicroMeter;
    private final double  sigmaValueFilter;
    private final double radius;
    private final  int numberOfLineScan;
    private final  int lineScanLengthInMicroMeter;
    private final  int numberOfRandomBoxes;
    private final  int randomBoxWidth;

    private final Context context;
    private ImgPlus<UnsignedShortType> sideViewImgPlus;
    private BatchModeModel batchModeModel;
    private BatchModeGlobalStateModel batchModeGlobalStateModel;

    public BatchStartWorker(BatchModeModel batchModeModel, BatchModeGlobalStateModel batchModeGlobalStateModel, Context context) {
        this.targetXYpixelSize = batchModeModel.getXyPixelSize();
        this.targetZpixelSize = batchModeModel.getzPixelSize();
        this.ligninToCelluloseWeight = batchModeModel.getCelluloseToLigninRatio();
        this.windowSizeinMicroMeter = batchModeModel.getAnalysisWindow();
        this.sigmaValueFilter = batchModeModel.getSmoothingSigma();
        this.radius = batchModeModel.getInnerVesselRadius();
//        this.filePath = filePath;
        this.context = context;
        this.batchModeModel = batchModeModel;
        this.batchModeGlobalStateModel = batchModeGlobalStateModel;
        this.numberOfLineScan = batchModeModel.getNumberOfLineScan();
        this.lineScanLengthInMicroMeter = batchModeModel.getLinescanLength();
        this.numberOfRandomBoxes = batchModeModel.getNumberOfRandomBoxes();
        this.randomBoxWidth = batchModeModel.getRandomBoxWidth();
    }

    public ImgPlus<UnsignedShortType> getSideViewImgPlus() {
        return sideViewImgPlus;
    }

    @Override
    protected Void doInBackground() throws Exception {
        // Get DatasetService and UIService from context
//        StatusService statusService = context.getService(StatusService.class);
        DatasetIOService ioService = context.getService(DatasetIOService.class);
        LogService logService = context.getService(LogService.class);
//        UIService uiService = context.getService(UIService.class);
//        int total = batchModeModel.getTotalNumberOfFiles();
        while(!batchModeGlobalStateModel.getStartQueue().isEmpty()) {
            try {
                Path filePath = Paths.get(batchModeGlobalStateModel.getFirstStartQueue());
                IJ.log("loading file: " + filePath.getFileName().toString());
                // load the image
                IJ.log("importing image to create side view ....");
                Dataset img = ioService.open(filePath.toAbsolutePath().toString());
                IJ.log("image is imported successfully");
                ImgPlus<T> genericImgPlus = (ImgPlus<T>) img.getImgPlus();
                IJ.log("Creating side view...");
                IJ.log("add file path to the global state model");
                CreateSideView createSideView = new CreateSideView(context,
                        genericImgPlus,
                        targetXYpixelSize,
                        targetZpixelSize);
                Img<UnsignedShortType> sideViewImg = createSideView.process();
                sideViewImgPlus = new ImgPlus<>(sideViewImg);
                // Add meta data
                sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.X, "micron", targetXYpixelSize * 0.001), 0);
                sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.Y, "micron", targetXYpixelSize * 0.001), 1);
                sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.Z, "micron", targetZpixelSize * 0.001), 3);
                sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.CHANNEL, "", 1.0), 2);
                // create an ImageData Object correspond to the image
                ImageData<UnsignedShortType, UnsignedShortType> imageData = new ImageData<>();
                imageData.setXyPixelSize(targetXYpixelSize);
                imageData.setzPixelSize(targetZpixelSize);
                imageData.setNoOfRandomLineScan(numberOfLineScan);
                imageData.setLengthOfLineScan(lineScanLengthInMicroMeter);
                imageData.setRandomBoxWidth(randomBoxWidth);
                imageData.setNoOfRandomBox(numberOfRandomBoxes);
                imageData.setImagePath(filePath);
                Path outputDirForThisImage = this.createOutputDir(filePath);
                imageData.setImageOutputPath(outputDirForThisImage);
                // create paths for the hierarchy structure of the outputs and temp files of this image
                // Image name
                String filename = imageData.getImagePath().getFileName().toString();
                imageData.setImageName(filename);
                // side view result path
                Path outputDir = imageData.getImageOutputPath();
                String outputFileNameXylemWaterView = "Xylem_Water_View-"+filename;
                Path outputXylemWaterViewPath = outputDir.resolve(outputFileNameXylemWaterView);
                imageData.setSideViewPathWithoutEdgeCentroid(outputXylemWaterViewPath);
                // temp dir path
                Path tempPath = imageData.getImageOutputPath().resolve("temp");
                imageData.setTempDirPath(tempPath);
                // temp ser file path
                String serFileName = "temp.ser";
                Path serPath = imageData.getTempDirPath().resolve(serFileName);
                imageData.setSerializedObjectPath(serPath);
                IJ.log("Path of .ser file: " + serPath);
                // temp side view result path
                Path sideViewTempPathWithoutEdgeCentroid = tempPath.resolve(outputFileNameXylemWaterView);
                imageData.setSideViewTempPathWithoutEdgeCentroid(sideViewTempPathWithoutEdgeCentroid);
                // temp hybrid first Slice path
                String hybridFirstSliceName="hybridFirstSlice.tif";
                Path hybridFirstSlicePath = tempPath.resolve(hybridFirstSliceName);
                imageData.setHybridFirstSlicePath(hybridFirstSlicePath);
                // get the file name of the image
                String fileNameWithExtension = imageData.getImagePath().getFileName().toString();
                int extDotIndex = fileNameWithExtension.lastIndexOf(".");
                String nameOnly = fileNameWithExtension.substring(0,extDotIndex);
                imageData.setImageName(nameOnly);
                IJ.log("get the name of the image: " + nameOnly);
                imageData.setSideView(sideViewImgPlus);
                // projection and smoothing
                int windowSizeinSlideNumber = Math.round(windowSizeinMicroMeter/0.2f); //TODO: replace 0.2 f with a user-defined number
                CreateHybridStack chs = new CreateHybridStack(context,
                        sideViewImgPlus,
                        ligninToCelluloseWeight,
                        windowSizeinSlideNumber,
                        sigmaValueFilter,
                        radius);

                RandomAccessibleInterval<UnsignedShortType> hybridStackSmoothed = chs.process();
                RandomAccessibleInterval<UnsignedShortType> hybridStackNonSmoothed = chs.getHybridNonSmoothedStack();
                double radius = chs.getRadius();
                int width = chs.getSmoothedStackWidth();
                int height = chs.getSmoothedStackHeight();
                int slicesNumber = chs.getSmoothedStackSlicesNumber();
                RandomAccessibleInterval<UnsignedShortType> cellulose = chs.getCellulose();
                RandomAccessibleInterval<UnsignedShortType> lignin = chs.getLignin();
                // set Field for ImageData object
                imageData.setHybridStackNonSmoothed(hybridStackNonSmoothed);
                imageData.setHybridStackSmoothed(hybridStackSmoothed);
                imageData.setHybridStackSmoothedWidth(width);
                imageData.setHybridStackSmoothedHeight(height);
                imageData.setCellulose(cellulose);
                imageData.setLignin(lignin);
                imageData.setHybridStackSmoothedSlicesNumber(slicesNumber);
                imageData.setInnerVesselRadius(radius);
                IJ.log("Complete setting fields for imageData object");
                // create the file paths for the side view temp files

                try {
                    if(Files.notExists(imageData.getTempDirPath())){
                        IJ.log("the temp directory does not exist, creating a new one...");
                        Files.createDirectories(imageData.getTempDirPath());
                        IJ.log("temp dir is created at: " + tempPath);
                    } else {
                        IJ.log("the temp directory exists");
                    }
                    // create the serializable object using the factory class
                ImageDataSerializable imageDataSerializable = ImageDataSerializableUtils.convertImageDataToSerializable(imageData);

                IJ.log("serializable file path: " + imageDataSerializable.getSerializedObjectPath());
                imageDataSerializable.serializeObject();
                } catch (IOException ex) {
                    IJ.log("IO error in creating and saving temp files: " + imageData.getImageName());
                }
//                batchModeModel.addCentroidSelectionList(imageData);
//                batchModeModel.setNumberOfUnprocessedFilePath(batchModeModel.getNumberOfUnprocessedFilePath()-1);
                // worker to save the output in this step
                SaveImageSideViewWithoutEdgeCentroid sisvwdc = new SaveImageSideViewWithoutEdgeCentroid(imageData, context);
                IJ.log("Saving the side view result  ");
                sisvwdc.execute();
                SavingSideViewTemp savingSideViewTemp = new SavingSideViewTemp(imageData,context);
                IJ.log("Saving side view to temp dir");
                savingSideViewTemp.execute();
                SaveFirstSlice saveFirstSlice = new SaveFirstSlice(imageData.getHybridStackSmoothed(),imageData.getTempDirPath(), imageData.getHybridFirstSlicePath());
                saveFirstSlice.saveFirstSlice();
                batchModeGlobalStateModel.addLastCentroidSelectionQueue(imageData.getSerializedObjectPath().toString());
                batchModeGlobalStateModel.removeFirstStartQueue();
            } catch (IOException e) {
                logService.error("Input Output error");
            }
        }

        return null;
    }

    public static void saveRandomAccessInterval(RandomAccessibleInterval<UnsignedShortType> rai, Path filepath, Context context) throws IOException {
        DatasetService datasetService = context.getService(DatasetService.class);
        DatasetIOService datasetIOService = context.getService(DatasetIOService.class);
        Dataset dataset = datasetService.create(rai);
        Files.deleteIfExists(filepath);
        datasetIOService.save(dataset, filepath.toAbsolutePath().toString());
    }

    public Path createOutputDir(Path filePath){
        Path outputParentDir =  filePath.getParent();
        String filename = RadialProjectionUtils.filenameWithoutExtension(filePath.getFileName().toString());
        Path outputDir = outputParentDir.resolve(filename+"_Out");
        try {
            Files.createDirectories(outputDir);
            IJ.log("Create output dir at: " + outputDir);
        } catch (IOException ex) {
            IJ.log("Fail to create the output dir for image: " + filename);
        }
        return outputDir;
    }

}
