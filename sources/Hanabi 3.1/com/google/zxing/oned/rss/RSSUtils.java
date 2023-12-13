package com.google.zxing.oned.rss;

public final class RSSUtils {
   private RSSUtils() {
   }

   static int[] getRSSwidths(int val, int n, int elements, int maxWidth, boolean noNarrow) {
      int[] widths = new int[elements];
      int narrowMask = 0;

      int bar;
      for(bar = 0; bar < elements - 1; ++bar) {
         narrowMask |= 1 << bar;
         int elmWidth = 1;

         while(true) {
            int subVal = combins(n - elmWidth - 1, elements - bar - 2);
            if (noNarrow && narrowMask == 0 && n - elmWidth - (elements - bar - 1) >= elements - bar - 1) {
               subVal -= combins(n - elmWidth - (elements - bar), elements - bar - 2);
            }

            if (elements - bar - 1 <= 1) {
               if (n - elmWidth > maxWidth) {
                  --subVal;
               }
            } else {
               int lessVal = 0;

               for(int mxwElement = n - elmWidth - (elements - bar - 2); mxwElement > maxWidth; --mxwElement) {
                  lessVal += combins(n - elmWidth - mxwElement - 1, elements - bar - 3);
               }

               subVal -= lessVal * (elements - 1 - bar);
            }

            val -= subVal;
            if (val < 0) {
               val += subVal;
               n -= elmWidth;
               widths[bar] = elmWidth;
               break;
            }

            ++elmWidth;
            narrowMask &= ~(1 << bar);
         }
      }

      widths[bar] = n;
      return widths;
   }

   public static int getRSSvalue(int[] widths, int maxWidth, boolean noNarrow) {
      int elements = widths.length;
      int n = 0;
      int[] arr$ = widths;
      int narrowMask = widths.length;

      int bar;
      int elmWidth;
      for(bar = 0; bar < narrowMask; ++bar) {
         elmWidth = arr$[bar];
         n += elmWidth;
      }

      int val = 0;
      narrowMask = 0;

      for(bar = 0; bar < elements - 1; ++bar) {
         elmWidth = 1;

         for(narrowMask |= 1 << bar; elmWidth < widths[bar]; narrowMask &= ~(1 << bar)) {
            int subVal = combins(n - elmWidth - 1, elements - bar - 2);
            if (noNarrow && narrowMask == 0 && n - elmWidth - (elements - bar - 1) >= elements - bar - 1) {
               subVal -= combins(n - elmWidth - (elements - bar), elements - bar - 2);
            }

            if (elements - bar - 1 <= 1) {
               if (n - elmWidth > maxWidth) {
                  --subVal;
               }
            } else {
               int lessVal = 0;

               for(int mxwElement = n - elmWidth - (elements - bar - 2); mxwElement > maxWidth; --mxwElement) {
                  lessVal += combins(n - elmWidth - mxwElement - 1, elements - bar - 3);
               }

               subVal -= lessVal * (elements - 1 - bar);
            }

            val += subVal;
            ++elmWidth;
         }

         n -= elmWidth;
      }

      return val;
   }

   private static int combins(int n, int r) {
      int maxDenom;
      int minDenom;
      if (n - r > r) {
         minDenom = r;
         maxDenom = n - r;
      } else {
         minDenom = n - r;
         maxDenom = r;
      }

      int val = 1;
      int j = 1;

      for(int i = n; i > maxDenom; --i) {
         val *= i;
         if (j <= minDenom) {
            val /= j;
            ++j;
         }
      }

      while(j <= minDenom) {
         val /= j;
         ++j;
      }

      return val;
   }

   static int[] elements(int[] eDist, int N, int K) {
      int[] widths = new int[eDist.length + 2];
      int twoK = K << 1;
      widths[0] = 1;
      int minEven = 10;
      int barSum = 1;

      int i;
      for(i = 1; i < twoK - 2; i += 2) {
         widths[i] = eDist[i - 1] - widths[i - 1];
         widths[i + 1] = eDist[i] - widths[i];
         barSum += widths[i] + widths[i + 1];
         if (widths[i] < minEven) {
            minEven = widths[i];
         }
      }

      widths[twoK - 1] = N - barSum;
      if (widths[twoK - 1] < minEven) {
         minEven = widths[twoK - 1];
      }

      if (minEven > 1) {
         for(i = 0; i < twoK; i += 2) {
            widths[i] += minEven - 1;
            widths[i + 1] -= minEven - 1;
         }
      }

      return widths;
   }
}
