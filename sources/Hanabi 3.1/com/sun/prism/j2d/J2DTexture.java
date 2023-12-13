package com.sun.prism.j2d;

import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgr;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.IntArgbPre;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseTexture;
import com.sun.prism.impl.ManagedResource;
import com.sun.prism.impl.PrismSettings;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class J2DTexture extends BaseTexture {
   private final PixelSetter setter;

   static J2DTexture create(PixelFormat var0, Texture.WrapMode var1, int var2, int var3) {
      byte var4;
      Object var5;
      switch (var0) {
         case BYTE_RGB:
            var4 = 5;
            var5 = ByteBgr.setter;
            break;
         case BYTE_GRAY:
            var4 = 10;
            var5 = ByteGray.setter;
            break;
         case INT_ARGB_PRE:
         case BYTE_BGRA_PRE:
            var4 = 3;
            var5 = IntArgbPre.setter;
            break;
         default:
            throw new InternalError("Unrecognized PixelFormat (" + var0 + ")!");
      }

      J2DTexturePool var6 = J2DTexturePool.instance;
      long var7 = J2DTexturePool.size(var2, var3, var4);
      if (!var6.prepareForAllocation(var7)) {
         return null;
      } else {
         BufferedImage var9 = new BufferedImage(var2, var3, var4);
         return new J2DTexture(var9, var0, (PixelSetter)var5, var1);
      }
   }

   J2DTexture(BufferedImage var1, PixelFormat var2, PixelSetter var3, Texture.WrapMode var4) {
      super(new J2DTexResource(var1), var2, var4, var1.getWidth(), var1.getHeight());
      this.setter = var3;
   }

   J2DTexture(J2DTexture var1, Texture.WrapMode var2) {
      super(var1, var2, false);
      this.setter = var1.setter;
   }

   protected Texture createSharedTexture(Texture.WrapMode var1) {
      return new J2DTexture(this, var1);
   }

   BufferedImage getBufferedImage() {
      return (BufferedImage)((J2DTexResource)this.resource).getResource();
   }

   private static PixelGetter getGetter(PixelFormat var0) {
      switch (var0) {
         case BYTE_RGB:
            return ByteRgb.getter;
         case BYTE_GRAY:
            return ByteGray.getter;
         case INT_ARGB_PRE:
            return IntArgbPre.getter;
         case BYTE_BGRA_PRE:
            return ByteBgraPre.getter;
         default:
            throw new InternalError("Unrecognized PixelFormat (" + var0 + ")!");
      }
   }

   private static Buffer getDstBuffer(BufferedImage var0) {
      if (var0.getType() == 3) {
         int[] var2 = ((DataBufferInt)var0.getRaster().getDataBuffer()).getData();
         return IntBuffer.wrap(var2);
      } else {
         byte[] var1 = ((DataBufferByte)var0.getRaster().getDataBuffer()).getData();
         return ByteBuffer.wrap(var1);
      }
   }

   void updateFromBuffer(BufferedImage var1, Buffer var2, PixelFormat var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      PixelGetter var11 = getGetter(var3);
      PixelConverter var12 = PixelUtils.getConverter(var11, this.setter);
      if (PrismSettings.debug) {
         System.out.println("src = [" + var6 + ", " + var7 + "] x [" + var8 + ", " + var9 + "], dst = [" + var4 + ", " + var5 + "]");
         System.out.println("bimg = " + var1);
         System.out.println("format = " + var3 + ", buffer = " + var2);
         System.out.println("getter = " + var11 + ", setter = " + this.setter);
         System.out.println("converter = " + var12);
      }

      int var13 = var1.getWidth() * this.setter.getNumElements();
      int var14 = var5 * var13 + var4 * this.setter.getNumElements();
      if (var11 instanceof IntPixelGetter) {
         var10 /= 4;
      }

      int var15 = var2.position() + var7 * var10 + var6 * var11.getNumElements();
      var12.convert(var2, var15, var10, getDstBuffer(var1), var14, var13, var8, var9);
   }

   public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      BufferedImage var11 = this.getBufferedImage();
      var1.position(0);
      this.updateFromBuffer(var11, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public void update(MediaFrame var1, boolean var2) {
      var1.holdFrame();
      if (var1.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
         MediaFrame var3 = var1.convertToFormat(PixelFormat.INT_ARGB_PRE);
         var1.releaseFrame();
         var1 = var3;
         if (null == var3) {
            return;
         }
      }

      ByteBuffer var5 = var1.getBufferForPlane(0);
      BufferedImage var4 = this.getBufferedImage();
      this.updateFromBuffer(var4, var5.asIntBuffer(), PixelFormat.INT_ARGB_PRE, 0, 0, 0, 0, var1.getWidth(), var1.getHeight(), var1.strideForPlane(0));
      var1.releaseFrame();
   }

   static class J2DTexResource extends ManagedResource {
      public J2DTexResource(BufferedImage var1) {
         super(var1, J2DTexturePool.instance);
      }

      public void free() {
         ((BufferedImage)this.resource).flush();
      }
   }
}
