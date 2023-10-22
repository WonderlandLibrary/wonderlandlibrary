package javafx.scene.control;

import javafx.scene.Node;

/** @deprecated */
@Deprecated
public class CustomMenuItemBuilder extends MenuItemBuilder {
   private int __set;
   private Node content;
   private boolean hideOnClick;

   protected CustomMenuItemBuilder() {
   }

   public static CustomMenuItemBuilder create() {
      return new CustomMenuItemBuilder();
   }

   public void applyTo(CustomMenuItem var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setContent(this.content);
      }

      if ((var2 & 2) != 0) {
         var1.setHideOnClick(this.hideOnClick);
      }

   }

   public CustomMenuItemBuilder content(Node var1) {
      this.content = var1;
      this.__set |= 1;
      return this;
   }

   public CustomMenuItemBuilder hideOnClick(boolean var1) {
      this.hideOnClick = var1;
      this.__set |= 2;
      return this;
   }

   public CustomMenuItem build() {
      CustomMenuItem var1 = new CustomMenuItem();
      this.applyTo(var1);
      return var1;
   }
}
