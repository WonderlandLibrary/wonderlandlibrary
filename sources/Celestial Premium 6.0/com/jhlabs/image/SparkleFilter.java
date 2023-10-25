/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;
import java.util.Random;

public class SparkleFilter
extends PointFilter {
    private int rays = 50;
    private int radius = 25;
    private int amount = 50;
    private int color = -1;
    private int randomness = 25;
    private int width;
    private int height;
    private int centreX;
    private int centreY;
    private long seed = 371L;
    private float[] rayLengths;
    private Random randomNumbers = new Random();

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setRandomness(int randomness) {
        this.randomness = randomness;
    }

    public int getRandomness() {
        return this.randomness;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setRays(int rays) {
        this.rays = rays;
    }

    public int getRays() {
        return this.rays;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return this.radius;
    }

    @Override
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        this.centreX = width / 2;
        this.centreY = height / 2;
        super.setDimensions(width, height);
        this.randomNumbers.setSeed(this.seed);
        this.rayLengths = new float[this.rays];
        for (int i = 0; i < this.rays; ++i) {
            this.rayLengths[i] = (float)this.radius + (float)this.randomness / 100.0f * (float)this.radius * (float)this.randomNumbers.nextGaussian();
        }
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        float dx = x - this.centreX;
        float dy = y - this.centreY;
        float distance = dx * dx + dy * dy;
        float angle = (float)Math.atan2(dy, dx);
        float d = (angle + (float)Math.PI) / ((float)Math.PI * 2) * (float)this.rays;
        int i = (int)d;
        float f = d - (float)i;
        if (this.radius != 0) {
            float length = ImageMath.lerp(f, this.rayLengths[i % this.rays], this.rayLengths[(i + 1) % this.rays]);
            float g = length * length / (distance + 1.0E-4f);
            g = (float)Math.pow(g, (double)(100 - this.amount) / 50.0);
            f -= 0.5f;
            f = 1.0f - f * f;
            f *= g;
        }
        f = ImageMath.clamp(f, 0.0f, 1.0f);
        return ImageMath.mixColors(f, rgb, this.color);
    }

    public String toString() {
        return "Stylize/Sparkle...";
    }
}

