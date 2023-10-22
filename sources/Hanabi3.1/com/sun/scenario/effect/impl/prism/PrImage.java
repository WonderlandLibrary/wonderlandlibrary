package com.sun.scenario.effect.impl.prism;

import com.sun.prism.Image;
import com.sun.scenario.effect.Filterable;

public class PrImage implements Filterable {
   private final Image image;

   private PrImage(Image var1) {
      this.image = var1;
   }

   public static PrImage create(Image var0) {
      return new PrImage(var0);
   }

   public Image getImage() {
      return this.image;
   }

   public Object getData() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public int getContentWidth() {
      return this.image.getWidth();
   }

   public int getContentHeight() {
      return this.image.getHeight();
   }

   public int getPhysicalWidth() {
      return this.image.getWidth();
   }

   public int getPhysicalHeight() {
      return this.image.getHeight();
   }

   public float getPixelScale() {
      return this.image.getPixelScale();
   }

   public int getMaxContentWidth() {
      return this.image.getWidth();
   }

   public int getMaxContentHeight() {
      return this.image.getHeight();
   }

   public void setContentWidth(int var1) {
      throw new UnsupportedOperationException("Not supported.");
   }

   public void setContentHeight(int var1) {
      throw new UnsupportedOperationException("Not supported");
   }

   public void lock() {
   }

   public void unlock() {
   }

   public boolean isLost() {
      return false;
   }

   public void flush() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
