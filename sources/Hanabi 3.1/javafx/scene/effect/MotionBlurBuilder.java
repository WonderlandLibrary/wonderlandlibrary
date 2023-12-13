package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class MotionBlurBuilder implements Builder {
   private int __set;
   private double angle;
   private Effect input;
   private double radius;

   protected MotionBlurBuilder() {
   }

   public static MotionBlurBuilder create() {
      return new MotionBlurBuilder();
   }

   public void applyTo(MotionBlur var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAngle(this.angle);
      }

      if ((var2 & 2) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 4) != 0) {
         var1.setRadius(this.radius);
      }

   }

   public MotionBlurBuilder angle(double var1) {
      this.angle = var1;
      this.__set |= 1;
      return this;
   }

   public MotionBlurBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 2;
      return this;
   }

   public MotionBlurBuilder radius(double var1) {
      this.radius = var1;
      this.__set |= 4;
      return this;
   }

   public MotionBlur build() {
      MotionBlur var1 = new MotionBlur();
      this.applyTo(var1);
      return var1;
   }
}
