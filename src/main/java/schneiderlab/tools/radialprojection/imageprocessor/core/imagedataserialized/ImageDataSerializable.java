package schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized;

import java.io.Serializable;

public class ImageDataSerializable implements Serializable {

    private String imagePath; // Path to image file
    private String tempDir;
//    private String imageOutputPath; // Path to output dir of Segmentation and Radial Projection
    private String outputDirPath; // Path to the Directory of output file
//    private int numberOfChannels;
//    private int originalWidth;
//    private int originalHeight;
//    private int originalNumberOfSlice;
    // parameters
    private int xyPixelSize;
    private int zPixelSize;
    private int analysisWindow;
    private double smoothingSigma;
    private int sliceIndexForTuning;
    private double innerVesselRadius;
    private int CelluloseToLigninRatio;
    // file path to side view image
    private String sideViewLigninPath;
    private String sideViewCellulosePath;
    private String sideViewHybridPath;
    private String sideViewHybridSmoothedPath;

    public ImageDataSerializable() {
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

    public void setOutputDirPath(String outputDirPath) {
        this.outputDirPath = outputDirPath;
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

    public String getSideViewLigninPath() {
        return sideViewLigninPath;
    }

    public void setSideViewLigninPath(String sideViewLigninPath) {
        this.sideViewLigninPath = sideViewLigninPath;
    }

    public String getSideViewCellulosePath() {
        return sideViewCellulosePath;
    }

    public void setSideViewCellulosePath(String sideViewCellulosePath) {
        this.sideViewCellulosePath = sideViewCellulosePath;
    }

    public String getSideViewHybridPath() {
        return sideViewHybridPath;
    }

    public void setSideViewHybridPath(String sideViewHybridPath) {
        this.sideViewHybridPath = sideViewHybridPath;
    }

    public String getSideViewHybridSmoothedPath() {
        return sideViewHybridSmoothedPath;
    }

    public void setSideViewHybridSmoothedPath(String sideViewHybridSmoothedPath) {
        this.sideViewHybridSmoothedPath = sideViewHybridSmoothedPath;
    }
}
