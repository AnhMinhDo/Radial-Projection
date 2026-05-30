package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ByteProcessor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.CurrentImageStage;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializableUtils;
import schneiderlab.tools.radialprojection.imageprocessor.core.polarprojection.PolarProjection;
import schneiderlab.tools.radialprojection.imageprocessor.core.segmentation.Reconstruction;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.ContourDetection;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.UnrollSingleVessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializableOnlySliceInfo;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializableUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeGlobalStateModel;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class BatchProcessWholeStackWorker extends SwingWorker<Void, Void> {
    private List<ImageData<UnsignedShortType, UnsignedShortType>> imageDataList;
    private BatchModeModel batchModeModel;
    private BatchModeGlobalStateModel batchModeGlobalStateModel;
    private int singleFileProgress = 0;
    private Context context;


    public BatchProcessWholeStackWorker(BatchModeModel batchModeModel, BatchModeGlobalStateModel batchModeGlobalStateModel, Context context) {
        this.imageDataList = batchModeModel.getWatershedList();
        this.batchModeModel = batchModeModel;
        this.batchModeGlobalStateModel = batchModeGlobalStateModel;
        this.context = context;
    }

    private void setSingleFileProgress(int progress){
        int oldValue = singleFileProgress;
        singleFileProgress=progress;
        firePropertyChange("singleFileProgress",oldValue, progress );
    }

    @Override
    protected Void doInBackground() throws Exception {
        int counter = 0;
        while(!batchModeGlobalStateModel.getWatershedAndRadialProjectionQueue().isEmpty())
        {
            String serFile = batchModeGlobalStateModel.getFirstWatershedAndRadialProjectionQueue();
            IJ.log("serialized object path: " + serFile);
            ImageDataSerializable imageDataSerializable = ImageDataSerializableUtils.imageDataDeserializeObject(Paths.get(serFile));
            IJ.log("complete deserialization");
            ImageData<UnsignedShortType, UnsignedShortType> imageData = ImageDataSerializableUtils.convertSerializableToImageData(imageDataSerializable, CurrentImageStage.WatershedAndRadialProjection,context);
            IJ.log("complete conversion to ImageData object");
            RandomAccessibleInterval<UnsignedShortType> hybridStackSmoothed= imageData.getHybridStackSmoothed();
            int hybridStackSmoothedWidth= imageData.getHybridStackSmoothedWidth();
            int hybridStackSmoothedHeight= imageData.getHybridStackSmoothedHeight();
            double vesselRadius= imageData.getInnerVesselRadius();
            List<Point> coordinatesBatch = imageData.getUserSelectedCentroidsList();
            int slideForTuning = 0;
            int pixelScaleINNanometer = imageData.getXyPixelSize();
            int totalNumOfSlice = (int)hybridStackSmoothed.dimension(2);
            IJ.log("Processing file: " + imageData.getImagePath().getFileName().toString());
            Reconstruction recon = new Reconstruction(hybridStackSmoothed,
                    hybridStackSmoothedWidth,
                    hybridStackSmoothedHeight,
                    vesselRadius,
                    coordinatesBatch,
                    slideForTuning,
                    pixelScaleINNanometer
            );
            recon.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if("currentSlice".equals(evt.getPropertyName())){
                        int currentSlice = (int) evt.getNewValue();
                        int currentProgress=(int)Math.floor(currentSlice*(double)(100.0/totalNumOfSlice));
                        setSingleFileProgress(currentProgress);
                    }
                }
            });
            IJ.log("start processing whole stack");
            recon.processWholeStack();
            IJ.log("finish");
            ImagePlus edgeBinaryMaskImagePlus=recon.getEdgeBinaryMaskImagePlus();
            HashMap<Integer, List<Point>> centroidHashMap=recon.getCentroidHashMap();
            ImagePlus edgeCentroidImagePlus= recon.getEdgeCentroidMaskImgPlus();
            List<Vessel> vesselArrayList =recon.getVesselsArray();
            ImagePlus rawSegmentation= recon.getRawSegmentation();
            imageData.setEdgeBinaryMaskImagePlus(edgeBinaryMaskImagePlus);
            imageData.setCentroidHashMap(centroidHashMap);
            imageData.setVesselList(vesselArrayList);
            imageData.setRawSegmentation(rawSegmentation);
            imageData.setEdgeCentroidMaskImagePlus(edgeCentroidImagePlus);
            IJ.log("Complete vessel detection of file: " + imageData.getImagePath().getFileName().toString());
            IJ.log("Performing Radial projection and unrolling...");
            // Create copy of hybrid using cursors
            ImagePlus hybridRawStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getHybridStackNonSmoothed(), "Non Smoothed Hybrid Stack");
            ImagePlus hybridSmoothedStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getHybridStackSmoothed(), "Smoothed Hybrid Stack");
            // Create copy of Lignin using cursors
            ImagePlus ligninStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getLignin(), "Non Smoothed Lignin Stack");
            // Create copy of cellulose using cursors
            ImagePlus celluloseStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getCellulose(), "Non Smoothed Cellulose Stack");
            IJ.log("finish preparing input for Radial projection step");
            for (int i = 0; i < vesselArrayList.size(); i++){
                vesselArrayList.get(i).resetCroppedRange(); // reset the range, in case the user has applied a range before, this give the user the option to redo the range selection
                vesselArrayList.get(i).setNoOfRandomBox(imageData.getNoOfRandomBox());
                vesselArrayList.get(i).setRandomBoxWidth(imageData.getRandomBoxWidth());
                vesselArrayList.get(i).setLengthOfLineScan(imageData.getLengthOfLineScan());
                vesselArrayList.get(i).setNoOfRandomLineScan(imageData.getNoOfRandomLineScan());
                // Perform polar projection
                PolarProjection polarProjectionHybrid = new PolarProjection(hybridSmoothedStack,
                        hybridRawStack,
                        imageData.getEdgeBinaryMaskImagePlus(),
                        vesselArrayList.get(i),
                        5, // 5 degree is considered adequately small angle
                        imageData.getXyPixelSize()/1000.0,
                        context);
                PolarProjection polarProjectionCellulose = new PolarProjection(hybridSmoothedStack,celluloseStack,
                        imageData.getEdgeBinaryMaskImagePlus(),
                        vesselArrayList.get(i),
                        5, // 5 degree is considered adequately small angle
                        imageData.getXyPixelSize()/1000.0,
                        context);
                PolarProjection polarProjectionLignin = new PolarProjection(hybridSmoothedStack,
                        ligninStack,
                        imageData.getEdgeBinaryMaskImagePlus(),
                        vesselArrayList.get(i),
                        5, // 5 degree is considered adequately small angle
                        imageData.getXyPixelSize()/1000.0,
                        context);
                IJ.log("radial projection hybrid stack, vessel " + (i+1));
                ImagePlus vesselPolarProjectionHybrid=polarProjectionHybrid.process();
                IJ.log("radial projection cellulose stack, vessel " + (i+1));
                ImagePlus vesselPolarProjectionCellulose=polarProjectionCellulose.process();
                IJ.log("radial projection lignin stack, vessel " + (i+1));
                ImagePlus vesselPolarProjectionLignin=polarProjectionLignin.process();
                String imageTitleHybrid = "Radial Projection Vessel " + (i + 1) + " Hybrid";
                String imageTitleCellulose = "Radial Projection Vessel " + (i + 1) + " Cellulose channel";
                String imageTitleLignin = "Radial Projection Vessel " + (i + 1) + " Lignin channel";
                vesselPolarProjectionHybrid.setTitle(imageTitleHybrid);
                vesselPolarProjectionCellulose.setTitle(imageTitleCellulose);
                vesselPolarProjectionLignin.setTitle(imageTitleLignin);
                // perform flipping of the vessel
                vesselPolarProjectionHybrid.getProcessor().flipHorizontal();
                vesselPolarProjectionLignin.getProcessor().flipHorizontal();
                vesselPolarProjectionCellulose.getProcessor().flipHorizontal();
                // add the result to the object
                vesselArrayList.get(i).setRadialProjectionHybrid(vesselPolarProjectionHybrid);
                vesselArrayList.get(i).setRadialProjectionLignin(vesselPolarProjectionLignin);
                vesselArrayList.get(i).setRadialProjectionCellulose(vesselPolarProjectionCellulose);
                // combine all the radial projection
                ImageStack imageStackRadialProjection = new ImageStack(vesselPolarProjectionHybrid.getWidth(), vesselPolarProjectionHybrid.getHeight());
                imageStackRadialProjection.addSlice(vesselPolarProjectionLignin.getProcessor());
                imageStackRadialProjection.addSlice(vesselPolarProjectionCellulose.getProcessor());
                imageStackRadialProjection.addSlice(vesselPolarProjectionHybrid.getProcessor());
                ImagePlus combineRadialProjection = new ImagePlus("RadialProjection_Vessel_"+(i+1),imageStackRadialProjection);
                FileSaver radialProjectionSaver = new FileSaver(combineRadialProjection);

                // create objects for Unrolling class
                UnrollSingleVessel unrolledLignin = new UnrollSingleVessel(hybridSmoothedStack,
                        ligninStack,
                        imageData.getEdgeBinaryMaskImagePlus(),
                        vesselArrayList.get(i).getCentroidArrayList(),
                        5 // 5 degree is considered adequately small angle
                );
                UnrollSingleVessel unrolledCellulose = new UnrollSingleVessel(hybridSmoothedStack,
                        celluloseStack,
                        imageData.getEdgeBinaryMaskImagePlus(),
                        vesselArrayList.get(i).getCentroidArrayList(),
                        5 // 5 degree is considered adequately small angle
                );
                UnrollSingleVessel unrolledHybrid = new UnrollSingleVessel(hybridSmoothedStack,
                        hybridRawStack,
                        imageData.getEdgeBinaryMaskImagePlus(),
                        vesselArrayList.get(i).getCentroidArrayList(),
                        5 // 5 degree is considered adequately small angle
                );
                // start unrolling
                IJ.log("unrolling hybrid stack, vessel " + (i+1));
                ImagePlus vesselUnrolledHybrid=unrolledHybrid.process();
                IJ.log("unrolling lignin stack, vessel " + (i+1));
                ImagePlus vesselUnrolledLignin=unrolledLignin.process();
                IJ.log("unrolling cellulose stack, vessel " + (i+1));
                ImagePlus vesselUnrolledCellulose=unrolledCellulose.process();
                String imageTitleHybridUnrolling = "Unrolled Vessel " + (i + 1) + " Hybrid";
                String imageTitleCelluloseUnrolling = "Unrolled Vessel " + (i + 1) + " Cellulose channel";
                String imageTitleLigninUnrolling = "Unrolled Vessel " + (i + 1) + " Lignin channel";
                vesselUnrolledHybrid.setTitle(imageTitleHybridUnrolling);
                vesselUnrolledLignin.setTitle(imageTitleLigninUnrolling);
                vesselUnrolledCellulose.setTitle(imageTitleCelluloseUnrolling);
                // flip the unrolling
                vesselUnrolledHybrid.getProcessor().flipHorizontal();
                vesselUnrolledLignin.getProcessor().flipHorizontal();
                vesselUnrolledCellulose.getProcessor().flipHorizontal();
                // add the unrolling result to the object
                vesselArrayList.get(i).setUnrolledVesselHybrid(vesselUnrolledHybrid);
                vesselArrayList.get(i).setUnrolledVesselLignin(vesselUnrolledLignin);
                vesselArrayList.get(i).setUnrolledVesselCellulose(vesselUnrolledCellulose);
                // Contour tracing hybrid channel
                IJ.log("start the contour detection");
                ImagePlus hybridImagePlus = vesselArrayList.get(i).getUnrolledVesselHybrid();
                ContourDetection contourDetection = new ContourDetection(hybridImagePlus.getProcessor());
                ByteProcessor contour = contourDetection.process();
                String title = "Contour Vessel " + (i + 1) + " Hybrid";
                ImagePlus contourImagePlus = new ImagePlus(title,contour);
                vesselArrayList.get(i).setContour(contourImagePlus);
                // combine all the unrolling
                ImageStack imageStackUnrolling = new ImageStack(vesselUnrolledHybrid.getWidth(), vesselUnrolledHybrid.getHeight());
                imageStackUnrolling.addSlice(vesselUnrolledLignin.getProcessor());
                imageStackUnrolling.addSlice(vesselUnrolledCellulose.getProcessor());
                imageStackUnrolling.addSlice(vesselUnrolledHybrid.getProcessor());
                ImagePlus combineUnrolling = new ImagePlus("unrolled_Vessel_"+(i+1),imageStackUnrolling);
                FileSaver unrollingSaver = new FileSaver(combineUnrolling);
                // create the path for the radialProjection and unrolling output
                Path outputFolder = imageData.getImageOutputPath();
                Path tempDirPath = imageData.getTempDirPath();
                String radialProjectionFileName = "Radial_Projection_Vessel_" + (i+1) + ".tif";
                String unrollingFileName = "Unrolled_Vessel_"+(i+1)+".tif";
                Path radialProjectionPath = outputFolder.resolve(radialProjectionFileName);
                IJ.log(radialProjectionPath.toString());
                Path radialProjectionTempPath = tempDirPath.resolve(radialProjectionFileName);
                IJ.log(radialProjectionTempPath.toString());
                Path unrollingPath = outputFolder.resolve(unrollingFileName);
                Path unrollingTempPath = tempDirPath.resolve(unrollingFileName);
                vesselArrayList.get(i).setRadialProjectionPath(radialProjectionPath);
                vesselArrayList.get(i).setUnrollingPath(unrollingPath);
                vesselArrayList.get(i).setRadialProjectionsTempPath(radialProjectionTempPath);
                vesselArrayList.get(i).setUnrollingTempPath(unrollingTempPath);
                // create the path for the serializable vessel object
                // temp folder, with name as vessel_i, i is the index of the vessel
                Path vesselSerPath = tempDirPath.resolve("vessel_"+(i+1)+".ser");
                Path vesselSliceInfoSerPath = tempDirPath.resolve("vessel_"+(i+1)+ "_slice_info" + ".ser");
                vesselArrayList.get(i).setDirectoryPath(tempDirPath);
                vesselArrayList.get(i).setSerializableObjectPath(vesselSerPath);
                vesselArrayList.get(i).setSerializableObjectOnlySliceInfoPath(vesselSliceInfoSerPath);
                // Create serializable from the vessel object
                IJ.log("creating vessel-serializable objects");
                VesselSerializable vesselSerializable = VesselSerializableUtils.convertVesselToSerializable(vesselArrayList.get(i));
                VesselSerializableOnlySliceInfo vesselSerializableOnlySliceInfo = VesselSerializableUtils.convertVesselToVesselSerializableOnlySliceInfo(vesselArrayList.get(i));
                IJ.log("complete creating vessel-serializable objects");
                // add vessel ser file Path to the imageData
                imageData.addPathToVesselSerFilePathList(vesselSerPath);
                // save the Vessel ser files
                IJ.log("performing serialization");
                vesselSerializable.serializeObject();
                vesselSerializableOnlySliceInfo.serializeObject();
                IJ.log("serialization completed");
                // Save the radial projection to temp folder and output folder
                radialProjectionSaver.saveAsTiff(radialProjectionPath.toString());
                radialProjectionSaver.saveAsTiff(radialProjectionTempPath.toString());
                // save the unrolling
                unrollingSaver.saveAsTiff(unrollingPath.toString());
                unrollingSaver.saveAsTiff(unrollingTempPath.toString());
            }
//            setProgress(counter+=1);
            // save the image ser file
            ImageDataSerializable imageDataSerializable1 = ImageDataSerializableUtils.convertImageDataToSerializable(imageData);
            imageDataSerializable1.serializeObject();
            // update the queues
            batchModeGlobalStateModel.removeFirstWatershedAndRadialProjectionQueue();
            batchModeGlobalStateModel.addLastRefineVesselQueue(serFile);
        }
        return null;
    }
}
