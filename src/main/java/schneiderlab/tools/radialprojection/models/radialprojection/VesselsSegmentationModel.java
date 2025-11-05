package schneiderlab.tools.radialprojection.models.radialprojection;

import ij.ImagePlus;
import ij.Prefs;
import ij.gui.Overlay;
import net.imagej.ImgPlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class VesselsSegmentationModel {
    // Current Image with its object
    private ImageData<UnsignedShortType,FloatType> imageData;
    // overall Info of the file
    private Path filePath;
    private Path directoryPath;
    // UI info of the vessel segmentation step
    private int xyPixelSize;
    private int zPixelSize;
    private int analysisWindow;
    private double smoothingSigma;
    private int sliceIndexForTuning;
    private double innerVesselRadius;
    private int CelluloseToLigninRatio;
    // TODO: the state of the button, which is enable, disable
    // output from the vessel Segmentation step
    // output-create side view
    private ImgPlus<UnsignedShortType> sideView;
    private ImagePlus sideViewDisplay;
    private RandomAccessibleInterval<FloatType> lignin;
    private ImagePlus ligninImagePlus;
    private RandomAccessibleInterval<FloatType> cellulose;
    private ImagePlus celluloseImagePlus;
    private RandomAccessibleInterval<FloatType> hybridStackNonSmoothed;
    private ImagePlus hybridStackNonSmoothedImagePlus;
    // output-projection and smoothing
    private RandomAccessibleInterval<FloatType> hybridStackSmoothed;
    private ImagePlus hybridStackSmoothedImagePlus;
    private ImagePlus hybridStackSmoothedDisplay;
    private ImagePlus hybridStackNonSmoothedDisplay;
    private int hybridStackSmoothedWidth;
    private int hybridStackSmoothedHeight;
    private int hybridStackSmoothedSlicesNumber;
    // output-process whole stack
    private ImagePlus RawSegmentation;
    private ImagePlus edgeBinaryMaskImagePlus;
    private ImagePlus EdgeCentroidMaskImagePlus;
    private Overlay overlaySegmentation;
    private ImagePlus impInByte;
    private final List<Point> coordinates = new ArrayList<>() ;
    private final List<Point> coordinatesBatch = new ArrayList<>() ;
    private HashMap<Integer, List<Point>> centroidHashMap;
    private List<Vessel> vesselArrayList;
    public VesselsSegmentationModel() {
    }
    public void initValues(String propertiesFile){
        // load initial values for cziToTifModel from properties file
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/properties_files/initValues.properties")){
            props.load(input);
            int xyPixelSize = (int)Prefs.get("RadialProjection.VesselsSegmentationModel.xyPixelSize",
                    Integer.parseInt(props.getProperty("VesselsSegmentationModel.xyPixelSize")));
            this.setXyPixelSize(xyPixelSize);
            int zPixelSize = (int)Prefs.get("RadialProjection.VesselsSegmentationModel.zPixelSize",
                    Integer.parseInt(props.getProperty("VesselsSegmentationModel.zPixelSize")));
            this.setzPixelSize(zPixelSize);
            int analysisWindow = (int)Prefs.get("RadialProjection.VesselsSegmentationModel.analysisWindow",
                    Integer.parseInt(props.getProperty("VesselsSegmentationModel.analysisWindow")));
            this.setAnalysisWindow(analysisWindow);
            double smoothingSigma = Prefs.get("RadialProjection.VesselsSegmentationModel.smoothingSigma",
                    Double.parseDouble(props.getProperty("VesselsSegmentationModel.smoothingSigma")));
            this.setSmoothingSigma(smoothingSigma);
            int sliceIndexForTuning = (int)Prefs.get("RadialProjection.VesselsSegmentationModel.sliceIndexForTuning",
                    Integer.parseInt(props.getProperty("VesselsSegmentationModel.sliceIndexForTuning")));
            this.setSliceIndexForTuning(sliceIndexForTuning);
            double innerVesselRadius = Prefs.get("RadialProjection.VesselsSegmentationModel.innerVesselRadius",
                    Double.parseDouble(props.getProperty("VesselsSegmentationModel.innerVesselRadius")));
            this.setInnerVesselRadius(innerVesselRadius);
            int celluloseToLigninRatio= (int)Prefs.get("RadialProjection.VesselsSegmentationModel.celluloseToLigninRatio",
                    Integer.parseInt(props.getProperty("VesselsSegmentationModel.celluloseToLigninRatio")));
            this.setCelluloseToLigninRatio(celluloseToLigninRatio);
        } catch (IOException e){
            System.err.println("Fail to load .properties file");
        }
    }

    public ImageData<UnsignedShortType, FloatType> getImageData() {
        return imageData;
    }

    public void setImageData(ImageData<UnsignedShortType, FloatType> imageData) {
        this.imageData = imageData;
    }

    public Path getFilePath(){
        return filePath;
    }

    public void  setFilePath(Path filePath){
        this.filePath=filePath;
    }

    public Path getDirPath(){
        return directoryPath;
    }

    public ImgPlus<UnsignedShortType> getSideView() {
        return sideView;
    }

    public void setSideView(ImgPlus<UnsignedShortType> sideView) {
        this.sideView = sideView;
    }

    public ImagePlus getSideViewDisplay() {
        return sideViewDisplay;
    }

    public void setSideViewDisplay(ImagePlus sideViewDisplay) {
        this.sideViewDisplay = sideViewDisplay;
    }

    public RandomAccessibleInterval<FloatType> getHybridStackNonSmoothed() {
        return hybridStackNonSmoothed;
    }

    public ImagePlus getHybridStackNonSmoothedImagePlus(){
        if(hybridStackNonSmoothedImagePlus!= null){
            return hybridStackNonSmoothedImagePlus;
        } else{
            hybridStackNonSmoothedImagePlus = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    hybridStackNonSmoothed, "Non Smoothed hybrid Stack");
            return hybridStackNonSmoothedImagePlus;
        }
    }
    public void setHybridStackNonSmoothed(RandomAccessibleInterval<FloatType> hybridStackNonSmoothed) {
        this.hybridStackNonSmoothed = hybridStackNonSmoothed;
    }

    public RandomAccessibleInterval<FloatType> getHybridStackSmoothed() {
        return hybridStackSmoothed;
    }

    public void setHybridStackSmoothed(RandomAccessibleInterval<FloatType> hybridStackSmoothed) {
        this.hybridStackSmoothed = hybridStackSmoothed;
    }

    public ImagePlus getHybridStackSmoothedDisplay() {
        return hybridStackSmoothedDisplay;
    }

    public void setHybridStackSmoothedDisplay(ImagePlus hybridStackSmoothedDisplay) {
        this.hybridStackSmoothedDisplay = hybridStackSmoothedDisplay;
    }

    public ImagePlus getHybridStackNonSmoothedDisplay() {
        return hybridStackNonSmoothedDisplay;
    }

    public void setHybridStackNonSmoothedDisplay(ImagePlus hybridStackNonSmoothedDisplay) {
        this.hybridStackNonSmoothedDisplay = hybridStackNonSmoothedDisplay;
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

    public int getHybridStackSmoothedSlicesNumber() { return hybridStackSmoothedSlicesNumber;}

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

    public RandomAccessibleInterval<FloatType> getLignin() {
        return lignin;
    }

    public ImagePlus getLigninImagePlus(){
        if(ligninImagePlus!= null){
            return ligninImagePlus;
        } else{
            ligninImagePlus = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    lignin, "Non Smoothed Lignin Stack");
            return ligninImagePlus;
        }
    }

    public void setLignin(RandomAccessibleInterval<FloatType> lignin) {
        this.lignin = lignin;
    }

    public RandomAccessibleInterval<FloatType> getCellulose() {
        return cellulose;
    }

    public ImagePlus getCelluloseImagePlus(){
        if(celluloseImagePlus!= null){
            return celluloseImagePlus;
        } else{
            celluloseImagePlus = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    cellulose, "Non Smoothed cellulose Stack");
            return celluloseImagePlus;
        }
    }

    public void setCellulose(RandomAccessibleInterval<FloatType> cellulose) {
        this.cellulose = cellulose;
    }

    public ImagePlus getHybridStackSmoothedImagePlus() {
        if(this.hybridStackSmoothedImagePlus!=null){
            return hybridStackSmoothedImagePlus;
        } else {
            hybridStackSmoothedImagePlus = RadialProjectionUtils.copyAndConvertRandomAccessIntervalToImagePlus(
                    hybridStackSmoothed, "Smoothed hybrid Stack");
            return hybridStackSmoothedImagePlus;
        }
    }

    public void setHybridStackSmoothedImagePlus(ImagePlus hybridStackSmoothedImagePlus) {
        this.hybridStackSmoothedImagePlus = hybridStackSmoothedImagePlus;
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

    public List<Point> getCoordinates() {
        return coordinates;
    }

    public List<Point> getCoordinatesBatch() {
        return coordinatesBatch;
    }

    public HashMap<Integer, List<Point>> getCentroidHashMap() {
        return centroidHashMap;
    }

    public void setCentroidHashMap(HashMap<Integer, List<Point>> centroidHashMap) {
        this.centroidHashMap = centroidHashMap;
    }

    public List<Vessel> getVesselArrayList() {
        return vesselArrayList;
    }

    public void setVesselArrayList(List<Vessel> vesselArrayList) {
        this.vesselArrayList = vesselArrayList;
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
}
