package org.apache.logging.log4j.core.pattern;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "DatePatternConverter",
   category = "Converter"
)
@ConverterKeys({"d", "date"})
public final class DatePatternConverter extends LogEventPatternConverter implements ArrayPatternConverter {
   private static final String ABSOLUTE_FORMAT = "ABSOLUTE";
   private static final String COMPACT_FORMAT = "COMPACT";
   private static final String ABSOLUTE_TIME_PATTERN = "HH:mm:ss,SSS";
   private static final String DATE_AND_TIME_FORMAT = "DATE";
   private static final String DATE_AND_TIME_PATTERN = "dd MMM yyyy HH:mm:ss,SSS";
   private static final String ISO8601_FORMAT = "ISO8601";
   private static final String ISO8601_BASIC_FORMAT = "ISO8601_BASIC";
   private static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
   private static final String ISO8601_BASIC_PATTERN = "yyyyMMdd HHmmss,SSS";
   private static final String COMPACT_PATTERN = "yyyyMMddHHmmssSSS";
   private String cachedDate;
   private long lastTimestamp;
   private final SimpleDateFormat simpleFormat;

   private DatePatternConverter(String[] var1) {
      super("Date", "date");
      String var2;
      if (var1 != null && var1.length != 0) {
         var2 = var1[0];
      } else {
         var2 = null;
      }

      String var3;
      if (var2 != null && !var2.equalsIgnoreCase("ISO8601")) {
         if (var2.equalsIgnoreCase("ISO8601_BASIC")) {
            var3 = "yyyyMMdd HHmmss,SSS";
         } else if (var2.equalsIgnoreCase("ABSOLUTE")) {
            var3 = "HH:mm:ss,SSS";
         } else if (var2.equalsIgnoreCase("DATE")) {
            var3 = "dd MMM yyyy HH:mm:ss,SSS";
         } else if (var2.equalsIgnoreCase("COMPACT")) {
            var3 = "yyyyMMddHHmmssSSS";
         } else {
            var3 = var2;
         }
      } else {
         var3 = "yyyy-MM-dd HH:mm:ss,SSS";
      }

      SimpleDateFormat var4;
      try {
         var4 = new SimpleDateFormat(var3);
      } catch (IllegalArgumentException var6) {
         LOGGER.warn((String)("Could not instantiate SimpleDateFormat with pattern " + var2), (Throwable)var6);
         var4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
      }

      if (var1 != null && var1.length > 1) {
         TimeZone var5 = TimeZone.getTimeZone(var1[1]);
         var4.setTimeZone(var5);
      }

      this.simpleFormat = var4;
   }

   public static DatePatternConverter newInstance(String[] var0) {
      return new DatePatternConverter(var0);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      long var3 = var1.getMillis();
      synchronized(this){}
      if (var3 != this.lastTimestamp) {
         this.lastTimestamp = var3;
         this.cachedDate = this.simpleFormat.format(var3);
      }

      var2.append(this.cachedDate);
   }

   public void format(StringBuilder var1, Object... var2) {
      Object[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object var6 = var3[var5];
         if (var6 instanceof Date) {
            this.format(var6, var1);
            break;
         }
      }

   }

   public void format(Object var1, StringBuilder var2) {
      if (var1 instanceof Date) {
         this.format((Date)var1, var2);
      }

      super.format(var1, var2);
   }

   public void format(Date var1, StringBuilder var2) {
      synchronized(this){}
      var2.append(this.simpleFormat.format(var1.getTime()));
   }

   public String getPattern() {
      return this.simpleFormat.toPattern();
   }
}
