package schneiderlab.tools.radialprojection.imageprocessor.core.io;

public enum CsvHeader {
    FOLDER_NAME("Folder name"),
    FILE_NAME("File name"),
    VESSEL("Vessel"),
    MEAN_DIAMETER("Mean diameter [µm]"),
    SD_DIAMETER("SD diameter [µm]"),
    N_Z_SLICES("N (no. of z-slices measured)"),
    MEAN_CIRCULARITY("Mean circularity [0-1]"),
    SD_CIRCULARITY("SD circularity [0-1]"),
    NO_RANDOM_LINESCANS("No. of random linescans"),
    LENGTH_LINESCAN("Length of linescan [µm]"),
    TOTAL_BANDS("Total number of bands"),
    MEAN_BAND_WIDTH("Mean band width [µm]"),
    SD_BAND_WIDTH("SD band width [µm]"),
    TOTAL_GAPS("Total number of gaps"),
    MEAN_GAP_WIDTH("Mean gap width [µm]"),
    SD_GAP_WIDTH("SD gap width [µm]"),
    NO_RANDOM_BOXES("No. of random boxes"),
    MEAN_ANISOTROPY("Mean anisotropy [0-1]"),
    SD_ANISOTROPY("SD anisotropy [0-1]"),
    MEAN_BAND_ORIENTATION("Mean band orientation [deg]"),
    SD_BAND_ORIENTATION("SD band orientation [deg]"),
    MEAN_SPACING("Mean spacing [µm]");


    private final String header;

    CsvHeader(String header) {
        this.header = header;
    }

    public String get() {
        return header;
    }
}
