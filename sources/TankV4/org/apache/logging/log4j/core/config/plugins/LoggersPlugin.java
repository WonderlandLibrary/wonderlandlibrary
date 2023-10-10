package org.apache.logging.log4j.core.config.plugins;

import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Loggers;

@Plugin(
   name = "loggers",
   category = "Core"
)
public final class LoggersPlugin {
   private LoggersPlugin() {
   }

   @PluginFactory
   public static Loggers createLoggers(@PluginElement("Loggers") LoggerConfig[] var0) {
      ConcurrentHashMap var1 = new ConcurrentHashMap();
      LoggerConfig var2 = null;
      LoggerConfig[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         LoggerConfig var6 = var3[var5];
         if (var6 != null) {
            if (var6.getName().isEmpty()) {
               var2 = var6;
            }

            var1.put(var6.getName(), var6);
         }
      }

      return new Loggers(var1, var2);
   }
}
