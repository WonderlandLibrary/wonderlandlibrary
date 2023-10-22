package javafx.scene.control.cell;

import javafx.scene.control.TreeCellBuilder;
import javafx.util.Callback;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class CheckBoxTreeCellBuilder extends TreeCellBuilder {
   private int __set;
   private StringConverter converter;
   private Callback selectedStateCallback;

   protected CheckBoxTreeCellBuilder() {
   }

   public static CheckBoxTreeCellBuilder create() {
      return new CheckBoxTreeCellBuilder();
   }

   public void applyTo(CheckBoxTreeCell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 2) != 0) {
         var1.setSelectedStateCallback(this.selectedStateCallback);
      }

   }

   public CheckBoxTreeCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 1;
      return this;
   }

   public CheckBoxTreeCellBuilder selectedStateCallback(Callback var1) {
      this.selectedStateCallback = var1;
      this.__set |= 2;
      return this;
   }

   public CheckBoxTreeCell build() {
      CheckBoxTreeCell var1 = new CheckBoxTreeCell();
      this.applyTo(var1);
      return var1;
   }
}
