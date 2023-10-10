package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
   name = "style",
   category = "Converter"
)
@ConverterKeys({"style"})
public final class StyleConverter extends LogEventPatternConverter {
   private final List patternFormatters;
   private final String style;

   private StyleConverter(List var1, String var2) {
      super("style", "style");
      this.patternFormatters = var1;
      this.style = var2;
   }

   public static StyleConverter newInstance(Configuration var0, String[] var1) {
      if (var1.length < 1) {
         LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + var1.length);
         return null;
      } else if (var1[0] == null) {
         LOGGER.error("No pattern supplied on style");
         return null;
      } else if (var1[1] == null) {
         LOGGER.error("No style attributes provided");
         return null;
      } else {
         PatternParser var2 = PatternLayout.createPatternParser(var0);
         List var3 = var2.parse(var1[0]);
         String var4 = AnsiEscape.createSequence(var1[1].split("\\s*,\\s*"));
         return new StyleConverter(var3, var4);
      }
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StringBuilder var3 = new StringBuilder();
      Iterator var4 = this.patternFormatters.iterator();

      while(var4.hasNext()) {
         PatternFormatter var5 = (PatternFormatter)var4.next();
         var5.format(var1, var3);
      }

      if (var3.length() > 0) {
         var2.append(this.style).append(var3.toString()).append(AnsiEscape.getDefaultStyle());
      }

   }

   public boolean handlesThrowable() {
      Iterator var1 = this.patternFormatters.iterator();

      PatternFormatter var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (PatternFormatter)var1.next();
      } while(!var2.handlesThrowable());

      return true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("[style=");
      var1.append(this.style);
      var1.append(", patternFormatters=");
      var1.append(this.patternFormatters);
      var1.append("]");
      return var1.toString();
   }
}
