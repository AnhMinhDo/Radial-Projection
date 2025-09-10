package schneiderlab.tools.radialprojection.imageprocessor.core.unrolling;

import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

public class ContourDetection {
    private final ImageProcessor inputUnrolledVessel;
    private ByteProcessor outputContour;

    public ContourDetection(ImageProcessor inputUnrolledVessel) {
        this.inputUnrolledVessel = inputUnrolledVessel;
    }

    public ByteProcessor process(){
        short[] inputPixelArray = (short[]) inputUnrolledVessel.getPixels();
        outputContour = contourTracing(inputPixelArray,
                inputUnrolledVessel.getWidth(),
                inputUnrolledVessel.getHeight());
        return outputContour;
    }
    public static ByteProcessor contourTracing(short[] inputPixelArray,int width,int height){
        byte[] output = new byte[inputPixelArray.length];
        int iteratorVar = width*(height-1);
        // loop each element at the last row:
        for (int i = 0; i < width; i++) {
            int innerIteratorVar = iteratorVar+i;
            while(innerIteratorVar >=0){
                if(inputPixelArray[innerIteratorVar] != 0){
                    output[innerIteratorVar] = (byte)255;
                    break;
                }
                innerIteratorVar-=width;
            }
        }
        return new ByteProcessor(width,height,output);
    }
}
