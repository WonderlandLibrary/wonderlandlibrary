package javafx.scene.chart;

/** @deprecated */
@Deprecated
public final class NumberAxisBuilder extends ValueAxisBuilder {
   private int __set;
   private boolean forceZeroInRange;
   private double tickUnit;

   protected NumberAxisBuilder() {
   }

   public static NumberAxisBuilder create() {
      return new NumberAxisBuilder();
   }

   public void applyTo(NumberAxis var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setForceZeroInRange(this.forceZeroInRange);
      }

      if ((var2 & 2) != 0) {
         var1.setTickUnit(this.tickUnit);
      }

   }

   public NumberAxisBuilder forceZeroInRange(boolean var1) {
      this.forceZeroInRange = var1;
      this.__set |= 1;
      return this;
   }

   public NumberAxisBuilder tickUnit(double var1) {
      this.tickUnit = var1;
      this.__set |= 2;
      return this;
   }

   public NumberAxis build() {
      NumberAxis var1 = new NumberAxis();
      this.applyTo(var1);
      return var1;
   }
}
