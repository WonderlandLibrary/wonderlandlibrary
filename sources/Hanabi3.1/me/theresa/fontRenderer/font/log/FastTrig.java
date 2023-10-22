package me.theresa.fontRenderer.font.log;

public class FastTrig {
   private static double reduceSinAngle(double radians) {
      radians %= 6.283185307179586;
      if (Math.abs(radians) > Math.PI) {
         radians -= 6.283185307179586;
      }

      if (Math.abs(radians) > 1.5707963267948966) {
         radians = Math.PI - radians;
      }

      return radians;
   }

   public static double sin(double radians) {
      radians = reduceSinAngle(radians);
      return Math.abs(radians) <= 0.7853981633974483 ? Math.sin(radians) : Math.cos(1.5707963267948966 - radians);
   }

   public static double cos(double radians) {
      return sin(radians + 1.5707963267948966);
   }
}
