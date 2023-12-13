package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "NDCPatternConverter",
   category = "Converter"
)
@ConverterKeys({"x", "NDC"})
public final class NDCPatternConverter extends LogEventPatternConverter {
   private static final NDCPatternConverter INSTANCE = new NDCPatternConverter();

   private NDCPatternConverter() {
      super("NDC", "ndc");
   }

   public static NDCPatternConverter newInstance(String[] var0) {
      return INSTANCE;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      var2.append(var1.getContextStack());
   }
}
