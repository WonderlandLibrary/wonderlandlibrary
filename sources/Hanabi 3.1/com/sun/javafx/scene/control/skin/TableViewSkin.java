package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TableViewBehavior;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.util.Callback;

public class TableViewSkin extends TableViewSkinBase {
   private final TableView tableView;

   public TableViewSkin(TableView var1) {
      super(var1, new TableViewBehavior(var1));
      this.tableView = var1;
      this.flow.setFixedCellSize(var1.getFixedCellSize());
      super.init(var1);
      EventHandler var2 = (var1x) -> {
         if (var1.getEditingCell() != null) {
            var1.edit(-1, (TableColumn)null);
         }

         if (var1.isFocusTraversable()) {
            var1.requestFocus();
         }

      };
      this.flow.getVbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      this.flow.getHbar().addEventFilter(MouseEvent.MOUSE_PRESSED, var2);
      TableViewBehavior var3 = (TableViewBehavior)this.getBehavior();
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
      this.registerChangeListener(var1.fixedCellSizeProperty(), "FIXED_CELL_SIZE");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("FIXED_CELL_SIZE".equals(var1)) {
         this.flow.setFixedCellSize(((TableView)this.getSkinnable()).getFixedCellSize());
      }

   }

   protected ObservableList getVisibleLeafColumns() {
      return this.tableView.getVisibleLeafColumns();
   }

   protected int getVisibleLeafIndex(TableColumn var1) {
      return this.tableView.getVisibleLeafIndex(var1);
   }

   protected TableColumn getVisibleLeafColumn(int var1) {
      return this.tableView.getVisibleLeafColumn(var1);
   }

   protected TableView.TableViewFocusModel getFocusModel() {
      return this.tableView.getFocusModel();
   }

   protected TablePosition getFocusedCell() {
      return this.tableView.getFocusModel().getFocusedCell();
   }

   protected TableSelectionModel getSelectionModel() {
      return this.tableView.getSelectionModel();
   }

   protected ObjectProperty rowFactoryProperty() {
      return this.tableView.rowFactoryProperty();
   }

   protected ObjectProperty placeholderProperty() {
      return this.tableView.placeholderProperty();
   }

   protected ObjectProperty itemsProperty() {
      return this.tableView.itemsProperty();
   }

   protected ObservableList getColumns() {
      return this.tableView.getColumns();
   }

   protected BooleanProperty tableMenuButtonVisibleProperty() {
      return this.tableView.tableMenuButtonVisibleProperty();
   }

   protected ObjectProperty columnResizePolicyProperty() {
      return (ObjectProperty)this.tableView.columnResizePolicyProperty();
   }

   protected ObservableList getSortOrder() {
      return this.tableView.getSortOrder();
   }

   protected boolean resizeColumn(TableColumn var1, double var2) {
      return this.tableView.resizeColumn(var1, var2);
   }

   protected void edit(int var1, TableColumn var2) {
      this.tableView.edit(var1, var2);
   }

   protected void resizeColumnToFitContent(TableColumn var1, int var2) {
      if (var1.isResizable()) {
         List var3 = (List)this.itemsProperty().get();
         if (var3 != null && !var3.isEmpty()) {
            Callback var4 = var1.getCellFactory();
            if (var4 != null) {
               TableCell var5 = (TableCell)var4.call(var1);
               if (var5 != null) {
                  var5.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
                  double var6 = 10.0;
                  Node var8 = var5.getSkin() == null ? null : var5.getSkin().getNode();
                  if (var8 instanceof Region) {
                     Region var9 = (Region)var8;
                     var6 = var9.snappedLeftInset() + var9.snappedRightInset();
                  }

                  int var21 = var2 == -1 ? var3.size() : Math.min(var3.size(), var2);
                  double var10 = 0.0;

                  for(int var12 = 0; var12 < var21; ++var12) {
                     var5.updateTableColumn(var1);
                     var5.updateTableView(this.tableView);
                     var5.updateIndex(var12);
                     if (var5.getText() != null && !var5.getText().isEmpty() || var5.getGraphic() != null) {
                        this.getChildren().add(var5);
                        var5.applyCss();
                        var10 = Math.max(var10, var5.prefWidth(-1.0));
                        this.getChildren().remove(var5);
                     }
                  }

                  var5.updateIndex(-1);
                  TableColumnHeader var22 = this.getTableHeaderRow().getColumnHeaderFor(var1);
                  double var13 = Utils.computeTextWidth(var22.label.getFont(), var1.getText(), -1.0);
                  Node var15 = var22.label.getGraphic();
                  double var16 = var15 == null ? 0.0 : var15.prefWidth(-1.0) + var22.label.getGraphicTextGap();
                  double var18 = var13 + var16 + 10.0 + var22.snappedLeftInset() + var22.snappedRightInset();
                  var10 = Math.max(var10, var18);
                  var10 += var6;
                  if (this.tableView.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY && this.tableView.getWidth() > 0.0) {
                     if (var10 > var1.getMaxWidth()) {
                        var10 = var1.getMaxWidth();
                     }

                     int var20 = var1.getColumns().size();
                     if (var20 > 0) {
                        this.resizeColumnToFitContent((TableColumn)var1.getColumns().get(var20 - 1), var2);
                        return;
                     }

                     this.resizeColumn(var1, (double)Math.round(var10 - var1.getWidth()));
                  } else {
                     var1.impl_setWidth(var10);
                  }

               }
            }
         }
      }
   }

   public int getItemCount() {
      return this.tableView.getItems() == null ? 0 : this.tableView.getItems().size();
   }

   public TableRow createCell() {
      TableRow var1;
      if (this.tableView.getRowFactory() != null) {
         var1 = (TableRow)this.tableView.getRowFactory().call(this.tableView);
      } else {
         var1 = new TableRow();
      }

      var1.updateTableView(this.tableView);
      return var1;
   }

   protected void horizontalScroll() {
      super.horizontalScroll();
      if (((TableView)this.getSkinnable()).getFixedCellSize() > 0.0) {
         this.flow.requestCellLayout();
      }

   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case SELECTED_ITEMS:
            ArrayList var3 = new ArrayList();
            TableView.TableViewSelectionModel var4 = ((TableView)this.getSkinnable()).getSelectionModel();
            Iterator var5 = var4.getSelectedCells().iterator();

            while(var5.hasNext()) {
               TablePosition var6 = (TablePosition)var5.next();
               TableRow var7 = (TableRow)this.flow.getPrivateCell(var6.getRow());
               if (var7 != null) {
                  var3.add(var7);
               }
            }

            return FXCollections.observableArrayList((Collection)var3);
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   protected void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case SHOW_ITEM:
            Node var8 = (Node)var2[0];
            if (var8 instanceof TableCell) {
               TableCell var9 = (TableCell)var8;
               this.flow.show(var9.getIndex());
            }
            break;
         case SET_SELECTED_ITEMS:
            ObservableList var3 = (ObservableList)var2[0];
            if (var3 != null) {
               TableView.TableViewSelectionModel var4 = ((TableView)this.getSkinnable()).getSelectionModel();
               if (var4 != null) {
                  var4.clearSelection();
                  Iterator var5 = var3.iterator();

                  while(var5.hasNext()) {
                     Node var6 = (Node)var5.next();
                     if (var6 instanceof TableCell) {
                        TableCell var7 = (TableCell)var6;
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
}
