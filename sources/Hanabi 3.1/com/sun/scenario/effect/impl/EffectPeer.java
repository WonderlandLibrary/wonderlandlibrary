package com.sun.scenario.effect.impl;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class EffectPeer {
   private final FilterContext fctx;
   private final Renderer renderer;
   private final String uniqueName;
   private Effect effect;
   private RenderState renderState;
   private int pass;
   private final Rectangle[] inputBounds = new Rectangle[2];
   private final BaseTransform[] inputTransforms = new BaseTransform[2];
   private final Rectangle[] inputNativeBounds = new Rectangle[2];
   private Rectangle destBounds;
   private final Rectangle destNativeBounds = new Rectangle();

   protected EffectPeer(FilterContext var1, Renderer var2, String var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("FilterContext must be non-null");
      } else {
         this.fctx = var1;
         this.renderer = var2;
         this.uniqueName = var3;
      }
   }

   public boolean isImageDataCompatible(ImageData var1) {
      return this.getRenderer().isImageDataCompatible(var1);
   }

   public abstract ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5);

   public void dispose() {
   }

   public Effect.AccelType getAccelType() {
      return this.renderer.getAccelType();
   }

   protected final FilterContext getFilterContext() {
      return this.fctx;
   }

   protected Renderer getRenderer() {
      return this.renderer;
   }

   public String getUniqueName() {
      return this.uniqueName;
   }

   protected Effect getEffect() {
      return this.effect;
   }

   protected void setEffect(Effect var1) {
      this.effect = var1;
   }

   protected RenderState getRenderState() {
      return this.renderState;
   }

   protected void setRenderState(RenderState var1) {
      this.renderState = var1;
   }

   public final int getPass() {
      return this.pass;
   }

   public void setPass(int var1) {
      this.pass = var1;
   }

   protected final Rectangle getInputBounds(int var1) {
      return this.inputBounds[var1];
   }

   protected final void setInputBounds(int var1, Rectangle var2) {
      this.inputBounds[var1] = var2;
   }

   protected final BaseTransform getInputTransform(int var1) {
      return this.inputTransforms[var1];
   }

   protected final void setInputTransform(int var1, BaseTransform var2) {
      this.inputTransforms[var1] = var2;
   }

   protected final Rectangle getInputNativeBounds(int var1) {
      return this.inputNativeBounds[var1];
   }

   protected final void setInputNativeBounds(int var1, Rectangle var2) {
      this.inputNativeBounds[var1] = var2;
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      return this.getEffect().getResultBounds(var1, var2, var3);
   }

   protected float[] getSourceRegion(int var1) {
      return getSourceRegion(this.getInputBounds(var1), this.getInputNativeBounds(var1), this.getDestBounds());
   }

   static float[] getSourceRegion(Rectangle var0, Rectangle var1, Rectangle var2) {
      float var3 = (float)(var2.x - var0.x);
      float var4 = (float)(var2.y - var0.y);
      float var5 = var3 + (float)var2.width;
      float var6 = var4 + (float)var2.height;
      float var7 = (float)var1.width;
      float var8 = (float)var1.height;
      return new float[]{var3 / var7, var4 / var8, var5 / var7, var6 / var8};
   }

   public int getTextureCoordinates(int var1, float[] var2, float var3, float var4, float var5, float var6, Rectangle var7, BaseTransform var8) {
      return getTextureCoordinates(var2, var3, var4, var5, var6, var7, var8);
   }

   public static int getTextureCoordinates(float[] var0, float var1, float var2, float var3, float var4, Rectangle var5, BaseTransform var6) {
      var0[0] = (float)var5.x;
      var0[1] = (float)var5.y;
      var0[2] = var0[0] + (float)var5.width;
      var0[3] = var0[1] + (float)var5.height;
      byte var7;
      if (var6.isTranslateOrIdentity()) {
         var1 += (float)var6.getMxt();
         var2 += (float)var6.getMyt();
         var7 = 4;
      } else {
         var0[4] = var0[2];
         var0[5] = var0[1];
         var0[6] = var0[0];
         var0[7] = var0[3];
         var7 = 8;

         try {
            var6.inverseTransform((float[])var0, 0, (float[])var0, 0, 4);
         } catch (NoninvertibleTransformException var9) {
            var0[0] = var0[1] = var0[2] = var0[4] = 0.0F;
            return 4;
         }
      }

      for(int var8 = 0; var8 < var7; var8 += 2) {
         var0[var8] = (var0[var8] - var1) / var3;
         var0[var8 + 1] = (var0[var8 + 1] - var2) / var4;
      }

      return var7;
   }

   protected final void setDestBounds(Rectangle var1) {
      this.destBounds = var1;
   }

   protected final Rectangle getDestBounds() {
      return this.destBounds;
   }

   protected final Rectangle getDestNativeBounds() {
      return this.destNativeBounds;
   }

   protected final void setDestNativeBounds(int var1, int var2) {
      this.destNativeBounds.width = var1;
      this.destNativeBounds.height = var2;
   }

   protected Object getSamplerData(int var1) {
      return null;
   }

   protected boolean isOriginUpperLeft() {
      return this.getAccelType() != Effect.AccelType.OPENGL;
   }
}
