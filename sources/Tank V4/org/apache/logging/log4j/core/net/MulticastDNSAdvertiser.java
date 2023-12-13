package org.apache.logging.log4j.core.net;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "multicastdns",
   category = "Core",
   elementType = "advertiser",
   printObject = false
)
public class MulticastDNSAdvertiser implements Advertiser {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private static Object jmDNS = initializeJMDNS();
   private static Class jmDNSClass;
   private static Class serviceInfoClass;

   public Object advertise(Map var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (((String)var4.getKey()).length() <= 255 && ((String)var4.getValue()).length() <= 255) {
            var2.put(var4.getKey(), var4.getValue());
         }
      }

      String var15 = (String)var2.get("protocol");
      String var16 = "._log4j._" + (var15 != null ? var15 : "tcp") + ".local.";
      String var5 = (String)var2.get("port");
      int var6 = Integers.parseInt(var5, 4555);
      String var7 = (String)var2.get("name");
      if (jmDNS != null) {
         boolean var8 = false;

         try {
            jmDNSClass.getMethod("create", (Class[])null);
            var8 = true;
         } catch (NoSuchMethodException var14) {
         }

         Object var9;
         if (var8) {
            var9 = this.buildServiceInfoVersion3(var16, var6, var7, var2);
         } else {
            var9 = this.buildServiceInfoVersion1(var16, var6, var7, var2);
         }

         try {
            Method var10 = jmDNSClass.getMethod("registerService", serviceInfoClass);
            var10.invoke(jmDNS, var9);
         } catch (IllegalAccessException var11) {
            LOGGER.warn((String)"Unable to invoke registerService method", (Throwable)var11);
         } catch (NoSuchMethodException var12) {
            LOGGER.warn((String)"No registerService method", (Throwable)var12);
         } catch (InvocationTargetException var13) {
            LOGGER.warn((String)"Unable to invoke registerService method", (Throwable)var13);
         }

         return var9;
      } else {
         LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
         return null;
      }
   }

   public void unadvertise(Object var1) {
      if (jmDNS != null) {
         try {
            Method var2 = jmDNSClass.getMethod("unregisterService", serviceInfoClass);
            var2.invoke(jmDNS, var1);
         } catch (IllegalAccessException var3) {
            LOGGER.warn((String)"Unable to invoke unregisterService method", (Throwable)var3);
         } catch (NoSuchMethodException var4) {
            LOGGER.warn((String)"No unregisterService method", (Throwable)var4);
         } catch (InvocationTargetException var5) {
            LOGGER.warn((String)"Unable to invoke unregisterService method", (Throwable)var5);
         }
      }

   }

   private static Object createJmDNSVersion1() {
      try {
         return jmDNSClass.newInstance();
      } catch (InstantiationException var1) {
         LOGGER.warn((String)"Unable to instantiate JMDNS", (Throwable)var1);
      } catch (IllegalAccessException var2) {
         LOGGER.warn((String)"Unable to instantiate JMDNS", (Throwable)var2);
      }

      return null;
   }

   private static Object createJmDNSVersion3() {
      try {
         Method var0 = jmDNSClass.getMethod("create", (Class[])null);
         return var0.invoke((Object)null, (Object[])null);
      } catch (IllegalAccessException var1) {
         LOGGER.warn((String)"Unable to instantiate jmdns class", (Throwable)var1);
      } catch (NoSuchMethodException var2) {
         LOGGER.warn((String)"Unable to access constructor", (Throwable)var2);
      } catch (InvocationTargetException var3) {
         LOGGER.warn((String)"Unable to call constructor", (Throwable)var3);
      }

      return null;
   }

   private Object buildServiceInfoVersion1(String var1, int var2, String var3, Map var4) {
      Hashtable var5 = new Hashtable(var4);

      try {
         Class[] var6 = new Class[]{String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Hashtable.class};
         Constructor var7 = serviceInfoClass.getConstructor(var6);
         Object[] var8 = new Object[]{var1, var3, var2, 0, 0, var5};
         return var7.newInstance(var8);
      } catch (IllegalAccessException var9) {
         LOGGER.warn((String)"Unable to construct ServiceInfo instance", (Throwable)var9);
      } catch (NoSuchMethodException var10) {
         LOGGER.warn((String)"Unable to get ServiceInfo constructor", (Throwable)var10);
      } catch (InstantiationException var11) {
         LOGGER.warn((String)"Unable to construct ServiceInfo instance", (Throwable)var11);
      } catch (InvocationTargetException var12) {
         LOGGER.warn((String)"Unable to construct ServiceInfo instance", (Throwable)var12);
      }

      return null;
   }

   private Object buildServiceInfoVersion3(String var1, int var2, String var3, Map var4) {
      try {
         Class[] var5 = new Class[]{String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Map.class};
         Method var6 = serviceInfoClass.getMethod("create", var5);
         Object[] var7 = new Object[]{var1, var3, var2, 0, 0, var4};
         return var6.invoke((Object)null, var7);
      } catch (IllegalAccessException var8) {
         LOGGER.warn((String)"Unable to invoke create method", (Throwable)var8);
      } catch (NoSuchMethodException var9) {
         LOGGER.warn((String)"Unable to find create method", (Throwable)var9);
      } catch (InvocationTargetException var10) {
         LOGGER.warn((String)"Unable to invoke create method", (Throwable)var10);
      }

      return null;
   }

   private static Object initializeJMDNS() {
      try {
         jmDNSClass = Class.forName("javax.jmdns.JmDNS");
         serviceInfoClass = Class.forName("javax.jmdns.ServiceInfo");
         boolean var0 = false;

         try {
            jmDNSClass.getMethod("create", (Class[])null);
            var0 = true;
         } catch (NoSuchMethodException var2) {
         }

         if (var0) {
            return createJmDNSVersion3();
         }

         return createJmDNSVersion1();
      } catch (ClassNotFoundException var3) {
         LOGGER.warn((String)"JmDNS or serviceInfo class not found", (Throwable)var3);
      } catch (ExceptionInInitializerError var4) {
         LOGGER.warn((String)"JmDNS or serviceInfo class not found", (Throwable)var4);
      }

      return null;
   }
}
