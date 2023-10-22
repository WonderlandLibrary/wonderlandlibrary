package com.sun.javafx.sg.prism.web;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.prism.Graphics;
import com.sun.prism.PrinterGraphics;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCRectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NGWebView extends NGGroup {
   private static final Logger log = Logger.getLogger(NGWebView.class.getName());
   private volatile WebPage page;
   private volatile float width;
   private volatile float height;

   public void setPage(WebPage var1) {
      this.page = var1;
   }

   public void resize(float var1, float var2) {
      if (this.width != var1 || this.height != var2) {
         this.width = var1;
         this.height = var2;
         this.geometryChanged();
         if (this.page != null) {
            this.page.setBounds(0, 0, (int)var1, (int)var2);
         }
      }

   }

   public void update() {
      if (this.page != null) {
         BaseBounds var1 = this.getClippedBounds(new RectBounds(), BaseTransform.IDENTITY_TRANSFORM);
         if (!var1.isEmpty()) {
            log.log(Level.FINEST, "updating rectangle: {0}", var1);
            this.page.updateContent(new WCRectangle(var1.getMinX(), var1.getMinY(), var1.getWidth(), var1.getHeight()));
         }
      }

   }

   public void requestRender() {
      this.visualsChanged();
   }

   protected void renderContent(Graphics var1) {
      log.log(Level.FINEST, "rendering into {0}", var1);
      if (var1 != null && this.page != null && !(this.width <= 0.0F) && !(this.height <= 0.0F)) {
         WCGraphicsContext var2 = WCGraphicsManager.getGraphicsManager().createGraphicsContext(var1);

         try {
            if (var1 instanceof PrinterGraphics) {
               this.page.print(var2, 0, 0, (int)this.width, (int)this.height);
            } else {
               this.page.paint(var2, 0, 0, (int)this.width, (int)this.height);
            }

            var2.flush();
         } finally {
            var2.dispose();
         }

      }
   }

   public boolean hasOverlappingContents() {
      return false;
   }

   protected boolean hasVisuals() {
      return true;
   }
}
