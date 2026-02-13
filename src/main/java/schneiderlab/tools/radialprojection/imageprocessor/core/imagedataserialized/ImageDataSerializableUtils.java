package schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized;

import ij.IJ;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageDataSerializableUtils {
    public static ImageDataSerializable
    convertImageDataToSerializable(ImageData<UnsignedShortType, FloatType> imageData) {
        ImageDataSerializable imageDataSerializable = new ImageDataSerializable();
        // path to save the ser file
        Path serPath = imageData.getSerializedObjectPath();
        imageDataSerializable.setSerializedObjectPath(serPath.toAbsolutePath().toString());
//        IJ.log("Path of .ser file: " + serPath);
        // set the information from the ImageData to the ImageDataSerializable
        imageDataSerializable.setImagePath(imageData.getImagePath().toAbsolutePath().toString());
        imageDataSerializable.setOutputDirPath(imageData.getImageOutputPath().toAbsolutePath().toString());
        imageDataSerializable.setTempDir(imageData.getTempDirPath().toAbsolutePath().toString());
        imageDataSerializable.setHybridStackSmoothedWidth(imageData.getHybridStackSmoothedWidth());
        imageDataSerializable.setHybridStackSmoothedHeight(imageData.getHybridStackSmoothedHeight());
        imageDataSerializable.setXyPixelSize(imageData.getXyPixelSize());
        imageDataSerializable.setzPixelSize(imageData.getzPixelSize());
        imageDataSerializable.setAnalysisWindow(imageData.getAnalysisWindow());
        imageDataSerializable.setCelluloseToLigninRatio(imageData.getCelluloseToLigninRatio());
        imageDataSerializable.setInnerVesselRadius(imageData.getInnerVesselRadius());
        imageDataSerializable.setSmoothingSigma(imageData.getSmoothingSigma());
        imageDataSerializable.setSideViewTempPathWithoutEdgeCentroid(imageData.getSideViewTempPathWithoutEdgeCentroid().toString());
        imageDataSerializable.setUserSelectedCentroidsList(imageData.getUserSelectedCentroidsList());
//        imageDataSerializable.setSideViewLigninPath(imageData.getSideViewLigninPath().toAbsolutePath().toString());
//        imageDataSerializable.setSideViewCellulosePath(imageData.getSideViewCellulosePath().toAbsolutePath().toString());
//        imageDataSerializable.setSideViewHybridPath(imageData.getSideViewHybridPath().toAbsolutePath().toString());
//        imageDataSerializable.setSideViewHybridSmoothedPath(imageData.getSideViewHybridSmoothedPath().toAbsolutePath().toString());
        return imageDataSerializable;
    }

    public static ImageData<UnsignedShortType, FloatType>
    converSerializableToImageData(ImageDataSerializable imageDataSerializable, CurrentImageStage currentImageStage , Context context) {
        ImageData<UnsignedShortType, FloatType> imageData = new ImageData<>();
        imageData.setImagePath(Paths.get(imageDataSerializable.getImagePath()));
        imageData.setOutputDirPath(Paths.get(imageDataSerializable.getOutputDirPath()));
        imageData.setTempDirPath(Paths.get(imageDataSerializable.getTempDir()));
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
//        imageData.setSideViewLigninPath(Paths.get(imageDataSerializable.getSideViewLigninPath()));
//        imageData.setSideViewCellulosePath(Paths.get(imageDataSerializable.getSideViewCellulosePath()));
//        imageData.setSideViewHybridPath(Paths.get(imageDataSerializable.getSideViewHybridPath()));
//        imageData.setSideViewHybridSmoothedPath(Paths.get(imageDataSerializable.getSideViewHybridSmoothedPath()));
        //TODO: read the sideView image from paths, add it back to imageData
        DatasetIOService datasetIOService = context.getService(DatasetIOService.class);
        OpService ops = context.service(OpService.class);
        try {
            IJ.log("start importing the image to dataset");
            Dataset sideViewTempStack = datasetIOService.open(imageData.getSideViewTempPathWithoutEdgeCentroid().toString());
//            Dataset lignin = datasetIOService.open(imageData.getSideViewLigninPath().toString());
//            Dataset cellulose = datasetIOService.open(imageData.getSideViewCellulosePath().toString());
//            Dataset hybrid = datasetIOService.open(imageData.getSideViewHybridPath().toString());
//            Dataset hybridSmooth = datasetIOService.open(imageData.getSideViewHybridSmoothedPath().toString());
            IJ.log("dataset objects is imported");
            // get the last channel(index 3) as a RandomAccessibleInterval
            ImgPlus<FloatType> imgPlus = (ImgPlus<FloatType>) sideViewTempStack.getImgPlus();
            int channelDim = imgPlus.dimensionIndex(Axes.CHANNEL);
            RandomAccessibleInterval<FloatType> hybridSmoothedRAI = ops.convert().float32(Views.hyperSlice(imgPlus,channelDim,3)); // the hybridSmoothed stack has the channel index=3
            if(CurrentImageStage.WatershedAndRadialProjection.equals(currentImageStage)){
                RandomAccessibleInterval<FloatType> hybridNonSmoothedRAI = ops.convert().float32(Views.hyperSlice(imgPlus,channelDim,2)); // the hybridNonSmoothed stack has the channel index=2
                RandomAccessibleInterval<FloatType> celluloseRAI = ops.convert().float32(Views.hyperSlice(imgPlus,channelDim,1)); // the hybridNonSmoothed stack has the channel index=2
                RandomAccessibleInterval<FloatType> ligninRAI = ops.convert().float32(Views.hyperSlice(imgPlus,channelDim,0)); // the hybridNonSmoothed stack has the channel index=2
                imageData.setHybridStackNonSmoothed(hybridNonSmoothedRAI);
                imageData.setLignin(ligninRAI);
                imageData.setCellulose(celluloseRAI);
            }

//            RandomAccessibleInterval<FloatType> ligninRAI = ops.convert().float32((ImgPlus<FloatType>)lignin.getImgPlus());
//            RandomAccessibleInterval<FloatType> celluloseRAI = ops.convert().float32((ImgPlus<FloatType>)cellulose.getImgPlus());
//            RandomAccessibleInterval<FloatType> hybridRAI = ops.convert().float32((ImgPlus<FloatType>)hybrid.getImgPlus());
//            RandomAccessibleInterval<FloatType> hybridSmoothedRAI = ops.convert().float32((ImgPlus<FloatType>)hybridSmooth.getImgPlus());
//            imageData.setLignin(ligninRAI);
//            imageData.setCellulose(celluloseRAI);
            imageData.setHybridStackSmoothed(hybridSmoothedRAI);
//            imageData.setHybridStackNonSmoothed(hybridRAI);
            IJ.log("complete ImageData object creation");
            return imageData;
        } catch (IOException e) {
            IJ.log("fail to read the channels of side view");
        }
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
