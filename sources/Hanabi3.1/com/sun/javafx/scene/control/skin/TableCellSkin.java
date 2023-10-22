package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TableCellBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class TableCellSkin extends TableCellSkinBase {
   private final TableColumn tableColumn;

   public TableCellSkin(TableCell var1) {
      super(var1, new TableCellBehavior(var1));
      this.tableColumn = var1.getTableColumn();
      super.init(var1);
   }

   protected BooleanProperty columnVisibleProperty() {
      return this.tableColumn.visibleProperty();
   }

   protected ReadOnlyDoubleProperty columnWidthProperty() {
      return this.tableColumn.widthProperty();
   }
}
