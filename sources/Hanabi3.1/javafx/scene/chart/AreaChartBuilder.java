package javafx.scene.chart;

/** @deprecated */
@Deprecated
public class AreaChartBuilder extends XYChartBuilder {
   private Axis XAxis;
   private Axis YAxis;

   protected AreaChartBuilder() {
   }

   public static AreaChartBuilder create() {
      return new AreaChartBuilder();
   }

   public AreaChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public AreaChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }

   public AreaChart build() {
      AreaChart var1 = new AreaChart(this.XAxis, this.YAxis);
      this.applyTo(var1);
      return var1;
   }
}
