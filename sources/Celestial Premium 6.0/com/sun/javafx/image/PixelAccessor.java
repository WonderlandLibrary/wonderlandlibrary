/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.image;

import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import java.nio.Buffer;

public interface PixelAccessor<T extends Buffer>
extends PixelGetter<T>,
PixelSetter<T> {
}

