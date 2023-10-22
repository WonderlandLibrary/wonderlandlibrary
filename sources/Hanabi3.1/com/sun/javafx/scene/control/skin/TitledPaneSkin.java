package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.TitledPaneBehavior;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Labeled;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class TitledPaneSkin extends LabeledSkinBase {
   public static final Duration TRANSITION_DURATION = new Duration(350.0);
   private static final boolean CACHE_ANIMATION = PlatformUtil.isEmbedded();
   private final TitleRegion titleRegion = new TitleRegion();
   private final StackPane contentContainer = new StackPane() {
      {
         this.getStyleClass().setAll((Object[])("content"));
         if (TitledPaneSkin.this.content != null) {
            this.getChildren().setAll((Object[])(TitledPaneSkin.this.content));
         }

      }
   };
   private Node content = ((TitledPane)this.getSkinnable()).getContent();
   private Timeline timeline;
   private double transitionStartValue = 0.0;
   private Rectangle clipRect = new Rectangle();
   private Pos pos;
   private HPos hpos;
   private VPos vpos;
   private DoubleProperty transition;
   private double prefHeightFromAccordion = 0.0;

   public TitledPaneSkin(TitledPane var1) {
      super(var1, new TitledPaneBehavior(var1));
      this.contentContainer.setClip(this.clipRect);
      if (var1.isExpanded()) {
         this.setTransition(1.0);
         this.setExpanded(var1.isExpanded());
      } else {
         this.setTransition(0.0);
         if (this.content != null) {
            this.content.setVisible(false);
         }
      }

      this.getChildren().setAll((Object[])(this.contentContainer, this.titleRegion));
      this.registerChangeListener(var1.contentProperty(), "CONTENT");
      this.registerChangeListener(var1.expandedProperty(), "EXPANDED");
      this.registerChangeListener(var1.collapsibleProperty(), "COLLAPSIBLE");
      this.registerChangeListener(var1.alignmentProperty(), "ALIGNMENT");
      this.registerChangeListener(var1.widthProperty(), "WIDTH");
      this.registerChangeListener(var1.heightProperty(), "HEIGHT");
      this.registerChangeListener(this.titleRegion.alignmentProperty(), "TITLE_REGION_ALIGNMENT");
      this.pos = var1.getAlignment();
      this.hpos = this.pos == null ? HPos.LEFT : this.pos.getHpos();
      this.vpos = this.pos == null ? VPos.CENTER : this.pos.getVpos();
   }

   public StackPane getContentContainer() {
      return this.contentContainer;
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("CONTENT".equals(var1)) {
         this.content = ((TitledPane)this.getSkinnable()).getContent();
         if (this.content == null) {
            this.contentContainer.getChildren().clear();
         } else {
            this.contentContainer.getChildren().setAll((Object[])(this.content));
         }
      } else if ("EXPANDED".equals(var1)) {
         this.setExpanded(((TitledPane)this.getSkinnable()).isExpanded());
      } else if ("COLLAPSIBLE".equals(var1)) {
         this.titleRegion.update();
      } else if ("ALIGNMENT".equals(var1)) {
         this.pos = ((TitledPane)this.getSkinnable()).getAlignment();
         this.hpos = this.pos.getHpos();
         this.vpos = this.pos.getVpos();
      } else if ("TITLE_REGION_ALIGNMENT".equals(var1)) {
         this.pos = this.titleRegion.getAlignment();
         this.hpos = this.pos.getHpos();
         this.vpos = this.pos.getVpos();
      } else if ("WIDTH".equals(var1)) {
         this.clipRect.setWidth(((TitledPane)this.getSkinnable()).getWidth());
      } else if ("HEIGHT".equals(var1)) {
         this.clipRect.setHeight(this.contentContainer.getHeight());
      } else if ("GRAPHIC_TEXT_GAP".equals(var1)) {
         this.titleRegion.requestLayout();
      }

   }

   protected void updateChildren() {
      if (this.titleRegion != null) {
         this.titleRegion.update();
      }

   }

   private void setExpanded(boolean var1) {
      if (!((TitledPane)this.getSkinnable()).isCollapsible()) {
         this.setTransition(1.0);
      } else {
         if (((TitledPane)this.getSkinnable()).isAnimated()) {
            this.transitionStartValue = this.getTransition();
            this.doAnimationTransition();
         } else {
            if (var1) {
               this.setTransition(1.0);
            } else {
               this.setTransition(0.0);
            }

            if (this.content != null) {
               this.content.setVisible(var1);
            }

            ((TitledPane)this.getSkinnable()).requestLayout();
         }

      }
   }

   private void setTransition(double var1) {
      this.transitionProperty().set(var1);
   }

   private double getTransition() {
      return this.transition == null ? 0.0 : this.transition.get();
   }

   private DoubleProperty transitionProperty() {
      if (this.transition == null) {
         this.transition = new SimpleDoubleProperty(this, "transition", 0.0) {
            protected void invalidated() {
               TitledPaneSkin.this.contentContainer.requestLayout();
            }
         };
      }

      return this.transition;
   }

   private boolean isInsideAccordion() {
      return ((TitledPane)this.getSkinnable()).getParent() != null && ((TitledPane)this.getSkinnable()).getParent() instanceof Accordion;
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      double var9 = this.snapSize(this.titleRegion.prefHeight(-1.0));
      this.titleRegion.resize(var5, var9);
      this.positionInArea(this.titleRegion, var1, var3, var5, var9, 0.0, HPos.LEFT, VPos.CENTER);
      double var11 = (var7 - var9) * this.getTransition();
      if (this.isInsideAccordion() && this.prefHeightFromAccordion != 0.0) {
         var11 = (this.prefHeightFromAccordion - var9) * this.getTransition();
      }

      var11 = this.snapSize(var11);
      var3 += this.snapSize(var9);
      this.contentContainer.resize(var5, var11);
      this.clipRect.setHeight(var11);
      this.positionInArea(this.contentContainer, var1, var3, var5, var11, 0.0, HPos.CENTER, VPos.CENTER);
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.snapSize(this.titleRegion.prefWidth(var1));
      double var13 = this.snapSize(this.contentContainer.minWidth(var1));
      return Math.max(var11, var13) + var9 + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.snapSize(this.titleRegion.prefHeight(var1));
      double var13 = this.contentContainer.minHeight(var1) * this.getTransition();
      return var11 + this.snapSize(var13) + var3 + var7;
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.snapSize(this.titleRegion.prefWidth(var1));
      double var13 = this.snapSize(this.contentContainer.prefWidth(var1));
      return Math.max(var11, var13) + var9 + var5;
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      double var11 = this.snapSize(this.titleRegion.prefHeight(var1));
      double var13 = this.contentContainer.prefHeight(var1) * this.getTransition();
      return var11 + this.snapSize(var13) + var3 + var7;
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return Double.MAX_VALUE;
   }

   double getTitleRegionSize(double var1) {
      return this.snapSize(this.titleRegion.prefHeight(var1)) + this.snappedTopInset() + this.snappedBottomInset();
   }

   void setMaxTitledPaneHeightForAccordion(double var1) {
      this.prefHeightFromAccordion = var1;
   }

   double getTitledPaneHeightForAccordion() {
      double var1 = this.snapSize(this.titleRegion.prefHeight(-1.0));
      double var3 = (this.prefHeightFromAccordion - var1) * this.getTransition();
      return var1 + this.snapSize(var3) + this.snappedTopInset() + this.snappedBottomInset();
   }

   private void doAnimationTransition() {
      if (this.content != null) {
         Duration var1;
         if (this.timeline != null && this.timeline.getStatus() != Animation.Status.STOPPED) {
            var1 = this.timeline.getCurrentTime();
            this.timeline.stop();
         } else {
            var1 = TRANSITION_DURATION;
         }

         this.timeline = new Timeline();
         this.timeline.setCycleCount(1);
         KeyFrame var2;
         KeyFrame var3;
         if (((TitledPane)this.getSkinnable()).isExpanded()) {
            var2 = new KeyFrame(Duration.ZERO, (var1x) -> {
               if (CACHE_ANIMATION) {
                  this.content.setCache(true);
               }

               this.content.setVisible(true);
            }, new KeyValue[]{new KeyValue(this.transitionProperty(), this.transitionStartValue)});
            var3 = new KeyFrame(var1, (var1x) -> {
               if (CACHE_ANIMATION) {
                  this.content.setCache(false);
               }

            }, new KeyValue[]{new KeyValue(this.transitionProperty(), 1, Interpolator.LINEAR)});
         } else {
            var2 = new KeyFrame(Duration.ZERO, (var1x) -> {
               if (CACHE_ANIMATION) {
                  this.content.setCache(true);
               }

            }, new KeyValue[]{new KeyValue(this.transitionProperty(), this.transitionStartValue)});
            var3 = new KeyFrame(var1, (var1x) -> {
               this.content.setVisible(false);
               if (CACHE_ANIMATION) {
                  this.content.setCache(false);
               }

            }, new KeyValue[]{new KeyValue(this.transitionProperty(), 0, Interpolator.LINEAR)});
         }

         this.timeline.getKeyFrames().setAll((Object[])(var2, var3));
         this.timeline.play();
      }
   }

   class TitleRegion extends StackPane {
      private final StackPane arrowRegion;

      public TitleRegion() {
         this.getStyleClass().setAll((Object[])("title"));
         this.arrowRegion = new StackPane();
         this.arrowRegion.setId("arrowRegion");
         this.arrowRegion.getStyleClass().setAll((Object[])("arrow-button"));
         StackPane var2 = new StackPane();
         var2.setId("arrow");
         var2.getStyleClass().setAll((Object[])("arrow"));
         this.arrowRegion.getChildren().setAll((Object[])(var2));
         var2.rotateProperty().bind(new DoubleBinding() {
            {
               this.bind(new Observable[]{TitledPaneSkin.this.transitionProperty()});
            }

            protected double computeValue() {
               return -90.0 * (1.0 - TitledPaneSkin.this.getTransition());
            }
         });
         this.setAlignment(Pos.CENTER_LEFT);
         this.setOnMouseReleased((var1x) -> {
            if (var1x.getButton() == MouseButton.PRIMARY) {
               ContextMenu var2 = ((TitledPane)TitledPaneSkin.this.getSkinnable()).getContextMenu();
               if (var2 != null) {
                  var2.hide();
               }

               if (((TitledPane)TitledPaneSkin.this.getSkinnable()).isCollapsible() && ((TitledPane)TitledPaneSkin.this.getSkinnable()).isFocused()) {
                  ((TitledPaneBehavior)TitledPaneSkin.this.getBehavior()).toggle();
               }

            }
         });
         this.update();
      }

      private void update() {
         this.getChildren().clear();
         TitledPane var1 = (TitledPane)TitledPaneSkin.this.getSkinnable();
         if (var1.isCollapsible()) {
            this.getChildren().add(this.arrowRegion);
         }

         if (TitledPaneSkin.this.graphic != null) {
            TitledPaneSkin.this.graphic.layoutBoundsProperty().removeListener(TitledPaneSkin.this.graphicPropertyChangedListener);
         }

         TitledPaneSkin.this.graphic = var1.getGraphic();
         if (TitledPaneSkin.this.isIgnoreGraphic()) {
            if (var1.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY) {
               this.getChildren().clear();
               this.getChildren().add(this.arrowRegion);
            } else {
               this.getChildren().add(TitledPaneSkin.this.text);
            }
         } else {
            TitledPaneSkin.this.graphic.layoutBoundsProperty().addListener(TitledPaneSkin.this.graphicPropertyChangedListener);
            if (TitledPaneSkin.this.isIgnoreText()) {
               this.getChildren().add(TitledPaneSkin.this.graphic);
            } else {
               this.getChildren().addAll(TitledPaneSkin.this.graphic, TitledPaneSkin.this.text);
            }
         }

         this.setCursor(((TitledPane)TitledPaneSkin.this.getSkinnable()).isCollapsible() ? Cursor.HAND : Cursor.DEFAULT);
      }

      protected double computePrefWidth(double var1) {
         double var3 = this.snappedLeftInset();
         double var5 = this.snappedRightInset();
         double var7 = 0.0;
         double var9 = this.labelPrefWidth(var1);
         if (this.arrowRegion != null) {
            var7 = this.snapSize(this.arrowRegion.prefWidth(var1));
         }

         return var3 + var7 + var9 + var5;
      }

      protected double computePrefHeight(double var1) {
         double var3 = this.snappedTopInset();
         double var5 = this.snappedBottomInset();
         double var7 = 0.0;
         double var9 = this.labelPrefHeight(var1);
         if (this.arrowRegion != null) {
            var7 = this.snapSize(this.arrowRegion.prefHeight(var1));
         }

         return var3 + Math.max(var7, var9) + var5;
      }

      protected void layoutChildren() {
         double var1 = this.snappedTopInset();
         double var3 = this.snappedBottomInset();
         double var5 = this.snappedLeftInset();
         double var7 = this.snappedRightInset();
         double var9 = this.getWidth() - (var5 + var7);
         double var11 = this.getHeight() - (var1 + var3);
         double var13 = this.snapSize(this.arrowRegion.prefWidth(-1.0));
         double var15 = this.snapSize(this.arrowRegion.prefHeight(-1.0));
         double var17 = this.snapSize(Math.min(var9 - var13 / 2.0, this.labelPrefWidth(-1.0)));
         double var19 = this.snapSize(this.labelPrefHeight(-1.0));
         double var21 = var5 + var13 + Utils.computeXOffset(var9 - var13, var17, TitledPaneSkin.this.hpos);
         if (HPos.CENTER == TitledPaneSkin.this.hpos) {
            var21 = var5 + Utils.computeXOffset(var9, var17, TitledPaneSkin.this.hpos);
         }

         double var23 = var1 + Utils.computeYOffset(var11, Math.max(var15, var19), TitledPaneSkin.this.vpos);
         this.arrowRegion.resize(var13, var15);
         this.positionInArea(this.arrowRegion, var5, var1, var13, var11, 0.0, HPos.CENTER, VPos.CENTER);
         TitledPaneSkin.this.layoutLabelInArea(var21, var23, var17, var11, TitledPaneSkin.this.pos);
      }

      private double labelPrefWidth(double var1) {
         Labeled var3 = (Labeled)TitledPaneSkin.this.getSkinnable();
         Font var4 = TitledPaneSkin.this.text.getFont();
         String var5 = var3.getText();
         boolean var6 = var5 == null || var5.isEmpty();
         Insets var7 = var3.getLabelPadding();
         double var8 = var7.getLeft() + var7.getRight();
         double var10 = var6 ? 0.0 : Utils.computeTextWidth(var4, var5, 0.0);
         Node var12 = var3.getGraphic();
         if (TitledPaneSkin.this.isIgnoreGraphic()) {
            return var10 + var8;
         } else if (TitledPaneSkin.this.isIgnoreText()) {
            return var12.prefWidth(-1.0) + var8;
         } else {
            return var3.getContentDisplay() != ContentDisplay.LEFT && var3.getContentDisplay() != ContentDisplay.RIGHT ? Math.max(var10, var12.prefWidth(-1.0)) + var8 : var10 + var3.getGraphicTextGap() + var12.prefWidth(-1.0) + var8;
         }
      }

      private double labelPrefHeight(double var1) {
         Labeled var3 = (Labeled)TitledPaneSkin.this.getSkinnable();
         Font var4 = TitledPaneSkin.this.text.getFont();
         ContentDisplay var5 = var3.getContentDisplay();
         double var6 = var3.getGraphicTextGap();
         Insets var8 = var3.getLabelPadding();
         double var9 = this.snappedLeftInset() + this.snappedRightInset() + var8.getLeft() + var8.getRight();
         String var11 = var3.getText();
         if (var11 != null && var11.endsWith("\n")) {
            var11 = var11.substring(0, var11.length() - 1);
         }

         if (!TitledPaneSkin.this.isIgnoreGraphic() && (var5 == ContentDisplay.LEFT || var5 == ContentDisplay.RIGHT)) {
            var1 -= TitledPaneSkin.this.graphic.prefWidth(-1.0) + var6;
         }

         var1 -= var9;
         double var12 = Utils.computeTextHeight(var4, var11, var3.isWrapText() ? var1 : 0.0, TitledPaneSkin.this.text.getBoundsType());
         double var14 = var12;
         if (!TitledPaneSkin.this.isIgnoreGraphic()) {
            Node var16 = var3.getGraphic();
            if (var5 != ContentDisplay.TOP && var5 != ContentDisplay.BOTTOM) {
               var14 = Math.max(var12, var16.prefHeight(-1.0));
            } else {
               var14 = var16.prefHeight(-1.0) + var6 + var12;
            }
         }

         return var14 + var8.getTop() + var8.getBottom();
      }
   }
}
