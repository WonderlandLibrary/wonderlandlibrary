package com.sun.javafx.scene.control.behavior;

import javafx.collections.ObservableList;
import javafx.scene.control.FocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;

public class TableRowBehavior extends TableRowBehaviorBase {
   public TableRowBehavior(TableRow var1) {
      super(var1);
   }

   protected TableSelectionModel getSelectionModel() {
      return this.getCellContainer().getSelectionModel();
   }

   protected TablePositionBase getFocusedCell() {
      return this.getCellContainer().getFocusModel().getFocusedCell();
   }

   protected FocusModel getFocusModel() {
      return this.getCellContainer().getFocusModel();
   }

   protected ObservableList getVisibleLeafColumns() {
      return this.getCellContainer().getVisibleLeafColumns();
   }

   protected TableView getCellContainer() {
      return ((TableRow)this.getControl()).getTableView();
   }

   protected void edit(TableRow var1) {
   }
}
