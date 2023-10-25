/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.javafx.geom.transform.AffineBase;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.paint.Color;
import java.util.Arrays;

public final class VertexBuffer {
    protected static final int VERTS_PER_QUAD = 4;
    protected static final int FLOATS_PER_TC = 2;
    protected static final int FLOATS_PER_VC = 3;
    protected static final int FLOATS_PER_VERT = 7;
    protected static final int BYTES_PER_VERT = 4;
    protected static final int VCOFF = 0;
    protected static final int TC1OFF = 3;
    protected static final int TC2OFF = 5;
    protected int capacity;
    protected int index;
    protected byte r;
    protected byte g;
    protected byte b;
    protected byte a;
    protected byte[] colorArray;
    protected float[] coordArray;
    private final BaseContext ownerCtx;

    public VertexBuffer(BaseContext baseContext, int n) {
        this.ownerCtx = baseContext;
        this.capacity = n * 4;
        this.index = 0;
        this.colorArray = new byte[this.capacity * 4];
        this.coordArray = new float[this.capacity * 7];
    }

    public final void setPerVertexColor(Color color, float f) {
        float f2 = color.getAlpha() * f;
        this.r = (byte)(color.getRed() * f2 * 255.0f);
        this.g = (byte)(color.getGreen() * f2 * 255.0f);
        this.b = (byte)(color.getBlue() * f2 * 255.0f);
        this.a = (byte)(f2 * 255.0f);
    }

    public final void setPerVertexColor(float f) {
        this.b = this.a = (byte)(f * 255.0f);
        this.g = this.a;
        this.r = this.a;
    }

    public final void updateVertexColors(int n) {
        for (int i = 0; i != n; ++i) {
            this.putColor(i);
        }
    }

    private void putColor(int n) {
        int n2 = n * 4;
        this.colorArray[n2 + 0] = this.r;
        this.colorArray[n2 + 1] = this.g;
        this.colorArray[n2 + 2] = this.b;
        this.colorArray[n2 + 3] = this.a;
    }

    public final void flush() {
        if (this.index > 0) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
            this.index = 0;
        }
    }

    public final void rewind() {
        this.index = 0;
    }

    private void grow() {
        this.capacity *= 2;
        this.colorArray = Arrays.copyOf(this.colorArray, this.capacity * 4);
        this.coordArray = Arrays.copyOf(this.coordArray, this.capacity * 7);
    }

    public final void addVert(float f, float f2) {
        if (this.index == this.capacity) {
            this.grow();
        }
        int n = 7 * this.index;
        this.coordArray[n + 0] = f;
        this.coordArray[n + 1] = f2;
        this.coordArray[n + 2] = 0.0f;
        this.putColor(this.index);
        ++this.index;
    }

    public final void addVert(float f, float f2, float f3, float f4) {
        if (this.index == this.capacity) {
            this.grow();
        }
        int n = 7 * this.index;
        this.coordArray[n + 0] = f;
        this.coordArray[n + 1] = f2;
        this.coordArray[n + 2] = 0.0f;
        this.coordArray[n + 3] = f3;
        this.coordArray[n + 4] = f4;
        this.putColor(this.index);
        ++this.index;
    }

    public final void addVert(float f, float f2, float f3, float f4, float f5, float f6) {
        if (this.index == this.capacity) {
            this.grow();
        }
        int n = 7 * this.index;
        this.coordArray[n + 0] = f;
        this.coordArray[n + 1] = f2;
        this.coordArray[n + 2] = 0.0f;
        this.coordArray[n + 3] = f3;
        this.coordArray[n + 4] = f4;
        this.coordArray[n + 5] = f5;
        this.coordArray[n + 6] = f6;
        this.putColor(this.index);
        ++this.index;
    }

    private void addVertNoCheck(float f, float f2) {
        int n = 7 * this.index;
        this.coordArray[n + 0] = f;
        this.coordArray[n + 1] = f2;
        this.coordArray[n + 2] = 0.0f;
        this.putColor(this.index);
        ++this.index;
    }

    private void addVertNoCheck(float f, float f2, float f3, float f4) {
        int n = 7 * this.index;
        this.coordArray[n + 0] = f;
        this.coordArray[n + 1] = f2;
        this.coordArray[n + 2] = 0.0f;
        this.coordArray[n + 3] = f3;
        this.coordArray[n + 4] = f4;
        this.putColor(this.index);
        ++this.index;
    }

    private void addVertNoCheck(float f, float f2, float f3, float f4, float f5, float f6) {
        int n = 7 * this.index;
        this.coordArray[n + 0] = f;
        this.coordArray[n + 1] = f2;
        this.coordArray[n + 2] = 0.0f;
        this.coordArray[n + 3] = f3;
        this.coordArray[n + 4] = f4;
        this.coordArray[n + 5] = f5;
        this.coordArray[n + 6] = f6;
        this.putColor(this.index);
        ++this.index;
    }

    private void ensureCapacityForQuad() {
        if (this.index + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, this.index);
            this.index = 0;
        }
    }

    public final void addQuad(float f, float f2, float f3, float f4) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f, f2);
        this.addVertNoCheck(f, f4);
        this.addVertNoCheck(f3, f2);
        this.addVertNoCheck(f3, f4);
    }

    public final void addQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f, f2, f5, f6, f9, f10);
        this.addVertNoCheck(f, f4, f5, f8, f9, f12);
        this.addVertNoCheck(f3, f2, f7, f6, f11, f10);
        this.addVertNoCheck(f3, f4, f7, f8, f11, f12);
    }

    public final void addMappedQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f, f2, f5, f6);
        this.addVertNoCheck(f, f4, f9, f10);
        this.addVertNoCheck(f3, f2, f7, f8);
        this.addVertNoCheck(f3, f4, f11, f12);
    }

    public final void addMappedQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20) {
        this.ensureCapacityForQuad();
        this.addVertNoCheck(f, f2, f5, f6, f13, f14);
        this.addVertNoCheck(f, f4, f9, f10, f17, f18);
        this.addVertNoCheck(f3, f2, f7, f8, f15, f16);
        this.addVertNoCheck(f3, f4, f11, f12, f19, f20);
    }

    public final void addQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, AffineBase affineBase) {
        this.addQuad(f, f2, f3, f4, f5, f6, f7, f8);
        if (affineBase != null) {
            int n = 7 * this.index - 7;
            affineBase.transform(this.coordArray, n + 0, this.coordArray, n + 5, 1);
            affineBase.transform(this.coordArray, (n -= 7) + 0, this.coordArray, n + 5, 1);
            affineBase.transform(this.coordArray, (n -= 7) + 0, this.coordArray, n + 5, 1);
            affineBase.transform(this.coordArray, (n -= 7) + 0, this.coordArray, n + 5, 1);
        }
    }

    public final void addSuperQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, boolean bl) {
        int n = this.index;
        if (n + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n);
            this.index = 0;
            n = 0;
        }
        int n2 = 7 * n;
        float[] arrf = this.coordArray;
        float f9 = bl ? 1.0f : 0.0f;
        float f10 = bl ? 0.0f : 1.0f;
        arrf[n2] = f;
        arrf[++n2] = f2;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f5;
        arrf[++n2] = f6;
        arrf[++n2] = f10;
        arrf[++n2] = f9;
        arrf[++n2] = f;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f5;
        arrf[++n2] = f8;
        arrf[++n2] = f10;
        arrf[++n2] = f9;
        arrf[++n2] = f3;
        arrf[++n2] = f2;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f7;
        arrf[++n2] = f6;
        arrf[++n2] = f10;
        arrf[++n2] = f9;
        arrf[++n2] = f3;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f7;
        arrf[++n2] = f8;
        arrf[++n2] = f10;
        arrf[++n2] = f9;
        ++n2;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n3 = 4 * n;
        arrby[n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        this.index = n + 4;
    }

    public final void addQuad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        int n = this.index;
        if (n + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n);
            this.index = 0;
            n = 0;
        }
        int n2 = 7 * n;
        float[] arrf = this.coordArray;
        arrf[n2] = f;
        arrf[++n2] = f2;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f5;
        arrf[++n2] = f6;
        arrf[n2 += 3] = f;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f5;
        arrf[++n2] = f8;
        arrf[n2 += 3] = f3;
        arrf[++n2] = f2;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f7;
        arrf[++n2] = f6;
        arrf[n2 += 3] = f3;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f7;
        arrf[++n2] = f8;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n3 = 4 * n;
        arrby[n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        this.index = n + 4;
    }

    public final void addQuadVO(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        int n = this.index;
        if (n + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n);
            this.index = 0;
            n = 0;
        }
        int n2 = 7 * n;
        float[] arrf = this.coordArray;
        arrf[n2] = f3;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f7;
        arrf[++n2] = f8;
        arrf[n2 += 3] = f3;
        arrf[++n2] = f6;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f7;
        arrf[++n2] = f10;
        arrf[n2 += 3] = f5;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f9;
        arrf[++n2] = f8;
        arrf[n2 += 3] = f5;
        arrf[++n2] = f6;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f9;
        arrf[++n2] = f10;
        byte[] arrby = this.colorArray;
        int n3 = 4 * n;
        byte by = (byte)(f * 255.0f);
        byte by2 = (byte)(f2 * 255.0f);
        arrby[n3] = by;
        arrby[++n3] = by;
        arrby[++n3] = by;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by2;
        arrby[++n3] = by2;
        arrby[++n3] = by2;
        arrby[++n3] = by;
        arrby[++n3] = by;
        arrby[++n3] = by;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by2;
        arrby[++n3] = by2;
        arrby[++n3] = by2;
        this.index = n + 4;
    }

    public final void addMappedPgram(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, AffineBase affineBase) {
        this.addMappedPgram(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f18, f17, f20, f19, f20);
        int n = 7 * this.index - 7;
        affineBase.transform(this.coordArray, n + 5, this.coordArray, n + 5, 1);
        affineBase.transform(this.coordArray, (n -= 7) + 5, this.coordArray, n + 5, 1);
        affineBase.transform(this.coordArray, (n -= 7) + 5, this.coordArray, n + 5, 1);
        affineBase.transform(this.coordArray, (n -= 7) + 5, this.coordArray, n + 5, 1);
    }

    public final void addMappedPgram(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18) {
        int n = this.index;
        if (n + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n);
            this.index = 0;
            n = 0;
        }
        int n2 = 7 * n;
        float[] arrf = this.coordArray;
        arrf[n2] = f;
        arrf[++n2] = f2;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f9;
        arrf[++n2] = f10;
        arrf[++n2] = f17;
        arrf[++n2] = f18;
        arrf[++n2] = f5;
        arrf[++n2] = f6;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f13;
        arrf[++n2] = f14;
        arrf[++n2] = f17;
        arrf[++n2] = f18;
        arrf[++n2] = f3;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f11;
        arrf[++n2] = f12;
        arrf[++n2] = f17;
        arrf[++n2] = f18;
        arrf[++n2] = f7;
        arrf[++n2] = f8;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f15;
        arrf[++n2] = f16;
        arrf[++n2] = f17;
        arrf[++n2] = f18;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n3 = 4 * n;
        arrby[n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        this.index = n + 4;
    }

    public final void addMappedPgram(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21, float f22, float f23, float f24) {
        int n = this.index;
        if (n + 4 > this.capacity) {
            this.ownerCtx.drawQuads(this.coordArray, this.colorArray, n);
            this.index = 0;
            n = 0;
        }
        int n2 = 7 * n;
        float[] arrf = this.coordArray;
        arrf[n2] = f;
        arrf[++n2] = f2;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f9;
        arrf[++n2] = f10;
        arrf[++n2] = f17;
        arrf[++n2] = f18;
        arrf[++n2] = f5;
        arrf[++n2] = f6;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f13;
        arrf[++n2] = f14;
        arrf[++n2] = f21;
        arrf[++n2] = f22;
        arrf[++n2] = f3;
        arrf[++n2] = f4;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f11;
        arrf[++n2] = f12;
        arrf[++n2] = f19;
        arrf[++n2] = f20;
        arrf[++n2] = f7;
        arrf[++n2] = f8;
        arrf[++n2] = 0.0f;
        arrf[++n2] = f15;
        arrf[++n2] = f16;
        arrf[++n2] = f23;
        arrf[++n2] = f24;
        byte[] arrby = this.colorArray;
        byte by = this.r;
        byte by2 = this.g;
        byte by3 = this.b;
        byte by4 = this.a;
        int n3 = 4 * n;
        arrby[n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        arrby[++n3] = by;
        arrby[++n3] = by2;
        arrby[++n3] = by3;
        arrby[++n3] = by4;
        this.index = n + 4;
    }
}

