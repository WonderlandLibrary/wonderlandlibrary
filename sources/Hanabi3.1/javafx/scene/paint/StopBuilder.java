package javafx.scene.paint;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class StopBuilder implements Builder {
   private Color color;
   private double offset;

   protected StopBuilder() {
      this.color = Color.BLACK;
   }

   public static StopBuilder create() {
      return new StopBuilder();
   }

   public StopBuilder color(Color var1) {
      this.color = var1;
      return this;
   }

   public StopBuilder offset(double var1) {
      this.offset = var1;
      return this;
   }

   public Stop build() {
      Stop var1 = new Stop(this.offset, this.color);
      return var1;
   }
}
