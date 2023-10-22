package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;

abstract class MultipleGradientContext {
   protected int cycleMethod;
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
   protected static final int GRADIENT_SIZE = 256;
   protected static final int GRADIENT_SIZE_INDEX = 255;
   private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;

   protected MultipleGradientContext(Gradient var1, BaseTransform var2, float[] var3, Color[] var4, int var5) {
      if (var2 == null) {
         throw new NullPointerException("Transform cannot be null");
      } else {
         BaseTransform var6;
         try {
            var6 = var2.createInverse();
         } catch (NoninvertibleTransformException var8) {
            var6 = BaseTransform.IDENTITY_TRANSFORM;
         }

         this.a00 = (float)var6.getMxx();
         this.a10 = (float)var6.getMyx();
         this.a01 = (float)var6.getMxy();
         this.a11 = (float)var6.getMyy();
         this.a02 = (float)var6.getMxt();
         this.a12 = (float)var6.getMyt();
         this.cycleMethod = var5;
         this.fractions = var3;
         this.calculateLookupData(var4);
      }
   }

   private void calculateLookupData(Color[] var1) {
      this.normalizedIntervals = new float[this.fractions.length - 1];

      for(int var3 = 0; var3 < this.normalizedIntervals.length; ++var3) {
         this.normalizedIntervals[var3] = this.fractions[var3 + 1] - this.fractions[var3];
      }

      this.transparencyTest = -16777216;
      this.gradients = new int[this.normalizedIntervals.length][];
      float var6 = 1.0F;

      for(int var4 = 0; var4 < this.normalizedIntervals.length; ++var4) {
         var6 = var6 > this.normalizedIntervals[var4] ? this.normalizedIntervals[var4] : var6;
      }

      float var7 = 0.0F;

      for(int var5 = 0; var5 < this.normalizedIntervals.length && Float.isFinite(var7); ++var5) {
         var7 += this.normalizedIntervals[var5] / var6 * 256.0F;
      }

      if (var7 <= 5000.0F) {
         this.calculateSingleArrayGradient(var1, var6);
      } else {
         this.calculateMultipleArrayGradient(var1);
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
         int var3 = var1[var6].getIntArgbPre();
         int var4 = var1[var6 + 1].getIntArgbPre();
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

      this.gradient[this.gradient.length - 1] = var1[var1.length - 1].getIntArgbPre();
      this.fastGradientArraySize = this.gradient.length - 1;
   }

   private void calculateMultipleArrayGradient(Color[] var1) {
      this.isSimpleLookup = false;

      for(int var4 = 0; var4 < this.gradients.length; ++var4) {
         this.gradients[var4] = new int[256];
         int var2 = var1[var4].getIntArgbPre();
         int var3 = var1[var4 + 1].getIntArgbPre();
         this.interpolate(var2, var3, this.gradients[var4]);
         this.transparencyTest &= var2;
         this.transparencyTest &= var3;
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

   protected final int indexIntoGradientsArrays(float var1) {
      int var2;
      if (this.cycleMethod == 0) {
         if (var1 > 1.0F) {
            var1 = 1.0F;
         } else if (var1 < 0.0F) {
            var1 = 0.0F;
         }
      } else if (this.cycleMethod == 2) {
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
      } else if (var1 < this.fractions[0]) {
         return this.gradients[0][0];
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

   protected abstract void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);
}
