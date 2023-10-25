/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ConvolveFilter;

public class BumpFilter
extends ConvolveFilter {
    private static float[] embossMatrix = new float[]{-1.0f, -1.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};

    public BumpFilter() {
        super(embossMatrix);
    }

    @Override
    public String toString() {
        return "Blur/Emboss Edges";
    }
}

