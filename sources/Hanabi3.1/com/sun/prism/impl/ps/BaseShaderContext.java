package com.sun.prism.impl.ps;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;

public abstract class BaseShaderContext extends BaseContext {
   private static final int CHECK_SHADER = 1;
   private static final int CHECK_TRANSFORM = 2;
   private static final int CHECK_CLIP = 4;
   private static final int CHECK_COMPOSITE = 8;
   private static final int CHECK_PAINT_OP_MASK = 15;
   private static final int CHECK_TEXTURE_OP_MASK = 15;
   private static final int CHECK_CLEAR_OP_MASK = 4;
   private static final int NUM_STOCK_SHADER_SLOTS = BaseShaderContext.MaskType.values().length << 4;
   private final Shader[] stockShaders;
   private final Shader[] stockATShaders;
   private final Shader[] specialShaders;
   private final Shader[] specialATShaders;
   private Shader externalShader;
   private RTTexture lcdBuffer;
   private final ShaderFactory factory;
   private State state;

   protected BaseShaderContext(Screen var1, ShaderFactory var2, int var3) {
      super(var1, var2, var3);
      this.stockShaders = new Shader[NUM_STOCK_SHADER_SLOTS];
      this.stockATShaders = new Shader[NUM_STOCK_SHADER_SLOTS];
      this.specialShaders = new Shader[BaseShaderContext.SpecialShaderType.values().length];
      this.specialATShaders = new Shader[BaseShaderContext.SpecialShaderType.values().length];
      this.factory = var2;
      this.init();
   }

   protected void init() {
      this.state = null;
      if (this.externalShader != null && !this.externalShader.isValid()) {
         this.externalShader.dispose();
         this.externalShader = null;
      }

   }

   protected void setPerspectiveTransform(GeneralTransform3D var1) {
      this.state.isXformValid = false;
      super.setPerspectiveTransform(var1);
   }

   protected void resetLastClip(State var1) {
      var1.lastClip = null;
   }

   protected abstract State updateRenderTarget(RenderTarget var1, NGCamera var2, boolean var3);

   protected abstract void updateTexture(int var1, Texture var2);

   protected abstract void updateShaderTransform(Shader var1, BaseTransform var2);

   protected abstract void updateWorldTransform(BaseTransform var1);

   protected abstract void updateClipRect(Rectangle var1);

   protected abstract void updateCompositeMode(CompositeMode var1);

   private static int getStockShaderIndex(MaskType var0, Paint var1) {
      int var2;
      int var3;
      if (var1 == null) {
         var2 = 0;
         var3 = 0;
      } else {
         var2 = var1.getType().ordinal();
         if (var1.getType().isGradient()) {
            var3 = ((Gradient)var1).getSpreadMethod();
         } else {
            var3 = 0;
         }
      }

      return var0.ordinal() << 4 | var2 << 2 | var3 << 0;
   }

   private Shader getPaintShader(boolean var1, MaskType var2, Paint var3) {
      int var4 = getStockShaderIndex(var2, var3);
      Shader[] var5 = var1 ? this.stockATShaders : this.stockShaders;
      Shader var6 = var5[var4];
      if (var6 != null && !var6.isValid()) {
         var6.dispose();
         var6 = null;
      }

      if (var6 == null) {
         String var7 = var2.getName() + "_" + var3.getType().getName();
         if (var3.getType().isGradient() && !var2.isNewPaintStyle()) {
            Gradient var8 = (Gradient)var3;
            int var9 = var8.getSpreadMethod();
            if (var9 == 0) {
               var7 = var7 + "_PAD";
            } else if (var9 == 1) {
               var7 = var7 + "_REFLECT";
            } else if (var9 == 2) {
               var7 = var7 + "_REPEAT";
            }
         }

         if (var1) {
            var7 = var7 + "_AlphaTest";
         }

         var6 = var5[var4] = this.factory.createStockShader(var7);
      }

      return var6;
   }

   private void updatePaintShader(BaseShaderGraphics var1, Shader var2, MaskType var3, Paint var4, float var5, float var6, float var7, float var8) {
      Paint.Type var9 = var4.getType();
      if (var9 != Paint.Type.COLOR && !var3.isNewPaintStyle()) {
         float var10;
         float var11;
         float var12;
         float var13;
         if (var4.isProportional()) {
            var10 = var5;
            var11 = var6;
            var12 = var7;
            var13 = var8;
         } else {
            var10 = 0.0F;
            var11 = 0.0F;
            var12 = 1.0F;
            var13 = 1.0F;
         }

         switch (var9) {
            case LINEAR_GRADIENT:
               PaintHelper.setLinearGradient(var1, var2, (LinearGradient)var4, var10, var11, var12, var13);
               break;
            case RADIAL_GRADIENT:
               PaintHelper.setRadialGradient(var1, var2, (RadialGradient)var4, var10, var11, var12, var13);
               break;
            case IMAGE_PATTERN:
               PaintHelper.setImagePattern(var1, var2, (ImagePattern)var4, var10, var11, var12, var13);
         }

      }
   }

   private Shader getSpecialShader(BaseGraphics var1, SpecialShaderType var2) {
      boolean var3 = var1.isAlphaTestShader();
      Shader[] var4 = var3 ? this.specialATShaders : this.specialShaders;
      Shader var5 = var4[var2.ordinal()];
      if (var5 != null && !var5.isValid()) {
         var5.dispose();
         var5 = null;
      }

      if (var5 == null) {
         String var6 = var2.getName();
         if (var3) {
            var6 = var6 + "_AlphaTest";
         }

         var4[var2.ordinal()] = var5 = this.factory.createStockShader(var6);
      }

      return var5;
   }

   public boolean isSuperShaderEnabled() {
      return this.state.lastShader == this.specialATShaders[BaseShaderContext.SpecialShaderType.SUPER.ordinal()] || this.state.lastShader == this.specialShaders[BaseShaderContext.SpecialShaderType.SUPER.ordinal()];
   }

   private void updatePerVertexColor(Paint var1, float var2) {
      if (var1 != null && var1.getType() == Paint.Type.COLOR) {
         this.getVertexBuffer().setPerVertexColor((Color)var1, var2);
      } else {
         this.getVertexBuffer().setPerVertexColor(var2);
      }

   }

   public void validateClearOp(BaseGraphics var1) {
      this.checkState((BaseShaderGraphics)var1, 4, (BaseTransform)null, (Shader)null);
   }

   public void validatePaintOp(BaseGraphics var1, BaseTransform var2, Texture var3, float var4, float var5, float var6, float var7) {
      this.validatePaintOp((BaseShaderGraphics)var1, var2, var3, var4, var5, var6, var7);
   }

   Shader validatePaintOp(BaseShaderGraphics var1, BaseTransform var2, MaskType var3, float var4, float var5, float var6, float var7) {
      return this.validatePaintOp(var1, var2, var3, (Texture)null, var4, var5, var6, var7);
   }

   Shader validatePaintOp(BaseShaderGraphics var1, BaseTransform var2, MaskType var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13) {
      if (this.state.lastConst1 != var8 || this.state.lastConst2 != var9 || this.state.lastConst3 != var10 || this.state.lastConst4 != var11 || this.state.lastConst5 != var12 || this.state.lastConst6 != var13) {
         this.flushVertexBuffer();
         this.state.lastConst1 = var8;
         this.state.lastConst2 = var9;
         this.state.lastConst3 = var10;
         this.state.lastConst4 = var11;
         this.state.lastConst5 = var12;
         this.state.lastConst6 = var13;
      }

      return this.validatePaintOp(var1, var2, var3, (Texture)null, var4, var5, var6, var7);
   }

   Shader validatePaintOp(BaseShaderGraphics var1, BaseTransform var2, MaskType var3, Texture var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14) {
      if (this.state.lastConst1 != var9 || this.state.lastConst2 != var10 || this.state.lastConst3 != var11 || this.state.lastConst4 != var12 || this.state.lastConst5 != var13 || this.state.lastConst6 != var14) {
         this.flushVertexBuffer();
         this.state.lastConst1 = var9;
         this.state.lastConst2 = var10;
         this.state.lastConst3 = var11;
         this.state.lastConst4 = var12;
         this.state.lastConst5 = var13;
         this.state.lastConst6 = var14;
      }

      return this.validatePaintOp(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   Shader validatePaintOp(BaseShaderGraphics var1, BaseTransform var2, Texture var3, float var4, float var5, float var6, float var7) {
      return this.validatePaintOp(var1, var2, BaseShaderContext.MaskType.TEXTURE, var3, var4, var5, var6, var7);
   }

   Shader validatePaintOp(BaseShaderGraphics var1, BaseTransform var2, MaskType var3, Texture var4, float var5, float var6, float var7, float var8) {
      if (var3 == null) {
         throw new InternalError("maskType must be non-null");
      } else if (this.externalShader == null) {
         Paint var9 = var1.getPaint();
         Texture var10 = null;
         if (var9.getType().isGradient()) {
            this.flushVertexBuffer();
            if (var3.isNewPaintStyle()) {
               var10 = PaintHelper.getWrapGradientTexture(var1);
            } else {
               var10 = PaintHelper.getGradientTexture(var1, (Gradient)var9);
            }
         } else if (var9.getType() == Paint.Type.IMAGE_PATTERN) {
            this.flushVertexBuffer();
            ImagePattern var13 = (ImagePattern)var9;
            ResourceFactory var14 = var1.getResourceFactory();
            var10 = var14.getCachedTexture(var13.getImage(), Texture.WrapMode.REPEAT);
         }

         Texture var11;
         Texture var12;
         Shader var15;
         if (this.factory.isSuperShaderAllowed() && var10 == null && var4 == this.factory.getGlyphTexture()) {
            var15 = this.getSpecialShader(var1, BaseShaderContext.SpecialShaderType.SUPER);
            var11 = this.factory.getRegionTexture();
            var12 = var4;
         } else {
            if (var4 != null) {
               var11 = var4;
               var12 = var10;
            } else {
               var11 = var10;
               var12 = null;
            }

            var15 = this.getPaintShader(var1.isAlphaTestShader(), var3, var9);
         }

         this.checkState(var1, 15, var2, var15);
         this.setTexture(0, var11);
         this.setTexture(1, var12);
         this.updatePaintShader(var1, var15, var3, var9, var5, var6, var7, var8);
         this.updatePerVertexColor(var9, var1.getExtraAlpha());
         if (var10 != null) {
            var10.unlock();
         }

         return var15;
      } else {
         this.checkState(var1, 15, var2, this.externalShader);
         this.setTexture(0, var4);
         this.setTexture(1, (Texture)null);
         this.updatePerVertexColor((Paint)null, var1.getExtraAlpha());
         return this.externalShader;
      }
   }

   public void validateTextureOp(BaseGraphics var1, BaseTransform var2, Texture var3, PixelFormat var4) {
      this.validateTextureOp((BaseShaderGraphics)var1, var2, var3, (Texture)null, var4);
   }

   public Shader validateLCDOp(BaseShaderGraphics var1, BaseTransform var2, Texture var3, Texture var4, boolean var5, Paint var6) {
      Shader var7 = var5 ? this.getSpecialShader(var1, BaseShaderContext.SpecialShaderType.TEXTURE_First_LCD) : this.getSpecialShader(var1, BaseShaderContext.SpecialShaderType.TEXTURE_SECOND_LCD);
      this.checkState(var1, 15, var2, var7);
      this.setTexture(0, var3);
      this.setTexture(1, var4);
      this.updatePerVertexColor(var6, var1.getExtraAlpha());
      return var7;
   }

   Shader validateTextureOp(BaseShaderGraphics var1, BaseTransform var2, Texture[] var3, PixelFormat var4) {
      if (var4 != PixelFormat.MULTI_YCbCr_420) {
         return null;
      } else if (var3.length < 3) {
         return null;
      } else {
         Shader var5;
         if (this.externalShader == null) {
            var5 = this.getSpecialShader(var1, BaseShaderContext.SpecialShaderType.TEXTURE_YV12);
         } else {
            var5 = this.externalShader;
         }

         if (null != var5) {
            this.checkState(var1, 15, var2, var5);
            int var6 = Math.max(0, Math.min(var3.length, 4));

            for(int var7 = 0; var7 < var6; ++var7) {
               this.setTexture(var7, var3[var7]);
            }

            this.updatePerVertexColor((Paint)null, var1.getExtraAlpha());
         }

         return var5;
      }
   }

   Shader validateTextureOp(BaseShaderGraphics var1, BaseTransform var2, Texture var3, Texture var4, PixelFormat var5) {
      Shader var6;
      if (this.externalShader == null) {
         switch (var5) {
            case INT_ARGB_PRE:
            case BYTE_BGRA_PRE:
            case BYTE_RGB:
            case BYTE_GRAY:
            case BYTE_APPLE_422:
               if (this.factory.isSuperShaderAllowed() && var3 == this.factory.getRegionTexture() && var4 == null) {
                  var6 = this.getSpecialShader(var1, BaseShaderContext.SpecialShaderType.SUPER);
                  var4 = this.factory.getGlyphTexture();
               } else {
                  var6 = this.getSpecialShader(var1, BaseShaderContext.SpecialShaderType.TEXTURE_RGB);
               }
               break;
            case MULTI_YCbCr_420:
            case BYTE_ALPHA:
            default:
               throw new InternalError("Pixel format not supported: " + var5);
         }
      } else {
         var6 = this.externalShader;
      }

      this.checkState(var1, 15, var2, var6);
      this.setTexture(0, var3);
      this.setTexture(1, var4);
      this.updatePerVertexColor((Paint)null, var1.getExtraAlpha());
      return var6;
   }

   Shader validateMaskTextureOp(BaseShaderGraphics var1, BaseTransform var2, Texture var3, Texture var4, PixelFormat var5) {
      Shader var6;
      if (this.externalShader == null) {
         switch (var5) {
            case INT_ARGB_PRE:
            case BYTE_BGRA_PRE:
            case BYTE_RGB:
            case BYTE_GRAY:
            case BYTE_APPLE_422:
               var6 = this.getSpecialShader(var1, BaseShaderContext.SpecialShaderType.TEXTURE_MASK_RGB);
               break;
            case MULTI_YCbCr_420:
            case BYTE_ALPHA:
            default:
               throw new InternalError("Pixel format not supported: " + var5);
         }
      } else {
         var6 = this.externalShader;
      }

      this.checkState(var1, 15, var2, var6);
      this.setTexture(0, var3);
      this.setTexture(1, var4);
      this.updatePerVertexColor((Paint)null, var1.getExtraAlpha());
      return var6;
   }

   void setExternalShader(BaseShaderGraphics var1, Shader var2) {
      this.flushVertexBuffer();
      if (var2 != null) {
         var2.enable();
      }

      this.externalShader = var2;
   }

   private void checkState(BaseShaderGraphics var1, int var2, BaseTransform var3, Shader var4) {
      this.setRenderTarget(var1);
      if ((var2 & 1) != 0 && var4 != this.state.lastShader) {
         this.flushVertexBuffer();
         var4.enable();
         this.state.lastShader = var4;
         this.state.isXformValid = false;
         var2 |= 2;
      }

      if ((var2 & 2) != 0 && (!this.state.isXformValid || !var3.equals(this.state.lastTransform))) {
         this.flushVertexBuffer();
         this.updateShaderTransform(var4, var3);
         this.state.lastTransform.setTransform(var3);
         this.state.isXformValid = true;
      }

      if ((var2 & 4) != 0) {
         Rectangle var5 = var1.getClipRectNoClone();
         if (var5 != this.state.lastClip) {
            this.flushVertexBuffer();
            this.updateClipRect(var5);
            this.state.lastClip = var5;
         }
      }

      if ((var2 & 8) != 0) {
         CompositeMode var6 = var1.getCompositeMode();
         if (var6 != this.state.lastComp) {
            this.flushVertexBuffer();
            this.updateCompositeMode(var6);
            this.state.lastComp = var6;
         }
      }

   }

   private void setTexture(int var1, Texture var2) {
      if (var2 != null) {
         var2.assertLocked();
      }

      if (var2 != this.state.lastTextures[var1]) {
         this.flushVertexBuffer();
         this.updateTexture(var1, var2);
         this.state.lastTextures[var1] = var2;
      }

   }

   public void initLCDBuffer(int var1, int var2) {
      this.lcdBuffer = this.factory.createRTTexture(var1, var2, Texture.WrapMode.CLAMP_NOT_NEEDED);
      this.lcdBuffer.makePermanent();
   }

   public void disposeLCDBuffer() {
      if (this.lcdBuffer != null) {
         this.lcdBuffer.dispose();
         this.lcdBuffer = null;
      }

   }

   public RTTexture getLCDBuffer() {
      return this.lcdBuffer;
   }

   public void validateLCDBuffer(RenderTarget var1) {
      if (this.lcdBuffer == null || this.lcdBuffer.getPhysicalWidth() < var1.getPhysicalWidth() || this.lcdBuffer.getPhysicalHeight() < var1.getPhysicalHeight()) {
         this.disposeLCDBuffer();
         this.initLCDBuffer(var1.getPhysicalWidth(), var1.getPhysicalHeight());
      }

   }

   public abstract void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

   protected void setRenderTarget(RenderTarget var1, NGCamera var2, boolean var3, boolean var4) {
      if (var1 instanceof Texture) {
         ((Texture)var1).assertLocked();
      }

      if (this.state == null || var4 != this.state.lastState3D || var1 != this.state.lastRenderTarget || var2 != this.state.lastCamera || var3 != this.state.lastDepthTest) {
         this.flushVertexBuffer();
         this.state = this.updateRenderTarget(var1, var2, var3);
         this.state.lastRenderTarget = var1;
         this.state.lastCamera = var2;
         this.state.lastDepthTest = var3;
         this.state.isXformValid = false;
         if (var4 != this.state.lastState3D) {
            this.state.lastState3D = var4;
            this.state.lastShader = null;
            this.state.lastConst1 = Float.NaN;
            this.state.lastConst2 = Float.NaN;
            this.state.lastConst3 = Float.NaN;
            this.state.lastConst4 = Float.NaN;
            this.state.lastConst5 = Float.NaN;
            this.state.lastConst6 = Float.NaN;
            this.state.lastComp = null;
            this.state.lastClip = null;

            for(int var5 = 0; var5 != this.state.lastTextures.length; ++var5) {
               this.state.lastTextures[var5] = null;
            }

            if (var4) {
               this.setDeviceParametersFor3D();
            } else {
               this.setDeviceParametersFor2D();
            }
         }
      }

   }

   protected void releaseRenderTarget() {
      if (this.state != null) {
         this.state.lastRenderTarget = null;

         for(int var1 = 0; var1 < this.state.lastTextures.length; ++var1) {
            this.state.lastTextures[var1] = null;
         }
      }

   }

   public static class State {
      private Shader lastShader;
      private RenderTarget lastRenderTarget;
      private NGCamera lastCamera;
      private boolean lastDepthTest;
      private BaseTransform lastTransform = new Affine3D();
      private Rectangle lastClip;
      private CompositeMode lastComp;
      private Texture[] lastTextures = new Texture[4];
      private boolean isXformValid;
      private float lastConst1 = Float.NaN;
      private float lastConst2 = Float.NaN;
      private float lastConst3 = Float.NaN;
      private float lastConst4 = Float.NaN;
      private float lastConst5 = Float.NaN;
      private float lastConst6 = Float.NaN;
      private boolean lastState3D = false;
   }

   public static enum SpecialShaderType {
      TEXTURE_RGB("Solid_TextureRGB"),
      TEXTURE_MASK_RGB("Mask_TextureRGB"),
      TEXTURE_YV12("Solid_TextureYV12"),
      TEXTURE_First_LCD("Solid_TextureFirstPassLCD"),
      TEXTURE_SECOND_LCD("Solid_TextureSecondPassLCD"),
      SUPER("Mask_TextureSuper");

      private String name;

      private SpecialShaderType(String var3) {
         this.name = var3;
      }

      public String getName() {
         return this.name;
      }
   }

   public static enum MaskType {
      SOLID("Solid"),
      TEXTURE("Texture"),
      ALPHA_ONE("AlphaOne", true),
      ALPHA_TEXTURE("AlphaTexture", true),
      ALPHA_TEXTURE_DIFF("AlphaTextureDifference", true),
      FILL_PGRAM("FillPgram"),
      DRAW_PGRAM("DrawPgram", FILL_PGRAM),
      FILL_CIRCLE("FillCircle"),
      DRAW_CIRCLE("DrawCircle", FILL_CIRCLE),
      FILL_ELLIPSE("FillEllipse"),
      DRAW_ELLIPSE("DrawEllipse", FILL_ELLIPSE),
      FILL_ROUNDRECT("FillRoundRect"),
      DRAW_ROUNDRECT("DrawRoundRect", FILL_ROUNDRECT),
      DRAW_SEMIROUNDRECT("DrawSemiRoundRect");

      private String name;
      private MaskType filltype;
      private boolean newPaintStyle;

      private MaskType(String var3) {
         this.name = var3;
      }

      private MaskType(String var3, boolean var4) {
         this.name = var3;
         this.newPaintStyle = var4;
      }

      private MaskType(String var3, MaskType var4) {
         this.name = var3;
         this.filltype = var4;
      }

      public String getName() {
         return this.name;
      }

      public MaskType getFillType() {
         return this.filltype;
      }

      public boolean isNewPaintStyle() {
         return this.newPaintStyle;
      }
   }
}
