package cn.hanabi.utils.animation;

public class EaseUtils {
   public static double easeInSine(double x) {
      return 1.0 - Math.cos(x * Math.PI / 2.0);
   }

   public static double easeOutSine(double x) {
      return Math.sin(x * Math.PI / 2.0);
   }

   public static double easeInOutSine(double x) {
      return -(Math.cos(Math.PI * x) - 1.0) / 2.0;
   }

   public static double easeInQuad(double x) {
      return x * x;
   }

   public static double easeOutQuad(double x) {
      return 1.0 - (1.0 - x) * (1.0 - x);
   }

   public static double easeInOutQuad(double x) {
      return x < 0.5 ? 2.0 * x * x : 1.0 - Math.pow(-2.0 * x + 2.0, 2.0) / 2.0;
   }
}
