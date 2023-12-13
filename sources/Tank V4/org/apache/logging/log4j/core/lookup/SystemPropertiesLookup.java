package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "sys",
   category = "Lookup"
)
public class SystemPropertiesLookup implements StrLookup {
   public String lookup(String var1) {
      try {
         return System.getProperty(var1);
      } catch (Exception var3) {
         return null;
      }
   }

   public String lookup(LogEvent var1, String var2) {
      try {
         return System.getProperty(var2);
      } catch (Exception var4) {
         return null;
      }
   }
}
