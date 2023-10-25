/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public abstract class WholeImageFilter
extends AbstractBufferedImageOp {
    protected Rectangle transformedSpace;
    protected Rectangle originalSpace;

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        WritableRaster srcRaster = src.getRaster();
        this.originalSpace = new Rectangle(0, 0, width, height);
        this.transformedSpace = new Rectangle(0, 0, width, height);
        this.transformSpace(this.transformedSpace);
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(this.transformedSpace.width, this.transformedSpace.height), dstCM.isAlphaPremultiplied(), null);
        }
        WritableRaster dstRaster = dst.getRaster();
        int[] inPixels = this.getRGB(src, 0, 0, width, height, null);
        inPixels = this.filterPixels(width, height, inPixels, this.transformedSpace);
        this.setRGB(dst, 0, 0, this.transformedSpace.width, this.transformedSpace.height, inPixels);
        return dst;
    }

    protected void transformSpace(Rectangle rect) {
    }

    protected abstract int[] filterPixels(int var1, int var2, int[] var3, Rectangle var4);
}

