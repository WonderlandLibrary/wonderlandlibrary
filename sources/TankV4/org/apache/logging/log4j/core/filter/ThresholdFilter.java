package org.apache.logging.log4j.core.filter;

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
   name = "ThresholdFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public final class ThresholdFilter extends AbstractFilter {
   private final Level level;

   private ThresholdFilter(Level var1, Filter.Result var2, Filter.Result var3) {
      super(var2, var3);
      this.level = var1;
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(LogEvent var1) {
      return this.filter(var1.getLevel());
   }

   private Filter.Result filter(Level var1) {
      return var1.isAtLeastAsSpecificAs(this.level) ? this.onMatch : this.onMismatch;
   }

   public String toString() {
      return this.level.toString();
   }

   @PluginFactory
   public static ThresholdFilter createFilter(@PluginAttribute("level") String var0, @PluginAttribute("onMatch") String var1, @PluginAttribute("onMismatch") String var2) {
      Level var3 = Level.toLevel(var0, Level.ERROR);
      Filter.Result var4 = Filter.Result.toResult(var1, Filter.Result.NEUTRAL);
      Filter.Result var5 = Filter.Result.toResult(var2, Filter.Result.DENY);
      return new ThresholdFilter(var3, var4, var5);
   }
}
