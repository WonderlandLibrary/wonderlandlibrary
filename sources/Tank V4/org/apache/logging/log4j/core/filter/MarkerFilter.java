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
   name = "MarkerFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public final class MarkerFilter extends AbstractFilter {
   private final String name;

   private MarkerFilter(String var1, Filter.Result var2, Filter.Result var3) {
      super(var2, var3);
      this.name = var1;
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      return this.filter(var3);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      return this.filter(var3);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      return this.filter(var3);
   }

   public Filter.Result filter(LogEvent var1) {
      return this.filter(var1.getMarker());
   }

   private Filter.Result filter(Marker var1) {
      return var1 != null && var1.isInstanceOf(this.name) ? this.onMatch : this.onMismatch;
   }

   public String toString() {
      return this.name;
   }

   @PluginFactory
   public static MarkerFilter createFilter(@PluginAttribute("marker") String var0, @PluginAttribute("onMatch") String var1, @PluginAttribute("onMismatch") String var2) {
      if (var0 == null) {
         LOGGER.error("A marker must be provided for MarkerFilter");
         return null;
      } else {
         Filter.Result var3 = Filter.Result.toResult(var1);
         Filter.Result var4 = Filter.Result.toResult(var2);
         return new MarkerFilter(var0, var3, var4);
      }
   }
}
