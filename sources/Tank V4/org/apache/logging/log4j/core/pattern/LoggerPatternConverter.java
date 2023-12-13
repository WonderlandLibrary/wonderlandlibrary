package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "LoggerPatternConverter",
   category = "Converter"
)
@ConverterKeys({"c", "logger"})
public final class LoggerPatternConverter extends NamePatternConverter {
   private static final LoggerPatternConverter INSTANCE = new LoggerPatternConverter((String[])null);

   private LoggerPatternConverter(String[] var1) {
      super("Logger", "logger", var1);
   }

   public static LoggerPatternConverter newInstance(String[] var0) {
      return var0 != null && var0.length != 0 ? new LoggerPatternConverter(var0) : INSTANCE;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      var2.append(this.abbreviate(var1.getLoggerName()));
   }
}
