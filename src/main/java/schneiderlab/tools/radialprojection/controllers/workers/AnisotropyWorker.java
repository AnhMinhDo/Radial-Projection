package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import schneiderlab.tools.radialprojection.imageprocessor.core.FibrilTool;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AnisotropyWorker extends SwingWorker<Void, Void> {
    private List<Vessel> vesselList;
    private int windowSize;
    private int numberOfRandomBox;


    public AnisotropyWorker(List<Vessel> vesselList) {
        this(vesselList,70,100);
    }

    public AnisotropyWorker(List<Vessel> vesselList,
                            int windowSize,
                            int numberOfRandomBox) {
        this.vesselList = vesselList;
        this.windowSize = windowSize;
        this.numberOfRandomBox=numberOfRandomBox;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for(Vessel vessel : vesselList){
            DescriptiveStatistics statsOrientation = new DescriptiveStatistics();
            DescriptiveStatistics statsAnisotropy = new DescriptiveStatistics();
            for (int i = 0; i < numberOfRandomBox+1; i++) {
                ImagePlus randombox = selectRandomBox(vessel.getRadialProjectionHybrid(),windowSize);
                FibrilTool fibrilTool = new FibrilTool(randombox);
                fibrilTool.calculate();
                statsOrientation.addValue(fibrilTool.getOrientation());
                statsAnisotropy.addValue(fibrilTool.getAnisotropy());
            }
            vessel.setMeanAnisotropy(statsAnisotropy.getMean());
            vessel.setMeanBandOrientation(statsOrientation.getMean());
            vessel.setSdAnisotropy(statsAnisotropy.getStandardDeviation());
            vessel.setSdBandOrientation(statsOrientation.getStandardDeviation());
            vessel.setNoOfRandomBox(numberOfRandomBox);
        }
        return null;
    }

    // select a point in range 0 to width-window size and build an ImagePlus object from that
    private static ImagePlus selectRandomBox(ImagePlus input, int windowSize){
        int min = 0;
        int max = input.getWidth()-windowSize;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max); // inclusive range
        int x = randomNum;      // top-left x coordinate
        int y = 0;      // top-left y coordinate
        int width = windowSize; // crop width
        int height = input.getHeight(); // crop height
        input.setRoi(new Rectangle(x, y, width, height));
        return input.crop();
    }
}
