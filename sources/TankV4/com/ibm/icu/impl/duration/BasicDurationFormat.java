package com.ibm.icu.impl.duration;

import com.ibm.icu.text.DurationFormat;
import com.ibm.icu.util.ULocale;
import java.text.FieldPosition;
import java.util.Date;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.DatatypeConstants.Field;

public class BasicDurationFormat extends DurationFormat {
   private static final long serialVersionUID = -3146984141909457700L;
   transient DurationFormatter formatter;
   transient PeriodFormatter pformatter;
   transient PeriodFormatterService pfs = null;
   private static boolean checkXMLDuration = true;

   public static BasicDurationFormat getInstance(ULocale var0) {
      return new BasicDurationFormat(var0);
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      String var4;
      if (var1 instanceof Long) {
         var4 = this.formatDurationFromNow((Long)var1);
         return var2.append(var4);
      } else if (var1 instanceof Date) {
         var4 = this.formatDurationFromNowTo((Date)var1);
         return var2.append(var4);
      } else {
         if (checkXMLDuration) {
            try {
               if (var1 instanceof Duration) {
                  var4 = this.formatDuration(var1);
                  return var2.append(var4);
               }
            } catch (NoClassDefFoundError var5) {
               System.err.println("Skipping XML capability");
               checkXMLDuration = false;
            }
         }

         throw new IllegalArgumentException("Cannot format given Object as a Duration");
      }
   }

   public BasicDurationFormat() {
      this.pfs = BasicPeriodFormatterService.getInstance();
      this.formatter = this.pfs.newDurationFormatterFactory().getFormatter();
      this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).getFormatter();
   }

   public BasicDurationFormat(ULocale var1) {
      super(var1);
      this.pfs = BasicPeriodFormatterService.getInstance();
      this.formatter = this.pfs.newDurationFormatterFactory().setLocale(var1.getName()).getFormatter();
      this.pformatter = this.pfs.newPeriodFormatterFactory().setDisplayPastFuture(false).setLocale(var1.getName()).getFormatter();
   }

   public String formatDurationFrom(long var1, long var3) {
      return this.formatter.formatDurationFrom(var1, var3);
   }

   public String formatDurationFromNow(long var1) {
      return this.formatter.formatDurationFromNow(var1);
   }

   public String formatDurationFromNowTo(Date var1) {
      return this.formatter.formatDurationFromNowTo(var1);
   }

   public String formatDuration(Object var1) {
      Field[] var2 = new Field[]{DatatypeConstants.YEARS, DatatypeConstants.MONTHS, DatatypeConstants.DAYS, DatatypeConstants.HOURS, DatatypeConstants.MINUTES, DatatypeConstants.SECONDS};
      TimeUnit[] var3 = new TimeUnit[]{TimeUnit.YEAR, TimeUnit.MONTH, TimeUnit.DAY, TimeUnit.HOUR, TimeUnit.MINUTE, TimeUnit.SECOND};
      Duration var4 = (Duration)var1;
      Period var5 = null;
      Duration var6 = var4;
      boolean var7 = false;
      if (var4.getSign() < 0) {
         var6 = var4.negate();
         var7 = true;
      }

      boolean var8 = false;

      for(int var9 = 0; var9 < var2.length; ++var9) {
         if (var6.isSet(var2[var9])) {
            Number var10 = var6.getField(var2[var9]);
            if (var10.intValue() != 0 || var8) {
               var8 = true;
               float var11 = var10.floatValue();
               TimeUnit var12 = null;
               float var13 = 0.0F;
               if (var3[var9] == TimeUnit.SECOND) {
                  double var14 = (double)var11;
                  double var16 = Math.floor((double)var11);
                  double var18 = (var14 - var16) * 1000.0D;
                  if (var18 > 0.0D) {
                     var12 = TimeUnit.MILLISECOND;
                     var13 = (float)var18;
                     var11 = (float)var16;
                  }
               }

               if (var5 == null) {
                  var5 = Period.at(var11, var3[var9]);
               } else {
                  var5 = var5.and(var11, var3[var9]);
               }

               if (var12 != null) {
                  var5 = var5.and(var13, var12);
               }
            }
         }
      }

      if (var5 == null) {
         return this.formatDurationFromNow(0L);
      } else {
         if (var7) {
            var5 = var5.inPast();
         } else {
            var5 = var5.inFuture();
         }

         return this.pformatter.format(var5);
      }
   }
}
