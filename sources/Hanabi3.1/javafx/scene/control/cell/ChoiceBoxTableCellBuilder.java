package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TableCellBuilder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class ChoiceBoxTableCellBuilder extends TableCellBuilder {
   private int __set;
   private StringConverter converter;
   private Collection items;

   protected ChoiceBoxTableCellBuilder() {
   }

   public static ChoiceBoxTableCellBuilder create() {
      return new ChoiceBoxTableCellBuilder();
   }

   public void applyTo(ChoiceBoxTableCell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 2) != 0) {
         var1.getItems().addAll(this.items);
      }

   }

   public ChoiceBoxTableCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 1;
      return this;
   }

   public ChoiceBoxTableCellBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 2;
      return this;
   }

   public ChoiceBoxTableCellBuilder items(Object... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public ChoiceBoxTableCell build() {
      ChoiceBoxTableCell var1 = new ChoiceBoxTableCell();
      this.applyTo(var1);
      return var1;
   }
}
