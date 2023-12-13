package org.apache.logging.log4j.core.pattern;

import java.util.UUID;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.UUIDUtil;

@Plugin(
   name = "UUIDPatternConverter",
   category = "Converter"
)
@ConverterKeys({"u", "uuid"})
public final class UUIDPatternConverter extends LogEventPatternConverter {
   private final boolean isRandom;

   private UUIDPatternConverter(boolean var1) {
      super("u", "uuid");
      this.isRandom = var1;
   }

   public static UUIDPatternConverter newInstance(String[] var0) {
      if (var0.length == 0) {
         return new UUIDPatternConverter(false);
      } else {
         if (var0.length > 1 || !var0[0].equalsIgnoreCase("RANDOM") && !var0[0].equalsIgnoreCase("Time")) {
            LOGGER.error("UUID Pattern Converter only accepts a single option with the value \"RANDOM\" or \"TIME\"");
         }

         return new UUIDPatternConverter(var0[0].equalsIgnoreCase("RANDOM"));
      }
   }

   public void format(LogEvent var1, StringBuilder var2) {
      UUID var3 = this.isRandom ? UUID.randomUUID() : UUIDUtil.getTimeBasedUUID();
      var2.append(var3.toString());
   }
}
