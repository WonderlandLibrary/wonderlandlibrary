package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumnBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class TableHeaderRow extends StackPane {
   private static final String MENU_SEPARATOR = ControlResources.getString("TableView.nestedColumnControlMenuSeparator");
   private final VirtualFlow flow;
   private final TableViewSkinBase tableSkin;
   private Map columnMenuItems = new HashMap();
   private double scrollX;
   private double tableWidth;
   private Rectangle clip;
   private TableColumnHeader reorderingRegion;
   private StackPane dragHeader;
   private final Label dragHeaderLabel = new Label();
   private final NestedTableColumnHeader header;
   private Region filler;
   private Pane cornerRegion;
   private ContextMenu columnPopupMenu;
   private BooleanProperty reordering = new SimpleBooleanProperty(this, "reordering", false) {
      protected void invalidated() {
         TableColumnHeader var1 = TableHeaderRow.this.getReorderingRegion();
         if (var1 != null) {
            double var2 = var1.getNestedColumnHeader() != null ? var1.getNestedColumnHeader().getHeight() : TableHeaderRow.this.getReorderingRegion().getHeight();
            TableHeaderRow.this.dragHeader.resize(TableHeaderRow.this.dragHeader.getWidth(), var2);
            TableHeaderRow.this.dragHeader.setTranslateY(TableHeaderRow.this.getHeight() - var2);
         }

         TableHeaderRow.this.dragHeader.setVisible(TableHeaderRow.this.isReordering());
      }
   };
   private InvalidationListener tableWidthListener = (var1x) -> {
      this.updateTableWidth();
   };
   private InvalidationListener tablePaddingListener = (var1x) -> {
      this.updateTableWidth();
   };
   private ListChangeListener visibleLeafColumnsListener = new ListChangeListener() {
      public void onChanged(ListChangeListener.Change var1) {
         TableHeaderRow.this.header.setHeadersNeedUpdate();
      }
   };
   private final ListChangeListener tableColumnsListener = (var1x) -> {
      while(var1x.next()) {
         this.updateTableColumnListeners(var1x.getAddedSubList(), var1x.getRemoved());
      }

   };
   private final InvalidationListener columnTextListener = (var1x) -> {
      TableColumnBase var2 = (TableColumnBase)((StringProperty)var1x).getBean();
      CheckMenuItem var3 = (CheckMenuItem)this.columnMenuItems.get(var2);
      if (var3 != null) {
         var3.setText(this.getText(var2.getText(), var2));
      }

   };
   private final WeakInvalidationListener weakTableWidthListener;
   private final WeakInvalidationListener weakTablePaddingListener;
   private final WeakListChangeListener weakVisibleLeafColumnsListener;
   private final WeakListChangeListener weakTableColumnsListener;
   private final WeakInvalidationListener weakColumnTextListener;

   public TableHeaderRow(TableViewSkinBase var1) {
      this.weakTableWidthListener = new WeakInvalidationListener(this.tableWidthListener);
      this.weakTablePaddingListener = new WeakInvalidationListener(this.tablePaddingListener);
      this.weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
      this.weakTableColumnsListener = new WeakListChangeListener(this.tableColumnsListener);
      this.weakColumnTextListener = new WeakInvalidationListener(this.columnTextListener);
      this.tableSkin = var1;
      this.flow = var1.flow;
      this.getStyleClass().setAll((Object[])("column-header-background"));
      this.clip = new Rectangle();
      this.clip.setSmooth(false);
      this.clip.heightProperty().bind(this.heightProperty());
      this.setClip(this.clip);
      this.updateTableWidth();
      this.tableSkin.getSkinnable().widthProperty().addListener(this.weakTableWidthListener);
      this.tableSkin.getSkinnable().paddingProperty().addListener(this.weakTablePaddingListener);
      var1.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
      this.columnPopupMenu = new ContextMenu();
      this.updateTableColumnListeners(this.tableSkin.getColumns(), Collections.emptyList());
      this.tableSkin.getVisibleLeafColumns().addListener(this.weakTableColumnsListener);
      this.tableSkin.getColumns().addListener(this.weakTableColumnsListener);
      this.dragHeader = new StackPane();
      this.dragHeader.setVisible(false);
      this.dragHeader.getStyleClass().setAll((Object[])("column-drag-header"));
      this.dragHeader.setManaged(false);
      this.dragHeader.getChildren().add(this.dragHeaderLabel);
      this.header = this.createRootHeader();
      this.header.setFocusTraversable(false);
      this.header.setTableHeaderRow(this);
      this.filler = new Region();
      this.filler.getStyleClass().setAll((Object[])("filler"));
      this.setOnMousePressed((var1x) -> {
         var1.getSkinnable().requestFocus();
      });
      final StackPane var2 = new StackPane();
      var2.setSnapToPixel(false);
      var2.getStyleClass().setAll((Object[])("show-hide-column-image"));
      this.cornerRegion = new StackPane() {
         protected void layoutChildren() {
            double var1 = var2.snappedLeftInset() + var2.snappedRightInset();
            double var3 = var2.snappedTopInset() + var2.snappedBottomInset();
            var2.resize(var1, var3);
            this.positionInArea(var2, 0.0, 0.0, this.getWidth(), this.getHeight() - 3.0, 0.0, HPos.CENTER, VPos.CENTER);
         }
      };
      this.cornerRegion.getStyleClass().setAll((Object[])("show-hide-columns-button"));
      this.cornerRegion.getChildren().addAll(var2);
      this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
      this.tableSkin.tableMenuButtonVisibleProperty().addListener((var1x) -> {
         this.cornerRegion.setVisible(this.tableSkin.tableMenuButtonVisibleProperty().get());
         this.requestLayout();
      });
      this.cornerRegion.setOnMousePressed((var1x) -> {
         this.columnPopupMenu.show(this.cornerRegion, Side.BOTTOM, 0.0, 0.0);
         var1x.consume();
      });
      this.getChildren().addAll(this.filler, this.header, this.cornerRegion, this.dragHeader);
   }

   protected void layoutChildren() {
      double var1 = this.scrollX;
      double var3 = this.snapSize(this.header.prefWidth(-1.0));
      double var5 = this.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
      double var7 = this.snapSize(this.flow.getVbar().prefWidth(-1.0));
      this.header.resizeRelocate(var1, this.snappedTopInset(), var3, var5);
      Control var9 = this.tableSkin.getSkinnable();
      if (var9 != null) {
         double var10 = var9.snappedLeftInset() + var9.snappedRightInset();
         double var12 = this.tableWidth - var3 + this.filler.getInsets().getLeft() - var10;
         var12 -= this.tableSkin.tableMenuButtonVisibleProperty().get() ? var7 : 0.0;
         this.filler.setVisible(var12 > 0.0);
         if (var12 > 0.0) {
            this.filler.resizeRelocate(var1 + var3, this.snappedTopInset(), var12, var5);
         }

         this.cornerRegion.resizeRelocate(this.tableWidth - var7, this.snappedTopInset(), var7, var5);
      }
   }

   protected double computePrefWidth(double var1) {
      return this.header.prefWidth(var1);
   }

   protected double computeMinHeight(double var1) {
      return this.computePrefHeight(var1);
   }

   protected double computePrefHeight(double var1) {
      double var3 = this.header.prefHeight(var1);
      var3 = var3 == 0.0 ? 24.0 : var3;
      return this.snappedTopInset() + var3 + this.snappedBottomInset();
   }

   protected NestedTableColumnHeader createRootHeader() {
      return new NestedTableColumnHeader(this.tableSkin, (TableColumnBase)null);
   }

   protected TableViewSkinBase getTableSkin() {
      return this.tableSkin;
   }

   protected void updateScrollX() {
      this.scrollX = this.flow.getHbar().isVisible() ? -this.flow.getHbar().getValue() : 0.0;
      this.requestLayout();
      this.layout();
   }

   public final void setReordering(boolean var1) {
      this.reordering.set(var1);
   }

   public final boolean isReordering() {
      return this.reordering.get();
   }

   public final BooleanProperty reorderingProperty() {
      return this.reordering;
   }

   public TableColumnHeader getReorderingRegion() {
      return this.reorderingRegion;
   }

   public void setReorderingColumn(TableColumnBase var1) {
      this.dragHeaderLabel.setText(var1 == null ? "" : var1.getText());
   }

   public void setReorderingRegion(TableColumnHeader var1) {
      this.reorderingRegion = var1;
      if (var1 != null) {
         this.dragHeader.resize(var1.getWidth(), this.dragHeader.getHeight());
      }

   }

   public void setDragHeaderX(double var1) {
      this.dragHeader.setTranslateX(var1);
   }

   public NestedTableColumnHeader getRootHeader() {
      return this.header;
   }

   protected void updateTableWidth() {
      Control var1 = this.tableSkin.getSkinnable();
      if (var1 == null) {
         this.tableWidth = 0.0;
      } else {
         Insets var2 = var1.getInsets() == null ? Insets.EMPTY : var1.getInsets();
         double var3 = this.snapSize(var2.getLeft()) + this.snapSize(var2.getRight());
         this.tableWidth = this.snapSize(var1.getWidth()) - var3;
      }

      this.clip.setWidth(this.tableWidth);
   }

   public TableColumnHeader getColumnHeaderFor(TableColumnBase var1) {
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         var2.add(var1);

         for(TableColumnBase var3 = var1.getParentColumn(); var3 != null; var3 = var3.getParentColumn()) {
            var2.add(0, var3);
         }

         Object var4 = this.getRootHeader();

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            TableColumnBase var6 = (TableColumnBase)var2.get(var5);
            var4 = this.getColumnHeaderFor(var6, (TableColumnHeader)var4);
         }

         return (TableColumnHeader)var4;
      }
   }

   public TableColumnHeader getColumnHeaderFor(TableColumnBase var1, TableColumnHeader var2) {
      if (var2 instanceof NestedTableColumnHeader) {
         ObservableList var3 = ((NestedTableColumnHeader)var2).getColumnHeaders();

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            TableColumnHeader var5 = (TableColumnHeader)var3.get(var4);
            if (var5.getTableColumn() == var1) {
               return var5;
            }
         }
      }

      return null;
   }

   private void updateTableColumnListeners(List var1, List var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         TableColumnBase var4 = (TableColumnBase)var3.next();
         this.remove(var4);
      }

      this.rebuildColumnMenu();
   }

   private void remove(TableColumnBase var1) {
      if (var1 != null) {
         CheckMenuItem var2 = (CheckMenuItem)this.columnMenuItems.remove(var1);
         if (var2 != null) {
            var1.textProperty().removeListener(this.weakColumnTextListener);
            var2.selectedProperty().unbindBidirectional(var1.visibleProperty());
            this.columnPopupMenu.getItems().remove(var2);
         }

         if (!var1.getColumns().isEmpty()) {
            Iterator var3 = var1.getColumns().iterator();

            while(var3.hasNext()) {
               TableColumnBase var4 = (TableColumnBase)var3.next();
               this.remove(var4);
            }
         }

      }
   }

   private void rebuildColumnMenu() {
      this.columnPopupMenu.getItems().clear();
      Iterator var1 = this.getTableSkin().getColumns().iterator();

      while(true) {
         while(var1.hasNext()) {
            TableColumnBase var2 = (TableColumnBase)var1.next();
            if (var2.getColumns().isEmpty()) {
               this.createMenuItem(var2);
            } else {
               List var3 = this.getLeafColumns(var2);
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  TableColumnBase var5 = (TableColumnBase)var4.next();
                  this.createMenuItem(var5);
               }
            }
         }

         return;
      }
   }

   private List getLeafColumns(TableColumnBase var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.getColumns().iterator();

      while(var3.hasNext()) {
         TableColumnBase var4 = (TableColumnBase)var3.next();
         if (var4.getColumns().isEmpty()) {
            var2.add(var4);
         } else {
            var2.addAll(this.getLeafColumns(var4));
         }
      }

      return var2;
   }

   private void createMenuItem(TableColumnBase var1) {
      CheckMenuItem var2 = (CheckMenuItem)this.columnMenuItems.get(var1);
      if (var2 == null) {
         var2 = new CheckMenuItem();
         this.columnMenuItems.put(var1, var2);
      }

      var2.setText(this.getText(var1.getText(), var1));
      var1.textProperty().addListener(this.weakColumnTextListener);
      var2.selectedProperty().bindBidirectional(var1.visibleProperty());
      this.columnPopupMenu.getItems().add(var2);
   }

   private String getText(String var1, TableColumnBase var2) {
      String var3 = var1;

      for(TableColumnBase var4 = var2.getParentColumn(); var4 != null; var4 = var4.getParentColumn()) {
         if (this.isColumnVisibleInHeader(var4, this.tableSkin.getColumns())) {
            var3 = var4.getText() + MENU_SEPARATOR + var3;
         }
      }

      return var3;
   }

   private boolean isColumnVisibleInHeader(TableColumnBase var1, List var2) {
      if (var1 == null) {
         return false;
      } else {
         for(int var3 = 0; var3 < var2.size(); ++var3) {
            TableColumnBase var4 = (TableColumnBase)var2.get(var3);
            if (var1.equals(var4)) {
               return true;
            }

            if (!var4.getColumns().isEmpty()) {
               boolean var5 = this.isColumnVisibleInHeader(var1, var4.getColumns());
               if (var5) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
