package org.lwjgl.util.glu;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class MipMap extends Util {
   public static int gluBuild2DMipmaps(int var0, int var1, int var2, int var3, int var4, int var5, ByteBuffer var6) {
      if (var2 >= 1 && var3 >= 1) {
         int var7 = bytesPerPixel(var4, var5);
         if (var7 == 0) {
            return 100900;
         } else {
            int var8 = glGetIntegerv(3379);
            int var9 = nearestPower(var2);
            if (var9 > var8) {
               var9 = var8;
            }

            int var10 = nearestPower(var3);
            if (var10 > var8) {
               var10 = var8;
            }

            PixelStoreState var11 = new PixelStoreState();
            GL11.glPixelStorei(3330, 0);
            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3331, 0);
            GL11.glPixelStorei(3332, 0);
            int var13 = 0;
            boolean var14 = false;
            ByteBuffer var12;
            if (var9 == var2 && var10 == var3) {
               var12 = var6;
            } else {
               var12 = BufferUtils.createByteBuffer((var9 + 4) * var10 * var7);
               int var15 = gluScaleImage(var4, var2, var3, var5, var6, var9, var10, var5, var12);
               if (var15 != 0) {
                  var13 = var15;
                  var14 = true;
               }

               GL11.glPixelStorei(3314, 0);
               GL11.glPixelStorei(3317, 1);
               GL11.glPixelStorei(3315, 0);
               GL11.glPixelStorei(3316, 0);
            }

            ByteBuffer var22 = null;
            ByteBuffer var16 = null;

            for(int var17 = 0; !var14; ++var17) {
               if (var12 != var6) {
                  GL11.glPixelStorei(3314, 0);
                  GL11.glPixelStorei(3317, 1);
                  GL11.glPixelStorei(3315, 0);
                  GL11.glPixelStorei(3316, 0);
               }

               GL11.glTexImage2D(var0, var17, var1, var9, var10, 0, var4, var5, (ByteBuffer)var12);
               if (var9 == 1 && var10 == 1) {
                  break;
               }

               int var18 = var9 < 2 ? 1 : var9 >> 1;
               int var19 = var10 < 2 ? 1 : var10 >> 1;
               ByteBuffer var20;
               if (var22 == null) {
                  var20 = var22 = BufferUtils.createByteBuffer((var18 + 4) * var19 * var7);
               } else if (var16 == null) {
                  var20 = var16 = BufferUtils.createByteBuffer((var18 + 4) * var19 * var7);
               } else {
                  var20 = var16;
               }

               int var21 = gluScaleImage(var4, var9, var10, var5, var12, var18, var19, var5, var20);
               if (var21 != 0) {
                  var13 = var21;
                  var14 = true;
               }

               var12 = var20;
               if (var16 != null) {
                  var16 = var22;
               }

               var9 = var18;
               var10 = var19;
            }

            var11.save();
            return var13;
         }
      } else {
         return 100901;
      }
   }

   public static int gluScaleImage(int var0, int var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, ByteBuffer var8) {
      int var9 = compPerPix(var0);
      if (var9 == -1) {
         return 100900;
      } else {
         float[] var13 = new float[var1 * var2 * var9];
         float[] var14 = new float[var5 * var6 * var9];
         byte var17;
         switch(var3) {
         case 5121:
            var17 = 1;
            break;
         case 5126:
            var17 = 4;
            break;
         default:
            return 1280;
         }

         byte var18;
         switch(var7) {
         case 5121:
            var18 = 1;
            break;
         case 5126:
            var18 = 4;
            break;
         default:
            return 1280;
         }

         PixelStoreState var21 = new PixelStoreState();
         int var20;
         if (var21.unpackRowLength > 0) {
            var20 = var21.unpackRowLength;
         } else {
            var20 = var1;
         }

         int var19;
         if (var17 >= var21.unpackAlignment) {
            var19 = var9 * var20;
         } else {
            var19 = var21.unpackAlignment / var17 * ceil(var9 * var20 * var17, var21.unpackAlignment);
         }

         int var10;
         int var11;
         int var12;
         int var22;
         label184:
         switch(var3) {
         case 5121:
            var12 = 0;
            var4.rewind();
            var10 = 0;

            while(true) {
               if (var10 >= var2) {
                  break label184;
               }

               var22 = var10 * var19 + var21.unpackSkipRows * var19 + var21.unpackSkipPixels * var9;

               for(var11 = 0; var11 < var1 * var9; ++var11) {
                  var13[var12++] = (float)(var4.get(var22++) & 255);
               }

               ++var10;
            }
         case 5126:
            var12 = 0;
            var4.rewind();
            var10 = 0;

            while(true) {
               if (var10 >= var2) {
                  break label184;
               }

               var22 = 4 * (var10 * var19 + var21.unpackSkipRows * var19 + var21.unpackSkipPixels * var9);

               for(var11 = 0; var11 < var1 * var9; ++var11) {
                  var13[var12++] = var4.getFloat(var22);
                  var22 += 4;
               }

               ++var10;
            }
         default:
            return 100900;
         }

         float var15 = (float)var1 / (float)var5;
         float var16 = (float)var2 / (float)var6;
         float[] var35 = new float[var9];

         int var25;
         for(var25 = 0; var25 < var6; ++var25) {
            for(int var26 = 0; var26 < var5; ++var26) {
               int var27 = (int)((float)var26 * var15);
               int var28 = (int)((float)(var26 + 1) * var15);
               int var29 = (int)((float)var25 * var16);
               int var30 = (int)((float)(var25 + 1) * var16);
               int var31 = 0;

               int var32;
               for(var32 = 0; var32 < var9; ++var32) {
                  var35[var32] = 0.0F;
               }

               int var23;
               for(var32 = var27; var32 < var28; ++var32) {
                  for(int var33 = var29; var33 < var30; ++var33) {
                     var23 = (var33 * var1 + var32) * var9;

                     for(int var34 = 0; var34 < var9; ++var34) {
                        var35[var34] += var13[var23 + var34];
                     }

                     ++var31;
                  }
               }

               int var24 = (var25 * var5 + var26) * var9;
               if (var31 == 0) {
                  var23 = (var29 * var1 + var27) * var9;

                  for(var32 = 0; var32 < var9; ++var32) {
                     var14[var24++] = var13[var23 + var32];
                  }
               } else {
                  for(var12 = 0; var12 < var9; ++var12) {
                     var14[var24++] = var35[var12] / (float)var31;
                  }
               }
            }
         }

         if (var21.packRowLength > 0) {
            var20 = var21.packRowLength;
         } else {
            var20 = var5;
         }

         if (var18 >= var21.packAlignment) {
            var19 = var9 * var20;
         } else {
            var19 = var21.packAlignment / var18 * ceil(var9 * var20 * var18, var21.packAlignment);
         }

         switch(var7) {
         case 5121:
            var12 = 0;

            for(var10 = 0; var10 < var6; ++var10) {
               var25 = var10 * var19 + var21.packSkipRows * var19 + var21.packSkipPixels * var9;

               for(var11 = 0; var11 < var5 * var9; ++var11) {
                  var8.put(var25++, (byte)((int)var14[var12++]));
               }
            }

            return 0;
         case 5126:
            var12 = 0;

            for(var10 = 0; var10 < var6; ++var10) {
               var25 = 4 * (var10 * var19 + var21.unpackSkipRows * var19 + var21.unpackSkipPixels * var9);

               for(var11 = 0; var11 < var5 * var9; ++var11) {
                  var8.putFloat(var25, var14[var12++]);
                  var25 += 4;
               }
            }

            return 0;
         default:
            return 100900;
         }
      }
   }
}
