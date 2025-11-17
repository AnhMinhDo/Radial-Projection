package schneiderlab.tools.radialprojection.controllers.workers;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import org.scijava.Context;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;
import schneiderlab.tools.radialprojection.imageprocessor.core.polarprojection.PolarProjection;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.ContourDetection;
import schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.UnrollSingleVessel;

import javax.swing.*;
import java.util.List;

public class RadialProjectionAndUnrollingWorker extends SwingWorker<Void, Void> {
    private final ImagePlus hybridRawStack;
    private final ImagePlus hybridSmoothedStack;
    private final ImagePlus celluloseStack;
    private final ImagePlus ligninStack;
    private final ImagePlus edgeBinaryMaskEdge;
    private final List<Vessel> vesselArrayList;
    private double xyPixelSizeInMicron;
    private boolean isFlipHorizontally;
    private Context context;

    public RadialProjectionAndUnrollingWorker(ImagePlus hybridRawStack,
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
    @Override
    protected Void doInBackground() throws Exception {
        // Radial Projection
        int currentProgress = 0;
        int increment = (int) 100.0/(vesselArrayList.size()*3);
        setProgress(currentProgress);
        for (int i = 0; i < vesselArrayList.size(); i++) { // for each vessel
            vesselArrayList.get(i).resetCroppedRange(); // reset the range, in case the user has applied a range before, this give the user the option to redo the range selection
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
        // Unrolling
        currentProgress = 0;
        increment = (int) 100.0/(vesselArrayList.size()*3);
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
                    hybridRawStack,
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
