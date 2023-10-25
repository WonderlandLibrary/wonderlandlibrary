/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelSetter;
import java.nio.ByteBuffer;

public interface BytePixelSetter
extends PixelSetter<ByteBuffer> {
    @Override
    public void setArgb(byte[] var1, int var2, int var3);

    @Override
    public void setArgbPre(byte[] var1, int var2, int var3);
}

