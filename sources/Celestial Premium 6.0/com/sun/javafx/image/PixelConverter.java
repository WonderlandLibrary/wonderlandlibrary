/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import java.nio.Buffer;

public interface PixelConverter<T extends Buffer, U extends Buffer> {
    public void convert(T var1, int var2, int var3, U var4, int var5, int var6, int var7, int var8);

    public PixelGetter<T> getGetter();

    public PixelSetter<U> getSetter();
}

