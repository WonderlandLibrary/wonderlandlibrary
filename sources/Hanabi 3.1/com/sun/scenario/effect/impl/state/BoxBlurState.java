package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Color4f;

public class BoxBlurState extends HVSeparableKernel {
   private int hsize;
   private int vsize;
   private int blurPasses;

   public int getHsize() {
      return this.hsize;
   }

   public void setHsize(int var1) {
      if (var1 >= 0 && var1 <= 255) {
         this.hsize = var1;
      } else {
         throw new IllegalArgumentException("Blur size must be in the range [0,255]");
      }
   }

   public int getVsize() {
      return this.vsize;
   }

   public void setVsize(int var1) {
      if (var1 >= 0 && var1 <= 255) {
         this.vsize = var1;
      } else {
         throw new IllegalArgumentException("Blur size must be in the range [0,255]");
      }
   }

   public int getBlurPasses() {
      return this.blurPasses;
   }

   public void setBlurPasses(int var1) {
      if (var1 >= 0 && var1 <= 3) {
         this.blurPasses = var1;
      } else {
         throw new IllegalArgumentException("Number of passes must be in the range [0,3]");
      }
   }

   public Color4f getShadowColor() {
      return null;
   }

   public float getSpread() {
      return 0.0F;
   }

   public LinearConvolveRenderState getRenderState(BaseTransform var1) {
      return new BoxRenderState((float)this.hsize, (float)this.vsize, this.blurPasses, this.getSpread(), this.isShadow(), this.getShadowColor(), var1);
   }

   public boolean isNop() {
      return this.blurPasses == 0 || this.hsize <= 1 && this.vsize <= 1;
   }

   public int getKernelSize(int var1) {
      int var2 = var1 == 0 ? this.hsize : this.vsize;
      if (var2 < 1) {
         var2 = 1;
      }

      var2 = (var2 - 1) * this.blurPasses + 1;
      var2 |= 1;
      return var2;
   }
}
