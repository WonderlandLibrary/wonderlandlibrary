package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "FullLocationPatternConverter",
   category = "Converter"
)
@ConverterKeys({"l", "location"})
public final class FullLocationPatternConverter extends LogEventPatternConverter {
   private static final FullLocationPatternConverter INSTANCE = new FullLocationPatternConverter();

   private FullLocationPatternConverter() {
      super("Full Location", "fullLocation");
   }

   public static FullLocationPatternConverter newInstance(String[] var0) {
      return INSTANCE;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StackTraceElement var3 = var1.getSource();
      if (var3 != null) {
         var2.append(var3.toString());
      }

   }
}
