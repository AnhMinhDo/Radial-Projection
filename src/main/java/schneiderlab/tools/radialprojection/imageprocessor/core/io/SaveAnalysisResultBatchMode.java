package schneiderlab.tools.radialprojection.imageprocessor.core.io;

import ij.IJ;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.imagedataserialized.ImageDataSerializable;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class SaveAnalysisResultBatchMode {
    private ImageDataSerializable imageDataSerializable;
    private Path analysisFilePath;
    private VesselSerializable vesselSerializable;
    private int vesselIdx;

    public SaveAnalysisResultBatchMode(VesselSerializable vesselSerializable,
                                       Path anaylsisFilePath,
                                       ImageDataSerializable imageDataSerializable,
                                       int vesselIdx) {
        this.imageDataSerializable = imageDataSerializable;
        this.analysisFilePath = anaylsisFilePath;
        this.vesselSerializable = vesselSerializable;
        this.vesselIdx = vesselIdx;
    }

    public void flush() throws IOException {
        // check if the file exist
        if(!Files.exists(analysisFilePath)){
            IJ.log("analysis.csv does not exists");
            return;
        }
        // append the record to csv file
        try (BufferedWriter writer = Files.newBufferedWriter(analysisFilePath, StandardOpenOption.APPEND);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)){
            Path dirOutputPath = Paths.get(imageDataSerializable.getImagePath()).getParent();
            String imageName = RadialProjectionUtils.filenameWithoutExtension(
                    Paths.get(imageDataSerializable.getImagePath()).getFileName().toString());
                VesselSerializable vessel = this.vesselSerializable;
                printer.printRecord(
                        dirOutputPath.toString(),// folder of the original file
                        imageName.toString(),// name of the original file
                        vesselIdx + 1, // vessel index
                        vessel.getMeanDiameter(),
                        vessel.getSdDiameter(),
                        vessel.getNumberOfSliceInStack(),
                        vessel.getMeanCircularity(),
                        vessel.getSdCircularity(),
                        vessel.getNoOfRandomLineScan(),// number of random linescan
                        vessel.getLengthOfLineScan(),// length of the line scan
                        vessel.getNoOfBands(),// total number of bands
                        vessel.getMeanBandWidth(),// mean band width
                        vessel.getSdBandWidth(),// sd band width
                        vessel.getNoOfGaps(),// total number of gaps
                        vessel.getMeanGapWidth(),// mean gap width
                        vessel.getSdGapWidth(),// sd gap width
                        vessel.getNoOfRandomBox(),// no. of random boxes
                        vessel.getMeanAnisotropy(),// Mean anisotropy
                        vessel.getSdAnisotropy(),// SD anisotropy
                        vessel.getMeanBandOrientation(),// Mean band orientation
                        vessel.getSdBandOrientation()// SD band orientation
//                        vessel.getMeanSpacing()// vessel.getMeanSpacing()// Mean spacing
                );

        }
    }
}
