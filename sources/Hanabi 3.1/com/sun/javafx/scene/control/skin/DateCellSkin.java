package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.DateCellBehavior;
import javafx.scene.control.DateCell;
import javafx.scene.text.Text;

public class DateCellSkin extends CellSkinBase {
   public DateCellSkin(DateCell var1) {
      super(var1, new DateCellBehavior(var1));
      var1.setMaxWidth(Double.MAX_VALUE);
   }

   protected void updateChildren() {
      super.updateChildren();
      Text var1 = (Text)((DateCell)this.getSkinnable()).getProperties().get("DateCell.secondaryText");
      if (var1 != null) {
         var1.setManaged(false);
         this.getChildren().add(var1);
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      super.layoutChildren(var1, var3, var5, var7);
      Text var9 = (Text)((DateCell)this.getSkinnable()).getProperties().get("DateCell.secondaryText");
      if (var9 != null) {
         double var10 = var1 + var5 - this.rightLabelPadding() - var9.getLayoutBounds().getWidth();
         double var12 = var3 + var7 - this.bottomLabelPadding() - var9.getLayoutBounds().getHeight();
         var9.relocate(this.snapPosition(var10), this.snapPosition(var12));
      }

   }

   private double cellSize() {
      double var1 = this.getCellSize();
      Text var3 = (Text)((DateCell)this.getSkinnable()).getProperties().get("DateCell.secondaryText");
      if (var3 != null && var1 == 24.0) {
         var1 = 36.0;
      }

      return var1;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = super.computePrefWidth(var1, var3, var5, var7, var9);
      return this.snapSize(Math.max(var11, this.cellSize()));
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = super.computePrefHeight(var1, var3, var5, var7, var9);
      return this.snapSize(Math.max(var11, this.cellSize()));
   }
}
