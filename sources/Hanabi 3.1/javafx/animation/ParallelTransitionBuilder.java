package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class ParallelTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private Collection children;
   private Node node;

   protected ParallelTransitionBuilder() {
   }

   public static ParallelTransitionBuilder create() {
      return new ParallelTransitionBuilder();
   }

   public void applyTo(ParallelTransition var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getChildren().addAll(this.children);
      }

      if ((var2 & 2) != 0) {
         var1.setNode(this.node);
      }

   }

   public ParallelTransitionBuilder children(Collection var1) {
      this.children = var1;
      this.__set |= 1;
      return this;
   }

   public ParallelTransitionBuilder children(Animation... var1) {
      return this.children((Collection)Arrays.asList(var1));
   }

   public ParallelTransitionBuilder node(Node var1) {
      this.node = var1;
      this.__set |= 2;
      return this;
   }

   public ParallelTransition build() {
      ParallelTransition var1 = new ParallelTransition();
      this.applyTo(var1);
      return var1;
   }
}
