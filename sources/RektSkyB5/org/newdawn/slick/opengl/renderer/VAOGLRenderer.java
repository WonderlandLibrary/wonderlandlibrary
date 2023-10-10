/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl.renderer;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.renderer.ImmediateModeOGLRenderer;

public class VAOGLRenderer
extends ImmediateModeOGLRenderer {
    private static final int TOLERANCE = 20;
    public static final int NONE = -1;
    public static final int MAX_VERTS = 5000;
    private int currentType = -1;
    private float[] color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
    private float[] tex = new float[]{0.0f, 0.0f};
    private int vertIndex;
    private float[] verts = new float[15000];
    private float[] cols = new float[20000];
    private float[] texs = new float[15000];
    private FloatBuffer vertices = BufferUtils.createFloatBuffer(15000);
    private FloatBuffer colors = BufferUtils.createFloatBuffer(20000);
    private FloatBuffer textures = BufferUtils.createFloatBuffer(10000);
    private int listMode = 0;

    public void initDisplay(int width, int height) {
        super.initDisplay(width, height);
        this.startBuffer();
        GL11.glEnableClientState(32884);
        GL11.glEnableClientState(32888);
        GL11.glEnableClientState(32886);
    }

    private void startBuffer() {
        this.vertIndex = 0;
    }

    private void flushBuffer() {
        if (this.vertIndex == 0) {
            return;
        }
        if (this.currentType == -1) {
            return;
        }
        if (this.vertIndex < 20) {
            GL11.glBegin(this.currentType);
            for (int i2 = 0; i2 < this.vertIndex; ++i2) {
                GL11.glColor4f(this.cols[i2 * 4 + 0], this.cols[i2 * 4 + 1], this.cols[i2 * 4 + 2], this.cols[i2 * 4 + 3]);
                GL11.glTexCoord2f(this.texs[i2 * 2 + 0], this.texs[i2 * 2 + 1]);
                GL11.glVertex3f(this.verts[i2 * 3 + 0], this.verts[i2 * 3 + 1], this.verts[i2 * 3 + 2]);
            }
            GL11.glEnd();
            this.currentType = -1;
            return;
        }
        this.vertices.clear();
        this.colors.clear();
        this.textures.clear();
        this.vertices.put(this.verts, 0, this.vertIndex * 3);
        this.colors.put(this.cols, 0, this.vertIndex * 4);
        this.textures.put(this.texs, 0, this.vertIndex * 2);
        this.vertices.flip();
        this.colors.flip();
        this.textures.flip();
        GL11.glVertexPointer(3, 0, this.vertices);
        GL11.glColorPointer(4, 0, this.colors);
        GL11.glTexCoordPointer(2, 0, this.textures);
        GL11.glDrawArrays(this.currentType, 0, this.vertIndex);
        this.currentType = -1;
    }

    private void applyBuffer() {
        if (this.listMode > 0) {
            return;
        }
        if (this.vertIndex != 0) {
            this.flushBuffer();
            this.startBuffer();
        }
        super.glColor4f(this.color[0], this.color[1], this.color[2], this.color[3]);
    }

    public void flush() {
        super.flush();
        this.applyBuffer();
    }

    public void glBegin(int geomType) {
        if (this.listMode > 0) {
            super.glBegin(geomType);
            return;
        }
        if (this.currentType != geomType) {
            this.applyBuffer();
            this.currentType = geomType;
        }
    }

    public void glColor4f(float r2, float g2, float b2, float a2) {
        this.color[0] = r2;
        this.color[1] = g2;
        this.color[2] = b2;
        this.color[3] = a2 *= this.alphaScale;
        if (this.listMode > 0) {
            super.glColor4f(r2, g2, b2, a2);
            return;
        }
    }

    public void glEnd() {
        if (this.listMode > 0) {
            super.glEnd();
            return;
        }
    }

    public void glTexCoord2f(float u2, float v2) {
        if (this.listMode > 0) {
            super.glTexCoord2f(u2, v2);
            return;
        }
        this.tex[0] = u2;
        this.tex[1] = v2;
    }

    public void glVertex2f(float x2, float y2) {
        if (this.listMode > 0) {
            super.glVertex2f(x2, y2);
            return;
        }
        this.glVertex3f(x2, y2, 0.0f);
    }

    public void glVertex3f(float x2, float y2, float z2) {
        if (this.listMode > 0) {
            super.glVertex3f(x2, y2, z2);
            return;
        }
        this.verts[this.vertIndex * 3 + 0] = x2;
        this.verts[this.vertIndex * 3 + 1] = y2;
        this.verts[this.vertIndex * 3 + 2] = z2;
        this.cols[this.vertIndex * 4 + 0] = this.color[0];
        this.cols[this.vertIndex * 4 + 1] = this.color[1];
        this.cols[this.vertIndex * 4 + 2] = this.color[2];
        this.cols[this.vertIndex * 4 + 3] = this.color[3];
        this.texs[this.vertIndex * 2 + 0] = this.tex[0];
        this.texs[this.vertIndex * 2 + 1] = this.tex[1];
        ++this.vertIndex;
        if (this.vertIndex > 4950 && this.isSplittable(this.vertIndex, this.currentType)) {
            int type = this.currentType;
            this.applyBuffer();
            this.currentType = type;
        }
    }

    private boolean isSplittable(int count, int type) {
        switch (type) {
            case 7: {
                return count % 4 == 0;
            }
            case 4: {
                return count % 3 == 0;
            }
            case 6913: {
                return count % 2 == 0;
            }
        }
        return false;
    }

    public void glBindTexture(int target, int id) {
        this.applyBuffer();
        super.glBindTexture(target, id);
    }

    public void glBlendFunc(int src, int dest) {
        this.applyBuffer();
        super.glBlendFunc(src, dest);
    }

    public void glCallList(int id) {
        this.applyBuffer();
        super.glCallList(id);
    }

    public void glClear(int value) {
        this.applyBuffer();
        super.glClear(value);
    }

    public void glClipPlane(int plane, DoubleBuffer buffer) {
        this.applyBuffer();
        super.glClipPlane(plane, buffer);
    }

    public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        this.applyBuffer();
        super.glColorMask(red, green, blue, alpha);
    }

    public void glDisable(int item) {
        this.applyBuffer();
        super.glDisable(item);
    }

    public void glEnable(int item) {
        this.applyBuffer();
        super.glEnable(item);
    }

    public void glLineWidth(float width) {
        this.applyBuffer();
        super.glLineWidth(width);
    }

    public void glPointSize(float size) {
        this.applyBuffer();
        super.glPointSize(size);
    }

    public void glPopMatrix() {
        this.applyBuffer();
        super.glPopMatrix();
    }

    public void glPushMatrix() {
        this.applyBuffer();
        super.glPushMatrix();
    }

    public void glRotatef(float angle, float x2, float y2, float z2) {
        this.applyBuffer();
        super.glRotatef(angle, x2, y2, z2);
    }

    public void glScalef(float x2, float y2, float z2) {
        this.applyBuffer();
        super.glScalef(x2, y2, z2);
    }

    public void glScissor(int x2, int y2, int width, int height) {
        this.applyBuffer();
        super.glScissor(x2, y2, width, height);
    }

    public void glTexEnvi(int target, int mode, int value) {
        this.applyBuffer();
        super.glTexEnvi(target, mode, value);
    }

    public void glTranslatef(float x2, float y2, float z2) {
        this.applyBuffer();
        super.glTranslatef(x2, y2, z2);
    }

    public void glEndList() {
        --this.listMode;
        super.glEndList();
    }

    public void glNewList(int id, int option) {
        ++this.listMode;
        super.glNewList(id, option);
    }

    public float[] getCurrentColor() {
        return this.color;
    }

    public void glLoadMatrix(FloatBuffer buffer) {
        this.flushBuffer();
        super.glLoadMatrix(buffer);
    }
}

