package org.apache.logging.log4j.core.selector;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.impl.ReflectiveCallerClassUtility;
import org.apache.logging.log4j.status.StatusLogger;

public class ClassLoaderContextSelector implements ContextSelector {
   private static final AtomicReference CONTEXT = new AtomicReference();
   private static final ClassLoaderContextSelector.PrivateSecurityManager SECURITY_MANAGER;
   private static final StatusLogger LOGGER = StatusLogger.getLogger();
   private static final ConcurrentMap CONTEXT_MAP = new ConcurrentHashMap();

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3) {
      return this.getContext(var1, var2, var3, (URI)null);
   }

   public LoggerContext getContext(String var1, ClassLoader var2, boolean var3, URI var4) {
      if (var3) {
         LoggerContext var15 = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
         return var15 != null ? var15 : this.getDefault();
      } else if (var2 != null) {
         return this.locateContext(var2, var4);
      } else {
         Class var5;
         boolean var6;
         if (ReflectiveCallerClassUtility.isSupported()) {
            try {
               var5 = Class.class;
               var6 = false;

               for(int var7 = 2; var5 != null; ++var7) {
                  var5 = ReflectiveCallerClassUtility.getCaller(var7);
                  if (var5 == null) {
                     break;
                  }

                  if (var5.getName().equals(var1)) {
                     var6 = true;
                  } else if (var6) {
                     break;
                  }
               }

               if (var5 != null) {
                  return this.locateContext(var5.getClassLoader(), var4);
               }
            } catch (Exception var13) {
            }
         }

         if (SECURITY_MANAGER != null) {
            var5 = SECURITY_MANAGER.getCaller(var1);
            if (var5 != null) {
               ClassLoader var16 = var5.getClassLoader() != null ? var5.getClassLoader() : ClassLoader.getSystemClassLoader();
               return this.locateContext(var16, var4);
            }
         }

         Throwable var14 = new Throwable();
         var6 = false;
         String var17 = null;
         StackTraceElement[] var8 = var14.getStackTrace();
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            StackTraceElement var11 = var8[var10];
            if (var11.getClassName().equals(var1)) {
               var6 = true;
            } else if (var6) {
               var17 = var11.getClassName();
               break;
            }
         }

         if (var17 != null) {
            try {
               return this.locateContext(Loader.loadClass(var17).getClassLoader(), var4);
            } catch (ClassNotFoundException var12) {
            }
         }

         LoggerContext var18 = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
         return var18 != null ? var18 : this.getDefault();
      }
   }

   public void removeContext(LoggerContext var1) {
      Iterator var2 = CONTEXT_MAP.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         LoggerContext var4 = (LoggerContext)((WeakReference)((AtomicReference)var3.getValue()).get()).get();
         if (var4 == var1) {
            CONTEXT_MAP.remove(var3.getKey());
         }
      }

   }

   public List getLoggerContexts() {
      ArrayList var1 = new ArrayList();
      Collection var2 = CONTEXT_MAP.values();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         AtomicReference var4 = (AtomicReference)var3.next();
         LoggerContext var5 = (LoggerContext)((WeakReference)var4.get()).get();
         if (var5 != null) {
            var1.add(var5);
         }
      }

      return Collections.unmodifiableList(var1);
   }

   private LoggerContext locateContext(ClassLoader var1, URI var2) {
      String var3 = var1.toString();
      AtomicReference var4 = (AtomicReference)CONTEXT_MAP.get(var3);
      if (var4 == null) {
         if (var2 == null) {
            for(ClassLoader var8 = var1.getParent(); var8 != null; var8 = var8.getParent()) {
               var4 = (AtomicReference)CONTEXT_MAP.get(var8.toString());
               if (var4 != null) {
                  WeakReference var10 = (WeakReference)var4.get();
                  LoggerContext var7 = (LoggerContext)var10.get();
                  if (var7 != null) {
                     return var7;
                  }
               }
            }
         }

         LoggerContext var9 = new LoggerContext(var3, (Object)null, var2);
         AtomicReference var11 = new AtomicReference();
         var11.set(new WeakReference(var9));
         CONTEXT_MAP.putIfAbsent(var1.toString(), var11);
         var9 = (LoggerContext)((WeakReference)((AtomicReference)CONTEXT_MAP.get(var3)).get()).get();
         return var9;
      } else {
         WeakReference var5 = (WeakReference)var4.get();
         LoggerContext var6 = (LoggerContext)var5.get();
         if (var6 == null) {
            var6 = new LoggerContext(var3, (Object)null, var2);
            var4.compareAndSet(var5, new WeakReference(var6));
            return var6;
         } else {
            if (var6.getConfigLocation() == null && var2 != null) {
               LOGGER.debug("Setting configuration to {}", new Object[]{var2});
               var6.setConfigLocation(var2);
            } else if (var6.getConfigLocation() != null && var2 != null && !var6.getConfigLocation().equals(var2)) {
               LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", new Object[]{var2, var6.getConfigLocation()});
            }

            return var6;
         }
      }
   }

   private LoggerContext getDefault() {
      LoggerContext var1 = (LoggerContext)CONTEXT.get();
      if (var1 != null) {
         return var1;
      } else {
         CONTEXT.compareAndSet((Object)null, new LoggerContext("Default"));
         return (LoggerContext)CONTEXT.get();
      }
   }

   static {
      if (ReflectiveCallerClassUtility.isSupported()) {
         SECURITY_MANAGER = null;
      } else {
         ClassLoaderContextSelector.PrivateSecurityManager var0;
         try {
            var0 = new ClassLoaderContextSelector.PrivateSecurityManager();
            if (var0.getCaller(ClassLoaderContextSelector.class.getName()) == null) {
               var0 = null;
               LOGGER.error("Unable to obtain call stack from security manager.");
            }
         } catch (Exception var2) {
            var0 = null;
            LOGGER.debug("Unable to install security manager", var2);
         }

         SECURITY_MANAGER = var0;
      }

   }

   private static class PrivateSecurityManager extends SecurityManager {
      private PrivateSecurityManager() {
      }

      public Class getCaller(String var1) {
         Class[] var2 = this.getClassContext();
         boolean var3 = false;
         Class[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class var7 = var4[var6];
            if (var7.getName().equals(var1)) {
               var3 = true;
            } else if (var3) {
               return var7;
            }
         }

         return null;
      }

      PrivateSecurityManager(Object var1) {
         this();
      }
   }
}
