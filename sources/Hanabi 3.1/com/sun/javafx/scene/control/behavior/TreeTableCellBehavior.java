package com.sun.javafx.scene.control.behavior;

import java.util.Iterator;
import javafx.scene.Node;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;

public class TreeTableCellBehavior extends TableCellBehaviorBase {
   public TreeTableCellBehavior(TreeTableCell var1) {
      super(var1);
   }

   protected TreeTableView getCellContainer() {
      return ((TreeTableCell)this.getControl()).getTreeTableView();
   }

   protected TreeTableColumn getTableColumn() {
      return ((TreeTableCell)this.getControl()).getTableColumn();
   }

   protected int getItemCount() {
      return this.getCellContainer().getExpandedItemCount();
   }

   protected TreeTableView.TreeTableViewSelectionModel getSelectionModel() {
      return this.getCellContainer().getSelectionModel();
   }

   protected TreeTableView.TreeTableViewFocusModel getFocusModel() {
      return this.getCellContainer().getFocusModel();
   }

   protected TablePositionBase getFocusedCell() {
      return this.getCellContainer().getFocusModel().getFocusedCell();
   }

   protected boolean isTableRowSelected() {
      return ((TreeTableCell)this.getControl()).getTreeTableRow().isSelected();
   }

   protected int getVisibleLeafIndex(TableColumnBase var1) {
      return this.getCellContainer().getVisibleLeafIndex((TreeTableColumn)var1);
   }

   protected void focus(int var1, TableColumnBase var2) {
      this.getFocusModel().focus(var1, (TreeTableColumn)var2);
   }

   protected void edit(TreeTableCell var1) {
      if (var1 == null) {
         this.getCellContainer().edit(-1, (TreeTableColumn)null);
      } else {
         this.getCellContainer().edit(var1.getIndex(), var1.getTableColumn());
      }

   }

   protected boolean handleDisclosureNode(double var1, double var3) {
      TreeItem var5 = ((TreeTableCell)this.getControl()).getTreeTableRow().getTreeItem();
      TreeTableView var6 = ((TreeTableCell)this.getControl()).getTreeTableView();
      TreeTableColumn var7 = this.getTableColumn();
      TreeTableColumn var8 = var6.getTreeColumn() == null ? var6.getVisibleLeafColumn(0) : var6.getTreeColumn();
      if (var7 == var8) {
         Node var9 = ((TreeTableCell)this.getControl()).getTreeTableRow().getDisclosureNode();
         if (var9 != null) {
            double var10 = 0.0;

            TreeTableColumn var13;
            for(Iterator var12 = var6.getVisibleLeafColumns().iterator(); var12.hasNext(); var10 += var13.getWidth()) {
               var13 = (TreeTableColumn)var12.next();
               if (var13 == var8) {
                  break;
               }
            }

            double var14 = var9.getBoundsInParent().getMaxX();
            if (var1 < var14 - var10) {
               if (var5 != null) {
                  var5.setExpanded(!var5.isExpanded());
               }

               return true;
            }
         }
      }

      return false;
   }

   protected void handleClicks(MouseButton var1, int var2, boolean var3) {
      TreeItem var4 = ((TreeTableCell)this.getControl()).getTreeTableRow().getTreeItem();
      if (var1 == MouseButton.PRIMARY) {
         if (var2 == 1 && var3) {
            this.edit((TreeTableCell)this.getControl());
         } else if (var2 == 1) {
            this.edit((TreeTableCell)null);
         } else if (var2 == 2 && var4.isLeaf()) {
            this.edit((TreeTableCell)this.getControl());
         } else if (var2 % 2 == 0) {
            var4.setExpanded(!var4.isExpanded());
         }
      }

   }
}
