package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import java.nio.FloatBuffer;

public class BoxRenderState extends LinearConvolveRenderState {
   private static final int[] MAX_BOX_SIZES;
   private final boolean isShadow;
   private final int blurPasses;
   private final float spread;
   private Color4f shadowColor;
   private RenderState.EffectCoordinateSpace space;
   private BaseTransform inputtx;
   private BaseTransform resulttx;
   private final float inputSizeH;
   private final float inputSizeV;
   private final int spreadPass;
   private float[] samplevectors;
   private int validatedPass;
   private float passSize;
   private FloatBuffer weights;
   private float weightsValidSize;
   private float weightsValidSpread;
   private boolean swCompatible;

   public static int getMaxSizeForKernelSize(int var0, int var1) {
      if (var1 == 0) {
         return Integer.MAX_VALUE;
      } else {
         int var2 = var0 - 1 | 1;
         var2 = (var2 - 1) / var1 | 1;

         assert getKernelSize(var2, var1) <= var0;

         return var2;
      }
   }

   public static int getKernelSize(int var0, int var1) {
      int var2 = var0 < 1 ? 1 : var0;
      var2 = (var2 - 1) * var1 + 1;
      var2 |= 1;
      return var2;
   }

   public BoxRenderState(float var1, float var2, int var3, float var4, boolean var5, Color4f var6, BaseTransform var7) {
      this.isShadow = var5;
      this.shadowColor = var6;
      this.spread = var4;
      this.blurPasses = var3;
      if (var7 == null) {
         var7 = BaseTransform.IDENTITY_TRANSFORM;
      }

      double var8 = Math.hypot(var7.getMxx(), var7.getMyx());
      double var10 = Math.hypot(var7.getMxy(), var7.getMyy());
      float var12 = (float)((double)var1 * var8);
      float var13 = (float)((double)var2 * var10);
      int var14 = MAX_BOX_SIZES[var3];
      if (var12 > (float)var14) {
         var8 = (double)((float)var14 / var1);
         var12 = (float)var14;
      }

      if (var13 > (float)var14) {
         var10 = (double)((float)var14 / var2);
         var13 = (float)var14;
      }

      this.inputSizeH = var12;
      this.inputSizeV = var13;
      this.spreadPass = var13 > 1.0F ? 1 : 0;
      boolean var15 = var8 != var7.getMxx() || 0.0 != var7.getMyx() || var10 != var7.getMyy() || 0.0 != var7.getMxy();
      if (var15) {
         this.space = RenderState.EffectCoordinateSpace.CustomSpace;
         this.inputtx = BaseTransform.getScaleInstance(var8, var10);
         this.resulttx = var7.copy().deriveWithScale(1.0 / var8, 1.0 / var10, 1.0);
      } else {
         this.space = RenderState.EffectCoordinateSpace.RenderSpace;
         this.inputtx = var7;
         this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
      }

   }

   public int getBoxPixelSize(int var1) {
      float var2 = this.passSize;
      if (var2 < 1.0F) {
         var2 = 1.0F;
      }

      int var3 = (int)Math.ceil((double)var2) | 1;
      return var3;
   }

   public int getBlurPasses() {
      return this.blurPasses;
   }

   public float getSpread() {
      return this.spread;
   }

   public boolean isShadow() {
      return this.isShadow;
   }

   public Color4f getShadowColor() {
      return this.shadowColor;
   }

   public float[] getPassShadowColorComponents() {
      return this.validatedPass == 0 ? BLACK_COMPONENTS : this.shadowColor.getPremultipliedRGBComponents();
   }

   public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
      return this.space;
   }

   public BaseTransform getInputTransform(BaseTransform var1) {
      return this.inputtx;
   }

   public BaseTransform getResultTransform(BaseTransform var1) {
      return this.resulttx;
   }

   public EffectPeer getPassPeer(Renderer var1, FilterContext var2) {
      if (this.isPassNop()) {
         return null;
      } else {
         int var3 = this.getPassKernelSize();
         int var4 = getPeerSize(var3);
         Effect.AccelType var5 = var1.getAccelType();
         String var6;
         switch (var5) {
            case NONE:
            case SIMD:
               if (this.swCompatible && this.spread == 0.0F) {
                  var6 = this.isShadow() ? "BoxShadow" : "BoxBlur";
                  break;
               }
            default:
               var6 = this.isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
         }

         EffectPeer var7 = var1.getPeerInstance(var2, var6, var4);
         return var7;
      }
   }

   public Rectangle getInputClip(int var1, Rectangle var2) {
      if (var2 != null) {
         int var3 = this.getInputKernelSize(0);
         int var4 = this.getInputKernelSize(1);
         if ((var3 | var4) > 1) {
            var2 = new Rectangle(var2);
            var2.grow(var3 / 2, var4 / 2);
         }
      }

      return var2;
   }

   public ImageData validatePassInput(ImageData var1, int var2) {
      this.validatedPass = var2;
      BaseTransform var3 = var1.getTransform();
      this.samplevectors = new float[2];
      this.samplevectors[var2] = 1.0F;
      float var4 = var2 == 0 ? this.inputSizeH : this.inputSizeV;
      float[] var10000;
      if (var3.isTranslateOrIdentity()) {
         this.swCompatible = true;
         this.passSize = var4;
      } else {
         try {
            var3.inverseDeltaTransform(this.samplevectors, 0, this.samplevectors, 0, 1);
         } catch (NoninvertibleTransformException var10) {
            this.passSize = 0.0F;
            this.samplevectors[0] = this.samplevectors[1] = 0.0F;
            this.swCompatible = true;
            return var1;
         }

         double var5 = Math.hypot((double)this.samplevectors[0], (double)this.samplevectors[1]);
         float var7 = (float)((double)var4 * var5);
         var7 = (float)((double)var7 * var5);
         int var8 = MAX_BOX_SIZES[this.blurPasses];
         if (var7 > (float)var8) {
            var7 = (float)var8;
            var5 = (double)((float)var8 / var4);
         }

         this.passSize = var7;
         var10000 = this.samplevectors;
         var10000[0] = (float)((double)var10000[0] / var5);
         var10000 = this.samplevectors;
         var10000[1] = (float)((double)var10000[1] / var5);
         Rectangle var9 = var1.getUntransformedBounds();
         if (var2 == 0) {
            this.swCompatible = nearOne(this.samplevectors[0], var9.width) && nearZero(this.samplevectors[1], var9.width);
         } else {
            this.swCompatible = nearZero(this.samplevectors[0], var9.height) && nearOne(this.samplevectors[1], var9.height);
         }
      }

      Filterable var11 = var1.getUntransformedImage();
      var10000 = this.samplevectors;
      var10000[0] /= (float)var11.getPhysicalWidth();
      var10000 = this.samplevectors;
      var10000[1] /= (float)var11.getPhysicalHeight();
      return var1;
   }

   public Rectangle getPassResultBounds(Rectangle var1, Rectangle var2) {
      Rectangle var3 = new Rectangle(var1);
      if (this.validatedPass == 0) {
         var3.grow(this.getInputKernelSize(0) / 2, 0);
      } else {
         var3.grow(0, this.getInputKernelSize(1) / 2);
      }

      if (var2 != null) {
         if (this.validatedPass == 0) {
            var2 = new Rectangle(var2);
            var2.grow(0, this.getInputKernelSize(1) / 2);
         }

         var3.intersectWith(var2);
      }

      return var3;
   }

   public float[] getPassVector() {
      float var1 = this.samplevectors[0];
      float var2 = this.samplevectors[1];
      int var3 = this.getPassKernelSize();
      int var4 = var3 / 2;
      float[] var5 = new float[]{var1, var2, (float)(-var4) * var1, (float)(-var4) * var2};
      return var5;
   }

   public int getPassWeightsArrayLength() {
      this.validateWeights();
      return this.weights.limit() / 4;
   }

   public FloatBuffer getPassWeights() {
      this.validateWeights();
      this.weights.rewind();
      return this.weights;
   }

   private void validateWeights() {
      float var1;
      if (this.blurPasses == 0) {
         var1 = 1.0F;
      } else {
         var1 = this.passSize;
         if (var1 < 1.0F) {
            var1 = 1.0F;
         }
      }

      float var2 = this.validatedPass == this.spreadPass ? this.spread : 0.0F;
      if (this.weights == null || this.weightsValidSize != var1 || this.weightsValidSpread != var2) {
         int var3 = (int)Math.ceil((double)var1) | 1;
         int var4 = var3;

         for(int var5 = 1; var5 < this.blurPasses; ++var5) {
            var4 += var3 - 1;
         }

         double[] var14 = new double[var4];

         for(int var6 = 0; var6 < var3; ++var6) {
            var14[var6] = 1.0;
         }

         double var15 = (double)((float)var3 - var1);
         if (var15 > 0.0) {
            var14[0] = var14[var3 - 1] = 1.0 - var15 * 0.5;
         }

         int var8 = var3;

         for(int var9 = 1; var9 < this.blurPasses; ++var9) {
            var8 += var3 - 1;

            int var10;
            double var11;
            int var13;
            for(var10 = var8 - 1; var10 > var3; var14[var10--] = var11) {
               var11 = var14[var10];

               for(var13 = 1; var13 < var3; ++var13) {
                  var11 += var14[var10 - var13];
               }
            }

            while(var10 > 0) {
               var11 = var14[var10];

               for(var13 = 0; var13 < var10; ++var13) {
                  var11 += var14[var13];
               }

               var14[var10--] = var11;
            }
         }

         double var16 = 0.0;

         int var17;
         for(var17 = 0; var17 < var14.length; ++var17) {
            var16 += var14[var17];
         }

         var16 += (1.0 - var16) * (double)var2;
         if (this.weights == null) {
            var17 = getPeerSize(MAX_KERNEL_SIZE);
            var17 = var17 + 3 & -4;
            this.weights = BufferUtil.newFloatBuffer(var17);
         }

         this.weights.clear();

         for(var17 = 0; var17 < var14.length; ++var17) {
            this.weights.put((float)(var14[var17] / var16));
         }

         var17 = getPeerSize(var14.length);

         while(this.weights.position() < var17) {
            this.weights.put(0.0F);
         }

         this.weights.limit(var17);
         this.weights.rewind();
      }
   }

   public int getInputKernelSize(int var1) {
      float var2 = var1 == 0 ? this.inputSizeH : this.inputSizeV;
      if (var2 < 1.0F) {
         var2 = 1.0F;
      }

      int var3 = (int)Math.ceil((double)var2) | 1;
      int var4 = 1;

      for(int var5 = 0; var5 < this.blurPasses; ++var5) {
         var4 += var3 - 1;
      }

      return var4;
   }

   public int getPassKernelSize() {
      float var1 = this.passSize;
      if (var1 < 1.0F) {
         var1 = 1.0F;
      }

      int var2 = (int)Math.ceil((double)var1) | 1;
      int var3 = 1;

      for(int var4 = 0; var4 < this.blurPasses; ++var4) {
         var3 += var2 - 1;
      }

      return var3;
   }

   public boolean isNop() {
      if (this.isShadow) {
         return false;
      } else {
         return this.blurPasses == 0 || this.inputSizeH <= 1.0F && this.inputSizeV <= 1.0F;
      }
   }

   public boolean isPassNop() {
      if (this.isShadow && this.validatedPass == 1) {
         return false;
      } else {
         return this.blurPasses == 0 || this.passSize <= 1.0F;
      }
   }

   static {
      MAX_BOX_SIZES = new int[]{getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 0), getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 1), getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 2), getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 3)};
   }
}
