package javafx.animation;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class StrokeTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private Duration duration;
   private Color fromValue;
   private Shape shape;
   private Color toValue;

   protected StrokeTransitionBuilder() {
   }

   public static StrokeTransitionBuilder create() {
      return new StrokeTransitionBuilder();
   }

   public void applyTo(StrokeTransition var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setDuration(this.duration);
      }

      if ((var2 & 2) != 0) {
         var1.setFromValue(this.fromValue);
      }

      if ((var2 & 4) != 0) {
         var1.setShape(this.shape);
      }

      if ((var2 & 8) != 0) {
         var1.setToValue(this.toValue);
      }

   }

   public StrokeTransitionBuilder duration(Duration var1) {
      this.duration = var1;
      this.__set |= 1;
      return this;
   }

   public StrokeTransitionBuilder fromValue(Color var1) {
      this.fromValue = var1;
      this.__set |= 2;
      return this;
   }

   public StrokeTransitionBuilder shape(Shape var1) {
      this.shape = var1;
      this.__set |= 4;
      return this;
   }

   public StrokeTransitionBuilder toValue(Color var1) {
      this.toValue = var1;
      this.__set |= 8;
      return this;
   }

   public StrokeTransition build() {
      StrokeTransition var1 = new StrokeTransition();
      this.applyTo(var1);
      return var1;
   }
}
