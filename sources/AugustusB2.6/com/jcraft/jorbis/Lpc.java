// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Lpc
{
    Drft fft;
    int ln;
    int m;
    
    Lpc() {
        this.fft = new Drft();
    }
    
    static float lpc_from_data(final float[] array, final float[] array2, final int n, final int n2) {
        final float[] array3 = new float[n2 + 1];
        int n3 = n2 + 1;
        while (n3-- != 0) {
            float n4 = 0.0f;
            for (int i = n3; i < n; ++i) {
                n4 += array[i] * array[i - n3];
            }
            array3[n3] = n4;
        }
        float n5 = array3[0];
        for (int j = 0; j < n2; ++j) {
            float n6 = -array3[j + 1];
            if (n5 == 0.0f) {
                for (int k = 0; k < n2; ++k) {
                    array2[k] = 0.0f;
                }
                return 0.0f;
            }
            for (int l = 0; l < j; ++l) {
                n6 -= array2[l] * array3[j - l];
            }
            final float n7 = n6 / n5;
            array2[j] = n7;
            int n8;
            for (n8 = 0; n8 < j / 2; ++n8) {
                final float n9 = array2[n8];
                final int n10 = n8;
                array2[n10] += n7 * array2[j - 1 - n8];
                final int n11 = j - 1 - n8;
                array2[n11] += n7 * n9;
            }
            if (j % 2 != 0) {
                final int n12 = n8;
                array2[n12] += array2[n8] * n7;
            }
            n5 *= (float)(1.0 - n7 * n7);
        }
        return n5;
    }
    
    float lpc_from_curve(final float[] array, final float[] array2) {
        final int ln = this.ln;
        final float[] array3 = new float[ln + ln];
        final float n = (float)(0.5 / ln);
        for (int i = 0; i < ln; ++i) {
            array3[i * 2] = array[i] * n;
            array3[i * 2 + 1] = 0.0f;
        }
        array3[ln * 2 - 1] = array[ln - 1] * n;
        final int n2 = ln * 2;
        this.fft.backward(array3);
        float n4;
        for (int j = 0, n3 = n2 / 2; j < n2 / 2; array3[j++] = array3[n3], array3[n3++] = n4) {
            n4 = array3[j];
        }
        return lpc_from_data(array3, array2, n2, this.m);
    }
    
    void init(final int ln, final int m) {
        this.ln = ln;
        this.m = m;
        this.fft.init(ln * 2);
    }
    
    void clear() {
        this.fft.clear();
    }
    
    static float FAST_HYPOT(final float n, final float n2) {
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    void lpc_to_curve(final float[] array, final float[] array2, final float n) {
        for (int i = 0; i < this.ln * 2; ++i) {
            array[i] = 0.0f;
        }
        if (n == 0.0f) {
            return;
        }
        for (int j = 0; j < this.m; ++j) {
            array[j * 2 + 1] = array2[j] / (4.0f * n);
            array[j * 2 + 2] = -array2[j] / (4.0f * n);
        }
        this.fft.backward(array);
        final int n2 = this.ln * 2;
        final float n3 = (float)(1.0 / n);
        array[0] = (float)(1.0 / (array[0] * 2.0f + n3));
        for (int k = 1; k < this.ln; ++k) {
            array[k] = (float)(1.0 / FAST_HYPOT(array[k] + array[n2 - k] + n3, array[k] - array[n2 - k]));
        }
    }
}
