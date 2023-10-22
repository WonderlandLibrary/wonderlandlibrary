package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.light.Light;

public class PhongLighting extends CoreEffect {
   private float surfaceScale;
   private float diffuseConstant;
   private float specularConstant;
   private float specularExponent;
   private Light light;

   public PhongLighting(Light var1) {
      this(var1, new GaussianShadow(10.0F), DefaultInput);
   }

   public PhongLighting(Light var1, Effect var2, Effect var3) {
      super(var2, var3);
      this.surfaceScale = 1.0F;
      this.diffuseConstant = 1.0F;
      this.specularConstant = 1.0F;
      this.specularExponent = 1.0F;
      this.setLight(var1);
   }

   public final Effect getBumpInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setBumpInput(Effect var1) {
      this.setInput(0, var1);
   }

   public final Effect getContentInput() {
      return (Effect)this.getInputs().get(1);
   }

   private Effect getContentInput(Effect var1) {
      return this.getDefaultedInput(1, var1);
   }

   public void setContentInput(Effect var1) {
      this.setInput(1, var1);
   }

   public Light getLight() {
      return this.light;
   }

   public void setLight(Light var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Light must be non-null");
      } else {
         this.light = var1;
         this.updatePeerKey("PhongLighting_" + var1.getType().name());
      }
   }

   public float getDiffuseConstant() {
      return this.diffuseConstant;
   }

   public void setDiffuseConstant(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 2.0F)) {
         float var2 = this.diffuseConstant;
         this.diffuseConstant = var1;
      } else {
         throw new IllegalArgumentException("Diffuse constant must be in the range [0,2]");
      }
   }

   public float getSpecularConstant() {
      return this.specularConstant;
   }

   public void setSpecularConstant(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 2.0F)) {
         float var2 = this.specularConstant;
         this.specularConstant = var1;
      } else {
         throw new IllegalArgumentException("Specular constant must be in the range [0,2]");
      }
   }

   public float getSpecularExponent() {
      return this.specularExponent;
   }

   public void setSpecularExponent(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 40.0F)) {
         float var2 = this.specularExponent;
         this.specularExponent = var1;
      } else {
         throw new IllegalArgumentException("Specular exponent must be in the range [0,40]");
      }
   }

   public float getSurfaceScale() {
      return this.surfaceScale;
   }

   public void setSurfaceScale(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 10.0F)) {
         float var2 = this.surfaceScale;
         this.surfaceScale = var1;
      } else {
         throw new IllegalArgumentException("Surface scale must be in the range [0,10]");
      }
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      return this.getContentInput(var2).getBounds(var1, var2);
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      return super.getResultBounds(var1, var2, new ImageData[]{var3[1]});
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getContentInput(var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getContentInput(var2).untransform(var1, var2);
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return new RenderState() {
         public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
            return RenderState.EffectCoordinateSpace.RenderSpace;
         }

         public BaseTransform getInputTransform(BaseTransform var1) {
            return var1;
         }

         public BaseTransform getResultTransform(BaseTransform var1) {
            return BaseTransform.IDENTITY_TRANSFORM;
         }

         public Rectangle getInputClip(int var1, Rectangle var2) {
            if (var1 == 0 && var2 != null) {
               Rectangle var3 = new Rectangle(var2);
               var3.grow(1, 1);
               return var3;
            } else {
               return var2;
            }
         }
      };
   }

   public boolean reducesOpaquePixels() {
      Effect var1 = this.getContentInput();
      return var1 != null && var1.reducesOpaquePixels();
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      var4.grow(1, 1);
      Effect var5 = this.getDefaultedInput(1, var1);
      DirtyRegionContainer var6 = var5.getDirtyRegions(var1, var2);
      var4.merge(var6);
      var2.checkIn(var6);
      return var4;
   }
}
