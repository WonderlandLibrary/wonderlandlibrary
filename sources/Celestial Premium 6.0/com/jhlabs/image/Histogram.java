/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

public class Histogram {
    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;
    public static final int GRAY = 3;
    protected int[][] histogram;
    protected int numSamples;
    protected int[] minValue;
    protected int[] maxValue;
    protected int[] minFrequency;
    protected int[] maxFrequency;
    protected float[] mean;
    protected boolean isGray;

    public Histogram() {
        this.histogram = null;
        this.numSamples = 0;
        this.isGray = true;
        this.minValue = null;
        this.maxValue = null;
        this.minFrequency = null;
        this.maxFrequency = null;
        this.mean = null;
    }

    public Histogram(int[] pixels, int w, int h, int offset, int stride) {
        int i;
        this.histogram = new int[3][256];
        this.minValue = new int[4];
        this.maxValue = new int[4];
        this.minFrequency = new int[3];
        this.maxFrequency = new int[3];
        this.mean = new float[3];
        this.numSamples = w * h;
        this.isGray = true;
        int index = 0;
        for (int y = 0; y < h; ++y) {
            index = offset + y * stride;
            for (int x = 0; x < w; ++x) {
                int rgb = pixels[index++];
                int r = rgb >> 16 & 0xFF;
                int g = rgb >> 8 & 0xFF;
                int b = rgb & 0xFF;
                int[] arrn = this.histogram[0];
                int n = r;
                arrn[n] = arrn[n] + 1;
                int[] arrn2 = this.histogram[1];
                int n2 = g;
                arrn2[n2] = arrn2[n2] + 1;
                int[] arrn3 = this.histogram[2];
                int n3 = b;
                arrn3[n3] = arrn3[n3] + 1;
            }
        }
        for (i = 0; i < 256; ++i) {
            if (this.histogram[0][i] == this.histogram[1][i] && this.histogram[1][i] == this.histogram[2][i]) continue;
            this.isGray = false;
            break;
        }
        i = 0;
        while (i < 3) {
            int j;
            for (j = 0; j < 256; ++j) {
                if (this.histogram[i][j] <= 0) continue;
                this.minValue[i] = j;
                break;
            }
            for (j = 255; j >= 0; --j) {
                if (this.histogram[i][j] <= 0) continue;
                this.maxValue[i] = j;
                break;
            }
            this.minFrequency[i] = Integer.MAX_VALUE;
            this.maxFrequency[i] = 0;
            for (j = 0; j < 256; ++j) {
                this.minFrequency[i] = Math.min(this.minFrequency[i], this.histogram[i][j]);
                this.maxFrequency[i] = Math.max(this.maxFrequency[i], this.histogram[i][j]);
                int n = i;
                this.mean[n] = this.mean[n] + (float)(j * this.histogram[i][j]);
            }
            int n = i++;
            this.mean[n] = this.mean[n] / (float)this.numSamples;
        }
        this.minValue[3] = Math.min(Math.min(this.minValue[0], this.minValue[1]), this.minValue[2]);
        this.maxValue[3] = Math.max(Math.max(this.maxValue[0], this.maxValue[1]), this.maxValue[2]);
    }

    public boolean isGray() {
        return this.isGray;
    }

    public int getNumSamples() {
        return this.numSamples;
    }

    public int getFrequency(int value) {
        if (this.numSamples > 0 && this.isGray && value >= 0 && value <= 255) {
            return this.histogram[0][value];
        }
        return -1;
    }

    public int getFrequency(int channel, int value) {
        if (this.numSamples < 1 || channel < 0 || channel > 2 || value < 0 || value > 255) {
            return -1;
        }
        return this.histogram[channel][value];
    }

    public int getMinFrequency() {
        if (this.numSamples > 0 && this.isGray) {
            return this.minFrequency[0];
        }
        return -1;
    }

    public int getMinFrequency(int channel) {
        if (this.numSamples < 1 || channel < 0 || channel > 2) {
            return -1;
        }
        return this.minFrequency[channel];
    }

    public int getMaxFrequency() {
        if (this.numSamples > 0 && this.isGray) {
            return this.maxFrequency[0];
        }
        return -1;
    }

    public int getMaxFrequency(int channel) {
        if (this.numSamples < 1 || channel < 0 || channel > 2) {
            return -1;
        }
        return this.maxFrequency[channel];
    }

    public int getMinValue() {
        if (this.numSamples > 0 && this.isGray) {
            return this.minValue[0];
        }
        return -1;
    }

    public int getMinValue(int channel) {
        return this.minValue[channel];
    }

    public int getMaxValue() {
        if (this.numSamples > 0 && this.isGray) {
            return this.maxValue[0];
        }
        return -1;
    }

    public int getMaxValue(int channel) {
        return this.maxValue[channel];
    }

    public float getMeanValue() {
        if (this.numSamples > 0 && this.isGray) {
            return this.mean[0];
        }
        return -1.0f;
    }

    public float getMeanValue(int channel) {
        if (this.numSamples > 0 && channel >= 0 && channel <= 2) {
            return this.mean[channel];
        }
        return -1.0f;
    }
}

