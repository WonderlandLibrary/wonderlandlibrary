package org.apache.logging.log4j.core.lookup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.status.StatusLogger;

public class Interpolator implements StrLookup {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final char PREFIX_SEPARATOR = ':';
   private final Map lookups = new HashMap();
   private final StrLookup defaultLookup;

   public Interpolator(StrLookup var1) {
      this.defaultLookup = (StrLookup)(var1 == null ? new MapLookup(new HashMap()) : var1);
      PluginManager var2 = new PluginManager("Lookup");
      var2.collectPlugins();
      Map var3 = var2.getPlugins();
      Iterator var4 = var3.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         Class var6 = ((PluginType)var5.getValue()).getPluginClass();

         try {
            this.lookups.put(var5.getKey(), var6.newInstance());
         } catch (Exception var8) {
            LOGGER.error((String)("Unable to create Lookup for " + (String)var5.getKey()), (Throwable)var8);
         }
      }

   }

   public Interpolator() {
      this.defaultLookup = new MapLookup(new HashMap());
      this.lookups.put("sys", new SystemPropertiesLookup());
      this.lookups.put("env", new EnvironmentLookup());
      this.lookups.put("jndi", new JndiLookup());

      try {
         if (Class.forName("javax.servlet.ServletContext") != null) {
            this.lookups.put("web", new WebLookup());
         }
      } catch (ClassNotFoundException var2) {
         LOGGER.debug("ServletContext not present - WebLookup not added");
      } catch (Exception var3) {
         LOGGER.error((String)"Unable to locate ServletContext", (Throwable)var3);
      }

   }

   public String lookup(String var1) {
      return this.lookup((LogEvent)null, var1);
   }

   public String lookup(LogEvent var1, String var2) {
      if (var2 == null) {
         return null;
      } else {
         int var3 = var2.indexOf(58);
         if (var3 >= 0) {
            String var4 = var2.substring(0, var3);
            String var5 = var2.substring(var3 + 1);
            StrLookup var6 = (StrLookup)this.lookups.get(var4);
            String var7 = null;
            if (var6 != null) {
               var7 = var1 == null ? var6.lookup(var5) : var6.lookup(var1, var5);
            }

            if (var7 != null) {
               return var7;
            }

            var2 = var2.substring(var3 + 1);
         }

         if (this.defaultLookup != null) {
            return var1 == null ? this.defaultLookup.lookup(var2) : this.defaultLookup.lookup(var1, var2);
         } else {
            return null;
         }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();

      String var3;
      for(Iterator var2 = this.lookups.keySet().iterator(); var2.hasNext(); var1.append(var3)) {
         var3 = (String)var2.next();
         if (var1.length() == 0) {
            var1.append("{");
         } else {
            var1.append(", ");
         }
      }

      if (var1.length() > 0) {
         var1.append("}");
      }

      return var1.toString();
   }
}
