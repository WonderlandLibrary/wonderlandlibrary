package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TreeCellBuilder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class ChoiceBoxTreeCellBuilder extends TreeCellBuilder {
   private int __set;
   private StringConverter converter;
   private Collection items;

   protected ChoiceBoxTreeCellBuilder() {
   }

   public static ChoiceBoxTreeCellBuilder create() {
      return new ChoiceBoxTreeCellBuilder();
   }

   public void applyTo(ChoiceBoxTreeCell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 2) != 0) {
         var1.getItems().addAll(this.items);
      }

   }

   public ChoiceBoxTreeCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 1;
      return this;
   }

   public ChoiceBoxTreeCellBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 2;
      return this;
   }

   public ChoiceBoxTreeCellBuilder items(Object... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public ChoiceBoxTreeCell build() {
      ChoiceBoxTreeCell var1 = new ChoiceBoxTreeCell();
      this.applyTo(var1);
      return var1;
   }
}
