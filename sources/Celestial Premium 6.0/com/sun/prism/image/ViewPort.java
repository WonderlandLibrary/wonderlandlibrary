/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.image.Coords;

public class ViewPort {
    public float u0;
    public float v0;
    public float u1;
    public float v1;

    public ViewPort(float f, float f2, float f3, float f4) {
        this.u0 = f;
        this.u1 = f + f3;
        this.v0 = f2;
        this.v1 = f2 + f4;
    }

    public ViewPort getScaledVersion(float f) {
        if (f == 1.0f) {
            return this;
        }
        float f2 = this.u0 * f;
        float f3 = this.v0 * f;
        float f4 = this.u1 * f;
        float f5 = this.v1 * f;
        return new ViewPort(f2, f3, f4 - f2, f5 - f3);
    }

    public float getRelX(float f) {
        return (f - this.u0) / (this.u1 - this.u0);
    }

    public float getRelY(float f) {
        return (f - this.v0) / (this.v1 - this.v0);
    }

    public Coords getClippedCoords(float f, float f2, float f3, float f4) {
        Coords coords = new Coords(f3, f4, this);
        if (this.u1 > f || this.u0 < 0.0f) {
            if (this.u0 >= f || this.u1 <= 0.0f) {
                return null;
            }
            if (this.u1 > f) {
                coords.x1 = f3 * this.getRelX(f);
                coords.u1 = f;
            }
            if (this.u0 < 0.0f) {
                coords.x0 = f3 * this.getRelX(0.0f);
                coords.u0 = 0.0f;
            }
        }
        if (this.v1 > f2 || this.v0 < 0.0f) {
            if (this.v0 >= f2 || this.v1 <= 0.0f) {
                return null;
            }
            if (this.v1 > f2) {
                coords.y1 = f4 * this.getRelY(f2);
                coords.v1 = f2;
            }
            if (this.v0 < 0.0f) {
                coords.y0 = f4 * this.getRelY(0.0f);
                coords.v0 = 0.0f;
            }
        }
        return coords;
    }
}

