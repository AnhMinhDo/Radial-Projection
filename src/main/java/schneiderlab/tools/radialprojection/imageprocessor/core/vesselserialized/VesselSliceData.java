package schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized;

import java.awt.*;
import java.io.Serializable;

public class VesselSliceData implements Serializable {
    private final Point centroid;
    private final Point clickPoint;
    private final int trueSliceIndex;
    private final int trueLabel;

    public VesselSliceData(Point centroid, Point clickPoint, int sliceIndex, int label) {
        this.centroid = centroid;
        this.clickPoint = clickPoint;
        this.trueSliceIndex = sliceIndex;
        this.trueLabel = label;
    }
    public Point getCentroid() {
        return centroid;
    }
    public Point getClickPoint() {
        return clickPoint;
    }
    public int getTrueSliceIndex() {
        return trueSliceIndex;
    }
    public int getTrueLabel() {
        return trueLabel;
    }
}
