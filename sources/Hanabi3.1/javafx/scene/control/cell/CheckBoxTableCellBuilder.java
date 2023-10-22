package javafx.scene.control.cell;

import javafx.scene.control.TableCellBuilder;
import javafx.util.Callback;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class CheckBoxTableCellBuilder extends TableCellBuilder {
   private int __set;
   private StringConverter converter;
   private Callback selectedStateCallback;

   protected CheckBoxTableCellBuilder() {
   }

   public static CheckBoxTableCellBuilder create() {
      return new CheckBoxTableCellBuilder();
   }

   public void applyTo(CheckBoxTableCell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 2) != 0) {
         var1.setSelectedStateCallback(this.selectedStateCallback);
      }

   }

   public CheckBoxTableCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 1;
      return this;
   }

   public CheckBoxTableCellBuilder selectedStateCallback(Callback var1) {
      this.selectedStateCallback = var1;
      this.__set |= 2;
      return this;
   }

   public CheckBoxTableCell build() {
      CheckBoxTableCell var1 = new CheckBoxTableCell();
      this.applyTo(var1);
      return var1;
   }
}
