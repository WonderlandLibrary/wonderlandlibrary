package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ToggleButtonBuilder extends ButtonBaseBuilder implements Builder {
   private int __set;
   private boolean selected;
   private ToggleGroup toggleGroup;

   protected ToggleButtonBuilder() {
   }

   public static ToggleButtonBuilder create() {
      return new ToggleButtonBuilder();
   }

   public void applyTo(ToggleButton var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setSelected(this.selected);
      }

      if ((var2 & 2) != 0) {
         var1.setToggleGroup(this.toggleGroup);
      }

   }

   public ToggleButtonBuilder selected(boolean var1) {
      this.selected = var1;
      this.__set |= 1;
      return this;
   }

   public ToggleButtonBuilder toggleGroup(ToggleGroup var1) {
      this.toggleGroup = var1;
      this.__set |= 2;
      return this;
   }

   public ToggleButton build() {
      ToggleButton var1 = new ToggleButton();
      this.applyTo(var1);
      return var1;
   }
}
