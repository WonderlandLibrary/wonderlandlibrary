package javafx.scene.chart;

import javafx.geometry.Side;
import javafx.scene.layout.RegionBuilder;

/** @deprecated */
@Deprecated
public abstract class ChartBuilder extends RegionBuilder {
   private int __set;
   private boolean animated;
   private Side legendSide;
   private boolean legendVisible;
   private String title;
   private Side titleSide;

   protected ChartBuilder() {
   }

   public void applyTo(Chart var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAnimated(this.animated);
      }

      if ((var2 & 2) != 0) {
         var1.setLegendSide(this.legendSide);
      }

      if ((var2 & 4) != 0) {
         var1.setLegendVisible(this.legendVisible);
      }

      if ((var2 & 8) != 0) {
         var1.setTitle(this.title);
      }

      if ((var2 & 16) != 0) {
         var1.setTitleSide(this.titleSide);
      }

   }

   public ChartBuilder animated(boolean var1) {
      this.animated = var1;
      this.__set |= 1;
      return this;
   }

   public ChartBuilder legendSide(Side var1) {
      this.legendSide = var1;
      this.__set |= 2;
      return this;
   }

   public ChartBuilder legendVisible(boolean var1) {
      this.legendVisible = var1;
      this.__set |= 4;
      return this;
   }

   public ChartBuilder title(String var1) {
      this.title = var1;
      this.__set |= 8;
      return this;
   }

   public ChartBuilder titleSide(Side var1) {
      this.titleSide = var1;
      this.__set |= 16;
      return this;
   }
}
