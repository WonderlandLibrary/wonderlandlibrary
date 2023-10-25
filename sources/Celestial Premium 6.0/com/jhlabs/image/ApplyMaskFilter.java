/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ApplyMaskFilter
extends AbstractBufferedImageOp {
    private BufferedImage destination;
    private BufferedImage maskImage;

    public ApplyMaskFilter() {
    }

    public ApplyMaskFilter(BufferedImage maskImage, BufferedImage destination) {
        this.maskImage = maskImage;
        this.destination = destination;
    }

    public void setDestination(BufferedImage destination) {
        this.destination = destination;
    }

    public BufferedImage getDestination() {
        return this.destination;
    }

    public void setMaskImage(BufferedImage maskImage) {
        this.maskImage = maskImage;
    }

    public BufferedImage getMaskImage() {
        return this.maskImage;
    }

    public static void composeThroughMask(Raster src, WritableRaster dst, Raster sel) {
        int x = src.getMinX();
        int y = src.getMinY();
        int w = src.getWidth();
        int h = src.getHeight();
        int[] srcRGB = null;
        int[] selRGB = null;
        int[] dstRGB = null;
        for (int i = 0; i < h; ++i) {
            srcRGB = src.getPixels(x, y, w, 1, srcRGB);
            selRGB = sel.getPixels(x, y, w, 1, selRGB);
            dstRGB = dst.getPixels(x, y, w, 1, dstRGB);
            int k = x;
            for (int j = 0; j < w; ++j) {
                int sr = srcRGB[k];
                int dir = dstRGB[k];
                int sg = srcRGB[k + 1];
                int dig = dstRGB[k + 1];
                int sb = srcRGB[k + 2];
                int dib = dstRGB[k + 2];
                int sa = srcRGB[k + 3];
                int dia = dstRGB[k + 3];
                float a = (float)selRGB[k + 3] / 255.0f;
                float ac = 1.0f - a;
                dstRGB[k] = (int)(a * (float)sr + ac * (float)dir);
                dstRGB[k + 1] = (int)(a * (float)sg + ac * (float)dig);
                dstRGB[k + 2] = (int)(a * (float)sb + ac * (float)dib);
                dstRGB[k + 3] = (int)(a * (float)sa + ac * (float)dia);
                k += 4;
            }
            dst.setPixels(x, y, w, 1, dstRGB);
            ++y;
        }
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        int type = src.getType();
        WritableRaster srcRaster = src.getRaster();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        WritableRaster dstRaster = dst.getRaster();
        if (this.destination != null && this.maskImage != null) {
            ApplyMaskFilter.composeThroughMask(src.getRaster(), dst.getRaster(), this.maskImage.getRaster());
        }
        return dst;
    }

    public String toString() {
        return "Keying/Key...";
    }
}

