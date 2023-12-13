package org.newdawn.slick.util;

public class FastTrig {
   private static double reduceSinAngle(double var0) {
      var0 %= 6.283185307179586D;
      if (Math.abs(var0) > 3.141592653589793D) {
         var0 -= 6.283185307179586D;
      }

      if (Math.abs(var0) > 1.5707963267948966D) {
         var0 = 3.141592653589793D - var0;
      }

      return var0;
   }

   public static double sin(double var0) {
      var0 = reduceSinAngle(var0);
      return Math.abs(var0) <= 0.7853981633974483D ? Math.sin(var0) : Math.cos(1.5707963267948966D - var0);
   }

   public static double cos(double var0) {
      return sin(var0 + 1.5707963267948966D);
   }
}
