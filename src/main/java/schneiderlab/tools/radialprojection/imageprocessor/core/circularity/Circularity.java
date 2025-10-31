package schneiderlab.tools.radialprojection.imageprocessor.core.circularity;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;

import java.awt.*;
import java.util.List;

import static ij.measure.Measurements.AREA;
import static ij.measure.Measurements.PERIMETER;

public class Circularity {
    private Vessel vessel;
    private ImageStack rawSegmentationStack;
    private List<Double> circularityList;

    public void calculate(){

    }

    private void measureCircularityWholeStack(){
        // use the vesselSliceDataArrayList
        // for each slice data
        for (int currentSLice = 1; currentSLice <= vessel.getNoOfSlice(); currentSLice++) {
            // get the true segmentation slice index and true label
            // remember ImageStack start with 1 but java List start with 0, must subtract to 1


            // create a mask with only that label
        }
    }

    static private Double measureCircularitySingleSlice(ImagePlus impBinary){
        int measurementFlags = Measurements.SHAPE_DESCRIPTORS;
        ResultsTable rt = new ResultsTable();
        Analyzer analyzer = new Analyzer(impBinary,measurementFlags, rt);
        analyzer.measure();
        return rt.getValue("Round",0);
    }
}
