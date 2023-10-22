package javafx.scene.chart;

import javafx.collections.ObservableList;

/** @deprecated */
@Deprecated
public class PieChartBuilder extends ChartBuilder {
   private int __set;
   private boolean clockwise;
   private ObservableList data;
   private double labelLineLength;
   private boolean labelsVisible;
   private double startAngle;

   protected PieChartBuilder() {
   }

   public static PieChartBuilder create() {
      return new PieChartBuilder();
   }

   public void applyTo(PieChart var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setClockwise(this.clockwise);
      }

      if ((var2 & 2) != 0) {
         var1.setData(this.data);
      }

      if ((var2 & 4) != 0) {
         var1.setLabelLineLength(this.labelLineLength);
      }

      if ((var2 & 8) != 0) {
         var1.setLabelsVisible(this.labelsVisible);
      }

      if ((var2 & 16) != 0) {
         var1.setStartAngle(this.startAngle);
      }

   }

   public PieChartBuilder clockwise(boolean var1) {
      this.clockwise = var1;
      this.__set |= 1;
      return this;
   }

   public PieChartBuilder data(ObservableList var1) {
      this.data = var1;
      this.__set |= 2;
      return this;
   }

   public PieChartBuilder labelLineLength(double var1) {
      this.labelLineLength = var1;
      this.__set |= 4;
      return this;
   }

   public PieChartBuilder labelsVisible(boolean var1) {
      this.labelsVisible = var1;
      this.__set |= 8;
      return this;
   }

   public PieChartBuilder startAngle(double var1) {
      this.startAngle = var1;
      this.__set |= 16;
      return this;
   }

   public PieChart build() {
      PieChart var1 = new PieChart();
      this.applyTo(var1);
      return var1;
   }
}
