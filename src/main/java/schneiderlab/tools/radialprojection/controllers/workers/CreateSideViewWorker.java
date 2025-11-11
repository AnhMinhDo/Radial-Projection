package schneiderlab.tools.radialprojection.controllers.workers;

import ij.IJ;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.DefaultLinearAxis;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import org.scijava.Context;
import org.scijava.log.LogService;
import schneiderlab.tools.radialprojection.imageprocessor.core.createsideview.CreateSideView;
import schneiderlab.tools.radialprojection.views.userinterfacecomponents.Radical_Projection_Tool;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Path;

public class CreateSideViewWorker <T extends RealType<T>> extends SwingWorker<Void, Void> {
    private final int targetXYpixelSize;
    private final int targetZpixelSize;
    private final Path filePath;
    private final Context context;
    private ImgPlus<UnsignedShortType> sideViewImgPlus;
    private Radical_Projection_Tool mainview;

    public CreateSideViewWorker(int targetXYpixelSize,
                                int targetZpixelSize,
                                Path filePath,
                                Context context,
                                Radical_Projection_Tool mainview) {
        this.targetXYpixelSize = targetXYpixelSize;
        this.targetZpixelSize = targetZpixelSize;
        this.filePath = filePath;
        this.context = context;
        this.mainview = mainview;
    }

    public ImgPlus<UnsignedShortType> getSideViewImgPlus() {
        return sideViewImgPlus;
    }

    @Override
    protected Void doInBackground() {
//        StatusService statusService = context.getService(StatusService.class);
        IJ.showStatus("loading file: " + filePath.getFileName().toString());
        DatasetIOService ioService = context.getService(DatasetIOService.class);
        LogService logService = context.getService(LogService.class);
        // Get DatasetService and UIService from context
        try {
            // load the image
            logService.info("importing image to create side view ....");
            Dataset img = ioService.open(filePath.toString());
            logService.info("image is imported successfully");
            ImgPlus<T> genericImgPlus = (ImgPlus<T>) img.getImgPlus();
            logService.info("Creating side view...");
            CreateSideView createSideView = new CreateSideView(context,
                    genericImgPlus,
                    targetXYpixelSize,
                    targetZpixelSize);
            PropertyChangeListener listener = new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("progress".equals(evt.getPropertyName())) {
                        setProgress((int) evt.getNewValue());
                    }
                }
            };
            createSideView.addPropertyChangeListener(listener);
            Img<UnsignedShortType> sideViewImg = createSideView.process();
            sideViewImgPlus = new ImgPlus<>(sideViewImg);
            // Add meta data
            sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.X, "micron", targetXYpixelSize * 0.001), 0);
            sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.Y, "micron", targetXYpixelSize * 0.001), 1);
            sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.Z, "micron", targetZpixelSize * 0.001), 3);
            sideViewImgPlus.setAxis(new DefaultLinearAxis(Axes.CHANNEL, "", 1.0), 2);

        } catch (IOException e){
            System.err.println("fail to import image file");
            logService.error("IO error; fail to import image file");
        }
        return null;
    }
}
