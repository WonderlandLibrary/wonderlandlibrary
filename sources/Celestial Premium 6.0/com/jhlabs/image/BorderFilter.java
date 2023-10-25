/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BorderFilter
extends AbstractBufferedImageOp {
    private int leftBorder;
    private int rightBorder;
    private int topBorder;
    private int bottomBorder;
    private Paint borderPaint;

    public BorderFilter() {
    }

    public BorderFilter(int leftBorder, int topBorder, int rightBorder, int bottomBorder, Paint borderPaint) {
        this.leftBorder = leftBorder;
        this.topBorder = topBorder;
        this.rightBorder = rightBorder;
        this.bottomBorder = bottomBorder;
        this.borderPaint = borderPaint;
    }

    public void setLeftBorder(int leftBorder) {
        this.leftBorder = leftBorder;
    }

    public int getLeftBorder() {
        return this.leftBorder;
    }

    public void setRightBorder(int rightBorder) {
        this.rightBorder = rightBorder;
    }

    public int getRightBorder() {
        return this.rightBorder;
    }

    public void setTopBorder(int topBorder) {
        this.topBorder = topBorder;
    }

    public int getTopBorder() {
        return this.topBorder;
    }

    public void setBottomBorder(int bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    public int getBottomBorder() {
        return this.bottomBorder;
    }

    public void setBorderPaint(Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    public Paint getBorderPaint() {
        return this.borderPaint;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = new BufferedImage(width + this.leftBorder + this.rightBorder, height + this.topBorder + this.bottomBorder, src.getType());
        }
        Graphics2D g = dst.createGraphics();
        if (this.borderPaint != null) {
            g.setPaint(this.borderPaint);
            if (this.leftBorder > 0) {
                g.fillRect(0, 0, this.leftBorder, height);
            }
            if (this.rightBorder > 0) {
                g.fillRect(width - this.rightBorder, 0, this.rightBorder, height);
            }
            if (this.topBorder > 0) {
                g.fillRect(this.leftBorder, 0, width - this.leftBorder - this.rightBorder, this.topBorder);
            }
            if (this.bottomBorder > 0) {
                g.fillRect(this.leftBorder, height - this.bottomBorder, width - this.leftBorder - this.rightBorder, this.bottomBorder);
            }
        }
        g.drawRenderedImage(src, AffineTransform.getTranslateInstance(this.leftBorder, this.rightBorder));
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Distort/Border...";
    }
}

