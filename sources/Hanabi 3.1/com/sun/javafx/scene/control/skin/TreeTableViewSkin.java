package com.sun.javafx.scene.control.skin;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.behavior.TreeTableViewBehavior;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class TreeTableViewSkin extends TableViewSkinBase {
   private TreeTableViewBackingList tableBackingList;
   private ObjectProperty tableBackingListProperty;
   private TreeTableView treeTableView;
   private WeakReference weakRootRef;
   private EventHandler rootListener = (var1x) -> {
      if (var1x.wasAdded() && var1x.wasRemoved() && var1x.getAddedSize() == var1x.getRemovedSize()) {
         this.rowCountDirty = true;
         ((TreeTableView)this.getSkinnable()).requestLayout();
      } else if (var1x.getEventType().equals(TreeItem.valueChangedEvent())) {
         this.needCellsRebuilt = true;
         ((TreeTableView)this.getSkinnable()).requestLayout();
      } else {
         for(EventType var2 = var1x.getEventType(); var2 != null; var2 = var2.getSuperType()) {
            if (var2.equals(TreeItem.expandedItemCountChangeEvent())) {
               this.rowCountDirty = true;
               ((TreeTableView)this.getSkinnable()).requestLayout();
               break;
            }
         }
      }

      ((TreeTableView)this.getSkinnable()).edit(-1, (TreeTableColumn)null);
   };
   private WeakEventHandler weakRootListener;

   public TreeTableViewSkin(TreeTableView var1) {
      super(var1, new TreeTableViewBehavior(var1));
      this.treeTableView = var1;
      this.tableBackingList = new TreeTableViewBackingList(var1);
      this.tableBackingListProperty = new SimpleObjectProperty(this.tableBackingList);
      this.flow.setFixedCellSize(var1.getFixedCellSize());
      super.init(var1);
      this.setRoot(((TreeTableView)this.getSkinnable()).getRoot());
      EventHandler var2 = (var1x) -> {
         if (var1.getEditingCell() != null) {
            var1.edit(-1, (TreeTableColumn)null);
         }

         if (var1.isFocusTraversable()) {
            var1.requestFocus();
         }

      };
      this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      TreeTableViewBehavior var3 = (TreeTableViewBehavior)this.getBehavior();
      var3.setOnFocusPreviousRow(() -> {
         this.onFocusPreviousCell();
      });
      var3.setOnFocusNextRow(() -> {
         this.onFocusNextCell();
      });
      var3.setOnMoveToFirstCell(() -> {
         this.onMoveToFirstCell();
      });
      var3.setOnMoveToLastCell(() -> {
         this.onMoveToLastCell();
      });
      var3.setOnScrollPageDown((var1x) -> {
         return this.onScrollPageDown(var1x);
      });
      var3.setOnScrollPageUp((var1x) -> {
         return this.onScrollPageUp(var1x);
      });
      var3.setOnSelectPreviousRow(() -> {
         this.onSelectPreviousCell();
      });
      var3.setOnSelectNextRow(() -> {
         this.onSelectNextCell();
      });
      var3.setOnSelectLeftCell(() -> {
         this.onSelectLeftCell();
      });
      var3.setOnSelectRightCell(() -> {
         this.onSelectRightCell();
      });
      this.registerChangeListener(var1.rootProperty(), "ROOT");
      this.registerChangeListener(var1.showRootProperty(), "SHOW_ROOT");
      this.registerChangeListener(var1.rowFactoryProperty(), "ROW_FACTORY");
      this.registerChangeListener(var1.expandedItemCountProperty(), "TREE_ITEM_COUNT");
      this.registerChangeListener(var1.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ROOT".equals(var1)) {
         ((TreeTableView)this.getSkinnable()).edit(-1, (TreeTableColumn)null);
         this.setRoot(((TreeTableView)this.getSkinnable()).getRoot());
      } else if ("SHOW_ROOT".equals(var1)) {
         if (!((TreeTableView)this.getSkinnable()).isShowRoot() && this.getRoot() != null) {
            this.getRoot().setExpanded(true);
         }

         this.updateRowCount();
      } else if ("ROW_FACTORY".equals(var1)) {
         this.flow.recreateCells();
      } else if ("TREE_ITEM_COUNT".equals(var1)) {
         this.rowCountDirty = true;
      } else if ("FIXED_CELL_SIZE".equals(var1)) {
         this.flow.setFixedCellSize(((TreeTableView)this.getSkinnable()).getFixedCellSize());
      }

   }

   private TreeItem getRoot() {
      return this.weakRootRef == null ? null : (TreeItem)this.weakRootRef.get();
   }

   private void setRoot(TreeItem var1) {
      if (this.getRoot() != null && this.weakRootListener != null) {
         this.getRoot().removeEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
      }

      this.weakRootRef = new WeakReference(var1);
      if (this.getRoot() != null) {
         this.weakRootListener = new WeakEventHandler(this.rootListener);
         this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), this.weakRootListener);
      }

      this.updateRowCount();
   }

   protected ObservableList getVisibleLeafColumns() {
      return this.treeTableView.getVisibleLeafColumns();
   }

   protected int getVisibleLeafIndex(TreeTableColumn var1) {
      return this.treeTableView.getVisibleLeafIndex(var1);
   }

   protected TreeTableColumn getVisibleLeafColumn(int var1) {
      return this.treeTableView.getVisibleLeafColumn(var1);
   }

   protected TreeTableView.TreeTableViewFocusModel getFocusModel() {
      return this.treeTableView.getFocusModel();
   }

   protected TreeTablePosition getFocusedCell() {
      return this.treeTableView.getFocusModel().getFocusedCell();
   }

   protected TableSelectionModel getSelectionModel() {
      return this.treeTableView.getSelectionModel();
   }

   protected ObjectProperty rowFactoryProperty() {
      return this.treeTableView.rowFactoryProperty();
   }

   protected ObjectProperty placeholderProperty() {
      return this.treeTableView.placeholderProperty();
   }

   protected ObjectProperty itemsProperty() {
      return this.tableBackingListProperty;
   }

   protected ObservableList getColumns() {
      return this.treeTableView.getColumns();
   }

   protected BooleanProperty tableMenuButtonVisibleProperty() {
      return this.treeTableView.tableMenuButtonVisibleProperty();
   }

   protected ObjectProperty columnResizePolicyProperty() {
      return (ObjectProperty)this.treeTableView.columnResizePolicyProperty();
   }

   protected ObservableList getSortOrder() {
      return this.treeTableView.getSortOrder();
   }

   protected boolean resizeColumn(TreeTableColumn var1, double var2) {
      return this.treeTableView.resizeColumn(var1, var2);
   }

   protected void edit(int var1, TreeTableColumn var2) {
      this.treeTableView.edit(var1, var2);
   }

   protected void resizeColumnToFitContent(TreeTableColumn var1, int var2) {
      TreeTableColumn var3 = var1;
      List var4 = (List)this.itemsProperty().get();
      if (var4 != null && !var4.isEmpty()) {
         Callback var5 = var1.getCellFactory();
         if (var5 != null) {
            TreeTableCell var6 = (TreeTableCell)var5.call(var1);
            if (var6 != null) {
               var6.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
               double var7 = 10.0;
               Node var9 = var6.getSkin() == null ? null : var6.getSkin().getNode();
               if (var9 instanceof Region) {
                  Region var10 = (Region)var9;
                  var7 = var10.snappedLeftInset() + var10.snappedRightInset();
               }

               TreeTableRow var23 = new TreeTableRow();
               var23.updateTreeTableView(this.treeTableView);
               int var11 = var2 == -1 ? var4.size() : Math.min(var4.size(), var2);
               double var12 = 0.0;

               double var15;
               for(int var14 = 0; var14 < var11; ++var14) {
                  var23.updateIndex(var14);
                  var23.updateTreeItem(this.treeTableView.getTreeItem(var14));
                  var6.updateTreeTableColumn(var3);
                  var6.updateTreeTableView(this.treeTableView);
                  var6.updateTreeTableRow(var23);
                  var6.updateIndex(var14);
                  if (var6.getText() != null && !var6.getText().isEmpty() || var6.getGraphic() != null) {
                     this.getChildren().add(var6);
                     var6.applyCss();
                     var15 = var6.prefWidth(-1.0);
                     var12 = Math.max(var12, var15);
                     this.getChildren().remove(var6);
                  }
               }

               var6.updateIndex(-1);
               TableColumnHeader var24 = this.getTableHeaderRow().getColumnHeaderFor(var1);
               var15 = Utils.computeTextWidth(var24.label.getFont(), var1.getText(), -1.0);
               Node var17 = var24.label.getGraphic();
               double var18 = var17 == null ? 0.0 : var17.prefWidth(-1.0) + var24.label.getGraphicTextGap();
               double var20 = var15 + var18 + 10.0 + var24.snappedLeftInset() + var24.snappedRightInset();
               var12 = Math.max(var12, var20);
               var12 += var7;
               if (this.treeTableView.getColumnResizePolicy() == TreeTableView.CONSTRAINED_RESIZE_POLICY && this.treeTableView.getWidth() > 0.0) {
                  if (var12 > var1.getMaxWidth()) {
                     var12 = var1.getMaxWidth();
                  }

                  int var22 = var1.getColumns().size();
                  if (var22 > 0) {
                     this.resizeColumnToFitContent((TreeTableColumn)var1.getColumns().get(var22 - 1), var2);
                     return;
                  }

                  this.resizeColumn(var1, (double)Math.round(var12 - var1.getWidth()));
               } else {
                  var3.impl_setWidth(var12);
               }

            }
         }
      }
   }

   public int getItemCount() {
      return this.treeTableView.getExpandedItemCount();
   }

   public TreeTableRow createCell() {
      TreeTableRow var1;
      if (this.treeTableView.getRowFactory() != null) {
         var1 = (TreeTableRow)this.treeTableView.getRowFactory().call(this.treeTableView);
      } else {
         var1 = new TreeTableRow();
      }

      if (var1.getDisclosureNode() == null) {
         StackPane var2 = new StackPane();
         var2.getStyleClass().setAll((Object[])("tree-disclosure-node"));
         var2.setMouseTransparent(true);
         StackPane var3 = new StackPane();
         var3.getStyleClass().setAll((Object[])("arrow"));
         var2.getChildren().add(var3);
         var1.setDisclosureNode(var2);
      }

      var1.updateTreeTableView(this.treeTableView);
      return var1;
   }

   protected void horizontalScroll() {
      super.horizontalScroll();
      if (((TreeTableView)this.getSkinnable()).getFixedCellSize() > 0.0) {
         this.flow.requestCellLayout();
      }

   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case ROW_AT_INDEX:
            int var8 = (Integer)var2[0];
            return var8 < 0 ? null : this.flow.getPrivateCell(var8);
         case SELECTED_ITEMS:
            ArrayList var3 = new ArrayList();
            TreeTableView.TreeTableViewSelectionModel var4 = ((TreeTableView)this.getSkinnable()).getSelectionModel();
            Iterator var5 = var4.getSelectedCells().iterator();

            while(var5.hasNext()) {
               TreeTablePosition var6 = (TreeTablePosition)var5.next();
               TreeTableRow var7 = (TreeTableRow)this.flow.getPrivateCell(var6.getRow());
               if (var7 != null) {
                  var3.add(var7);
               }
            }

            return FXCollections.observableArrayList((Collection)var3);
         case FOCUS_ITEM:
         case CELL_AT_ROW_COLUMN:
         case COLUMN_AT_INDEX:
         case HEADER:
         case VERTICAL_SCROLLBAR:
         case HORIZONTAL_SCROLLBAR:
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   protected void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case SHOW_ITEM:
            Node var8 = (Node)var2[0];
            if (var8 instanceof TreeTableCell) {
               TreeTableCell var9 = (TreeTableCell)var8;
               this.flow.show(var9.getIndex());
            }
            break;
         case SET_SELECTED_ITEMS:
            ObservableList var3 = (ObservableList)var2[0];
            if (var3 != null) {
               TreeTableView.TreeTableViewSelectionModel var4 = ((TreeTableView)this.getSkinnable()).getSelectionModel();
               if (var4 != null) {
                  var4.clearSelection();
                  Iterator var5 = var3.iterator();

                  while(var5.hasNext()) {
                     Node var6 = (Node)var5.next();
                     if (var6 instanceof TreeTableCell) {
                        TreeTableCell var7 = (TreeTableCell)var6;
                        var4.select(var7.getIndex(), var7.getTableColumn());
                     }
                  }
               }
            }
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }

   protected void updateRowCount() {
      this.updatePlaceholderRegionVisibility();
      this.tableBackingList.resetSize();
      int var1 = this.flow.getCellCount();
      int var2 = this.getItemCount();
      this.flow.setCellCount(var2);
      if (this.forceCellRecreate) {
         this.needCellsRecreated = true;
         this.forceCellRecreate = false;
      } else if (var2 != var1) {
         this.needCellsRebuilt = true;
      } else {
         this.needCellsReconfigured = true;
      }

   }

   private static class TreeTableViewBackingList extends ReadOnlyUnbackedObservableList {
      private final TreeTableView treeTable;
      private int size = -1;

      TreeTableViewBackingList(TreeTableView var1) {
         this.treeTable = var1;
      }

      void resetSize() {
         int var1 = this.size;
         this.size = -1;
         this.callObservers(new NonIterableChange.GenericAddRemoveChange(0, var1, FXCollections.emptyObservableList(), this));
      }

      public TreeItem get(int var1) {
         return this.treeTable.getTreeItem(var1);
      }

      public int size() {
         if (this.size == -1) {
            this.size = this.treeTable.getExpandedItemCount();
         }

         return this.size;
      }
   }
}
