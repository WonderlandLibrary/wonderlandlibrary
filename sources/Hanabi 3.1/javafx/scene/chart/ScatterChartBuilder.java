package javafx.scene.chart;

/** @deprecated */
@Deprecated
public class ScatterChartBuilder extends XYChartBuilder {
   private Axis XAxis;
   private Axis YAxis;

   protected ScatterChartBuilder() {
   }

   public static ScatterChartBuilder create() {
      return new ScatterChartBuilder();
   }

   public ScatterChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public ScatterChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }

   public ScatterChart build() {
      ScatterChart var1 = new ScatterChart(this.XAxis, this.YAxis);
      this.applyTo(var1);
      return var1;
   }
}
