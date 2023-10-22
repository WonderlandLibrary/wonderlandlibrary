package javafx.scene.control;

import com.sun.javafx.scene.control.skin.TreeCellSkin;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

public class TreeCell extends IndexedCell {
   private final ListChangeListener selectedListener = (var1) -> {
      this.updateSelection();
   };
   private final ChangeListener selectionModelPropertyListener = new ChangeListener() {
      public void changed(ObservableValue var1, MultipleSelectionModel var2, MultipleSelectionModel var3) {
         if (var2 != null) {
            var2.getSelectedIndices().removeListener(TreeCell.this.weakSelectedListener);
         }

         if (var3 != null) {
            var3.getSelectedIndices().addListener(TreeCell.this.weakSelectedListener);
         }

         TreeCell.this.updateSelection();
      }
   };
   private final InvalidationListener focusedListener = (var1) -> {
      this.updateFocus();
   };
   private final ChangeListener focusModelPropertyListener = new ChangeListener() {
      public void changed(ObservableValue var1, FocusModel var2, FocusModel var3) {
         if (var2 != null) {
            var2.focusedIndexProperty().removeListener(TreeCell.this.weakFocusedListener);
         }

         if (var3 != null) {
            var3.focusedIndexProperty().addListener(TreeCell.this.weakFocusedListener);
         }

         TreeCell.this.updateFocus();
      }
   };
   private final InvalidationListener editingListener = (var1) -> {
      this.updateEditing();
   };
   private final InvalidationListener leafListener = new InvalidationListener() {
      public void invalidated(Observable var1) {
         TreeItem var2 = TreeCell.this.getTreeItem();
         if (var2 != null) {
            TreeCell.this.requestLayout();
         }

      }
   };
   private boolean oldIsExpanded;
   private final InvalidationListener treeItemExpandedInvalidationListener = new InvalidationListener() {
      public void invalidated(Observable var1) {
         boolean var2 = ((BooleanProperty)var1).get();
         TreeCell.this.pseudoClassStateChanged(TreeCell.EXPANDED_PSEUDOCLASS_STATE, var2);
         TreeCell.this.pseudoClassStateChanged(TreeCell.COLLAPSED_PSEUDOCLASS_STATE, !var2);
         if (var2 != TreeCell.this.oldIsExpanded) {
            TreeCell.this.notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
         }

         TreeCell.this.oldIsExpanded = var2;
      }
   };
   private final InvalidationListener rootPropertyListener = (var1) -> {
      this.updateItem(-1);
   };
   private final WeakListChangeListener weakSelectedListener;
   private final WeakChangeListener weakSelectionModelPropertyListener;
   private final WeakInvalidationListener weakFocusedListener;
   private final WeakChangeListener weakFocusModelPropertyListener;
   private final WeakInvalidationListener weakEditingListener;
   private final WeakInvalidationListener weakLeafListener;
   private final WeakInvalidationListener weakTreeItemExpandedInvalidationListener;
   private final WeakInvalidationListener weakRootPropertyListener;
   private ReadOnlyObjectWrapper treeItem;
   private ObjectProperty disclosureNode;
   private ReadOnlyObjectWrapper treeView;
   private boolean isFirstRun;
   private boolean updateEditingIndex;
   private static final String DEFAULT_STYLE_CLASS = "tree-cell";
   private static final PseudoClass EXPANDED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("expanded");
   private static final PseudoClass COLLAPSED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("collapsed");

   public TreeCell() {
      this.weakSelectedListener = new WeakListChangeListener(this.selectedListener);
      this.weakSelectionModelPropertyListener = new WeakChangeListener(this.selectionModelPropertyListener);
      this.weakFocusedListener = new WeakInvalidationListener(this.focusedListener);
      this.weakFocusModelPropertyListener = new WeakChangeListener(this.focusModelPropertyListener);
      this.weakEditingListener = new WeakInvalidationListener(this.editingListener);
      this.weakLeafListener = new WeakInvalidationListener(this.leafListener);
      this.weakTreeItemExpandedInvalidationListener = new WeakInvalidationListener(this.treeItemExpandedInvalidationListener);
      this.weakRootPropertyListener = new WeakInvalidationListener(this.rootPropertyListener);
      this.treeItem = new ReadOnlyObjectWrapper(this, "treeItem") {
         TreeItem oldValue = null;

         protected void invalidated() {
            if (this.oldValue != null) {
               this.oldValue.expandedProperty().removeListener(TreeCell.this.weakTreeItemExpandedInvalidationListener);
            }

            this.oldValue = (TreeItem)this.get();
            if (this.oldValue != null) {
               TreeCell.this.oldIsExpanded = this.oldValue.isExpanded();
               this.oldValue.expandedProperty().addListener(TreeCell.this.weakTreeItemExpandedInvalidationListener);
               TreeCell.this.weakTreeItemExpandedInvalidationListener.invalidated(this.oldValue.expandedProperty());
            }

         }
      };
      this.disclosureNode = new SimpleObjectProperty(this, "disclosureNode");
      this.treeView = new ReadOnlyObjectWrapper() {
         private WeakReference weakTreeViewRef;

         protected void invalidated() {
            MultipleSelectionModel var1;
            FocusModel var2;
            TreeView var3;
            if (this.weakTreeViewRef != null) {
               var3 = (TreeView)this.weakTreeViewRef.get();
               if (var3 != null) {
                  var1 = var3.getSelectionModel();
                  if (var1 != null) {
                     var1.getSelectedIndices().removeListener(TreeCell.this.weakSelectedListener);
                  }

                  var2 = var3.getFocusModel();
                  if (var2 != null) {
                     var2.focusedIndexProperty().removeListener(TreeCell.this.weakFocusedListener);
                  }

                  var3.editingItemProperty().removeListener(TreeCell.this.weakEditingListener);
                  var3.focusModelProperty().removeListener(TreeCell.this.weakFocusModelPropertyListener);
                  var3.selectionModelProperty().removeListener(TreeCell.this.weakSelectionModelPropertyListener);
                  var3.rootProperty().removeListener(TreeCell.this.weakRootPropertyListener);
               }

               this.weakTreeViewRef = null;
            }

            var3 = (TreeView)this.get();
            if (var3 != null) {
               var1 = var3.getSelectionModel();
               if (var1 != null) {
                  var1.getSelectedIndices().addListener(TreeCell.this.weakSelectedListener);
               }

               var2 = var3.getFocusModel();
               if (var2 != null) {
                  var2.focusedIndexProperty().addListener(TreeCell.this.weakFocusedListener);
               }

               var3.editingItemProperty().addListener(TreeCell.this.weakEditingListener);
               var3.focusModelProperty().addListener(TreeCell.this.weakFocusModelPropertyListener);
               var3.selectionModelProperty().addListener(TreeCell.this.weakSelectionModelPropertyListener);
               var3.rootProperty().addListener(TreeCell.this.weakRootPropertyListener);
               this.weakTreeViewRef = new WeakReference(var3);
            }

            TreeCell.this.updateItem(-1);
            TreeCell.this.requestLayout();
         }

         public Object getBean() {
            return TreeCell.this;
         }

         public String getName() {
            return "treeView";
         }
      };
      this.isFirstRun = true;
      this.updateEditingIndex = true;
      this.getStyleClass().addAll("tree-cell");
      this.setAccessibleRole(AccessibleRole.TREE_ITEM);
   }

   private void setTreeItem(TreeItem var1) {
      this.treeItem.set(var1);
   }

   public final TreeItem getTreeItem() {
      return (TreeItem)this.treeItem.get();
   }

   public final ReadOnlyObjectProperty treeItemProperty() {
      return this.treeItem.getReadOnlyProperty();
   }

   public final void setDisclosureNode(Node var1) {
      this.disclosureNodeProperty().set(var1);
   }

   public final Node getDisclosureNode() {
      return (Node)this.disclosureNode.get();
   }

   public final ObjectProperty disclosureNodeProperty() {
      return this.disclosureNode;
   }

   private void setTreeView(TreeView var1) {
      this.treeView.set(var1);
   }

   public final TreeView getTreeView() {
      return (TreeView)this.treeView.get();
   }

   public final ReadOnlyObjectProperty treeViewProperty() {
      return this.treeView.getReadOnlyProperty();
   }

   public void startEdit() {
      if (!this.isEditing()) {
         TreeView var1 = this.getTreeView();
         if (this.isEditable() && (var1 == null || var1.isEditable())) {
            this.updateItem(-1);
            super.startEdit();
            if (var1 != null) {
               var1.fireEvent(new TreeView.EditEvent(var1, TreeView.editStartEvent(), this.getTreeItem(), this.getItem(), (Object)null));
               var1.requestFocus();
            }

         }
      }
   }

   public void commitEdit(Object var1) {
      if (this.isEditing()) {
         TreeItem var2 = this.getTreeItem();
         TreeView var3 = this.getTreeView();
         if (var3 != null) {
            var3.fireEvent(new TreeView.EditEvent(var3, TreeView.editCommitEvent(), var2, this.getItem(), var1));
         }

         super.commitEdit(var1);
         if (var2 != null) {
            var2.setValue(var1);
            this.updateTreeItem(var2);
            this.updateItem(var1, false);
         }

         if (var3 != null) {
            var3.edit((TreeItem)null);
            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var3);
         }

      }
   }

   public void cancelEdit() {
      if (this.isEditing()) {
         TreeView var1 = this.getTreeView();
         super.cancelEdit();
         if (var1 != null) {
            if (this.updateEditingIndex) {
               var1.edit((TreeItem)null);
            }

            ControlUtils.requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(var1);
            var1.fireEvent(new TreeView.EditEvent(var1, TreeView.editCancelEvent(), this.getTreeItem(), this.getItem(), (Object)null));
         }

      }
   }

   protected Skin createDefaultSkin() {
      return new TreeCellSkin(this);
   }

   void indexChanged(int var1, int var2) {
      super.indexChanged(var1, var2);
      if (!this.isEditing() || var2 != var1) {
         this.updateItem(var1);
         this.updateSelection();
         this.updateFocus();
      }

   }

   private void updateItem(int var1) {
      TreeView var2 = this.getTreeView();
      if (var2 != null) {
         int var3 = this.getIndex();
         boolean var4 = var3 >= 0 && var3 < var2.getExpandedItemCount();
         boolean var5 = this.isEmpty();
         TreeItem var6 = this.getTreeItem();
         if (var4) {
            TreeItem var7 = var2.getTreeItem(var3);
            Object var8 = var7 == null ? null : var7.getValue();
            Object var9 = var6 == null ? null : var6.getValue();
            if (var1 != var3 || this.isItemChanged(var9, var8)) {
               this.updateTreeItem(var7);
               this.updateItem(var8, false);
            }
         } else if (!var5 && var6 != null || this.isFirstRun) {
            this.updateTreeItem((TreeItem)null);
            this.updateItem((Object)null, true);
            this.isFirstRun = false;
         }

      }
   }

   private void updateSelection() {
      if (!this.isEmpty()) {
         if (this.getIndex() != -1 && this.getTreeView() != null) {
            MultipleSelectionModel var1 = this.getTreeView().getSelectionModel();
            if (var1 == null) {
               this.updateSelected(false);
            } else {
               boolean var2 = var1.isSelected(this.getIndex());
               if (this.isSelected() != var2) {
                  this.updateSelected(var2);
               }
            }
         }
      }
   }

   private void updateFocus() {
      if (this.getIndex() != -1 && this.getTreeView() != null) {
         FocusModel var1 = this.getTreeView().getFocusModel();
         if (var1 == null) {
            this.setFocused(false);
         } else {
            this.setFocused(var1.isFocused(this.getIndex()));
         }
      }
   }

   private void updateEditing() {
      int var1 = this.getIndex();
      TreeView var2 = this.getTreeView();
      TreeItem var3 = this.getTreeItem();
      TreeItem var4 = var2 == null ? null : var2.getEditingItem();
      boolean var5 = this.isEditing();
      if (var1 != -1 && var2 != null && var3 != null) {
         boolean var6 = var3.equals(var4);
         if (var6 && !var5) {
            this.startEdit();
         } else if (!var6 && var5) {
            this.updateEditingIndex = false;
            this.cancelEdit();
            this.updateEditingIndex = true;
         }

      }
   }

   public final void updateTreeView(TreeView var1) {
      this.setTreeView(var1);
   }

   public final void updateTreeItem(TreeItem var1) {
      TreeItem var2 = this.getTreeItem();
      if (var2 != null) {
         var2.leafProperty().removeListener(this.weakLeafListener);
      }

      this.setTreeItem(var1);
      if (var1 != null) {
         var1.leafProperty().addListener(this.weakLeafListener);
      }

   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      TreeItem var3 = this.getTreeItem();
      TreeView var4 = this.getTreeView();
      switch (var1) {
         case TREE_ITEM_PARENT:
            if (var4 == null) {
               return null;
            } else if (var3 == null) {
               return null;
            } else {
               TreeItem var8 = var3.getParent();
               if (var8 == null) {
                  return null;
               }

               int var9 = var4.getRow(var8);
               return var4.queryAccessibleAttribute(AccessibleAttribute.ROW_AT_INDEX, var9);
            }
         case TREE_ITEM_COUNT:
            if (var3 == null) {
               return 0;
            } else {
               if (!var3.isExpanded()) {
                  return 0;
               }

               return var3.getChildren().size();
            }
         case TREE_ITEM_AT_INDEX:
            if (var3 == null) {
               return null;
            } else if (!var3.isExpanded()) {
               return null;
            } else {
               int var5 = (Integer)var2[0];
               if (var5 >= var3.getChildren().size()) {
                  return null;
               } else {
                  TreeItem var6 = (TreeItem)var3.getChildren().get(var5);
                  if (var6 == null) {
                     return null;
                  }

                  int var7 = var4.getRow(var6);
                  return var4.queryAccessibleAttribute(AccessibleAttribute.ROW_AT_INDEX, var7);
               }
            }
         case LEAF:
            return var3 == null ? true : var3.isLeaf();
         case EXPANDED:
            return var3 == null ? false : var3.isExpanded();
         case INDEX:
            return this.getIndex();
         case SELECTED:
            return this.isSelected();
         case DISCLOSURE_LEVEL:
            return var4 == null ? 0 : var4.getTreeItemLevel(var3);
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      TreeItem var5;
      switch (var1) {
         case EXPAND:
            var5 = this.getTreeItem();
            if (var5 != null) {
               var5.setExpanded(true);
            }
            break;
         case COLLAPSE:
            var5 = this.getTreeItem();
            if (var5 != null) {
               var5.setExpanded(false);
            }
            break;
         case REQUEST_FOCUS:
            TreeView var3 = this.getTreeView();
            if (var3 != null) {
               FocusModel var4 = var3.getFocusModel();
               if (var4 != null) {
                  var4.focus(this.getIndex());
               }
            }
            break;
         default:
            super.executeAccessibleAction(var1, new Object[0]);
      }

   }
}
