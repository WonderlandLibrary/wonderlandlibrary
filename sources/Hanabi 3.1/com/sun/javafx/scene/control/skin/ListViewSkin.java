package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListViewBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class ListViewSkin extends VirtualContainerBase {
   public static final String RECREATE = "listRecreateKey";
   private StackPane placeholderRegion;
   private Node placeholderNode;
   private static final String EMPTY_LIST_TEXT = ControlResources.getString("ListView.noContent");
   private static final boolean IS_PANNABLE = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("com.sun.javafx.scene.control.skin.ListViewSkin.pannable");
   });
   private ObservableList listViewItems;
   private final InvalidationListener itemsChangeListener = (var1x) -> {
      this.updateListViewItems();
   };
   private MapChangeListener propertiesMapListener = (var1x) -> {
      if (var1x.wasAdded()) {
         if ("listRecreateKey".equals(var1x.getKey())) {
            this.needCellsRebuilt = true;
            ((ListView)this.getSkinnable()).requestLayout();
            ((ListView)this.getSkinnable()).getProperties().remove("listRecreateKey");
         }

      }
   };
   private final ListChangeListener listViewItemsListener = new ListChangeListener() {
      public void onChanged(ListChangeListener.Change var1) {
         while(true) {
            if (var1.next()) {
               if (var1.wasReplaced()) {
                  for(int var2 = var1.getFrom(); var2 < var1.getTo(); ++var2) {
                     ListViewSkin.this.flow.setCellDirty(var2);
                  }
               } else {
                  if (var1.getRemovedSize() != ListViewSkin.this.itemCount) {
                     continue;
                  }

                  ListViewSkin.this.itemCount = 0;
               }
            }

            ((ListView)ListViewSkin.this.getSkinnable()).edit(-1);
            ListViewSkin.this.rowCountDirty = true;
            ((ListView)ListViewSkin.this.getSkinnable()).requestLayout();
            return;
         }
      }
   };
   private final WeakListChangeListener weakListViewItemsListener;
   private int itemCount;
   private boolean needCellsRebuilt;
   private boolean needCellsReconfigured;

   public ListViewSkin(ListView var1) {
      super(var1, new ListViewBehavior(var1));
      this.weakListViewItemsListener = new WeakListChangeListener(this.listViewItemsListener);
      this.itemCount = -1;
      this.needCellsRebuilt = true;
      this.needCellsReconfigured = false;
      this.updateListViewItems();
      this.flow.setId("virtual-flow");
      this.flow.setPannable(IS_PANNABLE);
      this.flow.setVertical(((ListView)this.getSkinnable()).getOrientation() == Orientation.VERTICAL);
      this.flow.setCreateCell((var1x) -> {
         return this.createCell();
      });
      this.flow.setFixedCellSize(var1.getFixedCellSize());
      this.getChildren().add(this.flow);
      EventHandler var2 = (var1x) -> {
         if (var1.getEditingIndex() > -1) {
            var1.edit(-1);
         }

         if (var1.isFocusTraversable()) {
            var1.requestFocus();
         }

      };
      this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      this.updateRowCount();
      var1.itemsProperty().addListener(new WeakInvalidationListener(this.itemsChangeListener));
      ObservableMap var3 = var1.getProperties();
      var3.remove("listRecreateKey");
      var3.addListener(this.propertiesMapListener);
      ((ListViewBehavior)this.getBehavior()).setOnFocusPreviousRow(() -> {
         this.onFocusPreviousCell();
      });
      ((ListViewBehavior)this.getBehavior()).setOnFocusNextRow(() -> {
         this.onFocusNextCell();
      });
      ((ListViewBehavior)this.getBehavior()).setOnMoveToFirstCell(() -> {
         this.onMoveToFirstCell();
      });
      ((ListViewBehavior)this.getBehavior()).setOnMoveToLastCell(() -> {
         this.onMoveToLastCell();
      });
      ((ListViewBehavior)this.getBehavior()).setOnScrollPageDown((var1x) -> {
         return this.onScrollPageDown(var1x);
      });
      ((ListViewBehavior)this.getBehavior()).setOnScrollPageUp((var1x) -> {
         return this.onScrollPageUp(var1x);
      });
      ((ListViewBehavior)this.getBehavior()).setOnSelectPreviousRow(() -> {
         this.onSelectPreviousCell();
      });
      ((ListViewBehavior)this.getBehavior()).setOnSelectNextRow(() -> {
         this.onSelectNextCell();
      });
      this.registerChangeListener(var1.itemsProperty(), "ITEMS");
      this.registerChangeListener(var1.orientationProperty(), "ORIENTATION");
      this.registerChangeListener(var1.cellFactoryProperty(), "CELL_FACTORY");
      this.registerChangeListener(var1.parentProperty(), "PARENT");
      this.registerChangeListener(var1.placeholderProperty(), "PLACEHOLDER");
      this.registerChangeListener(var1.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ITEMS".equals(var1)) {
         this.updateListViewItems();
      } else if ("ORIENTATION".equals(var1)) {
         this.flow.setVertical(((ListView)this.getSkinnable()).getOrientation() == Orientation.VERTICAL);
      } else if ("CELL_FACTORY".equals(var1)) {
         this.flow.recreateCells();
      } else if ("PARENT".equals(var1)) {
         if (((ListView)this.getSkinnable()).getParent() != null && ((ListView)this.getSkinnable()).isVisible()) {
            ((ListView)this.getSkinnable()).requestLayout();
         }
      } else if ("PLACEHOLDER".equals(var1)) {
         this.updatePlaceholderRegionVisibility();
      } else if ("FIXED_CELL_SIZE".equals(var1)) {
         this.flow.setFixedCellSize(((ListView)this.getSkinnable()).getFixedCellSize());
      }

   }

   public void updateListViewItems() {
      if (this.listViewItems != null) {
         this.listViewItems.removeListener(this.weakListViewItemsListener);
      }

      this.listViewItems = ((ListView)this.getSkinnable()).getItems();
      if (this.listViewItems != null) {
         this.listViewItems.addListener(this.weakListViewItemsListener);
      }

      this.rowCountDirty = true;
      ((ListView)this.getSkinnable()).requestLayout();
   }

   public int getItemCount() {
      return this.itemCount;
   }

   protected void updateRowCount() {
      if (this.flow != null) {
         int var1 = this.itemCount;
         int var2 = this.listViewItems == null ? 0 : this.listViewItems.size();
         this.itemCount = var2;
         this.flow.setCellCount(var2);
         this.updatePlaceholderRegionVisibility();
         if (var2 != var1) {
            this.needCellsRebuilt = true;
         } else {
            this.needCellsReconfigured = true;
         }

      }
   }

   protected final void updatePlaceholderRegionVisibility() {
      boolean var1 = this.getItemCount() == 0;
      if (var1) {
         this.placeholderNode = ((ListView)this.getSkinnable()).getPlaceholder();
         if (this.placeholderNode == null && EMPTY_LIST_TEXT != null && !EMPTY_LIST_TEXT.isEmpty()) {
            this.placeholderNode = new Label();
            ((Label)this.placeholderNode).setText(EMPTY_LIST_TEXT);
         }

         if (this.placeholderNode != null) {
            if (this.placeholderRegion == null) {
               this.placeholderRegion = new StackPane();
               this.placeholderRegion.getStyleClass().setAll((Object[])("placeholder"));
               this.getChildren().add(this.placeholderRegion);
            }

            this.placeholderRegion.getChildren().setAll((Object[])(this.placeholderNode));
         }
      }

      this.flow.setVisible(!var1);
      if (this.placeholderRegion != null) {
         this.placeholderRegion.setVisible(var1);
      }

   }

   public ListCell createCell() {
      ListCell var1;
      if (((ListView)this.getSkinnable()).getCellFactory() != null) {
         var1 = (ListCell)((ListView)this.getSkinnable()).getCellFactory().call(this.getSkinnable());
      } else {
         var1 = createDefaultCellImpl();
      }

      var1.updateListView((ListView)this.getSkinnable());
      return var1;
   }

   private static ListCell createDefaultCellImpl() {
      return new ListCell() {
         public void updateItem(Object var1, boolean var2) {
            super.updateItem(var1, var2);
            if (var2) {
               this.setText((String)null);
               this.setGraphic((Node)null);
            } else if (var1 instanceof Node) {
               this.setText((String)null);
               Node var3 = this.getGraphic();
               Node var4 = (Node)var1;
               if (var3 == null || !var3.equals(var4)) {
                  this.setGraphic(var4);
               }
            } else {
               this.setText(var1 == null ? "null" : var1.toString());
               this.setGraphic((Node)null);
            }

         }
      };
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
      if (this.getItemCount() == 0) {
         if (this.placeholderRegion != null) {
            this.placeholderRegion.setVisible(var5 > 0.0 && var7 > 0.0);
            this.placeholderRegion.resizeRelocate(var1, var3, var5, var7);
         }
      } else {
         this.flow.resizeRelocate(var1, var3, var5, var7);
      }

   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      this.checkState();
      if (this.getItemCount() == 0) {
         if (this.placeholderRegion == null) {
            this.updatePlaceholderRegionVisibility();
         }

         if (this.placeholderRegion != null) {
            return this.placeholderRegion.prefWidth(var1) + var9 + var5;
         }
      }

      return this.computePrefHeight(-1.0, var3, var5, var7, var9) * 0.618033987;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return 400.0;
   }

   private void onFocusPreviousCell() {
      FocusModel var1 = ((ListView)this.getSkinnable()).getFocusModel();
      if (var1 != null) {
         this.flow.show(var1.getFocusedIndex());
      }
   }

   private void onFocusNextCell() {
      FocusModel var1 = ((ListView)this.getSkinnable()).getFocusModel();
      if (var1 != null) {
         this.flow.show(var1.getFocusedIndex());
      }
   }

   private void onSelectPreviousCell() {
      MultipleSelectionModel var1 = ((ListView)this.getSkinnable()).getSelectionModel();
      if (var1 != null) {
         int var2 = var1.getSelectedIndex();
         this.flow.show(var2);
         IndexedCell var3 = this.flow.getFirstVisibleCell();
         if (var3 == null || var2 < var3.getIndex()) {
            this.flow.setPosition((double)var2 / (double)this.getItemCount());
         }

      }
   }

   private void onSelectNextCell() {
      MultipleSelectionModel var1 = ((ListView)this.getSkinnable()).getSelectionModel();
      if (var1 != null) {
         int var2 = var1.getSelectedIndex();
         this.flow.show(var2);
         ListCell var3 = (ListCell)this.flow.getLastVisibleCell();
         if (var3 == null || var3.getIndex() < var2) {
            this.flow.setPosition((double)var2 / (double)this.getItemCount());
         }

      }
   }

   private void onMoveToFirstCell() {
      this.flow.show(0);
      this.flow.setPosition(0.0);
   }

   private void onMoveToLastCell() {
      int var1 = this.getItemCount() - 1;
      this.flow.show(var1);
      this.flow.setPosition(1.0);
   }

   private int onScrollPageDown(boolean var1) {
      ListCell var2 = (ListCell)this.flow.getLastVisibleCellWithinViewPort();
      if (var2 == null) {
         return -1;
      } else {
         MultipleSelectionModel var3 = ((ListView)this.getSkinnable()).getSelectionModel();
         FocusModel var4 = ((ListView)this.getSkinnable()).getFocusModel();
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
                  ListCell var8 = (ListCell)this.flow.getLastVisibleCellWithinViewPort();
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

   private int onScrollPageUp(boolean var1) {
      ListCell var2 = (ListCell)this.flow.getFirstVisibleCellWithinViewPort();
      if (var2 == null) {
         return -1;
      } else {
         MultipleSelectionModel var3 = ((ListView)this.getSkinnable()).getSelectionModel();
         FocusModel var4 = ((ListView)this.getSkinnable()).getFocusModel();
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
                  ListCell var8 = (ListCell)this.flow.getFirstVisibleCellWithinViewPort();
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
            FocusModel var10 = ((ListView)this.getSkinnable()).getFocusModel();
            int var11 = var10.getFocusedIndex();
            if (var11 == -1) {
               if (this.placeholderRegion != null && this.placeholderRegion.isVisible()) {
                  return this.placeholderRegion.getChildren().get(0);
               }

               if (this.getItemCount() <= 0) {
                  return null;
               }

               var11 = 0;
            }

            return this.flow.getPrivateCell(var11);
         case ITEM_COUNT:
            return this.getItemCount();
         case ITEM_AT_INDEX:
            Integer var9 = (Integer)var2[0];
            if (var9 == null) {
               return null;
            } else {
               if (0 <= var9 && var9 < this.getItemCount()) {
                  return this.flow.getPrivateCell(var9);
               }

               return null;
            }
         case SELECTED_ITEMS:
            MultipleSelectionModel var3 = ((ListView)this.getSkinnable()).getSelectionModel();
            ObservableList var4 = var3.getSelectedIndices();
            ArrayList var5 = new ArrayList(var4.size());
            Iterator var6 = var4.iterator();

            while(var6.hasNext()) {
               int var7 = (Integer)var6.next();
               ListCell var8 = (ListCell)this.flow.getPrivateCell(var7);
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
            if (var8 instanceof ListCell) {
               ListCell var9 = (ListCell)var8;
               this.flow.show(var9.getIndex());
            }
            break;
         case SET_SELECTED_ITEMS:
            ObservableList var3 = (ObservableList)var2[0];
            if (var3 != null) {
               MultipleSelectionModel var4 = ((ListView)this.getSkinnable()).getSelectionModel();
               if (var4 != null) {
                  var4.clearSelection();
                  Iterator var5 = var3.iterator();

                  while(var5.hasNext()) {
                     Node var6 = (Node)var5.next();
                     if (var6 instanceof ListCell) {
                        ListCell var7 = (ListCell)var6;
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
