package org.lwjgl.util.glu;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Util {
   private static IntBuffer scratch = BufferUtils.createIntBuffer(16);

   protected static int ceil(int var0, int var1) {
      return var0 % var1 == 0 ? var0 / var1 : var0 / var1 + 1;
   }

   protected static float[] normalize(float[] var0) {
      float var1 = (float)Math.sqrt((double)(var0[0] * var0[0] + var0[1] * var0[1] + var0[2] * var0[2]));
      if ((double)var1 == 0.0D) {
         return var0;
      } else {
         var1 = 1.0F / var1;
         var0[0] *= var1;
         var0[1] *= var1;
         var0[2] *= var1;
         return var0;
      }
   }

   protected static void cross(float[] var0, float[] var1, float[] var2) {
      var2[0] = var0[1] * var1[2] - var0[2] * var1[1];
      var2[1] = var0[2] * var1[0] - var0[0] * var1[2];
      var2[2] = var0[0] * var1[1] - var0[1] * var1[0];
   }

   protected static int compPerPix(int var0) {
      switch(var0) {
      case 6400:
      case 6401:
      case 6402:
      case 6403:
      case 6404:
      case 6405:
      case 6406:
      case 6409:
         return 1;
      case 6407:
      case 32992:
         return 3;
      case 6408:
      case 32993:
         return 4;
      case 6410:
         return 2;
      default:
         return -1;
      }
   }

   protected static int nearestPower(int var0) {
      int var1 = 1;
      if (var0 == 0) {
         return -1;
      } else {
         while(var0 != 1) {
            if (var0 == 3) {
               return var1 << 2;
            }

            var0 >>= 1;
            var1 <<= 1;
         }

         return var1;
      }
   }

   protected static int bytesPerPixel(int var0, int var1) {
      byte var2;
      switch(var0) {
      case 6400:
      case 6401:
      case 6402:
      case 6403:
      case 6404:
      case 6405:
      case 6406:
      case 6409:
         var2 = 1;
         break;
      case 6407:
      case 32992:
         var2 = 3;
         break;
      case 6408:
      case 32993:
         var2 = 4;
         break;
      case 6410:
         var2 = 2;
         break;
      default:
         var2 = 0;
      }

      byte var3;
      switch(var1) {
      case 5120:
         var3 = 1;
         break;
      case 5121:
         var3 = 1;
         break;
      case 5122:
         var3 = 2;
         break;
      case 5123:
         var3 = 2;
         break;
      case 5124:
         var3 = 4;
         break;
      case 5125:
         var3 = 4;
         break;
      case 5126:
         var3 = 4;
         break;
      case 6656:
         var3 = 1;
         break;
      default:
         var3 = 0;
      }

      return var2 * var3;
   }

   protected static int glGetIntegerv(int var0) {
      scratch.rewind();
      GL11.glGetInteger(var0, scratch);
      return scratch.get();
   }
}
