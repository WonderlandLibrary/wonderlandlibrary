/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.PixelFormat;
import java.nio.ByteBuffer;

public interface MediaFrame {
    public ByteBuffer getBufferForPlane(int var1);

    public PixelFormat getPixelFormat();

    public int getWidth();

    public int getHeight();

    public int getEncodedWidth();

    public int getEncodedHeight();

    public int planeCount();

    public int[] planeStrides();

    public int strideForPlane(int var1);

    public MediaFrame convertToFormat(PixelFormat var1);

    public void holdFrame();

    public void releaseFrame();
}

