package org.apache.commons.lang3.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;

public class DurationFormatUtils {
   public static final String ISO_EXTENDED_FORMAT_PATTERN = "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'";
   static final Object y = "y";
   static final Object M = "M";
   static final Object d = "d";
   static final Object H = "H";
   static final Object m = "m";
   static final Object s = "s";
   static final Object S = "S";

   public static String formatDurationHMS(long var0) {
      return formatDuration(var0, "H:mm:ss.SSS");
   }

   public static String formatDurationISO(long var0) {
      return formatDuration(var0, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false);
   }

   public static String formatDuration(long var0, String var2) {
      return formatDuration(var0, var2, true);
   }

   public static String formatDuration(long var0, String var2, boolean var3) {
      DurationFormatUtils.Token[] var4 = lexx(var2);
      long var5 = 0L;
      long var7 = 0L;
      long var9 = 0L;
      long var11 = 0L;
      long var13 = var0;
      if (DurationFormatUtils.Token.containsTokenWithValue(var4, d)) {
         var5 = var0 / 86400000L;
         var13 = var0 - var5 * 86400000L;
      }

      if (DurationFormatUtils.Token.containsTokenWithValue(var4, H)) {
         var7 = var13 / 3600000L;
         var13 -= var7 * 3600000L;
      }

      if (DurationFormatUtils.Token.containsTokenWithValue(var4, m)) {
         var9 = var13 / 60000L;
         var13 -= var9 * 60000L;
      }

      if (DurationFormatUtils.Token.containsTokenWithValue(var4, s)) {
         var11 = var13 / 1000L;
         var13 -= var11 * 1000L;
      }

      return format(var4, 0L, 0L, var5, var7, var9, var11, var13, var3);
   }

   public static String formatDurationWords(long var0, boolean var2, boolean var3) {
      String var4 = formatDuration(var0, "d' days 'H' hours 'm' minutes 's' seconds'");
      String var5;
      if (var2) {
         var4 = " " + var4;
         var5 = StringUtils.replaceOnce(var4, " 0 days", "");
         if (var5.length() != var4.length()) {
            var4 = var5;
            var5 = StringUtils.replaceOnce(var5, " 0 hours", "");
            if (var5.length() != var4.length()) {
               var5 = StringUtils.replaceOnce(var5, " 0 minutes", "");
               var4 = var5;
               if (var5.length() != var5.length()) {
                  var4 = StringUtils.replaceOnce(var5, " 0 seconds", "");
               }
            }
         }

         if (var4.length() != 0) {
            var4 = var4.substring(1);
         }
      }

      if (var3) {
         var5 = StringUtils.replaceOnce(var4, " 0 seconds", "");
         if (var5.length() != var4.length()) {
            var4 = var5;
            var5 = StringUtils.replaceOnce(var5, " 0 minutes", "");
            if (var5.length() != var4.length()) {
               var4 = var5;
               var5 = StringUtils.replaceOnce(var5, " 0 hours", "");
               if (var5.length() != var4.length()) {
                  var4 = StringUtils.replaceOnce(var5, " 0 days", "");
               }
            }
         }
      }

      var4 = " " + var4;
      var4 = StringUtils.replaceOnce(var4, " 1 seconds", " 1 second");
      var4 = StringUtils.replaceOnce(var4, " 1 minutes", " 1 minute");
      var4 = StringUtils.replaceOnce(var4, " 1 hours", " 1 hour");
      var4 = StringUtils.replaceOnce(var4, " 1 days", " 1 day");
      return var4.trim();
   }

   public static String formatPeriodISO(long var0, long var2) {
      return formatPeriod(var0, var2, "'P'yyyy'Y'M'M'd'DT'H'H'm'M's.S'S'", false, TimeZone.getDefault());
   }

   public static String formatPeriod(long var0, long var2, String var4) {
      return formatPeriod(var0, var2, var4, true, TimeZone.getDefault());
   }

   public static String formatPeriod(long var0, long var2, String var4, boolean var5, TimeZone var6) {
      DurationFormatUtils.Token[] var7 = lexx(var4);
      Calendar var8 = Calendar.getInstance(var6);
      var8.setTime(new Date(var0));
      Calendar var9 = Calendar.getInstance(var6);
      var9.setTime(new Date(var2));
      int var10 = var9.get(14) - var8.get(14);
      int var11 = var9.get(13) - var8.get(13);
      int var12 = var9.get(12) - var8.get(12);
      int var13 = var9.get(11) - var8.get(11);
      int var14 = var9.get(5) - var8.get(5);
      int var15 = var9.get(2) - var8.get(2);

      int var16;
      for(var16 = var9.get(1) - var8.get(1); var10 < 0; --var11) {
         var10 += 1000;
      }

      while(var11 < 0) {
         var11 += 60;
         --var12;
      }

      while(var12 < 0) {
         var12 += 60;
         --var13;
      }

      while(var13 < 0) {
         var13 += 24;
         --var14;
      }

      if (!DurationFormatUtils.Token.containsTokenWithValue(var7, M)) {
         if (!DurationFormatUtils.Token.containsTokenWithValue(var7, y)) {
            int var17 = var9.get(1);
            if (var15 < 0) {
               --var17;
            }

            while(var8.get(1) != var17) {
               var14 += var8.getActualMaximum(6) - var8.get(6);
               if (var8 instanceof GregorianCalendar && var8.get(2) == 1 && var8.get(5) == 29) {
                  ++var14;
               }

               var8.add(1, 1);
               var14 += var8.get(6);
            }

            var16 = 0;
         }

         while(var8.get(2) != var9.get(2)) {
            var14 += var8.getActualMaximum(5);
            var8.add(2, 1);
         }

         var15 = 0;

         while(var14 < 0) {
            var14 += var8.getActualMaximum(5);
            --var15;
            var8.add(2, 1);
         }
      } else {
         while(var14 < 0) {
            var14 += var8.getActualMaximum(5);
            --var15;
            var8.add(2, 1);
         }

         while(var15 < 0) {
            var15 += 12;
            --var16;
         }

         if (!DurationFormatUtils.Token.containsTokenWithValue(var7, y) && var16 != 0) {
            while(var16 != 0) {
               var15 += 12 * var16;
               var16 = 0;
            }
         }
      }

      if (!DurationFormatUtils.Token.containsTokenWithValue(var7, d)) {
         var13 += 24 * var14;
         var14 = 0;
      }

      if (!DurationFormatUtils.Token.containsTokenWithValue(var7, H)) {
         var12 += 60 * var13;
         var13 = 0;
      }

      if (!DurationFormatUtils.Token.containsTokenWithValue(var7, m)) {
         var11 += 60 * var12;
         var12 = 0;
      }

      if (!DurationFormatUtils.Token.containsTokenWithValue(var7, s)) {
         var10 += 1000 * var11;
         var11 = 0;
      }

      return format(var7, (long)var16, (long)var15, (long)var14, (long)var13, (long)var12, (long)var11, (long)var10, var5);
   }

   static String format(DurationFormatUtils.Token[] var0, long var1, long var3, long var5, long var7, long var9, long var11, long var13, boolean var15) {
      StringBuilder var16 = new StringBuilder();
      boolean var17 = false;
      int var18 = var0.length;

      for(int var19 = 0; var19 < var18; ++var19) {
         DurationFormatUtils.Token var20 = var0[var19];
         Object var21 = var20.getValue();
         int var22 = var20.getCount();
         if (var21 instanceof StringBuilder) {
            var16.append(var21.toString());
         } else if (var21 == y) {
            var16.append(paddedValue(var1, var15, var22));
            var17 = false;
         } else if (var21 == M) {
            var16.append(paddedValue(var3, var15, var22));
            var17 = false;
         } else if (var21 == d) {
            var16.append(paddedValue(var5, var15, var22));
            var17 = false;
         } else if (var21 == H) {
            var16.append(paddedValue(var7, var15, var22));
            var17 = false;
         } else if (var21 == m) {
            var16.append(paddedValue(var9, var15, var22));
            var17 = false;
         } else if (var21 == s) {
            var16.append(paddedValue(var11, var15, var22));
            var17 = true;
         } else if (var21 == S) {
            if (var17) {
               int var23 = var15 ? Math.max(3, var22) : 3;
               var16.append(paddedValue(var13, true, var23));
            } else {
               var16.append(paddedValue(var13, var15, var22));
            }

            var17 = false;
         }
      }

      return var16.toString();
   }

   private static String paddedValue(long var0, boolean var2, int var3) {
      String var4 = Long.toString(var0);
      return var2 ? StringUtils.leftPad(var4, var3, '0') : var4;
   }

   static DurationFormatUtils.Token[] lexx(String var0) {
      ArrayList var1 = new ArrayList(var0.length());
      boolean var2 = false;
      StringBuilder var3 = null;
      DurationFormatUtils.Token var4 = null;

      for(int var5 = 0; var5 < var0.length(); ++var5) {
         char var6 = var0.charAt(var5);
         if (var2 && var6 != '\'') {
            var3.append(var6);
         } else {
            Object var7 = null;
            switch(var6) {
            case '\'':
               if (var2) {
                  var3 = null;
                  var2 = false;
               } else {
                  var3 = new StringBuilder();
                  var1.add(new DurationFormatUtils.Token(var3));
                  var2 = true;
               }
               break;
            case 'H':
               var7 = H;
               break;
            case 'M':
               var7 = M;
               break;
            case 'S':
               var7 = S;
               break;
            case 'd':
               var7 = d;
               break;
            case 'm':
               var7 = m;
               break;
            case 's':
               var7 = s;
               break;
            case 'y':
               var7 = y;
               break;
            default:
               if (var3 == null) {
                  var3 = new StringBuilder();
                  var1.add(new DurationFormatUtils.Token(var3));
               }

               var3.append(var6);
            }

            if (var7 != null) {
               if (var4 != null && var4.getValue() == var7) {
                  var4.increment();
               } else {
                  DurationFormatUtils.Token var8 = new DurationFormatUtils.Token(var7);
                  var1.add(var8);
                  var4 = var8;
               }

               var3 = null;
            }
         }
      }

      if (var2) {
         throw new IllegalArgumentException("Unmatched quote in format: " + var0);
      } else {
         return (DurationFormatUtils.Token[])var1.toArray(new DurationFormatUtils.Token[var1.size()]);
      }
   }

   static class Token {
      private final Object value;
      private int count;

      static boolean containsTokenWithValue(DurationFormatUtils.Token[] var0, Object var1) {
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var0[var3].getValue() == var1) {
               return true;
            }
         }

         return false;
      }

      Token(Object var1) {
         this.value = var1;
         this.count = 1;
      }

      Token(Object var1, int var2) {
         this.value = var1;
         this.count = var2;
      }

      void increment() {
         ++this.count;
      }

      int getCount() {
         return this.count;
      }

      Object getValue() {
         return this.value;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof DurationFormatUtils.Token) {
            DurationFormatUtils.Token var2 = (DurationFormatUtils.Token)var1;
            if (this.value.getClass() != var2.value.getClass()) {
               return false;
            } else if (this.count != var2.count) {
               return false;
            } else if (this.value instanceof StringBuilder) {
               return this.value.toString().equals(var2.value.toString());
            } else if (this.value instanceof Number) {
               return this.value.equals(var2.value);
            } else {
               return this.value == var2.value;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.value.hashCode();
      }

      public String toString() {
         return StringUtils.repeat(this.value.toString(), this.count);
      }
   }
}
