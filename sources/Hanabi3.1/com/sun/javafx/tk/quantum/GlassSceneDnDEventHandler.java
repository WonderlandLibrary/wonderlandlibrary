package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import java.security.AccessController;
import javafx.application.Platform;
import javafx.scene.input.TransferMode;

class GlassSceneDnDEventHandler {
   private final GlassScene scene;

   public GlassSceneDnDEventHandler(GlassScene var1) {
      this.scene = var1;
   }

   private double getPlatformScaleX() {
      View var1 = this.scene.getPlatformView();
      if (var1 != null) {
         Window var2 = var1.getWindow();
         if (var2 != null) {
            return (double)var2.getPlatformScaleX();
         }
      }

      return 1.0;
   }

   private double getPlatformScaleY() {
      View var1 = this.scene.getPlatformView();
      if (var1 != null) {
         Window var2 = var1.getWindow();
         if (var2 != null) {
            return (double)var2.getPlatformScaleY();
         }
      }

      return 1.0;
   }

   public TransferMode handleDragEnter(int var1, int var2, int var3, int var4, TransferMode var5, ClipboardAssistance var6) {
      assert Platform.isFxApplicationThread();

      return (TransferMode)AccessController.doPrivileged(() -> {
         if (this.scene.dropTargetListener != null) {
            double var7 = this.getPlatformScaleX();
            double var9 = this.getPlatformScaleY();
            QuantumClipboard var11 = QuantumClipboard.getDragboardInstance(var6, false);
            return this.scene.dropTargetListener.dragEnter((double)var1 / var7, (double)var2 / var9, (double)var3 / var7, (double)var4 / var9, var5, var11);
         } else {
            return null;
         }
      }, this.scene.getAccessControlContext());
   }

   public void handleDragLeave(ClipboardAssistance var1) {
      assert Platform.isFxApplicationThread();

      AccessController.doPrivileged(() -> {
         if (this.scene.dropTargetListener != null) {
            this.scene.dropTargetListener.dragExit(0.0, 0.0, 0.0, 0.0);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   public TransferMode handleDragDrop(int var1, int var2, int var3, int var4, TransferMode var5, ClipboardAssistance var6) {
      assert Platform.isFxApplicationThread();

      return (TransferMode)AccessController.doPrivileged(() -> {
         if (this.scene.dropTargetListener != null) {
            double var6 = this.getPlatformScaleX();
            double var8 = this.getPlatformScaleY();
            return this.scene.dropTargetListener.drop((double)var1 / var6, (double)var2 / var8, (double)var3 / var6, (double)var4 / var8, var5);
         } else {
            return null;
         }
      }, this.scene.getAccessControlContext());
   }

   public TransferMode handleDragOver(int var1, int var2, int var3, int var4, TransferMode var5, ClipboardAssistance var6) {
      assert Platform.isFxApplicationThread();

      return (TransferMode)AccessController.doPrivileged(() -> {
         if (this.scene.dropTargetListener != null) {
            double var6 = this.getPlatformScaleX();
            double var8 = this.getPlatformScaleY();
            return this.scene.dropTargetListener.dragOver((double)var1 / var6, (double)var2 / var8, (double)var3 / var6, (double)var4 / var8, var5);
         } else {
            return null;
         }
      }, this.scene.getAccessControlContext());
   }

   public void handleDragStart(int var1, int var2, int var3, int var4, int var5, ClipboardAssistance var6) {
      assert Platform.isFxApplicationThread();

      AccessController.doPrivileged(() -> {
         if (this.scene.dragGestureListener != null) {
            double var7 = this.getPlatformScaleX();
            double var9 = this.getPlatformScaleY();
            QuantumClipboard var11 = QuantumClipboard.getDragboardInstance(var6, true);
            this.scene.dragGestureListener.dragGestureRecognized((double)var2 / var7, (double)var3 / var9, (double)var4 / var7, (double)var5 / var9, var1, var11);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   public void handleDragEnd(TransferMode var1, ClipboardAssistance var2) {
      assert Platform.isFxApplicationThread();

      AccessController.doPrivileged(() -> {
         try {
            if (this.scene.dragSourceListener != null) {
               this.scene.dragSourceListener.dragDropEnd(0.0, 0.0, 0.0, 0.0, var1);
            }
         } finally {
            QuantumClipboard.releaseCurrentDragboard();
         }

         return null;
      }, this.scene.getAccessControlContext());
   }
}
