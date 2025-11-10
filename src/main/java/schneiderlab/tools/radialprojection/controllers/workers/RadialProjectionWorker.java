package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.polarprojection.PolarProjection;

import javax.swing.*;
import java.util.List;

public class RadialProjectionWorker extends SwingWorker<Void, Void> {
    private final ImagePlus hybridRawStack;
    private final ImagePlus hybridSmoothedStack;
    private final ImagePlus celluloseStack;
    private final ImagePlus ligninStack;
    private final ImagePlus edgeBinaryMaskEdge;
    private final List<Vessel> vesselArrayList;
    private double xyPixelSizeInMicron;
    private boolean isFlipHorizontally;
    private Context context;

    public RadialProjectionWorker(ImagePlus hybridRawStack,
                                  ImagePlus hybridSmoothedStack,
                                  ImagePlus celluloseStack,
                                  ImagePlus ligninStack,
                                  ImagePlus edgeBinaryMaskEdge,
                                  List<Vessel> vesselArrayList,
                                  double xyPixelSizeInMicron,
                                  boolean isFlipHorizontally,
                                  Context context) {
        this.hybridRawStack = hybridRawStack;
        this.hybridSmoothedStack = hybridSmoothedStack;
        this.celluloseStack = celluloseStack;
        this.ligninStack = ligninStack;
        this.edgeBinaryMaskEdge =edgeBinaryMaskEdge;
        this.vesselArrayList= vesselArrayList;
        this.xyPixelSizeInMicron = xyPixelSizeInMicron;
        this.isFlipHorizontally = isFlipHorizontally;
        this.context= context;
    }

//    public ArrayList<ImagePlus> getVesselPolarProjectionArrayList() {
//        return vesselPolarProjectionArrayList;
//    }

    @Override
    protected Void doInBackground() {
//        vesselPolarProjectionArrayList = new ArrayList<>(vesselArrayList.size()*3);// 3 for lignin channel, cellulose channel, hybrid
        int currentProgress = 0;
        int increment = (int) 100.0/(vesselArrayList.size()*3);
        setProgress(currentProgress);
        for (int i = 0; i < vesselArrayList.size(); i++) { // for each vessel
            PolarProjection polarProjectionHybrid = new PolarProjection(hybridSmoothedStack,
                    hybridRawStack,
                    edgeBinaryMaskEdge,
                    vesselArrayList.get(i),
                    5, // 5 degree is considered adequately small angle
            xyPixelSizeInMicron,
                    context);
            PolarProjection polarProjectionCellulose = new PolarProjection(hybridSmoothedStack,celluloseStack,
                    edgeBinaryMaskEdge,
                    vesselArrayList.get(i),
                    5, // 5 degree is considered adequately small angle
                    xyPixelSizeInMicron,
                    context);
            PolarProjection polarProjectionLignin = new PolarProjection(hybridSmoothedStack,
                    ligninStack,
                    edgeBinaryMaskEdge,
                    vesselArrayList.get(i),
                    5, // 5 degree is considered adequately small angle
                    xyPixelSizeInMicron,
                    context);
            ImagePlus vesselPolarProjectionHybrid=polarProjectionHybrid.process();
            currentProgress= currentProgress+increment;
            setProgress(currentProgress);
            ImagePlus vesselPolarProjectionCellulose=polarProjectionCellulose.process();
            currentProgress= currentProgress+increment;
            setProgress(currentProgress);
            ImagePlus vesselPolarProjectionLignin=polarProjectionLignin.process();
            currentProgress= currentProgress+increment;
            setProgress(currentProgress);
            String imageTitleHybrid = "Radial Projection Vessel " + (i + 1) + " Hybrid";
            String imageTitleCellulose = "Radial Projection Vessel " + (i + 1) + " Cellulose channel";
            String imageTitleLignin = "Radial Projection Vessel " + (i + 1) + " Lignin channel";
            vesselPolarProjectionHybrid.setTitle(imageTitleHybrid);
            vesselPolarProjectionCellulose.setTitle(imageTitleCellulose);
            vesselPolarProjectionLignin.setTitle(imageTitleLignin);
            if(isFlipHorizontally){
                vesselPolarProjectionHybrid.getProcessor().flipHorizontal();
                vesselPolarProjectionLignin.getProcessor().flipHorizontal();
                vesselPolarProjectionCellulose.getProcessor().flipHorizontal();
            }
            vesselArrayList.get(i).setRadialProjectionHybrid(vesselPolarProjectionHybrid);
            vesselArrayList.get(i).setRadialProjectionLignin(vesselPolarProjectionLignin);
            vesselArrayList.get(i).setRadialProjectionCellulose(vesselPolarProjectionCellulose);
        }
        return null;
    }
}
