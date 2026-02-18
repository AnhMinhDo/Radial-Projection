package schneiderlab.tools.radialprojection.views.userinterfacecomponents;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.Roi;
import ij.gui.StackWindow;
import schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized.VesselSerializable;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StackWindowRefineVesselMultipleChannelsImagePlus extends StackWindow {
    private ImageCanvas canvas;

    public StackWindowRefineVesselMultipleChannelsImagePlus(ImagePlus imp, VesselSerializable vesselSerializable) {
        super(imp);
        // add 2 more buttons below the ImageWindow
        ImagePlus imagePlus = this.getImagePlus();
        canvas = this.getCanvas();
        canvas.addMouseListener(new MouseAdapterForCustomImageWindow(imagePlus, vesselSerializable));
    }

    static class MouseAdapterForCustomImageWindow extends MouseAdapter {
        private final ImagePlus imp;
//        private final java.util.List<ImagePlus> impList;
        private final VesselSerializable vesselSerializable;

        public MouseAdapterForCustomImageWindow(ImagePlus imp,
                                                VesselSerializable vessel) {
            this.imp = imp;
            this.vesselSerializable = vessel;
//            this.impList=impList;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            IJ.setTool("rectangle");
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            Roi roi = imp.getRoi();
            double xbase = roi.getXBase();
            double roiWidth = roi.getFloatWidth();
            Roi updatedRoi = new Roi(xbase,0,roiWidth,imp.getHeight());
            imp.setRoi(updatedRoi);
            vesselSerializable.setSliceCropRangeStart((int)xbase);
            vesselSerializable.setSliceCropRangeEnd((int)(xbase+roiWidth-1));
//            vesselSerializable.setSliceCroppedRange(vesselSerializable.createSliceCroppedRange((int)xbase,(int)(xbase+roiWidth-1)));
        }


    }


}
