package schneiderlab.tools.radialprojection.controllers.uiaction.radialprojection;

import ij.ImagePlus;
import ij.gui.Toolbar;
import org.scijava.Context;
import org.scijava.log.LogService;
import schneiderlab.tools.radialprojection.controllers.workers.RadialProjectionAndUnrollingWorker;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.models.radialprojection.RadialProjectionModel;
import schneiderlab.tools.radialprojection.models.radialprojection.VesselsSegmentationModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.ImageWindowGroupController;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RadialProjectionAndUnrollingButtonAction implements ActionListener {
    private final MainView mainView;
    private final RadialProjectionModel radialProjectionModel;
    private final Context context;
    private final LogService logService;

    public RadialProjectionAndUnrollingButtonAction(MainView mainView,
                                         RadialProjectionModel radialProjectionModel,
                                         Context context) {
        this.mainView = mainView;
        this.radialProjectionModel = radialProjectionModel;
        this.context=context;
        this.logService=context.getService(LogService.class);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // update the status bar
        mainView.getTextFieldStatusRadialProjection().setText("Radial Projection And Unrolling...");
        mainView.getProgressBarRadialProjection().setValue(0);
        // Create copy of hybrid using cursors
        ImagePlus hybridNonSmoothed = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                radialProjectionModel.getImageData().getHybridStackNonSmoothed(), "Non Smoothed Hybrid Stack");
        ImagePlus hybridSmoothed = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                radialProjectionModel.getImageData().getHybridStackSmoothed(), "Non Smoothed Hybrid Stack");
        // Create copy of Lignin using cursors
        ImagePlus lignin = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                radialProjectionModel.getImageData().getLignin(), "Non Smoothed Lignin Stack");
        // Create copy of cellulose using cursors
        ImagePlus cellulose = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                radialProjectionModel.getImageData().getCellulose(), "Non Smoothed Cellulose Stack");
        RadialProjectionAndUnrollingWorker radialProjectionAndUnrollingWorker = new RadialProjectionAndUnrollingWorker(
                hybridNonSmoothed,
                hybridSmoothed,
                cellulose,
                lignin,
                radialProjectionModel.getImageData().getEdgeBinaryMaskImagePlus(),
                radialProjectionModel.getImageData().getVesselList(),
                (int)mainView.getSpinnerXYPixelSizeCreateSideView().getValue()/1000.0,
                true,
                context
        );
        radialProjectionAndUnrollingWorker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if("progress".equals(evt.getPropertyName())){
                    mainView.getProgressBarRadialProjection().setValue((int)evt.getNewValue());
                }
                if ("state".equals(evt.getPropertyName()) &&
                        evt.getNewValue() == SwingWorker.StateValue.DONE){
                    mainView.getTextFieldStatusRadialProjection().setText("Radial Projection  and Unrolling Complete");
                    mainView.getProgressBarRadialProjection().setValue(100);
                    // show the results to users
                    Toolbar toolbar = new Toolbar();
                    int toolIdForCropping = Toolbar.RECTANGLE;
                    // show the radial Projection result
                    for (int i = 0; i < radialProjectionModel.getImageData().getVesselList().size(); i++) { // for each vessel
                        ImagePlus rpCellulose = radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionCellulose().duplicate();
                        rpCellulose.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionCellulose().getTitle());
                        ImagePlus rpHybrid = radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionHybrid().duplicate();
                        rpHybrid.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionHybrid().getTitle());
                        ImagePlus rpLignin = radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionLignin().duplicate();
                        rpLignin.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getRadialProjectionLignin().getTitle());
                        List<ImagePlus> imagePlusList = new ArrayList<>(Arrays.asList(rpLignin, rpCellulose, rpHybrid));
                        ImageWindowGroupController iwgc = new ImageWindowGroupController(imagePlusList,
                                radialProjectionModel.getImageData().getVesselList().get(i),
                                toolIdForCropping);
                        radialProjectionModel.addVesselRadialProjectionImageWindowGroup(iwgc);
                    }
                    // show the unrolling result
                    for (int i = 0; i < radialProjectionModel.getImageData().getVesselList().size(); i++) {
                        ImagePlus urCellulose = radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselCellulose().duplicate();
                        urCellulose.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselCellulose().getTitle());
                        ImagePlus urHybrid = radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselHybrid().duplicate();
                        urHybrid.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselHybrid().getTitle());
                        ImagePlus urLignin = radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselLignin().duplicate();
                        urLignin.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getUnrolledVesselLignin().getTitle());
                        ImagePlus urContour = radialProjectionModel.getImageData().getVesselList().get(i).getContour().duplicate();
                        urContour.setTitle(radialProjectionModel.getImageData().getVesselList().get(i).getContour().getTitle());
                        List<ImagePlus> imagePlusList = new ArrayList<>(Arrays.asList(urLignin, urCellulose, urHybrid, urContour));
                        ImageWindowGroupController iwgc = new ImageWindowGroupController(imagePlusList,
                                radialProjectionModel.getImageData().getVesselList().get(i),
                                toolIdForCropping);
                        radialProjectionModel.addVesselUnrollImageWindowGroup(iwgc);
                    }

                    mainView.getButtonMoveToAnalysis().setEnabled(true);
                }
            }
        });
        radialProjectionAndUnrollingWorker.execute();
    }
}
