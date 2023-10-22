package com.sun.webkit.plugin;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PluginManager {
   private static final Logger log = Logger.getLogger("com.sun.browser.plugin.PluginManager");
   private static final ServiceLoader pHandlers = ServiceLoader.load(PluginHandler.class);
   private static final TreeMap hndMap = new TreeMap();
   private static PluginHandler[] hndArray;
   private static final HashSet disabledPluginHandlers = new HashSet();

   private static void updatePluginHandlers() {
      log.fine("Update plugin handlers");
      hndMap.clear();
      Iterator var0 = pHandlers.iterator();

      while(true) {
         PluginHandler var1;
         do {
            do {
               if (!var0.hasNext()) {
                  Collection var7 = hndMap.values();
                  hndArray = (PluginHandler[])var7.toArray(new PluginHandler[var7.size()]);
                  return;
               }

               var1 = (PluginHandler)var0.next();
            } while(!var1.isSupportedPlatform());
         } while(isDisabledPlugin(var1));

         String[] var2 = var1.supportedMIMETypes();
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            hndMap.put(var6, var1);
            log.fine(var6);
         }
      }
   }

   public static Plugin createPlugin(URL var0, String var1, String[] var2, String[] var3) {
      try {
         PluginHandler var4 = (PluginHandler)hndMap.get(var1);
         if (var4 == null) {
            return new DefaultPlugin(var0, var1, var2, var3);
         } else {
            Plugin var5 = var4.createPlugin(var0, var1, var2, var3);
            return (Plugin)(var5 == null ? new DefaultPlugin(var0, var1, var2, var3) : var5);
         }
      } catch (Throwable var6) {
         log.log(Level.FINE, "Cannot create plugin", var6);
         return new DefaultPlugin(var0, var1, var2, var3);
      }
   }

   private static List getAvailablePlugins() {
      Vector var0 = new Vector();
      Iterator var1 = pHandlers.iterator();

      while(var1.hasNext()) {
         PluginHandler var2 = (PluginHandler)var1.next();
         if (var2.isSupportedPlatform()) {
            var0.add(var2);
         }
      }

      return var0;
   }

   private static PluginHandler getEnabledPlugin(int var0) {
      return var0 >= 0 && var0 < hndArray.length ? hndArray[var0] : null;
   }

   private static int getEnabledPluginCount() {
      return hndArray.length;
   }

   private static void disablePlugin(PluginHandler var0) {
      disabledPluginHandlers.add(var0.getClass().getCanonicalName());
      updatePluginHandlers();
   }

   private static void enablePlugin(PluginHandler var0) {
      disabledPluginHandlers.remove(var0.getClass().getCanonicalName());
      updatePluginHandlers();
   }

   private static boolean isDisabledPlugin(PluginHandler var0) {
      return disabledPluginHandlers.contains(var0.getClass().getCanonicalName());
   }

   private static boolean supportsMIMEType(String var0) {
      return hndMap.containsKey(var0);
   }

   private static String getPluginNameForMIMEType(String var0) {
      PluginHandler var1 = (PluginHandler)hndMap.get(var0);
      return var1 != null ? var1.getName() : "";
   }

   static {
      if ("false".equalsIgnoreCase(System.getProperty("com.sun.browser.plugin"))) {
         Iterator var0 = getAvailablePlugins().iterator();

         while(var0.hasNext()) {
            PluginHandler var1 = (PluginHandler)var0.next();
            disabledPluginHandlers.add(var1.getClass().getCanonicalName());
         }
      }

      updatePluginHandlers();
   }
}
