/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.image.BufferedImage;

public class OffsetFilter
extends TransformFilter {
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private boolean wrap;

    public OffsetFilter() {
        this(0, 0, true);
    }

    public OffsetFilter(int xOffset, int yOffset, boolean wrap) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.wrap = wrap;
        this.setEdgeAction(0);
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getXOffset() {
        return this.xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getYOffset() {
        return this.yOffset;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    public boolean getWrap() {
        return this.wrap;
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        if (this.wrap) {
            out[0] = (x + this.width - this.xOffset) % this.width;
            out[1] = (y + this.height - this.yOffset) % this.height;
        } else {
            out[0] = x - this.xOffset;
            out[1] = y - this.yOffset;
        }
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.width = src.getWidth();
        this.height = src.getHeight();
        if (this.wrap) {
            while (this.xOffset < 0) {
                this.xOffset += this.width;
            }
            while (this.yOffset < 0) {
                this.yOffset += this.height;
            }
            this.xOffset %= this.width;
            this.yOffset %= this.height;
        }
        return super.filter(src, dst);
    }

    public String toString() {
        return "Distort/Offset...";
    }
}

