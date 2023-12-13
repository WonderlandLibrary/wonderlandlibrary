/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.opengl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.LoadableImageData;

public class TGAImageData
implements LoadableImageData {
    private int texWidth;
    private int texHeight;
    private int width;
    private int height;
    private short pixelDepth;

    private short flipEndian(short signedShort) {
        int input = signedShort & 0xFFFF;
        return (short)(input << 8 | (input & 0xFF00) >>> 8);
    }

    public int getDepth() {
        return this.pixelDepth;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTexWidth() {
        return this.texWidth;
    }

    public int getTexHeight() {
        return this.texHeight;
    }

    public ByteBuffer loadImage(InputStream fis) throws IOException {
        return this.loadImage(fis, true, null);
    }

    public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
        return this.loadImage(fis, flipped, false, transparent);
    }

    public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
        int ofs;
        int j2;
        int i2;
        if (transparent != null) {
            forceAlpha = true;
        }
        byte red = 0;
        byte green = 0;
        byte blue = 0;
        int alpha = 0;
        BufferedInputStream bis = new BufferedInputStream(fis, 100000);
        DataInputStream dis = new DataInputStream(bis);
        short idLength = (short)dis.read();
        short colorMapType = (short)dis.read();
        short imageType = (short)dis.read();
        short cMapStart = this.flipEndian(dis.readShort());
        short cMapLength = this.flipEndian(dis.readShort());
        short cMapDepth = (short)dis.read();
        short xOffset = this.flipEndian(dis.readShort());
        short yOffset = this.flipEndian(dis.readShort());
        if (imageType != 2) {
            throw new IOException("Slick only supports uncompressed RGB(A) TGA images");
        }
        this.width = this.flipEndian(dis.readShort());
        this.height = this.flipEndian(dis.readShort());
        this.pixelDepth = (short)dis.read();
        if (this.pixelDepth == 32) {
            forceAlpha = false;
        }
        this.texWidth = this.get2Fold(this.width);
        this.texHeight = this.get2Fold(this.height);
        short imageDescriptor = (short)dis.read();
        if ((imageDescriptor & 0x20) == 0) {
            boolean bl = flipped = !flipped;
        }
        if (idLength > 0) {
            bis.skip(idLength);
        }
        byte[] rawData = null;
        if (this.pixelDepth == 32 || forceAlpha) {
            this.pixelDepth = (short)32;
            rawData = new byte[this.texWidth * this.texHeight * 4];
        } else if (this.pixelDepth == 24) {
            rawData = new byte[this.texWidth * this.texHeight * 3];
        } else {
            throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
        }
        if (this.pixelDepth == 24) {
            if (flipped) {
                for (i2 = this.height - 1; i2 >= 0; --i2) {
                    for (j2 = 0; j2 < this.width; ++j2) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        ofs = (j2 + i2 * this.texWidth) * 3;
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                    }
                }
            } else {
                for (i2 = 0; i2 < this.height; ++i2) {
                    for (j2 = 0; j2 < this.width; ++j2) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        ofs = (j2 + i2 * this.texWidth) * 3;
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                    }
                }
            }
        } else if (this.pixelDepth == 32) {
            if (flipped) {
                for (i2 = this.height - 1; i2 >= 0; --i2) {
                    for (j2 = 0; j2 < this.width; ++j2) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        alpha = forceAlpha ? -1 : (int)dis.readByte();
                        ofs = (j2 + i2 * this.texWidth) * 4;
                        rawData[ofs] = red;
                        rawData[ofs + 1] = green;
                        rawData[ofs + 2] = blue;
                        rawData[ofs + 3] = alpha;
                        if (alpha != 0) continue;
                        rawData[ofs + 2] = 0;
                        rawData[ofs + 1] = 0;
                        rawData[ofs] = 0;
                    }
                }
            } else {
                for (i2 = 0; i2 < this.height; ++i2) {
                    for (j2 = 0; j2 < this.width; ++j2) {
                        blue = dis.readByte();
                        green = dis.readByte();
                        red = dis.readByte();
                        alpha = forceAlpha ? -1 : (int)dis.readByte();
                        ofs = (j2 + i2 * this.texWidth) * 4;
                        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
                            rawData[ofs] = red;
                            rawData[ofs + 1] = green;
                            rawData[ofs + 2] = blue;
                            rawData[ofs + 3] = alpha;
                        } else {
                            rawData[ofs] = red;
                            rawData[ofs + 1] = green;
                            rawData[ofs + 2] = blue;
                            rawData[ofs + 3] = alpha;
                        }
                        if (alpha != 0) continue;
                        rawData[ofs + 2] = 0;
                        rawData[ofs + 1] = 0;
                        rawData[ofs] = 0;
                    }
                }
            }
        }
        fis.close();
        if (transparent != null) {
            for (i2 = 0; i2 < rawData.length; i2 += 4) {
                boolean match = true;
                for (int c2 = 0; c2 < 3; ++c2) {
                    if (rawData[i2 + c2] == transparent[c2]) continue;
                    match = false;
                }
                if (!match) continue;
                rawData[i2 + 3] = 0;
            }
        }
        ByteBuffer scratch = BufferUtils.createByteBuffer(rawData.length);
        scratch.put(rawData);
        int perPixel = this.pixelDepth / 8;
        if (this.height < this.texHeight - 1) {
            int topOffset = (this.texHeight - 1) * (this.texWidth * perPixel);
            int bottomOffset = (this.height - 1) * (this.texWidth * perPixel);
            for (int x2 = 0; x2 < this.texWidth * perPixel; ++x2) {
                scratch.put(topOffset + x2, scratch.get(x2));
                scratch.put(bottomOffset + this.texWidth * perPixel + x2, scratch.get(this.texWidth * perPixel + x2));
            }
        }
        if (this.width < this.texWidth - 1) {
            for (int y2 = 0; y2 < this.texHeight; ++y2) {
                for (int i3 = 0; i3 < perPixel; ++i3) {
                    scratch.put((y2 + 1) * (this.texWidth * perPixel) - perPixel + i3, scratch.get(y2 * (this.texWidth * perPixel) + i3));
                    scratch.put(y2 * (this.texWidth * perPixel) + this.width * perPixel + i3, scratch.get(y2 * (this.texWidth * perPixel) + (this.width - 1) * perPixel + i3));
                }
            }
        }
        scratch.flip();
        return scratch;
    }

    private int get2Fold(int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {
        }
        return ret;
    }

    public ByteBuffer getImageBufferData() {
        throw new RuntimeException("TGAImageData doesn't store it's image.");
    }

    public void configureEdging(boolean edging) {
    }
}

