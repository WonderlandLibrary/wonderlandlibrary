package optifine;

public class MathUtils {
   public static int getAverage(int[] var0) {
      if (var0.length <= 0) {
         return 0;
      } else {
         int var1 = 0;

         int var2;
         for(var2 = 0; var2 < var0.length; ++var2) {
            int var3 = var0[var2];
            var1 += var3;
         }

         var2 = var1 / var0.length;
         return var2;
      }
   }
}
