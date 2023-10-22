package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class TreeTableViewBehavior extends TableViewBehaviorBase {
   protected static final List TREE_TABLE_VIEW_BINDINGS = new ArrayList();
   private final ChangeListener selectionModelListener = (var1x, var2, var3) -> {
      if (var2 != null) {
         var2.getSelectedCells().removeListener(this.weakSelectedCellsListener);
      }

      if (var3 != null) {
         var3.getSelectedCells().addListener(this.weakSelectedCellsListener);
      }

   };
   private final WeakChangeListener weakSelectionModelListener;

   protected String matchActionForEvent(KeyEvent var1) {
      String var2 = super.matchActionForEvent(var1);
      if (((TreeTableView)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
         if ("CollapseRow".equals(var2) && (var1.getCode() == KeyCode.LEFT || var1.getCode() == KeyCode.KP_LEFT)) {
            var2 = "ExpandRow";
         } else if ("ExpandRow".equals(var2) && (var1.getCode() == KeyCode.RIGHT || var1.getCode() == KeyCode.KP_RIGHT)) {
            var2 = "CollapseRow";
         }
      }

      return var2;
   }

   protected void callAction(String var1) {
      if ("ExpandRow".equals(var1)) {
         this.rightArrowPressed();
      } else if ("CollapseRow".equals(var1)) {
         this.leftArrowPressed();
      } else if ("ExpandAll".equals(var1)) {
         this.expandAll();
      } else {
         super.callAction(var1);
      }

   }

   public TreeTableViewBehavior(TreeTableView var1) {
      super(var1, TREE_TABLE_VIEW_BINDINGS);
      this.weakSelectionModelListener = new WeakChangeListener(this.selectionModelListener);
      var1.selectionModelProperty().addListener(this.weakSelectionModelListener);
      if (this.getSelectionModel() != null) {
         var1.getSelectionModel().getSelectedCells().addListener(this.selectedCellsListener);
      }

   }

   protected int getItemCount() {
      return ((TreeTableView)this.getControl()).getExpandedItemCount();
   }

   protected TableFocusModel getFocusModel() {
      return ((TreeTableView)this.getControl()).getFocusModel();
   }

   protected TableSelectionModel getSelectionModel() {
      return ((TreeTableView)this.getControl()).getSelectionModel();
   }

   protected ObservableList getSelectedCells() {
      return ((TreeTableView)this.getControl()).getSelectionModel().getSelectedCells();
   }

   protected TablePositionBase getFocusedCell() {
      return ((TreeTableView)this.getControl()).getFocusModel().getFocusedCell();
   }

   protected int getVisibleLeafIndex(TableColumnBase var1) {
      return ((TreeTableView)this.getControl()).getVisibleLeafIndex((TreeTableColumn)var1);
   }

   protected TreeTableColumn getVisibleLeafColumn(int var1) {
      return ((TreeTableView)this.getControl()).getVisibleLeafColumn(var1);
   }

   protected void editCell(int var1, TableColumnBase var2) {
      ((TreeTableView)this.getControl()).edit(var1, (TreeTableColumn)var2);
   }

   protected ObservableList getVisibleLeafColumns() {
      return ((TreeTableView)this.getControl()).getVisibleLeafColumns();
   }

   protected TablePositionBase getTablePosition(int var1, TableColumnBase var2) {
      return new TreeTablePosition((TreeTableView)this.getControl(), var1, (TreeTableColumn)var2);
   }

   protected void selectAllToFocus(boolean var1) {
      if (((TreeTableView)this.getControl()).getEditingCell() == null) {
         super.selectAllToFocus(var1);
      }
   }

   private void rightArrowPressed() {
      if (((TreeTableView)this.getControl()).getSelectionModel().isCellSelectionEnabled()) {
         if (this.isRTL()) {
            this.selectLeftCell();
         } else {
            this.selectRightCell();
         }
      } else {
         this.expandRow();
      }

   }

   private void leftArrowPressed() {
      if (((TreeTableView)this.getControl()).getSelectionModel().isCellSelectionEnabled()) {
         if (this.isRTL()) {
            this.selectRightCell();
         } else {
            this.selectLeftCell();
         }
      } else {
         this.collapseRow();
      }

   }

   private void expandRow() {
      Callback var1 = (var1x) -> {
         return ((TreeTableView)this.getControl()).getRow(var1x);
      };
      TreeViewBehavior.expandRow(((TreeTableView)this.getControl()).getSelectionModel(), var1);
   }

   private void expandAll() {
      TreeViewBehavior.expandAll(((TreeTableView)this.getControl()).getRoot());
   }

   private void collapseRow() {
      TreeTableView var1 = (TreeTableView)this.getControl();
      TreeViewBehavior.collapseRow(var1.getSelectionModel(), var1.getRoot(), var1.isShowRoot());
   }

   static {
      TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "CollapseRow"));
      TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "CollapseRow"));
      TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ExpandRow"));
      TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "ExpandRow"));
      TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.MULTIPLY, "ExpandAll"));
      TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ADD, "ExpandRow"));
      TREE_TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SUBTRACT, "CollapseRow"));
      TREE_TABLE_VIEW_BINDINGS.addAll(TABLE_VIEW_BINDINGS);
   }
}
