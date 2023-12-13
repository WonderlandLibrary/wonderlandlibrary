package javafx.scene.paint;

import java.util.Arrays;
import java.util.List;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class LinearGradientBuilder implements Builder {
   private CycleMethod cycleMethod;
   private double endX = 1.0;
   private double endY = 1.0;
   private boolean proportional = true;
   private double startX;
   private double startY;
   private List stops;

   protected LinearGradientBuilder() {
   }

   public static LinearGradientBuilder create() {
      return new LinearGradientBuilder();
   }

   public LinearGradientBuilder cycleMethod(CycleMethod var1) {
      this.cycleMethod = var1;
      return this;
   }

   public LinearGradientBuilder endX(double var1) {
      this.endX = var1;
      return this;
   }

   public LinearGradientBuilder endY(double var1) {
      this.endY = var1;
      return this;
   }

   public LinearGradientBuilder proportional(boolean var1) {
      this.proportional = var1;
      return this;
   }

   public LinearGradientBuilder startX(double var1) {
      this.startX = var1;
      return this;
   }

   public LinearGradientBuilder startY(double var1) {
      this.startY = var1;
      return this;
   }

   public LinearGradientBuilder stops(List var1) {
      this.stops = var1;
      return this;
   }

   public LinearGradientBuilder stops(Stop... var1) {
      return this.stops(Arrays.asList(var1));
   }

   public LinearGradient build() {
      LinearGradient var1 = new LinearGradient(this.startX, this.startY, this.endX, this.endY, this.proportional, this.cycleMethod, this.stops);
      return var1;
   }
}
