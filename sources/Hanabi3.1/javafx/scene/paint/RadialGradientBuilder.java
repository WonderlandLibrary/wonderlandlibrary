package javafx.scene.paint;

import java.util.Arrays;
import java.util.List;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class RadialGradientBuilder implements Builder {
   private double centerX;
   private double centerY;
   private CycleMethod cycleMethod;
   private double focusAngle;
   private double focusDistance;
   private boolean proportional = true;
   private double radius = 1.0;
   private List stops;

   protected RadialGradientBuilder() {
   }

   public static RadialGradientBuilder create() {
      return new RadialGradientBuilder();
   }

   public RadialGradientBuilder centerX(double var1) {
      this.centerX = var1;
      return this;
   }

   public RadialGradientBuilder centerY(double var1) {
      this.centerY = var1;
      return this;
   }

   public RadialGradientBuilder cycleMethod(CycleMethod var1) {
      this.cycleMethod = var1;
      return this;
   }

   public RadialGradientBuilder focusAngle(double var1) {
      this.focusAngle = var1;
      return this;
   }

   public RadialGradientBuilder focusDistance(double var1) {
      this.focusDistance = var1;
      return this;
   }

   public RadialGradientBuilder proportional(boolean var1) {
      this.proportional = var1;
      return this;
   }

   public RadialGradientBuilder radius(double var1) {
      this.radius = var1;
      return this;
   }

   public RadialGradientBuilder stops(List var1) {
      this.stops = var1;
      return this;
   }

   public RadialGradientBuilder stops(Stop... var1) {
      return this.stops(Arrays.asList(var1));
   }

   public RadialGradient build() {
      RadialGradient var1 = new RadialGradient(this.focusAngle, this.focusDistance, this.centerX, this.centerY, this.radius, this.proportional, this.cycleMethod, this.stops);
      return var1;
   }
}
