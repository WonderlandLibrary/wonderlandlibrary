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
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(
   name = "StructuredDataFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public final class StructuredDataFilter extends MapFilter {
   private StructuredDataFilter(Map var1, boolean var2, Filter.Result var3, Filter.Result var4) {
      super(var1, var2, var3, var4);
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      return var4 instanceof StructuredDataMessage ? this.filter((StructuredDataMessage)var4) : Filter.Result.NEUTRAL;
   }

   public Filter.Result filter(LogEvent var1) {
      Message var2 = var1.getMessage();
      return var2 instanceof StructuredDataMessage ? this.filter((StructuredDataMessage)var2) : super.filter(var1);
   }

   protected Filter.Result filter(StructuredDataMessage var1) {
      boolean var2 = false;
      Iterator var3 = this.getMap().entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         String var5 = this.getValue(var1, (String)var4.getKey());
         if (var5 != null) {
            var2 = ((List)var4.getValue()).contains(var5);
         } else {
            var2 = false;
         }

         if (!this.isAnd() && var2 || this.isAnd() && !var2) {
            break;
         }
      }

      return var2 ? this.onMatch : this.onMismatch;
   }

   private String getValue(StructuredDataMessage var1, String var2) {
      if (var2.equalsIgnoreCase("id")) {
         return var1.getId().toString();
      } else if (var2.equalsIgnoreCase("id.name")) {
         return var1.getId().getName();
      } else if (var2.equalsIgnoreCase("type")) {
         return var1.getType();
      } else {
         return var2.equalsIgnoreCase("message") ? var1.getFormattedMessage() : (String)var1.getData().get(var2);
      }
   }

   @PluginFactory
   public static StructuredDataFilter createFilter(@PluginElement("Pairs") KeyValuePair[] var0, @PluginAttribute("operator") String var1, @PluginAttribute("onMatch") String var2, @PluginAttribute("onMismatch") String var3) {
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
            LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
            return null;
         } else {
            boolean var13 = var1 == null || !var1.equalsIgnoreCase("or");
            Filter.Result var14 = Filter.Result.toResult(var2);
            Filter.Result var15 = Filter.Result.toResult(var3);
            return new StructuredDataFilter(var4, var13, var14, var15);
         }
      } else {
         LOGGER.error("keys and values must be specified for the StructuredDataFilter");
         return null;
      }
   }
}
