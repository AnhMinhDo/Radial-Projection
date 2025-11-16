package schneiderlab.tools.radialprojection.controllers.uiaction.vesselsegmentation;

import ij.ImagePlus;
import org.scijava.Context;
import org.scijava.log.LogService;
import schneiderlab.tools.radialprojection.controllers.workers.SegmentWholeStackWorker;
import schneiderlab.tools.radialprojection.models.radialprojection.VesselsSegmentationModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProcessWholeStackButtonAction implements ActionListener {
    private final MainView mainView;
    private final VesselsSegmentationModel vesselsSegmentationModel;
    private final Context context;
    private final LogService logService;

    public ProcessWholeStackButtonAction(MainView mainView,
                                      VesselsSegmentationModel vesselsSegmentationModel,
                                      Context context) {
        this.mainView = mainView;
        this.vesselsSegmentationModel = vesselsSegmentationModel;
        this.context=context;
        this.logService=context.getService(LogService.class);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        vesselsSegmentationModel.getImpInByte().close();
        mainView.getTextField2StatusVesselSegmentation().setText("Processing whole stack...");
        mainView.getProgressBarVesselSegmentation().setValue(0);
        SegmentWholeStackWorker batchSegmentationWorker = new SegmentWholeStackWorker(
                vesselsSegmentationModel.getImageData().getHybridStackSmoothed(),
                vesselsSegmentationModel.getImageData().getHybridStackSmoothedWidth(),
                vesselsSegmentationModel.getImageData().getHybridStackSmoothedHeight(),
                (double)mainView.getSpinnerInnerVesselRadius().getValue(),
                vesselsSegmentationModel.getCoordinatesBatch(),
                vesselsSegmentationModel.getSliceIndexForTuning(),
                (int)mainView.getSpinnerXYPixelSizeCreateSideView().getValue()
        );
        batchSegmentationWorker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if("progress".equals(propertyChangeEvent.getPropertyName())){
                    int currentProgressValue = (int) propertyChangeEvent.getNewValue();
                    mainView.getProgressBarVesselSegmentation().setValue(currentProgressValue);
                    mainView.getProgressBarVesselSegmentation().setToolTipText(currentProgressValue + "%");
                }
                if ("state".equals(propertyChangeEvent.getPropertyName()) &&
                        propertyChangeEvent.getNewValue() == SwingWorker.StateValue.DONE){
                    ImagePlus finalSegmentation=batchSegmentationWorker.getFinalSegmentation();
                    vesselsSegmentationModel.getImageData().setEdgeBinaryMaskImagePlus(batchSegmentationWorker.getEdgeBinaryMaskImagePlus());
                    vesselsSegmentationModel.getImageData().setCentroidHashMap(batchSegmentationWorker.getCentroidHashMap());
                    vesselsSegmentationModel.getImageData().setVesselList(batchSegmentationWorker.getVesselArrayList());
                    vesselsSegmentationModel.getImageData().setRawSegmentation(batchSegmentationWorker.getFinalSegmentation());
                    vesselsSegmentationModel.getImageData().setEdgeCentroidMaskImagePlus(batchSegmentationWorker.getEdgeCentroidMaskImagePlus());
//                            ImagePlus hybridStackWithEdgeCentroidOverlay = batchSegmentationWorker.getStackWithVesselEdgeCentroidOverlay();
                    mainView.getTextField2StatusVesselSegmentation().setText("Complete processing whole stack ");
                    mainView.getButtonMoveToRadialProjection().setEnabled(true);
                    mainView.getProgressBarVesselSegmentation().setValue(100);
                    mainView.getProgressBarVesselSegmentation().setToolTipText(100+"%");
                    // show the results to users
                    ImagePlus segmentedStack = batchSegmentationWorker.getFinalSegmentation().duplicate();
                    segmentedStack.setTitle(batchSegmentationWorker.getFinalSegmentation().getTitle());
                    vesselsSegmentationModel.setRawSegmentation(segmentedStack);
                    ImagePlus edgeCentroidMask = batchSegmentationWorker.getEdgeCentroidMaskImagePlus().duplicate();
                    edgeCentroidMask.setTitle(batchSegmentationWorker.getEdgeCentroidMaskImagePlus().getTitle());
                    vesselsSegmentationModel.setEdgeCentroidMaskImagePlus(edgeCentroidMask);
                    segmentedStack.show();
                    edgeCentroidMask.show();
                }
            }
        });
        batchSegmentationWorker.execute();
    }
}
