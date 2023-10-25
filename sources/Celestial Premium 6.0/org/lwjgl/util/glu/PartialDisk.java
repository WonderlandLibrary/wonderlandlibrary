/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Quadric;

public class PartialDisk
extends Quadric {
    private static final int CACHE_SIZE = 240;

    public void draw(float innerRadius, float outerRadius, int slices, int loops, float startAngle, float sweepAngle) {
        int i;
        float[] sinCache = new float[240];
        float[] cosCache = new float[240];
        float texLow = 0.0f;
        float texHigh = 0.0f;
        if (slices >= 240) {
            slices = 239;
        }
        if (slices < 2 || loops < 1 || outerRadius <= 0.0f || innerRadius < 0.0f || innerRadius > outerRadius) {
            System.err.println("PartialDisk: GLU_INVALID_VALUE");
            return;
        }
        if (sweepAngle < -360.0f) {
            sweepAngle = 360.0f;
        }
        if (sweepAngle > 360.0f) {
            sweepAngle = 360.0f;
        }
        if (sweepAngle < 0.0f) {
            startAngle += sweepAngle;
            sweepAngle = -sweepAngle;
        }
        int slices2 = sweepAngle == 360.0f ? slices : slices + 1;
        float deltaRadius = outerRadius - innerRadius;
        float angleOffset = startAngle / 180.0f * (float)Math.PI;
        for (i = 0; i <= slices; ++i) {
            float angle = angleOffset + (float)Math.PI * sweepAngle / 180.0f * (float)i / (float)slices;
            sinCache[i] = this.sin(angle);
            cosCache[i] = this.cos(angle);
        }
        if (sweepAngle == 360.0f) {
            sinCache[slices] = sinCache[0];
            cosCache[slices] = cosCache[0];
        }
        switch (this.normals) {
            case 100000: 
            case 100001: {
                if (this.orientation == 100020) {
                    GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                    break;
                }
                GL11.glNormal3f(0.0f, 0.0f, -1.0f);
                break;
            }
        }
        block3 : switch (this.drawStyle) {
            case 100012: {
                float radiusLow;
                int finish;
                if (innerRadius == 0.0f) {
                    finish = loops - 1;
                    GL11.glBegin(6);
                    if (this.textureFlag) {
                        GL11.glTexCoord2f(0.5f, 0.5f);
                    }
                    GL11.glVertex3f(0.0f, 0.0f, 0.0f);
                    radiusLow = outerRadius - deltaRadius * ((float)(loops - 1) / (float)loops);
                    if (this.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                    }
                    if (this.orientation == 100020) {
                        for (i = slices; i >= 0; --i) {
                            if (this.textureFlag) {
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                        }
                    } else {
                        for (i = 0; i <= slices; ++i) {
                            if (this.textureFlag) {
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                        }
                    }
                    GL11.glEnd();
                } else {
                    finish = loops;
                }
                for (int j = 0; j < finish; ++j) {
                    radiusLow = outerRadius - deltaRadius * ((float)j / (float)loops);
                    float radiusHigh = outerRadius - deltaRadius * ((float)(j + 1) / (float)loops);
                    if (this.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                        texHigh = radiusHigh / outerRadius / 2.0f;
                    }
                    GL11.glBegin(8);
                    for (i = 0; i <= slices; ++i) {
                        if (this.orientation == 100020) {
                            if (this.textureFlag) {
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                            if (this.textureFlag) {
                                GL11.glTexCoord2f(texHigh * sinCache[i] + 0.5f, texHigh * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusHigh * sinCache[i], radiusHigh * cosCache[i], 0.0f);
                            continue;
                        }
                        if (this.textureFlag) {
                            GL11.glTexCoord2f(texHigh * sinCache[i] + 0.5f, texHigh * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusHigh * sinCache[i], radiusHigh * cosCache[i], 0.0f);
                        if (this.textureFlag) {
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                    }
                    GL11.glEnd();
                }
                break;
            }
            case 100010: {
                GL11.glBegin(0);
                for (i = 0; i < slices2; ++i) {
                    float sintemp = sinCache[i];
                    float costemp = cosCache[i];
                    for (int j = 0; j <= loops; ++j) {
                        float radiusLow = outerRadius - deltaRadius * ((float)j / (float)loops);
                        if (this.textureFlag) {
                            texLow = radiusLow / outerRadius / 2.0f;
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sintemp, radiusLow * costemp, 0.0f);
                    }
                }
                GL11.glEnd();
                break;
            }
            case 100011: {
                float radiusLow;
                int j;
                if (innerRadius == outerRadius) {
                    GL11.glBegin(3);
                    for (i = 0; i <= slices; ++i) {
                        if (this.textureFlag) {
                            GL11.glTexCoord2f(sinCache[i] / 2.0f + 0.5f, cosCache[i] / 2.0f + 0.5f);
                        }
                        GL11.glVertex3f(innerRadius * sinCache[i], innerRadius * cosCache[i], 0.0f);
                    }
                    GL11.glEnd();
                    break;
                }
                for (j = 0; j <= loops; ++j) {
                    radiusLow = outerRadius - deltaRadius * ((float)j / (float)loops);
                    if (this.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                    }
                    GL11.glBegin(3);
                    for (i = 0; i <= slices; ++i) {
                        if (this.textureFlag) {
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                    }
                    GL11.glEnd();
                }
                for (i = 0; i < slices2; ++i) {
                    float sintemp = sinCache[i];
                    float costemp = cosCache[i];
                    GL11.glBegin(3);
                    for (j = 0; j <= loops; ++j) {
                        radiusLow = outerRadius - deltaRadius * ((float)j / (float)loops);
                        if (this.textureFlag) {
                            texLow = radiusLow / outerRadius / 2.0f;
                        }
                        if (this.textureFlag) {
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sintemp, radiusLow * costemp, 0.0f);
                    }
                    GL11.glEnd();
                }
                break;
            }
            case 100013: {
                float radiusLow;
                int j;
                if (sweepAngle < 360.0f) {
                    for (i = 0; i <= slices; i += slices) {
                        float sintemp = sinCache[i];
                        float costemp = cosCache[i];
                        GL11.glBegin(3);
                        for (j = 0; j <= loops; ++j) {
                            radiusLow = outerRadius - deltaRadius * ((float)j / (float)loops);
                            if (this.textureFlag) {
                                texLow = radiusLow / outerRadius / 2.0f;
                                GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                            }
                            GL11.glVertex3f(radiusLow * sintemp, radiusLow * costemp, 0.0f);
                        }
                        GL11.glEnd();
                    }
                }
                for (j = 0; j <= loops; j += loops) {
                    radiusLow = outerRadius - deltaRadius * ((float)j / (float)loops);
                    if (this.textureFlag) {
                        texLow = radiusLow / outerRadius / 2.0f;
                    }
                    GL11.glBegin(3);
                    for (i = 0; i <= slices; ++i) {
                        if (this.textureFlag) {
                            GL11.glTexCoord2f(texLow * sinCache[i] + 0.5f, texLow * cosCache[i] + 0.5f);
                        }
                        GL11.glVertex3f(radiusLow * sinCache[i], radiusLow * cosCache[i], 0.0f);
                    }
                    GL11.glEnd();
                    if (innerRadius == outerRadius) break block3;
                }
                break;
            }
        }
    }
}

