package javafx.animation;

import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class FadeTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private double byValue;
   private Duration duration;
   private double fromValue;
   private Node node;
   private double toValue;

   protected FadeTransitionBuilder() {
   }

   public static FadeTransitionBuilder create() {
      return new FadeTransitionBuilder();
   }

   public void applyTo(FadeTransition var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setByValue(this.byValue);
      }

      if ((var2 & 2) != 0) {
         var1.setDuration(this.duration);
      }

      if ((var2 & 4) != 0) {
         var1.setFromValue(this.fromValue);
      }

      if ((var2 & 8) != 0) {
         var1.setNode(this.node);
      }

      if ((var2 & 16) != 0) {
         var1.setToValue(this.toValue);
      }

   }

   public FadeTransitionBuilder byValue(double var1) {
      this.byValue = var1;
      this.__set |= 1;
      return this;
   }

   public FadeTransitionBuilder duration(Duration var1) {
      this.duration = var1;
      this.__set |= 2;
      return this;
   }

   public FadeTransitionBuilder fromValue(double var1) {
      this.fromValue = var1;
      this.__set |= 4;
      return this;
   }

   public FadeTransitionBuilder node(Node var1) {
      this.node = var1;
      this.__set |= 8;
      return this;
   }

   public FadeTransitionBuilder toValue(double var1) {
      this.toValue = var1;
      this.__set |= 16;
      return this;
   }

   public FadeTransition build() {
      FadeTransition var1 = new FadeTransition();
      this.applyTo(var1);
      return var1;
   }
}
