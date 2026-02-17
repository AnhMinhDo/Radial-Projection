package schneiderlab.tools.radialprojection.imageprocessor.core.io;

import ij.IJ;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Utils {

    public static void checkAndCreateCombinedAnalysisFile(Path batchImageFolder, String analysisFileName) throws IOException {
        Path analysisfilePath = batchImageFolder.resolve(analysisFileName);
        if(!Files.exists(analysisfilePath)){
            if(!Files.exists(batchImageFolder)){
                Files.createDirectories(batchImageFolder);
            }
            // create a string[] of header
            String[] headers = Arrays.stream(CsvHeader.values())
                                    .map(CsvHeader::get)
                    .toArray(String[]::new);
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(headers)
                    .build();
            StringWriter sw = new StringWriter();
            try(BufferedWriter writer = Files.newBufferedWriter(analysisfilePath);
                    final CSVPrinter printer = new CSVPrinter(writer, csvFormat)){
                // this file has no record
                printer.flush();
            } catch (IOException e) {
                IJ.log("Error in creating the analysis.csv file");
            }
        }
    }

}
