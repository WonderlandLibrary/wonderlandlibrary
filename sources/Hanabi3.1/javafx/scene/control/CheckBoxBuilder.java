package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class CheckBoxBuilder extends ButtonBaseBuilder implements Builder {
   private int __set;
   private boolean allowIndeterminate;
   private boolean indeterminate;
   private boolean selected;

   protected CheckBoxBuilder() {
   }

   public static CheckBoxBuilder create() {
      return new CheckBoxBuilder();
   }

   public void applyTo(CheckBox var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAllowIndeterminate(this.allowIndeterminate);
      }

      if ((var2 & 2) != 0) {
         var1.setIndeterminate(this.indeterminate);
      }

      if ((var2 & 4) != 0) {
         var1.setSelected(this.selected);
      }

   }

   public CheckBoxBuilder allowIndeterminate(boolean var1) {
      this.allowIndeterminate = var1;
      this.__set |= 1;
      return this;
   }

   public CheckBoxBuilder indeterminate(boolean var1) {
      this.indeterminate = var1;
      this.__set |= 2;
      return this;
   }

   public CheckBoxBuilder selected(boolean var1) {
      this.selected = var1;
      this.__set |= 4;
      return this;
   }

   public CheckBox build() {
      CheckBox var1 = new CheckBox();
      this.applyTo(var1);
      return var1;
   }
}
