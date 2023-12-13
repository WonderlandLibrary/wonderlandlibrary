package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class ListCellSkin extends CellSkinBase {
   private double fixedCellSize;
   private boolean fixedCellSizeEnabled;

   public ListCellSkin(ListCell var1) {
      super(var1, new ListCellBehavior(var1));
      this.fixedCellSize = var1.getListView().getFixedCellSize();
      this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
      this.registerChangeListener(var1.getListView().fixedCellSizeProperty(), "FIXED_CELL_SIZE");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("FIXED_CELL_SIZE".equals(var1)) {
         this.fixedCellSize = ((ListCell)this.getSkinnable()).getListView().getFixedCellSize();
         this.fixedCellSizeEnabled = this.fixedCellSize > 0.0;
      }

   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = super.computePrefWidth(var1, var3, var5, var7, var9);
      ListView var13 = ((ListCell)this.getSkinnable()).getListView();
      return var13 == null ? 0.0 : (var13.getOrientation() == Orientation.VERTICAL ? var11 : Math.max(var11, this.getCellSize()));
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      if (this.fixedCellSizeEnabled) {
         return this.fixedCellSize;
      } else {
         double var11 = this.getCellSize();
         double var13 = var11 == 24.0 ? super.computePrefHeight(var1, var3, var5, var7, var9) : var11;
         return var13;
      }
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.fixedCellSizeEnabled ? this.fixedCellSize : super.computeMinHeight(var1, var3, var5, var7, var9);
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.fixedCellSizeEnabled ? this.fixedCellSize : super.computeMaxHeight(var1, var3, var5, var7, var9);
   }
}
