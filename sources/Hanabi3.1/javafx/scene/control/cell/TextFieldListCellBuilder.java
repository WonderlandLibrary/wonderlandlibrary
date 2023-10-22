package javafx.scene.control.cell;

import javafx.scene.control.ListCellBuilder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class TextFieldListCellBuilder extends ListCellBuilder {
   private boolean __set;
   private StringConverter converter;

   protected TextFieldListCellBuilder() {
   }

   public static TextFieldListCellBuilder create() {
      return new TextFieldListCellBuilder();
   }

   public void applyTo(TextFieldListCell var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setConverter(this.converter);
      }

   }

   public TextFieldListCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set = true;
      return this;
   }

   public TextFieldListCell build() {
      TextFieldListCell var1 = new TextFieldListCell();
      this.applyTo(var1);
      return var1;
   }
}
