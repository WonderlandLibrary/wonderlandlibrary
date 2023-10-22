package com.sun.scenario;

import com.sun.javafx.tk.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.util.Callback;

public class Settings {
   private final Map settings = new HashMap(5);
   private final CopyOnWriteArrayList listeners = new CopyOnWriteArrayList();
   private static final Object SETTINGS_KEY = new StringBuilder("SettingsKey");

   private static synchronized Settings getInstance() {
      Map var0 = Toolkit.getToolkit().getContextMap();
      Settings var1 = (Settings)var0.get(SETTINGS_KEY);
      if (var1 == null) {
         var1 = new Settings();
         var0.put(SETTINGS_KEY, var1);
      }

      return var1;
   }

   public static void set(String var0, String var1) {
      getInstance().setImpl(var0, var1);
   }

   private void setImpl(String var1, String var2) {
      this.checkKeyArg(var1);
      this.settings.put(var1, var2);
      Iterator var3 = this.listeners.iterator();

      while(var3.hasNext()) {
         Callback var4 = (Callback)var3.next();
         var4.call(var1);
      }

   }

   public static String get(String var0) {
      return getInstance().getImpl(var0);
   }

   private String getImpl(String var1) {
      this.checkKeyArg(var1);
      String var2 = (String)this.settings.get(var1);
      if (var2 == null) {
         try {
            var2 = System.getProperty(var1);
         } catch (SecurityException var4) {
         }
      }

      return var2;
   }

   public static boolean getBoolean(String var0) {
      return getInstance().getBooleanImpl(var0);
   }

   private boolean getBooleanImpl(String var1) {
      String var2 = this.getImpl(var1);
      return "true".equals(var2);
   }

   public static boolean getBoolean(String var0, boolean var1) {
      return getInstance().getBooleanImpl(var0, var1);
   }

   private boolean getBooleanImpl(String var1, boolean var2) {
      String var3 = this.getImpl(var1);
      boolean var4 = var2;
      if (var3 != null) {
         if ("false".equals(var3)) {
            var4 = false;
         } else if ("true".equals(var3)) {
            var4 = true;
         }
      }

      return var4;
   }

   public static int getInt(String var0, int var1) {
      return getInstance().getIntImpl(var0, var1);
   }

   private int getIntImpl(String var1, int var2) {
      String var3 = this.getImpl(var1);
      int var4 = var2;

      try {
         var4 = Integer.parseInt(var3);
      } catch (NumberFormatException var6) {
      }

      return var4;
   }

   public static void addPropertyChangeListener(Callback var0) {
      getInstance().addPropertyChangeListenerImpl(var0);
   }

   private void addPropertyChangeListenerImpl(Callback var1) {
      this.listeners.add(var1);
   }

   public static void removePropertyChangeListener(Callback var0) {
      getInstance().removePropertyChangeListenerImpl(var0);
   }

   private void removePropertyChangeListenerImpl(Callback var1) {
      this.listeners.remove(var1);
   }

   private void checkKeyArg(String var1) {
      if (null == var1 || "".equals(var1)) {
         throw new IllegalArgumentException("null key not allowed");
      }
   }

   private Settings() {
   }
}
