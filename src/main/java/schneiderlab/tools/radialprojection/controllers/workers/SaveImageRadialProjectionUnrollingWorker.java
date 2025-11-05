package schneiderlab.tools.radialprojection.controllers.workers;

import io.scif.services.DatasetIOService;
import net.imagej.DatasetService;
import org.scijava.Context;
import org.scijava.log.LogService;
import schneiderlab.tools.radialprojection.models.radialprojection.RadialProjectionModel;
import schneiderlab.tools.radialprojection.models.radialprojection.VesselsSegmentationModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.Radical_Projection_Tool;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveImageRadialProjectionUnrollingWorker extends SwingWorker<Void, Void> {
    private final Radical_Projection_Tool mainview;
    private final RadialProjectionModel radialProjectionModel;
    private final VesselsSegmentationModel vesselsSegmentationModel;
    private final Context context;
    private final DatasetIOService datasetIOService;
    private final DatasetService datasetService;
    private final LogService logService;

    public SaveImageRadialProjectionUnrollingWorker(Radical_Projection_Tool mainview,
                                                    RadialProjectionModel radialProjectionModel,
                                                    VesselsSegmentationModel vesselsSegmentationModel,
                                                    Context context) {
        this.mainview = mainview;
        this.radialProjectionModel = radialProjectionModel;
        this.vesselsSegmentationModel = vesselsSegmentationModel;
        this.context = context;
        this.datasetIOService=context.service(DatasetIOService.class);
        this.datasetService=context.service(DatasetService.class);
        this.logService=context.service(LogService.class);
    }

    @Override
    protected Void doInBackground() throws Exception {
        Path outputParentDir = Paths.get(mainview.getTextFieldOutputPath().getText());
        if(outputParentDir.toAbsolutePath().toString().isEmpty()){
            outputParentDir = vesselsSegmentationModel.getFilePath().getParent();
        }
        return null;
    }
}
