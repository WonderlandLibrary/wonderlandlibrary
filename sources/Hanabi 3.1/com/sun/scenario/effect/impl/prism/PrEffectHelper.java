package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.prism.Graphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.ImagePool;

public class PrEffectHelper {
   public static void render(Effect var0, Graphics var1, float var2, float var3, Effect var4) {
      Rectangle var6 = getGraphicsClipNoClone(var1);
      BaseTransform var7 = var1.getTransformNoClone().copy();
      Object var5;
      Object var8;
      if (var7.is2D()) {
         if (var2 == 0.0F && var3 == 0.0F && var7.isIdentity()) {
            var5 = BaseTransform.IDENTITY_TRANSFORM;
         } else {
            var5 = new Affine2D(var7);
            ((Affine2D)var5).translate((double)var2, (double)var3);
         }

         var1.setTransform((BaseTransform)null);
         var8 = null;
      } else {
         double var9 = Math.hypot(var7.getMxx(), var7.getMyx());
         double var11 = Math.hypot(var7.getMxy(), var7.getMyy());
         double var13 = Math.max(var9, var11);
         if (var13 <= 1.0) {
            var5 = BaseTransform.IDENTITY_TRANSFORM;
            var8 = var7;
         } else {
            var5 = BaseTransform.getScaleInstance(var13, var13);
            var8 = new Affine3D(var7);
            var13 = 1.0 / var13;
            ((Affine3D)var8).scale(var13, var13);
         }

         NGCamera var15 = var1.getCameraNoClone();

         BaseTransform var16;
         try {
            var16 = ((BaseTransform)var8).createInverse();
         } catch (NoninvertibleTransformException var31) {
            return;
         }

         PickRay var17 = new PickRay();
         Vec3d var18 = new Vec3d();
         float var19 = (float)var6.x + 0.5F;
         float var20 = (float)var6.y + 0.5F;
         float var21 = (float)(var6.x + var6.width) - 0.5F;
         float var22 = (float)(var6.y + var6.height) - 0.5F;
         double var23 = (double)var1.getRenderTarget().getContentWidth();
         double var25 = (double)var1.getRenderTarget().getContentHeight();
         Point2D var27 = project(var19, var20, var23, var25, var15, var16, var17, var18, (Point2D)null);
         Point2D var28 = project(var21, var20, var23, var25, var15, var16, var17, var18, (Point2D)null);
         Point2D var29 = project(var19, var22, var23, var25, var15, var16, var17, var18, (Point2D)null);
         Point2D var30 = project(var21, var22, var23, var25, var15, var16, var17, var18, (Point2D)null);
         var6 = clipbounds(var27, var28, var29, var30);
      }

      Screen var32 = var1.getAssociatedScreen();
      PrFilterContext var10;
      if (var32 == null) {
         ResourceFactory var33 = var1.getResourceFactory();
         var10 = PrFilterContext.getPrinterContext(var33);
      } else {
         var10 = PrFilterContext.getInstance(var32);
      }

      PrRenderInfo var34;
      if (var8 != null) {
         var34 = null;
      } else if (var1.isDepthBuffer() && var1.isDepthTest()) {
         var34 = null;
      } else {
         var34 = new PrRenderInfo(var1);
      }

      ++ImagePool.numEffects;

      boolean var12;
      do {
         ImageData var35 = var0.filter(var10, (BaseTransform)var5, var6, var34, var4);
         if (var35 == null) {
            return;
         }

         if (var12 = var35.validate(var10)) {
            Rectangle var14 = var35.getUntransformedBounds();
            Texture var36 = ((PrTexture)var35.getUntransformedImage()).getTextureObject();
            var1.setTransform((BaseTransform)var8);
            var1.transform(var35.getTransform());
            var1.drawTexture(var36, (float)var14.x, (float)var14.y, (float)var14.width, (float)var14.height);
         }

         var35.unref();
      } while(!var12);

      var1.setTransform(var7);
   }

   static Point2D project(float var0, float var1, double var2, double var4, NGCamera var6, BaseTransform var7, PickRay var8, Vec3d var9, Point2D var10) {
      double var11 = var6.getViewWidth() / var2;
      double var13 = var6.getViewHeight() / var4;
      var0 = (float)((double)var0 * var11);
      var1 = (float)((double)var1 * var13);
      var8 = var6.computePickRay(var0, var1, var8);
      unscale(var8.getOriginNoClone(), var11, var13);
      unscale(var8.getDirectionNoClone(), var11, var13);
      return var8.projectToZeroPlane(var7, var6 instanceof NGPerspectiveCamera, var9, var10);
   }

   private static void unscale(Vec3d var0, double var1, double var3) {
      var0.x /= var1;
      var0.y /= var3;
   }

   static Rectangle clipbounds(Point2D var0, Point2D var1, Point2D var2, Point2D var3) {
      if (var0 != null && var1 != null && var2 != null && var3 != null) {
         double var4;
         double var8;
         if (var0.x < var1.x) {
            var4 = (double)var0.x;
            var8 = (double)var1.x;
         } else {
            var4 = (double)var1.x;
            var8 = (double)var0.x;
         }

         double var6;
         double var10;
         if (var0.y < var1.y) {
            var6 = (double)var0.y;
            var10 = (double)var1.y;
         } else {
            var6 = (double)var1.y;
            var10 = (double)var0.y;
         }

         if (var2.x < var3.x) {
            var4 = Math.min(var4, (double)var2.x);
            var8 = Math.max(var8, (double)var3.x);
         } else {
            var4 = Math.min(var4, (double)var3.x);
            var8 = Math.max(var8, (double)var2.x);
         }

         if (var2.y < var3.y) {
            var6 = Math.min(var6, (double)var2.y);
            var10 = Math.max(var10, (double)var3.y);
         } else {
            var6 = Math.min(var6, (double)var3.y);
            var10 = Math.max(var10, (double)var2.y);
         }

         var4 = Math.floor(var4 - 0.5);
         var6 = Math.floor(var6 - 0.5);
         var8 = Math.ceil(var8 + 0.5) - var4;
         var10 = Math.ceil(var10 + 0.5) - var6;
         int var12 = (int)var4;
         int var13 = (int)var6;
         int var14 = (int)var8;
         int var15 = (int)var10;
         if ((double)var12 == var4 && (double)var13 == var6 && (double)var14 == var8 && (double)var15 == var10) {
            return new Rectangle(var12, var13, var14, var15);
         }
      }

      return null;
   }

   public static Rectangle getGraphicsClipNoClone(Graphics var0) {
      Rectangle var1 = var0.getClipRectNoClone();
      if (var1 == null) {
         RenderTarget var2 = var0.getRenderTarget();
         var1 = new Rectangle(var2.getContentWidth(), var2.getContentHeight());
      }

      return var1;
   }

   public static void renderImageData(Graphics var0, ImageData var1, Rectangle var2) {
      int var3 = var2.width;
      int var4 = var2.height;
      PrDrawable var5 = (PrDrawable)var1.getUntransformedImage();
      BaseTransform var6 = var1.getTransform();
      Rectangle var7 = var1.getUntransformedBounds();
      float var8 = 0.0F;
      float var9 = 0.0F;
      float var10 = var8 + (float)var3;
      float var11 = var9 + (float)var4;
      if (var6.isTranslateOrIdentity()) {
         float var12 = (float)var6.getMxt();
         float var13 = (float)var6.getMyt();
         float var14 = (float)var2.x - ((float)var7.x + var12);
         float var15 = (float)var2.y - ((float)var7.y + var13);
         float var16 = var14 + (float)var3;
         float var17 = var15 + (float)var4;
         var0.drawTexture(var5.getTextureObject(), var8, var9, var10, var11, var14, var15, var16, var17);
      } else {
         float[] var18 = new float[8];
         int var19 = EffectPeer.getTextureCoordinates(var18, (float)var7.x, (float)var7.y, (float)var5.getPhysicalWidth(), (float)var5.getPhysicalHeight(), var2, var6);
         if (var19 < 8) {
            var0.drawTextureRaw(var5.getTextureObject(), var8, var9, var10, var11, var18[0], var18[1], var18[2], var18[3]);
         } else {
            var0.drawMappedTextureRaw(var5.getTextureObject(), var8, var9, var10, var11, var18[0], var18[1], var18[4], var18[5], var18[6], var18[7], var18[2], var18[3]);
         }
      }

   }
}
