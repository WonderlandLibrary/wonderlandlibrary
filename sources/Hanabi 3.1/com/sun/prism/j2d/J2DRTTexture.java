package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class J2DRTTexture extends J2DTexture implements RTTexture {
   protected J2DResourceFactory factory;
   private boolean opaque;

   J2DRTTexture(int var1, int var2, J2DResourceFactory var3) {
      super(new BufferedImage(var1, var2, 3), PixelFormat.INT_ARGB_PRE, IntArgbPre.setter, Texture.WrapMode.CLAMP_TO_ZERO);
      this.factory = var3;
      this.opaque = false;
   }

   public int[] getPixels() {
      BufferedImage var1 = this.getBufferedImage();
      DataBuffer var2 = var1.getRaster().getDataBuffer();
      return var2 instanceof DataBufferInt ? ((DataBufferInt)var2).getData() : null;
   }

   public boolean readPixels(Buffer var1, int var2, int var3, int var4, int var5) {
      if (var2 == this.getContentX() && var3 == this.getContentY() && var4 == this.getContentWidth() && var5 == this.getContentHeight()) {
         return this.readPixels(var1);
      } else {
         throw new IllegalArgumentException("reading subtexture not yet supported!");
      }
   }

   public boolean readPixels(Buffer var1) {
      int var2 = this.getContentWidth();
      int var3 = this.getContentHeight();
      int[] var4 = this.getPixels();
      var1.clear();

      for(int var5 = 0; var5 < var2 * var3; ++var5) {
         int var6 = var4[var5];
         if (var1 instanceof IntBuffer) {
            ((IntBuffer)var1).put(var6);
         } else if (var1 instanceof ByteBuffer) {
            byte var7 = (byte)(var6 >> 24);
            byte var8 = (byte)(var6 >> 16);
            byte var9 = (byte)(var6 >> 8);
            byte var10 = (byte)var6;
            ((ByteBuffer)var1).put(var10);
            ((ByteBuffer)var1).put(var9);
            ((ByteBuffer)var1).put(var8);
            ((ByteBuffer)var1).put(var7);
         }
      }

      var1.rewind();
      return true;
   }

   public Graphics createGraphics() {
      BufferedImage var1 = this.getBufferedImage();
      J2DPresentable var2 = J2DPresentable.create(var1, this.factory);
      Graphics2D var3 = var1.createGraphics();
      return this.factory.createJ2DPrismGraphics(var2, var3);
   }

   Graphics2D createAWTGraphics2D() {
      return this.getBufferedImage().createGraphics();
   }

   public Screen getAssociatedScreen() {
      return this.factory.getScreen();
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

   public boolean isOpaque() {
      return this.opaque;
   }

   public void setOpaque(boolean var1) {
      this.opaque = var1;
   }

   public boolean isVolatile() {
      return false;
   }

   public boolean isMSAA() {
      return false;
   }
}
