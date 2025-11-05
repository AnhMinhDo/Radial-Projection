package schneiderlab.tools.radialprojection.imageprocessor.core.segmentation;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import inra.ijpb.binary.BinaryImages;
import inra.ijpb.binary.distmap.ChamferDistanceTransform2DFloat;
import inra.ijpb.binary.distmap.ChamferMask2D;
import inra.ijpb.morphology.MinimaAndMaxima;
import inra.ijpb.watershed.ExtendedMinimaWatershed;
import inra.ijpb.watershed.Watershed;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import static schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils.deepCopyPoints;

public class SegmentationExtendMinimaWaterShed {
    private final RandomAccessibleInterval<FloatType> inputSlice;
    private List<Point> clickCoordinate;
    private final int width;
    private final int height;
    private final Point pointForBackground;
    private final float radius;
    private final int pixelScaleInNanometer;
    private ImagePlus inputSliceImagePlus;

    public SegmentationExtendMinimaWaterShed( List<Point> clickCoordinate,
                                              RandomAccessibleInterval<FloatType> inputSlice,
                                              int width,
                                              int height,
                                              double radius,
                                              int pixelScaleInNanometer) {
        this.inputSlice = inputSlice;
        this.clickCoordinate = clickCoordinate;
        this.width = width;
        this.height = height;
        this.radius = (float) radius;
        this.pointForBackground = new Point((int) width - 1, (int) height - 1);
        this.pixelScaleInNanometer = pixelScaleInNanometer;
    }

    public ImagePlus getInputSliceImagePlus() {
        return inputSliceImagePlus;
    }

    public ImagePlus performSegmentation (){
        // add Point for background
        List<Point> clickPoints = deepCopyPoints(clickCoordinate);
        clickPoints.add(pointForBackground);
//        if(debugMode){ System.err.println(coordinatesOutside);}
        // Create innit mask
        CreateMask createMask = new CreateMask(clickPoints, (int) width, (int) height, radius);
        ImagePlus marker = createMask.drawMaskWithCoordinate();
//        if(debugMode){marker.show();}
        // invert marker
        ImagePlus markerInverted = marker.duplicate();
        markerInverted.getProcessor().invert();
        // Compute Distance Transform using Chamfer method
        ChamferDistanceTransform2DFloat cdtf = new ChamferDistanceTransform2DFloat(ChamferMask2D.BORGEFORS);
        FloatProcessor markerDistanceTransformed = cdtf.distanceMap(markerInverted.getProcessor());
        ImagePlus markerDistanceTransformedImagePlus = new ImagePlus("markerDistanceTransformed", markerDistanceTransformed);
//        if(debugMode){markerDistanceTransformedImagePlus.show();}
//        markerDistanceTransformedImagePlus.show();
        // Threshold: Keep pixels where distance <= radius
        ImageProcessor grownRegionProcessor = markerDistanceTransformed.duplicate();
        float[] markerDistanceTransformedFloatArray = (float[]) markerDistanceTransformed.getPixels();
        float[] grownRegionProcessorFloatArray = (float[]) grownRegionProcessor.getPixels();
        for (int p = 0; p < grownRegionProcessorFloatArray.length; p++) {
            grownRegionProcessorFloatArray[p] = (markerDistanceTransformedFloatArray[p]*pixelScaleInNanometer*0.001 <= radius) ? 255 : 0; // Multiply by 0,001 to convert from nm to µm,any point outside the range is marked as 0
        }
        // write the growRegion to imageplus
        ImagePlus growRegion = new ImagePlus("grown Region", grownRegionProcessor);
//        if(debugMode){growRegion.show();}
//        growRegion.show();
        // convert to Imagej1 Format
        ImagePlus imageForReconstruction = ImageJFunctions.wrapFloat(inputSlice, "original Image");
        inputSliceImagePlus = imageForReconstruction;
//        // Make sure the display range is set properly for float images
//        imageForReconstruction.resetDisplayRange();
//        // impose minima on growRegion image
//        ImageProcessor reconstructedProcessor = MinimaAndMaxima.imposeMinima(imageForReconstruction.getProcessor(),
//                growRegion.getProcessor(), 8);
//        reconstructedProcessor.convertToByte(true);
//        ImagePlus reconstructedImagePlus = new ImagePlus("reconstructed Image", reconstructedProcessor);
//        if(debugMode){imageForReconstruction.resetDisplayRange(); reconstructedImagePlus.show();}
//        imageForReconstruction.resetDisplayRange();
//        reconstructedImagePlus.show();
        // apply marker-based watershed using the labeled minima on the minima-imposed gradient image
//        ImagePlus segmentedImage = ExtendedMinimaWatershed.extendedMinimaWatershed(
//                reconstructedImagePlus, 255, 8
//        );
        ImageProcessor m = BinaryImages.componentsLabeling(grownRegionProcessor,8,32);
        ImagePlus markerNew = new ImagePlus("marker",m);
        ImagePlus segmentedImage = Watershed.computeWatershed(imageForReconstruction,markerNew,null,8,true,0,false);;
        return segmentedImage;
    }
}
