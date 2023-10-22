package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;

class D3DSwapChain extends D3DResource implements D3DRenderTarget, Presentable, D3DContextSource {
   private final D3DRTTexture texBackBuffer;
   private final float pixelScaleFactorX;
   private final float pixelScaleFactorY;

   D3DSwapChain(D3DContext var1, long var2, D3DRTTexture var4, float var5, float var6) {
      super(new D3DResource.D3DRecord(var1, var2));
      this.texBackBuffer = var4;
      this.pixelScaleFactorX = var5;
      this.pixelScaleFactorY = var6;
   }

   public void dispose() {
      this.texBackBuffer.dispose();
      super.dispose();
   }

   public boolean prepare(Rectangle var1) {
      D3DContext var2 = this.getContext();
      var2.flushVertexBuffer();
      D3DGraphics var3 = (D3DGraphics)D3DGraphics.create(this, var2);
      if (var3 == null) {
         return false;
      } else {
         int var4 = this.texBackBuffer.getContentWidth();
         int var5 = this.texBackBuffer.getContentHeight();
         int var6 = this.getContentWidth();
         int var7 = this.getContentHeight();
         if (this.isMSAA()) {
            var2.flushVertexBuffer();
            var3.blit(this.texBackBuffer, (RTTexture)null, 0, 0, var4, var5, 0, 0, var6, var7);
         } else {
            var3.setCompositeMode(CompositeMode.SRC);
            var3.drawTexture(this.texBackBuffer, 0.0F, 0.0F, (float)var6, (float)var7, 0.0F, 0.0F, (float)var4, (float)var5);
         }

         var2.flushVertexBuffer();
         this.texBackBuffer.unlock();
         return true;
      }
   }

   public boolean present() {
      D3DContext var1 = this.getContext();
      int var2 = nPresent(var1.getContextHandle(), this.d3dResRecord.getResource());
      return var1.validatePresent(var2);
   }

   public long getResourceHandle() {
      return this.d3dResRecord.getResource();
   }

   public int getPhysicalWidth() {
      return D3DResourceFactory.nGetTextureWidth(this.d3dResRecord.getResource());
   }

   public int getPhysicalHeight() {
      return D3DResourceFactory.nGetTextureHeight(this.d3dResRecord.getResource());
   }

   public int getContentWidth() {
      return this.getPhysicalWidth();
   }

   public int getContentHeight() {
      return this.getPhysicalHeight();
   }

   public int getContentX() {
      return 0;
   }

   public int getContentY() {
      return 0;
   }

   private static native int nPresent(long var0, long var2);

   public D3DContext getContext() {
      return this.d3dResRecord.getContext();
   }

   public boolean lockResources(PresentableState var1) {
      if (var1.getRenderWidth() == this.texBackBuffer.getContentWidth() && var1.getRenderHeight() == this.texBackBuffer.getContentHeight() && var1.getRenderScaleX() == this.pixelScaleFactorX && var1.getRenderScaleY() == this.pixelScaleFactorY) {
         this.texBackBuffer.lock();
         return this.texBackBuffer.isSurfaceLost();
      } else {
         return true;
      }
   }

   public Graphics createGraphics() {
      Graphics var1 = D3DGraphics.create(this.texBackBuffer, this.getContext());
      var1.scale(this.pixelScaleFactorX, this.pixelScaleFactorY);
      return var1;
   }

   public RTTexture getRTTBackBuffer() {
      return this.texBackBuffer;
   }

   public Screen getAssociatedScreen() {
      return this.getContext().getAssociatedScreen();
   }

   public float getPixelScaleFactorX() {
      return this.pixelScaleFactorX;
   }

   public float getPixelScaleFactorY() {
      return this.pixelScaleFactorY;
   }

   public boolean isOpaque() {
      return this.texBackBuffer.isOpaque();
   }

   public void setOpaque(boolean var1) {
      this.texBackBuffer.setOpaque(var1);
   }

   public boolean isMSAA() {
      return this.texBackBuffer != null ? this.texBackBuffer.isMSAA() : false;
   }
}
