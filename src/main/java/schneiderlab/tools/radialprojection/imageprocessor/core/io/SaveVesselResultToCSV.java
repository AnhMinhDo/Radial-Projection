package schneiderlab.tools.radialprojection.imageprocessor.core.io;

import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveVesselResultToCSV {
    // location of the output csv
    // define the header
    // method to get the info from each vessel object and add to the csvPrinter using printRecord, Arrays.asList and flush

    private List<ImageData<UnsignedShortType, FloatType>> imagesToExport;
//    private List<Vessel> allVesselList = new ArrayList<>();
    private boolean combine;

    public SaveVesselResultToCSV(List<ImageData<UnsignedShortType, FloatType>> imagesToExport) {
        this(imagesToExport, false);
    }

    public SaveVesselResultToCSV(List<ImageData<UnsignedShortType, FloatType>> imagesToExport, boolean combine) {
        this.imagesToExport = imagesToExport;
        this.combine = combine;
    }

    public void flush()throws IOException {
        if (!combine){
            // --- INDIVIDUAL MODE: one CSV file per image ---
            // loop through all ImageData Object and save the analysis Info
            for (ImageData<UnsignedShortType, FloatType> imageData : imagesToExport) {
                Path dirOutputPath = imageData.getImageOutputPath();
                String imageName = RadialProjectionUtils.filenameWithoutExtension(imageData.getImagePath().getFileName().toString());
//                Path finalDirPath = dirOutputPath.resolve(imageName + "_Out");
//                Files.createDirectories(finalDirPath);
                Path finalCsvPath = dirOutputPath.resolve("Vessel_Analysis.csv");
//            Path finalCsvPath = dirOutputPath.resolve("Vessel_Analysis.csv");
                List<Vessel> vesselList = imageData.getVesselList();
                // performing saving the csv file to the designated directory
                try (
                        BufferedWriter writer = Files.newBufferedWriter(finalCsvPath);
                        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                                .withHeader(Arrays.stream(CsvHeader.values())
                                        .map(CsvHeader::get)
                                        .toArray(String[]::new)));
                ) {
                    // for each Vessel in the list, get the coresponding info and printRecord
                    for (int i = 0; i < vesselList.size(); i++) {
                        Vessel vessel = vesselList.get(i);
                        csvPrinter.printRecord(
                                dirOutputPath.toString(),// folder of the original file
                                imageName.toString(),// name of the original file
                                i + 1, // vessel index
                                vessel.getMeanDiameter(),
                                vessel.getSdDiameter(),
                                vessel.getNoOfSlice(),
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
                                vessel.getSdBandOrientation(),// SD band orientation
                                vessel.getMeanSpacing()// vessel.getMeanSpacing()// Mean spacing
                        );
                    }
                }
            }
    }
        if(combine){
            // --- COMBINED MODE: one CSV file for all images ---
            Path combinedPath = imagesToExport.get(imagesToExport.size()-1).getImageOutputPath().getParent().resolve("Combined_Vessel_Analysis.csv");
            try (
                    BufferedWriter writer = Files.newBufferedWriter(combinedPath);
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                            .withHeader(Arrays.stream(CsvHeader.values())
                                    .map(CsvHeader::get)
                                    .toArray(String[]::new)))
            ) {
                for (ImageData<UnsignedShortType, FloatType> imageData : imagesToExport) {
                    Path dirOutputPath = imageData.getImageOutputPath().getParent();
                    String imageName = RadialProjectionUtils.filenameWithoutExtension(
                            imageData.getImagePath().getFileName().toString());
                    List<Vessel> vesselList = imageData.getVesselList();

                    for (int i = 0; i < vesselList.size(); i++) {
                        Vessel vessel = vesselList.get(i);
                        csvPrinter.printRecord(
                                dirOutputPath.toString(),
                                imageName,
                                i + 1,
                                vessel.getMeanDiameter(),
                                vessel.getSdDiameter(),
                                vessel.getNoOfSlice(),
                                vessel.getMeanCircularity(),
                                vessel.getSdCircularity(),
                                vessel.getNoOfRandomLineScan(),
                                vessel.getLengthOfLineScan(),
                                vessel.getNoOfBands(),
                                vessel.getMeanBandWidth(),
                                vessel.getSdBandWidth(),
                                vessel.getNoOfGaps(),
                                vessel.getMeanGapWidth(),
                                vessel.getSdGapWidth(),
                                vessel.getNoOfRandomBox(),
                                vessel.getMeanAnisotropy(),
                                vessel.getSdAnisotropy(),
                                vessel.getMeanBandOrientation(),
                                vessel.getSdBandOrientation(),
                                vessel.getMeanSpacing()
                        );
                    }
                }
                csvPrinter.flush();
            }
        }
    }
}
