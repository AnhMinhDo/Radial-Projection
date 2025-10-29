package schneiderlab.tools.radialprojection.imageprocessor.core;

import ij.ImagePlus;
import ij.plugin.ImageCalculator;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class FibrilTool {
    private final ImagePlus input;
    private final int thresholdFlatRegion;
    private Double orientation;
    private Double anisotropy;

    public FibrilTool(ImagePlus input, int thresholdFlatRegion){
        this.input=input;
        this.thresholdFlatRegion= thresholdFlatRegion;
    }
    public FibrilTool(ImagePlus input){
        this(input,2); // set default for flat region threshold to be 2
    }

    public double getOrientation() {
        if(orientation!=null){
            return orientation;
        } else {
            performMeasurement();
            return orientation;
        }
    }

    public double getAnisotropy() {
        if(anisotropy!=null){
            return anisotropy;
        } else {
            performMeasurement();
            return anisotropy;
        }
    }

    public void calculate(){
        performMeasurement();
    }

    private void performMeasurement(){
        ImageCalculator imageCalculator = new ImageCalculator();
        // compute the gradients for x and y
        ImagePlus gradientX = computeXGradient(input.getProcessor());
        ImagePlus gradientY = computeYGradient(input.getProcessor());
        // calculate the magnitude , replace small magnitude with a fixed minimum
        ImagePlus magnitude = computeNormOfGradient(gradientX,gradientY,thresholdFlatRegion);
        // divide each gradient by magnitude to get the unit vector with magnitude = 1
        ImagePlus gradientXnormalized = imageCalculator.run("divide create", gradientX, magnitude);
        ImagePlus gradientYnormalized = imageCalculator.run("divide create", gradientY, magnitude);
        // structure tensor, upper left corner, x^2
        ImagePlus nXX = imageCalculator.run("multiply create",gradientXnormalized,gradientXnormalized);
        ImagePlus nYY = imageCalculator.run("multiply create",gradientYnormalized,gradientYnormalized);
        ImagePlus nXY = imageCalculator.run("multiply create",gradientXnormalized, gradientYnormalized);
        // find the eigenvalue and eigenvector of the structure tensor (2x2 matrix) to get the overall direction and the coherence along that direction
        double xx = nXX.getStatistics().mean;
        double yy = nYY.getStatistics().mean;
        double xy = nXY.getStatistics().mean;
        DirectionAndScoreResult directionAndScore = calculateStructureTensor(xx,yy,xy);
        if(directionAndScore.directionInDegree < 0){
            this.orientation = directionAndScore.directionInDegree + 180; // convert to 0-180 instead of from -90 to 90
        } else {
            this.orientation = directionAndScore.directionInDegree;
        }

        this.anisotropy = directionAndScore.score;
    }

    private ImagePlus computeXGradient(ImageProcessor input) {
        ImageProcessor x = input.duplicate().convertToFloatProcessor();
        x.setInterpolationMethod(ImageProcessor.BICUBIC);
        x.translate(-0.5, 0);
        ImageProcessor x1 = x.duplicate();
        x1.setInterpolationMethod(ImageProcessor.NONE);
        x1.translate(1, 0);
        return new ImageCalculator().run("subtract create", new ImagePlus("", x), new ImagePlus("", x1));
    }

    private ImagePlus computeYGradient(ImageProcessor input){
        ImageProcessor y = input.duplicate().convertToFloatProcessor();
        y.setInterpolationMethod(ImageProcessor.BICUBIC);
        y.translate(0,-0.5);
        ImageProcessor y1 = y.duplicate();
        y1.setInterpolationMethod(ImageProcessor.NONE);
        y1.translate(0,1);
        return new ImageCalculator().run("subtract create", new ImagePlus("y", y), new ImagePlus("y1", y1));
    }

    private ImagePlus computeNormOfGradient(ImagePlus x, ImagePlus y, int thresholdFlatRegion) {
        FloatProcessor fx = x.getProcessor().convertToFloatProcessor();
        FloatProcessor fy = y.getProcessor().convertToFloatProcessor();
        int width = fx.getWidth();
        int height = fx.getHeight();
        float[] px = (float[]) fx.getPixels();
        float[] py = (float[]) fy.getPixels();
        FloatProcessor result = new FloatProcessor(width, height);
        float[] pr = (float[]) result.getPixels();
        for (int i = 0; i < px.length; i++) {
            double val = Math.sqrt(px[i] * px[i] + py[i] * py[i]);
            pr[i] = (float) (val < thresholdFlatRegion ? 255 : val);
        }
        return new ImagePlus("Norm of Gradient", result);
    }

    private ImagePlus normalizeToComponentsOfNorm(ImagePlus gradient, ImagePlus norm){
        ImageCalculator imageCalculator = new ImageCalculator();
        return imageCalculator.run("divide create", gradient, norm);
    }

    private DirectionAndScoreResult calculateStructureTensor(double xx,
                                                             double yy,
                                                             double xy){
        double m = (xx + yy) / 2;
        double d = (xx - yy) / 2;
        double sqrtTerm = Math.sqrt(xy*xy + d*d);
        double v1 = m + sqrtTerm;
        double v2 = m - sqrtTerm;
        //direction
        double tn = - Math.atan((v2 - xx) / xy);
        //score
        double scoren = Math.abs((v1 - v2) / (v1 + v2));
        return new DirectionAndScoreResult(tn,scoren);
    }

    private double euclideanDistance(int x1,int y1,int x2,int y2){
        int dx = x1 - x2;
        int dy = y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }

    private class DirectionAndScoreResult{
        private final double directionInRadian;
        private final double score;
        private final double directionInDegree;

        public DirectionAndScoreResult(double directionInRadian, double score) {
            this.directionInRadian = directionInRadian;
            this.score = score;
            this.directionInDegree = convertRadianToDegree(directionInRadian);
        }

        public double getDirectionInRadian() {
            return directionInRadian;
        }

        public double getScore() {
            return score;
        }

        public double getDirectionInDegree() {
            return directionInDegree;
        }

        private double convertRadianToDegree(double radian){
            return radian*180/Math.PI;
        }
    }
}