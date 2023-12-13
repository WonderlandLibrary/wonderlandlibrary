package optifine;

public class MathUtils {
   public static int getAverage(int[] p_getAverage_0_) {
      if (p_getAverage_0_.length <= 0) {
         return 0;
      } else {
         int i = 0;

         int l;
         for(l = 0; l < p_getAverage_0_.length; ++l) {
            int k = p_getAverage_0_[l];
            i += k;
         }

         l = i / p_getAverage_0_.length;
         return l;
      }
   }
}
