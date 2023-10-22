package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.behavior.MenuButtonBehaviorBase;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class MenuButtonSkinBase extends BehaviorSkinBase {
   protected final LabeledImpl label;
   protected final StackPane arrow;
   protected final StackPane arrowButton;
   protected ContextMenu popup;
   protected boolean behaveLikeButton = false;
   private ListChangeListener itemsChangedListener;

   public MenuButtonSkinBase(MenuButton var1, MenuButtonBehaviorBase var2) {
      super(var1, var2);
      if (var1.getOnMousePressed() == null) {
         var1.addEventHandler(MouseEvent.MOUSE_PRESSED, (var1x) -> {
            ((MenuButtonBehaviorBase)this.getBehavior()).mousePressed(var1x, this.behaveLikeButton);
         });
      }

      if (var1.getOnMouseReleased() == null) {
         var1.addEventHandler(MouseEvent.MOUSE_RELEASED, (var1x) -> {
            ((MenuButtonBehaviorBase)this.getBehavior()).mouseReleased(var1x, this.behaveLikeButton);
         });
      }

      this.label = new MenuLabeledImpl((MenuButton)this.getSkinnable());
      this.label.setMnemonicParsing(var1.isMnemonicParsing());
      this.label.setLabelFor(var1);
      this.arrow = new StackPane();
      this.arrow.getStyleClass().setAll((Object[])("arrow"));
      this.arrow.setMaxWidth(Double.NEGATIVE_INFINITY);
      this.arrow.setMaxHeight(Double.NEGATIVE_INFINITY);
      this.arrowButton = new StackPane();
      this.arrowButton.getStyleClass().setAll((Object[])("arrow-button"));
      this.arrowButton.getChildren().add(this.arrow);
      this.popup = new ContextMenu();
      this.popup.getItems().clear();
      this.popup.getItems().addAll(((MenuButton)this.getSkinnable()).getItems());
      this.getChildren().clear();
      this.getChildren().addAll(this.label, this.arrowButton);
      ((MenuButton)this.getSkinnable()).requestLayout();
      this.itemsChangedListener = (var1x) -> {
         while(var1x.next()) {
            this.popup.getItems().removeAll(var1x.getRemoved());
            this.popup.getItems().addAll(var1x.getFrom(), var1x.getAddedSubList());
         }

      };
      var1.getItems().addListener(this.itemsChangedListener);
      if (((MenuButton)this.getSkinnable()).getScene() != null) {
         ControlAcceleratorSupport.addAcceleratorsIntoScene(((MenuButton)this.getSkinnable()).getItems(), (Node)this.getSkinnable());
      }

      var1.sceneProperty().addListener((var1x, var2x, var3) -> {
         if (this.getSkinnable() != null && ((MenuButton)this.getSkinnable()).getScene() != null) {
            ControlAcceleratorSupport.addAcceleratorsIntoScene(((MenuButton)this.getSkinnable()).getItems(), (Node)this.getSkinnable());
         }

      });
      this.registerChangeListener(var1.showingProperty(), "SHOWING");
      this.registerChangeListener(var1.focusedProperty(), "FOCUSED");
      this.registerChangeListener(var1.mnemonicParsingProperty(), "MNEMONIC_PARSING");
      this.registerChangeListener(this.popup.showingProperty(), "POPUP_VISIBLE");
   }

   public void dispose() {
      ((MenuButton)this.getSkinnable()).getItems().removeListener(this.itemsChangedListener);
      super.dispose();
      if (this.popup != null) {
         if (this.popup.getSkin() != null && this.popup.getSkin().getNode() != null) {
            ContextMenuContent var1 = (ContextMenuContent)this.popup.getSkin().getNode();
            var1.dispose();
            var1 = null;
         }

         this.popup.setSkin((Skin)null);
         this.popup = null;
      }

   }

   private void show() {
      if (!this.popup.isShowing()) {
         this.popup.show(this.getSkinnable(), ((MenuButton)this.getSkinnable()).getPopupSide(), 0.0, 0.0);
      }

   }

   private void hide() {
      if (this.popup.isShowing()) {
         this.popup.hide();
      }

   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("SHOWING".equals(var1)) {
         if (((MenuButton)this.getSkinnable()).isShowing()) {
            this.show();
         } else {
            this.hide();
         }
      } else if ("FOCUSED".equals(var1)) {
         if (!((MenuButton)this.getSkinnable()).isFocused() && ((MenuButton)this.getSkinnable()).isShowing()) {
            this.hide();
         }

         if (!((MenuButton)this.getSkinnable()).isFocused() && this.popup.isShowing()) {
            this.hide();
         }
      } else if ("POPUP_VISIBLE".equals(var1)) {
         if (!this.popup.isShowing() && ((MenuButton)this.getSkinnable()).isShowing()) {
            ((MenuButton)this.getSkinnable()).hide();
         }

         if (this.popup.isShowing()) {
            Utils.addMnemonics(this.popup, ((MenuButton)this.getSkinnable()).getScene(), ((MenuButton)this.getSkinnable()).impl_isShowMnemonics());
         } else {
            Utils.removeMnemonics(this.popup, ((MenuButton)this.getSkinnable()).getScene());
         }
      } else if ("MNEMONIC_PARSING".equals(var1)) {
         this.label.setMnemonicParsing(((MenuButton)this.getSkinnable()).isMnemonicParsing());
         ((MenuButton)this.getSkinnable()).requestLayout();
      }

   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      return var9 + this.label.minWidth(var1) + this.snapSize(this.arrowButton.minWidth(var1)) + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      return var3 + Math.max(this.label.minHeight(var1), this.snapSize(this.arrowButton.minHeight(-1.0))) + var7;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      return var9 + this.label.prefWidth(var1) + this.snapSize(this.arrowButton.prefWidth(var1)) + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      return var3 + Math.max(this.label.prefHeight(var1), this.snapSize(this.arrowButton.prefHeight(-1.0))) + var7;
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return ((MenuButton)this.getSkinnable()).prefWidth(var1);
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((MenuButton)this.getSkinnable()).prefHeight(var1);
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      double var9 = this.snapSize(this.arrowButton.prefWidth(-1.0));
      this.label.resizeRelocate(var1, var3, var5 - var9, var7);
      this.arrowButton.resizeRelocate(var1 + (var5 - var9), var3, var9, var7);
   }

   private class MenuLabeledImpl extends LabeledImpl {
      MenuButton button;

      public MenuLabeledImpl(MenuButton var2) {
         super(var2);
         this.button = var2;
         this.addEventHandler(ActionEvent.ACTION, (var1x) -> {
            this.button.fireEvent(new ActionEvent());
            var1x.consume();
         });
      }
   }
}
