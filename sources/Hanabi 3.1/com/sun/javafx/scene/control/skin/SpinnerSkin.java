package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SpinnerBehavior;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class SpinnerSkin extends BehaviorSkinBase {
   private TextField textField;
   private Region incrementArrow;
   private StackPane incrementArrowButton;
   private Region decrementArrow;
   private StackPane decrementArrowButton;
   private static final int ARROWS_ON_RIGHT_VERTICAL = 0;
   private static final int ARROWS_ON_LEFT_VERTICAL = 1;
   private static final int ARROWS_ON_RIGHT_HORIZONTAL = 2;
   private static final int ARROWS_ON_LEFT_HORIZONTAL = 3;
   private static final int SPLIT_ARROWS_VERTICAL = 4;
   private static final int SPLIT_ARROWS_HORIZONTAL = 5;
   private int layoutMode = 0;
   private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("contains-focus");

   public SpinnerSkin(Spinner var1) {
      super(var1, new SpinnerBehavior(var1));
      this.textField = var1.getEditor();
      this.getChildren().add(this.textField);
      this.updateStyleClass();
      var1.getStyleClass().addListener((var1x) -> {
         this.updateStyleClass();
      });
      this.incrementArrow = new Region();
      this.incrementArrow.setFocusTraversable(false);
      this.incrementArrow.getStyleClass().setAll((Object[])("increment-arrow"));
      this.incrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
      this.incrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
      this.incrementArrow.setMouseTransparent(true);
      this.incrementArrowButton = new StackPane() {
         public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
            switch (var1) {
               case FIRE:
                  ((Spinner)SpinnerSkin.this.getSkinnable()).increment();
               default:
                  super.executeAccessibleAction(var1, var2);
            }
         }
      };
      this.incrementArrowButton.setAccessibleRole(AccessibleRole.INCREMENT_BUTTON);
      this.incrementArrowButton.setFocusTraversable(false);
      this.incrementArrowButton.getStyleClass().setAll((Object[])("increment-arrow-button"));
      this.incrementArrowButton.getChildren().add(this.incrementArrow);
      this.incrementArrowButton.setOnMousePressed((var1x) -> {
         ((Spinner)this.getSkinnable()).requestFocus();
         ((SpinnerBehavior)this.getBehavior()).startSpinning(true);
      });
      this.incrementArrowButton.setOnMouseReleased((var1x) -> {
         ((SpinnerBehavior)this.getBehavior()).stopSpinning();
      });
      this.decrementArrow = new Region();
      this.decrementArrow.setFocusTraversable(false);
      this.decrementArrow.getStyleClass().setAll((Object[])("decrement-arrow"));
      this.decrementArrow.setMaxWidth(Double.NEGATIVE_INFINITY);
      this.decrementArrow.setMaxHeight(Double.NEGATIVE_INFINITY);
      this.decrementArrow.setMouseTransparent(true);
      this.decrementArrowButton = new StackPane() {
         public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
            switch (var1) {
               case FIRE:
                  ((Spinner)SpinnerSkin.this.getSkinnable()).decrement();
               default:
                  super.executeAccessibleAction(var1, var2);
            }
         }
      };
      this.decrementArrowButton.setAccessibleRole(AccessibleRole.DECREMENT_BUTTON);
      this.decrementArrowButton.setFocusTraversable(false);
      this.decrementArrowButton.getStyleClass().setAll((Object[])("decrement-arrow-button"));
      this.decrementArrowButton.getChildren().add(this.decrementArrow);
      this.decrementArrowButton.setOnMousePressed((var1x) -> {
         ((Spinner)this.getSkinnable()).requestFocus();
         ((SpinnerBehavior)this.getBehavior()).startSpinning(false);
      });
      this.decrementArrowButton.setOnMouseReleased((var1x) -> {
         ((SpinnerBehavior)this.getBehavior()).stopSpinning();
      });
      this.getChildren().addAll(this.incrementArrowButton, this.decrementArrowButton);
      var1.focusedProperty().addListener((var1x, var2, var3) -> {
         ((ComboBoxPopupControl.FakeFocusTextField)this.textField).setFakeFocus(var3);
      });
      var1.addEventFilter(KeyEvent.ANY, (var2) -> {
         if (var1.isEditable()) {
            if (var2.getTarget().equals(this.textField)) {
               return;
            }

            if (var2.getCode() == KeyCode.ESCAPE) {
               return;
            }

            this.textField.fireEvent(var2.copyFor(this.textField, this.textField));
            var2.consume();
         }

      });
      this.textField.addEventFilter(KeyEvent.ANY, (var1x) -> {
         if (!var1.isEditable()) {
            var1.fireEvent(var1x.copyFor(var1, var1));
            var1x.consume();
         }

      });
      this.textField.focusedProperty().addListener((var2, var3, var4) -> {
         var1.getProperties().put("FOCUSED", var4);
         if (!var4) {
            this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, false);
         } else {
            this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, true);
         }

      });
      this.textField.focusTraversableProperty().bind(var1.editableProperty());
      var1.setImpl_traversalEngine(new ParentTraversalEngine(var1, new Algorithm() {
         public Node select(Node var1, Direction var2, TraversalContext var3) {
            return null;
         }

         public Node selectFirst(TraversalContext var1) {
            return null;
         }

         public Node selectLast(TraversalContext var1) {
            return null;
         }
      }));
   }

   private void updateStyleClass() {
      ObservableList var1 = ((Spinner)this.getSkinnable()).getStyleClass();
      if (var1.contains("arrows-on-left-vertical")) {
         this.layoutMode = 1;
      } else if (var1.contains("arrows-on-left-horizontal")) {
         this.layoutMode = 3;
      } else if (var1.contains("arrows-on-right-horizontal")) {
         this.layoutMode = 2;
      } else if (var1.contains("split-arrows-vertical")) {
         this.layoutMode = 4;
      } else if (var1.contains("split-arrows-horizontal")) {
         this.layoutMode = 5;
      } else {
         this.layoutMode = 0;
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      double var9 = this.incrementArrowButton.snappedLeftInset() + this.snapSize(this.incrementArrow.prefWidth(-1.0)) + this.incrementArrowButton.snappedRightInset();
      double var11 = this.decrementArrowButton.snappedLeftInset() + this.snapSize(this.decrementArrow.prefWidth(-1.0)) + this.decrementArrowButton.snappedRightInset();
      double var13 = Math.max(var9, var11);
      double var17;
      double var19;
      double var15;
      if (this.layoutMode != 0 && this.layoutMode != 1) {
         if (this.layoutMode != 2 && this.layoutMode != 3) {
            if (this.layoutMode == 4) {
               var15 = this.incrementArrowButton.snappedTopInset() + this.snapSize(this.incrementArrow.prefHeight(-1.0)) + this.incrementArrowButton.snappedBottomInset();
               var17 = this.decrementArrowButton.snappedTopInset() + this.snapSize(this.decrementArrow.prefHeight(-1.0)) + this.decrementArrowButton.snappedBottomInset();
               var19 = Math.max(var15, var17);
               this.incrementArrowButton.resize(var5, var19);
               this.positionInArea(this.incrementArrowButton, var1, var3, var5, var19, 0.0, HPos.CENTER, VPos.CENTER);
               this.textField.resizeRelocate(var1, var3 + var19, var5, var7 - 2.0 * var19);
               this.decrementArrowButton.resize(var5, var19);
               this.positionInArea(this.decrementArrowButton, var1, var7 - var19, var5, var19, 0.0, HPos.CENTER, VPos.CENTER);
            } else if (this.layoutMode == 5) {
               this.decrementArrowButton.resize(var13, var7);
               this.positionInArea(this.decrementArrowButton, var1, var3, var13, var7, 0.0, HPos.CENTER, VPos.CENTER);
               this.textField.resizeRelocate(var1 + var13, var3, var5 - 2.0 * var13, var7);
               this.incrementArrowButton.resize(var13, var7);
               this.positionInArea(this.incrementArrowButton, var5 - var13, var3, var13, var7, 0.0, HPos.CENTER, VPos.CENTER);
            }
         } else {
            var15 = var9 + var11;
            var17 = this.layoutMode == 2 ? var1 : var1 + var15;
            var19 = this.layoutMode == 2 ? var1 + var5 - var15 : var1;
            this.textField.resizeRelocate(var17, var3, var5 - var15, var7);
            this.decrementArrowButton.resize(var11, var7);
            this.positionInArea(this.decrementArrowButton, var19, var3, var11, var7, 0.0, HPos.CENTER, VPos.CENTER);
            this.incrementArrowButton.resize(var9, var7);
            this.positionInArea(this.incrementArrowButton, var19 + var11, var3, var9, var7, 0.0, HPos.CENTER, VPos.CENTER);
         }
      } else {
         var15 = this.layoutMode == 0 ? var1 : var1 + var13;
         var17 = this.layoutMode == 0 ? var1 + var5 - var13 : var1;
         var19 = Math.floor(var7 / 2.0);
         this.textField.resizeRelocate(var15, var3, var5 - var13, var7);
         this.incrementArrowButton.resize(var13, var19);
         this.positionInArea(this.incrementArrowButton, var17, var3, var13, var19, 0.0, HPos.CENTER, VPos.CENTER);
         this.decrementArrowButton.resize(var13, var19);
         this.positionInArea(this.decrementArrowButton, var17, var3 + var19, var13, var7 - var19, 0.0, HPos.CENTER, VPos.BOTTOM);
      }

   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return this.computePrefHeight(var1, var3, var5, var7, var9);
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.textField.prefWidth(var1);
      return var9 + var11 + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var13 = this.textField.prefHeight(var1);
      double var11;
      if (this.layoutMode == 4) {
         var11 = var3 + this.incrementArrowButton.prefHeight(var1) + var13 + this.decrementArrowButton.prefHeight(var1) + var7;
      } else {
         var11 = var3 + var13 + var7;
      }

      return var11;
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return ((Spinner)this.getSkinnable()).prefWidth(var1);
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((Spinner)this.getSkinnable()).prefHeight(var1);
   }

   protected double computeBaselineOffset(double var1, double var3, double var5, double var7) {
      return this.textField.getLayoutBounds().getMinY() + this.textField.getLayoutY() + this.textField.getBaselineOffset();
   }
}
