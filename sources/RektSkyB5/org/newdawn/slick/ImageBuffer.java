/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.ImageData;

public class ImageBuffer
implements ImageData {
    private int width;
    private int height;
    private int texWidth;
    private int texHeight;
    private byte[] rawData;

    public ImageBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.texWidth = this.get2Fold(width);
        this.texHeight = this.get2Fold(height);
        this.rawData = new byte[this.texWidth * this.texHeight * 4];
    }

    public byte[] getRGBA() {
        return this.rawData;
    }

    public int getDepth() {
        return 32;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTexHeight() {
        return this.texHeight;
    }

    public int getTexWidth() {
        return this.texWidth;
    }

    public int getWidth() {
        return this.width;
    }

    public ByteBuffer getImageBufferData() {
        ByteBuffer scratch = BufferUtils.createByteBuffer(this.rawData.length);
        scratch.put(this.rawData);
        scratch.flip();
        return scratch;
    }

    public void setRGBA(int x2, int y2, int r2, int g2, int b2, int a2) {
        if (x2 < 0 || x2 >= this.width || y2 < 0 || y2 >= this.height) {
            throw new RuntimeException("Specified location: " + x2 + "," + y2 + " outside of image");
        }
        int ofs = (x2 + y2 * this.texWidth) * 4;
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            this.rawData[ofs] = (byte)b2;
            this.rawData[ofs + 1] = (byte)g2;
            this.rawData[ofs + 2] = (byte)r2;
            this.rawData[ofs + 3] = (byte)a2;
        } else {
            this.rawData[ofs] = (byte)r2;
            this.rawData[ofs + 1] = (byte)g2;
            this.rawData[ofs + 2] = (byte)b2;
            this.rawData[ofs + 3] = (byte)a2;
        }
    }

    public Image getImage() {
        return new Image(this);
    }

    public Image getImage(int filter) {
        return new Image(this, filter);
    }

    private int get2Fold(int fold) {
        int ret;
        for (ret = 2; ret < fold; ret *= 2) {
        }
        return ret;
    }
}

