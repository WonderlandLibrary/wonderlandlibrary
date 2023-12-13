package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.deploy.uitoolkit.SynthesizedEventListener;
import com.sun.deploy.uitoolkit.Window;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.Toolkit;
import java.util.Map;
import javafx.stage.Stage;

public class FXWindow extends Window implements AppletStageManager, SynthesizedEventListener {
   private AppletWindow appletWindow;
   private int width = 0;
   private int height = 0;
   private Stage appletStage;
   private Stage preloaderStage;
   private Stage errorStage;

   FXWindow(long var1, String var3) {
      this.appletWindow = Toolkit.getToolkit().createAppletWindow(var1, var3);
      this.width = this.appletWindow.getWidth();
      this.height = this.appletWindow.getHeight();
   }

   public Object getWindowObject() {
      return this.appletWindow;
   }

   public void synthesizeEvent(Map var1) {
      this.appletWindow.dispatchEvent(var1);
   }

   public void requestFocus() {
   }

   public int getWindowLayerID() {
      return this.appletWindow.getRemoteLayerId();
   }

   public void setBackground(int var1) {
      this.appletWindow.setBackgroundColor(var1);
   }

   public void setForeground(int var1) {
      this.appletWindow.setForegroundColor(var1);
   }

   public void setVisible(boolean var1) {
      this.appletWindow.setVisible(var1);
   }

   public void setSize(int var1, int var2) {
      this.width = var1;
      this.height = var2;
      this.appletWindow.setSize(var1, var2);
      float var3 = this.appletWindow.getPlatformScaleX();
      float var4 = this.appletWindow.getPlatformScaleY();
      if (this.appletStage != null) {
         this.appletStage.setWidth((double)((float)var1 / var3));
         this.appletStage.setHeight((double)((float)var2 / var4));
      }

      if (this.preloaderStage != null) {
         this.preloaderStage.setWidth((double)((float)var1 / var3));
         this.preloaderStage.setHeight((double)((float)var2 / var4));
      }

      if (this.errorStage != null) {
         this.errorStage.setWidth((double)((float)var1 / var3));
         this.errorStage.setHeight((double)((float)var2 / var4));
      }

   }

   public Window.WindowSize getSize() {
      this.width = this.appletWindow.getWidth();
      this.height = this.appletWindow.getHeight();
      return new Window.WindowSize(this, this.width, this.height);
   }

   public void dispose() {
      if (null != this.appletStage) {
         this.appletStage.close();
         this.appletStage = null;
      }

      if (null != this.preloaderStage) {
         this.preloaderStage.close();
         this.preloaderStage = null;
      }

      if (null != this.errorStage) {
         this.errorStage.close();
         this.errorStage = null;
      }

      this.appletWindow = null;
      Toolkit.getToolkit().closeAppletWindow();
   }

   public void dispose(Window.DisposeListener var1, long var2) {
      this.dispose();
   }

   public void setPosition(int var1, int var2) {
      this.appletWindow.setPosition(var1, var2);
   }

   public synchronized Stage getAppletStage() {
      if (this.appletStage == null) {
         this.appletStage = this.createNewStage();
      }

      return this.appletStage;
   }

   public Stage getPreloaderStage() {
      if (this.preloaderStage == null) {
         this.preloaderStage = this.createNewStage();
         if (null == this.errorStage) {
            this.appletWindow.setStageOnTop(this.preloaderStage);
         }
      }

      return this.preloaderStage;
   }

   public Stage getErrorStage() {
      if (this.errorStage == null) {
         this.errorStage = this.createNewStage();
         this.appletWindow.setStageOnTop(this.errorStage);
      }

      return this.errorStage;
   }

   private Stage createNewStage() {
      Stage var1 = new Stage();
      var1.impl_setPrimary(true);
      float var2 = this.appletWindow.getPlatformScaleX();
      float var3 = this.appletWindow.getPlatformScaleY();
      var1.setWidth((double)((float)this.width / var2));
      var1.setHeight((double)((float)this.height / var3));
      var1.setX((double)((float)this.appletWindow.getPositionX() / var2));
      var1.setY((double)((float)this.appletWindow.getPositionY() / var3));
      var1.setResizable(false);
      return var1;
   }
}
