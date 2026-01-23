package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.segmentation.Reconstruction;
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


    public BatchProcessWholeStackWorker(BatchModeModel batchModeModel) {
        this.imageDataList = batchModeModel.getWatershedList();
        this.batchModeModel = batchModeModel;
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
            batchModeModel.addImageDataToRadialProjectionList(imageData);
            IJ.log("Complete vessel detection of file: " + imageData.getImagePath().getFileName().toString());
//            ImagePlus previewImage = imageData.getEdgeCentroidMaskImagePlus().duplicate();
//            previewImage.setTitle(imageData.getImagePath().getFileName().toString());
//            previewImage.show();
            setProgress(counter+=1);
        }
        SaveImageSideViewEdgeCentroid sisvec = new SaveImageSideViewEdgeCentroid(batchModeModel);
        sisvec.execute();
        return null;
    }
}
