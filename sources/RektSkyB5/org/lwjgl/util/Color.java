/*
 * Decompiled with CFR 0.152.
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

    public Color(int r2, int g2, int b2) {
        this(r2, g2, b2, 255);
    }

    public Color(byte r2, byte g2, byte b2) {
        this(r2, g2, b2, -1);
    }

    public Color(int r2, int g2, int b2, int a2) {
        this.set(r2, g2, b2, a2);
    }

    public Color(byte r2, byte g2, byte b2, byte a2) {
        this.set(r2, g2, b2, a2);
    }

    public Color(ReadableColor c2) {
        this.setColor(c2);
    }

    public void set(int r2, int g2, int b2, int a2) {
        this.red = (byte)r2;
        this.green = (byte)g2;
        this.blue = (byte)b2;
        this.alpha = (byte)a2;
    }

    public void set(byte r2, byte g2, byte b2, byte a2) {
        this.red = r2;
        this.green = g2;
        this.blue = b2;
        this.alpha = a2;
    }

    public void set(int r2, int g2, int b2) {
        this.set(r2, g2, b2, 255);
    }

    public void set(byte r2, byte g2, byte b2) {
        this.set(r2, g2, b2, (byte)-1);
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

    public boolean equals(Object o2) {
        return o2 != null && o2 instanceof ReadableColor && ((ReadableColor)o2).getRed() == this.getRed() && ((ReadableColor)o2).getGreen() == this.getGreen() && ((ReadableColor)o2).getBlue() == this.getBlue() && ((ReadableColor)o2).getAlpha() == this.getAlpha();
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
        int l2;
        int r2 = this.getRed();
        int g2 = this.getGreen();
        int b2 = this.getBlue();
        if (dest == null) {
            dest = new float[3];
        }
        int n2 = l2 = r2 <= g2 ? g2 : r2;
        if (b2 > l2) {
            l2 = b2;
        }
        int n3 = i1 = r2 >= g2 ? g2 : r2;
        if (b2 < i1) {
            i1 = b2;
        }
        float brightness = (float)l2 / 255.0f;
        float saturation = l2 != 0 ? (float)(l2 - i1) / (float)l2 : 0.0f;
        if (saturation == 0.0f) {
            hue = 0.0f;
        } else {
            float f3 = (float)(l2 - r2) / (float)(l2 - i1);
            float f4 = (float)(l2 - g2) / (float)(l2 - i1);
            float f5 = (float)(l2 - b2) / (float)(l2 - i1);
            hue = r2 == l2 ? f5 - f4 : (g2 == l2 ? 2.0f + f3 - f5 : 4.0f + f4 - f3);
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

