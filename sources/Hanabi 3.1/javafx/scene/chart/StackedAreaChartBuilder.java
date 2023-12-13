package javafx.scene.chart;

/** @deprecated */
@Deprecated
public class StackedAreaChartBuilder extends XYChartBuilder {
   private Axis XAxis;
   private Axis YAxis;

   protected StackedAreaChartBuilder() {
   }

   public static StackedAreaChartBuilder create() {
      return new StackedAreaChartBuilder();
   }

   public StackedAreaChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public StackedAreaChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }

   public StackedAreaChart build() {
      StackedAreaChart var1 = new StackedAreaChart(this.XAxis, this.YAxis);
      this.applyTo(var1);
      return var1;
   }
}
