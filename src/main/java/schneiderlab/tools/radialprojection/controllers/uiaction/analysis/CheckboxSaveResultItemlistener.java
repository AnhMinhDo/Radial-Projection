package schneiderlab.tools.radialprojection.controllers.uiaction.analysis;

import schneiderlab.tools.radialprojection.models.radialprojection.AnalysisModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.MainView;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.nio.file.Path;

public class CheckboxSaveResultItemlistener implements ItemListener {
    private final MainView mainView;
    private final AnalysisModel analysisModel;

    public CheckboxSaveResultItemlistener(MainView mainView,
                                          AnalysisModel analysisModel) {
        this.mainView = mainView;
        this.analysisModel = analysisModel;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            Path outputPath = analysisModel.getImageData().getImageOutputPath().getParent();
            mainView.getTextFieldOutputAnalysis().setText(outputPath.toString());
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            if (!mainView.getCheckBoxCombineResultCSV().isSelected() &&
            !mainView.getCheckBoxCombineResultXLSX().isSelected()){
                Path outputPath = analysisModel.getImageData().getImageOutputPath();
                mainView.getTextFieldOutputAnalysis().setText(outputPath.toString());
            }
        }
    }
}

