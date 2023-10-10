package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "MethodLocationPatternConverter",
   category = "Converter"
)
@ConverterKeys({"M", "method"})
public final class MethodLocationPatternConverter extends LogEventPatternConverter {
   private static final MethodLocationPatternConverter INSTANCE = new MethodLocationPatternConverter();

   private MethodLocationPatternConverter() {
      super("Method", "method");
   }

   public static MethodLocationPatternConverter newInstance(String[] var0) {
      return INSTANCE;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StackTraceElement var3 = var1.getSource();
      if (var3 != null) {
         var2.append(var3.getMethodName());
      }

   }
}
