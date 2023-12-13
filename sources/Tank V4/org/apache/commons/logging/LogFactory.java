package org.apache.commons.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class LogFactory {
   public static final String PRIORITY_KEY = "priority";
   public static final String TCCL_KEY = "use_tccl";
   public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
   public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
   public static final String FACTORY_PROPERTIES = "commons-logging.properties";
   protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
   public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
   private static PrintStream diagnosticsStream;
   private static final String diagnosticPrefix;
   public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
   private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
   private static final ClassLoader thisClassLoader;
   protected static Hashtable factories;
   /** @deprecated */
   protected static volatile LogFactory nullClassLoaderFactory;
   static Class class$java$lang$Thread;
   static Class class$org$apache$commons$logging$LogFactory;

   protected LogFactory() {
   }

   public abstract Object getAttribute(String var1);

   public abstract String[] getAttributeNames();

   public abstract Log getInstance(Class var1) throws LogConfigurationException;

   public abstract Log getInstance(String var1) throws LogConfigurationException;

   public abstract void release();

   public abstract void removeAttribute(String var1);

   public abstract void setAttribute(String var1, Object var2);

   private static final Hashtable createFactoryStore() {
      // $FF: Couldn't be decompiled
   }

   private static String trim(String var0) {
      return var0 == null ? null : var0.trim();
   }

   protected static void handleThrowable(Throwable var0) {
      if (var0 instanceof ThreadDeath) {
         throw (ThreadDeath)var0;
      } else if (var0 instanceof VirtualMachineError) {
         throw (VirtualMachineError)var0;
      }
   }

   public static LogFactory getFactory() throws LogConfigurationException {
      // $FF: Couldn't be decompiled
   }

   public static Log getLog(Class var0) throws LogConfigurationException {
      return getFactory().getInstance(var0);
   }

   public static Log getLog(String var0) throws LogConfigurationException {
      return getFactory().getInstance(var0);
   }

   public static void release(ClassLoader param0) {
      // $FF: Couldn't be decompiled
   }

   public static void releaseAll() {
      // $FF: Couldn't be decompiled
   }

   protected static ClassLoader getClassLoader(Class param0) {
      // $FF: Couldn't be decompiled
   }

   protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
      return directGetContextClassLoader();
   }

   private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
      return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return LogFactory.directGetContextClassLoader();
         }
      });
   }

   protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
      ClassLoader var0 = null;

      try {
         Method var1 = (class$java$lang$Thread == null ? (class$java$lang$Thread = class$("java.lang.Thread")) : class$java$lang$Thread).getMethod("getContextClassLoader", (Class[])null);

         try {
            var0 = (ClassLoader)var1.invoke(Thread.currentThread(), (Object[])null);
         } catch (IllegalAccessException var3) {
            throw new LogConfigurationException("Unexpected IllegalAccessException", var3);
         } catch (InvocationTargetException var4) {
            if (!(var4.getTargetException() instanceof SecurityException)) {
               throw new LogConfigurationException("Unexpected InvocationTargetException", var4.getTargetException());
            }
         }
      } catch (NoSuchMethodException var5) {
         var0 = getClassLoader(class$org$apache$commons$logging$LogFactory == null ? (class$org$apache$commons$logging$LogFactory = class$("org.apache.commons.logging.LogFactory")) : class$org$apache$commons$logging$LogFactory);
      }

      return var0;
   }

   private static LogFactory getCachedFactory(ClassLoader var0) {
      return var0 == null ? nullClassLoaderFactory : (LogFactory)factories.get(var0);
   }

   private static void cacheFactory(ClassLoader var0, LogFactory var1) {
      if (var1 != null) {
         if (var0 == null) {
            nullClassLoaderFactory = var1;
         } else {
            factories.put(var0, var1);
         }
      }

   }

   protected static LogFactory newFactory(String param0, ClassLoader param1, ClassLoader param2) throws LogConfigurationException {
      // $FF: Couldn't be decompiled
   }

   protected static LogFactory newFactory(String var0, ClassLoader var1) {
      return newFactory(var0, var1, (ClassLoader)null);
   }

   protected static Object createFactory(String param0, ClassLoader param1) {
      // $FF: Couldn't be decompiled
   }

   private static boolean implementsLogFactory(Class var0) {
      boolean var1 = false;
      if (var0 != null) {
         try {
            ClassLoader var2 = var0.getClassLoader();
            if (var2 == null) {
               logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
            } else {
               logHierarchy("[CUSTOM LOG FACTORY] ", var2);
               Class var3 = Class.forName("org.apache.commons.logging.LogFactory", false, var2);
               var1 = var3.isAssignableFrom(var0);
               if (var1) {
                  logDiagnostic("[CUSTOM LOG FACTORY] " + var0.getName() + " implements LogFactory but was loaded by an incompatible classloader.");
               } else {
                  logDiagnostic("[CUSTOM LOG FACTORY] " + var0.getName() + " does not implement LogFactory.");
               }
            }
         } catch (SecurityException var4) {
            logDiagnostic("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + var4.getMessage());
         } catch (LinkageError var5) {
            logDiagnostic("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: " + var5.getMessage());
         } catch (ClassNotFoundException var6) {
            logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
         }
      }

      return var1;
   }

   private static InputStream getResourceAsStream(ClassLoader var0, String var1) {
      return (InputStream)AccessController.doPrivileged(new PrivilegedAction(var0, var1) {
         private final ClassLoader val$loader;
         private final String val$name;

         {
            this.val$loader = var1;
            this.val$name = var2;
         }

         public Object run() {
            return this.val$loader != null ? this.val$loader.getResourceAsStream(this.val$name) : ClassLoader.getSystemResourceAsStream(this.val$name);
         }
      });
   }

   private static Enumeration getResources(ClassLoader var0, String var1) {
      PrivilegedAction var2 = new PrivilegedAction(var0, var1) {
         private final ClassLoader val$loader;
         private final String val$name;

         {
            this.val$loader = var1;
            this.val$name = var2;
         }

         public Object run() {
            try {
               return this.val$loader != null ? this.val$loader.getResources(this.val$name) : ClassLoader.getSystemResources(this.val$name);
            } catch (IOException var2) {
               if (LogFactory.isDiagnosticsEnabled()) {
                  LogFactory.access$000("Exception while trying to find configuration file " + this.val$name + ":" + var2.getMessage());
               }

               return null;
            } catch (NoSuchMethodError var3) {
               return null;
            }
         }
      };
      Object var3 = AccessController.doPrivileged(var2);
      return (Enumeration)var3;
   }

   private static Properties getProperties(URL var0) {
      PrivilegedAction var1 = new PrivilegedAction(var0) {
         private final URL val$url;

         {
            this.val$url = var1;
         }

         public Object run() {
            InputStream var1 = null;

            Properties var4;
            label82: {
               try {
                  URLConnection var2 = this.val$url.openConnection();
                  var2.setUseCaches(false);
                  var1 = var2.getInputStream();
                  if (var1 != null) {
                     Properties var3 = new Properties();
                     var3.load(var1);
                     var1.close();
                     var1 = null;
                     var4 = var3;
                     break label82;
                  }
               } catch (IOException var11) {
                  if (LogFactory.isDiagnosticsEnabled()) {
                     LogFactory.access$000("Unable to read URL " + this.val$url);
                  }

                  if (var1 != null) {
                     try {
                        var1.close();
                     } catch (IOException var9) {
                        if (LogFactory.isDiagnosticsEnabled()) {
                           LogFactory.access$000("Unable to close stream for URL " + this.val$url);
                           return null;
                        }
                     }

                     return null;
                  }

                  return null;
               }

               if (var1 != null) {
                  try {
                     var1.close();
                  } catch (IOException var10) {
                     if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.access$000("Unable to close stream for URL " + this.val$url);
                     }
                  }
               }

               return null;
            }

            if (var1 != null) {
               try {
                  var1.close();
               } catch (IOException var8) {
                  if (LogFactory.isDiagnosticsEnabled()) {
                     LogFactory.access$000("Unable to close stream for URL " + this.val$url);
                  }
               }
            }

            return var4;
         }
      };
      return (Properties)AccessController.doPrivileged(var1);
   }

   private static final Properties getConfigurationFile(ClassLoader param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   private static String getSystemProperty(String var0, String var1) throws SecurityException {
      return (String)AccessController.doPrivileged(new PrivilegedAction(var0, var1) {
         private final String val$key;
         private final String val$def;

         {
            this.val$key = var1;
            this.val$def = var2;
         }

         public Object run() {
            return System.getProperty(this.val$key, this.val$def);
         }
      });
   }

   private static PrintStream initDiagnostics() {
      String var0;
      try {
         var0 = getSystemProperty("org.apache.commons.logging.diagnostics.dest", (String)null);
         if (var0 == null) {
            return null;
         }
      } catch (SecurityException var3) {
         return null;
      }

      if (var0.equals("STDOUT")) {
         return System.out;
      } else if (var0.equals("STDERR")) {
         return System.err;
      } else {
         try {
            FileOutputStream var1 = new FileOutputStream(var0, true);
            return new PrintStream(var1);
         } catch (IOException var2) {
            return null;
         }
      }
   }

   private static final void logDiagnostic(String var0) {
      if (diagnosticsStream != null) {
         diagnosticsStream.print(diagnosticPrefix);
         diagnosticsStream.println(var0);
         diagnosticsStream.flush();
      }

   }

   protected static final void logRawDiagnostic(String var0) {
      if (diagnosticsStream != null) {
         diagnosticsStream.println(var0);
         diagnosticsStream.flush();
      }

   }

   private static void logClassLoaderEnvironment(Class param0) {
      // $FF: Couldn't be decompiled
   }

   private static void logHierarchy(String param0, ClassLoader param1) {
      // $FF: Couldn't be decompiled
   }

   public static String objectId(Object var0) {
      return var0 == null ? "null" : var0.getClass().getName() + "@" + System.identityHashCode(var0);
   }

   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static void access$000(String var0) {
      logDiagnostic(var0);
   }

   static {
      // $FF: Couldn't be decompiled
   }
}
