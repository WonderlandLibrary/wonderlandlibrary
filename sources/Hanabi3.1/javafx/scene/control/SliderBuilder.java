package javafx.scene.control;

import javafx.geometry.Orientation;
import javafx.util.Builder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class SliderBuilder extends ControlBuilder implements Builder {
   private int __set;
   private double blockIncrement;
   private StringConverter labelFormatter;
   private double majorTickUnit;
   private double max;
   private double min;
   private int minorTickCount;
   private Orientation orientation;
   private boolean showTickLabels;
   private boolean showTickMarks;
   private boolean snapToTicks;
   private double value;
   private boolean valueChanging;

   protected SliderBuilder() {
   }

   public static SliderBuilder create() {
      return new SliderBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Slider var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setBlockIncrement(this.blockIncrement);
               break;
            case 1:
               var1.setLabelFormatter(this.labelFormatter);
               break;
            case 2:
               var1.setMajorTickUnit(this.majorTickUnit);
               break;
            case 3:
               var1.setMax(this.max);
               break;
            case 4:
               var1.setMin(this.min);
               break;
            case 5:
               var1.setMinorTickCount(this.minorTickCount);
               break;
            case 6:
               var1.setOrientation(this.orientation);
               break;
            case 7:
               var1.setShowTickLabels(this.showTickLabels);
               break;
            case 8:
               var1.setShowTickMarks(this.showTickMarks);
               break;
            case 9:
               var1.setSnapToTicks(this.snapToTicks);
               break;
            case 10:
               var1.setValue(this.value);
               break;
            case 11:
               var1.setValueChanging(this.valueChanging);
         }
      }

   }

   public SliderBuilder blockIncrement(double var1) {
      this.blockIncrement = var1;
      this.__set(0);
      return this;
   }

   public SliderBuilder labelFormatter(StringConverter var1) {
      this.labelFormatter = var1;
      this.__set(1);
      return this;
   }

   public SliderBuilder majorTickUnit(double var1) {
      this.majorTickUnit = var1;
      this.__set(2);
      return this;
   }

   public SliderBuilder max(double var1) {
      this.max = var1;
      this.__set(3);
      return this;
   }

   public SliderBuilder min(double var1) {
      this.min = var1;
      this.__set(4);
      return this;
   }

   public SliderBuilder minorTickCount(int var1) {
      this.minorTickCount = var1;
      this.__set(5);
      return this;
   }

   public SliderBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set(6);
      return this;
   }

   public SliderBuilder showTickLabels(boolean var1) {
      this.showTickLabels = var1;
      this.__set(7);
      return this;
   }

   public SliderBuilder showTickMarks(boolean var1) {
      this.showTickMarks = var1;
      this.__set(8);
      return this;
   }

   public SliderBuilder snapToTicks(boolean var1) {
      this.snapToTicks = var1;
      this.__set(9);
      return this;
   }

   public SliderBuilder value(double var1) {
      this.value = var1;
      this.__set(10);
      return this;
   }

   public SliderBuilder valueChanging(boolean var1) {
      this.valueChanging = var1;
      this.__set(11);
      return this;
   }

   public Slider build() {
      Slider var1 = new Slider();
      this.applyTo(var1);
      return var1;
   }
}
