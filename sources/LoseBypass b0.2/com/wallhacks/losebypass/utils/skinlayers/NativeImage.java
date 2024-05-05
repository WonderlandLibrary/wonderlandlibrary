/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.utils.skinlayers;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;

public final class NativeImage
implements AutoCloseable {
    private final Format format;
    private final int width;
    private final int height;
    private ByteBuffer buffer;
    private final int size;

    public NativeImage(int i, int j, boolean bl) {
        this(Format.RGBA, i, j, bl);
    }

    public NativeImage(Format format, int i, int j, boolean bl) {
        if (i <= 0) throw new IllegalArgumentException("Invalid texture size: " + i + "x" + j);
        if (j <= 0) {
            throw new IllegalArgumentException("Invalid texture size: " + i + "x" + j);
        }
        this.format = format;
        this.width = i;
        this.height = j;
        this.size = i * j * format.components();
        this.buffer = ByteBuffer.allocateDirect(this.size);
    }

    private boolean isOutsideBounds(int i, int j) {
        if (i < 0) return true;
        if (i >= this.width) return true;
        if (j < 0) return true;
        if (j >= this.height) return true;
        return false;
    }

    @Override
    public void close() {
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Format format() {
        return this.format;
    }

    public int getPixelRGBA(int i, int j) {
        if (this.format != Format.RGBA) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", new Object[]{this.format}));
        }
        if (this.isOutsideBounds(i, j)) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", i, j, this.width, this.height));
        }
        int l = (i + j * this.width) * 4;
        return this.buffer.getInt(l);
    }

    public void setPixelRGBA(int i, int j, int k) {
        if (this.format != Format.RGBA) {
            throw new IllegalArgumentException(String.format("getPixelRGBA only works on RGBA images; have %s", new Object[]{this.format}));
        }
        if (this.isOutsideBounds(i, j)) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", i, j, this.width, this.height));
        }
        int l = (i + j * this.width) * 4;
        this.buffer.putInt(l, k);
    }

    public byte getLuminanceOrAlpha(int i, int j) {
        if (!this.format.hasLuminanceOrAlpha()) {
            throw new IllegalArgumentException(String.format("no luminance or alpha in %s", new Object[]{this.format}));
        }
        if (this.isOutsideBounds(i, j)) {
            throw new IllegalArgumentException(String.format("(%s, %s) outside of image bounds (%s, %s)", i, j, this.width, this.height));
        }
        int k = (i + j * this.width) * this.format.components() + this.format.luminanceOrAlphaOffset() / 8;
        return this.buffer.get(k);
    }

    public void downloadTexture(int i, boolean bl) {
        this.format.setPackPixelStoreState();
        GL11.glGetTexImage((int)3553, (int)i, (int)this.format.glFormat(), (int)5121, (ByteBuffer)this.buffer);
        if (!bl) return;
        if (!this.format.hasAlpha()) return;
        int j = 0;
        while (j < this.getHeight()) {
            for (int k = 0; k < this.getWidth(); ++k) {
                this.setPixelRGBA(k, j, this.getPixelRGBA(k, j) | 255 << this.format.alphaOffset());
            }
            ++j;
        }
    }

    public static int getA(int i) {
        return i >> 24 & 0xFF;
    }

    public static int getR(int i) {
        return i >> 0 & 0xFF;
    }

    public static int getG(int i) {
        return i >> 8 & 0xFF;
    }

    public static int getB(int i) {
        return i >> 16 & 0xFF;
    }

    public static int combine(int i, int j, int k, int l) {
        return (i & 0xFF) << 24 | (j & 0xFF) << 16 | (k & 0xFF) << 8 | (l & 0xFF) << 0;
    }

    public static enum Format {
        RGBA(4, 6408, true, true, true, false, true, 0, 8, 16, 255, 24, true),
        RGB(3, 6407, true, true, true, false, false, 0, 8, 16, 255, 255, true),
        LUMINANCE_ALPHA(2, 33319, false, false, false, true, true, 255, 255, 255, 0, 8, true),
        LUMINANCE(1, 6403, false, false, false, true, false, 0, 0, 0, 0, 255, true);

        final int components;
        private final int glFormat;
        private final boolean hasRed;
        private final boolean hasGreen;
        private final boolean hasBlue;
        private final boolean hasLuminance;
        private final boolean hasAlpha;
        private final int redOffset;
        private final int greenOffset;
        private final int blueOffset;
        private final int luminanceOffset;
        private final int alphaOffset;
        private final boolean supportedByStb;

        private Format(int j, int k, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, int l, int m, int n2, int o, int p, boolean bl6) {
            this.components = j;
            this.glFormat = k;
            this.hasRed = bl;
            this.hasGreen = bl2;
            this.hasBlue = bl3;
            this.hasLuminance = bl4;
            this.hasAlpha = bl5;
            this.redOffset = l;
            this.greenOffset = m;
            this.blueOffset = n2;
            this.luminanceOffset = o;
            this.alphaOffset = p;
            this.supportedByStb = bl6;
        }

        public int components() {
            return this.components;
        }

        public void setPackPixelStoreState() {
            GL11.glPixelStorei((int)3333, (int)this.components());
        }

        public void setUnpackPixelStoreState() {
            GL11.glPixelStorei((int)3317, (int)this.components());
        }

        public int glFormat() {
            return this.glFormat;
        }

        public boolean hasRed() {
            return this.hasRed;
        }

        public boolean hasGreen() {
            return this.hasGreen;
        }

        public boolean hasBlue() {
            return this.hasBlue;
        }

        public boolean hasLuminance() {
            return this.hasLuminance;
        }

        public boolean hasAlpha() {
            return this.hasAlpha;
        }

        public int redOffset() {
            return this.redOffset;
        }

        public int greenOffset() {
            return this.greenOffset;
        }

        public int blueOffset() {
            return this.blueOffset;
        }

        public int luminanceOffset() {
            return this.luminanceOffset;
        }

        public int alphaOffset() {
            return this.alphaOffset;
        }

        public boolean hasLuminanceOrRed() {
            if (this.hasLuminance) return true;
            if (this.hasRed) return true;
            return false;
        }

        public boolean hasLuminanceOrGreen() {
            if (this.hasLuminance) return true;
            if (this.hasGreen) return true;
            return false;
        }

        public boolean hasLuminanceOrBlue() {
            if (this.hasLuminance) return true;
            if (this.hasBlue) return true;
            return false;
        }

        public boolean hasLuminanceOrAlpha() {
            if (this.hasLuminance) return true;
            if (this.hasAlpha) return true;
            return false;
        }

        public int luminanceOrRedOffset() {
            int n;
            if (this.hasLuminance) {
                n = this.luminanceOffset;
                return n;
            }
            n = this.redOffset;
            return n;
        }

        public int luminanceOrGreenOffset() {
            int n;
            if (this.hasLuminance) {
                n = this.luminanceOffset;
                return n;
            }
            n = this.greenOffset;
            return n;
        }

        public int luminanceOrBlueOffset() {
            int n;
            if (this.hasLuminance) {
                n = this.luminanceOffset;
                return n;
            }
            n = this.blueOffset;
            return n;
        }

        public int luminanceOrAlphaOffset() {
            int n;
            if (this.hasLuminance) {
                n = this.luminanceOffset;
                return n;
            }
            n = this.alphaOffset;
            return n;
        }

        public boolean supportedByStb() {
            return this.supportedByStb;
        }

        static Format getStbFormat(int i) {
            switch (i) {
                case 1: {
                    return LUMINANCE;
                }
                case 2: {
                    return LUMINANCE_ALPHA;
                }
                case 3: {
                    return RGB;
                }
            }
            return RGBA;
        }
    }

    public static enum InternalGlFormat {
        RGBA(6408),
        RGB(6407),
        RG(33319),
        RED(6403);

        private final int glFormat;

        private InternalGlFormat(int j) {
            this.glFormat = j;
        }

        public int glFormat() {
            return this.glFormat;
        }
    }
}

