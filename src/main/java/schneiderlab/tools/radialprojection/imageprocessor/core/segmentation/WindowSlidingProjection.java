package schneiderlab.tools.radialprojection.imageprocessor.core.segmentation;

import java.util.Arrays;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.view.Views;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class WindowSlidingProjection {

    private int currentSliceProcess;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    public int getCurrentSlice() {
        return this.currentSliceProcess;
    }

    public void setNewCurrentSlice(int newCurrentSlice) {
        int previousSlice = this.currentSliceProcess;
        this.currentSliceProcess = newCurrentSlice;
        this.pcs.firePropertyChange("currentSlice", previousSlice, currentSliceProcess);
    }


    public void averageProjection(
            RandomAccessibleInterval<UnsignedShortType> input,
            RandomAccessibleInterval<UnsignedShortType> output,
            int windowSize,
            int depth,
            int width,
            int height){
        // perform window sliding Projection, each new slide is the average projection of all the slide in the window
        // Initialize a sum buffer for the output slice
        int[] sum = new int[(width * height)];
        for (long z = 0; z < depth; z++) {
            setNewCurrentSlice((int)z); // this is for updating the ProgressBar
            // Determine the slice window (handle boundaries)
            long startSlice = Math.max(0, z - windowSize / 2);
            long endSlice = Math.min(depth - 1, z + windowSize / 2);
            int numSlicesInWindow = (int) (endSlice - startSlice + 1);
            Arrays.fill(sum, 0);
            // Get the output slice (2D)
            RandomAccessibleInterval<UnsignedShortType> outputSlice = Views.hyperSlice(output, 2, z); // d=2 stands for the z dimension(slice)
            // Accumulate values from neighboring slices
            for (long zz = startSlice; zz <= endSlice; zz++) {
                RandomAccessibleInterval<UnsignedShortType> inputSlice = Views.hyperSlice(input, 2, zz); // d=2 stands for the z dimension(slice)
                Cursor<UnsignedShortType> cursor = Views.flatIterable(inputSlice).cursor();

                int pixelIndex = 0;
                while (cursor.hasNext()) {
                    sum[pixelIndex] += cursor.next().get();
                    pixelIndex++;
                }
            }
            // Compute average and write to output
            Cursor<UnsignedShortType> outputCursor = Views.flatIterable(outputSlice).cursor();
            int i = 0;
            while(outputCursor.hasNext()){
                outputCursor.next().set((int) Math.round((double) sum[i] / numSlicesInWindow));
                i++;
            }
        }
    }
}
