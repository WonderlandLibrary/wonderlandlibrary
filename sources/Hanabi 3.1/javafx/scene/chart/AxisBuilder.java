package javafx.scene.chart;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.scene.layout.RegionBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/** @deprecated */
@Deprecated
public abstract class AxisBuilder extends RegionBuilder {
   private int __set;
   private boolean animated;
   private boolean autoRanging;
   private String label;
   private Side side;
   private Paint tickLabelFill;
   private Font tickLabelFont;
   private double tickLabelGap;
   private double tickLabelRotation;
   private boolean tickLabelsVisible;
   private double tickLength;
   private Collection tickMarks;
   private boolean tickMarkVisible;

   protected AxisBuilder() {
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Axis var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setAnimated(this.animated);
               break;
            case 1:
               var1.setAutoRanging(this.autoRanging);
               break;
            case 2:
               var1.setLabel(this.label);
               break;
            case 3:
               var1.setSide(this.side);
               break;
            case 4:
               var1.setTickLabelFill(this.tickLabelFill);
               break;
            case 5:
               var1.setTickLabelFont(this.tickLabelFont);
               break;
            case 6:
               var1.setTickLabelGap(this.tickLabelGap);
               break;
            case 7:
               var1.setTickLabelRotation(this.tickLabelRotation);
               break;
            case 8:
               var1.setTickLabelsVisible(this.tickLabelsVisible);
               break;
            case 9:
               var1.setTickLength(this.tickLength);
               break;
            case 10:
               var1.getTickMarks().addAll(this.tickMarks);
               break;
            case 11:
               var1.setTickMarkVisible(this.tickMarkVisible);
         }
      }

   }

   public AxisBuilder animated(boolean var1) {
      this.animated = var1;
      this.__set(0);
      return this;
   }

   public AxisBuilder autoRanging(boolean var1) {
      this.autoRanging = var1;
      this.__set(1);
      return this;
   }

   public AxisBuilder label(String var1) {
      this.label = var1;
      this.__set(2);
      return this;
   }

   public AxisBuilder side(Side var1) {
      this.side = var1;
      this.__set(3);
      return this;
   }

   public AxisBuilder tickLabelFill(Paint var1) {
      this.tickLabelFill = var1;
      this.__set(4);
      return this;
   }

   public AxisBuilder tickLabelFont(Font var1) {
      this.tickLabelFont = var1;
      this.__set(5);
      return this;
   }

   public AxisBuilder tickLabelGap(double var1) {
      this.tickLabelGap = var1;
      this.__set(6);
      return this;
   }

   public AxisBuilder tickLabelRotation(double var1) {
      this.tickLabelRotation = var1;
      this.__set(7);
      return this;
   }

   public AxisBuilder tickLabelsVisible(boolean var1) {
      this.tickLabelsVisible = var1;
      this.__set(8);
      return this;
   }

   public AxisBuilder tickLength(double var1) {
      this.tickLength = var1;
      this.__set(9);
      return this;
   }

   public AxisBuilder tickMarks(Collection var1) {
      this.tickMarks = var1;
      this.__set(10);
      return this;
   }

   public AxisBuilder tickMarks(Axis.TickMark... var1) {
      return this.tickMarks((Collection)Arrays.asList(var1));
   }

   public AxisBuilder tickMarkVisible(boolean var1) {
      this.tickMarkVisible = var1;
      this.__set(11);
      return this;
   }
}
