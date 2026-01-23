package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.ImagePlus;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.ImageWindowGroupController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BatchRefineVesselWorker extends SwingWorker<Void, Void> {
    private List<ImageData<UnsignedShortType, FloatType>> imageDataList;
    private BatchModeModel batchModeModel;
    private Context context;
    private int singleFileProgress = 0;

    public BatchRefineVesselWorker(BatchModeModel batchModeModel) {
        this.batchModeModel = batchModeModel;
        this.imageDataList = this.batchModeModel.getRefineVesselList();
    }

    @Override
    protected Void doInBackground() throws Exception {
        int total = imageDataList.size();
        for(ImageData<UnsignedShortType, FloatType> imageData : imageDataList){
            for (int i = 0; i < imageData.getVesselList().size(); i++) {
                Vessel vessel = imageData.getVesselList().get(i);
                // view the Vessel image with imageWindowGroupController
                int rectangleToolID = 0;
                ImagePlus rpHybrid = vessel.getRadialProjectionHybrid();
                ImagePlus rpLignin = vessel.getRadialProjectionLignin();
                ImagePlus rpCellulose = vessel.getRadialProjectionCellulose();
                ImagePlus urHybrid = vessel.getUnrolledVesselHybrid();
                ImagePlus urLignin = vessel.getUnrolledVesselLignin();
                ImagePlus urCellulose = vessel.getRadialProjectionCellulose();
                List<ImagePlus> imagelist = Arrays.asList(rpHybrid,rpLignin,rpCellulose,urHybrid,urLignin,urCellulose);
                ImageWindowGroupController iwgc = new ImageWindowGroupController(imagelist,vessel,rectangleToolID);
                CountDownLatch countDownLatch = new CountDownLatch(1);
                rpHybrid.getWindow().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        iwgc.closeAllWindowInGroup();
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
            }
            batchModeModel.addImageDataToAnalysisBatchList(imageData);
            batchModeModel.setNumberOfImageDataInRefineVesselStep(batchModeModel.getNumberOfImageDataInRefineVesselStep()-1);
        }

        return null;
    }
}
