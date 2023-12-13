package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.SliderSkin;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.StringConverter;

public class Slider extends Control {
   private DoubleProperty max;
   private DoubleProperty min;
   private DoubleProperty value;
   private BooleanProperty valueChanging;
   private ObjectProperty orientation;
   private BooleanProperty showTickLabels;
   private BooleanProperty showTickMarks;
   private DoubleProperty majorTickUnit;
   private IntegerProperty minorTickCount;
   private BooleanProperty snapToTicks;
   private ObjectProperty labelFormatter;
   private DoubleProperty blockIncrement;
   private static final String DEFAULT_STYLE_CLASS = "slider";
   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

   public Slider() {
      this.initialize();
   }

   public Slider(double var1, double var3, double var5) {
      this.setMax(var3);
      this.setMin(var1);
      this.setValue(var5);
      this.adjustValues();
      this.initialize();
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("slider"));
      this.setAccessibleRole(AccessibleRole.SLIDER);
   }

   public final void setMax(double var1) {
      this.maxProperty().set(var1);
   }

   public final double getMax() {
      return this.max == null ? 100.0 : this.max.get();
   }

   public final DoubleProperty maxProperty() {
      if (this.max == null) {
         this.max = new DoublePropertyBase(100.0) {
            protected void invalidated() {
               if (this.get() < Slider.this.getMin()) {
                  Slider.this.setMin(this.get());
               }

               Slider.this.adjustValues();
               Slider.this.notifyAccessibleAttributeChanged(AccessibleAttribute.MAX_VALUE);
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "max";
            }
         };
      }

      return this.max;
   }

   public final void setMin(double var1) {
      this.minProperty().set(var1);
   }

   public final double getMin() {
      return this.min == null ? 0.0 : this.min.get();
   }

   public final DoubleProperty minProperty() {
      if (this.min == null) {
         this.min = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               if (this.get() > Slider.this.getMax()) {
                  Slider.this.setMax(this.get());
               }

               Slider.this.adjustValues();
               Slider.this.notifyAccessibleAttributeChanged(AccessibleAttribute.MIN_VALUE);
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "min";
            }
         };
      }

      return this.min;
   }

   public final void setValue(double var1) {
      if (!this.valueProperty().isBound()) {
         this.valueProperty().set(var1);
      }

   }

   public final double getValue() {
      return this.value == null ? 0.0 : this.value.get();
   }

   public final DoubleProperty valueProperty() {
      if (this.value == null) {
         this.value = new DoublePropertyBase(0.0) {
            protected void invalidated() {
               Slider.this.adjustValues();
               Slider.this.notifyAccessibleAttributeChanged(AccessibleAttribute.VALUE);
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "value";
            }
         };
      }

      return this.value;
   }

   public final void setValueChanging(boolean var1) {
      this.valueChangingProperty().set(var1);
   }

   public final boolean isValueChanging() {
      return this.valueChanging == null ? false : this.valueChanging.get();
   }

   public final BooleanProperty valueChangingProperty() {
      if (this.valueChanging == null) {
         this.valueChanging = new SimpleBooleanProperty(this, "valueChanging", false);
      }

      return this.valueChanging;
   }

   public final void setOrientation(Orientation var1) {
      this.orientationProperty().set(var1);
   }

   public final Orientation getOrientation() {
      return this.orientation == null ? Orientation.HORIZONTAL : (Orientation)this.orientation.get();
   }

   public final ObjectProperty orientationProperty() {
      if (this.orientation == null) {
         this.orientation = new StyleableObjectProperty(Orientation.HORIZONTAL) {
            protected void invalidated() {
               boolean var1 = this.get() == Orientation.VERTICAL;
               Slider.this.pseudoClassStateChanged(Slider.VERTICAL_PSEUDOCLASS_STATE, var1);
               Slider.this.pseudoClassStateChanged(Slider.HORIZONTAL_PSEUDOCLASS_STATE, !var1);
            }

            public CssMetaData getCssMetaData() {
               return Slider.StyleableProperties.ORIENTATION;
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "orientation";
            }
         };
      }

      return this.orientation;
   }

   public final void setShowTickLabels(boolean var1) {
      this.showTickLabelsProperty().set(var1);
   }

   public final boolean isShowTickLabels() {
      return this.showTickLabels == null ? false : this.showTickLabels.get();
   }

   public final BooleanProperty showTickLabelsProperty() {
      if (this.showTickLabels == null) {
         this.showTickLabels = new StyleableBooleanProperty(false) {
            public CssMetaData getCssMetaData() {
               return Slider.StyleableProperties.SHOW_TICK_LABELS;
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "showTickLabels";
            }
         };
      }

      return this.showTickLabels;
   }

   public final void setShowTickMarks(boolean var1) {
      this.showTickMarksProperty().set(var1);
   }

   public final boolean isShowTickMarks() {
      return this.showTickMarks == null ? false : this.showTickMarks.get();
   }

   public final BooleanProperty showTickMarksProperty() {
      if (this.showTickMarks == null) {
         this.showTickMarks = new StyleableBooleanProperty(false) {
            public CssMetaData getCssMetaData() {
               return Slider.StyleableProperties.SHOW_TICK_MARKS;
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "showTickMarks";
            }
         };
      }

      return this.showTickMarks;
   }

   public final void setMajorTickUnit(double var1) {
      if (var1 <= 0.0) {
         throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0.");
      } else {
         this.majorTickUnitProperty().set(var1);
      }
   }

   public final double getMajorTickUnit() {
      return this.majorTickUnit == null ? 25.0 : this.majorTickUnit.get();
   }

   public final DoubleProperty majorTickUnitProperty() {
      if (this.majorTickUnit == null) {
         this.majorTickUnit = new StyleableDoubleProperty(25.0) {
            public void invalidated() {
               if (this.get() <= 0.0) {
                  throw new IllegalArgumentException("MajorTickUnit cannot be less than or equal to 0.");
               }
            }

            public CssMetaData getCssMetaData() {
               return Slider.StyleableProperties.MAJOR_TICK_UNIT;
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "majorTickUnit";
            }
         };
      }

      return this.majorTickUnit;
   }

   public final void setMinorTickCount(int var1) {
      this.minorTickCountProperty().set(var1);
   }

   public final int getMinorTickCount() {
      return this.minorTickCount == null ? 3 : this.minorTickCount.get();
   }

   public final IntegerProperty minorTickCountProperty() {
      if (this.minorTickCount == null) {
         this.minorTickCount = new StyleableIntegerProperty(3) {
            public CssMetaData getCssMetaData() {
               return Slider.StyleableProperties.MINOR_TICK_COUNT;
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "minorTickCount";
            }
         };
      }

      return this.minorTickCount;
   }

   public final void setSnapToTicks(boolean var1) {
      this.snapToTicksProperty().set(var1);
   }

   public final boolean isSnapToTicks() {
      return this.snapToTicks == null ? false : this.snapToTicks.get();
   }

   public final BooleanProperty snapToTicksProperty() {
      if (this.snapToTicks == null) {
         this.snapToTicks = new StyleableBooleanProperty(false) {
            public CssMetaData getCssMetaData() {
               return Slider.StyleableProperties.SNAP_TO_TICKS;
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "snapToTicks";
            }
         };
      }

      return this.snapToTicks;
   }

   public final void setLabelFormatter(StringConverter var1) {
      this.labelFormatterProperty().set(var1);
   }

   public final StringConverter getLabelFormatter() {
      return this.labelFormatter == null ? null : (StringConverter)this.labelFormatter.get();
   }

   public final ObjectProperty labelFormatterProperty() {
      if (this.labelFormatter == null) {
         this.labelFormatter = new SimpleObjectProperty(this, "labelFormatter");
      }

      return this.labelFormatter;
   }

   public final void setBlockIncrement(double var1) {
      this.blockIncrementProperty().set(var1);
   }

   public final double getBlockIncrement() {
      return this.blockIncrement == null ? 10.0 : this.blockIncrement.get();
   }

   public final DoubleProperty blockIncrementProperty() {
      if (this.blockIncrement == null) {
         this.blockIncrement = new StyleableDoubleProperty(10.0) {
            public CssMetaData getCssMetaData() {
               return Slider.StyleableProperties.BLOCK_INCREMENT;
            }

            public Object getBean() {
               return Slider.this;
            }

            public String getName() {
               return "blockIncrement";
            }
         };
      }

      return this.blockIncrement;
   }

   public void adjustValue(double var1) {
      double var3 = this.getMin();
      double var5 = this.getMax();
      if (!(var5 <= var3)) {
         var1 = var1 < var3 ? var3 : var1;
         var1 = var1 > var5 ? var5 : var1;
         this.setValue(this.snapValueToTicks(var1));
      }
   }

   public void increment() {
      this.adjustValue(this.getValue() + this.getBlockIncrement());
   }

   public void decrement() {
      this.adjustValue(this.getValue() - this.getBlockIncrement());
   }

   private void adjustValues() {
      if (this.getValue() < this.getMin() || this.getValue() > this.getMax()) {
         this.setValue(Utils.clamp(this.getMin(), this.getValue(), this.getMax()));
      }

   }

   private double snapValueToTicks(double var1) {
      double var3 = var1;
      if (this.isSnapToTicks()) {
         double var5 = 0.0;
         if (this.getMinorTickCount() != 0) {
            var5 = this.getMajorTickUnit() / (double)(Math.max(this.getMinorTickCount(), 0) + 1);
         } else {
            var5 = this.getMajorTickUnit();
         }

         int var7 = (int)((var1 - this.getMin()) / var5);
         double var8 = (double)var7 * var5 + this.getMin();
         double var10 = (double)(var7 + 1) * var5 + this.getMin();
         var3 = Utils.nearest(var8, var1, var10);
      }

      return Utils.clamp(this.getMin(), var3, this.getMax());
   }

   protected Skin createDefaultSkin() {
      return new SliderSkin(this);
   }

   public static List getClassCssMetaData() {
      return Slider.StyleableProperties.STYLEABLES;
   }

   /** @deprecated */
   @Deprecated
   protected List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case VALUE:
            return this.getValue();
         case MAX_VALUE:
            return this.getMax();
         case MIN_VALUE:
            return this.getMin();
         case ORIENTATION:
            return this.getOrientation();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case INCREMENT:
            this.increment();
            break;
         case DECREMENT:
            this.decrement();
            break;
         case SET_VALUE:
            Double var3 = (Double)var2[0];
            if (var3 != null) {
               this.setValue(var3);
            }
            break;
         default:
            super.executeAccessibleAction(var1, var2);
      }

   }

   private static class StyleableProperties {
      private static final CssMetaData BLOCK_INCREMENT = new CssMetaData("-fx-block-increment", SizeConverter.getInstance(), 10.0) {
         public boolean isSettable(Slider var1) {
            return var1.blockIncrement == null || !var1.blockIncrement.isBound();
         }

         public StyleableProperty getStyleableProperty(Slider var1) {
            return (StyleableProperty)var1.blockIncrementProperty();
         }
      };
      private static final CssMetaData SHOW_TICK_LABELS;
      private static final CssMetaData SHOW_TICK_MARKS;
      private static final CssMetaData SNAP_TO_TICKS;
      private static final CssMetaData MAJOR_TICK_UNIT;
      private static final CssMetaData MINOR_TICK_COUNT;
      private static final CssMetaData ORIENTATION;
      private static final List STYLEABLES;

      static {
         SHOW_TICK_LABELS = new CssMetaData("-fx-show-tick-labels", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(Slider var1) {
               return var1.showTickLabels == null || !var1.showTickLabels.isBound();
            }

            public StyleableProperty getStyleableProperty(Slider var1) {
               return (StyleableProperty)var1.showTickLabelsProperty();
            }
         };
         SHOW_TICK_MARKS = new CssMetaData("-fx-show-tick-marks", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(Slider var1) {
               return var1.showTickMarks == null || !var1.showTickMarks.isBound();
            }

            public StyleableProperty getStyleableProperty(Slider var1) {
               return (StyleableProperty)var1.showTickMarksProperty();
            }
         };
         SNAP_TO_TICKS = new CssMetaData("-fx-snap-to-ticks", BooleanConverter.getInstance(), Boolean.FALSE) {
            public boolean isSettable(Slider var1) {
               return var1.snapToTicks == null || !var1.snapToTicks.isBound();
            }

            public StyleableProperty getStyleableProperty(Slider var1) {
               return (StyleableProperty)var1.snapToTicksProperty();
            }
         };
         MAJOR_TICK_UNIT = new CssMetaData("-fx-major-tick-unit", SizeConverter.getInstance(), 25.0) {
            public boolean isSettable(Slider var1) {
               return var1.majorTickUnit == null || !var1.majorTickUnit.isBound();
            }

            public StyleableProperty getStyleableProperty(Slider var1) {
               return (StyleableProperty)var1.majorTickUnitProperty();
            }
         };
         MINOR_TICK_COUNT = new CssMetaData("-fx-minor-tick-count", SizeConverter.getInstance(), 3.0) {
            public boolean isSettable(Slider var1) {
               return var1.minorTickCount == null || !var1.minorTickCount.isBound();
            }

            public StyleableProperty getStyleableProperty(Slider var1) {
               return (StyleableProperty)var1.minorTickCountProperty();
            }
         };
         ORIENTATION = new CssMetaData("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) {
            public Orientation getInitialValue(Slider var1) {
               return var1.getOrientation();
            }

            public boolean isSettable(Slider var1) {
               return var1.orientation == null || !var1.orientation.isBound();
            }

            public StyleableProperty getStyleableProperty(Slider var1) {
               return (StyleableProperty)var1.orientationProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(BLOCK_INCREMENT);
         var0.add(SHOW_TICK_LABELS);
         var0.add(SHOW_TICK_MARKS);
         var0.add(SNAP_TO_TICKS);
         var0.add(MAJOR_TICK_UNIT);
         var0.add(MINOR_TICK_COUNT);
         var0.add(ORIENTATION);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
