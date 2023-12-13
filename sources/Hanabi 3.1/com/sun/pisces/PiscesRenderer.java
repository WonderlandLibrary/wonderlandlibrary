package com.sun.pisces;

public final class PiscesRenderer {
   public static final int ARC_OPEN = 0;
   public static final int ARC_CHORD = 1;
   public static final int ARC_PIE = 2;
   private long nativePtr = 0L;
   private AbstractSurface surface;

   public PiscesRenderer(AbstractSurface var1) {
      this.surface = var1;
      this.initialize();
   }

   private native void initialize();

   public void setColor(int var1, int var2, int var3, int var4) {
      this.checkColorRange(var1, "RED");
      this.checkColorRange(var2, "GREEN");
      this.checkColorRange(var3, "BLUE");
      this.checkColorRange(var4, "ALPHA");
      this.setColorImpl(var1, var2, var3, var4);
   }

   private native void setColorImpl(int var1, int var2, int var3, int var4);

   private void checkColorRange(int var1, String var2) {
      if (var1 < 0 || var1 > 255) {
         throw new IllegalArgumentException(var2 + " color component is out of range");
      }
   }

   public void setColor(int var1, int var2, int var3) {
      this.setColor(var1, var2, var3, 255);
   }

   public void setCompositeRule(int var1) {
      if (var1 != 0 && var1 != 1 && var1 != 2) {
         throw new IllegalArgumentException("Invalid value for Composite-Rule");
      } else {
         this.setCompositeRuleImpl(var1);
      }
   }

   private native void setCompositeRuleImpl(int var1);

   private native void setLinearGradientImpl(int var1, int var2, int var3, int var4, int[] var5, int var6, Transform6 var7);

   public void setLinearGradient(int var1, int var2, int var3, int var4, int[] var5, int[] var6, int var7, Transform6 var8) {
      GradientColorMap var9 = new GradientColorMap(var5, var6, var7);
      this.setLinearGradientImpl(var1, var2, var3, var4, var9.colors, var7, var8 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : var8);
   }

   public void setLinearGradient(int var1, int var2, int var3, int var4, GradientColorMap var5, Transform6 var6) {
      this.setLinearGradientImpl(var1, var2, var3, var4, var5.colors, var5.cycleMethod, var6 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : var6);
   }

   public void setLinearGradient(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int[] var8 = new int[]{0, 65536};
      int[] var9 = new int[]{var3, var6};
      Transform6 var10 = new Transform6(65536, 0, 0, 65536, 0, 0);
      this.setLinearGradient(var1, var2, var4, var5, var8, var9, var7, var10);
   }

   private native void setRadialGradientImpl(int var1, int var2, int var3, int var4, int var5, int[] var6, int var7, Transform6 var8);

   public void setRadialGradient(int var1, int var2, int var3, int var4, int var5, int[] var6, int[] var7, int var8, Transform6 var9) {
      GradientColorMap var10 = new GradientColorMap(var6, var7, var8);
      this.setRadialGradientImpl(var1, var2, var3, var4, var5, var10.colors, var8, var9 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : var9);
   }

   public void setRadialGradient(int var1, int var2, int var3, int var4, int var5, GradientColorMap var6, Transform6 var7) {
      this.setRadialGradientImpl(var1, var2, var3, var4, var5, var6.colors, var6.cycleMethod, var7 == null ? new Transform6(65536, 0, 0, 65536, 0, 0) : var7);
   }

   public void setTexture(int var1, int[] var2, int var3, int var4, int var5, Transform6 var6, boolean var7, boolean var8, boolean var9) {
      this.inputImageCheck(var3, var4, 0, var5, var2.length);
      this.setTextureImpl(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   private native void setTextureImpl(int var1, int[] var2, int var3, int var4, int var5, Transform6 var6, boolean var7, boolean var8, boolean var9);

   public void setClip(int var1, int var2, int var3, int var4) {
      int var5 = Math.max(var1, 0);
      int var6 = Math.max(var2, 0);
      int var7 = Math.min(var1 + var3, this.surface.getWidth());
      int var8 = Math.min(var2 + var4, this.surface.getHeight());
      this.setClipImpl(var5, var6, var7 - var5, var8 - var6);
   }

   private native void setClipImpl(int var1, int var2, int var3, int var4);

   public void resetClip() {
      this.setClipImpl(0, 0, this.surface.getWidth(), this.surface.getHeight());
   }

   public void clearRect(int var1, int var2, int var3, int var4) {
      int var5 = Math.max(var1, 0);
      int var6 = Math.max(var2, 0);
      int var7 = Math.min(var1 + var3, this.surface.getWidth());
      int var8 = Math.min(var2 + var4, this.surface.getHeight());
      this.clearRectImpl(var5, var6, var7 - var5, var8 - var6);
   }

   private native void clearRectImpl(int var1, int var2, int var3, int var4);

   public void fillRect(int var1, int var2, int var3, int var4) {
      int var5 = Math.max(var1, 0);
      int var6 = Math.max(var2, 0);
      int var7 = Math.min(var1 + var3, this.surface.getWidth() << 16);
      int var8 = Math.min(var2 + var4, this.surface.getHeight() << 16);
      int var9 = var7 - var5;
      int var10 = var8 - var6;
      if (var9 > 0 && var10 > 0) {
         this.fillRectImpl(var5, var6, var9, var10);
      }

   }

   private native void fillRectImpl(int var1, int var2, int var3, int var4);

   public void emitAndClearAlphaRow(byte[] var1, int[] var2, int var3, int var4, int var5, int var6) {
      if (var5 - var4 > var2.length) {
         throw new IllegalArgumentException("rendering range exceeds length of data");
      } else {
         this.emitAndClearAlphaRowImpl(var1, var2, var3, var4, var5, var6);
      }
   }

   private native void emitAndClearAlphaRowImpl(byte[] var1, int[] var2, int var3, int var4, int var5, int var6);

   public void fillAlphaMask(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var1 == null) {
         throw new NullPointerException("Mask is NULL");
      } else {
         this.inputImageCheck(var4, var5, var6, var7, var1.length);
         this.fillAlphaMaskImpl(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   private native void fillAlphaMaskImpl(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

   public void setLCDGammaCorrection(float var1) {
      if (var1 <= 0.0F) {
         throw new IllegalArgumentException("Gamma must be greater than zero");
      } else {
         this.setLCDGammaCorrectionImpl(var1);
      }
   }

   private native void setLCDGammaCorrectionImpl(float var1);

   public void fillLCDAlphaMask(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var1 == null) {
         throw new NullPointerException("Mask is NULL");
      } else {
         this.inputImageCheck(var4, var5, var6, var7, var1.length);
         this.fillLCDAlphaMaskImpl(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   private native void fillLCDAlphaMaskImpl(byte[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

   public void drawImage(int var1, int var2, int[] var3, int var4, int var5, int var6, int var7, Transform6 var8, boolean var9, boolean var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22, boolean var23) {
      this.inputImageCheck(var4, var5, var6, var7, var3.length);
      this.drawImageImpl(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22, var23);
   }

   private native void drawImageImpl(int var1, int var2, int[] var3, int var4, int var5, int var6, int var7, Transform6 var8, boolean var9, boolean var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22, boolean var23);

   private void inputImageCheck(int var1, int var2, int var3, int var4, int var5) {
      if (var1 < 0) {
         throw new IllegalArgumentException("WIDTH must be positive");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("HEIGHT must be positive");
      } else if (var3 < 0) {
         throw new IllegalArgumentException("OFFSET must be positive");
      } else if (var4 < 0) {
         throw new IllegalArgumentException("STRIDE must be positive");
      } else if (var4 < var1) {
         throw new IllegalArgumentException("STRIDE must be >= WIDTH");
      } else {
         int var6 = 32 - Integer.numberOfLeadingZeros(var4) + 32 - Integer.numberOfLeadingZeros(var2);
         if (var6 > 31) {
            throw new IllegalArgumentException("STRIDE * HEIGHT is too large");
         } else if (var3 + var4 * (var2 - 1) + var1 > var5) {
            throw new IllegalArgumentException("STRIDE * HEIGHT exceeds length of data");
         }
      }
   }

   protected void finalize() {
      this.nativeFinalize();
   }

   private native void nativeFinalize();
}
