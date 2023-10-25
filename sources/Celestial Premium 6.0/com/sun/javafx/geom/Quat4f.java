/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.Matrix3f;

public class Quat4f {
    static final double EPS2 = 1.0E-30;
    public float x;
    public float y;
    public float z;
    public float w;

    public Quat4f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.w = 0.0f;
    }

    public Quat4f(float f, float f2, float f3, float f4) {
        float f5 = (float)(1.0 / Math.sqrt(f * f + f2 * f2 + f3 * f3 + f4 * f4));
        this.x = f * f5;
        this.y = f2 * f5;
        this.z = f3 * f5;
        this.w = f4 * f5;
    }

    public Quat4f(float[] arrf) {
        float f = (float)(1.0 / Math.sqrt(arrf[0] * arrf[0] + arrf[1] * arrf[1] + arrf[2] * arrf[2] + arrf[3] * arrf[3]));
        this.x = arrf[0] * f;
        this.y = arrf[1] * f;
        this.z = arrf[2] * f;
        this.w = arrf[3] * f;
    }

    public Quat4f(Quat4f quat4f) {
        this.x = quat4f.x;
        this.y = quat4f.y;
        this.z = quat4f.z;
        this.w = quat4f.w;
    }

    public final void normalize() {
        float f = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        if (f > 0.0f) {
            f = 1.0f / (float)Math.sqrt(f);
            this.x *= f;
            this.y *= f;
            this.z *= f;
            this.w *= f;
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
            this.w = 0.0f;
        }
    }

    public final void set(Matrix3f matrix3f) {
        float f = 0.25f * (matrix3f.m00 + matrix3f.m11 + matrix3f.m22 + 1.0f);
        if (f >= 0.0f) {
            if ((double)f >= 1.0E-30) {
                this.w = (float)Math.sqrt(f);
                f = 0.25f / this.w;
                this.x = (matrix3f.m21 - matrix3f.m12) * f;
                this.y = (matrix3f.m02 - matrix3f.m20) * f;
                this.z = (matrix3f.m10 - matrix3f.m01) * f;
                return;
            }
        } else {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.w = 0.0f;
        f = -0.5f * (matrix3f.m11 + matrix3f.m22);
        if (f >= 0.0f) {
            if ((double)f >= 1.0E-30) {
                this.x = (float)Math.sqrt(f);
                f = 0.5f / this.x;
                this.y = matrix3f.m10 * f;
                this.z = matrix3f.m20 * f;
                return;
            }
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.x = 0.0f;
        f = 0.5f * (1.0f - matrix3f.m22);
        if ((double)f >= 1.0E-30) {
            this.y = (float)Math.sqrt(f);
            this.z = matrix3f.m21 / (2.0f * this.y);
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }

    public final void set(float[][] arrf) {
        float f = 0.25f * (arrf[0][0] + arrf[1][1] + arrf[2][2] + 1.0f);
        if (f >= 0.0f) {
            if ((double)f >= 1.0E-30) {
                this.w = (float)Math.sqrt(f);
                f = 0.25f / this.w;
                this.x = (arrf[2][1] - arrf[1][2]) * f;
                this.y = (arrf[0][2] - arrf[2][0]) * f;
                this.z = (arrf[1][0] - arrf[0][1]) * f;
                return;
            }
        } else {
            this.w = 0.0f;
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.w = 0.0f;
        f = -0.5f * (arrf[1][1] + arrf[2][2]);
        if (f >= 0.0f) {
            if ((double)f >= 1.0E-30) {
                this.x = (float)Math.sqrt(f);
                f = 0.5f / this.x;
                this.y = arrf[1][0] * f;
                this.z = arrf[2][0] * f;
                return;
            }
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 1.0f;
            return;
        }
        this.x = 0.0f;
        f = 0.5f * (1.0f - arrf[2][2]);
        if ((double)f >= 1.0E-30) {
            this.y = (float)Math.sqrt(f);
            this.z = arrf[2][1] / (2.0f * this.y);
            return;
        }
        this.y = 0.0f;
        this.z = 1.0f;
    }

    public final void scale(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        this.w *= f;
    }

    public String toString() {
        return "Quat4f[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
}

