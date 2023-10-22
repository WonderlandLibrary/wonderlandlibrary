package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ChoiceBoxBehavior;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

public class ChoiceBoxSkin extends BehaviorSkinBase {
   private ObservableList choiceBoxItems;
   private ContextMenu popup;
   private StackPane openButton;
   private final ToggleGroup toggleGroup = new ToggleGroup();
   private SelectionModel selectionModel;
   private Label label;
   private final ListChangeListener choiceBoxItemsListener = new ListChangeListener() {
      public void onChanged(ListChangeListener.Change var1) {
         label29:
         while(true) {
            if (var1.next()) {
               int var2;
               if (var1.getRemovedSize() <= 0 && !var1.wasPermutated()) {
                  var2 = var1.getFrom();

                  while(true) {
                     if (var2 >= var1.getTo()) {
                        continue label29;
                     }

                     Object var5 = var1.getList().get(var2);
                     ChoiceBoxSkin.this.addPopupItem(var5, var2);
                     ++var2;
                  }
               }

               ChoiceBoxSkin.this.toggleGroup.getToggles().clear();
               ChoiceBoxSkin.this.popup.getItems().clear();
               var2 = 0;
               Iterator var3 = var1.getList().iterator();

               while(true) {
                  if (!var3.hasNext()) {
                     continue label29;
                  }

                  Object var4 = var3.next();
                  ChoiceBoxSkin.this.addPopupItem(var4, var2);
                  ++var2;
               }
            }

            ChoiceBoxSkin.this.updateSelection();
            ((ChoiceBox)ChoiceBoxSkin.this.getSkinnable()).requestLayout();
            return;
         }
      }
   };
   private final WeakListChangeListener weakChoiceBoxItemsListener;
   private final InvalidationListener itemsObserver;
   private InvalidationListener selectionChangeListener;

   public ChoiceBoxSkin(ChoiceBox var1) {
      super(var1, new ChoiceBoxBehavior(var1));
      this.weakChoiceBoxItemsListener = new WeakListChangeListener(this.choiceBoxItemsListener);
      this.selectionChangeListener = (var1x) -> {
         this.updateSelection();
      };
      this.initialize();
      this.itemsObserver = (var1x) -> {
         this.updateChoiceBoxItems();
      };
      var1.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
      var1.requestLayout();
      this.registerChangeListener(var1.selectionModelProperty(), "SELECTION_MODEL");
      this.registerChangeListener(var1.showingProperty(), "SHOWING");
      this.registerChangeListener(var1.itemsProperty(), "ITEMS");
      this.registerChangeListener(var1.getSelectionModel().selectedItemProperty(), "SELECTION_CHANGED");
      this.registerChangeListener(var1.converterProperty(), "CONVERTER");
   }

   private void initialize() {
      this.updateChoiceBoxItems();
      this.label = new Label();
      this.label.setMnemonicParsing(false);
      this.openButton = new StackPane();
      this.openButton.getStyleClass().setAll((Object[])("open-button"));
      StackPane var1 = new StackPane();
      var1.getStyleClass().setAll((Object[])("arrow"));
      this.openButton.getChildren().clear();
      this.openButton.getChildren().addAll(var1);
      this.popup = new ContextMenu();
      this.popup.showingProperty().addListener((var1x, var2, var3) -> {
         if (!var3) {
            ((ChoiceBox)this.getSkinnable()).hide();
         }

      });
      this.popup.setId("choice-box-popup-menu");
      this.getChildren().setAll((Object[])(this.label, this.openButton));
      this.updatePopupItems();
      this.updateSelectionModel();
      this.updateSelection();
      if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
         this.label.setText("");
      }

   }

   private void updateChoiceBoxItems() {
      if (this.choiceBoxItems != null) {
         this.choiceBoxItems.removeListener(this.weakChoiceBoxItemsListener);
      }

      this.choiceBoxItems = ((ChoiceBox)this.getSkinnable()).getItems();
      if (this.choiceBoxItems != null) {
         this.choiceBoxItems.addListener(this.weakChoiceBoxItemsListener);
      }

   }

   String getChoiceBoxSelectedText() {
      return this.label.getText();
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("ITEMS".equals(var1)) {
         this.updateChoiceBoxItems();
         this.updatePopupItems();
         this.updateSelectionModel();
         this.updateSelection();
         if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
            this.label.setText("");
         }
      } else if ("SELECTION_MODEL".equals(var1)) {
         this.updateSelectionModel();
      } else if ("SELECTION_CHANGED".equals(var1)) {
         if (((ChoiceBox)this.getSkinnable()).getSelectionModel() != null) {
            int var2 = ((ChoiceBox)this.getSkinnable()).getSelectionModel().getSelectedIndex();
            if (var2 != -1) {
               MenuItem var3 = (MenuItem)this.popup.getItems().get(var2);
               if (var3 instanceof RadioMenuItem) {
                  ((RadioMenuItem)var3).setSelected(true);
               }
            }
         }
      } else if ("SHOWING".equals(var1)) {
         if (((ChoiceBox)this.getSkinnable()).isShowing()) {
            MenuItem var11 = null;
            SingleSelectionModel var12 = ((ChoiceBox)this.getSkinnable()).getSelectionModel();
            if (var12 == null) {
               return;
            }

            long var4 = (long)var12.getSelectedIndex();
            int var6 = this.choiceBoxItems.size();
            boolean var7 = var4 >= 0L && var4 < (long)var6;
            if (var7) {
               var11 = (MenuItem)this.popup.getItems().get((int)var4);
               if (var11 != null && var11 instanceof RadioMenuItem) {
                  ((RadioMenuItem)var11).setSelected(true);
               }
            } else if (var6 > 0) {
               var11 = (MenuItem)this.popup.getItems().get(0);
            }

            ((ChoiceBox)this.getSkinnable()).autosize();
            double var8 = 0.0;
            if (this.popup.getSkin() != null) {
               ContextMenuContent var10 = (ContextMenuContent)this.popup.getSkin().getNode();
               if (var10 != null && var4 != -1L) {
                  var8 = -var10.getMenuYOffset((int)var4);
               }
            }

            this.popup.show(this.getSkinnable(), Side.BOTTOM, 2.0, var8);
         } else {
            this.popup.hide();
         }
      } else if ("CONVERTER".equals(var1)) {
         this.updateChoiceBoxItems();
         this.updatePopupItems();
      }

   }

   private void addPopupItem(Object var1, int var2) {
      Object var3 = null;
      if (var1 instanceof Separator) {
         var3 = new SeparatorMenuItem();
      } else if (var1 instanceof SeparatorMenuItem) {
         var3 = (SeparatorMenuItem)var1;
      } else {
         StringConverter var4 = ((ChoiceBox)this.getSkinnable()).getConverter();
         String var5 = var4 == null ? (var1 == null ? "" : var1.toString()) : var4.toString(var1);
         RadioMenuItem var6 = new RadioMenuItem(var5);
         var6.setId("choice-box-menu-item");
         var6.setToggleGroup(this.toggleGroup);
         var6.setOnAction((var3x) -> {
            if (this.selectionModel != null) {
               int var4 = ((ChoiceBox)this.getSkinnable()).getItems().indexOf(var1);
               this.selectionModel.select(var4);
               var6.setSelected(true);
            }
         });
         var3 = var6;
      }

      ((MenuItem)var3).setMnemonicParsing(false);
      this.popup.getItems().add(var2, var3);
   }

   private void updatePopupItems() {
      this.toggleGroup.getToggles().clear();
      this.popup.getItems().clear();
      this.toggleGroup.selectToggle((Toggle)null);

      for(int var1 = 0; var1 < this.choiceBoxItems.size(); ++var1) {
         Object var2 = this.choiceBoxItems.get(var1);
         this.addPopupItem(var2, var1);
      }

   }

   private void updateSelectionModel() {
      if (this.selectionModel != null) {
         this.selectionModel.selectedIndexProperty().removeListener(this.selectionChangeListener);
      }

      this.selectionModel = ((ChoiceBox)this.getSkinnable()).getSelectionModel();
      if (this.selectionModel != null) {
         this.selectionModel.selectedIndexProperty().addListener(this.selectionChangeListener);
      }

   }

   private void updateSelection() {
      if (this.selectionModel != null && !this.selectionModel.isEmpty()) {
         int var1 = this.selectionModel.getSelectedIndex();
         if (var1 == -1 || var1 > this.popup.getItems().size()) {
            this.label.setText("");
            return;
         }

         if (var1 < this.popup.getItems().size()) {
            MenuItem var2 = (MenuItem)this.popup.getItems().get(var1);
            if (var2 instanceof RadioMenuItem) {
               ((RadioMenuItem)var2).setSelected(true);
               this.toggleGroup.selectToggle((Toggle)null);
            }

            this.label.setText(((MenuItem)this.popup.getItems().get(var1)).getText());
         }
      } else {
         this.toggleGroup.selectToggle((Toggle)null);
         this.label.setText("");
      }

   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      double var9 = this.openButton.prefWidth(-1.0);
      ChoiceBox var11 = (ChoiceBox)this.getSkinnable();
      this.label.resizeRelocate(var1, var3, var5, var7);
      this.openButton.resize(var9, this.openButton.prefHeight(-1.0));
      this.positionInArea(this.openButton, var1 + var5 - var9, var3, var9, var7, 0.0, HPos.CENTER, VPos.CENTER);
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.label.minWidth(-1.0) + this.openButton.minWidth(-1.0);
      double var13 = this.popup.minWidth(-1.0);
      return var9 + Math.max(var11, var13) + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.label.minHeight(-1.0);
      double var13 = this.openButton.minHeight(-1.0);
      return var3 + Math.max(var11, var13) + var7;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.label.prefWidth(-1.0) + this.openButton.prefWidth(-1.0);
      double var13 = this.popup.prefWidth(-1.0);
      if (var13 <= 0.0 && this.popup.getItems().size() > 0) {
         var13 = (new Text(((MenuItem)this.popup.getItems().get(0)).getText())).prefWidth(-1.0);
      }

      return this.popup.getItems().size() == 0 ? 50.0 : var9 + Math.max(var11, var13) + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.label.prefHeight(-1.0);
      double var13 = this.openButton.prefHeight(-1.0);
      return var3 + Math.max(var11, var13) + var7;
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((ChoiceBox)this.getSkinnable()).prefHeight(var1);
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return ((ChoiceBox)this.getSkinnable()).prefWidth(var1);
   }
}
