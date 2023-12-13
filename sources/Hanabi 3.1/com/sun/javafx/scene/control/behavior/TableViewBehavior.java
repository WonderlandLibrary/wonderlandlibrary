package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;

public class TableViewBehavior extends TableViewBehaviorBase {
   private final ChangeListener selectionModelListener = (var1x, var2x, var3) -> {
      if (var2x != null) {
         var2x.getSelectedCells().removeListener(this.weakSelectedCellsListener);
      }

      if (var3 != null) {
         var3.getSelectedCells().addListener(this.weakSelectedCellsListener);
      }

   };
   private final WeakChangeListener weakSelectionModelListener;
   private TwoLevelFocusBehavior tlFocus;

   public TableViewBehavior(TableView var1) {
      super(var1);
      this.weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);
      var1.selectionModelProperty().addListener(this.weakSelectionModelListener);
      TableView.TableViewSelectionModel var2 = var1.getSelectionModel();
      if (var2 != null) {
         var2.getSelectedCells().addListener(this.selectedCellsListener);
      }

      if (Utils.isTwoLevelFocus()) {
         this.tlFocus = new TwoLevelFocusBehavior(var1);
      }

   }

   public void dispose() {
      if (this.tlFocus != null) {
         this.tlFocus.dispose();
      }

      super.dispose();
   }

   protected int getItemCount() {
      return ((TableView)this.getControl()).getItems() == null ? 0 : ((TableView)this.getControl()).getItems().size();
   }

   protected TableFocusModel getFocusModel() {
      return ((TableView)this.getControl()).getFocusModel();
   }

   protected TableSelectionModel getSelectionModel() {
      return ((TableView)this.getControl()).getSelectionModel();
   }

   protected ObservableList getSelectedCells() {
      return ((TableView)this.getControl()).getSelectionModel().getSelectedCells();
   }

   protected TablePositionBase getFocusedCell() {
      return ((TableView)this.getControl()).getFocusModel().getFocusedCell();
   }

   protected int getVisibleLeafIndex(TableColumnBase var1) {
      return ((TableView)this.getControl()).getVisibleLeafIndex((TableColumn)var1);
   }

   protected TableColumn getVisibleLeafColumn(int var1) {
      return ((TableView)this.getControl()).getVisibleLeafColumn(var1);
   }

   protected void editCell(int var1, TableColumnBase var2) {
      ((TableView)this.getControl()).edit(var1, (TableColumn)var2);
   }

   protected ObservableList getVisibleLeafColumns() {
      return ((TableView)this.getControl()).getVisibleLeafColumns();
   }

   protected TablePositionBase getTablePosition(int var1, TableColumnBase var2) {
      return new TablePosition((TableView)this.getControl(), var1, (TableColumn)var2);
   }

   protected void selectAllToFocus(boolean var1) {
      if (((TableView)this.getControl()).getEditingCell() == null) {
         super.selectAllToFocus(var1);
      }
   }
}
