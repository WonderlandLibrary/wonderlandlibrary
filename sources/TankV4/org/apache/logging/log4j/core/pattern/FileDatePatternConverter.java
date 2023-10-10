package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "FileDatePatternConverter",
   category = "FileConverter"
)
@ConverterKeys({"d", "date"})
public final class FileDatePatternConverter {
   private FileDatePatternConverter() {
   }

   public static PatternConverter newInstance(String[] var0) {
      return var0 != null && var0.length != 0 ? DatePatternConverter.newInstance(var0) : DatePatternConverter.newInstance(new String[]{"yyyy-MM-dd"});
   }
}
