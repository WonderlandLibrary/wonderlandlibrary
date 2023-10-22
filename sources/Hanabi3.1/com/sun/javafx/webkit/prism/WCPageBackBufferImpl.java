package com.sun.javafx.webkit.prism;

import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.webkit.graphics.WCCamera;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCPageBackBuffer;

final class WCPageBackBufferImpl extends WCPageBackBuffer implements ResourceFactoryListener {
   private RTTexture texture;
   private boolean listenerAdded = false;
   private float pixelScale;

   WCPageBackBufferImpl(float var1) {
      this.pixelScale = var1;
   }

   private static RTTexture createTexture(int var0, int var1) {
      return GraphicsPipeline.getDefaultResourceFactory().createRTTexture(var0, var1, Texture.WrapMode.CLAMP_NOT_NEEDED);
   }

   public WCGraphicsContext createGraphics() {
      Graphics var1 = this.texture.createGraphics();
      var1.setCamera(WCCamera.INSTANCE);
      var1.scale(this.pixelScale, this.pixelScale);
      return WCGraphicsManager.getGraphicsManager().createGraphicsContext(var1);
   }

   public void disposeGraphics(WCGraphicsContext var1) {
      var1.dispose();
   }

   public void flush(WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      int var6 = var2 + var4;
      int var7 = var3 + var5;
      ((Graphics)var1.getPlatformGraphics()).drawTexture(this.texture, (float)var2, (float)var3, (float)var6, (float)var7, (float)var2 * this.pixelScale, (float)var3 * this.pixelScale, (float)var6 * this.pixelScale, (float)var7 * this.pixelScale);
      this.texture.unlock();
   }

   protected void copyArea(int var1, int var2, int var3, int var4, int var5, int var6) {
      var1 = (int)((float)var1 * this.pixelScale);
      var2 = (int)((float)var2 * this.pixelScale);
      var3 = (int)Math.ceil((double)((float)var3 * this.pixelScale));
      var4 = (int)Math.ceil((double)((float)var4 * this.pixelScale));
      var5 = (int)((float)var5 * this.pixelScale);
      var6 = (int)((float)var6 * this.pixelScale);
      RTTexture var7 = createTexture(var3, var4);
      var7.createGraphics().drawTexture(this.texture, 0.0F, 0.0F, (float)var3, (float)var4, (float)var1, (float)var2, (float)(var1 + var3), (float)(var2 + var4));
      this.texture.createGraphics().drawTexture(var7, (float)(var1 + var5), (float)(var2 + var6), (float)(var1 + var3 + var5), (float)(var2 + var4 + var6), 0.0F, 0.0F, (float)var3, (float)var4);
      var7.dispose();
   }

   public boolean validate(int var1, int var2) {
      var1 = (int)Math.ceil((double)((float)var1 * this.pixelScale));
      var2 = (int)Math.ceil((double)((float)var2 * this.pixelScale));
      if (this.texture != null) {
         this.texture.lock();
         if (this.texture.isSurfaceLost()) {
            this.texture.dispose();
            this.texture = null;
         }
      }

      if (this.texture == null) {
         this.texture = createTexture(var1, var2);
         this.texture.contentsUseful();
         if (this.listenerAdded) {
            this.texture.unlock();
            return false;
         }

         GraphicsPipeline.getDefaultResourceFactory().addFactoryListener(this);
         this.listenerAdded = true;
      } else {
         int var3 = this.texture.getContentWidth();
         int var4 = this.texture.getContentHeight();
         if (var3 != var1 || var4 != var2) {
            RTTexture var5 = createTexture(var1, var2);
            var5.contentsUseful();
            var5.createGraphics().drawTexture(this.texture, 0.0F, 0.0F, (float)Math.min(var1, var3), (float)Math.min(var2, var4));
            this.texture.dispose();
            this.texture = var5;
         }
      }

      return true;
   }

   public void factoryReset() {
      if (this.texture != null) {
         this.texture.dispose();
         this.texture = null;
      }

   }

   public void factoryReleased() {
   }
}
