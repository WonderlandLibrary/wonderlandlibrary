package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

abstract class PerformanceTrackerHelper {
   private static final PerformanceTrackerHelper instance = createInstance();

   public static PerformanceTrackerHelper getInstance() {
      return instance;
   }

   private PerformanceTrackerHelper() {
   }

   private static PerformanceTrackerHelper createInstance() {
      Object var0 = (PerformanceTrackerHelper)AccessController.doPrivileged(new PrivilegedAction() {
         public PerformanceTrackerHelper run() {
            try {
               if (PrismSettings.perfLog != null) {
                  final PerformanceTrackerDefaultImpl var1 = new PerformanceTrackerDefaultImpl();
                  if (PrismSettings.perfLogExitFlush) {
                     Runtime.getRuntime().addShutdownHook(new Thread() {
                        public void run() {
                           var1.outputLog();
                        }
                     });
                  }

                  return var1;
               }
            } catch (Throwable var2) {
            }

            return null;
         }
      });
      if (var0 == null) {
         var0 = new PerformanceTrackerDummyImpl();
      }

      return (PerformanceTrackerHelper)var0;
   }

   public abstract void logEvent(String var1);

   public abstract void outputLog();

   public abstract boolean isPerfLoggingEnabled();

   public final long nanoTime() {
      return Toolkit.getToolkit().getPrimaryTimer().nanos();
   }

   // $FF: synthetic method
   PerformanceTrackerHelper(Object var1) {
      this();
   }

   private static final class PerformanceTrackerDummyImpl extends PerformanceTrackerHelper {
      private PerformanceTrackerDummyImpl() {
         super(null);
      }

      public void logEvent(String var1) {
      }

      public void outputLog() {
      }

      public boolean isPerfLoggingEnabled() {
         return false;
      }

      // $FF: synthetic method
      PerformanceTrackerDummyImpl(Object var1) {
         this();
      }
   }

   private static final class PerformanceTrackerDefaultImpl extends PerformanceTrackerHelper {
      private long firstTime;
      private long lastTime;
      private final Method logEventMethod;
      private final Method outputLogMethod;
      private final Method getStartTimeMethod;
      private final Method setStartTimeMethod;

      public PerformanceTrackerDefaultImpl() throws ClassNotFoundException, NoSuchMethodException {
         super(null);
         Class var1 = Class.forName("sun.misc.PerformanceLogger", true, (ClassLoader)null);
         this.logEventMethod = var1.getMethod("setTime", String.class);
         this.outputLogMethod = var1.getMethod("outputLog");
         this.getStartTimeMethod = var1.getMethod("getStartTime");
         this.setStartTimeMethod = var1.getMethod("setStartTime", String.class, Long.TYPE);
      }

      public void logEvent(String var1) {
         long var2 = System.currentTimeMillis();
         if (this.firstTime == 0L) {
            this.firstTime = var2;
         }

         try {
            this.logEventMethod.invoke((Object)null, "JavaFX> " + var1 + " (" + (var2 - this.firstTime) + "ms total, " + (var2 - this.lastTime) + "ms)");
         } catch (IllegalAccessException var5) {
         } catch (IllegalArgumentException var6) {
         } catch (InvocationTargetException var7) {
         }

         this.lastTime = var2;
      }

      public void outputLog() {
         this.logLaunchTime();

         try {
            this.outputLogMethod.invoke((Object)null);
         } catch (Exception var2) {
         }

      }

      public boolean isPerfLoggingEnabled() {
         return true;
      }

      private void logLaunchTime() {
         try {
            if ((Long)this.getStartTimeMethod.invoke((Object)null) <= 0L) {
               String var1 = (String)AccessController.doPrivileged(() -> {
                  return System.getProperty("launchTime");
               });
               if (var1 != null && !var1.equals("")) {
                  long var2 = Long.parseLong(var1);
                  this.setStartTimeMethod.invoke((Object)null, "LaunchTime", var2);
               }
            }
         } catch (Throwable var4) {
            var4.printStackTrace();
         }

      }
   }
}
