package optifine;

public class IntegerCache {
   private static final int CACHE_SIZE = 4096;
   private static final Integer[] cache = makeCache(4096);

   private static Integer[] makeCache(int var0) {
      Integer[] var1 = new Integer[var0];

      for(int var2 = 0; var2 < var0; ++var2) {
         var1[var2] = new Integer(var2);
      }

      return var1;
   }

   public static Integer valueOf(int var0) {
      return var0 >= 0 && var0 < 4096 ? cache[var0] : new Integer(var0);
   }
}
