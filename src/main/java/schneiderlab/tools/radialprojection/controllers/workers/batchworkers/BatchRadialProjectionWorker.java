package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.controllers.workers.RadialProjectionAndUnrollingWorker;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchRadialProjectionWorker extends SwingWorker<Void, Void> {
    private List<ImageData<UnsignedShortType, FloatType>> imageDataList;
    private BatchModeModel batchModeModel;
    private Context context;
    private int singleFileProgress = 0;

    public BatchRadialProjectionWorker(BatchModeModel batchModeModel,
                                       Context context) {
        this.batchModeModel = batchModeModel;
        this.imageDataList = batchModeModel.getRadialProjectionList();
    }

    private void setSingleFileProgress(int progress){
        int oldValue = singleFileProgress;
        singleFileProgress=progress;
        firePropertyChange("singleFileProgress",oldValue, progress );
    }

    @Override
    protected Void doInBackground() throws Exception {
        AtomicInteger counter = new AtomicInteger(); // because multiple threads access the same counter variable, use this to ensure thread-safe modification
        IJ.log("number of files in radial projection queue: " + batchModeModel.getRadialProjectionList().size());
        for (ImageData<UnsignedShortType, FloatType> imageData : imageDataList) {
            IJ.log("Processing file: " + imageData.getImagePath().getFileName().toString());
            // Create copy of hybrid using cursors
            ImagePlus hybridNonSmoothed = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getHybridStackNonSmoothed(), "Non Smoothed Hybrid Stack");
            ImagePlus hybridSmoothed = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getHybridStackSmoothed(), "Non Smoothed Hybrid Stack");
            // Create copy of Lignin using cursors
            ImagePlus lignin = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getLignin(), "Non Smoothed Lignin Stack");
            // Create copy of cellulose using cursors
            ImagePlus cellulose = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    imageData.getCellulose(), "Non Smoothed Cellulose Stack");
            RadialProjectionAndUnrollingWorker radialProjectionAndUnrollingWorker = new RadialProjectionAndUnrollingWorker(
                    hybridNonSmoothed,
                    hybridSmoothed,
                    cellulose,
                    lignin,
                    imageData.getEdgeBinaryMaskImagePlus(),
                    imageData.getVesselList(),
                    (int)imageData.getXyPixelSize()/1000.0,
                    true,
                    context
            );
            radialProjectionAndUnrollingWorker.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if("state".equals(evt.getPropertyName()) &&
                                                    evt.getNewValue() == StateValue.DONE){
                        // need to update the counter variable
                        setProgress(counter.incrementAndGet());
                        // show the image
//                        for (int i = 0; i < imageData.getVesselList().size(); i++) {
//                            Vessel vessel = imageData.getVesselList().get(i);
//                            ImagePlus previewRadialProjection = vessel.getRadialProjectionHybrid();
//                            previewRadialProjection.setTitle("Vessel " + i + " " + imageData.getImagePath().getFileName().toString());
//                            previewRadialProjection.show();
//                        }
                        SaveRadialProjectedImageWithoutRefining srpiwr = new SaveRadialProjectedImageWithoutRefining(imageData);
                        srpiwr.execute();
                        batchModeModel.addImageDataToRefineVesselList(imageData);
                    }
                }
            });
            radialProjectionAndUnrollingWorker.execute();
        }
        return null;
    }
}
