package schneiderlab.tools.radialprojection.imageprocessor.core;

import ij.ImagePlus;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Vessel {
    private final List<VesselSliceData> vesselSliceDataArrayList;
    private final List<Point> centroidArrayList ;
    private final List<Double> perimeterSizeInPixelList ;
    private final List<Double> averageDiameterList;
    private final List<Double> circularityList;
    // radial Projection
    private ImagePlus radialProjectionHybrid;
    private ImagePlus radialProjectionCellulose;
    private ImagePlus radialProjectionLignin;
    //Unrolled
    private ImagePlus unrolledVesselHybrid;
    private ImagePlus unrolledVesselCellulose;
    private ImagePlus unrolledVesselLignin;
    private ImagePlus contour;

    public Vessel(int numberOfSliceInStack) {
        this.vesselSliceDataArrayList= new ArrayList<>(numberOfSliceInStack);
        this.centroidArrayList = new ArrayList<>(numberOfSliceInStack);
        this.perimeterSizeInPixelList = new ArrayList<>(numberOfSliceInStack);
        this.averageDiameterList = new ArrayList<>(numberOfSliceInStack);
        this.circularityList = new ArrayList<>(numberOfSliceInStack);
    }

    public void addVesselSliceData(Point clickPoint, Point centroid, int trueSliceIndex, int trueLabel){
        vesselSliceDataArrayList.add(new VesselSliceData(centroid,clickPoint, trueSliceIndex,trueLabel));
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
            return this.centroidArrayList;
        }
    }

    public List<Double> getPerimeterSizeInPixelList() {
        return perimeterSizeInPixelList;
    }

    public List<Double> getAverageDiameterList() {
        return averageDiameterList;
    }

    public List<Double> getCircularityList() {
        return circularityList;
    }

    public void generateCentroidArrayList(){
        for (VesselSliceData vesselSliceData:vesselSliceDataArrayList){
            centroidArrayList.add(vesselSliceData.getCentroid());
        }
    }

    public ImagePlus getRadialProjectionHybrid() {
        return radialProjectionHybrid;
    }

    public void setRadialProjectionHybrid(ImagePlus radialProjectionHybrid) {
        this.radialProjectionHybrid = radialProjectionHybrid;
    }

    public ImagePlus getRadialProjectionCellulose() {
        return radialProjectionCellulose;
    }

    public void setRadialProjectionCellulose(ImagePlus radialProjectionCellulose) {
        this.radialProjectionCellulose = radialProjectionCellulose;
    }

    public ImagePlus getRadialProjectionLignin() {
        return radialProjectionLignin;
    }

    public void setRadialProjectionLignin(ImagePlus radialProjectionLignin) {
        this.radialProjectionLignin = radialProjectionLignin;
    }

    public ImagePlus getUnrolledVesselHybrid() {
        return unrolledVesselHybrid;
    }

    public void setUnrolledVesselHybrid(ImagePlus unrolledVesselHybrid) {
        this.unrolledVesselHybrid = unrolledVesselHybrid;
    }

    public ImagePlus getUnrolledVesselCellulose() {
        return unrolledVesselCellulose;
    }

    public void setUnrolledVesselCellulose(ImagePlus unrolledVesselCellulose) {
        this.unrolledVesselCellulose = unrolledVesselCellulose;
    }

    public ImagePlus getUnrolledVesselLignin() {
        return unrolledVesselLignin;
    }

    public void setUnrolledVesselLignin(ImagePlus unrolledVesselLignin) {
        this.unrolledVesselLignin = unrolledVesselLignin;
    }

    public ImagePlus getContour() {
        return contour;
    }

    public void setContour(ImagePlus contour) {
        this.contour = contour;
    }

    //    public ImagePlus getUnrolledVessel() {
//        return unrolledVessel;
//    }
//
//    public void setUnrolledVessel(ImagePlus unrolledVessel) {
//        this.unrolledVessel = unrolledVessel;
//    }

    private static class VesselSliceData{
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

}
