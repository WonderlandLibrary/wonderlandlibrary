package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.prism.PresentableState;
import com.sun.prism.PrinterGraphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class PrismPrintGraphics extends J2DPrismGraphics implements PrinterGraphics {
   private AffineTransform origTx2D;

   protected void setTransformG2D(AffineTransform var1) {
      this.g2d.setTransform(this.origTx2D);
      this.g2d.transform(var1);
   }

   protected void captureTransform(Graphics2D var1) {
      this.origTx2D = var1.getTransform();
   }

   public PrismPrintGraphics(Graphics2D var1, int var2, int var3) {
      super(new PagePresentable(var2, var3), var1);
      this.setClipRect(new Rectangle(0, 0, var2, var3));
   }

   PrismPrintGraphics(J2DPresentable var1, Graphics2D var2) {
      super(var1, var2);
   }

   static class PagePresentable extends J2DPresentable {
      private int width;
      private int height;
      static J2DResourceFactory factory = new PrintResourceFactory();
      private boolean opaque;

      PagePresentable(int var1, int var2) {
         super((BufferedImage)null, factory);
         this.width = var1;
         this.height = var2;
      }

      public BufferedImage createBuffer(int var1, int var2) {
         throw new UnsupportedOperationException("cannot create new buffers for image");
      }

      public boolean lockResources(PresentableState var1) {
         return false;
      }

      public boolean prepare(Rectangle var1) {
         throw new UnsupportedOperationException("Cannot prepare an image");
      }

      public boolean present() {
         throw new UnsupportedOperationException("Cannot present on image");
      }

      public int getContentWidth() {
         return this.width;
      }

      public int getContentHeight() {
         return this.height;
      }

      public void setOpaque(boolean var1) {
         this.opaque = var1;
      }

      public boolean isOpaque() {
         return this.opaque;
      }
   }

   static class PrintResourceFactory extends J2DResourceFactory {
      PrintResourceFactory() {
         super((Screen)null);
      }

      J2DPrismGraphics createJ2DPrismGraphics(J2DPresentable var1, Graphics2D var2) {
         PrismPrintGraphics var3 = new PrismPrintGraphics(var1, var2);
         Rectangle var4 = new Rectangle(0, 0, var1.getContentWidth(), var1.getContentHeight());
         var3.setClipRect(var4);
         return var3;
      }
   }
}
