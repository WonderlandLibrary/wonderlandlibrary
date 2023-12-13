package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SliderBehavior;
import javafx.animation.Transition;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Slider;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class SliderSkin extends BehaviorSkinBase {
   private NumberAxis tickLine = null;
   private double trackToTickGap = 2.0;
   private boolean showTickMarks;
   private double thumbWidth;
   private double thumbHeight;
   private double trackStart;
   private double trackLength;
   private double thumbTop;
   private double thumbLeft;
   private double preDragThumbPos;
   private Point2D dragStart;
   private StackPane thumb;
   private StackPane track;
   private boolean trackClicked = false;
   StringConverter stringConverterWrapper = new StringConverter() {
      Slider slider = (Slider)SliderSkin.this.getSkinnable();

      public String toString(Number var1) {
         return var1 != null ? this.slider.getLabelFormatter().toString(var1.doubleValue()) : "";
      }

      public Number fromString(String var1) {
         return (Number)this.slider.getLabelFormatter().fromString(var1);
      }
   };

   public SliderSkin(Slider var1) {
      super(var1, new SliderBehavior(var1));
      this.initialize();
      var1.requestLayout();
      this.registerChangeListener(var1.minProperty(), "MIN");
      this.registerChangeListener(var1.maxProperty(), "MAX");
      this.registerChangeListener(var1.valueProperty(), "VALUE");
      this.registerChangeListener(var1.orientationProperty(), "ORIENTATION");
      this.registerChangeListener(var1.showTickMarksProperty(), "SHOW_TICK_MARKS");
      this.registerChangeListener(var1.showTickLabelsProperty(), "SHOW_TICK_LABELS");
      this.registerChangeListener(var1.majorTickUnitProperty(), "MAJOR_TICK_UNIT");
      this.registerChangeListener(var1.minorTickCountProperty(), "MINOR_TICK_COUNT");
      this.registerChangeListener(var1.labelFormatterProperty(), "TICK_LABEL_FORMATTER");
      this.registerChangeListener(var1.snapToTicksProperty(), "SNAP_TO_TICKS");
   }

   private void initialize() {
      this.thumb = new StackPane() {
         public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
            switch (var1) {
               case VALUE:
                  return ((Slider)SliderSkin.this.getSkinnable()).getValue();
               default:
                  return super.queryAccessibleAttribute(var1, var2);
            }
         }
      };
      this.thumb.getStyleClass().setAll((Object[])("thumb"));
      this.thumb.setAccessibleRole(AccessibleRole.THUMB);
      this.track = new StackPane();
      this.track.getStyleClass().setAll((Object[])("track"));
      this.getChildren().clear();
      this.getChildren().addAll(this.track, this.thumb);
      this.setShowTickMarks(((Slider)this.getSkinnable()).isShowTickMarks(), ((Slider)this.getSkinnable()).isShowTickLabels());
      this.track.setOnMousePressed((var1) -> {
         if (!this.thumb.isPressed()) {
            this.trackClicked = true;
            if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
               ((SliderBehavior)this.getBehavior()).trackPress(var1, var1.getX() / this.trackLength);
            } else {
               ((SliderBehavior)this.getBehavior()).trackPress(var1, var1.getY() / this.trackLength);
            }

            this.trackClicked = false;
         }

      });
      this.track.setOnMouseDragged((var1) -> {
         if (!this.thumb.isPressed()) {
            if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
               ((SliderBehavior)this.getBehavior()).trackPress(var1, var1.getX() / this.trackLength);
            } else {
               ((SliderBehavior)this.getBehavior()).trackPress(var1, var1.getY() / this.trackLength);
            }
         }

      });
      this.thumb.setOnMousePressed((var1) -> {
         ((SliderBehavior)this.getBehavior()).thumbPressed(var1, 0.0);
         this.dragStart = this.thumb.localToParent(var1.getX(), var1.getY());
         this.preDragThumbPos = (((Slider)this.getSkinnable()).getValue() - ((Slider)this.getSkinnable()).getMin()) / (((Slider)this.getSkinnable()).getMax() - ((Slider)this.getSkinnable()).getMin());
      });
      this.thumb.setOnMouseReleased((var1) -> {
         ((SliderBehavior)this.getBehavior()).thumbReleased(var1);
      });
      this.thumb.setOnMouseDragged((var1) -> {
         Point2D var2 = this.thumb.localToParent(var1.getX(), var1.getY());
         double var3 = ((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL ? var2.getX() - this.dragStart.getX() : -(var2.getY() - this.dragStart.getY());
         ((SliderBehavior)this.getBehavior()).thumbDragged(var1, this.preDragThumbPos + var3 / this.trackLength);
      });
   }

   private void setShowTickMarks(boolean var1, boolean var2) {
      this.showTickMarks = var1 || var2;
      Slider var3 = (Slider)this.getSkinnable();
      if (this.showTickMarks) {
         if (this.tickLine == null) {
            this.tickLine = new NumberAxis();
            this.tickLine.setAutoRanging(false);
            this.tickLine.setSide(var3.getOrientation() == Orientation.VERTICAL ? Side.RIGHT : (var3.getOrientation() == null ? Side.RIGHT : Side.BOTTOM));
            this.tickLine.setUpperBound(var3.getMax());
            this.tickLine.setLowerBound(var3.getMin());
            this.tickLine.setTickUnit(var3.getMajorTickUnit());
            this.tickLine.setTickMarkVisible(var1);
            this.tickLine.setTickLabelsVisible(var2);
            this.tickLine.setMinorTickVisible(var1);
            this.tickLine.setMinorTickCount(Math.max(var3.getMinorTickCount(), 0) + 1);
            if (var3.getLabelFormatter() != null) {
               this.tickLine.setTickLabelFormatter(this.stringConverterWrapper);
            }

            this.getChildren().clear();
            this.getChildren().addAll(this.tickLine, this.track, this.thumb);
         } else {
            this.tickLine.setTickLabelsVisible(var2);
            this.tickLine.setTickMarkVisible(var1);
            this.tickLine.setMinorTickVisible(var1);
         }
      } else {
         this.getChildren().clear();
         this.getChildren().addAll(this.track, this.thumb);
      }

      ((Slider)this.getSkinnable()).requestLayout();
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      Slider var2 = (Slider)this.getSkinnable();
      if ("ORIENTATION".equals(var1)) {
         if (this.showTickMarks && this.tickLine != null) {
            this.tickLine.setSide(var2.getOrientation() == Orientation.VERTICAL ? Side.RIGHT : (var2.getOrientation() == null ? Side.RIGHT : Side.BOTTOM));
         }

         ((Slider)this.getSkinnable()).requestLayout();
      } else if ("VALUE".equals(var1)) {
         this.positionThumb(this.trackClicked);
      } else if ("MIN".equals(var1)) {
         if (this.showTickMarks && this.tickLine != null) {
            this.tickLine.setLowerBound(var2.getMin());
         }

         ((Slider)this.getSkinnable()).requestLayout();
      } else if ("MAX".equals(var1)) {
         if (this.showTickMarks && this.tickLine != null) {
            this.tickLine.setUpperBound(var2.getMax());
         }

         ((Slider)this.getSkinnable()).requestLayout();
      } else if (!"SHOW_TICK_MARKS".equals(var1) && !"SHOW_TICK_LABELS".equals(var1)) {
         if ("MAJOR_TICK_UNIT".equals(var1)) {
            if (this.tickLine != null) {
               this.tickLine.setTickUnit(var2.getMajorTickUnit());
               ((Slider)this.getSkinnable()).requestLayout();
            }
         } else if ("MINOR_TICK_COUNT".equals(var1)) {
            if (this.tickLine != null) {
               this.tickLine.setMinorTickCount(Math.max(var2.getMinorTickCount(), 0) + 1);
               ((Slider)this.getSkinnable()).requestLayout();
            }
         } else if ("TICK_LABEL_FORMATTER".equals(var1)) {
            if (this.tickLine != null) {
               if (var2.getLabelFormatter() == null) {
                  this.tickLine.setTickLabelFormatter((StringConverter)null);
               } else {
                  this.tickLine.setTickLabelFormatter(this.stringConverterWrapper);
                  this.tickLine.requestAxisLayout();
               }
            }
         } else if ("SNAP_TO_TICKS".equals(var1)) {
            var2.adjustValue(var2.getValue());
         }
      } else {
         this.setShowTickMarks(var2.isShowTickMarks(), var2.isShowTickLabels());
      }

   }

   void positionThumb(boolean var1) {
      Slider var2 = (Slider)this.getSkinnable();
      if (!(var2.getValue() > var2.getMax())) {
         boolean var3 = var2.getOrientation() == Orientation.HORIZONTAL;
         final double var4 = var3 ? this.trackStart + (this.trackLength * ((var2.getValue() - var2.getMin()) / (var2.getMax() - var2.getMin())) - this.thumbWidth / 2.0) : this.thumbLeft;
         final double var6 = var3 ? this.thumbTop : this.snappedTopInset() + this.trackLength - this.trackLength * ((var2.getValue() - var2.getMin()) / (var2.getMax() - var2.getMin()));
         if (var1) {
            final double var8 = this.thumb.getLayoutX();
            final double var10 = this.thumb.getLayoutY();
            Transition var12 = new Transition() {
               {
                  this.setCycleDuration(Duration.millis(200.0));
               }

               protected void interpolate(double var1) {
                  if (!Double.isNaN(var8)) {
                     SliderSkin.this.thumb.setLayoutX(var8 + var1 * (var4 - var8));
                  }

                  if (!Double.isNaN(var10)) {
                     SliderSkin.this.thumb.setLayoutY(var10 + var1 * (var6 - var10));
                  }

               }
            };
            var12.play();
         } else {
            this.thumb.setLayoutX(var4);
            this.thumb.setLayoutY(var6);
         }

      }
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      this.thumbWidth = this.snapSize(this.thumb.prefWidth(-1.0));
      this.thumbHeight = this.snapSize(this.thumb.prefHeight(-1.0));
      this.thumb.resize(this.thumbWidth, this.thumbHeight);
      double var9 = this.track.getBackground() == null ? 0.0 : (this.track.getBackground().getFills().size() > 0 ? ((BackgroundFill)this.track.getBackground().getFills().get(0)).getRadii().getTopLeftHorizontalRadius() : 0.0);
      double var17;
      double var19;
      double var21;
      double var11;
      double var13;
      double var15;
      if (((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL) {
         var11 = this.showTickMarks ? this.tickLine.prefHeight(-1.0) : 0.0;
         var13 = this.snapSize(this.track.prefHeight(-1.0));
         var15 = Math.max(var13, this.thumbHeight);
         var17 = var15 + (this.showTickMarks ? this.trackToTickGap + var11 : 0.0);
         var19 = var3 + (var7 - var17) / 2.0;
         this.trackLength = this.snapSize(var5 - this.thumbWidth);
         this.trackStart = this.snapPosition(var1 + this.thumbWidth / 2.0);
         var21 = (double)((int)(var19 + (var15 - var13) / 2.0));
         this.thumbTop = (double)((int)(var19 + (var15 - this.thumbHeight) / 2.0));
         this.positionThumb(false);
         this.track.resizeRelocate((double)((int)(this.trackStart - var9)), var21, (double)((int)(this.trackLength + var9 + var9)), var13);
         if (this.showTickMarks) {
            this.tickLine.setLayoutX(this.trackStart);
            this.tickLine.setLayoutY(var21 + var13 + this.trackToTickGap);
            this.tickLine.resize(this.trackLength, var11);
            this.tickLine.requestAxisLayout();
         } else {
            if (this.tickLine != null) {
               this.tickLine.resize(0.0, 0.0);
               this.tickLine.requestAxisLayout();
            }

            this.tickLine = null;
         }
      } else {
         var11 = this.showTickMarks ? this.tickLine.prefWidth(-1.0) : 0.0;
         var13 = this.snapSize(this.track.prefWidth(-1.0));
         var15 = Math.max(var13, this.thumbWidth);
         var17 = var15 + (this.showTickMarks ? this.trackToTickGap + var11 : 0.0);
         var19 = var1 + (var5 - var17) / 2.0;
         this.trackLength = this.snapSize(var7 - this.thumbHeight);
         this.trackStart = this.snapPosition(var3 + this.thumbHeight / 2.0);
         var21 = (double)((int)(var19 + (var15 - var13) / 2.0));
         this.thumbLeft = (double)((int)(var19 + (var15 - this.thumbWidth) / 2.0));
         this.positionThumb(false);
         this.track.resizeRelocate(var21, (double)((int)(this.trackStart - var9)), var13, (double)((int)(this.trackLength + var9 + var9)));
         if (this.showTickMarks) {
            this.tickLine.setLayoutX(var21 + var13 + this.trackToTickGap);
            this.tickLine.setLayoutY(this.trackStart);
            this.tickLine.resize(var11, this.trackLength);
            this.tickLine.requestAxisLayout();
         } else {
            if (this.tickLine != null) {
               this.tickLine.resize(0.0, 0.0);
               this.tickLine.requestAxisLayout();
            }

            this.tickLine = null;
         }
      }

   }

   double minTrackLength() {
      return 2.0 * this.thumb.prefWidth(-1.0);
   }

   protected double computeMinWidth(double var1, double var3, double var5, double var7, double var9) {
      Slider var11 = (Slider)this.getSkinnable();
      return var11.getOrientation() == Orientation.HORIZONTAL ? var9 + this.minTrackLength() + this.thumb.minWidth(-1.0) + var5 : var9 + this.thumb.prefWidth(-1.0) + var5;
   }

   protected double computeMinHeight(double var1, double var3, double var5, double var7, double var9) {
      Slider var11 = (Slider)this.getSkinnable();
      if (var11.getOrientation() == Orientation.HORIZONTAL) {
         double var12 = this.showTickMarks ? this.tickLine.prefHeight(-1.0) + this.trackToTickGap : 0.0;
         return var3 + this.thumb.prefHeight(-1.0) + var12 + var7;
      } else {
         return var3 + this.minTrackLength() + this.thumb.prefHeight(-1.0) + var7;
      }
   }

   protected double computePrefWidth(double var1, double var3, double var5, double var7, double var9) {
      Slider var11 = (Slider)this.getSkinnable();
      if (var11.getOrientation() == Orientation.HORIZONTAL) {
         return this.showTickMarks ? Math.max(140.0, this.tickLine.prefWidth(-1.0)) : 140.0;
      } else {
         double var12 = this.showTickMarks ? this.tickLine.prefWidth(-1.0) + this.trackToTickGap : 0.0;
         return var9 + Math.max(this.thumb.prefWidth(-1.0), this.track.prefWidth(-1.0)) + var12 + var5;
      }
   }

   protected double computePrefHeight(double var1, double var3, double var5, double var7, double var9) {
      Slider var11 = (Slider)this.getSkinnable();
      if (var11.getOrientation() == Orientation.HORIZONTAL) {
         return var3 + Math.max(this.thumb.prefHeight(-1.0), this.track.prefHeight(-1.0)) + (this.showTickMarks ? this.trackToTickGap + this.tickLine.prefHeight(-1.0) : 0.0) + var7;
      } else {
         return this.showTickMarks ? Math.max(140.0, this.tickLine.prefHeight(-1.0)) : 140.0;
      }
   }

   protected double computeMaxWidth(double var1, double var3, double var5, double var7, double var9) {
      return ((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL ? Double.MAX_VALUE : ((Slider)this.getSkinnable()).prefWidth(-1.0);
   }

   protected double computeMaxHeight(double var1, double var3, double var5, double var7, double var9) {
      return ((Slider)this.getSkinnable()).getOrientation() == Orientation.HORIZONTAL ? ((Slider)this.getSkinnable()).prefHeight(var1) : Double.MAX_VALUE;
   }
}
