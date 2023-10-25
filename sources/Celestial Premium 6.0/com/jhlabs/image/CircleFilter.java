/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.TransformFilter;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class CircleFilter
extends TransformFilter {
    private float radius = 10.0f;
    private float height = 20.0f;
    private float angle = 0.0f;
    private float spreadAngle = (float)Math.PI;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float icentreX;
    private float icentreY;
    private float iWidth;
    private float iHeight;

    public CircleFilter() {
        this.setEdgeAction(0);
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return this.height;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setSpreadAngle(float spreadAngle) {
        this.spreadAngle = spreadAngle;
    }

    public float getSpreadAngle() {
        return this.spreadAngle;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
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

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.iWidth = src.getWidth();
        this.iHeight = src.getHeight();
        this.icentreX = this.iWidth * this.centreX;
        this.icentreY = this.iHeight * this.centreY;
        this.iWidth -= 1.0f;
        return super.filter(src, dst);
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        float dx = (float)x - this.icentreX;
        float dy = (float)y - this.icentreY;
        float theta = (float)Math.atan2(-dy, -dx) + this.angle;
        float r = (float)Math.sqrt(dx * dx + dy * dy);
        theta = ImageMath.mod(theta, (float)Math.PI * 2);
        out[0] = this.iWidth * theta / (this.spreadAngle + 1.0E-5f);
        out[1] = this.iHeight * (1.0f - (r - this.radius) / (this.height + 1.0E-5f));
    }

    public String toString() {
        return "Distort/Circle...";
    }
}

