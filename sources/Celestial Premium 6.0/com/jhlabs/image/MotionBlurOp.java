/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MotionBlurOp
extends AbstractBufferedImageOp {
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float distance;
    private float angle;
    private float rotation;
    private float zoom;

    public MotionBlurOp() {
    }

    public MotionBlurOp(float distance, float angle, float rotation, float zoom) {
        this.distance = distance;
        this.angle = angle;
        this.rotation = rotation;
        this.zoom = zoom;
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

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return this.zoom;
    }

    public void setCentreX(float centreX) {
        this.centreX = centreX;
    }

    public float getCentreX() {
        return this.centreX;
    }

    public void setCentreY(float centreY) {
        this.centreY = centreY;
    }

    public float getCentreY() {
        return this.centreY;
    }

    public void setCentre(Point2D centre) {
        this.centreX = (float)centre.getX();
        this.centreY = (float)centre.getY();
    }

    public Point2D getCentre() {
        return new Point2D.Float(this.centreX, this.centreY);
    }

    private int log2(int n) {
        int m = 1;
        int log2n = 0;
        while (m < n) {
            m *= 2;
            ++log2n;
        }
        return log2n;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        BufferedImage tsrc = src;
        float cx = (float)src.getWidth() * this.centreX;
        float cy = (float)src.getHeight() * this.centreY;
        float imageRadius = (float)Math.sqrt(cx * cx + cy * cy);
        float translateX = (float)((double)this.distance * Math.cos(this.angle));
        float translateY = (float)((double)this.distance * -Math.sin(this.angle));
        float scale = this.zoom;
        float rotate = this.rotation;
        float maxDistance = this.distance + Math.abs(this.rotation * imageRadius) + this.zoom * imageRadius;
        int steps = this.log2((int)maxDistance);
        translateX /= maxDistance;
        translateY /= maxDistance;
        scale /= maxDistance;
        rotate /= maxDistance;
        if (steps == 0) {
            Graphics2D g = dst.createGraphics();
            g.drawRenderedImage(src, null);
            g.dispose();
            return dst;
        }
        BufferedImage tmp = this.createCompatibleDestImage(src, null);
        for (int i = 0; i < steps; ++i) {
            Graphics2D g = tmp.createGraphics();
            g.drawImage(tsrc, null, null);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setComposite(AlphaComposite.getInstance(3, 0.5f));
            g.translate(cx + translateX, cy + translateY);
            g.scale(1.0001 + (double)scale, 1.0001 + (double)scale);
            if (this.rotation != 0.0f) {
                g.rotate(rotate);
            }
            g.translate(-cx, -cy);
            g.drawImage(dst, null, null);
            g.dispose();
            BufferedImage ti = dst;
            dst = tmp;
            tmp = ti;
            tsrc = dst;
            translateX *= 2.0f;
            translateY *= 2.0f;
            scale *= 2.0f;
            rotate *= 2.0f;
        }
        return dst;
    }

    public String toString() {
        return "Blur/Faster Motion Blur...";
    }
}

