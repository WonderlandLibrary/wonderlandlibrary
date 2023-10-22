package javafx.scene.chart;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public abstract class Axis extends Region {
   Text measure = new Text();
   private Orientation effectiveOrientation;
   private double effectiveTickLabelRotation = Double.NaN;
   private Label axisLabel = new Label();
   private final Path tickMarkPath = new Path();
   private double oldLength = 0.0;
   boolean rangeValid = false;
   boolean measureInvalid = false;
   boolean tickLabelsVisibleInvalid = false;
   private BitSet labelsToSkip = new BitSet();
   private final ObservableList tickMarks = FXCollections.observableArrayList();
   private final ObservableList unmodifiableTickMarks;
   private ObjectProperty side;
   private ObjectProperty label;
   private BooleanProperty tickMarkVisible;
   private BooleanProperty tickLabelsVisible;
   private DoubleProperty tickLength;
   private BooleanProperty autoRanging;
   private ObjectProperty tickLabelFont;
   private ObjectProperty tickLabelFill;
   private DoubleProperty tickLabelGap;
   private BooleanProperty animated;
   private DoubleProperty tickLabelRotation;
   private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("top");
   private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("bottom");
   private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("left");
   private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("right");

   public ObservableList getTickMarks() {
      return this.unmodifiableTickMarks;
   }

   public final Side getSide() {
      return (Side)this.side.get();
   }

   public final void setSide(Side var1) {
      this.side.set(var1);
   }

   public final ObjectProperty sideProperty() {
      return this.side;
   }

   final void setEffectiveOrientation(Orientation var1) {
      this.effectiveOrientation = var1;
   }

   final Side getEffectiveSide() {
      Side var1 = this.getSide();
      if (var1 == null || var1.isVertical() && this.effectiveOrientation == Orientation.HORIZONTAL || var1.isHorizontal() && this.effectiveOrientation == Orientation.VERTICAL) {
         return this.effectiveOrientation == Orientation.VERTICAL ? Side.LEFT : Side.BOTTOM;
      } else {
         return var1;
      }
   }

   public final String getLabel() {
      return (String)this.label.get();
   }

   public final void setLabel(String var1) {
      this.label.set(var1);
   }

   public final ObjectProperty labelProperty() {
      return this.label;
   }

   public final boolean isTickMarkVisible() {
      return this.tickMarkVisible.get();
   }

   public final void setTickMarkVisible(boolean var1) {
      this.tickMarkVisible.set(var1);
   }

   public final BooleanProperty tickMarkVisibleProperty() {
      return this.tickMarkVisible;
   }

   public final boolean isTickLabelsVisible() {
      return this.tickLabelsVisible.get();
   }

   public final void setTickLabelsVisible(boolean var1) {
      this.tickLabelsVisible.set(var1);
   }

   public final BooleanProperty tickLabelsVisibleProperty() {
      return this.tickLabelsVisible;
   }

   public final double getTickLength() {
      return this.tickLength.get();
   }

   public final void setTickLength(double var1) {
      this.tickLength.set(var1);
   }

   public final DoubleProperty tickLengthProperty() {
      return this.tickLength;
   }

   public final boolean isAutoRanging() {
      return this.autoRanging.get();
   }

   public final void setAutoRanging(boolean var1) {
      this.autoRanging.set(var1);
   }

   public final BooleanProperty autoRangingProperty() {
      return this.autoRanging;
   }

   public final Font getTickLabelFont() {
      return (Font)this.tickLabelFont.get();
   }

   public final void setTickLabelFont(Font var1) {
      this.tickLabelFont.set(var1);
   }

   public final ObjectProperty tickLabelFontProperty() {
      return this.tickLabelFont;
   }

   public final Paint getTickLabelFill() {
      return (Paint)this.tickLabelFill.get();
   }

   public final void setTickLabelFill(Paint var1) {
      this.tickLabelFill.set(var1);
   }

   public final ObjectProperty tickLabelFillProperty() {
      return this.tickLabelFill;
   }

   public final double getTickLabelGap() {
      return this.tickLabelGap.get();
   }

   public final void setTickLabelGap(double var1) {
      this.tickLabelGap.set(var1);
   }

   public final DoubleProperty tickLabelGapProperty() {
      return this.tickLabelGap;
   }

   public final boolean getAnimated() {
      return this.animated.get();
   }

   public final void setAnimated(boolean var1) {
      this.animated.set(var1);
   }

   public final BooleanProperty animatedProperty() {
      return this.animated;
   }

   public final double getTickLabelRotation() {
      return this.tickLabelRotation.getValue();
   }

   public final void setTickLabelRotation(double var1) {
      this.tickLabelRotation.setValue((Number)var1);
   }

   public final DoubleProperty tickLabelRotationProperty() {
      return this.tickLabelRotation;
   }

   public Axis() {
      this.unmodifiableTickMarks = FXCollections.unmodifiableObservableList(this.tickMarks);
      this.side = new StyleableObjectProperty() {
         protected void invalidated() {
            Side var1 = (Side)this.get();
            Axis.this.pseudoClassStateChanged(Axis.TOP_PSEUDOCLASS_STATE, var1 == Side.TOP);
            Axis.this.pseudoClassStateChanged(Axis.RIGHT_PSEUDOCLASS_STATE, var1 == Side.RIGHT);
            Axis.this.pseudoClassStateChanged(Axis.BOTTOM_PSEUDOCLASS_STATE, var1 == Side.BOTTOM);
            Axis.this.pseudoClassStateChanged(Axis.LEFT_PSEUDOCLASS_STATE, var1 == Side.LEFT);
            Axis.this.requestAxisLayout();
         }

         public CssMetaData getCssMetaData() {
            return Axis.StyleableProperties.SIDE;
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "side";
         }
      };
      this.label = new ObjectPropertyBase() {
         protected void invalidated() {
            Axis.this.axisLabel.setText((String)this.get());
            Axis.this.requestAxisLayout();
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "label";
         }
      };
      this.tickMarkVisible = new StyleableBooleanProperty(true) {
         protected void invalidated() {
            Axis.this.tickMarkPath.setVisible(this.get());
            Axis.this.requestAxisLayout();
         }

         public CssMetaData getCssMetaData() {
            return Axis.StyleableProperties.TICK_MARK_VISIBLE;
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "tickMarkVisible";
         }
      };
      this.tickLabelsVisible = new StyleableBooleanProperty(true) {
         protected void invalidated() {
            Iterator var1 = Axis.this.tickMarks.iterator();

            while(var1.hasNext()) {
               TickMark var2 = (TickMark)var1.next();
               var2.setTextVisible(this.get());
            }

            Axis.this.tickLabelsVisibleInvalid = true;
            Axis.this.requestAxisLayout();
         }

         public CssMetaData getCssMetaData() {
            return Axis.StyleableProperties.TICK_LABELS_VISIBLE;
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "tickLabelsVisible";
         }
      };
      this.tickLength = new StyleableDoubleProperty(8.0) {
         protected void invalidated() {
            if (Axis.this.tickLength.get() < 0.0 && !Axis.this.tickLength.isBound()) {
               Axis.this.tickLength.set(0.0);
            }

            Axis.this.requestAxisLayout();
         }

         public CssMetaData getCssMetaData() {
            return Axis.StyleableProperties.TICK_LENGTH;
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "tickLength";
         }
      };
      this.autoRanging = new BooleanPropertyBase(true) {
         protected void invalidated() {
            if (this.get()) {
               Axis.this.requestAxisLayout();
            }

         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "autoRanging";
         }
      };
      this.tickLabelFont = new StyleableObjectProperty(Font.font("System", 8.0)) {
         protected void invalidated() {
            Font var1 = (Font)this.get();
            Axis.this.measure.setFont(var1);
            Iterator var2 = Axis.this.getTickMarks().iterator();

            while(var2.hasNext()) {
               TickMark var3 = (TickMark)var2.next();
               var3.textNode.setFont(var1);
            }

            Axis.this.measureInvalid = true;
            Axis.this.requestAxisLayout();
         }

         public CssMetaData getCssMetaData() {
            return Axis.StyleableProperties.TICK_LABEL_FONT;
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "tickLabelFont";
         }
      };
      this.tickLabelFill = new StyleableObjectProperty(Color.BLACK) {
         protected void invalidated() {
            Iterator var1 = Axis.this.tickMarks.iterator();

            while(var1.hasNext()) {
               TickMark var2 = (TickMark)var1.next();
               var2.textNode.setFill(Axis.this.getTickLabelFill());
            }

         }

         public CssMetaData getCssMetaData() {
            return Axis.StyleableProperties.TICK_LABEL_FILL;
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "tickLabelFill";
         }
      };
      this.tickLabelGap = new StyleableDoubleProperty(3.0) {
         protected void invalidated() {
            Axis.this.requestAxisLayout();
         }

         public CssMetaData getCssMetaData() {
            return Axis.StyleableProperties.TICK_LABEL_TICK_GAP;
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "tickLabelGap";
         }
      };
      this.animated = new SimpleBooleanProperty(this, "animated", true);
      this.tickLabelRotation = new DoublePropertyBase(0.0) {
         protected void invalidated() {
            if (Axis.this.isAutoRanging()) {
               Axis.this.invalidateRange();
            }

            Axis.this.requestAxisLayout();
         }

         public Object getBean() {
            return Axis.this;
         }

         public String getName() {
            return "tickLabelRotation";
         }
      };
      this.getStyleClass().setAll((Object[])("axis"));
      this.axisLabel.getStyleClass().add("axis-label");
      this.axisLabel.setAlignment(Pos.CENTER);
      this.tickMarkPath.getStyleClass().add("axis-tick-mark");
      this.getChildren().addAll(this.axisLabel, this.tickMarkPath);
   }

   protected final boolean isRangeValid() {
      return this.rangeValid;
   }

   protected final void invalidateRange() {
      this.rangeValid = false;
   }

   protected final boolean shouldAnimate() {
      return this.getAnimated() && this.impl_isTreeVisible() && this.getScene() != null;
   }

   public void requestLayout() {
   }

   public void requestAxisLayout() {
      super.requestLayout();
   }

   public void invalidateRange(List var1) {
      this.invalidateRange();
      this.requestAxisLayout();
   }

   protected abstract Object autoRange(double var1);

   protected abstract void setRange(Object var1, boolean var2);

   protected abstract Object getRange();

   public abstract double getZeroPosition();

   public abstract double getDisplayPosition(Object var1);

   public abstract Object getValueForDisplay(double var1);

   public abstract boolean isValueOnAxis(Object var1);

   public abstract double toNumericValue(Object var1);

   public abstract Object toRealValue(double var1);

   protected abstract List calculateTickValues(double var1, Object var3);

   protected double computePrefHeight(double var1) {
      Side var3 = this.getEffectiveSide();
      if (var3.isVertical()) {
         return 100.0;
      } else {
         Object var4 = this.autoRange(var1);
         double var5 = 0.0;
         if (this.isTickLabelsVisible()) {
            List var7 = this.calculateTickValues(var1, var4);

            Object var9;
            for(Iterator var8 = var7.iterator(); var8.hasNext(); var5 = Math.max(var5, this.measureTickMarkSize(var9, var4).getHeight())) {
               var9 = var8.next();
            }
         }

         double var11 = this.isTickMarkVisible() ? (this.getTickLength() > 0.0 ? this.getTickLength() : 0.0) : 0.0;
         double var12 = this.axisLabel.getText() != null && this.axisLabel.getText().length() != 0 ? this.axisLabel.prefHeight(-1.0) : 0.0;
         return var5 + this.getTickLabelGap() + var11 + var12;
      }
   }

   protected double computePrefWidth(double var1) {
      Side var3 = this.getEffectiveSide();
      if (!var3.isVertical()) {
         return 100.0;
      } else {
         Object var4 = this.autoRange(var1);
         double var5 = 0.0;
         if (this.isTickLabelsVisible()) {
            List var7 = this.calculateTickValues(var1, var4);

            Object var9;
            for(Iterator var8 = var7.iterator(); var8.hasNext(); var5 = Math.max(var5, this.measureTickMarkSize(var9, var4).getWidth())) {
               var9 = var8.next();
            }
         }

         double var11 = this.isTickMarkVisible() ? (this.getTickLength() > 0.0 ? this.getTickLength() : 0.0) : 0.0;
         double var12 = this.axisLabel.getText() != null && this.axisLabel.getText().length() != 0 ? this.axisLabel.prefHeight(-1.0) : 0.0;
         return var5 + this.getTickLabelGap() + var11 + var12;
      }
   }

   protected void tickMarksUpdated() {
   }

   protected void layoutChildren() {
      double var1 = this.getWidth();
      double var3 = this.getHeight();
      double var5 = this.isTickMarkVisible() && this.getTickLength() > 0.0 ? this.getTickLength() : 0.0;
      boolean var7 = this.oldLength == 0.0;
      Side var8 = this.getEffectiveSide();
      double var9 = var8.isVertical() ? var3 : var1;
      boolean var11 = !this.isRangeValid();
      boolean var12 = this.oldLength != var9;
      Iterator var15;
      TickMark var16;
      TickMark var28;
      if (var12 || var11) {
         Object var13;
         if (!this.isAutoRanging()) {
            var13 = this.getRange();
         } else {
            var13 = this.autoRange(var9);
            this.setRange(var13, this.getAnimated() && !var7 && this.impl_isTreeVisible() && var11);
         }

         List var14 = this.calculateTickValues(var9, var13);

         for(var15 = this.tickMarks.iterator(); var15.hasNext(); var15.remove()) {
            var16 = (TickMark)var15.next();
            if (this.shouldAnimate()) {
               FadeTransition var18 = new FadeTransition(Duration.millis(250.0), var16.textNode);
               var18.setToValue(0.0);
               var18.setOnFinished((var2) -> {
                  this.getChildren().remove(var16.textNode);
               });
               var18.play();
            } else {
               this.getChildren().remove(var16.textNode);
            }
         }

         Iterator var26 = var14.iterator();

         while(var26.hasNext()) {
            Object var17 = var26.next();
            var28 = new TickMark();
            var28.setValue(var17);
            var28.textNode.setText(this.getTickMarkLabel(var17));
            var28.textNode.setFont(this.getTickLabelFont());
            var28.textNode.setFill(this.getTickLabelFill());
            var28.setTextVisible(this.isTickLabelsVisible());
            if (this.shouldAnimate()) {
               var28.textNode.setOpacity(0.0);
            }

            this.getChildren().add(var28.textNode);
            this.tickMarks.add(var28);
            if (this.shouldAnimate()) {
               FadeTransition var19 = new FadeTransition(Duration.millis(750.0), var28.textNode);
               var19.setFromValue(0.0);
               var19.setToValue(1.0);
               var19.play();
            }
         }

         this.tickMarksUpdated();
         this.oldLength = var9;
         this.rangeValid = true;
      }

      if (var12 || var11 || this.measureInvalid || this.tickLabelsVisibleInvalid) {
         this.measureInvalid = false;
         this.tickLabelsVisibleInvalid = false;
         this.labelsToSkip.clear();
         int var22 = 0;
         double var23 = 0.0;
         double var27 = 0.0;
         Iterator var29 = this.tickMarks.iterator();

         TickMark var31;
         while(var29.hasNext()) {
            var31 = (TickMark)var29.next();
            var31.setPosition(this.getDisplayPosition(var31.getValue()));
            if (var31.isTextVisible()) {
               double var20 = this.measureTickMarkSize(var31.getValue(), var8);
               var23 += var20;
               var27 = (double)Math.round(Math.max(var27, var20));
            }
         }

         if (var27 > 0.0 && var9 < var23) {
            var22 = (int)((double)this.tickMarks.size() * var27 / var9) + 1;
         }

         if (var22 > 0) {
            int var30 = 0;
            Iterator var32 = this.tickMarks.iterator();

            while(var32.hasNext()) {
               TickMark var33 = (TickMark)var32.next();
               if (var33.isTextVisible()) {
                  var33.setTextVisible(var30++ % var22 == 0);
               }
            }
         }

         if (this.tickMarks.size() > 2) {
            var28 = (TickMark)this.tickMarks.get(0);
            var31 = (TickMark)this.tickMarks.get(1);
            if (this.isTickLabelsOverlap(var8, var28, var31, this.getTickLabelGap())) {
               var31.setTextVisible(false);
            }

            var28 = (TickMark)this.tickMarks.get(this.tickMarks.size() - 2);
            var31 = (TickMark)this.tickMarks.get(this.tickMarks.size() - 1);
            if (this.isTickLabelsOverlap(var8, var28, var31, this.getTickLabelGap())) {
               var28.setTextVisible(false);
            }
         }
      }

      this.tickMarkPath.getElements().clear();
      double var24 = this.getEffectiveTickLabelRotation();
      if (Side.LEFT.equals(var8)) {
         this.tickMarkPath.setLayoutX(-0.5);
         this.tickMarkPath.setLayoutY(0.5);
         if (this.getLabel() != null) {
            this.axisLabel.getTransforms().setAll((Object[])(new Translate(0.0, var3), new Rotate(-90.0, 0.0, 0.0)));
            this.axisLabel.setLayoutX(0.0);
            this.axisLabel.setLayoutY(0.0);
            this.axisLabel.resize(var3, Math.ceil(this.axisLabel.prefHeight(var1)));
         }

         var15 = this.tickMarks.iterator();

         while(var15.hasNext()) {
            var16 = (TickMark)var15.next();
            this.positionTextNode(var16.textNode, var1 - this.getTickLabelGap() - var5, var16.getPosition(), var24, var8);
            this.updateTickMark(var16, var9, var1 - var5, var16.getPosition(), var1, var16.getPosition());
         }
      } else {
         double var25;
         if (Side.RIGHT.equals(var8)) {
            this.tickMarkPath.setLayoutX(0.5);
            this.tickMarkPath.setLayoutY(0.5);
            if (this.getLabel() != null) {
               var25 = Math.ceil(this.axisLabel.prefHeight(var1));
               this.axisLabel.getTransforms().setAll((Object[])(new Translate(0.0, var3), new Rotate(-90.0, 0.0, 0.0)));
               this.axisLabel.setLayoutX(var1 - var25);
               this.axisLabel.setLayoutY(0.0);
               this.axisLabel.resize(var3, var25);
            }

            var15 = this.tickMarks.iterator();

            while(var15.hasNext()) {
               var16 = (TickMark)var15.next();
               this.positionTextNode(var16.textNode, this.getTickLabelGap() + var5, var16.getPosition(), var24, var8);
               this.updateTickMark(var16, var9, 0.0, var16.getPosition(), var5, var16.getPosition());
            }
         } else if (Side.TOP.equals(var8)) {
            this.tickMarkPath.setLayoutX(0.5);
            this.tickMarkPath.setLayoutY(-0.5);
            if (this.getLabel() != null) {
               this.axisLabel.getTransforms().clear();
               this.axisLabel.setLayoutX(0.0);
               this.axisLabel.setLayoutY(0.0);
               this.axisLabel.resize(var1, Math.ceil(this.axisLabel.prefHeight(var1)));
            }

            var15 = this.tickMarks.iterator();

            while(var15.hasNext()) {
               var16 = (TickMark)var15.next();
               this.positionTextNode(var16.textNode, var16.getPosition(), var3 - var5 - this.getTickLabelGap(), var24, var8);
               this.updateTickMark(var16, var9, var16.getPosition(), var3, var16.getPosition(), var3 - var5);
            }
         } else {
            this.tickMarkPath.setLayoutX(0.5);
            this.tickMarkPath.setLayoutY(0.5);
            if (this.getLabel() != null) {
               this.axisLabel.getTransforms().clear();
               var25 = Math.ceil(this.axisLabel.prefHeight(var1));
               this.axisLabel.setLayoutX(0.0);
               this.axisLabel.setLayoutY(var3 - var25);
               this.axisLabel.resize(var1, var25);
            }

            var15 = this.tickMarks.iterator();

            while(var15.hasNext()) {
               var16 = (TickMark)var15.next();
               this.positionTextNode(var16.textNode, var16.getPosition(), var5 + this.getTickLabelGap(), var24, var8);
               this.updateTickMark(var16, var9, var16.getPosition(), 0.0, var16.getPosition(), var5);
            }
         }
      }

   }

   private boolean isTickLabelsOverlap(Side var1, TickMark var2, TickMark var3, double var4) {
      if (var2.isTextVisible() && var3.isTextVisible()) {
         double var6 = this.measureTickMarkSize(var2.getValue(), var1);
         double var8 = this.measureTickMarkSize(var3.getValue(), var1);
         double var10 = var2.getPosition() - var6 / 2.0;
         double var12 = var2.getPosition() + var6 / 2.0;
         double var14 = var3.getPosition() - var8 / 2.0;
         double var16 = var3.getPosition() + var8 / 2.0;
         return var1.isVertical() ? var10 - var16 <= var4 : var14 - var12 <= var4;
      } else {
         return false;
      }
   }

   private void positionTextNode(Text var1, double var2, double var4, double var6, Side var8) {
      var1.setLayoutX(0.0);
      var1.setLayoutY(0.0);
      var1.setRotate(var6);
      Bounds var9 = var1.getBoundsInParent();
      if (Side.LEFT.equals(var8)) {
         var1.setLayoutX(var2 - var9.getWidth() - var9.getMinX());
         var1.setLayoutY(var4 - var9.getHeight() / 2.0 - var9.getMinY());
      } else if (Side.RIGHT.equals(var8)) {
         var1.setLayoutX(var2 - var9.getMinX());
         var1.setLayoutY(var4 - var9.getHeight() / 2.0 - var9.getMinY());
      } else if (Side.TOP.equals(var8)) {
         var1.setLayoutX(var2 - var9.getWidth() / 2.0 - var9.getMinX());
         var1.setLayoutY(var4 - var9.getHeight() - var9.getMinY());
      } else {
         var1.setLayoutX(var2 - var9.getWidth() / 2.0 - var9.getMinX());
         var1.setLayoutY(var4 - var9.getMinY());
      }

   }

   private void updateTickMark(TickMark var1, double var2, double var4, double var6, double var8, double var10) {
      if (var1.getPosition() >= 0.0 && var1.getPosition() <= Math.ceil(var2)) {
         var1.textNode.setVisible(var1.isTextVisible());
         this.tickMarkPath.getElements().addAll(new MoveTo(var4, var6), new LineTo(var8, var10));
      } else {
         var1.textNode.setVisible(false);
      }

   }

   protected abstract String getTickMarkLabel(Object var1);

   protected final Dimension2D measureTickMarkLabelSize(String var1, double var2) {
      this.measure.setRotate(var2);
      this.measure.setText(var1);
      Bounds var4 = this.measure.getBoundsInParent();
      return new Dimension2D(var4.getWidth(), var4.getHeight());
   }

   protected final Dimension2D measureTickMarkSize(Object var1, double var2) {
      return this.measureTickMarkLabelSize(this.getTickMarkLabel(var1), var2);
   }

   protected Dimension2D measureTickMarkSize(Object var1, Object var2) {
      return this.measureTickMarkSize(var1, this.getEffectiveTickLabelRotation());
   }

   private double measureTickMarkSize(Object var1, Side var2) {
      Dimension2D var3 = this.measureTickMarkSize(var1, this.getEffectiveTickLabelRotation());
      return var2.isVertical() ? var3.getHeight() : var3.getWidth();
   }

   final double getEffectiveTickLabelRotation() {
      return this.isAutoRanging() && !Double.isNaN(this.effectiveTickLabelRotation) ? this.effectiveTickLabelRotation : this.getTickLabelRotation();
   }

   final void setEffectiveTickLabelRotation(double var1) {
      this.effectiveTickLabelRotation = var1;
   }

   public static List getClassCssMetaData() {
      return Axis.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData SIDE = new CssMetaData("-fx-side", new EnumConverter(Side.class)) {
         public boolean isSettable(Axis var1) {
            return var1.side == null || !var1.side.isBound();
         }

         public StyleableProperty getStyleableProperty(Axis var1) {
            return (StyleableProperty)var1.sideProperty();
         }
      };
      private static final CssMetaData TICK_LENGTH = new CssMetaData("-fx-tick-length", SizeConverter.getInstance(), 8.0) {
         public boolean isSettable(Axis var1) {
            return var1.tickLength == null || !var1.tickLength.isBound();
         }

         public StyleableProperty getStyleableProperty(Axis var1) {
            return (StyleableProperty)var1.tickLengthProperty();
         }
      };
      private static final CssMetaData TICK_LABEL_FONT = new FontCssMetaData("-fx-tick-label-font", Font.font("system", 8.0)) {
         public boolean isSettable(Axis var1) {
            return var1.tickLabelFont == null || !var1.tickLabelFont.isBound();
         }

         public StyleableProperty getStyleableProperty(Axis var1) {
            return (StyleableProperty)var1.tickLabelFontProperty();
         }
      };
      private static final CssMetaData TICK_LABEL_FILL;
      private static final CssMetaData TICK_LABEL_TICK_GAP;
      private static final CssMetaData TICK_MARK_VISIBLE;
      private static final CssMetaData TICK_LABELS_VISIBLE;
      private static final List STYLEABLES;

      static {
         TICK_LABEL_FILL = new CssMetaData("-fx-tick-label-fill", PaintConverter.getInstance(), Color.BLACK) {
            public boolean isSettable(Axis var1) {
               return var1.tickLabelFill == null | !var1.tickLabelFill.isBound();
            }

            public StyleableProperty getStyleableProperty(Axis var1) {
               return (StyleableProperty)var1.tickLabelFillProperty();
            }
         };
         TICK_LABEL_TICK_GAP = new CssMetaData("-fx-tick-label-gap", SizeConverter.getInstance(), 3.0) {
            public boolean isSettable(Axis var1) {
               return var1.tickLabelGap == null || !var1.tickLabelGap.isBound();
            }

            public StyleableProperty getStyleableProperty(Axis var1) {
               return (StyleableProperty)var1.tickLabelGapProperty();
            }
         };
         TICK_MARK_VISIBLE = new CssMetaData("-fx-tick-mark-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Axis var1) {
               return var1.tickMarkVisible == null || !var1.tickMarkVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(Axis var1) {
               return (StyleableProperty)var1.tickMarkVisibleProperty();
            }
         };
         TICK_LABELS_VISIBLE = new CssMetaData("-fx-tick-labels-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(Axis var1) {
               return var1.tickLabelsVisible == null || !var1.tickLabelsVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(Axis var1) {
               return (StyleableProperty)var1.tickLabelsVisibleProperty();
            }
         };
         ArrayList var0 = new ArrayList(Region.getClassCssMetaData());
         var0.add(SIDE);
         var0.add(TICK_LENGTH);
         var0.add(TICK_LABEL_FONT);
         var0.add(TICK_LABEL_FILL);
         var0.add(TICK_LABEL_TICK_GAP);
         var0.add(TICK_MARK_VISIBLE);
         var0.add(TICK_LABELS_VISIBLE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }

   public static final class TickMark {
      private StringProperty label = new StringPropertyBase() {
         protected void invalidated() {
            TickMark.this.textNode.setText(this.getValue());
         }

         public Object getBean() {
            return TickMark.this;
         }

         public String getName() {
            return "label";
         }
      };
      private ObjectProperty value = new SimpleObjectProperty(this, "value");
      private DoubleProperty position = new SimpleDoubleProperty(this, "position");
      Text textNode = new Text();
      private BooleanProperty textVisible = new BooleanPropertyBase(true) {
         protected void invalidated() {
            if (!this.get()) {
               TickMark.this.textNode.setVisible(false);
            }

         }

         public Object getBean() {
            return TickMark.this;
         }

         public String getName() {
            return "textVisible";
         }
      };

      public final String getLabel() {
         return (String)this.label.get();
      }

      public final void setLabel(String var1) {
         this.label.set(var1);
      }

      public final StringExpression labelProperty() {
         return this.label;
      }

      public final Object getValue() {
         return this.value.get();
      }

      public final void setValue(Object var1) {
         this.value.set(var1);
      }

      public final ObjectExpression valueProperty() {
         return this.value;
      }

      public final double getPosition() {
         return this.position.get();
      }

      public final void setPosition(double var1) {
         this.position.set(var1);
      }

      public final DoubleExpression positionProperty() {
         return this.position;
      }

      public final boolean isTextVisible() {
         return this.textVisible.get();
      }

      public final void setTextVisible(boolean var1) {
         this.textVisible.set(var1);
      }

      public String toString() {
         return this.value.get().toString();
      }
   }
}
