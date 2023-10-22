package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.prism.PixelSource;

final class EmbeddedState extends SceneState {
   public EmbeddedState(GlassScene var1) {
      super(var1);
   }

   public void uploadPixels(PixelSource var1) {
      if (this.isValid()) {
         Pixels var2 = var1.getLatestPixels();
         if (var2 != null) {
            try {
               EmbeddedScene var3 = (EmbeddedScene)this.scene;
               var3.uploadPixels(var2);
            } finally {
               var1.doneWithPixels(var2);
            }
         }
      } else {
         var1.skipLatestPixels();
      }

   }

   public boolean isValid() {
      return this.scene != null && this.getWidth() > 0 && this.getHeight() > 0;
   }

   public void update() {
      super.update();
      float var1 = ((EmbeddedScene)this.scene).getRenderScaleX();
      float var2 = ((EmbeddedScene)this.scene).getRenderScaleY();
      this.update(1.0F, 1.0F, var1, var2, var1, var2);
      if (this.scene != null) {
         this.isWindowVisible = true;
         this.isWindowMinimized = false;
      }

   }
}
