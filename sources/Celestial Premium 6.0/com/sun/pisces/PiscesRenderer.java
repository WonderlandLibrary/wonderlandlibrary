/*
 * Decompiled with CFR 0.150.
 */
package com.sun.pisces;

import com.sun.pisces.AbstractSurface;
import com.sun.pisces.GradientColorMap;
import com.sun.pisces.Transform6;
import com.sun.prism.impl.Disposer;

public final class PiscesRenderer {
    public static final int ARC_OPEN = 0;
    public static final int ARC_CHORD = 1;
    public static final int ARC_PIE = 2;
    private long nativePtr = 0L;
    private AbstractSurface surface;

    public PiscesRenderer(AbstractSurface abstractSurface) {
        this.surface = abstractSurface;
        this.initialize();
        Disposer.addRecord(this, new PiscesRendererDisposerRecord(this.nativePtr));
    }

    private native void initialize();

    public void setColor(int n, int n2, int n3, int n4) {
        this.checkColorRange(n, "RED");
        this.checkColorRange(n2, "GREEN");
        this.checkColorRange(n3, "BLUE");
        this.checkColorRange(n4, "ALPHA");
        this.setColorImpl(n, n2, n3, n4);
    }

    private native void setColorImpl(int var1, int var2, int var3, int var4);

    private void checkColorRange(int n, String string) {
        if (n < 0 || n > 255) {
            throw new IllegalArgumentException(string + " color component is out of range");
        }
    }

    public void setColor(int n, int n2, int n3) {
        this.setColor(n, n2, n3, 255);
    }

    public void setCompositeRule(int n) {
        if (n != 0 && n != 1 && n != 2) {
            throw new IllegalArgumentException("Invalid value for Composite-Rule");
        }
        this.setCompositeRuleImpl(n);
    }

    private native void setCompositeRuleImpl(int var1);

    private native void setLinearGradientImpl(int var1, int var2, int var3, int var4, int[] var5, int var6, Transform6 var7);

    public void setLinearGradient(int n, int n2, int n3, int n4, int[] arrn, int[] arrn2, int n5, Transform6 transform6) {
        GradientColorMap gradientColorMap = new GradientColorMap(arrn, arrn2, n5);
        this.setLinearGradientImpl(n, n2, n3, n4, gradientColorMap.colors, n5, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setLinearGradient(int n, int n2, int n3, int n4, GradientColorMap gradientColorMap, Transform6 transform6) {
        this.setLinearGradientImpl(n, n2, n3, n4, gradientColorMap.colors, gradientColorMap.cycleMethod, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setLinearGradient(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int[] arrn = new int[]{0, 65536};
        int[] arrn2 = new int[]{n3, n6};
        Transform6 transform6 = new Transform6(65536, 0, 0, 65536, 0, 0);
        this.setLinearGradient(n, n2, n4, n5, arrn, arrn2, n7, transform6);
    }

    private native void setRadialGradientImpl(int var1, int var2, int var3, int var4, int var5, int[] var6, int var7, Transform6 var8);

    public void setRadialGradient(int n, int n2, int n3, int n4, int n5, int[] arrn, int[] arrn2, int n6, Transform6 transform6) {
        GradientColorMap gradientColorMap = new GradientColorMap(arrn, arrn2, n6);
        this.setRadialGradientImpl(n, n2, n3, n4, n5, gradientColorMap.colors, n6, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setRadialGradient(int n, int n2, int n3, int n4, int n5, GradientColorMap gradientColorMap, Transform6 transform6) {
        this.setRadialGradientImpl(n, n2, n3, n4, n5, gradientColorMap.colors, gradientColorMap.cycleMethod, transform6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : transform6);
    }

    public void setTexture(int n, int[] arrn, int n2, int n3, int n4, Transform6 transform6, boolean bl, boolean bl2, boolean bl3) {
        this.inputImageCheck(n2, n3, 0, n4, arrn.length);
        this.setTextureImpl(n, arrn, n2, n3, n4, transform6, bl, bl2, bl3);
    }

    private native void setTextureImpl(int var1, int[] var2, int var3, int var4, int var5, Transform6 var6, boolean var7, boolean var8, boolean var9);

    public void setClip(int n, int n2, int n3, int n4) {
        int n5 = Math.max(n, 0);
        int n6 = Math.max(n2, 0);
        int n7 = Math.min(n + n3, this.surface.getWidth());
        int n8 = Math.min(n2 + n4, this.surface.getHeight());
        this.setClipImpl(n5, n6, n7 - n5, n8 - n6);
    }

    private native void setClipImpl(int var1, int var2, int var3, int var4);

    public void resetClip() {
        this.setClipImpl(0, 0, this.surface.getWidth(), this.surface.getHeight());
    }

    public void clearRect(int n, int n2, int n3, int n4) {
        int n5 = Math.max(n, 0);
        int n6 = Math.max(n2, 0);
        int n7 = Math.min(n + n3, this.surface.getWidth());
        int n8 = Math.min(n2 + n4, this.surface.getHeight());
        this.clearRectImpl(n5, n6, n7 - n5, n8 - n6);
    }

    private native void clearRectImpl(int var1, int var2, int var3, int var4);

    public void fillRect(int n, int n2, int n3, int n4) {
        int n5 = Math.max(n, 0);
        int n6 = Math.max(n2, 0);
        int n7 = Math.min(n + n3, this.surface.getWidth() << 16);
        int n8 = Math.min(n2 + n4, this.surface.getHeight() << 16);
        int n9 = n7 - n5;
        int n10 = n8 - n6;
        if (n9 > 0 && n10 > 0) {
            this.fillRectImpl(n5, n6, n9, n10);
        }
    }

    private native void fillRectImpl(int var1, int var2, int var3, int var4);

    public void emitAndClearAlphaRow(byte[] arrby, int[] arrn, int n, int n2, int n3, int n4) {
        this.emitAndClearAlphaRow(arrby, arrn, n, n2, n3, 0, n4);
    }

    public void emitAndClearAlphaRow(byte[] arrby, int[] arrn, int n, int n2, int n3, int n4, int n5) {
        if (n4 < 0 || n4 + (n3 - n2) > arrn.length) {
            throw new IllegalArgumentException("rendering range exceeds length of data");
        }
        this.emitAndClearAlphaRowImpl(arrby, arrn, n, n2, n3, n4, n5);
    }

    private native void emitAndClearAlphaRowImpl(byte[] var1, int[] var2, int var3, int var4, int var5, int var6, int var7);

    public void fillAlphaMask(byte[] arrby, int n, int n2, int n3, int n4, int n5, int n6) {
        if (arrby == null) {
            throw new NullPointerException("Mask is NULL");
        }
        this.inputImageCheck(n3, n4, n5, n6, arrby.length);
        this.fillAlphaMaskImpl(arrby, n, n2, n3, n4, n5, n6);
    }

    private native void fillAlphaMaskImpl(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    public void setLCDGammaCorrection(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Gamma must be greater than zero");
        }
        this.setLCDGammaCorrectionImpl(f);
    }

    private native void setLCDGammaCorrectionImpl(float var1);

    public void fillLCDAlphaMask(byte[] arrby, int n, int n2, int n3, int n4, int n5, int n6) {
        if (arrby == null) {
            throw new NullPointerException("Mask is NULL");
        }
        this.inputImageCheck(n3, n4, n5, n6, arrby.length);
        this.fillLCDAlphaMaskImpl(arrby, n, n2, n3, n4, n5, n6);
    }

    private native void fillLCDAlphaMaskImpl(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    public void drawImage(int n, int n2, int[] arrn, int n3, int n4, int n5, int n6, Transform6 transform6, boolean bl, boolean bl2, int n7, int n8, int n9, int n10, int n11, int n12, int n13, int n14, int n15, int n16, int n17, int n18, boolean bl3) {
        this.inputImageCheck(n3, n4, n5, n6, arrn.length);
        this.drawImageImpl(n, n2, arrn, n3, n4, n5, n6, transform6, bl, bl2, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16, n17, n18, bl3);
    }

    private native void drawImageImpl(int var1, int var2, int[] var3, int var4, int var5, int var6, int var7, Transform6 var8, boolean var9, boolean var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22, boolean var23);

    private void inputImageCheck(int n, int n2, int n3, int n4, int n5) {
        if (n < 0) {
            throw new IllegalArgumentException("WIDTH must be positive");
        }
        if (n2 < 0) {
            throw new IllegalArgumentException("HEIGHT must be positive");
        }
        if (n3 < 0) {
            throw new IllegalArgumentException("OFFSET must be positive");
        }
        if (n4 < 0) {
            throw new IllegalArgumentException("STRIDE must be positive");
        }
        if (n4 < n) {
            throw new IllegalArgumentException("STRIDE must be >= WIDTH");
        }
        int n6 = 32 - Integer.numberOfLeadingZeros(n4) + 32 - Integer.numberOfLeadingZeros(n2);
        if (n6 > 31) {
            throw new IllegalArgumentException("STRIDE * HEIGHT is too large");
        }
        if (n3 + n4 * (n2 - 1) + n > n5) {
            throw new IllegalArgumentException("STRIDE * HEIGHT exceeds length of data");
        }
    }

    private static native void disposeNative(long var0);

    private static class PiscesRendererDisposerRecord
    implements Disposer.Record {
        private long nativeHandle;

        PiscesRendererDisposerRecord(long l) {
            this.nativeHandle = l;
        }

        @Override
        public void dispose() {
            if (this.nativeHandle != 0L) {
                PiscesRenderer.disposeNative(this.nativeHandle);
                this.nativeHandle = 0L;
            }
        }
    }
}

