package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import javafx.scene.Node;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;

public class TreeCellBehavior extends CellBehaviorBase {
   public TreeCellBehavior(TreeCell var1) {
      super(var1, Collections.emptyList());
   }

   protected MultipleSelectionModel getSelectionModel() {
      return this.getCellContainer().getSelectionModel();
   }

   protected FocusModel getFocusModel() {
      return this.getCellContainer().getFocusModel();
   }

   protected TreeView getCellContainer() {
      return ((TreeCell)this.getControl()).getTreeView();
   }

   protected void edit(TreeCell var1) {
      TreeItem var2 = var1 == null ? null : var1.getTreeItem();
      this.getCellContainer().edit(var2);
   }

   protected void handleClicks(MouseButton var1, int var2, boolean var3) {
      TreeItem var4 = ((TreeCell)this.getControl()).getTreeItem();
      if (var1 == MouseButton.PRIMARY) {
         if (var2 == 1 && var3) {
            this.edit((TreeCell)this.getControl());
         } else if (var2 == 1) {
            this.edit((TreeCell)null);
         } else if (var2 == 2 && var4.isLeaf()) {
            this.edit((TreeCell)this.getControl());
         } else if (var2 % 2 == 0) {
            var4.setExpanded(!var4.isExpanded());
         }
      }

   }

   protected boolean handleDisclosureNode(double var1, double var3) {
      TreeCell var5 = (TreeCell)this.getControl();
      Node var6 = var5.getDisclosureNode();
      if (var6 != null && var6.getBoundsInParent().contains(var1, var3)) {
         if (var5.getTreeItem() != null) {
            var5.getTreeItem().setExpanded(!var5.getTreeItem().isExpanded());
         }

         return true;
      } else {
         return false;
      }
   }
}
