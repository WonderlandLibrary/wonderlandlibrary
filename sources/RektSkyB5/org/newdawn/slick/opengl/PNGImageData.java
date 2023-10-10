/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.LoadableImageData;
import org.newdawn.slick.opengl.PNGDecoder;

public class PNGImageData
implements LoadableImageData {
    private int width;
    private int height;
    private int texHeight;
    private int texWidth;
    private PNGDecoder decoder;
    private int bitDepth;
    private ByteBuffer scratch;

    public int getDepth() {
        return this.bitDepth;
    }

    public ByteBuffer getImageBufferData() {
        return this.scratch;
    }

    public int getTexHeight() {
        return this.texHeight;
    }

    public int getTexWidth() {
        return this.texWidth;
    }

    public ByteBuffer loadImage(InputStream fis) throws IOException {
        return this.loadImage(fis, false, null);
    }

    public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }

    public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
        if (transparent != null) {
            forceAlpha = true;
            throw new IOException("Transparent color not support in custom PNG Decoder");
        }
        PNGDecoder decoder = new PNGDecoder(fis);
        if (!decoder.isRGB()) {
            throw new IOException("Only RGB formatted images are supported by the PNGLoader");
        }
        this.width = decoder.getWidth();
        this.height = decoder.getHeight();
        this.texWidth = this.get2Fold(this.width);
        this.texHeight = this.get2Fold(this.height);
        int perPixel = decoder.hasAlpha() ? 4 : 3;
        this.bitDepth = decoder.hasAlpha() ? 32 : 24;
        this.scratch = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * perPixel);
        decoder.decode(this.scratch, this.texWidth * perPixel, perPixel == 4 ? PNGDecoder.RGBA : PNGDecoder.RGB);
        if (this.height < this.texHeight - 1) {
            int topOffset = (this.texHeight - 1) * (this.texWidth * perPixel);
            int bottomOffset = (this.height - 1) * (this.texWidth * perPixel);
            for (int x2 = 0; x2 < this.texWidth; ++x2) {
                for (int i2 = 0; i2 < perPixel; ++i2) {
                    this.scratch.put(topOffset + x2 + i2, this.scratch.get(x2 + i2));
                    this.scratch.put(bottomOffset + this.texWidth * perPixel + x2 + i2, this.scratch.get(bottomOffset + x2 + i2));
                }
            }
        }
        if (this.width < this.texWidth - 1) {
            for (int y2 = 0; y2 < this.texHeight; ++y2) {
                for (int i3 = 0; i3 < perPixel; ++i3) {
                    this.scratch.put((y2 + 1) * (this.texWidth * perPixel) - perPixel + i3, this.scratch.get(y2 * (this.texWidth * perPixel) + i3));
                    this.scratch.put(y2 * (this.texWidth * perPixel) + this.width * perPixel + i3, this.scratch.get(y2 * (this.texWidth * perPixel) + (this.width - 1) * perPixel + i3));
                }
            }
        }
        if (!decoder.hasAlpha() && forceAlpha) {
            ByteBuffer temp = BufferUtils.createByteBuffer(this.texWidth * this.texHeight * 4);
            for (int x3 = 0; x3 < this.texWidth; ++x3) {
                for (int y3 = 0; y3 < this.texHeight; ++y3) {
                    int srcOffset = y3 * 3 + x3 * this.texHeight * 3;
                    int dstOffset = y3 * 4 + x3 * this.texHeight * 4;
                    temp.put(dstOffset, this.scratch.get(srcOffset));
                    temp.put(dstOffset + 1, this.scratch.get(srcOffset + 1));
                    temp.put(dstOffset + 2, this.scratch.get(srcOffset + 2));
                    if (x3 < this.getHeight() && y3 < this.getWidth()) {
                        temp.put(dstOffset + 3, (byte)-1);
                        continue;
                    }
                    temp.put(dstOffset + 3, (byte)0);
                }
            }
            this.bitDepth = 32;
            this.scratch = temp;
        }
        if (transparent != null) {
            for (int i4 = 0; i4 < this.texWidth * this.texHeight * 4; i4 += 4) {
                boolean match = true;
                for (int c2 = 0; c2 < 3; ++c2) {
                    if (this.toInt(this.scratch.get(i4 + c2)) == transparent[c2]) continue;
                    match = false;
                }
                if (!match) continue;
                this.scratch.put(i4 + 3, (byte)0);
            }
        }
        this.scratch.position(0);
        return this.scratch;
    }

    private int toInt(byte b2) {
        if (b2 < 0) {
            return 256 + b2;
        }
        return b2;
    }

    private int get2Fold(int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {
        }
        return ret;
    }

    public void configureEdging(boolean edging) {
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

