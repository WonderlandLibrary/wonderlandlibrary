/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.prism.PixelFormat;
import com.sun.prism.d3d.D3DTextureData;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;

class D3DVramPool
extends BaseResourcePool<D3DTextureData>
implements TextureResourcePool<D3DTextureData> {
    public static final D3DVramPool instance = new D3DVramPool();

    private D3DVramPool() {
        super(PrismSettings.targetVram, PrismSettings.maxVram);
    }

    @Override
    public long size(D3DTextureData d3DTextureData) {
        return d3DTextureData.getSize();
    }

    @Override
    public long estimateTextureSize(int n, int n2, PixelFormat pixelFormat) {
        return (long)n * (long)n2 * (long)pixelFormat.getBytesPerPixelUnit();
    }

    @Override
    public long estimateRTTextureSize(int n, int n2, boolean bl) {
        return (long)n * (long)n2 * 4L;
    }

    public String toString() {
        return "D3D Vram Pool";
    }
}

