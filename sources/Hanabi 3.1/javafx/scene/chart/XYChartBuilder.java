package javafx.scene.chart;

import javafx.collections.ObservableList;

/** @deprecated */
@Deprecated
public abstract class XYChartBuilder extends ChartBuilder {
   private int __set;
   private boolean alternativeColumnFillVisible;
   private boolean alternativeRowFillVisible;
   private ObservableList data;
   private boolean horizontalGridLinesVisible;
   private boolean horizontalZeroLineVisible;
   private boolean verticalGridLinesVisible;
   private boolean verticalZeroLineVisible;
   private Axis XAxis;
   private Axis YAxis;

   protected XYChartBuilder() {
   }

   public void applyTo(XYChart var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAlternativeColumnFillVisible(this.alternativeColumnFillVisible);
      }

      if ((var2 & 2) != 0) {
         var1.setAlternativeRowFillVisible(this.alternativeRowFillVisible);
      }

      if ((var2 & 4) != 0) {
         var1.setData(this.data);
      }

      if ((var2 & 8) != 0) {
         var1.setHorizontalGridLinesVisible(this.horizontalGridLinesVisible);
      }

      if ((var2 & 16) != 0) {
         var1.setHorizontalZeroLineVisible(this.horizontalZeroLineVisible);
      }

      if ((var2 & 32) != 0) {
         var1.setVerticalGridLinesVisible(this.verticalGridLinesVisible);
      }

      if ((var2 & 64) != 0) {
         var1.setVerticalZeroLineVisible(this.verticalZeroLineVisible);
      }

   }

   public XYChartBuilder alternativeColumnFillVisible(boolean var1) {
      this.alternativeColumnFillVisible = var1;
      this.__set |= 1;
      return this;
   }

   public XYChartBuilder alternativeRowFillVisible(boolean var1) {
      this.alternativeRowFillVisible = var1;
      this.__set |= 2;
      return this;
   }

   public XYChartBuilder data(ObservableList var1) {
      this.data = var1;
      this.__set |= 4;
      return this;
   }

   public XYChartBuilder horizontalGridLinesVisible(boolean var1) {
      this.horizontalGridLinesVisible = var1;
      this.__set |= 8;
      return this;
   }

   public XYChartBuilder horizontalZeroLineVisible(boolean var1) {
      this.horizontalZeroLineVisible = var1;
      this.__set |= 16;
      return this;
   }

   public XYChartBuilder verticalGridLinesVisible(boolean var1) {
      this.verticalGridLinesVisible = var1;
      this.__set |= 32;
      return this;
   }

   public XYChartBuilder verticalZeroLineVisible(boolean var1) {
      this.verticalZeroLineVisible = var1;
      this.__set |= 64;
      return this;
   }

   public XYChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public XYChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }
}
