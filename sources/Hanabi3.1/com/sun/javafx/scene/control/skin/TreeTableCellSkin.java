package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import java.util.Map;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

public class TreeTableCellSkin extends TableCellSkinBase {
   private final TreeTableCell treeTableCell;
   private final TreeTableColumn tableColumn;

   public TreeTableCellSkin(TreeTableCell var1) {
      super(var1, new TreeTableCellBehavior(var1));
      this.treeTableCell = var1;
      this.tableColumn = var1.getTableColumn();
      super.init(var1);
   }

   protected BooleanProperty columnVisibleProperty() {
      return this.tableColumn.visibleProperty();
   }

   protected ReadOnlyDoubleProperty columnWidthProperty() {
      return this.tableColumn.widthProperty();
   }

   protected double leftLabelPadding() {
      double var1 = super.leftLabelPadding();
      double var3 = this.getCellSize();
      TreeTableCell var5 = (TreeTableCell)this.getSkinnable();
      TreeTableColumn var6 = var5.getTableColumn();
      if (var6 == null) {
         return var1;
      } else {
         TreeTableView var7 = var5.getTreeTableView();
         if (var7 == null) {
            return var1;
         } else {
            int var8 = var7.getVisibleLeafIndex(var6);
            TreeTableColumn var9 = var7.getTreeColumn();
            if (var9 == null && var8 != 0 || var9 != null && !var6.equals(var9)) {
               return var1;
            } else {
               TreeTableRow var10 = var5.getTreeTableRow();
               if (var10 == null) {
                  return var1;
               } else {
                  TreeItem var11 = var10.getTreeItem();
                  if (var11 == null) {
                     return var1;
                  } else {
                     int var12 = var7.getTreeItemLevel(var11);
                     if (!var7.isShowRoot()) {
                        --var12;
                     }

                     double var13 = 10.0;
                     if (var10.getSkin() instanceof TreeTableRowSkin) {
                        var13 = ((TreeTableRowSkin)var10.getSkin()).getIndentationPerLevel();
                     }

                     var1 += (double)var12 * var13;
                     Map var15 = TableRowSkinBase.maxDisclosureWidthMap;
                     var1 += var15.containsKey(var7) ? (Double)var15.get(var7) : 0.0;
                     Node var16 = var11.getGraphic();
                     var1 += var16 == null ? 0.0 : var16.prefWidth(var3);
                     return var1;
                  }
               }
            }
         }
      }
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      return this.isDeferToParentForPrefWidth ? super.computePrefWidth(var1, var3, var5, var7, var9) : this.columnWidthProperty().get();
   }
}
