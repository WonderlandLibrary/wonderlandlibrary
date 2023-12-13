package org.apache.logging.log4j.core.config;

import java.io.File;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "JSONConfigurationFactory",
   category = "ConfigurationFactory"
)
@Order(6)
public class JSONConfigurationFactory extends ConfigurationFactory {
   public static final String[] SUFFIXES = new String[]{".json", ".jsn"};
   private static String[] dependencies = new String[]{"com.fasterxml.jackson.databind.ObjectMapper", "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonParser"};
   private final File configFile = null;
   private boolean isActive;

   public JSONConfigurationFactory() {
      try {
         String[] var1 = dependencies;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            Class.forName(var4);
         }
      } catch (ClassNotFoundException var5) {
         LOGGER.debug("Missing dependencies for Json support");
         this.isActive = false;
         return;
      }

      this.isActive = true;
   }

   protected boolean isActive() {
      return this.isActive;
   }

   public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource var1) {
      return !this.isActive ? null : new JSONConfiguration(var1);
   }

   public String[] getSupportedTypes() {
      return SUFFIXES;
   }
}
