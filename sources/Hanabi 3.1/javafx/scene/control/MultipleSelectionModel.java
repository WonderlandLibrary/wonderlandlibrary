package javafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ObservableList;

public abstract class MultipleSelectionModel extends SelectionModel {
   private ObjectProperty selectionMode;

   public final void setSelectionMode(SelectionMode var1) {
      this.selectionModeProperty().set(var1);
   }

   public final SelectionMode getSelectionMode() {
      return this.selectionMode == null ? SelectionMode.SINGLE : (SelectionMode)this.selectionMode.get();
   }

   public final ObjectProperty selectionModeProperty() {
      if (this.selectionMode == null) {
         this.selectionMode = new ObjectPropertyBase(SelectionMode.SINGLE) {
            protected void invalidated() {
               if (MultipleSelectionModel.this.getSelectionMode() == SelectionMode.SINGLE && !MultipleSelectionModel.this.isEmpty()) {
                  int var1 = MultipleSelectionModel.this.getSelectedIndex();
                  MultipleSelectionModel.this.clearSelection();
                  MultipleSelectionModel.this.select(var1);
               }

            }

            public Object getBean() {
               return MultipleSelectionModel.this;
            }

            public String getName() {
               return "selectionMode";
            }
         };
      }

      return this.selectionMode;
   }

   public abstract ObservableList getSelectedIndices();

   public abstract ObservableList getSelectedItems();

   public abstract void selectIndices(int var1, int... var2);

   public void selectRange(int var1, int var2) {
      if (var1 != var2) {
         boolean var3 = var1 < var2;
         int var4 = var3 ? var1 : var2;
         int var5 = var3 ? var2 : var1;
         int var6 = var5 - var4 - 1;
         int[] var7 = new int[var6];
         int var8 = var3 ? var4 : var5;
         int var9 = var3 ? var8++ : var8--;

         for(int var10 = 0; var10 < var6; ++var10) {
            var7[var10] = var3 ? var8++ : var8--;
         }

         this.selectIndices(var9, var7);
      }
   }

   public abstract void selectAll();

   public abstract void selectFirst();

   public abstract void selectLast();
}
