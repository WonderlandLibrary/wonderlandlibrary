/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.prism.Texture;
import java.nio.ByteBuffer;

public class MaskData {
    private ByteBuffer maskBuffer;
    private int originX;
    private int originY;
    private int width;
    private int height;

    public ByteBuffer getMaskBuffer() {
        return this.maskBuffer;
    }

    public int getOriginX() {
        return this.originX;
    }

    public int getOriginY() {
        return this.originY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void uploadToTexture(Texture texture, int n, int n2, boolean bl) {
        int n3 = this.width * texture.getPixelFormat().getBytesPerPixelUnit();
        texture.update(this.maskBuffer, texture.getPixelFormat(), n, n2, 0, 0, this.width, this.height, n3, bl);
    }

    public void update(ByteBuffer byteBuffer, int n, int n2, int n3, int n4) {
        this.maskBuffer = byteBuffer;
        this.originX = n;
        this.originY = n2;
        this.width = n3;
        this.height = n4;
    }

    public static MaskData create(byte[] arrby, int n, int n2, int n3, int n4) {
        MaskData maskData = new MaskData();
        maskData.update(ByteBuffer.wrap(arrby), n, n2, n3, n4);
        return maskData;
    }
}

