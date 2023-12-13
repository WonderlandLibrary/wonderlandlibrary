package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.View;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKSceneListener;
import com.sun.javafx.tk.TKScenePaintListener;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.input.InputMethodRequests;
import javafx.stage.StageStyle;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;

abstract class GlassScene implements TKScene {
   private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
   private GlassStage stage;
   protected TKSceneListener sceneListener;
   protected TKDragGestureListener dragGestureListener;
   protected TKDragSourceListener dragSourceListener;
   protected TKDropTargetListener dropTargetListener;
   protected InputMethodRequests inputMethodRequests;
   private TKScenePaintListener scenePaintListener;
   private NGNode root;
   private NGCamera camera;
   protected Paint fillPaint;
   private volatile boolean entireSceneDirty = true;
   private boolean doPresent = true;
   private final AtomicBoolean painting = new AtomicBoolean(false);
   private final boolean depthBuffer;
   private final boolean msaa;
   SceneState sceneState;
   private AccessControlContext accessCtrlCtx = null;
   private NGLightBase[] lights;

   protected GlassScene(boolean var1, boolean var2) {
      this.msaa = var2;
      this.depthBuffer = var1;
      this.sceneState = new SceneState(this);
   }

   public void dispose() {
      assert this.stage == null;

      this.root = null;
      this.camera = null;
      this.fillPaint = null;
      this.sceneListener = null;
      this.dragGestureListener = null;
      this.dragSourceListener = null;
      this.dropTargetListener = null;
      this.inputMethodRequests = null;
      this.scenePaintListener = null;
      this.sceneState = null;
   }

   public final AccessControlContext getAccessControlContext() {
      if (this.accessCtrlCtx == null) {
         throw new RuntimeException("Scene security context has not been set!");
      } else {
         return this.accessCtrlCtx;
      }
   }

   public final void setSecurityContext(AccessControlContext var1) {
      if (this.accessCtrlCtx != null) {
         throw new RuntimeException("Scene security context has been already set!");
      } else {
         AccessControlContext var2 = AccessController.getContext();
         this.accessCtrlCtx = (AccessControlContext)javaSecurityAccess.doIntersectionPrivilege(() -> {
            return AccessController.getContext();
         }, var2, var1);
      }
   }

   public void waitForRenderingToComplete() {
      PaintCollector.getInstance().waitForRenderingToComplete();
   }

   public void waitForSynchronization() {
      ViewPainter.renderLock.lock();
   }

   public void releaseSynchronization(boolean var1) {
      if (var1) {
         this.updateSceneState();
      }

      ViewPainter.renderLock.unlock();
   }

   boolean getDepthBuffer() {
      return this.depthBuffer;
   }

   boolean isMSAA() {
      return this.msaa;
   }

   protected abstract boolean isSynchronous();

   public void setTKSceneListener(TKSceneListener var1) {
      this.sceneListener = var1;
   }

   public synchronized void setTKScenePaintListener(TKScenePaintListener var1) {
      this.scenePaintListener = var1;
   }

   public void setTKDropTargetListener(TKDropTargetListener var1) {
      this.dropTargetListener = var1;
   }

   public void setTKDragSourceListener(TKDragSourceListener var1) {
      this.dragSourceListener = var1;
   }

   public void setTKDragGestureListener(TKDragGestureListener var1) {
      this.dragGestureListener = var1;
   }

   public void setInputMethodRequests(InputMethodRequests var1) {
      this.inputMethodRequests = var1;
   }

   public void setRoot(NGNode var1) {
      this.root = var1;
      this.entireSceneNeedsRepaint();
   }

   protected NGNode getRoot() {
      return this.root;
   }

   NGCamera getCamera() {
      return this.camera;
   }

   public NGLightBase[] getLights() {
      return this.lights;
   }

   public void setLights(NGLightBase[] var1) {
      this.lights = var1;
   }

   public void setCamera(NGCamera var1) {
      this.camera = var1 == null ? NGCamera.INSTANCE : var1;
      this.entireSceneNeedsRepaint();
   }

   public void setFillPaint(Object var1) {
      this.fillPaint = (Paint)var1;
      this.entireSceneNeedsRepaint();
   }

   public void setCursor(Object var1) {
   }

   public final void markDirty() {
      this.sceneChanged();
   }

   public void entireSceneNeedsRepaint() {
      if (Platform.isFxApplicationThread()) {
         this.entireSceneDirty = true;
         this.sceneChanged();
      } else {
         Platform.runLater(() -> {
            this.entireSceneDirty = true;
            this.sceneChanged();
         });
      }

   }

   public boolean isEntireSceneDirty() {
      return this.entireSceneDirty;
   }

   public void clearEntireSceneDirty() {
      this.entireSceneDirty = false;
   }

   public TKClipboard createDragboard(boolean var1) {
      ClipboardAssistance var2 = new ClipboardAssistance("DND") {
         public void actionPerformed(int var1) {
            super.actionPerformed(var1);
            AccessController.doPrivileged(() -> {
               try {
                  if (GlassScene.this.dragSourceListener != null) {
                     GlassScene.this.dragSourceListener.dragDropEnd(0.0, 0.0, 0.0, 0.0, QuantumToolkit.clipboardActionToTransferMode(var1));
                  }
               } finally {
                  QuantumClipboard.releaseCurrentDragboard();
               }

               return null;
            }, GlassScene.this.getAccessControlContext());
         }
      };
      return QuantumClipboard.getDragboardInstance(var2, var1);
   }

   protected final GlassStage getStage() {
      return this.stage;
   }

   void setStage(GlassStage var1) {
      this.stage = var1;
      this.sceneChanged();
   }

   final SceneState getSceneState() {
      return this.sceneState;
   }

   final void updateSceneState() {
      this.sceneState.update();
   }

   protected View getPlatformView() {
      return null;
   }

   boolean setPainting(boolean var1) {
      return this.painting.getAndSet(var1);
   }

   void repaint() {
   }

   final void stageVisible(boolean var1) {
      if (!var1 && PrismSettings.forceRepaint) {
         PaintCollector.getInstance().removeDirtyScene(this);
      }

      if (var1) {
         PaintCollector.getInstance().addDirtyScene(this);
      }

   }

   public void sceneChanged() {
      if (this.stage != null) {
         PaintCollector.getInstance().addDirtyScene(this);
      } else {
         PaintCollector.getInstance().removeDirtyScene(this);
      }

   }

   public final synchronized void frameRendered() {
      if (this.scenePaintListener != null) {
         this.scenePaintListener.frameRendered();
      }

   }

   public final synchronized void setDoPresent(boolean var1) {
      this.doPresent = var1;
   }

   public final synchronized boolean getDoPresent() {
      return this.doPresent;
   }

   protected Color getClearColor() {
      WindowStage var1 = this.stage instanceof WindowStage ? (WindowStage)this.stage : null;
      if (var1 != null && var1.getPlatformWindow().isTransparentWindow()) {
         return Color.TRANSPARENT;
      } else if (this.fillPaint == null) {
         return Color.WHITE;
      } else if (this.fillPaint.isOpaque() || var1 != null && var1.getPlatformWindow().isUnifiedWindow()) {
         if (this.fillPaint.getType() == Paint.Type.COLOR) {
            return (Color)this.fillPaint;
         } else {
            return this.depthBuffer ? Color.TRANSPARENT : null;
         }
      } else {
         return Color.WHITE;
      }
   }

   final Paint getCurrentPaint() {
      WindowStage var1 = this.stage instanceof WindowStage ? (WindowStage)this.stage : null;
      if (var1 != null && var1.getStyle() == StageStyle.TRANSPARENT) {
         return Color.TRANSPARENT.equals(this.fillPaint) ? null : this.fillPaint;
      } else {
         return this.fillPaint != null && this.fillPaint.isOpaque() && this.fillPaint.getType() == Paint.Type.COLOR ? null : this.fillPaint;
      }
   }

   public String toString() {
      return " scene: " + this.hashCode() + ")";
   }
}
