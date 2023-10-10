package org.apache.logging.log4j.core.pattern;

import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "RelativeTimePatternConverter",
   category = "Converter"
)
@ConverterKeys({"r", "relative"})
public class RelativeTimePatternConverter extends LogEventPatternConverter {
   private long lastTimestamp = Long.MIN_VALUE;
   private final long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
   private String relative;

   public RelativeTimePatternConverter() {
      super("Time", "time");
   }

   public static RelativeTimePatternConverter newInstance(String[] var0) {
      return new RelativeTimePatternConverter();
   }

   public void format(LogEvent var1, StringBuilder var2) {
      long var3 = var1.getMillis();
      synchronized(this){}
      if (var3 != this.lastTimestamp) {
         this.lastTimestamp = var3;
         this.relative = Long.toString(var3 - this.startTime);
      }

      var2.append(this.relative);
   }
}
