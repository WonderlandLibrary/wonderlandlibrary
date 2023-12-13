package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.GraphicsPipeline;
import java.nio.ByteOrder;

class ViewScene extends GlassScene {
   private static final String UNSUPPORTED_FORMAT = "Transparent windows only supported for BYTE_BGRA_PRE format on LITTLE_ENDIAN machines";
   private View platformView = Application.GetApplication().createView();
   private ViewPainter painter;
   private PaintRenderJob paintRenderJob;

   public ViewScene(boolean var1, boolean var2) {
      super(var1, var2);
      this.platformView.setEventHandler(new GlassViewEventHandler(this));
   }

   protected boolean isSynchronous() {
      return this.painter != null && this.painter instanceof PresentingPainter;
   }

   protected View getPlatformView() {
      return this.platformView;
   }

   ViewPainter getPainter() {
      return this.painter;
   }

   public void setStage(GlassStage var1) {
      super.setStage(var1);
      if (var1 != null) {
         WindowStage var2 = (WindowStage)var1;
         if (!var2.needsUpdateWindow() && !GraphicsPipeline.getPipeline().isUploading()) {
            this.painter = new PresentingPainter(this);
         } else {
            if (Pixels.getNativeFormat() != 1 || ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
               throw new UnsupportedOperationException("Transparent windows only supported for BYTE_BGRA_PRE format on LITTLE_ENDIAN machines");
            }

            this.painter = new UploadingPainter(this);
         }

         this.painter.setRoot(this.getRoot());
         this.paintRenderJob = new PaintRenderJob(this, PaintCollector.getInstance().getRendered(), this.painter);
      }

   }

   WindowStage getWindowStage() {
      return (WindowStage)this.getStage();
   }

   public void dispose() {
      if (this.platformView != null) {
         QuantumToolkit.runWithRenderLock(() -> {
            this.platformView.close();
            this.platformView = null;
            this.updateSceneState();
            this.painter = null;
            this.paintRenderJob = null;
            return null;
         });
      }

      super.dispose();
   }

   public void setRoot(NGNode var1) {
      super.setRoot(var1);
      if (this.painter != null) {
         this.painter.setRoot(var1);
      }

   }

   public void setCursor(Object var1) {
      super.setCursor(var1);
      Application.invokeLater(() -> {
         CursorFrame var2 = (CursorFrame)var1;
         Cursor var3 = CursorUtils.getPlatformCursor(var2);
         if (this.platformView != null) {
            Window var4 = this.platformView.getWindow();
            if (var4 != null) {
               var4.setCursor(var3);
            }
         }

      });
   }

   void repaint() {
      if (this.platformView != null) {
         if (!this.setPainting(true)) {
            Toolkit var1 = Toolkit.getToolkit();
            var1.addRenderJob(this.paintRenderJob);
         }

      }
   }

   public void enableInputMethodEvents(boolean var1) {
      this.platformView.enableInputMethodEvents(var1);
   }

   public void finishInputMethodComposition() {
      this.platformView.finishInputMethodComposition();
   }

   public String toString() {
      View var1 = this.getPlatformView();
      return " scene: " + this.hashCode() + " @ (" + var1.getWidth() + "," + var1.getHeight() + ")";
   }

   void synchroniseOverlayWarning() {
      try {
         this.waitForSynchronization();
         OverlayWarning var1 = this.getWindowStage().getWarning();
         if (var1 == null) {
            this.painter.setOverlayRoot((NGNode)null);
         } else {
            this.painter.setOverlayRoot(var1.impl_getPeer());
            var1.updateBounds();
            var1.impl_updatePeer();
         }
      } finally {
         this.releaseSynchronization(true);
         this.entireSceneNeedsRepaint();
      }

   }
}
