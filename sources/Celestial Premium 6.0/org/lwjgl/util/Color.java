/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.util;

import java.io.Serializable;
import java.nio.ByteBuffer;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.WritableColor;

public final class Color
implements ReadableColor,
Serializable,
WritableColor {
    static final long serialVersionUID = 1L;
    private byte red;
    private byte green;
    private byte blue;
    private byte alpha;

    public Color() {
        this(0, 0, 0, 255);
    }

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(byte r, byte g, byte b) {
        this(r, g, b, -1);
    }

    public Color(int r, int g, int b, int a) {
        this.set(r, g, b, a);
    }

    public Color(byte r, byte g, byte b, byte a) {
        this.set(r, g, b, a);
    }

    public Color(ReadableColor c) {
        this.setColor(c);
    }

    public void set(int r, int g, int b, int a) {
        this.red = (byte)r;
        this.green = (byte)g;
        this.blue = (byte)b;
        this.alpha = (byte)a;
    }

    public void set(byte r, byte g, byte b, byte a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public void set(int r, int g, int b) {
        this.set(r, g, b, 255);
    }

    public void set(byte r, byte g, byte b) {
        this.set(r, g, b, (byte)-1);
    }

    public int getRed() {
        return this.red & 0xFF;
    }

    public int getGreen() {
        return this.green & 0xFF;
    }

    public int getBlue() {
        return this.blue & 0xFF;
    }

    public int getAlpha() {
        return this.alpha & 0xFF;
    }

    public void setRed(int red) {
        this.red = (byte)red;
    }

    public void setGreen(int green) {
        this.green = (byte)green;
    }

    public void setBlue(int blue) {
        this.blue = (byte)blue;
    }

    public void setAlpha(int alpha) {
        this.alpha = (byte)alpha;
    }

    public void setRed(byte red) {
        this.red = red;
    }

    public void setGreen(byte green) {
        this.green = green;
    }

    public void setBlue(byte blue) {
        this.blue = blue;
    }

    public void setAlpha(byte alpha) {
        this.alpha = alpha;
    }

    public String toString() {
        return "Color [" + this.getRed() + ", " + this.getGreen() + ", " + this.getBlue() + ", " + this.getAlpha() + "]";
    }

    public boolean equals(Object o) {
        return o != null && o instanceof ReadableColor && ((ReadableColor)o).getRed() == this.getRed() && ((ReadableColor)o).getGreen() == this.getGreen() && ((ReadableColor)o).getBlue() == this.getBlue() && ((ReadableColor)o).getAlpha() == this.getAlpha();
    }

    public int hashCode() {
        return this.red << 24 | this.green << 16 | this.blue << 8 | this.alpha;
    }

    public byte getAlphaByte() {
        return this.alpha;
    }

    public byte getBlueByte() {
        return this.blue;
    }

    public byte getGreenByte() {
        return this.green;
    }

    public byte getRedByte() {
        return this.red;
    }

    public void writeRGBA(ByteBuffer dest) {
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
        dest.put(this.alpha);
    }

    public void writeRGB(ByteBuffer dest) {
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
    }

    public void writeABGR(ByteBuffer dest) {
        dest.put(this.alpha);
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
    }

    public void writeARGB(ByteBuffer dest) {
        dest.put(this.alpha);
        dest.put(this.red);
        dest.put(this.green);
        dest.put(this.blue);
    }

    public void writeBGR(ByteBuffer dest) {
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
    }

    public void writeBGRA(ByteBuffer dest) {
        dest.put(this.blue);
        dest.put(this.green);
        dest.put(this.red);
        dest.put(this.alpha);
    }

    public void readRGBA(ByteBuffer src) {
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
        this.alpha = src.get();
    }

    public void readRGB(ByteBuffer src) {
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
    }

    public void readARGB(ByteBuffer src) {
        this.alpha = src.get();
        this.red = src.get();
        this.green = src.get();
        this.blue = src.get();
    }

    public void readBGRA(ByteBuffer src) {
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
        this.alpha = src.get();
    }

    public void readBGR(ByteBuffer src) {
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
    }

    public void readABGR(ByteBuffer src) {
        this.alpha = src.get();
        this.blue = src.get();
        this.green = src.get();
        this.red = src.get();
    }

    public void setColor(ReadableColor src) {
        this.red = src.getRedByte();
        this.green = src.getGreenByte();
        this.blue = src.getBlueByte();
        this.alpha = src.getAlphaByte();
    }

    public void fromHSB(float hue, float saturation, float brightness) {
        if (saturation == 0.0f) {
            this.green = this.blue = (byte)(brightness * 255.0f + 0.5f);
            this.red = this.blue;
        } else {
            float f3 = (hue - (float)Math.floor(hue)) * 6.0f;
            float f4 = f3 - (float)Math.floor(f3);
            float f5 = brightness * (1.0f - saturation);
            float f6 = brightness * (1.0f - saturation * f4);
            float f7 = brightness * (1.0f - saturation * (1.0f - f4));
            switch ((int)f3) {
                case 0: {
                    this.red = (byte)(brightness * 255.0f + 0.5f);
                    this.green = (byte)(f7 * 255.0f + 0.5f);
                    this.blue = (byte)(f5 * 255.0f + 0.5f);
                    break;
                }
                case 1: {
                    this.red = (byte)(f6 * 255.0f + 0.5f);
                    this.green = (byte)(brightness * 255.0f + 0.5f);
                    this.blue = (byte)(f5 * 255.0f + 0.5f);
                    break;
                }
                case 2: {
                    this.red = (byte)(f5 * 255.0f + 0.5f);
                    this.green = (byte)(brightness * 255.0f + 0.5f);
                    this.blue = (byte)(f7 * 255.0f + 0.5f);
                    break;
                }
                case 3: {
                    this.red = (byte)(f5 * 255.0f + 0.5f);
                    this.green = (byte)(f6 * 255.0f + 0.5f);
                    this.blue = (byte)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 4: {
                    this.red = (byte)(f7 * 255.0f + 0.5f);
                    this.green = (byte)(f5 * 255.0f + 0.5f);
                    this.blue = (byte)(brightness * 255.0f + 0.5f);
                    break;
                }
                case 5: {
                    this.red = (byte)(brightness * 255.0f + 0.5f);
                    this.green = (byte)(f5 * 255.0f + 0.5f);
                    this.blue = (byte)(f6 * 255.0f + 0.5f);
                }
            }
        }
    }

    public float[] toHSB(float[] dest) {
        float hue;
        int i1;
        int l;
        int r = this.getRed();
        int g = this.getGreen();
        int b = this.getBlue();
        if (dest == null) {
            dest = new float[3];
        }
        int n = l = r <= g ? g : r;
        if (b > l) {
            l = b;
        }
        int n2 = i1 = r >= g ? g : r;
        if (b < i1) {
            i1 = b;
        }
        float brightness = (float)l / 255.0f;
        float saturation = l != 0 ? (float)(l - i1) / (float)l : 0.0f;
        if (saturation == 0.0f) {
            hue = 0.0f;
        } else {
            float f3 = (float)(l - r) / (float)(l - i1);
            float f4 = (float)(l - g) / (float)(l - i1);
            float f5 = (float)(l - b) / (float)(l - i1);
            hue = r == l ? f5 - f4 : (g == l ? 2.0f + f3 - f5 : 4.0f + f4 - f3);
            if ((hue /= 6.0f) < 0.0f) {
                hue += 1.0f;
            }
        }
        dest[0] = hue;
        dest[1] = saturation;
        dest[2] = brightness;
        return dest;
    }
}

