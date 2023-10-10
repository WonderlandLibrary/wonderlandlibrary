package org.apache.commons.compress.archivers.cpio;

class CpioUtil {
   static long fileType(long var0) {
      return var0 & 61440L;
   }

   static long byteArray2long(byte[] var0, boolean var1) {
      if (var0.length % 2 != 0) {
         throw new UnsupportedOperationException();
      } else {
         long var2 = 0L;
         boolean var4 = false;
         byte[] var5 = new byte[var0.length];
         System.arraycopy(var0, 0, var5, 0, var0.length);
         int var7;
         if (!var1) {
            boolean var6 = false;

            for(var7 = 0; var7 < var5.length; ++var7) {
               byte var8 = var5[var7];
               var5[var7++] = var5[var7];
               var5[var7] = var8;
            }
         }

         var2 = (long)(var5[0] & 255);

         for(var7 = 1; var7 < var5.length; ++var7) {
            var2 <<= 8;
            var2 |= (long)(var5[var7] & 255);
         }

         return var2;
      }
   }

   static byte[] long2byteArray(long var0, int var2, boolean var3) {
      byte[] var4 = new byte[var2];
      boolean var5 = false;
      long var6 = 0L;
      if (var2 % 2 == 0 && var2 >= 2) {
         var6 = var0;

         int var9;
         for(var9 = var2 - 1; var9 >= 0; --var9) {
            var4[var9] = (byte)((int)(var6 & 255L));
            var6 >>= 8;
         }

         if (!var3) {
            boolean var8 = false;

            for(var9 = 0; var9 < var2; ++var9) {
               byte var10 = var4[var9];
               var4[var9++] = var4[var9];
               var4[var9] = var10;
            }
         }

         return var4;
      } else {
         throw new UnsupportedOperationException();
      }
   }
}
