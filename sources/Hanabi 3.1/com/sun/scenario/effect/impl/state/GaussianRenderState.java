package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import java.nio.FloatBuffer;

public class GaussianRenderState extends LinearConvolveRenderState {
   public static final float MAX_RADIUS;
   private boolean isShadow;
   private Color4f shadowColor;
   private float spread;
   private RenderState.EffectCoordinateSpace space;
   private BaseTransform inputtx;
   private BaseTransform resulttx;
   private float inputRadiusX;
   private float inputRadiusY;
   private float spreadPass;
   private int validatedPass;
   private LinearConvolveRenderState.PassType passType;
   private float passRadius;
   private FloatBuffer weights;
   private float[] samplevectors;
   private float weightsValidRadius;
   private float weightsValidSpread;

   static FloatBuffer getGaussianWeights(FloatBuffer var0, int var1, float var2, float var3) {
      int var4 = var1;
      int var5 = var1 * 2 + 1;
      if (var0 == null) {
         var0 = BufferUtil.newFloatBuffer(128);
      }

      var0.clear();
      float var6 = var2 / 3.0F;
      float var7 = 2.0F * var6 * var6;
      if (var7 < Float.MIN_VALUE) {
         var7 = Float.MIN_VALUE;
      }

      float var8 = 0.0F;

      int var9;
      for(var9 = -var1; var9 <= var4; ++var9) {
         float var10 = (float)Math.exp((double)((float)(-(var9 * var9)) / var7));
         var0.put(var10);
         var8 += var10;
      }

      var8 += (var0.get(0) - var8) * var3;

      for(var9 = 0; var9 < var5; ++var9) {
         var0.put(var9, var0.get(var9) / var8);
      }

      var9 = getPeerSize(var5);

      while(var0.position() < var9) {
         var0.put(0.0F);
      }

      var0.limit(var9);
      var0.rewind();
      return var0;
   }

   public GaussianRenderState(float var1, float var2, float var3, boolean var4, Color4f var5, BaseTransform var6) {
      this.isShadow = var4;
      this.shadowColor = var5;
      this.spread = var3;
      if (var6 == null) {
         var6 = BaseTransform.IDENTITY_TRANSFORM;
      }

      double var7 = var6.getMxx();
      double var9 = var6.getMxy();
      double var11 = var6.getMyx();
      double var13 = var6.getMyy();
      double var15 = Math.hypot(var7, var11);
      double var17 = Math.hypot(var9, var13);
      boolean var19 = false;
      float var20 = (float)((double)var1 * var15);
      float var21 = (float)((double)var2 * var17);
      if (var20 < 0.00390625F && var21 < 0.00390625F) {
         this.inputRadiusX = 0.0F;
         this.inputRadiusY = 0.0F;
         this.spreadPass = 0.0F;
         this.space = RenderState.EffectCoordinateSpace.RenderSpace;
         this.inputtx = var6;
         this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
         this.samplevectors = new float[]{1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
      } else {
         if (var20 > MAX_RADIUS) {
            var20 = MAX_RADIUS;
            var15 = (double)(MAX_RADIUS / var1);
            var19 = true;
         }

         if (var21 > MAX_RADIUS) {
            var21 = MAX_RADIUS;
            var17 = (double)(MAX_RADIUS / var2);
            var19 = true;
         }

         this.inputRadiusX = var20;
         this.inputRadiusY = var21;
         this.spreadPass = !(this.inputRadiusY > 1.0F) && !(this.inputRadiusY >= this.inputRadiusX) ? 0.0F : 1.0F;
         if (var19) {
            this.space = RenderState.EffectCoordinateSpace.CustomSpace;
            this.inputtx = BaseTransform.getScaleInstance(var15, var17);
            this.resulttx = var6.copy().deriveWithScale(1.0 / var15, 1.0 / var17, 1.0);
            this.samplevectors = new float[]{1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
         } else {
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = var6;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{(float)(var7 / var15), (float)(var11 / var15), (float)(var9 / var17), (float)(var13 / var17), 0.0F, 0.0F};
         }
      }

   }

   public GaussianRenderState(float var1, float var2, float var3, BaseTransform var4) {
      this.isShadow = false;
      this.spread = 0.0F;
      if (var4 == null) {
         var4 = BaseTransform.IDENTITY_TRANSFORM;
      }

      double var5 = var4.getMxx();
      double var7 = var4.getMxy();
      double var9 = var4.getMyx();
      double var11 = var4.getMyy();
      double var13 = var5 * (double)var2 + var7 * (double)var3;
      double var15 = var9 * (double)var2 + var11 * (double)var3;
      double var17 = Math.hypot(var13, var15);
      boolean var19 = false;
      float var20 = (float)((double)var1 * var17);
      if (var20 < 0.00390625F) {
         this.inputRadiusX = 0.0F;
         this.inputRadiusY = 0.0F;
         this.spreadPass = 0.0F;
         this.space = RenderState.EffectCoordinateSpace.RenderSpace;
         this.inputtx = var4;
         this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
         this.samplevectors = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
      } else {
         if (var20 > MAX_RADIUS) {
            var20 = MAX_RADIUS;
            var17 = (double)(MAX_RADIUS / var1);
            var19 = true;
         }

         this.inputRadiusX = var20;
         this.inputRadiusY = 0.0F;
         this.spreadPass = 0.0F;
         if (var19) {
            double var21 = var7 * (double)var2 - var5 * (double)var3;
            double var23 = var11 * (double)var2 - var9 * (double)var3;
            double var25 = Math.hypot(var21, var23);
            this.space = RenderState.EffectCoordinateSpace.CustomSpace;
            Affine2D var27 = new Affine2D();
            var27.scale(var17, var25);
            var27.rotate((double)var2, (double)(-var3));

            Object var28;
            try {
               var28 = var27.createInverse();
            } catch (NoninvertibleTransformException var30) {
               var28 = BaseTransform.IDENTITY_TRANSFORM;
            }

            this.inputtx = var27;
            this.resulttx = var4.copy().deriveWithConcatenation((BaseTransform)var28);
            this.samplevectors = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
         } else {
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = var4;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{(float)(var13 / var17), (float)(var15 / var17), 0.0F, 0.0F, 0.0F, 0.0F};
         }
      }

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

   public Rectangle getInputClip(int var1, Rectangle var2) {
      if (var2 != null) {
         double var3 = (double)(this.samplevectors[0] * this.inputRadiusX);
         double var5 = (double)(this.samplevectors[1] * this.inputRadiusX);
         double var7 = (double)(this.samplevectors[2] * this.inputRadiusY);
         double var9 = (double)(this.samplevectors[3] * this.inputRadiusY);
         int var11 = (int)Math.ceil(var3 + var7);
         int var12 = (int)Math.ceil(var5 + var9);
         if ((var11 | var12) != 0) {
            var2 = new Rectangle(var2);
            var2.grow(var11, var12);
         }
      }

      return var2;
   }

   public ImageData validatePassInput(ImageData var1, int var2) {
      this.validatedPass = var2;
      Filterable var3 = var1.getUntransformedImage();
      BaseTransform var4 = var1.getTransform();
      float var5 = var2 == 0 ? this.inputRadiusX : this.inputRadiusY;
      int var6 = var2 * 2;
      float[] var10000;
      if (var4.isTranslateOrIdentity()) {
         this.passRadius = var5;
         this.samplevectors[4] = this.samplevectors[var6];
         this.samplevectors[5] = this.samplevectors[var6 + 1];
         if (this.validatedPass == 0) {
            if (nearOne(this.samplevectors[4], var3.getPhysicalWidth()) && nearZero(this.samplevectors[5], var3.getPhysicalWidth())) {
               this.passType = LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED;
            } else {
               this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
            }
         } else if (nearZero(this.samplevectors[4], var3.getPhysicalHeight()) && nearOne(this.samplevectors[5], var3.getPhysicalHeight())) {
            this.passType = LinearConvolveRenderState.PassType.VERTICAL_CENTERED;
         } else {
            this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
         }
      } else {
         this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;

         try {
            var4.inverseDeltaTransform(this.samplevectors, var6, this.samplevectors, 4, 1);
         } catch (NoninvertibleTransformException var10) {
            this.passRadius = 0.0F;
            this.samplevectors[4] = this.samplevectors[5] = 0.0F;
            return var1;
         }

         double var7 = Math.hypot((double)this.samplevectors[4], (double)this.samplevectors[5]);
         float var9 = (float)((double)var5 * var7);
         if (var9 > MAX_RADIUS) {
            var9 = MAX_RADIUS;
            var7 = (double)(MAX_RADIUS / var5);
         }

         this.passRadius = var9;
         var10000 = this.samplevectors;
         var10000[4] = (float)((double)var10000[4] / var7);
         var10000 = this.samplevectors;
         var10000[5] = (float)((double)var10000[5] / var7);
      }

      var10000 = this.samplevectors;
      var10000[4] /= (float)var3.getPhysicalWidth();
      var10000 = this.samplevectors;
      var10000[5] /= (float)var3.getPhysicalHeight();
      return var1;
   }

   public Rectangle getPassResultBounds(Rectangle var1, Rectangle var2) {
      double var3 = this.validatedPass == 0 ? (double)this.inputRadiusX : (double)this.inputRadiusY;
      int var5 = this.validatedPass * 2;
      double var6 = (double)this.samplevectors[var5 + 0] * var3;
      double var8 = (double)this.samplevectors[var5 + 1] * var3;
      int var10 = (int)Math.ceil(Math.abs(var6));
      int var11 = (int)Math.ceil(Math.abs(var8));
      Rectangle var12 = new Rectangle(var1);
      var12.grow(var10, var11);
      if (var2 != null) {
         if (this.validatedPass == 0) {
            var6 = (double)this.samplevectors[2] * var3;
            var8 = (double)this.samplevectors[3] * var3;
            var10 = (int)Math.ceil(Math.abs(var6));
            var11 = (int)Math.ceil(Math.abs(var8));
            if ((var10 | var11) != 0) {
               var2 = new Rectangle(var2);
               var2.grow(var10, var11);
            }
         }

         var12.intersectWith(var2);
      }

      return var12;
   }

   public LinearConvolveRenderState.PassType getPassType() {
      return this.passType;
   }

   public float[] getPassVector() {
      float var1 = this.samplevectors[4];
      float var2 = this.samplevectors[5];
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

   public int getInputKernelSize(int var1) {
      return 1 + 2 * (int)Math.ceil(var1 == 0 ? (double)this.inputRadiusX : (double)this.inputRadiusY);
   }

   public int getPassKernelSize() {
      return 1 + 2 * (int)Math.ceil((double)this.passRadius);
   }

   public boolean isNop() {
      if (this.isShadow) {
         return false;
      } else {
         return this.inputRadiusX < 0.00390625F && this.inputRadiusY < 0.00390625F;
      }
   }

   public boolean isPassNop() {
      if (this.isShadow && this.validatedPass == 1) {
         return false;
      } else {
         return this.passRadius < 0.00390625F;
      }
   }

   private void validateWeights() {
      float var1 = this.passRadius;
      float var2 = (float)this.validatedPass == this.spreadPass ? this.spread : 0.0F;
      if (this.weights == null || this.weightsValidRadius != var1 || this.weightsValidSpread != var2) {
         this.weights = getGaussianWeights(this.weights, (int)Math.ceil((double)var1), var1, var2);
         this.weightsValidRadius = var1;
         this.weightsValidSpread = var2;
      }

   }

   static {
      MAX_RADIUS = (float)((MAX_KERNEL_SIZE - 1) / 2);
   }
}
