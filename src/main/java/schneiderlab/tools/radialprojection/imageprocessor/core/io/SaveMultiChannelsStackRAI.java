package schneiderlab.tools.radialprojection.imageprocessor.core.io;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import io.scif.services.DatasetIOService;
import net.imagej.DatasetService;
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
import org.scijava.Context;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;

public class SaveMultiChannelsStackRAI implements Callable<Void> {
    private List<RandomAccessibleInterval<FloatType>> raiList;
    private Context context;
    private int width;
    private int height;
    private int zSlices;
    private int numberOfChannels;
    private double scaleX;
    private double scaleY;
    private double scaleZ;
    private Path fileOutputPath;

    public SaveMultiChannelsStackRAI(List<RandomAccessibleInterval<FloatType>> raiList,
                                     Path fileOutputPath,
                                     int width,
                                     int height,
                                     int zSlices,
                                     double scaleX,
                                     double scaleY,
                                     double scaleZ ,
                                     Context context) {
        this.raiList = raiList;
        this.context = context;
        this.width = width;
        this.height = height;
        this.zSlices = zSlices;
        this.numberOfChannels = raiList.size();
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.fileOutputPath = fileOutputPath;
    }

    @Override
    public Void call() throws IOException {
        actualImageSavingOperation();
        return null;
    }

    private void actualImageSavingOperation(){
        ImgPlus<UnsignedShortType> merge = createEmptyImgPlusForMultipleChannels(
                width, scaleX,
                height,scaleY,
                numberOfChannels,
                zSlices,scaleZ,
                1,
                "microns");
        for (int i = 0; i < raiList.size(); i++) {
            copyImgPlusToChannel(raiList.get(i), merge,i);
        }
        ImagePlus mergeImagePlus= ImageJFunctions.wrapUnsignedShort(merge, merge.getName());
        FileSaver mergeImagePlusSaver = new FileSaver(mergeImagePlus);
        mergeImagePlusSaver.saveAsTiff(fileOutputPath.toString());
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
