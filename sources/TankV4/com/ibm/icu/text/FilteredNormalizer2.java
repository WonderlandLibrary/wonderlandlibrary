package com.ibm.icu.text;

import java.io.IOException;

public class FilteredNormalizer2 extends Normalizer2 {
   private Normalizer2 norm2;
   private UnicodeSet set;

   public FilteredNormalizer2(Normalizer2 var1, UnicodeSet var2) {
      this.norm2 = var1;
      this.set = var2;
   }

   public StringBuilder normalize(CharSequence var1, StringBuilder var2) {
      if (var2 == var1) {
         throw new IllegalArgumentException();
      } else {
         var2.setLength(0);
         this.normalize(var1, var2, UnicodeSet.SpanCondition.SIMPLE);
         return var2;
      }
   }

   public Appendable normalize(CharSequence var1, Appendable var2) {
      if (var2 == var1) {
         throw new IllegalArgumentException();
      } else {
         return this.normalize(var1, var2, UnicodeSet.SpanCondition.SIMPLE);
      }
   }

   public StringBuilder normalizeSecondAndAppend(StringBuilder var1, CharSequence var2) {
      return this.normalizeSecondAndAppend(var1, var2, true);
   }

   public StringBuilder append(StringBuilder var1, CharSequence var2) {
      return this.normalizeSecondAndAppend(var1, var2, false);
   }

   public String getDecomposition(int var1) {
      return this.set.contains(var1) ? this.norm2.getDecomposition(var1) : null;
   }

   public String getRawDecomposition(int var1) {
      return this.set.contains(var1) ? this.norm2.getRawDecomposition(var1) : null;
   }

   public int composePair(int var1, int var2) {
      return this.set.contains(var1) && this.set.contains(var2) ? this.norm2.composePair(var1, var2) : -1;
   }

   public int getCombiningClass(int var1) {
      return this.set.contains(var1) ? this.norm2.getCombiningClass(var1) : 0;
   }

   public boolean isNormalized(CharSequence var1) {
      UnicodeSet.SpanCondition var2 = UnicodeSet.SpanCondition.SIMPLE;

      int var4;
      for(int var3 = 0; var3 < var1.length(); var3 = var4) {
         var4 = this.set.span(var1, var3, var2);
         if (var2 == UnicodeSet.SpanCondition.NOT_CONTAINED) {
            var2 = UnicodeSet.SpanCondition.SIMPLE;
         } else {
            if (!this.norm2.isNormalized(var1.subSequence(var3, var4))) {
               return false;
            }

            var2 = UnicodeSet.SpanCondition.NOT_CONTAINED;
         }
      }

      return true;
   }

   public Normalizer.QuickCheckResult quickCheck(CharSequence var1) {
      Normalizer.QuickCheckResult var2 = Normalizer.YES;
      UnicodeSet.SpanCondition var3 = UnicodeSet.SpanCondition.SIMPLE;

      int var5;
      for(int var4 = 0; var4 < var1.length(); var4 = var5) {
         var5 = this.set.span(var1, var4, var3);
         if (var3 == UnicodeSet.SpanCondition.NOT_CONTAINED) {
            var3 = UnicodeSet.SpanCondition.SIMPLE;
         } else {
            Normalizer.QuickCheckResult var6 = this.norm2.quickCheck(var1.subSequence(var4, var5));
            if (var6 == Normalizer.NO) {
               return var6;
            }

            if (var6 == Normalizer.MAYBE) {
               var2 = var6;
            }

            var3 = UnicodeSet.SpanCondition.NOT_CONTAINED;
         }
      }

      return var2;
   }

   public int spanQuickCheckYes(CharSequence var1) {
      UnicodeSet.SpanCondition var2 = UnicodeSet.SpanCondition.SIMPLE;

      int var4;
      for(int var3 = 0; var3 < var1.length(); var3 = var4) {
         var4 = this.set.span(var1, var3, var2);
         if (var2 == UnicodeSet.SpanCondition.NOT_CONTAINED) {
            var2 = UnicodeSet.SpanCondition.SIMPLE;
         } else {
            int var5 = var3 + this.norm2.spanQuickCheckYes(var1.subSequence(var3, var4));
            if (var5 < var4) {
               return var5;
            }

            var2 = UnicodeSet.SpanCondition.NOT_CONTAINED;
         }
      }

      return var1.length();
   }

   public boolean hasBoundaryBefore(int var1) {
      return !this.set.contains(var1) || this.norm2.hasBoundaryBefore(var1);
   }

   public boolean hasBoundaryAfter(int var1) {
      return !this.set.contains(var1) || this.norm2.hasBoundaryAfter(var1);
   }

   public boolean isInert(int var1) {
      return !this.set.contains(var1) || this.norm2.isInert(var1);
   }

   private Appendable normalize(CharSequence var1, Appendable var2, UnicodeSet.SpanCondition var3) {
      StringBuilder var4 = new StringBuilder();

      try {
         int var6;
         for(int var5 = 0; var5 < var1.length(); var5 = var6) {
            var6 = this.set.span(var1, var5, var3);
            int var7 = var6 - var5;
            if (var3 == UnicodeSet.SpanCondition.NOT_CONTAINED) {
               if (var7 != 0) {
                  var2.append(var1, var5, var6);
               }

               var3 = UnicodeSet.SpanCondition.SIMPLE;
            } else {
               if (var7 != 0) {
                  var2.append(this.norm2.normalize(var1.subSequence(var5, var6), var4));
               }

               var3 = UnicodeSet.SpanCondition.NOT_CONTAINED;
            }
         }

         return var2;
      } catch (IOException var8) {
         throw new RuntimeException(var8);
      }
   }

   private StringBuilder normalizeSecondAndAppend(StringBuilder var1, CharSequence var2, boolean var3) {
      if (var1 == var2) {
         throw new IllegalArgumentException();
      } else if (var1.length() == 0) {
         return var3 ? this.normalize(var2, var1) : var1.append(var2);
      } else {
         int var4 = this.set.span(var2, 0, UnicodeSet.SpanCondition.SIMPLE);
         CharSequence var5;
         if (var4 != 0) {
            var5 = var2.subSequence(0, var4);
            int var6 = this.set.spanBack(var1, Integer.MAX_VALUE, UnicodeSet.SpanCondition.SIMPLE);
            if (var6 == 0) {
               if (var3) {
                  this.norm2.normalizeSecondAndAppend(var1, var5);
               } else {
                  this.norm2.append(var1, var5);
               }
            } else {
               StringBuilder var7 = new StringBuilder(var1.subSequence(var6, var1.length()));
               if (var3) {
                  this.norm2.normalizeSecondAndAppend(var7, var5);
               } else {
                  this.norm2.append(var7, var5);
               }

               var1.delete(var6, Integer.MAX_VALUE).append(var7);
            }
         }

         if (var4 < var2.length()) {
            var5 = var2.subSequence(var4, var2.length());
            if (var3) {
               this.normalize(var5, var1, UnicodeSet.SpanCondition.NOT_CONTAINED);
            } else {
               var1.append(var5);
            }
         }

         return var1;
      }
   }
}
