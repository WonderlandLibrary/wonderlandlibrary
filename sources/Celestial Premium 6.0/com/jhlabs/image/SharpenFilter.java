/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ConvolveFilter;

public class SharpenFilter
extends ConvolveFilter {
    private static float[] sharpenMatrix = new float[]{0.0f, -0.2f, 0.0f, -0.2f, 1.8f, -0.2f, 0.0f, -0.2f, 0.0f};

    public SharpenFilter() {
        super(sharpenMatrix);
    }

    @Override
    public String toString() {
        return "Blur/Sharpen";
    }
}

