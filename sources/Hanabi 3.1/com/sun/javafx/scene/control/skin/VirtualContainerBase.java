package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ScrollToEvent;

public abstract class VirtualContainerBase extends BehaviorSkinBase {
   protected boolean rowCountDirty;
   protected final VirtualFlow flow = this.createVirtualFlow();

   public VirtualContainerBase(Control var1, BehaviorBase var2) {
      super(var1, var2);
      var1.addEventHandler(ScrollToEvent.scrollToTopIndex(), (var1x) -> {
         if (this.rowCountDirty) {
            this.updateRowCount();
            this.rowCountDirty = false;
         }

         this.flow.scrollTo((Integer)var1x.getScrollTarget());
      });
   }

   public abstract IndexedCell createCell();

   protected VirtualFlow createVirtualFlow() {
      return new VirtualFlow();
   }

   public abstract int getItemCount();

   protected abstract void updateRowCount();

   double getMaxCellWidth(int var1) {
      return this.snappedLeftInset() + this.flow.getMaxCellWidth(var1) + this.snappedRightInset();
   }

   double getVirtualFlowPreferredHeight(int var1) {
      double var2 = 1.0;

      for(int var4 = 0; var4 < var1 && var4 < this.getItemCount(); ++var4) {
         var2 += this.flow.getCellLength(var4);
      }

      return var2 + this.snappedTopInset() + this.snappedBottomInset();
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.checkState();
   }

   protected void checkState() {
      if (this.rowCountDirty) {
         this.updateRowCount();
         this.rowCountDirty = false;
      }

   }
}
