package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrRenderInfo;

public final class NodeEffectInput extends Effect {
   private NGNode node;
   private RenderType renderType;
   private BaseBounds tempBounds;
   private ImageData cachedIdentityImageData;
   private ImageData cachedTransformedImageData;
   private BaseTransform cachedTransform;

   public NodeEffectInput(NGNode var1) {
      this(var1, NodeEffectInput.RenderType.EFFECT_CONTENT);
   }

   public NodeEffectInput(NGNode var1, RenderType var2) {
      this.tempBounds = new RectBounds();
      this.node = var1;
      this.renderType = var2;
   }

   public NGNode getNode() {
      return this.node;
   }

   public void setNode(NGNode var1) {
      if (this.node != var1) {
         this.node = var1;
         this.flush();
      }

   }

   static boolean contains(ImageData var0, Rectangle var1) {
      Rectangle var2 = var0.getUntransformedBounds();
      return var2.contains(var1);
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      BaseTransform var3 = var1 == null ? BaseTransform.IDENTITY_TRANSFORM : var1;
      this.tempBounds = this.node.getContentBounds(this.tempBounds, var3);
      return this.tempBounds.copy();
   }

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      if (var4 instanceof PrRenderInfo) {
         Graphics var6 = ((PrRenderInfo)var4).getGraphics();
         if (var6 != null) {
            this.render(var6, var2);
            return null;
         }
      }

      Rectangle var8 = getImageBoundsForNode(this.node, this.renderType, var2, var3);
      if (var2.isIdentity()) {
         if (this.cachedIdentityImageData != null && contains(this.cachedIdentityImageData, var8) && this.cachedIdentityImageData.validate(var1)) {
            this.cachedIdentityImageData.addref();
            return this.cachedIdentityImageData;
         }
      } else if (this.cachedTransformedImageData != null && contains(this.cachedTransformedImageData, var8) && this.cachedTransformedImageData.validate(var1) && this.cachedTransform.equals(var2)) {
         this.cachedTransformedImageData.addref();
         return this.cachedTransformedImageData;
      }

      ImageData var7 = getImageDataForBoundedNode(var1, this.node, this.renderType, var2, var8);
      if (var2.isIdentity()) {
         this.flushIdentityImage();
         this.cachedIdentityImageData = var7;
         this.cachedIdentityImageData.addref();
      } else {
         this.flushTransformedImage();
         this.cachedTransform = var2.copy();
         this.cachedTransformedImageData = var7;
         this.cachedTransformedImageData.addref();
      }

      return var7;
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      return Effect.AccelType.INTRINSIC;
   }

   public void flushIdentityImage() {
      if (this.cachedIdentityImageData != null) {
         this.cachedIdentityImageData.unref();
         this.cachedIdentityImageData = null;
      }

   }

   public void flushTransformedImage() {
      if (this.cachedTransformedImageData != null) {
         this.cachedTransformedImageData.unref();
         this.cachedTransformedImageData = null;
      }

      this.cachedTransform = null;
   }

   public void flush() {
      this.flushIdentityImage();
      this.flushTransformedImage();
   }

   public void render(Graphics var1, BaseTransform var2) {
      BaseTransform var3 = null;
      if (!var2.isIdentity()) {
         var3 = var1.getTransformNoClone().copy();
         var1.transform(var2);
      }

      this.node.renderContent(var1);
      if (var3 != null) {
         var1.setTransform(var3);
      }

   }

   static ImageData getImageDataForNode(FilterContext var0, NGNode var1, boolean var2, BaseTransform var3, Rectangle var4) {
      RenderType var5 = var2 ? NodeEffectInput.RenderType.EFFECT_CONTENT : NodeEffectInput.RenderType.FULL_CONTENT;
      Rectangle var6 = getImageBoundsForNode(var1, var5, var3, var4);
      return getImageDataForBoundedNode(var0, var1, var5, var3, var6);
   }

   static Rectangle getImageBoundsForNode(NGNode var0, RenderType var1, BaseTransform var2, Rectangle var3) {
      Object var4 = new RectBounds();
      switch (var1) {
         case EFFECT_CONTENT:
            var4 = var0.getContentBounds((BaseBounds)var4, var2);
            break;
         case FULL_CONTENT:
            var4 = var0.getCompleteBounds((BaseBounds)var4, var2);
            break;
         case CLIPPED_CONTENT:
            var4 = var0.getClippedBounds((BaseBounds)var4, var2);
      }

      Rectangle var5 = new Rectangle((BaseBounds)var4);
      if (var3 != null) {
         var5.intersectWith(var3);
      }

      return var5;
   }

   private static ImageData getImageDataForBoundedNode(FilterContext var0, NGNode var1, RenderType var2, BaseTransform var3, Rectangle var4) {
      PrDrawable var5 = (PrDrawable)Effect.getCompatibleImage(var0, var4.width, var4.height);
      if (var5 != null) {
         Graphics var6 = var5.createGraphics();
         var6.translate((float)(-var4.x), (float)(-var4.y));
         if (var3 != null) {
            var6.transform(var3);
         }

         switch (var2) {
            case EFFECT_CONTENT:
               var1.renderContent(var6);
               break;
            case FULL_CONTENT:
               var1.render(var6);
               break;
            case CLIPPED_CONTENT:
               var1.renderForClip(var6);
         }
      }

      return new ImageData(var0, var5, var4);
   }

   public boolean reducesOpaquePixels() {
      return false;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      return null;
   }

   public static enum RenderType {
      EFFECT_CONTENT,
      CLIPPED_CONTENT,
      FULL_CONTENT;
   }
}
