package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.MouseButton;

public abstract class TableCellBehaviorBase extends CellBehaviorBase {
   public TableCellBehaviorBase(IndexedCell var1) {
      super(var1, Collections.emptyList());
   }

   protected abstract TableColumnBase getTableColumn();

   protected abstract int getItemCount();

   protected abstract TableSelectionModel getSelectionModel();

   protected abstract TableFocusModel getFocusModel();

   protected abstract TablePositionBase getFocusedCell();

   protected abstract boolean isTableRowSelected();

   protected abstract int getVisibleLeafIndex(TableColumnBase var1);

   protected abstract void focus(int var1, TableColumnBase var2);

   protected void doSelect(double var1, double var3, MouseButton var5, int var6, boolean var7, boolean var8) {
      IndexedCell var9 = (IndexedCell)this.getControl();
      if (var9.contains(var1, var3)) {
         Control var10 = this.getCellContainer();
         if (var10 != null) {
            int var11 = this.getItemCount();
            if (var9.getIndex() < var11) {
               TableSelectionModel var12 = this.getSelectionModel();
               if (var12 != null) {
                  boolean var13 = this.isSelected();
                  int var14 = var9.getIndex();
                  int var15 = this.getColumn();
                  TableColumnBase var16 = this.getTableColumn();
                  TableFocusModel var17 = this.getFocusModel();
                  if (var17 != null) {
                     TablePositionBase var18 = this.getFocusedCell();
                     if (!this.handleDisclosureNode(var1, var3)) {
                        if (var7) {
                           if (!hasNonDefaultAnchor(var10)) {
                              setAnchor(var10, var18, false);
                           }
                        } else {
                           removeAnchor(var10);
                        }

                        if (var5 == MouseButton.PRIMARY || var5 == MouseButton.SECONDARY && !var13) {
                           if (var12.getSelectionMode() == SelectionMode.SINGLE) {
                              this.simpleSelect(var5, var6, var8);
                           } else if (var8) {
                              if (var13) {
                                 var12.clearSelection(var14, var16);
                                 var17.focus(var14, var16);
                              } else {
                                 var12.select(var14, var16);
                              }
                           } else if (var7) {
                              TablePositionBase var19 = (TablePositionBase)getAnchor(var10, var18);
                              int var20 = var19.getRow();
                              boolean var21 = var20 < var14;
                              var12.clearSelection();
                              int var22 = Math.min(var20, var14);
                              int var23 = Math.max(var20, var14);
                              TableColumnBase var24 = var19.getColumn() < var15 ? var19.getTableColumn() : var16;
                              TableColumnBase var25 = var19.getColumn() >= var15 ? var19.getTableColumn() : var16;
                              if (var21) {
                                 var12.selectRange(var22, var24, var23, var25);
                              } else {
                                 var12.selectRange(var23, var24, var22, var25);
                              }
                           } else {
                              this.simpleSelect(var5, var6, var8);
                           }
                        }

                     }
                  }
               }
            }
         }
      }
   }

   protected void simpleSelect(MouseButton var1, int var2, boolean var3) {
      TableSelectionModel var4 = this.getSelectionModel();
      int var5 = ((IndexedCell)this.getControl()).getIndex();
      TableColumnBase var6 = this.getTableColumn();
      boolean var7 = var4.isSelected(var5, var6);
      if (var7 && var3) {
         var4.clearSelection(var5, var6);
         this.getFocusModel().focus(var5, var6);
         var7 = false;
      } else {
         var4.clearAndSelect(var5, var6);
      }

      this.handleClicks(var1, var2, var7);
   }

   private int getColumn() {
      if (this.getSelectionModel().isCellSelectionEnabled()) {
         TableColumnBase var1 = this.getTableColumn();
         return this.getVisibleLeafIndex(var1);
      } else {
         return -1;
      }
   }

   protected boolean isSelected() {
      TableSelectionModel var1 = this.getSelectionModel();
      if (var1 == null) {
         return false;
      } else if (var1.isCellSelectionEnabled()) {
         IndexedCell var2 = (IndexedCell)this.getControl();
         return var2.isSelected();
      } else {
         return this.isTableRowSelected();
      }
   }
}
