package com.sun.prism.j2d.paint;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

abstract class MultipleGradientPaintContext implements PaintContext {
   protected ColorModel model;
   private static ColorModel xrgbmodel = new DirectColorModel(24, 16711680, 65280, 255);
   protected static ColorModel cachedModel;
   protected static WeakReference cached;
   protected Raster saved;
   protected MultipleGradientPaint.CycleMethod cycleMethod;
   protected MultipleGradientPaint.ColorSpaceType colorSpace;
   protected float a00;
   protected float a01;
   protected float a10;
   protected float a11;
   protected float a02;
   protected float a12;
   protected boolean isSimpleLookup;
   protected int fastGradientArraySize;
   protected int[] gradient;
   private int[][] gradients;
   private float[] normalizedIntervals;
   private float[] fractions;
   private int transparencyTest;
   private static final int[] SRGBtoLinearRGB = new int[256];
   private static final int[] LinearRGBtoSRGB = new int[256];
   protected static final int GRADIENT_SIZE = 256;
   protected static final int GRADIENT_SIZE_INDEX = 255;
   private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;

   protected MultipleGradientPaintContext(MultipleGradientPaint var1, ColorModel var2, Rectangle var3, Rectangle2D var4, AffineTransform var5, RenderingHints var6, float[] var7, Color[] var8, MultipleGradientPaint.CycleMethod var9, MultipleGradientPaint.ColorSpaceType var10) {
      if (var3 == null) {
         throw new NullPointerException("Device bounds cannot be null");
      } else if (var4 == null) {
         throw new NullPointerException("User bounds cannot be null");
      } else if (var5 == null) {
         throw new NullPointerException("Transform cannot be null");
      } else {
         AffineTransform var11;
         try {
            var11 = var5.createInverse();
         } catch (NoninvertibleTransformException var13) {
            var11 = new AffineTransform();
         }

         double[] var12 = new double[6];
         var11.getMatrix(var12);
         this.a00 = (float)var12[0];
         this.a10 = (float)var12[1];
         this.a01 = (float)var12[2];
         this.a11 = (float)var12[3];
         this.a02 = (float)var12[4];
         this.a12 = (float)var12[5];
         this.cycleMethod = var9;
         this.colorSpace = var10;
         this.fractions = var7;
         this.gradient = var1.gradient != null ? (int[])var1.gradient.get() : null;
         this.gradients = var1.gradients != null ? (int[][])var1.gradients.get() : (int[][])null;
         if (this.gradient == null && this.gradients == null) {
            this.calculateLookupData(var8);
            var1.model = this.model;
            var1.normalizedIntervals = this.normalizedIntervals;
            var1.isSimpleLookup = this.isSimpleLookup;
            if (this.isSimpleLookup) {
               var1.fastGradientArraySize = this.fastGradientArraySize;
               var1.gradient = new SoftReference(this.gradient);
            } else {
               var1.gradients = new SoftReference(this.gradients);
            }
         } else {
            this.model = var1.model;
            this.normalizedIntervals = var1.normalizedIntervals;
            this.isSimpleLookup = var1.isSimpleLookup;
            this.fastGradientArraySize = var1.fastGradientArraySize;
         }

      }
   }

   private void calculateLookupData(Color[] var1) {
      Color[] var2;
      int var3;
      int var4;
      int var5;
      if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
         var2 = new Color[var1.length];

         for(var3 = 0; var3 < var1.length; ++var3) {
            var4 = var1[var3].getRGB();
            var5 = var4 >>> 24;
            int var6 = SRGBtoLinearRGB[var4 >> 16 & 255];
            int var7 = SRGBtoLinearRGB[var4 >> 8 & 255];
            int var8 = SRGBtoLinearRGB[var4 & 255];
            var2[var3] = new Color(var6, var7, var8, var5);
         }
      } else {
         var2 = var1;
      }

      this.normalizedIntervals = new float[this.fractions.length - 1];

      for(var3 = 0; var3 < this.normalizedIntervals.length; ++var3) {
         this.normalizedIntervals[var3] = this.fractions[var3 + 1] - this.fractions[var3];
      }

      this.transparencyTest = -16777216;
      this.gradients = new int[this.normalizedIntervals.length][];
      float var9 = 1.0F;

      for(var4 = 0; var4 < this.normalizedIntervals.length; ++var4) {
         var9 = var9 > this.normalizedIntervals[var4] ? this.normalizedIntervals[var4] : var9;
      }

      var4 = 0;

      for(var5 = 0; var5 < this.normalizedIntervals.length; ++var5) {
         var4 = (int)((float)var4 + this.normalizedIntervals[var5] / var9 * 256.0F);
      }

      if (var4 > 5000) {
         this.calculateMultipleArrayGradient(var2);
      } else {
         this.calculateSingleArrayGradient(var2, var9);
      }

      if (this.transparencyTest >>> 24 == 255) {
         this.model = xrgbmodel;
      } else {
         this.model = ColorModel.getRGBdefault();
      }

   }

   private void calculateSingleArrayGradient(Color[] var1, float var2) {
      this.isSimpleLookup = true;
      int var5 = 1;

      int var6;
      int var7;
      for(var6 = 0; var6 < this.gradients.length; ++var6) {
         var7 = (int)(this.normalizedIntervals[var6] / var2 * 255.0F);
         var5 += var7;
         this.gradients[var6] = new int[var7];
         int var3 = var1[var6].getRGB();
         int var4 = var1[var6 + 1].getRGB();
         this.interpolate(var3, var4, this.gradients[var6]);
         this.transparencyTest &= var3;
         this.transparencyTest &= var4;
      }

      this.gradient = new int[var5];
      var6 = 0;

      for(var7 = 0; var7 < this.gradients.length; ++var7) {
         System.arraycopy(this.gradients[var7], 0, this.gradient, var6, this.gradients[var7].length);
         var6 += this.gradients[var7].length;
      }

      this.gradient[this.gradient.length - 1] = var1[var1.length - 1].getRGB();
      if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
         for(var7 = 0; var7 < this.gradient.length; ++var7) {
            this.gradient[var7] = this.convertEntireColorLinearRGBtoSRGB(this.gradient[var7]);
         }
      }

      this.fastGradientArraySize = this.gradient.length - 1;
   }

   private void calculateMultipleArrayGradient(Color[] var1) {
      this.isSimpleLookup = false;

      int var4;
      for(var4 = 0; var4 < this.gradients.length; ++var4) {
         this.gradients[var4] = new int[256];
         int var2 = var1[var4].getRGB();
         int var3 = var1[var4 + 1].getRGB();
         this.interpolate(var2, var3, this.gradients[var4]);
         this.transparencyTest &= var2;
         this.transparencyTest &= var3;
      }

      if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
         for(var4 = 0; var4 < this.gradients.length; ++var4) {
            for(int var5 = 0; var5 < this.gradients[var4].length; ++var5) {
               this.gradients[var4][var5] = this.convertEntireColorLinearRGBtoSRGB(this.gradients[var4][var5]);
            }
         }
      }

   }

   private void interpolate(int var1, int var2, int[] var3) {
      float var12 = 1.0F / (float)var3.length;
      int var4 = var1 >> 24 & 255;
      int var5 = var1 >> 16 & 255;
      int var6 = var1 >> 8 & 255;
      int var7 = var1 & 255;
      int var8 = (var2 >> 24 & 255) - var4;
      int var9 = (var2 >> 16 & 255) - var5;
      int var10 = (var2 >> 8 & 255) - var6;
      int var11 = (var2 & 255) - var7;

      for(int var13 = 0; var13 < var3.length; ++var13) {
         var3[var13] = (int)((double)((float)var4 + (float)(var13 * var8) * var12) + 0.5) << 24 | (int)((double)((float)var5 + (float)(var13 * var9) * var12) + 0.5) << 16 | (int)((double)((float)var6 + (float)(var13 * var10) * var12) + 0.5) << 8 | (int)((double)((float)var7 + (float)(var13 * var11) * var12) + 0.5);
      }

   }

   private int convertEntireColorLinearRGBtoSRGB(int var1) {
      int var2 = var1 >> 24 & 255;
      int var3 = var1 >> 16 & 255;
      int var4 = var1 >> 8 & 255;
      int var5 = var1 & 255;
      var3 = LinearRGBtoSRGB[var3];
      var4 = LinearRGBtoSRGB[var4];
      var5 = LinearRGBtoSRGB[var5];
      return var2 << 24 | var3 << 16 | var4 << 8 | var5;
   }

   protected final int indexIntoGradientsArrays(float var1) {
      int var2;
      if (this.cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE) {
         if (var1 > 1.0F) {
            var1 = 1.0F;
         } else if (var1 < 0.0F) {
            var1 = 0.0F;
         }
      } else if (this.cycleMethod == MultipleGradientPaint.CycleMethod.REPEAT) {
         var1 -= (float)((int)var1);
         if (var1 < 0.0F) {
            ++var1;
         }
      } else {
         if (var1 < 0.0F) {
            var1 = -var1;
         }

         var2 = (int)var1;
         var1 -= (float)var2;
         if ((var2 & 1) == 1) {
            var1 = 1.0F - var1;
         }
      }

      if (this.isSimpleLookup) {
         return this.gradient[(int)(var1 * (float)this.fastGradientArraySize)];
      } else {
         for(var2 = 0; var2 < this.gradients.length; ++var2) {
            if (var1 < this.fractions[var2 + 1]) {
               float var3 = var1 - this.fractions[var2];
               int var4 = (int)(var3 / this.normalizedIntervals[var2] * 255.0F);
               return this.gradients[var2][var4];
            }
         }

         return this.gradients[this.gradients.length - 1][255];
      }
   }

   private static int convertSRGBtoLinearRGB(int var0) {
      float var1 = (float)var0 / 255.0F;
      float var2;
      if (var1 <= 0.04045F) {
         var2 = var1 / 12.92F;
      } else {
         var2 = (float)Math.pow(((double)var1 + 0.055) / 1.055, 2.4);
      }

      return Math.round(var2 * 255.0F);
   }

   private static int convertLinearRGBtoSRGB(int var0) {
      float var1 = (float)var0 / 255.0F;
      float var2;
      if ((double)var1 <= 0.0031308) {
         var2 = var1 * 12.92F;
      } else {
         var2 = 1.055F * (float)Math.pow((double)var1, 0.4166666666666667) - 0.055F;
      }

      return Math.round(var2 * 255.0F);
   }

   public final Raster getRaster(int var1, int var2, int var3, int var4) {
      Raster var5 = this.saved;
      if (var5 == null || var5.getWidth() < var3 || var5.getHeight() < var4) {
         var5 = getCachedRaster(this.model, var3, var4);
         this.saved = var5;
      }

      DataBufferInt var6 = (DataBufferInt)var5.getDataBuffer();
      int[] var7 = var6.getData(0);
      int var8 = var6.getOffset();
      int var9 = ((SinglePixelPackedSampleModel)var5.getSampleModel()).getScanlineStride();
      int var10 = var9 - var3;
      this.fillRaster(var7, var8, var10, var1, var2, var3, var4);
      return var5;
   }

   protected abstract void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

   private static synchronized Raster getCachedRaster(ColorModel var0, int var1, int var2) {
      if (var0 == cachedModel && cached != null) {
         Raster var3 = (Raster)cached.get();
         if (var3 != null && var3.getWidth() >= var1 && var3.getHeight() >= var2) {
            cached = null;
            return var3;
         }
      }

      return var0.createCompatibleWritableRaster(var1, var2);
   }

   private static synchronized void putCachedRaster(ColorModel var0, Raster var1) {
      if (cached != null) {
         Raster var2 = (Raster)cached.get();
         if (var2 != null) {
            int var3 = var2.getWidth();
            int var4 = var2.getHeight();
            int var5 = var1.getWidth();
            int var6 = var1.getHeight();
            if (var3 >= var5 && var4 >= var6) {
               return;
            }

            if (var3 * var4 >= var5 * var6) {
               return;
            }
         }
      }

      cachedModel = var0;
      cached = new WeakReference(var1);
   }

   public final void dispose() {
      if (this.saved != null) {
         putCachedRaster(this.model, this.saved);
         this.saved = null;
      }

   }

   public final ColorModel getColorModel() {
      return this.model;
   }

   static {
      for(int var0 = 0; var0 < 256; ++var0) {
         SRGBtoLinearRGB[var0] = convertSRGBtoLinearRGB(var0);
         LinearRGBtoSRGB[var0] = convertLinearRGBtoSRGB(var0);
      }

   }
}
