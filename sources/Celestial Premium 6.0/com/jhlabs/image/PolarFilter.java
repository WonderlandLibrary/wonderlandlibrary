/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.TransformFilter;
import java.awt.image.BufferedImage;

public class PolarFilter
extends TransformFilter {
    public static final int RECT_TO_POLAR = 0;
    public static final int POLAR_TO_RECT = 1;
    public static final int INVERT_IN_CIRCLE = 2;
    private int type;
    private float width;
    private float height;
    private float centreX;
    private float centreY;
    private float radius;

    public PolarFilter() {
        this(0);
    }

    public PolarFilter(int type) {
        this.type = type;
        this.setEdgeAction(1);
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        this.width = src.getWidth();
        this.height = src.getHeight();
        this.centreX = this.width / 2.0f;
        this.centreY = this.height / 2.0f;
        this.radius = Math.max(this.centreY, this.centreX);
        return super.filter(src, dst);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    private float sqr(float x) {
        return x * x;
    }

    @Override
    protected void transformInverse(int x, int y, float[] out) {
        float r = 0.0f;
        switch (this.type) {
            case 0: {
                float theta = 0.0f;
                if ((float)x >= this.centreX) {
                    if ((float)y > this.centreY) {
                        theta = (float)Math.PI - (float)Math.atan(((float)x - this.centreX) / ((float)y - this.centreY));
                        r = (float)Math.sqrt(this.sqr((float)x - this.centreX) + this.sqr((float)y - this.centreY));
                    } else if ((float)y < this.centreY) {
                        theta = (float)Math.atan(((float)x - this.centreX) / (this.centreY - (float)y));
                        r = (float)Math.sqrt(this.sqr((float)x - this.centreX) + this.sqr(this.centreY - (float)y));
                    } else {
                        theta = 1.5707964f;
                        r = (float)x - this.centreX;
                    }
                } else if ((float)x < this.centreX) {
                    if ((float)y < this.centreY) {
                        theta = (float)Math.PI * 2 - (float)Math.atan((this.centreX - (float)x) / (this.centreY - (float)y));
                        r = (float)Math.sqrt(this.sqr(this.centreX - (float)x) + this.sqr(this.centreY - (float)y));
                    } else if ((float)y > this.centreY) {
                        theta = (float)Math.PI + (float)Math.atan((this.centreX - (float)x) / ((float)y - this.centreY));
                        r = (float)Math.sqrt(this.sqr(this.centreX - (float)x) + this.sqr((float)y - this.centreY));
                    } else {
                        theta = 4.712389f;
                        r = this.centreX - (float)x;
                    }
                }
                float m = (float)x != this.centreX ? Math.abs(((float)y - this.centreY) / ((float)x - this.centreX)) : 0.0f;
                if (m <= this.height / this.width) {
                    if ((float)x == this.centreX) {
                        float xmax = 0.0f;
                        float ymax = this.centreY;
                    } else {
                        float xmax = this.centreX;
                        float ymax = m * xmax;
                    }
                } else {
                    float ymax = this.centreY;
                    float xmax = ymax / m;
                }
                out[0] = this.width - 1.0f - (this.width - 1.0f) / ((float)Math.PI * 2) * theta;
                out[1] = this.height * r / this.radius;
                break;
            }
            case 1: {
                float theta = (float)x / this.width * ((float)Math.PI * 2);
                float theta2 = theta >= 4.712389f ? (float)Math.PI * 2 - theta : (theta >= (float)Math.PI ? theta - (float)Math.PI : (theta >= 1.5707964f ? (float)Math.PI - theta : theta));
                float t = (float)Math.tan(theta2);
                float m = t != 0.0f ? 1.0f / t : 0.0f;
                if (m <= this.height / this.width) {
                    if (theta2 == 0.0f) {
                        float xmax = 0.0f;
                        float ymax = this.centreY;
                    } else {
                        float xmax = this.centreX;
                        float ymax = m * xmax;
                    }
                } else {
                    float ymax = this.centreY;
                    float xmax = ymax / m;
                }
                r = this.radius * ((float)y / this.height);
                float nx = -r * (float)Math.sin(theta2);
                float ny = r * (float)Math.cos(theta2);
                if (theta >= 4.712389f) {
                    out[0] = this.centreX - nx;
                    out[1] = this.centreY - ny;
                    break;
                }
                if ((double)theta >= Math.PI) {
                    out[0] = this.centreX - nx;
                    out[1] = this.centreY + ny;
                    break;
                }
                if ((double)theta >= 1.5707963267948966) {
                    out[0] = this.centreX + nx;
                    out[1] = this.centreY + ny;
                    break;
                }
                out[0] = this.centreX + nx;
                out[1] = this.centreY - ny;
                break;
            }
            case 2: {
                float dx = (float)x - this.centreX;
                float dy = (float)y - this.centreY;
                float distance2 = dx * dx + dy * dy;
                out[0] = this.centreX + this.centreX * this.centreX * dx / distance2;
                out[1] = this.centreY + this.centreY * this.centreY * dy / distance2;
            }
        }
    }

    public String toString() {
        return "Distort/Polar Coordinates...";
    }
}

