package schneiderlab.tools.radialprojection.imageprocessor.core.segmentation;

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageProcessor;

public class Measure {
    private final ImageProcessor input;
    private double areaInPixel;
    private double perimeterInPixel;
    private double circularityInPixel;
    private ResultsTable rt;
    private int trueLabel;

    public Measure(ImageProcessor input, int trueLabel) {
        this.input = input;
        this.trueLabel=trueLabel;
    }

    public double getAreaInPixel() {
        return areaInPixel;
    }

    public double getPerimeterInPixel() {
        return perimeterInPixel;
    }

    public double getCircularityInPixel() {
        return circularityInPixel;
    }

    public void process(){
        performAnalyzer();
    }

    private void performAnalyzer(){
        ImageProcessor maskIp = this.input.duplicate();
        maskIp.setThreshold(trueLabel,trueLabel,ImageProcessor.BLACK_AND_WHITE_LUT);
        ImagePlus mask = new ImagePlus("binary mask", maskIp);
        this.rt = new ResultsTable();
        Analyzer.setResultsTable(rt);
        // Only area, perimeter, circularity
        int measurements = Measurements.AREA | Measurements.PERIMETER | Measurements.CIRCULARITY;
        int options = ParticleAnalyzer.SHOW_NONE;
        ParticleAnalyzer pa = new ParticleAnalyzer(options, measurements, rt, 0, Double.POSITIVE_INFINITY);
        pa.analyze(mask);
        this.areaInPixel = rt.getValue("Area", 0);
        // Access results directly, no window
        this.perimeterInPixel = rt.getValue("Perim.", 0);
        this.circularityInPixel = rt.getValue("Circ.", 0);
    }
}
