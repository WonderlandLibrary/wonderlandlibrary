package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.IntPixelGetter;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class IntTo4ByteSameConverter extends BaseIntToByteConverter {
   IntTo4ByteSameConverter(IntPixelGetter var1, BytePixelSetter var2) {
      super(var1, var2);
   }

   void doConvert(int[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8) {
      var3 -= var7;
      var6 -= var7 * 4;

      while(true) {
         --var8;
         if (var8 < 0) {
            return;
         }

         for(int var9 = 0; var9 < var7; ++var9) {
            int var10 = var1[var2++];
            var4[var5++] = (byte)var10;
            var4[var5++] = (byte)(var10 >> 8);
            var4[var5++] = (byte)(var10 >> 16);
            var4[var5++] = (byte)(var10 >> 24);
         }

         var2 += var3;
         var5 += var6;
      }
   }

   void doConvert(IntBuffer var1, int var2, int var3, ByteBuffer var4, int var5, int var6, int var7, int var8) {
      var6 -= var7 * 4;

      while(true) {
         --var8;
         if (var8 < 0) {
            return;
         }

         for(int var9 = 0; var9 < var7; ++var9) {
            int var10 = var1.get(var2 + var9);
            var4.put(var5, (byte)var10);
            var4.put(var5 + 1, (byte)(var10 >> 8));
            var4.put(var5 + 2, (byte)(var10 >> 16));
            var4.put(var5 + 3, (byte)(var10 >> 24));
            var5 += 4;
         }

         var2 += var3;
         var5 += var6;
      }
   }
}
