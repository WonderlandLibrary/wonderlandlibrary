/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Quadric;

public class Disk
extends Quadric {
    public void draw(float innerRadius, float outerRadius, int slices, int loops) {
        if (this.normals != 100002) {
            if (this.orientation == 100020) {
                GL11.glNormal3f(0.0f, 0.0f, 1.0f);
            } else {
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
            }
        }
        float da = (float)Math.PI * 2 / (float)slices;
        float dr = (outerRadius - innerRadius) / (float)loops;
        switch (this.drawStyle) {
            case 100012: {
                float dtc = 2.0f * outerRadius;
                float r1 = innerRadius;
                for (int l = 0; l < loops; ++l) {
                    float ca;
                    float sa;
                    float a;
                    int s;
                    float r2 = r1 + dr;
                    if (this.orientation == 100020) {
                        GL11.glBegin(8);
                        for (s = 0; s <= slices; ++s) {
                            a = s == slices ? 0.0f : (float)s * da;
                            sa = this.sin(a);
                            ca = this.cos(a);
                            this.TXTR_COORD(0.5f + sa * r2 / dtc, 0.5f + ca * r2 / dtc);
                            GL11.glVertex2f(r2 * sa, r2 * ca);
                            this.TXTR_COORD(0.5f + sa * r1 / dtc, 0.5f + ca * r1 / dtc);
                            GL11.glVertex2f(r1 * sa, r1 * ca);
                        }
                        GL11.glEnd();
                    } else {
                        GL11.glBegin(8);
                        for (s = slices; s >= 0; --s) {
                            a = s == slices ? 0.0f : (float)s * da;
                            sa = this.sin(a);
                            ca = this.cos(a);
                            this.TXTR_COORD(0.5f - sa * r2 / dtc, 0.5f + ca * r2 / dtc);
                            GL11.glVertex2f(r2 * sa, r2 * ca);
                            this.TXTR_COORD(0.5f - sa * r1 / dtc, 0.5f + ca * r1 / dtc);
                            GL11.glVertex2f(r1 * sa, r1 * ca);
                        }
                        GL11.glEnd();
                    }
                    r1 = r2;
                }
                break;
            }
            case 100011: {
                int s;
                int l;
                for (l = 0; l <= loops; ++l) {
                    float r = innerRadius + (float)l * dr;
                    GL11.glBegin(2);
                    for (s = 0; s < slices; ++s) {
                        float a = (float)s * da;
                        GL11.glVertex2f(r * this.sin(a), r * this.cos(a));
                    }
                    GL11.glEnd();
                }
                for (s = 0; s < slices; ++s) {
                    float a = (float)s * da;
                    float x = this.sin(a);
                    float y = this.cos(a);
                    GL11.glBegin(3);
                    for (l = 0; l <= loops; ++l) {
                        float r = innerRadius + (float)l * dr;
                        GL11.glVertex2f(r * x, r * y);
                    }
                    GL11.glEnd();
                }
                break;
            }
            case 100010: {
                GL11.glBegin(0);
                for (int s = 0; s < slices; ++s) {
                    float a = (float)s * da;
                    float x = this.sin(a);
                    float y = this.cos(a);
                    for (int l = 0; l <= loops; ++l) {
                        float r = innerRadius * (float)l * dr;
                        GL11.glVertex2f(r * x, r * y);
                    }
                }
                GL11.glEnd();
                break;
            }
            case 100013: {
                float y;
                float x;
                float a;
                if ((double)innerRadius != 0.0) {
                    GL11.glBegin(2);
                    a = 0.0f;
                    while ((double)a < 6.2831854820251465) {
                        x = innerRadius * this.sin(a);
                        y = innerRadius * this.cos(a);
                        GL11.glVertex2f(x, y);
                        a += da;
                    }
                    GL11.glEnd();
                }
                GL11.glBegin(2);
                for (a = 0.0f; a < (float)Math.PI * 2; a += da) {
                    x = outerRadius * this.sin(a);
                    y = outerRadius * this.cos(a);
                    GL11.glVertex2f(x, y);
                }
                GL11.glEnd();
                break;
            }
            default: {
                return;
            }
        }
    }
}

