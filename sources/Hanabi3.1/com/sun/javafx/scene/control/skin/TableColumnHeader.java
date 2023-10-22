package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.TableColumnSortTypeWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class TableColumnHeader extends Region {
   static final double DEFAULT_COLUMN_WIDTH = 80.0;
   private boolean autoSizeComplete = false;
   private double dragOffset;
   private final TableViewSkinBase skin;
   private NestedTableColumnHeader nestedColumnHeader;
   private final TableColumnBase column;
   private TableHeaderRow tableHeaderRow;
   private NestedTableColumnHeader parentHeader;
   Label label;
   int sortPos = -1;
   private Region arrow;
   private Label sortOrderLabel;
   private HBox sortOrderDots;
   private Node sortArrow;
   private boolean isSortColumn;
   private boolean isSizeDirty = false;
   boolean isLastVisibleColumn = false;
   int columnIndex = -1;
   private int newColumnPos;
   protected final Region columnReorderLine;
   protected final MultiplePropertyChangeListenerHandler changeListenerHandler;
   private ListChangeListener sortOrderListener = (var1x) -> {
      this.updateSortPosition();
   };
   private ListChangeListener visibleLeafColumnsListener = (var1x) -> {
      this.updateColumnIndex();
      this.updateSortPosition();
   };
   private ListChangeListener styleClassListener = (var1x) -> {
      this.updateStyleClass();
   };
   private WeakListChangeListener weakSortOrderListener;
   private final WeakListChangeListener weakVisibleLeafColumnsListener;
   private final WeakListChangeListener weakStyleClassListener;
   private static final EventHandler mousePressedHandler = (var0) -> {
      TableColumnHeader var1 = (TableColumnHeader)var0.getSource();
      var1.getTableViewSkin().getSkinnable().requestFocus();
      if (var0.isPrimaryButtonDown() && var1.isColumnReorderingEnabled()) {
         var1.columnReorderingStarted(var0.getX());
      }

      var0.consume();
   };
   private static final EventHandler mouseDraggedHandler = (var0) -> {
      TableColumnHeader var1 = (TableColumnHeader)var0.getSource();
      if (var0.isPrimaryButtonDown() && var1.isColumnReorderingEnabled()) {
         var1.columnReordering(var0.getSceneX(), var0.getSceneY());
      }

      var0.consume();
   };
   private static final EventHandler mouseReleasedHandler = (var0) -> {
      if (!var0.isPopupTrigger()) {
         TableColumnHeader var1 = (TableColumnHeader)var0.getSource();
         TableColumnBase var2 = var1.getTableColumn();
         ContextMenu var3 = var2.getContextMenu();
         if (var3 == null || !var3.isShowing()) {
            if (var1.getTableHeaderRow().isReordering() && var1.isColumnReorderingEnabled()) {
               var1.columnReorderingComplete();
            } else if (var0.isStillSincePress()) {
               var1.sortColumn(var0.isShiftDown());
            }

            var0.consume();
         }
      }
   };
   private static final EventHandler contextMenuRequestedHandler = (var0) -> {
      TableColumnHeader var1 = (TableColumnHeader)var0.getSource();
      TableColumnBase var2 = var1.getTableColumn();
      ContextMenu var3 = var2.getContextMenu();
      if (var3 != null) {
         var3.show(var1, var0.getScreenX(), var0.getScreenY());
         var0.consume();
      }

   };
   private DoubleProperty size;
   private static final PseudoClass PSEUDO_CLASS_LAST_VISIBLE = PseudoClass.getPseudoClass("last-visible");

   public TableColumnHeader(TableViewSkinBase var1, TableColumnBase var2) {
      this.weakSortOrderListener = new WeakListChangeListener(this.sortOrderListener);
      this.weakVisibleLeafColumnsListener = new WeakListChangeListener(this.visibleLeafColumnsListener);
      this.weakStyleClassListener = new WeakListChangeListener(this.styleClassListener);
      this.skin = var1;
      this.column = var2;
      this.columnReorderLine = var1.getColumnReorderLine();
      this.setFocusTraversable(false);
      this.updateColumnIndex();
      this.initUI();
      this.changeListenerHandler = new MultiplePropertyChangeListenerHandler((var1x) -> {
         this.handlePropertyChanged(var1x);
         return null;
      });
      this.changeListenerHandler.registerChangeListener(this.sceneProperty(), "SCENE");
      if (this.column != null && var1 != null) {
         this.updateSortPosition();
         var1.getSortOrder().addListener(this.weakSortOrderListener);
         var1.getVisibleLeafColumns().addListener(this.weakVisibleLeafColumnsListener);
      }

      if (this.column != null) {
         this.changeListenerHandler.registerChangeListener(this.column.idProperty(), "TABLE_COLUMN_ID");
         this.changeListenerHandler.registerChangeListener(this.column.styleProperty(), "TABLE_COLUMN_STYLE");
         this.changeListenerHandler.registerChangeListener(this.column.widthProperty(), "TABLE_COLUMN_WIDTH");
         this.changeListenerHandler.registerChangeListener(this.column.visibleProperty(), "TABLE_COLUMN_VISIBLE");
         this.changeListenerHandler.registerChangeListener(this.column.sortNodeProperty(), "TABLE_COLUMN_SORT_NODE");
         this.changeListenerHandler.registerChangeListener(this.column.sortableProperty(), "TABLE_COLUMN_SORTABLE");
         this.changeListenerHandler.registerChangeListener(this.column.textProperty(), "TABLE_COLUMN_TEXT");
         this.changeListenerHandler.registerChangeListener(this.column.graphicProperty(), "TABLE_COLUMN_GRAPHIC");
         this.column.getStyleClass().addListener(this.weakStyleClassListener);
         this.setId(this.column.getId());
         this.setStyle(this.column.getStyle());
         this.updateStyleClass();
         this.setAccessibleRole(AccessibleRole.TABLE_COLUMN);
      }

   }

   private double getSize() {
      return this.size == null ? 20.0 : this.size.doubleValue();
   }

   private DoubleProperty sizeProperty() {
      if (this.size == null) {
         this.size = new StyleableDoubleProperty(20.0) {
            protected void invalidated() {
               double var1 = this.get();
               if (var1 <= 0.0) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(20.0);
                  throw new IllegalArgumentException("Size cannot be 0 or negative");
               }
            }

            public Object getBean() {
               return TableColumnHeader.this;
            }

            public String getName() {
               return "size";
            }

            public CssMetaData getCssMetaData() {
               return TableColumnHeader.StyleableProperties.SIZE;
            }
         };
      }

      return this.size;
   }

   protected void handlePropertyChanged(String var1) {
      if ("SCENE".equals(var1)) {
         this.updateScene();
      } else if ("TABLE_COLUMN_VISIBLE".equals(var1)) {
         this.setVisible(this.getTableColumn().isVisible());
      } else if ("TABLE_COLUMN_WIDTH".equals(var1)) {
         this.isSizeDirty = true;
         this.requestLayout();
      } else if ("TABLE_COLUMN_ID".equals(var1)) {
         this.setId(this.column.getId());
      } else if ("TABLE_COLUMN_STYLE".equals(var1)) {
         this.setStyle(this.column.getStyle());
      } else if ("TABLE_COLUMN_SORT_TYPE".equals(var1)) {
         this.updateSortGrid();
         if (this.arrow != null) {
            this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0 : 0.0);
         }
      } else if ("TABLE_COLUMN_SORT_NODE".equals(var1)) {
         this.updateSortGrid();
      } else if ("TABLE_COLUMN_SORTABLE".equals(var1)) {
         if (this.skin.getSortOrder().contains(this.getTableColumn())) {
            NestedTableColumnHeader var2 = this.getTableHeaderRow().getRootHeader();
            this.updateAllHeaders(var2);
         }
      } else if ("TABLE_COLUMN_TEXT".equals(var1)) {
         this.label.setText(this.column.getText());
      } else if ("TABLE_COLUMN_GRAPHIC".equals(var1)) {
         this.label.setGraphic(this.column.getGraphic());
      }

   }

   protected TableViewSkinBase getTableViewSkin() {
      return this.skin;
   }

   NestedTableColumnHeader getNestedColumnHeader() {
      return this.nestedColumnHeader;
   }

   void setNestedColumnHeader(NestedTableColumnHeader var1) {
      this.nestedColumnHeader = var1;
   }

   public TableColumnBase getTableColumn() {
      return this.column;
   }

   TableHeaderRow getTableHeaderRow() {
      return this.tableHeaderRow;
   }

   void setTableHeaderRow(TableHeaderRow var1) {
      this.tableHeaderRow = var1;
   }

   NestedTableColumnHeader getParentHeader() {
      return this.parentHeader;
   }

   void setParentHeader(NestedTableColumnHeader var1) {
      this.parentHeader = var1;
   }

   protected void layoutChildren() {
      if (this.isSizeDirty) {
         this.resize(this.getTableColumn().getWidth(), this.getHeight());
         this.isSizeDirty = false;
      }

      double var1 = 0.0;
      double var3 = this.snapSize(this.getWidth()) - (this.snappedLeftInset() + this.snappedRightInset());
      double var5 = this.getHeight() - (this.snappedTopInset() + this.snappedBottomInset());
      if (this.arrow != null) {
         this.arrow.setMaxSize(this.arrow.prefWidth(-1.0), this.arrow.prefHeight(-1.0));
      }

      if (this.sortArrow != null && this.sortArrow.isVisible()) {
         var1 = this.sortArrow.prefWidth(-1.0);
         double var7 = var3 - var1;
         this.sortArrow.resize(var1, this.sortArrow.prefHeight(-1.0));
         this.positionInArea(this.sortArrow, var7, this.snappedTopInset(), var1, var5, 0.0, HPos.CENTER, VPos.CENTER);
      }

      if (this.label != null) {
         double var9 = var3 - var1;
         this.label.resizeRelocate(this.snappedLeftInset(), 0.0, var9, this.getHeight());
      }

   }

   protected double computePrefWidth(double var1) {
      if (this.getNestedColumnHeader() != null) {
         double var3 = this.getNestedColumnHeader().prefWidth(var1);
         if (this.column != null) {
            this.column.impl_setWidth(var3);
         }

         return var3;
      } else {
         return this.column != null && this.column.isVisible() ? this.column.getWidth() : 0.0;
      }
   }

   protected double computeMinHeight(double var1) {
      return this.label == null ? 0.0 : this.label.minHeight(var1);
   }

   protected double computePrefHeight(double var1) {
      return this.getTableColumn() == null ? 0.0 : Math.max(this.getSize(), this.label.prefHeight(-1.0));
   }

   private void updateAllHeaders(TableColumnHeader var1) {
      if (var1 instanceof NestedTableColumnHeader) {
         ObservableList var2 = ((NestedTableColumnHeader)var1).getColumnHeaders();

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            this.updateAllHeaders((TableColumnHeader)var2.get(var3));
         }
      } else {
         var1.updateSortPosition();
      }

   }

   private void updateStyleClass() {
      this.getStyleClass().setAll((Object[])("column-header"));
      this.getStyleClass().addAll(this.column.getStyleClass());
   }

   private void updateScene() {
      if (!this.autoSizeComplete) {
         if (this.getTableColumn() == null || this.getTableColumn().getWidth() != 80.0 || this.getScene() == null) {
            return;
         }

         this.doColumnAutoSize(this.getTableColumn(), 30);
         this.autoSizeComplete = true;
      }

   }

   void dispose() {
      TableViewSkinBase var1 = this.getTableViewSkin();
      if (var1 != null) {
         var1.getVisibleLeafColumns().removeListener(this.weakVisibleLeafColumnsListener);
         var1.getSortOrder().removeListener(this.weakSortOrderListener);
      }

      this.changeListenerHandler.dispose();
   }

   private boolean isSortingEnabled() {
      return true;
   }

   private boolean isColumnReorderingEnabled() {
      return !BehaviorSkinBase.IS_TOUCH_SUPPORTED && this.getTableViewSkin().getVisibleLeafColumns().size() > 1;
   }

   private void initUI() {
      if (this.column != null) {
         this.setOnMousePressed(mousePressedHandler);
         this.setOnMouseDragged(mouseDraggedHandler);
         this.setOnDragDetected((var0) -> {
            var0.consume();
         });
         this.setOnContextMenuRequested(contextMenuRequestedHandler);
         this.setOnMouseReleased(mouseReleasedHandler);
         this.label = new Label();
         this.label.setText(this.column.getText());
         this.label.setGraphic(this.column.getGraphic());
         this.label.setVisible(this.column.isVisible());
         if (this.isSortingEnabled()) {
            this.updateSortGrid();
         }

      }
   }

   private void doColumnAutoSize(TableColumnBase var1, int var2) {
      double var3 = var1.getPrefWidth();
      if (var3 == 80.0) {
         this.getTableViewSkin().resizeColumnToFitContent(var1, var2);
      }

   }

   private void updateSortPosition() {
      this.sortPos = !this.column.isSortable() ? -1 : this.getSortPosition();
      this.updateSortGrid();
   }

   private void updateSortGrid() {
      if (!(this instanceof NestedTableColumnHeader)) {
         this.getChildren().clear();
         this.getChildren().add(this.label);
         if (this.isSortingEnabled()) {
            this.isSortColumn = this.sortPos != -1;
            if (!this.isSortColumn) {
               if (this.sortArrow != null) {
                  this.sortArrow.setVisible(false);
               }

            } else {
               int var1 = this.skin.getVisibleLeafIndex(this.getTableColumn());
               if (var1 != -1) {
                  int var2 = this.getVisibleSortOrderColumnCount();
                  boolean var3 = this.sortPos <= 3 && var2 > 1;
                  Object var4 = null;
                  if (this.getTableColumn().getSortNode() != null) {
                     var4 = this.getTableColumn().getSortNode();
                     this.getChildren().add(var4);
                  } else {
                     GridPane var5 = new GridPane();
                     var4 = var5;
                     var5.setPadding(new Insets(0.0, 3.0, 0.0, 0.0));
                     this.getChildren().add(var5);
                     if (this.arrow == null) {
                        this.arrow = new Region();
                        this.arrow.getStyleClass().setAll((Object[])("arrow"));
                        this.arrow.setVisible(true);
                        this.arrow.setRotate(TableColumnSortTypeWrapper.isAscending(this.column) ? 180.0 : 0.0);
                        this.changeListenerHandler.registerChangeListener(TableColumnSortTypeWrapper.getSortTypeProperty(this.column), "TABLE_COLUMN_SORT_TYPE");
                     }

                     this.arrow.setVisible(this.isSortColumn);
                     if (this.sortPos > 2) {
                        if (this.sortOrderLabel == null) {
                           this.sortOrderLabel = new Label();
                           this.sortOrderLabel.getStyleClass().add("sort-order");
                        }

                        this.sortOrderLabel.setText("" + (this.sortPos + 1));
                        this.sortOrderLabel.setVisible(var2 > 1);
                        var5.add(this.arrow, 1, 1);
                        GridPane.setHgrow(this.arrow, Priority.NEVER);
                        GridPane.setVgrow(this.arrow, Priority.NEVER);
                        var5.add(this.sortOrderLabel, 2, 1);
                     } else if (var3) {
                        if (this.sortOrderDots == null) {
                           this.sortOrderDots = new HBox(0.0);
                           this.sortOrderDots.getStyleClass().add("sort-order-dots-container");
                        }

                        boolean var6 = TableColumnSortTypeWrapper.isAscending(this.column);
                        int var7 = var6 ? 1 : 2;
                        int var8 = var6 ? 2 : 1;
                        var5.add(this.arrow, 1, var7);
                        GridPane.setHalignment(this.arrow, HPos.CENTER);
                        var5.add(this.sortOrderDots, 1, var8);
                        this.updateSortOrderDots(this.sortPos);
                     } else {
                        var5.add(this.arrow, 1, 1);
                        GridPane.setHgrow(this.arrow, Priority.NEVER);
                        GridPane.setVgrow(this.arrow, Priority.ALWAYS);
                     }
                  }

                  this.sortArrow = (Node)var4;
                  if (this.sortArrow != null) {
                     this.sortArrow.setVisible(this.isSortColumn);
                  }

                  this.requestLayout();
               }
            }
         }
      }
   }

   private void updateSortOrderDots(int var1) {
      double var2 = this.arrow.prefWidth(-1.0);
      this.sortOrderDots.getChildren().clear();

      for(int var4 = 0; var4 <= var1; ++var4) {
         Region var5 = new Region();
         var5.getStyleClass().add("sort-order-dot");
         String var6 = TableColumnSortTypeWrapper.getSortTypeName(this.column);
         if (var6 != null && !var6.isEmpty()) {
            var5.getStyleClass().add(var6.toLowerCase(Locale.ROOT));
         }

         this.sortOrderDots.getChildren().add(var5);
         if (var4 < var1) {
            Region var7 = new Region();
            double var8 = var1 == 1 ? 1.0 : 1.0;
            double var10 = var1 == 1 ? 1.0 : 0.0;
            var7.setPadding(new Insets(0.0, var8, 0.0, var10));
            this.sortOrderDots.getChildren().add(var7);
         }
      }

      this.sortOrderDots.setAlignment(Pos.TOP_CENTER);
      this.sortOrderDots.setMaxWidth(var2);
   }

   void moveColumn(TableColumnBase var1, int var2) {
      if (var1 != null && var2 >= 0) {
         ObservableList var3 = this.getColumns(var1);
         int var4 = var3.size();
         int var5 = var3.indexOf(var1);
         int var6 = var2;
         int var7 = var2;

         for(int var8 = 0; var8 <= var7 && var8 < var4; ++var8) {
            var6 += ((TableColumnBase)var3.get(var8)).isVisible() ? 0 : 1;
         }

         if (var6 >= var4) {
            var6 = var4 - 1;
         } else if (var6 < 0) {
            var6 = 0;
         }

         if (var6 != var5) {
            ArrayList var9 = new ArrayList(var3);
            var9.remove(var1);
            var9.add(var6, var1);
            var3.setAll((Collection)var9);
         }
      }
   }

   private ObservableList getColumns(TableColumnBase var1) {
      return var1.getParentColumn() == null ? this.getTableViewSkin().getColumns() : var1.getParentColumn().getColumns();
   }

   private int getIndex(TableColumnBase var1) {
      if (var1 == null) {
         return -1;
      } else {
         ObservableList var2 = this.getColumns(var1);
         int var3 = -1;

         for(int var4 = 0; var4 < var2.size(); ++var4) {
            TableColumnBase var5 = (TableColumnBase)var2.get(var4);
            if (var5.isVisible()) {
               ++var3;
               if (var1.equals(var5)) {
                  break;
               }
            }
         }

         return var3;
      }
   }

   private void updateColumnIndex() {
      TableViewSkinBase var1 = this.getTableViewSkin();
      TableColumnBase var2 = this.getTableColumn();
      this.columnIndex = var1 != null && var2 != null ? var1.getVisibleLeafIndex(var2) : -1;
      this.isLastVisibleColumn = this.getTableColumn() != null && this.columnIndex != -1 && this.columnIndex == this.getTableViewSkin().getVisibleLeafColumns().size() - 1;
      this.pseudoClassStateChanged(PSEUDO_CLASS_LAST_VISIBLE, this.isLastVisibleColumn);
   }

   private void sortColumn(boolean var1) {
      if (this.isSortingEnabled()) {
         if (this.column != null && this.column.getColumns().size() == 0 && this.column.getComparator() != null && this.column.isSortable()) {
            ObservableList var2 = this.getTableViewSkin().getSortOrder();
            if (var1) {
               if (!this.isSortColumn) {
                  TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
                  var2.add(this.column);
               } else if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                  TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
               } else {
                  int var3 = var2.indexOf(this.column);
                  if (var3 != -1) {
                     var2.remove(var3);
                  }
               }
            } else if (this.isSortColumn && var2.size() == 1) {
               if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                  TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
               } else {
                  var2.remove(this.column);
               }
            } else if (this.isSortColumn) {
               if (TableColumnSortTypeWrapper.isAscending(this.column)) {
                  TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.DESCENDING);
               } else if (TableColumnSortTypeWrapper.isDescending(this.column)) {
                  TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
               }

               ArrayList var4 = new ArrayList(var2);
               var4.remove(this.column);
               var4.add(0, this.column);
               var2.setAll((Object[])(this.column));
            } else {
               TableColumnSortTypeWrapper.setSortType(this.column, TableColumn.SortType.ASCENDING);
               var2.setAll((Object[])(this.column));
            }

         }
      }
   }

   private int getSortPosition() {
      if (this.column == null) {
         return -1;
      } else {
         List var1 = this.getVisibleSortOrderColumns();
         int var2 = 0;

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            TableColumnBase var4 = (TableColumnBase)var1.get(var3);
            if (this.column.equals(var4)) {
               return var2;
            }

            ++var2;
         }

         return -1;
      }
   }

   private List getVisibleSortOrderColumns() {
      ObservableList var1 = this.getTableViewSkin().getSortOrder();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         TableColumnBase var4 = (TableColumnBase)var1.get(var3);
         if (var4 != null && var4.isSortable() && var4.isVisible()) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private int getVisibleSortOrderColumnCount() {
      return this.getVisibleSortOrderColumns().size();
   }

   void columnReorderingStarted(double var1) {
      if (this.column.impl_isReorderable()) {
         this.dragOffset = var1;
         this.getTableHeaderRow().setReorderingColumn(this.column);
         this.getTableHeaderRow().setReorderingRegion(this);
      }
   }

   void columnReordering(double var1, double var3) {
      if (this.column.impl_isReorderable()) {
         this.getTableHeaderRow().setReordering(true);
         TableColumnHeader var5 = null;
         double var6 = this.getParentHeader().sceneToLocal(var1, var3).getX();
         double var8 = this.getTableViewSkin().getSkinnable().sceneToLocal(var1, var3).getX() - this.dragOffset;
         this.getTableHeaderRow().setDragHeaderX(var8);
         double var10 = 0.0;
         double var12 = 0.0;
         double var14 = 0.0;
         this.newColumnPos = 0;
         Iterator var16 = this.getParentHeader().getColumnHeaders().iterator();

         while(var16.hasNext()) {
            TableColumnHeader var17 = (TableColumnHeader)var16.next();
            if (var17.isVisible()) {
               double var18 = var17.prefWidth(-1.0);
               var14 += var18;
               var10 = var17.getBoundsInParent().getMinX();
               var12 = var10 + var18;
               if (var6 >= var10 && var6 < var12) {
                  var5 = var17;
                  break;
               }

               ++this.newColumnPos;
            }
         }

         if (var5 == null) {
            this.newColumnPos = var6 > var14 ? this.getParentHeader().getColumns().size() - 1 : 0;
         } else {
            double var22 = var10 + (var12 - var10) / 2.0;
            boolean var23 = var6 <= var22;
            int var19 = this.getIndex(this.column);
            this.newColumnPos += this.newColumnPos > var19 && var23 ? -1 : (this.newColumnPos < var19 && !var23 ? 1 : 0);
            double var20 = this.getTableHeaderRow().sceneToLocal(var5.localToScene(var5.getBoundsInLocal())).getMinX();
            var20 += var23 ? 0.0 : var5.getWidth();
            if (var20 >= -0.5 && var20 <= this.getTableViewSkin().getSkinnable().getWidth()) {
               this.columnReorderLine.setTranslateX(var20);
               this.columnReorderLine.setVisible(true);
            }

            this.getTableHeaderRow().setReordering(true);
         }
      }
   }

   void columnReorderingComplete() {
      if (this.column.impl_isReorderable()) {
         this.moveColumn(this.getTableColumn(), this.newColumnPos);
         this.columnReorderLine.setTranslateX(0.0);
         this.columnReorderLine.setLayoutX(0.0);
         this.newColumnPos = 0;
         this.getTableHeaderRow().setReordering(false);
         this.columnReorderLine.setVisible(false);
         this.getTableHeaderRow().setReorderingColumn((TableColumnBase)null);
         this.getTableHeaderRow().setReorderingRegion((TableColumnHeader)null);
         this.dragOffset = 0.0;
      }
   }

   double getDragRectHeight() {
      return this.getHeight();
   }

   public static List getClassCssMetaData() {
      return TableColumnHeader.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case INDEX:
            return this.getIndex(this.column);
         case TEXT:
            return this.column != null ? this.column.getText() : null;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData SIZE = new CssMetaData("-fx-size", SizeConverter.getInstance(), 20.0) {
         public boolean isSettable(TableColumnHeader var1) {
            return var1.size == null || !var1.size.isBound();
         }

         public StyleableProperty getStyleableProperty(TableColumnHeader var1) {
            return (StyleableProperty)var1.sizeProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         var0.add(SIZE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
