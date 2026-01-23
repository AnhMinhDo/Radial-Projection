package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.plugin.ContrastEnhancer;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.axis.DefaultLinearAxis;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;

public class SaveImageSideViewWithoutEdgeCentroid extends SwingWorker<Void, Void> {
    BatchModeModel batchModeModel;

    public SaveImageSideViewWithoutEdgeCentroid(BatchModeModel batchModeModel) {
        this.batchModeModel = batchModeModel;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for(ImageData<UnsignedShortType, FloatType> imageData : batchModeModel.getCentroidelectionList()){
            RandomAccessibleInterval<FloatType> hybridStackNonSmoothed = imageData.getHybridStackNonSmoothed();
            RandomAccessibleInterval<FloatType> ligninStackNonSmoothed = imageData.getLignin();
            RandomAccessibleInterval<FloatType> celluloseStackNonSmoothed = imageData.getCellulose();
//            ImageStack edgeAndCentroidMaskStack = imageData.getEdgeCentroidMaskImagePlus().getImageStack();
            int width = imageData.getHybridStackSmoothedWidth();
            int height = imageData.getHybridStackSmoothedHeight();
            int slices = imageData.getHybridStackSmoothedSlicesNumber();
            double scaleX = (double) ((int) batchModeModel.getXyPixelSize()) /1000;
            double scaleY = (double) ((int) batchModeModel.getXyPixelSize()) /1000;
            double scaleZ = (double) ((int) batchModeModel.getzPixelSize()) /1000;
            // build the path for the output
            String filename = imageData.getImagePath().getFileName().toString();
            IJ.log("filename: " + filename);
            Path outputDir = imageData.getImageOutputPath();
            String outputFileNameXylemWaterView = "Xylem_Water_View-"+filename;
            Path completeOutPutPath = outputDir.resolve(outputFileNameXylemWaterView);
            IJ.log("image output path: " + completeOutPutPath);
            int noOfChannels = 3;
            ImgPlus<UnsignedShortType> merge = createEmptyImgPlusForMultipleChannels(
                    width, scaleX,
                    height,scaleY,
                    noOfChannels,
                    slices,scaleZ,
                    1,
                    "microns");
            copyImgPlusToChannel(ligninStackNonSmoothed,merge,0);
            copyImgPlusToChannel(celluloseStackNonSmoothed,merge,1);
            copyImgPlusToChannel(hybridStackNonSmoothed,merge,2);
//            copyByteProcessorToChannel(edgeAndCentroidMaskStack, merge,3);
            ImagePlus mergeImagePlus= ImageJFunctions.wrapUnsignedShort(merge, merge.getName());
            FileSaver mergeImagePlusSaver = new FileSaver(mergeImagePlus);
            mergeImagePlusSaver.saveAsTiff(completeOutPutPath.toAbsolutePath().toString());
            IJ.log("Saving SideView Complete: " + imageData.getImagePath().getFileName());
        }

        return null;
    }

    private static ImgPlus<UnsignedShortType>
    createEmptyImgPlusForMultipleChannels (long x,
                                           double scaleX,
                                           long y,
                                           double scaleY,
                                           long noOfChannels,
                                           long z,
                                           double scaleZ,
                                           long noOftimePoints,
                                           String unit
    ){
        // Assign axis types
        AxisType[] axisTypes = new AxisType[] {
                Axes.X, Axes.Y,  Axes.CHANNEL, Axes.Z,Axes.TIME
        };
        long[] dims = new long[] {x, y, noOfChannels, z, noOftimePoints};
        Img<UnsignedShortType> img = new ArrayImgFactory<>(new UnsignedShortType()).create(dims);
        ImgPlus<UnsignedShortType> imgPlus = new ImgPlus<>(img);
        // the default order of dimension in imagej2 is: X, Y, CHANNELS, Z , TIME; This corresponds to index 0,1,2,3,4
        imgPlus.setAxis(new DefaultLinearAxis(Axes.X,unit,scaleX),0);
        imgPlus.setAxis(new DefaultLinearAxis(Axes.Y,unit,scaleY),1);
        imgPlus.setAxis(new DefaultLinearAxis(Axes.Z,unit,scaleZ),3);
        imgPlus.setAxis(new DefaultLinearAxis(Axes.CHANNEL,"",1),2);
        imgPlus.setAxis(new DefaultLinearAxis(Axes.TIME,"",1),4);
//        for (int i = 0; i < axisTypes.length; i++) {
//            imgPlus.setAxis(new DefaultLinearAxis(axisTypes[i]), i);
//        }
        return imgPlus;
    }

    public static void copyImgPlusToChannel(RandomAccessibleInterval<FloatType> source, ImgPlus<UnsignedShortType> imgPlus, int channelPosition) {
        int channelDim = imgPlus.dimensionIndex(Axes.CHANNEL);
        // Get a view of just the desired channel
        RandomAccessibleInterval<UnsignedShortType> desiredChannel =
                Views.hyperSlice(imgPlus, channelDim, channelPosition);
        // Calculate min/max from source
        double[] minMax = getMinMax(source);
        // Use cursors for efficient iteration
        Cursor<FloatType> srcCursor = Views.flatIterable(source).cursor();
        Cursor<UnsignedShortType> destCursor = Views.flatIterable(desiredChannel).cursor();
        // loop and copy data
        while (srcCursor.hasNext()) {
            destCursor.next().set(FloatTypeToUnsignedShort(srcCursor.next(),minMax[0],minMax[1]));
        }
        // Update display range for channel
        imgPlus.setChannelMinimum(channelPosition, minMax[0]);
        imgPlus.setChannelMaximum(channelPosition, minMax[1]);
    }

    public static void copyByteProcessorToChannel(ImageStack source, ImgPlus<UnsignedShortType> imgPlus, int channelPosition) {
        int totalNumberOfPixelsInStack = source.getWidth()*source.getHeight()*source.getSize();
        int totalNumberOfPixelsInSlice = source.getWidth()*source.getHeight();
        byte[] bytes = new byte[totalNumberOfPixelsInStack];
        for (int z = 1; z < source.size()+1; z++) {
            byte[] bytes1 = (byte[]) source.getProcessor(z).getPixels();
            for (int i = 0; i < bytes1.length; i++) {
                bytes[totalNumberOfPixelsInSlice*(z-1)+i]=bytes1[i];
            }
        }
        int channelDim = imgPlus.dimensionIndex(Axes.CHANNEL);
        // Get a view of just the desired channel
        RandomAccessibleInterval<UnsignedShortType> desiredChannel =
                Views.hyperSlice(imgPlus, channelDim, channelPosition);
        // Use cursors for efficient iteration
        Cursor<UnsignedShortType> destCursor = Views.flatIterable(desiredChannel).cursor();
        // loop and copy data
        int index = 0;
        while (destCursor.hasNext()) {
            destCursor.next().set(byteToUnsignedShort(bytes[index]));
            index ++;
        }
        // Update display range for channel
        imgPlus.setChannelMinimum(channelPosition, 0);
        imgPlus.setChannelMaximum(channelPosition, 65535);
    }




    // Helper method to get min/max values
    private static double[] getMinMax(RandomAccessibleInterval<FloatType> image) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (FloatType pixel : Views.flatIterable(image)) {
            float val = pixel.get();
            min = Math.min(min, val);
            max = Math.max(max, val);
        }

        return new double[]{min, max};
    }

    // convert float value to unsignedShortType
    private static UnsignedShortType FloatTypeToUnsignedShort(FloatType floatTypeValue, double min, double max) {
        float value = floatTypeValue.get();
        // scale value to 0–65535
        double scaled = (value - min) / (max - min) * 65535;
        // clamp to 0–65535
        scaled = Math.max(0, Math.min(65535, scaled));
        UnsignedShortType ushort = new UnsignedShortType();
        ushort.set((int) Math.round(scaled));
        return ushort;
    }

    private static UnsignedShortType byteToUnsignedShort(byte byteValue){
        int unsignedValue = byteValue & 0xFF;
        // scale value to 0–65535
        double scaled = (double) (unsignedValue) / (255) * 65535;
        // clamp to 0–65535
        scaled = Math.max(0, Math.min(65535, scaled));
        UnsignedShortType ushort = new UnsignedShortType();
        ushort.set((int) Math.round(scaled));
        return ushort;
    }
}
