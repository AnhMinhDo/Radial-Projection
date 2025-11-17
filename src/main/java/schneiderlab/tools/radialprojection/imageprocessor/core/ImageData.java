package schneiderlab.tools.radialprojection.imageprocessor.core;

import ij.ImagePlus;
import ij.gui.Overlay;
import net.imagej.ImgPlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.real.FloatType;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageData<T extends NumericType<T>,
                        K extends NumericType<K>> {
    private Path imagePath; // Path to image file
    private Path imageOutputPath; // Path to output dir of Segmentation and Radial Projection
    private Path outputDirPath; // Path to the Directory of output file
    private int numberOfChannels;
    private int originalWidth;
    private int originalHeight;
    private int originalNumberOfSlice;
    // parameters
    private int xyPixelSize;
    private int zPixelSize;
    private int analysisWindow;
    private double smoothingSigma;
    private int sliceIndexForTuning;
    private double innerVesselRadius;
    private int CelluloseToLigninRatio;
    // output from the vessel Segmentation step
    // output-create side view
    private ImgPlus<T> sideView;// Side view
    private RandomAccessibleInterval<K> lignin;
    private RandomAccessibleInterval<K> cellulose;
    private RandomAccessibleInterval<K> hybridStackNonSmoothed;
    private ImagePlus ligninImagePlus;
    private ImagePlus celluloseImagePlus;
    private ImagePlus hybridStackNonSmoothedImagePlus;
    // output-projection and smoothing
    private RandomAccessibleInterval<K> hybridStackSmoothed;
    private ImgPlus<K> projectedAndSmoothedHybrid;// projected and smooth hybrid
    private int hybridStackSmoothedWidth;
    private int hybridStackSmoothedHeight;
    private int hybridStackSmoothedSlicesNumber;
    // output-process whole stack
    private ImagePlus RawSegmentation;
    private ImagePlus edgeBinaryMaskImagePlus;
    private ImagePlus EdgeCentroidMaskImagePlus;
    private Overlay overlaySegmentation;
    private ImagePlus impInByte;
    private HashMap<Integer, List<Point>> centroidHashMap;
    private List<Vessel> vesselList = new ArrayList<>() ; // Vessel objects

    public Path getImagePath() {
        return imagePath;
    }

    public void setImagePath(Path imagePath) {
        this.imagePath = imagePath;
    }

    public Path getImageOutputPath() {
        return imageOutputPath;
    }

    public void setImageOutputPath(Path imageOutputPath) {
        this.imageOutputPath = imageOutputPath;
    }

    public Path getOutputDirPath() {
        return outputDirPath;
    }


    public void setOutputDirPath(Path outputDirPath) {
        this.outputDirPath = outputDirPath;
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public void setNumberOfChannels(int numberOfChannels) {
        this.numberOfChannels = numberOfChannels;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public int getOriginalNumberOfSlice() {
        return originalNumberOfSlice;
    }

    public void setOriginalNumberOfSlice(int originalNumberOfSlice) {
        this.originalNumberOfSlice = originalNumberOfSlice;
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

    public int getSliceIndexForTuning() {
        return sliceIndexForTuning;
    }

    public void setSliceIndexForTuning(int sliceIndexForTuning) {
        this.sliceIndexForTuning = sliceIndexForTuning;
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

    public ImgPlus<T> getSideView() {
        return sideView;
    }

    public void setSideView(ImgPlus<T> sideView) {
        this.sideView = sideView;
    }

    public RandomAccessibleInterval<K> getLignin() {
        return lignin;
    }

    public void setLignin(RandomAccessibleInterval<K> lignin) {
        this.lignin = lignin;
    }

    public RandomAccessibleInterval<K> getCellulose() {
        return cellulose;
    }

    public void setCellulose(RandomAccessibleInterval<K> cellulose) {
        this.cellulose = cellulose;
    }

    public RandomAccessibleInterval<K> getHybridStackNonSmoothed() {
        return hybridStackNonSmoothed;
    }

    public void setHybridStackNonSmoothed(RandomAccessibleInterval<K> hybridStackNonSmoothed) {
        this.hybridStackNonSmoothed = hybridStackNonSmoothed;
    }

    public ImagePlus getLigninImagePlus() {
        return ligninImagePlus;
    }

    public void setLigninImagePlus(ImagePlus ligninImagePlus) {
        this.ligninImagePlus = ligninImagePlus;
    }

    public ImagePlus getCelluloseImagePlus() {
        return celluloseImagePlus;
    }

    public void setCelluloseImagePlus(ImagePlus celluloseImagePlus) {
        this.celluloseImagePlus = celluloseImagePlus;
    }

    public ImagePlus getHybridStackNonSmoothedImagePlus() {
        return hybridStackNonSmoothedImagePlus;
    }

    public void setHybridStackNonSmoothedImagePlus(ImagePlus hybridStackNonSmoothedImagePlus) {
        this.hybridStackNonSmoothedImagePlus = hybridStackNonSmoothedImagePlus;
    }

    public RandomAccessibleInterval<K> getHybridStackSmoothed() {
        return hybridStackSmoothed;
    }

    public void setHybridStackSmoothed(RandomAccessibleInterval<K> hybridStackSmoothed) {
        this.hybridStackSmoothed = hybridStackSmoothed;
    }

    public ImgPlus<K> getProjectedAndSmoothedHybrid() {
        return projectedAndSmoothedHybrid;
    }

    public void setProjectedAndSmoothedHybrid(ImgPlus<K> projectedAndSmoothedHybrid) {
        this.projectedAndSmoothedHybrid = projectedAndSmoothedHybrid;
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

    public int getHybridStackSmoothedSlicesNumber() {
        return hybridStackSmoothedSlicesNumber;
    }

    public void setHybridStackSmoothedSlicesNumber(int hybridStackSmoothedSlicesNumber) {
        this.hybridStackSmoothedSlicesNumber = hybridStackSmoothedSlicesNumber;
    }

    public ImagePlus getRawSegmentation() {
        return RawSegmentation;
    }

    public void setRawSegmentation(ImagePlus rawSegmentation) {
        RawSegmentation = rawSegmentation;
    }

    public ImagePlus getEdgeBinaryMaskImagePlus() {
        return edgeBinaryMaskImagePlus;
    }

    public void setEdgeBinaryMaskImagePlus(ImagePlus edgeBinaryMaskImagePlus) {
        this.edgeBinaryMaskImagePlus = edgeBinaryMaskImagePlus;
    }

    public ImagePlus getEdgeCentroidMaskImagePlus() {
        return EdgeCentroidMaskImagePlus;
    }

    public void setEdgeCentroidMaskImagePlus(ImagePlus edgeCentroidMaskImagePlus) {
        EdgeCentroidMaskImagePlus = edgeCentroidMaskImagePlus;
    }

    public Overlay getOverlaySegmentation() {
        return overlaySegmentation;
    }

    public void setOverlaySegmentation(Overlay overlaySegmentation) {
        this.overlaySegmentation = overlaySegmentation;
    }

    public ImagePlus getImpInByte() {
        return impInByte;
    }

    public void setImpInByte(ImagePlus impInByte) {
        this.impInByte = impInByte;
    }

    public HashMap<Integer, List<Point>> getCentroidHashMap() {
        return centroidHashMap;
    }

    public void setCentroidHashMap(HashMap<Integer, List<Point>> centroidHashMap) {
        this.centroidHashMap = centroidHashMap;
    }

    public List<Vessel> getVesselList() {
        return vesselList;
    }

    public void setVesselList(List<Vessel> vesselList) {
        this.vesselList = vesselList;
    }
}
