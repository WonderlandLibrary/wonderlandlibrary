package javafx.scene.chart;

/** @deprecated */
@Deprecated
public class BubbleChartBuilder extends XYChartBuilder {
   private Axis XAxis;
   private Axis YAxis;

   protected BubbleChartBuilder() {
   }

   public static BubbleChartBuilder create() {
      return new BubbleChartBuilder();
   }

   public BubbleChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public BubbleChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }

   public BubbleChart build() {
      BubbleChart var1 = new BubbleChart(this.XAxis, this.YAxis);
      this.applyTo(var1);
      return var1;
   }
}
