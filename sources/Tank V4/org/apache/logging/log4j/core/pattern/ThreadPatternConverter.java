package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "ThreadPatternConverter",
   category = "Converter"
)
@ConverterKeys({"t", "thread"})
public final class ThreadPatternConverter extends LogEventPatternConverter {
   private static final ThreadPatternConverter INSTANCE = new ThreadPatternConverter();

   private ThreadPatternConverter() {
      super("Thread", "thread");
   }

   public static ThreadPatternConverter newInstance(String[] var0) {
      return INSTANCE;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      var2.append(var1.getThreadName());
   }
}
