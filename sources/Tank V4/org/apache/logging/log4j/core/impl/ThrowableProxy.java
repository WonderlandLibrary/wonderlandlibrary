package org.apache.logging.log4j.core.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public class ThrowableProxy implements Serializable {
   private static final long serialVersionUID = -2752771578252251910L;
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final ThrowableProxy.PrivateSecurityManager SECURITY_MANAGER;
   private static final Method GET_SUPPRESSED;
   private static final Method ADD_SUPPRESSED;
   private final ThrowableProxy proxyCause;
   private final Throwable throwable;
   private final String name;
   private final StackTracePackageElement[] callerPackageData;
   private int commonElementCount;

   public ThrowableProxy(Throwable var1) {
      this.throwable = var1;
      this.name = var1.getClass().getName();
      HashMap var2 = new HashMap();
      Stack var3 = this.getCurrentStack();
      this.callerPackageData = this.resolvePackageData(var3, var2, (StackTraceElement[])null, var1.getStackTrace());
      this.proxyCause = var1.getCause() == null ? null : new ThrowableProxy(var1, var3, var2, var1.getCause());
      this.setSuppressed(var1);
   }

   private ThrowableProxy(Throwable var1, Stack var2, Map var3, Throwable var4) {
      this.throwable = var4;
      this.name = var4.getClass().getName();
      this.callerPackageData = this.resolvePackageData(var2, var3, var1.getStackTrace(), var4.getStackTrace());
      this.proxyCause = var4.getCause() == null ? null : new ThrowableProxy(var1, var2, var3, var4.getCause());
      this.setSuppressed(var4);
   }

   public Throwable getThrowable() {
      return this.throwable;
   }

   public ThrowableProxy getCause() {
      return this.proxyCause;
   }

   public String getName() {
      return this.name;
   }

   public int getCommonElementCount() {
      return this.commonElementCount;
   }

   public StackTracePackageElement[] getPackageData() {
      return this.callerPackageData;
   }

   public String toString() {
      String var1 = this.throwable.getMessage();
      return var1 != null ? this.name + ": " + var1 : this.name;
   }

   public String getRootCauseStackTrace() {
      return this.getRootCauseStackTrace((List)null);
   }

   public String getRootCauseStackTrace(List var1) {
      StringBuilder var2 = new StringBuilder();
      if (this.proxyCause != null) {
         this.formatWrapper(var2, this.proxyCause);
         var2.append("Wrapped by: ");
      }

      var2.append(this.toString());
      var2.append("\n");
      this.formatElements(var2, 0, this.throwable.getStackTrace(), this.callerPackageData, var1);
      return var2.toString();
   }

   public void formatWrapper(StringBuilder var1, ThrowableProxy var2) {
      this.formatWrapper(var1, var2, (List)null);
   }

   public void formatWrapper(StringBuilder var1, ThrowableProxy var2, List var3) {
      Throwable var4 = var2.getCause() != null ? var2.getCause().getThrowable() : null;
      if (var4 != null) {
         this.formatWrapper(var1, var2.proxyCause);
         var1.append("Wrapped by: ");
      }

      var1.append(var2).append("\n");
      this.formatElements(var1, var2.commonElementCount, var2.getThrowable().getStackTrace(), var2.callerPackageData, var3);
   }

   public String getExtendedStackTrace() {
      return this.getExtendedStackTrace((List)null);
   }

   public String getExtendedStackTrace(List var1) {
      StringBuilder var2 = new StringBuilder(this.name);
      String var3 = this.throwable.getMessage();
      if (var3 != null) {
         var2.append(": ").append(this.throwable.getMessage());
      }

      var2.append("\n");
      this.formatElements(var2, 0, this.throwable.getStackTrace(), this.callerPackageData, var1);
      if (this.proxyCause != null) {
         this.formatCause(var2, this.proxyCause, var1);
      }

      return var2.toString();
   }

   public String getSuppressedStackTrace() {
      ThrowableProxy[] var1 = this.getSuppressed();
      if (var1 != null && var1.length != 0) {
         StringBuilder var2 = new StringBuilder("Suppressed Stack Trace Elements:\n");
         ThrowableProxy[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ThrowableProxy var6 = var3[var5];
            var2.append(var6.getExtendedStackTrace());
         }

         return var2.toString();
      } else {
         return "";
      }
   }

   private void formatCause(StringBuilder var1, ThrowableProxy var2, List var3) {
      var1.append("Caused by: ").append(var2).append("\n");
      this.formatElements(var1, var2.commonElementCount, var2.getThrowable().getStackTrace(), var2.callerPackageData, var3);
      if (var2.getCause() != null) {
         this.formatCause(var1, var2.proxyCause, var3);
      }

   }

   private void formatElements(StringBuilder var1, int var2, StackTraceElement[] var3, StackTracePackageElement[] var4, List var5) {
      int var6;
      if (var5 != null && var5.size() != 0) {
         var6 = 0;

         for(int var7 = 0; var7 < var4.length; ++var7) {
            StackTraceElement var10001 = var3[var7];
            if (var5 != false) {
               if (var6 > 0) {
                  if (var6 == 1) {
                     var1.append("\t....\n");
                  } else {
                     var1.append("\t... suppressed ").append(var6).append(" lines\n");
                  }

                  var6 = 0;
               }

               this.formatEntry(var3[var7], var4[var7], var1);
            } else {
               ++var6;
            }
         }

         if (var6 > 0) {
            if (var6 == 1) {
               var1.append("\t...\n");
            } else {
               var1.append("\t... suppressed ").append(var6).append(" lines\n");
            }
         }
      } else {
         for(var6 = 0; var6 < var4.length; ++var6) {
            this.formatEntry(var3[var6], var4[var6], var1);
         }
      }

      if (var2 != 0) {
         var1.append("\t... ").append(var2).append(" more").append("\n");
      }

   }

   private void formatEntry(StackTraceElement var1, StackTracePackageElement var2, StringBuilder var3) {
      var3.append("\tat ");
      var3.append(var1);
      var3.append(" ");
      var3.append(var2);
      var3.append("\n");
   }

   private Stack getCurrentStack() {
      if (ReflectiveCallerClassUtility.isSupported()) {
         Stack var7 = new Stack();
         int var8 = 1;

         for(Class var9 = ReflectiveCallerClassUtility.getCaller(var8); var9 != null; var9 = ReflectiveCallerClassUtility.getCaller(var8)) {
            var7.push(var9);
            ++var8;
         }

         return var7;
      } else if (SECURITY_MANAGER == null) {
         return new Stack();
      } else {
         Class[] var1 = SECURITY_MANAGER.getClasses();
         Stack var2 = new Stack();
         Class[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            var2.push(var6);
         }

         return var2;
      }
   }

   StackTracePackageElement[] resolvePackageData(Stack var1, Map var2, StackTraceElement[] var3, StackTraceElement[] var4) {
      int var5;
      if (var3 != null) {
         int var6 = var3.length - 1;

         int var7;
         for(var7 = var4.length - 1; var6 >= 0 && var7 >= 0 && var3[var6].equals(var4[var7]); --var7) {
            --var6;
         }

         this.commonElementCount = var4.length - 1 - var7;
         var5 = var7 + 1;
      } else {
         this.commonElementCount = 0;
         var5 = var4.length;
      }

      StackTracePackageElement[] var12 = new StackTracePackageElement[var5];
      Class var13 = var1.isEmpty() ? null : (Class)var1.peek();
      ClassLoader var8 = null;

      for(int var9 = var5 - 1; var9 >= 0; --var9) {
         String var10 = var4[var9].getClassName();
         ThrowableProxy.CacheEntry var11;
         if (var13 != null && var10.equals(var13.getName())) {
            var11 = this.resolvePackageElement(var13, true);
            var12[var9] = ThrowableProxy.CacheEntry.access$100(var11);
            var8 = ThrowableProxy.CacheEntry.access$200(var11);
            var1.pop();
            var13 = var1.isEmpty() ? null : (Class)var1.peek();
         } else if (var2.containsKey(var10)) {
            var11 = (ThrowableProxy.CacheEntry)var2.get(var10);
            var12[var9] = ThrowableProxy.CacheEntry.access$100(var11);
            if (ThrowableProxy.CacheEntry.access$200(var11) != null) {
               var8 = ThrowableProxy.CacheEntry.access$200(var11);
            }
         } else {
            var11 = this.resolvePackageElement(this.loadClass(var8, var10), false);
            var12[var9] = ThrowableProxy.CacheEntry.access$100(var11);
            var2.put(var10, var11);
            if (ThrowableProxy.CacheEntry.access$200(var11) != null) {
               var8 = ThrowableProxy.CacheEntry.access$200(var11);
            }
         }
      }

      return var12;
   }

   private ThrowableProxy.CacheEntry resolvePackageElement(Class var1, boolean var2) {
      String var3 = "?";
      String var4 = "?";
      ClassLoader var5 = null;
      if (var1 != null) {
         try {
            CodeSource var6 = var1.getProtectionDomain().getCodeSource();
            if (var6 != null) {
               URL var7 = var6.getLocation();
               if (var7 != null) {
                  String var8 = var7.toString().replace('\\', '/');
                  int var9 = var8.lastIndexOf("/");
                  if (var9 >= 0 && var9 == var8.length() - 1) {
                     var9 = var8.lastIndexOf("/", var9 - 1);
                     var3 = var8.substring(var9 + 1);
                  } else {
                     var3 = var8.substring(var9 + 1);
                  }
               }
            }
         } catch (Exception var10) {
         }

         Package var11 = var1.getPackage();
         if (var11 != null) {
            String var12 = var11.getImplementationVersion();
            if (var12 != null) {
               var4 = var12;
            }
         }

         var5 = var1.getClassLoader();
      }

      return new ThrowableProxy.CacheEntry(this, new StackTracePackageElement(var3, var4, var2), var5);
   }

   private Class loadClass(ClassLoader var1, String var2) {
      Class var3;
      if (var1 != null) {
         try {
            var3 = var1.loadClass(var2);
            if (var3 != null) {
               return var3;
            }
         } catch (Exception var10) {
         }
      }

      try {
         var3 = Thread.currentThread().getContextClassLoader().loadClass(var2);
      } catch (ClassNotFoundException var9) {
         try {
            var3 = Class.forName(var2);
         } catch (ClassNotFoundException var8) {
            try {
               var3 = this.getClass().getClassLoader().loadClass(var2);
            } catch (ClassNotFoundException var7) {
               return null;
            }
         }
      }

      return var3;
   }

   public ThrowableProxy[] getSuppressed() {
      if (GET_SUPPRESSED != null) {
         try {
            return (ThrowableProxy[])((ThrowableProxy[])GET_SUPPRESSED.invoke(this.throwable));
         } catch (Exception var2) {
            return null;
         }
      } else {
         return null;
      }
   }

   private void setSuppressed(Throwable var1) {
      if (GET_SUPPRESSED != null && ADD_SUPPRESSED != null) {
         try {
            Throwable[] var2 = (Throwable[])((Throwable[])GET_SUPPRESSED.invoke(var1));
            Throwable[] var3 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Throwable var6 = var3[var5];
               ADD_SUPPRESSED.invoke(this, new ThrowableProxy(var6));
            }
         } catch (Exception var7) {
         }
      }

   }

   static {
      if (ReflectiveCallerClassUtility.isSupported()) {
         SECURITY_MANAGER = null;
      } else {
         ThrowableProxy.PrivateSecurityManager var0;
         try {
            var0 = new ThrowableProxy.PrivateSecurityManager();
            if (var0.getClasses() == null) {
               var0 = null;
               LOGGER.error("Unable to obtain call stack from security manager.");
            }
         } catch (Exception var7) {
            var0 = null;
            LOGGER.debug((String)"Unable to install security manager.", (Throwable)var7);
         }

         SECURITY_MANAGER = var0;
      }

      Method var8 = null;
      Method var1 = null;
      Method[] var2 = Throwable.class.getMethods();
      Method[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         if (var6.getName().equals("getSuppressed")) {
            var8 = var6;
         } else if (var6.getName().equals("addSuppressed")) {
            var1 = var6;
         }
      }

      GET_SUPPRESSED = var8;
      ADD_SUPPRESSED = var1;
   }

   private static class PrivateSecurityManager extends SecurityManager {
      private PrivateSecurityManager() {
      }

      public Class[] getClasses() {
         return this.getClassContext();
      }

      PrivateSecurityManager(Object var1) {
         this();
      }
   }

   class CacheEntry {
      private final StackTracePackageElement element;
      private final ClassLoader loader;
      final ThrowableProxy this$0;

      public CacheEntry(ThrowableProxy var1, StackTracePackageElement var2, ClassLoader var3) {
         this.this$0 = var1;
         this.element = var2;
         this.loader = var3;
      }

      static StackTracePackageElement access$100(ThrowableProxy.CacheEntry var0) {
         return var0.element;
      }

      static ClassLoader access$200(ThrowableProxy.CacheEntry var0) {
         return var0.loader;
      }
   }
}
