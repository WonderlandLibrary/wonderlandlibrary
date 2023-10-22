package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2;
import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.CancelException;
import com.sun.applet2.preloader.Preloader;
import com.sun.applet2.preloader.event.AppletInitEvent;
import com.sun.applet2.preloader.event.ApplicationExitEvent;
import com.sun.applet2.preloader.event.DownloadEvent;
import com.sun.applet2.preloader.event.ErrorEvent;
import com.sun.applet2.preloader.event.PreloaderEvent;
import com.sun.applet2.preloader.event.UserDeclinedEvent;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.uitoolkit.Applet2Adapter;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXDefaultPreloader;
import com.sun.javafx.applet.ExperimentalExtensions;
import com.sun.javafx.applet.FXApplet2;
import com.sun.javafx.application.ParametersImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.Callable;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXPreloader extends Preloader {
   private static final Object lock = new Object();
   private static FXPreloader defaultPreloader = null;
   private javafx.application.Preloader fxPreview = null;
   private FXWindow window = null;
   private boolean seenFatalError = false;

   FXPreloader() {
      this.fxPreview = new FXDefaultPreloader();
   }

   FXPreloader(Applet2Context var1, FXWindow var2) {
      this.window = var2;

      try {
         FXPluginToolkit.callAndWait(new Callable() {
            public Object call() {
               FXPreloader.this.fxPreview = new FXDefaultPreloader();
               return null;
            }
         });
      } catch (RuntimeException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new RuntimeException(var6);
      }

      String[] var3 = Utils.getUnnamed(var1);
      Map var4 = Utils.getNamedParameters(var1);
      ParametersImpl.registerParameters(this.fxPreview, new ParametersImpl(var4, var3));
   }

   FXPreloader(final Class var1, Applet2Context var2, FXWindow var3) throws InstantiationException, IllegalAccessException {
      this.window = var3;
      if (!javafx.application.Preloader.class.isAssignableFrom(var1)) {
         throw new IllegalArgumentException("Unrecognized FX Preloader class");
      } else {
         try {
            FXPluginToolkit.callAndWait(new Callable() {
               public Object call() throws InstantiationException, IllegalAccessException {
                  FXPreloader.this.fxPreview = (javafx.application.Preloader)var1.newInstance();
                  return null;
               }
            });
         } catch (InstantiationException var6) {
            throw var6;
         } catch (IllegalAccessException var7) {
            throw var7;
         } catch (RuntimeException var8) {
            throw var8;
         } catch (Exception var9) {
            throw new RuntimeException(var9);
         }

         String[] var4 = Utils.getUnnamed(var2);
         Map var5 = Utils.getNamedParameters(var2);
         ParametersImpl.registerParameters(this.fxPreview, new ParametersImpl(var5, var4));
      }
   }

   static FXPreloader getDefaultPreloader() {
      synchronized(lock) {
         if (defaultPreloader != null) {
            defaultPreloader = new FXPreloader();
         }
      }

      return defaultPreloader;
   }

   public static void notfiyCurrentPreloaderOnExit() {
      FXPreloader.Notifier.send(new ApplicationExitEvent());
   }

   public static void notifyCurrentPreloaderOnError(ErrorEvent var0) {
      FXPreloader.Notifier.send(var0);
   }

   public static void notifyCurrentPreloader(javafx.application.Preloader.PreloaderNotification var0) {
      FXPreloader.Notifier.send(new UserEvent(var0));
   }

   public Object getOwner() {
      return null;
   }

   public boolean handleEvent(PreloaderEvent var1) throws CancelException {
      Boolean var2 = false;
      if (var1 instanceof ErrorEvent) {
         FXApplet2Adapter var3 = (FXApplet2Adapter)FXApplet2Adapter.get();
         var3.abortApplet();
      }

      FXDispatcher var7 = new FXDispatcher(var1);

      try {
         var2 = (Boolean)FXPluginToolkit.callAndWait(var7);
      } catch (CancelException var5) {
         throw var5;
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return var2;
   }

   void start() throws Exception {
      this.fxPreview.init();
      FXPluginToolkit.callAndWait(new Callable() {
         public Object call() throws Exception {
            Stage var1;
            if (FXPreloader.this.window == null) {
               var1 = new Stage();
               var1.impl_setPrimary(true);
            } else {
               var1 = FXPreloader.this.window.getPreloaderStage();
            }

            FXPreloader.this.fxPreview.start(var1);
            if (!(FXPreloader.this.fxPreview instanceof FXDefaultPreloader)) {
               FXPreloader.hideSplash();
            }

            return null;
         }
      });
   }

   public static void hideSplash() {
      ExperimentalExtensions var0 = ExperimentalExtensions.get();
      if (var0 != null) {
         var0.getSplash().hide();
      }

   }

   void stop() throws Exception {
      if (this.fxPreview != null) {
         this.fxPreview.stop();
      }

   }

   static {
      Class var0 = Notifier.class;
      Class var1 = UserEvent.class;
   }

   class FXDispatcher implements Callable {
      PreloaderEvent pe;

      FXDispatcher(PreloaderEvent var2) {
         this.pe = var2;
      }

      private void gotFatalError() {
         FXPreloader.this.seenFatalError = true;
         FXApplet2Adapter var1 = (FXApplet2Adapter)FXApplet2Adapter.get();
         var1.setExitOnIdle(true);
      }

      public Boolean call() throws Exception {
         if (FXPreloader.this.seenFatalError) {
            throw new CancelException("Cancel launch after fatal error");
         } else {
            switch (this.pe.getType()) {
               case 1:
                  return true;
               case 3:
                  DownloadEvent var5 = (DownloadEvent)this.pe;
                  double var6 = (double)var5.getOverallPercentage() / 100.0;
                  FXPreloader.this.fxPreview.handleProgressNotification(new javafx.application.Preloader.ProgressNotification(var6));
                  return true;
               case 5:
                  AppletInitEvent var1 = (AppletInitEvent)this.pe;
                  Application var2 = null;
                  Applet2 var3 = var1.getApplet();
                  if (var3 != null && var3 instanceof FXApplet2) {
                     var2 = ((FXApplet2)var3).getApplication();
                  }

                  switch (var1.getSubtype()) {
                     case 2:
                        FXPreloader.this.fxPreview.handleStateChangeNotification(new javafx.application.Preloader.StateChangeNotification(javafx.application.Preloader.StateChangeNotification.Type.BEFORE_LOAD));
                        break;
                     case 3:
                        FXPreloader.this.fxPreview.handleStateChangeNotification(new javafx.application.Preloader.StateChangeNotification(javafx.application.Preloader.StateChangeNotification.Type.BEFORE_INIT, var2));
                        break;
                     case 4:
                        FXPreloader.this.fxPreview.handleStateChangeNotification(new javafx.application.Preloader.StateChangeNotification(javafx.application.Preloader.StateChangeNotification.Type.BEFORE_START, var2));
                  }

                  return true;
               case 6:
                  ErrorEvent var8 = (ErrorEvent)this.pe;
                  String var9 = var8.getLocation() != null ? var8.getLocation().toString() : null;
                  Throwable var10 = var8.getException();
                  String var11 = var8.getValue();
                  if (var11 == null) {
                     var11 = var10 != null ? var10.getMessage() : "unknown error";
                  }

                  this.gotFatalError();
                  return FXPreloader.this.fxPreview.handleErrorNotification(new javafx.application.Preloader.ErrorNotification(var9, var11, var10));
               case 7:
                  UserDeclinedEvent var12 = (UserDeclinedEvent)this.pe;
                  String var13 = var12.getLocation();
                  return FXPreloader.this.fxPreview.handleErrorNotification(FXPreloader.this.new UserDeclinedNotification(var13));
               case 1000:
                  javafx.application.Preloader.PreloaderNotification var4 = ((UserEvent)this.pe).get();
                  FXPreloader.this.fxPreview.handleApplicationNotification(var4);
                  return true;
               default:
                  return false;
            }
         }
      }
   }

   class UserDeclinedNotification extends javafx.application.Preloader.ErrorNotification {
      public UserDeclinedNotification(String var2) {
         super(var2, "User declined to grant permissions to the application.", (Throwable)null);
      }
   }

   static class Notifier implements PrivilegedExceptionAction {
      PreloaderEvent pe;

      Notifier(PreloaderEvent var1) {
         this.pe = var1;
      }

      static void send(PreloaderEvent var0) {
         try {
            AccessController.doPrivileged(new Notifier(var0));
         } catch (Exception var2) {
            Trace.ignoredException(var2);
         }

      }

      public Void run() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CancelException {
         Applet2Adapter var1 = FXApplet2Adapter.get();
         if (var1 != null) {
            Class var2 = Class.forName("com.sun.javaws.progress.Progress");
            Method var3 = var2.getMethod("get", Applet2Adapter.class);
            Preloader var4 = (Preloader)var3.invoke((Object)null, var1);
            var4.handleEvent(this.pe);
         }

         return null;
      }
   }

   static class UserEvent extends PreloaderEvent {
      public static final int CUSTOM_USER_EVENT = 1000;
      javafx.application.Preloader.PreloaderNotification pe;

      UserEvent(javafx.application.Preloader.PreloaderNotification var1) {
         super(1000);
         this.pe = var1;
      }

      javafx.application.Preloader.PreloaderNotification get() {
         return this.pe;
      }
   }
}
