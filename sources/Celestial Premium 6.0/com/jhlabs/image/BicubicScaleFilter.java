/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class BicubicScaleFilter
extends AbstractBufferedImageOp {
    private int width;
    private int height;

    public BicubicScaleFilter() {
        this(32, 32);
    }

    public BicubicScaleFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int w = src.getWidth();
        int h = src.getHeight();
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(this.width, this.height), dstCM.isAlphaPremultiplied(), null);
        }
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(src, 0, 0, this.width, this.height, null);
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Distort/Bicubic Scale";
    }
}

