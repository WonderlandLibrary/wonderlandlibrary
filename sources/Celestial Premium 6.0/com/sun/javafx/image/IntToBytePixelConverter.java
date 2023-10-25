/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface IntToBytePixelConverter
extends PixelConverter<IntBuffer, ByteBuffer> {
    @Override
    public void convert(int[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

    @Override
    public void convert(IntBuffer var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8);

    @Override
    public void convert(int[] var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8);
}

