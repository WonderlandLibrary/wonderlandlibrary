/*
 * Decompiled with CFR 0.150.
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
            float s;
            float rho;
            int imax;
            int imin;
            float z;
            float y;
            float x;
            float theta;
            int j;
            if (!this.textureFlag) {
                GL11.glBegin(6);
                GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                GL11.glVertex3f(0.0f, 0.0f, nsign * radius);
                for (j = 0; j <= slices; ++j) {
                    theta = j == slices ? 0.0f : (float)j * dtheta;
                    x = -this.sin(theta) * this.sin(drho);
                    y = this.cos(theta) * this.sin(drho);
                    z = nsign * this.cos(drho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
            float ds = 1.0f / (float)slices;
            float dt = 1.0f / (float)stacks;
            float t = 1.0f;
            if (this.textureFlag) {
                imin = 0;
                imax = stacks;
            } else {
                imin = 1;
                imax = stacks - 1;
            }
            for (int i = imin; i < imax; ++i) {
                rho = (float)i * drho;
                GL11.glBegin(8);
                s = 0.0f;
                for (j = 0; j <= slices; ++j) {
                    theta = j == slices ? 0.0f : (float)j * dtheta;
                    x = -this.sin(theta) * this.sin(rho);
                    y = this.cos(theta) * this.sin(rho);
                    z = nsign * this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    this.TXTR_COORD(s, t);
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                    x = -this.sin(theta) * this.sin(rho + drho);
                    y = this.cos(theta) * this.sin(rho + drho);
                    z = nsign * this.cos(rho + drho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    this.TXTR_COORD(s, t - dt);
                    s += ds;
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
                t -= dt;
            }
            if (!this.textureFlag) {
                GL11.glBegin(6);
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
                GL11.glVertex3f(0.0f, 0.0f, -radius * nsign);
                rho = (float)Math.PI - drho;
                s = 1.0f;
                for (j = slices; j >= 0; --j) {
                    theta = j == slices ? 0.0f : (float)j * dtheta;
                    x = -this.sin(theta) * this.sin(rho);
                    y = this.cos(theta) * this.sin(rho);
                    z = nsign * this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    s -= ds;
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
        } else if (this.drawStyle == 100011 || this.drawStyle == 100013) {
            float z;
            float y;
            float x;
            float theta;
            int j;
            float rho;
            int i;
            for (i = 1; i < stacks; ++i) {
                rho = (float)i * drho;
                GL11.glBegin(2);
                for (j = 0; j < slices; ++j) {
                    theta = (float)j * dtheta;
                    x = this.cos(theta) * this.sin(rho);
                    y = this.sin(theta) * this.sin(rho);
                    z = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
                GL11.glEnd();
            }
            for (j = 0; j < slices; ++j) {
                theta = (float)j * dtheta;
                GL11.glBegin(3);
                for (i = 0; i <= stacks; ++i) {
                    rho = (float)i * drho;
                    x = this.cos(theta) * this.sin(rho);
                    y = this.sin(theta) * this.sin(rho);
                    z = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
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
            for (int i = 1; i < stacks - 1; ++i) {
                float rho = (float)i * drho;
                for (int j = 0; j < slices; ++j) {
                    float theta = (float)j * dtheta;
                    float x = this.cos(theta) * this.sin(rho);
                    float y = this.sin(theta) * this.sin(rho);
                    float z = this.cos(rho);
                    if (normals) {
                        GL11.glNormal3f(x * nsign, y * nsign, z * nsign);
                    }
                    GL11.glVertex3f(x * radius, y * radius, z * radius);
                }
            }
            GL11.glEnd();
        }
    }
}

