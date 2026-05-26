package schneiderlab.tools.radialprojection.imageprocessor.core.vesselserialized;

import java.io.Serializable;

public class SliceCroppedRange implements Serializable {
    private int start;
    private int end;

    public SliceCroppedRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getEnd() {
        return end;
    }
}
