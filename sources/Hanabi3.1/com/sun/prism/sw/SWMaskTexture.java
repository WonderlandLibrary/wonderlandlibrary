package com.sun.prism.sw;

import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class SWMaskTexture extends SWTexture {
   private byte[] data;

   SWMaskTexture(SWResourceFactory var1, Texture.WrapMode var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   SWMaskTexture(SWMaskTexture var1, Texture.WrapMode var2) {
      super(var1, var2);
      this.data = var1.data;
   }

   byte[] getDataNoClone() {
      return this.data;
   }

   public PixelFormat getPixelFormat() {
      return PixelFormat.BYTE_ALPHA;
   }

   public void update(Buffer var1, PixelFormat var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10) {
      if (PrismSettings.debug) {
         System.out.println("MASK TEXTURE, Pixel format: " + var2 + ", buffer: " + var1);
         System.out.println("dstx:" + var3 + " dsty:" + var4);
         System.out.println("srcx:" + var5 + " srcy:" + var6 + " srcw:" + var7 + " srch:" + var8 + " srcscan: " + var9);
      }

      if (var2 != PixelFormat.BYTE_ALPHA) {
         throw new IllegalArgumentException("SWMaskTexture supports BYTE_ALPHA format only.");
      } else {
         this.checkAllocation(var7, var8);
         this.physicalWidth = var7;
         this.physicalHeight = var8;
         this.allocate();
         ByteBuffer var11 = (ByteBuffer)var1;

         for(int var12 = 0; var12 < var8; ++var12) {
            var11.position((var6 + var12) * var9 + var5);
            var11.get(this.data, var12 * this.physicalWidth, var7);
         }

      }
   }

   public void update(MediaFrame var1, boolean var2) {
      throw new UnsupportedOperationException("update6:unimp");
   }

   void checkAllocation(int var1, int var2) {
      if (this.allocated) {
         int var3 = var1 * var2;
         if (var3 > this.data.length) {
            throw new IllegalArgumentException("SRCW * SRCH exceeds buffer length");
         }
      }

   }

   void allocateBuffer() {
      this.data = new byte[this.physicalWidth * this.physicalHeight];
   }

   Texture createSharedLockedTexture(Texture.WrapMode var1) {
      return new SWMaskTexture(this, var1);
   }
}
