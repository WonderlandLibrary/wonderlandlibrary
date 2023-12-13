package javafx.animation;

import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class ScaleTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private double byX;
   private double byY;
   private double byZ;
   private Duration duration;
   private double fromX;
   private double fromY;
   private double fromZ;
   private Node node;
   private double toX;
   private double toY;
   private double toZ;

   protected ScaleTransitionBuilder() {
   }

   public static ScaleTransitionBuilder create() {
      return new ScaleTransitionBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(ScaleTransition var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setByX(this.byX);
               break;
            case 1:
               var1.setByY(this.byY);
               break;
            case 2:
               var1.setByZ(this.byZ);
               break;
            case 3:
               var1.setDuration(this.duration);
               break;
            case 4:
               var1.setFromX(this.fromX);
               break;
            case 5:
               var1.setFromY(this.fromY);
               break;
            case 6:
               var1.setFromZ(this.fromZ);
               break;
            case 7:
               var1.setNode(this.node);
               break;
            case 8:
               var1.setToX(this.toX);
               break;
            case 9:
               var1.setToY(this.toY);
               break;
            case 10:
               var1.setToZ(this.toZ);
         }
      }

   }

   public ScaleTransitionBuilder byX(double var1) {
      this.byX = var1;
      this.__set(0);
      return this;
   }

   public ScaleTransitionBuilder byY(double var1) {
      this.byY = var1;
      this.__set(1);
      return this;
   }

   public ScaleTransitionBuilder byZ(double var1) {
      this.byZ = var1;
      this.__set(2);
      return this;
   }

   public ScaleTransitionBuilder duration(Duration var1) {
      this.duration = var1;
      this.__set(3);
      return this;
   }

   public ScaleTransitionBuilder fromX(double var1) {
      this.fromX = var1;
      this.__set(4);
      return this;
   }

   public ScaleTransitionBuilder fromY(double var1) {
      this.fromY = var1;
      this.__set(5);
      return this;
   }

   public ScaleTransitionBuilder fromZ(double var1) {
      this.fromZ = var1;
      this.__set(6);
      return this;
   }

   public ScaleTransitionBuilder node(Node var1) {
      this.node = var1;
      this.__set(7);
      return this;
   }

   public ScaleTransitionBuilder toX(double var1) {
      this.toX = var1;
      this.__set(8);
      return this;
   }

   public ScaleTransitionBuilder toY(double var1) {
      this.toY = var1;
      this.__set(9);
      return this;
   }

   public ScaleTransitionBuilder toZ(double var1) {
      this.toZ = var1;
      this.__set(10);
      return this;
   }

   public ScaleTransition build() {
      ScaleTransition var1 = new ScaleTransition();
      this.applyTo(var1);
      return var1;
   }
}
