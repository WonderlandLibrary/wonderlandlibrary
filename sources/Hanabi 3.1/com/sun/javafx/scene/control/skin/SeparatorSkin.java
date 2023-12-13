package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.Collections;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

public class SeparatorSkin extends BehaviorSkinBase {
   private static final double DEFAULT_LENGTH = 10.0;
   private final Region line = new Region();

   public SeparatorSkin(Separator var1) {
      super(var1, new BehaviorBase(var1, Collections.emptyList()));
      this.line.getStyleClass().setAll((Object[])("line"));
      this.getChildren().add(this.line);
      this.registerChangeListener(var1.orientationProperty(), "ORIENTATION");
      this.registerChangeListener(var1.halignmentProperty(), "HALIGNMENT");
      this.registerChangeListener(var1.valignmentProperty(), "VALIGNMENT");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ORIENTATION".equals(var1) || "HALIGNMENT".equals(var1) || "VALIGNMENT".equals(var1)) {
         ((Separator)this.getSkinnable()).requestLayout();
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      Separator var9 = (Separator)this.getSkinnable();
      if (var9.getOrientation() == Orientation.HORIZONTAL) {
         this.line.resize(var5, this.line.prefHeight(-1.0));
      } else {
         this.line.resize(this.line.prefWidth(-1.0), var7);
      }

      this.positionInArea(this.line, var1, var3, var5, var7, 0.0, var9.getHalignment(), var9.getValignment());
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      return this.computePrefWidth(var1, var3, var5, var7, var9);
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.computePrefHeight(var1, var3, var5, var7, var9);
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      Separator var11 = (Separator)this.getSkinnable();
      double var12 = var11.getOrientation() == Orientation.VERTICAL ? this.line.prefWidth(-1.0) : 10.0;
      return var12 + var9 + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      Separator var11 = (Separator)this.getSkinnable();
      double var12 = var11.getOrientation() == Orientation.VERTICAL ? 10.0 : this.line.prefHeight(-1.0);
      return var12 + var3 + var7;
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      Separator var11 = (Separator)this.getSkinnable();
      return var11.getOrientation() == Orientation.VERTICAL ? var11.prefWidth(var1) : Double.MAX_VALUE;
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      Separator var11 = (Separator)this.getSkinnable();
      return var11.getOrientation() == Orientation.VERTICAL ? Double.MAX_VALUE : var11.prefHeight(var1);
   }
}
