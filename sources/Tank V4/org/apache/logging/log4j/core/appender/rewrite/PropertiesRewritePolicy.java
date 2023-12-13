package org.apache.logging.log4j.core.appender.rewrite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "PropertiesRewritePolicy",
   category = "Core",
   elementType = "rewritePolicy",
   printObject = true
)
public final class PropertiesRewritePolicy implements RewritePolicy {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private final Map properties;
   private final Configuration config;

   private PropertiesRewritePolicy(Configuration var1, List var2) {
      this.config = var1;
      this.properties = new HashMap(var2.size());
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Property var4 = (Property)var3.next();
         Boolean var5 = var4.getValue().contains("${");
         this.properties.put(var4, var5);
      }

   }

   public LogEvent rewrite(LogEvent var1) {
      HashMap var2 = new HashMap(var1.getContextMap());
      Iterator var3 = this.properties.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         Property var5 = (Property)var4.getKey();
         var2.put(var5.getName(), (Boolean)var4.getValue() ? this.config.getStrSubstitutor().replace(var5.getValue()) : var5.getValue());
      }

      return new Log4jLogEvent(var1.getLoggerName(), var1.getMarker(), var1.getFQCN(), var1.getLevel(), var1.getMessage(), var1.getThrown(), var2, var1.getContextStack(), var1.getThreadName(), var1.getSource(), var1.getMillis());
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(" {");
      boolean var2 = true;

      for(Iterator var3 = this.properties.entrySet().iterator(); var3.hasNext(); var2 = false) {
         Entry var4 = (Entry)var3.next();
         if (!var2) {
            var1.append(", ");
         }

         Property var5 = (Property)var4.getKey();
         var1.append(var5.getName()).append("=").append(var5.getValue());
      }

      var1.append("}");
      return var1.toString();
   }

   @PluginFactory
   public static PropertiesRewritePolicy createPolicy(@PluginConfiguration Configuration var0, @PluginElement("Properties") Property[] var1) {
      if (var1 != null && var1.length != 0) {
         List var2 = Arrays.asList(var1);
         return new PropertiesRewritePolicy(var0, var2);
      } else {
         LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
         return null;
      }
   }
}
