package com.sun.javafx.sg.prism;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DropShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.InnerShadow;

class EffectUtil {
   private static final int TEX_SIZE = 256;
   private static Texture itex;
   private static Texture dtex;

   static boolean renderEffectForRectangularNode(NGNode var0, Graphics var1, Effect var2, float var3, boolean var4, float var5, float var6, float var7, float var8) {
      if (!var1.getTransformNoClone().is2D() && var1.isDepthBuffer() && var1.isDepthTest()) {
         return false;
      } else {
         float var10;
         if (var2 instanceof InnerShadow && !var4) {
            InnerShadow var11 = (InnerShadow)var2;
            var10 = var11.getRadius();
            if (var10 > 0.0F && var10 < var7 / 2.0F && var10 < var8 / 2.0F && var11.getChoke() == 0.0F && var11.getShadowSourceInput() == null && var11.getContentInput() == null) {
               var0.renderContent(var1);
               renderRectInnerShadow(var1, var11, var3, var5, var6, var7, var8);
               return true;
            }
         } else if (var2 instanceof DropShadow) {
            DropShadow var9 = (DropShadow)var2;
            var10 = var9.getRadius();
            if (var10 > 0.0F && var10 < var7 / 2.0F && var10 < var8 / 2.0F && var9.getSpread() == 0.0F && var9.getShadowSourceInput() == null && var9.getContentInput() == null) {
               renderRectDropShadow(var1, var9, var3, var5, var6, var7, var8);
               var0.renderContent(var1);
               return true;
            }
         }

         return false;
      }
   }

   static void renderRectInnerShadow(Graphics var0, InnerShadow var1, float var2, float var3, float var4, float var5, float var6) {
      if (itex == null) {
         byte[] var7 = new byte[65536];
         fillGaussian(var7, 256, 128.0F, var1.getChoke(), true);
         Image var8 = Image.fromByteAlphaData((byte[])var7, 256, 256);
         itex = var0.getResourceFactory().createTexture(var8, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);

         assert itex.getWrapMode() == Texture.WrapMode.CLAMP_TO_EDGE;

         itex.contentsUseful();
         itex.makePermanent();
      }

      float var21 = var1.getRadius();
      int var22 = itex.getPhysicalWidth();
      int var9 = itex.getContentX();
      int var10 = var9 + itex.getContentWidth();
      float var11 = ((float)var9 + 0.5F) / (float)var22;
      float var12 = ((float)var10 - 0.5F) / (float)var22;
      float var15 = var3 + var5;
      float var16 = var4 + var6;
      float var17 = var3 + (float)var1.getOffsetX();
      float var18 = var4 + (float)var1.getOffsetY();
      float var19 = var17 + var5;
      float var20 = var18 + var6;
      var0.setPaint(toPrismColor(var1.getColor(), var2));
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var3, var4, var15, var18 - var21, var11, var11, var11, var11);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var17 - var21, var18 - var21, var17 + var21, var18 + var21, var11, var11, var12, var12);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var17 + var21, var18 - var21, var19 - var21, var18 + var21, var12, var11, var12, var12);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var19 - var21, var18 - var21, var19 + var21, var18 + var21, var12, var11, var11, var12);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var3, var18 - var21, var17 - var21, var20 + var21, var11, var11, var11, var11);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var17 - var21, var18 + var21, var17 + var21, var20 - var21, var11, var12, var12, var12);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var19 - var21, var18 + var21, var19 + var21, var20 - var21, var12, var12, var11, var12);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var19 + var21, var18 - var21, var15, var20 + var21, var11, var11, var11, var11);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var17 - var21, var20 - var21, var17 + var21, var20 + var21, var11, var12, var12, var11);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var17 + var21, var20 - var21, var19 - var21, var20 + var21, var12, var12, var12, var11);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var19 - var21, var20 - var21, var19 + var21, var20 + var21, var12, var12, var11, var11);
      drawClippedTexture(var0, itex, var3, var4, var15, var16, var3, var20 + var21, var15, var16, var11, var11, var11, var11);
   }

   static void drawClippedTexture(Graphics var0, Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (!(var6 >= var8) && !(var7 >= var9) && !(var2 >= var4) && !(var3 >= var5)) {
         if (var8 > var2 && var6 < var4) {
            if (var6 < var2) {
               var10 += (var12 - var10) * (var2 - var6) / (var8 - var6);
               var6 = var2;
            }

            if (var8 > var4) {
               var12 -= (var12 - var10) * (var8 - var4) / (var8 - var6);
               var8 = var4;
            }

            if (var9 > var3 && var7 < var5) {
               if (var7 < var3) {
                  var11 += (var13 - var11) * (var3 - var7) / (var9 - var7);
                  var7 = var3;
               }

               if (var9 > var5) {
                  var13 -= (var13 - var11) * (var9 - var5) / (var9 - var7);
                  var9 = var5;
               }

               var0.drawTextureRaw(var1, var6, var7, var8, var9, var10, var11, var12, var13);
            }
         }
      }
   }

   static void renderRectDropShadow(Graphics var0, DropShadow var1, float var2, float var3, float var4, float var5, float var6) {
      if (dtex == null) {
         byte[] var7 = new byte[65536];
         fillGaussian(var7, 256, 128.0F, var1.getSpread(), false);
         Image var8 = Image.fromByteAlphaData((byte[])var7, 256, 256);
         dtex = var0.getResourceFactory().createTexture(var8, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);

         assert dtex.getWrapMode() == Texture.WrapMode.CLAMP_TO_EDGE;

         dtex.contentsUseful();
         dtex.makePermanent();
      }

      float var17 = var1.getRadius();
      int var18 = dtex.getPhysicalWidth();
      int var9 = dtex.getContentX();
      int var10 = var9 + dtex.getContentWidth();
      float var11 = ((float)var9 + 0.5F) / (float)var18;
      float var12 = ((float)var10 - 0.5F) / (float)var18;
      float var13 = var3 + (float)var1.getOffsetX();
      float var14 = var4 + (float)var1.getOffsetY();
      float var15 = var13 + var5;
      float var16 = var14 + var6;
      var0.setPaint(toPrismColor(var1.getColor(), var2));
      var0.drawTextureRaw(dtex, var13 - var17, var14 - var17, var13 + var17, var14 + var17, var11, var11, var12, var12);
      var0.drawTextureRaw(dtex, var15 - var17, var14 - var17, var15 + var17, var14 + var17, var12, var11, var11, var12);
      var0.drawTextureRaw(dtex, var15 - var17, var16 - var17, var15 + var17, var16 + var17, var12, var12, var11, var11);
      var0.drawTextureRaw(dtex, var13 - var17, var16 - var17, var13 + var17, var16 + var17, var11, var12, var12, var11);
      var0.drawTextureRaw(dtex, var13 + var17, var14 + var17, var15 - var17, var16 - var17, var12, var12, var12, var12);
      var0.drawTextureRaw(dtex, var13 - var17, var14 + var17, var13 + var17, var16 - var17, var11, var12, var12, var12);
      var0.drawTextureRaw(dtex, var15 - var17, var14 + var17, var15 + var17, var16 - var17, var12, var12, var11, var12);
      var0.drawTextureRaw(dtex, var13 + var17, var14 - var17, var15 - var17, var14 + var17, var12, var11, var12, var12);
      var0.drawTextureRaw(dtex, var13 + var17, var16 - var17, var15 - var17, var16 + var17, var12, var12, var12, var11);
   }

   private static void fillGaussian(byte[] var0, int var1, float var2, float var3, boolean var4) {
      float var5 = var2 / 3.0F;
      float var6 = 2.0F * var5 * var5;
      if (var6 < Float.MIN_VALUE) {
         var6 = Float.MIN_VALUE;
      }

      float[] var7 = new float[var1];
      int var8 = (var1 + 1) / 2;
      float var9 = 0.0F;

      int var10;
      int var11;
      for(var10 = 0; var10 < var7.length; ++var10) {
         var11 = var8 - var10;
         var9 += (float)Math.exp((double)((float)(-(var11 * var11)) / var6));
         var7[var10] = var9;
      }

      for(var10 = 0; var10 < var7.length; ++var10) {
         var7[var10] /= var9;
      }

      for(var10 = 0; var10 < var1; ++var10) {
         for(var11 = 0; var11 < var1; ++var11) {
            float var12 = var7[var10] * var7[var11];
            if (var4) {
               var12 = 1.0F - var12;
            }

            int var13 = (int)(var12 * 255.0F);
            if (var13 < 0) {
               var13 = 0;
            } else if (var13 > 255) {
               var13 = 255;
            }

            var0[var10 * var1 + var11] = (byte)var13;
         }
      }

   }

   private static Color toPrismColor(Color4f var0, float var1) {
      float var2 = var0.getRed();
      float var3 = var0.getGreen();
      float var4 = var0.getBlue();
      float var5 = var0.getAlpha() * var1;
      return new Color(var2, var3, var4, var5);
   }

   private EffectUtil() {
   }
}
