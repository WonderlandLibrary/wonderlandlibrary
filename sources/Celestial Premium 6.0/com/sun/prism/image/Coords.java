/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.prism.image.ViewPort;

public class Coords {
    float x0;
    float y0;
    float x1;
    float y1;
    float u0;
    float v0;
    float u1;
    float v1;

    public Coords(float f, float f2, ViewPort viewPort) {
        this.x0 = 0.0f;
        this.x1 = f;
        this.y0 = 0.0f;
        this.y1 = f2;
        this.u0 = viewPort.u0;
        this.u1 = viewPort.u1;
        this.v0 = viewPort.v0;
        this.v1 = viewPort.v1;
    }

    public Coords() {
    }

    public void draw(Texture texture, Graphics graphics, float f, float f2) {
        graphics.drawTexture(texture, f + this.x0, f2 + this.y0, f + this.x1, f2 + this.y1, this.u0, this.v0, this.u1, this.v1);
    }

    public float getX(float f) {
        return (this.x0 * (this.u1 - f) + this.x1 * (f - this.u0)) / (this.u1 - this.u0);
    }

    public float getY(float f) {
        return (this.y0 * (this.v1 - f) + this.y1 * (f - this.v0)) / (this.v1 - this.v0);
    }
}

