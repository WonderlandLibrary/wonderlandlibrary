/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class TileImageFilter
extends AbstractBufferedImageOp {
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;

    public TileImageFilter() {
        this(32, 32);
    }

    public TileImageFilter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int tileWidth = src.getWidth();
        int tileHeight = src.getHeight();
        if (dst == null) {
            ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(this.width, this.height), dstCM.isAlphaPremultiplied(), null);
        }
        Graphics2D g = dst.createGraphics();
        for (int y = 0; y < this.height; y += tileHeight) {
            for (int x = 0; x < this.width; x += tileWidth) {
                g.drawImage(src, null, x, y);
            }
        }
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Tile";
    }
}

