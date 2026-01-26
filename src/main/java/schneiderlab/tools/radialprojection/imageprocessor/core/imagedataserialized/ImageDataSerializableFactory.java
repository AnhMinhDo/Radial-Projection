package schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized;

import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;

public class ImageDataSerializableFactory {
    public static ImageDataSerializable
    convertImageDataToSerializable (ImageData<UnsignedShortType, FloatType> imageData){
        ImageDataSerializable imageDataSerializable = new ImageDataSerializable();
        // set the information from the ImageData to the ImageDataSerializable
        imageDataSerializable.setImagePath(imageData.getImagePath().toAbsolutePath().toString());
        imageDataSerializable.setOutputDirPath(imageData.getOutputDirPath().toAbsolutePath().toString());
        imageDataSerializable.setTempDir(imageData.getTempDirPath().toAbsolutePath().toString());
        imageDataSerializable.setXyPixelSize(imageData.getXyPixelSize());
        imageDataSerializable.setzPixelSize(imageData.getzPixelSize());
        imageDataSerializable.setAnalysisWindow(imageData.getAnalysisWindow());
        imageDataSerializable.setCelluloseToLigninRatio(imageData.getCelluloseToLigninRatio());
        imageDataSerializable.setInnerVesselRadius(imageData.getInnerVesselRadius());
        imageDataSerializable.setSmoothingSigma(imageData.getSmoothingSigma());
        imageDataSerializable.setSideViewLigninPath(imageData.getSideViewLigninPath().toAbsolutePath().toString());
        imageDataSerializable.setSideViewCellulosePath(imageData.getSideViewCellulosePath().toAbsolutePath().toString());
        imageDataSerializable.setSideViewHybridPath(imageData.getSideViewHybridPath().toAbsolutePath().toString());
        imageDataSerializable.setSideViewHybridSmoothedPath(imageData.getSideViewHybridSmoothedPath().toAbsolutePath().toString());
        return imageDataSerializable;
    }
//    public static ImageData<UnsignedShortType, FloatType>
//    converSerializableToImageData (ImageDataSerializable imageDataSerializable){
//        //TODO: create the ImageData object from ImageDataSerializable
//
//    }
}
