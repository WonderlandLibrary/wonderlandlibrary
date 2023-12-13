package com.sun.webkit.network;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DateParser {
   private static final Logger logger = Logger.getLogger(DateParser.class.getName());
   private static final Pattern DELIMITER_PATTERN = Pattern.compile("[\\x09\\x20-\\x2F\\x3B-\\x40\\x5B-\\x60\\x7B-\\x7E]+");
   private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})(?:[^\\d].*)*");
   private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})(?:[^\\d].*)*");
   private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})(?:[^\\d].*)*");
   private static final Map MONTH_MAP;

   private DateParser() {
      throw new AssertionError();
   }

   static long parse(String var0) throws ParseException {
      logger.log(Level.FINEST, "date: [{0}]", var0);
      Time var1 = null;
      Integer var2 = null;
      Integer var3 = null;
      Integer var4 = null;
      String[] var5 = DELIMITER_PATTERN.split(var0, 0);
      String[] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         if (var9.length() != 0) {
            Time var10;
            if (var1 == null && (var10 = parseTime(var9)) != null) {
               var1 = var10;
            } else {
               Integer var11;
               if (var2 == null && (var11 = parseDayOfMonth(var9)) != null) {
                  var2 = var11;
               } else {
                  Integer var12;
                  if (var3 == null && (var12 = parseMonth(var9)) != null) {
                     var3 = var12;
                  } else {
                     Integer var13;
                     if (var4 == null && (var13 = parseYear(var9)) != null) {
                        var4 = var13;
                     }
                  }
               }
            }
         }
      }

      if (var4 != null) {
         if (var4 >= 70 && var4 <= 99) {
            var4 = var4 + 1900;
         } else if (var4 >= 0 && var4 <= 69) {
            var4 = var4 + 2000;
         }
      }

      if (var1 != null && var2 != null && var3 != null && var4 != null && var2 >= 1 && var2 <= 31 && var4 >= 1601 && var1.hour <= 23 && var1.minute <= 59 && var1.second <= 59) {
         Calendar var15 = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
         var15.setLenient(false);
         var15.clear();
         var15.set(var4, var3, var2, var1.hour, var1.minute, var1.second);

         try {
            long var16 = var15.getTimeInMillis();
            if (logger.isLoggable(Level.FINEST)) {
               logger.log(Level.FINEST, "result: [{0}]", (new Date(var16)).toString());
            }

            return var16;
         } catch (Exception var14) {
            ParseException var17 = new ParseException("Error parsing date", 0);
            var17.initCause(var14);
            throw var17;
         }
      } else {
         throw new ParseException("Error parsing date", 0);
      }
   }

   private static Time parseTime(String var0) {
      Matcher var1 = TIME_PATTERN.matcher(var0);
      return var1.matches() ? new Time(Integer.parseInt(var1.group(1)), Integer.parseInt(var1.group(2)), Integer.parseInt(var1.group(3))) : null;
   }

   private static Integer parseDayOfMonth(String var0) {
      Matcher var1 = DAY_OF_MONTH_PATTERN.matcher(var0);
      return var1.matches() ? Integer.parseInt(var1.group(1)) : null;
   }

   private static Integer parseMonth(String var0) {
      return var0.length() >= 3 ? (Integer)MONTH_MAP.get(var0.substring(0, 3).toLowerCase()) : null;
   }

   private static Integer parseYear(String var0) {
      Matcher var1 = YEAR_PATTERN.matcher(var0);
      return var1.matches() ? Integer.parseInt(var1.group(1)) : null;
   }

   static {
      HashMap var0 = new HashMap(12);
      var0.put("jan", 0);
      var0.put("feb", 1);
      var0.put("mar", 2);
      var0.put("apr", 3);
      var0.put("may", 4);
      var0.put("jun", 5);
      var0.put("jul", 6);
      var0.put("aug", 7);
      var0.put("sep", 8);
      var0.put("oct", 9);
      var0.put("nov", 10);
      var0.put("dec", 11);
      MONTH_MAP = Collections.unmodifiableMap(var0);
   }

   private static final class Time {
      private final int hour;
      private final int minute;
      private final int second;

      private Time(int var1, int var2, int var3) {
         this.hour = var1;
         this.minute = var2;
         this.second = var3;
      }

      // $FF: synthetic method
      Time(int var1, int var2, int var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
