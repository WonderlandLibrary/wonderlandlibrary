package org.newdawn.slick.opengl;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.BufferUtils;

public class TGAImageData implements LoadableImageData {
   private int texWidth;
   private int texHeight;
   private int width;
   private int height;
   private short pixelDepth;

   private short flipEndian(short var1) {
      int var2 = var1 & '\uffff';
      return (short)(var2 << 8 | (var2 & '\uff00') >>> 8);
   }

   public int getDepth() {
      return this.pixelDepth;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getTexWidth() {
      return this.texWidth;
   }

   public int getTexHeight() {
      return this.texHeight;
   }

   public ByteBuffer loadImage(InputStream var1) throws IOException {
      return this.loadImage(var1, true, (int[])null);
   }

   public ByteBuffer loadImage(InputStream var1, boolean var2, int[] var3) throws IOException {
      return this.loadImage(var1, var2, false, var3);
   }

   public ByteBuffer loadImage(InputStream var1, boolean var2, boolean var3, int[] var4) throws IOException {
      if (var4 != null) {
         var3 = true;
      }

      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      BufferedInputStream var9 = new BufferedInputStream(var1, 100000);
      DataInputStream var10 = new DataInputStream(var9);
      short var11 = (short)var10.read();
      short var12 = (short)var10.read();
      short var13 = (short)var10.read();
      this.flipEndian(var10.readShort());
      this.flipEndian(var10.readShort());
      short var16 = (short)var10.read();
      this.flipEndian(var10.readShort());
      this.flipEndian(var10.readShort());
      this.width = this.flipEndian(var10.readShort());
      this.height = this.flipEndian(var10.readShort());
      this.pixelDepth = (short)var10.read();
      if (this.pixelDepth == 32) {
         var3 = false;
      }

      this.texWidth = this.get2Fold(this.width);
      this.texHeight = this.get2Fold(this.height);
      short var19 = (short)var10.read();
      if ((var19 & 32) == 0) {
         var2 = !var2;
      }

      if (var11 > 0) {
         var9.skip((long)var11);
      }

      Object var20 = null;
      byte[] var30;
      if (this.pixelDepth != 32 && !var3) {
         if (this.pixelDepth != 24) {
            throw new RuntimeException("Only 24 and 32 bit TGAs are supported");
         }

         var30 = new byte[this.texWidth * this.texHeight * 3];
      } else {
         this.pixelDepth = 32;
         var30 = new byte[this.texWidth * this.texHeight * 4];
      }

      int var21;
      int var22;
      int var23;
      byte var26;
      byte var27;
      byte var28;
      if (this.pixelDepth == 24) {
         if (var2) {
            for(var21 = this.height - 1; var21 >= 0; --var21) {
               for(var22 = 0; var22 < this.width; ++var22) {
                  var28 = var10.readByte();
                  var27 = var10.readByte();
                  var26 = var10.readByte();
                  var23 = (var22 + var21 * this.texWidth) * 3;
                  var30[var23] = var26;
                  var30[var23 + 1] = var27;
                  var30[var23 + 2] = var28;
               }
            }
         } else {
            for(var21 = 0; var21 < this.height; ++var21) {
               for(var22 = 0; var22 < this.width; ++var22) {
                  var28 = var10.readByte();
                  var27 = var10.readByte();
                  var26 = var10.readByte();
                  var23 = (var22 + var21 * this.texWidth) * 3;
                  var30[var23] = var26;
                  var30[var23 + 1] = var27;
                  var30[var23 + 2] = var28;
               }
            }
         }
      } else if (this.pixelDepth == 32) {
         byte var29;
         if (var2) {
            for(var21 = this.height - 1; var21 >= 0; --var21) {
               for(var22 = 0; var22 < this.width; ++var22) {
                  var28 = var10.readByte();
                  var27 = var10.readByte();
                  var26 = var10.readByte();
                  if (var3) {
                     var29 = -1;
                  } else {
                     var29 = var10.readByte();
                  }

                  var23 = (var22 + var21 * this.texWidth) * 4;
                  var30[var23] = var26;
                  var30[var23 + 1] = var27;
                  var30[var23 + 2] = var28;
                  var30[var23 + 3] = var29;
                  if (var29 == 0) {
                     var30[var23 + 2] = 0;
                     var30[var23 + 1] = 0;
                     var30[var23] = 0;
                  }
               }
            }
         } else {
            for(var21 = 0; var21 < this.height; ++var21) {
               for(var22 = 0; var22 < this.width; ++var22) {
                  var28 = var10.readByte();
                  var27 = var10.readByte();
                  var26 = var10.readByte();
                  if (var3) {
                     var29 = -1;
                  } else {
                     var29 = var10.readByte();
                  }

                  var23 = (var22 + var21 * this.texWidth) * 4;
                  if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
                     var30[var23] = var26;
                     var30[var23 + 1] = var27;
                     var30[var23 + 2] = var28;
                     var30[var23 + 3] = var29;
                  } else {
                     var30[var23] = var26;
                     var30[var23 + 1] = var27;
                     var30[var23 + 2] = var28;
                     var30[var23 + 3] = var29;
                  }

                  if (var29 == 0) {
                     var30[var23 + 2] = 0;
                     var30[var23 + 1] = 0;
                     var30[var23] = 0;
                  }
               }
            }
         }
      }

      var1.close();
      if (var4 != null) {
         for(var21 = 0; var21 < var30.length; var21 += 4) {
            boolean var32 = true;

            for(var23 = 0; var23 < 3; ++var23) {
               if (var30[var21 + var23] != var4[var23]) {
                  var32 = false;
               }
            }

            if (var32) {
               var30[var21 + 3] = 0;
            }
         }
      }

      ByteBuffer var31 = BufferUtils.createByteBuffer(var30.length);
      var31.put(var30);
      var22 = this.pixelDepth / 8;
      int var24;
      if (this.height < this.texHeight - 1) {
         var23 = (this.texHeight - 1) * this.texWidth * var22;
         var24 = (this.height - 1) * this.texWidth * var22;

         for(int var25 = 0; var25 < this.texWidth * var22; ++var25) {
            var31.put(var23 + var25, var31.get(var25));
            var31.put(var24 + this.texWidth * var22 + var25, var31.get(this.texWidth * var22 + var25));
         }
      }

      if (this.width < this.texWidth - 1) {
         for(var23 = 0; var23 < this.texHeight; ++var23) {
            for(var24 = 0; var24 < var22; ++var24) {
               var31.put((var23 + 1) * this.texWidth * var22 - var22 + var24, var31.get(var23 * this.texWidth * var22 + var24));
               var31.put(var23 * this.texWidth * var22 + this.width * var22 + var24, var31.get(var23 * this.texWidth * var22 + (this.width - 1) * var22 + var24));
            }
         }
      }

      var31.flip();
      return var31;
   }

   private int get2Fold(int var1) {
      int var2;
      for(var2 = 2; var2 < var1; var2 *= 2) {
      }

      return var2;
   }

   public ByteBuffer getImageBufferData() {
      throw new RuntimeException("TGAImageData doesn't store it's image.");
   }

   public void configureEdging(boolean var1) {
   }
}
