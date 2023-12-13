package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;

/** @deprecated */
@Deprecated
public abstract class MultipleSelectionModelBuilder {
   private int __set;
   private Collection selectedIndices;
   private Collection selectedItems;
   private SelectionMode selectionMode;

   protected MultipleSelectionModelBuilder() {
   }

   public void applyTo(MultipleSelectionModel var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getSelectedIndices().addAll(this.selectedIndices);
      }

      if ((var2 & 2) != 0) {
         var1.getSelectedItems().addAll(this.selectedItems);
      }

      if ((var2 & 4) != 0) {
         var1.setSelectionMode(this.selectionMode);
      }

   }

   public MultipleSelectionModelBuilder selectedIndices(Collection var1) {
      this.selectedIndices = var1;
      this.__set |= 1;
      return this;
   }

   public MultipleSelectionModelBuilder selectedIndices(Integer... var1) {
      return this.selectedIndices((Collection)Arrays.asList(var1));
   }

   public MultipleSelectionModelBuilder selectedItems(Collection var1) {
      this.selectedItems = var1;
      this.__set |= 2;
      return this;
   }

   public MultipleSelectionModelBuilder selectedItems(Object... var1) {
      return this.selectedItems((Collection)Arrays.asList(var1));
   }

   public MultipleSelectionModelBuilder selectionMode(SelectionMode var1) {
      this.selectionMode = var1;
      this.__set |= 4;
      return this;
   }
}
