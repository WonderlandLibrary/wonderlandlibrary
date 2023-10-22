package com.sun.prism.impl;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.paint.PaintUtil;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.paint.Gradient;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BaseContext {
   private final Screen screen;
   private final ResourceFactory factory;
   private final VertexBuffer vertexBuffer;
   private static final int MIN_MASK_DIM = 1024;
   private Texture maskTex;
   private ByteBuffer maskBuffer;
   private ByteBuffer clearBuffer;
   private int curMaskRow;
   private int nextMaskRow;
   private int curMaskCol;
   private int highMaskCol;
   private Texture paintTex;
   private int[] paintPixels;
   private ByteBuffer paintBuffer;
   private Texture rectTex;
   private int rectTexMax;
   private Texture wrapRectTex;
   private Texture ovalTex;
   private final GeneralTransform3D perspectiveTransform = new GeneralTransform3D();
   private final Map greyGlyphCaches = new HashMap();
   private final Map lcdGlyphCaches = new HashMap();

   protected BaseContext(Screen var1, ResourceFactory var2, int var3) {
      this.screen = var1;
      this.factory = var2;
      this.vertexBuffer = new VertexBuffer(this, var3);
   }

   protected void setDeviceParametersFor2D() {
   }

   protected void setDeviceParametersFor3D() {
   }

   public Screen getAssociatedScreen() {
      return this.screen;
   }

   public ResourceFactory getResourceFactory() {
      return this.factory;
   }

   public VertexBuffer getVertexBuffer() {
      return this.vertexBuffer;
   }

   public void flushVertexBuffer() {
      this.vertexBuffer.flush();
   }

   protected final void flushMask() {
      if (this.curMaskRow > 0 || this.curMaskCol > 0) {
         this.maskTex.lock();
         this.maskTex.update(this.maskBuffer, this.maskTex.getPixelFormat(), 0, 0, 0, 0, this.highMaskCol, this.nextMaskRow, this.maskTex.getContentWidth(), true);
         this.maskTex.unlock();
         this.curMaskRow = this.curMaskCol = this.nextMaskRow = this.highMaskCol = 0;
      }

   }

   public void drawQuads(float[] var1, byte[] var2, int var3) {
      this.flushMask();
      this.renderQuads(var1, var2, var3);
   }

   protected GeneralTransform3D getPerspectiveTransformNoClone() {
      return this.perspectiveTransform;
   }

   protected void setPerspectiveTransform(GeneralTransform3D var1) {
      if (var1 == null) {
         this.perspectiveTransform.setIdentity();
      } else {
         this.perspectiveTransform.set(var1);
      }

   }

   protected abstract void renderQuads(float[] var1, byte[] var2, int var3);

   public void setRenderTarget(BaseGraphics var1) {
      if (var1 != null) {
         this.setRenderTarget(var1.getRenderTarget(), var1.getCameraNoClone(), var1.isDepthTest() && var1.isDepthBuffer(), var1.isState3D());
      } else {
         this.releaseRenderTarget();
      }

   }

   protected void releaseRenderTarget() {
   }

   protected abstract void setRenderTarget(RenderTarget var1, NGCamera var2, boolean var3, boolean var4);

   public abstract void validateClearOp(BaseGraphics var1);

   public abstract void validatePaintOp(BaseGraphics var1, BaseTransform var2, Texture var3, float var4, float var5, float var6, float var7);

   public abstract void validateTextureOp(BaseGraphics var1, BaseTransform var2, Texture var3, PixelFormat var4);

   public void clearGlyphCaches() {
      this.clearCaches(this.greyGlyphCaches);
      this.clearCaches(this.lcdGlyphCaches);
   }

   private void clearCaches(Map var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         ((FontStrike)var2.next()).clearDesc();
      }

      var2 = var1.values().iterator();

      while(var2.hasNext()) {
         GlyphCache var3 = (GlyphCache)var2.next();
         if (var3 != null) {
            var3.clear();
         }
      }

      var1.clear();
   }

   public abstract RTTexture getLCDBuffer();

   public GlyphCache getGlyphCache(FontStrike var1) {
      Map var2 = var1.getAAMode() == 1 ? this.lcdGlyphCaches : this.greyGlyphCaches;
      return this.getGlyphCache(var1, var2);
   }

   public boolean isSuperShaderEnabled() {
      return false;
   }

   private GlyphCache getGlyphCache(FontStrike var1, Map var2) {
      GlyphCache var3 = (GlyphCache)var2.get(var1);
      if (var3 == null) {
         var3 = new GlyphCache(this, var1);
         var2.put(var1, var3);
      }

      return var3;
   }

   public Texture validateMaskTexture(MaskData var1, boolean var2) {
      int var3 = var2 ? 1 : 0;
      int var4 = var1.getWidth() + var3 + var3;
      int var5 = var1.getHeight() + var3 + var3;
      int var6 = 0;
      int var7 = 0;
      if (this.maskTex != null) {
         this.maskTex.lock();
         if (this.maskTex.isSurfaceLost()) {
            this.maskTex = null;
         } else {
            var6 = this.maskTex.getContentWidth();
            var7 = this.maskTex.getContentHeight();
         }
      }

      if (this.maskTex == null || var6 < var4 || var7 < var5) {
         if (this.maskTex != null) {
            this.flushVertexBuffer();
            this.maskTex.dispose();
            this.maskTex = null;
         }

         this.maskBuffer = null;
         int var8 = Math.max(1024, Math.max(var4, var6));
         int var9 = Math.max(1024, Math.max(var5, var7));
         this.maskTex = this.getResourceFactory().createMaskTexture(var8, var9, Texture.WrapMode.CLAMP_NOT_NEEDED);
         this.maskBuffer = ByteBuffer.allocate(var8 * var9);
         if (this.clearBuffer == null || this.clearBuffer.capacity() < var8) {
            this.clearBuffer = null;
            this.clearBuffer = ByteBuffer.allocate(var8);
         }

         this.curMaskRow = this.curMaskCol = this.nextMaskRow = this.highMaskCol = 0;
      }

      return this.maskTex;
   }

   public void updateMaskTexture(MaskData var1, RectBounds var2, boolean var3) {
      this.maskTex.assertLocked();
      int var4 = var1.getWidth();
      int var5 = var1.getHeight();
      int var6 = this.maskTex.getContentWidth();
      int var7 = this.maskTex.getContentHeight();
      int var8 = var3 ? 1 : 0;
      int var9 = var4 + var8 + var8;
      int var10 = var5 + var8 + var8;
      if (this.curMaskCol + var9 > var6) {
         this.curMaskCol = 0;
         this.curMaskRow = this.nextMaskRow;
      }

      if (this.curMaskRow + var10 > var7) {
         this.flushVertexBuffer();
      }

      int var11 = this.curMaskRow * var6 + this.curMaskCol;
      ByteToBytePixelConverter var12 = ByteGray.ToByteGrayConverter();
      if (var3) {
         var12.convert((Buffer)this.clearBuffer, 0, 0, (Buffer)this.maskBuffer, var11, var6, var4 + 1, 1);
         int var13 = var11 + var4 + 1;
         var12.convert((Buffer)this.clearBuffer, 0, 0, (Buffer)this.maskBuffer, var13, var6, 1, var5 + 1);
         var13 = var11 + var6;
         var12.convert((Buffer)this.clearBuffer, 0, 0, (Buffer)this.maskBuffer, var13, var6, 1, var5 + 1);
         var13 = var11 + (var5 + 1) * var6 + 1;
         var12.convert((Buffer)this.clearBuffer, 0, 0, (Buffer)this.maskBuffer, var13, var6, var4 + 1, 1);
         var11 += var6 + 1;
      }

      var12.convert((Buffer)var1.getMaskBuffer(), 0, var4, (Buffer)this.maskBuffer, var11, var6, var4, var5);
      float var15 = (float)this.maskTex.getPhysicalWidth();
      float var14 = (float)this.maskTex.getPhysicalHeight();
      var2.setMinX((float)(this.curMaskCol + var8) / var15);
      var2.setMinY((float)(this.curMaskRow + var8) / var14);
      var2.setMaxX((float)(this.curMaskCol + var8 + var4) / var15);
      var2.setMaxY((float)(this.curMaskRow + var8 + var5) / var14);
      this.curMaskCol += var9;
      if (this.highMaskCol < this.curMaskCol) {
         this.highMaskCol = this.curMaskCol;
      }

      if (this.nextMaskRow < this.curMaskRow + var10) {
         this.nextMaskRow = this.curMaskRow + var10;
      }

   }

   public int getRectTextureMaxSize() {
      if (this.rectTex == null) {
         this.createRectTexture();
      }

      return this.rectTexMax;
   }

   public Texture getRectTexture() {
      if (this.rectTex == null) {
         this.createRectTexture();
      }

      this.rectTex.lock();
      return this.rectTex;
   }

   private void createRectTexture() {
      int var1 = PrismSettings.primTextureSize;
      if (var1 < 0) {
         var1 = this.getResourceFactory().getMaximumTextureSize();
      }

      int var2 = 3;

      for(int var3 = 2; var2 + var3 + 1 <= var1; var2 += var3) {
         this.rectTexMax = var3++;
      }

      byte[] var4 = new byte[var2 * var2];
      int var5 = 1;

      int var8;
      for(int var6 = 1; var6 <= this.rectTexMax; ++var6) {
         int var7 = 1;

         for(var8 = 1; var8 <= this.rectTexMax; ++var8) {
            int var9 = var5 * var2 + var7;

            for(int var10 = 0; var10 < var6; ++var10) {
               for(int var11 = 0; var11 < var8; ++var11) {
                  var4[var9 + var11] = -1;
               }

               var9 += var2;
            }

            var7 += var8 + 1;
         }

         var5 += var6 + 1;
      }

      if (PrismSettings.verbose) {
         System.out.println("max rectangle texture cell size = " + this.rectTexMax);
      }

      Texture var12 = this.getResourceFactory().createMaskTexture(var2, var2, Texture.WrapMode.CLAMP_NOT_NEEDED);
      var12.contentsUseful();
      var12.makePermanent();
      PixelFormat var13 = var12.getPixelFormat();
      var8 = var2 * var13.getBytesPerPixelUnit();
      var12.update(ByteBuffer.wrap(var4), var13, 0, 0, 0, 0, var2, var2, var8, false);
      this.rectTex = var12;
   }

   public Texture getWrapRectTexture() {
      if (this.wrapRectTex == null) {
         Texture var1 = this.getResourceFactory().createMaskTexture(2, 2, Texture.WrapMode.CLAMP_TO_EDGE);
         var1.contentsUseful();
         var1.makePermanent();
         int var2 = var1.getPhysicalWidth();
         int var3 = var1.getPhysicalHeight();
         if (PrismSettings.verbose) {
            System.out.println("wrap rectangle texture = " + var2 + " x " + var3);
         }

         byte[] var4 = new byte[var2 * var3];
         int var5 = var2;

         int var7;
         for(int var6 = 1; var6 < var3; ++var6) {
            for(var7 = 1; var7 < var3; ++var7) {
               var4[var5 + var7] = -1;
            }

            var5 += var2;
         }

         PixelFormat var8 = var1.getPixelFormat();
         var7 = var2 * var8.getBytesPerPixelUnit();
         var1.update(ByteBuffer.wrap(var4), var8, 0, 0, 0, 0, var2, var3, var7, false);
         this.wrapRectTex = var1;
      }

      this.wrapRectTex.lock();
      return this.wrapRectTex;
   }

   public Texture getOvalTexture() {
      if (this.ovalTex == null) {
         int var1 = this.getRectTextureMaxSize();
         int var2 = var1 * (var1 + 1) / 2;
         var2 += var1 + 1;
         byte[] var3 = new byte[var2 * var2];
         int var4 = 1;

         int var7;
         for(int var5 = 1; var5 <= var1; ++var5) {
            int var6 = 1;

            for(var7 = 1; var7 <= var1; ++var7) {
               int var8 = var4 * var2 + var6;

               for(int var9 = 0; var9 < var5; ++var9) {
                  int var11;
                  int var19;
                  if (var9 * 2 >= var5) {
                     int var18 = var5 - 1 - var9;
                     var11 = var8 + (var18 - var9) * var2;

                     for(var19 = 0; var19 < var7; ++var19) {
                        var3[var8 + var19] = var3[var11 + var19];
                     }
                  } else {
                     float var10 = (float)var9 + 0.0625F;

                     for(var11 = 0; var11 < 8; ++var11) {
                        float var12 = var10 / (float)var5 - 0.5F;
                        var12 = (float)Math.sqrt((double)(0.25F - var12 * var12));
                        int var13 = Math.round((float)var7 * 4.0F * (1.0F - var12 * 2.0F));
                        int var14 = var13 >> 3;
                        int var15 = var13 & 7;
                        var3[var8 + var14] = (byte)(var3[var8 + var14] + (8 - var15));
                        var3[var8 + var14 + 1] = (byte)(var3[var8 + var14 + 1] + var15);
                        var10 += 0.125F;
                     }

                     var11 = 0;

                     for(var19 = 0; var19 < var7; ++var19) {
                        if (var19 * 2 >= var7) {
                           var3[var8 + var19] = var3[var8 + var7 - 1 - var19];
                        } else {
                           var11 += var3[var8 + var19];
                           var3[var8 + var19] = (byte)((var11 * 255 + 32) / 64);
                        }
                     }

                     var3[var8 + var7] = 0;
                  }

                  var8 += var2;
               }

               var6 += var7 + 1;
            }

            var4 += var5 + 1;
         }

         Texture var16 = this.getResourceFactory().createMaskTexture(var2, var2, Texture.WrapMode.CLAMP_NOT_NEEDED);
         var16.contentsUseful();
         var16.makePermanent();
         PixelFormat var17 = var16.getPixelFormat();
         var7 = var2 * var17.getBytesPerPixelUnit();
         var16.update(ByteBuffer.wrap(var3), var17, 0, 0, 0, 0, var2, var2, var7, false);
         this.ovalTex = var16;
      }

      this.ovalTex.lock();
      return this.ovalTex;
   }

   public Texture getGradientTexture(Gradient var1, BaseTransform var2, int var3, int var4, MaskData var5, float var6, float var7, float var8, float var9) {
      int var10 = var3 * var4;
      int var11 = var10 * 4;
      if (this.paintBuffer == null || this.paintBuffer.capacity() < var11) {
         this.paintPixels = new int[var10];
         this.paintBuffer = ByteBuffer.wrap(new byte[var11]);
      }

      if (this.paintTex != null) {
         this.paintTex.lock();
         if (this.paintTex.isSurfaceLost()) {
            this.paintTex = null;
         }
      }

      int var13;
      if (this.paintTex == null || this.paintTex.getContentWidth() < var3 || this.paintTex.getContentHeight() < var4) {
         int var12 = var3;
         var13 = var4;
         if (this.paintTex != null) {
            var12 = Math.max(var3, this.paintTex.getContentWidth());
            var13 = Math.max(var4, this.paintTex.getContentHeight());
            this.paintTex.dispose();
         }

         this.paintTex = this.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, var12, var13);
      }

      PaintUtil.fillImageWithGradient(this.paintPixels, var1, var2, 0, 0, var3, var4, var6, var7, var8, var9);
      byte[] var18 = this.paintBuffer.array();
      int var14;
      int var15;
      if (var5 != null) {
         byte[] var19 = var5.getMaskBuffer().array();
         var14 = 0;

         for(var15 = 0; var15 < var10; ++var15) {
            int var16 = this.paintPixels[var15];
            int var17 = var19[var15] & 255;
            var18[var14++] = (byte)((var16 & 255) * var17 / 255);
            var18[var14++] = (byte)((var16 >> 8 & 255) * var17 / 255);
            var18[var14++] = (byte)((var16 >> 16 & 255) * var17 / 255);
            var18[var14++] = (byte)((var16 >>> 24) * var17 / 255);
         }
      } else {
         var13 = 0;

         for(var14 = 0; var14 < var10; ++var14) {
            var15 = this.paintPixels[var14];
            var18[var13++] = (byte)(var15 & 255);
            var18[var13++] = (byte)(var15 >> 8 & 255);
            var18[var13++] = (byte)(var15 >> 16 & 255);
            var18[var13++] = (byte)(var15 >>> 24);
         }
      }

      this.paintTex.update(this.paintBuffer, PixelFormat.BYTE_BGRA_PRE, 0, 0, 0, 0, var3, var4, var3 * 4, false);
      return this.paintTex;
   }
}
