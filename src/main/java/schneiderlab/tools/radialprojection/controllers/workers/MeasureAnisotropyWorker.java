package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import ij.gui.Roi;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import schneiderlab.tools.radialprojection.imageprocessor.core.FibrilTool;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.models.radialprojection.RadialProjectionModel;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MeasureAnisotropyWorker extends SwingWorker<Void, Void> {
    private int imageWidth;
    private int imageHeight;
    private int boxWidth;
    private int boxHeight;
    private int noOfRandomBoxes;
    private List<Double> anisotropyList;
    private List<Double> orientationList;
    private RadialProjectionModel radialProjectionModel;

    public MeasureAnisotropyWorker(RadialProjectionModel radialProjectionModel, int boxHeight, int boxWidth, int noOfRandomBoxes) {
        this.radialProjectionModel = radialProjectionModel;
        this.boxHeight = boxHeight;
        this.boxWidth = boxWidth;
        this.noOfRandomBoxes = noOfRandomBoxes;
    }

    public List<Double> getAnisotropyList() {
        return anisotropyList;
    }

    public void setAnisotropyList(List<Double> anisotropyList) {
        this.anisotropyList = anisotropyList;
    }

    public List<Double> getOrientationList() {
        return orientationList;
    }

    public void setOrientationList(List<Double> orientationList) {
        this.orientationList = orientationList;
    }

    @Override
    protected Void doInBackground() throws Exception {
        int min = 0; int max = imageWidth-boxWidth;
        boxHeight = imageHeight;
        List<Vessel> vesselList = radialProjectionModel.getVesselArrayList();
        for (Vessel vessel:vesselList){
            ImagePlus radialProjectionHybrid = vessel.getRadialProjectionHybrid();
            DescriptiveStatistics statsOrientation = new DescriptiveStatistics();
            DescriptiveStatistics statsAnisotropy = new DescriptiveStatistics();
            for (int i = 0; i < noOfRandomBoxes+1; i++) {
                // choose a random integer point from 0 to imageWidth-boxLength
                int n = ThreadLocalRandom.current().nextInt(0, max + 1);
                Roi roi = new Roi(n,0,boxWidth,boxHeight);
                radialProjectionHybrid.getProcessor().setRoi(roi);
                ImagePlus roiImagePlus = radialProjectionHybrid.crop();
                FibrilTool fibrilTool = new FibrilTool(roiImagePlus);
                fibrilTool.calculate();
                double orientation = fibrilTool.getOrientation();
                double anisotropy = fibrilTool.getAnisotropy();
                statsOrientation.addValue(orientation);
                statsAnisotropy.addValue(anisotropy);
            }
            double meanOrientation = statsOrientation.getMean();
            double sdOrientation = statsOrientation.getStandardDeviation();
            double meanAnisotropy = statsAnisotropy.getMean();
            double sdAnisotropy = statsAnisotropy.getStandardDeviation();
            vessel.setMeanBandOrientation(meanOrientation);
            vessel.setSdBandOrientation(sdOrientation);
            vessel.setMeanAnisotropy(meanAnisotropy);
            vessel.setSdAnisotropy(sdAnisotropy);
        }


        return null;
    }


}
