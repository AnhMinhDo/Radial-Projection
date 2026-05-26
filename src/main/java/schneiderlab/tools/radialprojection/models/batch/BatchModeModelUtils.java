package schneiderlab.tools.radialprojection.models.batch;

public class BatchModeModelUtils {
    public static void loadProgressToBatchModeGlobalStateModel(BatchModeGlobalStateModel batchModeGlobalStateModel, BatchModeGlobalStateModel progressModel){
        batchModeGlobalStateModel.clearStartQueue();
        batchModeGlobalStateModel.clearCentroidSelectionQueue();
        batchModeGlobalStateModel.clearWatershedAndRadialProjectionQueue();
        batchModeGlobalStateModel.clearRefineVesselQueue();
        batchModeGlobalStateModel.clearAnalysisQueue();

        for (String path : progressModel.getStartQueue()){
            batchModeGlobalStateModel.addLastStartQueue(path);
        }
        for (String path : progressModel.getCentroidSelectionQueue()){
            batchModeGlobalStateModel.addLastCentroidSelectionQueue(path);
        }
        for (String path : progressModel.getWatershedAndRadialProjectionQueue()){
            batchModeGlobalStateModel.addLastWatershedRadialProjectionQueue(path);
        }
        for (String path: progressModel.getRefineVesselQueue()){
            batchModeGlobalStateModel.addLastRefineVesselQueue(path);
        }
        for (String path : progressModel.getAnalysisQueue()){
            batchModeGlobalStateModel.addLastAnalysisQueue(path);
        }
        batchModeGlobalStateModel.setSerializedObjectPath(progressModel.getSerializedObjectPath());
    }
}
