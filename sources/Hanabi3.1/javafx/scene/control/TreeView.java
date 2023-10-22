package javafx.scene.control;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.TreeCellBehavior;
import com.sun.javafx.scene.control.skin.TreeViewSkin;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.Callback;

@DefaultProperty("root")
public class TreeView extends Control {
   private static final EventType EDIT_ANY_EVENT;
   private static final EventType EDIT_START_EVENT;
   private static final EventType EDIT_CANCEL_EVENT;
   private static final EventType EDIT_COMMIT_EVENT;
   private boolean expandedItemCountDirty;
   private Map treeItemCacheMap;
   private final EventHandler rootEvent;
   private WeakEventHandler weakRootEventListener;
   private ObjectProperty cellFactory;
   private ObjectProperty root;
   private BooleanProperty showRoot;
   private ObjectProperty selectionModel;
   private ObjectProperty focusModel;
   private ReadOnlyIntegerWrapper expandedItemCount;
   private DoubleProperty fixedCellSize;
   private BooleanProperty editable;
   private ReadOnlyObjectWrapper editingItem;
   private ObjectProperty onEditStart;
   private ObjectProperty onEditCommit;
   private ObjectProperty onEditCancel;
   private ObjectProperty onScrollTo;
   private static final String DEFAULT_STYLE_CLASS = "tree-view";

   public static EventType editAnyEvent() {
      return EDIT_ANY_EVENT;
   }

   public static EventType editStartEvent() {
      return EDIT_START_EVENT;
   }

   public static EventType editCancelEvent() {
      return EDIT_CANCEL_EVENT;
   }

   public static EventType editCommitEvent() {
      return EDIT_COMMIT_EVENT;
   }

   /** @deprecated */
   @Deprecated
   public static int getNodeLevel(TreeItem var0) {
      if (var0 == null) {
         return -1;
      } else {
         int var1 = 0;

         for(TreeItem var2 = var0.getParent(); var2 != null; var2 = var2.getParent()) {
            ++var1;
         }

         return var1;
      }
   }

   public TreeView() {
      this((TreeItem)null);
   }

   public TreeView(TreeItem var1) {
      this.expandedItemCountDirty = true;
      this.treeItemCacheMap = new HashMap();
      this.rootEvent = (var1x) -> {
         EventType var2 = var1x.getEventType();

         boolean var3;
         for(var3 = false; var2 != null; var2 = var2.getSuperType()) {
            if (var2.equals(TreeItem.expandedItemCountChangeEvent())) {
               var3 = true;
               break;
            }
         }

         if (var3) {
            this.expandedItemCountDirty = true;
            this.requestLayout();
         }

      };
      this.root = new SimpleObjectProperty(this, "root") {
         private WeakReference weakOldItem;

         protected void invalidated() {
            TreeItem var1 = this.weakOldItem == null ? null : (TreeItem)this.weakOldItem.get();
            if (var1 != null && TreeView.this.weakRootEventListener != null) {
               var1.removeEventHandler(TreeItem.treeNotificationEvent(), TreeView.this.weakRootEventListener);
            }

            TreeItem var2 = TreeView.this.getRoot();
            if (var2 != null) {
               TreeView.this.weakRootEventListener = new WeakEventHandler(TreeView.this.rootEvent);
               TreeView.this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), TreeView.this.weakRootEventListener);
               this.weakOldItem = new WeakReference(var2);
            }

            TreeView.this.edit((TreeItem)null);
            TreeView.this.expandedItemCountDirty = true;
            TreeView.this.updateRootExpanded();
         }
      };
      this.expandedItemCount = new ReadOnlyIntegerWrapper(this, "expandedItemCount", 0);
      this.getStyleClass().setAll((Object[])("tree-view"));
      this.setAccessibleRole(AccessibleRole.TREE_VIEW);
      this.setRoot(var1);
      this.updateExpandedItemCount(var1);
      TreeViewBitSetSelectionModel var2 = new TreeViewBitSetSelectionModel(this);
      this.setSelectionModel(var2);
      this.setFocusModel(new TreeViewFocusModel(this));
   }

   public final void setCellFactory(Callback var1) {
      this.cellFactoryProperty().set(var1);
   }

   public final Callback getCellFactory() {
      return this.cellFactory == null ? null : (Callback)this.cellFactory.get();
   }

   public final ObjectProperty cellFactoryProperty() {
      if (this.cellFactory == null) {
         this.cellFactory = new SimpleObjectProperty(this, "cellFactory");
      }

      return this.cellFactory;
   }

   public final void setRoot(TreeItem var1) {
      this.rootProperty().set(var1);
   }

   public final TreeItem getRoot() {
      return this.root == null ? null : (TreeItem)this.root.get();
   }

   public final ObjectProperty rootProperty() {
      return this.root;
   }

   public final void setShowRoot(boolean var1) {
      this.showRootProperty().set(var1);
   }

   public final boolean isShowRoot() {
      return this.showRoot == null ? true : this.showRoot.get();
   }

   public final BooleanProperty showRootProperty() {
      if (this.showRoot == null) {
         this.showRoot = new SimpleBooleanProperty(this, "showRoot", true) {
            protected void invalidated() {
               TreeView.this.updateRootExpanded();
               TreeView.this.updateExpandedItemCount(TreeView.this.getRoot());
            }
         };
      }

      return this.showRoot;
   }

   public final void setSelectionModel(MultipleSelectionModel var1) {
      this.selectionModelProperty().set(var1);
   }

   public final MultipleSelectionModel getSelectionModel() {
      return this.selectionModel == null ? null : (MultipleSelectionModel)this.selectionModel.get();
   }

   public final ObjectProperty selectionModelProperty() {
      if (this.selectionModel == null) {
         this.selectionModel = new SimpleObjectProperty(this, "selectionModel");
      }

      return this.selectionModel;
   }

   public final void setFocusModel(FocusModel var1) {
      this.focusModelProperty().set(var1);
   }

   public final FocusModel getFocusModel() {
      return this.focusModel == null ? null : (FocusModel)this.focusModel.get();
   }

   public final ObjectProperty focusModelProperty() {
      if (this.focusModel == null) {
         this.focusModel = new SimpleObjectProperty(this, "focusModel");
      }

      return this.focusModel;
   }

   public final ReadOnlyIntegerProperty expandedItemCountProperty() {
      return this.expandedItemCount.getReadOnlyProperty();
   }

   private void setExpandedItemCount(int var1) {
      this.expandedItemCount.set(var1);
   }

   public final int getExpandedItemCount() {
      if (this.expandedItemCountDirty) {
         this.updateExpandedItemCount(this.getRoot());
      }

      return this.expandedItemCount.get();
   }

   public final void setFixedCellSize(double var1) {
      this.fixedCellSizeProperty().set(var1);
   }

   public final double getFixedCellSize() {
      return this.fixedCellSize == null ? -1.0 : this.fixedCellSize.get();
   }

   public final DoubleProperty fixedCellSizeProperty() {
      if (this.fixedCellSize == null) {
         this.fixedCellSize = new StyleableDoubleProperty(-1.0) {
            public CssMetaData getCssMetaData() {
               return TreeView.StyleableProperties.FIXED_CELL_SIZE;
            }

            public Object getBean() {
               return TreeView.this;
            }

            public String getName() {
               return "fixedCellSize";
            }
         };
      }

      return this.fixedCellSize;
   }

   public final void setEditable(boolean var1) {
      this.editableProperty().set(var1);
   }

   public final boolean isEditable() {
      return this.editable == null ? false : this.editable.get();
   }

   public final BooleanProperty editableProperty() {
      if (this.editable == null) {
         this.editable = new SimpleBooleanProperty(this, "editable", false);
      }

      return this.editable;
   }

   private void setEditingItem(TreeItem var1) {
      this.editingItemPropertyImpl().set(var1);
   }

   public final TreeItem getEditingItem() {
      return this.editingItem == null ? null : (TreeItem)this.editingItem.get();
   }

   public final ReadOnlyObjectProperty editingItemProperty() {
      return this.editingItemPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper editingItemPropertyImpl() {
      if (this.editingItem == null) {
         this.editingItem = new ReadOnlyObjectWrapper(this, "editingItem");
      }

      return this.editingItem;
   }

   public final void setOnEditStart(EventHandler var1) {
      this.onEditStartProperty().set(var1);
   }

   public final EventHandler getOnEditStart() {
      return this.onEditStart == null ? null : (EventHandler)this.onEditStart.get();
   }

   public final ObjectProperty onEditStartProperty() {
      if (this.onEditStart == null) {
         this.onEditStart = new SimpleObjectProperty(this, "onEditStart") {
            protected void invalidated() {
               TreeView.this.setEventHandler(TreeView.EDIT_START_EVENT, (EventHandler)this.get());
            }
         };
      }

      return this.onEditStart;
   }

   public final void setOnEditCommit(EventHandler var1) {
      this.onEditCommitProperty().set(var1);
   }

   public final EventHandler getOnEditCommit() {
      return this.onEditCommit == null ? null : (EventHandler)this.onEditCommit.get();
   }

   public final ObjectProperty onEditCommitProperty() {
      if (this.onEditCommit == null) {
         this.onEditCommit = new SimpleObjectProperty(this, "onEditCommit") {
            protected void invalidated() {
               TreeView.this.setEventHandler(TreeView.EDIT_COMMIT_EVENT, (EventHandler)this.get());
            }
         };
      }

      return this.onEditCommit;
   }

   public final void setOnEditCancel(EventHandler var1) {
      this.onEditCancelProperty().set(var1);
   }

   public final EventHandler getOnEditCancel() {
      return this.onEditCancel == null ? null : (EventHandler)this.onEditCancel.get();
   }

   public final ObjectProperty onEditCancelProperty() {
      if (this.onEditCancel == null) {
         this.onEditCancel = new SimpleObjectProperty(this, "onEditCancel") {
            protected void invalidated() {
               TreeView.this.setEventHandler(TreeView.EDIT_CANCEL_EVENT, (EventHandler)this.get());
            }
         };
      }

      return this.onEditCancel;
   }

   protected void layoutChildren() {
      if (this.expandedItemCountDirty) {
         this.updateExpandedItemCount(this.getRoot());
      }

      super.layoutChildren();
   }

   public void edit(TreeItem var1) {
      if (this.isEditable()) {
         this.setEditingItem(var1);
      }
   }

   public void scrollTo(int var1) {
      ControlUtils.scrollToIndex(this, var1);
   }

   public void setOnScrollTo(EventHandler var1) {
      this.onScrollToProperty().set(var1);
   }

   public EventHandler getOnScrollTo() {
      return this.onScrollTo != null ? (EventHandler)this.onScrollTo.get() : null;
   }

   public ObjectProperty onScrollToProperty() {
      if (this.onScrollTo == null) {
         this.onScrollTo = new ObjectPropertyBase() {
            protected void invalidated() {
               TreeView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), (EventHandler)this.get());
            }

            public Object getBean() {
               return TreeView.this;
            }

            public String getName() {
               return "onScrollTo";
            }
         };
      }

      return this.onScrollTo;
   }

   public int getRow(TreeItem var1) {
      return TreeUtil.getRow(var1, this.getRoot(), this.expandedItemCountDirty, this.isShowRoot());
   }

   public TreeItem getTreeItem(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         int var2 = this.isShowRoot() ? var1 : var1 + 1;
         if (this.expandedItemCountDirty) {
            this.updateExpandedItemCount(this.getRoot());
         } else if (this.treeItemCacheMap.containsKey(var2)) {
            SoftReference var3 = (SoftReference)this.treeItemCacheMap.get(var2);
            TreeItem var4 = (TreeItem)var3.get();
            if (var4 != null) {
               return var4;
            }
         }

         TreeItem var5 = TreeUtil.getItem(this.getRoot(), var2, this.expandedItemCountDirty);
         this.treeItemCacheMap.put(var2, new SoftReference(var5));
         return var5;
      }
   }

   public int getTreeItemLevel(TreeItem var1) {
      TreeItem var2 = this.getRoot();
      if (var1 == null) {
         return -1;
      } else if (var1 == var2) {
         return 0;
      } else {
         int var3 = 0;

         for(TreeItem var4 = var1.getParent(); var4 != null; var4 = var4.getParent()) {
            ++var3;
            if (var4 == var2) {
               break;
            }
         }

         return var3;
      }
   }

   protected Skin createDefaultSkin() {
      return new TreeViewSkin(this);
   }

   public void refresh() {
      this.getProperties().put("treeRecreateKey", Boolean.TRUE);
   }

   private void updateExpandedItemCount(TreeItem var1) {
      this.setExpandedItemCount(TreeUtil.updateExpandedItemCount(var1, this.expandedItemCountDirty, this.isShowRoot()));
      if (this.expandedItemCountDirty) {
         this.treeItemCacheMap.clear();
      }

      this.expandedItemCountDirty = false;
   }

   private void updateRootExpanded() {
      if (!this.isShowRoot() && this.getRoot() != null && !this.getRoot().isExpanded()) {
         this.getRoot().setExpanded(true);
      }

   }

   public static List getClassCssMetaData() {
      return TreeView.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case MULTIPLE_SELECTION:
            MultipleSelectionModel var3 = this.getSelectionModel();
            return var3 != null && var3.getSelectionMode() == SelectionMode.MULTIPLE;
         case ROW_COUNT:
            return this.getExpandedItemCount();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   static {
      EDIT_ANY_EVENT = new EventType(Event.ANY, "TREE_VIEW_EDIT");
      EDIT_START_EVENT = new EventType(editAnyEvent(), "EDIT_START");
      EDIT_CANCEL_EVENT = new EventType(editAnyEvent(), "EDIT_CANCEL");
      EDIT_COMMIT_EVENT = new EventType(editAnyEvent(), "EDIT_COMMIT");
   }

   static class TreeViewFocusModel extends FocusModel {
      private final TreeView treeView;
      private final ChangeListener rootPropertyListener = (var1x, var2, var3) -> {
         this.updateTreeEventListener(var2, var3);
      };
      private final WeakChangeListener weakRootPropertyListener;
      private EventHandler treeItemListener;
      private WeakEventHandler weakTreeItemListener;

      public TreeViewFocusModel(TreeView var1) {
         this.weakRootPropertyListener = new WeakChangeListener(this.rootPropertyListener);
         this.treeItemListener = new EventHandler() {
            public void handle(TreeItem.TreeModificationEvent var1) {
               if (TreeViewFocusModel.this.getFocusedIndex() != -1) {
                  int var2 = TreeViewFocusModel.this.treeView.getRow(var1.getTreeItem());
                  int var3 = 0;
                  if (var1.getChange() != null) {
                     var1.getChange().next();
                  }

                  int var4;
                  do {
                     if (var1.wasExpanded()) {
                        if (var2 < TreeViewFocusModel.this.getFocusedIndex()) {
                           var3 += var1.getTreeItem().getExpandedDescendentCount(false) - 1;
                        }
                     } else if (var1.wasCollapsed()) {
                        if (var2 < TreeViewFocusModel.this.getFocusedIndex()) {
                           var3 += -var1.getTreeItem().previousExpandedDescendentCount + 1;
                        }
                     } else if (var1.wasAdded()) {
                        TreeItem var7 = var1.getTreeItem();
                        if (var7.isExpanded()) {
                           for(int var8 = 0; var8 < var1.getAddedChildren().size(); ++var8) {
                              TreeItem var6 = (TreeItem)var1.getAddedChildren().get(var8);
                              var2 = TreeViewFocusModel.this.treeView.getRow(var6);
                              if (var6 != null && var2 <= TreeViewFocusModel.this.getFocusedIndex()) {
                                 var3 += var6.getExpandedDescendentCount(false);
                              }
                           }
                        }
                     } else if (var1.wasRemoved()) {
                        var2 += var1.getFrom() + 1;

                        for(var4 = 0; var4 < var1.getRemovedChildren().size(); ++var4) {
                           TreeItem var5 = (TreeItem)var1.getRemovedChildren().get(var4);
                           if (var5 != null && var5.equals(TreeViewFocusModel.this.getFocusedItem())) {
                              TreeViewFocusModel.this.focus(Math.max(0, TreeViewFocusModel.this.getFocusedIndex() - 1));
                              return;
                           }
                        }

                        if (var2 <= TreeViewFocusModel.this.getFocusedIndex()) {
                           var3 += var1.getTreeItem().isExpanded() ? -var1.getRemovedSize() : 0;
                        }
                     }
                  } while(var1.getChange() != null && var1.getChange().next());

                  if (var3 != 0) {
                     var4 = TreeViewFocusModel.this.getFocusedIndex() + var3;
                     if (var4 >= 0) {
                        Platform.runLater(() -> {
                           TreeViewFocusModel.this.focus(var4);
                        });
                     }
                  }

               }
            }
         };
         this.treeView = var1;
         this.treeView.rootProperty().addListener(this.weakRootPropertyListener);
         this.updateTreeEventListener((TreeItem)null, var1.getRoot());
         if (var1.getExpandedItemCount() > 0) {
            this.focus(0);
         }

         var1.showRootProperty().addListener((var1x) -> {
            if (this.isFocused(0)) {
               this.focus(-1);
               this.focus(0);
            }

         });
      }

      private void updateTreeEventListener(TreeItem var1, TreeItem var2) {
         if (var1 != null && this.weakTreeItemListener != null) {
            var1.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

         if (var2 != null) {
            this.weakTreeItemListener = new WeakEventHandler(this.treeItemListener);
            var2.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

      }

      protected int getItemCount() {
         return this.treeView == null ? -1 : this.treeView.getExpandedItemCount();
      }

      protected TreeItem getModelItem(int var1) {
         if (this.treeView == null) {
            return null;
         } else {
            return var1 >= 0 && var1 < this.treeView.getExpandedItemCount() ? this.treeView.getTreeItem(var1) : null;
         }
      }

      public void focus(int var1) {
         if (this.treeView.expandedItemCountDirty) {
            this.treeView.updateExpandedItemCount(this.treeView.getRoot());
         }

         super.focus(var1);
      }
   }

   static class TreeViewBitSetSelectionModel extends MultipleSelectionModelBase {
      private TreeView treeView = null;
      private ChangeListener rootPropertyListener = (var1x, var2, var3) -> {
         this.updateDefaultSelection();
         this.updateTreeEventListener(var2, var3);
      };
      private EventHandler treeItemListener = (var1x) -> {
         if (this.getSelectedIndex() != -1 || this.getSelectedItem() != null) {
            TreeItem var2 = var1x.getTreeItem();
            if (var2 != null) {
               this.treeView.expandedItemCountDirty = true;
               int var3 = this.treeView.getRow(var2);
               int var4 = 0;
               ListChangeListener.Change var5 = var1x.getChange();
               if (var5 != null) {
                  var5.next();
               }

               do {
                  int var6 = var5 == null ? 0 : var5.getAddedSize();
                  int var7 = var5 == null ? 0 : var5.getRemovedSize();
                  if (var1x.wasExpanded()) {
                     var4 += var2.getExpandedDescendentCount(false) - 1;
                     ++var3;
                  } else {
                     int var9;
                     int var13;
                     int var15;
                     if (!var1x.wasCollapsed()) {
                        if (!var1x.wasPermutated()) {
                           if (var1x.wasAdded()) {
                              var4 += var2.isExpanded() ? var6 : 0;
                              var3 = this.treeView.getRow((TreeItem)var1x.getChange().getAddedSubList().get(0));
                           } else if (var1x.wasRemoved()) {
                              var4 += var2.isExpanded() ? -var7 : 0;
                              var3 += var1x.getFrom() + 1;
                              ObservableList var18 = this.getSelectedIndices();
                              var9 = this.getSelectedIndex();
                              ObservableList var19 = this.getSelectedItems();
                              TreeItem var20 = (TreeItem)this.getSelectedItem();
                              List var21 = var1x.getChange().getRemoved();

                              for(var13 = 0; var13 < var18.size() && !var19.isEmpty(); ++var13) {
                                 int var22 = (Integer)var18.get(var13);
                                 if (var22 > var19.size()) {
                                    break;
                                 }

                                 if (var21.size() == 1 && var19.size() == 1 && var20 != null && var20.equals(var21.get(0)) && var9 < this.getItemCount()) {
                                    var15 = var9 == 0 ? 0 : var9 - 1;
                                    TreeItem var16 = this.getModelItem(var15);
                                    if (!var20.equals(var16)) {
                                       this.select(var16);
                                    }
                                 }
                              }
                           }
                        }
                     } else {
                        var2.getExpandedDescendentCount(false);
                        int var8 = var2.previousExpandedDescendentCount;
                        var9 = this.getSelectedIndex();
                        boolean var10 = var9 >= var3 + 1 && var9 < var3 + var8;
                        boolean var11 = false;
                        this.startAtomic();
                        int var12 = var3 + 1;
                        var13 = var3 + var8;
                        ArrayList var14 = new ArrayList();

                        for(var15 = var12; var15 < var13; ++var15) {
                           if (this.isSelected(var15)) {
                              var11 = true;
                              this.clearSelection(var15);
                              var14.add(var15);
                           }
                        }

                        this.stopAtomic();
                        if (var10 && var11) {
                           this.select(var3);
                        } else {
                           NonIterableChange.GenericAddRemoveChange var23 = new NonIterableChange.GenericAddRemoveChange(var12, var12, var14, this.selectedIndicesSeq);
                           this.selectedIndicesSeq.callObservers(var23);
                        }

                        var4 += -var8 + 1;
                        ++var3;
                     }
                  }
               } while(var1x.getChange() != null && var1x.getChange().next());

               this.shiftSelection(var3, var4, (Callback)null);
               if (var1x.wasAdded() || var1x.wasRemoved()) {
                  Integer var17 = (Integer)TreeCellBehavior.getAnchor(this.treeView, (Object)null);
                  if (var17 != null && this.isSelected(var17 + var4)) {
                     TreeCellBehavior.setAnchor(this.treeView, var17 + var4, false);
                  }
               }

            }
         }
      };
      private WeakChangeListener weakRootPropertyListener;
      private WeakEventHandler weakTreeItemListener;

      public TreeViewBitSetSelectionModel(TreeView var1) {
         this.weakRootPropertyListener = new WeakChangeListener(this.rootPropertyListener);
         if (var1 == null) {
            throw new IllegalArgumentException("TreeView can not be null");
         } else {
            this.treeView = var1;
            this.treeView.rootProperty().addListener(this.weakRootPropertyListener);
            this.treeView.showRootProperty().addListener((var2) -> {
               this.shiftSelection(0, var1.isShowRoot() ? 1 : -1, (Callback)null);
            });
            this.updateTreeEventListener((TreeItem)null, var1.getRoot());
            this.updateDefaultSelection();
         }
      }

      private void updateTreeEventListener(TreeItem var1, TreeItem var2) {
         if (var1 != null && this.weakTreeItemListener != null) {
            var1.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

         if (var2 != null) {
            this.weakTreeItemListener = new WeakEventHandler(this.treeItemListener);
            var2.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

      }

      public void selectAll() {
         int var1 = (Integer)TreeCellBehavior.getAnchor(this.treeView, -1);
         super.selectAll();
         TreeCellBehavior.setAnchor(this.treeView, var1, false);
      }

      public void select(TreeItem var1) {
         if (var1 == null && this.getSelectionMode() == SelectionMode.SINGLE) {
            this.clearSelection();
         } else {
            if (var1 != null) {
               for(TreeItem var2 = var1.getParent(); var2 != null; var2 = var2.getParent()) {
                  var2.setExpanded(true);
               }
            }

            this.treeView.updateExpandedItemCount(this.treeView.getRoot());
            int var3 = this.treeView.getRow(var1);
            if (var3 == -1) {
               this.setSelectedIndex(-1);
               this.setSelectedItem(var1);
            } else {
               this.select(var3);
            }

         }
      }

      public void clearAndSelect(int var1) {
         TreeCellBehavior.setAnchor(this.treeView, var1, false);
         super.clearAndSelect(var1);
      }

      protected void focus(int var1) {
         if (this.treeView.getFocusModel() != null) {
            this.treeView.getFocusModel().focus(var1);
         }

         this.treeView.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
      }

      protected int getFocusedIndex() {
         return this.treeView.getFocusModel() == null ? -1 : this.treeView.getFocusModel().getFocusedIndex();
      }

      protected int getItemCount() {
         return this.treeView == null ? 0 : this.treeView.getExpandedItemCount();
      }

      public TreeItem getModelItem(int var1) {
         if (this.treeView == null) {
            return null;
         } else {
            return var1 >= 0 && var1 < this.treeView.getExpandedItemCount() ? this.treeView.getTreeItem(var1) : null;
         }
      }

      private void updateDefaultSelection() {
         this.clearSelection();
         this.focus(this.getItemCount() > 0 ? 0 : -1);
      }
   }

   public static class EditEvent extends Event {
      private static final long serialVersionUID = -4437033058917528976L;
      public static final EventType ANY;
      private final Object oldValue;
      private final Object newValue;
      private final transient TreeItem treeItem;

      public EditEvent(TreeView var1, EventType var2, TreeItem var3, Object var4, Object var5) {
         super(var1, Event.NULL_SOURCE_TARGET, var2);
         this.oldValue = var4;
         this.newValue = var5;
         this.treeItem = var3;
      }

      public TreeView getSource() {
         return (TreeView)super.getSource();
      }

      public TreeItem getTreeItem() {
         return this.treeItem;
      }

      public Object getNewValue() {
         return this.newValue;
      }

      public Object getOldValue() {
         return this.oldValue;
      }

      static {
         ANY = TreeView.EDIT_ANY_EVENT;
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData FIXED_CELL_SIZE = new CssMetaData("-fx-fixed-cell-size", SizeConverter.getInstance(), -1.0) {
         public Double getInitialValue(TreeView var1) {
            return var1.getFixedCellSize();
         }

         public boolean isSettable(TreeView var1) {
            return var1.fixedCellSize == null || !var1.fixedCellSize.isBound();
         }

         public StyleableProperty getStyleableProperty(TreeView var1) {
            return (StyleableProperty)var1.fixedCellSizeProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(FIXED_CELL_SIZE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
