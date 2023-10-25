/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;
import com.jhlabs.math.Noise;
import java.awt.geom.Point2D;

public class FlareFilter
extends PointFilter {
    private int rays = 50;
    private float radius;
    private float baseAmount = 1.0f;
    private float ringAmount = 0.2f;
    private float rayAmount = 0.1f;
    private int color = -1;
    private int width;
    private int height;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float ringWidth = 1.6f;
    private float linear = 0.03f;
    private float gauss = 0.006f;
    private float mix = 0.5f;
    private float falloff = 6.0f;
    private float sigma;
    private float icentreX;
    private float icentreY;

    public FlareFilter() {
        this.setRadius(50.0f);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setRingWidth(float ringWidth) {
        this.ringWidth = ringWidth;
    }

    public float getRingWidth() {
        return this.ringWidth;
    }

    public void setBaseAmount(float baseAmount) {
        this.baseAmount = baseAmount;
    }

    public float getBaseAmount() {
        return this.baseAmount;
    }

    public void setRingAmount(float ringAmount) {
        this.ringAmount = ringAmount;
    }

    public float getRingAmount() {
        return this.ringAmount;
    }

    public void setRayAmount(float rayAmount) {
        this.rayAmount = rayAmount;
    }

    public float getRayAmount() {
        return this.rayAmount;
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
        this.sigma = radius / 3.0f;
    }

    public float getRadius() {
        return this.radius;
    }

    @Override
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        this.icentreX = this.centreX * (float)width;
        this.icentreY = this.centreY * (float)height;
        super.setDimensions(width, height);
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        float ring;
        float dx = (float)x - this.icentreX;
        float dy = (float)y - this.icentreY;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        float a = (float)Math.exp(-distance * distance * this.gauss) * this.mix + (float)Math.exp(-distance * this.linear) * (1.0f - this.mix);
        a *= this.baseAmount;
        if (distance > this.radius + this.ringWidth) {
            a = ImageMath.lerp((distance - (this.radius + this.ringWidth)) / this.falloff, a, 0.0f);
        }
        if (distance < this.radius - this.ringWidth || distance > this.radius + this.ringWidth) {
            ring = 0.0f;
        } else {
            ring = Math.abs(distance - this.radius) / this.ringWidth;
            ring = 1.0f - ring * ring * (3.0f - 2.0f * ring);
            ring *= this.ringAmount;
        }
        a += ring;
        float angle = (float)Math.atan2(dx, dy) + (float)Math.PI;
        angle = (ImageMath.mod(angle / (float)Math.PI * 17.0f + 1.0f + Noise.noise1(angle * 10.0f), 1.0f) - 0.5f) * 2.0f;
        angle = Math.abs(angle);
        angle = (float)Math.pow(angle, 5.0);
        float b = this.rayAmount * angle / (1.0f + distance * 0.1f);
        a += b;
        a = ImageMath.clamp(a, 0.0f, 1.0f);
        return ImageMath.mixColors(a, rgb, this.color);
    }

    public String toString() {
        return "Stylize/Flare...";
    }
}

