package com.sun.prism.sw;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.pisces.PiscesRenderer;
import com.sun.pisces.Transform6;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;

final class SWPaint {
   private final SWContext context;
   private final PiscesRenderer pr;
   private final BaseTransform paintTx = new Affine2D();
   private final Transform6 piscesTx = new Transform6();
   private float compositeAlpha = 1.0F;
   private float px;
   private float py;
   private float pw;
   private float ph;

   SWPaint(SWContext var1, PiscesRenderer var2) {
      this.context = var1;
      this.pr = var2;
   }

   float getCompositeAlpha() {
      return this.compositeAlpha;
   }

   void setCompositeAlpha(float var1) {
      this.compositeAlpha = var1;
   }

   void setColor(Color var1, float var2) {
      if (PrismSettings.debug) {
         System.out.println("PR.setColor: " + var1);
      }

      this.pr.setColor((int)(var1.getRed() * 255.0F), (int)(255.0F * var1.getGreen()), (int)(255.0F * var1.getBlue()), (int)(255.0F * var1.getAlpha() * var2));
   }

   void setPaintFromShape(Paint var1, BaseTransform var2, Shape var3, RectBounds var4, float var5, float var6, float var7, float var8) {
      this.computePaintBounds(var1, var3, var4, var5, var6, var7, var8);
      this.setPaintBeforeDraw(var1, var2, this.px, this.py, this.pw, this.ph);
   }

   private void computePaintBounds(Paint var1, Shape var2, RectBounds var3, float var4, float var5, float var6, float var7) {
      if (var1.isProportional()) {
         if (var3 != null) {
            this.px = var3.getMinX();
            this.py = var3.getMinY();
            this.pw = var3.getWidth();
            this.ph = var3.getHeight();
         } else if (var2 != null) {
            RectBounds var8 = var2.getBounds();
            this.px = var8.getMinX();
            this.py = var8.getMinY();
            this.pw = var8.getWidth();
            this.ph = var8.getHeight();
         } else {
            this.px = var4;
            this.py = var5;
            this.pw = var6;
            this.ph = var7;
         }
      } else {
         this.px = this.py = this.pw = this.ph = 0.0F;
      }

   }

   void setPaintBeforeDraw(Paint var1, BaseTransform var2, float var3, float var4, float var5, float var6) {
      switch (var1.getType()) {
         case COLOR:
            this.setColor((Color)var1, this.compositeAlpha);
            break;
         case LINEAR_GRADIENT:
            LinearGradient var7 = (LinearGradient)var1;
            if (PrismSettings.debug) {
               System.out.println("PR.setLinearGradient: " + var7.getX1() + ", " + var7.getY1() + ", " + var7.getX2() + ", " + var7.getY2());
            }

            this.paintTx.setTransform(var2);
            SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
            float var8 = var7.getX1();
            float var9 = var7.getY1();
            float var10 = var7.getX2();
            float var11 = var7.getY2();
            if (var7.isProportional()) {
               var8 = var3 + var5 * var8;
               var9 = var4 + var6 * var9;
               var10 = var3 + var5 * var10;
               var11 = var4 + var6 * var11;
            }

            this.pr.setLinearGradient((int)(65536.0F * var8), (int)(65536.0F * var9), (int)(65536.0F * var10), (int)(65536.0F * var11), getFractions(var7), getARGB(var7, this.compositeAlpha), getPiscesGradientCycleMethod(var7.getSpreadMethod()), this.piscesTx);
            break;
         case RADIAL_GRADIENT:
            RadialGradient var12 = (RadialGradient)var1;
            if (PrismSettings.debug) {
               System.out.println("PR.setRadialGradient: " + var12.getCenterX() + ", " + var12.getCenterY() + ", " + var12.getFocusAngle() + ", " + var12.getFocusDistance() + ", " + var12.getRadius());
            }

            this.paintTx.setTransform(var2);
            float var13 = var12.getCenterX();
            float var14 = var12.getCenterY();
            float var15 = var12.getRadius();
            float var16;
            float var17;
            if (var12.isProportional()) {
               var16 = Math.min(var5, var6);
               var17 = var3 + var5 * 0.5F;
               float var20 = var4 + var6 * 0.5F;
               var13 = var17 + (var13 - 0.5F) * var16;
               var14 = var20 + (var14 - 0.5F) * var16;
               var15 *= var16;
               if (var5 != var6 && (double)var5 != 0.0 && (double)var6 != 0.0) {
                  this.paintTx.deriveWithTranslation((double)var17, (double)var20);
                  this.paintTx.deriveWithConcatenation((double)(var5 / var16), 0.0, 0.0, (double)(var6 / var16), 0.0, 0.0);
                  this.paintTx.deriveWithTranslation((double)(-var17), (double)(-var20));
               }
            }

            SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
            var16 = (float)((double)var13 + (double)(var12.getFocusDistance() * var15) * Math.cos(Math.toRadians((double)var12.getFocusAngle())));
            var17 = (float)((double)var14 + (double)(var12.getFocusDistance() * var15) * Math.sin(Math.toRadians((double)var12.getFocusAngle())));
            this.pr.setRadialGradient((int)(65536.0F * var13), (int)(65536.0F * var14), (int)(65536.0F * var16), (int)(65536.0F * var17), (int)(65536.0F * var15), getFractions(var12), getARGB(var12, this.compositeAlpha), getPiscesGradientCycleMethod(var12.getSpreadMethod()), this.piscesTx);
            break;
         case IMAGE_PATTERN:
            ImagePattern var18 = (ImagePattern)var1;
            if (var18.getImage().getPixelFormat() == PixelFormat.BYTE_ALPHA) {
               throw new UnsupportedOperationException("Alpha image is not supported as an image pattern.");
            }

            this.computeImagePatternTransform(var18, var2, var3, var4, var5, var6);
            SWArgbPreTexture var19 = this.context.validateImagePaintTexture(var18.getImage().getWidth(), var18.getImage().getHeight());
            var19.update(var18.getImage());
            if (this.compositeAlpha < 1.0F) {
               var19.applyCompositeAlpha(this.compositeAlpha);
            }

            this.pr.setTexture(1, var19.getDataNoClone(), var19.getContentWidth(), var19.getContentHeight(), var19.getPhysicalWidth(), this.piscesTx, var19.getWrapMode() == Texture.WrapMode.REPEAT, var19.getLinearFiltering(), var19.hasAlpha());
            break;
         default:
            throw new IllegalArgumentException("Unknown paint type: " + var1.getType());
      }

   }

   private static int[] getARGB(Gradient var0, float var1) {
      int var2 = var0.getNumStops();
      int[] var3 = new int[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         Stop var5 = (Stop)var0.getStops().get(var4);
         Color var6 = var5.getColor();
         float var7 = 255.0F * var6.getAlpha() * var1;
         var3[var4] = (((int)var7 & 255) << 24) + (((int)(var7 * var6.getRed()) & 255) << 16) + (((int)(var7 * var6.getGreen()) & 255) << 8) + ((int)(var7 * var6.getBlue()) & 255);
      }

      return var3;
   }

   private static int[] getFractions(Gradient var0) {
      int var1 = var0.getNumStops();
      int[] var2 = new int[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         Stop var4 = (Stop)var0.getStops().get(var3);
         var2[var3] = (int)(65536.0F * var4.getOffset());
      }

      return var2;
   }

   private static int getPiscesGradientCycleMethod(int var0) {
      switch (var0) {
         case 0:
            return 0;
         case 1:
            return 2;
         case 2:
            return 1;
         default:
            return 0;
      }
   }

   Transform6 computeDrawTexturePaintTransform(BaseTransform var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      this.paintTx.setTransform(var1);
      float var10 = this.computeScale(var2, var4, var6, var8);
      float var11 = this.computeScale(var3, var5, var7, var9);
      if (var10 == 1.0F && var11 == 1.0F) {
         this.paintTx.deriveWithTranslation((double)(-Math.min(var6, var8) + Math.min(var2, var4)), (double)(-Math.min(var7, var9) + Math.min(var3, var5)));
      } else {
         this.paintTx.deriveWithTranslation((double)Math.min(var2, var4), (double)Math.min(var3, var5));
         this.paintTx.deriveWithTranslation(var10 >= 0.0F ? 0.0 : (double)Math.abs(var4 - var2), var11 >= 0.0F ? 0.0 : (double)Math.abs(var5 - var3));
         this.paintTx.deriveWithConcatenation((double)var10, 0.0, 0.0, (double)var11, 0.0, 0.0);
         this.paintTx.deriveWithTranslation((double)(-Math.min(var6, var8)), (double)(-Math.min(var7, var9)));
      }

      SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
      return this.piscesTx;
   }

   private float computeScale(float var1, float var2, float var3, float var4) {
      float var5 = var2 - var1;
      float var6 = var5 / (var4 - var3);
      if (Math.abs(var6) > 32767.0F) {
         var6 = Math.signum(var6) * 32767.0F;
      }

      return var6;
   }

   Transform6 computeSetTexturePaintTransform(Paint var1, BaseTransform var2, RectBounds var3, float var4, float var5, float var6, float var7) {
      this.computePaintBounds(var1, (Shape)null, var3, var4, var5, var6, var7);
      ImagePattern var8 = (ImagePattern)var1;
      this.computeImagePatternTransform(var8, var2, this.px, this.py, this.pw, this.ph);
      return this.piscesTx;
   }

   private void computeImagePatternTransform(ImagePattern var1, BaseTransform var2, float var3, float var4, float var5, float var6) {
      Image var7 = var1.getImage();
      if (PrismSettings.debug) {
         System.out.println("PR.setTexturePaint: " + var7);
         System.out.println("imagePattern: x: " + var1.getX() + ", y: " + var1.getY() + ", w: " + var1.getWidth() + ", h: " + var1.getHeight() + ", proportional: " + var1.isProportional());
      }

      this.paintTx.setTransform(var2);
      this.paintTx.deriveWithConcatenation(var1.getPatternTransformNoClone());
      if (var1.isProportional()) {
         this.paintTx.deriveWithConcatenation((double)(var5 / (float)var7.getWidth() * var1.getWidth()), 0.0, 0.0, (double)(var6 / (float)var7.getHeight() * var1.getHeight()), (double)(var3 + var5 * var1.getX()), (double)(var4 + var6 * var1.getY()));
      } else {
         this.paintTx.deriveWithConcatenation((double)(var1.getWidth() / (float)var7.getWidth()), 0.0, 0.0, (double)(var1.getHeight() / (float)var7.getHeight()), (double)(var3 + var1.getX()), (double)(var4 + var1.getY()));
      }

      SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
   }
}
