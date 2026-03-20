package schneiderlab.tools.radialprojection.imageprocessor.core.utils;

import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class RadialProjectionUtils {

    public static List<Point> deepCopyPoints(List<Point> points) {
        List<Point> copy = new ArrayList<>(points.size());
        for (Point p : points) {
            copy.add(new Point(p.x, p.y));
        }
        return copy;
    }

    public static ImagePlus copyAndConvertRandomAccessIntervalToImagePlus(RandomAccessibleInterval<UnsignedShortType> input, String name){
        // Create copy using cursors
        Img<UnsignedShortType> copy = ArrayImgs.unsignedShorts(Intervals.dimensionsAsLongArray(input));
        net.imglib2.Cursor<UnsignedShortType> srcCursor = Views.flatIterable(input).cursor();
        net.imglib2.Cursor<UnsignedShortType> dstCursor = copy.cursor();
        while (srcCursor.hasNext()) {
            dstCursor.next().set(srcCursor.next());
        }
        // Convert to ImagePlus
        ImagePlus impUnsignedShort = ImageJFunctions.wrapUnsignedShort(copy, name);
        impUnsignedShort.resetDisplayRange();
        return impUnsignedShort;
    }

    public static String filenameWithoutExtension(String filenameWithExtension){
        int idx = filenameWithExtension.lastIndexOf(".");
        return filenameWithExtension.substring(0,idx);
    }
}
