package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ListCellSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class ListCell extends IndexedCell {
   private final InvalidationListener editingListener = (var1) -> {
      this.updateEditing();
   };
   private boolean updateEditingIndex = true;
   private final ListChangeListener selectedListener = (var1) -> {
      this.updateSelection();
   };
   private final ChangeListener selectionModelPropertyListener = new ChangeListener() {
      public void changed(ObservableValue var1, MultipleSelectionModel var2, MultipleSelectionModel var3) {
         if (var2 != null) {
            var2.getSelectedIndices().removeListener(ListCell.this.weakSelectedListener);
         }

         if (var3 != null) {
            var3.getSelectedIndices().addListener(ListCell.this.weakSelectedListener);
         }

         ListCell.this.updateSelection();
      }
   };
   private final ListChangeListener itemsListener = (var1) -> {
      boolean var2;
      boolean var7;
      boolean var9;
      for(var2 = false; var1.next(); var2 = var9 || var7 && !var1.wasReplaced() && (var1.wasRemoved() || var1.wasAdded())) {
         int var3 = this.getIndex();
         ListView var4 = this.getListView();
         ObservableList var5 = var4 == null ? null : var4.getItems();
         int var6 = var5 == null ? 0 : var5.size();
         var7 = var3 >= var1.getFrom();
         boolean var8 = var3 < var1.getTo() || var3 == var6;
         var9 = var7 && var8;
      }

      if (var2) {
         this.updateItem(-1);
      }

   };
   private final ChangeListener itemsPropertyListener = new ChangeListener() {
      public void changed(ObservableValue var1, ObservableList var2, ObservableList var3) {
         if (var2 != null) {
            var2.removeListener(ListCell.this.weakItemsListener);
         }

         if (var3 != null) {
            var3.addListener(ListCell.this.weakItemsListener);
         }

         ListCell.this.updateItem(-1);
      }
   };
   private final InvalidationListener focusedListener = (var1) -> {
      this.updateFocus();
   };
   private final ChangeListener focusModelPropertyListener = new ChangeListener() {
      public void changed(ObservableValue var1, FocusModel var2, FocusModel var3) {
         if (var2 != null) {
            var2.focusedIndexProperty().removeListener(ListCell.this.weakFocusedListener);
         }

         if (var3 != null) {
            var3.focusedIndexProperty().addListener(ListCell.this.weakFocusedListener);
         }

         ListCell.this.updateFocus();
      }
   };
   private final WeakInvalidationListener weakEditingListener;
   private final WeakListChangeListener weakSelectedListener;
   private final WeakChangeListener weakSelectionModelPropertyListener;
   private final WeakListChangeListener weakItemsListener;
   private final WeakChangeListener weakItemsPropertyListener;
   private final WeakInvalidationListener weakFocusedListener;
   private final WeakChangeListener weakFocusModelPropertyListener;
   private ReadOnlyObjectWrapper listView;
   private boolean firstRun;
   private static final String DEFAULT_STYLE_CLASS = "list-cell";

   public ListCell() {
      this.weakEditingListener = new WeakInvalidationListener(this.editingListener);
      this.weakSelectedListener = new WeakListChangeListener(this.selectedListener);
      this.weakSelectionModelPropertyListener = new WeakChangeListener(this.selectionModelPropertyListener);
      this.weakItemsListener = new WeakListChangeListener(this.itemsListener);
      this.weakItemsPropertyListener = new WeakChangeListener(this.itemsPropertyListener);
      this.weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
      this.weakFocusModelPropertyListener = new WeakChangeListener(this.focusModelPropertyListener);
      this.listView = new ReadOnlyObjectWrapper(this, "listView") {
         private WeakReference weakListViewRef = new WeakReference((Object)null);

         protected void invalidated() {
            ListView var1 = (ListView)this.get();
            ListView var2 = (ListView)this.weakListViewRef.get();
            if (var1 != var2) {
               MultipleSelectionModel var3;
               FocusModel var4;
               ObservableList var5;
               if (var2 != null) {
                  var3 = var2.getSelectionModel();
                  if (var3 != null) {
                     var3.getSelectedIndices().removeListener(ListCell.this.weakSelectedListener);
                  }

                  var4 = var2.getFocusModel();
                  if (var4 != null) {
                     var4.focusedIndexProperty().removeListener(ListCell.this.weakFocusedListener);
                  }

                  var5 = var2.getItems();
                  if (var5 != null) {
                     var5.removeListener(ListCell.this.weakItemsListener);
                  }

                  var2.editingIndexProperty().removeListener(ListCell.this.weakEditingListener);
                  var2.itemsProperty().removeListener(ListCell.this.weakItemsPropertyListener);
                  var2.focusModelProperty().removeListener(ListCell.this.weakFocusModelPropertyListener);
                  var2.selectionModelProperty().removeListener(ListCell.this.weakSelectionModelPropertyListener);
               }

               if (var1 != null) {
                  var3 = var1.getSelectionModel();
                  if (var3 != null) {
                     var3.getSelectedIndices().addListener(ListCell.this.weakSelectedListener);
                  }

                  var4 = var1.getFocusModel();
                  if (var4 != null) {
                     var4.focusedIndexProperty().addListener(ListCell.this.weakFocusedListener);
                  }

                  var5 = var1.getItems();
                  if (var5 != null) {
                     var5.addListener(ListCell.this.weakItemsListener);
                  }

                  var1.editingIndexProperty().addListener(ListCell.this.weakEditingListener);
                  var1.itemsProperty().addListener(ListCell.this.weakItemsPropertyListener);
                  var1.focusModelProperty().addListener(ListCell.this.weakFocusModelPropertyListener);
                  var1.selectionModelProperty().addListener(ListCell.this.weakSelectionModelPropertyListener);
                  this.weakListViewRef = new WeakReference(var1);
               }

               ListCell.this.updateItem(-1);
               ListCell.this.updateSelection();
               ListCell.this.updateFocus();
               ListCell.this.requestLayout();
            }
         }
      };
      this.firstRun = true;
      this.getStyleClass().addAll("list-cell");
      this.setAccessibleRole(AccessibleRole.LIST_ITEM);
   }

   private void setListView(ListView var1) {
      this.listView.set(var1);
   }

   public final ListView getListView() {
      return (ListView)this.listView.get();
   }

   public final ReadOnlyObjectProperty listViewProperty() {
      return this.listView.getReadOnlyProperty();
   }

   void indexChanged(int var1, int var2) {
      super.indexChanged(var1, var2);
      if (!this.isEditing() || var2 != var1) {
         this.updateItem(var1);
         this.updateSelection();
         this.updateFocus();
      }

   }

   protected Skin createDefaultSkin() {
      return new ListCellSkin(this);
   }

   public void startEdit() {
      ListView var1 = this.getListView();
      if (this.isEditable() && (var1 == null || var1.isEditable())) {
         super.startEdit();
         if (var1 != null) {
            var1.fireEvent(new ListView.EditEvent(var1, ListView.editStartEvent(), (Object)null, var1.getEditingIndex()));
            var1.edit(this.getIndex());
            var1.requestFocus();
         }

      }
   }

   public void commitEdit(Object var1) {
      if (this.isEditing()) {
         ListView var2 = this.getListView();
         if (var2 != null) {
            var2.fireEvent(new ListView.EditEvent(var2, ListView.editCommitEvent(), var1, var2.getEditingIndex()));
         }

         super.commitEdit(var1);
         this.updateItem(var1, false);
         if (var2 != null) {
            var2.edit(-1);
            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var2);
         }

      }
   }

   public void cancelEdit() {
      if (this.isEditing()) {
         ListView var1 = this.getListView();
         super.cancelEdit();
         if (var1 != null) {
            int var2 = var1.getEditingIndex();
            if (this.updateEditingIndex) {
               var1.edit(-1);
            }

            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var1);
            var1.fireEvent(new ListView.EditEvent(var1, ListView.editCancelEvent(), (Object)null, var2));
         }

      }
   }

   private void updateItem(int var1) {
      ListView var2 = this.getListView();
      ObservableList var3 = var2 == null ? null : var2.getItems();
      int var4 = this.getIndex();
      int var5 = var3 == null ? -1 : var3.size();
      boolean var6 = var3 != null && var4 >= 0 && var4 < var5;
      Object var7 = this.getItem();
      boolean var8 = this.isEmpty();
      if (var6) {
         Object var9 = var3.get(var4);
         if (var1 != var4 || this.isItemChanged(var7, var9)) {
            this.updateItem(var9, false);
         }
      } else if (!var8 && var7 != null || this.firstRun) {
         this.updateItem((Object)null, true);
         this.firstRun = false;
      }

   }

   public final void updateListView(ListView var1) {
      this.setListView(var1);
   }

   private void updateSelection() {
      if (!this.isEmpty()) {
         int var1 = this.getIndex();
         ListView var2 = this.getListView();
         if (var1 != -1 && var2 != null) {
            MultipleSelectionModel var3 = var2.getSelectionModel();
            if (var3 == null) {
               this.updateSelected(false);
            } else {
               boolean var4 = var3.isSelected(var1);
               if (this.isSelected() != var4) {
                  this.updateSelected(var4);
               }
            }
         }
      }
   }

   private void updateFocus() {
      int var1 = this.getIndex();
      ListView var2 = this.getListView();
      if (var1 != -1 && var2 != null) {
         FocusModel var3 = var2.getFocusModel();
         if (var3 == null) {
            this.setFocused(false);
         } else {
            this.setFocused(var3.isFocused(var1));
         }
      }
   }

   private void updateEditing() {
      int var1 = this.getIndex();
      ListView var2 = this.getListView();
      int var3 = var2 == null ? -1 : var2.getEditingIndex();
      boolean var4 = this.isEditing();
      if (var1 != -1 && var2 != null) {
         if (var1 == var3 && !var4) {
            this.startEdit();
         } else if (var1 != var3 && var4) {
            this.updateEditingIndex = false;
            this.cancelEdit();
            this.updateEditingIndex = true;
         }
      }

   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case INDEX:
            return this.getIndex();
         case SELECTED:
            return this.isSelected();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case REQUEST_FOCUS:
            ListView var3 = this.getListView();
            if (var3 != null) {
               FocusModel var4 = var3.getFocusModel();
               if (var4 != null) {
                  var4.focus(this.getIndex());
               }
            }
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }
}
