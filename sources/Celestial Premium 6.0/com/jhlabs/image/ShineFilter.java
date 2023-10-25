/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.composite.AddComposite;
import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ErodeAlphaFilter;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.RescaleFilter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ShineFilter
extends AbstractBufferedImageOp {
    private float radius = 5.0f;
    private float angle = 5.4977875f;
    private float distance = 5.0f;
    private float bevel = 0.5f;
    private boolean shadowOnly = false;
    private int shineColor = -1;
    private float brightness = 0.2f;
    private float softness = 0.0f;

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

    public void setBevel(float bevel) {
        this.bevel = bevel;
    }

    public float getBevel() {
        return this.bevel;
    }

    public void setShineColor(int shineColor) {
        this.shineColor = shineColor;
    }

    public int getShineColor() {
        return this.shineColor;
    }

    public void setShadowOnly(boolean shadowOnly) {
        this.shadowOnly = shadowOnly;
    }

    public boolean getShadowOnly() {
        return this.shadowOnly;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setSoftness(float softness) {
        this.softness = softness;
    }

    public float getSoftness() {
        return this.softness;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        float xOffset = this.distance * (float)Math.cos(this.angle);
        float yOffset = -this.distance * (float)Math.sin(this.angle);
        BufferedImage matte = new BufferedImage(width, height, 2);
        ErodeAlphaFilter s = new ErodeAlphaFilter(this.bevel * 10.0f, 0.75f, 0.1f);
        matte = s.filter(src, null);
        BufferedImage shineLayer = new BufferedImage(width, height, 2);
        Graphics2D g = shineLayer.createGraphics();
        g.setColor(new Color(this.shineColor));
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.DstIn);
        g.drawRenderedImage(matte, null);
        g.setComposite(AlphaComposite.DstOut);
        g.translate(xOffset, yOffset);
        g.drawRenderedImage(matte, null);
        g.dispose();
        shineLayer = new GaussianFilter(this.radius).filter(shineLayer, null);
        shineLayer = new RescaleFilter(3.0f * this.brightness).filter(shineLayer, shineLayer);
        g = dst.createGraphics();
        g.drawRenderedImage(src, null);
        g.setComposite(new AddComposite(1.0f));
        g.drawRenderedImage(shineLayer, null);
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Stylize/Shine...";
    }
}

