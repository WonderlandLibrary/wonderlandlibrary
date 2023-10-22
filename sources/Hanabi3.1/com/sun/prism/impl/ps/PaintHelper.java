package com.sun.prism.impl.ps;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

class PaintHelper {
   static final int MULTI_MAX_FRACTIONS = 12;
   private static final int MULTI_TEXTURE_SIZE = 16;
   private static final int MULTI_CACHE_SIZE = 256;
   private static final int GTEX_CLR_TABLE_SIZE = 101;
   private static final int GTEX_CLR_TABLE_MIRRORED_SIZE = 201;
   private static final float FULL_TEXEL_Y = 0.00390625F;
   private static final float HALF_TEXEL_Y = 0.001953125F;
   private static final FloatBuffer stopVals = BufferUtil.newFloatBuffer(48);
   private static final ByteBuffer bgraColors = BufferUtil.newByteBuffer(64);
   private static final Image colorsImg;
   private static final int[] previousColors;
   private static final byte[] gtexColors;
   private static final Image gtexImg;
   private static long cacheOffset;
   private static Texture gradientCacheTexture;
   private static Texture gtexCacheTexture;
   private static final Affine2D scratchXform2D;
   private static final Affine3D scratchXform3D;
   private static Color PINK;

   private static float len(float var0, float var1) {
      return var0 == 0.0F ? Math.abs(var1) : (var1 == 0.0F ? Math.abs(var0) : (float)Math.sqrt((double)(var0 * var0 + var1 * var1)));
   }

   static void initGradientTextures(ShaderGraphics var0) {
      gradientCacheTexture = var0.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE, 16, 256);
      gradientCacheTexture.setLinearFiltering(true);
      gradientCacheTexture.contentsUseful();
      gradientCacheTexture.makePermanent();
      gtexCacheTexture = var0.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, 201, 256);
      gtexCacheTexture.setLinearFiltering(true);
      gtexCacheTexture.contentsUseful();
      gtexCacheTexture.makePermanent();
   }

   static Texture getGradientTexture(ShaderGraphics var0, Gradient var1) {
      if (gradientCacheTexture == null) {
         initGradientTextures(var0);
      }

      gradientCacheTexture.lock();
      return gradientCacheTexture;
   }

   static Texture getWrapGradientTexture(ShaderGraphics var0) {
      if (gtexCacheTexture == null) {
         initGradientTextures(var0);
      }

      gtexCacheTexture.lock();
      return gtexCacheTexture;
   }

   private static void stopsToImage(List var0, int var1) {
      if (var1 > 12) {
         throw new RuntimeException("Maximum number of gradient stops exceeded (paint uses " + var1 + " stops, but max is " + 12 + ")");
      } else {
         bgraColors.clear();
         Color var2 = null;

         for(int var3 = 0; var3 < 16; ++var3) {
            Color var4;
            if (var3 < var1) {
               var4 = ((Stop)var0.get(var3)).getColor();
               var2 = var4;
            } else {
               var4 = var2;
            }

            var4.putBgraPreBytes(bgraColors);
            int var5 = var4.getIntArgbPre();
            if (var5 != previousColors[var3]) {
               previousColors[var3] = var5;
            }
         }

         bgraColors.rewind();
      }
   }

   private static void insertInterpColor(byte[] var0, int var1, Color var2, Color var3, float var4) {
      var4 *= 255.0F;
      float var5 = 255.0F - var4;
      var1 *= 4;
      var0[var1 + 0] = (byte)((int)(var2.getBluePremult() * var5 + var3.getBluePremult() * var4 + 0.5F));
      var0[var1 + 1] = (byte)((int)(var2.getGreenPremult() * var5 + var3.getGreenPremult() * var4 + 0.5F));
      var0[var1 + 2] = (byte)((int)(var2.getRedPremult() * var5 + var3.getRedPremult() * var4 + 0.5F));
      var0[var1 + 3] = (byte)((int)(var2.getAlpha() * var5 + var3.getAlpha() * var4 + 0.5F));
   }

   private static void stopsToGtexImage(List var0, int var1) {
      Color var2 = ((Stop)var0.get(0)).getColor();
      float var3 = ((Stop)var0.get(0)).getOffset();
      int var4 = (int)(var3 * 100.0F + 0.5F);
      insertInterpColor(gtexColors, 0, var2, var2, 0.0F);

      int var5;
      int var7;
      for(var5 = 1; var5 < var1; ++var5) {
         Color var6 = ((Stop)var0.get(var5)).getColor();
         var3 = ((Stop)var0.get(var5)).getOffset();
         var7 = (int)(var3 * 100.0F + 0.5F);
         if (var7 == var4) {
            insertInterpColor(gtexColors, var7, var2, var6, 0.5F);
         } else {
            for(int var8 = var4 + 1; var8 <= var7; ++var8) {
               float var9 = (float)(var8 - var4);
               var9 /= (float)(var7 - var4);
               insertInterpColor(gtexColors, var8, var2, var6, var9);
            }
         }

         var4 = var7;
         var2 = var6;
      }

      for(var5 = 1; var5 < 101; ++var5) {
         int var10 = (100 + var5) * 4;
         var7 = (100 - var5) * 4;
         gtexColors[var10 + 0] = gtexColors[var7 + 0];
         gtexColors[var10 + 1] = gtexColors[var7 + 1];
         gtexColors[var10 + 2] = gtexColors[var7 + 2];
         gtexColors[var10 + 3] = gtexColors[var7 + 3];
      }

   }

   public static int initGradient(Gradient var0) {
      long var1 = var0.getGradientOffset();
      if (var1 >= 0L && var1 > cacheOffset - 256L) {
         return (int)(var1 % 256L);
      } else {
         List var3 = var0.getStops();
         int var4 = var0.getNumStops();
         stopsToImage(var3, var4);
         stopsToGtexImage(var3, var4);
         long var5 = ++cacheOffset;
         var0.setGradientOffset(var5);
         int var7 = (int)(var5 % 256L);
         gradientCacheTexture.update(colorsImg, 0, var7);
         gtexCacheTexture.update(gtexImg, 0, var7);
         return var7;
      }
   }

   private static void setMultiGradient(Shader var0, Gradient var1) {
      List var2 = var1.getStops();
      int var3 = var1.getNumStops();
      stopVals.clear();

      for(int var4 = 0; var4 < 12; ++var4) {
         stopVals.put(var4 < var3 ? ((Stop)var2.get(var4)).getOffset() : 0.0F);
         stopVals.put(var4 < var3 - 1 ? 1.0F / (((Stop)var2.get(var4 + 1)).getOffset() - ((Stop)var2.get(var4)).getOffset()) : 0.0F);
         stopVals.put(0.0F);
         stopVals.put(0.0F);
      }

      stopVals.rewind();
      var0.setConstants("fractions", (FloatBuffer)stopVals, 0, 12);
      float var5 = (float)initGradient(var1);
      var0.setConstant("offset", var5 / 256.0F + 0.001953125F);
   }

   private static void setTextureGradient(Shader var0, Gradient var1) {
      float var2 = (float)initGradient(var1) + 0.5F;
      float var3 = 0.5F;
      float var4 = 0.0F;
      float var5 = 0.0F;
      switch (var1.getSpreadMethod()) {
         case 0:
            var5 = 100.0F;
            break;
         case 1:
            var4 = 200.0F;
            break;
         case 2:
            var4 = 100.0F;
      }

      float var6 = 1.0F / (float)gtexCacheTexture.getPhysicalWidth();
      float var7 = 1.0F / (float)gtexCacheTexture.getPhysicalHeight();
      var3 *= var6;
      var2 *= var7;
      var4 *= var6;
      var5 *= var6;
      var0.setConstant("content", var3, var2, var4, var5);
   }

   static void setLinearGradient(ShaderGraphics var0, Shader var1, LinearGradient var2, float var3, float var4, float var5, float var6) {
      BaseTransform var7 = var2.getGradientTransformNoClone();
      Affine3D var8 = scratchXform3D;
      var0.getPaintShaderTransform(var8);
      if (var7 != null) {
         var8.concatenate(var7);
      }

      float var9 = var3 + var2.getX1() * var5;
      float var10 = var4 + var2.getY1() * var6;
      float var11 = var3 + var2.getX2() * var5;
      float var12 = var4 + var2.getY2() * var6;
      var8.translate((double)var9, (double)var10);
      float var13 = var11 - var9;
      float var14 = var12 - var10;
      double var15 = (double)len(var13, var14);
      var8.rotate(Math.atan2((double)var14, (double)var13));
      var8.scale(var15, 1.0);
      double var17;
      double var19;
      double var21;
      if (!var8.is2D()) {
         Object var23;
         try {
            var23 = var8.createInverse();
         } catch (NoninvertibleTransformException var37) {
            var8.setToScale(0.0, 0.0, 0.0);
            var23 = var8;
         }

         NGCamera var24 = var0.getCameraNoClone();
         Vec3d var25 = new Vec3d();
         PickRay var26 = new PickRay();
         PickRay var27 = project(0.0F, 0.0F, var24, (BaseTransform)var23, var26, var25, (Point2D)null);
         PickRay var28 = project(1.0F, 0.0F, var24, (BaseTransform)var23, var26, var25, (Point2D)null);
         PickRay var29 = project(0.0F, 1.0F, var24, (BaseTransform)var23, var26, var25, (Point2D)null);
         var17 = var28.getDirectionNoClone().x - var27.getDirectionNoClone().x;
         var19 = var29.getDirectionNoClone().x - var27.getDirectionNoClone().x;
         var21 = var27.getDirectionNoClone().x;
         var17 *= -var27.getOriginNoClone().z;
         var19 *= -var27.getOriginNoClone().z;
         var21 *= -var27.getOriginNoClone().z;
         double var30 = var28.getDirectionNoClone().z - var27.getDirectionNoClone().z;
         double var32 = var29.getDirectionNoClone().z - var27.getDirectionNoClone().z;
         double var34 = var27.getDirectionNoClone().z;
         var1.setConstant("gradParams", (float)var17, (float)var19, (float)var21, (float)var27.getOriginNoClone().x);
         var1.setConstant("perspVec", (float)var30, (float)var32, (float)var34);
      } else {
         try {
            var8.invert();
         } catch (NoninvertibleTransformException var36) {
            var8.setToScale(0.0, 0.0, 0.0);
         }

         var17 = (double)((float)var8.getMxx());
         var19 = (double)((float)var8.getMxy());
         var21 = (double)((float)var8.getMxt());
         var1.setConstant("gradParams", (float)var17, (float)var19, (float)var21, 0.0F);
         var1.setConstant("perspVec", 0.0F, 0.0F, 1.0F);
      }

      setMultiGradient(var1, var2);
   }

   static AffineBase getLinearGradientTx(LinearGradient var0, Shader var1, BaseTransform var2, float var3, float var4, float var5, float var6) {
      float var8 = var0.getX1();
      float var9 = var0.getY1();
      float var10 = var0.getX2();
      float var11 = var0.getY2();
      if (var0.isProportional()) {
         var8 = var3 + var8 * var5;
         var9 = var4 + var9 * var6;
         var10 = var3 + var10 * var5;
         var11 = var4 + var11 * var6;
      }

      float var12 = var10 - var8;
      float var13 = var11 - var9;
      float var14 = len(var12, var13);
      if (var0.getSpreadMethod() == 1) {
         var14 *= 2.0F;
      }

      BaseTransform var15 = var0.getGradientTransformNoClone();
      Object var7;
      if (var15.isIdentity() && var2.isIdentity()) {
         Affine2D var18 = scratchXform2D;
         var18.setToTranslation((double)var8, (double)var9);
         var18.rotate((double)var12, (double)var13);
         var18.scale((double)var14, 1.0);
         var7 = var18;
      } else {
         Affine3D var16 = scratchXform3D;
         var16.setTransform(var2);
         var16.concatenate(var15);
         var16.translate((double)var8, (double)var9);
         var16.rotate(Math.atan2((double)var13, (double)var12));
         var16.scale((double)var14, 1.0);
         var7 = var16;
      }

      try {
         ((AffineBase)var7).invert();
      } catch (NoninvertibleTransformException var17) {
         scratchXform2D.setToScale(0.0, 0.0);
         var7 = scratchXform2D;
      }

      setTextureGradient(var1, var0);
      return (AffineBase)var7;
   }

   static void setRadialGradient(ShaderGraphics var0, Shader var1, RadialGradient var2, float var3, float var4, float var5, float var6) {
      Affine3D var7 = scratchXform3D;
      var0.getPaintShaderTransform(var7);
      float var8 = var2.getRadius();
      float var9 = var2.getCenterX();
      float var10 = var2.getCenterY();
      float var11 = var2.getFocusAngle();
      float var12 = var2.getFocusDistance();
      if (var12 < 0.0F) {
         var12 = -var12;
         var11 += 180.0F;
      }

      var11 = (float)Math.toRadians((double)var11);
      float var14;
      float var15;
      if (var2.isProportional()) {
         float var13 = var3 + var5 / 2.0F;
         var14 = var4 + var6 / 2.0F;
         var15 = Math.min(var5, var6);
         var9 = (var9 - 0.5F) * var15 + var13;
         var10 = (var10 - 0.5F) * var15 + var14;
         if (var5 != var6 && var5 != 0.0F && var6 != 0.0F) {
            var7.translate((double)var13, (double)var14);
            var7.scale((double)(var5 / var15), (double)(var6 / var15));
            var7.translate((double)(-var13), (double)(-var14));
         }

         var8 *= var15;
      }

      BaseTransform var39 = var2.getGradientTransformNoClone();
      if (var39 != null) {
         var7.concatenate(var39);
      }

      var7.translate((double)var9, (double)var10);
      var7.rotate((double)var11);
      var7.scale((double)var8, (double)var8);

      try {
         var7.invert();
      } catch (Exception var38) {
         var7.setToScale(0.0, 0.0, 0.0);
      }

      if (!var7.is2D()) {
         NGCamera var40 = var0.getCameraNoClone();
         Vec3d var41 = new Vec3d();
         PickRay var16 = new PickRay();
         PickRay var17 = project(0.0F, 0.0F, var40, var7, var16, var41, (Point2D)null);
         PickRay var18 = project(1.0F, 0.0F, var40, var7, var16, var41, (Point2D)null);
         PickRay var19 = project(0.0F, 1.0F, var40, var7, var16, var41, (Point2D)null);
         double var20 = var18.getDirectionNoClone().x - var17.getDirectionNoClone().x;
         double var22 = var19.getDirectionNoClone().x - var17.getDirectionNoClone().x;
         double var24 = var17.getDirectionNoClone().x;
         double var26 = var18.getDirectionNoClone().y - var17.getDirectionNoClone().y;
         double var28 = var19.getDirectionNoClone().y - var17.getDirectionNoClone().y;
         double var30 = var17.getDirectionNoClone().y;
         var20 *= -var17.getOriginNoClone().z;
         var22 *= -var17.getOriginNoClone().z;
         var24 *= -var17.getOriginNoClone().z;
         var26 *= -var17.getOriginNoClone().z;
         var28 *= -var17.getOriginNoClone().z;
         var30 *= -var17.getOriginNoClone().z;
         double var32 = var18.getDirectionNoClone().z - var17.getDirectionNoClone().z;
         double var34 = var19.getDirectionNoClone().z - var17.getDirectionNoClone().z;
         double var36 = var17.getDirectionNoClone().z;
         var1.setConstant("perspVec", (float)var32, (float)var34, (float)var36);
         var1.setConstant("m0", (float)var20, (float)var22, (float)var24, (float)var17.getOriginNoClone().x);
         var1.setConstant("m1", (float)var26, (float)var28, (float)var30, (float)var17.getOriginNoClone().y);
      } else {
         var14 = (float)var7.getMxx();
         var15 = (float)var7.getMxy();
         float var42 = (float)var7.getMxt();
         var1.setConstant("m0", var14, var15, var42, 0.0F);
         float var43 = (float)var7.getMyx();
         float var44 = (float)var7.getMyy();
         float var45 = (float)var7.getMyt();
         var1.setConstant("m1", var43, var44, var45, 0.0F);
         var1.setConstant("perspVec", 0.0F, 0.0F, 1.0F);
      }

      var12 = Math.min(var12, 0.99F);
      var14 = 1.0F - var12 * var12;
      var15 = 1.0F / var14;
      var1.setConstant("precalc", var12, var14, var15);
      setMultiGradient(var1, var2);
   }

   static AffineBase getRadialGradientTx(RadialGradient var0, Shader var1, BaseTransform var2, float var3, float var4, float var5, float var6) {
      Affine3D var7 = scratchXform3D;
      var7.setTransform(var2);
      float var8 = var0.getRadius();
      float var9 = var0.getCenterX();
      float var10 = var0.getCenterY();
      float var11 = var0.getFocusAngle();
      float var12 = var0.getFocusDistance();
      if (var12 < 0.0F) {
         var12 = -var12;
         var11 += 180.0F;
      }

      var11 = (float)Math.toRadians((double)var11);
      float var14;
      float var15;
      if (var0.isProportional()) {
         float var13 = var3 + var5 / 2.0F;
         var14 = var4 + var6 / 2.0F;
         var15 = Math.min(var5, var6);
         var9 = (var9 - 0.5F) * var15 + var13;
         var10 = (var10 - 0.5F) * var15 + var14;
         if (var5 != var6 && var5 != 0.0F && var6 != 0.0F) {
            var7.translate((double)var13, (double)var14);
            var7.scale((double)(var5 / var15), (double)(var6 / var15));
            var7.translate((double)(-var13), (double)(-var14));
         }

         var8 *= var15;
      }

      if (var0.getSpreadMethod() == 1) {
         var8 *= 2.0F;
      }

      BaseTransform var17 = var0.getGradientTransformNoClone();
      if (var17 != null) {
         var7.concatenate(var17);
      }

      var7.translate((double)var9, (double)var10);
      var7.rotate((double)var11);
      var7.scale((double)var8, (double)var8);

      try {
         var7.invert();
      } catch (Exception var16) {
         var7.setToScale(0.0, 0.0, 0.0);
      }

      var12 = Math.min(var12, 0.99F);
      var14 = 1.0F - var12 * var12;
      var15 = 1.0F / var14;
      var1.setConstant("precalc", var12, var14, var15);
      setTextureGradient(var1, var0);
      return var7;
   }

   static void setImagePattern(ShaderGraphics var0, Shader var1, ImagePattern var2, float var3, float var4, float var5, float var6) {
      float var7 = var3 + var2.getX() * var5;
      float var8 = var4 + var2.getY() * var6;
      float var9 = var7 + var2.getWidth() * var5;
      float var10 = var8 + var2.getHeight() * var6;
      ResourceFactory var11 = var0.getResourceFactory();
      Image var12 = var2.getImage();
      Texture var13 = var11.getCachedTexture(var12, Texture.WrapMode.REPEAT);
      float var14 = (float)var13.getContentX();
      float var15 = (float)var13.getContentY();
      float var16 = (float)var13.getContentWidth();
      float var17 = (float)var13.getContentHeight();
      float var18 = (float)var13.getPhysicalWidth();
      float var19 = (float)var13.getPhysicalHeight();
      var13.unlock();
      Affine3D var20 = scratchXform3D;
      var0.getPaintShaderTransform(var20);
      BaseTransform var21 = var2.getPatternTransformNoClone();
      if (var21 != null) {
         var20.concatenate(var21);
      }

      var20.translate((double)var7, (double)var8);
      var20.scale((double)(var9 - var7), (double)(var10 - var8));
      if (var16 < var18) {
         var20.translate(0.5 / (double)var16, 0.0);
         var14 += 0.5F;
      }

      if (var17 < var19) {
         var20.translate(0.0, 0.5 / (double)var17);
         var15 += 0.5F;
      }

      try {
         var20.invert();
      } catch (Exception var46) {
         var20.setToScale(0.0, 0.0, 0.0);
      }

      if (!var20.is2D()) {
         NGCamera var22 = var0.getCameraNoClone();
         Vec3d var23 = new Vec3d();
         PickRay var24 = new PickRay();
         PickRay var25 = project(0.0F, 0.0F, var22, var20, var24, var23, (Point2D)null);
         PickRay var26 = project(1.0F, 0.0F, var22, var20, var24, var23, (Point2D)null);
         PickRay var27 = project(0.0F, 1.0F, var22, var20, var24, var23, (Point2D)null);
         double var28 = var26.getDirectionNoClone().x - var25.getDirectionNoClone().x;
         double var30 = var27.getDirectionNoClone().x - var25.getDirectionNoClone().x;
         double var32 = var25.getDirectionNoClone().x;
         double var34 = var26.getDirectionNoClone().y - var25.getDirectionNoClone().y;
         double var36 = var27.getDirectionNoClone().y - var25.getDirectionNoClone().y;
         double var38 = var25.getDirectionNoClone().y;
         var28 *= -var25.getOriginNoClone().z;
         var30 *= -var25.getOriginNoClone().z;
         var32 *= -var25.getOriginNoClone().z;
         var34 *= -var25.getOriginNoClone().z;
         var36 *= -var25.getOriginNoClone().z;
         var38 *= -var25.getOriginNoClone().z;
         double var40 = var26.getDirectionNoClone().z - var25.getDirectionNoClone().z;
         double var42 = var27.getDirectionNoClone().z - var25.getDirectionNoClone().z;
         double var44 = var25.getDirectionNoClone().z;
         var1.setConstant("perspVec", (float)var40, (float)var42, (float)var44);
         var1.setConstant("xParams", (float)var28, (float)var30, (float)var32, (float)var25.getOriginNoClone().x);
         var1.setConstant("yParams", (float)var34, (float)var36, (float)var38, (float)var25.getOriginNoClone().y);
      } else {
         float var47 = (float)var20.getMxx();
         float var48 = (float)var20.getMxy();
         float var49 = (float)var20.getMxt();
         var1.setConstant("xParams", var47, var48, var49, 0.0F);
         float var50 = (float)var20.getMyx();
         float var51 = (float)var20.getMyy();
         float var52 = (float)var20.getMyt();
         var1.setConstant("yParams", var50, var51, var52, 0.0F);
         var1.setConstant("perspVec", 0.0F, 0.0F, 1.0F);
      }

      var14 /= var18;
      var15 /= var19;
      var16 /= var18;
      var17 /= var19;
      var1.setConstant("content", var14, var15, var16, var17);
   }

   static AffineBase getImagePatternTx(ShaderGraphics var0, ImagePattern var1, Shader var2, BaseTransform var3, float var4, float var5, float var6, float var7) {
      float var8 = var1.getX();
      float var9 = var1.getY();
      float var10 = var1.getWidth();
      float var11 = var1.getHeight();
      if (var1.isProportional()) {
         var8 = var4 + var8 * var6;
         var9 = var5 + var9 * var7;
         var10 *= var6;
         var11 *= var7;
      }

      ResourceFactory var12 = var0.getResourceFactory();
      Image var13 = var1.getImage();
      Texture var14 = var12.getCachedTexture(var13, Texture.WrapMode.REPEAT);
      float var15 = (float)var14.getContentX();
      float var16 = (float)var14.getContentY();
      float var17 = (float)var14.getContentWidth();
      float var18 = (float)var14.getContentHeight();
      float var19 = (float)var14.getPhysicalWidth();
      float var20 = (float)var14.getPhysicalHeight();
      var14.unlock();
      BaseTransform var22 = var1.getPatternTransformNoClone();
      Object var21;
      if (var22.isIdentity() && var3.isIdentity()) {
         Affine2D var25 = scratchXform2D;
         var25.setToTranslation((double)var8, (double)var9);
         var25.scale((double)var10, (double)var11);
         var21 = var25;
      } else {
         Affine3D var23 = scratchXform3D;
         var23.setTransform(var3);
         var23.concatenate(var22);
         var23.translate((double)var8, (double)var9);
         var23.scale((double)var10, (double)var11);
         var21 = var23;
      }

      if (var17 < var19) {
         ((AffineBase)var21).translate(0.5 / (double)var17, 0.0);
         var15 += 0.5F;
      }

      if (var18 < var20) {
         ((AffineBase)var21).translate(0.0, 0.5 / (double)var18);
         var16 += 0.5F;
      }

      try {
         ((AffineBase)var21).invert();
      } catch (Exception var24) {
         var21 = scratchXform2D;
         scratchXform2D.setToScale(0.0, 0.0);
      }

      var15 /= var19;
      var16 /= var20;
      var17 /= var19;
      var18 /= var20;
      var2.setConstant("content", var15, var16, var17, var18);
      return (AffineBase)var21;
   }

   static PickRay project(float var0, float var1, NGCamera var2, BaseTransform var3, PickRay var4, Vec3d var5, Point2D var6) {
      var4 = var2.computePickRay(var0, var1, var4);
      return var4.project(var3, var2 instanceof NGPerspectiveCamera, var5, var6);
   }

   static {
      colorsImg = Image.fromByteBgraPreData((ByteBuffer)bgraColors, 16, 1);
      previousColors = new int[16];
      gtexColors = new byte[804];
      gtexImg = Image.fromByteBgraPreData((ByteBuffer)ByteBuffer.wrap(gtexColors), 201, 1);
      cacheOffset = -1L;
      gradientCacheTexture = null;
      gtexCacheTexture = null;
      scratchXform2D = new Affine2D();
      scratchXform3D = new Affine3D();
      PINK = new Color(1.0F, 0.078431375F, 0.5764706F, 1.0F);
   }
}
