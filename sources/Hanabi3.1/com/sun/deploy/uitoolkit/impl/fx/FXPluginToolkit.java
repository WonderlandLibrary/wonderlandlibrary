package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.Preloader;
import com.sun.deploy.appcontext.AppContext;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.uitoolkit.Applet2Adapter;
import com.sun.deploy.uitoolkit.DragContext;
import com.sun.deploy.uitoolkit.DragHelper;
import com.sun.deploy.uitoolkit.DragListener;
import com.sun.deploy.uitoolkit.PluginUIToolkit;
import com.sun.deploy.uitoolkit.PluginWindowFactory;
import com.sun.deploy.uitoolkit.UIToolkit;
import com.sun.deploy.uitoolkit.Window;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXAppContext;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXUIFactory;
import com.sun.deploy.uitoolkit.ui.UIFactory;
import com.sun.deploy.util.Waiter;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import sun.plugin2.applet.Plugin2Manager;
import sun.plugin2.message.Pipe;

public class FXPluginToolkit extends PluginUIToolkit {
   static PluginWindowFactory windowFactory = null;
   private final Object initializedLock = new Object();
   private boolean initialized = false;
   private PlatformImpl.FinishListener finishListener = null;
   public static final DragHelper noOpDragHelper = new DragHelper() {
      public void register(DragContext var1, DragListener var2) {
      }

      public void makeDisconnected(DragContext var1, Window var2) {
      }

      public void restore(DragContext var1) {
      }

      public void unregister(DragContext var1) {
      }
   };
   static FXUIFactory fxUIFactory = new FXUIFactory();

   public PluginWindowFactory getWindowFactory() {
      if (windowFactory == null) {
         this.doInitIfNeeded();
         windowFactory = new FXWindowFactory();
      }

      return windowFactory;
   }

   public Preloader getDefaultPreloader() {
      this.doInitIfNeeded();
      return FXPreloader.getDefaultPreloader();
   }

   public Applet2Adapter getApplet2Adapter(Applet2Context var1) {
      this.doInitIfNeeded();
      FXApplet2Adapter var2 = FXApplet2Adapter.getFXApplet2Adapter(var1);
      this.uninstallFinishListener();
      return var2;
   }

   public void init() {
      Waiter.set(new FxWaiter());
   }

   private void doInitIfNeeded() {
      synchronized(this.initializedLock) {
         if (!this.initialized) {
            Boolean var2 = (Boolean)AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  try {
                     ClassLoader var1 = Thread.currentThread().getContextClassLoader();
                     Class var2 = Class.forName("com.sun.deploy.uitoolkit.ToolkitStore", false, var1);
                     Field var3 = var2.getDeclaredField("isPlugin");
                     var3.setAccessible(true);
                     return var3.getBoolean(var2);
                  } catch (Throwable var4) {
                     Trace.ignored(var4, true);
                     return Boolean.FALSE;
                  }
               }
            });
            boolean var3 = var2;
            PlatformImpl.setTaskbarApplication(!var3);
            this.installFinishListener();
            PlatformImpl.startup(new Runnable() {
               public void run() {
               }
            });
            this.initialized = true;
         }

      }
   }

   private void installFinishListener() {
      this.finishListener = new PlatformImpl.FinishListener() {
         public void idle(boolean var1) {
         }

         public void exitCalled() {
            FXPluginToolkit.this.uninstallFinishListener();
            PlatformImpl.tkExit();
         }
      };
      PlatformImpl.addListener(this.finishListener);
   }

   private void uninstallFinishListener() {
      synchronized(this.initializedLock) {
         if (this.finishListener != null) {
            PlatformImpl.removeListener(this.finishListener);
            this.finishListener = null;
         }

      }
   }

   public boolean printApplet(Plugin2Manager var1, int var2, Pipe var3, long var4, boolean var6, int var7, int var8, int var9, int var10) {
      this.doInitIfNeeded();
      return false;
   }

   public DragHelper getDragHelper() {
      this.doInitIfNeeded();
      return noOpDragHelper;
   }

   public void dispose() throws Exception {
      PlatformImpl.tkExit();
   }

   public boolean isHeadless() {
      return false;
   }

   public void setContextClassLoader(final ClassLoader var1) {
      if (var1 != null) {
         Platform.runLater(new Runnable() {
            public void run() {
               Thread.currentThread().setContextClassLoader(var1);
            }
         });
      }
   }

   public void warmup() {
      this.doInitIfNeeded();
   }

   public AppContext getAppContext() {
      return FXAppContext.getInstance();
   }

   public AppContext createAppContext() {
      return FXAppContext.getInstance();
   }

   public SecurityManager getSecurityManager() {
      SecurityManager var1 = null;

      try {
         Class var2 = Class.forName("sun.plugin2.applet.FXAppletSecurityManager", false, Thread.currentThread().getContextClassLoader());
         Constructor var3 = var2.getDeclaredConstructor();
         var1 = (SecurityManager)var3.newInstance();
      } catch (Exception var4) {
         Trace.ignoredException(var4);
      }

      return var1;
   }

   public UIToolkit changeMode(int var1) {
      return this;
   }

   public UIFactory getUIFactory() {
      this.doInitIfNeeded();
      return fxUIFactory;
   }

   public static Object callAndWait(Callable var0) throws Exception {
      Caller var1 = new Caller(var0);
      if (Platform.isFxApplicationThread()) {
         var1.run();
      } else {
         Platform.runLater(var1);
         if (FXApplet2Adapter.isPlatformDestroyed()) {
            return null;
         }
      }

      return var1.waitForReturn();
   }

   private static class Caller implements Runnable {
      private Object returnValue = null;
      Exception exception = null;
      private Callable returnable;
      private Boolean returned;
      private final Object lock;

      Caller(Callable var1) {
         this.returned = Boolean.FALSE;
         this.lock = new Object();
         this.returnable = var1;
      }

      public void run() {
         boolean var12 = false;

         label97: {
            try {
               var12 = true;
               this.returnValue = this.returnable.call();
               var12 = false;
               break label97;
            } catch (Throwable var16) {
               if (var16 instanceof Exception) {
                  this.exception = (Exception)var16;
                  var12 = false;
               } else {
                  this.exception = new RuntimeException("Problem in callAndWait()", var16);
                  var12 = false;
               }
            } finally {
               if (var12) {
                  synchronized(this.lock) {
                     this.returned = true;
                     this.lock.notifyAll();
                  }
               }
            }

            synchronized(this.lock) {
               this.returned = true;
               this.lock.notifyAll();
               return;
            }
         }

         synchronized(this.lock) {
            this.returned = true;
            this.lock.notifyAll();
         }

      }

      Object waitForReturn() throws Exception {
         synchronized(this.lock) {
            while(!this.returned && !FXApplet2Adapter.isPlatformDestroyed()) {
               try {
                  this.lock.wait(500L);
               } catch (InterruptedException var4) {
                  System.out.println("Interrupted wait" + var4.getStackTrace());
               }
            }
         }

         if (this.exception != null) {
            throw this.exception;
         } else {
            return this.returnValue;
         }
      }
   }

   private static final class FxWaiter extends Waiter {
      private final Toolkit tk = Toolkit.getToolkit();

      FxWaiter() {
         Class var1 = TaskThread.class;
      }

      public Object wait(Waiter.WaiterTask var1) throws Exception {
         if (!this.tk.isFxUserThread()) {
            return var1.run();
         } else {
            final Object var2 = new Object();
            Runnable var3 = new Runnable() {
               public void run() {
                  FxWaiter.this.tk.exitNestedEventLoop(var2, (Object)null);
               }
            };
            TaskThread var4 = new TaskThread(var1, var3);
            var4.start();
            this.tk.enterNestedEventLoop(var2);
            return var4.getResult();
         }
      }
   }

   private static class TaskThread extends Thread {
      final Waiter.WaiterTask task;
      final Runnable cleanup;
      Object rv;

      TaskThread(Waiter.WaiterTask var1, Runnable var2) {
         this.task = var1;
         this.cleanup = var2;
      }

      public Object getResult() throws Exception {
         if (this.rv instanceof Exception) {
            throw (Exception)this.rv;
         } else {
            return this.rv;
         }
      }

      public void run() {
         try {
            this.rv = this.task.run();
         } catch (Exception var5) {
            this.rv = var5;
         } finally {
            Platform.runLater(this.cleanup);
         }

      }
   }
}
