package javafx.animation;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class FillTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private Duration duration;
   private Color fromValue;
   private Shape shape;
   private Color toValue;

   protected FillTransitionBuilder() {
   }

   public static FillTransitionBuilder create() {
      return new FillTransitionBuilder();
   }

   public void applyTo(FillTransition var1) {
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

   public FillTransitionBuilder duration(Duration var1) {
      this.duration = var1;
      this.__set |= 1;
      return this;
   }

   public FillTransitionBuilder fromValue(Color var1) {
      this.fromValue = var1;
      this.__set |= 2;
      return this;
   }

   public FillTransitionBuilder shape(Shape var1) {
      this.shape = var1;
      this.__set |= 4;
      return this;
   }

   public FillTransitionBuilder toValue(Color var1) {
      this.toValue = var1;
      this.__set |= 8;
      return this;
   }

   public FillTransition build() {
      FillTransition var1 = new FillTransition();
      this.applyTo(var1);
      return var1;
   }
}
