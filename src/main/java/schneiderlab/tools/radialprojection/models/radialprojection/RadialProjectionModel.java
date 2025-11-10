package schneiderlab.tools.radialprojection.models.radialprojection;

import ij.ImagePlus;
import ij.gui.ImageWindow;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.ImageWindowGroupController;

import java.util.ArrayList;
import java.util.List;

public class RadialProjectionModel {
    private ImagePlus lignin;
    private ImagePlus hybrid;
    private ImagePlus cellulose;
    private List<Vessel> vesselArrayList;
    private ImageData<UnsignedShortType, FloatType> imageData;
    private List<ImageWindowGroupController> vesselRadialProjectionImageWindowGroupList = new ArrayList<>();
    private List<ImageWindowGroupController> vesselUnrollImageWindowGroupList = new ArrayList<>();

    public RadialProjectionModel() {
    }

    public ImageData<UnsignedShortType, FloatType> getImageData() {
        return imageData;
    }

    public void setImageData(ImageData<UnsignedShortType, FloatType> imageData) {
        this.imageData = imageData;
    }

    public ImagePlus getLignin() {
        return lignin;
    }

    public void setLignin(ImagePlus lignin) {
        this.lignin = lignin;
    }

    public ImagePlus getHybrid() {
        return hybrid;
    }

    public void setHybrid(ImagePlus hybrid) {
        this.hybrid = hybrid;
    }

    public ImagePlus getCellulose() {
        return cellulose;
    }

    public void setCellulose(ImagePlus cellulose) {
        this.cellulose = cellulose;
    }

    public List<Vessel> getVesselArrayList() {
        return vesselArrayList;
    }

    public void setVesselArrayList(List<Vessel> vesselArrayList) {
        this.vesselArrayList = vesselArrayList;
    }

    public List<ImageWindowGroupController> getVesselRadialProjectionImageWindowGroupList() {
        return vesselRadialProjectionImageWindowGroupList;
    }

    public void addVesselRadialProjectionImageWindowGroup(ImageWindowGroupController vesselRadialProjectionImageWindowGroup) {
        this.vesselRadialProjectionImageWindowGroupList.add(vesselRadialProjectionImageWindowGroup);
    }

    public void closeAllImageWindowGroup(){
        for(ImageWindowGroupController imageWindowGroupController: vesselRadialProjectionImageWindowGroupList){
            imageWindowGroupController.closeAllWindowInGroup();
        }
        for (ImageWindowGroupController imageWindowGroupController: vesselUnrollImageWindowGroupList){
            imageWindowGroupController.closeAllWindowInGroup();
        }
    }

    public List<ImageWindowGroupController> getVesselUnrollImageWindowGroupList() {
        return vesselUnrollImageWindowGroupList;
    }

    public void addVesselUnrollImageWindowGroup(ImageWindowGroupController vesselUnrollImageWindowGroup) {
        this.vesselUnrollImageWindowGroupList.add(vesselUnrollImageWindowGroup) ;
    }

}
