package org.apache.logging.log4j.core.config.plugins;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.Appender;

@Plugin(
   name = "appenders",
   category = "Core"
)
public final class AppendersPlugin {
   private AppendersPlugin() {
   }

   @PluginFactory
   public static ConcurrentMap createAppenders(@PluginElement("Appenders") Appender[] var0) {
      ConcurrentHashMap var1 = new ConcurrentHashMap();
      Appender[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Appender var5 = var2[var4];
         var1.put(var5.getName(), var5);
      }

      return var1;
   }
}
