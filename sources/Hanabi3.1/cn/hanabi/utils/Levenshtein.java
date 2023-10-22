package cn.hanabi.utils;

public class Levenshtein {
   private int compare(String str, String target) {
      int n = str.length();
      int m = target.length();
      if (n == 0) {
         return m;
      } else if (m == 0) {
         return n;
      } else {
         int[][] d = new int[n + 1][m + 1];

         int i;
         for(i = 0; i <= n; d[i][0] = i++) {
         }

         int j;
         for(j = 0; j <= m; d[0][j] = j++) {
         }

         for(i = 1; i <= n; ++i) {
            char ch1 = str.charAt(i - 1);

            for(j = 1; j <= m; ++j) {
               char ch2 = target.charAt(j - 1);
               byte temp;
               if (ch1 == ch2) {
                  temp = 0;
               } else {
                  temp = 1;
               }

               d[i][j] = this.min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
         }

         return d[n][m];
      }
   }

   private int min(int one, int two, int three) {
      return (one = Math.min(one, two)) < three ? one : three;
   }

   public float getSimilarityRatio(String str, String target) {
      return 1.0F - (float)this.compare(str, target) / (float)Math.max(str.length(), target.length());
   }
}
