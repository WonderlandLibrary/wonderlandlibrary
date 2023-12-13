package javafx.scene.layout;

import javafx.scene.Node;

/** @deprecated */
@Deprecated
public class BorderPaneBuilder extends PaneBuilder {
   private int __set;
   private Node bottom;
   private Node center;
   private Node left;
   private Node right;
   private Node top;

   protected BorderPaneBuilder() {
   }

   public static BorderPaneBuilder create() {
      return new BorderPaneBuilder();
   }

   public void applyTo(BorderPane var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBottom(this.bottom);
      }

      if ((var2 & 2) != 0) {
         var1.setCenter(this.center);
      }

      if ((var2 & 4) != 0) {
         var1.setLeft(this.left);
      }

      if ((var2 & 8) != 0) {
         var1.setRight(this.right);
      }

      if ((var2 & 16) != 0) {
         var1.setTop(this.top);
      }

   }

   public BorderPaneBuilder bottom(Node var1) {
      this.bottom = var1;
      this.__set |= 1;
      return this;
   }

   public BorderPaneBuilder center(Node var1) {
      this.center = var1;
      this.__set |= 2;
      return this;
   }

   public BorderPaneBuilder left(Node var1) {
      this.left = var1;
      this.__set |= 4;
      return this;
   }

   public BorderPaneBuilder right(Node var1) {
      this.right = var1;
      this.__set |= 8;
      return this;
   }

   public BorderPaneBuilder top(Node var1) {
      this.top = var1;
      this.__set |= 16;
      return this;
   }

   public BorderPane build() {
      BorderPane var1 = new BorderPane();
      this.applyTo(var1);
      return var1;
   }
}
