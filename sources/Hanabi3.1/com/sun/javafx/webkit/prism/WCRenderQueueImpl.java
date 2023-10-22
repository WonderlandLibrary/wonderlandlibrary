package com.sun.javafx.webkit.prism;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;

final class WCRenderQueueImpl extends WCRenderQueue {
   WCRenderQueueImpl(WCGraphicsContext var1) {
      super(var1);
   }

   WCRenderQueueImpl(WCRectangle var1, boolean var2) {
      super(var1, var2);
   }

   protected void flush() {
      if (!this.isEmpty()) {
         PrismInvoker.invokeOnRenderThread(() -> {
            this.decode();
         });
      }

   }

   protected void disposeGraphics() {
      PrismInvoker.invokeOnRenderThread(() -> {
         if (this.gc != null) {
            this.gc.dispose();
         }

      });
   }
}
