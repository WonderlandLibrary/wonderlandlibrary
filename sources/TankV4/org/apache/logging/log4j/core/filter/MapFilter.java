package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;

@Plugin(
   name = "MapFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public class MapFilter extends AbstractFilter {
   private final Map map;
   private final boolean isAnd;

   protected MapFilter(Map var1, boolean var2, Filter.Result var3, Filter.Result var4) {
      super(var3, var4);
      if (var1 == null) {
         throw new NullPointerException("key cannot be null");
      } else {
         this.isAnd = var2;
         this.map = var1;
      }
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      if (var4 instanceof MapMessage) {
         return ((MapMessage)var4).getData() != false ? this.onMatch : this.onMismatch;
      } else {
         return Filter.Result.NEUTRAL;
      }
   }

   public Filter.Result filter(LogEvent var1) {
      Message var2 = var1.getMessage();
      if (var2 instanceof MapMessage) {
         return ((MapMessage)var2).getData() != false ? this.onMatch : this.onMismatch;
      } else {
         return Filter.Result.NEUTRAL;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("isAnd=").append(this.isAnd);
      if (this.map.size() > 0) {
         var1.append(", {");
         boolean var2 = true;
         Iterator var3 = this.map.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (!var2) {
               var1.append(", ");
            }

            var2 = false;
            List var5 = (List)var4.getValue();
            String var6 = var5.size() > 1 ? (String)var5.get(0) : var5.toString();
            var1.append((String)var4.getKey()).append("=").append(var6);
         }

         var1.append("}");
      }

      return var1.toString();
   }

   protected boolean isAnd() {
      return this.isAnd;
   }

   protected Map getMap() {
      return this.map;
   }

   @PluginFactory
   public static MapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] var0, @PluginAttribute("operator") String var1, @PluginAttribute("onMatch") String var2, @PluginAttribute("onMismatch") String var3) {
      if (var0 != null && var0.length != 0) {
         HashMap var4 = new HashMap();
         KeyValuePair[] var5 = var0;
         int var6 = var0.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            KeyValuePair var8 = var5[var7];
            String var9 = var8.getKey();
            if (var9 == null) {
               LOGGER.error("A null key is not valid in MapFilter");
            } else {
               String var10 = var8.getValue();
               if (var10 == null) {
                  LOGGER.error("A null value for key " + var9 + " is not allowed in MapFilter");
               } else {
                  List var11 = (List)var4.get(var8.getKey());
                  if (var11 != null) {
                     var11.add(var10);
                  } else {
                     ArrayList var12 = new ArrayList();
                     var12.add(var10);
                     var4.put(var8.getKey(), var12);
                  }
               }
            }
         }

         if (var4.size() == 0) {
            LOGGER.error("MapFilter is not configured with any valid key value pairs");
            return null;
         } else {
            boolean var13 = var1 == null || !var1.equalsIgnoreCase("or");
            Filter.Result var14 = Filter.Result.toResult(var2);
            Filter.Result var15 = Filter.Result.toResult(var3);
            return new MapFilter(var4, var13, var14, var15);
         }
      } else {
         LOGGER.error("keys and values must be specified for the MapFilter");
         return null;
      }
   }
}
