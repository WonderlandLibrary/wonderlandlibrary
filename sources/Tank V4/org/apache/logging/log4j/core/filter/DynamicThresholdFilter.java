package org.apache.logging.log4j.core.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.Message;

@Plugin(
   name = "DynamicThresholdFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public final class DynamicThresholdFilter extends AbstractFilter {
   private Map levelMap = new HashMap();
   private Level defaultThreshold;
   private final String key;

   private DynamicThresholdFilter(String var1, Map var2, Level var3, Filter.Result var4, Filter.Result var5) {
      super(var4, var5);
      this.defaultThreshold = Level.ERROR;
      if (var1 == null) {
         throw new NullPointerException("key cannot be null");
      } else {
         this.key = var1;
         this.levelMap = var2;
         this.defaultThreshold = var3;
      }
   }

   public String getKey() {
      return this.key;
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      return this.filter(var2);
   }

   public Filter.Result filter(LogEvent var1) {
      return this.filter(var1.getLevel());
   }

   private Filter.Result filter(Level var1) {
      String var2 = ThreadContext.get(this.key);
      if (var2 != null) {
         Level var3 = (Level)this.levelMap.get(var2);
         if (var3 == null) {
            var3 = this.defaultThreshold;
         }

         return var1.isAtLeastAsSpecificAs(var3) ? this.onMatch : this.onMismatch;
      } else {
         return Filter.Result.NEUTRAL;
      }
   }

   public Map getLevelMap() {
      return this.levelMap;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("key=").append(this.key);
      var1.append(", default=").append(this.defaultThreshold);
      if (this.levelMap.size() > 0) {
         var1.append("{");
         boolean var2 = true;

         Entry var4;
         for(Iterator var3 = this.levelMap.entrySet().iterator(); var3.hasNext(); var1.append((String)var4.getKey()).append("=").append(var4.getValue())) {
            var4 = (Entry)var3.next();
            if (!var2) {
               var1.append(", ");
               var2 = false;
            }
         }

         var1.append("}");
      }

      return var1.toString();
   }

   @PluginFactory
   public static DynamicThresholdFilter createFilter(@PluginAttribute("key") String var0, @PluginElement("Pairs") KeyValuePair[] var1, @PluginAttribute("defaultThreshold") String var2, @PluginAttribute("onMatch") String var3, @PluginAttribute("onMismatch") String var4) {
      Filter.Result var5 = Filter.Result.toResult(var3);
      Filter.Result var6 = Filter.Result.toResult(var4);
      HashMap var7 = new HashMap();
      KeyValuePair[] var8 = var1;
      int var9 = var1.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         KeyValuePair var11 = var8[var10];
         var7.put(var11.getKey(), Level.toLevel(var11.getValue()));
      }

      Level var12 = Level.toLevel(var2, Level.ERROR);
      return new DynamicThresholdFilter(var0, var7, var12, var5, var6);
   }
}
