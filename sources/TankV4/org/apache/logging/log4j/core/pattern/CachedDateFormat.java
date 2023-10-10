package org.apache.logging.log4j.core.pattern;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

final class CachedDateFormat extends DateFormat {
   public static final int NO_MILLISECONDS = -2;
   public static final int UNRECOGNIZED_MILLISECONDS = -1;
   private static final long serialVersionUID = -1253877934598423628L;
   private static final String DIGITS = "0123456789";
   private static final int MAGIC1 = 654;
   private static final String MAGICSTRING1 = "654";
   private static final int MAGIC2 = 987;
   private static final String MAGICSTRING2 = "987";
   private static final String ZERO_STRING = "000";
   private static final int BUF_SIZE = 50;
   private static final int DEFAULT_VALIDITY = 1000;
   private static final int THREE_DIGITS = 100;
   private static final int TWO_DIGITS = 10;
   private static final long SLOTS = 1000L;
   private final DateFormat formatter;
   private int millisecondStart;
   private long slotBegin;
   private final StringBuffer cache = new StringBuffer(50);
   private final int expiration;
   private long previousTime;
   private final Date tmpDate = new Date(0L);

   public CachedDateFormat(DateFormat var1, int var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("dateFormat cannot be null");
      } else if (var2 < 0) {
         throw new IllegalArgumentException("expiration must be non-negative");
      } else {
         this.formatter = var1;
         this.expiration = var2;
         this.millisecondStart = 0;
         this.previousTime = Long.MIN_VALUE;
         this.slotBegin = Long.MIN_VALUE;
      }
   }

   public static int findMillisecondStart(long var0, String var2, DateFormat var3) {
      long var4 = var0 / 1000L * 1000L;
      if (var4 > var0) {
         var4 -= 1000L;
      }

      int var6 = (int)(var0 - var4);
      short var7 = 654;
      String var8 = "654";
      if (var6 == 654) {
         var7 = 987;
         var8 = "987";
      }

      String var9 = var3.format(new Date(var4 + (long)var7));
      if (var9.length() != var2.length()) {
         return -1;
      } else {
         for(int var10 = 0; var10 < var2.length(); ++var10) {
            if (var2.charAt(var10) != var9.charAt(var10)) {
               StringBuffer var11 = new StringBuffer("ABC");
               millisecondFormat(var6, var11, 0);
               String var12 = var3.format(new Date(var4));
               if (var12.length() == var2.length() && var8.regionMatches(0, var9, var10, var8.length()) && var11.toString().regionMatches(0, var2, var10, var8.length()) && "000".regionMatches(0, var12, var10, 3)) {
                  return var10;
               }

               return -1;
            }
         }

         return -2;
      }
   }

   public StringBuffer format(Date var1, StringBuffer var2, FieldPosition var3) {
      this.format(var1.getTime(), var2);
      return var2;
   }

   public StringBuffer format(long var1, StringBuffer var3) {
      if (var1 == this.previousTime) {
         var3.append(this.cache);
         return var3;
      } else if (this.millisecondStart != -1 && var1 < this.slotBegin + (long)this.expiration && var1 >= this.slotBegin && var1 < this.slotBegin + 1000L) {
         if (this.millisecondStart >= 0) {
            millisecondFormat((int)(var1 - this.slotBegin), this.cache, this.millisecondStart);
         }

         this.previousTime = var1;
         var3.append(this.cache);
         return var3;
      } else {
         this.cache.setLength(0);
         this.tmpDate.setTime(var1);
         this.cache.append(this.formatter.format(this.tmpDate));
         var3.append(this.cache);
         this.previousTime = var1;
         this.slotBegin = this.previousTime / 1000L * 1000L;
         if (this.slotBegin > this.previousTime) {
            this.slotBegin -= 1000L;
         }

         if (this.millisecondStart >= 0) {
            this.millisecondStart = findMillisecondStart(var1, this.cache.toString(), this.formatter);
         }

         return var3;
      }
   }

   private static void millisecondFormat(int var0, StringBuffer var1, int var2) {
      var1.setCharAt(var2, "0123456789".charAt(var0 / 100));
      var1.setCharAt(var2 + 1, "0123456789".charAt(var0 / 10 % 10));
      var1.setCharAt(var2 + 2, "0123456789".charAt(var0 % 10));
   }

   public void setTimeZone(TimeZone var1) {
      this.formatter.setTimeZone(var1);
      this.previousTime = Long.MIN_VALUE;
      this.slotBegin = Long.MIN_VALUE;
   }

   public Date parse(String var1, ParsePosition var2) {
      return this.formatter.parse(var1, var2);
   }

   public NumberFormat getNumberFormat() {
      return this.formatter.getNumberFormat();
   }

   public static int getMaximumCacheValidity(String var0) {
      int var1 = var0.indexOf(83);
      return var1 >= 0 && var1 != var0.lastIndexOf("SSS") ? 1 : 1000;
   }
}
