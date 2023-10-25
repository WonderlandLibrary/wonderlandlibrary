/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

public class Spectrum {
    private static int adjust(float color, float factor, float gamma) {
        if ((double)color == 0.0) {
            return 0;
        }
        return (int)Math.round(255.0 * Math.pow(color * factor, gamma));
    }

    public static int wavelengthToRGB(float wavelength) {
        float b;
        float g;
        float r;
        float gamma = 0.8f;
        int w = (int)wavelength;
        if (w < 380) {
            r = 0.0f;
            g = 0.0f;
            b = 0.0f;
        } else if (w < 440) {
            r = -(wavelength - 440.0f) / 60.0f;
            g = 0.0f;
            b = 1.0f;
        } else if (w < 490) {
            r = 0.0f;
            g = (wavelength - 440.0f) / 50.0f;
            b = 1.0f;
        } else if (w < 510) {
            r = 0.0f;
            g = 1.0f;
            b = -(wavelength - 510.0f) / 20.0f;
        } else if (w < 580) {
            r = (wavelength - 510.0f) / 70.0f;
            g = 1.0f;
            b = 0.0f;
        } else if (w < 645) {
            r = 1.0f;
            g = -(wavelength - 645.0f) / 65.0f;
            b = 0.0f;
        } else if (w <= 780) {
            r = 1.0f;
            g = 0.0f;
            b = 0.0f;
        } else {
            r = 0.0f;
            g = 0.0f;
            b = 0.0f;
        }
        float factor = 380 <= w && w <= 419 ? 0.3f + 0.7f * (wavelength - 380.0f) / 40.0f : (420 <= w && w <= 700 ? 1.0f : (701 <= w && w <= 780 ? 0.3f + 0.7f * (780.0f - wavelength) / 80.0f : 0.0f));
        int ir = Spectrum.adjust(r, factor, gamma);
        int ig = Spectrum.adjust(g, factor, gamma);
        int ib = Spectrum.adjust(b, factor, gamma);
        return 0xFF000000 | ir << 16 | ig << 8 | ib;
    }
}

