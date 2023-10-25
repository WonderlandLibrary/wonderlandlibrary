/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PointFilter;

public class CheckFilter
extends PointFilter {
    private int xScale = 8;
    private int yScale = 8;
    private int foreground = -1;
    private int background = -16777216;
    private int fuzziness = 0;
    private float angle = 0.0f;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    public int getForeground() {
        return this.foreground;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getBackground() {
        return this.background;
    }

    public void setXScale(int xScale) {
        this.xScale = xScale;
    }

    public int getXScale() {
        return this.xScale;
    }

    public void setYScale(int yScale) {
        this.yScale = yScale;
    }

    public int getYScale() {
        return this.yScale;
    }

    public void setFuzziness(int fuzziness) {
        this.fuzziness = fuzziness;
    }

    public int getFuzziness() {
        return this.fuzziness;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        this.m00 = cos;
        this.m01 = sin;
        this.m10 = -sin;
        this.m11 = cos;
    }

    public float getAngle() {
        return this.angle;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        float f;
        float nx = (this.m00 * (float)x + this.m01 * (float)y) / (float)this.xScale;
        float ny = (this.m10 * (float)x + this.m11 * (float)y) / (float)this.yScale;
        float f2 = f = (int)(nx + 100000.0f) % 2 != (int)(ny + 100000.0f) % 2 ? 1.0f : 0.0f;
        if (this.fuzziness != 0) {
            float fuzz = (float)this.fuzziness / 100.0f;
            float fx = ImageMath.smoothPulse(0.0f, fuzz, 1.0f - fuzz, 1.0f, ImageMath.mod(nx, 1.0f));
            float fy = ImageMath.smoothPulse(0.0f, fuzz, 1.0f - fuzz, 1.0f, ImageMath.mod(ny, 1.0f));
            f *= fx * fy;
        }
        return ImageMath.mixColors(f, this.foreground, this.background);
    }

    public String toString() {
        return "Texture/Checkerboard...";
    }
}

