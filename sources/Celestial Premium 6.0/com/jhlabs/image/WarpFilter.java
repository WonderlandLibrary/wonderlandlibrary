/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.WarpGrid;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class WarpFilter
extends WholeImageFilter {
    private WarpGrid sourceGrid;
    private WarpGrid destGrid;
    private int frames = 1;
    private BufferedImage morphImage;
    private float time;

    public WarpFilter() {
    }

    public WarpFilter(WarpGrid sourceGrid, WarpGrid destGrid) {
        this.sourceGrid = sourceGrid;
        this.destGrid = destGrid;
    }

    public void setSourceGrid(WarpGrid sourceGrid) {
        this.sourceGrid = sourceGrid;
    }

    public WarpGrid getSourceGrid() {
        return this.sourceGrid;
    }

    public void setDestGrid(WarpGrid destGrid) {
        this.destGrid = destGrid;
    }

    public WarpGrid getDestGrid() {
        return this.destGrid;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public int getFrames() {
        return this.frames;
    }

    public void setMorphImage(BufferedImage morphImage) {
        this.morphImage = morphImage;
    }

    public BufferedImage getMorphImage() {
        return this.morphImage;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getTime() {
        return this.time;
    }

    @Override
    protected void transformSpace(Rectangle r) {
        r.width *= this.frames;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int[] outPixels = new int[width * height];
        if (this.morphImage != null) {
            int[] morphPixels = this.getRGB(this.morphImage, 0, 0, width, height, null);
            this.morph(inPixels, morphPixels, outPixels, this.sourceGrid, this.destGrid, width, height, this.time);
        } else if (this.frames <= 1) {
            this.sourceGrid.warp(inPixels, width, height, this.sourceGrid, this.destGrid, outPixels);
        } else {
            WarpGrid newGrid = new WarpGrid(this.sourceGrid.rows, this.sourceGrid.cols, width, height);
            for (int i = 0; i < this.frames; ++i) {
                float t = (float)i / (float)(this.frames - 1);
                this.sourceGrid.lerp(t, this.destGrid, newGrid);
                this.sourceGrid.warp(inPixels, width, height, this.sourceGrid, newGrid, outPixels);
            }
        }
        return outPixels;
    }

    public void morph(int[] srcPixels, int[] destPixels, int[] outPixels, WarpGrid srcGrid, WarpGrid destGrid, int width, int height, float t) {
        WarpGrid newGrid = new WarpGrid(srcGrid.rows, srcGrid.cols, width, height);
        srcGrid.lerp(t, destGrid, newGrid);
        srcGrid.warp(srcPixels, width, height, srcGrid, newGrid, outPixels);
        int[] destPixels2 = new int[width * height];
        destGrid.warp(destPixels, width, height, destGrid, newGrid, destPixels2);
        this.crossDissolve(outPixels, destPixels2, width, height, t);
    }

    public void crossDissolve(int[] pixels1, int[] pixels2, int width, int height, float t) {
        int index = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                pixels1[index] = ImageMath.mixColors(t, pixels1[index], pixels2[index]);
                ++index;
            }
        }
    }

    public String toString() {
        return "Distort/Mesh Warp...";
    }
}

