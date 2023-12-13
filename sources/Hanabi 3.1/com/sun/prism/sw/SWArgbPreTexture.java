package com.sun.prism.sw;

import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import java.nio.Buffer;
import java.nio.IntBuffer;

class SWArgbPreTexture extends SWTexture {
   private int[] data;
   private int offset;
   private boolean hasAlpha = true;

   SWArgbPreTexture(SWResourceFactory var1, Texture.WrapMode var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.offset = 0;
   }

   SWArgbPreTexture(SWArgbPreTexture var1, Texture.WrapMode var2) {
      super(var1, var2);
      this.data = var1.data;
      this.offset = var1.offset;
      this.hasAlpha = var1.hasAlpha;
   }

   int[] getDataNoClone() {
      return this.data;
   }

   int getOffset() {
      return this.offset;
   }

   boolean hasAlpha() {
      return this.hasAlpha;
   }

   public PixelFormat getPixelFormat() {
      return PixelFormat.INT_ARGB_PRE;
   }

   public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      if (PrismSettings.debug) {
         System.out.println("ARGB_PRE TEXTURE, Pixel format: " + var2 + ", buffer: " + var1);
         System.out.println("dstx:" + var3 + " dsty:" + var4);
         System.out.println("srcx:" + var5 + " srcy:" + var6 + " srcw:" + var7 + " srch:" + var8 + " srcscan: " + var9);
      }

      this.checkDimensions(var3 + var7, var4 + var8);
      this.allocate();
      Object var11;
      switch (var2) {
         case BYTE_RGB:
            var11 = ByteRgb.getter;
            this.hasAlpha = false;
            break;
         case INT_ARGB_PRE:
            var11 = IntArgbPre.getter;
            var9 >>= 2;
            this.hasAlpha = true;
            break;
         case BYTE_BGRA_PRE:
            var11 = ByteBgraPre.getter;
            this.hasAlpha = true;
            break;
         case BYTE_GRAY:
            var11 = ByteGray.getter;
            this.hasAlpha = false;
            break;
         default:
            throw new UnsupportedOperationException("!!! UNSUPPORTED PIXEL FORMAT: " + var2);
      }

      PixelConverter var12 = PixelUtils.getConverter((PixelGetter)var11, IntArgbPre.setter);
      var1.position(0);
      var12.convert(var1, var6 * var9 + var5, var9, IntBuffer.wrap(this.data), var4 * this.physicalWidth + var3, this.physicalWidth, var7, var8);
   }

   public void update(MediaFrame var1, boolean var2) {
      if (PrismSettings.debug) {
         System.out.println("Media Pixel format: " + var1.getPixelFormat());
      }

      var1.holdFrame();
      if (var1.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
         MediaFrame var3 = var1.convertToFormat(PixelFormat.INT_ARGB_PRE);
         var1.releaseFrame();
         var1 = var3;
      }

      int var6 = var1.strideForPlane(0) / 4;
      IntBuffer var4 = var1.getBufferForPlane(0).asIntBuffer();
      if (var4.hasArray()) {
         this.allocated = false;
         this.offset = 0;
         this.physicalWidth = var6;
         this.data = var4.array();
      } else {
         this.allocate();

         for(int var5 = 0; var5 < this.contentHeight; ++var5) {
            var4.position(this.offset + var5 * var6);
            var4.get(this.data, var5 * this.physicalWidth, this.contentWidth);
         }
      }

      var1.releaseFrame();
   }

   void checkDimensions(int var1, int var2) {
      if (var1 < 0) {
         throw new IllegalArgumentException("srcw must be >=0");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("srch must be >=0");
      } else if (var1 > this.physicalWidth) {
         throw new IllegalArgumentException("srcw exceeds WIDTH");
      } else if (var2 > this.physicalHeight) {
         throw new IllegalArgumentException("srch exceeds HEIGHT");
      }
   }

   void applyCompositeAlpha(float var1) {
      if (!this.allocated) {
         throw new IllegalStateException("Cannot apply composite alpha to texture with non-allocated data");
      } else {
         this.hasAlpha = this.hasAlpha || var1 < 1.0F;

         for(int var3 = 0; var3 < this.data.length; ++var3) {
            int var2 = (int)((float)(this.data[var3] >> 24) * var1 + 0.5F) & 255;
            this.data[var3] = var2 << 24 | this.data[var3] & 16777215;
         }

      }
   }

   void allocateBuffer() {
      this.data = new int[this.physicalWidth * this.physicalHeight];
   }

   Texture createSharedLockedTexture(Texture.WrapMode var1) {
      return new SWArgbPreTexture(this, var1);
   }
}
