/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;

public class WeaveFilter
extends PointFilter {
    private float xWidth = 16.0f;
    private float yWidth = 16.0f;
    private float xGap = 6.0f;
    private float yGap = 6.0f;
    private int rows = 4;
    private int cols = 4;
    private int rgbX = -32640;
    private int rgbY = -8355585;
    private boolean useImageColors = true;
    private boolean roundThreads = false;
    private boolean shadeCrossings = true;
    public int[][] matrix;

    public WeaveFilter() {
        int[][] arrarrn = new int[4][];
        int[] arrn = new int[4];
        arrn[1] = 1;
        arrn[3] = 1;
        arrarrn[0] = arrn;
        int[] arrn2 = new int[4];
        arrn2[0] = 1;
        arrn2[2] = 1;
        arrarrn[1] = arrn2;
        int[] arrn3 = new int[4];
        arrn3[1] = 1;
        arrn3[3] = 1;
        arrarrn[2] = arrn3;
        int[] arrn4 = new int[4];
        arrn4[0] = 1;
        arrn4[2] = 1;
        arrarrn[3] = arrn4;
        this.matrix = arrarrn;
    }

    public void setXGap(float xGap) {
        this.xGap = xGap;
    }

    public void setXWidth(float xWidth) {
        this.xWidth = xWidth;
    }

    public float getXWidth() {
        return this.xWidth;
    }

    public void setYWidth(float yWidth) {
        this.yWidth = yWidth;
    }

    public float getYWidth() {
        return this.yWidth;
    }

    public float getXGap() {
        return this.xGap;
    }

    public void setYGap(float yGap) {
        this.yGap = yGap;
    }

    public float getYGap() {
        return this.yGap;
    }

    public void setCrossings(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getCrossings() {
        return this.matrix;
    }

    public void setUseImageColors(boolean useImageColors) {
        this.useImageColors = useImageColors;
    }

    public boolean getUseImageColors() {
        return this.useImageColors;
    }

    public void setRoundThreads(boolean roundThreads) {
        this.roundThreads = roundThreads;
    }

    public boolean getRoundThreads() {
        return this.roundThreads;
    }

    public void setShadeCrossings(boolean shadeCrossings) {
        this.shadeCrossings = shadeCrossings;
    }

    public boolean getShadeCrossings() {
        return this.shadeCrossings;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int v;
        int lrgbX;
        int lrgbY;
        float cY;
        float cX;
        float dY;
        float dX;
        boolean inY;
        x = (int)((float)x + (this.xWidth + this.xGap / 2.0f));
        y = (int)((float)y + (this.yWidth + this.yGap / 2.0f));
        float nx = ImageMath.mod((float)x, this.xWidth + this.xGap);
        float ny = ImageMath.mod((float)y, this.yWidth + this.yGap);
        int ix = (int)((float)x / (this.xWidth + this.xGap));
        int iy = (int)((float)y / (this.yWidth + this.yGap));
        boolean inX = nx < this.xWidth;
        boolean bl = inY = ny < this.yWidth;
        if (this.roundThreads) {
            dX = Math.abs(this.xWidth / 2.0f - nx) / this.xWidth / 2.0f;
            dY = Math.abs(this.yWidth / 2.0f - ny) / this.yWidth / 2.0f;
        } else {
            dY = 0.0f;
            dX = 0.0f;
        }
        if (this.shadeCrossings) {
            cX = ImageMath.smoothStep(this.xWidth / 2.0f, this.xWidth / 2.0f + this.xGap, Math.abs(this.xWidth / 2.0f - nx));
            cY = ImageMath.smoothStep(this.yWidth / 2.0f, this.yWidth / 2.0f + this.yGap, Math.abs(this.yWidth / 2.0f - ny));
        } else {
            cY = 0.0f;
            cX = 0.0f;
        }
        if (this.useImageColors) {
            lrgbX = lrgbY = rgb;
        } else {
            lrgbX = this.rgbX;
            lrgbY = this.rgbY;
        }
        int ixc = ix % this.cols;
        int iyr = iy % this.rows;
        int m = this.matrix[iyr][ixc];
        if (inX) {
            if (inY) {
                v = m == 1 ? lrgbX : lrgbY;
                v = ImageMath.mixColors(2.0f * (m == 1 ? dX : dY), v, -16777216);
            } else {
                if (this.shadeCrossings) {
                    if (m != this.matrix[(iy + 1) % this.rows][ixc]) {
                        if (m == 0) {
                            cY = 1.0f - cY;
                        }
                        lrgbX = ImageMath.mixColors(cY *= 0.5f, lrgbX, -16777216);
                    } else if (m == 0) {
                        lrgbX = ImageMath.mixColors(0.5f, lrgbX, -16777216);
                    }
                }
                v = ImageMath.mixColors(2.0f * dX, lrgbX, -16777216);
            }
        } else if (inY) {
            if (this.shadeCrossings) {
                if (m != this.matrix[iyr][(ix + 1) % this.cols]) {
                    if (m == 1) {
                        cX = 1.0f - cX;
                    }
                    lrgbY = ImageMath.mixColors(cX *= 0.5f, lrgbY, -16777216);
                } else if (m == 1) {
                    lrgbY = ImageMath.mixColors(0.5f, lrgbY, -16777216);
                }
            }
            v = ImageMath.mixColors(2.0f * dY, lrgbY, -16777216);
        } else {
            v = 0;
        }
        return v;
    }

    public String toString() {
        return "Texture/Weave...";
    }
}

