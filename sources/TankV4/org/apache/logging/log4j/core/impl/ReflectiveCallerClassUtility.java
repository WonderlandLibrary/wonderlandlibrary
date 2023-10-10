package org.apache.logging.log4j.core.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;

public final class ReflectiveCallerClassUtility {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final boolean GET_CALLER_CLASS_SUPPORTED;
   private static final Method GET_CALLER_CLASS_METHOD;
   static final int JAVA_7U25_COMPENSATION_OFFSET;

   private ReflectiveCallerClassUtility() {
   }

   public static boolean isSupported() {
      return GET_CALLER_CLASS_SUPPORTED;
   }

   public static Class getCaller(int var0) {
      if (!GET_CALLER_CLASS_SUPPORTED) {
         return null;
      } else {
         try {
            return (Class)GET_CALLER_CLASS_METHOD.invoke((Object)null, var0 + 1 + JAVA_7U25_COMPENSATION_OFFSET);
         } catch (IllegalAccessException var2) {
            LOGGER.warn("Should not have failed to call getCallerClass.");
         } catch (InvocationTargetException var3) {
            LOGGER.warn("Should not have failed to call getCallerClass.");
         }

         return null;
      }
   }

   static {
      Method var0 = null;
      byte var1 = 0;

      try {
         ClassLoader var2 = Loader.getClassLoader();
         Class var3 = var2.loadClass("sun.reflect.Reflection");
         Method[] var4 = var3.getMethods();
         Method[] var5 = var4;
         int var6 = var4.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method var8 = var5[var7];
            int var9 = var8.getModifiers();
            Class[] var10 = var8.getParameterTypes();
            if (var8.getName().equals("getCallerClass") && Modifier.isStatic(var9) && var10.length == 1 && var10[0] == Integer.TYPE) {
               var0 = var8;
               break;
            }
         }

         if (var0 == null) {
            LOGGER.info("sun.reflect.Reflection#getCallerClass does not exist.");
         } else {
            Object var14 = var0.invoke((Object)null, 0);
            if (var14 != null && var14 == var3) {
               var14 = var0.invoke((Object)null, 1);
               if (var14 == var3) {
                  var1 = 1;
                  LOGGER.warn("sun.reflect.Reflection#getCallerClass is broken in Java 7u25. You should upgrade to 7u40. Using alternate stack offset to compensate.");
               }
            } else {
               var0 = null;
               LOGGER.warn("sun.reflect.Reflection#getCallerClass returned unexpected value of [{}] and is unusable. Will fall back to another option.", var14);
            }
         }
      } catch (ClassNotFoundException var11) {
         LOGGER.info("sun.reflect.Reflection is not installed.");
      } catch (IllegalAccessException var12) {
         LOGGER.info("sun.reflect.Reflection#getCallerClass is not accessible.");
      } catch (InvocationTargetException var13) {
         LOGGER.info("sun.reflect.Reflection#getCallerClass is not supported.");
      }

      if (var0 == null) {
         GET_CALLER_CLASS_SUPPORTED = false;
         GET_CALLER_CLASS_METHOD = null;
         JAVA_7U25_COMPENSATION_OFFSET = -1;
      } else {
         GET_CALLER_CLASS_SUPPORTED = true;
         GET_CALLER_CLASS_METHOD = var0;
         JAVA_7U25_COMPENSATION_OFFSET = var1;
      }

   }
}
