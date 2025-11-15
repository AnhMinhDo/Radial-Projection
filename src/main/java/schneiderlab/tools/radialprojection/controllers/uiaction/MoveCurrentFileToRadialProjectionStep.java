package schneiderlab.tools.radialprojection.controllers.uiaction;

import ij.ImagePlus;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import org.scijava.ui.UIService;
import schneiderlab.tools.radialprojection.controllers.workers.SaveImageSegmentationWorker;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.models.radialprojection.RadialProjectionModel;
import schneiderlab.tools.radialprojection.models.radialprojection.VesselsSegmentationModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class MoveCurrentFileToRadialProjectionStep implements ActionListener {
    private final JTable table;
    private final JTextField textField;
    private final JPanel radialProjectionPanel;
    private final JButton radialProjectionButton;
    private Path outputPath;
    private Path inputImagePath;
    private ImagePlus hybridStackNonSmoothed;
    private ImagePlus ligninStackNonSmoothed;
    private ImagePlus celluloseStackNonSmoothed;
    private ImagePlus contourAndCentroid;
    private ImagePlus rawVesselSegmentation;
    private ImagePlus smoothSideView;
    private final int numberOfWalkSum;
    private VesselsSegmentationModel vesselsSegmentationModel;
    private JTextField textfieldOutput;
    private final Context context;
    private final MainView mainview;
    private ImageData<UnsignedShortType, FloatType> imageData;
    private final RadialProjectionModel radialProjectionModel;
    private UIService uiService;

    public MoveCurrentFileToRadialProjectionStep(JTable table,
                                                 JTextField textFieldRadicalProjectionStep,
                                                 JPanel radialProjectionPanel,
                                                 JButton radialProjectionButton,
                                                 JTextField textfieldOutput,
                                                 VesselsSegmentationModel vesselsSegmentationModel,
                                                 int numberOfWalkSum,
                                                 Context context,
                                                 MainView mainview,
                                                 RadialProjectionModel radialProjectionModel) {
        this.table = table;
        this.textField = textFieldRadicalProjectionStep;
        this.radialProjectionPanel = radialProjectionPanel;
        this.radialProjectionButton = radialProjectionButton;
        this.textfieldOutput= textfieldOutput;
        this.vesselsSegmentationModel=vesselsSegmentationModel;
        this.numberOfWalkSum = numberOfWalkSum;
        this.context=context;
        this.mainview=mainview;
        this.radialProjectionModel=radialProjectionModel;
        this.uiService=context.getService(UIService.class);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // combine the non-smooth side view to stack
        // save the result of the segmentation step
        SaveImageSegmentationWorker saveImageWorker = new SaveImageSegmentationWorker(
                mainview,
                vesselsSegmentationModel,
                this.context);
        saveImageWorker.execute();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (model.getRowCount() > 0) {
//            String currentFilePathString = (String) model.getValueAt(0,0);
            model.removeRow(0);
        }
        radialProjectionModel.setImageData(vesselsSegmentationModel.getImageData());
        textField.setText(radialProjectionModel.getImageData().getImagePath().getFileName().toString());
        // add the parameters for this image to the object
        radialProjectionModel.getImageData().setXyPixelSize(vesselsSegmentationModel.getXyPixelSize());
        radialProjectionModel.getImageData().setzPixelSize(vesselsSegmentationModel.getzPixelSize());
        radialProjectionModel.getImageData().setCelluloseToLigninRatio(vesselsSegmentationModel.getCelluloseToLigninRatio());
        radialProjectionModel.getImageData().setAnalysisWindow(vesselsSegmentationModel.getAnalysisWindow());
        radialProjectionModel.getImageData().setSmoothingSigma(vesselsSegmentationModel.getSmoothingSigma());
        radialProjectionModel.getImageData().setInnerVesselRadius(vesselsSegmentationModel.getInnerVesselRadius());
        CardLayout card = mainview.getMainPanelCardLayout();
        card.show(mainview.getPanelMainRight(),"card3");
        radialProjectionButton.setEnabled(true);
        mainview.getButtonProjAndSmooth().setEnabled(false);
        mainview.getButtonSelectCentroid().setEnabled(false);
        mainview.getButtonWatershed().setEnabled(false);
        mainview.getButtonProcessWholeStack().setEnabled(false);
        mainview.getButtonMoveToRadialProjection().setEnabled(false);
        mainview.getButtonCreateSideView().setEnabled(true);
        mainview.getTextFieldCurrentFileSegmentation().setText("");
        vesselsSegmentationModel.getSideViewDisplay().close();
        vesselsSegmentationModel.getRawSegmentation().close();
        vesselsSegmentationModel.getEdgeCentroidMaskImagePlus().close();
        vesselsSegmentationModel.getHybridStackNonSmoothedDisplay().close();
        vesselsSegmentationModel.getHybridStackSmoothedDisplay().close();
    }
}
