package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalEngine;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class TabPaneSkin extends BehaviorSkinBase {
   private ObjectProperty openTabAnimation;
   private ObjectProperty closeTabAnimation;
   private static final double ANIMATION_SPEED = 150.0;
   private static final int SPACER = 10;
   private TabHeaderArea tabHeaderArea;
   private ObservableList tabContentRegions;
   private Rectangle clipRect;
   private Rectangle tabHeaderAreaClipRect;
   private Tab selectedTab;
   private boolean isSelectingTab;
   private double maxw;
   private double maxh;
   static int CLOSE_BTN_SIZE = 16;
   private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("selected");
   private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("top");
   private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("bottom");
   private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("left");
   private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("right");
   private static final PseudoClass DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("disabled");

   private static int getRotation(Side var0) {
      switch (var0) {
         case TOP:
            return 0;
         case BOTTOM:
            return 180;
         case LEFT:
            return -90;
         case RIGHT:
            return 90;
         default:
            return 0;
      }
   }

   private static Node clone(Node var0) {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof ImageView) {
         ImageView var3 = (ImageView)var0;
         ImageView var4 = new ImageView();
         var4.setImage(var3.getImage());
         return var4;
      } else if (var0 instanceof Label) {
         Label var1 = (Label)var0;
         Label var2 = new Label(var1.getText(), var1.getGraphic());
         return var2;
      } else {
         return null;
      }
   }

   public TabPaneSkin(TabPane var1) {
      super(var1, new TabPaneBehavior(var1));
      this.openTabAnimation = new StyleableObjectProperty(TabPaneSkin.TabAnimation.GROW) {
         public CssMetaData getCssMetaData() {
            return TabPaneSkin.StyleableProperties.OPEN_TAB_ANIMATION;
         }

         public Object getBean() {
            return TabPaneSkin.this;
         }

         public String getName() {
            return "openTabAnimation";
         }
      };
      this.closeTabAnimation = new StyleableObjectProperty(TabPaneSkin.TabAnimation.GROW) {
         public CssMetaData getCssMetaData() {
            return TabPaneSkin.StyleableProperties.CLOSE_TAB_ANIMATION;
         }

         public Object getBean() {
            return TabPaneSkin.this;
         }

         public String getName() {
            return "closeTabAnimation";
         }
      };
      this.maxw = 0.0;
      this.maxh = 0.0;
      this.clipRect = new Rectangle(var1.getWidth(), var1.getHeight());
      ((TabPane)this.getSkinnable()).setClip(this.clipRect);
      this.tabContentRegions = FXCollections.observableArrayList();
      Iterator var2 = ((TabPane)this.getSkinnable()).getTabs().iterator();

      while(var2.hasNext()) {
         Tab var3 = (Tab)var2.next();
         this.addTabContent(var3);
      }

      this.tabHeaderAreaClipRect = new Rectangle();
      this.tabHeaderArea = new TabHeaderArea();
      this.tabHeaderArea.setClip(this.tabHeaderAreaClipRect);
      this.getChildren().add(this.tabHeaderArea);
      if (((TabPane)this.getSkinnable()).getTabs().size() == 0) {
         this.tabHeaderArea.setVisible(false);
      }

      this.initializeTabListener();
      this.registerChangeListener(var1.getSelectionModel().selectedItemProperty(), "SELECTED_TAB");
      this.registerChangeListener(var1.sideProperty(), "SIDE");
      this.registerChangeListener(var1.widthProperty(), "WIDTH");
      this.registerChangeListener(var1.heightProperty(), "HEIGHT");
      this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
      if (this.selectedTab == null && ((TabPane)this.getSkinnable()).getSelectionModel().getSelectedIndex() != -1) {
         ((TabPane)this.getSkinnable()).getSelectionModel().select(((TabPane)this.getSkinnable()).getSelectionModel().getSelectedIndex());
         this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
      }

      if (this.selectedTab == null) {
         ((TabPane)this.getSkinnable()).getSelectionModel().selectFirst();
      }

      this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
      this.isSelectingTab = false;
      this.initializeSwipeHandlers();
   }

   public StackPane getSelectedTabContentRegion() {
      Iterator var1 = this.tabContentRegions.iterator();

      TabContentRegion var2;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         var2 = (TabContentRegion)var1.next();
      } while(!var2.getTab().equals(this.selectedTab));

      return var2;
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("SELECTED_TAB".equals(var1)) {
         this.isSelectingTab = true;
         this.selectedTab = (Tab)((TabPane)this.getSkinnable()).getSelectionModel().getSelectedItem();
         ((TabPane)this.getSkinnable()).requestLayout();
      } else if ("SIDE".equals(var1)) {
         this.updateTabPosition();
      } else if ("WIDTH".equals(var1)) {
         this.clipRect.setWidth(((TabPane)this.getSkinnable()).getWidth());
      } else if ("HEIGHT".equals(var1)) {
         this.clipRect.setHeight(((TabPane)this.getSkinnable()).getHeight());
      }

   }

   private void removeTabs(List var1) {
      Iterator var2 = var1.iterator();

      while(true) {
         Tab var3;
         TabHeaderSkin var4;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (Tab)var2.next();
            this.stopCurrentAnimation(var3);
            var4 = this.tabHeaderArea.getTabHeaderSkin(var3);
         } while(var4 == null);

         var4.isClosing = true;
         var4.removeListeners(var3);
         this.removeTabContent(var3);
         ContextMenu var5 = this.tabHeaderArea.controlButtons.popup;
         TabMenuItem var6 = null;
         if (var5 != null) {
            for(Iterator var7 = var5.getItems().iterator(); var7.hasNext(); var6 = null) {
               MenuItem var8 = (MenuItem)var7.next();
               var6 = (TabMenuItem)var8;
               if (var3 == var6.getTab()) {
                  break;
               }
            }
         }

         if (var6 != null) {
            var6.dispose();
            var5.getItems().remove(var6);
         }

         EventHandler var9 = (var3x) -> {
            var4.animationState = TabPaneSkin.TabAnimationState.NONE;
            this.tabHeaderArea.removeTab(var3);
            this.tabHeaderArea.requestLayout();
            if (((TabPane)this.getSkinnable()).getTabs().isEmpty()) {
               this.tabHeaderArea.setVisible(false);
            }

         };
         if (this.closeTabAnimation.get() == TabPaneSkin.TabAnimation.GROW) {
            var4.animationState = TabPaneSkin.TabAnimationState.HIDING;
            Timeline var10 = var4.currentAnimation = this.createTimeline(var4, Duration.millis(150.0), 0.0, var9);
            var10.play();
         } else {
            var9.handle((Event)null);
         }
      }
   }

   private void stopCurrentAnimation(Tab var1) {
      TabHeaderSkin var2 = this.tabHeaderArea.getTabHeaderSkin(var1);
      if (var2 != null) {
         Timeline var3 = var2.currentAnimation;
         if (var3 != null && var3.getStatus() == Animation.Status.RUNNING) {
            var3.getOnFinished().handle((Event)null);
            var3.stop();
            var2.currentAnimation = null;
         }
      }

   }

   private void addTabs(List var1, int var2) {
      int var3 = 0;
      ArrayList var4 = new ArrayList(this.tabHeaderArea.headersRegion.getChildren());
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Node var6 = (Node)var5.next();
         TabHeaderSkin var7 = (TabHeaderSkin)var6;
         if (var7.animationState == TabPaneSkin.TabAnimationState.HIDING) {
            this.stopCurrentAnimation(var7.tab);
         }
      }

      var5 = var1.iterator();

      while(var5.hasNext()) {
         Tab var9 = (Tab)var5.next();
         this.stopCurrentAnimation(var9);
         if (!this.tabHeaderArea.isVisible()) {
            this.tabHeaderArea.setVisible(true);
         }

         int var10 = var2 + var3++;
         this.tabHeaderArea.addTab(var9, var10);
         this.addTabContent(var9);
         TabHeaderSkin var8 = this.tabHeaderArea.getTabHeaderSkin(var9);
         if (var8 != null) {
            if (this.openTabAnimation.get() == TabPaneSkin.TabAnimation.GROW) {
               var8.animationState = TabPaneSkin.TabAnimationState.SHOWING;
               var8.animationTransition.setValue((Number)0.0);
               var8.setVisible(true);
               var8.currentAnimation = this.createTimeline(var8, Duration.millis(150.0), 1.0, (var1x) -> {
                  var8.animationState = TabPaneSkin.TabAnimationState.NONE;
                  var8.setVisible(true);
                  var8.inner.requestLayout();
               });
               var8.currentAnimation.play();
            } else {
               var8.setVisible(true);
               var8.inner.requestLayout();
            }
         }
      }

   }

   private void initializeTabListener() {
      ((TabPane)this.getSkinnable()).getTabs().addListener((var1) -> {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         int var4 = -1;

         while(var1.next()) {
            if (var1.wasPermutated()) {
               TabPane var5 = (TabPane)this.getSkinnable();
               ObservableList var6 = var5.getTabs();
               int var7 = var1.getTo() - var1.getFrom();
               Tab var8 = (Tab)var5.getSelectionModel().getSelectedItem();
               ArrayList var9 = new ArrayList(var7);
               ((TabPane)this.getSkinnable()).getSelectionModel().clearSelection();
               TabAnimation var10 = (TabAnimation)this.openTabAnimation.get();
               TabAnimation var11 = (TabAnimation)this.closeTabAnimation.get();
               this.openTabAnimation.set(TabPaneSkin.TabAnimation.NONE);
               this.closeTabAnimation.set(TabPaneSkin.TabAnimation.NONE);

               for(int var12 = var1.getFrom(); var12 < var1.getTo(); ++var12) {
                  var9.add(var6.get(var12));
               }

               this.removeTabs(var9);
               this.addTabs(var9, var1.getFrom());
               this.openTabAnimation.set(var10);
               this.closeTabAnimation.set(var11);
               ((TabPane)this.getSkinnable()).getSelectionModel().select(var8);
            }

            if (var1.wasRemoved()) {
               var2.addAll(var1.getRemoved());
            }

            if (var1.wasAdded()) {
               var3.addAll(var1.getAddedSubList());
               var4 = var1.getFrom();
            }
         }

         var2.removeAll(var3);
         this.removeTabs(var2);
         if (!var3.isEmpty()) {
            Iterator var13 = this.tabContentRegions.iterator();

            while(var13.hasNext()) {
               TabContentRegion var14 = (TabContentRegion)var13.next();
               Tab var15 = var14.getTab();
               TabHeaderSkin var16 = this.tabHeaderArea.getTabHeaderSkin(var15);
               if (!var16.isClosing && var3.contains(var14.getTab())) {
                  var3.remove(var14.getTab());
               }
            }

            this.addTabs(var3, var4 == -1 ? this.tabContentRegions.size() : var4);
         }

         ((TabPane)this.getSkinnable()).requestLayout();
      });
   }

   private void addTabContent(Tab var1) {
      TabContentRegion var2 = new TabContentRegion(var1);
      var2.setClip(new Rectangle());
      this.tabContentRegions.add(var2);
      this.getChildren().add(0, var2);
   }

   private void removeTabContent(Tab var1) {
      Iterator var2 = this.tabContentRegions.iterator();

      while(var2.hasNext()) {
         TabContentRegion var3 = (TabContentRegion)var2.next();
         if (var3.getTab().equals(var1)) {
            var3.removeListeners(var1);
            this.getChildren().remove(var3);
            this.tabContentRegions.remove(var3);
            break;
         }
      }

   }

   private void updateTabPosition() {
      this.tabHeaderArea.setScrollOffset(0.0);
      ((TabPane)this.getSkinnable()).applyCss();
      ((TabPane)this.getSkinnable()).requestLayout();
   }

   private Timeline createTimeline(TabHeaderSkin var1, Duration var2, double var3, EventHandler var5) {
      Timeline var6 = new Timeline();
      var6.setCycleCount(1);
      KeyValue var7 = new KeyValue(var1.animationTransition, var3, Interpolator.LINEAR);
      var6.getKeyFrames().clear();
      var6.getKeyFrames().add(new KeyFrame(var2, new KeyValue[]{var7}));
      var6.setOnFinished(var5);
      return var6;
   }

   private boolean isHorizontal() {
      Side var1 = ((TabPane)this.getSkinnable()).getSide();
      return Side.TOP.equals(var1) || Side.BOTTOM.equals(var1);
   }

   private void initializeSwipeHandlers() {
      if (IS_TOUCH_SUPPORTED) {
         ((TabPane)this.getSkinnable()).addEventHandler(SwipeEvent.SWIPE_LEFT, (var1) -> {
            ((TabPaneBehavior)this.getBehavior()).selectNextTab();
         });
         ((TabPane)this.getSkinnable()).addEventHandler(SwipeEvent.SWIPE_RIGHT, (var1) -> {
            ((TabPaneBehavior)this.getBehavior()).selectPreviousTab();
         });
      }

   }

   private boolean isFloatingStyleClass() {
      return ((TabPane)this.getSkinnable()).getStyleClass().contains("floating");
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      TabContentRegion var12;
      for(Iterator var11 = this.tabContentRegions.iterator(); var11.hasNext(); this.maxw = Math.max(this.maxw, this.snapSize(var12.prefWidth(-1.0)))) {
         var12 = (TabContentRegion)var11.next();
      }

      boolean var16 = this.isHorizontal();
      double var17 = this.snapSize(var16 ? this.tabHeaderArea.prefWidth(-1.0) : this.tabHeaderArea.prefHeight(-1.0));
      double var14 = var16 ? Math.max(this.maxw, var17) : this.maxw + var17;
      return this.snapSize(var14) + var5 + var9;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      TabContentRegion var12;
      for(Iterator var11 = this.tabContentRegions.iterator(); var11.hasNext(); this.maxh = Math.max(this.maxh, this.snapSize(var12.prefHeight(-1.0)))) {
         var12 = (TabContentRegion)var11.next();
      }

      boolean var16 = this.isHorizontal();
      double var17 = this.snapSize(var16 ? this.tabHeaderArea.prefHeight(-1.0) : this.tabHeaderArea.prefWidth(-1.0));
      double var14 = var16 ? this.maxh + this.snapSize(var17) : Math.max(this.maxh, var17);
      return this.snapSize(var14) + var3 + var7;
   }

   public double computeBaselineOffset(double var1, double var3, double var5, double var7) {
      Side var9 = ((TabPane)this.getSkinnable()).getSide();
      return var9 == Side.TOP ? this.tabHeaderArea.getBaselineOffset() + var1 : 0.0;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      TabPane var9 = (TabPane)this.getSkinnable();
      Side var10 = var9.getSide();
      double var11 = this.snapSize(this.tabHeaderArea.prefHeight(-1.0));
      double var13 = var10.equals(Side.RIGHT) ? var1 + var5 - var11 : var1;
      double var15 = var10.equals(Side.BOTTOM) ? var3 + var7 - var11 : var3;
      if (var10 == Side.TOP) {
         this.tabHeaderArea.resize(var5, var11);
         this.tabHeaderArea.relocate(var13, var15);
         this.tabHeaderArea.getTransforms().clear();
         this.tabHeaderArea.getTransforms().add(new Rotate((double)getRotation(Side.TOP)));
      } else if (var10 == Side.BOTTOM) {
         this.tabHeaderArea.resize(var5, var11);
         this.tabHeaderArea.relocate(var5, var15 - var11);
         this.tabHeaderArea.getTransforms().clear();
         this.tabHeaderArea.getTransforms().add(new Rotate((double)getRotation(Side.BOTTOM), 0.0, var11));
      } else if (var10 == Side.LEFT) {
         this.tabHeaderArea.resize(var7, var11);
         this.tabHeaderArea.relocate(var13 + var11, var7 - var11);
         this.tabHeaderArea.getTransforms().clear();
         this.tabHeaderArea.getTransforms().add(new Rotate((double)getRotation(Side.LEFT), 0.0, var11));
      } else if (var10 == Side.RIGHT) {
         this.tabHeaderArea.resize(var7, var11);
         this.tabHeaderArea.relocate(var13, var3 - var11);
         this.tabHeaderArea.getTransforms().clear();
         this.tabHeaderArea.getTransforms().add(new Rotate((double)getRotation(Side.RIGHT), 0.0, var11));
      }

      this.tabHeaderAreaClipRect.setX(0.0);
      this.tabHeaderAreaClipRect.setY(0.0);
      if (this.isHorizontal()) {
         this.tabHeaderAreaClipRect.setWidth(var5);
      } else {
         this.tabHeaderAreaClipRect.setWidth(var7);
      }

      this.tabHeaderAreaClipRect.setHeight(var11);
      double var17 = 0.0;
      double var19 = 0.0;
      if (var10 == Side.TOP) {
         var17 = var1;
         var19 = var3 + var11;
         if (this.isFloatingStyleClass()) {
            --var19;
         }
      } else if (var10 == Side.BOTTOM) {
         var17 = var1;
         var19 = var3;
         if (this.isFloatingStyleClass()) {
            var19 = 1.0;
         }
      } else if (var10 == Side.LEFT) {
         var17 = var1 + var11;
         var19 = var3;
         if (this.isFloatingStyleClass()) {
            --var17;
         }
      } else if (var10 == Side.RIGHT) {
         var17 = var1;
         var19 = var3;
         if (this.isFloatingStyleClass()) {
            var17 = 1.0;
         }
      }

      double var21 = var5 - (this.isHorizontal() ? 0.0 : var11);
      double var23 = var7 - (this.isHorizontal() ? var11 : 0.0);
      int var25 = 0;

      for(int var26 = this.tabContentRegions.size(); var25 < var26; ++var25) {
         TabContentRegion var27 = (TabContentRegion)this.tabContentRegions.get(var25);
         var27.setAlignment(Pos.TOP_LEFT);
         if (var27.getClip() != null) {
            ((Rectangle)var27.getClip()).setWidth(var21);
            ((Rectangle)var27.getClip()).setHeight(var23);
         }

         var27.resize(var21, var23);
         var27.relocate(var17, var19);
      }

   }

   public static List getClassCssMetaData() {
      return TabPaneSkin.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case FOCUS_ITEM:
            return this.tabHeaderArea.getTabHeaderSkin(this.selectedTab);
         case ITEM_COUNT:
            return this.tabHeaderArea.headersRegion.getChildren().size();
         case ITEM_AT_INDEX:
            Integer var3 = (Integer)var2[0];
            if (var3 == null) {
               return null;
            }

            return this.tabHeaderArea.headersRegion.getChildren().get(var3);
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   class TabMenuItem extends RadioMenuItem {
      Tab tab;
      private InvalidationListener disableListener = new InvalidationListener() {
         public void invalidated(Observable var1) {
            TabMenuItem.this.setDisable(TabMenuItem.this.tab.isDisable());
         }
      };
      private WeakInvalidationListener weakDisableListener;

      public TabMenuItem(Tab var2) {
         super(var2.getText(), TabPaneSkin.clone(var2.getGraphic()));
         this.weakDisableListener = new WeakInvalidationListener(this.disableListener);
         this.tab = var2;
         this.setDisable(var2.isDisable());
         var2.disableProperty().addListener(this.weakDisableListener);
         this.textProperty().bind(var2.textProperty());
      }

      public Tab getTab() {
         return this.tab;
      }

      public void dispose() {
         this.tab.disableProperty().removeListener(this.weakDisableListener);
      }
   }

   class TabControlButtons extends StackPane {
      private StackPane inner;
      private StackPane downArrow;
      private Pane downArrowBtn;
      private boolean showControlButtons;
      private ContextMenu popup;
      private boolean showTabsMenu = false;

      public TabControlButtons() {
         this.getStyleClass().setAll((Object[])("control-buttons-tab"));
         TabPane var2 = (TabPane)TabPaneSkin.this.getSkinnable();
         this.downArrowBtn = new Pane();
         this.downArrowBtn.getStyleClass().setAll((Object[])("tab-down-button"));
         this.downArrowBtn.setVisible(this.isShowTabsMenu());
         this.downArrow = new StackPane();
         this.downArrow.setManaged(false);
         this.downArrow.getStyleClass().setAll((Object[])("arrow"));
         this.downArrow.setRotate(var2.getSide().equals(Side.BOTTOM) ? 180.0 : 0.0);
         this.downArrowBtn.getChildren().add(this.downArrow);
         this.downArrowBtn.setOnMouseClicked((var1x) -> {
            this.showPopupMenu();
         });
         this.setupPopupMenu();
         this.inner = new StackPane() {
            protected double computePrefWidth(double var1) {
               double var5 = !TabControlButtons.this.isShowTabsMenu() ? 0.0 : this.snapSize(TabControlButtons.this.downArrow.prefWidth(this.getHeight())) + this.snapSize(TabControlButtons.this.downArrowBtn.prefWidth(this.getHeight()));
               double var3 = 0.0;
               if (TabControlButtons.this.isShowTabsMenu()) {
                  var3 += var5;
               }

               if (var3 > 0.0) {
                  var3 += this.snappedLeftInset() + this.snappedRightInset();
               }

               return var3;
            }

            protected double computePrefHeight(double var1) {
               double var3 = 0.0;
               if (TabControlButtons.this.isShowTabsMenu()) {
                  var3 = Math.max(var3, this.snapSize(TabControlButtons.this.downArrowBtn.prefHeight(var1)));
               }

               if (var3 > 0.0) {
                  var3 += this.snappedTopInset() + this.snappedBottomInset();
               }

               return var3;
            }

            protected void layoutChildren() {
               if (TabControlButtons.this.isShowTabsMenu()) {
                  double var1 = 0.0;
                  double var3 = this.snappedTopInset();
                  double var5 = this.snapSize(this.getWidth()) - var1 + this.snappedLeftInset();
                  double var7 = this.snapSize(this.getHeight()) - var3 + this.snappedBottomInset();
                  this.positionArrow(TabControlButtons.this.downArrowBtn, TabControlButtons.this.downArrow, var1, var3, var5, var7);
               }

            }

            private void positionArrow(Pane var1, StackPane var2, double var3, double var5, double var7, double var9) {
               var1.resize(var7, var9);
               this.positionInArea(var1, var3, var5, var7, var9, 0.0, HPos.CENTER, VPos.CENTER);
               double var11 = this.snapSize(var2.prefWidth(-1.0));
               double var13 = this.snapSize(var2.prefHeight(-1.0));
               var2.resize(var11, var13);
               this.positionInArea(var2, var1.snappedLeftInset(), var1.snappedTopInset(), var7 - var1.snappedLeftInset() - var1.snappedRightInset(), var9 - var1.snappedTopInset() - var1.snappedBottomInset(), 0.0, HPos.CENTER, VPos.CENTER);
            }
         };
         this.inner.getStyleClass().add("container");
         this.inner.getChildren().add(this.downArrowBtn);
         this.getChildren().add(this.inner);
         var2.sideProperty().addListener((var1x) -> {
            Side var2 = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            this.downArrow.setRotate(var2.equals(Side.BOTTOM) ? 180.0 : 0.0);
         });
         var2.getTabs().addListener((var1x) -> {
            this.setupPopupMenu();
         });
         this.showControlButtons = false;
         if (this.isShowTabsMenu()) {
            this.showControlButtons = true;
            this.requestLayout();
         }

         this.getProperties().put(ContextMenu.class, this.popup);
      }

      private void showTabsMenu(boolean var1) {
         boolean var2 = this.isShowTabsMenu();
         this.showTabsMenu = var1;
         if (this.showTabsMenu && !var2) {
            this.downArrowBtn.setVisible(true);
            this.showControlButtons = true;
            this.inner.requestLayout();
            TabPaneSkin.this.tabHeaderArea.requestLayout();
         } else if (!this.showTabsMenu && var2) {
            this.hideControlButtons();
         }

      }

      private boolean isShowTabsMenu() {
         return this.showTabsMenu;
      }

      protected double computePrefWidth(double var1) {
         double var3 = this.snapSize(this.inner.prefWidth(var1));
         if (var3 > 0.0) {
            var3 += this.snappedLeftInset() + this.snappedRightInset();
         }

         return var3;
      }

      protected double computePrefHeight(double var1) {
         return Math.max(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinHeight(), this.snapSize(this.inner.prefHeight(var1))) + this.snappedTopInset() + this.snappedBottomInset();
      }

      protected void layoutChildren() {
         double var1 = this.snappedLeftInset();
         double var3 = this.snappedTopInset();
         double var5 = this.snapSize(this.getWidth()) - var1 + this.snappedRightInset();
         double var7 = this.snapSize(this.getHeight()) - var3 + this.snappedBottomInset();
         if (this.showControlButtons) {
            this.showControlButtons();
            this.showControlButtons = false;
         }

         this.inner.resize(var5, var7);
         this.positionInArea(this.inner, var1, var3, var5, var7, 0.0, HPos.CENTER, VPos.BOTTOM);
      }

      private void showControlButtons() {
         this.setVisible(true);
         if (this.popup == null) {
            this.setupPopupMenu();
         }

      }

      private void hideControlButtons() {
         if (this.isShowTabsMenu()) {
            this.showControlButtons = true;
         } else {
            this.setVisible(false);
            this.popup.getItems().clear();
            this.popup = null;
         }

         this.requestLayout();
      }

      private void setupPopupMenu() {
         if (this.popup == null) {
            this.popup = new ContextMenu();
         }

         this.popup.getItems().clear();
         ToggleGroup var1 = new ToggleGroup();
         ObservableList var2 = FXCollections.observableArrayList();
         Iterator var3 = ((TabPane)TabPaneSkin.this.getSkinnable()).getTabs().iterator();

         while(var3.hasNext()) {
            Tab var4 = (Tab)var3.next();
            TabMenuItem var5 = TabPaneSkin.this.new TabMenuItem(var4);
            var5.setToggleGroup(var1);
            var5.setOnAction((var2x) -> {
               ((TabPane)TabPaneSkin.this.getSkinnable()).getSelectionModel().select(var4);
            });
            var2.add(var5);
         }

         this.popup.getItems().addAll(var2);
      }

      private void showPopupMenu() {
         Iterator var1 = this.popup.getItems().iterator();

         while(var1.hasNext()) {
            MenuItem var2 = (MenuItem)var1.next();
            TabMenuItem var3 = (TabMenuItem)var2;
            if (TabPaneSkin.this.selectedTab.equals(var3.getTab())) {
               var3.setSelected(true);
               break;
            }
         }

         this.popup.show(this.downArrowBtn, Side.BOTTOM, 0.0, 0.0);
      }
   }

   class TabContentRegion extends StackPane {
      private TraversalEngine engine;
      private Direction direction;
      private Tab tab;
      private InvalidationListener tabContentListener;
      private InvalidationListener tabSelectedListener;
      private WeakInvalidationListener weakTabContentListener;
      private WeakInvalidationListener weakTabSelectedListener;

      public Tab getTab() {
         return this.tab;
      }

      public TabContentRegion(Tab var2) {
         this.direction = Direction.NEXT;
         this.tabContentListener = (var1x) -> {
            this.updateContent();
         };
         this.tabSelectedListener = new InvalidationListener() {
            public void invalidated(Observable var1) {
               TabContentRegion.this.setVisible(TabContentRegion.this.tab.isSelected());
            }
         };
         this.weakTabContentListener = new WeakInvalidationListener(this.tabContentListener);
         this.weakTabSelectedListener = new WeakInvalidationListener(this.tabSelectedListener);
         this.getStyleClass().setAll((Object[])("tab-content-area"));
         this.setManaged(false);
         this.tab = var2;
         this.updateContent();
         this.setVisible(var2.isSelected());
         var2.selectedProperty().addListener(this.weakTabSelectedListener);
         var2.contentProperty().addListener(this.weakTabContentListener);
      }

      private void updateContent() {
         Node var1 = this.getTab().getContent();
         if (var1 == null) {
            this.getChildren().clear();
         } else {
            this.getChildren().setAll((Object[])(var1));
         }

      }

      private void removeListeners(Tab var1) {
         var1.selectedProperty().removeListener(this.weakTabSelectedListener);
         var1.contentProperty().removeListener(this.weakTabContentListener);
      }
   }

   class TabHeaderSkin extends StackPane {
      private final Tab tab;
      private Label label;
      private StackPane closeBtn;
      private StackPane inner;
      private Tooltip oldTooltip;
      private Tooltip tooltip;
      private Rectangle clip;
      private boolean isClosing = false;
      private MultiplePropertyChangeListenerHandler listener = new MultiplePropertyChangeListenerHandler((var1x) -> {
         this.handlePropertyChanged(var1x);
         return null;
      });
      private final ListChangeListener styleClassListener = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            TabHeaderSkin.this.getStyleClass().setAll((Collection)TabHeaderSkin.this.tab.getStyleClass());
         }
      };
      private final WeakListChangeListener weakStyleClassListener;
      private final DoubleProperty animationTransition;
      private TabAnimationState animationState;
      private Timeline currentAnimation;

      public Tab getTab() {
         return this.tab;
      }

      public TabHeaderSkin(Tab var2) {
         this.weakStyleClassListener = new WeakListChangeListener(this.styleClassListener);
         this.animationTransition = new SimpleDoubleProperty(this, "animationTransition", 1.0) {
            protected void invalidated() {
               TabHeaderSkin.this.requestLayout();
            }
         };
         this.animationState = TabPaneSkin.TabAnimationState.NONE;
         this.getStyleClass().setAll((Collection)var2.getStyleClass());
         this.setId(var2.getId());
         this.setStyle(var2.getStyle());
         this.setAccessibleRole(AccessibleRole.TAB_ITEM);
         this.tab = var2;
         this.clip = new Rectangle();
         this.setClip(this.clip);
         this.label = new Label(var2.getText(), var2.getGraphic());
         this.label.getStyleClass().setAll((Object[])("tab-label"));
         this.closeBtn = new StackPane() {
            protected double computePrefWidth(double var1) {
               return (double)TabPaneSkin.CLOSE_BTN_SIZE;
            }

            protected double computePrefHeight(double var1) {
               return (double)TabPaneSkin.CLOSE_BTN_SIZE;
            }

            public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
               switch (var1) {
                  case FIRE:
                     Tab var3 = TabHeaderSkin.this.getTab();
                     TabPaneBehavior var4 = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                     if (var4.canCloseTab(var3)) {
                        var4.closeTab(var3);
                        this.setOnMousePressed((EventHandler)null);
                     }
                  default:
                     super.executeAccessibleAction(var1, var2);
               }
            }
         };
         this.closeBtn.setAccessibleRole(AccessibleRole.BUTTON);
         this.closeBtn.setAccessibleText(ControlResources.getString("Accessibility.title.TabPane.CloseButton"));
         this.closeBtn.getStyleClass().setAll((Object[])("tab-close-button"));
         this.closeBtn.setOnMousePressed(new EventHandler() {
            public void handle(MouseEvent var1) {
               Tab var2 = TabHeaderSkin.this.getTab();
               TabPaneBehavior var3 = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
               if (var3.canCloseTab(var2)) {
                  var3.closeTab(var2);
                  TabHeaderSkin.this.setOnMousePressed((EventHandler)null);
               }

            }
         });
         this.updateGraphicRotation();
         final Region var3 = new Region();
         var3.setMouseTransparent(true);
         var3.getStyleClass().add("focus-indicator");
         this.inner = new StackPane() {
            protected void layoutChildren() {
               TabPane var1 = (TabPane)TabPaneSkin.this.getSkinnable();
               double var2 = this.snappedTopInset();
               double var4 = this.snappedRightInset();
               double var6 = this.snappedBottomInset();
               double var8 = this.snappedLeftInset();
               double var10 = this.getWidth() - (var8 + var4);
               double var12 = this.getHeight() - (var2 + var6);
               double var14 = this.snapSize(TabHeaderSkin.this.label.prefWidth(-1.0));
               double var16 = this.snapSize(TabHeaderSkin.this.label.prefHeight(-1.0));
               double var18 = TabHeaderSkin.this.showCloseButton() ? this.snapSize(TabHeaderSkin.this.closeBtn.prefWidth(-1.0)) : 0.0;
               double var20 = TabHeaderSkin.this.showCloseButton() ? this.snapSize(TabHeaderSkin.this.closeBtn.prefHeight(-1.0)) : 0.0;
               double var22 = this.snapSize(var1.getTabMinWidth());
               double var24 = this.snapSize(var1.getTabMaxWidth());
               double var26 = this.snapSize(var1.getTabMaxHeight());
               double var28 = var14;
               double var30 = var14;
               double var32 = var16;
               double var34 = var14 + var18;
               double var36 = Math.max(var16, var20);
               if (var34 > var24 && var24 != Double.MAX_VALUE) {
                  var28 = var24 - var18;
                  var30 = var24 - var18;
               } else if (var34 < var22) {
                  var28 = var22 - var18;
               }

               if (var36 > var26 && var26 != Double.MAX_VALUE) {
                  var32 = var26;
               }

               if (TabHeaderSkin.this.animationState != TabPaneSkin.TabAnimationState.NONE) {
                  var28 *= TabHeaderSkin.this.animationTransition.get();
                  TabHeaderSkin.this.closeBtn.setVisible(false);
               } else {
                  TabHeaderSkin.this.closeBtn.setVisible(TabHeaderSkin.this.showCloseButton());
               }

               TabHeaderSkin.this.label.resize(var30, var32);
               double var40 = (var24 < Double.MAX_VALUE ? Math.min(var10, var24) : var10) - var4 - var18;
               this.positionInArea(TabHeaderSkin.this.label, var8, var2, var28, var12, 0.0, HPos.CENTER, VPos.CENTER);
               if (TabHeaderSkin.this.closeBtn.isVisible()) {
                  TabHeaderSkin.this.closeBtn.resize(var18, var20);
                  this.positionInArea(TabHeaderSkin.this.closeBtn, var40, var2, var18, var12, 0.0, HPos.CENTER, VPos.CENTER);
               }

               int var42 = com.sun.javafx.util.Utils.isMac() ? 2 : 3;
               int var43 = com.sun.javafx.util.Utils.isMac() ? 2 : 1;
               var3.resizeRelocate(var8 - (double)var43, var2 + (double)var42, var10 + (double)(2 * var43), var12 - (double)(2 * var42));
            }
         };
         this.inner.getStyleClass().add("tab-container");
         this.inner.setRotate(((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals(Side.BOTTOM) ? 180.0 : 0.0);
         this.inner.getChildren().addAll(this.label, this.closeBtn, var3);
         this.getChildren().addAll(this.inner);
         this.tooltip = var2.getTooltip();
         if (this.tooltip != null) {
            Tooltip.install(this, this.tooltip);
            this.oldTooltip = this.tooltip;
         }

         this.listener.registerChangeListener(var2.closableProperty(), "CLOSABLE");
         this.listener.registerChangeListener(var2.selectedProperty(), "SELECTED");
         this.listener.registerChangeListener(var2.textProperty(), "TEXT");
         this.listener.registerChangeListener(var2.graphicProperty(), "GRAPHIC");
         this.listener.registerChangeListener(var2.contextMenuProperty(), "CONTEXT_MENU");
         this.listener.registerChangeListener(var2.tooltipProperty(), "TOOLTIP");
         this.listener.registerChangeListener(var2.disableProperty(), "DISABLE");
         this.listener.registerChangeListener(var2.styleProperty(), "STYLE");
         var2.getStyleClass().addListener(this.weakStyleClassListener);
         this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabClosingPolicyProperty(), "TAB_CLOSING_POLICY");
         this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).sideProperty(), "SIDE");
         this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).rotateGraphicProperty(), "ROTATE_GRAPHIC");
         this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMinWidthProperty(), "TAB_MIN_WIDTH");
         this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMaxWidthProperty(), "TAB_MAX_WIDTH");
         this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMinHeightProperty(), "TAB_MIN_HEIGHT");
         this.listener.registerChangeListener(((TabPane)TabPaneSkin.this.getSkinnable()).tabMaxHeightProperty(), "TAB_MAX_HEIGHT");
         this.getProperties().put(Tab.class, var2);
         this.getProperties().put(ContextMenu.class, var2.getContextMenu());
         this.setOnContextMenuRequested((var1x) -> {
            if (this.getTab().getContextMenu() != null) {
               this.getTab().getContextMenu().show(this.inner, var1x.getScreenX(), var1x.getScreenY());
               var1x.consume();
            }

         });
         this.setOnMousePressed(new EventHandler() {
            public void handle(MouseEvent var1) {
               if (!TabHeaderSkin.this.getTab().isDisable()) {
                  if (var1.getButton().equals(MouseButton.MIDDLE)) {
                     if (TabHeaderSkin.this.showCloseButton()) {
                        Tab var2 = TabHeaderSkin.this.getTab();
                        TabPaneBehavior var3 = (TabPaneBehavior)TabPaneSkin.this.getBehavior();
                        if (var3.canCloseTab(var2)) {
                           TabHeaderSkin.this.removeListeners(var2);
                           var3.closeTab(var2);
                        }
                     }
                  } else if (var1.getButton().equals(MouseButton.PRIMARY)) {
                     ((TabPaneBehavior)TabPaneSkin.this.getBehavior()).selectTab(TabHeaderSkin.this.getTab());
                  }

               }
            }
         });
         this.pseudoClassStateChanged(TabPaneSkin.SELECTED_PSEUDOCLASS_STATE, var2.isSelected());
         this.pseudoClassStateChanged(TabPaneSkin.DISABLED_PSEUDOCLASS_STATE, var2.isDisable());
         Side var4 = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
         this.pseudoClassStateChanged(TabPaneSkin.TOP_PSEUDOCLASS_STATE, var4 == Side.TOP);
         this.pseudoClassStateChanged(TabPaneSkin.RIGHT_PSEUDOCLASS_STATE, var4 == Side.RIGHT);
         this.pseudoClassStateChanged(TabPaneSkin.BOTTOM_PSEUDOCLASS_STATE, var4 == Side.BOTTOM);
         this.pseudoClassStateChanged(TabPaneSkin.LEFT_PSEUDOCLASS_STATE, var4 == Side.LEFT);
      }

      private void handlePropertyChanged(String var1) {
         if ("CLOSABLE".equals(var1)) {
            this.inner.requestLayout();
            this.requestLayout();
         } else if ("SELECTED".equals(var1)) {
            this.pseudoClassStateChanged(TabPaneSkin.SELECTED_PSEUDOCLASS_STATE, this.tab.isSelected());
            this.inner.requestLayout();
            this.requestLayout();
         } else if ("TEXT".equals(var1)) {
            this.label.setText(this.getTab().getText());
         } else if ("GRAPHIC".equals(var1)) {
            this.label.setGraphic(this.getTab().getGraphic());
         } else if (!"CONTEXT_MENU".equals(var1)) {
            if ("TOOLTIP".equals(var1)) {
               if (this.oldTooltip != null) {
                  Tooltip.uninstall(this, this.oldTooltip);
               }

               this.tooltip = this.tab.getTooltip();
               if (this.tooltip != null) {
                  Tooltip.install(this, this.tooltip);
                  this.oldTooltip = this.tooltip;
               }
            } else if ("DISABLE".equals(var1)) {
               this.pseudoClassStateChanged(TabPaneSkin.DISABLED_PSEUDOCLASS_STATE, this.tab.isDisable());
               this.inner.requestLayout();
               this.requestLayout();
            } else if ("STYLE".equals(var1)) {
               this.setStyle(this.tab.getStyle());
            } else if ("TAB_CLOSING_POLICY".equals(var1)) {
               this.inner.requestLayout();
               this.requestLayout();
            } else if ("SIDE".equals(var1)) {
               Side var2 = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
               this.pseudoClassStateChanged(TabPaneSkin.TOP_PSEUDOCLASS_STATE, var2 == Side.TOP);
               this.pseudoClassStateChanged(TabPaneSkin.RIGHT_PSEUDOCLASS_STATE, var2 == Side.RIGHT);
               this.pseudoClassStateChanged(TabPaneSkin.BOTTOM_PSEUDOCLASS_STATE, var2 == Side.BOTTOM);
               this.pseudoClassStateChanged(TabPaneSkin.LEFT_PSEUDOCLASS_STATE, var2 == Side.LEFT);
               this.inner.setRotate(var2 == Side.BOTTOM ? 180.0 : 0.0);
               if (((TabPane)TabPaneSkin.this.getSkinnable()).isRotateGraphic()) {
                  this.updateGraphicRotation();
               }
            } else if ("ROTATE_GRAPHIC".equals(var1)) {
               this.updateGraphicRotation();
            } else if ("TAB_MIN_WIDTH".equals(var1)) {
               this.requestLayout();
               ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
            } else if ("TAB_MAX_WIDTH".equals(var1)) {
               this.requestLayout();
               ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
            } else if ("TAB_MIN_HEIGHT".equals(var1)) {
               this.requestLayout();
               ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
            } else if ("TAB_MAX_HEIGHT".equals(var1)) {
               this.requestLayout();
               ((TabPane)TabPaneSkin.this.getSkinnable()).requestLayout();
            }
         }

      }

      private void updateGraphicRotation() {
         if (this.label.getGraphic() != null) {
            this.label.getGraphic().setRotate(((TabPane)TabPaneSkin.this.getSkinnable()).isRotateGraphic() ? 0.0 : (double)(((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals(Side.RIGHT) ? -90.0F : (((TabPane)TabPaneSkin.this.getSkinnable()).getSide().equals(Side.LEFT) ? 90.0F : 0.0F)));
         }

      }

      private boolean showCloseButton() {
         return this.tab.isClosable() && (((TabPane)TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals(TabPane.TabClosingPolicy.ALL_TABS) || ((TabPane)TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals(TabPane.TabClosingPolicy.SELECTED_TAB) && this.tab.isSelected());
      }

      private void removeListeners(Tab var1) {
         this.listener.dispose();
         this.inner.getChildren().clear();
         this.getChildren().clear();
      }

      protected double computePrefWidth(double var1) {
         double var3 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinWidth());
         double var5 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMaxWidth());
         double var7 = this.snappedRightInset();
         double var9 = this.snappedLeftInset();
         double var11 = this.snapSize(this.label.prefWidth(-1.0));
         if (this.showCloseButton()) {
            var11 += this.snapSize(this.closeBtn.prefWidth(-1.0));
         }

         if (var11 > var5) {
            var11 = var5;
         } else if (var11 < var3) {
            var11 = var3;
         }

         var11 += var7 + var9;
         return var11;
      }

      protected double computePrefHeight(double var1) {
         double var3 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMinHeight());
         double var5 = this.snapSize(((TabPane)TabPaneSkin.this.getSkinnable()).getTabMaxHeight());
         double var7 = this.snappedTopInset();
         double var9 = this.snappedBottomInset();
         double var11 = this.snapSize(this.label.prefHeight(var1));
         if (var11 > var5) {
            var11 = var5;
         } else if (var11 < var3) {
            var11 = var3;
         }

         var11 += var7 + var9;
         return var11;
      }

      protected void layoutChildren() {
         double var1 = (this.snapSize(this.getWidth()) - this.snappedRightInset() - this.snappedLeftInset()) * this.animationTransition.getValue();
         this.inner.resize(var1, this.snapSize(this.getHeight()) - this.snappedTopInset() - this.snappedBottomInset());
         this.inner.relocate(this.snappedLeftInset(), this.snappedTopInset());
      }

      protected void setWidth(double var1) {
         super.setWidth(var1);
         this.clip.setWidth(var1);
      }

      protected void setHeight(double var1) {
         super.setHeight(var1);
         this.clip.setHeight(var1);
      }

      public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
         switch (var1) {
            case TEXT:
               return this.getTab().getText();
            case SELECTED:
               return TabPaneSkin.this.selectedTab == this.getTab();
            default:
               return super.queryAccessibleAttribute(var1, var2);
         }
      }

      public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
         switch (var1) {
            case REQUEST_FOCUS:
               ((TabPane)TabPaneSkin.this.getSkinnable()).getSelectionModel().select(this.getTab());
               break;
            default:
               super.executeAccessibleAction(var1, var2);
         }

      }
   }

   class TabHeaderArea extends StackPane {
      private Rectangle headerClip;
      private StackPane headersRegion;
      private StackPane headerBackground;
      private TabControlButtons controlButtons;
      private boolean measureClosingTabs = false;
      private double scrollOffset;
      private List removeTab = new ArrayList();

      public TabHeaderArea() {
         this.getStyleClass().setAll((Object[])("tab-header-area"));
         this.setManaged(false);
         TabPane var2 = (TabPane)TabPaneSkin.this.getSkinnable();
         this.headerClip = new Rectangle();
         this.headersRegion = new StackPane() {
            protected double computePrefWidth(double var1) {
               double var3 = 0.0;
               Iterator var5 = this.getChildren().iterator();

               while(true) {
                  TabHeaderSkin var7;
                  do {
                     do {
                        if (!var5.hasNext()) {
                           return this.snapSize(var3) + this.snappedLeftInset() + this.snappedRightInset();
                        }

                        Node var6 = (Node)var5.next();
                        var7 = (TabHeaderSkin)var6;
                     } while(!var7.isVisible());
                  } while(!TabHeaderArea.this.measureClosingTabs && var7.isClosing);

                  var3 += var7.prefWidth(var1);
               }
            }

            protected double computePrefHeight(double var1) {
               double var3 = 0.0;

               TabHeaderSkin var7;
               for(Iterator var5 = this.getChildren().iterator(); var5.hasNext(); var3 = Math.max(var3, var7.prefHeight(var1))) {
                  Node var6 = (Node)var5.next();
                  var7 = (TabHeaderSkin)var6;
               }

               return this.snapSize(var3) + this.snappedTopInset() + this.snappedBottomInset();
            }

            protected void layoutChildren() {
               if (TabHeaderArea.this.tabsFit()) {
                  TabHeaderArea.this.setScrollOffset(0.0);
               } else if (!TabHeaderArea.this.removeTab.isEmpty()) {
                  double var1 = 0.0;
                  double var3 = TabPaneSkin.this.tabHeaderArea.getWidth() - this.snapSize(TabHeaderArea.this.controlButtons.prefWidth(-1.0)) - TabHeaderArea.this.firstTabIndent() - 10.0;

                  double var7;
                  for(Iterator var5 = this.getChildren().iterator(); var5.hasNext(); var1 += var7) {
                     TabHeaderSkin var6 = (TabHeaderSkin)var5.next();
                     var7 = this.snapSize(var6.prefWidth(-1.0));
                     if (TabHeaderArea.this.removeTab.contains(var6)) {
                        if (var1 < var3) {
                           TabPaneSkin.this.isSelectingTab = true;
                        }

                        var5.remove();
                        TabHeaderArea.this.removeTab.remove(var6);
                        if (TabHeaderArea.this.removeTab.isEmpty()) {
                           break;
                        }
                     }
                  }
               }

               if (TabPaneSkin.this.isSelectingTab) {
                  TabHeaderArea.this.ensureSelectedTabIsVisible();
                  TabPaneSkin.this.isSelectingTab = false;
               } else {
                  TabHeaderArea.this.validateScrollOffset();
               }

               Side var15 = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
               double var2 = this.snapSize(this.prefHeight(-1.0));
               double var4 = !var15.equals(Side.LEFT) && !var15.equals(Side.BOTTOM) ? TabHeaderArea.this.getScrollOffset() : this.snapSize(this.getWidth()) - TabHeaderArea.this.getScrollOffset();
               TabHeaderArea.this.updateHeaderClip();
               Iterator var16 = this.getChildren().iterator();

               while(true) {
                  while(var16.hasNext()) {
                     Node var17 = (Node)var16.next();
                     TabHeaderSkin var8 = (TabHeaderSkin)var17;
                     double var9 = this.snapSize(var8.prefWidth(-1.0) * var8.animationTransition.get());
                     double var11 = this.snapSize(var8.prefHeight(-1.0));
                     var8.resize(var9, var11);
                     double var13 = var15.equals(Side.BOTTOM) ? 0.0 : var2 - var11 - this.snappedBottomInset();
                     if (!var15.equals(Side.LEFT) && !var15.equals(Side.BOTTOM)) {
                        var8.relocate(var4, var13);
                        var4 += var9;
                     } else {
                        var4 -= var9;
                        var8.relocate(var4, var13);
                     }
                  }

                  return;
               }
            }
         };
         this.headersRegion.getStyleClass().setAll((Object[])("headers-region"));
         this.headersRegion.setClip(this.headerClip);
         this.headerBackground = new StackPane();
         this.headerBackground.getStyleClass().setAll((Object[])("tab-header-background"));
         int var3 = 0;
         Iterator var4 = var2.getTabs().iterator();

         while(var4.hasNext()) {
            Tab var5 = (Tab)var4.next();
            this.addTab(var5, var3++);
         }

         this.controlButtons = TabPaneSkin.this.new TabControlButtons();
         this.controlButtons.setVisible(false);
         if (this.controlButtons.isVisible()) {
            this.controlButtons.setVisible(true);
         }

         this.getChildren().addAll(this.headerBackground, this.headersRegion, this.controlButtons);
         this.addEventHandler(ScrollEvent.SCROLL, (var1x) -> {
            Side var2 = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
            var2 = var2 == null ? Side.TOP : var2;
            switch (var2) {
               case TOP:
               case BOTTOM:
               default:
                  this.setScrollOffset(this.scrollOffset - var1x.getDeltaY());
                  break;
               case LEFT:
               case RIGHT:
                  this.setScrollOffset(this.scrollOffset + var1x.getDeltaY());
            }

         });
      }

      private void updateHeaderClip() {
         Side var1 = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
         double var2 = 0.0;
         double var4 = 0.0;
         double var6 = 0.0;
         double var8 = 0.0;
         double var10 = 0.0;
         double var12 = 0.0;
         double var14 = this.firstTabIndent();
         double var16 = this.snapSize(this.controlButtons.prefWidth(-1.0));
         this.measureClosingTabs = true;
         double var18 = this.snapSize(this.headersRegion.prefWidth(-1.0));
         this.measureClosingTabs = false;
         double var20 = this.snapSize(this.headersRegion.prefHeight(-1.0));
         if (var16 > 0.0) {
            var16 += 10.0;
         }

         if (this.headersRegion.getEffect() instanceof DropShadow) {
            DropShadow var22 = (DropShadow)this.headersRegion.getEffect();
            var12 = var22.getRadius();
         }

         var10 = this.snapSize(this.getWidth()) - var16 - var14;
         if (!var1.equals(Side.LEFT) && !var1.equals(Side.BOTTOM)) {
            var2 = -var12;
            var6 = (var18 < var10 ? var18 : var10) + var12;
            var8 = var20;
         } else {
            if (var18 < var10) {
               var6 = var18 + var12;
            } else {
               var2 = var18 - var10;
               var6 = var10 + var12;
            }

            var8 = var20;
         }

         this.headerClip.setX(var2);
         this.headerClip.setY(var4);
         this.headerClip.setWidth(var6);
         this.headerClip.setHeight(var8);
      }

      private void addTab(Tab var1, int var2) {
         TabHeaderSkin var3 = TabPaneSkin.this.new TabHeaderSkin(var1);
         this.headersRegion.getChildren().add(var2, var3);
      }

      private void removeTab(Tab var1) {
         TabHeaderSkin var2 = this.getTabHeaderSkin(var1);
         if (var2 != null) {
            if (this.tabsFit()) {
               this.headersRegion.getChildren().remove(var2);
            } else {
               this.removeTab.add(var2);
               var2.removeListeners(var1);
            }
         }

      }

      private TabHeaderSkin getTabHeaderSkin(Tab var1) {
         Iterator var2 = this.headersRegion.getChildren().iterator();

         TabHeaderSkin var4;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            Node var3 = (Node)var2.next();
            var4 = (TabHeaderSkin)var3;
         } while(!var4.getTab().equals(var1));

         return var4;
      }

      private boolean tabsFit() {
         double var1 = this.snapSize(this.headersRegion.prefWidth(-1.0));
         double var3 = this.snapSize(this.controlButtons.prefWidth(-1.0));
         double var5 = var1 + var3 + this.firstTabIndent() + 10.0;
         return var5 < this.getWidth();
      }

      private void ensureSelectedTabIsVisible() {
         double var1 = this.snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane)TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane)TabPaneSkin.this.getSkinnable()).getHeight());
         double var3 = this.snapSize(this.controlButtons.getWidth());
         double var5 = var1 - var3 - this.firstTabIndent() - 10.0;
         double var7 = 0.0;
         double var9 = 0.0;
         double var11 = 0.0;

         double var16;
         for(Iterator var13 = this.headersRegion.getChildren().iterator(); var13.hasNext(); var7 += var16) {
            Node var14 = (Node)var13.next();
            TabHeaderSkin var15 = (TabHeaderSkin)var14;
            var16 = this.snapSize(var15.prefWidth(-1.0));
            if (TabPaneSkin.this.selectedTab != null && TabPaneSkin.this.selectedTab.equals(var15.getTab())) {
               var9 = var7;
               var11 = var16;
            }
         }

         double var21 = this.getScrollOffset();
         double var17 = var9 + var11;
         if (var9 < -var21) {
            this.setScrollOffset(-var9);
         } else if (var17 > var5 - var21) {
            this.setScrollOffset(var5 - var17);
         }

      }

      public double getScrollOffset() {
         return this.scrollOffset;
      }

      private void validateScrollOffset() {
         this.setScrollOffset(this.getScrollOffset());
      }

      private void setScrollOffset(double var1) {
         double var3 = this.snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane)TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane)TabPaneSkin.this.getSkinnable()).getHeight());
         double var5 = this.snapSize(this.controlButtons.getWidth());
         double var7 = var3 - var5 - this.firstTabIndent() - 10.0;
         double var9 = 0.0;

         double var14;
         for(Iterator var11 = this.headersRegion.getChildren().iterator(); var11.hasNext(); var9 += var14) {
            Node var12 = (Node)var11.next();
            TabHeaderSkin var13 = (TabHeaderSkin)var12;
            var14 = this.snapSize(var13.prefWidth(-1.0));
         }

         double var16;
         if (var7 - var1 > var9 && var1 < 0.0) {
            var16 = var7 - var9;
         } else if (var1 > 0.0) {
            var16 = 0.0;
         } else {
            var16 = var1;
         }

         if (var16 != this.scrollOffset) {
            this.scrollOffset = var16;
            this.headersRegion.requestLayout();
         }

      }

      private double firstTabIndent() {
         switch (((TabPane)TabPaneSkin.this.getSkinnable()).getSide()) {
            case TOP:
            case BOTTOM:
               return this.snappedLeftInset();
            case LEFT:
            case RIGHT:
               return this.snappedTopInset();
            default:
               return 0.0;
         }
      }

      protected double computePrefWidth(double var1) {
         double var3 = TabPaneSkin.this.isHorizontal() ? this.snappedLeftInset() + this.snappedRightInset() : this.snappedTopInset() + this.snappedBottomInset();
         return this.snapSize(this.headersRegion.prefWidth(var1)) + this.controlButtons.prefWidth(var1) + this.firstTabIndent() + 10.0 + var3;
      }

      protected double computePrefHeight(double var1) {
         double var3 = TabPaneSkin.this.isHorizontal() ? this.snappedTopInset() + this.snappedBottomInset() : this.snappedLeftInset() + this.snappedRightInset();
         return this.snapSize(this.headersRegion.prefHeight(-1.0)) + var3;
      }

      public double getBaselineOffset() {
         return ((TabPane)TabPaneSkin.this.getSkinnable()).getSide() == Side.TOP ? this.headersRegion.getBaselineOffset() + this.snappedTopInset() : 0.0;
      }

      protected void layoutChildren() {
         double var1 = this.snappedLeftInset();
         double var3 = this.snappedRightInset();
         double var5 = this.snappedTopInset();
         double var7 = this.snappedBottomInset();
         double var9 = this.snapSize(this.getWidth()) - (TabPaneSkin.this.isHorizontal() ? var1 + var3 : var5 + var7);
         double var11 = this.snapSize(this.getHeight()) - (TabPaneSkin.this.isHorizontal() ? var5 + var7 : var1 + var3);
         double var13 = this.snapSize(this.prefHeight(-1.0));
         double var15 = this.snapSize(this.headersRegion.prefWidth(-1.0));
         double var17 = this.snapSize(this.headersRegion.prefHeight(-1.0));
         this.controlButtons.showTabsMenu(!this.tabsFit());
         this.updateHeaderClip();
         this.headersRegion.requestLayout();
         double var19 = this.snapSize(this.controlButtons.prefWidth(-1.0));
         double var21 = this.controlButtons.prefHeight(var19);
         this.controlButtons.resize(var19, var21);
         this.headersRegion.resize(var15, var17);
         if (TabPaneSkin.this.isFloatingStyleClass()) {
            this.headerBackground.setVisible(false);
         } else {
            this.headerBackground.resize(this.snapSize(this.getWidth()), this.snapSize(this.getHeight()));
            this.headerBackground.setVisible(true);
         }

         double var23 = 0.0;
         double var25 = 0.0;
         double var27 = 0.0;
         double var29 = 0.0;
         Side var31 = ((TabPane)TabPaneSkin.this.getSkinnable()).getSide();
         if (var31.equals(Side.TOP)) {
            var23 = var1;
            var25 = var13 - var17 - var7;
            var27 = var9 - var19 + var1;
            var29 = this.snapSize(this.getHeight()) - var21 - var7;
         } else if (var31.equals(Side.RIGHT)) {
            var23 = var5;
            var25 = var13 - var17 - var1;
            var27 = var9 - var19 + var5;
            var29 = this.snapSize(this.getHeight()) - var21 - var1;
         } else if (var31.equals(Side.BOTTOM)) {
            var23 = this.snapSize(this.getWidth()) - var15 - var1;
            var25 = var13 - var17 - var5;
            var27 = var3;
            var29 = this.snapSize(this.getHeight()) - var21 - var5;
         } else if (var31.equals(Side.LEFT)) {
            var23 = this.snapSize(this.getWidth()) - var15 - var5;
            var25 = var13 - var17 - var3;
            var27 = var1;
            var29 = this.snapSize(this.getHeight()) - var21 - var3;
         }

         if (this.headerBackground.isVisible()) {
            this.positionInArea(this.headerBackground, 0.0, 0.0, this.snapSize(this.getWidth()), this.snapSize(this.getHeight()), 0.0, HPos.CENTER, VPos.CENTER);
         }

         this.positionInArea(this.headersRegion, var23, var25, var9, var11, 0.0, HPos.LEFT, VPos.CENTER);
         this.positionInArea(this.controlButtons, var27, var29, var19, var21, 0.0, HPos.CENTER, VPos.CENTER);
      }
   }

   private static class StyleableProperties {
      private static final List STYLEABLES;
      private static final CssMetaData OPEN_TAB_ANIMATION;
      private static final CssMetaData CLOSE_TAB_ANIMATION;

      static {
         OPEN_TAB_ANIMATION = new CssMetaData("-fx-open-tab-animation", new EnumConverter(TabAnimation.class), TabPaneSkin.TabAnimation.GROW) {
            public boolean isSettable(TabPane var1) {
               return true;
            }

            public StyleableProperty getStyleableProperty(TabPane var1) {
               TabPaneSkin var2 = (TabPaneSkin)var1.getSkin();
               return (StyleableProperty)var2.openTabAnimation;
            }
         };
         CLOSE_TAB_ANIMATION = new CssMetaData("-fx-close-tab-animation", new EnumConverter(TabAnimation.class), TabPaneSkin.TabAnimation.GROW) {
            public boolean isSettable(TabPane var1) {
               return true;
            }

            public StyleableProperty getStyleableProperty(TabPane var1) {
               TabPaneSkin var2 = (TabPaneSkin)var1.getSkin();
               return (StyleableProperty)var2.closeTabAnimation;
            }
         };
         ArrayList var0 = new ArrayList(SkinBase.getClassCssMetaData());
         var0.add(OPEN_TAB_ANIMATION);
         var0.add(CLOSE_TAB_ANIMATION);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   private static enum TabAnimationState {
      SHOWING,
      HIDING,
      NONE;
   }

   private static enum TabAnimation {
      NONE,
      GROW;
   }
}
