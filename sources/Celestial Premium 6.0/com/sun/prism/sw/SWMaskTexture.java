/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.sw.SWResourceFactory;
import com.sun.prism.sw.SWTexture;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class SWMaskTexture
extends SWTexture {
    private byte[] data;

    SWMaskTexture(SWResourceFactory sWResourceFactory, Texture.WrapMode wrapMode, int n, int n2) {
        super(sWResourceFactory, wrapMode, n, n2);
    }

    SWMaskTexture(SWMaskTexture sWMaskTexture, Texture.WrapMode wrapMode) {
        super(sWMaskTexture, wrapMode);
        this.data = sWMaskTexture.data;
    }

    byte[] getDataNoClone() {
        return this.data;
    }

    @Override
    public PixelFormat getPixelFormat() {
        return PixelFormat.BYTE_ALPHA;
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        if (PrismSettings.debug) {
            System.out.println("MASK TEXTURE, Pixel format: " + pixelFormat + ", buffer: " + buffer);
            System.out.println("dstx:" + n + " dsty:" + n2);
            System.out.println("srcx:" + n3 + " srcy:" + n4 + " srcw:" + n5 + " srch:" + n6 + " srcscan: " + n7);
        }
        if (pixelFormat != PixelFormat.BYTE_ALPHA) {
            throw new IllegalArgumentException("SWMaskTexture supports BYTE_ALPHA format only.");
        }
        this.checkAllocation(n5, n6);
        this.physicalWidth = n5;
        this.physicalHeight = n6;
        this.allocate();
        ByteBuffer byteBuffer = (ByteBuffer)buffer;
        for (int i = 0; i < n6; ++i) {
            byteBuffer.position((n4 + i) * n7 + n3);
            byteBuffer.get(this.data, i * this.physicalWidth, n5);
        }
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        throw new UnsupportedOperationException("update6:unimp");
    }

    void checkAllocation(int n, int n2) {
        int n3;
        if (this.allocated && (n3 = n * n2) > this.data.length) {
            throw new IllegalArgumentException("SRCW * SRCH exceeds buffer length");
        }
    }

    @Override
    void allocateBuffer() {
        this.data = new byte[this.physicalWidth * this.physicalHeight];
    }

    @Override
    Texture createSharedLockedTexture(Texture.WrapMode wrapMode) {
        return new SWMaskTexture(this, wrapMode);
    }
}

