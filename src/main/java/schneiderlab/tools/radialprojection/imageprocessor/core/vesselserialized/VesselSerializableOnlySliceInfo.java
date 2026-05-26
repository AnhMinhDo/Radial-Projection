package schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized;

import ij.IJ;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSliceData;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VesselSerializableOnlySliceInfo implements Serializable {
    private String serializableObjectOnlySliceInfoPath;
    //    private int userSelectedUpperboundSlice;
    private List<VesselSliceData> vesselSliceDataArrayList;
    private List<Point> centroidArrayList ;
    private List<Double> perimeterSizeInPixelList ;
    private  List<Double> averageDiameterList;
    private  List<Double> circularityList;
    private int numberOfSliceInStack;

    public VesselSerializableOnlySliceInfo (int numberOfSliceInStack) {
        this.numberOfSliceInStack = numberOfSliceInStack;
        this.vesselSliceDataArrayList = new ArrayList<>(numberOfSliceInStack);
        this.centroidArrayList = new ArrayList<>(numberOfSliceInStack) ;
        this.perimeterSizeInPixelList = new ArrayList<>(numberOfSliceInStack) ;
        this.averageDiameterList = new ArrayList<>(numberOfSliceInStack) ;
        this.circularityList = new ArrayList<>(numberOfSliceInStack) ;
    }

    public void serializeObject(){
        try{
            IJ.log("Start Vessel Serialization");
            FileOutputStream file = new FileOutputStream(this.serializableObjectOnlySliceInfoPath);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.flush();
            out.close();
            file.close();
            IJ.log("Successfully save the Vessel Slice-Info object to storage");
        } catch (IOException e){
            IJ.log("IO error while serializing the vessel Slice-Info object");
            e.printStackTrace();
        }
    }

    public String getSerializableObjectOnlySliceInfoPath() {
        return serializableObjectOnlySliceInfoPath;
    }

    public void setSerializableObjectOnlySliceInfoPath(String serializableObjectOnlySliceInfoPath) {
        this.serializableObjectOnlySliceInfoPath = serializableObjectOnlySliceInfoPath;
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

    public int getNumberOfSliceInStack() {
        return numberOfSliceInStack;
    }

    public void setNumberOfSliceInStack(int numberOfSliceInStack) {
        this.numberOfSliceInStack = numberOfSliceInStack;
    }
}
