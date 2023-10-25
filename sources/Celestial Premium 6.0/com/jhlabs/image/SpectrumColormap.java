/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.Spectrum;

public class SpectrumColormap
implements Colormap {
    @Override
    public int getColor(float v) {
        return Spectrum.wavelengthToRGB(380.0f + 400.0f * ImageMath.clamp(v, 0.0f, 1.0f));
    }
}

