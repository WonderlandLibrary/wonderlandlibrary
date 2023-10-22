package javafx.scene.control.cell;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TreeCellBuilder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class ComboBoxTreeCellBuilder extends TreeCellBuilder {
   private int __set;
   private boolean comboBoxEditable;
   private StringConverter converter;
   private Collection items;

   protected ComboBoxTreeCellBuilder() {
   }

   public static ComboBoxTreeCellBuilder create() {
      return new ComboBoxTreeCellBuilder();
   }

   public void applyTo(ComboBoxTreeCell var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setComboBoxEditable(this.comboBoxEditable);
      }

      if ((var2 & 2) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 4) != 0) {
         var1.getItems().addAll(this.items);
      }

   }

   public ComboBoxTreeCellBuilder comboBoxEditable(boolean var1) {
      this.comboBoxEditable = var1;
      this.__set |= 1;
      return this;
   }

   public ComboBoxTreeCellBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 2;
      return this;
   }

   public ComboBoxTreeCellBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 4;
      return this;
   }

   public ComboBoxTreeCellBuilder items(Object... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public ComboBoxTreeCell build() {
      ComboBoxTreeCell var1 = new ComboBoxTreeCell();
      this.applyTo(var1);
      return var1;
   }
}
