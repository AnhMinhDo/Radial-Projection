package schneiderlab.tools.radialprojection.controllers.workers.batchworkers;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializableUtils;
import schneiderlab.tools.radialprojection.models.batch.BatchModeGlobalStateModel;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.ImageWindowCentroidSelection;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class CentroidSelectionWorker extends SwingWorker<Void, Void> {
    private BatchModeModel batchModeModel;
    private BatchModeGlobalStateModel batchModeGlobalStateModel;
    private Context context;

    public CentroidSelectionWorker(BatchModeModel batchModeModel, BatchModeGlobalStateModel batchModeGlobalStateModel, Context context) {
        this.batchModeModel = batchModeModel;
        this.batchModeGlobalStateModel = batchModeGlobalStateModel;
        this.context = context;
    }

    @Override
    protected Void doInBackground() throws Exception {
        IJ.log("start the centroid selection worker");
        while(!batchModeGlobalStateModel.getCentroidSelectionQueue().isEmpty())
            {
            String serFile = batchModeGlobalStateModel.getFirstCentroidSelectionQueue();
            IJ.log("serialized object path: " + serFile);
            ImageDataSerializable imageDataSerializable = ImageDataSerializableUtils.imageDataDeserializeObject(Paths.get(serFile));
            IJ.log("complete deserialization");
            ImageData<UnsignedShortType, FloatType> imageData = ImageDataSerializableUtils.converSerializableToImageData(imageDataSerializable, context);
            IJ.log("complete conversion to ImageData object");
//            imageData = imageDataRetrieved;
            RandomAccessibleInterval<FloatType> smoothedStack  = imageData.getHybridStackSmoothed();
            int slideForTuning = 0;
            // get the firstSlide
            RandomAccessibleInterval<FloatType> just1Slide = Views.hyperSlice(smoothedStack,2,slideForTuning);
            // Copy the view to a new Img<FloatType>
            // Create copy using cursors
            Img<FloatType> copy = ArrayImgs.floats(Intervals.dimensionsAsLongArray(just1Slide));
            net.imglib2.Cursor<FloatType> srcCursor = Views.flatIterable(just1Slide).cursor();
            net.imglib2.Cursor<FloatType> dstCursor = copy.cursor();
            while (srcCursor.hasNext()) {
                dstCursor.next().set(srcCursor.next());
            }
            // Convert to ImagePlus
            ImagePlus impFloat = ImageJFunctions.wrap(copy, "smoothed Side View");
            impFloat.resetDisplayRange();
            ImagePlus impByte = new ImagePlus(impFloat.getTitle(),impFloat.getProcessor().convertToByte(true));
            impByte.resetDisplayRange();
            ImageWindowCentroidSelection iwcs = new ImageWindowCentroidSelection(impByte,imageData);
            CountDownLatch latch = new CountDownLatch(1);
            iwcs.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    latch.countDown();
                }
            });
            latch.await();

            // serialize the imageData object to ser file, include the centroid coordinates
            imageDataSerializable.setUserSelectedCentroidsList(imageData.getUserSelectedCentroidsList());
            imageDataSerializable.serializeObject();
            int totalTaskNumber = batchModeGlobalStateModel.getCentroidSelectionQueue().size();
            setProgress(totalTaskNumber-1);
            batchModeGlobalStateModel.addLastWatershedRadialProjectionQueue(serFile);
            batchModeGlobalStateModel.removeFirstCentroidSelectionQueue();

        }
        return null;
    }
}
