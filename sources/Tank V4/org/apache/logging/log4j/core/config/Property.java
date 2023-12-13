package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "property",
   category = "Core",
   printObject = true
)
public final class Property {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private final String name;
   private final String value;

   private Property(String var1, String var2) {
      this.name = var1;
      this.value = var2;
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   @PluginFactory
   public static Property createProperty(@PluginAttribute("name") String var0, @PluginValue("value") String var1) {
      if (var0 == null) {
         LOGGER.error("Property key cannot be null");
      }

      return new Property(var0, var1);
   }

   public String toString() {
      return this.name + "=" + this.value;
   }
}
