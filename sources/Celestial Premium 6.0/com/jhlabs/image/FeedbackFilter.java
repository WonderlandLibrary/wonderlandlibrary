/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ImageMath;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FeedbackFilter
extends AbstractBufferedImageOp {
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float distance;
    private float angle;
    private float rotation;
    private float zoom;
    private float startAlpha = 1.0f;
    private float endAlpha = 1.0f;
    private int iterations;

    public FeedbackFilter() {
    }

    public FeedbackFilter(float distance, float angle, float rotation, float zoom) {
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

    public void setStartAlpha(float startAlpha) {
        this.startAlpha = startAlpha;
    }

    public float getStartAlpha() {
        return this.startAlpha;
    }

    public void setEndAlpha(float endAlpha) {
        this.endAlpha = endAlpha;
    }

    public float getEndAlpha() {
        return this.endAlpha;
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

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return this.iterations;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        float cx = (float)src.getWidth() * this.centreX;
        float cy = (float)src.getHeight() * this.centreY;
        float imageRadius = (float)Math.sqrt(cx * cx + cy * cy);
        float translateX = (float)((double)this.distance * Math.cos(this.angle));
        float translateY = (float)((double)this.distance * -Math.sin(this.angle));
        float scale = (float)Math.exp(this.zoom);
        float rotate = this.rotation;
        if (this.iterations == 0) {
            Graphics2D g = dst.createGraphics();
            g.drawRenderedImage(src, null);
            g.dispose();
            return dst;
        }
        Graphics2D g = dst.createGraphics();
        g.drawImage(src, null, null);
        for (int i = 0; i < this.iterations; ++i) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setComposite(AlphaComposite.getInstance(3, ImageMath.lerp((float)i / (float)(this.iterations - 1), this.startAlpha, this.endAlpha)));
            g.translate(cx + translateX, cy + translateY);
            g.scale(scale, scale);
            if (this.rotation != 0.0f) {
                g.rotate(rotate);
            }
            g.translate(-cx, -cy);
            g.drawImage(src, null, null);
        }
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Effects/Feedback...";
    }
}

