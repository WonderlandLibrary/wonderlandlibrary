package cn.hanabi.utils.random;

public class Random {
   public static int nextInt(int startInclusive, int endExclusive) {
      return endExclusive - startInclusive <= 0 ? startInclusive : startInclusive + (new java.util.Random()).nextInt(endExclusive - startInclusive);
   }

   public static double nextDouble(double startInclusive, double endInclusive) {
      return startInclusive != endInclusive && !(endInclusive - startInclusive <= 0.0) ? startInclusive + (endInclusive - startInclusive) * Math.random() : startInclusive;
   }

   public static float nextFloat(float startInclusive, float endInclusive) {
      return startInclusive != endInclusive && !(endInclusive - startInclusive <= 0.0F) ? (float)((double)startInclusive + (double)(endInclusive - startInclusive) * Math.random()) : startInclusive;
   }

   public static String randomNumber(int length) {
      return random(length, "123456789");
   }

   public static String randomString(int length) {
      return random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
   }

   public static String random(int length, String chars) {
      return random(length, chars.toCharArray());
   }

   public static String random(int length, char[] chars) {
      StringBuilder stringBuilder = new StringBuilder();

      for(int i = 0; i < length; ++i) {
         stringBuilder.append(chars[(new java.util.Random()).nextInt(chars.length)]);
      }

      return stringBuilder.toString();
   }
}
