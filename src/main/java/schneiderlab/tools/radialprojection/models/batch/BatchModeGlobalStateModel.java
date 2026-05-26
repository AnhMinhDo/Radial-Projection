package schneiderlab.tools.radialprojection.models.batch;

import ij.IJ;
import ij.Prefs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class BatchModeGlobalStateModel implements Serializable {
    // init when start plugin
    // fetch data from the Fiji global state
    private ArrayDeque<String> startQueue = new ArrayDeque<>();
    private ArrayDeque<String> centroidSelectionQueue = new ArrayDeque<>();
    private ArrayDeque<String> watershedAndRadialProjectionQueue = new ArrayDeque<>();
    private ArrayDeque<String> refineVesselQueue = new ArrayDeque<>();
    private ArrayDeque<String> analysisQueue = new ArrayDeque<>();
    private String serializedObjectPath;
    private String suffix = "_progress.ser";
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);

    public BatchModeGlobalStateModel() {
    }

    public void serializeObject(){
        try{
            Path path = Paths.get(serializedObjectPath);
            String dirname = path.getFileName().toString();
            String progressFilename = dirname + this.suffix;
            Path savePath = path.resolve(progressFilename);
            FileOutputStream file = new FileOutputStream(savePath.toAbsolutePath().toString());
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.close();
            file.close();
        } catch (IOException e){
            IJ.log("IO error while saving the Progress Object to Storage");
            e.printStackTrace();
        }
    }

    public String getSerializedObjectPath() {
        return serializedObjectPath;
    }

    public void setSerializedObjectPath(String serializedObjectPath) {
        this.serializedObjectPath = serializedObjectPath;
    }

    // load from Fiji Prefs
    public void loadFromPref(){
        startQueue.clear();
        centroidSelectionQueue.clear();
        watershedAndRadialProjectionQueue.clear();
        refineVesselQueue.clear();
        analysisQueue.clear();

        String startQueueString = Prefs.get("RadialProjectionPlugin.batch.startQueue", "");
        String centroidSelectionQueueString = Prefs.get("RadialProjectionPlugin.batch.centroidSelectionQueue", "");
        String watershedQueueString = Prefs.get("RadialProjectionPlugin.batch.watershedAndRadialProjectionQueue", "");
        String refineQueueString = Prefs.get("RadialProjectionPlugin.batch.refineVesselQueue", "");
        String analysisQueueString = Prefs.get("RadialProjectionPlugin.batch.analysisQueue", "");

        if (!startQueueString.isEmpty()) {
            startQueue.addAll(Arrays.asList(startQueueString.split("\\|")));
        }
        if (!centroidSelectionQueueString.isEmpty()) {
            centroidSelectionQueue.addAll(Arrays.asList(centroidSelectionQueueString.split("\\|")));
        }
        if (!watershedQueueString.isEmpty()) {
            watershedAndRadialProjectionQueue.addAll(Arrays.asList(watershedQueueString.split("\\|")));
        }
        if (!refineQueueString.isEmpty()) {
            refineVesselQueue.addAll(Arrays.asList(refineQueueString.split("\\|")));
        }
        if (!analysisQueueString.isEmpty()) {
            analysisQueue.addAll(Arrays.asList(analysisQueueString.split("\\|")));
        }
    }

    // save all queues to Fiji Prefs
    public void saveToPref(){
        String startqueueString = combineStringInArrayDeque(startQueue);
        String centroidSelectionQueueString = combineStringInArrayDeque(centroidSelectionQueue);
        String watershedAndRadialProjectionQueueString = combineStringInArrayDeque(watershedAndRadialProjectionQueue);
        String refineVesselQueueString = combineStringInArrayDeque(refineVesselQueue);
        String analysisQueueString = combineStringInArrayDeque(analysisQueue);
        Prefs.set("RadialProjectionPlugin.batch.startQueue",startqueueString);
        Prefs.set("RadialProjectionPlugin.batch.centroidSelectionQueue",centroidSelectionQueueString);
        Prefs.set("RadialProjectionPlugin.batch.watershedAndRadialProjectionQueue",watershedAndRadialProjectionQueueString);
        Prefs.set("RadialProjectionPlugin.batch.refineVesselQueue",refineVesselQueueString);
        Prefs.set("RadialProjectionPlugin.batch.analysisQueue", analysisQueueString);
    }

    private String combineStringInArrayDeque(ArrayDeque<String> arrayDeque){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : arrayDeque){
            stringBuilder.append(string).append("|");
            if (stringBuilder.length() > 0) stringBuilder.setLength(stringBuilder.length() - 1); // -1 to remove the the last | character
        }
        return stringBuilder.toString();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }


    // start queue

    public ArrayDeque<String> getStartQueue() {
        return startQueue;
    }
    public void addLastStartQueue(String filePath){
        startQueue.addLast(filePath);
        int newValue = startQueue.size();
        support.firePropertyChange("numberOfImageDataInStartStep", newValue-1,newValue);
    }

    public void removeFirstStartQueue(){
        startQueue.removeFirst();
        int newValue = startQueue.size();
        support.firePropertyChange("numberOfImageDataInStartStep", newValue+1,newValue);
    }

    public String getFirstStartQueue(){
        return startQueue.getFirst();
    }

    public void clearStartQueue(){
        int oldValue = startQueue.size();
        startQueue.clear();
        support.firePropertyChange("numberOfImageDataInStartStep",oldValue, 0);
    }

    // centroid selection queue

    public ArrayDeque<String> getCentroidSelectionQueue() {
        return centroidSelectionQueue;
    }

    public void addLastCentroidSelectionQueue(String filePath){
        centroidSelectionQueue.addLast(filePath);
        int newValue = centroidSelectionQueue.size();
        support.firePropertyChange("numberOfImageDataInCentroidSelectionStep",newValue-1, newValue);
    }

    public void removeFirstCentroidSelectionQueue() {
        centroidSelectionQueue.removeFirst();
        int newValue = centroidSelectionQueue.size();
        support.firePropertyChange("numberOfImageDataInCentroidSelectionStep",newValue+1, newValue);
    }

    public String getFirstCentroidSelectionQueue(){
        return centroidSelectionQueue.getFirst();
    }

    public void clearCentroidSelectionQueue(){
        int oldValue = centroidSelectionQueue.size();
        centroidSelectionQueue.clear();
        support.firePropertyChange("numberOfImageDataInCentroidSelectionStep",oldValue, 0);
    }

    // watershed and radial projection

    public ArrayDeque<String> getWatershedAndRadialProjectionQueue() {
        return watershedAndRadialProjectionQueue;
    }

    public void addLastWatershedRadialProjectionQueue(String filePath){
        watershedAndRadialProjectionQueue.addLast(filePath);
        int newValue = watershedAndRadialProjectionQueue.size();
        support.firePropertyChange("numberOfImageDataInWatershedStep", newValue-1, newValue);
    }

    public void removeFirstWatershedAndRadialProjectionQueue() {
        watershedAndRadialProjectionQueue.removeFirst();
        int newValue = watershedAndRadialProjectionQueue.size();
        support.firePropertyChange("numberOfImageDataInWatershedStep", newValue+1, newValue);
    }

    public String getFirstWatershedAndRadialProjectionQueue(){
        return watershedAndRadialProjectionQueue.getFirst();
    }

    public void clearWatershedAndRadialProjectionQueue() {
        int oldValue = watershedAndRadialProjectionQueue.size();
        watershedAndRadialProjectionQueue.clear();
        support.firePropertyChange("numberOfImageDataInWatershedStep",oldValue, 0);
    }

    // refine vessel
    public ArrayDeque<String> getRefineVesselQueue() {
        return refineVesselQueue;
    }

    public void addLastRefineVesselQueue(String filePath){
        refineVesselQueue.addLast(filePath);
        int newValue = refineVesselQueue.size();
        support.firePropertyChange("numberOfImageDataInRefineVesselStep", newValue-1, newValue);
    }

    public void removeFirstRefineVesselQueue() {
        refineVesselQueue.removeFirst();
        int newValue = refineVesselQueue.size();
        support.firePropertyChange("numberOfImageDataInRefineVesselStep", newValue+1, newValue);
    }

    public String getFirstRefineVesselQueue(){
        return refineVesselQueue.getFirst();
    }

    public void clearRefineVesselQueue() {
        int oldValue = refineVesselQueue.size();
        refineVesselQueue.clear();
        support.firePropertyChange("numberOfImageDataInRefineVesselStep",oldValue, 0);

    }

    // Analysis

    public ArrayDeque<String> getAnalysisQueue() {
        return analysisQueue;
    }

    public void addLastAnalysisQueue(String filePath){
        analysisQueue.addLast(filePath);
        int newValue = analysisQueue.size();
        support.firePropertyChange("numberOfImageDataInAnalysisBatchStep", newValue-1, newValue);
    }

    public void removeFirstAnalysisQueue() {
        analysisQueue.removeFirst();
        int newValue = analysisQueue.size();
        support.firePropertyChange("numberOfImageDataInAnalysisBatchStep", newValue+1, newValue);
    }

    public String getFirstAnalysisQueue(){
        return analysisQueue.getFirst();
    }

    public void clearAnalysisQueue() {
        int oldValue = analysisQueue.size();
        analysisQueue.clear();
        support.firePropertyChange("numberOfImageDataInAnalysisBatchStep",oldValue, 0);

    }

}
