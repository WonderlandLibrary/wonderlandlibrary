package org.apache.logging.log4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.spi.MutableThreadContextStack;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextStack;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.ProviderUtil;

public final class ThreadContext {
   public static final Map EMPTY_MAP = Collections.emptyMap();
   public static final ThreadContextStack EMPTY_STACK = new MutableThreadContextStack(new ArrayList());
   private static final String DISABLE_MAP = "disableThreadContextMap";
   private static final String DISABLE_STACK = "disableThreadContextStack";
   private static final String DISABLE_ALL = "disableThreadContext";
   private static final String THREAD_CONTEXT_KEY = "log4j2.threadContextMap";
   private static boolean all;
   private static boolean useMap;
   private static boolean useStack;
   private static ThreadContextMap contextMap;
   private static ThreadContextStack contextStack;
   private static final Logger LOGGER = StatusLogger.getLogger();

   private ThreadContext() {
   }

   public static void put(String var0, String var1) {
      contextMap.put(var0, var1);
   }

   public static String get(String var0) {
      return contextMap.get(var0);
   }

   public static void remove(String var0) {
      contextMap.remove(var0);
   }

   public static void clear() {
      contextMap.clear();
   }

   public static boolean containsKey(String var0) {
      return contextMap.containsKey(var0);
   }

   public static Map getContext() {
      return contextMap.getCopy();
   }

   public static Map getImmutableContext() {
      Map var0 = contextMap.getImmutableMapOrNull();
      return var0 == null ? EMPTY_MAP : var0;
   }

   public static boolean isEmpty() {
      return contextMap.isEmpty();
   }

   public static void clearStack() {
      contextStack.clear();
   }

   public static ThreadContext.ContextStack cloneStack() {
      return contextStack.copy();
   }

   public static ThreadContext.ContextStack getImmutableStack() {
      return contextStack;
   }

   public static void setStack(Collection var0) {
      if (var0.size() != 0 && useStack) {
         contextStack.clear();
         contextStack.addAll(var0);
      }
   }

   public static int getDepth() {
      return contextStack.getDepth();
   }

   public static String pop() {
      return contextStack.pop();
   }

   public static String peek() {
      return contextStack.peek();
   }

   public static void push(String var0) {
      contextStack.push(var0);
   }

   public static void push(String var0, Object... var1) {
      contextStack.push(ParameterizedMessage.format(var0, var1));
   }

   public static void removeStack() {
      contextStack.clear();
   }

   public static void trim(int var0) {
      contextStack.trim(var0);
   }

   static {
      PropertiesUtil var0 = PropertiesUtil.getProperties();
      all = var0.getBooleanProperty("disableThreadContext");
      useStack = !var0.getBooleanProperty("disableThreadContextStack") && !all;
      contextStack = new DefaultThreadContextStack(useStack);
      useMap = !var0.getBooleanProperty("disableThreadContextMap") && !all;
      String var1 = var0.getStringProperty("log4j2.threadContextMap");
      ClassLoader var2 = ProviderUtil.findClassLoader();
      if (var1 != null) {
         try {
            Class var3 = var2.loadClass(var1);
            if (ThreadContextMap.class.isAssignableFrom(var3)) {
               contextMap = (ThreadContextMap)var3.newInstance();
            }
         } catch (ClassNotFoundException var8) {
            LOGGER.error("Unable to locate configured LoggerContextFactory {}", var1);
         } catch (Exception var9) {
            LOGGER.error("Unable to create configured LoggerContextFactory {}", var1, var9);
         }
      }

      if (contextMap == null && ProviderUtil.hasProviders()) {
         LoggerContextFactory var12 = LogManager.getFactory();
         Iterator var4 = ProviderUtil.getProviders();

         while(var4.hasNext()) {
            Provider var5 = (Provider)var4.next();
            var1 = var5.getThreadContextMap();
            String var6 = var5.getClassName();
            if (var1 != null && var12.getClass().getName().equals(var6)) {
               try {
                  Class var7 = var2.loadClass(var1);
                  if (ThreadContextMap.class.isAssignableFrom(var7)) {
                     contextMap = (ThreadContextMap)var7.newInstance();
                     break;
                  }
               } catch (ClassNotFoundException var10) {
                  LOGGER.error("Unable to locate configured LoggerContextFactory {}", var1);
                  contextMap = new DefaultThreadContextMap(useMap);
               } catch (Exception var11) {
                  LOGGER.error("Unable to create configured LoggerContextFactory {}", var1, var11);
                  contextMap = new DefaultThreadContextMap(useMap);
               }
            }
         }
      }

      if (contextMap == null) {
         contextMap = new DefaultThreadContextMap(useMap);
      }

   }

   public interface ContextStack extends Serializable {
      void clear();

      String pop();

      String peek();

      void push(String var1);

      int getDepth();

      List asList();

      void trim(int var1);

      ThreadContext.ContextStack copy();
   }
}
