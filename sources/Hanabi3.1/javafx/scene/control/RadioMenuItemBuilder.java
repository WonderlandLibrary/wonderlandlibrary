package javafx.scene.control;

/** @deprecated */
@Deprecated
public class RadioMenuItemBuilder extends MenuItemBuilder {
   private int __set;
   private boolean selected;
   private String text;
   private ToggleGroup toggleGroup;

   protected RadioMenuItemBuilder() {
   }

   public static RadioMenuItemBuilder create() {
      return new RadioMenuItemBuilder();
   }

   public void applyTo(RadioMenuItem var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setSelected(this.selected);
      }

      if ((var2 & 2) != 0) {
         var1.setToggleGroup(this.toggleGroup);
      }

   }

   public RadioMenuItemBuilder selected(boolean var1) {
      this.selected = var1;
      this.__set |= 1;
      return this;
   }

   public RadioMenuItemBuilder text(String var1) {
      this.text = var1;
      return this;
   }

   public RadioMenuItemBuilder toggleGroup(ToggleGroup var1) {
      this.toggleGroup = var1;
      this.__set |= 2;
      return this;
   }

   public RadioMenuItem build() {
      RadioMenuItem var1 = new RadioMenuItem(this.text);
      this.applyTo(var1);
      return var1;
   }
}
