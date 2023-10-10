package org.apache.http.client.utils;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class DateUtils {
   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
   public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
   public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
   private static final String[] DEFAULT_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy"};
   private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

   public static Date parseDate(String var0) {
      return parseDate(var0, (String[])null, (Date)null);
   }

   public static Date parseDate(String var0, String[] var1) {
      return parseDate(var0, var1, (Date)null);
   }

   public static Date parseDate(String var0, String[] var1, Date var2) {
      Args.notNull(var0, "Date value");
      String[] var3 = var1 != null ? var1 : DEFAULT_PATTERNS;
      Date var4 = var2 != null ? var2 : DEFAULT_TWO_DIGIT_YEAR_START;
      String var5 = var0;
      if (var0.length() > 1 && var0.startsWith("'") && var0.endsWith("'")) {
         var5 = var0.substring(1, var0.length() - 1);
      }

      String[] var6 = var3;
      int var7 = var3.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String var9 = var6[var8];
         SimpleDateFormat var10 = DateUtils.DateFormatHolder.formatFor(var9);
         var10.set2DigitYearStart(var4);
         ParsePosition var11 = new ParsePosition(0);
         Date var12 = var10.parse(var5, var11);
         if (var11.getIndex() != 0) {
            return var12;
         }
      }

      return null;
   }

   public static String formatDate(Date var0) {
      return formatDate(var0, "EEE, dd MMM yyyy HH:mm:ss zzz");
   }

   public static String formatDate(Date var0, String var1) {
      Args.notNull(var0, "Date");
      Args.notNull(var1, "Pattern");
      SimpleDateFormat var2 = DateUtils.DateFormatHolder.formatFor(var1);
      return var2.format(var0);
   }

   public static void clearThreadLocal() {
      DateUtils.DateFormatHolder.clearThreadLocal();
   }

   private DateUtils() {
   }

   static {
      Calendar var0 = Calendar.getInstance();
      var0.setTimeZone(GMT);
      var0.set(2000, 0, 1, 0, 0, 0);
      var0.set(14, 0);
      DEFAULT_TWO_DIGIT_YEAR_START = var0.getTime();
   }

   static final class DateFormatHolder {
      private static final ThreadLocal THREADLOCAL_FORMATS = new ThreadLocal() {
         protected SoftReference initialValue() {
            return new SoftReference(new HashMap());
         }

         protected Object initialValue() {
            return this.initialValue();
         }
      };

      public static SimpleDateFormat formatFor(String var0) {
         SoftReference var1 = (SoftReference)THREADLOCAL_FORMATS.get();
         Object var2 = (Map)var1.get();
         if (var2 == null) {
            var2 = new HashMap();
            THREADLOCAL_FORMATS.set(new SoftReference(var2));
         }

         SimpleDateFormat var3 = (SimpleDateFormat)((Map)var2).get(var0);
         if (var3 == null) {
            var3 = new SimpleDateFormat(var0, Locale.US);
            var3.setTimeZone(TimeZone.getTimeZone("GMT"));
            ((Map)var2).put(var0, var3);
         }

         return var3;
      }

      public static void clearThreadLocal() {
         THREADLOCAL_FORMATS.remove();
      }
   }
}
