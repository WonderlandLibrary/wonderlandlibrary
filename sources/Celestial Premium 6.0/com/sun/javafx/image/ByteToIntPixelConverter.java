/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelConverter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface ByteToIntPixelConverter
extends PixelConverter<ByteBuffer, IntBuffer> {
    @Override
    public void convert(byte[] var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    @Override
    public void convert(ByteBuffer var1, int var2, int var3, int[] var4, int var5, int var6, int var7, int var8);

    @Override
    public void convert(byte[] var1, int var2, int var3, IntBuffer var4, int var5, int var6, int var7, int var8);
}

