package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.ContourDetection;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.UnrollSingleVessel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class UnrollVesselWorker extends SwingWorker<Void, Void> {
    private final ImagePlus hybridStack;
    private final ImagePlus hybridSmoothedStack;
    private final ImagePlus ligninStack;
    private final ImagePlus celluloseStack;
    private final ImagePlus edgeBinaryMaskEdge;
    private final List<Vessel> vesselArrayList;
    private final boolean isFlipHorizontally;
//    private ArrayList<ImagePlus> vesselUnrolledArrayList;

    public UnrollVesselWorker(ImagePlus hybridSmoothedStack,
            ImagePlus hybridStack,
                              ImagePlus celluloseStack,
                              ImagePlus ligninStack,
                                 ImagePlus edgeBinaryMaskEdge,
                                 List<Vessel> vesselArrayList,
                              boolean isFlipHorizontally) {
        this.hybridStack = hybridStack;
        this.hybridSmoothedStack = hybridSmoothedStack;
        this.ligninStack = ligninStack;
        this.celluloseStack = celluloseStack;
        this.edgeBinaryMaskEdge =edgeBinaryMaskEdge;
        this.vesselArrayList= vesselArrayList;
        this.isFlipHorizontally= isFlipHorizontally;
    }

//    public ArrayList<ImagePlus> getVesselPolarProjectionArrayList() {
//        return vesselUnrolledArrayList;
//    }

    @Override
    protected Void doInBackground() {
//        vesselUnrolledArrayList = new ArrayList<>(vesselArrayList.size()*3); // 3 for lignin channel, cellulose channel, hybrid
        int currentProgress = 0;
        int increment = (int) 100.0/(vesselArrayList.size()*3);
        setProgress(currentProgress);
        for (int i = 0; i < vesselArrayList.size(); i++) {
            // create objects for Unrolling class
            UnrollSingleVessel unrolledLignin = new UnrollSingleVessel(hybridSmoothedStack,
                    ligninStack,
                    edgeBinaryMaskEdge,
                    vesselArrayList.get(i).getCentroidArrayList(),
                    5 // 5 degree is considered adequately small angle
            );
            UnrollSingleVessel unrolledCellulose = new UnrollSingleVessel(hybridSmoothedStack,
                    celluloseStack,
                    edgeBinaryMaskEdge,
                    vesselArrayList.get(i).getCentroidArrayList(),
                    5 // 5 degree is considered adequately small angle
            );
            UnrollSingleVessel unrolledHybrid = new UnrollSingleVessel(hybridSmoothedStack,
                    hybridStack,
                    edgeBinaryMaskEdge,
                    vesselArrayList.get(i).getCentroidArrayList(),
                    5 // 5 degree is considered adequately small angle
            );
            // start unrolling
            ImagePlus vesselUnrolledHybrid=unrolledHybrid.process();
            currentProgress= currentProgress+increment;
            setProgress(currentProgress);
            ImagePlus vesselUnrolledLignin=unrolledLignin.process();
            currentProgress= currentProgress+increment;
            setProgress(currentProgress);
            ImagePlus vesselUnrolledCellulose=unrolledCellulose.process();
            currentProgress= currentProgress+increment;
            setProgress(currentProgress);
            String imageTitleHybrid = "Unrolled Vessel " + (i + 1) + " Hybrid";
            String imageTitleCellulose = "Unrolled Vessel " + (i + 1) + " Cellulose channel";
            String imageTitleLignin = "Unrolled Vessel " + (i + 1) + " Lignin channel";
            vesselUnrolledHybrid.setTitle(imageTitleHybrid);
            vesselUnrolledLignin.setTitle(imageTitleLignin);
            vesselUnrolledCellulose.setTitle(imageTitleCellulose);
//            vesselUnrolledArrayList.add(vesselUnrolledHybrid);
//            vesselUnrolledArrayList.add(vesselUnrolledLignin);
//            vesselUnrolledArrayList.add(vesselUnrolledCellulose);
            if (isFlipHorizontally){ // Flip the radial projection if required
                vesselUnrolledHybrid.getProcessor().flipHorizontal();
                vesselUnrolledLignin.getProcessor().flipHorizontal();
                vesselUnrolledCellulose.getProcessor().flipHorizontal();
            }
            vesselArrayList.get(i).setUnrolledVesselHybrid(vesselUnrolledHybrid);
            vesselArrayList.get(i).setUnrolledVesselLignin(vesselUnrolledLignin);
            vesselArrayList.get(i).setUnrolledVesselCellulose(vesselUnrolledCellulose);
        }
        // Contour tracing hybrid channel
        for (int i = 0; i < vesselArrayList.size(); i++){
            // get the hybrid
            ImagePlus hybridImagePlus = vesselArrayList.get(i).getUnrolledVesselHybrid();
            ContourDetection contourDetection = new ContourDetection(hybridImagePlus.getProcessor());
            ByteProcessor contour = contourDetection.process();
            String title = "Contour Vessel " + (i + 1) + " Hybrid";
            ImagePlus contourImagePlus = new ImagePlus(title,contour);
            vesselArrayList.get(i).setContour(contourImagePlus);
        }
        return null;
    }
}
