package com.sun.javafx.scene.control.skin;

import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class EmbeddedTextContextMenuContent extends StackPane {
   private ContextMenu contextMenu;
   private StackPane pointer;
   private HBox menuBox;

   public EmbeddedTextContextMenuContent(ContextMenu var1) {
      this.contextMenu = var1;
      this.menuBox = new HBox();
      this.pointer = new StackPane();
      this.pointer.getStyleClass().add("pointer");
      this.updateMenuItemContainer();
      this.getChildren().addAll(this.pointer, this.menuBox);
      this.contextMenu.ownerNodeProperty().addListener((var1x) -> {
         if (this.contextMenu.getOwnerNode() instanceof TextArea) {
            TextAreaSkin var2 = (TextAreaSkin)((TextArea)this.contextMenu.getOwnerNode()).getSkin();
            ((TextArea)var2.getSkinnable()).getProperties().addListener(new InvalidationListener() {
               public void invalidated(Observable var1) {
                  EmbeddedTextContextMenuContent.this.requestLayout();
               }
            });
         } else if (this.contextMenu.getOwnerNode() instanceof TextField) {
            TextFieldSkin var3 = (TextFieldSkin)((TextField)this.contextMenu.getOwnerNode()).getSkin();
            ((TextField)var3.getSkinnable()).getProperties().addListener(new InvalidationListener() {
               public void invalidated(Observable var1) {
                  EmbeddedTextContextMenuContent.this.requestLayout();
               }
            });
         }

      });
      this.contextMenu.getItems().addListener((var1x) -> {
         this.updateMenuItemContainer();
      });
   }

   private void updateMenuItemContainer() {
      this.menuBox.getChildren().clear();
      Iterator var1 = this.contextMenu.getItems().iterator();

      while(var1.hasNext()) {
         MenuItem var2 = (MenuItem)var1.next();
         MenuItemContainer var3 = new MenuItemContainer(var2);
         var3.visibleProperty().bind(var2.visibleProperty());
         this.menuBox.getChildren().add(var3);
      }

   }

   private void hideAllMenus(MenuItem var1) {
      this.contextMenu.hide();

      Menu var2;
      while((var2 = ((MenuItem)var1).getParentMenu()) != null) {
         var2.hide();
         var1 = var2;
      }

      if (var2 == null && ((MenuItem)var1).getParentPopup() != null) {
         ((MenuItem)var1).getParentPopup().hide();
      }

   }

   protected double computePrefHeight(double var1) {
      double var3 = this.snapSize(this.pointer.prefHeight(var1));
      double var5 = this.snapSize(this.menuBox.prefHeight(var1));
      return this.snappedTopInset() + var3 + var5 + this.snappedBottomInset();
   }

   protected double computePrefWidth(double var1) {
      double var3 = this.snapSize(this.menuBox.prefWidth(var1));
      return this.snappedLeftInset() + var3 + this.snappedRightInset();
   }

   protected void layoutChildren() {
      double var1 = this.snappedLeftInset();
      double var3 = this.snappedRightInset();
      double var5 = this.snappedTopInset();
      double var7 = this.getWidth() - (var1 + var3);
      double var9 = this.snapSize(Utils.boundedSize(this.pointer.prefWidth(-1.0), this.pointer.minWidth(-1.0), this.pointer.maxWidth(-1.0)));
      double var11 = this.snapSize(Utils.boundedSize(this.pointer.prefWidth(-1.0), this.pointer.minWidth(-1.0), this.pointer.maxWidth(-1.0)));
      double var13 = this.snapSize(Utils.boundedSize(this.menuBox.prefWidth(-1.0), this.menuBox.minWidth(-1.0), this.menuBox.maxWidth(-1.0)));
      double var15 = this.snapSize(Utils.boundedSize(this.menuBox.prefWidth(-1.0), this.menuBox.minWidth(-1.0), this.menuBox.maxWidth(-1.0)));
      double var17 = 0.0;
      double var19 = 0.0;
      double var21 = 0.0;
      ObservableMap var23 = null;
      if (this.contextMenu.getOwnerNode() instanceof TextArea) {
         var23 = ((TextArea)this.contextMenu.getOwnerNode()).getProperties();
      } else if (this.contextMenu.getOwnerNode() instanceof TextField) {
         var23 = ((TextField)this.contextMenu.getOwnerNode()).getProperties();
      }

      if (var23 != null) {
         if (var23.containsKey("CONTEXT_MENU_SCENE_X")) {
            var17 = Double.valueOf(var23.get("CONTEXT_MENU_SCENE_X").toString());
            var23.remove("CONTEXT_MENU_SCENE_X");
         }

         if (var23.containsKey("CONTEXT_MENU_SCREEN_X")) {
            var19 = Double.valueOf(var23.get("CONTEXT_MENU_SCREEN_X").toString());
            var23.remove("CONTEXT_MENU_SCREEN_X");
         }
      }

      if (var17 == 0.0) {
         var21 = var7 / 2.0;
      } else {
         var21 = var19 - var17 - this.contextMenu.getX() + var17;
      }

      this.pointer.resize(var9, var11);
      this.positionInArea(this.pointer, var21, var5, var9, var11, 0.0, HPos.CENTER, VPos.CENTER);
      this.menuBox.resize(var13, var15);
      this.positionInArea(this.menuBox, var1, var5 + var11, var13, var15, 0.0, HPos.CENTER, VPos.CENTER);
   }

   class MenuItemContainer extends Button {
      private MenuItem item;

      public MenuItemContainer(MenuItem var2) {
         this.getStyleClass().addAll(var2.getStyleClass());
         this.setId(var2.getId());
         this.item = var2;
         this.setText(var2.getText());
         this.setStyle(var2.getStyle());
         this.textProperty().bind(var2.textProperty());
      }

      public MenuItem getItem() {
         return this.item;
      }

      public void fire() {
         Event.fireEvent(this.item, new ActionEvent());
         if (!Boolean.TRUE.equals((Boolean)this.item.getProperties().get("refreshMenu"))) {
            EmbeddedTextContextMenuContent.this.hideAllMenus(this.item);
         }

      }
   }
}
