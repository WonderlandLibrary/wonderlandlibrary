package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.Texture;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class D3DRTTexture extends D3DTexture implements D3DRenderTarget, RTTexture, ReadbackRenderTarget {
   private boolean opaque = false;

   D3DRTTexture(D3DContext var1, Texture.WrapMode var2, long var3, int var5, int var6, int var7, int var8) {
      super(var1, PixelFormat.INT_ARGB_PRE, var2, var3, var5, var6, var7, var8, true);
   }

   D3DRTTexture(D3DContext var1, Texture.WrapMode var2, long var3, int var5, int var6, int var7, int var8, int var9, int var10, int var11) {
      super(var1, PixelFormat.INT_ARGB_PRE, var2, var3, var5, var6, var7, var8, var9, var10, true, var11, false);
   }

   public Texture getBackBuffer() {
      return this;
   }

   public long getResourceHandle() {
      return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getResource();
   }

   public Graphics createGraphics() {
      return D3DGraphics.create(this, this.getContext());
   }

   public int[] getPixels() {
      return null;
   }

   public boolean readPixels(Buffer var1, int var2, int var3, int var4, int var5) {
      if (var2 == this.getContentX() && var3 == this.getContentY() && var4 == this.getContentWidth() && var5 == this.getContentHeight()) {
         return this.readPixels(var1);
      } else {
         throw new IllegalArgumentException("reading subtexture not yet supported!");
      }
   }

   public boolean readPixels(Buffer var1) {
      this.getContext().flushVertexBuffer();
      long var2 = this.getContext().getContextHandle();
      boolean var4 = false;
      long var7;
      int var9;
      if (var1 instanceof ByteBuffer) {
         ByteBuffer var5 = (ByteBuffer)var1;
         byte[] var6 = var5.hasArray() ? var5.array() : null;
         var7 = (long)var5.capacity();
         var9 = D3DResourceFactory.nReadPixelsB(var2, this.getNativeSourceHandle(), var7, var1, var6, this.getContentWidth(), this.getContentHeight());
      } else {
         if (!(var1 instanceof IntBuffer)) {
            throw new IllegalArgumentException("Buffer of this type is not supported: " + var1);
         }

         IntBuffer var10 = (IntBuffer)var1;
         int[] var11 = var10.hasArray() ? var10.array() : null;
         var7 = (long)(var10.capacity() * 4);
         var9 = D3DResourceFactory.nReadPixelsI(var2, this.getNativeSourceHandle(), var7, var1, var11, this.getContentWidth(), this.getContentHeight());
      }

      return this.getContext().validatePresent(var9);
   }

   public Screen getAssociatedScreen() {
      return this.getContext().getAssociatedScreen();
   }

   public void update(Image var1) {
      throw new UnsupportedOperationException("update() not supported for RTTextures");
   }

   public void update(Image var1, int var2, int var3) {
      throw new UnsupportedOperationException("update() not supported for RTTextures");
   }

   public void update(Image var1, int var2, int var3, int var4, int var5) {
      throw new UnsupportedOperationException("update() not supported for RTTextures");
   }

   public void update(Image var1, int var2, int var3, int var4, int var5, boolean var6) {
      throw new UnsupportedOperationException("update() not supported for RTTextures");
   }

   public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      throw new UnsupportedOperationException("update() not supported for RTTextures");
   }

   public void setOpaque(boolean var1) {
      this.opaque = var1;
   }

   public boolean isOpaque() {
      return this.opaque;
   }

   public boolean isVolatile() {
      return this.getContext().isRTTVolatile();
   }

   public boolean isMSAA() {
      return ((D3DTextureData)((D3DTextureResource)this.resource).getResource()).getSamples() != 0;
   }
}
