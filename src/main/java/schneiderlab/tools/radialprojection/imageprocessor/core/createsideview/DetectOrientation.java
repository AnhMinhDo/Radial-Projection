package schneiderlab.tools.radialprojection.imageprocessor.core.createsideview;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class DetectOrientation {
    /**
     * Detects the dominant direction of tubes in an image.
     * Uses Sobel gradients + angle histogram.
     *
     * @param imp Input ImagePlus (grayscale)
     * @param nBins Number of histogram bins (higher = more precise)
     * @return Dominant direction in degrees (-90 to +90)
     */
    public static double detectDirection(ImagePlus imp, int nBins) {

        ImageProcessor ip = imp.getProcessor().convertToFloat();
        int width = ip.getWidth();
        int height = ip.getHeight();

        // Compute Sobel gradients
        float[] pixels = (float[]) ip.getPixels();

        double[] histogram = new double[nBins]; // angle histogram

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {

                // Sobel kernel in X direction
                double gx =
                        -1 * getPixel(pixels, width, x-1, y-1) +
                                1 * getPixel(pixels, width, x+1, y-1) +
                                -2 * getPixel(pixels, width, x-1, y  ) +
                                2 * getPixel(pixels, width, x+1, y  ) +
                                -1 * getPixel(pixels, width, x-1, y+1) +
                                1 * getPixel(pixels, width, x+1, y+1);

                // Sobel kernel in Y direction
                double gy =
                        -1 * getPixel(pixels, width, x-1, y-1) +
                                -2 * getPixel(pixels, width, x,   y-1) +
                                -1 * getPixel(pixels, width, x+1, y-1) +
                                1 * getPixel(pixels, width, x-1, y+1) +
                                2 * getPixel(pixels, width, x,   y+1) +
                                1 * getPixel(pixels, width, x+1, y+1);

                // --- Step 2: Compute magnitude and angle ---
                double magnitude = Math.sqrt(gx * gx + gy * gy);

                // Skip weak gradients (background noise)
                if (magnitude < 10) continue;

                // Angle in degrees (-90 to +90)
                double angle = Math.toDegrees(Math.atan2(gy, gx));

                // Normalize to -90 to +90 range
                // (lines are symmetric, 0 and 180 are the same direction)
                if (angle > 90)  angle -= 180;
                if (angle < -90) angle += 180;

                // Vote in histogram weighted by magnitude
                int bin = angleToBin(angle, nBins);
                histogram[bin] += magnitude;
            }
        }

        // Find histogram peak
        int peakBin = 0;
        double peakValue = 0;
        for (int i = 0; i < nBins; i++) {
            if (histogram[i] > peakValue) {
                peakValue = histogram[i];
                peakBin = i;
            }
        }

        // Convert bin back to angle
        return binToAngle(peakBin, nBins);
    }

    // Helper: get pixel value safely
    private static float getPixel(float[] pixels, int width, int x, int y) {
        return pixels[y * width + x];
    }

    // Helper: convert angle (-90 to +90) to histogram bin
    private static int angleToBin(double angle, int nBins) {
        int bin = (int) Math.floor((angle + 90.0) / 180.0 * nBins);
        return Math.min(Math.max(bin, 0), nBins - 1);
    }

    // Helper: convert bin back to angle
    private static double binToAngle(int bin, int nBins) {
        return (bin + 0.5) / nBins * 180.0 - 90.0;
    }
}
