package javafx.scene.control.cell;

import javafx.scene.control.TableCellBuilder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class TextFieldTableCellBuilder extends TableCellBuilder {
   private boolean __set;
   private StringConverter converter;

   protected TextFieldTableCellBuilder() {
   }

   public static TextFieldTableCellBuilder create() {
      return new TextFieldTableCellBuilder();
   }

   public void applyTo(TextFieldTableCell var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setConverter(this.converter);
      }

   }

   public TextFieldTableCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set = true;
      return this;
   }

   public TextFieldTableCell build() {
      TextFieldTableCell var1 = new TextFieldTableCell();
      this.applyTo(var1);
      return var1;
   }
}
