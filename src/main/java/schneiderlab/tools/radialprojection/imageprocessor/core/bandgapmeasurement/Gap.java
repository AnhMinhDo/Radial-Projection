package schneiderlab.tools.radialprojection.imageprocessor.core.bandgapmeasurement;

import java.awt.*;

public class Gap {
    private final int length;
    private final Point leftEdge;
    private final Point rightEdge;

    public Gap(int length, Point leftEdge, Point rightEdge) {
        this.length = length;
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
    }

    public int getLength() {
        return length;
    }

    public Point getLeftEdge() {
        return leftEdge;
    }

    public Point getRightEdge() {
        return rightEdge;
    }
}
