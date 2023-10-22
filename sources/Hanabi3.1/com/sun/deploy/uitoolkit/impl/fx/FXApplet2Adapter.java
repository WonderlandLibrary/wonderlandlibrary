package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2;
import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.Preloader;
import com.sun.deploy.uitoolkit.Applet2Adapter;
import com.sun.deploy.uitoolkit.Window;
import com.sun.deploy.uitoolkit.impl.fx.ui.ErrorPane;
import com.sun.javafx.applet.ExperimentalExtensions;
import com.sun.javafx.applet.FXApplet2;
import com.sun.javafx.applet.HostServicesImpl;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXApplet2Adapter extends Applet2Adapter {
   FXApplet2 applet2 = null;
   FXPreloader preLoader = null;
   private PlatformImpl.FinishListener finishListener;
   private boolean exitOnIdle = false;
   private static boolean platformDestroyed = false;
   private static FXApplet2Adapter adapter = null;
   private FXWindow window = null;

   FXApplet2Adapter(Applet2Context var1) {
      super(var1);
      Class var2 = FXApplet2Adapter.class;
      synchronized(FXApplet2Adapter.class) {
         adapter = this;
      }

      ExperimentalExtensions.init(var1);
      HostServicesImpl.init(var1);
      this.installFinishListener();
   }

   void abortApplet() {
      if (this.applet2 != null) {
         this.applet2.abortLaunch();
      }

   }

   synchronized void setExitOnIdle(boolean var1) {
      this.exitOnIdle = var1;
   }

   synchronized boolean isExitOnIdle() {
      return this.exitOnIdle;
   }

   static synchronized boolean isPlatformDestroyed() {
      return platformDestroyed;
   }

   private static synchronized void platformExit() {
      platformDestroyed = true;
      PlatformImpl.tkExit();
   }

   private void installFinishListener() {
      this.finishListener = new PlatformImpl.FinishListener() {
         private void destroyIfNeeded() {
            if (FXApplet2Adapter.this.window == null) {
               try {
                  if (FXApplet2Adapter.this.applet2 != null) {
                     FXApplet2Adapter.this.applet2.stop();
                  }

                  if (FXApplet2Adapter.this.preLoader != null) {
                     FXApplet2Adapter.this.preLoader.stop();
                  }

                  if (FXApplet2Adapter.this.applet2 != null) {
                     FXApplet2Adapter.this.applet2.destroy();
                  }

                  FXApplet2Adapter.this.applet2 = null;
               } catch (Exception var5) {
                  var5.printStackTrace();
               } finally {
                  PlatformImpl.removeListener(FXApplet2Adapter.this.finishListener);
                  FXApplet2Adapter.platformExit();
               }
            }

         }

         public void idle(boolean var1) {
            if (var1 && FXApplet2Adapter.this.isExitOnIdle()) {
               this.destroyIfNeeded();
            }

         }

         public void exitCalled() {
            this.destroyIfNeeded();
         }
      };
      PlatformImpl.addListener(this.finishListener);
   }

   public static FXApplet2Adapter getFXApplet2Adapter(Applet2Context var0) {
      return new FXApplet2Adapter(var0);
   }

   static synchronized Applet2Adapter get() {
      return adapter;
   }

   public void setParentContainer(Window var1) {
      if (var1 instanceof FXWindow) {
         this.window = (FXWindow)var1;
      }

   }

   public void instantiateApplet(Class var1) throws InstantiationException, IllegalAccessException {
      this.applet2 = new FXApplet2(var1, this.window);
   }

   public void instantiateSerialApplet(ClassLoader var1, String var2) {
      throw new RuntimeException("Serial applets are not supported with FX toolkit");
   }

   public Object getLiveConnectObject() {
      return this.applet2 == null ? null : this.applet2.getApplication();
   }

   public Applet2 getApplet2() {
      return this.applet2;
   }

   public Preloader instantiatePreloader(Class var1) {
      try {
         if (var1 != null) {
            this.preLoader = new FXPreloader(var1, this.getApplet2Context(), this.window);
         } else {
            this.preLoader = new FXPreloader(this.getApplet2Context(), this.window);
         }

         this.preLoader.start();
      } catch (Exception var3) {
         this.preLoader = null;
         throw new RuntimeException(var3);
      }

      return this.preLoader;
   }

   public Preloader getPreloader() {
      return this.preLoader;
   }

   public void init() {
      if (this.applet2 != null) {
         this.applet2.init(this.getApplet2Context());
      }

   }

   public void start() {
      if (this.applet2 != null) {
         this.applet2.start();
      }

      this.setExitOnIdle(true);
   }

   public void stop() {
      if (this.applet2 != null) {
         this.applet2.stop();
      }

   }

   public void destroy() {
      if (this.applet2 != null) {
         this.applet2.destroy();
      }

   }

   public void abort() {
   }

   public void resize(int var1, int var2) {
   }

   public void doShowApplet() {
   }

   public void doShowPreloader() {
   }

   public void doShowError(String var1, final Throwable var2, final boolean var3) {
      FXPreloader.hideSplash();
      Platform.runLater(new Runnable() {
         public void run() {
            Stage var1 = FXApplet2Adapter.this.window != null ? FXApplet2Adapter.this.window.getErrorStage() : new Stage();
            ErrorPane var2x = new ErrorPane(FXApplet2Adapter.this.getApplet2Context(), var2, var3);
            var1.setScene(new Scene(var2x));
            var1.show();
            var1.toFront();
         }
      });
   }

   public void doClearAppletArea() {
   }

   public boolean isInstantiated() {
      return this.applet2 != null;
   }
}
