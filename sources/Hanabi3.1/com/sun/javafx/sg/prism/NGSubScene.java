package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public class NGSubScene extends NGNode {
   private float slWidth;
   private float slHeight;
   private double lastScaledW;
   private double lastScaledH;
   private RTTexture rtt;
   private RTTexture resolveRTT;
   private NGNode root;
   private boolean renderSG;
   private final boolean depthBuffer;
   private final boolean msaa;
   private Paint fillPaint;
   private NGCamera camera;
   private NGLightBase[] lights;
   private boolean isOpaque;
   static final double THRESHOLD = 0.00390625;

   public NGSubScene(boolean var1, boolean var2) {
      this.resolveRTT = null;
      this.root = null;
      this.renderSG = true;
      this.isOpaque = false;
      this.depthBuffer = var1;
      this.msaa = var2;
   }

   private NGSubScene() {
      this(false, false);
   }

   public void setRoot(NGNode var1) {
      this.root = var1;
   }

   public void setFillPaint(Object var1) {
      this.fillPaint = (Paint)var1;
   }

   public void setCamera(NGCamera var1) {
      this.camera = var1 == null ? NGCamera.INSTANCE : var1;
   }

   public void setWidth(float var1) {
      if (this.slWidth != var1) {
         this.slWidth = var1;
         this.geometryChanged();
         this.invalidateRTT();
      }

   }

   public void setHeight(float var1) {
      if (this.slHeight != var1) {
         this.slHeight = var1;
         this.geometryChanged();
         this.invalidateRTT();
      }

   }

   public NGLightBase[] getLights() {
      return this.lights;
   }

   public void setLights(NGLightBase[] var1) {
      this.lights = var1;
   }

   public void markContentDirty() {
      this.visualsChanged();
   }

   protected void visualsChanged() {
      this.renderSG = true;
      super.visualsChanged();
   }

   protected void geometryChanged() {
      this.renderSG = true;
      super.geometryChanged();
   }

   private void invalidateRTT() {
      if (this.rtt != null) {
         this.rtt.dispose();
         this.rtt = null;
      }

   }

   protected boolean hasOverlappingContents() {
      return false;
   }

   private void applyBackgroundFillPaint(Graphics var1) {
      this.isOpaque = true;
      if (this.fillPaint != null) {
         if (this.fillPaint instanceof Color) {
            Color var2 = (Color)this.fillPaint;
            this.isOpaque = (double)var2.getAlpha() >= 1.0;
            var1.clear(var2);
         } else {
            if (!this.fillPaint.isOpaque()) {
               var1.clear();
               this.isOpaque = false;
            }

            var1.setPaint(this.fillPaint);
            var1.fillRect(0.0F, 0.0F, (float)this.rtt.getContentWidth(), (float)this.rtt.getContentHeight());
         }
      } else {
         this.isOpaque = false;
         var1.clear();
      }

   }

   public void renderForcedContent(Graphics var1) {
      this.root.renderForcedContent(var1);
   }

   private static double hypot(double var0, double var2, double var4) {
      return Math.sqrt(var0 * var0 + var2 * var2 + var4 * var4);
   }

   protected void renderContent(Graphics var1) {
      if (!((double)this.slWidth <= 0.0) && !((double)this.slHeight <= 0.0)) {
         BaseTransform var2 = var1.getTransformNoClone();
         double var3 = hypot(var2.getMxx(), var2.getMyx(), var2.getMzx());
         double var5 = hypot(var2.getMxy(), var2.getMyy(), var2.getMzy());
         double var7 = (double)this.slWidth * var3;
         double var9 = (double)this.slHeight * var5;
         int var11 = (int)Math.ceil(var7 - 0.00390625);
         int var12 = (int)Math.ceil(var9 - 0.00390625);
         if (Math.max(Math.abs(var7 - this.lastScaledW), Math.abs(var9 - this.lastScaledH)) > 0.00390625) {
            if (this.rtt != null && (var11 != this.rtt.getContentWidth() || var12 != this.rtt.getContentHeight())) {
               this.invalidateRTT();
            }

            this.renderSG = true;
            this.lastScaledW = var7;
            this.lastScaledH = var9;
         }

         if (this.rtt != null) {
            this.rtt.lock();
            if (this.rtt.isSurfaceLost()) {
               this.renderSG = true;
               this.rtt = null;
            }
         }

         if (this.renderSG || !this.root.isClean()) {
            if (this.rtt == null) {
               ResourceFactory var13 = var1.getResourceFactory();
               this.rtt = var13.createRTTexture(var11, var12, Texture.WrapMode.CLAMP_TO_ZERO, this.msaa);
            }

            Graphics var28 = this.rtt.createGraphics();
            var28.scale((float)var3, (float)var5);
            var28.setLights(this.lights);
            var28.setDepthBuffer(this.depthBuffer);
            if (this.camera != null) {
               var28.setCamera(this.camera);
            }

            this.applyBackgroundFillPaint(var28);
            this.root.render(var28);
            this.root.clearDirtyTree();
            this.renderSG = false;
         }

         if (this.msaa) {
            int var29 = this.rtt.getContentX();
            int var14 = this.rtt.getContentY();
            int var15 = var29 + var11;
            int var16 = var14 + var12;
            if ((this.isOpaque || var1.getCompositeMode() == CompositeMode.SRC) && isDirectBlitTransform(var2, var3, var5) && !var1.isDepthTest()) {
               int var17 = (int)(var2.getMxt() + 0.5);
               int var18 = (int)(var2.getMyt() + 0.5);
               RenderTarget var19 = var1.getRenderTarget();
               int var20 = var19.getContentX() + var17;
               int var21 = var19.getContentY() + var18;
               int var22 = var20 + var11;
               int var23 = var21 + var12;
               int var24 = var19.getContentWidth();
               int var25 = var19.getContentHeight();
               int var26 = var22 > var24 ? var24 - var22 : 0;
               int var27 = var23 > var25 ? var25 - var23 : 0;
               var1.blit(this.rtt, (RTTexture)null, var29, var14, var15 + var26, var16 + var27, var20, var21, var22 + var26, var23 + var27);
            } else {
               if (this.resolveRTT != null && (this.resolveRTT.getContentWidth() < var11 || this.resolveRTT.getContentHeight() < var12)) {
                  this.resolveRTT.dispose();
                  this.resolveRTT = null;
               }

               if (this.resolveRTT != null) {
                  this.resolveRTT.lock();
                  if (this.resolveRTT.isSurfaceLost()) {
                     this.resolveRTT = null;
                  }
               }

               if (this.resolveRTT == null) {
                  this.resolveRTT = var1.getResourceFactory().createRTTexture(var11, var12, Texture.WrapMode.CLAMP_TO_ZERO, false);
               }

               this.resolveRTT.createGraphics().blit(this.rtt, this.resolveRTT, var29, var14, var15, var16, var29, var14, var15, var16);
               var1.drawTexture(this.resolveRTT, 0.0F, 0.0F, (float)((double)var11 / var3), (float)((double)var12 / var5), 0.0F, 0.0F, (float)var11, (float)var12);
               this.resolveRTT.unlock();
            }
         } else {
            var1.drawTexture(this.rtt, 0.0F, 0.0F, (float)((double)var11 / var3), (float)((double)var12 / var5), 0.0F, 0.0F, (float)var11, (float)var12);
         }

         this.rtt.unlock();
      }
   }

   private static boolean isDirectBlitTransform(BaseTransform var0, double var1, double var3) {
      if (var1 == 1.0 && var3 == 1.0) {
         return var0.isTranslateOrIdentity();
      } else if (!var0.is2D()) {
         return false;
      } else {
         return var0.getMxx() == var1 && var0.getMxy() == 0.0 && var0.getMyx() == 0.0 && var0.getMyy() == var3;
      }
   }

   public NGCamera getCamera() {
      return this.camera;
   }
}
