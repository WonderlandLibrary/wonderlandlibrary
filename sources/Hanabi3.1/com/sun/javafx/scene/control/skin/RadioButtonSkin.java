package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ToggleButtonBehavior;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;

public class RadioButtonSkin extends LabeledSkinBase {
   private StackPane radio = createRadio();

   public RadioButtonSkin(RadioButton var1) {
      super(var1, new ToggleButtonBehavior(var1));
      this.updateChildren();
   }

   protected void updateChildren() {
      super.updateChildren();
      if (this.radio != null) {
         this.getChildren().add(this.radio);
      }

   }

   private static StackPane createRadio() {
      StackPane var0 = new StackPane();
      var0.getStyleClass().setAll((Object[])("radio"));
      var0.setSnapToPixel(false);
      StackPane var1 = new StackPane();
      var1.getStyleClass().setAll((Object[])("dot"));
      var0.getChildren().clear();
      var0.getChildren().addAll(var1);
      return var0;
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      return super.computeMinWidth(var1, var3, var5, var7, var9) + this.snapSize(this.radio.minWidth(-1.0));
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return Math.max(this.snapSize(super.computeMinHeight(var1 - this.radio.minWidth(-1.0), var3, var5, var7, var9)), var3 + this.radio.minHeight(-1.0) + var7);
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      return super.computePrefWidth(var1, var3, var5, var7, var9) + this.snapSize(this.radio.prefWidth(-1.0));
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return Math.max(this.snapSize(super.computePrefHeight(var1 - this.radio.prefWidth(-1.0), var3, var5, var7, var9)), var3 + this.radio.prefHeight(-1.0) + var7);
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      RadioButton var9 = (RadioButton)this.getSkinnable();
      double var10 = this.radio.prefWidth(-1.0);
      double var12 = this.radio.prefHeight(-1.0);
      double var14 = Math.max(var9.prefWidth(-1.0), var9.minWidth(-1.0));
      double var16 = Math.min(var14 - var10, var5 - this.snapSize(var10));
      double var18 = Math.min(var9.prefHeight(var16), var7);
      double var20 = Math.max(var12, var18);
      double var22 = Utils.computeXOffset(var5, var16 + var10, var9.getAlignment().getHpos()) + var1;
      double var24 = Utils.computeYOffset(var7, var20, var9.getAlignment().getVpos()) + var3;
      this.layoutLabelInArea(var22 + var10, var24, var16, var20, var9.getAlignment());
      this.radio.resize(this.snapSize(var10), this.snapSize(var12));
      this.positionInArea(this.radio, var22, var24, var10, var20, 0.0, var9.getAlignment().getHpos(), var9.getAlignment().getVpos());
   }
}
