package schneiderlab.tools.radialprojection.imageprocessor.core.io;

import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
//import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.utils.RadialProjectionUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class SaveVesselResultToXLSX {
    private List<ImageData<UnsignedShortType, FloatType>> imagesToExport;
    private boolean combine;

    public SaveVesselResultToXLSX(List<ImageData<UnsignedShortType, FloatType>> imagesToExport) {
        this(imagesToExport, false);
//        this.imagesToExport = imagesToExport;
    }

    public SaveVesselResultToXLSX(List<ImageData<UnsignedShortType, FloatType>> imagesToExport, boolean combine) {
        this.imagesToExport = imagesToExport;
        this.combine = combine;
    }

    public void flush() throws IOException {
        if (!combine){
            // --- INDIVIDUAL MODE: one XLSX per image ---
            for (ImageData<UnsignedShortType, FloatType> imageData : imagesToExport) {
                Path dirOutputPath = imageData.getImageOutputPath();
                String imageName = RadialProjectionUtils.filenameWithoutExtension(imageData.getImagePath().getFileName().toString());
//                Path finalDirPath = dirOutputPath.resolve(imageName + "_Out");
//                Files.createDirectories(finalDirPath);
                Path finalXlsxPath = dirOutputPath.resolve("Vessel_Analysis.xlsx");

                List<Vessel> vesselList = imageData.getVesselList();
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Vessel Analysis");

                // create header row
                XSSFRow headerRow = sheet.createRow(0);
                String[] headers = Arrays.stream(CsvHeader.values())
                        .map(CsvHeader::get)
                        .toArray(String[]::new);
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                // fill data rows
                for (int i = 0; i < vesselList.size(); i++) {
                    Vessel vessel = vesselList.get(i);
                    XSSFRow row = sheet.createRow(i + 1); // row 0 is header
                    int col = 0;
                    row.createCell(col++).setCellValue(dirOutputPath.toString());
                    row.createCell(col++).setCellValue(imageName);
                    row.createCell(col++).setCellValue(i + 1);
                    row.createCell(col++).setCellValue(vessel.getMeanDiameter());
                    row.createCell(col++).setCellValue(vessel.getSdDiameter());
                    row.createCell(col++).setCellValue(vessel.getNoOfSlice());
                    row.createCell(col++).setCellValue(vessel.getMeanCircularity());
                    row.createCell(col++).setCellValue(vessel.getSdCircularity());
                    row.createCell(col++).setCellValue(vessel.getNoOfRandomLineScan());
                    row.createCell(col++).setCellValue(vessel.getLengthOfLineScan());
                    row.createCell(col++).setCellValue(vessel.getNoOfBands());
                    row.createCell(col++).setCellValue(vessel.getMeanBandWidth());
                    row.createCell(col++).setCellValue(vessel.getSdBandWidth());
                    row.createCell(col++).setCellValue(vessel.getNoOfGaps());
                    row.createCell(col++).setCellValue(vessel.getMeanGapWidth());
                    row.createCell(col++).setCellValue(vessel.getSdGapWidth());
                    row.createCell(col++).setCellValue(vessel.getNoOfRandomBox());
                    row.createCell(col++).setCellValue(vessel.getMeanAnisotropy());
                    row.createCell(col++).setCellValue(vessel.getSdAnisotropy());
                    row.createCell(col++).setCellValue(vessel.getMeanBandOrientation());
                    row.createCell(col++).setCellValue(vessel.getSdBandOrientation());
                    row.createCell(col++).setCellValue(vessel.getMeanSpacing());
                }

                // write workbook to file
                try (FileOutputStream out = new FileOutputStream(finalXlsxPath.toFile())) {
                    workbook.write(out);
                }
                workbook.close();
            }
    }
        if(combine){
            // --- COMBINED MODE: store all in one XLSX ---
            XSSFWorkbook workbookCombine = new XSSFWorkbook();
            XSSFSheet sheet = workbookCombine.createSheet("Vessel Analysis");
            // create header row
            XSSFRow headerRow = sheet.createRow(0);
            String[] headers = Arrays.stream(CsvHeader.values())
                    .map(CsvHeader::get)
                    .toArray(String[]::new);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            int rowNum = 1;
            for (ImageData<UnsignedShortType, FloatType> imageData : imagesToExport) {
                Path dirOutputPath = imageData.getImageOutputPath();
                String imageName = RadialProjectionUtils.filenameWithoutExtension(
                        imageData.getImagePath().getFileName().toString());
                List<Vessel> vesselList = imageData.getVesselList();

                for (int i = 0; i < vesselList.size(); i++) {
                    Vessel vessel = vesselList.get(i);
                    XSSFRow row = sheet.createRow(rowNum++);
                    int col = 0;
                    row.createCell(col++).setCellValue(dirOutputPath.toString());
                    row.createCell(col++).setCellValue(imageName);
                    row.createCell(col++).setCellValue(i + 1);
                    row.createCell(col++).setCellValue(vessel.getMeanDiameter());
                    row.createCell(col++).setCellValue(vessel.getSdDiameter());
                    row.createCell(col++).setCellValue(vessel.getNoOfSlice());
                    row.createCell(col++).setCellValue(vessel.getMeanCircularity());
                    row.createCell(col++).setCellValue(vessel.getSdCircularity());
                    row.createCell(col++).setCellValue(vessel.getNoOfRandomLineScan());
                    row.createCell(col++).setCellValue(vessel.getLengthOfLineScan());
                    row.createCell(col++).setCellValue(vessel.getNoOfBands());
                    row.createCell(col++).setCellValue(vessel.getMeanBandWidth());
                    row.createCell(col++).setCellValue(vessel.getSdBandWidth());
                    row.createCell(col++).setCellValue(vessel.getNoOfGaps());
                    row.createCell(col++).setCellValue(vessel.getMeanGapWidth());
                    row.createCell(col++).setCellValue(vessel.getSdGapWidth());
                    row.createCell(col++).setCellValue(vessel.getNoOfRandomBox());
                    row.createCell(col++).setCellValue(vessel.getMeanAnisotropy());
                    row.createCell(col++).setCellValue(vessel.getSdAnisotropy());
                    row.createCell(col++).setCellValue(vessel.getMeanBandOrientation());
                    row.createCell(col++).setCellValue(vessel.getSdBandOrientation());
                    row.createCell(col++).setCellValue(vessel.getMeanSpacing());
                }
            }
            // Save combined file to parent directory
            Path combinedPath = imagesToExport.get(imagesToExport.size()-1).getImageOutputPath().getParent().resolve("Combined_Vessel_Analysis.xlsx"); // save the combine in the parent dir of the last Image in the list
            try (FileOutputStream out = new FileOutputStream(combinedPath.toFile())) {
                workbookCombine.write(out);
            }
            workbookCombine.close();
        }
    }
}
