/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;
import java.awt.image.BufferedImage;

public class HalftoneFilter
extends AbstractBufferedImageOp {
    private float softness = 0.1f;
    private boolean invert;
    private boolean monochrome;
    private BufferedImage mask;

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public float getSoftness() {
        return this.softness;
    }

    public void setMask(BufferedImage mask) {
        this.mask = mask;
    }

    public BufferedImage getMask() {
        return this.mask;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public boolean getInvert() {
        return this.invert;
    }

    public void setMonochrome(boolean monochrome) {
        this.monochrome = monochrome;
    }

    public boolean getMonochrome() {
        return this.monochrome;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        if (this.mask == null) {
            return dst;
        }
        int maskWidth = this.mask.getWidth();
        int maskHeight = this.mask.getHeight();
        float s = 255.0f * this.softness;
        int[] inPixels = new int[width];
        int[] maskPixels = new int[maskWidth];
        for (int y = 0; y < height; ++y) {
            this.getRGB(src, 0, y, width, 1, inPixels);
            this.getRGB(this.mask, 0, y % maskHeight, maskWidth, 1, maskPixels);
            for (int x = 0; x < width; ++x) {
                int maskRGB = maskPixels[x % maskWidth];
                int inRGB = inPixels[x];
                if (this.invert) {
                    maskRGB ^= 0xFFFFFF;
                }
                if (this.monochrome) {
                    int v = PixelUtils.brightness(maskRGB);
                    int iv = PixelUtils.brightness(inRGB);
                    float f = 1.0f - ImageMath.smoothStep((float)iv - s, (float)iv + s, v);
                    int a = (int)(255.0f * f);
                    inPixels[x] = inRGB & 0xFF000000 | a << 16 | a << 8 | a;
                    continue;
                }
                int ir = inRGB >> 16 & 0xFF;
                int ig = inRGB >> 8 & 0xFF;
                int ib = inRGB & 0xFF;
                int mr = maskRGB >> 16 & 0xFF;
                int mg = maskRGB >> 8 & 0xFF;
                int mb = maskRGB & 0xFF;
                int r = (int)(255.0f * (1.0f - ImageMath.smoothStep((float)ir - s, (float)ir + s, mr)));
                int g = (int)(255.0f * (1.0f - ImageMath.smoothStep((float)ig - s, (float)ig + s, mg)));
                int b = (int)(255.0f * (1.0f - ImageMath.smoothStep((float)ib - s, (float)ib + s, mb)));
                inPixels[x] = inRGB & 0xFF000000 | r << 16 | g << 8 | b;
            }
            this.setRGB(dst, 0, y, width, 1, inPixels);
        }
        return dst;
    }

    public String toString() {
        return "Stylize/Halftone...";
    }
}

