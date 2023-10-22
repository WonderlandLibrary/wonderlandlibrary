package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class FocusModel {
   private ReadOnlyIntegerWrapper focusedIndex = new ReadOnlyIntegerWrapper(this, "focusedIndex", -1);
   private ReadOnlyObjectWrapper focusedItem = new ReadOnlyObjectWrapper(this, "focusedItem");

   public FocusModel() {
      this.focusedIndexProperty().addListener((var1) -> {
         this.setFocusedItem(this.getModelItem(this.getFocusedIndex()));
      });
   }

   public final ReadOnlyIntegerProperty focusedIndexProperty() {
      return this.focusedIndex.getReadOnlyProperty();
   }

   public final int getFocusedIndex() {
      return this.focusedIndex.get();
   }

   final void setFocusedIndex(int var1) {
      this.focusedIndex.set(var1);
   }

   public final ReadOnlyObjectProperty focusedItemProperty() {
      return this.focusedItem.getReadOnlyProperty();
   }

   public final Object getFocusedItem() {
      return this.focusedItemProperty().get();
   }

   final void setFocusedItem(Object var1) {
      this.focusedItem.set(var1);
   }

   protected abstract int getItemCount();

   protected abstract Object getModelItem(int var1);

   public boolean isFocused(int var1) {
      if (var1 >= 0 && var1 < this.getItemCount()) {
         return this.getFocusedIndex() == var1;
      } else {
         return false;
      }
   }

   public void focus(int var1) {
      if (var1 >= 0 && var1 < this.getItemCount()) {
         int var2 = this.getFocusedIndex();
         this.setFocusedIndex(var1);
         if (var2 == var1) {
            this.setFocusedItem(this.getModelItem(var1));
         }
      } else {
         this.setFocusedIndex(-1);
      }

   }

   public void focusPrevious() {
      if (this.getFocusedIndex() == -1) {
         this.focus(0);
      } else if (this.getFocusedIndex() > 0) {
         this.focus(this.getFocusedIndex() - 1);
      }

   }

   public void focusNext() {
      if (this.getFocusedIndex() == -1) {
         this.focus(0);
      } else if (this.getFocusedIndex() != this.getItemCount() - 1) {
         this.focus(this.getFocusedIndex() + 1);
      }

   }
}
