package javafx.animation;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class SequentialTransitionBuilder extends TransitionBuilder implements Builder {
   private int __set;
   private Collection children;
   private Node node;

   protected SequentialTransitionBuilder() {
   }

   public static SequentialTransitionBuilder create() {
      return new SequentialTransitionBuilder();
   }

   public void applyTo(SequentialTransition var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getChildren().addAll(this.children);
      }

      if ((var2 & 2) != 0) {
         var1.setNode(this.node);
      }

   }

   public SequentialTransitionBuilder children(Collection var1) {
      this.children = var1;
      this.__set |= 1;
      return this;
   }

   public SequentialTransitionBuilder children(Animation... var1) {
      return this.children((Collection)Arrays.asList(var1));
   }

   public SequentialTransitionBuilder node(Node var1) {
      this.node = var1;
      this.__set |= 2;
      return this;
   }

   public SequentialTransition build() {
      SequentialTransition var1 = new SequentialTransition();
      this.applyTo(var1);
      return var1;
   }
}
