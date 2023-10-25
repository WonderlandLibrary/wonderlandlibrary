/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelSetter;
import java.nio.IntBuffer;

public interface IntPixelSetter
extends PixelSetter<IntBuffer> {
    @Override
    public void setArgb(int[] var1, int var2, int var3);

    @Override
    public void setArgbPre(int[] var1, int var2, int var3);
}

