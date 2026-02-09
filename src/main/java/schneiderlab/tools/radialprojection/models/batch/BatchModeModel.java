package schneiderlab.tools.radialprojection.models.batch;

import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BatchModeModel {
    // general field
    private List<Path> filePathList = new ArrayList<>();
    private int numberOfUnprocessedFilePath = 0;
    private int totalNumberOfFiles = 0;
    private List<ImageData<UnsignedShortType, FloatType>> imageDataList = new ArrayList<>();
    // Queue in each step

    // vessel segmentation
    private int xyPixelSize=200;
    private int zPixelSize=200;
    private int analysisWindow=15;
    private double smoothingSigma=1;
    private double innerVesselRadius=1;
    private int CelluloseToLigninRatio=25;
    private List<ImageData<UnsignedShortType,FloatType>> centroidSelectionList = new ArrayList<>();
    private int numberOfImageDataInCentroidSelectionStep = 0;

    // perform watershed
    private List<ImageData<UnsignedShortType,FloatType>> watershedList = new ArrayList<>();
    private int numberOfImageDataInWatershedStep = 0;

    // Radial Projection
    private List<ImageData<UnsignedShortType,FloatType>> radialProjectionList = new ArrayList<>();
    private int numberOfImageDataInRadialProjectionStep = 0;

    // refine Vessel
    private List<ImageData<UnsignedShortType,FloatType>> refineVesselList = new ArrayList<>();
    private int numberOfImageDataInRefineVesselStep = 0;

    // analysis
    private List<ImageData<UnsignedShortType,FloatType>> analysisBatchList = new ArrayList<>();
    private int numberOfImageDataInAnalysisBatchStep = 0;

    // anisotropy
    private int numberOfRandomBoxes=100;
    private int randomBoxWidth=70;
    // Line scan
    private int numberOfLineScan=100;
    private int linescanLength=25;

    // Complete
    private List<ImageData<UnsignedShortType,FloatType>> completeBatchList = new ArrayList<>();
    private int numberOfImageDataInCompleteBatchStep = 0;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    public BatchModeModel() {
    }

    // start button
    public List<Path> getFilePathList() {
        return filePathList;
    }

    private void setFilePathList(List<Path> filePathList) {
        this.filePathList = filePathList;
    }

    public void addPathToPathList(Path path){
        this.getFilePathList().add(path);
        numberOfUnprocessedFilePath+=1;
        totalNumberOfFiles+=1;
        support.firePropertyChange("numberOfUnprocessedFilePath", numberOfUnprocessedFilePath-1, numberOfUnprocessedFilePath);
        support.firePropertyChange("totalNumberOfFiles", totalNumberOfFiles-1, totalNumberOfFiles);
    }

    public int getTotalNumberOfFiles(){
        return this.getFilePathList().size();
    }

    public void setTotalNumberOfFiles(int newValue){
        int oldValue = totalNumberOfFiles;
        this.totalNumberOfFiles=newValue;
        support.firePropertyChange("totalNumberOfFiles",oldValue, totalNumberOfFiles);
    }

    public int getNumberOfUnprocessedFilePath() {
        return numberOfUnprocessedFilePath;
    }

    public void setNumberOfUnprocessedFilePath(int newValue){
        int oldValue = numberOfUnprocessedFilePath;
        numberOfUnprocessedFilePath = newValue;
        support.firePropertyChange("numberOfUnprocessedFilePath",oldValue,numberOfUnprocessedFilePath);
    }

    public void removeAllFilePathList(){
        this.getFilePathList().clear();
        this.setTotalNumberOfFiles(0);
        this.numberOfUnprocessedFilePath=0;
    }

    public Path getNextFilePathFromStartList(){
        return this.getFilePathList().get(this.filePathList.size()-1);
    }

    // centroid Selection list

    public List<ImageData<UnsignedShortType,FloatType>> getCentroidelectionList(){
        return this.centroidSelectionList;
    }

    public int getNumberOfImageDataInCentroidSelection(){
        return centroidSelectionList.size();
    }

    public void addCentroidSelectionList(ImageData<UnsignedShortType,FloatType> imageData){
        centroidSelectionList.add(imageData);
        numberOfImageDataInCentroidSelectionStep = centroidSelectionList.size();
        support.firePropertyChange("numberOfImageDataInCentroidSelectionStep",numberOfImageDataInCentroidSelectionStep-1, numberOfImageDataInCentroidSelectionStep);
    }

    public void setNumberOfImageDataInCentroidSelectionStep(int numberOfImageDataInCentroidSelectionStep) {
        int oldValue = this.numberOfImageDataInCentroidSelectionStep;
        this.numberOfImageDataInCentroidSelectionStep = numberOfImageDataInCentroidSelectionStep;
        support.firePropertyChange("numberOfImageDataInCentroidSelectionStep",oldValue,numberOfImageDataInCentroidSelectionStep);
    }

    //    public ImageData<UnsignedShortType, FloatType> getFirstCentroidSelectionQueue(){
//        return centroidSelectionQueue.getFirst();
//    }

    public int getNumberOfImageDataInCentroidSelectionStep() {
        return centroidSelectionList.size();
    }

    // Watershed step

    public List<ImageData<UnsignedShortType, FloatType>> getWatershedList() {
        return watershedList;
    }

    public void addImageDataToWatershedList(ImageData<UnsignedShortType, FloatType> imageData) {
        this.watershedList.add(imageData);
        setNumberOfImageDataInWatershedStep(getNumberOfImageDataInWatershedStep()+1);
    }

    public int getNumberOfImageDataInWatershedStep() {
        return numberOfImageDataInWatershedStep;
    }

    public void setNumberOfImageDataInWatershedStep(int numberOfImageDataInWatershedStep) {
        int oldValue = this.numberOfImageDataInWatershedStep;
        this.numberOfImageDataInWatershedStep = numberOfImageDataInWatershedStep;
        support.firePropertyChange("numberOfImageDataInWatershedStep",oldValue,numberOfImageDataInWatershedStep);
    }

    // Radial Projection batch

    public List<ImageData<UnsignedShortType, FloatType>> getRadialProjectionList() {
        return radialProjectionList;
    }

    public void addImageDataToRadialProjectionList(ImageData<UnsignedShortType, FloatType> imageData) {
        this.radialProjectionList.add(imageData);
        setNumberOfImageDataInRadialProjectionStep(getNumberOfImageDataInRadialProjectionStep()+1);
    }

    public int getNumberOfImageDataInRadialProjectionStep() {
        return numberOfImageDataInRadialProjectionStep;
    }

    public void setNumberOfImageDataInRadialProjectionStep(int numberOfImageDataInRadialProjectionStep) {
        int oldValue = this.numberOfImageDataInRadialProjectionStep;
        this.numberOfImageDataInRadialProjectionStep = numberOfImageDataInRadialProjectionStep;
        support.firePropertyChange("numberOfImageDataInRadialProjectionStep", oldValue, numberOfImageDataInRadialProjectionStep);
    }
    // refine Vessel

    public List<ImageData<UnsignedShortType, FloatType>> getRefineVesselList() {
        return refineVesselList;
    }

    public void addImageDataToRefineVesselList(ImageData<UnsignedShortType, FloatType> imageData) {
        this.refineVesselList.add(imageData);
        setNumberOfImageDataInRefineVesselStep(getNumberOfImageDataInRefineVesselStep()+1);
    }

    public int getNumberOfImageDataInRefineVesselStep() {
        return numberOfImageDataInRefineVesselStep;
    }

    public void setNumberOfImageDataInRefineVesselStep(int numberOfImageDataInRefineVesselStep) {
        int oldValue = this.numberOfImageDataInRefineVesselStep;
        this.numberOfImageDataInRefineVesselStep = numberOfImageDataInRefineVesselStep;
        support.firePropertyChange("numberOfImageDataInRefineVesselStep",oldValue, numberOfImageDataInRefineVesselStep);
    }

    // analysis batch

    public List<ImageData<UnsignedShortType, FloatType>> getAnalysisBatchList() {
        return analysisBatchList;
    }

    public void addImageDataToAnalysisBatchList(ImageData<UnsignedShortType, FloatType> imageData) {
        this.analysisBatchList.add(imageData);
        setNumberOfImageDataInAnalysisBatchStep(getNumberOfImageDataInAnalysisBatchStep()+1);
    }

    public int getNumberOfImageDataInAnalysisBatchStep() {
        return numberOfImageDataInAnalysisBatchStep;
    }

    public void setNumberOfImageDataInAnalysisBatchStep(int numberOfImageDataInAnalysisBatchStep) {
        int oldValue = this.numberOfImageDataInAnalysisBatchStep;
        this.numberOfImageDataInAnalysisBatchStep = numberOfImageDataInAnalysisBatchStep;
        support.firePropertyChange("numberOfImageDataInAnalysisBatchStep", oldValue, numberOfImageDataInAnalysisBatchStep );
    }

    // complete
    public List<ImageData<UnsignedShortType, FloatType>> getCompleteBatchList() {
        return completeBatchList;
    }

    public void addImageDataToCompleteBatchList(ImageData<UnsignedShortType, FloatType> imageData) {
        this.completeBatchList.add(imageData);
        setNumberOfImageDataInCompleteBatchStep(getNumberOfImageDataInCompleteBatchStep()+1);
    }

    public int getNumberOfImageDataInCompleteBatchStep() {
        return numberOfImageDataInCompleteBatchStep;
    }

    public void setNumberOfImageDataInCompleteBatchStep(int numberOfImageDataInCompleteStep) {
        int oldValue = this.numberOfImageDataInCompleteBatchStep;
        this.numberOfImageDataInCompleteBatchStep = numberOfImageDataInCompleteBatchStep;
        support.firePropertyChange("numberOfImageDataInAnalysisBatchStep", oldValue, numberOfImageDataInCompleteBatchStep );
    }

    // UI user input parameters

    public int getXyPixelSize() {
        return xyPixelSize;
    }

    public void setXyPixelSize(int xyPixelSize) {
        this.xyPixelSize = xyPixelSize;
    }

    public int getzPixelSize() {
        return zPixelSize;
    }

    public void setzPixelSize(int zPixelSize) {
        this.zPixelSize = zPixelSize;
    }

    public int getCelluloseToLigninRatio() {
        return CelluloseToLigninRatio;
    }

    public void setCelluloseToLigninRatio(int celluloseToLigninRatio) {
        CelluloseToLigninRatio = celluloseToLigninRatio;
    }

    public int getAnalysisWindow() {
        return analysisWindow;
    }

    public void setAnalysisWindow(int analysisWindow) {
        this.analysisWindow = analysisWindow;
    }

    public double getSmoothingSigma() {
        return smoothingSigma;
    }

    public void setSmoothingSigma(double smoothingSigma) {
        this.smoothingSigma = smoothingSigma;
    }

    public double getInnerVesselRadius() {
        return innerVesselRadius;
    }

    public void setInnerVesselRadius(double innerVesselRadius) {
        this.innerVesselRadius = innerVesselRadius;
    }

    public int getNumberOfRandomBoxes() {
        return numberOfRandomBoxes;
    }

    public void setNumberOfRandomBoxes(int numberOfRandomBoxes) {
        this.numberOfRandomBoxes = numberOfRandomBoxes;
    }

    public int getRandomBoxWidth() {
        return randomBoxWidth;
    }

    public void setRandomBoxWidth(int randomBoxWidth) {
        this.randomBoxWidth = randomBoxWidth;
    }

    public int getNumberOfLineScan() {
        return numberOfLineScan;
    }

    public void setNumberOfLineScan(int numberOfLineScan) {
        this.numberOfLineScan = numberOfLineScan;
    }

    public int getLinescanLength() {
        return linescanLength;
    }

    public void setLinescanLength(int linescanLength) {
        this.linescanLength = linescanLength;
    }
}
