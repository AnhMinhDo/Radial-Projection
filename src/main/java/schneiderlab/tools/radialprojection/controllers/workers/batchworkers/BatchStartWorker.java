package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.DefaultLinearAxis;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import org.scijava.log.LogService;
import org.scijava.ui.UIService;
import schneiderlab.tools.radialprojection.controllers.workers.ProjectionAndSmoothingWorker;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.createsideview.CreateSideView;
import schneiderlab.tools.radialprojection.imageprocessor.core.segmentation.CreateHybridStack;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BatchStartWorker<T extends RealType<T>> extends SwingWorker<Void, Void> {
    private final int targetXYpixelSize;
    private final int targetZpixelSize;
    private final int ligninToCelluloseWeight;
    private final int windowSizeinMicroMeter;
    private final double  sigmaValueFilter;
    private final double radius;

    private final Context context;
    private ImgPlus<UnsignedShortType> sideViewImgPlus;
    private BatchModeModel batchModeModel;

    public BatchStartWorker(BatchModeModel batchModeModel, Context context) {
        this.targetXYpixelSize = batchModeModel.getXyPixelSize();
        this.targetZpixelSize = batchModeModel.getzPixelSize();
        this.ligninToCelluloseWeight = batchModeModel.getCelluloseToLigninRatio();
        this.windowSizeinMicroMeter = batchModeModel.getAnalysisWindow();
        this.sigmaValueFilter = batchModeModel.getSmoothingSigma();
        this.radius = batchModeModel.getInnerVesselRadius();
//        this.filePath = filePath;
        this.context = context;
        this.batchModeModel = batchModeModel;
    }

    public ImgPlus<UnsignedShortType> getSideViewImgPlus() {
        return sideViewImgPlus;
    }

    @Override
    protected Void doInBackground() {
        // Get DatasetService and UIService from context
//        StatusService statusService = context.getService(StatusService.class);
        DatasetIOService ioService = context.getService(DatasetIOService.class);
        LogService logService = context.getService(LogService.class);
        UIService uiService = context.getService(UIService.class);
        int total = batchModeModel.getTotalNumberOfFiles();
        for(Path filePath: batchModeModel.getFilePathList()) {
            try {
                IJ.log("loading file: " + filePath.getFileName().toString());
                // load the image
                IJ.log("importing image to create side view ....");
                Dataset img = ioService.open(filePath.toAbsolutePath().toString());
                IJ.log("image is imported successfully");
                ImgPlus<T> genericImgPlus = (ImgPlus<T>) img.getImgPlus();
                IJ.log("Creating side view...");
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
                ImageData<UnsignedShortType, FloatType> imageData = new ImageData<>();
                imageData.setXyPixelSize(targetXYpixelSize);
                imageData.setzPixelSize(targetZpixelSize);
                imageData.setImagePath(filePath);
                Path outputDirForThisImage = this.createOutputDir(filePath);
                imageData.setImageOutputPath(outputDirForThisImage);
                imageData.setSideView(sideViewImgPlus);

                // projection and smoothing
                int windowSizeinSlideNumber = Math.round(windowSizeinMicroMeter/0.2f); //TODO: replace 0.2 f with a user-defined number
                CreateHybridStack chs = new CreateHybridStack(context,
                        sideViewImgPlus,
                        ligninToCelluloseWeight,
                        windowSizeinSlideNumber,
                        sigmaValueFilter,
                        radius);

                RandomAccessibleInterval<FloatType> hybridStackSmoothed = chs.process();
                RandomAccessibleInterval<FloatType> hybridStackNonSmoothed = chs.getHybridNonSmoothedStack();
                double radius = chs.getRadius();
                int width = chs.getSmoothedStackWidth();
                int height = chs.getSmoothedStackHeight();
                int slicesNumber = chs.getSmoothedStackSlicesNumber();
                RandomAccessibleInterval<FloatType> cellulose = chs.getCellulose();
                RandomAccessibleInterval<FloatType> lignin = chs.getLignin();
                // set Field for ImageData object
                imageData.setHybridStackNonSmoothed(hybridStackNonSmoothed);
                imageData.setHybridStackSmoothed(hybridStackSmoothed);
                imageData.setHybridStackSmoothedWidth(width);
                imageData.setHybridStackSmoothedHeight(height);
                imageData.setCellulose(cellulose);
                imageData.setLignin(lignin);
                imageData.setHybridStackSmoothedSlicesNumber(slicesNumber);
                batchModeModel.addCentroidSelectionList(imageData);
                batchModeModel.setNumberOfUnprocessedFilePath(batchModeModel.getNumberOfUnprocessedFilePath()-1);
            } catch (IOException e) {
                System.err.println("fail to import image file");
                logService.error("IO error; fail to import image file");
            }
        }
        //TODO: start a new worker here to save the images in this step
        SaveImageSideViewWithoutEdgeCentroid sisvwdc = new SaveImageSideViewWithoutEdgeCentroid(batchModeModel);
        IJ.log("Saving the result in this step ");
        sisvwdc.execute();
        return null;
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
