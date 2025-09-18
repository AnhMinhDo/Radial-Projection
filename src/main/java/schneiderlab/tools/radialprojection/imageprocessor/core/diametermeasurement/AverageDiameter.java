package schneiderlab.tools.radialprojection.imageprocessor.core.diametermeasurement;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Line;
import ij.gui.ProfilePlot;
import ij.process.ImageProcessor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class AverageDiameter {

    private final ImagePlus binaryMaskEdge;
    private final ImageStack binaryMaskEdgeImageStack;
    private final List<Point> centroidList;
    private final int angleStep;
    private final int numberOfAngles;
    private final int maxRadius;

    public AverageDiameter(ImagePlus binaryMaskEdge,
                           List<Point> centroidList1Vessel){
        this(binaryMaskEdge,centroidList1Vessel,5);
    }

    public AverageDiameter(ImagePlus binaryMaskEdge,
                           List<Point> centroidList1Vessel,
                           int angleStep) {
        this.binaryMaskEdge = binaryMaskEdge;
        this.binaryMaskEdgeImageStack = binaryMaskEdge.getImageStack();
        this.angleStep = angleStep;
        this.numberOfAngles = (int) Math.ceil(360.0 / angleStep); // 360 degree of a circle
        this.centroidList = centroidList1Vessel;
        this.maxRadius = (int)Math.min(binaryMaskEdge.getHeight(),binaryMaskEdge.getWidth())/4;
    }

    public List<Double> process(){
        return measureAverageDiameter();
    }

    private List<Double> measureAverageDiameter(){
        List<Double> averageDiameterEachSliceList = new ArrayList<>(binaryMaskEdgeImageStack.getSize());
        for (int i = 1; i < binaryMaskEdgeImageStack.getSize()+1; i++) { // for each slice in the binary mask stack
            int cx = centroidList.get(i-1).x; // get the x value of centroid at slice i
            int cy = centroidList.get(i-1).y; // get the y value of centroid at slice i
            double averageRadius = measureAverageRadius1Slice(binaryMaskEdgeImageStack.getProcessor(i),
                    cx,cy,
                    maxRadius,
                    angleStep,
                    numberOfAngles);
            double averageDiameter= averageRadius*2;
            averageDiameterEachSliceList.add(averageDiameter);
        }
        return averageDiameterEachSliceList;
    }

    private double measureAverageRadius1Slice(ImageProcessor binaryMask,
                                            int cx, int cy,
                                            int maxRadius,
                                            int angleStep,
                                            int numberOfAngles
                                            ){
        double result = 0;
        for (int i = 0; i < numberOfAngles; i ++) { // for every corner from 0 to 355 step size 5
            double radius = 0;
            double rad = Math.toRadians(i*angleStep); // get the value in radian of the current angle
            // start from 3 o'clock direction and rotate counter-clockwise
            double x2 = cx + maxRadius * Math.cos(rad);
            double y2 = cy - maxRadius * Math.sin(rad);
            x2 = Math.max(0,Math.min(x2, binaryMask.getWidth()-1)); // choose shorter point, the point needed to be inside the image
            y2 = Math.max(0,Math.min(y2, binaryMask.getHeight()-1)); // choose shorter point, the point needed to be inside the image
            Line line = new Line(cx, cy, x2, y2);
            // Get profile (pixel values along line)
            ImagePlus binaryMaskWithROI = new ImagePlus("binary mask with ROI", binaryMask);
            binaryMaskWithROI.setRoi(line);
            ProfilePlot profile = new ProfilePlot(binaryMaskWithROI);
            double[] values = profile.getProfile();
            for (int j = 0; j < values.length; j++) { // traverse each of the pixel on the line
                if(values[j] > 0){ // find the signal of the mask
                    double sizeOf1Pixel = line.getLength()/ (values.length-1);
                    radius = sizeOf1Pixel*(j+1); // the distance from the centroid to this point will be the radius for this angle
                    break; // the crossed point has been found , stop the loop and move on to the next angle
                }
            }
            result += radius;
        }
        return result/numberOfAngles;
    }

//    /*
//    this function calculate the Euclidean distance between 2 points
//    assuming the pixels are square and has edges are 1 in unit
//    can multiple with the scale later
//     */
//    private double euclideanDistance(int x1,int y1,int x2,int y2){
//        int dx = x2 - x1;
//        int dy = y2 - y1;
//        return Math.sqrt(dx*dx+dy*dy);
//    }
}
