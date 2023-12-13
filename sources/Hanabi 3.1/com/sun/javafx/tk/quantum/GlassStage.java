package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.TKStageListener;
import com.sun.javafx.tk.Toolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;

abstract class GlassStage implements TKStage {
   private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
   private static final List windows = new ArrayList();
   private static List importantWindows = new ArrayList();
   private GlassScene scene;
   protected TKStageListener stageListener;
   private boolean visible;
   private boolean important = true;
   private AccessControlContext accessCtrlCtx = null;
   protected static final AtomicReference activeFSWindow = new AtomicReference();

   protected GlassStage() {
      windows.add(this);
   }

   public void close() {
      assert this.scene == null;

      windows.remove(this);
      importantWindows.remove(this);
      notifyWindowListeners();
   }

   public void setTKStageListener(TKStageListener var1) {
      this.stageListener = var1;
   }

   protected final GlassScene getScene() {
      return this.scene;
   }

   public void setScene(TKScene var1) {
      if (this.scene != null) {
         this.scene.setStage((GlassStage)null);
      }

      this.scene = (GlassScene)var1;
      if (this.scene != null) {
         this.scene.setStage(this);
      }

   }

   final AccessControlContext getAccessControlContext() {
      if (this.accessCtrlCtx == null) {
         throw new RuntimeException("Stage security context has not been set!");
      } else {
         return this.accessCtrlCtx;
      }
   }

   public final void setSecurityContext(AccessControlContext var1) {
      if (this.accessCtrlCtx != null) {
         throw new RuntimeException("Stage security context has been already set!");
      } else {
         AccessControlContext var2 = AccessController.getContext();
         this.accessCtrlCtx = (AccessControlContext)javaSecurityAccess.doIntersectionPrivilege(() -> {
            return AccessController.getContext();
         }, var2, var1);
      }
   }

   public void requestFocus() {
   }

   public void requestFocus(FocusCause var1) {
   }

   public void setVisible(boolean var1) {
      this.visible = var1;
      if (var1) {
         if (this.important) {
            importantWindows.add(this);
            notifyWindowListeners();
         }
      } else if (this.important) {
         importantWindows.remove(this);
         notifyWindowListeners();
      }

      if (this.scene != null) {
         this.scene.stageVisible(var1);
      }

   }

   boolean isVisible() {
      return this.visible;
   }

   protected void setPlatformEnabled(boolean var1) {
   }

   void windowsSetEnabled(boolean var1) {
      GlassStage[] var2 = (GlassStage[])windows.toArray(new GlassStage[windows.size()]);
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         GlassStage var5 = var2[var4];
         if (var5 != this && windows.contains(var5)) {
            var5.setPlatformEnabled(var1);
         }
      }

   }

   public void setImportant(boolean var1) {
      this.important = var1;
   }

   private static void notifyWindowListeners() {
      Toolkit.getToolkit().notifyWindowListeners(importantWindows);
   }

   static void requestClosingAllWindows() {
      GlassStage var0 = (GlassStage)activeFSWindow.get();
      if (var0 != null) {
         var0.setFullScreen(false);
      }

      GlassStage[] var1 = (GlassStage[])windows.toArray(new GlassStage[windows.size()]);
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         GlassStage var4 = var1[var3];
         if (windows.contains(var4) && var4.isVisible() && var4.stageListener != null) {
            AccessController.doPrivileged(() -> {
               var4.stageListener.closing();
               return null;
            }, var4.getAccessControlContext());
         }
      }

   }
}
