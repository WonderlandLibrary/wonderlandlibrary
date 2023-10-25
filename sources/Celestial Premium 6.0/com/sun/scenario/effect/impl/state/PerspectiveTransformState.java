/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

public class PerspectiveTransformState {
    private float[][] itx = new float[3][3];

    public float[][] getITX() {
        return this.itx;
    }

    public void updateTx(float[][] arrf) {
        float f = PerspectiveTransformState.get3x3Determinant(arrf);
        if ((double)Math.abs(f) < 1.0E-10) {
            this.itx[2][0] = 0.0f;
            this.itx[1][0] = 0.0f;
            this.itx[0][0] = 0.0f;
            this.itx[2][1] = 0.0f;
            this.itx[1][1] = 0.0f;
            this.itx[0][1] = 0.0f;
            this.itx[1][2] = -1.0f;
            this.itx[0][2] = -1.0f;
            this.itx[2][2] = 1.0f;
        } else {
            float f2 = 1.0f / f;
            this.itx[0][0] = f2 * (arrf[1][1] * arrf[2][2] - arrf[1][2] * arrf[2][1]);
            this.itx[1][0] = f2 * (arrf[1][2] * arrf[2][0] - arrf[1][0] * arrf[2][2]);
            this.itx[2][0] = f2 * (arrf[1][0] * arrf[2][1] - arrf[1][1] * arrf[2][0]);
            this.itx[0][1] = f2 * (arrf[0][2] * arrf[2][1] - arrf[0][1] * arrf[2][2]);
            this.itx[1][1] = f2 * (arrf[0][0] * arrf[2][2] - arrf[0][2] * arrf[2][0]);
            this.itx[2][1] = f2 * (arrf[0][1] * arrf[2][0] - arrf[0][0] * arrf[2][1]);
            this.itx[0][2] = f2 * (arrf[0][1] * arrf[1][2] - arrf[0][2] * arrf[1][1]);
            this.itx[1][2] = f2 * (arrf[0][2] * arrf[1][0] - arrf[0][0] * arrf[1][2]);
            this.itx[2][2] = f2 * (arrf[0][0] * arrf[1][1] - arrf[0][1] * arrf[1][0]);
        }
    }

    private static float get3x3Determinant(float[][] arrf) {
        return arrf[0][0] * (arrf[1][1] * arrf[2][2] - arrf[1][2] * arrf[2][1]) - arrf[0][1] * (arrf[1][0] * arrf[2][2] - arrf[1][2] * arrf[2][0]) + arrf[0][2] * (arrf[1][0] * arrf[2][1] - arrf[1][1] * arrf[2][0]);
    }
}

