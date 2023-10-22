package javafx.scene.chart;

import com.sun.javafx.charts.ChartLayoutAnimator;
import com.sun.javafx.css.converters.SizeConverter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.util.Duration;
import javafx.util.StringConverter;

public final class NumberAxis extends ValueAxis {
   private Object currentAnimationID;
   private final ChartLayoutAnimator animator = new ChartLayoutAnimator(this);
   private final StringProperty currentFormatterProperty = new SimpleStringProperty(this, "currentFormatter", "");
   private final DefaultFormatter defaultFormatter = new DefaultFormatter(this);
   private BooleanProperty forceZeroInRange = new BooleanPropertyBase(true) {
      protected void invalidated() {
         if (NumberAxis.this.isAutoRanging()) {
            NumberAxis.this.requestAxisLayout();
            NumberAxis.this.invalidateRange();
         }

      }

      public Object getBean() {
         return NumberAxis.this;
      }

      public String getName() {
         return "forceZeroInRange";
      }
   };
   private DoubleProperty tickUnit = new StyleableDoubleProperty(5.0) {
      protected void invalidated() {
         if (!NumberAxis.this.isAutoRanging()) {
            NumberAxis.this.invalidateRange();
            NumberAxis.this.requestAxisLayout();
         }

      }

      public CssMetaData getCssMetaData() {
         return NumberAxis.StyleableProperties.TICK_UNIT;
      }

      public Object getBean() {
         return NumberAxis.this;
      }

      public String getName() {
         return "tickUnit";
      }
   };

   public final boolean isForceZeroInRange() {
      return this.forceZeroInRange.getValue();
   }

   public final void setForceZeroInRange(boolean var1) {
      this.forceZeroInRange.setValue(var1);
   }

   public final BooleanProperty forceZeroInRangeProperty() {
      return this.forceZeroInRange;
   }

   public final double getTickUnit() {
      return this.tickUnit.get();
   }

   public final void setTickUnit(double var1) {
      this.tickUnit.set(var1);
   }

   public final DoubleProperty tickUnitProperty() {
      return this.tickUnit;
   }

   public NumberAxis() {
   }

   public NumberAxis(double var1, double var3, double var5) {
      super(var1, var3);
      this.setTickUnit(var5);
   }

   public NumberAxis(String var1, double var2, double var4, double var6) {
      super(var2, var4);
      this.setTickUnit(var6);
      this.setLabel(var1);
   }

   protected String getTickMarkLabel(Number var1) {
      Object var2 = this.getTickLabelFormatter();
      if (var2 == null) {
         var2 = this.defaultFormatter;
      }

      return ((StringConverter)var2).toString(var1);
   }

   protected Object getRange() {
      return new Object[]{this.getLowerBound(), this.getUpperBound(), this.getTickUnit(), this.getScale(), this.currentFormatterProperty.get()};
   }

   protected void setRange(Object var1, boolean var2) {
      Object[] var3 = (Object[])((Object[])var1);
      double var4 = (Double)var3[0];
      double var6 = (Double)var3[1];
      double var8 = (Double)var3[2];
      double var10 = (Double)var3[3];
      String var12 = (String)var3[4];
      this.currentFormatterProperty.set(var12);
      double var13 = this.getLowerBound();
      this.setLowerBound(var4);
      this.setUpperBound(var6);
      this.setTickUnit(var8);
      if (var2) {
         this.animator.stop(this.currentAnimationID);
         this.currentAnimationID = this.animator.animate(new KeyFrame(Duration.ZERO, new KeyValue[]{new KeyValue(this.currentLowerBound, var13), new KeyValue(this.scalePropertyImpl(), this.getScale())}), new KeyFrame(Duration.millis(700.0), new KeyValue[]{new KeyValue(this.currentLowerBound, var4), new KeyValue(this.scalePropertyImpl(), var10)}));
      } else {
         this.currentLowerBound.set(var4);
         this.setScale(var10);
      }

   }

   protected List calculateTickValues(double var1, Object var3) {
      Object[] var4 = (Object[])((Object[])var3);
      double var5 = (Double)var4[0];
      double var7 = (Double)var4[1];
      double var9 = (Double)var4[2];
      ArrayList var11 = new ArrayList();
      if (var5 == var7) {
         var11.add(var5);
      } else if (var9 <= 0.0) {
         var11.add(var5);
         var11.add(var7);
      } else if (var9 > 0.0) {
         var11.add(var5);
         if ((var7 - var5) / var9 > 2000.0) {
            System.err.println("Warning we tried to create more than 2000 major tick marks on a NumberAxis. Lower Bound=" + var5 + ", Upper Bound=" + var7 + ", Tick Unit=" + var9);
         } else if (var5 + var9 < var7) {
            double var12 = Math.rint(var9) == var9 ? Math.ceil(var5) : var5 + var9;
            int var14 = (int)Math.ceil((var7 - var12) / var9);

            for(int var15 = 0; var12 < var7 && var15 < var14; ++var15) {
               if (!var11.contains(var12)) {
                  var11.add(var12);
               }

               var12 += var9;
            }
         }

         var11.add(var7);
      }

      return var11;
   }

   protected List calculateMinorTickMarks() {
      ArrayList var1 = new ArrayList();
      double var2 = this.getLowerBound();
      double var4 = this.getUpperBound();
      double var6 = this.getTickUnit();
      double var8 = var6 / (double)Math.max(1, this.getMinorTickCount());
      if (var6 > 0.0) {
         if ((var4 - var2) / var8 > 10000.0) {
            System.err.println("Warning we tried to create more than 10000 minor tick marks on a NumberAxis. Lower Bound=" + this.getLowerBound() + ", Upper Bound=" + this.getUpperBound() + ", Tick Unit=" + var6);
            return var1;
         }

         boolean var10 = Math.rint(var6) == var6;
         double var11;
         int var13;
         int var14;
         if (var10) {
            var11 = Math.floor(var2) + var8;
            var13 = (int)Math.ceil((Math.ceil(var2) - var11) / var8);

            for(var14 = 0; var11 < Math.ceil(var2) && var14 < var13; ++var14) {
               if (var11 > var2) {
                  var1.add(var11);
               }

               var11 += var8;
            }
         }

         var11 = var10 ? Math.ceil(var2) : var2;
         var13 = (int)Math.ceil((var4 - var11) / var6);

         for(var14 = 0; var11 < var4 && var14 < var13; ++var14) {
            double var15 = Math.min(var11 + var6, var4);
            double var17 = var11 + var8;
            int var19 = (int)Math.ceil((var15 - var17) / var8);

            for(int var20 = 0; var17 < var15 && var20 < var19; ++var20) {
               var1.add(var17);
               var17 += var8;
            }

            var11 += var6;
         }
      }

      return var1;
   }

   protected Dimension2D measureTickMarkSize(Number var1, Object var2) {
      Object[] var3 = (Object[])((Object[])var2);
      String var4 = (String)var3[4];
      return this.measureTickMarkSize(var1, this.getTickLabelRotation(), var4);
   }

   private Dimension2D measureTickMarkSize(Number var1, double var2, String var4) {
      Object var6 = this.getTickLabelFormatter();
      if (var6 == null) {
         var6 = this.defaultFormatter;
      }

      String var5;
      if (var6 instanceof DefaultFormatter) {
         var5 = ((DefaultFormatter)var6).toString(var1, var4);
      } else {
         var5 = ((StringConverter)var6).toString(var1);
      }

      return this.measureTickMarkLabelSize(var5, var2);
   }

   protected Object autoRange(double var1, double var3, double var5, double var7) {
      Side var9 = this.getEffectiveSide();
      if (this.isForceZeroInRange()) {
         if (var3 < 0.0) {
            var3 = 0.0;
         } else if (var1 > 0.0) {
            var1 = 0.0;
         }
      }

      int var10 = (int)Math.floor(var5 / var7);
      var10 = Math.max(var10, 2);
      int var11 = Math.max(this.getMinorTickCount(), 1);
      double var12 = var3 - var1;
      if (var12 != 0.0 && var12 / (double)(var10 * var11) <= Math.ulp(var1)) {
         var12 = 0.0;
      }

      double var14 = var12 == 0.0 ? (var1 == 0.0 ? 2.0 : Math.abs(var1) * 0.02) : Math.abs(var12) * 1.02;
      double var16 = (var14 - var12) / 2.0;
      double var18 = var1 - var16;
      double var20 = var3 + var16;
      if (var18 < 0.0 && var1 >= 0.0 || var18 > 0.0 && var1 <= 0.0) {
         var18 = 0.0;
      }

      if (var20 < 0.0 && var3 >= 0.0 || var20 > 0.0 && var3 <= 0.0) {
         var20 = 0.0;
      }

      double var22 = var14 / (double)var10;
      double var24 = 0.0;
      double var26 = 0.0;
      double var28 = 0.0;
      int var30 = 0;
      double var31 = Double.MAX_VALUE;
      String var33 = "0.00000000";

      while(var31 > var5 || var30 > 20) {
         int var34 = (int)Math.floor(Math.log10(var22));
         double var35 = var22 / Math.pow(10.0, (double)var34);
         double var37 = var35;
         if (var35 > 5.0) {
            ++var34;
            var37 = 1.0;
         } else if (var35 > 1.0) {
            var37 = var35 > 2.5 ? 5.0 : 2.5;
         }

         if (var34 > 1) {
            var33 = "#,##0";
         } else if (var34 == 1) {
            var33 = "0";
         } else {
            boolean var39 = Math.rint(var37) != var37;
            StringBuilder var40 = new StringBuilder("0");
            int var41 = var39 ? Math.abs(var34) + 1 : Math.abs(var34);
            if (var41 > 0) {
               var40.append(".");
            }

            for(int var42 = 0; var42 < var41; ++var42) {
               var40.append("0");
            }

            var33 = var40.toString();
         }

         var24 = var37 * Math.pow(10.0, (double)var34);
         var26 = Math.floor(var18 / var24) * var24;
         var28 = Math.ceil(var20 / var24) * var24;
         double var50 = 0.0;
         double var51 = 0.0;
         var30 = (int)Math.ceil((var28 - var26) / var24);
         double var43 = var26;

         for(int var45 = 0; var43 <= var28 && var45 < var30; ++var45) {
            Dimension2D var46 = this.measureTickMarkSize(var43, this.getTickLabelRotation(), var33);
            double var47 = var9.isVertical() ? var46.getHeight() : var46.getWidth();
            if (var45 == 0) {
               var51 = var47 / 2.0;
            } else {
               var50 = Math.max(var50, var51 + 6.0 + var47 / 2.0);
            }

            var43 += var24;
         }

         var31 = (double)(var30 - 1) * var50;
         var22 = var24;
         if (var10 == 2 && var31 > var5) {
            break;
         }

         if (var31 > var5 || var30 > 20) {
            var22 = var24 * 2.0;
         }
      }

      double var49 = this.calculateNewScale(var5, var26, var28);
      return new Object[]{var26, var28, var24, var49, var33};
   }

   public static List getClassCssMetaData() {
      return NumberAxis.StyleableProperties.STYLEABLES;
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public static class DefaultFormatter extends StringConverter {
      private DecimalFormat formatter;
      private String prefix;
      private String suffix;

      public DefaultFormatter(NumberAxis var1) {
         this.prefix = null;
         this.suffix = null;
         this.formatter = var1.isAutoRanging() ? new DecimalFormat((String)var1.currentFormatterProperty.get()) : new DecimalFormat();
         ChangeListener var2 = (var2x, var3, var4) -> {
            this.formatter = var1.isAutoRanging() ? new DecimalFormat((String)var1.currentFormatterProperty.get()) : new DecimalFormat();
         };
         var1.currentFormatterProperty.addListener(var2);
         var1.autoRangingProperty().addListener(var2);
      }

      public DefaultFormatter(NumberAxis var1, String var2, String var3) {
         this(var1);
         this.prefix = var2;
         this.suffix = var3;
      }

      public String toString(Number var1) {
         return this.toString(var1, this.formatter);
      }

      private String toString(Number var1, String var2) {
         return var2 != null && !var2.isEmpty() ? this.toString(var1, new DecimalFormat(var2)) : this.toString(var1, this.formatter);
      }

      private String toString(Number var1, DecimalFormat var2) {
         if (this.prefix != null && this.suffix != null) {
            return this.prefix + var2.format(var1) + this.suffix;
         } else if (this.prefix != null) {
            return this.prefix + var2.format(var1);
         } else {
            return this.suffix != null ? var2.format(var1) + this.suffix : var2.format(var1);
         }
      }

      public Number fromString(String var1) {
         try {
            int var2 = this.prefix == null ? 0 : this.prefix.length();
            int var3 = this.suffix == null ? 0 : this.suffix.length();
            return this.formatter.parse(var1.substring(var2, var1.length() - var3));
         } catch (ParseException var4) {
            return null;
         }
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData TICK_UNIT = new CssMetaData("-fx-tick-unit", SizeConverter.getInstance(), 5.0) {
         public boolean isSettable(NumberAxis var1) {
            return var1.tickUnit == null || !var1.tickUnit.isBound();
         }

         public StyleableProperty getStyleableProperty(NumberAxis var1) {
            return (StyleableProperty)var1.tickUnitProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(ValueAxis.getClassCssMetaData());
         var0.add(TICK_UNIT);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
