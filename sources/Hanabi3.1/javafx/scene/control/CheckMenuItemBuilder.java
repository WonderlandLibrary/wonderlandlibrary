package javafx.scene.control;

/** @deprecated */
@Deprecated
public class CheckMenuItemBuilder extends MenuItemBuilder {
   private boolean __set;
   private boolean selected;

   protected CheckMenuItemBuilder() {
   }

   public static CheckMenuItemBuilder create() {
      return new CheckMenuItemBuilder();
   }

   public void applyTo(CheckMenuItem var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setSelected(this.selected);
      }

   }

   public CheckMenuItemBuilder selected(boolean var1) {
      this.selected = var1;
      this.__set = true;
      return this;
   }

   public CheckMenuItem build() {
      CheckMenuItem var1 = new CheckMenuItem();
      this.applyTo(var1);
      return var1;
   }
}
