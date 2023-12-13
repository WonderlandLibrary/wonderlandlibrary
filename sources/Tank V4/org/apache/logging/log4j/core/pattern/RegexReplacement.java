package org.apache.logging.log4j.core.pattern;

import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "replace",
   category = "Core",
   printObject = true
)
public final class RegexReplacement {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final Pattern pattern;
   private final String substitution;

   private RegexReplacement(Pattern var1, String var2) {
      this.pattern = var1;
      this.substitution = var2;
   }

   public String format(String var1) {
      return this.pattern.matcher(var1).replaceAll(this.substitution);
   }

   public String toString() {
      return "replace(regex=" + this.pattern.pattern() + ", replacement=" + this.substitution + ")";
   }

   @PluginFactory
   public static RegexReplacement createRegexReplacement(@PluginAttribute("regex") String var0, @PluginAttribute("replacement") String var1) {
      if (var0 == null) {
         LOGGER.error("A regular expression is required for replacement");
         return null;
      } else {
         if (var1 == null) {
            LOGGER.error("A replacement string is required to perform replacement");
         }

         Pattern var2 = Pattern.compile(var0);
         return new RegexReplacement(var2, var1);
      }
   }
}
