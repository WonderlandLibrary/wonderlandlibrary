/*
 * Decompiled with CFR 0.150.
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
            for (int i = 0; i < slices; ++i) {
                float x = this.cos((float)i * da);
                float y = this.sin((float)i * da);
                this.normal3f(x * nsign, y * nsign, nz * nsign);
                float z = 0.0f;
                float r = baseRadius;
                for (int j = 0; j <= stacks; ++j) {
                    GL11.glVertex3f(x * r, y * r, z);
                    z += dz;
                    r += dr;
                }
            }
            GL11.glEnd();
        } else if (this.drawStyle == 100011 || this.drawStyle == 100013) {
            float y;
            float x;
            int i;
            if (this.drawStyle == 100011) {
                float z = 0.0f;
                float r = baseRadius;
                for (int j = 0; j <= stacks; ++j) {
                    GL11.glBegin(2);
                    for (i = 0; i < slices; ++i) {
                        x = this.cos((float)i * da);
                        y = this.sin((float)i * da);
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        GL11.glVertex3f(x * r, y * r, z);
                    }
                    GL11.glEnd();
                    z += dz;
                    r += dr;
                }
            } else if ((double)baseRadius != 0.0) {
                GL11.glBegin(2);
                for (i = 0; i < slices; ++i) {
                    x = this.cos((float)i * da);
                    y = this.sin((float)i * da);
                    this.normal3f(x * nsign, y * nsign, nz * nsign);
                    GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0f);
                }
                GL11.glEnd();
                GL11.glBegin(2);
                for (i = 0; i < slices; ++i) {
                    x = this.cos((float)i * da);
                    y = this.sin((float)i * da);
                    this.normal3f(x * nsign, y * nsign, nz * nsign);
                    GL11.glVertex3f(x * topRadius, y * topRadius, height);
                }
                GL11.glEnd();
            }
            GL11.glBegin(1);
            for (i = 0; i < slices; ++i) {
                x = this.cos((float)i * da);
                y = this.sin((float)i * da);
                this.normal3f(x * nsign, y * nsign, nz * nsign);
                GL11.glVertex3f(x * baseRadius, y * baseRadius, 0.0f);
                GL11.glVertex3f(x * topRadius, y * topRadius, height);
            }
            GL11.glEnd();
        } else if (this.drawStyle == 100012) {
            float ds = 1.0f / (float)slices;
            float dt = 1.0f / (float)stacks;
            float t = 0.0f;
            float z = 0.0f;
            float r = baseRadius;
            for (int j = 0; j < stacks; ++j) {
                float s = 0.0f;
                GL11.glBegin(8);
                for (int i = 0; i <= slices; ++i) {
                    float y;
                    float x;
                    if (i == slices) {
                        x = this.sin(0.0f);
                        y = this.cos(0.0f);
                    } else {
                        x = this.sin((float)i * da);
                        y = this.cos((float)i * da);
                    }
                    if (nsign == 1.0f) {
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t);
                        GL11.glVertex3f(x * r, y * r, z);
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t + dt);
                        GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
                    } else {
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t);
                        GL11.glVertex3f(x * r, y * r, z);
                        this.normal3f(x * nsign, y * nsign, nz * nsign);
                        this.TXTR_COORD(s, t + dt);
                        GL11.glVertex3f(x * (r + dr), y * (r + dr), z + dz);
                    }
                    s += ds;
                }
                GL11.glEnd();
                r += dr;
                t += dt;
                z += dz;
            }
        }
    }
}

