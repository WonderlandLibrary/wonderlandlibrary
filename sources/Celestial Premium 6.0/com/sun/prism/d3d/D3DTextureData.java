/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.PixelFormat;
import com.sun.prism.d3d.D3DContext;
import com.sun.prism.d3d.D3DResource;
import com.sun.prism.impl.PrismTrace;

public class D3DTextureData
extends D3DResource.D3DRecord {
    private final long size;
    private final boolean isRTT;
    private final int samples;

    static long estimateSize(int n, int n2, PixelFormat pixelFormat) {
        return (long)n * (long)n2 * (long)pixelFormat.getBytesPerPixelUnit();
    }

    static long estimateRTSize(int n, int n2, boolean bl) {
        return (long)n * (long)n2 * 4L;
    }

    D3DTextureData(D3DContext d3DContext, long l, boolean bl, int n, int n2, PixelFormat pixelFormat, int n3) {
        super(d3DContext, l);
        this.size = bl ? D3DTextureData.estimateRTSize(n, n2, false) : D3DTextureData.estimateSize(n, n2, pixelFormat);
        this.isRTT = bl;
        this.samples = n3;
        if (bl) {
            PrismTrace.rttCreated(l, n, n2, this.size);
        } else {
            PrismTrace.textureCreated(l, n, n2, this.size);
        }
    }

    int getSamples() {
        return this.samples;
    }

    long getSize() {
        return this.size;
    }

    @Override
    protected void markDisposed() {
        long l = this.getResource();
        if (l != 0L) {
            if (this.isRTT) {
                PrismTrace.rttDisposed(l);
            } else {
                PrismTrace.textureDisposed(l);
            }
        }
        super.markDisposed();
    }

    @Override
    public void dispose() {
        long l = this.getResource();
        if (l != 0L) {
            if (this.isRTT) {
                PrismTrace.rttDisposed(l);
            } else {
                PrismTrace.textureDisposed(l);
            }
        }
        super.dispose();
    }
}

