package com.sun.prism.impl.shape;

import com.sun.prism.Texture;
import java.nio.ByteBuffer;

public class MaskData {
   private ByteBuffer maskBuffer;
   private int originX;
   private int originY;
   private int width;
   private int height;

   public ByteBuffer getMaskBuffer() {
      return this.maskBuffer;
   }

   public int getOriginX() {
      return this.originX;
   }

   public int getOriginY() {
      return this.originY;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void uploadToTexture(Texture var1, int var2, int var3, boolean var4) {
      int var5 = this.width * var1.getPixelFormat().getBytesPerPixelUnit();
      var1.update(this.maskBuffer, var1.getPixelFormat(), var2, var3, 0, 0, this.width, this.height, var5, var4);
   }

   public void update(ByteBuffer var1, int var2, int var3, int var4, int var5) {
      this.maskBuffer = var1;
      this.originX = var2;
      this.originY = var3;
      this.width = var4;
      this.height = var5;
   }

   public static MaskData create(byte[] var0, int var1, int var2, int var3, int var4) {
      MaskData var5 = new MaskData();
      var5.update(ByteBuffer.wrap(var0), var1, var2, var3, var4);
      return var5;
   }
}
