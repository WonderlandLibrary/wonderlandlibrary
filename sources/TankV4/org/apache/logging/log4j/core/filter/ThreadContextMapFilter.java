package org.apache.logging.log4j.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
   name = "ThreadContextMapFilter",
   category = "Core",
   elementType = "filter",
   printObject = true
)
public class ThreadContextMapFilter extends MapFilter {
   private final String key;
   private final String value;
   private final boolean useMap;

   public ThreadContextMapFilter(Map var1, boolean var2, Filter.Result var3, Filter.Result var4) {
      super(var1, var2, var3, var4);
      if (var1.size() == 1) {
         Iterator var5 = var1.entrySet().iterator();
         Entry var6 = (Entry)var5.next();
         if (((List)var6.getValue()).size() == 1) {
            this.key = (String)var6.getKey();
            this.value = (String)((List)var6.getValue()).get(0);
            this.useMap = false;
         } else {
            this.key = null;
            this.value = null;
            this.useMap = true;
         }
      } else {
         this.key = null;
         this.value = null;
         this.useMap = true;
      }

   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, String var4, Object... var5) {
      return this.filter();
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5) {
      return this.filter();
   }

   public Filter.Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5) {
      return this.filter();
   }

   private Filter.Result filter() {
      boolean var1 = false;
      if (this.useMap) {
         Iterator var2 = this.getMap().entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            String var4 = ThreadContext.get((String)var3.getKey());
            if (var4 != null) {
               var1 = ((List)var3.getValue()).contains(var4);
            } else {
               var1 = false;
            }

            if (!this.isAnd() && var1 || this.isAnd() && !var1) {
               break;
            }
         }
      } else {
         var1 = this.value.equals(ThreadContext.get(this.key));
      }

      return var1 ? this.onMatch : this.onMismatch;
   }

   public Filter.Result filter(LogEvent var1) {
      return super.filter(var1.getContextMap()) ? this.onMatch : this.onMismatch;
   }

   @PluginFactory
   public static ThreadContextMapFilter createFilter(@PluginElement("Pairs") KeyValuePair[] var0, @PluginAttribute("operator") String var1, @PluginAttribute("onMatch") String var2, @PluginAttribute("onMismatch") String var3) {
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
            LOGGER.error("ThreadContextMapFilter is not configured with any valid key value pairs");
            return null;
         } else {
            boolean var13 = var1 == null || !var1.equalsIgnoreCase("or");
            Filter.Result var14 = Filter.Result.toResult(var2);
            Filter.Result var15 = Filter.Result.toResult(var3);
            return new ThreadContextMapFilter(var4, var13, var14, var15);
         }
      } else {
         LOGGER.error("key and value pairs must be specified for the ThreadContextMapFilter");
         return null;
      }
   }
}
