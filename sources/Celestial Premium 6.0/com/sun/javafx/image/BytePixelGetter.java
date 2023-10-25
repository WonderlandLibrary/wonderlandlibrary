/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelGetter;
import java.nio.ByteBuffer;

public interface BytePixelGetter
extends PixelGetter<ByteBuffer> {
    @Override
    public int getArgb(byte[] var1, int var2);

    @Override
    public int getArgbPre(byte[] var1, int var2);
}

