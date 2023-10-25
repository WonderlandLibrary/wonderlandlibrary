/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelConverter;
import java.nio.IntBuffer;

public interface IntToIntPixelConverter
extends PixelConverter<IntBuffer, IntBuffer> {
    @Override
    public void convert(int[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    @Override
    public void convert(IntBuffer var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    @Override
    public void convert(int[] var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);
}

