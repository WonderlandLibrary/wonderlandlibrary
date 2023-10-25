/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DContextSource;
import com.sun.prism.d3d.D3DResourceFactory;
import com.sun.prism.d3d.D3DTextureData;
import com.sun.prism.d3d.D3DTextureResource;
import com.sun.prism.impl.BaseTexture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class D3DTexture
extends BaseTexture<D3DTextureResource>
implements D3DContextSource {
    D3DTexture(D3DContext d3DContext, PixelFormat pixelFormat, Texture.WrapMode wrapMode, long l, int n, int n2, int n3, int n4, boolean bl) {
        this(d3DContext, pixelFormat, wrapMode, l, n, n2, 0, 0, n3, n4, bl, 0, false);
    }

    D3DTexture(D3DContext d3DContext, PixelFormat pixelFormat, Texture.WrapMode wrapMode, long l, int n, int n2, int n3, int n4, int n5, int n6, boolean bl, int n7, boolean bl2) {
        super(new D3DTextureResource(new D3DTextureData(d3DContext, l, bl, n, n2, pixelFormat, n7)), pixelFormat, wrapMode, n, n2, n3, n4, n5, n6, n, n2, bl2);
    }

    D3DTexture(D3DTexture d3DTexture, Texture.WrapMode wrapMode) {
        super(d3DTexture, wrapMode, false);
    }

    @Override
    protected Texture createSharedTexture(Texture.WrapMode wrapMode) {
        return new D3DTexture(this, wrapMode);
    }

    public long getNativeSourceHandle() {
        return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getResource();
    }

    public long getNativeTextureObject() {
        return D3DResourceFactory.nGetNativeTextureObject(this.getNativeSourceHandle());
    }

    @Override
    public D3DContext getContext() {
        return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getContext();
    }

    @Override
    public void update(MediaFrame mediaFrame, boolean bl) {
        PixelFormat pixelFormat;
        if (mediaFrame.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            throw new IllegalArgumentException("Unsupported format " + mediaFrame.getPixelFormat());
        }
        mediaFrame.holdFrame();
        ByteBuffer byteBuffer = mediaFrame.getBufferForPlane(0);
        D3DContext d3DContext = this.getContext();
        if (!bl) {
            d3DContext.flushVertexBuffer();
        }
        int n = (pixelFormat = mediaFrame.getPixelFormat()).getDataType() == PixelFormat.DataType.INT ? D3DResourceFactory.nUpdateTextureI(d3DContext.getContextHandle(), this.getNativeSourceHandle(), byteBuffer.asIntBuffer(), null, 0, 0, 0, 0, mediaFrame.getEncodedWidth(), mediaFrame.getEncodedHeight(), mediaFrame.strideForPlane(0)) : D3DResourceFactory.nUpdateTextureB(d3DContext.getContextHandle(), this.getNativeSourceHandle(), byteBuffer, null, pixelFormat.ordinal(), 0, 0, 0, 0, mediaFrame.getEncodedWidth(), mediaFrame.getEncodedHeight(), mediaFrame.strideForPlane(0));
        D3DContext.validate(n);
        mediaFrame.releaseFrame();
    }

    @Override
    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
        this.checkUpdateParams(buffer, pixelFormat, n, n2, n3, n4, n5, n6, n7);
        if (!bl) {
            this.getContext().flushVertexBuffer();
        }
        int n8 = this.getContentX();
        int n9 = this.getContentY();
        int n10 = this.getContentWidth();
        int n11 = this.getContentHeight();
        int n12 = this.getPhysicalWidth();
        int n13 = this.getPhysicalHeight();
        this.update(buffer, pixelFormat, n8 + n, n9 + n2, n3, n4, n5, n6, n7);
        switch (this.getWrapMode()) {
            case CLAMP_TO_EDGE: {
                break;
            }
            case CLAMP_TO_EDGE_SIMULATED: {
                boolean bl2;
                boolean bl3 = n10 < n12 && n + n5 == n10;
                boolean bl4 = bl2 = n11 < n13 && n2 + n6 == n11;
                if (bl3) {
                    this.update(buffer, pixelFormat, n8 + n10, n9 + n2, n3 + n5 - 1, n4, 1, n6, n7);
                }
                if (!bl2) break;
                this.update(buffer, pixelFormat, n8 + n, n9 + n11, n3, n4 + n6 - 1, n5, 1, n7);
                if (!bl3) break;
                this.update(buffer, pixelFormat, n8 + n10, n9 + n11, n3 + n5 - 1, n4 + n6 - 1, 1, 1, n7);
                break;
            }
            case REPEAT: {
                break;
            }
            case REPEAT_SIMULATED: {
                boolean bl5;
                boolean bl6 = n10 < n12 && n == 0;
                boolean bl7 = bl5 = n11 < n13 && n2 == 0;
                if (bl6) {
                    this.update(buffer, pixelFormat, n8 + n10, n9 + n2, n3, n4, 1, n6, n7);
                }
                if (!bl5) break;
                this.update(buffer, pixelFormat, n8 + n, n9 + n11, n3, n4, n5, 1, n7);
                if (!bl6) break;
                this.update(buffer, pixelFormat, n8 + n10, n9 + n11, n3, n4, 1, 1, n7);
                break;
            }
        }
    }

    public void update(Buffer buffer, PixelFormat pixelFormat, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        D3DContext d3DContext = this.getContext();
        if (pixelFormat.getDataType() == PixelFormat.DataType.INT) {
            IntBuffer intBuffer = (IntBuffer)buffer;
            int[] arrn = intBuffer.hasArray() ? intBuffer.array() : null;
            n8 = D3DResourceFactory.nUpdateTextureI(d3DContext.getContextHandle(), this.getNativeSourceHandle(), intBuffer, arrn, n, n2, n3, n4, n5, n6, n7);
        } else if (pixelFormat.getDataType() == PixelFormat.DataType.FLOAT) {
            FloatBuffer floatBuffer = (FloatBuffer)buffer;
            float[] arrf = floatBuffer.hasArray() ? floatBuffer.array() : null;
            n8 = D3DResourceFactory.nUpdateTextureF(d3DContext.getContextHandle(), this.getNativeSourceHandle(), floatBuffer, arrf, n, n2, n3, n4, n5, n6, n7);
        } else {
            ByteBuffer byteBuffer = (ByteBuffer)buffer;
            byteBuffer.rewind();
            byte[] arrby = byteBuffer.hasArray() ? byteBuffer.array() : null;
            n8 = D3DResourceFactory.nUpdateTextureB(d3DContext.getContextHandle(), this.getNativeSourceHandle(), byteBuffer, arrby, pixelFormat.ordinal(), n, n2, n3, n4, n5, n6, n7);
        }
        D3DContext.validate(n8);
    }
}

