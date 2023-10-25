/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.AlphaType;
import java.nio.Buffer;

public interface PixelGetter<T extends Buffer> {
    public AlphaType getAlphaType();

    public int getNumElements();

    public int getArgb(T var1, int var2);

    public int getArgbPre(T var1, int var2);
}

