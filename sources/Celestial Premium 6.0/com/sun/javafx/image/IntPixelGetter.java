/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelGetter;
import java.nio.IntBuffer;

public interface IntPixelGetter
extends PixelGetter<IntBuffer> {
    @Override
    public int getArgb(int[] var1, int var2);

    @Override
    public int getArgbPre(int[] var1, int var2);
}

