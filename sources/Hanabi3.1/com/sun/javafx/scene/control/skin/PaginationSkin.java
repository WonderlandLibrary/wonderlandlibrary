package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.PaginationBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class PaginationSkin extends BehaviorSkinBase {
   private static final Duration DURATION = new Duration(125.0);
   private static final double SWIPE_THRESHOLD = 0.3;
   private static final double TOUCH_THRESHOLD = 15.0;
   private Pagination pagination;
   private StackPane currentStackPane;
   private StackPane nextStackPane;
   private Timeline timeline;
   private Rectangle clipRect = new Rectangle();
   private NavigationControl navigation;
   private int fromIndex;
   private int previousIndex;
   private int currentIndex;
   private int toIndex;
   private int pageCount;
   private int maxPageIndicatorCount;
   private boolean animate = true;
   private double startTouchPos;
   private double lastTouchPos;
   private long startTouchTime;
   private long lastTouchTime;
   private double touchVelocity;
   private boolean touchThresholdBroken;
   private int touchEventId = -1;
   private boolean nextPageReached = false;
   private boolean setInitialDirection = false;
   private int direction;
   private static final Interpolator interpolator = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);
   private int currentAnimatedIndex;
   private boolean hasPendingAnimation = false;
   private EventHandler swipeAnimationEndEventHandler = new EventHandler() {
      public void handle(ActionEvent var1) {
         PaginationSkin.this.swapPanes();
         PaginationSkin.this.timeline = null;
         if (PaginationSkin.this.hasPendingAnimation) {
            PaginationSkin.this.animateSwitchPage();
            PaginationSkin.this.hasPendingAnimation = false;
         }

      }
   };
   private EventHandler clampAnimationEndEventHandler = new EventHandler() {
      public void handle(ActionEvent var1) {
         PaginationSkin.this.currentStackPane.setTranslateX(0.0);
         PaginationSkin.this.nextStackPane.setTranslateX(0.0);
         PaginationSkin.this.nextStackPane.setVisible(false);
         PaginationSkin.this.timeline = null;
      }
   };
   private final DoubleProperty arrowButtonGap = new StyleableDoubleProperty(60.0) {
      public Object getBean() {
         return PaginationSkin.this;
      }

      public String getName() {
         return "arrowButtonGap";
      }

      public CssMetaData getCssMetaData() {
         return PaginationSkin.StyleableProperties.ARROW_BUTTON_GAP;
      }
   };
   private BooleanProperty arrowsVisible;
   private BooleanProperty pageInformationVisible;
   private ObjectProperty pageInformationAlignment;
   private BooleanProperty tooltipVisible;
   private static final Boolean DEFAULT_ARROW_VISIBLE;
   private static final Boolean DEFAULT_PAGE_INFORMATION_VISIBLE;
   private static final Side DEFAULT_PAGE_INFORMATION_ALIGNMENT;
   private static final Boolean DEFAULT_TOOLTIP_VISIBLE;

   public PaginationSkin(Pagination var1) {
      super(var1, new PaginationBehavior(var1));
      ((Pagination)this.getSkinnable()).setClip(this.clipRect);
      this.pagination = var1;
      this.currentStackPane = new StackPane();
      this.currentStackPane.getStyleClass().add("page");
      this.nextStackPane = new StackPane();
      this.nextStackPane.getStyleClass().add("page");
      this.nextStackPane.setVisible(false);
      this.resetIndexes(true);
      this.navigation = new NavigationControl();
      this.getChildren().addAll(this.currentStackPane, this.nextStackPane, this.navigation);
      var1.maxPageIndicatorCountProperty().addListener((var1x) -> {
         this.resetIndiciesAndNav();
      });
      this.registerChangeListener(var1.widthProperty(), "WIDTH");
      this.registerChangeListener(var1.heightProperty(), "HEIGHT");
      this.registerChangeListener(var1.pageCountProperty(), "PAGE_COUNT");
      this.registerChangeListener(var1.pageFactoryProperty(), "PAGE_FACTORY");
      this.initializeSwipeAndTouchHandlers();
   }

   protected void resetIndiciesAndNav() {
      this.resetIndexes(false);
      this.navigation.initializePageIndicators();
      this.navigation.updatePageIndicators();
   }

   public void selectNext() {
      if (this.getCurrentPageIndex() < this.getPageCount() - 1) {
         this.pagination.setCurrentPageIndex(this.getCurrentPageIndex() + 1);
      }

   }

   public void selectPrevious() {
      if (this.getCurrentPageIndex() > 0) {
         this.pagination.setCurrentPageIndex(this.getCurrentPageIndex() - 1);
      }

   }

   private void initializeSwipeAndTouchHandlers() {
      Pagination var1 = (Pagination)this.getSkinnable();
      ((Pagination)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_PRESSED, (var1x) -> {
         if (this.touchEventId == -1) {
            this.touchEventId = var1x.getTouchPoint().getId();
         }

         if (this.touchEventId == var1x.getTouchPoint().getId()) {
            this.lastTouchPos = this.startTouchPos = var1x.getTouchPoint().getX();
            this.lastTouchTime = this.startTouchTime = System.currentTimeMillis();
            this.touchThresholdBroken = false;
            var1x.consume();
         }
      });
      ((Pagination)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_MOVED, (var2) -> {
         if (this.touchEventId == var2.getTouchPoint().getId()) {
            double var3 = var2.getTouchPoint().getX() - this.lastTouchPos;
            long var5 = System.currentTimeMillis() - this.lastTouchTime;
            this.touchVelocity = var3 / (double)var5;
            this.lastTouchPos = var2.getTouchPoint().getX();
            this.lastTouchTime = System.currentTimeMillis();
            double var7 = var2.getTouchPoint().getX() - this.startTouchPos;
            if (!this.touchThresholdBroken && Math.abs(var7) > 15.0) {
               this.touchThresholdBroken = true;
            }

            if (this.touchThresholdBroken) {
               double var9 = var1.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
               if (!this.setInitialDirection) {
                  this.setInitialDirection = true;
                  this.direction = var7 < 0.0 ? 1 : -1;
               }

               double var11;
               double var13;
               if (var7 < 0.0) {
                  if (this.direction == -1) {
                     this.nextStackPane.getChildren().clear();
                     this.direction = 1;
                  }

                  if (Math.abs(var7) <= var9) {
                     var11 = var7;
                     var13 = var9 + var7;
                     this.nextPageReached = false;
                  } else {
                     var11 = -var9;
                     var13 = 0.0;
                     this.nextPageReached = true;
                  }

                  this.currentStackPane.setTranslateX(var11);
                  if (this.getCurrentPageIndex() < this.getPageCount() - 1) {
                     this.createPage(this.nextStackPane, this.currentIndex + 1);
                     this.nextStackPane.setVisible(true);
                     this.nextStackPane.setTranslateX(var13);
                  } else {
                     this.currentStackPane.setTranslateX(0.0);
                  }
               } else {
                  if (this.direction == 1) {
                     this.nextStackPane.getChildren().clear();
                     this.direction = -1;
                  }

                  if (Math.abs(var7) <= var9) {
                     var11 = var7;
                     var13 = -var9 + var7;
                     this.nextPageReached = false;
                  } else {
                     var11 = var9;
                     var13 = 0.0;
                     this.nextPageReached = true;
                  }

                  this.currentStackPane.setTranslateX(var11);
                  if (this.getCurrentPageIndex() != 0) {
                     this.createPage(this.nextStackPane, this.currentIndex - 1);
                     this.nextStackPane.setVisible(true);
                     this.nextStackPane.setTranslateX(var13);
                  } else {
                     this.currentStackPane.setTranslateX(0.0);
                  }
               }
            }

            var2.consume();
         }
      });
      ((Pagination)this.getSkinnable()).addEventHandler(TouchEvent.TOUCH_RELEASED, (var2) -> {
         if (this.touchEventId == var2.getTouchPoint().getId()) {
            this.touchEventId = -1;
            this.setInitialDirection = false;
            if (this.touchThresholdBroken) {
               double var3 = var2.getTouchPoint().getX() - this.startTouchPos;
               long var5 = System.currentTimeMillis() - this.startTouchTime;
               boolean var7 = var5 < 300L;
               double var8 = var7 ? var3 / (double)var5 : this.touchVelocity;
               double var10 = var8 * 500.0;
               double var12 = var1.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
               double var14 = Math.abs(var10 / var12);
               double var16 = Math.abs(var3 / var12);
               if (!(var14 > 0.3) && !(var16 > 0.3)) {
                  this.animateClamping(this.startTouchPos > var2.getTouchPoint().getSceneX());
               } else if (this.startTouchPos > var2.getTouchPoint().getX()) {
                  this.selectNext();
               } else {
                  this.selectPrevious();
               }
            }

            var2.consume();
         }
      });
   }

   private void resetIndexes(boolean var1) {
      this.maxPageIndicatorCount = this.getMaxPageIndicatorCount();
      this.pageCount = this.getPageCount();
      if (this.pageCount > this.maxPageIndicatorCount) {
         this.pageCount = this.maxPageIndicatorCount;
      }

      this.fromIndex = 0;
      this.previousIndex = 0;
      this.currentIndex = var1 ? this.getCurrentPageIndex() : 0;
      this.toIndex = this.pageCount - 1;
      if (this.pageCount == Integer.MAX_VALUE && this.maxPageIndicatorCount == Integer.MAX_VALUE) {
         this.toIndex = 0;
      }

      boolean var2 = this.animate;
      if (var2) {
         this.animate = false;
      }

      this.currentStackPane.getChildren().clear();
      this.nextStackPane.getChildren().clear();
      this.pagination.setCurrentPageIndex(this.currentIndex);
      this.createPage(this.currentStackPane, this.currentIndex);
      if (var2) {
         this.animate = true;
      }

   }

   private boolean createPage(StackPane var1, int var2) {
      if (this.pagination.getPageFactory() != null && var1.getChildren().isEmpty()) {
         Node var3 = (Node)this.pagination.getPageFactory().call(var2);
         if (var3 != null) {
            var1.getChildren().setAll((Object[])(var3));
            return true;
         } else {
            boolean var4 = this.animate;
            if (var4) {
               this.animate = false;
            }

            if (this.pagination.getPageFactory().call(this.previousIndex) != null) {
               this.pagination.setCurrentPageIndex(this.previousIndex);
            } else {
               this.pagination.setCurrentPageIndex(0);
            }

            if (var4) {
               this.animate = true;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private int getPageCount() {
      return ((Pagination)this.getSkinnable()).getPageCount() < 1 ? 1 : ((Pagination)this.getSkinnable()).getPageCount();
   }

   private int getMaxPageIndicatorCount() {
      return ((Pagination)this.getSkinnable()).getMaxPageIndicatorCount();
   }

   private int getCurrentPageIndex() {
      return ((Pagination)this.getSkinnable()).getCurrentPageIndex();
   }

   private void animateSwitchPage() {
      if (this.timeline != null) {
         this.timeline.setRate(8.0);
         this.hasPendingAnimation = true;
      } else if (this.nextStackPane.isVisible() || this.createPage(this.nextStackPane, this.currentAnimatedIndex)) {
         if (this.nextPageReached) {
            this.swapPanes();
            this.nextPageReached = false;
         } else {
            this.nextStackPane.setCache(true);
            this.currentStackPane.setCache(true);
            Platform.runLater(() -> {
               boolean var1 = this.nextStackPane.getTranslateX() != 0.0;
               KeyFrame var2;
               KeyFrame var3;
               if (this.currentAnimatedIndex > this.previousIndex) {
                  if (!var1) {
                     this.nextStackPane.setTranslateX(this.currentStackPane.getWidth());
                  }

                  this.nextStackPane.setVisible(true);
                  this.timeline = new Timeline();
                  var2 = new KeyFrame(Duration.millis(0.0), new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), var1 ? this.currentStackPane.getTranslateX() : 0.0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), var1 ? this.nextStackPane.getTranslateX() : this.currentStackPane.getWidth(), interpolator)});
                  var3 = new KeyFrame(DURATION, this.swipeAnimationEndEventHandler, new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), -this.currentStackPane.getWidth(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), 0, interpolator)});
                  this.timeline.getKeyFrames().setAll((Object[])(var2, var3));
                  this.timeline.play();
               } else {
                  if (!var1) {
                     this.nextStackPane.setTranslateX(-this.currentStackPane.getWidth());
                  }

                  this.nextStackPane.setVisible(true);
                  this.timeline = new Timeline();
                  var2 = new KeyFrame(Duration.millis(0.0), new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), var1 ? this.currentStackPane.getTranslateX() : 0.0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), var1 ? this.nextStackPane.getTranslateX() : -this.currentStackPane.getWidth(), interpolator)});
                  var3 = new KeyFrame(DURATION, this.swipeAnimationEndEventHandler, new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), this.currentStackPane.getWidth(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), 0, interpolator)});
                  this.timeline.getKeyFrames().setAll((Object[])(var2, var3));
                  this.timeline.play();
               }

            });
         }
      }
   }

   private void swapPanes() {
      StackPane var1 = this.currentStackPane;
      this.currentStackPane = this.nextStackPane;
      this.nextStackPane = var1;
      this.currentStackPane.setTranslateX(0.0);
      this.currentStackPane.setCache(false);
      this.nextStackPane.setTranslateX(0.0);
      this.nextStackPane.setCache(false);
      this.nextStackPane.setVisible(false);
      this.nextStackPane.getChildren().clear();
   }

   private void animateClamping(boolean var1) {
      KeyFrame var2;
      KeyFrame var3;
      if (var1) {
         this.timeline = new Timeline();
         var2 = new KeyFrame(Duration.millis(0.0), new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), this.currentStackPane.getTranslateX(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), this.nextStackPane.getTranslateX(), interpolator)});
         var3 = new KeyFrame(DURATION, this.clampAnimationEndEventHandler, new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), 0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), this.currentStackPane.getWidth(), interpolator)});
         this.timeline.getKeyFrames().setAll((Object[])(var2, var3));
         this.timeline.play();
      } else {
         this.timeline = new Timeline();
         var2 = new KeyFrame(Duration.millis(0.0), new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), this.currentStackPane.getTranslateX(), interpolator), new KeyValue(this.nextStackPane.translateXProperty(), this.nextStackPane.getTranslateX(), interpolator)});
         var3 = new KeyFrame(DURATION, this.clampAnimationEndEventHandler, new KeyValue[]{new KeyValue(this.currentStackPane.translateXProperty(), 0, interpolator), new KeyValue(this.nextStackPane.translateXProperty(), -this.currentStackPane.getWidth(), interpolator)});
         this.timeline.getKeyFrames().setAll((Object[])(var2, var3));
         this.timeline.play();
      }

   }

   private DoubleProperty arrowButtonGapProperty() {
      return this.arrowButtonGap;
   }

   public final void setArrowsVisible(boolean var1) {
      this.arrowsVisibleProperty().set(var1);
   }

   public final boolean isArrowsVisible() {
      return this.arrowsVisible == null ? DEFAULT_ARROW_VISIBLE : this.arrowsVisible.get();
   }

   public final BooleanProperty arrowsVisibleProperty() {
      if (this.arrowsVisible == null) {
         this.arrowsVisible = new StyleableBooleanProperty(DEFAULT_ARROW_VISIBLE) {
            protected void invalidated() {
               ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return PaginationSkin.StyleableProperties.ARROWS_VISIBLE;
            }

            public Object getBean() {
               return PaginationSkin.this;
            }

            public String getName() {
               return "arrowVisible";
            }
         };
      }

      return this.arrowsVisible;
   }

   public final void setPageInformationVisible(boolean var1) {
      this.pageInformationVisibleProperty().set(var1);
   }

   public final boolean isPageInformationVisible() {
      return this.pageInformationVisible == null ? DEFAULT_PAGE_INFORMATION_VISIBLE : this.pageInformationVisible.get();
   }

   public final BooleanProperty pageInformationVisibleProperty() {
      if (this.pageInformationVisible == null) {
         this.pageInformationVisible = new StyleableBooleanProperty(DEFAULT_PAGE_INFORMATION_VISIBLE) {
            protected void invalidated() {
               ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return PaginationSkin.StyleableProperties.PAGE_INFORMATION_VISIBLE;
            }

            public Object getBean() {
               return PaginationSkin.this;
            }

            public String getName() {
               return "pageInformationVisible";
            }
         };
      }

      return this.pageInformationVisible;
   }

   public final void setPageInformationAlignment(Side var1) {
      this.pageInformationAlignmentProperty().set(var1);
   }

   public final Side getPageInformationAlignment() {
      return this.pageInformationAlignment == null ? DEFAULT_PAGE_INFORMATION_ALIGNMENT : (Side)this.pageInformationAlignment.get();
   }

   public final ObjectProperty pageInformationAlignmentProperty() {
      if (this.pageInformationAlignment == null) {
         this.pageInformationAlignment = new StyleableObjectProperty(Side.BOTTOM) {
            protected void invalidated() {
               ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return PaginationSkin.StyleableProperties.PAGE_INFORMATION_ALIGNMENT;
            }

            public Object getBean() {
               return PaginationSkin.this;
            }

            public String getName() {
               return "pageInformationAlignment";
            }
         };
      }

      return this.pageInformationAlignment;
   }

   public final void setTooltipVisible(boolean var1) {
      this.tooltipVisibleProperty().set(var1);
   }

   public final boolean isTooltipVisible() {
      return this.tooltipVisible == null ? DEFAULT_TOOLTIP_VISIBLE : this.tooltipVisible.get();
   }

   public final BooleanProperty tooltipVisibleProperty() {
      if (this.tooltipVisible == null) {
         this.tooltipVisible = new StyleableBooleanProperty(DEFAULT_TOOLTIP_VISIBLE) {
            protected void invalidated() {
               ((Pagination)PaginationSkin.this.getSkinnable()).requestLayout();
            }

            public CssMetaData getCssMetaData() {
               return PaginationSkin.StyleableProperties.TOOLTIP_VISIBLE;
            }

            public Object getBean() {
               return PaginationSkin.this;
            }

            public String getName() {
               return "tooltipVisible";
            }
         };
      }

      return this.tooltipVisible;
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("PAGE_FACTORY".equals(var1)) {
         if (this.animate && this.timeline != null) {
            this.timeline.setRate(8.0);
            this.timeline.setOnFinished((var1x) -> {
               this.resetIndiciesAndNav();
            });
            return;
         }

         this.resetIndiciesAndNav();
      } else if ("PAGE_COUNT".equals(var1)) {
         this.resetIndiciesAndNav();
      } else if ("WIDTH".equals(var1)) {
         this.clipRect.setWidth(((Pagination)this.getSkinnable()).getWidth());
      } else if ("HEIGHT".equals(var1)) {
         this.clipRect.setHeight(((Pagination)this.getSkinnable()).getHeight());
      }

      ((Pagination)this.getSkinnable()).requestLayout();
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.navigation.isVisible() ? this.snapSize(this.navigation.minWidth(var1)) : 0.0;
      return var9 + Math.max(this.currentStackPane.minWidth(var1), var11) + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.navigation.isVisible() ? this.snapSize(this.navigation.minHeight(var1)) : 0.0;
      return var3 + this.currentStackPane.minHeight(var1) + var11 + var7;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.navigation.isVisible() ? this.snapSize(this.navigation.prefWidth(var1)) : 0.0;
      return var9 + Math.max(this.currentStackPane.prefWidth(var1), var11) + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.navigation.isVisible() ? this.snapSize(this.navigation.prefHeight(var1)) : 0.0;
      return var3 + this.currentStackPane.prefHeight(var1) + var11 + var7;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      double var9 = this.navigation.isVisible() ? this.snapSize(this.navigation.prefHeight(-1.0)) : 0.0;
      double var11 = this.snapSize(var7 - var9);
      this.layoutInArea(this.currentStackPane, var1, var3, var5, var11, 0.0, HPos.CENTER, VPos.CENTER);
      this.layoutInArea(this.nextStackPane, var1, var3, var5, var11, 0.0, HPos.CENTER, VPos.CENTER);
      this.layoutInArea(this.navigation, var1, var11, var5, var9, 0.0, HPos.CENTER, VPos.CENTER);
   }

   protected Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case FOCUS_ITEM:
            return this.navigation.indicatorButtons.getSelectedToggle();
         case ITEM_COUNT:
            return this.navigation.indicatorButtons.getToggles().size();
         case ITEM_AT_INDEX:
            Integer var3 = (Integer)var2[0];
            if (var3 == null) {
               return null;
            }

            return this.navigation.indicatorButtons.getToggles().get(var3);
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public static List getClassCssMetaData() {
      return PaginationSkin.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   static {
      DEFAULT_ARROW_VISIBLE = Boolean.FALSE;
      DEFAULT_PAGE_INFORMATION_VISIBLE = Boolean.FALSE;
      DEFAULT_PAGE_INFORMATION_ALIGNMENT = Side.BOTTOM;
      DEFAULT_TOOLTIP_VISIBLE = Boolean.FALSE;
   }

   private static class StyleableProperties {
      private static final CssMetaData ARROWS_VISIBLE;
      private static final CssMetaData PAGE_INFORMATION_VISIBLE;
      private static final CssMetaData PAGE_INFORMATION_ALIGNMENT;
      private static final CssMetaData TOOLTIP_VISIBLE;
      private static final CssMetaData ARROW_BUTTON_GAP;
      private static final List STYLEABLES;

      static {
         ARROWS_VISIBLE = new CssMetaData("-fx-arrows-visible", BooleanConverter.getInstance(), PaginationSkin.DEFAULT_ARROW_VISIBLE) {
            public boolean isSettable(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return var2.arrowsVisible == null || !var2.arrowsVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return (StyleableProperty)var2.arrowsVisibleProperty();
            }
         };
         PAGE_INFORMATION_VISIBLE = new CssMetaData("-fx-page-information-visible", BooleanConverter.getInstance(), PaginationSkin.DEFAULT_PAGE_INFORMATION_VISIBLE) {
            public boolean isSettable(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return var2.pageInformationVisible == null || !var2.pageInformationVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return (StyleableProperty)var2.pageInformationVisibleProperty();
            }
         };
         PAGE_INFORMATION_ALIGNMENT = new CssMetaData("-fx-page-information-alignment", new EnumConverter(Side.class), PaginationSkin.DEFAULT_PAGE_INFORMATION_ALIGNMENT) {
            public boolean isSettable(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return var2.pageInformationAlignment == null || !var2.pageInformationAlignment.isBound();
            }

            public StyleableProperty getStyleableProperty(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return (StyleableProperty)var2.pageInformationAlignmentProperty();
            }
         };
         TOOLTIP_VISIBLE = new CssMetaData("-fx-tooltip-visible", BooleanConverter.getInstance(), PaginationSkin.DEFAULT_TOOLTIP_VISIBLE) {
            public boolean isSettable(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return var2.tooltipVisible == null || !var2.tooltipVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return (StyleableProperty)var2.tooltipVisibleProperty();
            }
         };
         ARROW_BUTTON_GAP = new CssMetaData("-fx-arrow-button-gap", SizeConverter.getInstance(), 4) {
            public boolean isSettable(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return var2.arrowButtonGap == null || !var2.arrowButtonGap.isBound();
            }

            public StyleableProperty getStyleableProperty(Pagination var1) {
               PaginationSkin var2 = (PaginationSkin)var1.getSkin();
               return (StyleableProperty)var2.arrowButtonGapProperty();
            }
         };
         ArrayList var0 = new ArrayList(SkinBase.getClassCssMetaData());
         var0.add(ARROWS_VISIBLE);
         var0.add(PAGE_INFORMATION_VISIBLE);
         var0.add(PAGE_INFORMATION_ALIGNMENT);
         var0.add(TOOLTIP_VISIBLE);
         var0.add(ARROW_BUTTON_GAP);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   class IndicatorButton extends ToggleButton {
      private final ListChangeListener updateSkinIndicatorType = (var1x) -> {
         this.setIndicatorType();
      };
      private final ChangeListener updateTooltipVisibility = (var1x, var2x, var3) -> {
         this.setTooltipVisible(var3);
      };
      private int pageNumber;

      public IndicatorButton(int var2) {
         this.pageNumber = var2;
         this.setFocusTraversable(false);
         this.setIndicatorType();
         this.setTooltipVisible(PaginationSkin.this.isTooltipVisible());
         ((Pagination)PaginationSkin.this.getSkinnable()).getStyleClass().addListener(this.updateSkinIndicatorType);
         this.setOnAction(new EventHandler() {
            public void handle(ActionEvent var1) {
               int var2 = PaginationSkin.this.getCurrentPageIndex();
               if (var2 != IndicatorButton.this.pageNumber) {
                  PaginationSkin.this.pagination.setCurrentPageIndex(IndicatorButton.this.pageNumber);
                  IndicatorButton.this.requestLayout();
               }

            }
         });
         PaginationSkin.this.tooltipVisibleProperty().addListener(this.updateTooltipVisibility);
         this.prefHeightProperty().bind(this.minHeightProperty());
         this.setAccessibleRole(AccessibleRole.PAGE_ITEM);
      }

      private void setIndicatorType() {
         if (((Pagination)PaginationSkin.this.getSkinnable()).getStyleClass().contains("bullet")) {
            this.getStyleClass().remove("number-button");
            this.getStyleClass().add("bullet-button");
            this.setText((String)null);
            this.prefWidthProperty().bind(this.minWidthProperty());
         } else {
            this.getStyleClass().remove("bullet-button");
            this.getStyleClass().add("number-button");
            this.setText(Integer.toString(this.pageNumber + 1));
            this.prefWidthProperty().unbind();
         }

      }

      private void setTooltipVisible(boolean var1) {
         if (var1) {
            this.setTooltip(new Tooltip(Integer.toString(this.pageNumber + 1)));
         } else {
            this.setTooltip((Tooltip)null);
         }

      }

      public int getPageNumber() {
         return this.pageNumber;
      }

      public void fire() {
         if (this.getToggleGroup() == null || !this.isSelected()) {
            super.fire();
         }

      }

      public void release() {
         ((Pagination)PaginationSkin.this.getSkinnable()).getStyleClass().removeListener(this.updateSkinIndicatorType);
         PaginationSkin.this.tooltipVisibleProperty().removeListener(this.updateTooltipVisibility);
      }

      public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
         switch (var1) {
            case TEXT:
               return this.getText();
            case SELECTED:
               return this.isSelected();
            default:
               return super.queryAccessibleAttribute(var1, var2);
         }
      }

      public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
         switch (var1) {
            case REQUEST_FOCUS:
               ((Pagination)PaginationSkin.this.getSkinnable()).setCurrentPageIndex(this.pageNumber);
               break;
            default:
               super.executeAccessibleAction(var1, new Object[0]);
         }

      }
   }

   class NavigationControl extends StackPane {
      private HBox controlBox;
      private Button leftArrowButton;
      private StackPane leftArrow;
      private Button rightArrowButton;
      private StackPane rightArrow;
      private ToggleGroup indicatorButtons;
      private Label pageInformation;
      private double previousWidth = -1.0;
      private double minButtonSize = -1.0;
      private int previousIndicatorCount = 0;

      public NavigationControl() {
         this.getStyleClass().setAll((Object[])("pagination-control"));
         this.addEventHandler(MouseEvent.MOUSE_PRESSED, (var1x) -> {
            ((PaginationBehavior)PaginationSkin.this.getBehavior()).mousePressed(var1x);
         });
         this.addEventHandler(MouseEvent.MOUSE_RELEASED, (var1x) -> {
            ((PaginationBehavior)PaginationSkin.this.getBehavior()).mouseReleased(var1x);
         });
         this.addEventHandler(MouseEvent.MOUSE_ENTERED, (var1x) -> {
            ((PaginationBehavior)PaginationSkin.this.getBehavior()).mouseEntered(var1x);
         });
         this.addEventHandler(MouseEvent.MOUSE_EXITED, (var1x) -> {
            ((PaginationBehavior)PaginationSkin.this.getBehavior()).mouseExited(var1x);
         });
         this.controlBox = new HBox();
         this.controlBox.getStyleClass().add("control-box");
         this.leftArrowButton = new Button();
         this.leftArrowButton.setAccessibleText(ControlResources.getString("Accessibility.title.Pagination.PreviousButton"));
         this.minButtonSize = this.leftArrowButton.getFont().getSize() * 2.0;
         this.leftArrowButton.fontProperty().addListener((var1x, var2, var3) -> {
            this.minButtonSize = var3.getSize() * 2.0;
            Iterator var4 = this.controlBox.getChildren().iterator();

            while(var4.hasNext()) {
               Node var5 = (Node)var4.next();
               ((Control)var5).setMinSize(this.minButtonSize, this.minButtonSize);
            }

            this.requestLayout();
         });
         this.leftArrowButton.setMinSize(this.minButtonSize, this.minButtonSize);
         this.leftArrowButton.prefWidthProperty().bind(this.leftArrowButton.minWidthProperty());
         this.leftArrowButton.prefHeightProperty().bind(this.leftArrowButton.minHeightProperty());
         this.leftArrowButton.getStyleClass().add("left-arrow-button");
         this.leftArrowButton.setFocusTraversable(false);
         HBox.setMargin(this.leftArrowButton, new Insets(0.0, this.snapSize(PaginationSkin.this.arrowButtonGap.get()), 0.0, 0.0));
         this.leftArrow = new StackPane();
         this.leftArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
         this.leftArrowButton.setGraphic(this.leftArrow);
         this.leftArrow.getStyleClass().add("left-arrow");
         this.rightArrowButton = new Button();
         this.rightArrowButton.setAccessibleText(ControlResources.getString("Accessibility.title.Pagination.NextButton"));
         this.rightArrowButton.setMinSize(this.minButtonSize, this.minButtonSize);
         this.rightArrowButton.prefWidthProperty().bind(this.rightArrowButton.minWidthProperty());
         this.rightArrowButton.prefHeightProperty().bind(this.rightArrowButton.minHeightProperty());
         this.rightArrowButton.getStyleClass().add("right-arrow-button");
         this.rightArrowButton.setFocusTraversable(false);
         HBox.setMargin(this.rightArrowButton, new Insets(0.0, 0.0, 0.0, this.snapSize(PaginationSkin.this.arrowButtonGap.get())));
         this.rightArrow = new StackPane();
         this.rightArrow.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
         this.rightArrowButton.setGraphic(this.rightArrow);
         this.rightArrow.getStyleClass().add("right-arrow");
         this.indicatorButtons = new ToggleGroup();
         this.pageInformation = new Label();
         this.pageInformation.getStyleClass().add("page-information");
         this.getChildren().addAll(this.controlBox, this.pageInformation);
         this.initializeNavigationHandlers();
         this.initializePageIndicators();
         this.updatePageIndex();
         PaginationSkin.this.arrowButtonGap.addListener((var1x, var2, var3) -> {
            if (var3.doubleValue() == 0.0) {
               HBox.setMargin(this.leftArrowButton, (Insets)null);
               HBox.setMargin(this.rightArrowButton, (Insets)null);
            } else {
               HBox.setMargin(this.leftArrowButton, new Insets(0.0, this.snapSize(var3.doubleValue()), 0.0, 0.0));
               HBox.setMargin(this.rightArrowButton, new Insets(0.0, 0.0, 0.0, this.snapSize(var3.doubleValue())));
            }

         });
      }

      private void initializeNavigationHandlers() {
         this.leftArrowButton.setOnAction((var1) -> {
            PaginationSkin.this.selectPrevious();
            this.requestLayout();
         });
         this.rightArrowButton.setOnAction((var1) -> {
            PaginationSkin.this.selectNext();
            this.requestLayout();
         });
         PaginationSkin.this.pagination.currentPageIndexProperty().addListener((var1, var2, var3) -> {
            PaginationSkin.this.previousIndex = var2.intValue();
            PaginationSkin.this.currentIndex = var3.intValue();
            this.updatePageIndex();
            if (PaginationSkin.this.animate) {
               PaginationSkin.this.currentAnimatedIndex = PaginationSkin.this.currentIndex;
               PaginationSkin.this.animateSwitchPage();
            } else {
               PaginationSkin.this.createPage(PaginationSkin.this.currentStackPane, PaginationSkin.this.currentIndex);
            }

         });
      }

      private void initializePageIndicators() {
         this.previousIndicatorCount = 0;
         this.controlBox.getChildren().clear();
         this.clearIndicatorButtons();
         this.controlBox.getChildren().add(this.leftArrowButton);

         for(int var1 = PaginationSkin.this.fromIndex; var1 <= PaginationSkin.this.toIndex; ++var1) {
            IndicatorButton var2 = PaginationSkin.this.new IndicatorButton(var1);
            var2.setMinSize(this.minButtonSize, this.minButtonSize);
            var2.setToggleGroup(this.indicatorButtons);
            this.controlBox.getChildren().add(var2);
         }

         this.controlBox.getChildren().add(this.rightArrowButton);
      }

      private void clearIndicatorButtons() {
         Iterator var1 = this.indicatorButtons.getToggles().iterator();

         while(var1.hasNext()) {
            Toggle var2 = (Toggle)var1.next();
            if (var2 instanceof IndicatorButton) {
               IndicatorButton var3 = (IndicatorButton)var2;
               var3.release();
            }
         }

         this.indicatorButtons.getToggles().clear();
      }

      private void updatePageIndicators() {
         for(int var1 = 0; var1 < this.indicatorButtons.getToggles().size(); ++var1) {
            IndicatorButton var2 = (IndicatorButton)this.indicatorButtons.getToggles().get(var1);
            if (var2.getPageNumber() == PaginationSkin.this.currentIndex) {
               var2.setSelected(true);
               this.updatePageInformation();
               break;
            }
         }

         ((Pagination)PaginationSkin.this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
      }

      private void updatePageIndex() {
         if (PaginationSkin.this.pageCount == PaginationSkin.this.maxPageIndicatorCount && this.changePageSet()) {
            this.initializePageIndicators();
         }

         this.updatePageIndicators();
         this.requestLayout();
      }

      private void updatePageInformation() {
         String var1 = Integer.toString(PaginationSkin.this.currentIndex + 1);
         String var2 = PaginationSkin.this.getPageCount() == Integer.MAX_VALUE ? "..." : Integer.toString(PaginationSkin.this.getPageCount());
         this.pageInformation.setText(var1 + "/" + var2);
      }

      private void layoutPageIndicators() {
         double var1 = this.snappedLeftInset();
         double var3 = this.snappedRightInset();
         double var5 = this.snapSize(this.getWidth()) - (var1 + var3);
         double var7 = this.controlBox.snappedLeftInset();
         double var9 = this.controlBox.snappedRightInset();
         double var11 = this.snapSize(Utils.boundedSize(this.leftArrowButton.prefWidth(-1.0), this.leftArrowButton.minWidth(-1.0), this.leftArrowButton.maxWidth(-1.0)));
         double var13 = this.snapSize(Utils.boundedSize(this.rightArrowButton.prefWidth(-1.0), this.rightArrowButton.minWidth(-1.0), this.rightArrowButton.maxWidth(-1.0)));
         double var15 = this.snapSize(this.controlBox.getSpacing());
         double var17 = var5 - (var7 + var11 + 2.0 * PaginationSkin.this.arrowButtonGap.get() + var15 + var13 + var9);
         if (PaginationSkin.this.isPageInformationVisible() && (Side.LEFT.equals(PaginationSkin.this.getPageInformationAlignment()) || Side.RIGHT.equals(PaginationSkin.this.getPageInformationAlignment()))) {
            var17 -= this.snapSize(this.pageInformation.prefWidth(-1.0));
         }

         double var19 = 0.0;
         int var21 = 0;

         int var22;
         for(var22 = 0; var22 < PaginationSkin.this.getMaxPageIndicatorCount(); ++var22) {
            int var23 = var22 < this.indicatorButtons.getToggles().size() ? var22 : this.indicatorButtons.getToggles().size() - 1;
            double var24 = this.minButtonSize;
            if (var23 != -1) {
               IndicatorButton var26 = (IndicatorButton)this.indicatorButtons.getToggles().get(var23);
               var24 = this.snapSize(Utils.boundedSize(var26.prefWidth(-1.0), var26.minWidth(-1.0), var26.maxWidth(-1.0)));
            }

            var19 += var24 + var15;
            if (var19 > var17) {
               break;
            }

            ++var21;
         }

         if (var21 == 0) {
            var21 = 1;
         }

         if (var21 != this.previousIndicatorCount) {
            if (var21 < PaginationSkin.this.getMaxPageIndicatorCount()) {
               PaginationSkin.this.maxPageIndicatorCount = var21;
            } else {
               PaginationSkin.this.maxPageIndicatorCount = PaginationSkin.this.getMaxPageIndicatorCount();
            }

            if (PaginationSkin.this.pageCount > PaginationSkin.this.maxPageIndicatorCount) {
               PaginationSkin.this.pageCount = PaginationSkin.this.maxPageIndicatorCount;
               var22 = PaginationSkin.this.maxPageIndicatorCount - 1;
            } else if (var21 > PaginationSkin.this.getPageCount()) {
               PaginationSkin.this.pageCount = PaginationSkin.this.getPageCount();
               var22 = PaginationSkin.this.getPageCount() - 1;
            } else {
               PaginationSkin.this.pageCount = var21;
               var22 = var21 - 1;
            }

            if (PaginationSkin.this.currentIndex >= PaginationSkin.this.toIndex) {
               PaginationSkin.this.toIndex = PaginationSkin.this.currentIndex;
               PaginationSkin.this.fromIndex = PaginationSkin.this.toIndex - var22;
            } else if (PaginationSkin.this.currentIndex <= PaginationSkin.this.fromIndex) {
               PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex;
               PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + var22;
            } else {
               PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + var22;
            }

            if (PaginationSkin.this.toIndex > PaginationSkin.this.getPageCount() - 1) {
               PaginationSkin.this.toIndex = PaginationSkin.this.getPageCount() - 1;
            }

            if (PaginationSkin.this.fromIndex < 0) {
               PaginationSkin.this.fromIndex = 0;
               PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + var22;
            }

            this.initializePageIndicators();
            this.updatePageIndicators();
            this.previousIndicatorCount = var21;
         }

      }

      private boolean changePageSet() {
         int var1 = this.indexToIndicatorButtonsIndex(PaginationSkin.this.currentIndex);
         int var2 = PaginationSkin.this.maxPageIndicatorCount - 1;
         if (PaginationSkin.this.previousIndex < PaginationSkin.this.currentIndex && var1 == 0 && var2 != 0 && var1 % var2 == 0) {
            PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex;
            PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + var2;
         } else if (PaginationSkin.this.currentIndex < PaginationSkin.this.previousIndex && var1 == var2 && var2 != 0 && var1 % var2 == 0) {
            PaginationSkin.this.toIndex = PaginationSkin.this.currentIndex;
            PaginationSkin.this.fromIndex = PaginationSkin.this.toIndex - var2;
         } else {
            if (PaginationSkin.this.currentIndex >= PaginationSkin.this.fromIndex && PaginationSkin.this.currentIndex <= PaginationSkin.this.toIndex) {
               return false;
            }

            PaginationSkin.this.fromIndex = PaginationSkin.this.currentIndex - var1;
            PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + var2;
         }

         if (PaginationSkin.this.toIndex > PaginationSkin.this.getPageCount() - 1) {
            if (PaginationSkin.this.fromIndex > PaginationSkin.this.getPageCount() - 1) {
               return false;
            }

            PaginationSkin.this.toIndex = PaginationSkin.this.getPageCount() - 1;
         }

         if (PaginationSkin.this.fromIndex < 0) {
            PaginationSkin.this.fromIndex = 0;
            PaginationSkin.this.toIndex = PaginationSkin.this.fromIndex + var2;
         }

         return true;
      }

      private int indexToIndicatorButtonsIndex(int var1) {
         if (var1 >= PaginationSkin.this.fromIndex && var1 <= PaginationSkin.this.toIndex) {
            return var1 - PaginationSkin.this.fromIndex;
         } else {
            int var2 = 0;
            int var3 = PaginationSkin.this.fromIndex;
            int var4 = PaginationSkin.this.toIndex;
            if (PaginationSkin.this.currentIndex > PaginationSkin.this.previousIndex) {
               while(var3 < PaginationSkin.this.getPageCount() && var4 < PaginationSkin.this.getPageCount()) {
                  var3 += var2;
                  var4 += var2;
                  if (var1 >= var3 && var1 <= var4) {
                     if (var1 == var3) {
                        return 0;
                     }

                     if (var1 == var4) {
                        return PaginationSkin.this.maxPageIndicatorCount - 1;
                     }

                     return var1 - var3;
                  }

                  var2 += PaginationSkin.this.maxPageIndicatorCount;
               }
            } else {
               while(var3 > 0 && var4 > 0) {
                  var3 -= var2;
                  var4 -= var2;
                  if (var1 >= var3 && var1 <= var4) {
                     if (var1 == var3) {
                        return 0;
                     }

                     if (var1 == var4) {
                        return PaginationSkin.this.maxPageIndicatorCount - 1;
                     }

                     return var1 - var3;
                  }

                  var2 += PaginationSkin.this.maxPageIndicatorCount;
               }
            }

            return PaginationSkin.this.maxPageIndicatorCount - 1;
         }
      }

      private Pos sideToPos(Side var1) {
         if (Side.TOP.equals(var1)) {
            return Pos.TOP_CENTER;
         } else if (Side.RIGHT.equals(var1)) {
            return Pos.CENTER_RIGHT;
         } else {
            return Side.BOTTOM.equals(var1) ? Pos.BOTTOM_CENTER : Pos.CENTER_LEFT;
         }
      }

      protected double computeMinWidth(double var1) {
         double var3 = this.snappedLeftInset();
         double var5 = this.snappedRightInset();
         double var7 = this.snapSize(Utils.boundedSize(this.leftArrowButton.prefWidth(-1.0), this.leftArrowButton.minWidth(-1.0), this.leftArrowButton.maxWidth(-1.0)));
         double var9 = this.snapSize(Utils.boundedSize(this.rightArrowButton.prefWidth(-1.0), this.rightArrowButton.minWidth(-1.0), this.rightArrowButton.maxWidth(-1.0)));
         double var11 = this.snapSize(this.controlBox.getSpacing());
         double var13 = 0.0;
         Side var15 = PaginationSkin.this.getPageInformationAlignment();
         if (Side.LEFT.equals(var15) || Side.RIGHT.equals(var15)) {
            var13 = this.snapSize(this.pageInformation.prefWidth(-1.0));
         }

         double var16 = PaginationSkin.this.arrowButtonGap.get();
         return var3 + var7 + 2.0 * var16 + this.minButtonSize + 2.0 * var11 + var9 + var5 + var13;
      }

      protected double computeMinHeight(double var1) {
         return this.computePrefHeight(var1);
      }

      protected double computePrefWidth(double var1) {
         double var3 = this.snappedLeftInset();
         double var5 = this.snappedRightInset();
         double var7 = this.snapSize(this.controlBox.prefWidth(var1));
         double var9 = 0.0;
         Side var11 = PaginationSkin.this.getPageInformationAlignment();
         if (Side.LEFT.equals(var11) || Side.RIGHT.equals(var11)) {
            var9 = this.snapSize(this.pageInformation.prefWidth(-1.0));
         }

         return var3 + var7 + var5 + var9;
      }

      protected double computePrefHeight(double var1) {
         double var3 = this.snappedTopInset();
         double var5 = this.snappedBottomInset();
         double var7 = this.snapSize(this.controlBox.prefHeight(var1));
         double var9 = 0.0;
         Side var11 = PaginationSkin.this.getPageInformationAlignment();
         if (Side.TOP.equals(var11) || Side.BOTTOM.equals(var11)) {
            var9 = this.snapSize(this.pageInformation.prefHeight(-1.0));
         }

         return var3 + var7 + var9 + var5;
      }

      protected void layoutChildren() {
         double var1 = this.snappedTopInset();
         double var3 = this.snappedBottomInset();
         double var5 = this.snappedLeftInset();
         double var7 = this.snappedRightInset();
         double var9 = this.snapSize(this.getWidth()) - (var5 + var7);
         double var11 = this.snapSize(this.getHeight()) - (var1 + var3);
         double var13 = this.snapSize(this.controlBox.prefWidth(-1.0));
         double var15 = this.snapSize(this.controlBox.prefHeight(-1.0));
         double var17 = this.snapSize(this.pageInformation.prefWidth(-1.0));
         double var19 = this.snapSize(this.pageInformation.prefHeight(-1.0));
         this.leftArrowButton.setDisable(false);
         this.rightArrowButton.setDisable(false);
         if (PaginationSkin.this.currentIndex == 0) {
            this.leftArrowButton.setDisable(true);
         }

         if (PaginationSkin.this.currentIndex == PaginationSkin.this.getPageCount() - 1) {
            this.rightArrowButton.setDisable(true);
         }

         this.applyCss();
         this.leftArrowButton.setVisible(PaginationSkin.this.isArrowsVisible());
         this.rightArrowButton.setVisible(PaginationSkin.this.isArrowsVisible());
         this.pageInformation.setVisible(PaginationSkin.this.isPageInformationVisible());
         this.layoutPageIndicators();
         this.previousWidth = this.getWidth();
         HPos var21 = this.controlBox.getAlignment().getHpos();
         VPos var22 = this.controlBox.getAlignment().getVpos();
         double var23 = var5 + Utils.computeXOffset(var9, var13, var21);
         double var25 = var1 + Utils.computeYOffset(var11, var15, var22);
         if (PaginationSkin.this.isPageInformationVisible()) {
            Pos var27 = this.sideToPos(PaginationSkin.this.getPageInformationAlignment());
            HPos var28 = var27.getHpos();
            VPos var29 = var27.getVpos();
            double var30 = var5 + Utils.computeXOffset(var9, var17, var28);
            double var32 = var1 + Utils.computeYOffset(var11, var19, var29);
            if (Side.TOP.equals(PaginationSkin.this.getPageInformationAlignment())) {
               var32 = var1;
               var25 = var1 + var19;
            } else if (Side.RIGHT.equals(PaginationSkin.this.getPageInformationAlignment())) {
               var30 = var9 - var7 - var17;
            } else if (Side.BOTTOM.equals(PaginationSkin.this.getPageInformationAlignment())) {
               var25 = var1;
               var32 = var1 + var15;
            } else if (Side.LEFT.equals(PaginationSkin.this.getPageInformationAlignment())) {
               var30 = var5;
            }

            this.layoutInArea(this.pageInformation, var30, var32, var17, var19, 0.0, var28, var29);
         }

         this.layoutInArea(this.controlBox, var23, var25, var13, var15, 0.0, var21, var22);
      }
   }
}
