package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.polarprojection.PolarProjection;
import schneiderlab.tools.radialprojection.imageprocessor.core.segmentation.Reconstruction;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.ContourDetection;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.UnrollSingleVessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;

public class BatchProcessWholeStackWorker extends SwingWorker<Void, Void> {
    private List<ImageData<UnsignedShortType, FloatType>> imageDataList;
    private BatchModeModel batchModeModel;
    private int singleFileProgress = 0;
    private Context context;


    public BatchProcessWholeStackWorker(BatchModeModel batchModeModel,  Context context) {
        this.imageDataList = batchModeModel.getWatershedList();
        this.batchModeModel = batchModeModel;
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
        for(ImageData<UnsignedShortType, FloatType> imageData: imageDataList){
            RandomAccessibleInterval<FloatType> hybridStackSmoothed= imageData.getHybridStackSmoothed();
            int hybridStackSmoothedWidth= imageData.getHybridStackSmoothedWidth();
            int hybridStackSmoothedHeight= imageData.getHybridStackSmoothedHeight();
            double vesselRadius= batchModeModel.getInnerVesselRadius();
            List<Point> coordinatesBatch = imageData.getUserSelectedCentroidsList();
            int slideForTuning = 0;
            int pixelScaleINNanometer = batchModeModel.getXyPixelSize();
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
            recon.processWholeStack();
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
//            batchModeModel.addImageDataToRadialProjectionList(imageData);
            IJ.log("Complete vessel detection of file: " + imageData.getImagePath().getFileName().toString());
            IJ.log("Performing Radial projection and unrolling...");
            // TODO: performing radial projection and unrolling without creating a new thread
            // Create copy of hybrid using cursors
            ImagePlus hybridRawStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getHybridStackNonSmoothed(), "Non Smoothed Hybrid Stack");
            ImagePlus hybridSmoothedStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getHybridStackSmoothed(), "Non Smoothed Hybrid Stack");
            // Create copy of Lignin using cursors
            ImagePlus ligninStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getLignin(), "Non Smoothed Lignin Stack");
            // Create copy of cellulose using cursors
            ImagePlus celluloseStack = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getCellulose(), "Non Smoothed Cellulose Stack");
            for (int i = 0; i < vesselArrayList.size(); i++){
                vesselArrayList.get(i).resetCroppedRange(); // reset the range, in case the user has applied a range before, this give the user the option to redo the range selection
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
                // get the hybrid
                ImagePlus hybridImagePlus = vesselArrayList.get(i).getUnrolledVesselHybrid();
                ContourDetection contourDetection = new ContourDetection(hybridImagePlus.getProcessor());
                ByteProcessor contour = contourDetection.process();
                String title = "Contour Vessel " + (i + 1) + " Hybrid";
                ImagePlus contourImagePlus = new ImagePlus(title,contour);
                vesselArrayList.get(i).setContour(contourImagePlus);
            }
            setProgress(counter+=1);
        }
        SaveImageSideViewEdgeCentroid sisvec = new SaveImageSideViewEdgeCentroid(batchModeModel);
        sisvec.execute();
        //TODO: performing saving of the radial projection and unrolling results
        for(ImageData<UnsignedShortType, FloatType> imageData : batchModeModel.getWatershedList()){
            SaveRadialProjectedImageWithoutRefining srpiwr = new SaveRadialProjectedImageWithoutRefining(imageData);
            srpiwr.execute();
            batchModeModel.addImageDataToRefineVesselList(imageData);
        }

        return null;
    }
}
