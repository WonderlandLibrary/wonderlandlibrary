package org.apache.logging.log4j.core.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(
   name = "RegexFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public final class RegexFilter extends AbstractFilter {
   private final Pattern pattern;
   private final boolean useRawMessage;

   private RegexFilter(boolean var1, Pattern var2, Filter.Result var3, Filter.Result var4) {
      super(var3, var4);
      this.pattern = var2;
      this.useRawMessage = var1;
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      return this.filter(var4);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      return var4 == null ? this.onMismatch : this.filter(var4.toString());
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      if (var4 == null) {
         return this.onMismatch;
      } else {
         String var6 = this.useRawMessage ? var4.getFormat() : var4.getFormattedMessage();
         return this.filter(var6);
      }
   }

   public Filter.Result filter(LogEvent var1) {
      String var2 = this.useRawMessage ? var1.getMessage().getFormat() : var1.getMessage().getFormattedMessage();
      return this.filter(var2);
   }

   private Filter.Result filter(String var1) {
      if (var1 == null) {
         return this.onMismatch;
      } else {
         Matcher var2 = this.pattern.matcher(var1);
         return var2.matches() ? this.onMatch : this.onMismatch;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("useRaw=").append(this.useRawMessage);
      var1.append(", pattern=").append(this.pattern.toString());
      return var1.toString();
   }

   @PluginFactory
   public static RegexFilter createFilter(@PluginAttribute("regex") String var0, @PluginAttribute("useRawMsg") String var1, @PluginAttribute("onMatch") String var2, @PluginAttribute("onMismatch") String var3) {
      if (var0 == null) {
         LOGGER.error("A regular expression must be provided for RegexFilter");
         return null;
      } else {
         boolean var4 = Boolean.parseBoolean(var1);

         Pattern var5;
         try {
            var5 = Pattern.compile(var0);
         } catch (Exception var8) {
            LOGGER.error("RegexFilter caught exception compiling pattern: " + var0 + " cause: " + var8.getMessage());
            return null;
         }

         Filter.Result var6 = Filter.Result.toResult(var2);
         Filter.Result var7 = Filter.Result.toResult(var3);
         return new RegexFilter(var4, var5, var6, var7);
      }
   }
}
