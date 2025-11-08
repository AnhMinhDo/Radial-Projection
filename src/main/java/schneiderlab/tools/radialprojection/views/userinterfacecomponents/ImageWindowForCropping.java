package schneiderlab.tools.radialprojection.views.userinterfacecomponents;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.Toolbar;
import schneiderlab.tools.radialprojection.imageprocessor.core.ProjectionType;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class ImageWindowForCropping extends ImageWindow {
//    private int currentToolId;
    private final Vessel vessel;
//    private final ProjectionType projectionType;
    private final int expectedToolId;
    private final List<ImagePlus> imagePlusList;

    public ImageWindowForCropping(ImagePlus imp,
                                  Vessel vessel,
                                    List<ImagePlus> all3ImagePlusObjects,
                                  int toolIdForCroppingImage) {
        super(imp);
        this.vessel = vessel;
        this.imagePlusList = all3ImagePlusObjects;
//        this.projectionType = projectionType;
        expectedToolId = toolIdForCroppingImage;
//        currentToolId = Toolbar.getToolId();
        getCanvas().addMouseListener(new MouseAdapterForCustomImageWindow(imp,
                this.vessel,
                this.imagePlusList,
                this.expectedToolId));
    }


    static class MouseAdapterForCustomImageWindow extends MouseAdapter {
        private int currentToolId;
        private final ImagePlus imp;
        private final List<ImagePlus> impList;
        private final Vessel vessel;
//        private final ProjectionType projectionType;
        private final int expectedToolId;
        private final Toolbar toolbar;

        public MouseAdapterForCustomImageWindow(ImagePlus imp,
                                                Vessel vessel,
                                                List<ImagePlus> impList,
                                                int expectedToolId) {
            this.currentToolId = Toolbar.getToolId();
            this.imp = imp;
            this.vessel = vessel;
            this.impList=impList;
//            this.projectionType = projectionType;
            this.expectedToolId = expectedToolId;
            this.toolbar = new Toolbar();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            IJ.setTool("rectangle");
        }

//        @Override
//        public void mouseExited(MouseEvent e) {
//            IJ.setTool(currentToolId);
//        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Roi roi = imp.getRoi();
            double xbase = roi.getXBase();
            double roiWidth = roi.getFloatWidth();
            Roi updatedRoi = new Roi(xbase,0,roiWidth,imp.getHeight());
            for (ImagePlus imagePlus: impList){
                imagePlus.setRoi(updatedRoi);
                imagePlus.updateAndDraw();
            }
            vessel.setSliceCroppedRange((int)xbase,(int)(xbase+roiWidth-1));
        }

        private void addCroppedImageToVessel(ImagePlus imagePlus,
                                             Vessel vessel,
                                             ProjectionType projectionType) {
            switch (projectionType) {
                case RADIAL_LIGNIN:
                    vessel.setRadialProjectionLignin(imagePlus);
                    break;
                case RADIAL_CELLULOSE:
                    vessel.setRadialProjectionCellulose(imagePlus);
                    break;
                case RADIAL_HYBRID:
                    vessel.setRadialProjectionHybrid(imagePlus);
                    break;
                case UNROLL_LIGNIN:
                    vessel.setUnrolledVesselLignin(imagePlus);
                    break;
                case UNROLL_CELLULOSE:
                    vessel.setUnrolledVesselCellulose(imagePlus);
                    break;
                case UNROLL_HYBRID:
                    vessel.setUnrolledVesselHybrid(imagePlus);
                    break;
                default:
                    System.err.println("No suitable ProjectionType");
                    break;
            }
        }

        private static ImagePlus croppedUpdateTitle(ImagePlus imp, Roi roi){
            imp.setRoi(roi);
            ImagePlus croppedImage = imp.crop();
            croppedImage.setTitle("cropped_" + imp.getTitle());
            return croppedImage;
        }
    }
}
