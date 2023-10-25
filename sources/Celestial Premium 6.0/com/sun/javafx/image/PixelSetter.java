/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.AlphaType;
import java.nio.Buffer;

public interface PixelSetter<T extends Buffer> {
    public AlphaType getAlphaType();

    public int getNumElements();

    public void setArgb(T var1, int var2, int var3);

    public void setArgbPre(T var1, int var2, int var3);
}

