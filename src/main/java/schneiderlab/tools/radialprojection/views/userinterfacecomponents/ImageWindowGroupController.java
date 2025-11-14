package schneiderlab.tools.radialprojection.views.userinterfacecomponents;

import ij.ImagePlus;
import ij.gui.ImageWindow;
import schneiderlab.tools.radialprojection.imageprocessor.core.Vessel;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class ImageWindowGroupController {
    private final List<ImageWindow> imageWindowList ;
    private final List<Point> imageWindowPositionList ;

    public ImageWindowGroupController(List<ImagePlus> imagePlusList,
                                      Vessel vessel,
                                      int toolForCroppingImage) {
        imageWindowList = new ArrayList<>(imagePlusList.size());
        imageWindowPositionList = new ArrayList<>(imagePlusList.size());
        for (int i = 0; i < imagePlusList.size(); i++) {
            ImageWindowForCropping iwfc = new ImageWindowForCropping(
                    imagePlusList.get(i),
                    vessel,
                    imagePlusList,
                    toolForCroppingImage);
            imageWindowList.add(iwfc);
//            if(i != 0){
//                ImageWindow preImageWindow = imageWindowList.get(i-1);
//                int previousHeight = preImageWindow.getHeight();
//                int x = (int)preImageWindow.getLocation().getX();
//                int y = (int)preImageWindow.getLocation().getY()-previousHeight;
//                iwfc.setLocation(x,y);
//            }
//            imageWindowPositionList.add(new Point((int)iwfc.getX(),(int)iwfc.getY()));
        }
        for (int i = 0; i < imageWindowList.size(); i++) {
            // TODO: reposition of the window sequentially
            ImageWindow iwfc = imageWindowList.get(i);
            if(i==0){
                imageWindowPositionList.add(new Point((int)iwfc.getX(),(int)iwfc.getY()));
            } else {
                ImageWindow preImageWindow = imageWindowList.get(i-1);
                int previousHeight = preImageWindow.getHeight();
                int x = (int)preImageWindow.getLocation().getX();
                int y = (int)preImageWindow.getLocation().getY()-previousHeight;
                iwfc.setLocation(x,y);
                imageWindowPositionList.add(new Point(x,y));
            }
        }
        ImageWindow imageWindow = imageWindowList.get(0);
        imageWindow.addComponentListener(new ImageWindowAdapter(
                imageWindow,
                imageWindowList,
                0,
                imageWindowPositionList
        ));
    }

    public void closeAllWindowInGroup(){
        for(ImageWindow imageWindow : imageWindowList){
            imageWindow.close();
        }
    }

    class ImageWindowAdapter extends ComponentAdapter{
        private final ImageWindow imageWindowSelf;
        private final List<ImageWindow> imageWindowList ;
//        private final List<Point> imageWindowPositionList ;
        private final List<Offset>  offsetList;
        private final int indexOrder;

        public ImageWindowAdapter(ImageWindow imageWindowSelf,
                                  List<ImageWindow> imageWindowList,
                                  int indexOrder,
                                  List<Point> imageWindowPositionList) {
            this.imageWindowSelf = imageWindowSelf;
            this.imageWindowList = imageWindowList;
            this.indexOrder = indexOrder;
//            this.imageWindowPositionList = imageWindowPositionList;
            this.offsetList = new ArrayList<>(imageWindowList.size());
            for (int i = 0; i < imageWindowPositionList.size(); i++) {
                    int referenceX = imageWindowPositionList.get(indexOrder).x;
                    int referenceY = imageWindowPositionList.get(indexOrder).y;
                if(i != indexOrder){
                     int x = (int) imageWindowPositionList.get(i).getX();
                     int y = (int) imageWindowPositionList.get(i).getY();
                    Offset offset = new Offset(referenceX-x,
                            referenceY-y);
                    offsetList.add(offset);
                } else {
                    offsetList.add(new Offset(0,0));
                }
            }
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            // get the new position
           int updatedX = imageWindowSelf.getX();
           int updatedY = imageWindowSelf.getY();
            for (int i = 0; i < imageWindowList.size(); i++) {
                if(i != indexOrder){
                    ImageWindow imageWindow = imageWindowList.get(i);
                    int offsetX = offsetList.get(i).getOffsetX();
                    int offsetY = offsetList.get(i).getOffsetY();
                    imageWindow.setLocation(updatedX+offsetX,updatedY+offsetY);
                }
            }
        }

        class Offset {
            private final int offsetX;
            private final int offsetY;

            public Offset(int offsetX, int offsetY) {
                this.offsetX = offsetX;
                this.offsetY = offsetY;
            }

            public int getOffsetX() {
                return offsetX;
            }

            public int getOffsetY() {
                return offsetY;
            }
        }
    }

}
