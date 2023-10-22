package javafx.scene.layout;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;

/** @deprecated */
@Deprecated
public class PaneBuilder extends RegionBuilder {
   private boolean __set;
   private Collection children;

   protected PaneBuilder() {
   }

   public static PaneBuilder create() {
      return new PaneBuilder();
   }

   public void applyTo(Pane var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.getChildren().addAll(this.children);
      }

   }

   public PaneBuilder children(Collection var1) {
      this.children = var1;
      this.__set = true;
      return this;
   }

   public PaneBuilder children(Node... var1) {
      return this.children((Collection)Arrays.asList(var1));
   }

   public Pane build() {
      Pane var1 = new Pane();
      this.applyTo(var1);
      return var1;
   }
}
