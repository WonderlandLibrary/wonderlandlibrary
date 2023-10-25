/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWTexture;

class SWTexturePool
extends BaseResourcePool<SWTexture>
implements TextureResourcePool<SWTexture> {
    static final SWTexturePool instance = new SWTexturePool();

    private static long maxVram() {
        long l = Runtime.getRuntime().maxMemory();
        long l2 = PrismSettings.maxVram;
        return Math.min(l / 4L, l2);
    }

    private static long targetVram() {
        long l = SWTexturePool.maxVram();
        return Math.min(l / 2L, PrismSettings.targetVram);
    }

    private SWTexturePool() {
        super(null, SWTexturePool.targetVram(), SWTexturePool.maxVram());
    }

    @Override
    public long used() {
        return 0L;
    }

    @Override
    public long size(SWTexture sWTexture) {
        long l = sWTexture.getPhysicalWidth();
        l *= (long)sWTexture.getPhysicalHeight();
        if (sWTexture instanceof SWArgbPreTexture) {
            l *= 4L;
        }
        return l;
    }

    @Override
    public long estimateTextureSize(int n, int n2, PixelFormat pixelFormat) {
        switch (pixelFormat) {
            case BYTE_ALPHA: {
                return (long)n * (long)n2;
            }
        }
        return (long)n * (long)n2 * 4L;
    }

    @Override
    public long estimateRTTextureSize(int n, int n2, boolean bl) {
        return (long)n * (long)n2 * 4L;
    }
}

