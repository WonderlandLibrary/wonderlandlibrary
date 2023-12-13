package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Duration;

public class ContextMenuContent extends Region {
   private ContextMenu contextMenu;
   private double maxGraphicWidth = 0.0;
   private double maxRightWidth = 0.0;
   private double maxLabelWidth = 0.0;
   private double maxRowHeight = 0.0;
   private double maxLeftWidth = 0.0;
   private double oldWidth = 0.0;
   private Rectangle clipRect;
   MenuBox itemsContainer;
   private ArrowMenuItem upArrow;
   private ArrowMenuItem downArrow;
   private int currentFocusedIndex = -1;
   private boolean itemsDirty = true;
   private InvalidationListener popupShowingListener = (var1x) -> {
      this.updateItems();
   };
   private WeakInvalidationListener weakPopupShowingListener;
   private boolean isFirstShow;
   private double ty;
   private ChangeListener menuShowingListener;
   private ListChangeListener contextMenuItemsListener;
   private ChangeListener menuItemVisibleListener;
   private Menu openSubmenu;
   private ContextMenu submenu;
   Region selectedBackground;
   private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("selected");
   private static final PseudoClass DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("disabled");
   private static final PseudoClass CHECKED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("checked");

   public ContextMenuContent(ContextMenu var1) {
      this.weakPopupShowingListener = new WeakInvalidationListener(this.popupShowingListener);
      this.isFirstShow = true;
      this.menuShowingListener = (var1x, var2, var3) -> {
         ReadOnlyBooleanProperty var4 = (ReadOnlyBooleanProperty)var1x;
         Menu var5 = (Menu)var4.getBean();
         if (var2 && !var3) {
            this.hideSubmenu();
         } else if (!var2 && var3) {
            this.showSubmenu(var5);
         }

      };
      this.contextMenuItemsListener = (var1x) -> {
         while(var1x.next()) {
            this.updateMenuShowingListeners(var1x.getRemoved(), false);
            this.updateMenuShowingListeners(var1x.getAddedSubList(), true);
         }

         this.itemsDirty = true;
         this.updateItems();
      };
      this.menuItemVisibleListener = (var1x, var2, var3) -> {
         this.requestLayout();
      };
      this.contextMenu = var1;
      this.clipRect = new Rectangle();
      this.clipRect.setSmooth(false);
      this.itemsContainer = new MenuBox();
      this.itemsContainer.setClip(this.clipRect);
      this.upArrow = new ArrowMenuItem(this);
      this.upArrow.setUp(true);
      this.upArrow.setFocusTraversable(false);
      this.downArrow = new ArrowMenuItem(this);
      this.downArrow.setUp(false);
      this.downArrow.setFocusTraversable(false);
      this.getChildren().add(this.itemsContainer);
      this.getChildren().add(this.upArrow);
      this.getChildren().add(this.downArrow);
      this.initialize();
      this.setUpBinds();
      this.updateItems();
      var1.showingProperty().addListener(this.weakPopupShowingListener);
      if (Utils.isTwoLevelFocus()) {
         new TwoLevelFocusPopupBehavior(this);
      }

   }

   public VBox getItemsContainer() {
      return this.itemsContainer;
   }

   int getCurrentFocusIndex() {
      return this.currentFocusedIndex;
   }

   void setCurrentFocusedIndex(int var1) {
      if (var1 < this.itemsContainer.getChildren().size()) {
         this.currentFocusedIndex = var1;
      }

   }

   private void updateItems() {
      if (this.itemsDirty) {
         this.updateVisualItems();
         this.itemsDirty = false;
      }

   }

   private void computeVisualMetrics() {
      this.maxRightWidth = 0.0;
      this.maxLabelWidth = 0.0;
      this.maxRowHeight = 0.0;
      this.maxGraphicWidth = 0.0;
      this.maxLeftWidth = 0.0;

      for(int var1 = 0; var1 < this.itemsContainer.getChildren().size(); ++var1) {
         Node var2 = (Node)this.itemsContainer.getChildren().get(var1);
         if (var2 instanceof MenuItemContainer) {
            MenuItemContainer var3 = (MenuItemContainer)this.itemsContainer.getChildren().get(var1);
            if (var3.isVisible()) {
               double var4 = -1.0;
               Node var6 = var3.left;
               if (var6 != null) {
                  if (var6.getContentBias() == Orientation.VERTICAL) {
                     var4 = this.snapSize(var6.prefHeight(-1.0));
                  } else {
                     var4 = -1.0;
                  }

                  this.maxLeftWidth = Math.max(this.maxLeftWidth, this.snapSize(var6.prefWidth(var4)));
                  this.maxRowHeight = Math.max(this.maxRowHeight, var6.prefHeight(-1.0));
               }

               var6 = var3.graphic;
               if (var6 != null) {
                  if (var6.getContentBias() == Orientation.VERTICAL) {
                     var4 = this.snapSize(var6.prefHeight(-1.0));
                  } else {
                     var4 = -1.0;
                  }

                  this.maxGraphicWidth = Math.max(this.maxGraphicWidth, this.snapSize(var6.prefWidth(var4)));
                  this.maxRowHeight = Math.max(this.maxRowHeight, var6.prefHeight(-1.0));
               }

               var6 = var3.label;
               if (var6 != null) {
                  if (var6.getContentBias() == Orientation.VERTICAL) {
                     var4 = this.snapSize(var6.prefHeight(-1.0));
                  } else {
                     var4 = -1.0;
                  }

                  this.maxLabelWidth = Math.max(this.maxLabelWidth, this.snapSize(var6.prefWidth(var4)));
                  this.maxRowHeight = Math.max(this.maxRowHeight, var6.prefHeight(-1.0));
               }

               var6 = var3.right;
               if (var6 != null) {
                  if (var6.getContentBias() == Orientation.VERTICAL) {
                     var4 = this.snapSize(var6.prefHeight(-1.0));
                  } else {
                     var4 = -1.0;
                  }

                  this.maxRightWidth = Math.max(this.maxRightWidth, this.snapSize(var6.prefWidth(var4)));
                  this.maxRowHeight = Math.max(this.maxRowHeight, var6.prefHeight(-1.0));
               }
            }
         }
      }

      double var7 = this.maxRightWidth + this.maxLabelWidth + this.maxGraphicWidth + this.maxLeftWidth;
      Window var8 = this.contextMenu.getOwnerWindow();
      if (var8 instanceof ContextMenu && this.contextMenu.getX() < var8.getX() && this.oldWidth != var7) {
         this.contextMenu.setX(this.contextMenu.getX() + this.oldWidth - var7);
      }

      this.oldWidth = var7;
   }

   private void updateVisualItems() {
      ObservableList var1 = this.itemsContainer.getChildren();
      this.disposeVisualItems();

      for(int var2 = 0; var2 < this.getItems().size(); ++var2) {
         MenuItem var3 = (MenuItem)this.getItems().get(var2);
         if (!(var3 instanceof CustomMenuItem) || ((CustomMenuItem)var3).getContent() != null) {
            if (var3 instanceof SeparatorMenuItem) {
               Node var4 = ((CustomMenuItem)var3).getContent();
               var4.visibleProperty().bind(var3.visibleProperty());
               var1.add(var4);
               var4.getProperties().put(MenuItem.class, var3);
            } else {
               MenuItemContainer var6 = new MenuItemContainer(var3);
               var6.visibleProperty().bind(var3.visibleProperty());
               var1.add(var6);
            }
         }
      }

      if (this.getItems().size() > 0) {
         MenuItem var5 = (MenuItem)this.getItems().get(0);
         this.getProperties().put(Menu.class, var5.getParentMenu());
      }

      this.impl_reapplyCSS();
   }

   private void disposeVisualItems() {
      ObservableList var1 = this.itemsContainer.getChildren();
      int var2 = 0;

      for(int var3 = var1.size(); var2 < var3; ++var2) {
         Node var4 = (Node)var1.get(var2);
         if (var4 instanceof MenuItemContainer) {
            MenuItemContainer var5 = (MenuItemContainer)var4;
            var5.visibleProperty().unbind();
            var5.dispose();
         }
      }

      var1.clear();
   }

   public void dispose() {
      this.disposeBinds();
      this.disposeVisualItems();
      this.disposeContextMenu(this.submenu);
      this.submenu = null;
      this.openSubmenu = null;
      this.selectedBackground = null;
      if (this.contextMenu != null) {
         this.contextMenu.getItems().clear();
         this.contextMenu = null;
      }

   }

   public void disposeContextMenu(ContextMenu var1) {
      if (var1 != null) {
         Skin var2 = var1.getSkin();
         if (var2 != null) {
            ContextMenuContent var3 = (ContextMenuContent)var2.getNode();
            if (var3 != null) {
               var3.dispose();
            }
         }
      }
   }

   protected void layoutChildren() {
      if (this.itemsContainer.getChildren().size() != 0) {
         double var1 = this.snappedLeftInset();
         double var3 = this.snappedTopInset();
         double var5 = this.getWidth() - var1 - this.snappedRightInset();
         double var7 = this.getHeight() - var3 - this.snappedBottomInset();
         double var9 = this.snapSize(this.getContentHeight());
         this.itemsContainer.resize(var5, var9);
         this.itemsContainer.relocate(var1, var3);
         if (this.isFirstShow && this.ty == 0.0) {
            this.upArrow.setVisible(false);
            this.isFirstShow = false;
         } else {
            this.upArrow.setVisible(this.ty < var3 && this.ty < 0.0);
         }

         this.downArrow.setVisible(this.ty + var9 > var3 + var7);
         this.clipRect.setX(0.0);
         this.clipRect.setY(0.0);
         this.clipRect.setWidth(var5);
         this.clipRect.setHeight(var7);
         double var11;
         if (this.upArrow.isVisible()) {
            var11 = this.snapSize(this.upArrow.prefHeight(-1.0));
            this.clipRect.setHeight(this.snapSize(this.clipRect.getHeight() - var11));
            this.clipRect.setY(this.snapSize(this.clipRect.getY()) + var11);
            this.upArrow.resize(this.snapSize(this.upArrow.prefWidth(-1.0)), var11);
            this.positionInArea(this.upArrow, var1, var3, var5, var11, 0.0, HPos.CENTER, VPos.CENTER);
         }

         if (this.downArrow.isVisible()) {
            var11 = this.snapSize(this.downArrow.prefHeight(-1.0));
            this.clipRect.setHeight(this.snapSize(this.clipRect.getHeight()) - var11);
            this.downArrow.resize(this.snapSize(this.downArrow.prefWidth(-1.0)), var11);
            this.positionInArea(this.downArrow, var1, var3 + var7 - var11, var5, var11, 0.0, HPos.CENTER, VPos.CENTER);
         }

      }
   }

   protected double computePrefWidth(double var1) {
      this.computeVisualMetrics();
      double var3 = 0.0;
      if (this.itemsContainer.getChildren().size() == 0) {
         return 0.0;
      } else {
         Iterator var5 = this.itemsContainer.getChildren().iterator();

         while(var5.hasNext()) {
            Node var6 = (Node)var5.next();
            if (var6.isVisible()) {
               var3 = Math.max(var3, this.snapSize(var6.prefWidth(-1.0)));
            }
         }

         return this.snappedLeftInset() + this.snapSize(var3) + this.snappedRightInset();
      }
   }

   protected double computePrefHeight(double var1) {
      if (this.itemsContainer.getChildren().size() == 0) {
         return 0.0;
      } else {
         double var3 = this.getScreenHeight();
         double var5 = this.getContentHeight();
         double var7 = this.snappedTopInset() + this.snapSize(var5) + this.snappedBottomInset();
         double var9 = var3 <= 0.0 ? var7 : Math.min(var7, var3);
         return var9;
      }
   }

   protected double computeMinHeight(double var1) {
      return 0.0;
   }

   protected double computeMaxHeight(double var1) {
      return this.getScreenHeight();
   }

   private double getScreenHeight() {
      return this.contextMenu != null && this.contextMenu.getOwnerWindow() != null && this.contextMenu.getOwnerWindow().getScene() != null ? this.snapSize(com.sun.javafx.util.Utils.getScreen(this.contextMenu.getOwnerWindow().getScene().getRoot()).getVisualBounds().getHeight()) : -1.0;
   }

   private double getContentHeight() {
      double var1 = 0.0;
      Iterator var3 = this.itemsContainer.getChildren().iterator();

      while(var3.hasNext()) {
         Node var4 = (Node)var3.next();
         if (var4.isVisible()) {
            var1 += this.snapSize(var4.prefHeight(-1.0));
         }
      }

      return var1;
   }

   private void ensureFocusedMenuItemIsVisible(Node var1) {
      if (var1 != null) {
         Bounds var2 = var1.getBoundsInParent();
         Bounds var3 = this.clipRect.getBoundsInParent();
         if (var2.getMaxY() >= var3.getMaxY()) {
            this.scroll(-var2.getMaxY() + var3.getMaxY());
         } else if (var2.getMinY() <= var3.getMinY()) {
            this.scroll(-var2.getMinY() + var3.getMinY());
         }

      }
   }

   protected ObservableList getItems() {
      return this.contextMenu.getItems();
   }

   private int findFocusedIndex() {
      for(int var1 = 0; var1 < this.itemsContainer.getChildren().size(); ++var1) {
         Node var2 = (Node)this.itemsContainer.getChildren().get(var1);
         if (var2.isFocused()) {
            return var1;
         }
      }

      return -1;
   }

   private void initialize() {
      this.contextMenu.focusedProperty().addListener((var1, var2, var3) -> {
         if (var3) {
            this.currentFocusedIndex = -1;
            this.requestFocus();
         }

      });
      this.contextMenu.addEventHandler(Menu.ON_SHOWN, (var1) -> {
         Iterator var2 = this.itemsContainer.getChildren().iterator();

         while(var2.hasNext()) {
            Node var3 = (Node)var2.next();
            if (var3 instanceof MenuItemContainer) {
               MenuItem var4 = ((MenuItemContainer)var3).item;
               if ("choice-box-menu-item".equals(var4.getId()) && ((RadioMenuItem)var4).isSelected()) {
                  var3.requestFocus();
                  break;
               }
            }
         }

      });
      this.setOnKeyPressed(new EventHandler() {
         public void handle(KeyEvent var1) {
            Node var2;
            switch (var1.getCode()) {
               case LEFT:
                  if (ContextMenuContent.this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                     ContextMenuContent.this.processRightKey(var1);
                  } else {
                     ContextMenuContent.this.processLeftKey(var1);
                  }
                  break;
               case RIGHT:
                  if (ContextMenuContent.this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                     ContextMenuContent.this.processLeftKey(var1);
                  } else {
                     ContextMenuContent.this.processRightKey(var1);
                  }
                  break;
               case CANCEL:
                  var1.consume();
                  break;
               case ESCAPE:
                  var2 = ContextMenuContent.this.contextMenu.getOwnerNode();
                  if (!(var2 instanceof MenuBarSkin.MenuBarButton)) {
                     ContextMenuContent.this.contextMenu.hide();
                     var1.consume();
                  }
                  break;
               case DOWN:
                  ContextMenuContent.this.moveToNextSibling();
                  var1.consume();
                  break;
               case UP:
                  ContextMenuContent.this.moveToPreviousSibling();
                  var1.consume();
                  break;
               case SPACE:
               case ENTER:
                  ContextMenuContent.this.selectMenuItem();
                  var1.consume();
            }

            if (!var1.isConsumed()) {
               var2 = ContextMenuContent.this.contextMenu.getOwnerNode();
               if (var2 instanceof MenuItemContainer) {
                  Parent var3;
                  for(var3 = var2.getParent(); var3 != null && !(var3 instanceof ContextMenuContent); var3 = var3.getParent()) {
                  }

                  if (var3 instanceof ContextMenuContent) {
                     var3.getOnKeyPressed().handle(var1);
                  }
               } else if (var2 instanceof MenuBarSkin.MenuBarButton) {
                  MenuBarSkin var4 = ((MenuBarSkin.MenuBarButton)var2).getMenuBarSkin();
                  if (var4 != null && var4.getKeyEventHandler() != null) {
                     var4.getKeyEventHandler().handle(var1);
                  }
               }
            }

         }
      });
      this.addEventHandler(ScrollEvent.SCROLL, (var1) -> {
         double var2 = var1.getTextDeltaY();
         double var4 = var1.getDeltaY();
         if (this.downArrow.isVisible() && (var2 < 0.0 || var4 < 0.0) || this.upArrow.isVisible() && (var2 > 0.0 || var4 > 0.0)) {
            switch (var1.getTextDeltaYUnits()) {
               case LINES:
                  int var6 = this.findFocusedIndex();
                  if (var6 == -1) {
                     var6 = 0;
                  }

                  double var7 = ((Node)this.itemsContainer.getChildren().get(var6)).prefHeight(-1.0);
                  this.scroll(var2 * var7);
                  break;
               case PAGES:
                  this.scroll(var2 * this.itemsContainer.getHeight());
                  break;
               case NONE:
                  this.scroll(var4);
            }

            var1.consume();
         }

      });
   }

   private void processLeftKey(KeyEvent var1) {
      if (this.currentFocusedIndex != -1) {
         Node var2 = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex);
         if (var2 instanceof MenuItemContainer) {
            MenuItem var3 = ((MenuItemContainer)var2).item;
            if (var3 instanceof Menu) {
               Menu var4 = (Menu)var3;
               if (var4 == this.openSubmenu && this.submenu != null && this.submenu.isShowing()) {
                  this.hideSubmenu();
                  var1.consume();
               }
            }
         }
      }

   }

   private void processRightKey(KeyEvent var1) {
      if (this.currentFocusedIndex != -1) {
         Node var2 = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex);
         if (var2 instanceof MenuItemContainer) {
            MenuItem var3 = ((MenuItemContainer)var2).item;
            if (var3 instanceof Menu) {
               Menu var4 = (Menu)var3;
               if (var4.isDisable()) {
                  return;
               }

               this.selectedBackground = (MenuItemContainer)var2;
               if (this.openSubmenu == var4 && this.submenu != null && this.submenu.isShowing()) {
                  return;
               }

               this.showMenu(var4);
               var1.consume();
            }
         }
      }

   }

   private void showMenu(Menu var1) {
      var1.show();
      ContextMenuContent var2 = (ContextMenuContent)this.submenu.getSkin().getNode();
      if (var2 != null) {
         if (var2.itemsContainer.getChildren().size() > 0) {
            ((Node)var2.itemsContainer.getChildren().get(0)).requestFocus();
            var2.currentFocusedIndex = 0;
         } else {
            var2.requestFocus();
         }
      }

   }

   private void selectMenuItem() {
      if (this.currentFocusedIndex != -1) {
         Node var1 = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex);
         if (var1 instanceof MenuItemContainer) {
            MenuItem var2 = ((MenuItemContainer)var1).item;
            if (var2 instanceof Menu) {
               Menu var3 = (Menu)var2;
               if (this.openSubmenu != null) {
                  this.hideSubmenu();
               }

               if (var3.isDisable()) {
                  return;
               }

               this.selectedBackground = (MenuItemContainer)var1;
               var3.show();
            } else {
               ((MenuItemContainer)var1).doSelect();
            }
         }
      }

   }

   private int findNext(int var1) {
      int var2;
      Node var3;
      for(var2 = var1; var2 < this.itemsContainer.getChildren().size(); ++var2) {
         var3 = (Node)this.itemsContainer.getChildren().get(var2);
         if (var3 instanceof MenuItemContainer) {
            return var2;
         }
      }

      for(var2 = 0; var2 < var1; ++var2) {
         var3 = (Node)this.itemsContainer.getChildren().get(var2);
         if (var3 instanceof MenuItemContainer) {
            return var2;
         }
      }

      return -1;
   }

   private void moveToNextSibling() {
      if (this.currentFocusedIndex != -1) {
         this.currentFocusedIndex = this.findNext(this.currentFocusedIndex + 1);
      } else if (this.currentFocusedIndex == -1 || this.currentFocusedIndex == this.itemsContainer.getChildren().size() - 1) {
         this.currentFocusedIndex = this.findNext(0);
      }

      if (this.currentFocusedIndex != -1) {
         Node var1 = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex);
         this.selectedBackground = (MenuItemContainer)var1;
         var1.requestFocus();
         this.ensureFocusedMenuItemIsVisible(var1);
      }

   }

   private int findPrevious(int var1) {
      int var2;
      Node var3;
      for(var2 = var1; var2 >= 0; --var2) {
         var3 = (Node)this.itemsContainer.getChildren().get(var2);
         if (var3 instanceof MenuItemContainer) {
            return var2;
         }
      }

      for(var2 = this.itemsContainer.getChildren().size() - 1; var2 > var1; --var2) {
         var3 = (Node)this.itemsContainer.getChildren().get(var2);
         if (var3 instanceof MenuItemContainer) {
            return var2;
         }
      }

      return -1;
   }

   private void moveToPreviousSibling() {
      if (this.currentFocusedIndex != -1) {
         this.currentFocusedIndex = this.findPrevious(this.currentFocusedIndex - 1);
      } else if (this.currentFocusedIndex == -1 || this.currentFocusedIndex == 0) {
         this.currentFocusedIndex = this.findPrevious(this.itemsContainer.getChildren().size() - 1);
      }

      if (this.currentFocusedIndex != -1) {
         Node var1 = (Node)this.itemsContainer.getChildren().get(this.currentFocusedIndex);
         this.selectedBackground = (MenuItemContainer)var1;
         var1.requestFocus();
         this.ensureFocusedMenuItemIsVisible(var1);
      }

   }

   double getMenuYOffset(int var1) {
      double var2 = 0.0;
      if (this.itemsContainer.getChildren().size() > var1) {
         var2 = this.snappedTopInset();
         Node var4 = (Node)this.itemsContainer.getChildren().get(var1);
         var2 += var4.getLayoutY() + var4.prefHeight(-1.0);
      }

      return var2;
   }

   private void setUpBinds() {
      this.updateMenuShowingListeners(this.contextMenu.getItems(), true);
      this.contextMenu.getItems().addListener(this.contextMenuItemsListener);
   }

   private void disposeBinds() {
      this.updateMenuShowingListeners(this.contextMenu.getItems(), false);
      this.contextMenu.getItems().removeListener(this.contextMenuItemsListener);
   }

   private void updateMenuShowingListeners(List var1, boolean var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         MenuItem var4 = (MenuItem)var3.next();
         if (var4 instanceof Menu) {
            Menu var5 = (Menu)var4;
            if (var2) {
               var5.showingProperty().addListener(this.menuShowingListener);
            } else {
               var5.showingProperty().removeListener(this.menuShowingListener);
            }
         }

         if (var2) {
            var4.visibleProperty().addListener(this.menuItemVisibleListener);
         } else {
            var4.visibleProperty().removeListener(this.menuItemVisibleListener);
         }
      }

   }

   ContextMenu getSubMenu() {
      return this.submenu;
   }

   Menu getOpenSubMenu() {
      return this.openSubmenu;
   }

   private void createSubmenu() {
      if (this.submenu == null) {
         this.submenu = new ContextMenu();
         this.submenu.showingProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue var1, Boolean var2, Boolean var3) {
               if (!ContextMenuContent.this.submenu.isShowing()) {
                  Iterator var4 = ContextMenuContent.this.itemsContainer.getChildren().iterator();

                  while(var4.hasNext()) {
                     Node var5 = (Node)var4.next();
                     if (var5 instanceof MenuItemContainer && ((MenuItemContainer)var5).item instanceof Menu) {
                        Menu var6 = (Menu)((MenuItemContainer)var5).item;
                        if (var6.isShowing()) {
                           var6.hide();
                        }
                     }
                  }
               }

            }
         });
      }

   }

   private void showSubmenu(Menu var1) {
      this.openSubmenu = var1;
      this.createSubmenu();
      this.submenu.getItems().setAll((Collection)var1.getItems());
      this.submenu.show(this.selectedBackground, Side.RIGHT, 0.0, 0.0);
   }

   private void hideSubmenu() {
      if (this.submenu != null) {
         this.submenu.hide();
         this.openSubmenu = null;
         this.disposeContextMenu(this.submenu);
         this.submenu = null;
      }
   }

   private void hideAllMenus(MenuItem var1) {
      if (this.contextMenu != null) {
         this.contextMenu.hide();
      }

      Menu var2;
      while((var2 = ((MenuItem)var1).getParentMenu()) != null) {
         var2.hide();
         var1 = var2;
      }

      if (((MenuItem)var1).getParentPopup() != null) {
         ((MenuItem)var1).getParentPopup().hide();
      }

   }

   void scroll(double var1) {
      double var3 = this.ty + var1;
      if (this.ty != var3) {
         if (var3 > 0.0) {
            var3 = 0.0;
         }

         if (var1 < 0.0 && this.getHeight() - var3 > this.itemsContainer.getHeight() - this.downArrow.getHeight()) {
            var3 = this.getHeight() - this.itemsContainer.getHeight() - this.downArrow.getHeight();
         }

         this.ty = var3;
         this.itemsContainer.requestLayout();
      }
   }

   public Styleable getStyleableParent() {
      return this.contextMenu;
   }

   public static List getClassCssMetaData() {
      return ContextMenuContent.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   protected Label getLabelAt(int var1) {
      return ((MenuItemContainer)this.itemsContainer.getChildren().get(var1)).getLabel();
   }

   private class MenuLabel extends Label {
      public MenuLabel(MenuItem var2, MenuItemContainer var3) {
         super(var2.getText());
         this.setMnemonicParsing(var2.isMnemonicParsing());
         this.setLabelFor(var3);
      }
   }

   public class MenuItemContainer extends Region {
      private final MenuItem item;
      private Node left;
      private Node graphic;
      private Node label;
      private Node right;
      private final MultiplePropertyChangeListenerHandler listener = new MultiplePropertyChangeListenerHandler((var1x) -> {
         this.handlePropertyChanged(var1x);
         return null;
      });
      private EventHandler mouseEnteredEventHandler;
      private EventHandler mouseReleasedEventHandler;
      private EventHandler actionEventHandler;
      private EventHandler customMenuItemMouseClickedHandler;

      protected Label getLabel() {
         return (Label)this.label;
      }

      public MenuItem getItem() {
         return this.item;
      }

      public MenuItemContainer(MenuItem var2) {
         if (var2 == null) {
            throw new NullPointerException("MenuItem can not be null");
         } else {
            this.getStyleClass().addAll(var2.getStyleClass());
            this.setId(var2.getId());
            this.setFocusTraversable(!(var2 instanceof CustomMenuItem));
            this.item = var2;
            this.createChildren();
            if (var2 instanceof Menu) {
               ReadOnlyBooleanProperty var3 = ((Menu)var2).showingProperty();
               this.listener.registerChangeListener(var3, "MENU_SHOWING");
               this.pseudoClassStateChanged(ContextMenuContent.SELECTED_PSEUDOCLASS_STATE, var3.get());
               this.setAccessibleRole(AccessibleRole.MENU);
            } else {
               BooleanProperty var4;
               if (var2 instanceof RadioMenuItem) {
                  var4 = ((RadioMenuItem)var2).selectedProperty();
                  this.listener.registerChangeListener(var4, "RADIO_ITEM_SELECTED");
                  this.pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, var4.get());
                  this.setAccessibleRole(AccessibleRole.RADIO_MENU_ITEM);
               } else if (var2 instanceof CheckMenuItem) {
                  var4 = ((CheckMenuItem)var2).selectedProperty();
                  this.listener.registerChangeListener(var4, "CHECK_ITEM_SELECTED");
                  this.pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, var4.get());
                  this.setAccessibleRole(AccessibleRole.CHECK_MENU_ITEM);
               } else {
                  this.setAccessibleRole(AccessibleRole.MENU_ITEM);
               }
            }

            this.pseudoClassStateChanged(ContextMenuContent.DISABLED_PSEUDOCLASS_STATE, var2.disableProperty().get());
            this.listener.registerChangeListener(var2.disableProperty(), "DISABLE");
            this.getProperties().put(MenuItem.class, var2);
            this.listener.registerChangeListener(var2.graphicProperty(), "GRAPHIC");
            this.actionEventHandler = (var2x) -> {
               if (var2 instanceof Menu) {
                  Menu var3 = (Menu)var2;
                  if (ContextMenuContent.this.openSubmenu == var3 && ContextMenuContent.this.submenu.isShowing()) {
                     return;
                  }

                  if (ContextMenuContent.this.openSubmenu != null) {
                     ContextMenuContent.this.hideSubmenu();
                  }

                  ContextMenuContent.this.selectedBackground = this;
                  ContextMenuContent.this.showMenu(var3);
               } else {
                  this.doSelect();
               }

            };
            this.addEventHandler(ActionEvent.ACTION, this.actionEventHandler);
         }
      }

      public void dispose() {
         if (this.item instanceof CustomMenuItem) {
            Node var1 = ((CustomMenuItem)this.item).getContent();
            if (var1 != null) {
               var1.removeEventHandler(MouseEvent.MOUSE_CLICKED, this.customMenuItemMouseClickedHandler);
            }
         }

         this.listener.dispose();
         this.removeEventHandler(ActionEvent.ACTION, this.actionEventHandler);
         if (this.label != null) {
            ((Label)this.label).textProperty().unbind();
         }

         this.left = null;
         this.graphic = null;
         this.label = null;
         this.right = null;
      }

      private void handlePropertyChanged(String var1) {
         if ("MENU_SHOWING".equals(var1)) {
            Menu var2 = (Menu)this.item;
            this.pseudoClassStateChanged(ContextMenuContent.SELECTED_PSEUDOCLASS_STATE, var2.isShowing());
         } else if ("RADIO_ITEM_SELECTED".equals(var1)) {
            RadioMenuItem var3 = (RadioMenuItem)this.item;
            this.pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, var3.isSelected());
         } else if ("CHECK_ITEM_SELECTED".equals(var1)) {
            CheckMenuItem var4 = (CheckMenuItem)this.item;
            this.pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, var4.isSelected());
         } else if ("DISABLE".equals(var1)) {
            this.pseudoClassStateChanged(ContextMenuContent.DISABLED_PSEUDOCLASS_STATE, this.item.isDisable());
         } else if ("GRAPHIC".equals(var1)) {
            this.createChildren();
            ContextMenuContent.this.computeVisualMetrics();
         } else if ("ACCELERATOR".equals(var1)) {
            this.updateAccelerator();
         } else if ("FOCUSED".equals(var1) && this.isFocused()) {
            ContextMenuContent.this.currentFocusedIndex = ContextMenuContent.this.itemsContainer.getChildren().indexOf(this);
         }

      }

      private void createChildren() {
         this.getChildren().clear();
         if (this.item instanceof CustomMenuItem) {
            this.createNodeMenuItemChildren((CustomMenuItem)this.item);
            if (this.mouseEnteredEventHandler == null) {
               this.mouseEnteredEventHandler = (var1x) -> {
                  this.requestFocus();
               };
            } else {
               this.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
            }

            this.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
         } else {
            Node var1 = this.getLeftGraphic(this.item);
            if (var1 != null) {
               StackPane var2 = new StackPane();
               var2.getStyleClass().add("left-container");
               var2.getChildren().add(var1);
               this.left = var2;
               this.getChildren().add(this.left);
               this.left.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            }

            StackPane var3;
            if (this.item.getGraphic() != null) {
               Node var4 = this.item.getGraphic();
               var3 = new StackPane();
               var3.getStyleClass().add("graphic-container");
               var3.getChildren().add(var4);
               this.graphic = var3;
               this.getChildren().add(this.graphic);
            }

            this.label = ContextMenuContent.this.new MenuLabel(this.item, this);
            this.label.setStyle(this.item.getStyle());
            ((Label)this.label).textProperty().bind(this.item.textProperty());
            this.label.setMouseTransparent(true);
            this.getChildren().add(this.label);
            this.listener.unregisterChangeListener(this.focusedProperty());
            this.listener.registerChangeListener(this.focusedProperty(), "FOCUSED");
            if (this.item instanceof Menu) {
               Region var5 = new Region();
               var5.setMouseTransparent(true);
               var5.getStyleClass().add("arrow");
               var3 = new StackPane();
               var3.setMaxWidth(Math.max(var5.prefWidth(-1.0), 10.0));
               var3.setMouseTransparent(true);
               var3.getStyleClass().add("right-container");
               var3.getChildren().add(var5);
               this.right = var3;
               this.getChildren().add(var3);
               if (this.mouseEnteredEventHandler == null) {
                  this.mouseEnteredEventHandler = (var1x) -> {
                     if (ContextMenuContent.this.openSubmenu != null && this.item != ContextMenuContent.this.openSubmenu) {
                        ContextMenuContent.this.hideSubmenu();
                     }

                     Menu var2 = (Menu)this.item;
                     if (!var2.isDisable()) {
                        ContextMenuContent.this.selectedBackground = this;
                        var2.show();
                        this.requestFocus();
                     }
                  };
               } else {
                  this.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
               }

               if (this.mouseReleasedEventHandler == null) {
                  this.mouseReleasedEventHandler = (var1x) -> {
                     this.item.fire();
                  };
               } else {
                  this.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
               }

               this.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
               this.addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
            } else {
               this.listener.unregisterChangeListener(this.item.acceleratorProperty());
               this.updateAccelerator();
               if (this.mouseEnteredEventHandler == null) {
                  this.mouseEnteredEventHandler = (var1x) -> {
                     if (ContextMenuContent.this.openSubmenu != null) {
                        ContextMenuContent.this.openSubmenu.hide();
                     }

                     this.requestFocus();
                  };
               } else {
                  this.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
               }

               if (this.mouseReleasedEventHandler == null) {
                  this.mouseReleasedEventHandler = (var1x) -> {
                     this.doSelect();
                  };
               } else {
                  this.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
               }

               this.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
               this.addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
               this.listener.registerChangeListener(this.item.acceleratorProperty(), "ACCELERATOR");
            }
         }

      }

      private void updateAccelerator() {
         if (this.item.getAccelerator() != null) {
            if (this.right != null) {
               this.getChildren().remove(this.right);
            }

            String var1 = this.item.getAccelerator().getDisplayText();
            this.right = new Label(var1);
            this.right.setStyle(this.item.getStyle());
            this.right.getStyleClass().add("accelerator-text");
            this.getChildren().add(this.right);
         } else {
            this.getChildren().remove(this.right);
         }

      }

      void doSelect() {
         if (!this.item.isDisable()) {
            if (this.item instanceof CheckMenuItem) {
               CheckMenuItem var1 = (CheckMenuItem)this.item;
               var1.setSelected(!var1.isSelected());
            } else if (this.item instanceof RadioMenuItem) {
               RadioMenuItem var2 = (RadioMenuItem)this.item;
               var2.setSelected(var2.getToggleGroup() != null ? true : !var2.isSelected());
            }

            this.item.fire();
            if (this.item instanceof CustomMenuItem) {
               CustomMenuItem var3 = (CustomMenuItem)this.item;
               if (var3.isHideOnClick()) {
                  ContextMenuContent.this.hideAllMenus(this.item);
               }
            } else {
               ContextMenuContent.this.hideAllMenus(this.item);
            }

         }
      }

      private void createNodeMenuItemChildren(CustomMenuItem var1) {
         Node var2 = var1.getContent();
         this.getChildren().add(var2);
         this.customMenuItemMouseClickedHandler = (var2x) -> {
            if (var1 != null && !var1.isDisable()) {
               var1.fire();
               if (var1.isHideOnClick()) {
                  ContextMenuContent.this.hideAllMenus(var1);
               }

            }
         };
         var2.addEventHandler(MouseEvent.MOUSE_CLICKED, this.customMenuItemMouseClickedHandler);
      }

      protected void layoutChildren() {
         double var3 = this.prefHeight(-1.0);
         double var1;
         if (this.left != null) {
            var1 = this.snappedLeftInset();
            this.left.resize(this.left.prefWidth(-1.0), this.left.prefHeight(-1.0));
            this.positionInArea(this.left, var1, 0.0, ContextMenuContent.this.maxLeftWidth, var3, 0.0, HPos.LEFT, VPos.CENTER);
         }

         if (this.graphic != null) {
            var1 = this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth;
            this.graphic.resize(this.graphic.prefWidth(-1.0), this.graphic.prefHeight(-1.0));
            this.positionInArea(this.graphic, var1, 0.0, ContextMenuContent.this.maxGraphicWidth, var3, 0.0, HPos.LEFT, VPos.CENTER);
         }

         if (this.label != null) {
            var1 = this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth;
            this.label.resize(this.label.prefWidth(-1.0), this.label.prefHeight(-1.0));
            this.positionInArea(this.label, var1, 0.0, ContextMenuContent.this.maxLabelWidth, var3, 0.0, HPos.LEFT, VPos.CENTER);
         }

         if (this.right != null) {
            var1 = this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth + ContextMenuContent.this.maxLabelWidth;
            this.right.resize(this.right.prefWidth(-1.0), this.right.prefHeight(-1.0));
            this.positionInArea(this.right, var1, 0.0, ContextMenuContent.this.maxRightWidth, var3, 0.0, HPos.RIGHT, VPos.CENTER);
         }

         if (this.item instanceof CustomMenuItem) {
            Node var5 = ((CustomMenuItem)this.item).getContent();
            if (this.item instanceof SeparatorMenuItem) {
               double var6 = this.prefWidth(-1.0) - (this.snappedLeftInset() + ContextMenuContent.this.maxGraphicWidth + this.snappedRightInset());
               var5.resize(var6, var5.prefHeight(-1.0));
               this.positionInArea(var5, this.snappedLeftInset() + ContextMenuContent.this.maxGraphicWidth, 0.0, this.prefWidth(-1.0), var3, 0.0, HPos.LEFT, VPos.CENTER);
            } else {
               var5.resize(var5.prefWidth(-1.0), var5.prefHeight(-1.0));
               this.positionInArea(var5, this.snappedLeftInset(), 0.0, this.getWidth(), var3, 0.0, HPos.LEFT, VPos.CENTER);
            }
         }

      }

      protected double computePrefHeight(double var1) {
         double var3 = 0.0;
         if (!(this.item instanceof CustomMenuItem) && !(this.item instanceof SeparatorMenuItem)) {
            var3 = Math.max(var3, this.left != null ? this.left.prefHeight(-1.0) : 0.0);
            var3 = Math.max(var3, this.graphic != null ? this.graphic.prefHeight(-1.0) : 0.0);
            var3 = Math.max(var3, this.label != null ? this.label.prefHeight(-1.0) : 0.0);
            var3 = Math.max(var3, this.right != null ? this.right.prefHeight(-1.0) : 0.0);
         } else {
            var3 = this.getChildren().isEmpty() ? 0.0 : ((Node)this.getChildren().get(0)).prefHeight(-1.0);
         }

         return this.snappedTopInset() + var3 + this.snappedBottomInset();
      }

      protected double computePrefWidth(double var1) {
         double var3 = 0.0;
         if (this.item instanceof CustomMenuItem && !(this.item instanceof SeparatorMenuItem)) {
            var3 = this.snappedLeftInset() + ((CustomMenuItem)this.item).getContent().prefWidth(-1.0) + this.snappedRightInset();
         }

         return Math.max(var3, this.snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth + ContextMenuContent.this.maxLabelWidth + ContextMenuContent.this.maxRightWidth + this.snappedRightInset());
      }

      private Node getLeftGraphic(MenuItem var1) {
         if (var1 instanceof RadioMenuItem) {
            Region var3 = new Region();
            var3.getStyleClass().add("radio");
            return var3;
         } else if (var1 instanceof CheckMenuItem) {
            StackPane var2 = new StackPane();
            var2.getStyleClass().add("check");
            return var2;
         } else {
            return null;
         }
      }

      public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
         String var4;
         switch (var1) {
            case SELECTED:
               if (this.item instanceof CheckMenuItem) {
                  return ((CheckMenuItem)this.item).isSelected();
               } else {
                  if (this.item instanceof RadioMenuItem) {
                     return ((RadioMenuItem)this.item).isSelected();
                  }

                  return false;
               }
            case ACCELERATOR:
               return this.item.getAccelerator();
            case TEXT:
               String var8 = "";
               if (this.graphic != null) {
                  var4 = (String)this.graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT);
                  if (var4 != null) {
                     var8 = var8 + var4;
                  }
               }

               Label var9 = this.getLabel();
               if (var9 != null) {
                  String var5 = (String)var9.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0]);
                  if (var5 != null) {
                     var8 = var8 + var5;
                  }
               }

               if (this.item instanceof CustomMenuItem) {
                  Node var10 = ((CustomMenuItem)this.item).getContent();
                  if (var10 != null) {
                     String var6 = (String)var10.queryAccessibleAttribute(AccessibleAttribute.TEXT);
                     if (var6 != null) {
                        var8 = var8 + var6;
                     }
                  }
               }

               return var8;
            case MNEMONIC:
               Label var7 = this.getLabel();
               if (var7 != null) {
                  var4 = (String)var7.queryAccessibleAttribute(AccessibleAttribute.MNEMONIC, new Object[0]);
                  if (var4 != null) {
                     return var4;
                  }
               }

               return null;
            case DISABLED:
               return this.item.isDisable();
            case SUBMENU:
               ContextMenuContent.this.createSubmenu();
               if (ContextMenuContent.this.submenu.getSkin() == null) {
                  ContextMenuContent.this.submenu.impl_styleableGetNode().impl_processCSS(true);
               }

               ContextMenuContent var3 = (ContextMenuContent)ContextMenuContent.this.submenu.getSkin().getNode();
               return var3.itemsContainer;
            default:
               return super.queryAccessibleAttribute(var1, var2);
         }
      }

      public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
         switch (var1) {
            case SHOW_MENU:
               if (this.item instanceof Menu) {
                  Menu var3 = (Menu)this.item;
                  if (var3.isShowing()) {
                     var3.hide();
                  } else {
                     var3.show();
                  }
               }
               break;
            case FIRE:
               this.doSelect();
               break;
            default:
               super.executeAccessibleAction(var1, new Object[0]);
         }

      }
   }

   class ArrowMenuItem extends StackPane {
      private StackPane upDownArrow;
      private ContextMenuContent popupMenuContent;
      private boolean up = false;
      private Timeline scrollTimeline;

      public final boolean isUp() {
         return this.up;
      }

      public void setUp(boolean var1) {
         this.up = var1;
         this.upDownArrow.getStyleClass().setAll((Object[])(this.isUp() ? "menu-up-arrow" : "menu-down-arrow"));
      }

      public ArrowMenuItem(ContextMenuContent var2) {
         this.getStyleClass().setAll((Object[])("scroll-arrow"));
         this.upDownArrow = new StackPane();
         this.popupMenuContent = var2;
         this.upDownArrow.setMouseTransparent(true);
         this.upDownArrow.getStyleClass().setAll((Object[])(this.isUp() ? "menu-up-arrow" : "menu-down-arrow"));
         this.addEventHandler(MouseEvent.MOUSE_ENTERED, (var1x) -> {
            if (this.scrollTimeline == null || this.scrollTimeline.getStatus() == Animation.Status.STOPPED) {
               this.startTimeline();
            }
         });
         this.addEventHandler(MouseEvent.MOUSE_EXITED, (var1x) -> {
            this.stopTimeline();
         });
         this.setVisible(false);
         this.setManaged(false);
         this.getChildren().add(this.upDownArrow);
      }

      protected double computePrefWidth(double var1) {
         return ContextMenuContent.this.itemsContainer.getWidth();
      }

      protected double computePrefHeight(double var1) {
         return this.snappedTopInset() + this.upDownArrow.prefHeight(-1.0) + this.snappedBottomInset();
      }

      protected void layoutChildren() {
         double var1 = this.snapSize(this.upDownArrow.prefWidth(-1.0));
         double var3 = this.snapSize(this.upDownArrow.prefHeight(-1.0));
         this.upDownArrow.resize(var1, var3);
         this.positionInArea(this.upDownArrow, 0.0, 0.0, this.getWidth(), this.getHeight(), 0.0, HPos.CENTER, VPos.CENTER);
      }

      private void adjust() {
         if (this.up) {
            this.popupMenuContent.scroll(12.0);
         } else {
            this.popupMenuContent.scroll(-12.0);
         }

      }

      private void startTimeline() {
         this.scrollTimeline = new Timeline();
         this.scrollTimeline.setCycleCount(-1);
         KeyFrame var1 = new KeyFrame(Duration.millis(60.0), (var1x) -> {
            this.adjust();
         }, new KeyValue[0]);
         this.scrollTimeline.getKeyFrames().clear();
         this.scrollTimeline.getKeyFrames().add(var1);
         this.scrollTimeline.play();
      }

      private void stopTimeline() {
         this.scrollTimeline.stop();
         this.scrollTimeline = null;
      }
   }

   class MenuBox extends VBox {
      MenuBox() {
         this.setAccessibleRole(AccessibleRole.CONTEXT_MENU);
      }

      protected void layoutChildren() {
         double var1 = ContextMenuContent.this.ty;
         Iterator var3 = this.getChildren().iterator();

         while(var3.hasNext()) {
            Node var4 = (Node)var3.next();
            if (var4.isVisible()) {
               double var5 = this.snapSize(var4.prefHeight(-1.0));
               var4.resize(this.snapSize(this.getWidth()), var5);
               var4.relocate(this.snappedLeftInset(), var1);
               var1 += var5;
            }
         }

      }

      public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
         switch (var1) {
            case VISIBLE:
               return ContextMenuContent.this.contextMenu.isShowing();
            case PARENT_MENU:
               return ContextMenuContent.this.contextMenu.getOwnerNode();
            default:
               return super.queryAccessibleAttribute(var1, var2);
         }
      }
   }

   private static class StyleableProperties {
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         List var1 = Node.getClassCssMetaData();
         int var2 = 0;

         for(int var3 = var1.size(); var2 < var3; ++var2) {
            CssMetaData var4 = (CssMetaData)var1.get(var2);
            if ("effect".equals(var4.getProperty())) {
               var0.add(var4);
               break;
            }
         }

         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
