package schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized;

import ij.IJ;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDataSerializable implements Serializable {

    private String serializedObjectPath;
    private String imagePath; // Path to image file
    private String tempDir;
//    private String imageOutputPath; // Path to output dir of Segmentation and Radial Projection
    private String outputDirPath; // Path to the Directory of output file
    private String hybridFirstSlicePath;
//    private int numberOfChannels;
    private int hybridStackSmoothedWidth;
    private int hybridStackSmoothedHeight;
    private int hybridStackSmoothedNumberOfSlice;
    // parameters
    private int xyPixelSize;
    private int zPixelSize;
    private int analysisWindow;
    private double smoothingSigma;
    private int sliceIndexForTuning;
    private double innerVesselRadius;
    private int CelluloseToLigninRatio;
    // file path to side view image
    private String sideViewTempPathWithoutEdgeCentroid;
    // file path to side view image with EdgeCentroid
    private String sideViewTempPathEdgeCentroid;
    // user select centroids
    private List<Point> userSelectedCentroidsList = new ArrayList<Point>();
    // List of add vesselSerFilePath
    private List<String> vesselSerFilePathList = new ArrayList<String>();
    // analysis input parameters
    private int noOfRandomLineScan;
    private double lengthOfLineScan;
    private int noOfRandomBox;
    private int RandomBoxWidth;

    public ImageDataSerializable() {
    }

    public void serializeObject(){
        try{
            FileOutputStream file = new FileOutputStream(serializedObjectPath);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.close();
            file.close();
            IJ.log("Successfully save the ImageData object to storage");
        } catch (IOException e){
            IJ.log("IO error while serializing the object");
        }
    }

    public String getSerializedObjectPath() {
        return serializedObjectPath;
    }

    public void setSerializedObjectPath(String serializedObjectPath) {
        this.serializedObjectPath = serializedObjectPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public String getOutputDirPath() {
        return outputDirPath;
    }

    public void setOutputDirPath(String outputDirPath) {this.outputDirPath = outputDirPath;}

    public String getHybridFirstSlicePath() {
        return hybridFirstSlicePath;
    }

    public void setHybridFirstSlicePath(String hybridFirstSlicePath) {
        this.hybridFirstSlicePath = hybridFirstSlicePath;
    }

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

    public int getCelluloseToLigninRatio() {
        return CelluloseToLigninRatio;
    }

    public void setCelluloseToLigninRatio(int celluloseToLigninRatio) {
        CelluloseToLigninRatio = celluloseToLigninRatio;
    }

    public String getSideViewTempPathWithoutEdgeCentroid() {
        return sideViewTempPathWithoutEdgeCentroid;
    }

    public void setSideViewTempPathWithoutEdgeCentroid(String sideViewTempPathWithoutEdgeCentroid) {
        this.sideViewTempPathWithoutEdgeCentroid = sideViewTempPathWithoutEdgeCentroid;
    }

    public String getSideViewTempPathEdgeCentroid() {
        return sideViewTempPathEdgeCentroid;
    }

    public void setSideViewTempPathEdgeCentroid(String sideViewTempPathEdgeCentroid) {
        this.sideViewTempPathEdgeCentroid = sideViewTempPathEdgeCentroid;
    }

    public List<Point> getUserSelectedCentroidsList() {
        return userSelectedCentroidsList;
    }

    public void setUserSelectedCentroidsList(List<Point> userSelectedCentroidsList) {
        this.userSelectedCentroidsList = userSelectedCentroidsList;
    }

    public int getHybridStackSmoothedWidth() {
        return hybridStackSmoothedWidth;
    }

    public void setHybridStackSmoothedWidth(int hybridStackSmoothedWidth) {
        this.hybridStackSmoothedWidth = hybridStackSmoothedWidth;
    }

    public int getHybridStackSmoothedHeight() {
        return hybridStackSmoothedHeight;
    }

    public void setHybridStackSmoothedHeight(int hybridStackSmoothedHeight) {
        this.hybridStackSmoothedHeight = hybridStackSmoothedHeight;
    }

    public int getHybridStackSmoothedNumberOfSlice() {
        return hybridStackSmoothedNumberOfSlice;
    }

    public void setHybridStackSmoothedNumberOfSlice(int hybridStackSmoothedNumberOfSlice) {
        this.hybridStackSmoothedNumberOfSlice = hybridStackSmoothedNumberOfSlice;
    }

    public List<String> getVesselSerFilePathList() {
        return vesselSerFilePathList;
    }

    public void setVesselSerFilePath(List<String> vesselSerFilePath) {
        this.vesselSerFilePathList = vesselSerFilePath;
    }

    public void addPathToVesselSerFilePathList(String vesselSerFilePath){
        vesselSerFilePathList.add(vesselSerFilePath);
    }

    public int getNoOfRandomLineScan() {
        return noOfRandomLineScan;
    }

    public void setNoOfRandomLineScan(int noOfRandomLineScan) {
        this.noOfRandomLineScan = noOfRandomLineScan;
    }

    public double getLengthOfLineScan() {
        return lengthOfLineScan;
    }

    public void setLengthOfLineScan(double lengthOfLineScan) {
        this.lengthOfLineScan = lengthOfLineScan;
    }

    public int getNoOfRandomBox() {
        return noOfRandomBox;
    }

    public void setNoOfRandomBox(int noOfRandomBox) {
        this.noOfRandomBox = noOfRandomBox;
    }

    public int getRandomBoxWidth() {
        return RandomBoxWidth;
    }

    public void setRandomBoxWidth(int randomBoxWidth) {
        RandomBoxWidth = randomBoxWidth;
    }
}
