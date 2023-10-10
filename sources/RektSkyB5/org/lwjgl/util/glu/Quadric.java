/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Quadric {
    protected int drawStyle = 100012;
    protected int orientation = 100020;
    protected boolean textureFlag = false;
    protected int normals = 100000;

    protected void normal3f(float x2, float y2, float z2) {
        float mag = (float)Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2);
        if (mag > 1.0E-5f) {
            x2 /= mag;
            y2 /= mag;
            z2 /= mag;
        }
        GL11.glNormal3f(x2, y2, z2);
    }

    public void setDrawStyle(int drawStyle) {
        this.drawStyle = drawStyle;
    }

    public void setNormals(int normals) {
        this.normals = normals;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setTextureFlag(boolean textureFlag) {
        this.textureFlag = textureFlag;
    }

    public int getDrawStyle() {
        return this.drawStyle;
    }

    public int getNormals() {
        return this.normals;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public boolean getTextureFlag() {
        return this.textureFlag;
    }

    protected void TXTR_COORD(float x2, float y2) {
        if (this.textureFlag) {
            GL11.glTexCoord2f(x2, y2);
        }
    }

    protected float sin(float r2) {
        return (float)Math.sin(r2);
    }

    protected float cos(float r2) {
        return (float)Math.cos(r2);
    }
}

