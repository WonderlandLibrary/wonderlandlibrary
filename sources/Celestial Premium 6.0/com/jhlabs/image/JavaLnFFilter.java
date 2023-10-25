/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;

public class JavaLnFFilter
extends PointFilter {
    @Override
    public int filterRGB(int x, int y, int rgb) {
        if ((x & 1) == (y & 1)) {
            return rgb;
        }
        return ImageMath.mixColors(0.25f, -6710887, rgb);
    }

    public String toString() {
        return "Stylize/Java L&F Stipple";
    }
}

