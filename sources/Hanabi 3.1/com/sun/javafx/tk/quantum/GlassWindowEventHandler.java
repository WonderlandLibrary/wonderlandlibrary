package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.FocusCause;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

class GlassWindowEventHandler extends Window.EventHandler implements PrivilegedAction {
   private final WindowStage stage;
   private Window window;
   private int type;

   public GlassWindowEventHandler(WindowStage var1) {
      this.stage = var1;
   }

   public Void run() {
      if (this.stage != null && this.stage.stageListener != null) {
         float var1;
         float var2;
         switch (this.type) {
            case 511:
               var1 = this.window.getPlatformScaleX();
               var2 = this.window.getPlatformScaleY();
               this.stage.stageListener.changedSize((float)this.window.getWidth() / var1, (float)this.window.getHeight() / var2);
               break;
            case 512:
               var1 = (float)this.window.getX();
               var2 = (float)this.window.getY();
               Screen var3 = this.window.getScreen();
               float var4;
               float var5;
               if (var3 != null) {
                  float var6 = var3.getPlatformScaleX();
                  float var7 = var3.getPlatformScaleY();
                  float var8 = (float)var3.getX();
                  float var9 = (float)var3.getY();
                  float var10 = (float)var3.getPlatformX();
                  float var11 = (float)var3.getPlatformY();
                  var4 = var8 + (var1 - var10) / var6;
                  var5 = var9 + (var2 - var11) / var7;
               } else {
                  var4 = var1;
                  var5 = var2;
               }

               this.stage.stageListener.changedLocation(var4, var5);
               if (!Application.GetApplication().hasWindowManager()) {
                  QuantumToolkit.runWithRenderLock(() -> {
                     GlassScene var1 = this.stage.getScene();
                     if (var1 != null) {
                        var1.updateSceneState();
                     }

                     return null;
                  });
               }
               break;
            case 513:
               var1 = this.window.getOutputScaleX();
               var2 = this.window.getOutputScaleY();
               this.stage.stageListener.changedScale(var1, var2);
               QuantumToolkit.runWithRenderLock(() -> {
                  GlassScene var1 = this.stage.getScene();
                  if (var1 != null) {
                     var1.entireSceneNeedsRepaint();
                     var1.updateSceneState();
                  }

                  return null;
               });
               break;
            case 514:
            case 515:
            case 516:
            case 517:
            case 518:
            case 519:
            case 520:
            case 523:
            case 524:
            case 525:
            case 526:
            case 527:
            case 528:
            case 529:
            case 530:
            case 534:
            case 535:
            case 536:
            case 537:
            case 538:
            case 539:
            case 540:
            default:
               if (QuantumToolkit.verbose) {
                  System.err.println("GlassWindowEventHandler: unknown type: " + this.type);
               }
               break;
            case 521:
               this.stage.stageListener.closing();
               break;
            case 522:
               this.stage.setPlatformWindowClosed();
               this.stage.stageListener.closed();
               break;
            case 531:
               this.stage.stageListener.changedIconified(true);
               break;
            case 532:
               this.stage.stageListener.changedIconified(false);
               this.stage.stageListener.changedMaximized(true);
               break;
            case 533:
               this.stage.stageListener.changedIconified(false);
               this.stage.stageListener.changedMaximized(false);
               break;
            case 541:
               this.stage.stageListener.changedFocused(false, FocusCause.DEACTIVATED);
               break;
            case 542:
               WindowStage.addActiveWindow(this.stage);
               this.stage.stageListener.changedFocused(true, FocusCause.ACTIVATED);
               break;
            case 543:
               WindowStage.addActiveWindow(this.stage);
               this.stage.stageListener.changedFocused(true, FocusCause.TRAVERSED_FORWARD);
               break;
            case 544:
               WindowStage.addActiveWindow(this.stage);
               this.stage.stageListener.changedFocused(true, FocusCause.TRAVERSED_BACKWARD);
               break;
            case 545:
               this.stage.handleFocusDisabled();
               break;
            case 546:
               this.stage.stageListener.focusUngrab();
         }

         return null;
      } else {
         return null;
      }
   }

   public void handleLevelEvent(int var1) {
      QuantumToolkit.runWithoutRenderLock(() -> {
         AccessControlContext var2 = this.stage.getAccessControlContext();
         return (Void)AccessController.doPrivileged(() -> {
            this.stage.stageListener.changedAlwaysOnTop(var1 != 1);
            return (Void)null;
         }, var2);
      });
   }

   public void handleWindowEvent(Window var1, long var2, int var4) {
      this.window = var1;
      this.type = var4;
      QuantumToolkit.runWithoutRenderLock(() -> {
         AccessControlContext var1 = this.stage.getAccessControlContext();
         return (Void)AccessController.doPrivileged(this, var1);
      });
   }

   public void handleScreenChangedEvent(Window var1, long var2, Screen var4, Screen var5) {
      GlassScene var6 = this.stage.getScene();
      if (var6 != null) {
         QuantumToolkit.runWithRenderLock(() -> {
            var6.entireSceneNeedsRepaint();
            var6.updateSceneState();
            return null;
         });
      }

      QuantumToolkit.runWithoutRenderLock(() -> {
         AccessControlContext var3 = this.stage.getAccessControlContext();
         return (Void)AccessController.doPrivileged(() -> {
            this.stage.stageListener.changedScreen(var4, var5);
            return (Void)null;
         }, var3);
      });
   }
}
