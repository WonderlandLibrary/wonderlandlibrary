package javafx.scene.control;

import javafx.geometry.Orientation;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ScrollBarBuilder extends ControlBuilder implements Builder {
   private int __set;
   private double blockIncrement;
   private double max;
   private double min;
   private Orientation orientation;
   private double unitIncrement;
   private double value;
   private double visibleAmount;

   protected ScrollBarBuilder() {
   }

   public static ScrollBarBuilder create() {
      return new ScrollBarBuilder();
   }

   public void applyTo(ScrollBar var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBlockIncrement(this.blockIncrement);
      }

      if ((var2 & 2) != 0) {
         var1.setMax(this.max);
      }

      if ((var2 & 4) != 0) {
         var1.setMin(this.min);
      }

      if ((var2 & 8) != 0) {
         var1.setOrientation(this.orientation);
      }

      if ((var2 & 16) != 0) {
         var1.setUnitIncrement(this.unitIncrement);
      }

      if ((var2 & 32) != 0) {
         var1.setValue(this.value);
      }

      if ((var2 & 64) != 0) {
         var1.setVisibleAmount(this.visibleAmount);
      }

   }

   public ScrollBarBuilder blockIncrement(double var1) {
      this.blockIncrement = var1;
      this.__set |= 1;
      return this;
   }

   public ScrollBarBuilder max(double var1) {
      this.max = var1;
      this.__set |= 2;
      return this;
   }

   public ScrollBarBuilder min(double var1) {
      this.min = var1;
      this.__set |= 4;
      return this;
   }

   public ScrollBarBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set |= 8;
      return this;
   }

   public ScrollBarBuilder unitIncrement(double var1) {
      this.unitIncrement = var1;
      this.__set |= 16;
      return this;
   }

   public ScrollBarBuilder value(double var1) {
      this.value = var1;
      this.__set |= 32;
      return this;
   }

   public ScrollBarBuilder visibleAmount(double var1) {
      this.visibleAmount = var1;
      this.__set |= 64;
      return this;
   }

   public ScrollBar build() {
      ScrollBar var1 = new ScrollBar();
      this.applyTo(var1);
      return var1;
   }
}
