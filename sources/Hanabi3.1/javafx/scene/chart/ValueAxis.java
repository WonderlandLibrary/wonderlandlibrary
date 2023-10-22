package javafx.scene.chart;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Side;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.StringConverter;

public abstract class ValueAxis extends Axis {
   private final Path minorTickPath;
   private double offset;
   double dataMinValue;
   double dataMaxValue;
   private List minorTickMarkValues;
   protected final DoubleProperty currentLowerBound;
   private BooleanProperty minorTickVisible;
   private ReadOnlyDoubleWrapper scale;
   private DoubleProperty upperBound;
   private DoubleProperty lowerBound;
   private final ObjectProperty tickLabelFormatter;
   private DoubleProperty minorTickLength;
   private IntegerProperty minorTickCount;

   public final boolean isMinorTickVisible() {
      return this.minorTickVisible.get();
   }

   public final void setMinorTickVisible(boolean var1) {
      this.minorTickVisible.set(var1);
   }

   public final BooleanProperty minorTickVisibleProperty() {
      return this.minorTickVisible;
   }

   public final double getScale() {
      return this.scale.get();
   }

   protected final void setScale(double var1) {
      this.scale.set(var1);
   }

   public final ReadOnlyDoubleProperty scaleProperty() {
      return this.scale.getReadOnlyProperty();
   }

   ReadOnlyDoubleWrapper scalePropertyImpl() {
      return this.scale;
   }

   public final double getUpperBound() {
      return this.upperBound.get();
   }

   public final void setUpperBound(double var1) {
      this.upperBound.set(var1);
   }

   public final DoubleProperty upperBoundProperty() {
      return this.upperBound;
   }

   public final double getLowerBound() {
      return this.lowerBound.get();
   }

   public final void setLowerBound(double var1) {
      this.lowerBound.set(var1);
   }

   public final DoubleProperty lowerBoundProperty() {
      return this.lowerBound;
   }

   public final StringConverter getTickLabelFormatter() {
      return (StringConverter)this.tickLabelFormatter.getValue();
   }

   public final void setTickLabelFormatter(StringConverter var1) {
      this.tickLabelFormatter.setValue(var1);
   }

   public final ObjectProperty tickLabelFormatterProperty() {
      return this.tickLabelFormatter;
   }

   public final double getMinorTickLength() {
      return this.minorTickLength.get();
   }

   public final void setMinorTickLength(double var1) {
      this.minorTickLength.set(var1);
   }

   public final DoubleProperty minorTickLengthProperty() {
      return this.minorTickLength;
   }

   public final int getMinorTickCount() {
      return this.minorTickCount.get();
   }

   public final void setMinorTickCount(int var1) {
      this.minorTickCount.set(var1);
   }

   public final IntegerProperty minorTickCountProperty() {
      return this.minorTickCount;
   }

   public ValueAxis() {
      this.minorTickPath = new Path();
      this.minorTickMarkValues = null;
      this.currentLowerBound = new SimpleDoubleProperty(this, "currentLowerBound");
      this.minorTickVisible = new StyleableBooleanProperty(true) {
         protected void invalidated() {
            ValueAxis.this.minorTickPath.setVisible(this.get());
            ValueAxis.this.requestAxisLayout();
         }

         public Object getBean() {
            return ValueAxis.this;
         }

         public String getName() {
            return "minorTickVisible";
         }

         public CssMetaData getCssMetaData() {
            return ValueAxis.StyleableProperties.MINOR_TICK_VISIBLE;
         }
      };
      this.scale = new ReadOnlyDoubleWrapper(this, "scale", 0.0) {
         protected void invalidated() {
            ValueAxis.this.requestAxisLayout();
            ValueAxis.this.measureInvalid = true;
         }
      };
      this.upperBound = new DoublePropertyBase(100.0) {
         protected void invalidated() {
            if (!ValueAxis.this.isAutoRanging()) {
               ValueAxis.this.invalidateRange();
               ValueAxis.this.requestAxisLayout();
            }

         }

         public Object getBean() {
            return ValueAxis.this;
         }

         public String getName() {
            return "upperBound";
         }
      };
      this.lowerBound = new DoublePropertyBase(0.0) {
         protected void invalidated() {
            if (!ValueAxis.this.isAutoRanging()) {
               ValueAxis.this.invalidateRange();
               ValueAxis.this.requestAxisLayout();
            }

         }

         public Object getBean() {
            return ValueAxis.this;
         }

         public String getName() {
            return "lowerBound";
         }
      };
      this.tickLabelFormatter = new ObjectPropertyBase((StringConverter)null) {
         protected void invalidated() {
            ValueAxis.this.invalidateRange();
            ValueAxis.this.requestAxisLayout();
         }

         public Object getBean() {
            return ValueAxis.this;
         }

         public String getName() {
            return "tickLabelFormatter";
         }
      };
      this.minorTickLength = new StyleableDoubleProperty(5.0) {
         protected void invalidated() {
            ValueAxis.this.requestAxisLayout();
         }

         public Object getBean() {
            return ValueAxis.this;
         }

         public String getName() {
            return "minorTickLength";
         }

         public CssMetaData getCssMetaData() {
            return ValueAxis.StyleableProperties.MINOR_TICK_LENGTH;
         }
      };
      this.minorTickCount = new StyleableIntegerProperty(5) {
         protected void invalidated() {
            ValueAxis.this.invalidateRange();
            ValueAxis.this.requestAxisLayout();
         }

         public Object getBean() {
            return ValueAxis.this;
         }

         public String getName() {
            return "minorTickCount";
         }

         public CssMetaData getCssMetaData() {
            return ValueAxis.StyleableProperties.MINOR_TICK_COUNT;
         }
      };
      this.minorTickPath.getStyleClass().add("axis-minor-tick-mark");
      this.getChildren().add(this.minorTickPath);
   }

   public ValueAxis(double var1, double var3) {
      this();
      this.setAutoRanging(false);
      this.setLowerBound(var1);
      this.setUpperBound(var3);
   }

   protected final Object autoRange(double var1) {
      if (this.isAutoRanging()) {
         double var3 = this.getTickLabelFont().getSize() * 2.0;
         return this.autoRange(this.dataMinValue, this.dataMaxValue, var1, var3);
      } else {
         return this.getRange();
      }
   }

   protected final double calculateNewScale(double var1, double var3, double var5) {
      double var7 = 1.0;
      Side var9 = this.getEffectiveSide();
      if (var9.isVertical()) {
         this.offset = var1;
         var7 = var5 - var3 == 0.0 ? -var1 : -(var1 / (var5 - var3));
      } else {
         this.offset = 0.0;
         var7 = var5 - var3 == 0.0 ? var1 : var1 / (var5 - var3);
      }

      return var7;
   }

   protected Object autoRange(double var1, double var3, double var5, double var7) {
      return null;
   }

   protected abstract List calculateMinorTickMarks();

   protected void tickMarksUpdated() {
      super.tickMarksUpdated();
      this.minorTickMarkValues = this.calculateMinorTickMarks();
   }

   protected void layoutChildren() {
      Side var1 = this.getEffectiveSide();
      double var2 = var1.isVertical() ? this.getHeight() : this.getWidth();
      if (!this.isAutoRanging()) {
         this.setScale(this.calculateNewScale(var2, this.getLowerBound(), this.getUpperBound()));
         this.currentLowerBound.set(this.getLowerBound());
      }

      super.layoutChildren();
      int var4 = (this.getTickMarks().size() - 1) * (Math.max(1, this.getMinorTickCount()) - 1);
      double var5 = (double)((this.getTickMarks().size() + var4) * 2);
      this.minorTickPath.getElements().clear();
      double var7 = Math.max(0.0, this.getMinorTickLength());
      if (var7 > 0.0 && var2 > var5) {
         Iterator var9;
         Number var10;
         double var11;
         if (Side.LEFT.equals(var1)) {
            this.minorTickPath.setLayoutX(-0.5);
            this.minorTickPath.setLayoutY(0.5);
            var9 = this.minorTickMarkValues.iterator();

            while(var9.hasNext()) {
               var10 = (Number)var9.next();
               var11 = this.getDisplayPosition(var10);
               if (var11 >= 0.0 && var11 <= var2) {
                  this.minorTickPath.getElements().addAll(new MoveTo(this.getWidth() - var7, var11), new LineTo(this.getWidth() - 1.0, var11));
               }
            }
         } else if (Side.RIGHT.equals(var1)) {
            this.minorTickPath.setLayoutX(0.5);
            this.minorTickPath.setLayoutY(0.5);
            var9 = this.minorTickMarkValues.iterator();

            while(var9.hasNext()) {
               var10 = (Number)var9.next();
               var11 = this.getDisplayPosition(var10);
               if (var11 >= 0.0 && var11 <= var2) {
                  this.minorTickPath.getElements().addAll(new MoveTo(1.0, var11), new LineTo(var7, var11));
               }
            }
         } else if (Side.TOP.equals(var1)) {
            this.minorTickPath.setLayoutX(0.5);
            this.minorTickPath.setLayoutY(-0.5);
            var9 = this.minorTickMarkValues.iterator();

            while(var9.hasNext()) {
               var10 = (Number)var9.next();
               var11 = this.getDisplayPosition(var10);
               if (var11 >= 0.0 && var11 <= var2) {
                  this.minorTickPath.getElements().addAll(new MoveTo(var11, this.getHeight() - 1.0), new LineTo(var11, this.getHeight() - var7));
               }
            }
         } else {
            this.minorTickPath.setLayoutX(0.5);
            this.minorTickPath.setLayoutY(0.5);
            var9 = this.minorTickMarkValues.iterator();

            while(var9.hasNext()) {
               var10 = (Number)var9.next();
               var11 = this.getDisplayPosition(var10);
               if (var11 >= 0.0 && var11 <= var2) {
                  this.minorTickPath.getElements().addAll(new MoveTo(var11, 1.0), new LineTo(var11, var7));
               }
            }
         }
      }

   }

   public void invalidateRange(List var1) {
      if (var1.isEmpty()) {
         this.dataMaxValue = this.getUpperBound();
         this.dataMinValue = this.getLowerBound();
      } else {
         this.dataMinValue = Double.MAX_VALUE;
         this.dataMaxValue = -1.7976931348623157E308;
      }

      Number var3;
      for(Iterator var2 = var1.iterator(); var2.hasNext(); this.dataMaxValue = Math.max(this.dataMaxValue, var3.doubleValue())) {
         var3 = (Number)var2.next();
         this.dataMinValue = Math.min(this.dataMinValue, var3.doubleValue());
      }

      super.invalidateRange(var1);
   }

   public double getDisplayPosition(Number var1) {
      return this.offset + (var1.doubleValue() - this.currentLowerBound.get()) * this.getScale();
   }

   public Number getValueForDisplay(double var1) {
      return this.toRealValue((var1 - this.offset) / this.getScale() + this.currentLowerBound.get());
   }

   public double getZeroPosition() {
      return !(0.0 < this.getLowerBound()) && !(0.0 > this.getUpperBound()) ? this.getDisplayPosition((Number)0.0) : Double.NaN;
   }

   public boolean isValueOnAxis(Number var1) {
      double var2 = var1.doubleValue();
      return var2 >= this.getLowerBound() && var2 <= this.getUpperBound();
   }

   public double toNumericValue(Number var1) {
      return var1 == null ? Double.NaN : var1.doubleValue();
   }

   public Number toRealValue(double var1) {
      return new Double(var1);
   }

   public static List getClassCssMetaData() {
      return ValueAxis.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   private static class StyleableProperties {
      private static final CssMetaData MINOR_TICK_LENGTH = new CssMetaData("-fx-minor-tick-length", SizeConverter.getInstance(), 5.0) {
         public boolean isSettable(ValueAxis var1) {
            return var1.minorTickLength == null || !var1.minorTickLength.isBound();
         }

         public StyleableProperty getStyleableProperty(ValueAxis var1) {
            return (StyleableProperty)var1.minorTickLengthProperty();
         }
      };
      private static final CssMetaData MINOR_TICK_COUNT = new CssMetaData("-fx-minor-tick-count", SizeConverter.getInstance(), 5) {
         public boolean isSettable(ValueAxis var1) {
            return var1.minorTickCount == null || !var1.minorTickCount.isBound();
         }

         public StyleableProperty getStyleableProperty(ValueAxis var1) {
            return (StyleableProperty)var1.minorTickCountProperty();
         }
      };
      private static final CssMetaData MINOR_TICK_VISIBLE;
      private static final List STYLEABLES;

      static {
         MINOR_TICK_VISIBLE = new CssMetaData("-fx-minor-tick-visible", BooleanConverter.getInstance(), Boolean.TRUE) {
            public boolean isSettable(ValueAxis var1) {
               return var1.minorTickVisible == null || !var1.minorTickVisible.isBound();
            }

            public StyleableProperty getStyleableProperty(ValueAxis var1) {
               return (StyleableProperty)var1.minorTickVisibleProperty();
            }
         };
         ArrayList var0 = new ArrayList(Axis.getClassCssMetaData());
         var0.add(MINOR_TICK_COUNT);
         var0.add(MINOR_TICK_LENGTH);
         var0.add(MINOR_TICK_COUNT);
         var0.add(MINOR_TICK_VISIBLE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
