package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ToolBarBuilder extends ControlBuilder implements Builder {
   private int __set;
   private Collection items;
   private Orientation orientation;

   protected ToolBarBuilder() {
   }

   public static ToolBarBuilder create() {
      return new ToolBarBuilder();
   }

   public void applyTo(ToolBar var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getItems().addAll(this.items);
      }

      if ((var2 & 2) != 0) {
         var1.setOrientation(this.orientation);
      }

   }

   public ToolBarBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 1;
      return this;
   }

   public ToolBarBuilder items(Node... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public ToolBarBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set |= 2;
      return this;
   }

   public ToolBar build() {
      ToolBar var1 = new ToolBar();
      this.applyTo(var1);
      return var1;
   }
}
