package javafx.scene.chart;

/** @deprecated */
@Deprecated
public class StackedBarChartBuilder extends XYChartBuilder {
   private boolean __set;
   private double categoryGap;
   private Axis XAxis;
   private Axis YAxis;

   protected StackedBarChartBuilder() {
   }

   public static StackedBarChartBuilder create() {
      return new StackedBarChartBuilder();
   }

   public void applyTo(StackedBarChart var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setCategoryGap(this.categoryGap);
      }

   }

   public StackedBarChartBuilder categoryGap(double var1) {
      this.categoryGap = var1;
      this.__set = true;
      return this;
   }

   public StackedBarChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public StackedBarChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }

   public StackedBarChart build() {
      StackedBarChart var1 = new StackedBarChart(this.XAxis, this.YAxis);
      this.applyTo(var1);
      return var1;
   }
}
