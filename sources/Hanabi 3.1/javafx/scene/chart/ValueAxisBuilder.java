package javafx.scene.chart;

import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public abstract class ValueAxisBuilder extends AxisBuilder {
   private int __set;
   private double lowerBound;
   private int minorTickCount;
   private double minorTickLength;
   private boolean minorTickVisible;
   private StringConverter tickLabelFormatter;
   private double upperBound;

   protected ValueAxisBuilder() {
   }

   public void applyTo(ValueAxis var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setLowerBound(this.lowerBound);
      }

      if ((var2 & 2) != 0) {
         var1.setMinorTickCount(this.minorTickCount);
      }

      if ((var2 & 4) != 0) {
         var1.setMinorTickLength(this.minorTickLength);
      }

      if ((var2 & 8) != 0) {
         var1.setMinorTickVisible(this.minorTickVisible);
      }

      if ((var2 & 16) != 0) {
         var1.setTickLabelFormatter(this.tickLabelFormatter);
      }

      if ((var2 & 32) != 0) {
         var1.setUpperBound(this.upperBound);
      }

   }

   public ValueAxisBuilder lowerBound(double var1) {
      this.lowerBound = var1;
      this.__set |= 1;
      return this;
   }

   public ValueAxisBuilder minorTickCount(int var1) {
      this.minorTickCount = var1;
      this.__set |= 2;
      return this;
   }

   public ValueAxisBuilder minorTickLength(double var1) {
      this.minorTickLength = var1;
      this.__set |= 4;
      return this;
   }

   public ValueAxisBuilder minorTickVisible(boolean var1) {
      this.minorTickVisible = var1;
      this.__set |= 8;
      return this;
   }

   public ValueAxisBuilder tickLabelFormatter(StringConverter var1) {
      this.tickLabelFormatter = var1;
      this.__set |= 16;
      return this;
   }

   public ValueAxisBuilder upperBound(double var1) {
      this.upperBound = var1;
      this.__set |= 32;
      return this;
   }
}
