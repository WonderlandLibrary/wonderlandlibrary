package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;

class CompactDecimalDataCache {
   private static final String SHORT_STYLE = "short";
   private static final String LONG_STYLE = "long";
   private static final String NUMBER_ELEMENTS = "NumberElements";
   private static final String PATTERN_LONG_PATH = "patternsLong/decimalFormat";
   private static final String PATTERNS_SHORT_PATH = "patternsShort/decimalFormat";
   static final String OTHER = "other";
   static final int MAX_DIGITS = 15;
   private static final String LATIN_NUMBERING_SYSTEM = "latn";
   private final ICUCache cache = new SimpleCache();

   CompactDecimalDataCache.DataBundle get(ULocale var1) {
      CompactDecimalDataCache.DataBundle var2 = (CompactDecimalDataCache.DataBundle)this.cache.get(var1);
      if (var2 == null) {
         var2 = load(var1);
         this.cache.put(var1, var2);
      }

      return var2;
   }

   private static CompactDecimalDataCache.DataBundle load(ULocale var0) {
      NumberingSystem var1 = NumberingSystem.getInstance(var0);
      ICUResourceBundle var2 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
      var2 = var2.getWithFallback("NumberElements");
      String var3 = var1.getName();
      ICUResourceBundle var4 = null;
      ICUResourceBundle var5 = null;
      ICUResourceBundle var6;
      if (!"latn".equals(var3)) {
         var6 = findWithFallback(var2, var3, CompactDecimalDataCache.UResFlags.NOT_ROOT);
         var4 = findWithFallback(var6, "patternsShort/decimalFormat", CompactDecimalDataCache.UResFlags.NOT_ROOT);
         var5 = findWithFallback(var6, "patternsLong/decimalFormat", CompactDecimalDataCache.UResFlags.NOT_ROOT);
      }

      if (var4 == null) {
         var6 = getWithFallback(var2, "latn", CompactDecimalDataCache.UResFlags.ANY);
         var4 = getWithFallback(var6, "patternsShort/decimalFormat", CompactDecimalDataCache.UResFlags.ANY);
         if (var5 == null) {
            var5 = findWithFallback(var6, "patternsLong/decimalFormat", CompactDecimalDataCache.UResFlags.ANY);
            if (var5 != null && var5 == false && var4 == false) {
               var5 = null;
            }
         }
      }

      CompactDecimalDataCache.Data var8 = loadStyle(var4, var0, "short");
      CompactDecimalDataCache.Data var7;
      if (var5 == null) {
         var7 = var8;
      } else {
         var7 = loadStyle(var5, var0, "long");
      }

      return new CompactDecimalDataCache.DataBundle(var8, var7);
   }

   private static ICUResourceBundle findWithFallback(ICUResourceBundle var0, String var1, CompactDecimalDataCache.UResFlags var2) {
      if (var0 == null) {
         return null;
      } else {
         ICUResourceBundle var3 = var0.findWithFallback(var1);
         if (var3 == null) {
            return null;
         } else {
            switch(var2) {
            case NOT_ROOT:
               return var3 == false ? null : var3;
            case ANY:
               return var3;
            default:
               throw new IllegalArgumentException();
            }
         }
      }
   }

   private static ICUResourceBundle getWithFallback(ICUResourceBundle var0, String var1, CompactDecimalDataCache.UResFlags var2) {
      ICUResourceBundle var3 = findWithFallback(var0, var1, var2);
      if (var3 == null) {
         throw new MissingResourceException("Cannot find " + var1, ICUResourceBundle.class.getName(), var1);
      } else {
         return var3;
      }
   }

   private static CompactDecimalDataCache.Data loadStyle(ICUResourceBundle var0, ULocale var1, String var2) {
      int var3 = var0.getSize();
      CompactDecimalDataCache.Data var4 = new CompactDecimalDataCache.Data(new long[15], new HashMap());

      for(int var5 = 0; var5 < var3; ++var5) {
         populateData(var0.get(var5), var1, var2, var4);
      }

      fillInMissing(var4);
      return var4;
   }

   private static void populateData(UResourceBundle var0, ULocale var1, String var2, CompactDecimalDataCache.Data var3) {
      long var4 = Long.parseLong(var0.getKey());
      int var6 = (int)Math.log10((double)var4);
      if (var6 < 15) {
         int var7 = var0.getSize();
         int var8 = 0;
         boolean var9 = false;

         for(int var10 = 0; var10 < var7; ++var10) {
            UResourceBundle var11 = var0.get(var10);
            String var12 = var11.getKey();
            String var13 = var11.getString();
            if (var12.equals("other")) {
               var9 = true;
            }

            int var14 = populatePrefixSuffix(var12, var6, var13, var1, var2, var3);
            if (var14 != var8) {
               if (var8 != 0) {
                  throw new IllegalArgumentException("Plural variant '" + var12 + "' template '" + var13 + "' for 10^" + var6 + " has wrong number of zeros in " + localeAndStyle(var1, var2));
               }

               var8 = var14;
            }
         }

         if (!var9) {
            throw new IllegalArgumentException("No 'other' plural variant defined for 10^" + var6 + "in " + localeAndStyle(var1, var2));
         } else {
            long var15 = var4;

            for(int var16 = 1; var16 < var8; ++var16) {
               var15 /= 10L;
            }

            var3.divisors[var6] = var15;
         }
      }
   }

   private static int populatePrefixSuffix(String var0, int var1, String var2, ULocale var3, String var4, CompactDecimalDataCache.Data var5) {
      int var6 = var2.indexOf("0");
      int var7 = var2.lastIndexOf("0");
      if (var6 == -1) {
         throw new IllegalArgumentException("Expect at least one zero in template '" + var2 + "' for variant '" + var0 + "' for 10^" + var1 + " in " + localeAndStyle(var3, var4));
      } else {
         String var8 = fixQuotes(var2.substring(0, var6));
         String var9 = fixQuotes(var2.substring(var7 + 1));
         saveUnit(new DecimalFormat.Unit(var8, var9), var0, var1, var5.units);
         if (var8.trim().length() == 0 && var9.trim().length() == 0) {
            return var1 + 1;
         } else {
            int var10;
            for(var10 = var6 + 1; var10 <= var7 && var2.charAt(var10) == '0'; ++var10) {
            }

            return var10 - var6;
         }
      }
   }

   private static String fixQuotes(String var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = var0.length();
      CompactDecimalDataCache.QuoteState var3 = CompactDecimalDataCache.QuoteState.OUTSIDE;

      for(int var4 = 0; var4 < var2; ++var4) {
         char var5 = var0.charAt(var4);
         if (var5 == '\'') {
            if (var3 == CompactDecimalDataCache.QuoteState.INSIDE_EMPTY) {
               var1.append('\'');
            }
         } else {
            var1.append(var5);
         }

         switch(var3) {
         case OUTSIDE:
            var3 = var5 == '\'' ? CompactDecimalDataCache.QuoteState.INSIDE_EMPTY : CompactDecimalDataCache.QuoteState.OUTSIDE;
            break;
         case INSIDE_EMPTY:
         case INSIDE_FULL:
            var3 = var5 == '\'' ? CompactDecimalDataCache.QuoteState.OUTSIDE : CompactDecimalDataCache.QuoteState.INSIDE_FULL;
            break;
         default:
            throw new IllegalStateException();
         }
      }

      return var1.toString();
   }

   private static String localeAndStyle(ULocale var0, String var1) {
      return "locale '" + var0 + "' style '" + var1 + "'";
   }

   private static void fillInMissing(CompactDecimalDataCache.Data var0) {
      long var1 = 1L;

      for(int var3 = 0; var3 < var0.divisors.length; ++var3) {
         if (((DecimalFormat.Unit[])var0.units.get("other"))[var3] == null) {
            var0.divisors[var3] = var1;
            copyFromPreviousIndex(var3, var0.units);
         } else {
            var1 = var0.divisors[var3];
            propagateOtherToMissing(var3, var0.units);
         }
      }

   }

   private static void propagateOtherToMissing(int var0, Map var1) {
      DecimalFormat.Unit var2 = ((DecimalFormat.Unit[])var1.get("other"))[var0];
      Iterator var3 = var1.values().iterator();

      while(var3.hasNext()) {
         DecimalFormat.Unit[] var4 = (DecimalFormat.Unit[])var3.next();
         if (var4[var0] == null) {
            var4[var0] = var2;
         }
      }

   }

   private static void copyFromPreviousIndex(int var0, Map var1) {
      Iterator var2 = var1.values().iterator();

      while(var2.hasNext()) {
         DecimalFormat.Unit[] var3 = (DecimalFormat.Unit[])var2.next();
         if (var0 == 0) {
            var3[var0] = DecimalFormat.NULL_UNIT;
         } else {
            var3[var0] = var3[var0 - 1];
         }
      }

   }

   private static void saveUnit(DecimalFormat.Unit var0, String var1, int var2, Map var3) {
      DecimalFormat.Unit[] var4 = (DecimalFormat.Unit[])var3.get(var1);
      if (var4 == null) {
         var4 = new DecimalFormat.Unit[15];
         var3.put(var1, var4);
      }

      var4[var2] = var0;
   }

   static DecimalFormat.Unit getUnit(Map var0, String var1, int var2) {
      DecimalFormat.Unit[] var3 = (DecimalFormat.Unit[])var0.get(var1);
      if (var3 == null) {
         var3 = (DecimalFormat.Unit[])var0.get("other");
      }

      return var3[var2];
   }

   private static enum UResFlags {
      ANY,
      NOT_ROOT;

      private static final CompactDecimalDataCache.UResFlags[] $VALUES = new CompactDecimalDataCache.UResFlags[]{ANY, NOT_ROOT};
   }

   private static enum QuoteState {
      OUTSIDE,
      INSIDE_EMPTY,
      INSIDE_FULL;

      private static final CompactDecimalDataCache.QuoteState[] $VALUES = new CompactDecimalDataCache.QuoteState[]{OUTSIDE, INSIDE_EMPTY, INSIDE_FULL};
   }

   static class DataBundle {
      CompactDecimalDataCache.Data shortData;
      CompactDecimalDataCache.Data longData;

      DataBundle(CompactDecimalDataCache.Data var1, CompactDecimalDataCache.Data var2) {
         this.shortData = var1;
         this.longData = var2;
      }
   }

   static class Data {
      long[] divisors;
      Map units;

      Data(long[] var1, Map var2) {
         this.divisors = var1;
         this.units = var2;
      }
   }
}
