package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.ScrollBarSkin;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

public class ScrollBar extends Control {
   private DoubleProperty min;
   private DoubleProperty max;
   private DoubleProperty value;
   private ObjectProperty orientation;
   private DoubleProperty unitIncrement;
   private DoubleProperty blockIncrement;
   private DoubleProperty visibleAmount;
   private static final String DEFAULT_STYLE_CLASS = "scroll-bar";
   private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
   private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

   public ScrollBar() {
      this.setWidth(20.0);
      this.setHeight(100.0);
      this.getStyleClass().setAll((Object[])("scroll-bar"));
      this.setAccessibleRole(AccessibleRole.SCROLL_BAR);
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      this.pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
   }

   public final void setMin(double var1) {
      this.minProperty().set(var1);
   }

   public final double getMin() {
      return this.min == null ? 0.0 : this.min.get();
   }

   public final DoubleProperty minProperty() {
      if (this.min == null) {
         this.min = new SimpleDoubleProperty(this, "min");
      }

      return this.min;
   }

   public final void setMax(double var1) {
      this.maxProperty().set(var1);
   }

   public final double getMax() {
      return this.max == null ? 100.0 : this.max.get();
   }

   public final DoubleProperty maxProperty() {
      if (this.max == null) {
         this.max = new SimpleDoubleProperty(this, "max", 100.0);
      }

      return this.max;
   }

   public final void setValue(double var1) {
      this.valueProperty().set(var1);
   }

   public final double getValue() {
      return this.value == null ? 0.0 : this.value.get();
   }

   public final DoubleProperty valueProperty() {
      if (this.value == null) {
         this.value = new SimpleDoubleProperty(this, "value");
      }

      return this.value;
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
               ScrollBar.this.pseudoClassStateChanged(ScrollBar.VERTICAL_PSEUDOCLASS_STATE, var1);
               ScrollBar.this.pseudoClassStateChanged(ScrollBar.HORIZONTAL_PSEUDOCLASS_STATE, !var1);
            }

            public CssMetaData getCssMetaData() {
               return ScrollBar.StyleableProperties.ORIENTATION;
            }

            public Object getBean() {
               return ScrollBar.this;
            }

            public String getName() {
               return "orientation";
            }
         };
      }

      return this.orientation;
   }

   public final void setUnitIncrement(double var1) {
      this.unitIncrementProperty().set(var1);
   }

   public final double getUnitIncrement() {
      return this.unitIncrement == null ? 1.0 : this.unitIncrement.get();
   }

   public final DoubleProperty unitIncrementProperty() {
      if (this.unitIncrement == null) {
         this.unitIncrement = new StyleableDoubleProperty(1.0) {
            public CssMetaData getCssMetaData() {
               return ScrollBar.StyleableProperties.UNIT_INCREMENT;
            }

            public Object getBean() {
               return ScrollBar.this;
            }

            public String getName() {
               return "unitIncrement";
            }
         };
      }

      return this.unitIncrement;
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
               return ScrollBar.StyleableProperties.BLOCK_INCREMENT;
            }

            public Object getBean() {
               return ScrollBar.this;
            }

            public String getName() {
               return "blockIncrement";
            }
         };
      }

      return this.blockIncrement;
   }

   public final void setVisibleAmount(double var1) {
      this.visibleAmountProperty().set(var1);
   }

   public final double getVisibleAmount() {
      return this.visibleAmount == null ? 15.0 : this.visibleAmount.get();
   }

   public final DoubleProperty visibleAmountProperty() {
      if (this.visibleAmount == null) {
         this.visibleAmount = new SimpleDoubleProperty(this, "visibleAmount");
      }

      return this.visibleAmount;
   }

   public void adjustValue(double var1) {
      double var3 = (this.getMax() - this.getMin()) * Utils.clamp(0.0, var1, 1.0) + this.getMin();
      if (Double.compare(var3, this.getValue()) != 0) {
         double var5;
         if (var3 > this.getValue()) {
            var5 = this.getValue() + this.getBlockIncrement();
         } else {
            var5 = this.getValue() - this.getBlockIncrement();
         }

         boolean var7 = var1 > (this.getValue() - this.getMin()) / (this.getMax() - this.getMin());
         if (var7 && var5 > var3) {
            var5 = var3;
         }

         if (!var7 && var5 < var3) {
            var5 = var3;
         }

         this.setValue(Utils.clamp(this.getMin(), var5, this.getMax()));
      }

   }

   public void increment() {
      this.setValue(Utils.clamp(this.getMin(), this.getValue() + this.getUnitIncrement(), this.getMax()));
   }

   public void decrement() {
      this.setValue(Utils.clamp(this.getMin(), this.getValue() - this.getUnitIncrement(), this.getMax()));
   }

   private void blockIncrement() {
      this.adjustValue(this.getValue() + this.getBlockIncrement());
   }

   private void blockDecrement() {
      this.adjustValue(this.getValue() - this.getBlockIncrement());
   }

   protected Skin createDefaultSkin() {
      return new ScrollBarSkin(this);
   }

   public static List getClassCssMetaData() {
      return ScrollBar.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
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
         case BLOCK_INCREMENT:
            this.blockIncrement();
            break;
         case BLOCK_DECREMENT:
            this.blockDecrement();
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
      private static final CssMetaData ORIENTATION;
      private static final CssMetaData UNIT_INCREMENT;
      private static final CssMetaData BLOCK_INCREMENT;
      private static final List STYLEABLES;

      static {
         ORIENTATION = new CssMetaData("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) {
            public Orientation getInitialValue(ScrollBar var1) {
               return var1.getOrientation();
            }

            public boolean isSettable(ScrollBar var1) {
               return var1.orientation == null || !var1.orientation.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollBar var1) {
               return (StyleableProperty)var1.orientationProperty();
            }
         };
         UNIT_INCREMENT = new CssMetaData("-fx-unit-increment", SizeConverter.getInstance(), 1.0) {
            public boolean isSettable(ScrollBar var1) {
               return var1.unitIncrement == null || !var1.unitIncrement.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollBar var1) {
               return (StyleableProperty)var1.unitIncrementProperty();
            }
         };
         BLOCK_INCREMENT = new CssMetaData("-fx-block-increment", SizeConverter.getInstance(), 10.0) {
            public boolean isSettable(ScrollBar var1) {
               return var1.blockIncrement == null || !var1.blockIncrement.isBound();
            }

            public StyleableProperty getStyleableProperty(ScrollBar var1) {
               return (StyleableProperty)var1.blockIncrementProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(ORIENTATION);
         var0.add(UNIT_INCREMENT);
         var0.add(BLOCK_INCREMENT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
