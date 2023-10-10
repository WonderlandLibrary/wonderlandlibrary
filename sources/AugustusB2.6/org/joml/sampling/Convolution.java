// 
// Decompiled by Procyon v0.5.36
// 

package org.joml.sampling;

import org.joml.Math;
import java.nio.FloatBuffer;

public class Convolution
{
    public static void gaussianKernel(final int rows, final int cols, final float sigma, final FloatBuffer dest) {
        if ((rows & 0x1) == 0x0) {
            throw new IllegalArgumentException("rows must be an odd number");
        }
        if ((cols & 0x1) == 0x0) {
            throw new IllegalArgumentException("cols must be an odd number");
        }
        if (dest == null) {
            throw new IllegalArgumentException("dest must not be null");
        }
        if (dest.remaining() < rows * cols) {
            throw new IllegalArgumentException("dest must have at least " + rows * cols + " remaining values");
        }
        float sum = 0.0f;
        final int pos = dest.position();
        int i = 0;
        for (int y = -(rows - 1) / 2; y <= (rows - 1) / 2; ++y) {
            for (int x = -(cols - 1) / 2; x <= (cols - 1) / 2; ++x, ++i) {
                final float k = (float)Math.exp(-(y * y + x * x) / (2.0 * sigma * sigma));
                dest.put(pos + i, k);
                sum += k;
            }
        }
        for (i = 0; i < rows * cols; ++i) {
            dest.put(pos + i, dest.get(pos + i) / sum);
        }
    }
    
    public static void gaussianKernel(final int rows, final int cols, final float sigma, final float[] dest) {
        if ((rows & 0x1) == 0x0) {
            throw new IllegalArgumentException("rows must be an odd number");
        }
        if ((cols & 0x1) == 0x0) {
            throw new IllegalArgumentException("cols must be an odd number");
        }
        if (dest == null) {
            throw new IllegalArgumentException("dest must not be null");
        }
        if (dest.length < rows * cols) {
            throw new IllegalArgumentException("dest must have a size of at least " + rows * cols);
        }
        float sum = 0.0f;
        int i = 0;
        for (int y = -(rows - 1) / 2; y <= (rows - 1) / 2; ++y) {
            for (int x = -(cols - 1) / 2; x <= (cols - 1) / 2; ++x, ++i) {
                final float k = (float)Math.exp(-(y * y + x * x) / (2.0 * sigma * sigma));
                dest[i] = k;
                sum += k;
            }
        }
        for (i = 0; i < rows * cols; ++i) {
            dest[i] /= sum;
        }
    }
}
