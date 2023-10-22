package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TableRowSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class TableRow extends IndexedCell {
   private ListChangeListener selectedListener = (var1) -> {
      this.updateSelection();
   };
   private final InvalidationListener focusedListener = (var1) -> {
      this.updateFocus();
   };
   private final InvalidationListener editingListener = (var1) -> {
      this.updateEditing();
   };
   private final WeakListChangeListener weakSelectedListener;
   private final WeakInvalidationListener weakFocusedListener;
   private final WeakInvalidationListener weakEditingListener;
   private ReadOnlyObjectWrapper tableView;
   private boolean isFirstRun;
   private static final String DEFAULT_STYLE_CLASS = "table-row-cell";

   public TableRow() {
      this.weakSelectedListener = new WeakListChangeListener(this.selectedListener);
      this.weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
      this.weakEditingListener = new WeakInvalidationListener(this.editingListener);
      this.isFirstRun = true;
      this.getStyleClass().addAll("table-row-cell");
      this.setAccessibleRole(AccessibleRole.TABLE_ROW);
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
               TableView.TableViewSelectionModel var1;
               TableView.TableViewFocusModel var2;
               TableView var3;
               if (this.weakTableViewRef != null) {
                  var3 = (TableView)this.weakTableViewRef.get();
                  if (var3 != null) {
                     var1 = var3.getSelectionModel();
                     if (var1 != null) {
                        var1.getSelectedCells().removeListener(TableRow.this.weakSelectedListener);
                     }

                     var2 = var3.getFocusModel();
                     if (var2 != null) {
                        var2.focusedCellProperty().removeListener(TableRow.this.weakFocusedListener);
                     }

                     var3.editingCellProperty().removeListener(TableRow.this.weakEditingListener);
                  }

                  this.weakTableViewRef = null;
               }

               var3 = TableRow.this.getTableView();
               if (var3 != null) {
                  var1 = var3.getSelectionModel();
                  if (var1 != null) {
                     var1.getSelectedCells().addListener(TableRow.this.weakSelectedListener);
                  }

                  var2 = var3.getFocusModel();
                  if (var2 != null) {
                     var2.focusedCellProperty().addListener(TableRow.this.weakFocusedListener);
                  }

                  var3.editingCellProperty().addListener(TableRow.this.weakEditingListener);
                  this.weakTableViewRef = new WeakReference(this.get());
               }

            }

            public Object getBean() {
               return TableRow.this;
            }

            public String getName() {
               return "tableView";
            }
         };
      }

      return this.tableView;
   }

   protected Skin createDefaultSkin() {
      return new TableRowSkin(this);
   }

   void indexChanged(int var1, int var2) {
      super.indexChanged(var1, var2);
      this.updateItem(var1);
      this.updateSelection();
      this.updateFocus();
   }

   private void updateItem(int var1) {
      TableView var2 = this.getTableView();
      if (var2 != null && var2.getItems() != null) {
         ObservableList var3 = var2.getItems();
         int var4 = var3 == null ? -1 : var3.size();
         int var5 = this.getIndex();
         boolean var6 = var5 >= 0 && var5 < var4;
         Object var7 = this.getItem();
         boolean var8 = this.isEmpty();
         if (var6) {
            Object var9 = var3.get(var5);
            if (var1 != var5 || this.isItemChanged(var7, var9)) {
               this.updateItem(var9, false);
            }
         } else if (!var8 && var7 != null || this.isFirstRun) {
            this.updateItem((Object)null, true);
            this.isFirstRun = false;
         }

      }
   }

   private void updateSelection() {
      if (this.getIndex() != -1) {
         TableView var1 = this.getTableView();
         boolean var2 = var1 != null && var1.getSelectionModel() != null && !var1.getSelectionModel().isCellSelectionEnabled() && var1.getSelectionModel().isSelected(this.getIndex());
         this.updateSelected(var2);
      }
   }

   private void updateFocus() {
      if (this.getIndex() != -1) {
         TableView var1 = this.getTableView();
         if (var1 != null) {
            TableView.TableViewSelectionModel var2 = var1.getSelectionModel();
            TableView.TableViewFocusModel var3 = var1.getFocusModel();
            if (var2 != null && var3 != null) {
               boolean var4 = !var2.isCellSelectionEnabled() && var3.isFocused(this.getIndex());
               this.setFocused(var4);
            }
         }
      }
   }

   private void updateEditing() {
      if (this.getIndex() != -1) {
         TableView var1 = this.getTableView();
         if (var1 != null) {
            TableView.TableViewSelectionModel var2 = var1.getSelectionModel();
            if (var2 != null && !var2.isCellSelectionEnabled()) {
               TablePosition var3 = var1.getEditingCell();
               if (var3 == null || var3.getTableColumn() == null) {
                  boolean var4 = var3 == null ? false : var3.getRow() == this.getIndex();
                  if (!this.isEditing() && var4) {
                     this.startEdit();
                  } else if (this.isEditing() && !var4) {
                     this.cancelEdit();
                  }

               }
            }
         }
      }
   }

   public final void updateTableView(TableView var1) {
      this.setTableView(var1);
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case INDEX:
            return this.getIndex();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
