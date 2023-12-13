package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TreeItemBuilder implements Builder {
   private int __set;
   private Collection children;
   private boolean expanded;
   private Node graphic;
   private Object value;

   protected TreeItemBuilder() {
   }

   public static TreeItemBuilder create() {
      return new TreeItemBuilder();
   }

   public void applyTo(TreeItem var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getChildren().addAll(this.children);
      }

      if ((var2 & 2) != 0) {
         var1.setExpanded(this.expanded);
      }

      if ((var2 & 4) != 0) {
         var1.setGraphic(this.graphic);
      }

      if ((var2 & 8) != 0) {
         var1.setValue(this.value);
      }

   }

   public TreeItemBuilder children(Collection var1) {
      this.children = var1;
      this.__set |= 1;
      return this;
   }

   public TreeItemBuilder children(TreeItem... var1) {
      return this.children((Collection)Arrays.asList(var1));
   }

   public TreeItemBuilder expanded(boolean var1) {
      this.expanded = var1;
      this.__set |= 2;
      return this;
   }

   public TreeItemBuilder graphic(Node var1) {
      this.graphic = var1;
      this.__set |= 4;
      return this;
   }

   public TreeItemBuilder value(Object var1) {
      this.value = var1;
      this.__set |= 8;
      return this;
   }

   public TreeItem build() {
      TreeItem var1 = new TreeItem();
      this.applyTo(var1);
      return var1;
   }
}
