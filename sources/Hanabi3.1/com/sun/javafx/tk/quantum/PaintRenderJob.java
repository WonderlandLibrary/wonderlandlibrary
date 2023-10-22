package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;

class PaintRenderJob extends RenderJob {
   private GlassScene scene;

   public PaintRenderJob(GlassScene var1, CompletionListener var2, Runnable var3) {
      super(var3, var2);
      this.scene = var1;
   }

   public GlassScene getScene() {
      return this.scene;
   }
}
