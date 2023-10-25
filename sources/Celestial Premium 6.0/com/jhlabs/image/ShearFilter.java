/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.Rectangle;

public class ShearFilter
extends TransformFilter {
    private float xangle = 0.0f;
    private float yangle = 0.0f;
    private float shx = 0.0f;
    private float shy = 0.0f;
    private float xoffset = 0.0f;
    private float yoffset = 0.0f;
    private boolean resize = true;

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isResize() {
        return this.resize;
    }

    public void setXAngle(float xangle) {
        this.xangle = xangle;
        this.initialize();
    }

    public float getXAngle() {
        return this.xangle;
    }

    public void setYAngle(float yangle) {
        this.yangle = yangle;
        this.initialize();
    }

    public float getYAngle() {
        return this.yangle;
    }

    private void initialize() {
        this.shx = (float)Math.sin(this.xangle);
        this.shy = (float)Math.sin(this.yangle);
    }

    @Override
    protected void transformSpace(Rectangle r) {
        float tangent = (float)Math.tan(this.xangle);
        this.xoffset = (float)(-r.height) * tangent;
        if ((double)tangent < 0.0) {
            tangent = -tangent;
        }
        r.width = (int)((float)r.height * tangent + (float)r.width + 0.999999f);
        tangent = (float)Math.tan(this.yangle);
        this.yoffset = (float)(-r.width) * tangent;
        if ((double)tangent < 0.0) {
            tangent = -tangent;
        }
        r.height = (int)((float)r.width * tangent + (float)r.height + 0.999999f);
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        out[0] = (float)x + this.xoffset + (float)y * this.shx;
        out[1] = (float)y + this.yoffset + (float)x * this.shy;
    }

    public String toString() {
        return "Distort/Shear...";
    }
}

