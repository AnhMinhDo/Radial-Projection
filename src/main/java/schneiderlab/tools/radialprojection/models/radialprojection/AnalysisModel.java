package schneiderlab.tools.radialprojection.models.radialprojection;

import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;

import java.util.ArrayList;
import java.util.List;

public class AnalysisModel {
    private ImageData<UnsignedShortType, UnsignedShortType> imageData;
    private List<ImageData<UnsignedShortType, UnsignedShortType>> imageDataList = new ArrayList<>();

    // anisotropy
    private int numberOfRandomBoxes;
    private int randomBoxWidth;

    public AnalysisModel() {
    }

    public ImageData<UnsignedShortType, UnsignedShortType> getImageData() {
        return imageData;
    }

    public void setImageData(ImageData<UnsignedShortType, UnsignedShortType> imageData) {
        imageDataList.add(imageData);
        this.imageData = imageData;
    }

    public List<ImageData<UnsignedShortType, UnsignedShortType>> getImageDataList() {
        return imageDataList;
    }

    public int getNumberOfRandomBoxes() {
        return numberOfRandomBoxes;
    }

    public void setNumberOfRandomBoxes(int numberOfRandomBoxes) {
        this.numberOfRandomBoxes = numberOfRandomBoxes;
    }

    public int getRandomBoxWidth() {
        return randomBoxWidth;
    }

    public void setRandomBoxWidth(int randomBoxWidth) {
        this.randomBoxWidth = randomBoxWidth;
    }
}
