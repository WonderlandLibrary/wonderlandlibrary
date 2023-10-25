/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class PinchFilter
extends TransformFilter {
    private float angle = 0.0f;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float radius = 100.0f;
    private float amount = 0.5f;
    private float radius2 = 0.0f;
    private float icentreX;
    private float icentreY;
    private float width;
    private float height;

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

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.width = src.getWidth();
        this.height = src.getHeight();
        this.icentreX = this.width * this.centreX;
        this.icentreY = this.height * this.centreY;
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
        if (distance > this.radius2 || distance == 0.0f) {
            out[0] = x;
            out[1] = y;
        } else {
            float d = (float)Math.sqrt(distance / this.radius2);
            float t = (float)Math.pow(Math.sin(1.5707963267948966 * (double)d), -this.amount);
            float e = 1.0f - d;
            float a = this.angle * e * e;
            float s = (float)Math.sin(a);
            float c = (float)Math.cos(a);
            out[0] = this.icentreX + c * (dx *= t) - s * (dy *= t);
            out[1] = this.icentreY + s * dx + c * dy;
        }
    }

    public String toString() {
        return "Distort/Pinch...";
    }
}

