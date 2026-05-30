package schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.process.ImageStatistics;
import ij.process.LUT;
import java.awt.Color;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializableUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageDataSerializableUtils {
    public static ImageDataSerializable
    convertImageDataToSerializable(ImageData<UnsignedShortType, UnsignedShortType> imageData) {
        ImageDataSerializable imageDataSerializable = new ImageDataSerializable();
        // path to save the ser file
        Path serPath = imageData.getSerializedObjectPath();

        imageDataSerializable.setSerializedObjectPath(serPath.toAbsolutePath().toString());

//        IJ.log("Path of .ser file: " + serPath);
        // set the information from the ImageData to the ImageDataSerializable
        imageDataSerializable.setImagePath(imageData.getImagePath().toAbsolutePath().toString());
        imageDataSerializable.setOutputDirPath(imageData.getImageOutputPath().toAbsolutePath().toString());

        imageDataSerializable.setTempDir(imageData.getTempDirPath().toAbsolutePath().toString());

        imageDataSerializable.setHybridFirstSlicePath(imageData.getHybridFirstSlicePath().toAbsolutePath().toString());

        imageDataSerializable.setHybridStackSmoothedWidth(imageData.getHybridStackSmoothedWidth());

        imageDataSerializable.setHybridStackSmoothedHeight(imageData.getHybridStackSmoothedHeight());

        imageDataSerializable.setXyPixelSize(imageData.getXyPixelSize());

        imageDataSerializable.setzPixelSize(imageData.getzPixelSize());

        imageDataSerializable.setAnalysisWindow(imageData.getAnalysisWindow());

        imageDataSerializable.setRandomBoxWidth(imageData.getRandomBoxWidth());

        imageDataSerializable.setNoOfRandomBox(imageData.getNoOfRandomBox());

        imageDataSerializable.setLengthOfLineScan(imageData.getLengthOfLineScan());

        imageDataSerializable.setNoOfRandomLineScan(imageData.getNoOfRandomLineScan());

        imageDataSerializable.setCelluloseToLigninRatio(imageData.getCelluloseToLigninRatio());

        imageDataSerializable.setInnerVesselRadius(imageData.getInnerVesselRadius());

        imageDataSerializable.setSmoothingSigma(imageData.getSmoothingSigma());

        imageDataSerializable.setSideViewTempPathWithoutEdgeCentroid(imageData.getSideViewTempPathWithoutEdgeCentroid().toString());

        imageDataSerializable.setUserSelectedCentroidsList(imageData.getUserSelectedCentroidsList());

        // path to the vessel.ser files as a List<String>
        for(Path vesselSerFilePath : imageData.getVesselSerFilePathList()){
            imageDataSerializable.addPathToVesselSerFilePathList(vesselSerFilePath.toString());
        }

        // watershed and radial Projection info

        return imageDataSerializable;
    }

    public static ImageData<UnsignedShortType, UnsignedShortType>
    convertSerializableToImageData(ImageDataSerializable imageDataSerializable, CurrentImageStage currentImageStage , Context context) {
        ImageData<UnsignedShortType, UnsignedShortType> imageData = new ImageData<>();
        imageData.setImagePath(Paths.get(imageDataSerializable.getImagePath()));
        imageData.setSerializedObjectPath(Paths.get(imageDataSerializable.getSerializedObjectPath()));
//        imageData.setOutputDirPath(Paths.get(imageDataSerializable.getOutputDirPath()));
        imageData.setImageOutputPath(Paths.get(imageDataSerializable.getOutputDirPath()));
        imageData.setTempDirPath(Paths.get(imageDataSerializable.getTempDir()));
        imageData.setHybridFirstSlicePath(Paths.get(imageDataSerializable.getHybridFirstSlicePath()));
        imageData.setXyPixelSize(imageDataSerializable.getXyPixelSize());
        imageData.setzPixelSize(imageDataSerializable.getzPixelSize());
        imageData.setAnalysisWindow(imageDataSerializable.getAnalysisWindow());
        imageData.setCelluloseToLigninRatio(imageDataSerializable.getCelluloseToLigninRatio());
        imageData.setInnerVesselRadius(imageDataSerializable.getInnerVesselRadius());
        imageData.setSmoothingSigma(imageDataSerializable.getSmoothingSigma());
        imageData.setSideViewTempPathWithoutEdgeCentroid(Paths.get(imageDataSerializable.getSideViewTempPathWithoutEdgeCentroid()));
        imageData.setUserSelectedCentroidsList(imageDataSerializable.getUserSelectedCentroidsList());
        imageData.setHybridStackSmoothedHeight(imageDataSerializable.getHybridStackSmoothedHeight());
        imageData.setHybridStackSmoothedWidth(imageDataSerializable.getHybridStackSmoothedWidth());
        imageData.setNoOfRandomBox(imageDataSerializable.getNoOfRandomBox());
        imageData.setRandomBoxWidth(imageDataSerializable.getRandomBoxWidth());
        imageData.setLengthOfLineScan(imageDataSerializable.getLengthOfLineScan());
        imageData.setNoOfRandomLineScan(imageDataSerializable.getNoOfRandomLineScan());
        DatasetIOService datasetIOService = context.getService(DatasetIOService.class);
        if(CurrentImageStage.CentroidSelection.equals(currentImageStage)){
            ImagePlus hybridFirstSlice = IJ.openImage(imageData.getHybridFirstSlicePath().toAbsolutePath().toString());
            hybridFirstSlice.setLut(LUT.createLutFromColor(Color.WHITE));
            ImageStatistics stats = hybridFirstSlice.getStatistics();
            hybridFirstSlice.setDisplayRange(stats.min, stats.max);
            imageData.setHybridFirstSlice(hybridFirstSlice);
        }
        if(CurrentImageStage.WatershedAndRadialProjection.equals(currentImageStage)){
            try {
                IJ.log("start importing the image to dataset");
                Dataset sideViewTempStack = datasetIOService.open(imageData.getSideViewTempPathWithoutEdgeCentroid().toString());
                IJ.log("dataset objects is imported");
                // get the last channel(index 3) as a RandomAccessibleInterval
                ImgPlus<UnsignedShortType> imgPlus = (ImgPlus<UnsignedShortType>) sideViewTempStack.getImgPlus();
                int channelDim = imgPlus.dimensionIndex(Axes.CHANNEL);
                RandomAccessibleInterval<UnsignedShortType> hybridSmoothedRAI = Views.hyperSlice(imgPlus,channelDim,3); // the hybridSmoothed stack has the channel index=3
                RandomAccessibleInterval<UnsignedShortType> hybridNonSmoothedRAI = Views.hyperSlice(imgPlus,channelDim,2); // the hybridNonSmoothed stack has the channel index=2
                RandomAccessibleInterval<UnsignedShortType> celluloseRAI = Views.hyperSlice(imgPlus,channelDim,1); // the hybridNonSmoothed stack has the channel index=2
                RandomAccessibleInterval<UnsignedShortType> ligninRAI = Views.hyperSlice(imgPlus,channelDim,0); // the hybridNonSmoothed stack has the channel index=2
                imageData.setHybridStackNonSmoothed(hybridNonSmoothedRAI);
                imageData.setLignin(ligninRAI);
                imageData.setCellulose(celluloseRAI);
                imageData.setHybridStackSmoothed(hybridSmoothedRAI);
            } catch (IOException e) {
                IJ.log("fail to read the channels of side view");
            }
        }
        if(CurrentImageStage.RefineVessel.equals(currentImageStage)){
            // reload the vessel ser file path list and perform vessel deserialization
            for(String vesselSerFilePath : imageDataSerializable.getVesselSerFilePathList()){
                imageData.addPathToVesselSerFilePathList(Paths.get(vesselSerFilePath));
            }
            for (Path vesselSerFilePath : imageData.getVesselSerFilePathList()){
                // deserialize the vessel object
                VesselSerializable vesselSerializable = VesselSerializableUtils.vesselDeserializeObject(vesselSerFilePath);
                Vessel vessel = VesselSerializableUtils.convertSerializableToVessel(vesselSerializable,CurrentImageStage.RefineVessel,context);
                imageData.addToVesselList(vessel);
            }
        }
        if(CurrentImageStage.Analysis.equals(currentImageStage)){
            //TODO
        }
        IJ.log("complete ImageData object creation");
        return imageData;
        }


    public static ImageDataSerializable imageDataDeserializeObject(Path serializedObjectPath){
        try {
            FileInputStream file = new FileInputStream(serializedObjectPath.toString());
            ObjectInputStream in = new ObjectInputStream(file);
            ImageDataSerializable imageDataSerializable = (ImageDataSerializable) in.readObject();
            in.close();
            file.close();
            return imageDataSerializable;
        } catch (IOException | ClassNotFoundException e) {
            IJ.log("fail to create the serializeObject");
            return null;
        }

    }

}
