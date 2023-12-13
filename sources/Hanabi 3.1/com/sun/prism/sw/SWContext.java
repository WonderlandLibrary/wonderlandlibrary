package com.sun.prism.sw;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.Logging;
import com.sun.openpisces.Renderer;
import com.sun.pisces.PiscesRenderer;
import com.sun.prism.BasicStroke;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import com.sun.prism.impl.shape.ShapeUtil;
import java.lang.ref.SoftReference;

final class SWContext {
   private final ResourceFactory factory;
   private final ShapeRenderer shapeRenderer;
   private SoftReference readBackBufferRef;
   private SoftReference imagePaintTextureRef;

   SWContext(ResourceFactory var1) {
      this.factory = var1;
      this.shapeRenderer = (ShapeRenderer)(PrismSettings.doNativePisces ? new NativeShapeRenderer() : new JavaShapeRenderer());
   }

   void renderShape(PiscesRenderer var1, Shape var2, BasicStroke var3, BaseTransform var4, Rectangle var5, boolean var6) {
      this.shapeRenderer.renderShape(var1, var2, var3, var4, var5, var6);
   }

   private SWRTTexture initRBBuffer(int var1, int var2) {
      SWRTTexture var3 = (SWRTTexture)this.factory.createRTTexture(var1, var2, Texture.WrapMode.CLAMP_NOT_NEEDED);
      this.readBackBufferRef = new SoftReference(var3);
      return var3;
   }

   private void disposeRBBuffer() {
      if (this.readBackBufferRef != null) {
         this.readBackBufferRef.clear();
         this.readBackBufferRef = null;
      }

   }

   SWRTTexture validateRBBuffer(int var1, int var2) {
      SWRTTexture var3;
      if (this.readBackBufferRef == null) {
         var3 = this.initRBBuffer(var1, var2);
      } else {
         var3 = (SWRTTexture)this.readBackBufferRef.get();
         if (var3 == null || var3.getPhysicalWidth() < var1 || var3.getPhysicalHeight() < var2) {
            this.disposeRBBuffer();
            var3 = this.initRBBuffer(var1, var2);
         }

         var3.setContentWidth(var1);
         var3.setContentHeight(var2);
      }

      return var3;
   }

   private SWArgbPreTexture initImagePaintTexture(int var1, int var2) {
      SWArgbPreTexture var3 = (SWArgbPreTexture)this.factory.createTexture(PixelFormat.INT_ARGB_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.REPEAT, var1, var2);
      this.imagePaintTextureRef = new SoftReference(var3);
      return var3;
   }

   private void disposeImagePaintTexture() {
      if (this.imagePaintTextureRef != null) {
         this.imagePaintTextureRef.clear();
         this.imagePaintTextureRef = null;
      }

   }

   SWArgbPreTexture validateImagePaintTexture(int var1, int var2) {
      SWArgbPreTexture var3;
      if (this.imagePaintTextureRef == null) {
         var3 = this.initImagePaintTexture(var1, var2);
      } else {
         var3 = (SWArgbPreTexture)this.imagePaintTextureRef.get();
         if (var3 == null || var3.getPhysicalWidth() < var1 || var3.getPhysicalHeight() < var2) {
            this.disposeImagePaintTexture();
            var3 = this.initImagePaintTexture(var1, var2);
         }

         var3.setContentWidth(var1);
         var3.setContentHeight(var2);
      }

      return var3;
   }

   void dispose() {
      this.disposeRBBuffer();
      this.disposeImagePaintTexture();
      this.shapeRenderer.dispose();
   }

   class JavaShapeRenderer implements ShapeRenderer {
      private final DirectRTPiscesAlphaConsumer alphaConsumer = new DirectRTPiscesAlphaConsumer();

      public void renderShape(PiscesRenderer var1, Shape var2, BasicStroke var3, BaseTransform var4, Rectangle var5, boolean var6) {
         if (var3 != null && var3.getType() != 0) {
            var2 = var3.createStrokedShape(var2);
            var3 = null;
         }

         try {
            Renderer var7 = OpenPiscesPrismUtils.setupRenderer(var2, var3, var4, var5, var6);
            this.alphaConsumer.initConsumer(var7, var1);
            var7.produceAlphas(this.alphaConsumer);
         } catch (Throwable var8) {
            if (PrismSettings.verbose) {
               var8.printStackTrace();
            }

            Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + var8.toString());
         }

      }

      public void dispose() {
      }
   }

   class NativeShapeRenderer implements ShapeRenderer {
      private SoftReference maskTextureRef;

      public void renderShape(PiscesRenderer var1, Shape var2, BasicStroke var3, BaseTransform var4, Rectangle var5, boolean var6) {
         try {
            MaskData var7 = ShapeUtil.rasterizeShape(var2, var3, var5.toRectBounds(), var4, true, var6);
            SWMaskTexture var8 = this.validateMaskTexture(var7.getWidth(), var7.getHeight());
            var7.uploadToTexture(var8, 0, 0, false);
            var1.fillAlphaMask(var8.getDataNoClone(), var7.getOriginX(), var7.getOriginY(), var7.getWidth(), var7.getHeight(), 0, var8.getPhysicalWidth());
         } catch (Throwable var9) {
            if (PrismSettings.verbose) {
               var9.printStackTrace();
            }

            Logging.getJavaFXLogger().warning("Cannot rasterize Shape: " + var9.toString());
         }

      }

      private SWMaskTexture initMaskTexture(int var1, int var2) {
         SWMaskTexture var3 = (SWMaskTexture)SWContext.this.factory.createMaskTexture(var1, var2, Texture.WrapMode.CLAMP_NOT_NEEDED);
         this.maskTextureRef = new SoftReference(var3);
         return var3;
      }

      private void disposeMaskTexture() {
         if (this.maskTextureRef != null) {
            this.maskTextureRef.clear();
            this.maskTextureRef = null;
         }

      }

      private SWMaskTexture validateMaskTexture(int var1, int var2) {
         SWMaskTexture var3;
         if (this.maskTextureRef == null) {
            var3 = this.initMaskTexture(var1, var2);
         } else {
            var3 = (SWMaskTexture)this.maskTextureRef.get();
            if (var3 == null || var3.getPhysicalWidth() < var1 || var3.getPhysicalHeight() < var2) {
               this.disposeMaskTexture();
               var3 = this.initMaskTexture(var1, var2);
            }
         }

         return var3;
      }

      public void dispose() {
         this.disposeMaskTexture();
      }
   }

   interface ShapeRenderer {
      void renderShape(PiscesRenderer var1, Shape var2, BasicStroke var3, BaseTransform var4, Rectangle var5, boolean var6);

      void dispose();
   }
}
