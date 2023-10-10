/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Quadric;

public class Cylinder
extends Quadric {
    public void draw(float baseRadius, float topRadius, float height, int slices, int stacks) {
        float nsign = this.orientation == 100021 ? -1.0f : 1.0f;
        float da = (float)Math.PI * 2 / (float)slices;
        float dr = (topRadius - baseRadius) / (float)stacks;
        float dz = height / (float)stacks;
        float nz = (baseRadius - topRadius) / height;
        if (this.drawStyle == 100010) {
            GL11.glBegin(0);
            for (int i2 = 0; i2 < slices; ++i2) {
                float x2 = this.cos((float)i2 * da);
                float y2 = this.sin((float)i2 * da);
                this.normal3f(x2 * nsign, y2 * nsign, nz * nsign);
                float z2 = 0.0f;
                float r2 = baseRadius;
                for (int j2 = 0; j2 <= stacks; ++j2) {
                    GL11.glVertex3f(x2 * r2, y2 * r2, z2);
                    z2 += dz;
                    r2 += dr;
                }
            }
            GL11.glEnd();
        } else if (this.drawStyle == 100011 || this.drawStyle == 100013) {
            float y3;
            float x3;
            int i3;
            if (this.drawStyle == 100011) {
                float z3 = 0.0f;
                float r3 = baseRadius;
                for (int j3 = 0; j3 <= stacks; ++j3) {
                    GL11.glBegin(2);
                    for (i3 = 0; i3 < slices; ++i3) {
                        x3 = this.cos((float)i3 * da);
                        y3 = this.sin((float)i3 * da);
                        this.normal3f(x3 * nsign, y3 * nsign, nz * nsign);
                        GL11.glVertex3f(x3 * r3, y3 * r3, z3);
                    }
                    GL11.glEnd();
                    z3 += dz;
                    r3 += dr;
                }
            } else if ((double)baseRadius != 0.0) {
                GL11.glBegin(2);
                for (i3 = 0; i3 < slices; ++i3) {
                    x3 = this.cos((float)i3 * da);
                    y3 = this.sin((float)i3 * da);
                    this.normal3f(x3 * nsign, y3 * nsign, nz * nsign);
                    GL11.glVertex3f(x3 * baseRadius, y3 * baseRadius, 0.0f);
                }
                GL11.glEnd();
                GL11.glBegin(2);
                for (i3 = 0; i3 < slices; ++i3) {
                    x3 = this.cos((float)i3 * da);
                    y3 = this.sin((float)i3 * da);
                    this.normal3f(x3 * nsign, y3 * nsign, nz * nsign);
                    GL11.glVertex3f(x3 * topRadius, y3 * topRadius, height);
                }
                GL11.glEnd();
            }
            GL11.glBegin(1);
            for (i3 = 0; i3 < slices; ++i3) {
                x3 = this.cos((float)i3 * da);
                y3 = this.sin((float)i3 * da);
                this.normal3f(x3 * nsign, y3 * nsign, nz * nsign);
                GL11.glVertex3f(x3 * baseRadius, y3 * baseRadius, 0.0f);
                GL11.glVertex3f(x3 * topRadius, y3 * topRadius, height);
            }
            GL11.glEnd();
        } else if (this.drawStyle == 100012) {
            float ds = 1.0f / (float)slices;
            float dt = 1.0f / (float)stacks;
            float t2 = 0.0f;
            float z4 = 0.0f;
            float r4 = baseRadius;
            for (int j4 = 0; j4 < stacks; ++j4) {
                float s2 = 0.0f;
                GL11.glBegin(8);
                for (int i4 = 0; i4 <= slices; ++i4) {
                    float y4;
                    float x4;
                    if (i4 == slices) {
                        x4 = this.sin(0.0f);
                        y4 = this.cos(0.0f);
                    } else {
                        x4 = this.sin((float)i4 * da);
                        y4 = this.cos((float)i4 * da);
                    }
                    if (nsign == 1.0f) {
                        this.normal3f(x4 * nsign, y4 * nsign, nz * nsign);
                        this.TXTR_COORD(s2, t2);
                        GL11.glVertex3f(x4 * r4, y4 * r4, z4);
                        this.normal3f(x4 * nsign, y4 * nsign, nz * nsign);
                        this.TXTR_COORD(s2, t2 + dt);
                        GL11.glVertex3f(x4 * (r4 + dr), y4 * (r4 + dr), z4 + dz);
                    } else {
                        this.normal3f(x4 * nsign, y4 * nsign, nz * nsign);
                        this.TXTR_COORD(s2, t2);
                        GL11.glVertex3f(x4 * r4, y4 * r4, z4);
                        this.normal3f(x4 * nsign, y4 * nsign, nz * nsign);
                        this.TXTR_COORD(s2, t2 + dt);
                        GL11.glVertex3f(x4 * (r4 + dr), y4 * (r4 + dr), z4 + dz);
                    }
                    s2 += ds;
                }
                GL11.glEnd();
                r4 += dr;
                t2 += dt;
                z4 += dz;
            }
        }
    }
}

