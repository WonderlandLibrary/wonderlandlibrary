/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DGraphics;
import com.sun.prism.d3d.D3DRenderTarget;
import com.sun.prism.d3d.D3DResourceFactory;
import com.sun.prism.d3d.D3DTexture;
import com.sun.prism.d3d.D3DTextureData;
import com.sun.prism.d3d.D3DTextureResource;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class D3DRTTexture
extends D3DTexture
implements D3DRenderTarget,
RTTexture,
ReadbackRenderTarget {
    private boolean opaque = false;

    D3DRTTexture(D3DContext d3DContext, Texture.WrapMode wrapMode, long l, int n, int n2, int n3, int n4) {
        super(d3DContext, PixelFormat.INT_ARGB_PRE, wrapMode, l, n, n2, n3, n4, true);
    }

    D3DRTTexture(D3DContext d3DContext, Texture.WrapMode wrapMode, long l, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        super(d3DContext, PixelFormat.INT_ARGB_PRE, wrapMode, l, n, n2, n3, n4, n5, n6, true, n7, false);
    }

    @Override
    public Texture getBackBuffer() {
        return this;
    }

    @Override
    public long getResourceHandle() {
        return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getResource();
    }

    @Override
    public Graphics createGraphics() {
        return D3DGraphics.create(this, this.getContext());
    }

    @Override
    public int[] getPixels() {
        return null;
    }

    @Override
    public boolean readPixels(Buffer buffer, int n, int n2, int n3, int n4) {
        if (n != this.getContentX() || n2 != this.getContentY() || n3 != this.getContentWidth() || n4 != this.getContentHeight()) {
            throw new IllegalArgumentException("reading subtexture not yet supported!");
        }
        return this.readPixels(buffer);
    }

    @Override
    public boolean readPixels(Buffer buffer) {
        D3DContext d3DContext = this.getContext();
        if (d3DContext.isDisposed()) {
            return false;
        }
        d3DContext.flushVertexBuffer();
        long l = this.getContext().getContextHandle();
        int n = 0;
        if (buffer instanceof ByteBuffer) {
            ByteBuffer byteBuffer = (ByteBuffer)buffer;
            byte[] arrby = byteBuffer.hasArray() ? byteBuffer.array() : null;
            long l2 = byteBuffer.capacity();
            n = D3DResourceFactory.nReadPixelsB(l, this.getNativeSourceHandle(), l2, buffer, arrby, this.getContentWidth(), this.getContentHeight());
        } else if (buffer instanceof IntBuffer) {
            IntBuffer intBuffer = (IntBuffer)buffer;
            int[] arrn = intBuffer.hasArray() ? intBuffer.array() : null;
            long l3 = intBuffer.capacity() * 4;
            n = D3DResourceFactory.nReadPixelsI(l, this.getNativeSourceHandle(), l3, buffer, arrn, this.getContentWidth(), this.getContentHeight());
        } else {
            throw new IllegalArgumentException("Buffer of this type is not supported: " + buffer);
        }
        return d3DContext.validatePresent(n);
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.getContext().getAssociatedScreen();
    }

    @Override
    public void update(Image image) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n, int n2) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Image image, int n, int n2, int n3, int n4, boolean bl) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        throw new UnsupportedOperationException("update() not supported for RTTextures");
    }

    @Override
    public void setOpaque(boolean bl) {
        this.opaque = bl;
    }

    @Override
    public boolean isOpaque() {
        return this.opaque;
    }

    @Override
    public boolean isVolatile() {
        return this.getContext().isRTTVolatile();
    }

    @Override
    public boolean isMSAA() {
        return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getSamples() != 0;
    }
}

