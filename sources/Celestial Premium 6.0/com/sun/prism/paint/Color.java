/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.prism.paint.Paint;
import java.nio.ByteBuffer;

public final class Color
extends Paint {
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    private final int argb;
    private final float r;
    private final float g;
    private final float b;
    private final float a;

    public Color(float f, float f2, float f3, float f4) {
        super(Paint.Type.COLOR, false, false);
        int n = (int)(255.0 * (double)f4);
        int n2 = (int)(255.0 * (double)f * (double)f4);
        int n3 = (int)(255.0 * (double)f2 * (double)f4);
        int n4 = (int)(255.0 * (double)f3 * (double)f4);
        this.argb = n << 24 | n2 << 16 | n3 << 8 | n4 << 0;
        this.r = f;
        this.g = f2;
        this.b = f3;
        this.a = f4;
    }

    public int getIntArgbPre() {
        return this.argb;
    }

    public void putRgbaPreBytes(byte[] arrby, int n) {
        arrby[n + 0] = (byte)(this.argb >> 16 & 0xFF);
        arrby[n + 1] = (byte)(this.argb >> 8 & 0xFF);
        arrby[n + 2] = (byte)(this.argb & 0xFF);
        arrby[n + 3] = (byte)(this.argb >> 24 & 0xFF);
    }

    public void putBgraPreBytes(ByteBuffer byteBuffer) {
        byteBuffer.put((byte)(this.argb & 0xFF));
        byteBuffer.put((byte)(this.argb >> 8 & 0xFF));
        byteBuffer.put((byte)(this.argb >> 16 & 0xFF));
        byteBuffer.put((byte)(this.argb >> 24 & 0xFF));
    }

    public float getRed() {
        return this.r;
    }

    public float getRedPremult() {
        return this.r * this.a;
    }

    public float getGreen() {
        return this.g;
    }

    public float getGreenPremult() {
        return this.g * this.a;
    }

    public float getBlue() {
        return this.b;
    }

    public float getBluePremult() {
        return this.b * this.a;
    }

    public float getAlpha() {
        return this.a;
    }

    @Override
    public boolean isOpaque() {
        return this.a >= 1.0f;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Color)) {
            return false;
        }
        Color color = (Color)object;
        return this.r == color.r && this.g == color.g && this.b == color.b && this.a == color.a;
    }

    public int hashCode() {
        int n = 3;
        n = 53 * n + Float.floatToIntBits(this.r);
        n = 53 * n + Float.floatToIntBits(this.g);
        n = 53 * n + Float.floatToIntBits(this.b);
        n = 53 * n + Float.floatToIntBits(this.a);
        return n;
    }

    public String toString() {
        return "Color[r=" + this.r + ", g=" + this.g + ", b=" + this.b + ", a=" + this.a + "]";
    }
}

