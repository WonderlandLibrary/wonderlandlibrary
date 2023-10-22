package javafx.animation;

import javafx.scene.Node;
import javafx.scene.shape.Shape;
import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class PathTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private Duration duration;
   private Node node;
   private PathTransition.OrientationType orientation;
   private Shape path;

   protected PathTransitionBuilder() {
   }

   public static PathTransitionBuilder create() {
      return new PathTransitionBuilder();
   }

   public void applyTo(PathTransition var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setDuration(this.duration);
      }

      if ((var2 & 2) != 0) {
         var1.setNode(this.node);
      }

      if ((var2 & 4) != 0) {
         var1.setOrientation(this.orientation);
      }

      if ((var2 & 8) != 0) {
         var1.setPath(this.path);
      }

   }

   public PathTransitionBuilder duration(Duration var1) {
      this.duration = var1;
      this.__set |= 1;
      return this;
   }

   public PathTransitionBuilder node(Node var1) {
      this.node = var1;
      this.__set |= 2;
      return this;
   }

   public PathTransitionBuilder orientation(PathTransition.OrientationType var1) {
      this.orientation = var1;
      this.__set |= 4;
      return this;
   }

   public PathTransitionBuilder path(Shape var1) {
      this.path = var1;
      this.__set |= 8;
      return this;
   }

   public PathTransition build() {
      PathTransition var1 = new PathTransition();
      this.applyTo(var1);
      return var1;
   }
}
