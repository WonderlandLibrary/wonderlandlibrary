package com.sun.prism.impl.ps;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.MultiTexture;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.GlyphCache;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.security.AccessController;

public abstract class BaseShaderGraphics extends BaseGraphics implements ShaderGraphics, ReadbackGraphics, MaskTextureGraphics {
   private static Affine2D TEMP_TX2D = new Affine2D();
   private static Affine3D TEMP_TX3D = new Affine3D();
   private final BaseShaderContext context;
   private Shader externalShader;
   private boolean isComplexPaint;
   private NGLightBase[] lights = null;
   private static RectBounds TMP_BOUNDS = new RectBounds();
   private static final float FRINGE_FACTOR;
   private static final double SQRT_2;
   private boolean lcdSampleInvalid = false;

   protected BaseShaderGraphics(BaseShaderContext var1, RenderTarget var2) {
      super(var1, var2);
      this.context = var1;
   }

   BaseShaderContext getContext() {
      return this.context;
   }

   boolean isComplexPaint() {
      return this.isComplexPaint;
   }

   public void getPaintShaderTransform(Affine3D var1) {
      var1.setTransform(this.getTransformNoClone());
   }

   public Shader getExternalShader() {
      return this.externalShader;
   }

   public void setExternalShader(Shader var1) {
      this.externalShader = var1;
      this.context.setExternalShader(this, var1);
   }

   public void setPaint(Paint var1) {
      if (var1.getType().isGradient()) {
         Gradient var2 = (Gradient)var1;
         this.isComplexPaint = var2.getNumStops() > 12;
      } else {
         this.isComplexPaint = false;
      }

      super.setPaint(var1);
   }

   public void setLights(NGLightBase[] var1) {
      this.lights = var1;
   }

   public final NGLightBase[] getLights() {
      return this.lights;
   }

   public void drawTexture(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      if (var1 instanceof MultiTexture) {
         this.drawMultiTexture((MultiTexture)var1, var2, var3, var4, var5, var6, var7, var8, var9);
      } else {
         super.drawTexture(var1, var2, var3, var4, var5, var6, var7, var8, var9);
      }

   }

   public void drawTexture3SliceH(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (!(var1 instanceof MultiTexture)) {
         super.drawTexture3SliceH(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      } else {
         MultiTexture var14 = (MultiTexture)var1;
         this.drawMultiTexture(var14, var2, var3, var10, var5, var6, var7, var12, var9);
         this.drawMultiTexture(var14, var10, var3, var11, var5, var12, var7, var13, var9);
         this.drawMultiTexture(var14, var11, var3, var4, var5, var13, var7, var8, var9);
      }
   }

   public void drawTexture3SliceV(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (!(var1 instanceof MultiTexture)) {
         super.drawTexture3SliceV(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      } else {
         MultiTexture var14 = (MultiTexture)var1;
         this.drawMultiTexture(var14, var2, var3, var4, var10, var6, var7, var8, var12);
         this.drawMultiTexture(var14, var2, var10, var4, var11, var6, var12, var8, var13);
         this.drawMultiTexture(var14, var2, var11, var4, var5, var6, var13, var8, var9);
      }
   }

   public void drawTexture9Slice(Texture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17) {
      if (!(var1 instanceof MultiTexture)) {
         super.drawTexture9Slice(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      } else {
         MultiTexture var18 = (MultiTexture)var1;
         this.drawMultiTexture(var18, var2, var3, var10, var11, var6, var7, var14, var15);
         this.drawMultiTexture(var18, var10, var3, var12, var11, var14, var7, var16, var15);
         this.drawMultiTexture(var18, var12, var3, var4, var11, var16, var7, var8, var15);
         this.drawMultiTexture(var18, var2, var11, var10, var13, var6, var15, var14, var17);
         this.drawMultiTexture(var18, var10, var11, var12, var13, var14, var15, var16, var17);
         this.drawMultiTexture(var18, var12, var11, var4, var13, var16, var15, var8, var17);
         this.drawMultiTexture(var18, var2, var13, var10, var5, var6, var17, var14, var9);
         this.drawMultiTexture(var18, var10, var13, var12, var5, var14, var17, var16, var9);
         this.drawMultiTexture(var18, var12, var13, var4, var5, var16, var17, var8, var9);
      }
   }

   private static float calculateScaleFactor(float var0, float var1) {
      return var0 == var1 ? 1.0F : (var0 - 1.0F) / var1;
   }

   protected void drawMultiTexture(MultiTexture var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      BaseTransform var10 = this.getTransformNoClone();
      if (this.isSimpleTranslate) {
         var10 = IDENT;
         var2 += this.transX;
         var3 += this.transY;
         var4 += this.transX;
         var5 += this.transY;
      }

      Texture[] var11 = var1.getTextures();
      Shader var12 = this.context.validateTextureOp(this, var10, var11, var1.getPixelFormat());
      if (null != var12) {
         if (var1.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            Texture var13 = var11[0];
            Texture var14 = var11[2];
            Texture var15 = var11[1];
            float var16 = (float)var1.getContentWidth();
            float var17 = (float)var1.getContentHeight();
            float var18 = calculateScaleFactor(var16, (float)var13.getPhysicalWidth());
            float var19 = calculateScaleFactor(var17, (float)var13.getPhysicalHeight());
            float var20;
            float var21;
            if (var11.length > 3) {
               Texture var26 = var11[3];
               var20 = calculateScaleFactor(var16, (float)var26.getPhysicalWidth());
               var21 = calculateScaleFactor(var17, (float)var26.getPhysicalHeight());
            } else {
               var21 = 0.0F;
               var20 = 0.0F;
            }

            float var33 = (float)Math.floor((double)var16 / 2.0);
            float var27 = (float)Math.floor((double)var17 / 2.0);
            float var22 = calculateScaleFactor(var33, (float)var14.getPhysicalWidth());
            float var23 = calculateScaleFactor(var27, (float)var14.getPhysicalHeight());
            float var24 = calculateScaleFactor(var33, (float)var15.getPhysicalWidth());
            float var25 = calculateScaleFactor(var27, (float)var15.getPhysicalHeight());
            var12.setConstant("lumaAlphaScale", var18, var19, var20, var21);
            var12.setConstant("cbCrScale", var22, var23, var24, var25);
            float var28 = var6 / var16;
            float var29 = var7 / var17;
            float var30 = var8 / var16;
            float var31 = var9 / var17;
            VertexBuffer var32 = this.context.getVertexBuffer();
            var32.addQuad(var2, var3, var4, var5, var28, var29, var30, var31);
         } else {
            throw new UnsupportedOperationException("Unsupported multitexture format " + var1.getPixelFormat());
         }
      }
   }

   public void drawTextureRaw2(Texture var1, Texture var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14) {
      BaseTransform var15 = this.getTransformNoClone();
      if (this.isSimpleTranslate) {
         var15 = IDENT;
         var3 += this.transX;
         var4 += this.transY;
         var5 += this.transX;
         var6 += this.transY;
      }

      this.context.validateTextureOp(this, var15, var1, var2, PixelFormat.INT_ARGB_PRE);
      VertexBuffer var16 = this.context.getVertexBuffer();
      var16.addQuad(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14);
   }

   public void drawMappedTextureRaw2(Texture var1, Texture var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22) {
      BaseTransform var23 = this.getTransformNoClone();
      if (this.isSimpleTranslate) {
         var23 = IDENT;
         var3 += this.transX;
         var4 += this.transY;
         var5 += this.transX;
         var6 += this.transY;
      }

      this.context.validateTextureOp(this, var23, var1, var2, PixelFormat.INT_ARGB_PRE);
      VertexBuffer var24 = this.context.getVertexBuffer();
      var24.addMappedQuad(var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var22);
   }

   public void drawPixelsMasked(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      if (var5 > 0 && var6 > 0) {
         float var11 = (float)var1.getPhysicalWidth();
         float var12 = (float)var1.getPhysicalHeight();
         float var13 = (float)var2.getPhysicalWidth();
         float var14 = (float)var2.getPhysicalHeight();
         float var15 = (float)var3;
         float var16 = (float)var4;
         float var17 = (float)(var3 + var5);
         float var18 = (float)(var4 + var6);
         float var19 = (float)var7 / var11;
         float var20 = (float)var8 / var12;
         float var21 = (float)(var7 + var5) / var11;
         float var22 = (float)(var8 + var6) / var12;
         float var23 = (float)var9 / var13;
         float var24 = (float)var10 / var14;
         float var25 = (float)(var9 + var5) / var13;
         float var26 = (float)(var10 + var6) / var14;
         this.context.validateMaskTextureOp(this, IDENT, var1, var2, PixelFormat.INT_ARGB_PRE);
         VertexBuffer var27 = this.context.getVertexBuffer();
         var27.addQuad(var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25, var26);
      }
   }

   public void maskInterpolatePixels(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      if (var5 > 0 && var6 > 0) {
         float var11 = (float)var1.getPhysicalWidth();
         float var12 = (float)var1.getPhysicalHeight();
         float var13 = (float)var2.getPhysicalWidth();
         float var14 = (float)var2.getPhysicalHeight();
         float var15 = (float)var3;
         float var16 = (float)var4;
         float var17 = (float)(var3 + var5);
         float var18 = (float)(var4 + var6);
         float var19 = (float)var7 / var11;
         float var20 = (float)var8 / var12;
         float var21 = (float)(var7 + var5) / var11;
         float var22 = (float)(var8 + var6) / var12;
         float var23 = (float)var9 / var13;
         float var24 = (float)var10 / var14;
         float var25 = (float)(var9 + var5) / var13;
         float var26 = (float)(var10 + var6) / var14;
         CompositeMode var27 = this.getCompositeMode();
         this.setCompositeMode(CompositeMode.DST_OUT);
         this.context.validateTextureOp((BaseGraphics)this, IDENT, (Texture)var2, PixelFormat.INT_ARGB_PRE);
         VertexBuffer var28 = this.context.getVertexBuffer();
         var28.addQuad(var15, var16, var17, var18, var23, var24, var25, var26);
         this.setCompositeMode(CompositeMode.ADD);
         this.context.validateMaskTextureOp(this, IDENT, var1, var2, PixelFormat.INT_ARGB_PRE);
         var28.addQuad(var15, var16, var17, var18, var19, var20, var21, var22, var23, var24, var25, var26);
         this.setCompositeMode(var27);
      }
   }

   private void renderWithComplexPaint(Shape var1, BasicStroke var2, float var3, float var4, float var5, float var6) {
      this.context.flushVertexBuffer();
      BaseTransform var7 = this.getTransformNoClone();
      MaskData var8 = ShapeUtil.rasterizeShape(var1, var2, this.getFinalClipNoClone(), var7, true, this.isAntialiasedShape());
      int var9 = var8.getWidth();
      int var10 = var8.getHeight();
      float var11 = (float)var8.getOriginX();
      float var12 = (float)var8.getOriginY();
      float var13 = var11 + (float)var9;
      float var14 = var12 + (float)var10;
      Gradient var15 = (Gradient)this.paint;
      TEMP_TX2D.setToTranslation((double)(-var11), (double)(-var12));
      TEMP_TX2D.concatenate(var7);
      Texture var16 = this.context.getGradientTexture(var15, TEMP_TX2D, var9, var10, var8, var3, var4, var5, var6);
      float var17 = 0.0F;
      float var18 = 0.0F;
      float var19 = var17 + (float)var9 / (float)var16.getPhysicalWidth();
      float var20 = var18 + (float)var10 / (float)var16.getPhysicalHeight();
      VertexBuffer var21 = this.context.getVertexBuffer();
      this.context.validateTextureOp(this, IDENT, var16, (Texture)null, var16.getPixelFormat());
      var21.addQuad(var11, var12, var13, var14, var17, var18, var19, var20);
      var16.unlock();
   }

   protected void renderShape(Shape var1, BasicStroke var2, float var3, float var4, float var5, float var6) {
      if (this.isComplexPaint) {
         this.renderWithComplexPaint(var1, var2, var3, var4, var5, var6);
      } else {
         BaseTransform var7 = this.getTransformNoClone();
         MaskData var8 = ShapeUtil.rasterizeShape(var1, var2, this.getFinalClipNoClone(), var7, true, this.isAntialiasedShape());
         Texture var9 = this.context.validateMaskTexture(var8, false);
         AffineBase var10;
         if (PrismSettings.primTextureSize != 0) {
            Shader var11 = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, var9, var3, var4, var5, var6);
            var10 = this.getPaintTextureTx(var7, var11, var3, var4, var5, var6);
         } else {
            this.context.validatePaintOp(this, IDENT, var9, var3, var4, var5, var6);
            var10 = null;
         }

         this.context.updateMaskTexture(var8, TMP_BOUNDS, false);
         float var20 = (float)var8.getOriginX();
         float var12 = (float)var8.getOriginY();
         float var13 = var20 + (float)var8.getWidth();
         float var14 = var12 + (float)var8.getHeight();
         float var15 = TMP_BOUNDS.getMinX();
         float var16 = TMP_BOUNDS.getMinY();
         float var17 = TMP_BOUNDS.getMaxX();
         float var18 = TMP_BOUNDS.getMaxY();
         VertexBuffer var19 = this.context.getVertexBuffer();
         var19.addQuad(var20, var12, var13, var14, var15, var16, var17, var18, var10);
         var9.unlock();
      }
   }

   private static float getStrokeExpansionFactor(BasicStroke var0) {
      if (var0.getType() == 2) {
         return 1.0F;
      } else {
         return var0.getType() == 0 ? 0.5F : 0.0F;
      }
   }

   private BaseTransform extract3Dremainder(BaseTransform var1) {
      if (var1.is2D()) {
         return IDENT;
      } else {
         TEMP_TX3D.setTransform(var1);
         TEMP_TX2D.setTransform(var1.getMxx(), var1.getMyx(), var1.getMxy(), var1.getMyy(), var1.getMxt(), var1.getMyt());

         try {
            TEMP_TX2D.invert();
            TEMP_TX3D.concatenate(TEMP_TX2D);
         } catch (NoninvertibleTransformException var3) {
         }

         return TEMP_TX3D;
      }
   }

   private void renderGeneralRoundedRect(float var1, float var2, float var3, float var4, float var5, float var6, BaseShaderContext.MaskType var7, BasicStroke var8) {
      float var9;
      float var10;
      float var11;
      float var12;
      float var13;
      float var14;
      if (var8 == null) {
         var9 = var1;
         var10 = var2;
         var11 = var3;
         var12 = var4;
         var14 = 0.0F;
         var13 = 0.0F;
      } else {
         float var21 = var8.getLineWidth();
         float var22 = getStrokeExpansionFactor(var8) * var21;
         var9 = var1 - var22;
         var10 = var2 - var22;
         var22 *= 2.0F;
         var11 = var3 + var22;
         var12 = var4 + var22;
         if (var5 > 0.0F && var6 > 0.0F) {
            var5 += var22;
            var6 += var22;
         } else if (var8.getLineJoin() == 1) {
            var6 = var22;
            var5 = var22;
            var7 = BaseShaderContext.MaskType.DRAW_ROUNDRECT;
         } else {
            var6 = 0.0F;
            var5 = 0.0F;
         }

         var13 = (var11 - var21 * 2.0F) / var11;
         var14 = (var12 - var21 * 2.0F) / var12;
         if (var13 <= 0.0F || var14 <= 0.0F) {
            var7 = var7.getFillType();
         }
      }

      BaseTransform var25 = this.getTransformNoClone();
      float var15;
      float var16;
      float var17;
      float var18;
      float var19;
      float var20;
      BaseTransform var26;
      if (this.isSimpleTranslate) {
         var20 = 1.0F;
         var17 = 1.0F;
         var19 = 0.0F;
         var18 = 0.0F;
         var15 = var9 + this.transX;
         var16 = var10 + this.transY;
         var26 = IDENT;
      } else {
         var26 = this.extract3Dremainder(var25);
         var17 = (float)var25.getMxx();
         var19 = (float)var25.getMxy();
         var18 = (float)var25.getMyx();
         var20 = (float)var25.getMyy();
         var15 = var9 * var17 + var10 * var19 + (float)var25.getMxt();
         var16 = var9 * var18 + var10 * var20 + (float)var25.getMyt();
      }

      var17 *= var11;
      var18 *= var11;
      var19 *= var12;
      var20 *= var12;
      float var23 = var5 / var11;
      float var24 = var6 / var12;
      this.renderGeneralRoundedPgram(var15, var16, var17, var18, var19, var20, var23, var24, var13, var14, var26, var7, var1, var2, var3, var4);
   }

   private void renderGeneralRoundedPgram(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, BaseTransform var11, BaseShaderContext.MaskType var12, float var13, float var14, float var15, float var16) {
      float var17 = len(var3, var4);
      float var18 = len(var5, var6);
      if (var17 != 0.0F && var18 != 0.0F) {
         float var21 = var1 + var3;
         float var22 = var2 + var4;
         float var23 = var1 + var5;
         float var24 = var2 + var6;
         float var25 = var21 + var5;
         float var26 = var22 + var6;
         float var27 = (var3 * var6 - var4 * var5) * 0.5F;
         float var28 = var27 / var18;
         float var29 = var27 / var17;
         if (var28 < 0.0F) {
            var28 = -var28;
         }

         if (var29 < 0.0F) {
            var29 = -var29;
         }

         float var30 = var3 / var17;
         float var31 = var4 / var17;
         float var32 = var5 / var18;
         float var33 = var6 / var18;
         float var34 = -var5 * (var30 + var32) - var6 * (var31 + var33);
         float var35 = var6 * var3 - var5 * var4;
         float var36 = var34 / var35;
         float var37 = FRINGE_FACTOR * Math.signum(var36);
         float var38 = (var36 * var3 + var31) * var37;
         float var39 = (var36 * var4 - var30) * var37;
         float var19 = var1 + var38;
         float var20 = var2 + var39;
         var25 -= var38;
         var26 -= var39;
         var34 = var4 * (var33 - var31) - var3 * (var30 - var32);
         var36 = var34 / var35;
         var37 = FRINGE_FACTOR * Math.signum(var36);
         var38 = (var36 * var5 + var33) * var37;
         var39 = (var36 * var6 - var32) * var37;
         var21 += var38;
         var22 += var39;
         var23 -= var38;
         var24 -= var39;
         float var40 = (var19 + var25) * 0.5F;
         float var41 = (var20 + var26) * 0.5F;
         float var42 = var40 * var33 - var41 * var32;
         float var43 = var40 * var31 - var41 * var30;
         float var44 = var19 * var33 - var20 * var32 - var42;
         float var45 = var19 * var31 - var20 * var30 - var43;
         float var46 = var21 * var33 - var22 * var32 - var42;
         float var47 = var21 * var31 - var22 * var30 - var43;
         float var48 = var23 * var33 - var24 * var32 - var42;
         float var49 = var23 * var31 - var24 * var30 - var43;
         float var50 = var25 * var33 - var26 * var32 - var42;
         float var51 = var25 * var31 - var26 * var30 - var43;
         float var52;
         float var53;
         if (var12 == BaseShaderContext.MaskType.DRAW_ROUNDRECT || var12 == BaseShaderContext.MaskType.FILL_ROUNDRECT) {
            var52 = var28 * var7;
            var53 = var29 * var8;
            if (!((double)var52 < 0.5) && !((double)var53 < 0.5)) {
               float var54 = var28 - var52;
               float var55 = var29 - var53;
               float var56;
               float var57;
               if (var12 == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                  float var58 = var28 * var9;
                  float var59 = var29 * var10;
                  var56 = var58 - var54;
                  var57 = var59 - var55;
                  if (!(var56 < 0.5F) && !(var57 < 0.5F)) {
                     var56 = 1.0F / var56;
                     var57 = 1.0F / var57;
                  } else {
                     var56 = var58;
                     var57 = var59;
                     var12 = BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT;
                  }
               } else {
                  var57 = 0.0F;
                  var56 = 0.0F;
               }

               var52 = 1.0F / var52;
               var53 = 1.0F / var53;
               Shader var61 = this.context.validatePaintOp(this, var11, var12, var13, var14, var15, var16, var52, var53, var56, var57, 0.0F, 0.0F);
               var61.setConstant("oinvarcradii", var52, var53);
               if (var12 == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                  var61.setConstant("iinvarcradii", var56, var57);
               } else if (var12 == BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT) {
                  var61.setConstant("idim", var56, var57);
               }

               var28 = var54;
               var29 = var55;
            } else {
               var12 = var12 == BaseShaderContext.MaskType.DRAW_ROUNDRECT ? BaseShaderContext.MaskType.DRAW_PGRAM : BaseShaderContext.MaskType.FILL_PGRAM;
            }
         }

         if (var12 != BaseShaderContext.MaskType.DRAW_PGRAM && var12 != BaseShaderContext.MaskType.DRAW_ELLIPSE) {
            if (var12 == BaseShaderContext.MaskType.FILL_ELLIPSE) {
               if ((double)Math.abs(var28 - var29) < 0.01) {
                  var12 = BaseShaderContext.MaskType.FILL_CIRCLE;
                  var29 = (float)Math.min(1.0, (double)(var29 * var29) * Math.PI);
               } else {
                  var28 = 1.0F / var28;
                  var29 = 1.0F / var29;
                  var44 *= var28;
                  var45 *= var29;
                  var46 *= var28;
                  var47 *= var29;
                  var48 *= var28;
                  var49 *= var29;
                  var50 *= var28;
                  var51 *= var29;
               }

               this.context.validatePaintOp(this, var11, var12, var13, var14, var15, var16);
            } else if (var12 == BaseShaderContext.MaskType.FILL_PGRAM) {
               this.context.validatePaintOp(this, var11, var12, var13, var14, var15, var16);
            }
         } else {
            var52 = var28 * var9;
            var53 = var29 * var10;
            if (var12 == BaseShaderContext.MaskType.DRAW_ELLIPSE) {
               if ((double)Math.abs(var28 - var29) < 0.01) {
                  var12 = BaseShaderContext.MaskType.DRAW_CIRCLE;
                  var29 = (float)Math.min(1.0, (double)(var29 * var29) * Math.PI);
                  var53 = (float)Math.min(1.0, (double)(var53 * var53) * Math.PI);
               } else {
                  var28 = 1.0F / var28;
                  var29 = 1.0F / var29;
                  var52 = 1.0F / var52;
                  var53 = 1.0F / var53;
               }
            }

            Shader var60 = this.context.validatePaintOp(this, var11, var12, var13, var14, var15, var16, var52, var53, 0.0F, 0.0F, 0.0F, 0.0F);
            var60.setConstant("idim", var52, var53);
         }

         this.context.getVertexBuffer().addMappedPgram(var19, var20, var21, var22, var23, var24, var25, var26, var44, var45, var46, var47, var48, var49, var50, var51, var28, var29);
      }
   }

   AffineBase getPaintTextureTx(BaseTransform var1, Shader var2, float var3, float var4, float var5, float var6) {
      switch (this.paint.getType()) {
         case COLOR:
            return null;
         case LINEAR_GRADIENT:
            return PaintHelper.getLinearGradientTx((LinearGradient)this.paint, var2, var1, var3, var4, var5, var6);
         case RADIAL_GRADIENT:
            return PaintHelper.getRadialGradientTx((RadialGradient)this.paint, var2, var1, var3, var4, var5, var6);
         case IMAGE_PATTERN:
            return PaintHelper.getImagePatternTx(this, (ImagePattern)this.paint, var2, var1, var3, var4, var5, var6);
         default:
            throw new InternalError("Unrecogized paint type: " + this.paint);
      }
   }

   boolean fillPrimRect(float var1, float var2, float var3, float var4, Texture var5, Texture var6, float var7, float var8, float var9, float var10) {
      BaseTransform var11 = this.getTransformNoClone();
      float var12 = (float)var11.getMxx();
      float var13 = (float)var11.getMxy();
      float var14 = (float)var11.getMxt();
      float var15 = (float)var11.getMyx();
      float var16 = (float)var11.getMyy();
      float var17 = (float)var11.getMyt();
      float var18 = len(var12, var15);
      float var19 = len(var13, var16);
      if (var18 != 0.0F && var19 != 0.0F) {
         float var20 = 1.0F / var18;
         float var21 = 1.0F / var19;
         float var22 = var1 - var20 * 0.5F;
         float var23 = var2 - var21 * 0.5F;
         float var24 = var1 + var3 + var20 * 0.5F;
         float var25 = var2 + var4 + var21 * 0.5F;
         int var26 = (int)Math.ceil((double)(var3 * var18 - 0.001953125F));
         int var27 = (int)Math.ceil((double)(var4 * var19 - 0.001953125F));
         VertexBuffer var28 = this.context.getVertexBuffer();
         int var29 = this.context.getRectTextureMaxSize();
         float var30;
         float var31;
         float var32;
         float var33;
         if (var26 <= var29 && var27 <= var29) {
            var30 = (float)(var26 * (var26 + 1) / 2) - 0.5F;
            var31 = (float)(var27 * (var27 + 1) / 2) - 0.5F;
            var32 = var30 + (float)var26 + 1.0F;
            var33 = var31 + (float)var27 + 1.0F;
            var30 /= (float)var5.getPhysicalWidth();
            var31 /= (float)var5.getPhysicalHeight();
            var32 /= (float)var5.getPhysicalWidth();
            var33 /= (float)var5.getPhysicalHeight();
            Shader var60;
            AffineBase var61;
            if (var11.isTranslateOrIdentity()) {
               var22 += var14;
               var23 += var17;
               var24 += var14;
               var25 += var17;
               var11 = IDENT;
            } else {
               if (var11.is2D()) {
                  var60 = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, var5, var7, var8, var9, var10);
                  var61 = this.getPaintTextureTx(IDENT, var60, var7, var8, var9, var10);
                  if (var61 == null) {
                     var28.addMappedPgram(var22 * var12 + var23 * var13 + var14, var22 * var15 + var23 * var16 + var17, var24 * var12 + var23 * var13 + var14, var24 * var15 + var23 * var16 + var17, var22 * var12 + var25 * var13 + var14, var22 * var15 + var25 * var16 + var17, var24 * var12 + var25 * var13 + var14, var24 * var15 + var25 * var16 + var17, var30, var31, var32, var31, var30, var33, var32, var33, 0.0F, 0.0F);
                  } else {
                     var28.addMappedPgram(var22 * var12 + var23 * var13 + var14, var22 * var15 + var23 * var16 + var17, var24 * var12 + var23 * var13 + var14, var24 * var15 + var23 * var16 + var17, var22 * var12 + var25 * var13 + var14, var22 * var15 + var25 * var16 + var17, var24 * var12 + var25 * var13 + var14, var24 * var15 + var25 * var16 + var17, var30, var31, var32, var31, var30, var33, var32, var33, var22, var23, var24, var25, var61);
                  }

                  return true;
               }

               System.out.println("Not a 2d transform!");
               var17 = 0.0F;
               var14 = 0.0F;
            }

            var60 = this.context.validatePaintOp(this, var11, BaseShaderContext.MaskType.ALPHA_TEXTURE, var5, var7, var8, var9, var10);
            var61 = this.getPaintTextureTx(IDENT, var60, var7, var8, var9, var10);
            if (var61 == null) {
               var28.addQuad(var22, var23, var24, var25, var30, var31, var32, var33);
            } else {
               var61.translate((double)(-var14), (double)(-var17));
               var28.addQuad(var22, var23, var24, var25, var30, var31, var32, var33, var61);
            }

            return true;
         } else if (var6 == null) {
            return false;
         } else {
            var30 = 0.5F / (float)var6.getPhysicalWidth();
            var31 = 0.5F / (float)var6.getPhysicalHeight();
            var32 = ((float)var26 * 0.5F + 1.0F) / (float)var6.getPhysicalWidth();
            var33 = ((float)var27 * 0.5F + 1.0F) / (float)var6.getPhysicalHeight();
            float var34 = var1 + var3 * 0.5F;
            float var35 = var2 + var4 * 0.5F;
            Shader var36;
            AffineBase var37;
            if (var11.isTranslateOrIdentity()) {
               var22 += var14;
               var23 += var17;
               var34 += var14;
               var35 += var17;
               var24 += var14;
               var25 += var17;
               var11 = IDENT;
            } else {
               if (var11.is2D()) {
                  var36 = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, var6, var7, var8, var9, var10);
                  var37 = this.getPaintTextureTx(IDENT, var36, var7, var8, var9, var10);
                  float var38 = var12 * var22;
                  float var39 = var15 * var22;
                  float var40 = var13 * var23;
                  float var41 = var16 * var23;
                  float var42 = var12 * var34;
                  float var43 = var15 * var34;
                  float var44 = var13 * var35;
                  float var45 = var16 * var35;
                  float var46 = var12 * var24;
                  float var47 = var15 * var24;
                  float var48 = var13 * var25;
                  float var49 = var16 * var25;
                  float var50 = var42 + var44 + var14;
                  float var51 = var43 + var45 + var17;
                  float var52 = var42 + var40 + var14;
                  float var53 = var43 + var41 + var17;
                  float var54 = var38 + var44 + var14;
                  float var55 = var39 + var45 + var17;
                  float var56 = var42 + var48 + var14;
                  float var57 = var43 + var49 + var17;
                  float var58 = var46 + var44 + var14;
                  float var59 = var47 + var45 + var17;
                  if (var37 == null) {
                     var28.addMappedPgram(var22 * var12 + var23 * var13 + var14, var22 * var15 + var23 * var16 + var17, var52, var53, var54, var55, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, 0.0F, 0.0F);
                     var28.addMappedPgram(var24 * var12 + var23 * var13 + var14, var24 * var15 + var23 * var16 + var17, var52, var53, var58, var59, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, 0.0F, 0.0F);
                     var28.addMappedPgram(var22 * var12 + var25 * var13 + var14, var22 * var15 + var25 * var16 + var17, var56, var57, var54, var55, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, 0.0F, 0.0F);
                     var28.addMappedPgram(var24 * var12 + var25 * var13 + var14, var24 * var15 + var25 * var16 + var17, var56, var57, var58, var59, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, 0.0F, 0.0F);
                  } else {
                     var28.addMappedPgram(var22 * var12 + var23 * var13 + var14, var22 * var15 + var23 * var16 + var17, var52, var53, var54, var55, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, var22, var23, var34, var35, var37);
                     var28.addMappedPgram(var24 * var12 + var23 * var13 + var14, var24 * var15 + var23 * var16 + var17, var52, var53, var58, var59, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, var24, var23, var34, var35, var37);
                     var28.addMappedPgram(var22 * var12 + var25 * var13 + var14, var22 * var15 + var25 * var16 + var17, var56, var57, var54, var55, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, var22, var25, var34, var35, var37);
                     var28.addMappedPgram(var24 * var12 + var25 * var13 + var14, var24 * var15 + var25 * var16 + var17, var56, var57, var58, var59, var50, var51, var30, var31, var32, var31, var30, var33, var32, var33, var24, var25, var34, var35, var37);
                  }

                  return true;
               }

               System.out.println("Not a 2d transform!");
               var17 = 0.0F;
               var14 = 0.0F;
            }

            var36 = this.context.validatePaintOp(this, var11, BaseShaderContext.MaskType.ALPHA_TEXTURE, var6, var7, var8, var9, var10);
            var37 = this.getPaintTextureTx(IDENT, var36, var7, var8, var9, var10);
            if (var37 != null) {
               var37.translate((double)(-var14), (double)(-var17));
            }

            var28.addQuad(var22, var23, var34, var35, var30, var31, var32, var33, var37);
            var28.addQuad(var24, var23, var34, var35, var30, var31, var32, var33, var37);
            var28.addQuad(var22, var25, var34, var35, var30, var31, var32, var33, var37);
            var28.addQuad(var24, var25, var34, var35, var30, var31, var32, var33, var37);
            return true;
         }
      } else {
         return true;
      }
   }

   boolean drawPrimRect(float var1, float var2, float var3, float var4) {
      float var5 = this.stroke.getLineWidth();
      float var6 = getStrokeExpansionFactor(this.stroke) * var5;
      BaseTransform var7 = this.getTransformNoClone();
      float var8 = (float)var7.getMxx();
      float var9 = (float)var7.getMxy();
      float var10 = (float)var7.getMxt();
      float var11 = (float)var7.getMyx();
      float var12 = (float)var7.getMyy();
      float var13 = (float)var7.getMyt();
      float var14 = len(var8, var11);
      float var15 = len(var9, var12);
      if (var14 != 0.0F && var15 != 0.0F) {
         float var16 = 1.0F / var14;
         float var17 = 1.0F / var15;
         float var18 = var1 - var6 - var16 * 0.5F;
         float var19 = var2 - var6 - var17 * 0.5F;
         float var20 = var1 + var3 * 0.5F;
         float var21 = var2 + var4 * 0.5F;
         float var22 = var1 + var3 + var6 + var16 * 0.5F;
         float var23 = var2 + var4 + var6 + var17 * 0.5F;
         Texture var24 = this.context.getWrapRectTexture();
         float var25 = 1.0F / (float)var24.getPhysicalWidth();
         float var26 = 1.0F / (float)var24.getPhysicalHeight();
         float var27 = 0.5F * var25;
         float var28 = 0.5F * var26;
         float var29 = ((var3 * 0.5F + var6) * var14 + 1.0F) * var25;
         float var30 = ((var4 * 0.5F + var6) * var15 + 1.0F) * var26;
         float var31 = var5 * var14 * var25;
         float var32 = var5 * var15 * var26;
         VertexBuffer var33 = this.context.getVertexBuffer();
         Shader var34;
         AffineBase var35;
         if (var7.isTranslateOrIdentity()) {
            var18 += var10;
            var19 += var13;
            var20 += var10;
            var21 += var13;
            var22 += var10;
            var23 += var13;
            var7 = IDENT;
         } else {
            if (var7.is2D()) {
               var34 = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, var24, var1, var2, var3, var4, var31, var32, 0.0F, 0.0F, 0.0F, 0.0F);
               var34.setConstant("innerOffset", var31, var32);
               var35 = this.getPaintTextureTx(IDENT, var34, var1, var2, var3, var4);
               float var36 = var8 * var18;
               float var37 = var11 * var18;
               float var38 = var9 * var19;
               float var39 = var12 * var19;
               float var40 = var8 * var20;
               float var41 = var11 * var20;
               float var42 = var9 * var21;
               float var43 = var12 * var21;
               float var44 = var8 * var22;
               float var45 = var11 * var22;
               float var46 = var9 * var23;
               float var47 = var12 * var23;
               float var48 = var40 + var42 + var10;
               float var49 = var41 + var43 + var13;
               float var50 = var40 + var38 + var10;
               float var51 = var41 + var39 + var13;
               float var52 = var36 + var42 + var10;
               float var53 = var37 + var43 + var13;
               float var54 = var40 + var46 + var10;
               float var55 = var41 + var47 + var13;
               float var56 = var44 + var42 + var10;
               float var57 = var45 + var43 + var13;
               if (var35 == null) {
                  var33.addMappedPgram(var36 + var38 + var10, var37 + var39 + var13, var50, var51, var52, var53, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, 0.0F, 0.0F);
                  var33.addMappedPgram(var44 + var38 + var10, var45 + var39 + var13, var50, var51, var56, var57, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, 0.0F, 0.0F);
                  var33.addMappedPgram(var36 + var46 + var10, var37 + var47 + var13, var54, var55, var52, var53, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, 0.0F, 0.0F);
                  var33.addMappedPgram(var44 + var46 + var10, var45 + var47 + var13, var54, var55, var56, var57, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, 0.0F, 0.0F);
               } else {
                  var33.addMappedPgram(var36 + var38 + var10, var37 + var39 + var13, var50, var51, var52, var53, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, var18, var19, var20, var21, var35);
                  var33.addMappedPgram(var44 + var38 + var10, var45 + var39 + var13, var50, var51, var56, var57, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, var22, var19, var20, var21, var35);
                  var33.addMappedPgram(var36 + var46 + var10, var37 + var47 + var13, var54, var55, var52, var53, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, var18, var23, var20, var21, var35);
                  var33.addMappedPgram(var44 + var46 + var10, var45 + var47 + var13, var54, var55, var56, var57, var48, var49, var27, var28, var29, var28, var27, var30, var29, var30, var22, var23, var20, var21, var35);
               }

               var24.unlock();
               return true;
            }

            System.out.println("Not a 2d transform!");
            var13 = 0.0F;
            var10 = 0.0F;
         }

         var34 = this.context.validatePaintOp(this, var7, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, var24, var1, var2, var3, var4, var31, var32, 0.0F, 0.0F, 0.0F, 0.0F);
         var34.setConstant("innerOffset", var31, var32);
         var35 = this.getPaintTextureTx(IDENT, var34, var1, var2, var3, var4);
         if (var35 != null) {
            var35.translate((double)(-var10), (double)(-var13));
         }

         var33.addQuad(var18, var19, var20, var21, var27, var28, var29, var30, var35);
         var33.addQuad(var22, var19, var20, var21, var27, var28, var29, var30, var35);
         var33.addQuad(var18, var23, var20, var21, var27, var28, var29, var30, var35);
         var33.addQuad(var22, var23, var20, var21, var27, var28, var29, var30, var35);
         var24.unlock();
         return true;
      } else {
         return true;
      }
   }

   boolean drawPrimDiagonal(float var1, float var2, float var3, float var4, float var5, int var6, float var7, float var8, float var9, float var10) {
      if (this.stroke.getType() == 0) {
         var5 *= 0.5F;
      }

      float var11 = var3 - var1;
      float var12 = var4 - var2;
      float var13 = len(var11, var12);
      var11 /= var13;
      var12 /= var13;
      float var14 = var11 * var5;
      float var15 = var12 * var5;
      float var16 = var1 + var15;
      float var17 = var2 - var14;
      float var18 = var3 + var15;
      float var19 = var4 - var14;
      float var20 = var1 - var15;
      float var21 = var2 + var14;
      float var22 = var3 - var15;
      float var23 = var4 + var14;
      if (var6 == 2) {
         var16 -= var14;
         var17 -= var15;
         var20 -= var14;
         var21 -= var15;
         var18 += var14;
         var19 += var15;
         var22 += var14;
         var23 += var15;
      }

      BaseTransform var30 = this.getTransformNoClone();
      float var31 = (float)var30.getMxt();
      float var32 = (float)var30.getMyt();
      float var24;
      float var25;
      float var26;
      float var27;
      int var28;
      int var29;
      float var35;
      float var36;
      float var37;
      float var38;
      float var39;
      if (var30.isTranslateOrIdentity()) {
         var24 = var11;
         var25 = var12;
         var26 = var12;
         var27 = -var11;
         var28 = (int)Math.ceil((double)len(var18 - var16, var19 - var17));
         var29 = (int)Math.ceil((double)len(var20 - var16, var21 - var17));
         var30 = IDENT;
      } else {
         if (!var30.is2D()) {
            System.out.println("Not a 2d transform!");
            return false;
         }

         float var33 = (float)var30.getMxx();
         float var34 = (float)var30.getMxy();
         var35 = (float)var30.getMyx();
         var36 = (float)var30.getMyy();
         var37 = var33 * var16 + var34 * var17;
         var38 = var35 * var16 + var36 * var17;
         var16 = var37;
         var17 = var38;
         var37 = var33 * var18 + var34 * var19;
         var38 = var35 * var18 + var36 * var19;
         var18 = var37;
         var19 = var38;
         var37 = var33 * var20 + var34 * var21;
         var38 = var35 * var20 + var36 * var21;
         var20 = var37;
         var21 = var38;
         var37 = var33 * var22 + var34 * var23;
         var38 = var35 * var22 + var36 * var23;
         var22 = var37;
         var23 = var38;
         var24 = var33 * var11 + var34 * var12;
         var25 = var35 * var11 + var36 * var12;
         var39 = len(var24, var25);
         if (var39 == 0.0F) {
            return true;
         }

         var24 /= var39;
         var25 /= var39;
         var26 = var33 * var12 - var34 * var11;
         var27 = var35 * var12 - var36 * var11;
         var39 = len(var26, var27);
         if (var39 == 0.0F) {
            return true;
         }

         var26 /= var39;
         var27 /= var39;
         var28 = (int)Math.ceil((double)Math.abs((var18 - var16) * var24 + (var19 - var17) * var25));
         var29 = (int)Math.ceil((double)Math.abs((var20 - var16) * var26 + (var21 - var17) * var27));
         var30 = IDENT;
      }

      var24 *= 0.5F;
      var25 *= 0.5F;
      var26 *= 0.5F;
      var27 *= 0.5F;
      var16 = var16 + var31 + var26 - var24;
      var17 = var17 + var32 + var27 - var25;
      var18 = var18 + var31 + var26 + var24;
      var19 = var19 + var32 + var27 + var25;
      var20 = var20 + var31 - var26 - var24;
      var21 = var21 + var32 - var27 - var25;
      var22 = var22 + var31 - var26 + var24;
      var23 = var23 + var32 - var27 + var25;
      VertexBuffer var50 = this.context.getVertexBuffer();
      int var51 = this.context.getRectTextureMaxSize();
      float var40;
      float var41;
      float var42;
      float var43;
      float var44;
      float var46;
      float var47;
      if (var29 <= var51) {
         var35 = (float)(var29 * (var29 + 1) / 2) - 0.5F;
         var36 = var35 + (float)var29 + 1.0F;
         Texture var52 = this.context.getRectTexture();
         var35 /= (float)var52.getPhysicalHeight();
         var36 /= (float)var52.getPhysicalHeight();
         if (var28 <= var51) {
            var38 = (float)(var28 * (var28 + 1) / 2) - 0.5F;
            var39 = var38 + (float)var28 + 1.0F;
            var38 /= (float)var52.getPhysicalWidth();
            var39 /= (float)var52.getPhysicalWidth();
            this.context.validatePaintOp(this, var30, BaseShaderContext.MaskType.ALPHA_TEXTURE, var52, var7, var8, var9, var10);
            var50.addMappedPgram(var16, var17, var18, var19, var20, var21, var22, var23, var38, var35, var39, var35, var38, var36, var39, var36, 0.0F, 0.0F);
            var52.unlock();
            return true;
         } else if (var28 <= var51 * 2 - 1) {
            var38 = (var16 + var18) * 0.5F;
            var39 = (var17 + var19) * 0.5F;
            var40 = (var20 + var22) * 0.5F;
            var41 = (var21 + var23) * 0.5F;
            var42 = (float)(var51 * (var51 + 1) / 2) - 0.5F;
            var43 = var42 + 0.5F + (float)var28 * 0.5F;
            var42 /= (float)var52.getPhysicalWidth();
            var43 /= (float)var52.getPhysicalWidth();
            this.context.validatePaintOp(this, var30, BaseShaderContext.MaskType.ALPHA_TEXTURE, var52, var7, var8, var9, var10);
            var50.addMappedPgram(var16, var17, var38, var39, var20, var21, var40, var41, var42, var35, var43, var35, var42, var36, var43, var36, 0.0F, 0.0F);
            var50.addMappedPgram(var18, var19, var38, var39, var22, var23, var40, var41, var42, var35, var43, var35, var42, var36, var43, var36, 0.0F, 0.0F);
            var52.unlock();
            return true;
         } else {
            var38 = 0.5F / (float)var52.getPhysicalWidth();
            var39 = 1.5F / (float)var52.getPhysicalWidth();
            var24 *= 2.0F;
            var25 *= 2.0F;
            var40 = var16 + var24;
            var41 = var17 + var25;
            var42 = var18 - var24;
            var43 = var19 - var25;
            var44 = var20 + var24;
            float var53 = var21 + var25;
            var46 = var22 - var24;
            var47 = var23 - var25;
            this.context.validatePaintOp(this, var30, BaseShaderContext.MaskType.ALPHA_TEXTURE, var52, var7, var8, var9, var10);
            var50.addMappedPgram(var16, var17, var40, var41, var20, var21, var44, var53, var38, var35, var39, var35, var38, var36, var39, var36, 0.0F, 0.0F);
            var50.addMappedPgram(var40, var41, var42, var43, var44, var53, var46, var47, var39, var35, var39, var35, var39, var36, var39, var36, 0.0F, 0.0F);
            var50.addMappedPgram(var42, var43, var18, var19, var46, var47, var22, var23, var39, var35, var38, var35, var39, var36, var38, var36, 0.0F, 0.0F);
            var52.unlock();
            return true;
         }
      } else {
         var35 = (var16 + var18) * 0.5F;
         var36 = (var17 + var19) * 0.5F;
         var37 = (var20 + var22) * 0.5F;
         var38 = (var21 + var23) * 0.5F;
         var39 = (var16 + var20) * 0.5F;
         var40 = (var17 + var21) * 0.5F;
         var41 = (var18 + var22) * 0.5F;
         var42 = (var19 + var23) * 0.5F;
         var43 = (var35 + var37) * 0.5F;
         var44 = (var36 + var38) * 0.5F;
         Texture var45 = this.context.getWrapRectTexture();
         var46 = 0.5F / (float)var45.getPhysicalWidth();
         var47 = 0.5F / (float)var45.getPhysicalHeight();
         float var48 = ((float)var28 * 0.5F + 1.0F) / (float)var45.getPhysicalWidth();
         float var49 = ((float)var29 * 0.5F + 1.0F) / (float)var45.getPhysicalHeight();
         this.context.validatePaintOp(this, var30, BaseShaderContext.MaskType.ALPHA_TEXTURE, var45, var7, var8, var9, var10);
         var50.addMappedPgram(var16, var17, var35, var36, var39, var40, var43, var44, var46, var47, var48, var47, var46, var49, var48, var49, 0.0F, 0.0F);
         var50.addMappedPgram(var18, var19, var35, var36, var41, var42, var43, var44, var46, var47, var48, var47, var46, var49, var48, var49, 0.0F, 0.0F);
         var50.addMappedPgram(var20, var21, var37, var38, var39, var40, var43, var44, var46, var47, var48, var47, var46, var49, var48, var49, 0.0F, 0.0F);
         var50.addMappedPgram(var22, var23, var37, var38, var41, var42, var43, var44, var46, var47, var48, var47, var46, var49, var48, var49, 0.0F, 0.0F);
         var45.unlock();
         return true;
      }
   }

   public void fillRect(float var1, float var2, float var3, float var4) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (!this.isAntialiasedShape()) {
            this.fillQuad(var1, var2, var1 + var3, var2 + var4);
         } else if (this.isComplexPaint) {
            scratchRRect.setRoundRect(var1, var2, var3, var4, 0.0F, 0.0F);
            this.renderWithComplexPaint(scratchRRect, (BasicStroke)null, var1, var2, var3, var4);
         } else {
            if (PrismSettings.primTextureSize != 0) {
               Texture var5 = this.context.getRectTexture();
               Texture var6 = this.context.getWrapRectTexture();
               boolean var7 = this.fillPrimRect(var1, var2, var3, var4, var5, var6, var1, var2, var3, var4);
               var5.unlock();
               var6.unlock();
               if (var7) {
                  return;
               }
            }

            this.renderGeneralRoundedRect(var1, var2, var3, var4, 0.0F, 0.0F, BaseShaderContext.MaskType.FILL_PGRAM, (BasicStroke)null);
         }
      }
   }

   public void fillEllipse(float var1, float var2, float var3, float var4) {
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (this.isComplexPaint) {
            scratchEllipse.setFrame(var1, var2, var3, var4);
            this.renderWithComplexPaint(scratchEllipse, (BasicStroke)null, var1, var2, var3, var4);
         } else if (!this.isAntialiasedShape()) {
            scratchEllipse.setFrame(var1, var2, var3, var4);
            this.renderShape(scratchEllipse, (BasicStroke)null, var1, var2, var3, var4);
         } else if (PrismSettings.primTextureSize == 0 || !this.fillPrimRect(var1, var2, var3, var4, this.context.getOvalTexture(), (Texture)null, var1, var2, var3, var4)) {
            this.renderGeneralRoundedRect(var1, var2, var3, var4, var3, var4, BaseShaderContext.MaskType.FILL_ELLIPSE, (BasicStroke)null);
         }
      }
   }

   public void fillRoundRect(float var1, float var2, float var3, float var4, float var5, float var6) {
      var5 = Math.min(Math.abs(var5), var3);
      var6 = Math.min(Math.abs(var6), var4);
      if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
         if (this.isComplexPaint) {
            scratchRRect.setRoundRect(var1, var2, var3, var4, var5, var6);
            this.renderWithComplexPaint(scratchRRect, (BasicStroke)null, var1, var2, var3, var4);
         } else if (!this.isAntialiasedShape()) {
            scratchRRect.setRoundRect(var1, var2, var3, var4, var5, var6);
            this.renderShape(scratchRRect, (BasicStroke)null, var1, var2, var3, var4);
         } else {
            this.renderGeneralRoundedRect(var1, var2, var3, var4, var5, var6, BaseShaderContext.MaskType.FILL_ROUNDRECT, (BasicStroke)null);
         }
      }
   }

   public void fillQuad(float var1, float var2, float var3, float var4) {
      float var5;
      float var7;
      if (var1 <= var3) {
         var5 = var1;
         var7 = var3 - var1;
      } else {
         var5 = var3;
         var7 = var1 - var3;
      }

      float var6;
      float var8;
      if (var2 <= var4) {
         var6 = var2;
         var8 = var4 - var2;
      } else {
         var6 = var4;
         var8 = var2 - var4;
      }

      if (this.isComplexPaint) {
         scratchRRect.setRoundRect(var5, var6, var7, var8, 0.0F, 0.0F);
         this.renderWithComplexPaint(scratchRRect, (BasicStroke)null, var5, var6, var7, var8);
      } else {
         BaseTransform var9 = this.getTransformNoClone();
         if (PrismSettings.primTextureSize != 0) {
            float var11;
            float var14;
            if (var9.isTranslateOrIdentity()) {
               var14 = (float)var9.getMxt();
               var11 = (float)var9.getMyt();
               var9 = IDENT;
               var1 += var14;
               var2 += var11;
               var3 += var14;
               var4 += var11;
            } else {
               var11 = 0.0F;
               var14 = 0.0F;
            }

            Shader var12 = this.context.validatePaintOp(this, var9, BaseShaderContext.MaskType.ALPHA_ONE, (Texture)null, var5, var6, var7, var8);
            AffineBase var13 = this.getPaintTextureTx(IDENT, var12, var5, var6, var7, var8);
            if (var13 != null) {
               var13.translate((double)(-var14), (double)(-var11));
            }

            this.context.getVertexBuffer().addQuad(var1, var2, var3, var4, 0.0F, 0.0F, 0.0F, 0.0F, var13);
         } else {
            if (this.isSimpleTranslate) {
               var9 = IDENT;
               var5 += this.transX;
               var6 += this.transY;
            }

            this.context.validatePaintOp(this, var9, BaseShaderContext.MaskType.SOLID, var5, var6, var7, var8);
            VertexBuffer var10 = this.context.getVertexBuffer();
            var10.addQuad(var5, var6, var5 + var7, var6 + var8);
         }
      }
   }

   private static boolean canUseStrokeShader(BasicStroke var0) {
      return !var0.isDashed() && (var0.getType() == 1 || var0.getLineJoin() == 1 || var0.getLineJoin() == 0 && (double)var0.getMiterLimit() >= SQRT_2);
   }

   public void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      if (var2 == null) {
         this.context.setRenderTarget(this);
      } else {
         this.context.setRenderTarget((BaseGraphics)var2.createGraphics());
      }

      this.context.blit(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public void drawRect(float var1, float var2, float var3, float var4) {
      if (!(var3 < 0.0F) && !(var4 < 0.0F)) {
         if (var3 != 0.0F && var4 != 0.0F) {
            if (this.isComplexPaint) {
               scratchRRect.setRoundRect(var1, var2, var3, var4, 0.0F, 0.0F);
               this.renderWithComplexPaint(scratchRRect, this.stroke, var1, var2, var3, var4);
            } else if (!this.isAntialiasedShape()) {
               scratchRRect.setRoundRect(var1, var2, var3, var4, 0.0F, 0.0F);
               this.renderShape(scratchRRect, this.stroke, var1, var2, var3, var4);
            } else if (canUseStrokeShader(this.stroke)) {
               if (PrismSettings.primTextureSize == 0 || this.stroke.getLineJoin() == 1 || !this.drawPrimRect(var1, var2, var3, var4)) {
                  this.renderGeneralRoundedRect(var1, var2, var3, var4, 0.0F, 0.0F, BaseShaderContext.MaskType.DRAW_PGRAM, this.stroke);
               }
            } else {
               scratchRRect.setRoundRect(var1, var2, var3, var4, 0.0F, 0.0F);
               this.renderShape(scratchRRect, this.stroke, var1, var2, var3, var4);
            }
         } else {
            this.drawLine(var1, var2, var1 + var3, var2 + var4);
         }
      }
   }

   private boolean checkInnerCurvature(float var1, float var2) {
      float var3 = this.stroke.getLineWidth() * (1.0F - getStrokeExpansionFactor(this.stroke));
      var1 -= var3;
      var2 -= var3;
      return var1 <= 0.0F || var2 <= 0.0F || var1 * 2.0F > var2 && var2 * 2.0F > var1;
   }

   public void drawEllipse(float var1, float var2, float var3, float var4) {
      if (!(var3 < 0.0F) && !(var4 < 0.0F)) {
         if (!this.isComplexPaint && !this.stroke.isDashed() && this.checkInnerCurvature(var3, var4) && this.isAntialiasedShape()) {
            this.renderGeneralRoundedRect(var1, var2, var3, var4, var3, var4, BaseShaderContext.MaskType.DRAW_ELLIPSE, this.stroke);
         } else {
            scratchEllipse.setFrame(var1, var2, var3, var4);
            this.renderShape(scratchEllipse, this.stroke, var1, var2, var3, var4);
         }
      }
   }

   public void drawRoundRect(float var1, float var2, float var3, float var4, float var5, float var6) {
      var5 = Math.min(Math.abs(var5), var3);
      var6 = Math.min(Math.abs(var6), var4);
      if (!(var3 < 0.0F) && !(var4 < 0.0F)) {
         if (!this.isComplexPaint && !this.stroke.isDashed() && this.checkInnerCurvature(var5, var6) && this.isAntialiasedShape()) {
            this.renderGeneralRoundedRect(var1, var2, var3, var4, var5, var6, BaseShaderContext.MaskType.DRAW_ROUNDRECT, this.stroke);
         } else {
            scratchRRect.setRoundRect(var1, var2, var3, var4, var5, var6);
            this.renderShape(scratchRRect, this.stroke, var1, var2, var3, var4);
         }
      }
   }

   public void drawLine(float var1, float var2, float var3, float var4) {
      float var5;
      float var7;
      if (var1 <= var3) {
         var5 = var1;
         var7 = var3 - var1;
      } else {
         var5 = var3;
         var7 = var1 - var3;
      }

      float var6;
      float var8;
      if (var2 <= var4) {
         var6 = var2;
         var8 = var4 - var2;
      } else {
         var6 = var4;
         var8 = var2 - var4;
      }

      if (this.stroke.getType() != 1) {
         if (this.isComplexPaint) {
            scratchLine.setLine(var1, var2, var3, var4);
            this.renderWithComplexPaint(scratchLine, this.stroke, var5, var6, var7, var8);
         } else if (!this.isAntialiasedShape()) {
            scratchLine.setLine(var1, var2, var3, var4);
            this.renderShape(scratchLine, this.stroke, var5, var6, var7, var8);
         } else {
            int var9 = this.stroke.getEndCap();
            if (this.stroke.isDashed()) {
               scratchLine.setLine(var1, var2, var3, var4);
               this.renderShape(scratchLine, this.stroke, var5, var6, var7, var8);
            } else {
               float var10 = this.stroke.getLineWidth();
               float var11;
               float var12;
               float var13;
               if (PrismSettings.primTextureSize != 0 && var9 != 1) {
                  var11 = var10;
                  if (this.stroke.getType() == 0) {
                     var11 = var10 * 0.5F;
                  }

                  if (var7 != 0.0F && var8 != 0.0F) {
                     if (this.drawPrimDiagonal(var1, var2, var3, var4, var10, var9, var5, var6, var7, var8)) {
                        return;
                     }
                  } else {
                     if (var9 == 2) {
                        var13 = var11;
                        var12 = var11;
                     } else if (var7 != 0.0F) {
                        var12 = 0.0F;
                        var13 = var11;
                     } else {
                        if (var8 == 0.0F) {
                           return;
                        }

                        var12 = var11;
                        var13 = 0.0F;
                     }

                     Texture var14 = this.context.getRectTexture();
                     Texture var15 = this.context.getWrapRectTexture();
                     boolean var16 = this.fillPrimRect(var5 - var12, var6 - var13, var7 + var12 + var12, var8 + var13 + var13, var14, var15, var5, var6, var7, var8);
                     var14.unlock();
                     var15.unlock();
                     if (var16) {
                        return;
                     }
                  }
               }

               if (this.stroke.getType() == 2) {
                  var10 *= 2.0F;
               }

               var11 = var3 - var1;
               var12 = var4 - var2;
               var13 = len(var11, var12);
               float var25;
               float var26;
               if (var13 == 0.0F) {
                  if (var9 == 0) {
                     return;
                  }

                  var25 = var10;
                  var26 = 0.0F;
               } else {
                  var25 = var10 * var11 / var13;
                  var26 = var10 * var12 / var13;
               }

               BaseTransform var27 = this.getTransformNoClone();
               BaseTransform var17;
               float var18;
               float var19;
               if (this.isSimpleTranslate) {
                  double var20 = var27.getMxt();
                  double var22 = var27.getMyt();
                  var1 = (float)((double)var1 + var20);
                  var2 = (float)((double)var2 + var22);
                  float var10000 = (float)((double)var3 + var20);
                  var10000 = (float)((double)var4 + var22);
                  var18 = var26;
                  var19 = -var25;
                  var17 = IDENT;
               } else {
                  var17 = this.extract3Dremainder(var27);
                  double[] var28 = new double[]{(double)var1, (double)var2, (double)var3, (double)var4};
                  var27.transform((double[])var28, 0, (double[])var28, 0, 2);
                  var1 = (float)var28[0];
                  var2 = (float)var28[1];
                  var3 = (float)var28[2];
                  var4 = (float)var28[3];
                  var11 = var3 - var1;
                  var12 = var4 - var2;
                  var28[0] = (double)var25;
                  var28[1] = (double)var26;
                  var28[2] = (double)var26;
                  var28[3] = (double)(-var25);
                  var27.deltaTransform((double[])var28, 0, (double[])var28, 0, 2);
                  var25 = (float)var28[0];
                  var26 = (float)var28[1];
                  var18 = (float)var28[2];
                  var19 = (float)var28[3];
               }

               float var29 = var1 - var18 / 2.0F;
               float var21 = var2 - var19 / 2.0F;
               float var23;
               BaseShaderContext.MaskType var24;
               float var30;
               if (var9 != 0) {
                  var29 -= var25 / 2.0F;
                  var21 -= var26 / 2.0F;
                  var11 += var25;
                  var12 += var26;
                  if (var9 == 1) {
                     var30 = len(var25, var26) / len(var11, var12);
                     var23 = 1.0F;
                     var24 = BaseShaderContext.MaskType.FILL_ROUNDRECT;
                  } else {
                     var23 = 0.0F;
                     var30 = 0.0F;
                     var24 = BaseShaderContext.MaskType.FILL_PGRAM;
                  }
               } else {
                  var23 = 0.0F;
                  var30 = 0.0F;
                  var24 = BaseShaderContext.MaskType.FILL_PGRAM;
               }

               this.renderGeneralRoundedPgram(var29, var21, var11, var12, var18, var19, var30, var23, 0.0F, 0.0F, var17, var24, var5, var6, var7, var8);
            }
         }
      }
   }

   private static float len(float var0, float var1) {
      return var0 == 0.0F ? Math.abs(var1) : (var1 == 0.0F ? Math.abs(var0) : (float)Math.sqrt((double)(var0 * var0 + var1 * var1)));
   }

   public void setNodeBounds(RectBounds var1) {
      this.nodeBounds = var1;
      this.lcdSampleInvalid = var1 != null;
   }

   private void initLCDSampleRT() {
      if (this.lcdSampleInvalid) {
         RectBounds var1 = new RectBounds();
         this.getTransformNoClone().transform((BaseBounds)this.nodeBounds, (BaseBounds)var1);
         Rectangle var2 = this.getClipRectNoClone();
         if (var2 != null && !var2.isEmpty()) {
            var1.intersectWith(var2);
         }

         float var3 = var1.getMinX() - 1.0F;
         float var4 = var1.getMinY();
         float var5 = var1.getWidth() + 2.0F;
         float var6 = var1.getHeight() + 1.0F;
         this.context.validateLCDBuffer(this.getRenderTarget());
         BaseShaderGraphics var7 = (BaseShaderGraphics)this.context.getLCDBuffer().createGraphics();
         var7.setCompositeMode(CompositeMode.SRC);
         this.context.validateLCDOp(var7, IDENT, (Texture)this.getRenderTarget(), (Texture)null, true, (Paint)null);
         int var8 = this.getRenderTarget().getPhysicalHeight();
         int var9 = this.getRenderTarget().getPhysicalWidth();
         float var10 = var3 / (float)var9;
         float var11 = var4 / (float)var8;
         float var12 = (var3 + var5) / (float)var9;
         float var13 = (var4 + var6) / (float)var8;
         var7.drawLCDBuffer(var3, var4, var5, var6, var10, var11, var12, var13);
         this.context.setRenderTarget(this);
      }

      this.lcdSampleInvalid = false;
   }

   public void drawString(GlyphList var1, FontStrike var2, float var3, float var4, Color var5, int var6, int var7) {
      BaseTransform var8;
      if (!this.isComplexPaint && !this.paint.getType().isImagePattern() && !var2.drawAsShapes()) {
         var8 = this.getTransformNoClone();
         Paint var25 = this.getPaint();
         Color var10 = var25.getType() == Paint.Type.COLOR ? (Color)var25 : null;
         CompositeMode var11 = this.getCompositeMode();
         boolean var12 = var11 == CompositeMode.SRC_OVER && var10 != null && var8.is2D() && !this.getRenderTarget().isMSAA();
         float var14;
         if (var2.getAAMode() == 1 && !var12) {
            FontResource var13 = var2.getFontResource();
            var14 = var2.getSize();
            BaseTransform var15 = var2.getTransform();
            var2 = var13.getStrike(var14, var15, 0);
         }

         float var26 = 0.0F;
         var14 = 0.0F;
         float var27 = 0.0F;
         float var16 = 0.0F;
         RectBounds var17;
         if (this.paint.getType().isGradient() && ((Gradient)this.paint).isProportional()) {
            var17 = this.nodeBounds;
            if (var17 == null) {
               Metrics var18 = var2.getMetrics();
               float var19 = -var18.getAscent() * 0.4F;
               var17 = new RectBounds(-var19, var18.getAscent(), var1.getWidth() + 2.0F * var19, var18.getDescent() + var18.getLineGap());
               var26 = var3;
               var14 = var4;
            }

            var26 += var17.getMinX();
            var14 += var17.getMinY();
            var27 = var17.getWidth();
            var16 = var17.getHeight();
         }

         var17 = null;
         Point2D var28 = new Point2D(var3, var4);
         if (this.isSimpleTranslate) {
            var17 = this.getFinalClipNoClone();
            var8 = IDENT;
            var28.x += this.transX;
            var28.y += this.transY;
         }

         GlyphCache var29 = this.context.getGlyphCache(var2);
         Texture var20 = var29.getBackingStore();
         if (var2.getAAMode() == 1) {
            if (this.nodeBounds == null) {
               Metrics var21 = var2.getMetrics();
               RectBounds var22 = new RectBounds(var3 - 2.0F, var4 + var21.getAscent(), var3 + 2.0F + var1.getWidth(), var4 + 1.0F + var21.getDescent() + var21.getLineGap());
               this.setNodeBounds(var22);
               this.initLCDSampleRT();
               this.setNodeBounds((RectBounds)null);
            } else {
               this.initLCDSampleRT();
            }

            float var30 = PrismFontFactory.getLCDContrast();
            float var31 = 1.0F / var30;
            var10 = new Color((float)Math.pow((double)var10.getRed(), (double)var30), (float)Math.pow((double)var10.getGreen(), (double)var30), (float)Math.pow((double)var10.getBlue(), (double)var30), (float)Math.pow((double)var10.getAlpha(), (double)var30));
            if (var5 != null) {
               var5 = new Color((float)Math.pow((double)var5.getRed(), (double)var30), (float)Math.pow((double)var5.getGreen(), (double)var30), (float)Math.pow((double)var5.getBlue(), (double)var30), (float)Math.pow((double)var5.getAlpha(), (double)var30));
            }

            this.setCompositeMode(CompositeMode.SRC);
            Shader var23 = this.context.validateLCDOp(this, IDENT, this.context.getLCDBuffer(), var20, false, var10);
            float var24 = 1.0F / (float)var20.getPhysicalWidth();
            var23.setConstant("gamma", var31, var30, var24);
            this.setCompositeMode(var11);
         } else {
            this.context.validatePaintOp(this, IDENT, var20, var26, var14, var27, var16);
         }

         if (this.isSimpleTranslate) {
            var28.y = (float)Math.round(var28.y);
            var28.x = (float)Math.round(var28.x);
         }

         var29.render(this.context, var1, var28.x, var28.y, var6, var7, var5, var10, var8, var17);
      } else {
         var8 = BaseTransform.getTranslateInstance((double)var3, (double)var4);
         Shape var9 = var2.getOutline(var1, var8);
         this.fill(var9);
      }
   }

   private void drawLCDBuffer(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.context.setRenderTarget(this);
      this.context.getVertexBuffer().addQuad(var1, var2, var1 + var3, var2 + var4, var5, var6, var7, var8);
   }

   public boolean canReadBack() {
      RenderTarget var1 = this.getRenderTarget();
      return var1 instanceof ReadbackRenderTarget && ((ReadbackRenderTarget)var1).getBackBuffer() != null;
   }

   public RTTexture readBack(Rectangle var1) {
      RenderTarget var2 = this.getRenderTarget();
      this.context.flushVertexBuffer();
      this.context.validateLCDBuffer(var2);
      RTTexture var3 = this.context.getLCDBuffer();
      Texture var4 = ((ReadbackRenderTarget)var2).getBackBuffer();
      float var5 = (float)var1.x;
      float var6 = (float)var1.y;
      float var7 = var5 + (float)var1.width;
      float var8 = var6 + (float)var1.height;
      BaseShaderGraphics var9 = (BaseShaderGraphics)var3.createGraphics();
      var9.setCompositeMode(CompositeMode.SRC);
      this.context.validateTextureOp((BaseGraphics)var9, IDENT, (Texture)var4, var4.getPixelFormat());
      var9.drawTexture(var4, 0.0F, 0.0F, (float)var1.width, (float)var1.height, var5, var6, var7, var8);
      this.context.flushVertexBuffer();
      this.context.setRenderTarget(this);
      return var3;
   }

   public void releaseReadBackBuffer(RTTexture var1) {
   }

   public void setup3DRendering() {
      this.context.setRenderTarget(this);
   }

   static {
      String var0 = (String)AccessController.doPrivileged(() -> {
         return System.getProperty("prism.primshaderpad");
      });
      if (var0 == null) {
         FRINGE_FACTOR = -0.5F;
      } else {
         FRINGE_FACTOR = -Float.valueOf(var0);
         System.out.println("Prism ShaderGraphics primitive shader pad = " + FRINGE_FACTOR);
      }

      SQRT_2 = Math.sqrt(2.0);
   }
}
