package javafx.animation;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class RotateTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private Point3D axis;
   private double byAngle;
   private Duration duration;
   private double fromAngle;
   private Node node;
   private double toAngle;

   protected RotateTransitionBuilder() {
   }

   public static RotateTransitionBuilder create() {
      return new RotateTransitionBuilder();
   }

   public void applyTo(RotateTransition var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAxis(this.axis);
      }

      if ((var2 & 2) != 0) {
         var1.setByAngle(this.byAngle);
      }

      if ((var2 & 4) != 0) {
         var1.setDuration(this.duration);
      }

      if ((var2 & 8) != 0) {
         var1.setFromAngle(this.fromAngle);
      }

      if ((var2 & 16) != 0) {
         var1.setNode(this.node);
      }

      if ((var2 & 32) != 0) {
         var1.setToAngle(this.toAngle);
      }

   }

   public RotateTransitionBuilder axis(Point3D var1) {
      this.axis = var1;
      this.__set |= 1;
      return this;
   }

   public RotateTransitionBuilder byAngle(double var1) {
      this.byAngle = var1;
      this.__set |= 2;
      return this;
   }

   public RotateTransitionBuilder duration(Duration var1) {
      this.duration = var1;
      this.__set |= 4;
      return this;
   }

   public RotateTransitionBuilder fromAngle(double var1) {
      this.fromAngle = var1;
      this.__set |= 8;
      return this;
   }

   public RotateTransitionBuilder node(Node var1) {
      this.node = var1;
      this.__set |= 16;
      return this;
   }

   public RotateTransitionBuilder toAngle(double var1) {
      this.toAngle = var1;
      this.__set |= 32;
      return this;
   }

   public RotateTransition build() {
      RotateTransition var1 = new RotateTransition();
      this.applyTo(var1);
      return var1;
   }
}
