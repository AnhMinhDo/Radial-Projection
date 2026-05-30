package schneiderlab.tools.radialprojection.imageprocessor.core;

import ij.ImagePlus;
import ij.gui.Roi;

import java.awt.Point;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSliceData;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.SliceCroppedRange;

public class Vessel {
    private String fileName;
    private Path directoryPath;
    private Path serializableObjectPath;
    private Path serializableObjectOnlySliceInfoPath;
    private Path radialProjectionsTempPath;
    private Path radialProjectionPath;
    private Path unrollingTempPath;
    private Path unrollingPath;
    private int numberOfSliceInStack;
//    private int userSelectedLowerboundSlice;
//    private int userSelectedUpperboundSlice;
    private List<VesselSliceData> vesselSliceDataArrayList;
    private List<Point> centroidArrayList ;
    private List<Double> perimeterSizeInPixelList ;
    private  List<Double> averageDiameterList;
    private  List<Double> circularityList;
    // radial Projection
    private ImagePlus radialProjectionHybrid;
    private ImagePlus radialProjectionCellulose;
    private ImagePlus radialProjectionLignin;
    // cropped radial Projection
    private SliceCroppedRange sliceCroppedRange;
//    private ImagePlus croppedRadialProjectionHybrid;
//    private ImagePlus croppedRadialProjectionCellulose;
//    private ImagePlus croppedRadialProjectionLignin;
    //Unrolled
    private ImagePlus unrolledVesselHybrid;
    private ImagePlus unrolledVesselCellulose;
    private ImagePlus unrolledVesselLignin;
    private ImagePlus contour;

//    // cropped Unrolled
//    private ImagePlus croppedUnrolledVesselHybrid;
//    private ImagePlus croppedUnrolledVesselCellulose;
//    private ImagePlus croppedUnrolledVesselLignin;
//    private ImagePlus croppedContour;
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
    private ImagePlus bandHybridImagePlus;
    private ImagePlus bandHybridMaskImagePlus;
    // anisotropy
    private int noOfRandomBox;
    private int randomBoxWidth;
    private Double meanAnisotropy;
    private Double sdAnisotropy;
    private Double meanBandOrientation;
    private Double sdBandOrientation;
    private Double meanSpacing;

    public Vessel(int numberOfSliceInStack) {
        this.numberOfSliceInStack = numberOfSliceInStack;
        this.vesselSliceDataArrayList= new ArrayList<>(numberOfSliceInStack);
        this.centroidArrayList = new ArrayList<>(numberOfSliceInStack);
        this.perimeterSizeInPixelList = new ArrayList<>(numberOfSliceInStack);
        this.averageDiameterList = new ArrayList<>(numberOfSliceInStack);
        this.circularityList = new ArrayList<>(numberOfSliceInStack);
    }

    public void addVesselSliceData(Point clickPoint, Point centroid, int trueSliceIndex, int trueLabel){
        vesselSliceDataArrayList.add(new VesselSliceData(centroid,clickPoint, trueSliceIndex,trueLabel));
    }
    public List<VesselSliceData> getVesselSliceDataArrayList(){
        return this.vesselSliceDataArrayList;
    }

    public void setVesselSliceDataArrayList(List<VesselSliceData> vesselSliceDataArrayList) {
        this.vesselSliceDataArrayList = vesselSliceDataArrayList;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Path getDirectoryPath(){
        return directoryPath;
    }

    public Path getSerializableObjectPath() {
        return serializableObjectPath;
    }

    public void setSerializableObjectPath(Path serializableObjectPath) {
        this.serializableObjectPath = serializableObjectPath;
    }

    public Path getSerializableObjectOnlySliceInfoPath() {
        return serializableObjectOnlySliceInfoPath;
    }

    public void setSerializableObjectOnlySliceInfoPath(Path serializableObjectOnlySliceInfoPath) {
        this.serializableObjectOnlySliceInfoPath = serializableObjectOnlySliceInfoPath;
    }

    public int getNumberOfSliceInStack() {
        return this.getCentroidArrayList().size();
    }

    public Path getRadialProjectionsTempPath() {
        return radialProjectionsTempPath;
    }

    public void setRadialProjectionsTempPath(Path radialProjectionsTempPath) {
        this.radialProjectionsTempPath = radialProjectionsTempPath;
    }

    public Path getRadialProjectionPath() {
        return radialProjectionPath;
    }

    public void setRadialProjectionPath(Path radialProjectionPath) {
        this.radialProjectionPath = radialProjectionPath;
    }

    public Path getUnrollingTempPath() {
        return unrollingTempPath;
    }

    public void setUnrollingTempPath(Path unrollingTempPath) {
        this.unrollingTempPath = unrollingTempPath;
    }

    public Path getUnrollingPath() {
        return unrollingPath;
    }

    public void setUnrollingPath(Path unrollingPath) {
        this.unrollingPath = unrollingPath;
    }

    public void setDirectoryPath(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    public int getNoOfSlice(){
        return Math.abs(this.sliceCroppedRange.getStart()-this.sliceCroppedRange.getEnd()+1);
//        return vesselSliceDataArrayList.size();
    }


    public Point getClickPoint(int index){
        return vesselSliceDataArrayList.get(index).getClickPoint();
    }
    public Point getCentroid(int index){
        return vesselSliceDataArrayList.get(index).getCentroid();
    }
    public int getTrueSliceIndex(int index){
        return vesselSliceDataArrayList.get(index).getTrueSliceIndex();
    }
    public int getTrueLabel(int index){
        return vesselSliceDataArrayList.get(index).getTrueLabel();
    }


    public List<Point> getCentroidArrayList(){
        if (centroidArrayList.isEmpty()){
            generateCentroidArrayList();
                return this.centroidArrayList;

        } else {
            return this.centroidArrayList.subList(this.sliceCroppedRange.getStart(),this.sliceCroppedRange.getEnd()+1);
        }
    }

    public void setCentroidArrayList(List<Point> centroidArrayList) {
        this.centroidArrayList = centroidArrayList;
    }

    public void resetCroppedRange(){
        sliceCroppedRange.setStart(0);
        sliceCroppedRange.setEnd(centroidArrayList.size()-1);
    }

    public List<Point> getCentroidArrayListWithoutCropping(){
        if (centroidArrayList.isEmpty()){
            generateCentroidArrayList();
            return this.centroidArrayList;

        } else {
            return this.centroidArrayList;
        }
    }



    public List<Double> getPerimeterSizeInPixelList() {
        return perimeterSizeInPixelList;
    }

    public void setPerimeterSizeInPixelList(List<Double> perimeterSizeInPixelList){
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



    public void generateCentroidArrayList(){
        for (VesselSliceData vesselSliceData:vesselSliceDataArrayList){
            centroidArrayList.add(vesselSliceData.getCentroid());
        }
        this.sliceCroppedRange = new SliceCroppedRange(0,centroidArrayList.size()-1);
    }

    public ImagePlus getRadialProjectionHybrid() {
        return croppedImage(radialProjectionHybrid,sliceCroppedRange.getStart(),sliceCroppedRange.getEnd());
    }

    public void setRadialProjectionHybrid(ImagePlus radialProjectionHybrid) {
        this.radialProjectionHybrid = radialProjectionHybrid;
    }

    public ImagePlus getRadialProjectionCellulose() {
        return croppedImage(radialProjectionCellulose,sliceCroppedRange.getStart(),sliceCroppedRange.getEnd());
    }

    public void setRadialProjectionCellulose(ImagePlus radialProjectionCellulose) {
        this.radialProjectionCellulose = radialProjectionCellulose;
    }

    public ImagePlus getRadialProjectionLignin() {
        return croppedImage(radialProjectionLignin,sliceCroppedRange.getStart(),sliceCroppedRange.getEnd());
    }

    public void setRadialProjectionLignin(ImagePlus radialProjectionLignin) {
        this.radialProjectionLignin = radialProjectionLignin;
    }

    /**
     * set the slice range of the newly cropped radial projection or unrolling
     * @param start index of the start slice, inclusive
     * @param end index of the last slice, inclusive
     */
    public void setSliceCroppedRange(int start, int end){
        this.sliceCroppedRange= new SliceCroppedRange(start,end);
    }

    public SliceCroppedRange getSliceCroppedRange(){
        return this.sliceCroppedRange;
    }

    public ImagePlus getUnrolledVesselHybrid() {
        return croppedImage(unrolledVesselHybrid,sliceCroppedRange.getStart(),sliceCroppedRange.getEnd());
    }

    public void setUnrolledVesselHybrid(ImagePlus unrolledVesselHybrid) {
        this.unrolledVesselHybrid = unrolledVesselHybrid;
    }

    public ImagePlus getUnrolledVesselCellulose() {
        return croppedImage(unrolledVesselCellulose,sliceCroppedRange.getStart(),sliceCroppedRange.getEnd());
    }

    public void setUnrolledVesselCellulose(ImagePlus unrolledVesselCellulose) {
        this.unrolledVesselCellulose = unrolledVesselCellulose;
    }

    public ImagePlus getUnrolledVesselLignin() {
        return croppedImage(unrolledVesselLignin,sliceCroppedRange.getStart(),sliceCroppedRange.getEnd());
    }

    public void setUnrolledVesselLignin(ImagePlus unrolledVesselLignin) {
        this.unrolledVesselLignin = unrolledVesselLignin;
    }

    public ImagePlus getContour() {
        return croppedImage(contour,sliceCroppedRange.getStart(),sliceCroppedRange.getEnd());
    }

    public void setContour(ImagePlus contour) {
        this.contour = contour;
    }

    public Double getMeanDiameter(){
        if(meanDiameter != null){
            return meanDiameter;
        } else {
            return this.meanDiameter();
        }
    }

    public void setMeanDiameter(Double meanDiameter) {
        this.meanDiameter = meanDiameter;
    }

    public Double getSdDiameter(){
        if(sdDiameter != null){
            return sdDiameter;
        } else {
            return this.sdDiameter();
        }
    }

    public void setSdDiameter(Double sdDiameter) {
        this.sdDiameter = sdDiameter;
    }

    public Double getMeanCircularity(){
        if(meanCircularity != null){
            return meanCircularity;
        } else {
            return this.meanCircularity();
        }
    }

    public void setMeanCircularity(Double meanCircularity) {
        this.meanCircularity = meanCircularity;
    }

    public Double getSdCircularity(){
        if(sdCircularity != null){
            return sdCircularity;
        } else {
            return this.sdCircularity();
        }
    }

    public void setSdCircularity(Double sdCircularity) {
        this.sdCircularity = sdCircularity;
    }

    private Double meanDiameter(){
        Double sum = 0.0;
        for(Double averageDiameterEachSlice : averageDiameterList){
            sum+=averageDiameterEachSlice;
        }
        return sum /averageDiameterList.size();
    }

    private Double sdDiameter(){
        Double sum = 0.0;
        for(Double averageDiameterEachSlice : averageDiameterList){
            sum+=averageDiameterEachSlice;
        }
        Double mean =  sum /averageDiameterList.size();
        Double sumOfSquareDeviation=0.0;
        for (Double averageDiameterEachSlice : averageDiameterList){
            sumOfSquareDeviation+=(averageDiameterEachSlice-mean)*(averageDiameterEachSlice-mean);
        }
        return Math.sqrt(sumOfSquareDeviation/ averageDiameterList.size());
    }

    private Double meanCircularity(){
        Double sum = 0.0;
        for(Double circularityEachSlice : circularityList){
            sum+=circularityEachSlice;
        }
        return sum /circularityList.size();
    }

    private Double sdCircularity(){
        Double sum = 0.0;
        for(Double circularityEachSlice : circularityList){
            sum+=circularityEachSlice;
        }
        Double mean =  sum /circularityList.size();
        Double sumOfSquareDeviation=0.0;
        for (Double circularityEachSlice : circularityList){
            sumOfSquareDeviation+=(circularityEachSlice-mean)*(circularityEachSlice-mean);
        }
        return Math.sqrt(sumOfSquareDeviation/ circularityList.size());
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

    public int getNoOfRandomLineScan() {
        return noOfRandomLineScan;
    }

    public void setNoOfRandomLineScan(int noOfRandomLineScan) {
        this.noOfRandomLineScan = noOfRandomLineScan;
    }

    public Double getLengthOfLineScan() {
        return lengthOfLineScan;
    }

    public void setLengthOfLineScan(double lengthOfLineScan) {
        this.lengthOfLineScan = lengthOfLineScan;
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


    public ImagePlus getBandHybridImagePlus() {
        return bandHybridImagePlus;
    }

    public void setBandHybridImagePlus(ImagePlus bandHybridImagePlus) {
        this.bandHybridImagePlus = bandHybridImagePlus;
    }

    public ImagePlus getBandHybridMaskImagePlus() {
        return bandHybridMaskImagePlus;
    }

    public void setBandHybridMaskImagePlus(ImagePlus bandHybridMaskImagePlus) {
        this.bandHybridMaskImagePlus = bandHybridMaskImagePlus;
    }

    // anisotropy

    public void setNoOfRandomBox(int noOfRandomBox) {
        this.noOfRandomBox = noOfRandomBox;
    }

    public int getNoOfRandomBox() {
        return noOfRandomBox;
    }

    public void setRandomBoxWidth(int randomBoxWidth) {
        this.randomBoxWidth = randomBoxWidth;
    }

    public int getRandomBoxWidth() {
        return randomBoxWidth;
    }

    public Double getMeanAnisotropy() {
        return meanAnisotropy;
    }

    public void setMeanAnisotropy(double meanAnisotropy) {
        this.meanAnisotropy = meanAnisotropy;
    }

    public Double getSdAnisotropy() {
        return sdAnisotropy;
    }

    public void setSdAnisotropy(double sdAnisotropy) {
        this.sdAnisotropy = sdAnisotropy;
    }

    public Double getMeanBandOrientation() {
        return meanBandOrientation;
    }

    public void setMeanBandOrientation(double meanBandOrientation) {
        this.meanBandOrientation = meanBandOrientation;
    }

    public Double getSdBandOrientation() {
        return sdBandOrientation;
    }

    public void setSdBandOrientation(double sdBandOrientation) {
        this.sdBandOrientation = sdBandOrientation;
    }

    public Double getMeanSpacing() {
        return meanBandWidth+meanGapWidth;
    }

    private static ImagePlus croppedImage(ImagePlus imp, int start, int end){
        Roi updatedRoi = new Roi(start,0,end-start+1,imp.getHeight());
        imp.setRoi(updatedRoi);
        ImagePlus croppedImage = imp.crop();
        croppedImage.setTitle(imp.getTitle());
        return croppedImage;
    }
}
