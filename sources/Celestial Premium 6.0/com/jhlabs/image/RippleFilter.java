/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;
import com.jhlabs.image.TransformFilter;
import com.jhlabs.math.Noise;
import java.awt.Rectangle;

public class RippleFilter
extends TransformFilter {
    public static final int SINE = 0;
    public static final int SAWTOOTH = 1;
    public static final int TRIANGLE = 2;
    public static final int NOISE = 3;
    private float xAmplitude = 5.0f;
    private float yAmplitude = 0.0f;
    private float xWavelength = 16.0f;
    private float yWavelength = 16.0f;
    private int waveType;

    public void setXAmplitude(float xAmplitude) {
        this.xAmplitude = xAmplitude;
    }

    public float getXAmplitude() {
        return this.xAmplitude;
    }

    public void setXWavelength(float xWavelength) {
        this.xWavelength = xWavelength;
    }

    public float getXWavelength() {
        return this.xWavelength;
    }

    public void setYAmplitude(float yAmplitude) {
        this.yAmplitude = yAmplitude;
    }

    public float getYAmplitude() {
        return this.yAmplitude;
    }

    public void setYWavelength(float yWavelength) {
        this.yWavelength = yWavelength;
    }

    public float getYWavelength() {
        return this.yWavelength;
    }

    public void setWaveType(int waveType) {
        this.waveType = waveType;
    }

    public int getWaveType() {
        return this.waveType;
    }

    @Override
    protected void transformSpace(Rectangle r) {
        if (this.edgeAction == 0) {
            r.x -= (int)this.xAmplitude;
            r.width += (int)(2.0f * this.xAmplitude);
            r.y -= (int)this.yAmplitude;
            r.height += (int)(2.0f * this.yAmplitude);
        }
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        float fy;
        float fx;
        float nx = (float)y / this.xWavelength;
        float ny = (float)x / this.yWavelength;
        switch (this.waveType) {
            default: {
                fx = (float)Math.sin(nx);
                fy = (float)Math.sin(ny);
                break;
            }
            case 1: {
                fx = ImageMath.mod(nx, 1.0f);
                fy = ImageMath.mod(ny, 1.0f);
                break;
            }
            case 2: {
                fx = ImageMath.triangle(nx);
                fy = ImageMath.triangle(ny);
                break;
            }
            case 3: {
                fx = Noise.noise1(nx);
                fy = Noise.noise1(ny);
            }
        }
        out[0] = (float)x + this.xAmplitude * fx;
        out[1] = (float)y + this.yAmplitude * fy;
    }

    public String toString() {
        return "Distort/Ripple...";
    }
}

