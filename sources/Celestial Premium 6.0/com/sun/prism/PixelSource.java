/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.glass.ui.Pixels;

public interface PixelSource {
    public Pixels getLatestPixels();

    public void doneWithPixels(Pixels var1);

    public void skipLatestPixels();
}

