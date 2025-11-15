package schneiderlab.tools.radialprojection.controllers.uiaction.vesselsegmentation;

import ij.ImagePlus;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.Context;
import org.scijava.log.LogService;
import schneiderlab.tools.radialprojection.controllers.workers.CreateSideViewWorker;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.models.radialprojection.VesselsSegmentationModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Paths;

public class CreateSideViewButtonAction implements ActionListener {
    private final MainView mainView;
    private final VesselsSegmentationModel vesselsSegmentationModel;
    private final Context context;
    private final LogService logService;

    public CreateSideViewButtonAction(MainView mainView,
                                      VesselsSegmentationModel vesselsSegmentationModel,
                                      Context context) {
        this.mainView = mainView;
        this.vesselsSegmentationModel = vesselsSegmentationModel;
        this.context=context;
        this.logService=context.getService(LogService.class);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainView.getTextField2StatusVesselSegmentation().setText("Creating Side View...");
        mainView.getButtonProjAndSmooth().setEnabled(false);
        mainView.getButtonSelectCentroid().setEnabled(false);
        mainView.getButtonWatershed().setEnabled(false);
        mainView.getButtonProcessWholeStack().setEnabled(false);
        mainView.getButtonMoveToRadialProjection().setEnabled(false);
        int rowCount = mainView.getTableAddedFileVesselSegmentation().getModel().getRowCount();
        if(rowCount ==0){
            mainView.getButtonAddFile().doClick();
            return;
        }
        String fileToProcess = (String) mainView.getTableAddedFileVesselSegmentation()
                .getModel()
                .getValueAt(0, 0);
        vesselsSegmentationModel.setFilePath(Paths.get(fileToProcess));
        ImageData<UnsignedShortType, FloatType> imageData = new ImageData<>();
        vesselsSegmentationModel.setImageData(imageData);
        vesselsSegmentationModel.getImageData().setImagePath(vesselsSegmentationModel.getFilePath());
        vesselsSegmentationModel.getImageData().setOutputDirPath(Paths.get(mainView.getTextFieldOutputPath().getText()));
        logService.info("Load image at: " + vesselsSegmentationModel.getImageData().getImagePath().toAbsolutePath().toString());
        CreateSideViewWorker createSideViewWorker = new CreateSideViewWorker(
                (int) mainView.getSpinnerXYPixelSizeCreateSideView().getValue(),
                (int) mainView.getSpinnerZPixelSizeCreateSideView().getValue(),
                Paths.get(fileToProcess),
                context,
                mainView
        );
        createSideViewWorker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())){
                    mainView.getProgressBarVesselSegmentation().setValue((int)evt.getNewValue());
                }
            }
        });
        createSideViewWorker.addPropertyChangeListener(propChangeEvent -> {
            if ("state".equals(propChangeEvent.getPropertyName()) &&
                    propChangeEvent.getNewValue() == SwingWorker.StateValue.DONE) {
                vesselsSegmentationModel.setSideView(createSideViewWorker.getSideViewImgPlus());
                vesselsSegmentationModel.getImageData().setSideView(createSideViewWorker.getSideViewImgPlus());
                ImagePlus sideViewDisplay = ImageJFunctions.wrapUnsignedShort(vesselsSegmentationModel.getImageData().getSideView(), "Side View");
                sideViewDisplay.getProcessor().resetMinAndMax();
                vesselsSegmentationModel.setSideViewDisplay(sideViewDisplay);
                mainView.getTextField2StatusVesselSegmentation().setText("Side View Created");
                mainView.getButtonProjAndSmooth().setEnabled(true);
                mainView.getTextFieldCurrentFileSegmentation().setText(vesselsSegmentationModel.getFilePath().getFileName().toString());
                sideViewDisplay.show();
            }
        });
        createSideViewWorker.execute();
    }
}
