package org.apache.logging.log4j.core.appender.rolling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.pattern.ArrayPatternConverter;
import org.apache.logging.log4j.core.pattern.DatePatternConverter;
import org.apache.logging.log4j.core.pattern.FormattingInfo;
import org.apache.logging.log4j.core.pattern.PatternParser;

public class PatternProcessor {
   private static final String KEY = "FileConverter";
   private static final char YEAR_CHAR = 'y';
   private static final char MONTH_CHAR = 'M';
   private static final char[] WEEK_CHARS = new char[]{'w', 'W'};
   private static final char[] DAY_CHARS = new char[]{'D', 'd', 'F', 'E'};
   private static final char[] HOUR_CHARS = new char[]{'H', 'K', 'h', 'k'};
   private static final char MINUTE_CHAR = 'm';
   private static final char SECOND_CHAR = 's';
   private static final char MILLIS_CHAR = 'S';
   private final ArrayPatternConverter[] patternConverters;
   private final FormattingInfo[] patternFields;
   private long prevFileTime = 0L;
   private long nextFileTime = 0L;
   private RolloverFrequency frequency = null;

   public PatternProcessor(String var1) {
      PatternParser var2 = this.createPatternParser();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      var2.parse(var1, var3, var4);
      FormattingInfo[] var5 = new FormattingInfo[var4.size()];
      this.patternFields = (FormattingInfo[])var4.toArray(var5);
      ArrayPatternConverter[] var6 = new ArrayPatternConverter[var3.size()];
      this.patternConverters = (ArrayPatternConverter[])var3.toArray(var6);
      ArrayPatternConverter[] var7 = this.patternConverters;
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         ArrayPatternConverter var10 = var7[var9];
         if (var10 instanceof DatePatternConverter) {
            DatePatternConverter var11 = (DatePatternConverter)var10;
            this.frequency = this.calculateFrequency(var11.getPattern());
         }
      }

   }

   public long getNextTime(long var1, int var3, boolean var4) {
      this.prevFileTime = this.nextFileTime;
      if (this.frequency == null) {
         throw new IllegalStateException("Pattern does not contain a date");
      } else {
         Calendar var7 = Calendar.getInstance();
         var7.setTimeInMillis(var1);
         Calendar var8 = Calendar.getInstance();
         var8.set(var7.get(1), 0, 1, 0, 0, 0);
         var8.set(14, 0);
         long var5;
         if (this.frequency == RolloverFrequency.ANNUALLY) {
            this.increment(var8, 1, var3, var4);
            var5 = var8.getTimeInMillis();
            var8.add(1, -1);
            this.nextFileTime = var8.getTimeInMillis();
            return var5;
         } else if (this.frequency == RolloverFrequency.MONTHLY) {
            this.increment(var8, 2, var3, var4);
            var5 = var8.getTimeInMillis();
            var8.add(2, -1);
            this.nextFileTime = var8.getTimeInMillis();
            return var5;
         } else if (this.frequency == RolloverFrequency.WEEKLY) {
            this.increment(var8, 3, var3, var4);
            var5 = var8.getTimeInMillis();
            var8.add(3, -1);
            this.nextFileTime = var8.getTimeInMillis();
            return var5;
         } else {
            var8.set(6, var7.get(6));
            if (this.frequency == RolloverFrequency.DAILY) {
               this.increment(var8, 6, var3, var4);
               var5 = var8.getTimeInMillis();
               var8.add(6, -1);
               this.nextFileTime = var8.getTimeInMillis();
               return var5;
            } else {
               var8.set(10, var7.get(10));
               if (this.frequency == RolloverFrequency.HOURLY) {
                  this.increment(var8, 10, var3, var4);
                  var5 = var8.getTimeInMillis();
                  var8.add(10, -1);
                  this.nextFileTime = var8.getTimeInMillis();
                  return var5;
               } else {
                  var8.set(12, var7.get(12));
                  if (this.frequency == RolloverFrequency.EVERY_MINUTE) {
                     this.increment(var8, 12, var3, var4);
                     var5 = var8.getTimeInMillis();
                     var8.add(12, -1);
                     this.nextFileTime = var8.getTimeInMillis();
                     return var5;
                  } else {
                     var8.set(13, var7.get(13));
                     if (this.frequency == RolloverFrequency.EVERY_SECOND) {
                        this.increment(var8, 13, var3, var4);
                        var5 = var8.getTimeInMillis();
                        var8.add(13, -1);
                        this.nextFileTime = var8.getTimeInMillis();
                        return var5;
                     } else {
                        this.increment(var8, 14, var3, var4);
                        var5 = var8.getTimeInMillis();
                        var8.add(14, -1);
                        this.nextFileTime = var8.getTimeInMillis();
                        return var5;
                     }
                  }
               }
            }
         }
      }
   }

   private void increment(Calendar var1, int var2, int var3, boolean var4) {
      int var5 = var4 ? var3 - var1.get(var2) % var3 : var3;
      var1.add(var2, var5);
   }

   public final void formatFileName(StringBuilder var1, Object var2) {
      long var3 = this.prevFileTime == 0L ? System.currentTimeMillis() : this.prevFileTime;
      this.formatFileName(var1, new Date(var3), var2);
   }

   public final void formatFileName(StrSubstitutor var1, StringBuilder var2, Object var3) {
      long var4 = this.prevFileTime == 0L ? System.currentTimeMillis() : this.prevFileTime;
      this.formatFileName(var2, new Date(var4), var3);
      Log4jLogEvent var6 = new Log4jLogEvent(var4);
      String var7 = var1.replace((LogEvent)var6, (StringBuilder)var2);
      var2.setLength(0);
      var2.append(var7);
   }

   protected final void formatFileName(StringBuilder var1, Object... var2) {
      for(int var3 = 0; var3 < this.patternConverters.length; ++var3) {
         int var4 = var1.length();
         this.patternConverters[var3].format(var1, var2);
         if (this.patternFields[var3] != null) {
            this.patternFields[var3].format(var4, var1);
         }
      }

   }

   private RolloverFrequency calculateFrequency(String var1) {
      return RolloverFrequency.EVERY_MILLISECOND;
   }

   private PatternParser createPatternParser() {
      return new PatternParser((Configuration)null, "FileConverter", (Class)null);
   }
}
