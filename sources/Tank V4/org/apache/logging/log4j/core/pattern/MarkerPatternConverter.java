package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "MarkerPatternConverter",
   category = "Converter"
)
@ConverterKeys({"marker"})
public final class MarkerPatternConverter extends LogEventPatternConverter {
   private MarkerPatternConverter(String[] var1) {
      super("Marker", "marker");
   }

   public static MarkerPatternConverter newInstance(String[] var0) {
      return new MarkerPatternConverter(var0);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      Marker var3 = var1.getMarker();
      if (var3 != null) {
         var2.append(var3.toString());
      }

   }
}
