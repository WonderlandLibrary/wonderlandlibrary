package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public abstract class SelectionModel {
   private ReadOnlyIntegerWrapper selectedIndex = new ReadOnlyIntegerWrapper(this, "selectedIndex", -1);
   private ReadOnlyObjectWrapper selectedItem = new ReadOnlyObjectWrapper(this, "selectedItem");

   public final ReadOnlyIntegerProperty selectedIndexProperty() {
      return this.selectedIndex.getReadOnlyProperty();
   }

   protected final void setSelectedIndex(int var1) {
      this.selectedIndex.set(var1);
   }

   public final int getSelectedIndex() {
      return this.selectedIndexProperty().get();
   }

   public final ReadOnlyObjectProperty selectedItemProperty() {
      return this.selectedItem.getReadOnlyProperty();
   }

   protected final void setSelectedItem(Object var1) {
      this.selectedItem.set(var1);
   }

   public final Object getSelectedItem() {
      return this.selectedItemProperty().get();
   }

   public abstract void clearAndSelect(int var1);

   public abstract void select(int var1);

   public abstract void select(Object var1);

   public abstract void clearSelection(int var1);

   public abstract void clearSelection();

   public abstract boolean isSelected(int var1);

   public abstract boolean isEmpty();

   public abstract void selectPrevious();

   public abstract void selectNext();

   public abstract void selectFirst();

   public abstract void selectLast();
}
