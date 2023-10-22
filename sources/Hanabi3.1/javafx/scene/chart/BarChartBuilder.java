package javafx.scene.chart;

/** @deprecated */
@Deprecated
public class BarChartBuilder extends XYChartBuilder {
   private int __set;
   private double barGap;
   private double categoryGap;
   private Axis XAxis;
   private Axis YAxis;

   protected BarChartBuilder() {
   }

   public static BarChartBuilder create() {
      return new BarChartBuilder();
   }

   public void applyTo(BarChart var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBarGap(this.barGap);
      }

      if ((var2 & 2) != 0) {
         var1.setCategoryGap(this.categoryGap);
      }

   }

   public BarChartBuilder barGap(double var1) {
      this.barGap = var1;
      this.__set |= 1;
      return this;
   }

   public BarChartBuilder categoryGap(double var1) {
      this.categoryGap = var1;
      this.__set |= 2;
      return this;
   }

   public BarChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public BarChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }

   public BarChart build() {
      BarChart var1 = new BarChart(this.XAxis, this.YAxis);
      this.applyTo(var1);
      return var1;
   }
}
