package com.jcraft.jorbis;

class Util {
   static int ilog(int var0) {
      int var1;
      for(var1 = 0; var0 != 0; var0 >>>= 1) {
         ++var1;
      }

      return var1;
   }

   static int ilog2(int var0) {
      int var1;
      for(var1 = 0; var0 > 1; var0 >>>= 1) {
         ++var1;
      }

      return var1;
   }

   static int icount(int var0) {
      int var1;
      for(var1 = 0; var0 != 0; var0 >>>= 1) {
         var1 += var0 & 1;
      }

      return var1;
   }
}
