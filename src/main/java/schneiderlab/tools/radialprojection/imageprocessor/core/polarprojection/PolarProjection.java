package schneiderlab.tools.radialprojection.imageprocessor.core.polarprojection;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Line;
import ij.gui.ProfilePlot;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import ij.plugin.ContrastEnhancer;
import java.awt.Point;
import java.util.List;

public class PolarProjection {
    private final ImageStack hybridSmoothedStackImageStack;
    private final ImageStack binaryMaskEdgeImageStack;
    private final ImageStack rawStackImageStack;
    private final List<Point> centroidList;
    private final int angleStep;
    private final int angleCount;
    private final int maxRadius;
    private final int outputHeight;
    private final int outputWidth;
    private final ShortProcessor hybridWallProcessor;
    private Vessel vessel;
    private double xyPixelSizeInMicron;
    private Context context;

    public PolarProjection(ImagePlus hybridSmoothedStack,
                           ImagePlus RawStack,
                           ImagePlus binaryMaskEdge,
                            Vessel vesselObject,
                           int angleStep,
                           double xyPixelSizeInMicron,
                           Context context) {
        this.vessel = vesselObject;
        this.hybridSmoothedStackImageStack = hybridSmoothedStack.getImageStack();
        this.binaryMaskEdgeImageStack = binaryMaskEdge.getImageStack();
        this.rawStackImageStack = RawStack.getImageStack();
        this.angleStep = angleStep;
        this.angleCount = (int) Math.ceil(360.0 / angleStep); // 360 degree of a circle
        this.centroidList = vesselObject.getCentroidArrayList();
        this.outputHeight = angleCount;
        this.outputWidth = hybridSmoothedStackImageStack.size();
        this.maxRadius = (int)Math.min(binaryMaskEdge.getHeight(),binaryMaskEdge.getWidth())/4;
        this.hybridWallProcessor = new ShortProcessor(outputWidth,outputHeight);
        this.xyPixelSizeInMicron = xyPixelSizeInMicron;
        this.context = context;
    }

    // process function here
    public ImagePlus process(){
        performPolarProjection();
        ContrastEnhancer contrastEnhancer = new ContrastEnhancer();
        ImagePlus result = new ImagePlus("polar projected Vessel", hybridWallProcessor);
        contrastEnhancer.stretchHistogram(result,0.35);
        return result;
    }

    private void performPolarProjection(){
        // create 2D array to store intermediate result
        short[][] intermediateResult = new short[outputHeight][outputWidth];
        List<Double> diameterlist = vessel.getAverageDiameterList();
        // perform main process
        for (int i = 1; i < binaryMaskEdgeImageStack.getSize()+1; i++) { // for each slice in the binary mask stack
            int cx = centroidList.get(i-1).x; // get the x value of centroid at slice i
            int cy = centroidList.get(i-1).y; // get the y value of centroid at slice i
            findRadialEdgeIntersections(binaryMaskEdgeImageStack.getProcessor(i),
                    cx,cy,
                    maxRadius,
                    intermediateResult,
                    i-1,
                    hybridSmoothedStackImageStack.getProcessor(i),
                    rawStackImageStack.getProcessor(i),
                    angleStep,
                    angleCount,
                    diameterlist,
                    xyPixelSizeInMicron);
        }
        // write the intensity to the outputImageProcessor
        short[] resultShortProcessorArray = (short[])hybridWallProcessor.getPixels();
        for (int rowIdx = 0; rowIdx < angleCount; rowIdx++) {
            for (int colIdx = 0; colIdx < outputWidth; colIdx++) {
                resultShortProcessorArray[rowIdx*outputWidth+colIdx] = intermediateResult[rowIdx][colIdx];
            }
        }

    }
    public void findRadialEdgeIntersections(ImageProcessor binaryMask,
                                                   int cx, int cy,
                                                   int maxRadius,
                                                   short[][] output2DArray,
                                                   int currentSliceIdx,
                                                   ImageProcessor hybridSmoothedSliceProcessor,
                                                   ImageProcessor rawSliceProcessor,
                                                   int angleStep,
                                                   int angleCount,
                                                   List<Double> diameterList,
                                                   double xyPixelSizeInMicron) {
        double SumOfRadiusSingleSlice = 0;
        for (int i = 0; i < angleCount; i ++) {
            double radius = 0;
            double rad = Math.toRadians(i*angleStep); // get the value in radian of the current angle
            double x2 = cx + maxRadius * Math.cos(rad);
            double y2 = cy - maxRadius * Math.sin(rad);
            x2 = Math.max(0,Math.min(x2, binaryMask.getWidth()-1));
            y2 = Math.max(0,Math.min(y2, binaryMask.getHeight()-1));
            Line line = new Line(cx, cy, x2, y2);
            // Get profile (pixel values along line)
            ImagePlus binaryMaskWithROI = new ImagePlus("binary mask with ROI", binaryMask);
            binaryMaskWithROI.setRoi(line);
            ProfilePlot profileBinaryMask = new ProfilePlot(binaryMaskWithROI);
            double[] valuesProfileBinaryMask = profileBinaryMask.getProfile();
            ImagePlus hybridSmoothedSliceImagePlusWithROI = new ImagePlus("hybrid smoothed Slice",hybridSmoothedSliceProcessor);
            hybridSmoothedSliceImagePlusWithROI.setRoi(line);
            ProfilePlot profileHybridSmoothedImage = new ProfilePlot(hybridSmoothedSliceImagePlusWithROI);
            double[] valuesProfileHybridSmoothedSlice = profileHybridSmoothedImage.getProfile();
            for (int j = 0; j < valuesProfileBinaryMask.length; j++) { // traverse each of the pixel on the line
                if(valuesProfileBinaryMask[j] > 0){ // this is the intersection with the edge
//                    Result result = selectBestSignal2(cx,cy,rad,j,valuesProfileHybridSmoothedSlice, originalProcessor);
//                    output2DArray[i][currentSliceIdx] = selectBestSignal(cx,cy,rad,j,originalProcessor);
//                    output2DArray[i][currentSliceIdx] = (short)result.getIntensityValueOnTheOriginalImage();
                    Result result = selectBestSignal3(cx,cy,rad,j,valuesProfileHybridSmoothedSlice,rawSliceProcessor );
                    output2DArray[i][currentSliceIdx] = result.getIntensityValueOnTheOriginalImage();
                    double sizeOf1Pixel = line.getLength()/ (valuesProfileBinaryMask.length-1);
                    radius = sizeOf1Pixel*(result.getIndexOnTheProfile()+1);
                    break;
                }
            }
            SumOfRadiusSingleSlice += radius;
        }
        diameterList.add((SumOfRadiusSingleSlice/72)*2*xyPixelSizeInMicron);
    }

    private static short selectBestSignal (int cx, int cy, double rad, int currentIndex, ImageProcessor imageProcessor){
        //point at interception
        int px = (int)(cx + (currentIndex+1) * Math.cos(rad));
        int py = (int)(cy - (currentIndex+1) * Math.sin(rad));
        //point before interception
        int px_b = (int)(cx + (currentIndex) * Math.cos(rad));
        int py_b = (int)(cy - (currentIndex) * Math.sin(rad));
        //point after interception
        int px_a = (int)(cx + (currentIndex+2) * Math.cos(rad));
        int py_a = (int)(cy - (currentIndex+2) * Math.sin(rad));
        return (short)Math.max(imageProcessor.getf(px,py),
                Math.max(imageProcessor.getf(px_b,py_b),imageProcessor.getf(px_a,py_a))); // select the highest signal out of the 3 points to ensure the best projection quality

    }

    private Result selectBestSignal3(int cx, int cy, double rad, int currentIndex, double[] profileHybridSmoothedSlice, ImageProcessor rawSliceProcessor){
        double max = profileHybridSmoothedSlice[currentIndex-1];
        int idxMax = currentIndex-1;
        int range = currentIndex/2; // TODO: range value is still not optimal, should come up with better threshold
        for (int i = currentIndex; i <Math.min(currentIndex+range,profileHybridSmoothedSlice.length-currentIndex); i++) {
            if (profileHybridSmoothedSlice[i] > max){
                max = profileHybridSmoothedSlice[i];
                idxMax = i;
            }
        }
        int px = (int)(cx + idxMax * Math.cos(rad));
        int py = (int)(cy - idxMax * Math.sin(rad));
        return new Result (idxMax,(short) rawSliceProcessor.getf(px, py),px, py);
    }

    private Result selectBestSignal2 (int cx, int cy, double rad, int currentIndex, double[] profileOriginalSlice, ImageProcessor imageProcessor){
        // since the array start with index 0, but the first pixel(index 0) already given the distance of 1,
        // we need to take currentIndex+1 to get the geometric distance
        int i = currentIndex;
        int iMax = profileOriginalSlice.length-1;
        Integer leftEdge=null ;
        Integer rightEdge=null ;
        Integer midPoint=null ;
        while(i < iMax){
            // check if the previous element is smaller
            if(profileOriginalSlice[i-1] < profileOriginalSlice[i]){
                int iAhead =  i +1;
                // Find the next element that is not equal to x[i]
                while (iAhead < iMax && profileOriginalSlice[iAhead] == profileOriginalSlice[i]) {
                    iAhead++;
                }
                // Check if the next unequal element is smaller
                if (profileOriginalSlice[iAhead] < profileOriginalSlice[i]) {
                    leftEdge = i;
                    rightEdge = iAhead;
                    midPoint = (i + (iAhead - 1)) / 2;
                    i = iAhead;
                    break;
                }
                break;
            }
            i++;
        }
        // In case the Profile Array is strictly increasing or decreasing, select the last or first values respectively
        if(midPoint == null){
            midPoint=currentIndex;
            }
        int xCoordinate = (int)(cx + (midPoint+1) * Math.cos(rad));
        int yCoordinate = (int)(cy - (midPoint+1) * Math.sin(rad));
        return new Result(midPoint,(short)imageProcessor.getf(xCoordinate,yCoordinate),xCoordinate,yCoordinate);
    }

    private short intensityOnProfileLine(int cx,int cy,int idx,double rad, ImageProcessor imageProcessor){
        return (short)imageProcessor.getf(calculateXCoordinate(cx,idx,  rad),calculateYCoordinate(cy,idx, rad));
    }

    private int calculateXCoordinate(int cx,int idx, double rad){
        return (int)(cx + (idx+1) * Math.cos(rad));
    }

    private int calculateYCoordinate(int cy,int idx, double rad){
        return (int)(cy - (idx+1) * Math.cos(rad));
    }

    private class Result{
        private int indexOnTheProfile;
        private short intensityValueOnTheOriginalImage;
        private int xCoordinate;
        private int yCoordinate;

        public Result(int indexOnTheProfile, short intensityValueOnTheOriginalImage, int xCoordinate, int yCoordinate) {
            this.indexOnTheProfile = indexOnTheProfile;
            this.intensityValueOnTheOriginalImage = intensityValueOnTheOriginalImage;
            this.xCoordinate = xCoordinate;
            this.yCoordinate = yCoordinate;
        }

        public int getIndexOnTheProfile() {
            return indexOnTheProfile;
        }

        public short getIntensityValueOnTheOriginalImage() {
            return intensityValueOnTheOriginalImage;
        }

        public int getxCoordinate() {
            return xCoordinate;
        }

        public int getyCoordinate() {
            return yCoordinate;
        }
    }

}
