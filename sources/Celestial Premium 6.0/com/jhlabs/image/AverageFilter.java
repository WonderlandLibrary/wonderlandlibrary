/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ConvolveFilter;

public class AverageFilter
extends ConvolveFilter {
    protected static float[] theMatrix = new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f};

    public AverageFilter() {
        super(theMatrix);
    }

    @Override
    public String toString() {
        return "Blur/Average Blur";
    }
}

