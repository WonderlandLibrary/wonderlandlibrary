package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "failovers",
   category = "Core"
)
public final class FailoversPlugin {
   private static final Logger LOGGER = StatusLogger.getLogger();

   private FailoversPlugin() {
   }

   @PluginFactory
   public static String[] createFailovers(@PluginElement("AppenderRef") AppenderRef... var0) {
      if (var0 == null) {
         LOGGER.error("failovers must contain an appender reference");
         return null;
      } else {
         String[] var1 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2].getRef();
         }

         return var1;
      }
   }
}
