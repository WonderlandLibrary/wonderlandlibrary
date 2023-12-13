package javafx.scene.control.cell;

import javafx.scene.control.TreeCellBuilder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class TextFieldTreeCellBuilder extends TreeCellBuilder {
   private boolean __set;
   private StringConverter converter;

   protected TextFieldTreeCellBuilder() {
   }

   public static TextFieldTreeCellBuilder create() {
      return new TextFieldTreeCellBuilder();
   }

   public void applyTo(TextFieldTreeCell var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setConverter(this.converter);
      }

   }

   public TextFieldTreeCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set = true;
      return this;
   }

   public TextFieldTreeCell build() {
      TextFieldTreeCell var1 = new TextFieldTreeCell();
      this.applyTo(var1);
      return var1;
   }
}
