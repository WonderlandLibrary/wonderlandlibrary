package org.apache.logging.log4j.core.layout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.StructuredDataId;

@Plugin(
   name = "LoggerFields",
   category = "Core",
   printObject = true
)
public final class LoggerFields {
   private final Map map;
   private final String sdId;
   private final String enterpriseId;
   private final boolean discardIfAllFieldsAreEmpty;

   private LoggerFields(Map var1, String var2, String var3, boolean var4) {
      this.sdId = var2;
      this.enterpriseId = var3;
      this.map = Collections.unmodifiableMap(var1);
      this.discardIfAllFieldsAreEmpty = var4;
   }

   public Map getMap() {
      return this.map;
   }

   public String toString() {
      return this.map.toString();
   }

   @PluginFactory
   public static LoggerFields createLoggerFields(@PluginElement("LoggerFields") KeyValuePair[] var0, @PluginAttribute("sdId") String var1, @PluginAttribute("enterpriseId") String var2, @PluginAttribute("discardIfAllFieldsAreEmpty") String var3) {
      HashMap var4 = new HashMap();
      KeyValuePair[] var5 = var0;
      int var6 = var0.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         KeyValuePair var8 = var5[var7];
         var4.put(var8.getKey(), var8.getValue());
      }

      boolean var9 = Booleans.parseBoolean(var3, false);
      return new LoggerFields(var4, var1, var2, var9);
   }

   public StructuredDataId getSdId() {
      if (this.enterpriseId != null && this.sdId != null) {
         int var1 = Integer.parseInt(this.enterpriseId);
         return new StructuredDataId(this.sdId, var1, (String[])null, (String[])null);
      } else {
         return null;
      }
   }

   public boolean getDiscardIfAllFieldsAreEmpty() {
      return this.discardIfAllFieldsAreEmpty;
   }
}
