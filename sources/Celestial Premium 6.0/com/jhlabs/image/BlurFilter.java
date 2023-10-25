/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ConvolveFilter;

public class BlurFilter
extends ConvolveFilter {
    protected static float[] blurMatrix = new float[]{0.071428575f, 0.14285715f, 0.071428575f, 0.14285715f, 0.14285715f, 0.14285715f, 0.071428575f, 0.14285715f, 0.071428575f};

    public BlurFilter() {
        super(blurMatrix);
    }

    @Override
    public String toString() {
        return "Blur/Simple Blur";
    }
}

