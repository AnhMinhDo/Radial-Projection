package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.ImagePlus;
import ij.io.FileSaver;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;

import java.nio.file.Path;

public class SaveFirstSlice {
    private RandomAccessibleInterval<UnsignedShortType> imageStackOneChanelOny;
    private  Path tempDirPath;
    private Path firstSliceTempPath;

    public SaveFirstSlice(RandomAccessibleInterval<UnsignedShortType> imageStackOneChanelOny, Path tempDirPath, Path firstSliceTempPath) {
        this.imageStackOneChanelOny = imageStackOneChanelOny;
        this.tempDirPath = tempDirPath;
        this.firstSliceTempPath = firstSliceTempPath;
    }

    public void saveFirstSlice(){
        RandomAccessibleInterval<UnsignedShortType> firstSlice =  Views.hyperSlice(imageStackOneChanelOny,2,0); // dimension 2 is Z, position 0 is first slice
        ImagePlus imagePlus = ImageJFunctions.wrapUnsignedShort(firstSlice,"Hybrid First Slice");
        FileSaver fileSaver = new FileSaver(imagePlus);
        fileSaver.saveAsTiff(firstSliceTempPath.toAbsolutePath().toString());
    }
}
