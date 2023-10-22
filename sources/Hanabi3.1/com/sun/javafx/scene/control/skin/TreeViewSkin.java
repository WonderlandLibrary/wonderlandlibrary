package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TreeViewBehavior;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class TreeViewSkin extends VirtualContainerBase {
   public static final String RECREATE = "treeRecreateKey";
   private static final boolean IS_PANNABLE = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("com.sun.javafx.scene.control.skin.TreeViewSkin.pannable");
   });
   private boolean needCellsRebuilt = true;
   private boolean needCellsReconfigured = false;
   private MapChangeListener propertiesMapListener = (var1x) -> {
      if (var1x.wasAdded()) {
         if ("treeRecreateKey".equals(var1x.getKey())) {
            this.needCellsRebuilt = true;
            ((TreeView)this.getSkinnable()).requestLayout();
            ((TreeView)this.getSkinnable()).getProperties().remove("treeRecreateKey");
         }

      }
   };
   private EventHandler rootListener = (var1x) -> {
      if (var1x.wasAdded() && var1x.wasRemoved() && var1x.getAddedSize() == var1x.getRemovedSize()) {
         this.rowCountDirty = true;
         ((TreeView)this.getSkinnable()).requestLayout();
      } else if (var1x.getEventType().equals(TreeItem.valueChangedEvent())) {
         this.needCellsRebuilt = true;
         ((TreeView)this.getSkinnable()).requestLayout();
      } else {
         for(EventType var2 = var1x.getEventType(); var2 != null; var2 = var2.getSuperType()) {
            if (var2.equals(TreeItem.expandedItemCountChangeEvent())) {
               this.rowCountDirty = true;
               ((TreeView)this.getSkinnable()).requestLayout();
               break;
            }
         }
      }

      ((TreeView)this.getSkinnable()).edit((TreeItem)null);
   };
   private WeakEventHandler weakRootListener;
   private WeakReference weakRoot;

   public TreeViewSkin(TreeView var1) {
      super(var1, new TreeViewBehavior(var1));
      this.flow.setPannable(IS_PANNABLE);
      this.flow.setCreateCell((var1x) -> {
         return this.createCell();
      });
      this.flow.setFixedCellSize(var1.getFixedCellSize());
      this.getChildren().add(this.flow);
      this.setRoot(((TreeView)this.getSkinnable()).getRoot());
      EventHandler var2 = (var1x) -> {
         if (var1.getEditingItem() != null) {
            var1.edit((TreeItem)null);
         }

         if (var1.isFocusTraversable()) {
            var1.requestFocus();
         }

      };
      this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      ObservableMap var3 = var1.getProperties();
      var3.remove("treeRecreateKey");
      var3.addListener(this.propertiesMapListener);
      ((TreeViewBehavior)this.getBehavior()).setOnFocusPreviousRow(() -> {
         this.onFocusPreviousCell();
      });
      ((TreeViewBehavior)this.getBehavior()).setOnFocusNextRow(() -> {
         this.onFocusNextCell();
      });
      ((TreeViewBehavior)this.getBehavior()).setOnMoveToFirstCell(() -> {
         this.onMoveToFirstCell();
      });
      ((TreeViewBehavior)this.getBehavior()).setOnMoveToLastCell(() -> {
         this.onMoveToLastCell();
      });
      ((TreeViewBehavior)this.getBehavior()).setOnScrollPageDown((var1x) -> {
         return this.onScrollPageDown(var1x);
      });
      ((TreeViewBehavior)this.getBehavior()).setOnScrollPageUp((var1x) -> {
         return this.onScrollPageUp(var1x);
      });
      ((TreeViewBehavior)this.getBehavior()).setOnSelectPreviousRow(() -> {
         this.onSelectPreviousCell();
      });
      ((TreeViewBehavior)this.getBehavior()).setOnSelectNextRow(() -> {
         this.onSelectNextCell();
      });
      this.registerChangeListener(var1.rootProperty(), "ROOT");
      this.registerChangeListener(var1.showRootProperty(), "SHOW_ROOT");
      this.registerChangeListener(var1.cellFactoryProperty(), "CELL_FACTORY");
      this.registerChangeListener(var1.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
      this.updateRowCount();
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ROOT".equals(var1)) {
         this.setRoot(((TreeView)this.getSkinnable()).getRoot());
      } else if ("SHOW_ROOT".equals(var1)) {
         if (!((TreeView)this.getSkinnable()).isShowRoot() && this.getRoot() != null) {
            this.getRoot().setExpanded(true);
         }

         this.updateRowCount();
      } else if ("CELL_FACTORY".equals(var1)) {
         this.flow.recreateCells();
      } else if ("FIXED_CELL_SIZE".equals(var1)) {
         this.flow.setFixedCellSize(((TreeView)this.getSkinnable()).getFixedCellSize());
      }

   }

   private TreeItem getRoot() {
      return this.weakRoot == null ? null : (TreeItem)this.weakRoot.get();
   }

   private void setRoot(TreeItem var1) {
      if (this.getRoot() != null && this.weakRootListener != null) {
         this.getRoot().removeEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
      }

      this.weakRoot = new WeakReference(var1);
      if (this.getRoot() != null) {
         this.weakRootListener = new WeakEventHandler(this.rootListener);
         this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
      }

      this.updateRowCount();
   }

   public int getItemCount() {
      return ((TreeView)this.getSkinnable()).getExpandedItemCount();
   }

   protected void updateRowCount() {
      int var1 = this.getItemCount();
      this.flow.setCellCount(var1);
      this.needCellsRebuilt = true;
      ((TreeView)this.getSkinnable()).requestLayout();
   }

   public TreeCell createCell() {
      TreeCell var1;
      if (((TreeView)this.getSkinnable()).getCellFactory() != null) {
         var1 = (TreeCell)((TreeView)this.getSkinnable()).getCellFactory().call(this.getSkinnable());
      } else {
         var1 = this.createDefaultCellImpl();
      }

      if (var1.getDisclosureNode() == null) {
         StackPane var2 = new StackPane();
         var2.getStyleClass().setAll((Object[])("tree-disclosure-node"));
         StackPane var3 = new StackPane();
         var3.getStyleClass().setAll((Object[])("arrow"));
         var2.getChildren().add(var3);
         var1.setDisclosureNode(var2);
      }

      var1.updateTreeView((TreeView)this.getSkinnable());
      return var1;
   }

   private TreeCell createDefaultCellImpl() {
      return new TreeCell() {
         private HBox hbox;
         private WeakReference treeItemRef;
         private InvalidationListener treeItemGraphicListener = (var1x) -> {
            this.updateDisplay(this.getItem(), this.isEmpty());
         };
         private InvalidationListener treeItemListener = new InvalidationListener() {
            public void invalidated(Observable var1) {
               TreeItem var2 = treeItemRef == null ? null : (TreeItem)treeItemRef.get();
               if (var2 != null) {
                  var2.graphicProperty().removeListener(weakTreeItemGraphicListener);
               }

               TreeItem var3 = getTreeItem();
               if (var3 != null) {
                  var3.graphicProperty().addListener(weakTreeItemGraphicListener);
                  treeItemRef = new WeakReference(var3);
               }

            }
         };
         private WeakInvalidationListener weakTreeItemGraphicListener;
         private WeakInvalidationListener weakTreeItemListener;

         {
            this.weakTreeItemGraphicListener = new WeakInvalidationListener(this.treeItemGraphicListener);
            this.weakTreeItemListener = new WeakInvalidationListener(this.treeItemListener);
            this.treeItemProperty().addListener(this.weakTreeItemListener);
            if (this.getTreeItem() != null) {
               this.getTreeItem().graphicProperty().addListener(this.weakTreeItemGraphicListener);
            }

         }

         private void updateDisplay(Object var1, boolean var2) {
            if (var1 != null && !var2) {
               TreeItem var3 = this.getTreeItem();
               Node var4 = var3 == null ? null : var3.getGraphic();
               if (var4 != null) {
                  if (var1 instanceof Node) {
                     this.setText((String)null);
                     if (this.hbox == null) {
                        this.hbox = new HBox(3.0);
                     }

                     this.hbox.getChildren().setAll((Object[])(var4, (Node)var1));
                     this.setGraphic(this.hbox);
                  } else {
                     this.hbox = null;
                     this.setText(var1.toString());
                     this.setGraphic(var4);
                  }
               } else {
                  this.hbox = null;
                  if (var1 instanceof Node) {
                     this.setText((String)null);
                     this.setGraphic((Node)var1);
                  } else {
                     this.setText(var1.toString());
                     this.setGraphic((Node)null);
                  }
               }
            } else {
               this.hbox = null;
               this.setText((String)null);
               this.setGraphic((Node)null);
            }

         }

         public void updateItem(Object var1, boolean var2) {
            super.updateItem(var1, var2);
            this.updateDisplay(var1, var2);
         }
      };
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      return this.computePrefHeight(-1.0, var3, var5, var7, var9) * 0.618033987;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return 400.0;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      super.layoutChildren(var1, var3, var5, var7);
      if (this.needCellsRebuilt) {
         this.flow.rebuildCells();
      } else if (this.needCellsReconfigured) {
         this.flow.reconfigureCells();
      }

      this.needCellsRebuilt = false;
      this.needCellsReconfigured = false;
      this.flow.resizeRelocate(var1, var3, var5, var7);
   }

   private void onFocusPreviousCell() {
      FocusModel var1 = ((TreeView)this.getSkinnable()).getFocusModel();
      if (var1 != null) {
         this.flow.show(var1.getFocusedIndex());
      }
   }

   private void onFocusNextCell() {
      FocusModel var1 = ((TreeView)this.getSkinnable()).getFocusModel();
      if (var1 != null) {
         this.flow.show(var1.getFocusedIndex());
      }
   }

   private void onSelectPreviousCell() {
      int var1 = ((TreeView)this.getSkinnable()).getSelectionModel().getSelectedIndex();
      this.flow.show(var1);
   }

   private void onSelectNextCell() {
      int var1 = ((TreeView)this.getSkinnable()).getSelectionModel().getSelectedIndex();
      this.flow.show(var1);
   }

   private void onMoveToFirstCell() {
      this.flow.show(0);
      this.flow.setPosition(0.0);
   }

   private void onMoveToLastCell() {
      this.flow.show(this.getItemCount());
      this.flow.setPosition(1.0);
   }

   public int onScrollPageDown(boolean var1) {
      TreeCell var2 = (TreeCell)this.flow.getLastVisibleCellWithinViewPort();
      if (var2 == null) {
         return -1;
      } else {
         MultipleSelectionModel var3 = ((TreeView)this.getSkinnable()).getSelectionModel();
         FocusModel var4 = ((TreeView)this.getSkinnable()).getFocusModel();
         if (var3 != null && var4 != null) {
            int var5 = var2.getIndex();
            boolean var6 = false;
            if (var1) {
               var6 = var2.isFocused() || var4.isFocused(var5);
            } else {
               var6 = var2.isSelected() || var3.isSelected(var5);
            }

            if (var6) {
               boolean var7 = var1 && var4.getFocusedIndex() == var5 || !var1 && var3.getSelectedIndex() == var5;
               if (var7) {
                  this.flow.showAsFirst(var2);
                  TreeCell var8 = (TreeCell)this.flow.getLastVisibleCellWithinViewPort();
                  var2 = var8 == null ? var2 : var8;
               }
            }

            int var9 = var2.getIndex();
            this.flow.show(var2);
            return var9;
         } else {
            return -1;
         }
      }
   }

   public int onScrollPageUp(boolean var1) {
      TreeCell var2 = (TreeCell)this.flow.getFirstVisibleCellWithinViewPort();
      if (var2 == null) {
         return -1;
      } else {
         MultipleSelectionModel var3 = ((TreeView)this.getSkinnable()).getSelectionModel();
         FocusModel var4 = ((TreeView)this.getSkinnable()).getFocusModel();
         if (var3 != null && var4 != null) {
            int var5 = var2.getIndex();
            boolean var6 = false;
            if (var1) {
               var6 = var2.isFocused() || var4.isFocused(var5);
            } else {
               var6 = var2.isSelected() || var3.isSelected(var5);
            }

            if (var6) {
               boolean var7 = var1 && var4.getFocusedIndex() == var5 || !var1 && var3.getSelectedIndex() == var5;
               if (var7) {
                  this.flow.showAsLast(var2);
                  TreeCell var8 = (TreeCell)this.flow.getFirstVisibleCellWithinViewPort();
                  var2 = var8 == null ? var2 : var8;
               }
            }

            int var9 = var2.getIndex();
            this.flow.show(var2);
            return var9;
         } else {
            return -1;
         }
      }
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case FOCUS_ITEM:
            FocusModel var10 = ((TreeView)this.getSkinnable()).getFocusModel();
            int var11 = var10.getFocusedIndex();
            if (var11 == -1) {
               if (this.getItemCount() <= 0) {
                  return null;
               }

               var11 = 0;
            }

            return this.flow.getPrivateCell(var11);
         case ROW_AT_INDEX:
            int var9 = (Integer)var2[0];
            return var9 < 0 ? null : this.flow.getPrivateCell(var9);
         case SELECTED_ITEMS:
            MultipleSelectionModel var3 = ((TreeView)this.getSkinnable()).getSelectionModel();
            ObservableList var4 = var3.getSelectedIndices();
            ArrayList var5 = new ArrayList(var4.size());
            Iterator var6 = var4.iterator();

            while(var6.hasNext()) {
               int var7 = (Integer)var6.next();
               TreeCell var8 = (TreeCell)this.flow.getPrivateCell(var7);
               if (var8 != null) {
                  var5.add(var8);
               }
            }

            return FXCollections.observableArrayList((Collection)var5);
         case VERTICAL_SCROLLBAR:
            return this.flow.getVbar();
         case HORIZONTAL_SCROLLBAR:
            return this.flow.getHbar();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   protected void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case SHOW_ITEM:
            Node var8 = (Node)var2[0];
            if (var8 instanceof TreeCell) {
               TreeCell var9 = (TreeCell)var8;
               this.flow.show(var9.getIndex());
            }
            break;
         case SET_SELECTED_ITEMS:
            ObservableList var3 = (ObservableList)var2[0];
            if (var3 != null) {
               MultipleSelectionModel var4 = ((TreeView)this.getSkinnable()).getSelectionModel();
               if (var4 != null) {
                  var4.clearSelection();
                  Iterator var5 = var3.iterator();

                  while(var5.hasNext()) {
                     Node var6 = (Node)var5.next();
                     if (var6 instanceof TreeCell) {
                        TreeCell var7 = (TreeCell)var6;
                        var4.select(var7.getIndex());
                     }
                  }
               }
            }
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }
}
