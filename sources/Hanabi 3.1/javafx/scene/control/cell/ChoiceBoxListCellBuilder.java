package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.ListCellBuilder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class ChoiceBoxListCellBuilder extends ListCellBuilder {
   private int __set;
   private StringConverter converter;
   private Collection items;

   protected ChoiceBoxListCellBuilder() {
   }

   public static ChoiceBoxListCellBuilder create() {
      return new ChoiceBoxListCellBuilder();
   }

   public void applyTo(ChoiceBoxListCell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 2) != 0) {
         var1.getItems().addAll(this.items);
      }

   }

   public ChoiceBoxListCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 1;
      return this;
   }

   public ChoiceBoxListCellBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 2;
      return this;
   }

   public ChoiceBoxListCellBuilder items(Object... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public ChoiceBoxListCell build() {
      ChoiceBoxListCell var1 = new ChoiceBoxListCell();
      this.applyTo(var1);
      return var1;
   }
}
