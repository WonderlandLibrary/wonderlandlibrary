/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

public class FFT {
    protected float[] w1;
    protected float[] w2;
    protected float[] w3;

    public FFT(int logN) {
        this.w1 = new float[logN];
        this.w2 = new float[logN];
        this.w3 = new float[logN];
        int N = 1;
        for (int k = 0; k < logN; ++k) {
            double angle = Math.PI * -2 / (double)(N <<= 1);
            this.w1[k] = (float)Math.sin(0.5 * angle);
            this.w2[k] = -2.0f * this.w1[k] * this.w1[k];
            this.w3[k] = (float)Math.sin(angle);
        }
    }

    private void scramble(int n, float[] real, float[] imag) {
        int j = 0;
        for (int i = 0; i < n; ++i) {
            int m;
            if (i > j) {
                float t = real[j];
                real[j] = real[i];
                real[i] = t;
                t = imag[j];
                imag[j] = imag[i];
                imag[i] = t;
            }
            for (m = n >> 1; j >= m && m >= 2; j -= m, m >>= 1) {
            }
            j += m;
        }
    }

    private void butterflies(int n, int logN, int direction, float[] real, float[] imag) {
        int N = 1;
        for (int k = 0; k < logN; ++k) {
            int half_N = N;
            N <<= 1;
            float wt = (float)direction * this.w1[k];
            float wp_re = this.w2[k];
            float wp_im = (float)direction * this.w3[k];
            float w_re = 1.0f;
            float w_im = 0.0f;
            for (int offset = 0; offset < half_N; ++offset) {
                for (int i = offset; i < n; i += N) {
                    int j = i + half_N;
                    float re = real[j];
                    float im = imag[j];
                    float temp_re = w_re * re - w_im * im;
                    float temp_im = w_im * re + w_re * im;
                    real[j] = real[i] - temp_re;
                    int n2 = i;
                    real[n2] = real[n2] + temp_re;
                    imag[j] = imag[i] - temp_im;
                    int n3 = i;
                    imag[n3] = imag[n3] + temp_im;
                }
                wt = w_re;
                w_re = wt * wp_re - w_im * wp_im + w_re;
                w_im = w_im * wp_re + wt * wp_im + w_im;
            }
        }
        if (direction == -1) {
            float nr = 1.0f / (float)n;
            int i = 0;
            while (i < n) {
                int n4 = i;
                real[n4] = real[n4] * nr;
                int n5 = i++;
                imag[n5] = imag[n5] * nr;
            }
        }
    }

    public void transform1D(float[] real, float[] imag, int logN, int n, boolean forward) {
        this.scramble(n, real, imag);
        this.butterflies(n, logN, forward ? 1 : -1, real, imag);
    }

    public void transform2D(float[] real, float[] imag, int cols, int rows, boolean forward) {
        int log2cols = this.log2(cols);
        int log2rows = this.log2(rows);
        int n = Math.max(rows, cols);
        float[] rtemp = new float[n];
        float[] itemp = new float[n];
        for (int y = 0; y < rows; ++y) {
            int offset = y * cols;
            System.arraycopy(real, offset, rtemp, 0, cols);
            System.arraycopy(imag, offset, itemp, 0, cols);
            this.transform1D(rtemp, itemp, log2cols, cols, forward);
            System.arraycopy(rtemp, 0, real, offset, cols);
            System.arraycopy(itemp, 0, imag, offset, cols);
        }
        for (int x = 0; x < cols; ++x) {
            int y;
            int index = x;
            for (y = 0; y < rows; ++y) {
                rtemp[y] = real[index];
                itemp[y] = imag[index];
                index += cols;
            }
            this.transform1D(rtemp, itemp, log2rows, rows, forward);
            index = x;
            for (y = 0; y < rows; ++y) {
                real[index] = rtemp[y];
                imag[index] = itemp[y];
                index += cols;
            }
        }
    }

    private int log2(int n) {
        int m = 1;
        int log2n = 0;
        while (m < n) {
            m *= 2;
            ++log2n;
        }
        return m == n ? log2n : -1;
    }
}

