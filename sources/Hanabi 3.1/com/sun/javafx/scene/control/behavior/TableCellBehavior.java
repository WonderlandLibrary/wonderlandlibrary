package com.sun.javafx.scene.control.behavior;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableView;

public class TableCellBehavior extends TableCellBehaviorBase {
   public TableCellBehavior(TableCell var1) {
      super(var1);
   }

   protected TableView getCellContainer() {
      return ((TableCell)this.getControl()).getTableView();
   }

   protected TableColumn getTableColumn() {
      return ((TableCell)this.getControl()).getTableColumn();
   }

   protected int getItemCount() {
      return this.getCellContainer().getItems().size();
   }

   protected TableView.TableViewSelectionModel getSelectionModel() {
      return this.getCellContainer().getSelectionModel();
   }

   protected TableView.TableViewFocusModel getFocusModel() {
      return this.getCellContainer().getFocusModel();
   }

   protected TablePositionBase getFocusedCell() {
      return this.getCellContainer().getFocusModel().getFocusedCell();
   }

   protected boolean isTableRowSelected() {
      return ((TableCell)this.getControl()).getTableRow().isSelected();
   }

   protected int getVisibleLeafIndex(TableColumnBase var1) {
      return this.getCellContainer().getVisibleLeafIndex((TableColumn)var1);
   }

   protected void focus(int var1, TableColumnBase var2) {
      this.getFocusModel().focus(var1, (TableColumn)var2);
   }

   protected void edit(TableCell var1) {
      if (var1 == null) {
         this.getCellContainer().edit(-1, (TableColumn)null);
      } else {
         this.getCellContainer().edit(var1.getIndex(), var1.getTableColumn());
      }

   }
}
