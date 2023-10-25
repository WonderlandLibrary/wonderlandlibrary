/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

import com.sun.pisces.Surface;
import com.sun.prism.impl.Disposer;

public abstract class AbstractSurface
implements Surface {
    private long nativePtr = 0L;
    private int width;
    private int height;

    AbstractSurface(int n, int n2) {
        if (n < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        int n3 = 32 - Integer.numberOfLeadingZeros(n) + 32 - Integer.numberOfLeadingZeros(n2);
        if (n3 > 31) {
            throw new IllegalArgumentException("WIDTH * HEIGHT is too large");
        }
        this.width = n;
        this.height = n2;
    }

    protected void addDisposerRecord() {
        Disposer.addRecord(this, new AbstractSurfaceDisposerRecord(this.nativePtr));
    }

    @Override
    public final void getRGB(int[] arrn, int n, int n2, int n3, int n4, int n5, int n6) {
        this.rgbCheck(arrn.length, n, n2, n3, n4, n5, n6);
        this.getRGBImpl(arrn, n, n2, n3, n4, n5, n6);
    }

    private native void getRGBImpl(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    @Override
    public final void setRGB(int[] arrn, int n, int n2, int n3, int n4, int n5, int n6) {
        this.rgbCheck(arrn.length, n, n2, n3, n4, n5, n6);
        this.setRGBImpl(arrn, n, n2, n3, n4, n5, n6);
    }

    private native void setRGBImpl(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    private void rgbCheck(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (n4 < 0 || n4 >= this.width) {
            throw new IllegalArgumentException("X is out of surface");
        }
        if (n5 < 0 || n5 >= this.height) {
            throw new IllegalArgumentException("Y is out of surface");
        }
        if (n6 < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (n7 < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        if (n4 + n6 > this.width) {
            throw new IllegalArgumentException("X+WIDTH is out of surface");
        }
        if (n5 + n7 > this.height) {
            throw new IllegalArgumentException("Y+HEIGHT is out of surface");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("OFFSET must be positive");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("SCAN-LENGTH must be positive");
        }
        if (n3 < n6) {
            throw new IllegalArgumentException("SCAN-LENGTH must be >= WIDTH");
        }
        int n8 = 32 - Integer.numberOfLeadingZeros(n3) + 32 - Integer.numberOfLeadingZeros(n7);
        if (n8 > 31) {
            throw new IllegalArgumentException("SCAN-LENGTH * HEIGHT is too large");
        }
        if (n2 + n3 * (n7 - 1) + n6 > n) {
            throw new IllegalArgumentException("STRIDE * HEIGHT exceeds length of data");
        }
    }

    private static native void disposeNative(long var0);

    @Override
    public final int getWidth() {
        return this.width;
    }

    @Override
    public final int getHeight() {
        return this.height;
    }

    private static class AbstractSurfaceDisposerRecord
    implements Disposer.Record {
        private long nativeHandle;

        AbstractSurfaceDisposerRecord(long l) {
            this.nativeHandle = l;
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                AbstractSurface.disposeNative(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

