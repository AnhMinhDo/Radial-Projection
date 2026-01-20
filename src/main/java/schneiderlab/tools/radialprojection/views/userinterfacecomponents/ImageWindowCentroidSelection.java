package schneiderlab.tools.radialprojection.views.userinterfacecomponents;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.*;
import schneiderlab.tools.radialprojection.imageprocessor.core.ImageData;
import schneiderlab.tools.radialprojection.models.batch.BatchModeModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageWindowCentroidSelection extends ImageWindow {
    private Button okButton;
    private Button refreshButton;
    private Button cancelButton;
    private ImageCanvas canvas;
//    private PointRoi pointRoi;

    public ImageWindowCentroidSelection(ImagePlus imp, ImageData imageData) {
        super(imp);
        // add 2 more buttons below the ImageWindow
        ImagePlus imagePlus = this.getImagePlus();
        canvas = this.getCanvas();
        Panel panel = new Panel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel.setLayout(new FlowLayout());
        okButton = new Button("OK");
        panel.add(okButton);
        refreshButton = new Button("Refresh");
        panel.add(refreshButton);
        cancelButton = new Button("Cancel");
        panel.add(cancelButton);
        this.add(panel);
        this.pack();
        double magnificationLevel = 4.0;
        canvas.setMagnification(magnificationLevel);
        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        // Calculate window size for zoom
        int imgWidth = imagePlus.getWidth() * (int) magnificationLevel;
        int imgHeight = imagePlus.getHeight() * (int) magnificationLevel;
        // position the window at  bottom left
        int xlocation = 10;
        int ylocation = screenHeight-imgHeight-(screenHeight *4/100); // screenHeight*4/100 to create a little bit space
        ImageWindow window = imagePlus.getWindow();
        window.setLocationAndSize( xlocation,ylocation ,imgWidth,imgHeight);
        // press ok will update the initial centroid coordinates in the ImageData class
        this.okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(imagePlus.getRoi() instanceof PointRoi){
                    PointRoi pointRoi = (PointRoi) imagePlus.getRoi();
                    Point[] pointArray = pointRoi.getContainedPoints();
                    for(Point p : pointArray) {
                        imageData.getUserSelectedCentroidsList().add(p);
                    }
                    IJ.log("Selected Point: " + imageData.getUserSelectedCentroidsList().toString());
                    imagePlus.getWindow().close();
                } else {
                    IJ.log("No point has been selected");
                    IJ.log(imageData.getUserSelectedCentroidsList().toString());
                }
            }
        });
        // press refresh to remove the selected centroid coordinates in the ImageData class
        this.refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePlus.deleteRoi();
                imageData.getUserSelectedCentroidsList().clear();
                if(!(imagePlus.getRoi() instanceof PointRoi)){
                    IJ.log("Selected Points have been deleted");
                }
            }
        });
        // press cancel to close the current window;
        this.cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imagePlus.getWindow().close();
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PointRoi pointRoi = (PointRoi) imagePlus.getRoi();
                IJ.log(Arrays.toString(pointRoi.getContainedPoints()));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                IJ.setTool(Toolbar.POINT);
            }
        });
    }




}
