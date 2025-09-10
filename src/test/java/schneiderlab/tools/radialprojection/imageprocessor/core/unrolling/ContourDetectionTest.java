package schneiderlab.tools.radialprojection.imageprocessor.core.unrolling;

import ij.process.ByteProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static schneiderlab.tools.radialprojection.imageprocessor.core.unrolling.ContourDetection.contourTracing;

class ContourDetectionTest {
    @Test
    public void testContourTracing() {
        int width = 4;
        int height = 3;
        // Matrix (row-major order, bottom row last):
        // 0 0 1 0
        // 0 2 0 0
        // 3 0 0 4
        short[] input = {
                0, 0, 1, 0,
                0, 2, 0, 0,
                3, 0, 0, 4
        };

        ByteProcessor result = contourTracing(input, width, height);
        byte[] out = (byte[]) result.getPixels();

        // Expected: first non-zero from bottom in each column
        // Column 0 → 3 (row 2, col 0)
        // Column 1 → 2 (row 1, col 1)
        // Column 2 → 1 (row 0, col 2)
        // Column 3 → 4 (row 2, col 3)
        // So output marks only those four positions with 255
        byte[] expected = {
                0, 0, (byte)255, 0,
                0, (byte)255, 0, 0,
                (byte)255, 0, 0, (byte)255
        };

        assertArrayEquals(expected, out);
    }
}