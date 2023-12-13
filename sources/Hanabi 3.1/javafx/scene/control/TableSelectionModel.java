package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class TableSelectionModel extends MultipleSelectionModelBase {
   private BooleanProperty cellSelectionEnabled = new SimpleBooleanProperty(this, "cellSelectionEnabled");

   public abstract boolean isSelected(int var1, TableColumnBase var2);

   public abstract void select(int var1, TableColumnBase var2);

   public abstract void clearAndSelect(int var1, TableColumnBase var2);

   public abstract void clearSelection(int var1, TableColumnBase var2);

   public abstract void selectLeftCell();

   public abstract void selectRightCell();

   public abstract void selectAboveCell();

   public abstract void selectBelowCell();

   public abstract void selectRange(int var1, TableColumnBase var2, int var3, TableColumnBase var4);

   public final BooleanProperty cellSelectionEnabledProperty() {
      return this.cellSelectionEnabled;
   }

   public final void setCellSelectionEnabled(boolean var1) {
      this.cellSelectionEnabledProperty().set(var1);
   }

   public final boolean isCellSelectionEnabled() {
      return this.cellSelectionEnabled == null ? false : this.cellSelectionEnabled.get();
   }
}
