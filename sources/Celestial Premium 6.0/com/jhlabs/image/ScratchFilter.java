/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ScratchFilter
extends AbstractBufferedImageOp {
    private float density = 0.1f;
    private float angle;
    private float angleVariation = 1.0f;
    private float width = 0.5f;
    private float length = 0.5f;
    private int color = -1;
    private int seed = 0;

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngleVariation(float angleVariation) {
        this.angleVariation = angleVariation;
    }

    public float getAngleVariation() {
        return this.angleVariation;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getLength() {
        return this.length;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getWidth() {
        return this.width;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getSeed() {
        return this.seed;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int width = src.getWidth();
        int height = src.getHeight();
        int numScratches = (int)(this.density * (float)width * (float)height / 100.0f);
        float l = this.length * (float)width;
        Random random = new Random(this.seed);
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(this.color));
        g.setStroke(new BasicStroke(this.width));
        for (int i = 0; i < numScratches; ++i) {
            float x = (float)width * random.nextFloat();
            float y = (float)height * random.nextFloat();
            float a = this.angle + (float)Math.PI * 2 * (this.angleVariation * (random.nextFloat() - 0.5f));
            float s = (float)Math.sin(a) * l;
            float c = (float)Math.cos(a) * l;
            float x1 = x - c;
            float y1 = y - s;
            float x2 = x + c;
            float y2 = y + s;
            g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
        }
        g.dispose();
        return dst;
    }

    public String toString() {
        return "Render/Scratches...";
    }
}

