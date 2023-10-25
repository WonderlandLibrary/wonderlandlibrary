/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;

public class WarpGrid {
    public float[] xGrid = null;
    public float[] yGrid = null;
    public int rows;
    public int cols;
    private static final float m00 = -0.5f;
    private static final float m01 = 1.5f;
    private static final float m02 = -1.5f;
    private static final float m03 = 0.5f;
    private static final float m10 = 1.0f;
    private static final float m11 = -2.5f;
    private static final float m12 = 2.0f;
    private static final float m13 = -0.5f;
    private static final float m20 = -0.5f;
    private static final float m22 = 0.5f;
    private static final float m31 = 1.0f;

    public WarpGrid(int rows, int cols, int w, int h) {
        this.rows = rows;
        this.cols = cols;
        this.xGrid = new float[rows * cols];
        this.yGrid = new float[rows * cols];
        int index = 0;
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                this.xGrid[index] = (float)col * (float)(w - 1) / (float)(cols - 1);
                this.yGrid[index] = (float)row * (float)(h - 1) / (float)(rows - 1);
                ++index;
            }
        }
    }

    public void addRow(int before) {
        int size = (this.rows + 1) * this.cols;
        float[] x = new float[size];
        float[] y = new float[size];
        ++this.rows;
        int i = 0;
        int j = 0;
        for (int row = 0; row < this.rows; ++row) {
            for (int col = 0; col < this.cols; ++col) {
                int k = j + col;
                int l = i + col;
                if (row == before) {
                    x[k] = (this.xGrid[l] + this.xGrid[k]) / 2.0f;
                    y[k] = (this.yGrid[l] + this.yGrid[k]) / 2.0f;
                    continue;
                }
                x[k] = this.xGrid[l];
                y[k] = this.yGrid[l];
            }
            if (row != before - 1) {
                i += this.cols;
            }
            j += this.cols;
        }
        this.xGrid = x;
        this.yGrid = y;
    }

    public void addCol(int before) {
        int size = this.rows * (this.cols + 1);
        float[] x = new float[size];
        float[] y = new float[size];
        ++this.cols;
        int i = 0;
        int j = 0;
        for (int row = 0; row < this.rows; ++row) {
            for (int col = 0; col < this.cols; ++col) {
                if (col == before) {
                    x[j] = (this.xGrid[i] + this.xGrid[i - 1]) / 2.0f;
                    y[j] = (this.yGrid[i] + this.yGrid[i - 1]) / 2.0f;
                } else {
                    x[j] = this.xGrid[i];
                    y[j] = this.yGrid[i];
                    ++i;
                }
                ++j;
            }
        }
        this.xGrid = x;
        this.yGrid = y;
    }

    public void removeRow(int r) {
        int size = (this.rows - 1) * this.cols;
        float[] x = new float[size];
        float[] y = new float[size];
        --this.rows;
        int i = 0;
        int j = 0;
        for (int row = 0; row < this.rows; ++row) {
            for (int col = 0; col < this.cols; ++col) {
                int k = j + col;
                int l = i + col;
                x[k] = this.xGrid[l];
                y[k] = this.yGrid[l];
            }
            if (row == r - 1) {
                i += this.cols;
            }
            i += this.cols;
            j += this.cols;
        }
        this.xGrid = x;
        this.yGrid = y;
    }

    public void removeCol(int r) {
        int size = this.rows * (this.cols + 1);
        float[] x = new float[size];
        float[] y = new float[size];
        --this.cols;
        for (int row = 0; row < this.rows; ++row) {
            int i = row * (this.cols + 1);
            int j = row * this.cols;
            for (int col = 0; col < this.cols; ++col) {
                x[j] = this.xGrid[i];
                y[j] = this.yGrid[i];
                if (col == r - 1) {
                    ++i;
                }
                ++i;
                ++j;
            }
        }
        this.xGrid = x;
        this.yGrid = y;
    }

    public void lerp(float t, WarpGrid destination, WarpGrid intermediate) {
        if (this.rows != destination.rows || this.cols != destination.cols) {
            throw new IllegalArgumentException("source and destination are different sizes");
        }
        if (this.rows != intermediate.rows || this.cols != intermediate.cols) {
            throw new IllegalArgumentException("source and intermediate are different sizes");
        }
        int index = 0;
        for (int row = 0; row < this.rows; ++row) {
            for (int col = 0; col < this.cols; ++col) {
                intermediate.xGrid[index] = ImageMath.lerp(t, this.xGrid[index], destination.xGrid[index]);
                intermediate.yGrid[index] = ImageMath.lerp(t, this.yGrid[index], destination.yGrid[index]);
                ++index;
            }
        }
    }

    public void warp(int[] inPixels, int cols, int rows, WarpGrid sourceGrid, WarpGrid destGrid, int[] outPixels) {
        try {
            int y;
            int v;
            int i;
            int u;
            if (sourceGrid.rows != destGrid.rows || sourceGrid.cols != destGrid.cols) {
                throw new IllegalArgumentException("source and destination grids are different sizes");
            }
            int size = Math.max(cols, rows);
            float[] xrow = new float[size];
            float[] yrow = new float[size];
            float[] scale = new float[size + 1];
            float[] interpolated = new float[size + 1];
            int gridCols = sourceGrid.cols;
            int gridRows = sourceGrid.rows;
            WarpGrid splines = new WarpGrid(rows, gridCols, 1, 1);
            for (u = 0; u < gridCols; ++u) {
                i = u;
                for (v = 0; v < gridRows; ++v) {
                    xrow[v] = sourceGrid.xGrid[i];
                    yrow[v] = sourceGrid.yGrid[i];
                    i += gridCols;
                }
                this.interpolateSpline(yrow, xrow, 0, gridRows, interpolated, 0, rows);
                i = u;
                for (y = 0; y < rows; ++y) {
                    splines.xGrid[i] = interpolated[y];
                    i += gridCols;
                }
            }
            for (u = 0; u < gridCols; ++u) {
                i = u;
                for (v = 0; v < gridRows; ++v) {
                    xrow[v] = destGrid.xGrid[i];
                    yrow[v] = destGrid.yGrid[i];
                    i += gridCols;
                }
                this.interpolateSpline(yrow, xrow, 0, gridRows, interpolated, 0, rows);
                i = u;
                for (y = 0; y < rows; ++y) {
                    splines.yGrid[i] = interpolated[y];
                    i += gridCols;
                }
            }
            int[] intermediate = new int[rows * cols];
            int offset = 0;
            for (y = 0; y < rows; ++y) {
                this.interpolateSpline(splines.xGrid, splines.yGrid, offset, gridCols, scale, 0, cols);
                scale[cols] = cols;
                ImageMath.resample(inPixels, intermediate, cols, y * cols, 1, scale);
                offset += gridCols;
            }
            splines = new WarpGrid(gridRows, cols, 1, 1);
            offset = 0;
            int offset2 = 0;
            for (v = 0; v < gridRows; ++v) {
                this.interpolateSpline(sourceGrid.xGrid, sourceGrid.yGrid, offset, gridCols, splines.xGrid, offset2, cols);
                offset += gridCols;
                offset2 += cols;
            }
            offset = 0;
            offset2 = 0;
            for (v = 0; v < gridRows; ++v) {
                this.interpolateSpline(destGrid.xGrid, destGrid.yGrid, offset, gridCols, splines.yGrid, offset2, cols);
                offset += gridCols;
                offset2 += cols;
            }
            for (int x = 0; x < cols; ++x) {
                int i2 = x;
                for (v = 0; v < gridRows; ++v) {
                    xrow[v] = splines.xGrid[i2];
                    yrow[v] = splines.yGrid[i2];
                    i2 += cols;
                }
                this.interpolateSpline(xrow, yrow, 0, gridRows, scale, 0, rows);
                scale[rows] = rows;
                ImageMath.resample(intermediate, outPixels, rows, x, cols, scale);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void interpolateSpline(float[] xKnots, float[] yKnots, int offset, int length, float[] splineY, int splineOffset, int splineLength) {
        float k2;
        int index = offset;
        int end = offset + length - 1;
        float x0 = xKnots[index];
        float k1 = k2 = yKnots[index];
        float k0 = k2;
        float x1 = xKnots[index + 1];
        float k3 = yKnots[index + 1];
        for (int i = 0; i < splineLength; ++i) {
            if (index <= end && (float)i > xKnots[index]) {
                k0 = k1;
                k1 = k2;
                k2 = k3;
                x0 = xKnots[index];
                if (++index <= end) {
                    x1 = xKnots[index];
                }
                k3 = index < end ? yKnots[index + 1] : k2;
            }
            float t = ((float)i - x0) / (x1 - x0);
            float c3 = -0.5f * k0 + 1.5f * k1 + -1.5f * k2 + 0.5f * k3;
            float c2 = 1.0f * k0 + -2.5f * k1 + 2.0f * k2 + -0.5f * k3;
            float c1 = -0.5f * k0 + 0.5f * k2;
            float c0 = 1.0f * k1;
            splineY[splineOffset + i] = ((c3 * t + c2) * t + c1) * t + c0;
        }
    }

    protected void interpolateSpline2(float[] xKnots, float[] yKnots, int offset, float[] splineY, int splineOffset, int splineLength) {
        int index = offset;
        float leftX = xKnots[index];
        float leftY = yKnots[index];
        float rightX = xKnots[index + 1];
        float rightY = yKnots[index + 1];
        for (int i = 0; i < splineLength; ++i) {
            if ((float)i > xKnots[index]) {
                leftX = xKnots[index];
                leftY = yKnots[index];
                rightX = xKnots[++index];
                rightY = yKnots[index];
            }
            float f = ((float)i - leftX) / (rightX - leftX);
            splineY[splineOffset + i] = leftY + f * (rightY - leftY);
        }
    }
}

