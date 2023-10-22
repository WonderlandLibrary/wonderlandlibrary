package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.CellBehaviorBase;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.IndexedCell;
import javafx.scene.shape.Rectangle;

public abstract class TableCellSkinBase extends CellSkinBase {
   static final String DEFER_TO_PARENT_PREF_WIDTH = "deferToParentPrefWidth";
   boolean isDeferToParentForPrefWidth = false;
   private InvalidationListener columnWidthListener = (var1x) -> {
      ((IndexedCell)this.getSkinnable()).requestLayout();
   };
   private WeakInvalidationListener weakColumnWidthListener;

   protected abstract ReadOnlyDoubleProperty columnWidthProperty();

   protected abstract BooleanProperty columnVisibleProperty();

   public TableCellSkinBase(IndexedCell var1, CellBehaviorBase var2) {
      super(var1, var2);
      this.weakColumnWidthListener = new WeakInvalidationListener(this.columnWidthListener);
   }

   protected void init(IndexedCell var1) {
      Rectangle var2 = new Rectangle();
      var2.widthProperty().bind(var1.widthProperty());
      var2.heightProperty().bind(var1.heightProperty());
      ((IndexedCell)this.getSkinnable()).setClip(var2);
      ReadOnlyDoubleProperty var3 = this.columnWidthProperty();
      if (var3 != null) {
         var3.addListener(this.weakColumnWidthListener);
      }

      this.registerChangeListener(var1.visibleProperty(), "VISIBLE");
      if (var1.getProperties().containsKey("deferToParentPrefWidth")) {
         this.isDeferToParentForPrefWidth = true;
      }

   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("VISIBLE".equals(var1)) {
         ((IndexedCell)this.getSkinnable()).setVisible(this.columnVisibleProperty().get());
      }

   }

   public void dispose() {
      ReadOnlyDoubleProperty var1 = this.columnWidthProperty();
      if (var1 != null) {
         var1.removeListener(this.weakColumnWidthListener);
      }

      super.dispose();
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.layoutLabelInArea(var1, var3, var5, var7 - ((IndexedCell)this.getSkinnable()).getPadding().getBottom());
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      return this.isDeferToParentForPrefWidth ? super.computePrefWidth(var1, var3, var5, var7, var9) : this.columnWidthProperty().get();
   }
}
