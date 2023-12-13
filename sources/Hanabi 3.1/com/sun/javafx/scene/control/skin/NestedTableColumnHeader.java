package com.sun.javafx.scene.control.skin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class NestedTableColumnHeader extends TableColumnHeader {
   private static final int DRAG_RECT_WIDTH = 4;
   private static final String TABLE_COLUMN_KEY = "TableColumn";
   private static final String TABLE_COLUMN_HEADER_KEY = "TableColumnHeader";
   private ObservableList columns;
   private TableColumnHeader label;
   private ObservableList columnHeaders;
   private double lastX = 0.0;
   private double dragAnchorX = 0.0;
   private Map dragRects = new WeakHashMap();
   boolean updateColumns = true;
   private final ListChangeListener columnsListener = (var1x) -> {
      this.setHeadersNeedUpdate();
   };
   private final WeakListChangeListener weakColumnsListener;
   private static final EventHandler rectMousePressed = new EventHandler() {
      public void handle(MouseEvent var1) {
         Rectangle var2 = (Rectangle)var1.getSource();
         TableColumnBase var3 = (TableColumnBase)var2.getProperties().get("TableColumn");
         NestedTableColumnHeader var4 = (NestedTableColumnHeader)var2.getProperties().get("TableColumnHeader");
         if (var4.isColumnResizingEnabled()) {
            if (var1.getClickCount() == 2 && var1.isPrimaryButtonDown()) {
               var4.getTableViewSkin().resizeColumnToFitContent(var3, -1);
            } else {
               Rectangle var5 = (Rectangle)var1.getSource();
               double var6 = var4.getTableHeaderRow().sceneToLocal(var5.localToScene(var5.getBoundsInLocal())).getMinX() + 2.0;
               var4.dragAnchorX = var1.getSceneX();
               var4.columnResizingStarted(var6);
            }

            var1.consume();
         }
      }
   };
   private static final EventHandler rectMouseDragged = new EventHandler() {
      public void handle(MouseEvent var1) {
         Rectangle var2 = (Rectangle)var1.getSource();
         TableColumnBase var3 = (TableColumnBase)var2.getProperties().get("TableColumn");
         NestedTableColumnHeader var4 = (NestedTableColumnHeader)var2.getProperties().get("TableColumnHeader");
         if (var4.isColumnResizingEnabled()) {
            var4.columnResizing(var3, var1);
            var1.consume();
         }
      }
   };
   private static final EventHandler rectMouseReleased = new EventHandler() {
      public void handle(MouseEvent var1) {
         Rectangle var2 = (Rectangle)var1.getSource();
         TableColumnBase var3 = (TableColumnBase)var2.getProperties().get("TableColumn");
         NestedTableColumnHeader var4 = (NestedTableColumnHeader)var2.getProperties().get("TableColumnHeader");
         if (var4.isColumnResizingEnabled()) {
            var4.columnResizingComplete(var3, var1);
            var1.consume();
         }
      }
   };
   private static final EventHandler rectCursorChangeListener = new EventHandler() {
      public void handle(MouseEvent var1) {
         Rectangle var2 = (Rectangle)var1.getSource();
         TableColumnBase var3 = (TableColumnBase)var2.getProperties().get("TableColumn");
         NestedTableColumnHeader var4 = (NestedTableColumnHeader)var2.getProperties().get("TableColumnHeader");
         if (var4.getCursor() == null) {
            var2.setCursor(var4.isColumnResizingEnabled() && var2.isHover() && var3.isResizable() ? Cursor.H_RESIZE : null);
         }

      }
   };

   public NestedTableColumnHeader(TableViewSkinBase var1, TableColumnBase var2) {
      super(var1, var2);
      this.weakColumnsListener = new WeakListChangeListener(this.columnsListener);
      this.getStyleClass().setAll((Object[])("nested-column-header"));
      this.setFocusTraversable(false);
      this.label = new TableColumnHeader(var1, this.getTableColumn());
      this.label.setTableHeaderRow(this.getTableHeaderRow());
      this.label.setParentHeader(this.getParentHeader());
      this.label.setNestedColumnHeader(this);
      if (this.getTableColumn() != null) {
         this.changeListenerHandler.registerChangeListener(this.getTableColumn().textProperty(), "TABLE_COLUMN_TEXT");
      }

      this.changeListenerHandler.registerChangeListener(var1.columnResizePolicyProperty(), "TABLE_VIEW_COLUMN_RESIZE_POLICY");
   }

   protected void handlePropertyChanged(String var1) {
      super.handlePropertyChanged(var1);
      if ("TABLE_VIEW_COLUMN_RESIZE_POLICY".equals(var1)) {
         this.updateContent();
      } else if ("TABLE_COLUMN_TEXT".equals(var1)) {
         this.label.setVisible(this.getTableColumn().getText() != null && !this.getTableColumn().getText().isEmpty());
      }

   }

   public void setTableHeaderRow(TableHeaderRow var1) {
      super.setTableHeaderRow(var1);
      this.label.setTableHeaderRow(var1);
      Iterator var2 = this.getColumnHeaders().iterator();

      while(var2.hasNext()) {
         TableColumnHeader var3 = (TableColumnHeader)var2.next();
         var3.setTableHeaderRow(var1);
      }

   }

   public void setParentHeader(NestedTableColumnHeader var1) {
      super.setParentHeader(var1);
      this.label.setParentHeader(var1);
   }

   ObservableList getColumns() {
      return this.columns;
   }

   void setColumns(ObservableList var1) {
      if (this.columns != null) {
         this.columns.removeListener(this.weakColumnsListener);
      }

      this.columns = var1;
      if (this.columns != null) {
         this.columns.addListener(this.weakColumnsListener);
      }

   }

   void updateTableColumnHeaders() {
      if (this.getTableColumn() == null && this.getTableViewSkin() != null) {
         this.setColumns(this.getTableViewSkin().getColumns());
      } else if (this.getTableColumn() != null) {
         this.setColumns(this.getTableColumn().getColumns());
      }

      TableColumnHeader var2;
      int var3;
      if (this.getColumns().isEmpty()) {
         for(int var1 = 0; var1 < this.getColumnHeaders().size(); ++var1) {
            var2 = (TableColumnHeader)this.getColumnHeaders().get(var1);
            var2.dispose();
         }

         NestedTableColumnHeader var8 = this.getParentHeader();
         if (var8 != null) {
            ObservableList var10 = var8.getColumnHeaders();
            var3 = var10.indexOf(this);
            if (var3 >= 0 && var3 < var10.size()) {
               var10.set(var3, this.createColumnHeader(this.getTableColumn()));
            }
         } else {
            this.getColumnHeaders().clear();
         }
      } else {
         ArrayList var9 = new ArrayList(this.getColumnHeaders());
         ArrayList var12 = new ArrayList();

         for(var3 = 0; var3 < this.getColumns().size(); ++var3) {
            TableColumnBase var4 = (TableColumnBase)this.getColumns().get(var3);
            if (var4 != null && var4.isVisible()) {
               boolean var5 = false;

               for(int var6 = 0; var6 < var9.size(); ++var6) {
                  TableColumnHeader var7 = (TableColumnHeader)var9.get(var6);
                  if (var4 == var7.getTableColumn()) {
                     var12.add(var7);
                     var5 = true;
                     break;
                  }
               }

               if (!var5) {
                  var12.add(this.createColumnHeader(var4));
               }
            }
         }

         this.getColumnHeaders().setAll((Collection)var12);
         var9.removeAll(var12);

         for(var3 = 0; var3 < var9.size(); ++var3) {
            ((TableColumnHeader)var9.get(var3)).dispose();
         }
      }

      this.updateContent();
      Iterator var11 = this.getColumnHeaders().iterator();

      while(var11.hasNext()) {
         var2 = (TableColumnHeader)var11.next();
         var2.applyCss();
      }

   }

   void dispose() {
      super.dispose();
      if (this.label != null) {
         this.label.dispose();
      }

      if (this.getColumns() != null) {
         this.getColumns().removeListener(this.weakColumnsListener);
      }

      for(int var1 = 0; var1 < this.getColumnHeaders().size(); ++var1) {
         TableColumnHeader var2 = (TableColumnHeader)this.getColumnHeaders().get(var1);
         var2.dispose();
      }

      Iterator var3 = this.dragRects.values().iterator();

      while(var3.hasNext()) {
         Rectangle var4 = (Rectangle)var3.next();
         if (var4 != null) {
            var4.visibleProperty().unbind();
         }
      }

      this.dragRects.clear();
      this.getChildren().clear();
      this.changeListenerHandler.dispose();
   }

   public ObservableList getColumnHeaders() {
      if (this.columnHeaders == null) {
         this.columnHeaders = FXCollections.observableArrayList();
      }

      return this.columnHeaders;
   }

   protected void layoutChildren() {
      double var1 = this.getWidth() - this.snappedLeftInset() - this.snappedRightInset();
      double var3 = this.getHeight() - this.snappedTopInset() - this.snappedBottomInset();
      int var5 = (int)this.label.prefHeight(-1.0);
      if (this.label.isVisible()) {
         this.label.resize(var1, (double)var5);
         this.label.relocate(this.snappedLeftInset(), this.snappedTopInset());
      }

      double var6 = this.snappedLeftInset();
      boolean var8 = false;
      int var9 = 0;

      for(int var10 = this.getColumnHeaders().size(); var9 < var10; ++var9) {
         TableColumnHeader var11 = (TableColumnHeader)this.getColumnHeaders().get(var9);
         if (var11.isVisible()) {
            double var12 = this.snapSize(var11.prefWidth(-1.0));
            var11.resize(var12, this.snapSize(var3 - (double)var5));
            var11.relocate(var6, (double)var5 + this.snappedTopInset());
            var6 += var12;
            Rectangle var14 = (Rectangle)this.dragRects.get(var11.getTableColumn());
            if (var14 != null) {
               var14.setHeight(var11.getDragRectHeight());
               var14.relocate(var6 - 2.0, this.snappedTopInset() + (double)var5);
            }
         }
      }

   }

   double getDragRectHeight() {
      return this.label.prefHeight(-1.0);
   }

   protected double computePrefWidth(double var1) {
      this.checkState();
      double var3 = 0.0;
      if (this.getColumns() != null) {
         Iterator var5 = this.getColumnHeaders().iterator();

         while(var5.hasNext()) {
            TableColumnHeader var6 = (TableColumnHeader)var5.next();
            if (var6.isVisible()) {
               var3 += this.snapSize(var6.computePrefWidth(var1));
            }
         }
      }

      return var3;
   }

   protected double computePrefHeight(double var1) {
      this.checkState();
      double var3 = 0.0;
      TableColumnHeader var6;
      if (this.getColumnHeaders() != null) {
         for(Iterator var5 = this.getColumnHeaders().iterator(); var5.hasNext(); var3 = Math.max(var3, var6.prefHeight(-1.0))) {
            var6 = (TableColumnHeader)var5.next();
         }
      }

      return var3 + this.label.prefHeight(-1.0) + this.snappedTopInset() + this.snappedBottomInset();
   }

   protected TableColumnHeader createTableColumnHeader(TableColumnBase var1) {
      return (TableColumnHeader)(var1.getColumns().isEmpty() ? new TableColumnHeader(this.getTableViewSkin(), var1) : new NestedTableColumnHeader(this.getTableViewSkin(), var1));
   }

   protected void setHeadersNeedUpdate() {
      this.updateColumns = true;

      for(int var1 = 0; var1 < this.getColumnHeaders().size(); ++var1) {
         TableColumnHeader var2 = (TableColumnHeader)this.getColumnHeaders().get(var1);
         if (var2 instanceof NestedTableColumnHeader) {
            ((NestedTableColumnHeader)var2).setHeadersNeedUpdate();
         }
      }

      this.requestLayout();
   }

   private void updateContent() {
      ArrayList var1 = new ArrayList();
      var1.add(this.label);
      var1.addAll(this.getColumnHeaders());
      if (this.isColumnResizingEnabled()) {
         this.rebuildDragRects();
         var1.addAll(this.dragRects.values());
      }

      this.getChildren().setAll((Collection)var1);
   }

   private void rebuildDragRects() {
      if (this.isColumnResizingEnabled()) {
         this.getChildren().removeAll(this.dragRects.values());
         Iterator var1 = this.dragRects.values().iterator();

         while(var1.hasNext()) {
            Rectangle var2 = (Rectangle)var1.next();
            var2.visibleProperty().unbind();
         }

         this.dragRects.clear();
         ObservableList var8 = this.getColumns();
         if (var8 != null) {
            TableViewSkinBase var9 = this.getTableViewSkin();
            Callback var3 = (Callback)var9.columnResizePolicyProperty().get();
            boolean var4 = var9 instanceof TableViewSkin ? TableView.CONSTRAINED_RESIZE_POLICY.equals(var3) : (var9 instanceof TreeTableViewSkin ? TreeTableView.CONSTRAINED_RESIZE_POLICY.equals(var3) : false);
            if (!var4 || var9.getVisibleLeafColumns().size() != 1) {
               for(int var5 = 0; var5 < var8.size() && (!var4 || var5 != this.getColumns().size() - 1); ++var5) {
                  TableColumnBase var6 = (TableColumnBase)var8.get(var5);
                  Rectangle var7 = new Rectangle();
                  var7.getProperties().put("TableColumn", var6);
                  var7.getProperties().put("TableColumnHeader", this);
                  var7.setWidth(4.0);
                  var7.setHeight(this.getHeight() - this.label.getHeight());
                  var7.setFill(Color.TRANSPARENT);
                  var7.visibleProperty().bind(var6.visibleProperty());
                  var7.setOnMousePressed(rectMousePressed);
                  var7.setOnMouseDragged(rectMouseDragged);
                  var7.setOnMouseReleased(rectMouseReleased);
                  var7.setOnMouseEntered(rectCursorChangeListener);
                  var7.setOnMouseExited(rectCursorChangeListener);
                  this.dragRects.put(var6, var7);
               }

            }
         }
      }
   }

   private void checkState() {
      if (this.updateColumns) {
         this.updateTableColumnHeaders();
         this.updateColumns = false;
      }

   }

   private TableColumnHeader createColumnHeader(TableColumnBase var1) {
      TableColumnHeader var2 = this.createTableColumnHeader(var1);
      var2.setTableHeaderRow(this.getTableHeaderRow());
      var2.setParentHeader(this);
      return var2;
   }

   private boolean isColumnResizingEnabled() {
      return true;
   }

   private void columnResizingStarted(double var1) {
      this.setCursor(Cursor.H_RESIZE);
      this.columnReorderLine.setLayoutX(var1);
   }

   private void columnResizing(TableColumnBase var1, MouseEvent var2) {
      double var3 = var2.getSceneX() - this.dragAnchorX;
      if (this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
         var3 = -var3;
      }

      double var5 = var3 - this.lastX;
      boolean var7 = this.getTableViewSkin().resizeColumn(var1, var5);
      if (var7) {
         this.lastX = var3;
      }

   }

   private void columnResizingComplete(TableColumnBase var1, MouseEvent var2) {
      this.setCursor((Cursor)null);
      this.columnReorderLine.setTranslateX(0.0);
      this.columnReorderLine.setLayoutX(0.0);
      this.lastX = 0.0;
   }
}
