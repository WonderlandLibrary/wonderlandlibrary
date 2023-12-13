package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TreeTableCellSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class TreeTableCell extends IndexedCell {
   boolean lockItemOnEdit = false;
   private boolean itemDirty = false;
   private ListChangeListener selectedListener = (var1) -> {
      while(var1.next()) {
         if (var1.wasAdded() || var1.wasRemoved()) {
            this.updateSelection();
         }
      }

   };
   private final InvalidationListener focusedListener = (var1) -> {
      this.updateFocus();
   };
   private final InvalidationListener tableRowUpdateObserver = (var1) -> {
      this.itemDirty = true;
      this.requestLayout();
   };
   private final InvalidationListener editingListener = (var1) -> {
      this.updateEditing();
   };
   private ListChangeListener visibleLeafColumnsListener = (var1) -> {
      this.updateColumnIndex();
   };
   private ListChangeListener columnStyleClassListener = (var1) -> {
      while(var1.next()) {
         if (var1.wasRemoved()) {
            this.getStyleClass().removeAll(var1.getRemoved());
         }

         if (var1.wasAdded()) {
            this.getStyleClass().addAll(var1.getAddedSubList());
         }
      }

   };
   private final InvalidationListener rootPropertyListener = (var1) -> {
      this.updateItem(-1);
   };
   private final InvalidationListener columnStyleListener = (var1) -> {
      if (this.getTableColumn() != null) {
         this.possiblySetStyle(this.getTableColumn().getStyle());
      }

   };
   private final InvalidationListener columnIdListener = (var1) -> {
      if (this.getTableColumn() != null) {
         this.possiblySetId(this.getTableColumn().getId());
      }

   };
   private final WeakListChangeListener weakSelectedListener;
   private final WeakInvalidationListener weakFocusedListener;
   private final WeakInvalidationListener weaktableRowUpdateObserver;
   private final WeakInvalidationListener weakEditingListener;
   private final WeakListChangeListener weakVisibleLeafColumnsListener;
   private final WeakListChangeListener weakColumnStyleClassListener;
   private final WeakInvalidationListener weakColumnStyleListener;
   private final WeakInvalidationListener weakColumnIdListener;
   private final WeakInvalidationListener weakRootPropertyListener;
   private ReadOnlyObjectWrapper treeTableColumn;
   private ReadOnlyObjectWrapper treeTableView;
   private ReadOnlyObjectWrapper treeTableRow;
   private boolean isLastVisibleColumn;
   private int columnIndex;
   private boolean updateEditingIndex;
   private ObservableValue currentObservableValue;
   private boolean isFirstRun;
   private WeakReference oldRowItemRef;
   private static final String DEFAULT_STYLE_CLASS = "tree-table-cell";
   private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass("last-visible");

   public TreeTableCell() {
      this.weakSelectedListener = new WeakListChangeListener(this.selectedListener);
      this.weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
      this.weaktableRowUpdateObserver = new WeakInvalidationListener(this.tableRowUpdateObserver);
      this.weakEditingListener = new WeakInvalidationListener(this.editingListener);
      this.weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
      this.weakColumnStyleClassListener = new WeakListChangeListener(this.columnStyleClassListener);
      this.weakColumnStyleListener = new WeakInvalidationListener(this.columnStyleListener);
      this.weakColumnIdListener = new WeakInvalidationListener(this.columnIdListener);
      this.weakRootPropertyListener = new WeakInvalidationListener(this.rootPropertyListener);
      this.treeTableColumn = new ReadOnlyObjectWrapper(this, "treeTableColumn") {
         protected void invalidated() {
            TreeTableCell.this.updateColumnIndex();
         }
      };
      this.treeTableRow = new ReadOnlyObjectWrapper(this, "treeTableRow");
      this.isLastVisibleColumn = false;
      this.columnIndex = -1;
      this.updateEditingIndex = true;
      this.currentObservableValue = null;
      this.isFirstRun = true;
      this.getStyleClass().addAll("tree-table-cell");
      this.setAccessibleRole(AccessibleRole.TREE_TABLE_CELL);
      this.updateColumnIndex();
   }

   public final ReadOnlyObjectProperty tableColumnProperty() {
      return this.treeTableColumn.getReadOnlyProperty();
   }

   private void setTableColumn(TreeTableColumn var1) {
      this.treeTableColumn.set(var1);
   }

   public final TreeTableColumn getTableColumn() {
      return (TreeTableColumn)this.treeTableColumn.get();
   }

   private void setTreeTableView(TreeTableView var1) {
      this.treeTableViewPropertyImpl().set(var1);
   }

   public final TreeTableView getTreeTableView() {
      return this.treeTableView == null ? null : (TreeTableView)this.treeTableView.get();
   }

   public final ReadOnlyObjectProperty treeTableViewProperty() {
      return this.treeTableViewPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper treeTableViewPropertyImpl() {
      if (this.treeTableView == null) {
         this.treeTableView = new ReadOnlyObjectWrapper(this, "treeTableView") {
            private WeakReference weakTableViewRef;

            protected void invalidated() {
               TreeTableView.TreeTableViewSelectionModel var1;
               TreeTableView.TreeTableViewFocusModel var2;
               TreeTableView var3;
               if (this.weakTableViewRef != null) {
                  var3 = (TreeTableView)this.weakTableViewRef.get();
                  if (var3 != null) {
                     var1 = var3.getSelectionModel();
                     if (var1 != null) {
                        var1.getSelectedCells().removeListener(TreeTableCell.this.weakSelectedListener);
                     }

                     var2 = var3.getFocusModel();
                     if (var2 != null) {
                        var2.focusedCellProperty().removeListener(TreeTableCell.this.weakFocusedListener);
                     }

                     var3.editingCellProperty().removeListener(TreeTableCell.this.weakEditingListener);
                     var3.getVisibleLeafColumns().removeListener(TreeTableCell.this.weakVisibleLeafColumnsListener);
                     var3.rootProperty().removeListener(TreeTableCell.this.weakRootPropertyListener);
                  }
               }

               var3 = (TreeTableView)this.get();
               if (var3 != null) {
                  var1 = var3.getSelectionModel();
                  if (var1 != null) {
                     var1.getSelectedCells().addListener(TreeTableCell.this.weakSelectedListener);
                  }

                  var2 = var3.getFocusModel();
                  if (var2 != null) {
                     var2.focusedCellProperty().addListener(TreeTableCell.this.weakFocusedListener);
                  }

                  var3.editingCellProperty().addListener(TreeTableCell.this.weakEditingListener);
                  var3.getVisibleLeafColumns().addListener(TreeTableCell.this.weakVisibleLeafColumnsListener);
                  var3.rootProperty().addListener(TreeTableCell.this.weakRootPropertyListener);
                  this.weakTableViewRef = new WeakReference(var3);
               }

               TreeTableCell.this.updateColumnIndex();
            }
         };
      }

      return this.treeTableView;
   }

   private void setTreeTableRow(TreeTableRow var1) {
      this.treeTableRow.set(var1);
   }

   public final TreeTableRow getTreeTableRow() {
      return (TreeTableRow)this.treeTableRow.get();
   }

   public final ReadOnlyObjectProperty tableRowProperty() {
      return this.treeTableRow;
   }

   public void startEdit() {
      if (!this.isEditing()) {
         TreeTableView var1 = this.getTreeTableView();
         TreeTableColumn var2 = this.getTableColumn();
         if (this.isEditable() && (var1 == null || var1.isEditable()) && (var2 == null || this.getTableColumn().isEditable())) {
            if (!this.lockItemOnEdit) {
               this.updateItem(-1);
            }

            super.startEdit();
            if (var2 != null) {
               TreeTableColumn.CellEditEvent var3 = new TreeTableColumn.CellEditEvent(var1, var1.getEditingCell(), TreeTableColumn.editStartEvent(), (Object)null);
               Event.fireEvent(var2, var3);
            }

         }
      }
   }

   public void commitEdit(Object var1) {
      if (this.isEditing()) {
         TreeTableView var2 = this.getTreeTableView();
         if (var2 != null) {
            TreeTablePosition var3 = var2.getEditingCell();
            TreeTableColumn.CellEditEvent var4 = new TreeTableColumn.CellEditEvent(var2, var3, TreeTableColumn.editCommitEvent(), var1);
            Event.fireEvent(this.getTableColumn(), var4);
         }

         super.commitEdit(var1);
         this.updateItem(var1, false);
         if (var2 != null) {
            var2.edit(-1, (TreeTableColumn)null);
            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var2);
         }

      }
   }

   public void cancelEdit() {
      if (this.isEditing()) {
         TreeTableView var1 = this.getTreeTableView();
         super.cancelEdit();
         if (var1 != null) {
            TreeTablePosition var2 = var1.getEditingCell();
            if (this.updateEditingIndex) {
               var1.edit(-1, (TreeTableColumn)null);
            }

            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var1);
            TreeTableColumn.CellEditEvent var3 = new TreeTableColumn.CellEditEvent(var1, var2, TreeTableColumn.editCancelEvent(), (Object)null);
            Event.fireEvent(this.getTableColumn(), var3);
         }

      }
   }

   public void updateSelected(boolean var1) {
      if (this.getTreeTableRow() != null && !this.getTreeTableRow().isEmpty()) {
         this.setSelected(var1);
      }
   }

   void indexChanged(int var1, int var2) {
      super.indexChanged(var1, var2);
      if (!this.isEditing() || var2 != var1) {
         this.updateItem(var1);
         this.updateSelection();
         this.updateFocus();
         this.updateEditing();
      }

   }

   private void updateColumnIndex() {
      TreeTableView var1 = this.getTreeTableView();
      TreeTableColumn var2 = this.getTableColumn();
      this.columnIndex = var1 != null && var2 != null ? var1.getVisibleLeafIndex(var2) : -1;
      this.isLastVisibleColumn = this.getTableColumn() != null && this.columnIndex != -1 && this.columnIndex == var1.getVisibleLeafColumns().size() - 1;
      this.pseudoClassStateChanged(PSEUDO_CLASS_LAST_VISIBLE, this.isLastVisibleColumn);
   }

   private void updateSelection() {
      if (!this.isEmpty()) {
         boolean var1 = this.isSelected();
         if (!this.isInCellSelectionMode()) {
            if (var1) {
               this.updateSelected(false);
            }

         } else {
            TreeTableView var2 = this.getTreeTableView();
            if (this.getIndex() != -1 && var2 != null) {
               TreeTableView.TreeTableViewSelectionModel var3 = var2.getSelectionModel();
               if (var3 == null) {
                  this.updateSelected(false);
               } else {
                  boolean var4 = var3.isSelected(this.getIndex(), this.getTableColumn());
                  if (var1 != var4) {
                     this.updateSelected(var4);
                  }
               }
            }
         }
      }
   }

   private void updateFocus() {
      boolean var1 = this.isFocused();
      if (!this.isInCellSelectionMode()) {
         if (var1) {
            this.setFocused(false);
         }

      } else {
         TreeTableView var2 = this.getTreeTableView();
         if (this.getIndex() != -1 && var2 != null) {
            TreeTableView.TreeTableViewFocusModel var3 = var2.getFocusModel();
            if (var3 == null) {
               this.setFocused(false);
            } else {
               boolean var4 = var3 != null && var3.isFocused(this.getIndex(), this.getTableColumn());
               this.setFocused(var4);
            }
         }
      }
   }

   private void updateEditing() {
      TreeTableView var1 = this.getTreeTableView();
      if (this.getIndex() != -1 && var1 != null) {
         TreeTablePosition var2 = var1.getEditingCell();
         boolean var3 = this.match(var2);
         if (var3 && !this.isEditing()) {
            this.startEdit();
         } else if (!var3 && this.isEditing()) {
            this.updateEditingIndex = false;
            this.cancelEdit();
            this.updateEditingIndex = true;
         }

      }
   }

   private boolean match(TreeTablePosition var1) {
      return var1 != null && var1.getRow() == this.getIndex() && var1.getTableColumn() == this.getTableColumn();
   }

   private boolean isInCellSelectionMode() {
      TreeTableView var1 = this.getTreeTableView();
      if (var1 == null) {
         return false;
      } else {
         TreeTableView.TreeTableViewSelectionModel var2 = var1.getSelectionModel();
         return var2 != null && var2.isCellSelectionEnabled();
      }
   }

   private void updateItem(int var1) {
      if (this.currentObservableValue != null) {
         this.currentObservableValue.removeListener(this.weaktableRowUpdateObserver);
      }

      TreeTableView var2 = this.getTreeTableView();
      TreeTableColumn var3 = this.getTableColumn();
      int var4 = var2 == null ? -1 : this.getTreeTableView().getExpandedItemCount();
      int var5 = this.getIndex();
      boolean var6 = this.isEmpty();
      Object var7 = this.getItem();
      TreeTableRow var8 = this.getTreeTableRow();
      Object var9 = var8 == null ? null : var8.getItem();
      boolean var10 = var5 >= var4;
      if (!var10 && var5 >= 0 && this.columnIndex >= 0 && this.isVisible() && var3 != null && var3.isVisible() && var2.getRoot() != null) {
         label58: {
            this.currentObservableValue = var3.getCellObservableValue(var5);
            Object var11 = this.currentObservableValue == null ? null : this.currentObservableValue.getValue();
            if (var1 == var5 && !this.isItemChanged(var7, var11)) {
               Object var12 = this.oldRowItemRef != null ? this.oldRowItemRef.get() : null;
               if (var12 != null && var12.equals(var9)) {
                  break label58;
               }
            }

            this.updateItem(var11, false);
         }

         this.oldRowItemRef = new WeakReference(var9);
         if (this.currentObservableValue != null) {
            this.currentObservableValue.addListener(this.weaktableRowUpdateObserver);
         }
      } else {
         if (!var6 && var7 != null || this.isFirstRun || var10) {
            this.updateItem((Object)null, true);
            this.isFirstRun = false;
         }

      }
   }

   protected void layoutChildren() {
      if (this.itemDirty) {
         this.updateItem(-1);
         this.itemDirty = false;
      }

      super.layoutChildren();
   }

   public final void updateTreeTableView(TreeTableView var1) {
      this.setTreeTableView(var1);
   }

   public final void updateTreeTableRow(TreeTableRow var1) {
      this.setTreeTableRow(var1);
   }

   public final void updateTreeTableColumn(TreeTableColumn var1) {
      TreeTableColumn var2 = this.getTableColumn();
      if (var2 != null) {
         var2.getStyleClass().removeListener(this.weakColumnStyleClassListener);
         this.getStyleClass().removeAll(var2.getStyleClass());
         var2.idProperty().removeListener(this.weakColumnIdListener);
         var2.styleProperty().removeListener(this.weakColumnStyleListener);
         String var3 = this.getId();
         String var4 = this.getStyle();
         if (var3 != null && var3.equals(var2.getId())) {
            this.setId((String)null);
         }

         if (var4 != null && var4.equals(var2.getStyle())) {
            this.setStyle("");
         }
      }

      this.setTableColumn(var1);
      if (var1 != null) {
         this.getStyleClass().addAll(var1.getStyleClass());
         var1.getStyleClass().addListener(this.weakColumnStyleClassListener);
         var1.idProperty().addListener(this.weakColumnIdListener);
         var1.styleProperty().addListener(this.weakColumnStyleListener);
         this.possiblySetId(var1.getId());
         this.possiblySetStyle(var1.getStyle());
      }

   }

   protected Skin createDefaultSkin() {
      return new TreeTableCellSkin(this);
   }

   private void possiblySetId(String var1) {
      if (this.getId() == null || this.getId().isEmpty()) {
         this.setId(var1);
      }

   }

   private void possiblySetStyle(String var1) {
      if (this.getStyle() == null || this.getStyle().isEmpty()) {
         this.setStyle(var1);
      }

   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case ROW_INDEX:
            return this.getIndex();
         case COLUMN_INDEX:
            return this.columnIndex;
         case SELECTED:
            return this.isInCellSelectionMode() ? this.isSelected() : this.getTreeTableRow().isSelected();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case REQUEST_FOCUS:
            TreeTableView var3 = this.getTreeTableView();
            if (var3 != null) {
               TreeTableView.TreeTableViewFocusModel var4 = var3.getFocusModel();
               if (var4 != null) {
                  var4.focus(this.getIndex(), this.getTableColumn());
               }
            }
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }
}
