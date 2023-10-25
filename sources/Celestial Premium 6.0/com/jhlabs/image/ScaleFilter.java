/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class ScaleFilter
extends AbstractBufferedImageOp {
    private int width;
    private int height;

    public ScaleFilter() {
        this(32, 32);
    }

    public ScaleFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(this.width, this.height), dstCM.isAlphaPremultiplied(), null);
        }
        Image scaleImage = src.getScaledInstance(this.width, this.height, 16);
        Graphics2D g = dst.createGraphics();
        g.drawImage(scaleImage, 0, 0, this.width, this.height, null);
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Distort/Scale";
    }
}

