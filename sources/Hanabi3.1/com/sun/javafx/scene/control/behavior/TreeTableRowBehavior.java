package com.sun.javafx.scene.control.behavior;

import javafx.collections.ObservableList;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;

public class TreeTableRowBehavior extends TableRowBehaviorBase {
   public TreeTableRowBehavior(TreeTableRow var1) {
      super(var1);
   }

   protected TableSelectionModel getSelectionModel() {
      return this.getCellContainer().getSelectionModel();
   }

   protected TableFocusModel getFocusModel() {
      return this.getCellContainer().getFocusModel();
   }

   protected TreeTableView getCellContainer() {
      return ((TreeTableRow)this.getControl()).getTreeTableView();
   }

   protected TablePositionBase getFocusedCell() {
      return this.getCellContainer().getFocusModel().getFocusedCell();
   }

   protected ObservableList getVisibleLeafColumns() {
      return this.getCellContainer().getVisibleLeafColumns();
   }

   protected void edit(TreeTableRow var1) {
   }

   protected void handleClicks(MouseButton var1, int var2, boolean var3) {
      TreeItem var4 = ((TreeTableRow)this.getControl()).getTreeItem();
      if (var1 == MouseButton.PRIMARY) {
         if (var2 == 1 && var3) {
            this.edit((TreeTableRow)this.getControl());
         } else if (var2 == 1) {
            this.edit((TreeTableRow)null);
         } else if (var2 == 2 && var4.isLeaf()) {
            this.edit((TreeTableRow)this.getControl());
         } else if (var2 % 2 == 0) {
            var4.setExpanded(!var4.isExpanded());
         }
      }

   }
}
