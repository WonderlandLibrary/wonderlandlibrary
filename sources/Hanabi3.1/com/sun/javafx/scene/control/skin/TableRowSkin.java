package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TableRowBehavior;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

public class TableRowSkin extends TableRowSkinBase {
   private TableView tableView;
   private TableViewSkin tableViewSkin;

   public TableRowSkin(TableRow var1) {
      super(var1, new TableRowBehavior(var1));
      this.tableView = var1.getTableView();
      this.updateTableViewSkin();
      super.init(var1);
      this.registerChangeListener(var1.tableViewProperty(), "TABLE_VIEW");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("TABLE_VIEW".equals(var1)) {
         this.updateTableViewSkin();
         int var2 = 0;

         for(int var3 = this.cells.size(); var2 < var3; ++var2) {
            Node var4 = (Node)this.cells.get(var2);
            if (var4 instanceof TableCell) {
               ((TableCell)var4).updateTableView(((TableRow)this.getSkinnable()).getTableView());
            }
         }

         this.tableView = ((TableRow)this.getSkinnable()).getTableView();
      }

   }

   protected TableCell getCell(TableColumnBase var1) {
      TableColumn var2 = (TableColumn)var1;
      TableCell var3 = (TableCell)var2.getCellFactory().call(var2);
      var3.updateTableColumn(var2);
      var3.updateTableView(var2.getTableView());
      var3.updateTableRow((TableRow)this.getSkinnable());
      return var3;
   }

   protected ObservableList getVisibleLeafColumns() {
      return this.tableView.getVisibleLeafColumns();
   }

   protected void updateCell(TableCell var1, TableRow var2) {
      var1.updateTableRow(var2);
   }

   protected DoubleProperty fixedCellSizeProperty() {
      return this.tableView.fixedCellSizeProperty();
   }

   protected boolean isColumnPartiallyOrFullyVisible(TableColumnBase var1) {
      return this.tableViewSkin == null ? false : this.tableViewSkin.isColumnPartiallyOrFullyVisible((TableColumn)var1);
   }

   protected TableColumn getTableColumnBase(TableCell var1) {
      return var1.getTableColumn();
   }

   protected ObjectProperty graphicProperty() {
      return null;
   }

   protected Control getVirtualFlowOwner() {
      return ((TableRow)this.getSkinnable()).getTableView();
   }

   private void updateTableViewSkin() {
      TableView var1 = ((TableRow)this.getSkinnable()).getTableView();
      if (var1.getSkin() instanceof TableViewSkin) {
         this.tableViewSkin = (TableViewSkin)var1.getSkin();
      }

   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case SELECTED_ITEMS:
            ArrayList var9 = new ArrayList();
            int var11 = ((TableRow)this.getSkinnable()).getIndex();
            Iterator var13 = this.tableView.getSelectionModel().getSelectedCells().iterator();
            if (var13.hasNext()) {
               TablePosition var6 = (TablePosition)var13.next();
               if (var6.getRow() == var11) {
                  TableColumn var7 = var6.getTableColumn();
                  if (var7 == null) {
                     var7 = this.tableView.getVisibleLeafColumn(0);
                  }

                  TableCell var8 = (TableCell)((Reference)this.cellsMap.get(var7)).get();
                  if (var8 != null) {
                     var9.add(var8);
                  }
               }

               return FXCollections.observableArrayList((Collection)var9);
            }
         case CELL_AT_ROW_COLUMN:
            int var10 = (Integer)var2[1];
            TableColumn var12 = this.tableView.getVisibleLeafColumn(var10);
            if (this.cellsMap.containsKey(var12)) {
               return ((Reference)this.cellsMap.get(var12)).get();
            }

            return null;
         case FOCUS_ITEM:
            TableView.TableViewFocusModel var3 = this.tableView.getFocusModel();
            TablePosition var4 = var3.getFocusedCell();
            TableColumn var5 = var4.getTableColumn();
            if (var5 == null) {
               var5 = this.tableView.getVisibleLeafColumn(0);
            }

            if (this.cellsMap.containsKey(var5)) {
               return ((Reference)this.cellsMap.get(var5)).get();
            }

            return null;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
