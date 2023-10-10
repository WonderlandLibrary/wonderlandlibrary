package org.apache.logging.log4j.core.filter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(
   name = "TimeFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public final class TimeFilter extends AbstractFilter {
   private static final long HOUR_MS = 3600000L;
   private static final long MINUTE_MS = 60000L;
   private static final long SECOND_MS = 1000L;
   private final long start;
   private final long end;
   private final TimeZone timezone;

   private TimeFilter(long var1, long var3, TimeZone var5, Filter.Result var6, Filter.Result var7) {
      super(var6, var7);
      this.start = var1;
      this.end = var3;
      this.timezone = var5;
   }

   public Filter.Result filter(LogEvent var1) {
      Calendar var2 = Calendar.getInstance(this.timezone);
      var2.setTimeInMillis(var1.getMillis());
      long var3 = (long)var2.get(11) * 3600000L + (long)var2.get(12) * 60000L + (long)var2.get(13) * 1000L + (long)var2.get(14);
      return var3 >= this.start && var3 < this.end ? this.onMatch : this.onMismatch;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("start=").append(this.start);
      var1.append(", end=").append(this.end);
      var1.append(", timezone=").append(this.timezone.toString());
      return var1.toString();
   }

   @PluginFactory
   public static TimeFilter createFilter(@PluginAttribute("start") String var0, @PluginAttribute("end") String var1, @PluginAttribute("timezone") String var2, @PluginAttribute("onMatch") String var3, @PluginAttribute("onMismatch") String var4) {
      SimpleDateFormat var5 = new SimpleDateFormat("HH:mm:ss");
      long var6 = 0L;
      if (var0 != null) {
         var5.setTimeZone(TimeZone.getTimeZone("UTC"));

         try {
            var6 = var5.parse(var0).getTime();
         } catch (ParseException var14) {
            LOGGER.warn((String)("Error parsing start value " + var0), (Throwable)var14);
         }
      }

      long var8 = Long.MAX_VALUE;
      if (var1 != null) {
         var5.setTimeZone(TimeZone.getTimeZone("UTC"));

         try {
            var8 = var5.parse(var1).getTime();
         } catch (ParseException var13) {
            LOGGER.warn((String)("Error parsing start value " + var1), (Throwable)var13);
         }
      }

      TimeZone var10 = var2 == null ? TimeZone.getDefault() : TimeZone.getTimeZone(var2);
      Filter.Result var11 = Filter.Result.toResult(var3, Filter.Result.NEUTRAL);
      Filter.Result var12 = Filter.Result.toResult(var4, Filter.Result.DENY);
      return new TimeFilter(var6, var8, var10, var11, var12);
   }
}
