package com.sun.javafx.tk.quantum;

import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.Toolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.List;
import javafx.application.Platform;

final class EmbeddedStage extends GlassStage implements EmbeddedStageInterface {
   private HostInterface host;

   public EmbeddedStage(HostInterface var1) {
      this.host = var1;
   }

   public TKScene createTKScene(boolean var1, boolean var2, AccessControlContext var3) {
      EmbeddedScene var4 = new EmbeddedScene(this.host, var1, var2);
      var4.setSecurityContext(var3);
      return var4;
   }

   public void setScene(TKScene var1) {
      assert var1 == null || var1 instanceof EmbeddedScene;

      super.setScene(var1);
   }

   public void setBounds(float var1, float var2, boolean var3, boolean var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedStage.setBounds: x=" + var1 + " y=" + var2 + " xSet=" + var3 + " ySet=" + var4 + " w=" + var5 + " h= cw=" + var7 + " ch=" + var8);
      }

      float var13 = var5 > 0.0F ? var5 : var7;
      float var14 = var6 > 0.0F ? var6 : var8;
      if (var13 > 0.0F && var14 > 0.0F) {
         this.host.setPreferredSize((int)var13, (int)var14);
      }

      GlassScene var15 = this.getScene();
      if ((var11 > 0.0F || var12 > 0.0F) && var15 instanceof EmbeddedScene) {
         EmbeddedScene var16 = (EmbeddedScene)var15;
         if ((double)var11 <= 0.0) {
            var11 = var16.getRenderScaleX();
         }

         if ((double)var12 <= 0.0) {
            var12 = var16.getRenderScaleY();
         }

         var16.setPixelScaleFactors(var11, var12);
      }

   }

   public float getPlatformScaleX() {
      return 1.0F;
   }

   public float getPlatformScaleY() {
      return 1.0F;
   }

   public float getOutputScaleX() {
      GlassScene var1 = this.getScene();
      return var1 instanceof EmbeddedScene ? ((EmbeddedScene)var1).getRenderScaleX() : 1.0F;
   }

   public float getOutputScaleY() {
      GlassScene var1 = this.getScene();
      return var1 instanceof EmbeddedScene ? ((EmbeddedScene)var1).getRenderScaleY() : 1.0F;
   }

   public void setMinimumSize(int var1, int var2) {
   }

   public void setMaximumSize(int var1, int var2) {
   }

   protected void setPlatformEnabled(boolean var1) {
      super.setPlatformEnabled(var1);
      this.host.setEnabled(var1);
   }

   public void setIcons(List var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedStage.setIcons");
      }

   }

   public void setTitle(String var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedStage.setTitle " + var1);
      }

   }

   public void setVisible(boolean var1) {
      this.host.setEmbeddedStage(var1 ? this : null);
      super.setVisible(var1);
   }

   public void setOpacity(float var1) {
   }

   public void setIconified(boolean var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedScene.setIconified " + var1);
      }

   }

   public void setMaximized(boolean var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedScene.setMaximized " + var1);
      }

   }

   public void setAlwaysOnTop(boolean var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedScene.setAlwaysOnTop " + var1);
      }

   }

   public void setResizable(boolean var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedStage.setResizable " + var1);
      }

   }

   public void setFullScreen(boolean var1) {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedStage.setFullScreen " + var1);
      }

   }

   public void requestFocus() {
      if (this.host.requestFocus()) {
         super.requestFocus();
      }
   }

   public void toBack() {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedStage.toBack");
      }

   }

   public void toFront() {
      if (QuantumToolkit.verbose) {
         System.err.println("EmbeddedStage.toFront");
      }

   }

   public boolean grabFocus() {
      return this.host.grabFocus();
   }

   public void ungrabFocus() {
      this.host.ungrabFocus();
   }

   private void notifyStageListener(Runnable var1) {
      AccessControlContext var2 = this.getAccessControlContext();
      AccessController.doPrivileged(() -> {
         var1.run();
         return null;
      }, var2);
   }

   private void notifyStageListenerLater(Runnable var1) {
      Platform.runLater(() -> {
         this.notifyStageListener(var1);
      });
   }

   public void setLocation(int var1, int var2) {
      Runnable var3 = () -> {
         if (this.stageListener != null) {
            this.stageListener.changedLocation((float)var1, (float)var2);
         }

      };
      if (Toolkit.getToolkit().isFxUserThread()) {
         this.notifyStageListener(var3);
      } else {
         this.notifyStageListenerLater(var3);
      }

   }

   public void setSize(int var1, int var2) {
      Runnable var3 = () -> {
         if (this.stageListener != null) {
            this.stageListener.changedSize((float)var1, (float)var2);
         }

      };
      if (Toolkit.getToolkit().isFxUserThread()) {
         this.notifyStageListener(var3);
      } else {
         this.notifyStageListenerLater(var3);
      }

   }

   public void setFocused(boolean var1, int var2) {
      Runnable var3 = () -> {
         if (this.stageListener != null) {
            this.stageListener.changedFocused(var1, AbstractEvents.focusCauseToPeerFocusCause(var2));
         }

      };
      if (Toolkit.getToolkit().isFxUserThread()) {
         this.notifyStageListener(var3);
      } else {
         this.notifyStageListenerLater(var3);
      }

   }

   public void focusUngrab() {
      Runnable var1 = () -> {
         if (this.stageListener != null) {
            this.stageListener.focusUngrab();
         }

      };
      if (Toolkit.getToolkit().isFxUserThread()) {
         this.notifyStageListener(var1);
      } else {
         this.notifyStageListenerLater(var1);
      }

   }

   public void requestInput(String var1, int var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void releaseInput() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setRTL(boolean var1) {
   }

   public void setEnabled(boolean var1) {
   }

   public long getRawHandle() {
      return 0L;
   }
}
