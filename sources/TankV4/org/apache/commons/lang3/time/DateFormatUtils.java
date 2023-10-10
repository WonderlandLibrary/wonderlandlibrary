package org.apache.commons.lang3.time;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatUtils {
   private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
   public static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");
   public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");
   public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
   public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");
   public static final FastDateFormat ISO_TIME_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");
   public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZZ");
   public static final FastDateFormat ISO_TIME_NO_T_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
   public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZZ");
   public static final FastDateFormat SMTP_DATETIME_FORMAT;

   public static String formatUTC(long var0, String var2) {
      return format((Date)(new Date(var0)), var2, UTC_TIME_ZONE, (Locale)null);
   }

   public static String formatUTC(Date var0, String var1) {
      return format((Date)var0, var1, UTC_TIME_ZONE, (Locale)null);
   }

   public static String formatUTC(long var0, String var2, Locale var3) {
      return format(new Date(var0), var2, UTC_TIME_ZONE, var3);
   }

   public static String formatUTC(Date var0, String var1, Locale var2) {
      return format(var0, var1, UTC_TIME_ZONE, var2);
   }

   public static String format(long var0, String var2) {
      return format((Date)(new Date(var0)), var2, (TimeZone)null, (Locale)null);
   }

   public static String format(Date var0, String var1) {
      return format((Date)var0, var1, (TimeZone)null, (Locale)null);
   }

   public static String format(Calendar var0, String var1) {
      return format((Calendar)var0, var1, (TimeZone)null, (Locale)null);
   }

   public static String format(long var0, String var2, TimeZone var3) {
      return format((Date)(new Date(var0)), var2, var3, (Locale)null);
   }

   public static String format(Date var0, String var1, TimeZone var2) {
      return format((Date)var0, var1, var2, (Locale)null);
   }

   public static String format(Calendar var0, String var1, TimeZone var2) {
      return format((Calendar)var0, var1, var2, (Locale)null);
   }

   public static String format(long var0, String var2, Locale var3) {
      return format((Date)(new Date(var0)), var2, (TimeZone)null, var3);
   }

   public static String format(Date var0, String var1, Locale var2) {
      return format((Date)var0, var1, (TimeZone)null, var2);
   }

   public static String format(Calendar var0, String var1, Locale var2) {
      return format((Calendar)var0, var1, (TimeZone)null, var2);
   }

   public static String format(long var0, String var2, TimeZone var3, Locale var4) {
      return format(new Date(var0), var2, var3, var4);
   }

   public static String format(Date var0, String var1, TimeZone var2, Locale var3) {
      FastDateFormat var4 = FastDateFormat.getInstance(var1, var2, var3);
      return var4.format(var0);
   }

   public static String format(Calendar var0, String var1, TimeZone var2, Locale var3) {
      FastDateFormat var4 = FastDateFormat.getInstance(var1, var2, var3);
      return var4.format(var0);
   }

   static {
      SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
   }
}
