package org.apache.logging.log4j.core.pattern;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
   name = "replace",
   category = "Converter"
)
@ConverterKeys({"replace"})
public final class RegexReplacementConverter extends LogEventPatternConverter {
   private final Pattern pattern;
   private final String substitution;
   private final List formatters;

   private RegexReplacementConverter(List var1, Pattern var2, String var3) {
      super("replace", "replace");
      this.pattern = var2;
      this.substitution = var3;
      this.formatters = var1;
   }

   public static RegexReplacementConverter newInstance(Configuration var0, String[] var1) {
      if (var1.length != 3) {
         LOGGER.error("Incorrect number of options on replace. Expected 3 received " + var1.length);
         return null;
      } else if (var1[0] == null) {
         LOGGER.error("No pattern supplied on replace");
         return null;
      } else if (var1[1] == null) {
         LOGGER.error("No regular expression supplied on replace");
         return null;
      } else if (var1[2] == null) {
         LOGGER.error("No substitution supplied on replace");
         return null;
      } else {
         Pattern var2 = Pattern.compile(var1[1]);
         PatternParser var3 = PatternLayout.createPatternParser(var0);
         List var4 = var3.parse(var1[0]);
         return new RegexReplacementConverter(var4, var2, var1[2]);
      }
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StringBuilder var3 = new StringBuilder();
      Iterator var4 = this.formatters.iterator();

      while(var4.hasNext()) {
         PatternFormatter var5 = (PatternFormatter)var4.next();
         var5.format(var1, var3);
      }

      var2.append(this.pattern.matcher(var3.toString()).replaceAll(this.substitution));
   }
}
