package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

final class CLChecks {
   private CLChecks() {
   }

   static int calculateBufferRectSize(PointerBuffer var0, PointerBuffer var1, long var2, long var4) {
      if (!LWJGLUtil.CHECKS) {
         return 0;
      } else {
         long var6 = var0.get(0);
         long var8 = var0.get(1);
         long var10 = var0.get(2);
         if (!LWJGLUtil.DEBUG || var6 >= 0L && var8 >= 0L && var10 >= 0L) {
            long var12 = var1.get(0);
            long var14 = var1.get(1);
            long var16 = var1.get(2);
            if (LWJGLUtil.DEBUG && (var12 < 1L || var14 < 1L || var16 < 1L)) {
               throw new IllegalArgumentException("Invalid cl_mem rectangle region dimensions: " + var12 + " x " + var14 + " x " + var16);
            } else {
               if (var2 == 0L) {
                  var2 = var12;
               } else if (LWJGLUtil.DEBUG && var2 < var12) {
                  throw new IllegalArgumentException("Invalid host row pitch specified: " + var2);
               }

               if (var4 == 0L) {
                  var4 = var2 * var14;
               } else if (LWJGLUtil.DEBUG && var4 < var2 * var14) {
                  throw new IllegalArgumentException("Invalid host slice pitch specified: " + var4);
               }

               return (int)(var10 * var4 + var8 * var2 + var6 + var12 * var14 * var16);
            }
         } else {
            throw new IllegalArgumentException("Invalid cl_mem host offset: " + var6 + ", " + var8 + ", " + var10);
         }
      }
   }

   static int calculateImageSize(PointerBuffer var0, long var1, long var3) {
      if (!LWJGLUtil.CHECKS) {
         return 0;
      } else {
         long var5 = var0.get(0);
         long var7 = var0.get(1);
         long var9 = var0.get(2);
         if (!LWJGLUtil.DEBUG || var5 >= 1L && var7 >= 1L && var9 >= 1L) {
            if (var1 == 0L) {
               var1 = var5;
            } else if (LWJGLUtil.DEBUG && var1 < var5) {
               throw new IllegalArgumentException("Invalid row pitch specified: " + var1);
            }

            if (var3 == 0L) {
               var3 = var1 * var7;
            } else if (LWJGLUtil.DEBUG && var3 < var1 * var7) {
               throw new IllegalArgumentException("Invalid slice pitch specified: " + var3);
            }

            return (int)(var3 * var9);
         } else {
            throw new IllegalArgumentException("Invalid cl_mem image region dimensions: " + var5 + " x " + var7 + " x " + var9);
         }
      }
   }

   static int calculateImage2DSize(ByteBuffer var0, long var1, long var3, long var5) {
      if (!LWJGLUtil.CHECKS) {
         return 0;
      } else if (!LWJGLUtil.DEBUG || var1 >= 1L && var3 >= 1L) {
         int var7 = getElementSize(var0);
         if (var5 == 0L) {
            var5 = var1 * (long)var7;
         } else if (LWJGLUtil.DEBUG && (var5 < var1 * (long)var7 || var5 % (long)var7 != 0L)) {
            throw new IllegalArgumentException("Invalid image_row_pitch specified: " + var5);
         }

         return (int)(var5 * var3);
      } else {
         throw new IllegalArgumentException("Invalid 2D image dimensions: " + var1 + " x " + var3);
      }
   }

   static int calculateImage3DSize(ByteBuffer var0, long var1, long var3, long var5, long var7, long var9) {
      if (!LWJGLUtil.CHECKS) {
         return 0;
      } else if (!LWJGLUtil.DEBUG || var1 >= 1L && var3 >= 1L && var5 >= 2L) {
         int var11 = getElementSize(var0);
         if (var7 == 0L) {
            var7 = var1 * (long)var11;
         } else if (LWJGLUtil.DEBUG && (var7 < var1 * (long)var11 || var7 % (long)var11 != 0L)) {
            throw new IllegalArgumentException("Invalid image_row_pitch specified: " + var7);
         }

         if (var9 == 0L) {
            var9 = var7 * var3;
         } else if (LWJGLUtil.DEBUG && (var7 < var7 * var3 || var9 % var7 != 0L)) {
            throw new IllegalArgumentException("Invalid image_slice_pitch specified: " + var7);
         }

         return (int)(var9 * var5);
      } else {
         throw new IllegalArgumentException("Invalid 3D image dimensions: " + var1 + " x " + var3 + " x " + var5);
      }
   }

   private static int getElementSize(ByteBuffer var0) {
      int var1 = var0.getInt(var0.position() + 0);
      int var2 = var0.getInt(var0.position() + 4);
      return getChannelCount(var1) * getChannelSize(var2);
   }

   private static int getChannelCount(int var0) {
      switch(var0) {
      case 4272:
      case 4273:
      case 4280:
      case 4281:
      case 4282:
         return 1;
      case 4274:
      case 4275:
      case 4283:
         return 2;
      case 4276:
      case 4284:
         return 3;
      case 4277:
      case 4278:
      case 4279:
         return 4;
      default:
         throw new IllegalArgumentException("Invalid cl_channel_order specified: " + LWJGLUtil.toHexString(var0));
      }
   }

   private static int getChannelSize(int var0) {
      switch(var0) {
      case 4304:
      case 4306:
      case 4311:
      case 4314:
         return 1;
      case 4305:
      case 4307:
      case 4308:
      case 4309:
      case 4312:
      case 4315:
      case 4317:
         return 2;
      case 4310:
      case 4313:
      case 4316:
      case 4318:
         return 4;
      default:
         throw new IllegalArgumentException("Invalid cl_channel_type specified: " + LWJGLUtil.toHexString(var0));
      }
   }
}
