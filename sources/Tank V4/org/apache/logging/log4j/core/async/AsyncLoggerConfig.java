package org.apache.logging.log4j.core.async;

import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;

@Plugin(
   name = "asyncLogger",
   category = "Core",
   printObject = true
)
public class AsyncLoggerConfig extends LoggerConfig {
   private AsyncLoggerConfigHelper helper;

   public AsyncLoggerConfig() {
   }

   public AsyncLoggerConfig(String var1, Level var2, boolean var3) {
      super(var1, var2, var3);
   }

   protected AsyncLoggerConfig(String var1, List var2, Filter var3, Level var4, boolean var5, Property[] var6, Configuration var7, boolean var8) {
      super(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   protected void callAppenders(LogEvent var1) {
      var1.getSource();
      var1.getThreadName();
      this.helper.callAppendersFromAnotherThread(var1);
   }

   void asyncCallAppenders(LogEvent var1) {
      super.callAppenders(var1);
   }

   public void startFilter() {
      if (this.helper == null) {
         this.helper = new AsyncLoggerConfigHelper(this);
      } else {
         AsyncLoggerConfigHelper.claim();
      }

      super.startFilter();
   }

   public void stopFilter() {
      AsyncLoggerConfigHelper.release();
      super.stopFilter();
   }

   @PluginFactory
   public static LoggerConfig createLogger(@PluginAttribute("additivity") String var0, @PluginAttribute("level") String var1, @PluginAttribute("name") String var2, @PluginAttribute("includeLocation") String var3, @PluginElement("AppenderRef") AppenderRef[] var4, @PluginElement("Properties") Property[] var5, @PluginConfiguration Configuration var6, @PluginElement("Filters") Filter var7) {
      if (var2 == null) {
         LOGGER.error("Loggers cannot be configured without a name");
         return null;
      } else {
         List var8 = Arrays.asList(var4);

         Level var9;
         try {
            var9 = Level.toLevel(var1, Level.ERROR);
         } catch (Exception var12) {
            LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", var1);
            var9 = Level.ERROR;
         }

         String var10 = var2.equals("root") ? "" : var2;
         boolean var11 = Booleans.parseBoolean(var0, true);
         return new AsyncLoggerConfig(var10, var8, var7, var9, var11, var5, var6, includeLocation(var3));
      }
   }

   protected static boolean includeLocation(String var0) {
      return Boolean.parseBoolean(var0);
   }

   @Plugin(
      name = "asyncRoot",
      category = "Core",
      printObject = true
   )
   public static class RootLogger extends LoggerConfig {
      @PluginFactory
      public static LoggerConfig createLogger(@PluginAttribute("additivity") String var0, @PluginAttribute("level") String var1, @PluginAttribute("includeLocation") String var2, @PluginElement("AppenderRef") AppenderRef[] var3, @PluginElement("Properties") Property[] var4, @PluginConfiguration Configuration var5, @PluginElement("Filters") Filter var6) {
         List var7 = Arrays.asList(var3);

         Level var8;
         try {
            var8 = Level.toLevel(var1, Level.ERROR);
         } catch (Exception var10) {
            LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", var1);
            var8 = Level.ERROR;
         }

         boolean var9 = Booleans.parseBoolean(var0, true);
         return new AsyncLoggerConfig("", var7, var6, var8, var9, var4, var5, includeLocation(var2));
      }
   }
}
