/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class EmbossFilter
extends WholeImageFilter {
    private static final float pixelScale = 255.9f;
    private float azimuth = 2.3561945f;
    private float elevation = 0.5235988f;
    private boolean emboss = false;
    private float width45 = 3.0f;

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getAzimuth() {
        return this.azimuth;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public float getElevation() {
        return this.elevation;
    }

    public void setBumpHeight(float bumpHeight) {
        this.width45 = 3.0f * bumpHeight;
    }

    public float getBumpHeight() {
        return this.width45 / 3.0f;
    }

    public void setEmboss(boolean emboss) {
        this.emboss = emboss;
    }

    public boolean getEmboss() {
        return this.emboss;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];
        int bumpMapWidth = width;
        int bumpMapHeight = height;
        int[] bumpPixels = new int[bumpMapWidth * bumpMapHeight];
        for (int i = 0; i < inPixels.length; ++i) {
            bumpPixels[i] = PixelUtils.brightness(inPixels[i]);
        }
        int Lx = (int)(Math.cos(this.azimuth) * Math.cos(this.elevation) * (double)255.9f);
        int Ly = (int)(Math.sin(this.azimuth) * Math.cos(this.elevation) * (double)255.9f);
        int Lz = (int)(Math.sin(this.elevation) * (double)255.9f);
        int Nz = (int)(1530.0f / this.width45);
        int Nz2 = Nz * Nz;
        int NzLz = Nz * Lz;
        int background = Lz;
        int bumpIndex = 0;
        int y = 0;
        while (y < height) {
            int s1 = bumpIndex;
            int s2 = s1 + bumpMapWidth;
            int s3 = s2 + bumpMapWidth;
            int x = 0;
            while (x < width) {
                int shade;
                if (y != 0 && y < height - 2 && x != 0 && x < width - 2) {
                    int NdotL;
                    int Nx = bumpPixels[s1 - 1] + bumpPixels[s2 - 1] + bumpPixels[s3 - 1] - bumpPixels[s1 + 1] - bumpPixels[s2 + 1] - bumpPixels[s3 + 1];
                    int Ny = bumpPixels[s3 - 1] + bumpPixels[s3] + bumpPixels[s3 + 1] - bumpPixels[s1 - 1] - bumpPixels[s1] - bumpPixels[s1 + 1];
                    shade = Nx == 0 && Ny == 0 ? background : ((NdotL = Nx * Lx + Ny * Ly + NzLz) < 0 ? 0 : (int)((double)NdotL / Math.sqrt(Nx * Nx + Ny * Ny + Nz2)));
                } else {
                    shade = background;
                }
                if (this.emboss) {
                    int rgb = inPixels[index];
                    int a = rgb & 0xFF000000;
                    int r = rgb >> 16 & 0xFF;
                    int g = rgb >> 8 & 0xFF;
                    int b = rgb & 0xFF;
                    r = r * shade >> 8;
                    g = g * shade >> 8;
                    b = b * shade >> 8;
                    outPixels[index++] = a | r << 16 | g << 8 | b;
                } else {
                    outPixels[index++] = 0xFF000000 | shade << 16 | shade << 8 | shade;
                }
                ++x;
                ++s1;
                ++s2;
                ++s3;
            }
            ++y;
            bumpIndex += bumpMapWidth;
        }
        return outPixels;
    }

    public String toString() {
        return "Stylize/Emboss...";
    }
}

