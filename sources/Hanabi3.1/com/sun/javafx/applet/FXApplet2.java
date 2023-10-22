package com.sun.javafx.applet;

import com.sun.applet2.Applet2;
import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.event.ErrorEvent;
import com.sun.deploy.uitoolkit.impl.fx.FXPluginToolkit;
import com.sun.deploy.uitoolkit.impl.fx.FXPreloader;
import com.sun.deploy.uitoolkit.impl.fx.FXWindow;
import com.sun.deploy.uitoolkit.impl.fx.Utils;
import com.sun.javafx.application.ParametersImpl;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.perf.PerformanceTracker;
import java.net.URL;
import java.security.AllPermission;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import sun.misc.PerformanceLogger;

public class FXApplet2 implements Applet2 {
   static final int SANDBOXAPP_DESTROY_TIMEOUT = 200;
   static final String JAVFX_APPLICATION_PARAM = "javafx.application";
   private Application application;
   private Applet2Context a2c;
   private FXWindow window;
   private Class applicationClass = null;
   private boolean isAborted = false;

   public FXApplet2(Class var1, FXWindow var2) {
      this.applicationClass = var1;
      this.window = var2;
   }

   public void init(Applet2Context var1) {
      synchronized(this) {
         if (this.isAborted) {
            return;
         }
      }

      try {
         FXPluginToolkit.callAndWait(new Callable() {
            public Object call() throws Exception {
               FXApplet2.this.application = (Application)FXApplet2.this.applicationClass.newInstance();
               return null;
            }
         });
      } catch (Throwable var6) {
         var6.printStackTrace();
         FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent((URL)null, "Failed to instantiate application.", var6));
         if (var6 instanceof ClassCastException) {
            throw new UnsupportedOperationException("In the JavaFX mode we only support applications extending JavaFX Application class.", var6);
         }

         throw new RuntimeException(var6);
      }

      this.a2c = var1;
      String[] var2 = Utils.getUnnamed(this.a2c);
      Map var3 = Utils.getNamedParameters(this.a2c);
      ParametersImpl.registerParameters(this.application, new ParametersImpl(var3, var2));
      PlatformImpl.setApplicationName(this.applicationClass);

      try {
         this.application.init();
      } catch (Throwable var5) {
         var5.printStackTrace();
         FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent((URL)null, "Failed to init application.", var5));
         throw new RuntimeException(var5);
      }
   }

   public Application getApplication() {
      return this.application;
   }

   public synchronized void abortLaunch() {
      this.isAborted = true;
   }

   public void start() {
      synchronized(this) {
         if (this.isAborted) {
            return;
         }
      }

      Platform.runLater(new Runnable() {
         public void run() {
            if (FXApplet2.this.application != null) {
               Stage var1;
               if (FXApplet2.this.window == null) {
                  var1 = new Stage();
               } else {
                  var1 = FXApplet2.this.window.getAppletStage();
               }

               try {
                  FXApplet2.this.application.start(var1);
                  if (PerformanceTracker.isLoggingEnabled()) {
                     String var2 = (String)FXApplet2.this.application.getParameters().getNamed().get("sun_perflog_fx_launchtime");
                     if (var2 != null && !var2.equals("")) {
                        long var3 = Long.parseLong(var2);
                        PerformanceLogger.setStartTime("LaunchTime", var3);
                     }
                  }
               } catch (Throwable var5) {
                  var5.printStackTrace();
                  FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent((URL)null, "Failed to start application.", var5));
                  throw new RuntimeException(var5);
               }
            } else {
               System.err.println("application is null?");
            }

         }
      });
   }

   public void stop() {
   }

   private boolean isSandboxApplication() {
      try {
         ProtectionDomain var1 = this.applicationClass.getProtectionDomain();
         if (var1.getPermissions().implies(new AllPermission())) {
            return false;
         }
      } catch (SecurityException var2) {
         var2.printStackTrace();
      }

      return true;
   }

   public void destroy() {
      synchronized(this) {
         if (this.isAborted) {
            return;
         }
      }

      PlatformImpl.runAndWait(new Runnable() {
         public void run() {
            if (FXApplet2.this.isSandboxApplication()) {
               Timer var1 = new Timer("Exit timer", true);
               TimerTask var2 = new TimerTask() {
                  public void run() {
                     System.exit(0);
                  }
               };
               var1.schedule(var2, 200L);
            }

            try {
               FXApplet2.this.application.stop();
               FXPreloader.notfiyCurrentPreloaderOnExit();
            } catch (Throwable var3) {
               var3.printStackTrace();
               FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent((URL)null, "Failed to stop application.", var3));
            }

         }
      });
   }
}
