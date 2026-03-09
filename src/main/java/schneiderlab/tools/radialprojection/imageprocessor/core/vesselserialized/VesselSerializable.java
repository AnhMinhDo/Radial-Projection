package schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized;

import ij.IJ;
import ij.ImagePlus;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VesselSerializable implements Serializable {
    private String serializedObjectPath;
    private String fileName;
    private String directoryPath;
    private int numberOfSliceInStack;
    private  List<VesselSliceData> vesselSliceDataArrayList;
    private  List<Point> centroidArrayList ;
    private  List<Double> perimeterSizeInPixelList ;
    private  List<Double> averageDiameterList;
    private  List<Double> circularityList;
    // radial Projection
    private String pathMultiChannelsRadialProjection;
    // cropped radial Projection
    private SliceCroppedRange sliceCroppedRange;
    //Unrolled
    private String pathMultiChannelsUnrolling;

    // Analysis
    private Double meanDiameter=null;
    private Double sdDiameter=null;
    private Double meanCircularity=null;
    private Double sdCircularity=null;
    private Double noOfBands;
    private Double noOfGaps;
    private Double meanBandWidth;
    private Double sdBandWidth;
    private Double sdGapWidth;
    private Double meanGapWidth;
    private int noOfRandomLineScan;
    private double lengthOfLineScan;
//    private ImagePlus bandHybridImagePlus;
//    private ImagePlus bandHybridMaskImagePlus;
    private String pathBandHybrid;
    // anisotropy
    private int noOfRandomBox;
    private int randomBoxWidth;
    private Double meanAnisotropy;
    private Double sdAnisotropy;
    private Double meanBandOrientation;
    private Double sdBandOrientation;

    public VesselSerializable(int numberOfSliceInStack) {
        this.numberOfSliceInStack = numberOfSliceInStack;
        this.vesselSliceDataArrayList = new ArrayList<>(numberOfSliceInStack);
        this.centroidArrayList = new ArrayList<>(numberOfSliceInStack) ;
        this.perimeterSizeInPixelList = new ArrayList<>(numberOfSliceInStack) ;
        this.averageDiameterList = new ArrayList<>(numberOfSliceInStack) ;
        this.circularityList = new ArrayList<>(numberOfSliceInStack) ;
        this.sliceCroppedRange = new SliceCroppedRange(0,numberOfSliceInStack-1);

    }

    public void serializeObject(){
        try{
            FileOutputStream file = new FileOutputStream(serializedObjectPath);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.close();
            file.close();
            IJ.log("Successfully save the Vessel object to storage");
        } catch (IOException e){
            IJ.log("IO error while serializing the vessel object");
            e.printStackTrace();
        }
    }

    public String getSerializedObjectPath() {
        return serializedObjectPath;
    }

    public void setSerializedObjectPath(String serializedObjectPath) {
        this.serializedObjectPath = serializedObjectPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public int getNumberOfSliceInStack() {
        return this.getCentroidArrayList().size();
    }

    public List<VesselSliceData> getVesselSliceDataArrayList() {
        return vesselSliceDataArrayList;
    }

    public void setVesselSliceDataArrayList(List<VesselSliceData> vesselSliceDataArrayList) {
        this.vesselSliceDataArrayList = vesselSliceDataArrayList;
    }

    public List<Point> getCentroidArrayList() {
        return centroidArrayList;
    }

    public void setCentroidArrayList(List<Point> centroidArrayList) {
        this.centroidArrayList = centroidArrayList;
    }

    public List<Double> getPerimeterSizeInPixelList() {
        return perimeterSizeInPixelList;
    }

    public void setPerimeterSizeInPixelList(List<Double> perimeterSizeInPixelList) {
        this.perimeterSizeInPixelList = perimeterSizeInPixelList;
    }

    public List<Double> getAverageDiameterList() {
        return averageDiameterList;
    }

    public void setAverageDiameterList(List<Double> averageDiameterList) {
        this.averageDiameterList = averageDiameterList;
    }

    public List<Double> getCircularityList() {
        return circularityList;
    }

    public void setCircularityList(List<Double> circularityList) {
        this.circularityList = circularityList;
    }

    public String getPathMultiChannelsRadialProjection() {
        return pathMultiChannelsRadialProjection;
    }

    public void setPathMultiChannelsRadialProjection(String pathMultiChannelsRadialProjection) {
        this.pathMultiChannelsRadialProjection = pathMultiChannelsRadialProjection;
    }

    public SliceCroppedRange getSliceCroppedRange() {
        return sliceCroppedRange;
    }

    public void setSliceCroppedRange(SliceCroppedRange sliceCroppedRange) {
        this.sliceCroppedRange = sliceCroppedRange;
    }

    public SliceCroppedRange createSliceCroppedRange(int start, int end ){
        return new SliceCroppedRange(start, end);
    }

    public String getPathMultiChannelsUnrolling() {
        return pathMultiChannelsUnrolling;
    }

    public void setPathMultiChannelsUnrolling(String pathMultiChannelsUnrolling) {
        this.pathMultiChannelsUnrolling = pathMultiChannelsUnrolling;
    }

    public Double getMeanDiameter() {
        return meanDiameter;
    }

    public void setMeanDiameter(Double meanDiameter) {
        this.meanDiameter = meanDiameter;
    }

    public Double getSdDiameter() {
        return sdDiameter;
    }

    public void setSdDiameter(Double sdDiameter) {
        this.sdDiameter = sdDiameter;
    }

    public Double getMeanCircularity() {
        return meanCircularity;
    }

    public void setMeanCircularity(Double meanCircularity) {
        this.meanCircularity = meanCircularity;
    }

    public Double getSdCircularity() {
        return sdCircularity;
    }

    public void setSdCircularity(Double sdCircularity) {
        this.sdCircularity = sdCircularity;
    }

    public Double getNoOfBands() {
        return noOfBands;
    }

    public void setNoOfBands(Double noOfBands) {
        this.noOfBands = noOfBands;
    }

    public Double getNoOfGaps() {
        return noOfGaps;
    }

    public void setNoOfGaps(Double noOfGaps) {
        this.noOfGaps = noOfGaps;
    }

    public Double getMeanBandWidth() {
        return meanBandWidth;
    }

    public void setMeanBandWidth(Double meanBandWidth) {
        this.meanBandWidth = meanBandWidth;
    }

    public Double getSdBandWidth() {
        return sdBandWidth;
    }

    public void setSdBandWidth(Double sdBandWidth) {
        this.sdBandWidth = sdBandWidth;
    }

    public Double getSdGapWidth() {
        return sdGapWidth;
    }

    public void setSdGapWidth(Double sdGapWidth) {
        this.sdGapWidth = sdGapWidth;
    }

    public Double getMeanGapWidth() {
        return meanGapWidth;
    }

    public void setMeanGapWidth(Double meanGapWidth) {
        this.meanGapWidth = meanGapWidth;
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

    public String getPathBandHybrid() {
        return pathBandHybrid;
    }

    public void setPathBandHybrid(String pathBandHybrid) {
        this.pathBandHybrid = pathBandHybrid;
    }

    public int getNoOfRandomBox() {
        return noOfRandomBox;
    }

    public void setNoOfRandomBox(int noOfRandomBox) {
        this.noOfRandomBox = noOfRandomBox;
    }

    public int getRandomBoxWidth() {
        return randomBoxWidth;
    }

    public void setRandomBoxWidth(int randomBoxWidth) {
        this.randomBoxWidth = randomBoxWidth;
    }

    public Double getMeanAnisotropy() {
        return meanAnisotropy;
    }

    public void setMeanAnisotropy(Double meanAnisotropy) {
        this.meanAnisotropy = meanAnisotropy;
    }

    public Double getSdAnisotropy() {
        return sdAnisotropy;
    }

    public void setSdAnisotropy(Double sdAnisotropy) {
        this.sdAnisotropy = sdAnisotropy;
    }

    public Double getMeanBandOrientation() {
        return meanBandOrientation;
    }

    public void setMeanBandOrientation(Double meanBandOrientation) {
        this.meanBandOrientation = meanBandOrientation;
    }

    public Double getSdBandOrientation() {
        return sdBandOrientation;
    }

    public void setSdBandOrientation(Double sdBandOrientation) {
        this.sdBandOrientation = sdBandOrientation;
    }

    public Double getMeanSpacing() {
        return meanBandWidth+meanGapWidth;
    }

    public void setSliceCropRangeStart(int start){
        this.sliceCroppedRange.setStart(start);
    }

    public void setSliceCropRangeEnd(int end){
        this.sliceCroppedRange.setEnd(end);
    }

    public static class VesselSliceData implements Serializable {
        private final Point centroid;
        private final Point clickPoint;
        private final int trueSliceIndex;
        private final int trueLabel;

        public VesselSliceData(Point centroid, Point clickPoint, int sliceIndex, int label) {
            this.centroid = centroid;
            this.clickPoint = clickPoint;
            this.trueSliceIndex = sliceIndex;
            this.trueLabel = label;
        }
        public Point getCentroid() {
            return centroid;
        }
        public Point getClickPoint() {
            return clickPoint;
        }
        public int getTrueSliceIndex() {
            return trueSliceIndex;
        }
        public int getTrueLabel() {
            return trueLabel;
        }
    }

    public static class SliceCroppedRange implements Serializable{
        private int start;
        private int end;

        public SliceCroppedRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getStart() {
            return start;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getEnd() {
            return end;
        }
    }
}
