package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public abstract class ComboBoxBaseSkin extends BehaviorSkinBase {
   private Node displayNode;
   protected StackPane arrowButton;
   protected Region arrow;
   private ComboBoxMode mode;

   protected final ComboBoxMode getMode() {
      return this.mode;
   }

   protected final void setMode(ComboBoxMode var1) {
      this.mode = var1;
   }

   public ComboBoxBaseSkin(ComboBoxBase var1, ComboBoxBaseBehavior var2) {
      super(var1, var2);
      this.mode = ComboBoxMode.COMBOBOX;
      this.arrow = new Region();
      this.arrow.setFocusTraversable(false);
      this.arrow.getStyleClass().setAll((Object[])("arrow"));
      this.arrow.setId("arrow");
      this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
      this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
      this.arrow.setMouseTransparent(true);
      this.arrowButton = new StackPane();
      this.arrowButton.setFocusTraversable(false);
      this.arrowButton.setId("arrow-button");
      this.arrowButton.getStyleClass().setAll((Object[])("arrow-button"));
      this.arrowButton.getChildren().add(this.arrow);
      if (var1.isEditable()) {
         this.arrowButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (var1x) -> {
            ((ComboBoxBaseBehavior)this.getBehavior()).mouseEntered(var1x);
         });
         this.arrowButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (var1x) -> {
            ((ComboBoxBaseBehavior)this.getBehavior()).mousePressed(var1x);
            var1x.consume();
         });
         this.arrowButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (var1x) -> {
            ((ComboBoxBaseBehavior)this.getBehavior()).mouseReleased(var1x);
            var1x.consume();
         });
         this.arrowButton.addEventHandler(MouseEvent.MOUSE_EXITED, (var1x) -> {
            ((ComboBoxBaseBehavior)this.getBehavior()).mouseExited(var1x);
         });
      }

      this.getChildren().add(this.arrowButton);
      ((ComboBoxBase)this.getSkinnable()).focusedProperty().addListener((var1x, var2x, var3) -> {
         if (!var3) {
            this.focusLost();
         }

      });
      this.registerChangeListener(var1.editableProperty(), "EDITABLE");
      this.registerChangeListener(var1.showingProperty(), "SHOWING");
      this.registerChangeListener(var1.focusedProperty(), "FOCUSED");
      this.registerChangeListener(var1.valueProperty(), "VALUE");
   }

   protected void focusLost() {
      ((ComboBoxBase)this.getSkinnable()).hide();
   }

   public abstract Node getDisplayNode();

   public abstract void show();

   public abstract void hide();

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("SHOWING".equals(var1)) {
         if (((ComboBoxBase)this.getSkinnable()).isShowing()) {
            this.show();
         } else {
            this.hide();
         }
      } else if ("EDITABLE".equals(var1)) {
         this.updateDisplayArea();
      } else if ("VALUE".equals(var1)) {
         this.updateDisplayArea();
      }

   }

   protected void updateDisplayArea() {
      ObservableList var1 = this.getChildren();
      Node var2 = this.displayNode;
      this.displayNode = this.getDisplayNode();
      if (var2 != null && var2 != this.displayNode) {
         var1.remove(var2);
      }

      if (this.displayNode != null && !var1.contains(this.displayNode)) {
         var1.add(this.displayNode);
         this.displayNode.applyCss();
      }

   }

   private boolean isButton() {
      return this.getMode() == ComboBoxMode.BUTTON;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      if (this.displayNode == null) {
         this.updateDisplayArea();
      }

      double var9 = this.snapSize(this.arrow.prefWidth(-1.0));
      double var11 = this.isButton() ? 0.0 : this.arrowButton.snappedLeftInset() + var9 + this.arrowButton.snappedRightInset();
      if (this.displayNode != null) {
         this.displayNode.resizeRelocate(var1, var3, var5 - var11, var7);
      }

      this.arrowButton.setVisible(!this.isButton());
      if (!this.isButton()) {
         this.arrowButton.resize(var11, var7);
         this.positionInArea(this.arrowButton, var1 + var5 - var11, var3, var11, var7, 0.0, HPos.CENTER, VPos.CENTER);
      }

   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      if (this.displayNode == null) {
         this.updateDisplayArea();
      }

      double var11 = this.snapSize(this.arrow.prefWidth(-1.0));
      double var13 = this.isButton() ? 0.0 : this.arrowButton.snappedLeftInset() + var11 + this.arrowButton.snappedRightInset();
      double var15 = this.displayNode == null ? 0.0 : this.displayNode.prefWidth(var1);
      double var17 = var15 + var13;
      return var9 + var17 + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      if (this.displayNode == null) {
         this.updateDisplayArea();
      }

      double var11;
      if (this.displayNode == null) {
         double var14 = this.isButton() ? 0.0 : this.arrowButton.snappedTopInset() + this.arrow.prefHeight(-1.0) + this.arrowButton.snappedBottomInset();
         var11 = Math.max(21.0, var14);
      } else {
         var11 = this.displayNode.prefHeight(var1);
      }

      return var3 + var11 + var7;
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return ((ComboBoxBase)this.getSkinnable()).prefWidth(var1);
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((ComboBoxBase)this.getSkinnable()).prefHeight(var1);
   }

   protected double computeBaselineOffset(double var1, double var3, double var5, double var7) {
      if (this.displayNode == null) {
         this.updateDisplayArea();
      }

      return this.displayNode != null ? this.displayNode.getLayoutBounds().getMinY() + this.displayNode.getLayoutY() + this.displayNode.getBaselineOffset() : super.computeBaselineOffset(var1, var3, var5, var7);
   }
}
