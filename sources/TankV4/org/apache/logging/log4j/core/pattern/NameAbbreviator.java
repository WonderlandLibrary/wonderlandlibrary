package org.apache.logging.log4j.core.pattern;

import java.util.ArrayList;
import java.util.List;

public abstract class NameAbbreviator {
   private static final NameAbbreviator DEFAULT = new NameAbbreviator.NOPAbbreviator();

   public static NameAbbreviator getAbbreviator(String var0) {
      if (var0.length() <= 0) {
         return DEFAULT;
      } else {
         String var1 = var0.trim();
         if (var1.isEmpty()) {
            return DEFAULT;
         } else {
            int var2;
            for(var2 = 0; var2 < var1.length() && var1.charAt(var2) >= '0' && var1.charAt(var2) <= '9'; ++var2) {
            }

            if (var2 == var1.length()) {
               return new NameAbbreviator.MaxElementAbbreviator(Integer.parseInt(var1));
            } else {
               ArrayList var3 = new ArrayList(5);

               for(int var6 = 0; var6 < var1.length() && var6 >= 0; ++var6) {
                  int var7 = var6;
                  int var5;
                  if (var1.charAt(var6) == '*') {
                     var5 = Integer.MAX_VALUE;
                     var7 = var6 + 1;
                  } else if (var1.charAt(var6) >= '0' && var1.charAt(var6) <= '9') {
                     var5 = var1.charAt(var6) - 48;
                     var7 = var6 + 1;
                  } else {
                     var5 = 0;
                  }

                  char var4 = 0;
                  if (var7 < var1.length()) {
                     var4 = var1.charAt(var7);
                     if (var4 == '.') {
                        var4 = 0;
                     }
                  }

                  var3.add(new NameAbbreviator.PatternAbbreviatorFragment(var5, var4));
                  var6 = var1.indexOf(46, var6);
                  if (var6 == -1) {
                     break;
                  }
               }

               return new NameAbbreviator.PatternAbbreviator(var3);
            }
         }
      }
   }

   public static NameAbbreviator getDefaultAbbreviator() {
      return DEFAULT;
   }

   public abstract String abbreviate(String var1);

   private static class PatternAbbreviator extends NameAbbreviator {
      private final NameAbbreviator.PatternAbbreviatorFragment[] fragments;

      public PatternAbbreviator(List var1) {
         if (var1.size() == 0) {
            throw new IllegalArgumentException("fragments must have at least one element");
         } else {
            this.fragments = new NameAbbreviator.PatternAbbreviatorFragment[var1.size()];
            var1.toArray(this.fragments);
         }
      }

      public String abbreviate(String var1) {
         int var2 = 0;
         StringBuilder var3 = new StringBuilder(var1);

         for(int var4 = 0; var4 < this.fragments.length - 1 && var2 < var1.length(); ++var4) {
            var2 = this.fragments[var4].abbreviate(var3, var2);
         }

         for(NameAbbreviator.PatternAbbreviatorFragment var5 = this.fragments[this.fragments.length - 1]; var2 < var1.length() && var2 >= 0; var2 = var5.abbreviate(var3, var2)) {
         }

         return var3.toString();
      }
   }

   private static class PatternAbbreviatorFragment {
      private final int charCount;
      private final char ellipsis;

      public PatternAbbreviatorFragment(int var1, char var2) {
         this.charCount = var1;
         this.ellipsis = var2;
      }

      public int abbreviate(StringBuilder var1, int var2) {
         int var3 = var1.toString().indexOf(46, var2);
         if (var3 != -1) {
            if (var3 - var2 > this.charCount) {
               var1.delete(var2 + this.charCount, var3);
               var3 = var2 + this.charCount;
               if (this.ellipsis != 0) {
                  var1.insert(var3, this.ellipsis);
                  ++var3;
               }
            }

            ++var3;
         }

         return var3;
      }
   }

   private static class MaxElementAbbreviator extends NameAbbreviator {
      private final int count;

      public MaxElementAbbreviator(int var1) {
         this.count = var1 < 1 ? 1 : var1;
      }

      public String abbreviate(String var1) {
         int var2 = var1.length() - 1;

         for(int var3 = this.count; var3 > 0; --var3) {
            var2 = var1.lastIndexOf(46, var2 - 1);
            if (var2 == -1) {
               return var1;
            }
         }

         return var1.substring(var2 + 1);
      }
   }

   private static class NOPAbbreviator extends NameAbbreviator {
      public NOPAbbreviator() {
      }

      public String abbreviate(String var1) {
         return var1;
      }
   }
}
