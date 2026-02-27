package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializableUtils;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializableUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeGlobalStateModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.StackWindowRefineVesselMultipleChannelsImagePlus;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BatchRefineVesselWorker extends SwingWorker<Void, Void> {
//    private List<ImageData<UnsignedShortType, FloatType>> imageDataList;
//    private BatchModeModel batchModeModel;
    private BatchModeGlobalStateModel batchModeGlobalStateModel;
    private Context context;
    private int singleFileProgress = 0;

    public BatchRefineVesselWorker(BatchModeGlobalStateModel batchModeGlobalStateModel, Context context) {
        this.batchModeGlobalStateModel = batchModeGlobalStateModel;
//        this.imageDataList = this.batchModeModel.getRefineVesselList();
        this.context = context;
    }

    @Override
    protected Void doInBackground() throws Exception {
        IJ.log("Start the refinement step");
        while(!batchModeGlobalStateModel.getRefineVesselQueue().isEmpty()){
            // get the path to imageDataSerializable
            String serFile = batchModeGlobalStateModel.getFirstRefineVesselQueue();
            // load the temp ser file and deserialize, no need to convert to ImageData, get the list to vessel ser file path
            IJ.log("serialized object path: " + serFile);
            ImageDataSerializable imageDataSerializable = ImageDataSerializableUtils.imageDataDeserializeObject(Paths.get(serFile));
            IJ.log("complete deserialization");
            // load the vesselSerFileFilePathList, no need to convert back to Vessel object
            List<String> vesselFilePathList = imageDataSerializable.getVesselSerFilePathList();
            for (String vesselFilePath : vesselFilePathList){
                // deserialize the vesselSerializable object
                VesselSerializable vesselSerializable = VesselSerializableUtils.vesselDeserializeObject(Paths.get(vesselFilePath));
                // get the path to Radial Projection
                String pathMultiChannelsRadialProjection = vesselSerializable.getPathMultiChannelsRadialProjection();
                // load the radial projection using the file path
                ImagePlus radialProjectionMultipleChannels = IJ.openImage(pathMultiChannelsRadialProjection);
                // show the radial projection using custom ImageWindow
                int rectangleToolID = 0;
                CountDownLatch latch = new CountDownLatch(1);
                StackWindowRefineVesselMultipleChannelsImagePlus iwrvmcip = new StackWindowRefineVesselMultipleChannelsImagePlus(radialProjectionMultipleChannels,vesselSerializable);
                // using window adapter to listen to window close to countdown latch
                iwrvmcip.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        vesselSerializable.serializeObject();
                        latch.countDown();
                    }
                });
                latch.await();
            }
            // push the file name to the next queue
            batchModeGlobalStateModel.addLastAnalysisQueue(serFile);
            // remove the file name from the current queue
            batchModeGlobalStateModel.removeFirstRefineVesselQueue();
        }
        return null;
    }
}
