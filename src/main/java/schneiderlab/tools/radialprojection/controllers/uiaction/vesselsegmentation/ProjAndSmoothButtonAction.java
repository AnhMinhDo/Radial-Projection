package schneiderlab.tools.radialprojection.controllers.uiaction.vesselsegmentation;

import ij.ImagePlus;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.controllers.workers.ProjectionAndSmoothingWorker;
import schneiderlab.tools.radialprojection.models.radialprojection.VesselsSegmentationModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProjAndSmoothButtonAction implements ActionListener {
    private final MainView mainView;
    private final VesselsSegmentationModel vesselsSegmentationModel;
    private final Context context;

    public ProjAndSmoothButtonAction(MainView mainView,
                                     VesselsSegmentationModel vesselsSegmentationModel,
                                     Context context) {
        this.mainView = mainView;
        this.vesselsSegmentationModel = vesselsSegmentationModel;
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainView.getTextField2StatusVesselSegmentation().setText("Creating hybrid stack and smoothing...");
        mainView.getButtonSelectCentroid().setEnabled(false);
        mainView.getButtonWatershed().setEnabled(false);
        mainView.getButtonProcessWholeStack().setEnabled(false);
        mainView.getButtonMoveToRadialProjection().setEnabled(false);
        ProjectionAndSmoothingWorker pasw = new ProjectionAndSmoothingWorker(vesselsSegmentationModel.getImageData().getSideView(),
                mainView.getSliderHybridWeight().getValue(),
                (int)mainView.getSpinnerAnalysisWindow().getValue(),
                (double) mainView.getSpinnerPreWatershedSmoothing().getValue(),
                (double) mainView.getSpinnerInnerVesselRadius().getValue(),
                context);
        pasw.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("progress".equals(evt.getPropertyName())){
                    int currentProgress = (int)evt.getNewValue();
                    mainView.getProgressBarVesselSegmentation().setValue(currentProgress);
                    mainView.getProgressBarVesselSegmentation().setToolTipText(String.valueOf(currentProgress));
                }
                if ("state".equals(evt.getPropertyName()) &&
                        evt.getNewValue() == SwingWorker.StateValue.DONE){
                    vesselsSegmentationModel.setHybridStackNonSmoothed(pasw.getHybridStackNonSmoothed());
                    vesselsSegmentationModel.setHybridStackSmoothed(pasw.getHybridStackSmoothed());
                    vesselsSegmentationModel.setHybridStackSmoothedWidth(pasw.getWidth());
                    vesselsSegmentationModel.setHybridStackSmoothedHeight(pasw.getHeight());
                    vesselsSegmentationModel.setCellulose(pasw.getCellulose());
                    vesselsSegmentationModel.setLignin(pasw.getLignin());
                    vesselsSegmentationModel.setHybridStackSmoothedSlicesNumber(pasw.getSlicesNumber());
                    // set Field for ImageData object
                    vesselsSegmentationModel.getImageData().setHybridStackNonSmoothed(pasw.getHybridStackNonSmoothed());
                    vesselsSegmentationModel.getImageData().setHybridStackSmoothed(pasw.getHybridStackSmoothed());
                    vesselsSegmentationModel.getImageData().setHybridStackSmoothedWidth(pasw.getWidth());
                    vesselsSegmentationModel.getImageData().setHybridStackSmoothedHeight(pasw.getHeight());
                    vesselsSegmentationModel.getImageData().setCellulose(pasw.getCellulose());
                    vesselsSegmentationModel.getImageData().setLignin(pasw.getLignin());
                    vesselsSegmentationModel.getImageData().setHybridStackSmoothedSlicesNumber(pasw.getSlicesNumber());
                    ImagePlus hybridStackNonSmoothedDisplay = ImageJFunctions.show(vesselsSegmentationModel.getImageData().getHybridStackNonSmoothed(),"Raw Hybrid");
                    vesselsSegmentationModel.setHybridStackNonSmoothedDisplay(hybridStackNonSmoothedDisplay);
                    ImagePlus hybridStackSmoothedDisplay = ImageJFunctions.show(vesselsSegmentationModel.getImageData().getHybridStackSmoothed(), "Smoothed Hybrid");
                    vesselsSegmentationModel.setHybridStackSmoothedDisplay(hybridStackSmoothedDisplay);
                    // update UI
                    mainView.getTextField2StatusVesselSegmentation().setText("Complete Projection and Smoothing");
                    mainView.getButtonSelectCentroid().setEnabled(true);
                }
            }
        });
        pasw.execute();
    }
}
