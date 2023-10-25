/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.composite.SubtractComposite;
import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.BoxBlurFilter;
import com.jhlabs.image.InvertFilter;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class DoGFilter
extends AbstractBufferedImageOp {
    private float radius1 = 1.0f;
    private float radius2 = 2.0f;
    private boolean normalize = true;
    private boolean invert;

    public void setRadius1(float radius1) {
        this.radius1 = radius1;
    }

    public float getRadius1() {
        return this.radius1;
    }

    public void setRadius2(float radius2) {
        this.radius2 = radius2;
    }

    public float getRadius2() {
        return this.radius2;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public boolean getNormalize() {
        return this.normalize;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public boolean getInvert() {
        return this.invert;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage image1 = new BoxBlurFilter(this.radius1, this.radius1, 3).filter(src, null);
        BufferedImage image2 = new BoxBlurFilter(this.radius2, this.radius2, 3).filter(src, null);
        Graphics2D g2d = image2.createGraphics();
        g2d.setComposite(new SubtractComposite(1.0f));
        g2d.drawImage((Image)image1, 0, 0, null);
        g2d.dispose();
        if (this.normalize && this.radius1 != this.radius2) {
            int b;
            int g;
            int r;
            int rgb;
            int x;
            int y;
            int[] pixels = null;
            int max = 0;
            for (y = 0; y < height; ++y) {
                pixels = this.getRGB(image2, 0, y, width, 1, pixels);
                for (x = 0; x < width; ++x) {
                    rgb = pixels[x];
                    r = rgb >> 16 & 0xFF;
                    g = rgb >> 8 & 0xFF;
                    b = rgb & 0xFF;
                    if (r > max) {
                        max = r;
                    }
                    if (g > max) {
                        max = g;
                    }
                    if (b <= max) continue;
                    max = b;
                }
            }
            for (y = 0; y < height; ++y) {
                pixels = this.getRGB(image2, 0, y, width, 1, pixels);
                for (x = 0; x < width; ++x) {
                    rgb = pixels[x];
                    r = rgb >> 16 & 0xFF;
                    g = rgb >> 8 & 0xFF;
                    b = rgb & 0xFF;
                    r = r * 255 / max;
                    g = g * 255 / max;
                    b = b * 255 / max;
                    pixels[x] = rgb & 0xFF000000 | r << 16 | g << 8 | b;
                }
                this.setRGB(image2, 0, y, width, 1, pixels);
            }
        }
        if (this.invert) {
            image2 = new InvertFilter().filter(image2, image2);
        }
        return image2;
    }

    public String toString() {
        return "Edges/Difference of Gaussians...";
    }
}

