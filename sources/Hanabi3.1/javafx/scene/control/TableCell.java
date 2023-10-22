package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TableCellSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class TableCell extends IndexedCell {
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
   private final WeakInvalidationListener weakColumnStyleListener;
   private final WeakInvalidationListener weakColumnIdListener;
   private final WeakListChangeListener weakVisibleLeafColumnsListener;
   private final WeakListChangeListener weakColumnStyleClassListener;
   private ReadOnlyObjectWrapper tableColumn;
   private ReadOnlyObjectWrapper tableView;
   private ReadOnlyObjectWrapper tableRow;
   private boolean isLastVisibleColumn;
   private int columnIndex;
   private boolean updateEditingIndex;
   private ObservableValue currentObservableValue;
   private boolean isFirstRun;
   private WeakReference oldRowItemRef;
   private static final String DEFAULT_STYLE_CLASS = "table-cell";
   private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass("last-visible");

   public TableCell() {
      this.weakSelectedListener = new WeakListChangeListener(this.selectedListener);
      this.weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
      this.weaktableRowUpdateObserver = new WeakInvalidationListener(this.tableRowUpdateObserver);
      this.weakEditingListener = new WeakInvalidationListener(this.editingListener);
      this.weakColumnStyleListener = new WeakInvalidationListener(this.columnStyleListener);
      this.weakColumnIdListener = new WeakInvalidationListener(this.columnIdListener);
      this.weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
      this.weakColumnStyleClassListener = new WeakListChangeListener(this.columnStyleClassListener);
      this.tableColumn = new ReadOnlyObjectWrapper() {
         protected void invalidated() {
            TableCell.this.updateColumnIndex();
         }

         public Object getBean() {
            return TableCell.this;
         }

         public String getName() {
            return "tableColumn";
         }
      };
      this.tableRow = new ReadOnlyObjectWrapper(this, "tableRow");
      this.isLastVisibleColumn = false;
      this.columnIndex = -1;
      this.updateEditingIndex = true;
      this.currentObservableValue = null;
      this.isFirstRun = true;
      this.getStyleClass().addAll("table-cell");
      this.setAccessibleRole(AccessibleRole.TABLE_CELL);
      this.updateColumnIndex();
   }

   public final ReadOnlyObjectProperty tableColumnProperty() {
      return this.tableColumn.getReadOnlyProperty();
   }

   private void setTableColumn(TableColumn var1) {
      this.tableColumn.set(var1);
   }

   public final TableColumn getTableColumn() {
      return (TableColumn)this.tableColumn.get();
   }

   private void setTableView(TableView var1) {
      this.tableViewPropertyImpl().set(var1);
   }

   public final TableView getTableView() {
      return this.tableView == null ? null : (TableView)this.tableView.get();
   }

   public final ReadOnlyObjectProperty tableViewProperty() {
      return this.tableViewPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper tableViewPropertyImpl() {
      if (this.tableView == null) {
         this.tableView = new ReadOnlyObjectWrapper() {
            private WeakReference weakTableViewRef;

            protected void invalidated() {
               if (this.weakTableViewRef != null) {
                  TableCell.this.cleanUpTableViewListeners((TableView)this.weakTableViewRef.get());
               }

               if (this.get() != null) {
                  TableView.TableViewSelectionModel var1 = ((TableView)this.get()).getSelectionModel();
                  if (var1 != null) {
                     var1.getSelectedCells().addListener(TableCell.this.weakSelectedListener);
                  }

                  TableView.TableViewFocusModel var2 = ((TableView)this.get()).getFocusModel();
                  if (var2 != null) {
                     var2.focusedCellProperty().addListener(TableCell.this.weakFocusedListener);
                  }

                  ((TableView)this.get()).editingCellProperty().addListener(TableCell.this.weakEditingListener);
                  ((TableView)this.get()).getVisibleLeafColumns().addListener(TableCell.this.weakVisibleLeafColumnsListener);
                  this.weakTableViewRef = new WeakReference(this.get());
               }

               TableCell.this.updateColumnIndex();
            }

            public Object getBean() {
               return TableCell.this;
            }

            public String getName() {
               return "tableView";
            }
         };
      }

      return this.tableView;
   }

   private void setTableRow(TableRow var1) {
      this.tableRow.set(var1);
   }

   public final TableRow getTableRow() {
      return (TableRow)this.tableRow.get();
   }

   public final ReadOnlyObjectProperty tableRowProperty() {
      return this.tableRow;
   }

   public void startEdit() {
      TableView var1 = this.getTableView();
      TableColumn var2 = this.getTableColumn();
      if (this.isEditable() && (var1 == null || var1.isEditable()) && (var2 == null || this.getTableColumn().isEditable())) {
         if (!this.lockItemOnEdit) {
            this.updateItem(-1);
         }

         super.startEdit();
         if (var2 != null) {
            TableColumn.CellEditEvent var3 = new TableColumn.CellEditEvent(var1, var1.getEditingCell(), TableColumn.editStartEvent(), (Object)null);
            Event.fireEvent(var2, var3);
         }

      }
   }

   public void commitEdit(Object var1) {
      if (this.isEditing()) {
         TableView var2 = this.getTableView();
         if (var2 != null) {
            TableColumn.CellEditEvent var3 = new TableColumn.CellEditEvent(var2, var2.getEditingCell(), TableColumn.editCommitEvent(), var1);
            Event.fireEvent(this.getTableColumn(), var3);
         }

         super.commitEdit(var1);
         this.updateItem(var1, false);
         if (var2 != null) {
            var2.edit(-1, (TableColumn)null);
            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var2);
         }

      }
   }

   public void cancelEdit() {
      if (this.isEditing()) {
         TableView var1 = this.getTableView();
         super.cancelEdit();
         if (var1 != null) {
            TablePosition var2 = var1.getEditingCell();
            if (this.updateEditingIndex) {
               var1.edit(-1, (TableColumn)null);
            }

            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var1);
            TableColumn.CellEditEvent var3 = new TableColumn.CellEditEvent(var1, var2, TableColumn.editCancelEvent(), (Object)null);
            Event.fireEvent(this.getTableColumn(), var3);
         }

      }
   }

   public void updateSelected(boolean var1) {
      if (this.getTableRow() != null && !this.getTableRow().isEmpty()) {
         this.setSelected(var1);
      }
   }

   protected Skin createDefaultSkin() {
      return new TableCellSkin(this);
   }

   private void cleanUpTableViewListeners(TableView var1) {
      if (var1 != null) {
         TableView.TableViewSelectionModel var2 = var1.getSelectionModel();
         if (var2 != null) {
            var2.getSelectedCells().removeListener(this.weakSelectedListener);
         }

         TableView.TableViewFocusModel var3 = var1.getFocusModel();
         if (var3 != null) {
            var3.focusedCellProperty().removeListener(this.weakFocusedListener);
         }

         var1.editingCellProperty().removeListener(this.weakEditingListener);
         var1.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
      }

   }

   void indexChanged(int var1, int var2) {
      super.indexChanged(var1, var2);
      this.updateItem(var1);
      this.updateSelection();
      this.updateFocus();
   }

   private void updateColumnIndex() {
      TableView var1 = this.getTableView();
      TableColumn var2 = this.getTableColumn();
      this.columnIndex = var1 != null && var2 != null ? var1.getVisibleLeafIndex(var2) : -1;
      this.isLastVisibleColumn = this.getTableColumn() != null && this.columnIndex != -1 && this.columnIndex == this.getTableView().getVisibleLeafColumns().size() - 1;
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
            TableView var2 = this.getTableView();
            if (this.getIndex() != -1 && var2 != null) {
               TableView.TableViewSelectionModel var3 = var2.getSelectionModel();
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
         TableView var2 = this.getTableView();
         TableRow var3 = this.getTableRow();
         int var4 = this.getIndex();
         if (var4 != -1 && var2 != null && var3 != null) {
            TableView.TableViewFocusModel var5 = var2.getFocusModel();
            if (var5 == null) {
               this.setFocused(false);
            } else {
               boolean var6 = var5 != null && var5.isFocused(var4, this.getTableColumn());
               this.setFocused(var6);
            }
         }
      }
   }

   private void updateEditing() {
      if (this.getIndex() != -1 && this.getTableView() != null) {
         TablePosition var1 = this.getTableView().getEditingCell();
         boolean var2 = this.match(var1);
         if (var2 && !this.isEditing()) {
            this.startEdit();
         } else if (!var2 && this.isEditing()) {
            this.updateEditingIndex = false;
            this.cancelEdit();
            this.updateEditingIndex = true;
         }

      }
   }

   private boolean match(TablePosition var1) {
      return var1 != null && var1.getRow() == this.getIndex() && var1.getTableColumn() == this.getTableColumn();
   }

   private boolean isInCellSelectionMode() {
      TableView var1 = this.getTableView();
      if (var1 == null) {
         return false;
      } else {
         TableView.TableViewSelectionModel var2 = var1.getSelectionModel();
         return var2 != null && var2.isCellSelectionEnabled();
      }
   }

   private void updateItem(int var1) {
      if (this.currentObservableValue != null) {
         this.currentObservableValue.removeListener(this.weaktableRowUpdateObserver);
      }

      TableView var2 = this.getTableView();
      ObservableList var3 = var2 == null ? FXCollections.emptyObservableList() : var2.getItems();
      TableColumn var4 = this.getTableColumn();
      int var5 = var3 == null ? -1 : var3.size();
      int var6 = this.getIndex();
      boolean var7 = this.isEmpty();
      Object var8 = this.getItem();
      TableRow var9 = this.getTableRow();
      Object var10 = var9 == null ? null : var9.getItem();
      boolean var11 = var6 >= var5;
      if (!var11 && var6 >= 0 && this.columnIndex >= 0 && this.isVisible() && var4 != null && var4.isVisible()) {
         label62: {
            this.currentObservableValue = var4.getCellObservableValue(var6);
            Object var12 = this.currentObservableValue == null ? null : this.currentObservableValue.getValue();
            if (var1 == var6 && !this.isItemChanged(var8, var12)) {
               Object var13 = this.oldRowItemRef != null ? this.oldRowItemRef.get() : null;
               if (var13 != null && var13.equals(var10)) {
                  break label62;
               }
            }

            this.updateItem(var12, false);
         }

         this.oldRowItemRef = new WeakReference(var10);
         if (this.currentObservableValue != null) {
            this.currentObservableValue.addListener(this.weaktableRowUpdateObserver);
         }
      } else {
         if (!var7 && var8 != null || this.isFirstRun || var11) {
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

   public final void updateTableView(TableView var1) {
      this.setTableView(var1);
   }

   public final void updateTableRow(TableRow var1) {
      this.setTableRow(var1);
   }

   public final void updateTableColumn(TableColumn var1) {
      TableColumn var2 = this.getTableColumn();
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
            return this.isInCellSelectionMode() ? this.isSelected() : this.getTableRow().isSelected();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case REQUEST_FOCUS:
            TableView var3 = this.getTableView();
            if (var3 != null) {
               TableView.TableViewFocusModel var4 = var3.getFocusModel();
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
