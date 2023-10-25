/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class TwirlFilter
extends TransformFilter {
    private float angle = 0.0f;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float radius = 100.0f;
    private float radius2 = 0.0f;
    private float icentreX;
    private float icentreY;

    public TwirlFilter() {
        this.setEdgeAction(1);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
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

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.icentreX = (float)src.getWidth() * this.centreX;
        this.icentreY = (float)src.getHeight() * this.centreY;
        if (this.radius == 0.0f) {
            this.radius = Math.min(this.icentreX, this.icentreY);
        }
        this.radius2 = this.radius * this.radius;
        return super.filter(src, dst);
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        float dx = (float)x - this.icentreX;
        float dy = (float)y - this.icentreY;
        float distance = dx * dx + dy * dy;
        if (distance > this.radius2) {
            out[0] = x;
            out[1] = y;
        } else {
            distance = (float)Math.sqrt(distance);
            float a = (float)Math.atan2(dy, dx) + this.angle * (this.radius - distance) / this.radius;
            out[0] = this.icentreX + distance * (float)Math.cos(a);
            out[1] = this.icentreY + distance * (float)Math.sin(a);
        }
    }

    public String toString() {
        return "Distort/Twirl...";
    }
}

