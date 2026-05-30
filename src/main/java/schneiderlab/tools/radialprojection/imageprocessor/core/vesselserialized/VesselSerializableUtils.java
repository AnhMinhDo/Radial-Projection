package schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized;

import ij.IJ;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.CurrentImageStage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class VesselSerializableUtils {
    public static VesselSerializable
    convertVesselToSerializable(Vessel vessel){
        VesselSerializable vesselSerializable = new VesselSerializable(vessel.getNumberOfSliceInStack());
        vesselSerializable.setFileName(vessel.getFileName());
        vesselSerializable.setDirectoryPath(vessel.getDirectoryPath().toString());
        vesselSerializable.setSerializedObjectPath(vessel.getSerializableObjectPath().toString());
        vesselSerializable.setSerializableObjectOnlySliceInfoPath(vessel.getSerializableObjectOnlySliceInfoPath().toString());

        vesselSerializable.setPathMultiChannelsRadialProjection(vessel.getRadialProjectionsTempPath().toString());

        vesselSerializable.setSliceCroppedRange(vesselSerializable.createSliceCroppedRange(vessel.getSliceCroppedRange().getStart(),vessel.getSliceCroppedRange().getEnd()));
        vesselSerializable.setPathMultiChannelsUnrolling(vessel.getUnrollingTempPath().toString());

        vesselSerializable.setMeanDiameter(vessel.getMeanDiameter());
        vesselSerializable.setSdDiameter(vessel.getSdDiameter());
        vesselSerializable.setMeanCircularity(vessel.getMeanCircularity());
        vesselSerializable.setSdCircularity(vessel.getSdCircularity());
        vesselSerializable.setNoOfBands(vessel.getNoOfBands());
        vesselSerializable.setNoOfGaps(vessel.getNoOfGaps());
        vesselSerializable.setMeanBandWidth(vessel.getMeanBandWidth());
        vesselSerializable.setSdBandWidth(vessel.getSdBandWidth());
        vesselSerializable.setSdGapWidth(vessel.getSdGapWidth());
        vesselSerializable.setMeanGapWidth(vessel.getMeanGapWidth());
        vesselSerializable.setNoOfRandomLineScan(vessel.getNoOfRandomLineScan());
        vesselSerializable.setLengthOfLineScan(vessel.getLengthOfLineScan());
        vesselSerializable.setNoOfRandomBox(vessel.getNoOfRandomBox());
        vesselSerializable.setRandomBoxWidth(vessel.getRandomBoxWidth());
        vesselSerializable.setMeanAnisotropy(vessel.getMeanAnisotropy());
        vesselSerializable.setSdAnisotropy(vessel.getSdAnisotropy());
        vesselSerializable.setMeanBandOrientation(vessel.getMeanBandOrientation());
        vesselSerializable.setSdBandOrientation(vessel.getSdBandOrientation());
        return vesselSerializable;
    }

    public static VesselSerializableOnlySliceInfo convertVesselToVesselSerializableOnlySliceInfo(Vessel vessel){
        VesselSerializableOnlySliceInfo vsosi = new VesselSerializableOnlySliceInfo(vessel.getNumberOfSliceInStack());
        vsosi.setSerializableObjectOnlySliceInfoPath(vessel.getSerializableObjectOnlySliceInfoPath().toString());
        // transfer the VesselSliceData from Vessel object to vesselSerializable object
        vsosi.setVesselSliceDataArrayList(vessel.getVesselSliceDataArrayList());
        vsosi.setCentroidArrayList(vessel.getCentroidArrayListWithoutCropping());
        vsosi.setPerimeterSizeInPixelList(vessel.getPerimeterSizeInPixelList());
        vsosi.setAverageDiameterList(vessel.getAverageDiameterList());
        vsosi.setCircularityList(vessel.getCircularityList());
        return vsosi;
    }

    public static Vessel convertSerializableToVessel(VesselSerializable vesselSerializable,
                                                     CurrentImageStage currentImageStage,
                                                     Context context){
        Vessel vessel = new Vessel(vesselSerializable.getNumberOfSliceInStack());
        vessel.setFileName(vesselSerializable.getFileName());
        vessel.setDirectoryPath(Paths.get(vesselSerializable.getDirectoryPath()));
        // transfer the VesselSliceData from serializable object to vessel object
        if(currentImageStage ==CurrentImageStage.Analysis){
            // deserialized VesselSerializableSliceInfo
            VesselSerializableOnlySliceInfo vesselSerializableOnlySliceInfo = VesselSerializableUtils.vesselSliceInfoDeserializeObject(Paths.get(vesselSerializable.getSerializableObjectOnlySliceInfoPath()));
            vessel.setVesselSliceDataArrayList(vesselSerializableOnlySliceInfo.getVesselSliceDataArrayList());
            vessel.setCentroidArrayList(vesselSerializableOnlySliceInfo.getCentroidArrayList());
            vessel.setPerimeterSizeInPixelList(vesselSerializableOnlySliceInfo.getPerimeterSizeInPixelList());
            vessel.setAverageDiameterList(vesselSerializableOnlySliceInfo.getAverageDiameterList());
            vessel.setCircularityList(vesselSerializableOnlySliceInfo.getCircularityList());
        }

        vessel.setRadialProjectionPath(Paths.get(vesselSerializable.getPathMultiChannelsRadialProjection()));
        vessel.setSliceCroppedRange(vesselSerializable.getSliceCroppedRange().getStart(),vesselSerializable.getSliceCroppedRange().getEnd());

        vessel.setUnrollingPath(Paths.get(vesselSerializable.getPathMultiChannelsUnrolling()));
        vessel.setMeanDiameter(vesselSerializable.getMeanDiameter());
        vessel.setSdDiameter(vesselSerializable.getSdDiameter());
        vessel.setMeanCircularity(vesselSerializable.getMeanCircularity());
        vessel.setSdCircularity(vesselSerializable.getSdCircularity());
//        vessel.setNoOfBands(vesselSerializable.getNoOfBands());
//        vessel.setNoOfGaps(vesselSerializable.getNoOfGaps());
//        vessel.setMeanBandWidth(vesselSerializable.getMeanBandWidth());
//        vessel.setSdBandWidth(vesselSerializable.getSdBandWidth());
//        vessel.setSdGapWidth(vesselSerializable.getSdGapWidth());
//        vessel.setMeanGapWidth(vesselSerializable.getMeanGapWidth());
        vessel.setNoOfRandomLineScan(vesselSerializable.getNoOfRandomLineScan());
        vessel.setLengthOfLineScan(vesselSerializable.getLengthOfLineScan());
        vessel.setNoOfRandomBox(vesselSerializable.getNoOfRandomBox());
        vessel.setRandomBoxWidth(vesselSerializable.getRandomBoxWidth());
//        vessel.setMeanAnisotropy(vesselSerializable.getMeanAnisotropy());
//        vessel.setSdAnisotropy(vesselSerializable.getSdAnisotropy());
//        vessel.setMeanBandOrientation(vesselSerializable.getMeanBandOrientation());
//        vessel.setSdBandOrientation(vesselSerializable.getSdBandOrientation());
        return vessel;
    }

    public static VesselSerializable vesselDeserializeObject(Path serializedObjectPath){
        try {
            FileInputStream file = new FileInputStream(serializedObjectPath.toString());
            ObjectInputStream in = new ObjectInputStream(file);
            VesselSerializable vesselSerializable = (VesselSerializable) in.readObject();
            in.close();
            file.close();
            return vesselSerializable;
        } catch (IOException | ClassNotFoundException e) {
            IJ.log("fail to create the vessel serializeObject");
            return null;
        }
    }

    public static VesselSerializableOnlySliceInfo vesselSliceInfoDeserializeObject(Path serializedObjectPath){
        try {
            FileInputStream file = new FileInputStream(serializedObjectPath.toString());
            ObjectInputStream in = new ObjectInputStream(file);
            VesselSerializableOnlySliceInfo vesselSerializableOnlySliceInfo = (VesselSerializableOnlySliceInfo) in.readObject();
            in.close();
            file.close();
            return vesselSerializableOnlySliceInfo;
        } catch (IOException | ClassNotFoundException e) {
            IJ.log("fail to create the vessel serializeObject");
            return null;
        }
    }
}
