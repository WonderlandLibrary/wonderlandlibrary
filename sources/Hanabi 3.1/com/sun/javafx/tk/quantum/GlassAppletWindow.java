package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.TKStage;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.stage.Stage;

class GlassAppletWindow implements AppletWindow {
   private final Window glassWindow;
   private WeakReference topStage;
   private String serverName;

   GlassAppletWindow(long var1, String var3) {
      if (0L == var1) {
         if (var3 != null) {
            throw new RuntimeException("GlassAppletWindow constructor used incorrectly.");
         }

         this.glassWindow = Application.GetApplication().createWindow((Screen)null, 0);
      } else {
         this.serverName = var3;
         this.glassWindow = Application.GetApplication().createWindow(var1);
      }

   }

   Window getGlassWindow() {
      return this.glassWindow;
   }

   public void setBackgroundColor(int var1) {
      Application.invokeLater(() -> {
         float var2 = (float)(var1 >> 16 & 255) / 255.0F;
         float var3 = (float)(var1 >> 8 & 255) / 255.0F;
         float var4 = (float)(var1 & 255) / 255.0F;
         this.glassWindow.setBackground(var2, var3, var4);
      });
   }

   public void setForegroundColor(int var1) {
   }

   public void setVisible(boolean var1) {
      Application.invokeLater(() -> {
         this.glassWindow.setVisible(var1);
      });
   }

   public void setSize(int var1, int var2) {
      Application.invokeLater(() -> {
         this.glassWindow.setSize(var1, var2);
      });
   }

   public int getWidth() {
      AtomicInteger var1 = new AtomicInteger(0);
      Application.invokeAndWait(() -> {
         var1.set(this.glassWindow.getWidth());
      });
      return var1.get();
   }

   public int getHeight() {
      AtomicInteger var1 = new AtomicInteger(0);
      Application.invokeAndWait(() -> {
         var1.set(this.glassWindow.getHeight());
      });
      return var1.get();
   }

   public void setPosition(int var1, int var2) {
      Application.invokeLater(() -> {
         this.glassWindow.setPosition(var1, var2);
      });
   }

   public int getPositionX() {
      AtomicInteger var1 = new AtomicInteger(0);
      Application.invokeAndWait(() -> {
         var1.set(this.glassWindow.getX());
      });
      return var1.get();
   }

   public int getPositionY() {
      AtomicInteger var1 = new AtomicInteger(0);
      Application.invokeAndWait(() -> {
         var1.set(this.glassWindow.getY());
      });
      return var1.get();
   }

   public float getPlatformScaleX() {
      AtomicReference var1 = new AtomicReference(0.0F);
      Application.invokeAndWait(() -> {
         var1.set(this.glassWindow.getPlatformScaleX());
      });
      return (Float)var1.get();
   }

   public float getPlatformScaleY() {
      AtomicReference var1 = new AtomicReference(0.0F);
      Application.invokeAndWait(() -> {
         var1.set(this.glassWindow.getPlatformScaleY());
      });
      return (Float)var1.get();
   }

   void dispose() {
      QuantumToolkit.runWithRenderLock(() -> {
         this.glassWindow.close();
         return null;
      });
   }

   public void setStageOnTop(Stage var1) {
      if (null != var1) {
         this.topStage = new WeakReference(var1);
      } else {
         this.topStage = null;
      }

   }

   public int getRemoteLayerId() {
      AtomicInteger var1 = new AtomicInteger(-1);
      Application.invokeAndWait(() -> {
         View var2 = this.glassWindow.getView();
         if (var2 != null) {
            var1.set(var2.getNativeRemoteLayerId(this.serverName));
         }

      });
      return var1.get();
   }

   public void dispatchEvent(Map var1) {
      Application.invokeAndWait(() -> {
         this.glassWindow.dispatchNpapiEvent(var1);
      });
   }

   void assertStageOrder() {
      if (null != this.topStage) {
         Stage var1 = (Stage)this.topStage.get();
         if (null != var1) {
            TKStage var2 = var1.impl_getPeer();
            if (var2 instanceof WindowStage && ((WindowStage)var2).isVisible()) {
               Window var3 = ((WindowStage)var2).getPlatformWindow();
               if (null != var3) {
                  var3.toFront();
               }
            }
         }
      }

   }
}
