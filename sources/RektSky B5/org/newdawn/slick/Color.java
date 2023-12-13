/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick;

import java.io.Serializable;
import java.nio.FloatBuffer;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.opengl.renderer.SGL;

public class Color
implements Serializable {
    private static final long serialVersionUID = 1393939L;
    protected transient SGL GL = Renderer.get();
    public static final Color transparent = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    public static final Color white = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Color yellow = new Color(1.0f, 1.0f, 0.0f, 1.0f);
    public static final Color red = new Color(1.0f, 0.0f, 0.0f, 1.0f);
    public static final Color blue = new Color(0.0f, 0.0f, 1.0f, 1.0f);
    public static final Color green = new Color(0.0f, 1.0f, 0.0f, 1.0f);
    public static final Color black = new Color(0.0f, 0.0f, 0.0f, 1.0f);
    public static final Color gray = new Color(0.5f, 0.5f, 0.5f, 1.0f);
    public static final Color cyan = new Color(0.0f, 1.0f, 1.0f, 1.0f);
    public static final Color darkGray = new Color(0.3f, 0.3f, 0.3f, 1.0f);
    public static final Color lightGray = new Color(0.7f, 0.7f, 0.7f, 1.0f);
    public static final Color pink = new Color(255, 175, 175, 255);
    public static final Color orange = new Color(255, 200, 0, 255);
    public static final Color magenta = new Color(255, 0, 255, 255);
    public float r;
    public float g;
    public float b;
    public float a = 1.0f;

    public Color(Color color) {
        this.r = color.r;
        this.g = color.g;
        this.b = color.b;
        this.a = color.a;
    }

    public Color(FloatBuffer buffer) {
        this.r = buffer.get();
        this.g = buffer.get();
        this.b = buffer.get();
        this.a = buffer.get();
    }

    public Color(float r2, float g2, float b2) {
        this.r = r2;
        this.g = g2;
        this.b = b2;
        this.a = 1.0f;
    }

    public Color(float r2, float g2, float b2, float a2) {
        this.r = Math.min(r2, 1.0f);
        this.g = Math.min(g2, 1.0f);
        this.b = Math.min(b2, 1.0f);
        this.a = Math.min(a2, 1.0f);
    }

    public Color(int r2, int g2, int b2) {
        this.r = (float)r2 / 255.0f;
        this.g = (float)g2 / 255.0f;
        this.b = (float)b2 / 255.0f;
        this.a = 1.0f;
    }

    public Color(int r2, int g2, int b2, int a2) {
        this.r = (float)r2 / 255.0f;
        this.g = (float)g2 / 255.0f;
        this.b = (float)b2 / 255.0f;
        this.a = (float)a2 / 255.0f;
    }

    public Color(int value) {
        int r2 = (value & 0xFF0000) >> 16;
        int g2 = (value & 0xFF00) >> 8;
        int b2 = value & 0xFF;
        int a2 = (value & 0xFF000000) >> 24;
        if (a2 < 0) {
            a2 += 256;
        }
        if (a2 == 0) {
            a2 = 255;
        }
        this.r = (float)r2 / 255.0f;
        this.g = (float)g2 / 255.0f;
        this.b = (float)b2 / 255.0f;
        this.a = (float)a2 / 255.0f;
    }

    public static Color decode(String nm) {
        return new Color(Integer.decode(nm));
    }

    public void bind() {
        this.GL.glColor4f(this.r, this.g, this.b, this.a);
    }

    public int hashCode() {
        return (int)(this.r + this.g + this.b + this.a) * 255;
    }

    public boolean equals(Object other) {
        if (other instanceof Color) {
            Color o2 = (Color)other;
            return o2.r == this.r && o2.g == this.g && o2.b == this.b && o2.a == this.a;
        }
        return false;
    }

    public String toString() {
        return "Color (" + this.r + "," + this.g + "," + this.b + "," + this.a + ")";
    }

    public Color darker() {
        return this.darker(0.5f);
    }

    public Color darker(float scale) {
        scale = 1.0f - scale;
        Color temp = new Color(this.r * scale, this.g * scale, this.b * scale, this.a);
        return temp;
    }

    public Color brighter() {
        return this.brighter(0.2f);
    }

    public int getRed() {
        return (int)(this.r * 255.0f);
    }

    public int getGreen() {
        return (int)(this.g * 255.0f);
    }

    public int getBlue() {
        return (int)(this.b * 255.0f);
    }

    public int getAlpha() {
        return (int)(this.a * 255.0f);
    }

    public int getRedByte() {
        return (int)(this.r * 255.0f);
    }

    public int getGreenByte() {
        return (int)(this.g * 255.0f);
    }

    public int getBlueByte() {
        return (int)(this.b * 255.0f);
    }

    public int getAlphaByte() {
        return (int)(this.a * 255.0f);
    }

    public Color brighter(float scale) {
        Color temp = new Color(this.r * (scale += 1.0f), this.g * scale, this.b * scale, this.a);
        return temp;
    }

    public Color multiply(Color c2) {
        return new Color(this.r * c2.r, this.g * c2.g, this.b * c2.b, this.a * c2.a);
    }

    public void add(Color c2) {
        this.r += c2.r;
        this.g += c2.g;
        this.b += c2.b;
        this.a += c2.a;
    }

    public void scale(float value) {
        this.r *= value;
        this.g *= value;
        this.b *= value;
        this.a *= value;
    }

    public Color addToCopy(Color c2) {
        Color copy = new Color(this.r, this.g, this.b, this.a);
        copy.r += c2.r;
        copy.g += c2.g;
        copy.b += c2.b;
        copy.a += c2.a;
        return copy;
    }

    public Color scaleCopy(float value) {
        Color copy = new Color(this.r, this.g, this.b, this.a);
        copy.r *= value;
        copy.g *= value;
        copy.b *= value;
        copy.a *= value;
        return copy;
    }
}

