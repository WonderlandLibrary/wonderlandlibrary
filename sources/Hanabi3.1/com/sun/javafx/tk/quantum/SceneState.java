package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.PixelSource;
import com.sun.prism.PresentableState;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

class SceneState extends PresentableState {
   final GlassScene scene;
   private Color clearColor;
   private Paint currentPaint;
   private NGCamera camera;

   public SceneState(GlassScene var1) {
      this.scene = var1;
   }

   public boolean isMSAA() {
      return this.scene.isMSAA();
   }

   public GlassScene getScene() {
      return this.scene;
   }

   public boolean isValid() {
      return this.getWindow() != null && this.getView() != null && !this.isViewClosed() && this.getWidth() > 0 && this.getHeight() > 0;
   }

   public void update() {
      this.view = this.scene.getPlatformView();
      this.clearColor = this.scene.getClearColor();
      this.currentPaint = this.scene.getCurrentPaint();
      super.update();
      this.camera = this.scene.getCamera();
      if (this.camera != null) {
         this.viewWidth = (int)this.camera.getViewWidth();
         this.viewHeight = (int)this.camera.getViewHeight();
      }

   }

   public void uploadPixels(PixelSource var1) {
      Application.invokeLater(() -> {
         if (this.isValid()) {
            access$001(this, var1);
         } else {
            var1.skipLatestPixels();
         }

      });
   }

   Color getClearColor() {
      return this.clearColor;
   }

   Paint getCurrentPaint() {
      return this.currentPaint;
   }

   NGCamera getCamera() {
      return this.camera;
   }

   // $FF: synthetic method
   static void access$001(SceneState var0, PixelSource var1) {
      var0.uploadPixels(var1);
   }
}
