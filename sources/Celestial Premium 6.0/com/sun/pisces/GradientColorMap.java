/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

public final class GradientColorMap {
    public static final int CYCLE_NONE = 0;
    public static final int CYCLE_REPEAT = 1;
    public static final int CYCLE_REFLECT = 2;
    int cycleMethod;
    private static final int LG_RAMP_SIZE = 8;
    private static final int RAMP_SIZE = 256;
    int[] fractions = null;
    int[] rgba = null;
    int[] colors = null;

    GradientColorMap(int[] arrn, int[] arrn2, int n) {
        int[] arrn3;
        int[] arrn4;
        this.cycleMethod = n;
        int n2 = arrn.length;
        if (arrn[0] != 0) {
            arrn4 = new int[n2 + 1];
            arrn3 = new int[n2 + 1];
            System.arraycopy(arrn, 0, arrn4, 1, n2);
            System.arraycopy(arrn2, 0, arrn3, 1, n2);
            arrn4[0] = 0;
            arrn3[0] = arrn2[0];
            arrn = arrn4;
            arrn2 = arrn3;
            ++n2;
        }
        if (arrn[n2 - 1] != 65536) {
            arrn4 = new int[n2 + 1];
            arrn3 = new int[n2 + 1];
            System.arraycopy(arrn, 0, arrn4, 0, n2);
            System.arraycopy(arrn2, 0, arrn3, 0, n2);
            arrn4[n2] = 65536;
            arrn3[n2] = arrn2[n2 - 1];
            arrn = arrn4;
            arrn2 = arrn3;
        }
        this.fractions = new int[arrn.length];
        System.arraycopy(arrn, 0, this.fractions, 0, arrn.length);
        this.rgba = new int[arrn2.length];
        System.arraycopy(arrn2, 0, this.rgba, 0, arrn2.length);
        this.createRamp();
    }

    private int pad(int n) {
        switch (this.cycleMethod) {
            case 0: {
                if (n < 0) {
                    return 0;
                }
                if (n > 65535) {
                    return 65535;
                }
                return n;
            }
            case 1: {
                return n & 0xFFFF;
            }
            case 2: {
                if (n < 0) {
                    n = -n;
                }
                if ((n &= 0x1FFFF) > 65535) {
                    n = 131071 - n;
                }
                return n;
            }
        }
        throw new RuntimeException("Unknown cycle method: " + this.cycleMethod);
    }

    private int findStop(int n) {
        int n2 = this.fractions.length;
        for (int i = 1; i < n2; ++i) {
            if (this.fractions[i] <= n) continue;
            return i;
        }
        return 1;
    }

    private void accumColor(int n, int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6, int[] arrn7, int[] arrn8) {
        int n2 = this.findStop(n);
        int n3 = this.fractions[n2] - this.fractions[n2 - 1];
        arrn5[0] = arrn5[0] + (arrn[n2 - 1] + (n -= this.fractions[n2 - 1]) * (arrn[n2] - arrn[n2 - 1]) / n3);
        arrn6[0] = arrn6[0] + (arrn2[n2 - 1] + n * (arrn2[n2] - arrn2[n2 - 1]) / n3);
        arrn7[0] = arrn7[0] + (arrn3[n2 - 1] + n * (arrn3[n2] - arrn3[n2 - 1]) / n3);
        arrn8[0] = arrn8[0] + (arrn4[n2 - 1] + n * (arrn4[n2] - arrn4[n2 - 1]) / n3);
    }

    private int getColorAA(int n, int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6, int[] arrn7, int[] arrn8) {
        int n2;
        int n3 = this.findStop(n);
        if (this.fractions[n3 - 1] < this.pad(n - (n2 = 192)) && this.pad(n + n2) < this.fractions[n3]) {
            n2 = 0;
        }
        int n4 = 64;
        int n5 = 0;
        for (int i = -n2; i <= n2; i += n4) {
            int n6 = this.pad(n + i);
            this.accumColor(n6, arrn, arrn2, arrn3, arrn4, arrn5, arrn6, arrn7, arrn8);
            ++n5;
        }
        arrn8[0] = arrn8[0] / n5;
        arrn5[0] = arrn5[0] / n5;
        arrn6[0] = arrn6[0] / n5;
        arrn7[0] = arrn7[0] / n5;
        return arrn8[0] << 24 | arrn5[0] << 16 | arrn6[0] << 8 | arrn7[0];
    }

    private void createRamp() {
        int n;
        this.colors = new int[256];
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        int[] arrn4 = new int[1];
        int n2 = this.fractions.length;
        int[] arrn5 = new int[n2];
        int[] arrn6 = new int[n2];
        int[] arrn7 = new int[n2];
        int[] arrn8 = new int[n2];
        for (n = 0; n < n2; ++n) {
            arrn5[n] = this.rgba[n] >> 24 & 0xFF;
            arrn6[n] = this.rgba[n] >> 16 & 0xFF;
            arrn7[n] = this.rgba[n] >> 8 & 0xFF;
            arrn8[n] = this.rgba[n] & 0xFF;
        }
        n = 255;
        int n3 = 8;
        this.colors[0] = this.rgba[0];
        this.colors[n] = this.rgba[n2 - 1];
        for (int i = 1; i < n; ++i) {
            arrn[0] = 0;
            arrn4[0] = 0;
            arrn3[0] = 0;
            arrn2[0] = 0;
            this.colors[i] = this.getColorAA(i << n3, arrn6, arrn7, arrn8, arrn5, arrn2, arrn3, arrn4, arrn);
        }
    }
}

