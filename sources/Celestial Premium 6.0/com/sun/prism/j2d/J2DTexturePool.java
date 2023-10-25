/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.prism.PixelFormat;
import com.sun.prism.impl.BaseResourcePool;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import java.awt.image.BufferedImage;

class J2DTexturePool
extends BaseResourcePool<BufferedImage>
implements TextureResourcePool<BufferedImage> {
    static final J2DTexturePool instance = new J2DTexturePool();

    private static long maxVram() {
        long l = Runtime.getRuntime().maxMemory();
        long l2 = PrismSettings.maxVram;
        return Math.min(l / 4L, l2);
    }

    private static long targetVram() {
        long l = J2DTexturePool.maxVram();
        return Math.min(l / 2L, PrismSettings.targetVram);
    }

    private J2DTexturePool() {
        super(null, J2DTexturePool.targetVram(), J2DTexturePool.maxVram());
    }

    @Override
    public long used() {
        Runtime runtime = Runtime.getRuntime();
        long l = runtime.totalMemory() - runtime.freeMemory();
        long l2 = runtime.maxMemory() - l;
        long l3 = this.max() - this.managed();
        return this.max() - Math.min(l2, l3);
    }

    static long size(int n, int n2, int n3) {
        long l = (long)n * (long)n2;
        switch (n3) {
            case 5: {
                return l * 3L;
            }
            case 10: {
                return l;
            }
            case 3: {
                return l * 4L;
            }
        }
        throw new InternalError("Unrecognized BufferedImage");
    }

    @Override
    public long size(BufferedImage bufferedImage) {
        return J2DTexturePool.size(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
    }

    @Override
    public long estimateTextureSize(int n, int n2, PixelFormat pixelFormat) {
        int n3;
        switch (pixelFormat) {
            case BYTE_RGB: {
                n3 = 5;
                break;
            }
            case BYTE_GRAY: {
                n3 = 10;
                break;
            }
            case INT_ARGB_PRE: 
            case BYTE_BGRA_PRE: {
                n3 = 3;
                break;
            }
            default: {
                throw new InternalError("Unrecognized PixelFormat (" + pixelFormat + ")!");
            }
        }
        return J2DTexturePool.size(n, n2, n3);
    }

    @Override
    public long estimateRTTextureSize(int n, int n2, boolean bl) {
        return J2DTexturePool.size(n, n2, 3);
    }

    public String toString() {
        return "J2D Texture Pool";
    }
}

