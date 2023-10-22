package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.pisces.JavaSurface;
import com.sun.pisces.PiscesRenderer;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class SWRTTexture extends SWArgbPreTexture implements RTTexture {
   private PiscesRenderer pr;
   private JavaSurface surface;
   private final Rectangle dimensions = new Rectangle();
   private boolean isOpaque;

   SWRTTexture(SWResourceFactory var1, int var2, int var3) {
      super(var1, Texture.WrapMode.CLAMP_TO_ZERO, var2, var3);
      this.allocate();
      this.surface = new JavaSurface(this.getDataNoClone(), 1, var2, var3);
      this.dimensions.setBounds(0, 0, var2, var3);
   }

   JavaSurface getSurface() {
      return this.surface;
   }

   public int[] getPixels() {
      return this.contentWidth == this.physicalWidth ? this.getDataNoClone() : null;
   }

   public boolean readPixels(Buffer var1, int var2, int var3, int var4, int var5) {
      if (var2 == this.getContentX() && var3 == this.getContentY() && var4 == this.getContentWidth() && var5 == this.getContentHeight()) {
         return this.readPixels(var1);
      } else {
         throw new IllegalArgumentException("reading subtexture not yet supported!");
      }
   }

   public boolean readPixels(Buffer var1) {
      if (PrismSettings.debug) {
         System.out.println("+ SWRTT.readPixels: this: " + this);
      }

      int[] var2 = this.getDataNoClone();
      var1.clear();
      int var4;
      if (var1 instanceof IntBuffer) {
         IntBuffer var3 = (IntBuffer)var1;

         for(var4 = 0; var4 < this.contentHeight; ++var4) {
            var3.put(var2, var4 * this.physicalWidth, this.contentWidth);
         }
      } else {
         if (!(var1 instanceof ByteBuffer)) {
            return false;
         }

         ByteBuffer var11 = (ByteBuffer)var1;

         for(var4 = 0; var4 < this.contentHeight; ++var4) {
            for(int var5 = 0; var5 < this.contentWidth; ++var5) {
               int var6 = var2[var4 * this.physicalWidth + var5];
               byte var7 = (byte)(var6 >> 24);
               byte var8 = (byte)(var6 >> 16);
               byte var9 = (byte)(var6 >> 8);
               byte var10 = (byte)var6;
               var11.put(var10).put(var9).put(var8).put(var7);
            }
         }
      }

      var1.rewind();
      return true;
   }

   public Screen getAssociatedScreen() {
      return this.getResourceFactory().getScreen();
   }

   public Graphics createGraphics() {
      if (this.pr == null) {
         this.pr = new PiscesRenderer(this.surface);
      }

      return new SWGraphics(this, this.getResourceFactory().getContext(), this.pr);
   }

   public boolean isOpaque() {
      return this.isOpaque;
   }

   public void setOpaque(boolean var1) {
      this.isOpaque = var1;
   }

   Rectangle getDimensions() {
      return this.dimensions;
   }

   public boolean isVolatile() {
      return false;
   }

   public boolean isMSAA() {
      return false;
   }
}
