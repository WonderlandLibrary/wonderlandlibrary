package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public abstract class TableViewSkinBase extends VirtualContainerBase {
   public static final String REFRESH = "tableRefreshKey";
   public static final String RECREATE = "tableRecreateKey";
   private boolean contentWidthDirty = true;
   private Region columnReorderLine;
   private Region columnReorderOverlay;
   private TableHeaderRow tableHeaderRow;
   private Callback rowFactory;
   private StackPane placeholderRegion;
   private Label placeholderLabel;
   private static final String EMPTY_TABLE_TEXT = ControlResources.getString("TableView.noContent");
   private static final String NO_COLUMNS_TEXT = ControlResources.getString("TableView.noColumns");
   private int visibleColCount;
   protected boolean needCellsRebuilt = true;
   protected boolean needCellsRecreated = true;
   protected boolean needCellsReconfigured = false;
   private int itemCount = -1;
   protected boolean forceCellRecreate = false;
   private static final boolean IS_PANNABLE = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("com.sun.javafx.scene.control.skin.TableViewSkin.pannable");
   });
   private MapChangeListener propertiesMapListener = (var1x) -> {
      if (var1x.wasAdded()) {
         if ("tableRefreshKey".equals(var1x.getKey())) {
            this.refreshView();
            this.getSkinnable().getProperties().remove("tableRefreshKey");
         } else if ("tableRecreateKey".equals(var1x.getKey())) {
            this.forceCellRecreate = true;
            this.refreshView();
            this.getSkinnable().getProperties().remove("tableRecreateKey");
         }

      }
   };
   private ListChangeListener rowCountListener = (var1x) -> {
      while(true) {
         if (var1x.next()) {
            if (var1x.wasReplaced()) {
               this.itemCount = 0;
            } else {
               if (var1x.getRemovedSize() != this.itemCount) {
                  continue;
               }

               this.itemCount = 0;
            }
         }

         if (this.getSkinnable() instanceof TableView) {
            this.edit(-1, (TableColumnBase)null);
         }

         this.rowCountDirty = true;
         this.getSkinnable().requestLayout();
         return;
      }
   };
   private ListChangeListener visibleLeafColumnsListener = (var1x) -> {
      this.updateVisibleColumnCount();

      while(var1x.next()) {
         this.updateVisibleLeafColumnWidthListeners(var1x.getAddedSubList(), var1x.getRemoved());
      }

   };
   private InvalidationListener widthListener = (var1x) -> {
      this.needCellsReconfigured = true;
      if (this.getSkinnable() != null) {
         this.getSkinnable().requestLayout();
      }

   };
   private InvalidationListener itemsChangeListener;
   private WeakListChangeListener weakRowCountListener;
   private WeakListChangeListener weakVisibleLeafColumnsListener;
   private WeakInvalidationListener weakWidthListener;
   private WeakInvalidationListener weakItemsChangeListener;
   private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987;

   public TableViewSkinBase(Control var1, BehaviorBase var2) {
      super(var1, var2);
      this.weakRowCountListener = new WeakListChangeListener(this.rowCountListener);
      this.weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
      this.weakWidthListener = new WeakInvalidationListener(this.widthListener);
   }

   protected void init(Control var1) {
      this.flow.setPannable(IS_PANNABLE);
      this.flow.setCreateCell((var1x) -> {
         return this.createCell();
      });
      InvalidationListener var2 = (var1x) -> {
         this.horizontalScroll();
      };
      this.flow.getHbar().valueProperty().addListener(var2);
      this.flow.getHbar().setUnitIncrement(15.0);
      this.flow.getHbar().setBlockIncrement(80.0);
      this.columnReorderLine = new Region();
      this.columnReorderLine.getStyleClass().setAll((Object[])("column-resize-line"));
      this.columnReorderLine.setManaged(false);
      this.columnReorderLine.setVisible(false);
      this.columnReorderOverlay = new Region();
      this.columnReorderOverlay.getStyleClass().setAll((Object[])("column-overlay"));
      this.columnReorderOverlay.setVisible(false);
      this.columnReorderOverlay.setManaged(false);
      this.tableHeaderRow = this.createTableHeaderRow();
      this.tableHeaderRow.setFocusTraversable(false);
      this.getChildren().addAll(this.tableHeaderRow, this.flow, this.columnReorderOverlay, this.columnReorderLine);
      this.updateVisibleColumnCount();
      this.updateVisibleLeafColumnWidthListeners(this.getVisibleLeafColumns(), FXCollections.emptyObservableList());
      this.tableHeaderRow.reorderingProperty().addListener((var1x) -> {
         this.getSkinnable().requestLayout();
      });
      this.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
      this.updateTableItems((ObservableList)null, (ObservableList)this.itemsProperty().get());
      this.itemsChangeListener = new InvalidationListener() {
         private WeakReference weakItemsRef = new WeakReference(TableViewSkinBase.this.itemsProperty().get());

         public void invalidated(Observable var1) {
            ObservableList var2 = (ObservableList)this.weakItemsRef.get();
            this.weakItemsRef = new WeakReference(TableViewSkinBase.this.itemsProperty().get());
            TableViewSkinBase.this.updateTableItems(var2, (ObservableList)TableViewSkinBase.this.itemsProperty().get());
         }
      };
      this.weakItemsChangeListener = new WeakInvalidationListener(this.itemsChangeListener);
      this.itemsProperty().addListener(this.weakItemsChangeListener);
      ObservableMap var3 = var1.getProperties();
      var3.remove("tableRefreshKey");
      var3.remove("tableRecreateKey");
      var3.addListener(this.propertiesMapListener);
      var1.addEventHandler(ScrollToEvent.scrollToColumn(), (var1x) -> {
         this.scrollHorizontally((TableColumnBase)var1x.getScrollTarget());
      });
      InvalidationListener var4 = (var1x) -> {
         this.contentWidthDirty = true;
         this.getSkinnable().requestLayout();
      };
      this.flow.widthProperty().addListener(var4);
      this.flow.getVbar().widthProperty().addListener(var4);
      this.registerChangeListener(this.rowFactoryProperty(), "ROW_FACTORY");
      this.registerChangeListener(this.placeholderProperty(), "PLACEHOLDER");
      this.registerChangeListener(var1.widthProperty(), "WIDTH");
      this.registerChangeListener(this.flow.getVbar().visibleProperty(), "VBAR_VISIBLE");
   }

   protected abstract TableSelectionModel getSelectionModel();

   protected abstract TableFocusModel getFocusModel();

   protected abstract TablePositionBase getFocusedCell();

   protected abstract ObservableList getVisibleLeafColumns();

   protected abstract int getVisibleLeafIndex(TableColumnBase var1);

   protected abstract TableColumnBase getVisibleLeafColumn(int var1);

   protected abstract ObservableList getColumns();

   protected abstract ObservableList getSortOrder();

   protected abstract ObjectProperty itemsProperty();

   protected abstract ObjectProperty rowFactoryProperty();

   protected abstract ObjectProperty placeholderProperty();

   protected abstract BooleanProperty tableMenuButtonVisibleProperty();

   protected abstract ObjectProperty columnResizePolicyProperty();

   protected abstract boolean resizeColumn(TableColumnBase var1, double var2);

   protected abstract void resizeColumnToFitContent(TableColumnBase var1, int var2);

   protected abstract void edit(int var1, TableColumnBase var2);

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ROW_FACTORY".equals(var1)) {
         Callback var2 = this.rowFactory;
         this.rowFactory = (Callback)this.rowFactoryProperty().get();
         if (var2 != this.rowFactory) {
            this.needCellsRebuilt = true;
            this.getSkinnable().requestLayout();
         }
      } else if ("PLACEHOLDER".equals(var1)) {
         this.updatePlaceholderRegionVisibility();
      } else if ("VBAR_VISIBLE".equals(var1)) {
         this.updateContentWidth();
      }

   }

   public void dispose() {
      this.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
      this.itemsProperty().removeListener(this.weakItemsChangeListener);
      this.getSkinnable().getProperties().removeListener(this.propertiesMapListener);
      this.updateTableItems((ObservableList)this.itemsProperty().get(), (ObservableList)null);
      super.dispose();
   }

   protected TableHeaderRow createTableHeaderRow() {
      return new TableHeaderRow(this);
   }

   public TableHeaderRow getTableHeaderRow() {
      return this.tableHeaderRow;
   }

   public Region getColumnReorderLine() {
      return this.columnReorderLine;
   }

   public int onScrollPageDown(boolean var1) {
      TableSelectionModel var2 = this.getSelectionModel();
      if (var2 == null) {
         return -1;
      } else {
         int var3 = this.getItemCount();
         IndexedCell var4 = this.flow.getLastVisibleCellWithinViewPort();
         if (var4 == null) {
            return -1;
         } else {
            int var5 = var4.getIndex();
            var5 = var5 >= var3 ? var3 - 1 : var5;
            boolean var6;
            if (var1) {
               var6 = var4.isFocused() || this.isCellFocused(var5);
            } else {
               var6 = var4.isSelected() || this.isCellSelected(var5);
            }

            if (var6) {
               boolean var7 = this.isLeadIndex(var1, var5);
               if (var7) {
                  this.flow.showAsFirst(var4);
                  IndexedCell var8 = this.flow.getLastVisibleCellWithinViewPort();
                  var4 = var8 == null ? var4 : var8;
               }
            }

            int var9 = var4.getIndex();
            var9 = var9 >= var3 ? var3 - 1 : var9;
            this.flow.show(var9);
            return var9;
         }
      }
   }

   public int onScrollPageUp(boolean var1) {
      IndexedCell var2 = this.flow.getFirstVisibleCellWithinViewPort();
      if (var2 == null) {
         return -1;
      } else {
         int var3 = var2.getIndex();
         boolean var4 = false;
         if (var1) {
            var4 = var2.isFocused() || this.isCellFocused(var3);
         } else {
            var4 = var2.isSelected() || this.isCellSelected(var3);
         }

         if (var4) {
            boolean var5 = this.isLeadIndex(var1, var3);
            if (var5) {
               this.flow.showAsLast(var2);
               IndexedCell var6 = this.flow.getFirstVisibleCellWithinViewPort();
               var2 = var6 == null ? var2 : var6;
            }
         }

         int var7 = var2.getIndex();
         this.flow.show(var7);
         return var7;
      }
   }

   private boolean isLeadIndex(boolean var1, int var2) {
      TableSelectionModel var3 = this.getSelectionModel();
      TableFocusModel var4 = this.getFocusModel();
      return var1 && var4.getFocusedIndex() == var2 || !var1 && var3.getSelectedIndex() == var2;
   }

   boolean isColumnPartiallyOrFullyVisible(TableColumnBase var1) {
      if (var1 != null && var1.isVisible()) {
         double var2 = this.flow.getHbar().getValue();
         double var4 = 0.0;
         ObservableList var6 = this.getVisibleLeafColumns();
         int var7 = 0;

         for(int var8 = var6.size(); var7 < var8; ++var7) {
            TableColumnBase var9 = (TableColumnBase)var6.get(var7);
            if (var9.equals(var1)) {
               break;
            }

            var4 += var9.getWidth();
         }

         double var12 = var4 + var1.getWidth();
         Insets var13 = this.getSkinnable().getPadding();
         double var10 = this.getSkinnable().getWidth() - var13.getLeft() + var13.getRight();
         return (var4 >= var2 || var12 > var2) && (var4 < var10 + var2 || var12 <= var10 + var2);
      } else {
         return false;
      }
   }

   protected void horizontalScroll() {
      this.tableHeaderRow.updateScrollX();
   }

   protected void updateRowCount() {
      this.updatePlaceholderRegionVisibility();
      int var1 = this.itemCount;
      int var2 = this.getItemCount();
      this.itemCount = var2;
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

   protected void onFocusPreviousCell() {
      TableFocusModel var1 = this.getFocusModel();
      if (var1 != null) {
         this.flow.show(var1.getFocusedIndex());
      }
   }

   protected void onFocusNextCell() {
      TableFocusModel var1 = this.getFocusModel();
      if (var1 != null) {
         this.flow.show(var1.getFocusedIndex());
      }
   }

   protected void onSelectPreviousCell() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         this.flow.show(var1.getSelectedIndex());
      }
   }

   protected void onSelectNextCell() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 != null) {
         this.flow.show(var1.getSelectedIndex());
      }
   }

   protected void onSelectLeftCell() {
      this.scrollHorizontally();
   }

   protected void onSelectRightCell() {
      this.scrollHorizontally();
   }

   protected void onMoveToFirstCell() {
      this.flow.show(0);
      this.flow.setPosition(0.0);
   }

   protected void onMoveToLastCell() {
      int var1 = this.getItemCount();
      this.flow.show(var1);
      this.flow.setPosition(1.0);
   }

   public void updateTableItems(ObservableList var1, ObservableList var2) {
      if (var1 != null) {
         var1.removeListener(this.weakRowCountListener);
      }

      if (var2 != null) {
         var2.addListener(this.weakRowCountListener);
      }

      this.rowCountDirty = true;
      this.getSkinnable().requestLayout();
   }

   private void checkContentWidthState() {
      if (this.contentWidthDirty || this.getItemCount() == 0) {
         this.updateContentWidth();
         this.contentWidthDirty = false;
      }

   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return 400.0;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.computePrefHeight(-1.0, var3, var5, var7, var9);
      ObservableList var13 = this.getVisibleLeafColumns();
      if (var13 != null && !var13.isEmpty()) {
         double var14 = var9 + var5;
         int var16 = 0;

         for(int var17 = var13.size(); var16 < var17; ++var16) {
            TableColumnBase var18 = (TableColumnBase)var13.get(var16);
            var14 += Math.max(var18.getPrefWidth(), var18.getMinWidth());
         }

         return Math.max(var14, var11 * 0.618033987);
      } else {
         return var11 * 0.618033987;
      }
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      Control var9 = this.getSkinnable();
      if (var9 != null) {
         super.layoutChildren(var1, var3, var5, var7);
         if (this.needCellsRecreated) {
            this.flow.recreateCells();
         } else if (this.needCellsRebuilt) {
            this.flow.rebuildCells();
         } else if (this.needCellsReconfigured) {
            this.flow.reconfigureCells();
         }

         this.needCellsRebuilt = false;
         this.needCellsRecreated = false;
         this.needCellsReconfigured = false;
         double var10 = var9.getLayoutBounds().getHeight() / 2.0;
         double var12 = this.tableHeaderRow.prefHeight(-1.0);
         this.layoutInArea(this.tableHeaderRow, var1, var3, var5, var12, var10, HPos.CENTER, VPos.CENTER);
         var3 += var12;
         double var14 = Math.floor(var7 - var12);
         if (this.getItemCount() != 0 && this.visibleColCount != 0) {
            this.layoutInArea(this.flow, var1, var3, var5, var14, var10, HPos.CENTER, VPos.CENTER);
         } else {
            this.layoutInArea(this.placeholderRegion, var1, var3, var5, var14, var10, HPos.CENTER, VPos.CENTER);
         }

         if (this.tableHeaderRow.getReorderingRegion() != null) {
            TableColumnHeader var16 = this.tableHeaderRow.getReorderingRegion();
            TableColumnBase var17 = var16.getTableColumn();
            if (var17 != null) {
               TableColumnHeader var18 = this.tableHeaderRow.getReorderingRegion();
               double var19 = this.tableHeaderRow.sceneToLocal(var18.localToScene(var18.getBoundsInLocal())).getMinX();
               double var21 = var16.getWidth();
               if (var19 < 0.0) {
                  var21 += var19;
               }

               var19 = var19 < 0.0 ? 0.0 : var19;
               if (var19 + var21 > var5) {
                  var21 = var5 - var19;
                  if (this.flow.getVbar().isVisible()) {
                     var21 -= this.flow.getVbar().getWidth() - 1.0;
                  }
               }

               double var23 = var14;
               if (this.flow.getHbar().isVisible()) {
                  var23 = var14 - this.flow.getHbar().getHeight();
               }

               this.columnReorderOverlay.resize(var21, var23);
               this.columnReorderOverlay.setLayoutX(var19);
               this.columnReorderOverlay.setLayoutY(this.tableHeaderRow.getHeight());
            }

            double var25 = this.columnReorderLine.snappedLeftInset() + this.columnReorderLine.snappedRightInset();
            double var20 = var7 - (this.flow.getHbar().isVisible() ? this.flow.getHbar().getHeight() - 1.0 : 0.0);
            this.columnReorderLine.resizeRelocate(0.0, this.columnReorderLine.snappedTopInset(), var25, var20);
         }

         this.columnReorderLine.setVisible(this.tableHeaderRow.isReordering());
         this.columnReorderOverlay.setVisible(this.tableHeaderRow.isReordering());
         this.checkContentWidthState();
      }
   }

   private void updateVisibleColumnCount() {
      this.visibleColCount = this.getVisibleLeafColumns().size();
      this.updatePlaceholderRegionVisibility();
      this.needCellsRebuilt = true;
      this.getSkinnable().requestLayout();
   }

   private void updateVisibleLeafColumnWidthListeners(List var1, List var2) {
      int var3 = 0;

      int var4;
      TableColumnBase var5;
      for(var4 = var2.size(); var3 < var4; ++var3) {
         var5 = (TableColumnBase)var2.get(var3);
         var5.widthProperty().removeListener(this.weakWidthListener);
      }

      var3 = 0;

      for(var4 = var1.size(); var3 < var4; ++var3) {
         var5 = (TableColumnBase)var1.get(var3);
         var5.widthProperty().addListener(this.weakWidthListener);
      }

      this.needCellsRebuilt = true;
      this.getSkinnable().requestLayout();
   }

   protected final void updatePlaceholderRegionVisibility() {
      boolean var1 = this.visibleColCount == 0 || this.getItemCount() == 0;
      if (var1) {
         if (this.placeholderRegion == null) {
            this.placeholderRegion = new StackPane();
            this.placeholderRegion.getStyleClass().setAll((Object[])("placeholder"));
            this.getChildren().add(this.placeholderRegion);
         }

         Node var2 = (Node)this.placeholderProperty().get();
         if (var2 == null) {
            if (this.placeholderLabel == null) {
               this.placeholderLabel = new Label();
            }

            String var3 = this.visibleColCount == 0 ? NO_COLUMNS_TEXT : EMPTY_TABLE_TEXT;
            this.placeholderLabel.setText(var3);
            this.placeholderRegion.getChildren().setAll((Object[])(this.placeholderLabel));
         } else {
            this.placeholderRegion.getChildren().setAll((Object[])(var2));
         }
      }

      this.flow.setVisible(!var1);
      if (this.placeholderRegion != null) {
         this.placeholderRegion.setVisible(var1);
      }

   }

   private void updateContentWidth() {
      double var1 = this.flow.getWidth();
      if (this.flow.getVbar().isVisible()) {
         var1 -= this.flow.getVbar().getWidth();
      }

      if (var1 <= 0.0) {
         Control var3 = this.getSkinnable();
         var1 = var3.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
      }

      var1 = Math.max(0.0, var1);
      this.getSkinnable().getProperties().put("TableView.contentWidth", Math.floor(var1));
   }

   private void refreshView() {
      this.rowCountDirty = true;
      Control var1 = this.getSkinnable();
      if (var1 != null) {
         var1.requestLayout();
      }

   }

   protected void scrollHorizontally() {
      TableFocusModel var1 = this.getFocusModel();
      if (var1 != null) {
         TableColumnBase var2 = this.getFocusedCell().getTableColumn();
         this.scrollHorizontally(var2);
      }
   }

   protected void scrollHorizontally(TableColumnBase var1) {
      if (var1 != null && var1.isVisible()) {
         Control var2 = this.getSkinnable();
         TableColumnHeader var3 = this.tableHeaderRow.getColumnHeaderFor(var1);
         if (var3 != null && !(var3.getWidth() <= 0.0)) {
            double var4 = 0.0;

            TableColumnBase var7;
            for(Iterator var6 = this.getVisibleLeafColumns().iterator(); var6.hasNext(); var4 += var7.getWidth()) {
               var7 = (TableColumnBase)var6.next();
               if (var7.equals(var1)) {
                  break;
               }
            }

            double var18 = var4 + var1.getWidth();
            double var8 = var2.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
            double var10 = this.flow.getHbar().getValue();
            double var12 = this.flow.getHbar().getMax();
            double var14;
            if (var4 < var10 && var4 >= 0.0) {
               var14 = var4;
            } else {
               double var16 = !(var4 < 0.0) && !(var18 > var8) ? 0.0 : var4 - var10;
               var14 = var10 + var16 > var12 ? var12 : var10 + var16;
            }

            this.flow.getHbar().setValue(var14);
         } else {
            Platform.runLater(() -> {
               this.scrollHorizontally(var1);
            });
         }
      }
   }

   private boolean isCellSelected(int var1) {
      TableSelectionModel var2 = this.getSelectionModel();
      if (var2 == null) {
         return false;
      } else if (!var2.isCellSelectionEnabled()) {
         return false;
      } else {
         int var3 = this.getVisibleLeafColumns().size();

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var2.isSelected(var1, this.getVisibleLeafColumn(var4))) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean isCellFocused(int var1) {
      TableFocusModel var2 = this.getFocusModel();
      if (var2 == null) {
         return false;
      } else {
         int var3 = this.getVisibleLeafColumns().size();

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var2.isFocused(var1, this.getVisibleLeafColumn(var4))) {
               return true;
            }
         }

         return false;
      }
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      int var3;
      switch (var1) {
         case FOCUS_ITEM:
            TableFocusModel var5 = this.getFocusModel();
            int var6 = var5.getFocusedIndex();
            if (var6 == -1) {
               if (this.placeholderRegion != null && this.placeholderRegion.isVisible()) {
                  return this.placeholderRegion.getChildren().get(0);
               }

               if (this.getItemCount() <= 0) {
                  return null;
               }

               var6 = 0;
            }

            return this.flow.getPrivateCell(var6);
         case CELL_AT_ROW_COLUMN:
            var3 = (Integer)var2[0];
            return this.flow.getPrivateCell(var3);
         case COLUMN_AT_INDEX:
            var3 = (Integer)var2[0];
            TableColumnBase var4 = this.getVisibleLeafColumn(var3);
            return this.getTableHeaderRow().getColumnHeaderFor(var4);
         case HEADER:
            return this.getTableHeaderRow();
         case VERTICAL_SCROLLBAR:
            return this.flow.getVbar();
         case HORIZONTAL_SCROLLBAR:
            return this.flow.getHbar();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
