package javafx.scene.chart;

/** @deprecated */
@Deprecated
public class LineChartBuilder extends XYChartBuilder {
   private boolean __set;
   private boolean createSymbols;
   private Axis XAxis;
   private Axis YAxis;

   protected LineChartBuilder() {
   }

   public static LineChartBuilder create() {
      return new LineChartBuilder();
   }

   public void applyTo(LineChart var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setCreateSymbols(this.createSymbols);
      }

   }

   public LineChartBuilder createSymbols(boolean var1) {
      this.createSymbols = var1;
      this.__set = true;
      return this;
   }

   public LineChartBuilder XAxis(Axis var1) {
      this.XAxis = var1;
      return this;
   }

   public LineChartBuilder YAxis(Axis var1) {
      this.YAxis = var1;
      return this;
   }

   public LineChart build() {
      LineChart var1 = new LineChart(this.XAxis, this.YAxis);
      this.applyTo(var1);
      return var1;
   }
}
