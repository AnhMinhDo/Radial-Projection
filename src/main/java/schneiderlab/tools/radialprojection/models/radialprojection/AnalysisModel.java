package schneiderlab.tools.radialprojection.models.radialprojection;

import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;

import java.util.ArrayList;
import java.util.List;

public class AnalysisModel {
    private ImageData<UnsignedShortType, FloatType> imageData;
    private List<ImageData<UnsignedShortType, FloatType>> imageDataList = new ArrayList<>();

    // anisotropy
    private int numberOfRandomBoxes;
    private int randomBoxWidth;

    public AnalysisModel() {
    }

    public ImageData<UnsignedShortType, FloatType> getImageData() {
        return imageData;
    }

    public void setImageData(ImageData<UnsignedShortType, FloatType> imageData) {
        imageDataList.add(imageData);
        this.imageData = imageData;
    }

    public List<ImageData<UnsignedShortType, FloatType>> getImageDataList() {
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
