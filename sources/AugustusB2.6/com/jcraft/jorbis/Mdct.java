// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

class Mdct
{
    private static final float cPI3_8 = 0.38268343f;
    private static final float cPI2_8 = 0.70710677f;
    private static final float cPI1_8 = 0.9238795f;
    int n;
    int log2n;
    float[] trig;
    int[] bitrev;
    float scale;
    float[] _x;
    float[] _w;
    
    Mdct() {
        this._x = new float[1024];
        this._w = new float[1024];
    }
    
    void init(final int n) {
        this.bitrev = new int[n / 4];
        this.trig = new float[n + n / 4];
        this.log2n = (int)Math.rint(Math.log(n) / Math.log(2.0));
        this.n = n;
        final int n2 = 0;
        final int n3 = 1;
        final int n4 = n2 + n / 2;
        final int n5 = n4 + 1;
        final int n6 = n4 + n / 2;
        final int n7 = n6 + 1;
        for (int i = 0; i < n / 4; ++i) {
            this.trig[n2 + i * 2] = (float)Math.cos(3.141592653589793 / n * (4 * i));
            this.trig[n3 + i * 2] = (float)(-Math.sin(3.141592653589793 / n * (4 * i)));
            this.trig[n4 + i * 2] = (float)Math.cos(3.141592653589793 / (2 * n) * (2 * i + 1));
            this.trig[n5 + i * 2] = (float)Math.sin(3.141592653589793 / (2 * n) * (2 * i + 1));
        }
        for (int j = 0; j < n / 8; ++j) {
            this.trig[n6 + j * 2] = (float)Math.cos(3.141592653589793 / n * (4 * j + 2));
            this.trig[n7 + j * 2] = (float)(-Math.sin(3.141592653589793 / n * (4 * j + 2)));
        }
        final int n8 = (1 << this.log2n - 1) - 1;
        final int n9 = 1 << this.log2n - 2;
        for (int k = 0; k < n / 8; ++k) {
            int n10 = 0;
            for (int n11 = 0; n9 >>> n11 != 0; ++n11) {
                if ((n9 >>> n11 & k) != 0x0) {
                    n10 |= 1 << n11;
                }
            }
            this.bitrev[k * 2] = (~n10 & n8);
            this.bitrev[k * 2 + 1] = n10;
        }
        this.scale = 4.0f / n;
    }
    
    void clear() {
    }
    
    void forward(final float[] array, final float[] array2) {
    }
    
    synchronized void backward(final float[] array, final float[] array2) {
        if (this._x.length < this.n / 2) {
            this._x = new float[this.n / 2];
        }
        if (this._w.length < this.n / 2) {
            this._w = new float[this.n / 2];
        }
        final float[] x = this._x;
        final float[] w = this._w;
        final int n = this.n >>> 1;
        final int n2 = this.n >>> 2;
        final int n3 = this.n >>> 3;
        int n4 = 1;
        int n5 = 0;
        int n6 = n;
        for (int i = 0; i < n3; ++i) {
            n6 -= 2;
            x[n5++] = -array[n4 + 2] * this.trig[n6 + 1] - array[n4] * this.trig[n6];
            x[n5++] = array[n4] * this.trig[n6 + 1] - array[n4 + 2] * this.trig[n6];
            n4 += 4;
        }
        int n7 = n - 4;
        for (int j = 0; j < n3; ++j) {
            n6 -= 2;
            x[n5++] = array[n7] * this.trig[n6 + 1] + array[n7 + 2] * this.trig[n6];
            x[n5++] = array[n7] * this.trig[n6] - array[n7 + 2] * this.trig[n6 + 1];
            n7 -= 4;
        }
        final float[] mdct_kernel = this.mdct_kernel(x, w, this.n, n, n2, n3);
        int n8 = 0;
        int n9 = n;
        int n10 = n2;
        int n11 = n10 - 1;
        int n12 = n2 + n;
        int n13 = n12 - 1;
        for (int k = 0; k < n2; ++k) {
            final float n14 = mdct_kernel[n8] * this.trig[n9 + 1] - mdct_kernel[n8 + 1] * this.trig[n9];
            final float n15 = -(mdct_kernel[n8] * this.trig[n9] + mdct_kernel[n8 + 1] * this.trig[n9 + 1]);
            array2[n10] = -n14;
            array2[n11] = n14;
            array2[n13] = (array2[n12] = n15);
            ++n10;
            --n11;
            ++n12;
            --n13;
            n8 += 2;
            n9 += 2;
        }
    }
    
    private float[] mdct_kernel(float[] array, float[] array2, final int n, final int n2, final int n3, final int n4) {
        float n8;
        float n9;
        for (int n5 = n3, n6 = 0, n7 = n2, i = 0; i < n3; array2[i++] = n8 * this.trig[n7] + n9 * this.trig[n7 + 1], array2[i] = n9 * this.trig[n7] - n8 * this.trig[n7 + 1], array2[n3 + i] = array[n5++] + array[n6++], ++i) {
            n8 = array[n5] - array[n6];
            array2[n3 + i] = array[n5++] + array[n6++];
            n9 = array[n5] - array[n6];
            n7 -= 4;
        }
        for (int j = 0; j < this.log2n - 3; ++j) {
            int n10 = n >>> j + 2;
            final int n11 = 1 << j + 3;
            int n12 = n2 - 2;
            for (int n13 = 0, k = 0; k < n10 >>> 2; --n10, n13 += n11, ++k) {
                int n14 = n12;
                int n15 = n14 - (n10 >> 1);
                final float n16 = this.trig[n13];
                final float n17 = this.trig[n13 + 1];
                n12 -= 2;
                ++n10;
                for (int l = 0; l < 2 << j; ++l) {
                    final float n18 = array2[n14] - array2[n15];
                    array[n14] = array2[n14] + array2[n15];
                    final float n19 = array2[++n14] - array2[++n15];
                    array[n14] = array2[n14] + array2[n15];
                    array[n15] = n19 * n16 - n18 * n17;
                    array[n15 - 1] = n18 * n16 + n19 * n17;
                    n14 -= n10;
                    n15 -= n10;
                }
            }
            final float[] array3 = array2;
            array2 = array;
            array = array3;
        }
        int n20 = n;
        int n21 = 0;
        int n22 = 0;
        int n23 = n2 - 1;
        for (int n24 = 0; n24 < n4; ++n24) {
            final int n25 = this.bitrev[n21++];
            final int n26 = this.bitrev[n21++];
            final float n27 = array2[n25] - array2[n26 + 1];
            final float n28 = array2[n25 - 1] + array2[n26];
            final float n29 = array2[n25] + array2[n26 + 1];
            final float n30 = array2[n25 - 1] - array2[n26];
            final float n31 = n27 * this.trig[n20];
            final float n32 = n28 * this.trig[n20++];
            final float n33 = n27 * this.trig[n20];
            final float n34 = n28 * this.trig[n20++];
            array[n22++] = (n29 + n33 + n32) * 0.5f;
            final float[] array4 = array;
            final int n35 = n23;
            final int n36 = n35 - 1;
            array4[n35] = (-n30 + n34 - n31) * 0.5f;
            array[n22++] = (n30 + n34 - n31) * 0.5f;
            final float[] array5 = array;
            final int n37 = n36;
            n23 = n37 - 1;
            array5[n37] = (n29 - n33 - n32) * 0.5f;
        }
        return array;
    }
}
