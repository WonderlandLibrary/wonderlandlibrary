/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.GaussianFilter;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class ShadowFilter
extends AbstractBufferedImageOp {
    private float radius = 5.0f;
    private float angle = 4.712389f;
    private float distance = 5.0f;
    private float opacity = 0.5f;
    private boolean addMargins = false;
    private boolean shadowOnly = false;
    private int shadowColor = -16777216;

    public ShadowFilter() {
    }

    public ShadowFilter(float radius, float xOffset, float yOffset, float opacity) {
        this.radius = radius;
        this.angle = (float)Math.atan2(yOffset, xOffset);
        this.distance = (float)Math.sqrt(xOffset * xOffset + yOffset * yOffset);
        this.opacity = opacity;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getShadowColor() {
        return this.shadowColor;
    }

    public void setAddMargins(boolean addMargins) {
        this.addMargins = addMargins;
    }

    public boolean getAddMargins() {
        return this.addMargins;
    }

    public void setShadowOnly(boolean shadowOnly) {
        this.shadowOnly = shadowOnly;
    }

    public boolean getShadowOnly() {
        return this.shadowOnly;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        Rectangle r = new Rectangle(0, 0, src.getWidth(), src.getHeight());
        if (this.addMargins) {
            float xOffset = this.distance * (float)Math.cos(this.angle);
            float yOffset = -this.distance * (float)Math.sin(this.angle);
            r.width += (int)(Math.abs(xOffset) + 2.0f * this.radius);
            r.height += (int)(Math.abs(yOffset) + 2.0f * this.radius);
        }
        return r;
    }

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Double();
        }
        if (this.addMargins) {
            float xOffset = this.distance * (float)Math.cos(this.angle);
            float yOffset = -this.distance * (float)Math.sin(this.angle);
            float topShadow = Math.max(0.0f, this.radius - yOffset);
            float leftShadow = Math.max(0.0f, this.radius - xOffset);
            dstPt.setLocation(srcPt.getX() + (double)leftShadow, srcPt.getY() + (double)topShadow);
        } else {
            dstPt.setLocation(srcPt.getX(), srcPt.getY());
        }
        return dstPt;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        float xOffset = this.distance * (float)Math.cos(this.angle);
        float yOffset = -this.distance * (float)Math.sin(this.angle);
        if (dst == null) {
            if (this.addMargins) {
                ColorModel cm = src.getColorModel();
                dst = new BufferedImage(cm, cm.createCompatibleWritableRaster(src.getWidth() + (int)(Math.abs(xOffset) + this.radius), src.getHeight() + (int)(Math.abs(yOffset) + this.radius)), cm.isAlphaPremultiplied(), null);
            } else {
                dst = this.createCompatibleDestImage(src, null);
            }
        }
        float shadowR = (float)(this.shadowColor >> 16 & 0xFF) / 255.0f;
        float shadowG = (float)(this.shadowColor >> 8 & 0xFF) / 255.0f;
        float shadowB = (float)(this.shadowColor & 0xFF) / 255.0f;
        float[][] extractAlpha = new float[][]{{0.0f, 0.0f, 0.0f, shadowR}, {0.0f, 0.0f, 0.0f, shadowG}, {0.0f, 0.0f, 0.0f, shadowB}, {0.0f, 0.0f, 0.0f, this.opacity}};
        BufferedImage shadow = new BufferedImage(width, height, 2);
        new BandCombineOp(extractAlpha, null).filter(src.getRaster(), shadow.getRaster());
        shadow = new GaussianFilter(this.radius).filter(shadow, null);
        Graphics2D g = dst.createGraphics();
        g.setComposite(AlphaComposite.getInstance(3, this.opacity));
        if (this.addMargins) {
            float radius2 = this.radius / 2.0f;
            float topShadow = Math.max(0.0f, this.radius - yOffset);
            float leftShadow = Math.max(0.0f, this.radius - xOffset);
            g.translate(leftShadow, topShadow);
        }
        g.drawRenderedImage(shadow, AffineTransform.getTranslateInstance(xOffset, yOffset));
        if (!this.shadowOnly) {
            g.setComposite(AlphaComposite.SrcOver);
            g.drawRenderedImage(src, null);
        }
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Stylize/Drop Shadow...";
    }
}

