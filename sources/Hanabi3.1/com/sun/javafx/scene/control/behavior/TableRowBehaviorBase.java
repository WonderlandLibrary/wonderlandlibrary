package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public abstract class TableRowBehaviorBase extends CellBehaviorBase {
   public TableRowBehaviorBase(Cell var1) {
      super(var1, Collections.emptyList());
   }

   public void mousePressed(MouseEvent var1) {
      if (this.isClickPositionValid(var1.getX(), var1.getY())) {
         super.mousePressed(var1);
      }
   }

   protected abstract TableSelectionModel getSelectionModel();

   protected abstract TablePositionBase getFocusedCell();

   protected abstract ObservableList getVisibleLeafColumns();

   protected void doSelect(double var1, double var3, MouseButton var5, int var6, boolean var7, boolean var8) {
      Control var9 = this.getCellContainer();
      if (var9 != null) {
         if (!this.handleDisclosureNode(var1, var3)) {
            TableSelectionModel var10 = this.getSelectionModel();
            if (var10 != null && !var10.isCellSelectionEnabled()) {
               int var11 = this.getIndex();
               boolean var12 = var10.isSelected(var11);
               if (var6 == 1) {
                  if (!this.isClickPositionValid(var1, var3)) {
                     return;
                  }

                  if (var12 && var8) {
                     var10.clearSelection(var11);
                  } else if (var8) {
                     var10.select(this.getIndex());
                  } else if (var7) {
                     TablePositionBase var13 = (TablePositionBase)TableCellBehavior.getAnchor(var9, this.getFocusedCell());
                     int var14 = var13.getRow();
                     this.selectRows(var14, var11);
                  } else {
                     this.simpleSelect(var5, var6, var8);
                  }
               } else {
                  this.simpleSelect(var5, var6, var8);
               }

            }
         }
      }
   }

   protected boolean isClickPositionValid(double var1, double var3) {
      ObservableList var5 = this.getVisibleLeafColumns();
      double var6 = 0.0;

      for(int var8 = 0; var8 < var5.size(); ++var8) {
         var6 += ((TableColumnBase)var5.get(var8)).getWidth();
      }

      return var1 > var6;
   }
}
