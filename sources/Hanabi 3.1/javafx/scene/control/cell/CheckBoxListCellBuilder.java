package javafx.scene.control.cell;

import javafx.scene.control.ListCellBuilder;
import javafx.util.Callback;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class CheckBoxListCellBuilder extends ListCellBuilder {
   private int __set;
   private StringConverter converter;
   private Callback selectedStateCallback;

   protected CheckBoxListCellBuilder() {
   }

   public static CheckBoxListCellBuilder create() {
      return new CheckBoxListCellBuilder();
   }

   public void applyTo(CheckBoxListCell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 2) != 0) {
         var1.setSelectedStateCallback(this.selectedStateCallback);
      }

   }

   public CheckBoxListCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 1;
      return this;
   }

   public CheckBoxListCellBuilder selectedStateCallback(Callback var1) {
      this.selectedStateCallback = var1;
      this.__set |= 2;
      return this;
   }

   public CheckBoxListCell build() {
      CheckBoxListCell var1 = new CheckBoxListCell();
      this.applyTo(var1);
      return var1;
   }
}
