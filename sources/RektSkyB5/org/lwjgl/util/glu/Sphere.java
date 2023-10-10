/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Quadric;

public class Sphere
extends Quadric {
    public void draw(float radius, int slices, int stacks) {
        boolean normals = this.normals != 100002;
        float nsign = this.orientation == 100021 ? -1.0f : 1.0f;
        float drho = (float)Math.PI / (float)stacks;
        float dtheta = (float)Math.PI * 2 / (float)slices;
        if (this.drawStyle == 100012) {
            float s2;
            float rho;
            int imax;
            int imin;
            float z2;
            float y2;
            float x2;
            float theta;
            int j2;
            if (!this.textureFlag) {
                GL11.glBegin(6);
                GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                GL11.glVertex3f(0.0f, 0.0f, nsign * radius);
                for (j2 = 0; j2 <= slices; ++j2) {
                    theta = j2 == slices ? 0.0f : (float)j2 * dtheta;
                    x2 = -this.sin(theta) * this.sin(drho);
                    y2 = this.cos(theta) * this.sin(drho);
                    z2 = nsign * this.cos(drho);
                    if (normals) {
                        GL11.glNormal3f(x2 * nsign, y2 * nsign, z2 * nsign);
                    }
                    GL11.glVertex3f(x2 * radius, y2 * radius, z2 * radius);
                }
                GL11.glEnd();
            }
            float ds = 1.0f / (float)slices;
            float dt = 1.0f / (float)stacks;
            float t2 = 1.0f;
            if (this.textureFlag) {
                imin = 0;
                imax = stacks;
            } else {
                imin = 1;
                imax = stacks - 1;
            }
            for (int i2 = imin; i2 < imax; ++i2) {
                rho = (float)i2 * drho;
                GL11.glBegin(8);
                s2 = 0.0f;
                for (j2 = 0; j2 <= slices; ++j2) {
                    theta = j2 == slices ? 0.0f : (float)j2 * dtheta;
                    x2 = -this.sin(theta) * this.sin(rho);
                    y2 = this.cos(theta) * this.sin(rho);
                    z2 = nsign * this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x2 * nsign, y2 * nsign, z2 * nsign);
                    }
                    this.TXTR_COORD(s2, t2);
                    GL11.glVertex3f(x2 * radius, y2 * radius, z2 * radius);
                    x2 = -this.sin(theta) * this.sin(rho + drho);
                    y2 = this.cos(theta) * this.sin(rho + drho);
                    z2 = nsign * this.cos(rho + drho);
                    if (normals) {
                        GL11.glNormal3f(x2 * nsign, y2 * nsign, z2 * nsign);
                    }
                    this.TXTR_COORD(s2, t2 - dt);
                    s2 += ds;
                    GL11.glVertex3f(x2 * radius, y2 * radius, z2 * radius);
                }
                GL11.glEnd();
                t2 -= dt;
            }
            if (!this.textureFlag) {
                GL11.glBegin(6);
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
                GL11.glVertex3f(0.0f, 0.0f, -radius * nsign);
                rho = (float)Math.PI - drho;
                s2 = 1.0f;
                for (j2 = slices; j2 >= 0; --j2) {
                    theta = j2 == slices ? 0.0f : (float)j2 * dtheta;
                    x2 = -this.sin(theta) * this.sin(rho);
                    y2 = this.cos(theta) * this.sin(rho);
                    z2 = nsign * this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x2 * nsign, y2 * nsign, z2 * nsign);
                    }
                    s2 -= ds;
                    GL11.glVertex3f(x2 * radius, y2 * radius, z2 * radius);
                }
                GL11.glEnd();
            }
        } else if (this.drawStyle == 100011 || this.drawStyle == 100013) {
            float z3;
            float y3;
            float x3;
            float theta;
            int j3;
            float rho;
            int i3;
            for (i3 = 1; i3 < stacks; ++i3) {
                rho = (float)i3 * drho;
                GL11.glBegin(2);
                for (j3 = 0; j3 < slices; ++j3) {
                    theta = (float)j3 * dtheta;
                    x3 = this.cos(theta) * this.sin(rho);
                    y3 = this.sin(theta) * this.sin(rho);
                    z3 = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x3 * nsign, y3 * nsign, z3 * nsign);
                    }
                    GL11.glVertex3f(x3 * radius, y3 * radius, z3 * radius);
                }
                GL11.glEnd();
            }
            for (j3 = 0; j3 < slices; ++j3) {
                theta = (float)j3 * dtheta;
                GL11.glBegin(3);
                for (i3 = 0; i3 <= stacks; ++i3) {
                    rho = (float)i3 * drho;
                    x3 = this.cos(theta) * this.sin(rho);
                    y3 = this.sin(theta) * this.sin(rho);
                    z3 = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x3 * nsign, y3 * nsign, z3 * nsign);
                    }
                    GL11.glVertex3f(x3 * radius, y3 * radius, z3 * radius);
                }
                GL11.glEnd();
            }
        } else if (this.drawStyle == 100010) {
            GL11.glBegin(0);
            if (normals) {
                GL11.glNormal3f(0.0f, 0.0f, nsign);
            }
            GL11.glVertex3f(0.0f, 0.0f, radius);
            if (normals) {
                GL11.glNormal3f(0.0f, 0.0f, -nsign);
            }
            GL11.glVertex3f(0.0f, 0.0f, -radius);
            for (int i4 = 1; i4 < stacks - 1; ++i4) {
                float rho = (float)i4 * drho;
                for (int j4 = 0; j4 < slices; ++j4) {
                    float theta = (float)j4 * dtheta;
                    float x4 = this.cos(theta) * this.sin(rho);
                    float y4 = this.sin(theta) * this.sin(rho);
                    float z4 = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x4 * nsign, y4 * nsign, z4 * nsign);
                    }
                    GL11.glVertex3f(x4 * radius, y4 * radius, z4 * radius);
                }
            }
            GL11.glEnd();
        }
    }
}

