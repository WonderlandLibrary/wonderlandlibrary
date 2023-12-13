package com.sun.javafx.scene.control.skin;

import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollBar;

public class VirtualScrollBar extends ScrollBar {
   private final VirtualFlow flow;
   private boolean virtual;
   private boolean adjusting;

   public VirtualScrollBar(VirtualFlow var1) {
      this.flow = var1;
      super.valueProperty().addListener((var2) -> {
         if (this.isVirtual() && !this.adjusting) {
            var1.setPosition(this.getValue());
         }

      });
   }

   public void setVirtual(boolean var1) {
      this.virtual = var1;
   }

   public boolean isVirtual() {
      return this.virtual;
   }

   public void decrement() {
      if (this.isVirtual()) {
         this.flow.adjustPixels(-10.0);
      } else {
         super.decrement();
      }

   }

   public void increment() {
      if (this.isVirtual()) {
         this.flow.adjustPixels(10.0);
      } else {
         super.increment();
      }

   }

   public void adjustValue(double var1) {
      if (this.isVirtual()) {
         this.adjusting = true;
         double var3 = this.flow.getPosition();
         double var5 = (this.getMax() - this.getMin()) * com.sun.javafx.util.Utils.clamp(0.0, var1, 1.0) + this.getMin();
         IndexedCell var7;
         if (var5 < var3) {
            var7 = this.flow.getFirstVisibleCell();
            if (var7 == null) {
               return;
            }

            this.flow.showAsLast(var7);
         } else if (var5 > var3) {
            var7 = this.flow.getLastVisibleCell();
            if (var7 == null) {
               return;
            }

            this.flow.showAsFirst(var7);
         }

         this.adjusting = false;
      } else {
         super.adjustValue(var1);
      }

   }
}
