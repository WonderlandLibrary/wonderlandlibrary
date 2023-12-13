package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "Routes",
   category = "Core",
   printObject = true
)
public final class Routes {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final String pattern;
   private final Route[] routes;

   private Routes(String var1, Route... var2) {
      this.pattern = var1;
      this.routes = var2;
   }

   public String getPattern() {
      return this.pattern;
   }

   public Route[] getRoutes() {
      return this.routes;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      boolean var2 = true;
      Route[] var3 = this.routes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Route var6 = var3[var5];
         if (!var2) {
            var1.append(",");
         }

         var2 = false;
         var1.append(var6.toString());
      }

      var1.append("}");
      return var1.toString();
   }

   @PluginFactory
   public static Routes createRoutes(@PluginAttribute("pattern") String var0, @PluginElement("Routes") Route... var1) {
      if (var0 == null) {
         LOGGER.error("A pattern is required");
         return null;
      } else if (var1 != null && var1.length != 0) {
         return new Routes(var0, var1);
      } else {
         LOGGER.error("No routes configured");
         return null;
      }
   }
}
