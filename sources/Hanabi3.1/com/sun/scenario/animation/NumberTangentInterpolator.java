package com.sun.scenario.animation;

import com.sun.javafx.animation.TickCalculation;
import javafx.animation.Interpolator;
import javafx.util.Duration;

public class NumberTangentInterpolator extends Interpolator {
   private final double inValue;
   private final double outValue;
   private final long inTicks;
   private final long outTicks;

   public double getInValue() {
      return this.inValue;
   }

   public double getOutValue() {
      return this.outValue;
   }

   public double getInTicks() {
      return (double)this.inTicks;
   }

   public double getOutTicks() {
      return (double)this.outTicks;
   }

   public NumberTangentInterpolator(Duration var1, double var2, Duration var4, double var5) {
      this.inTicks = TickCalculation.fromDuration(var1);
      this.inValue = var2;
      this.outTicks = TickCalculation.fromDuration(var4);
      this.outValue = var5;
   }

   public NumberTangentInterpolator(Duration var1, double var2) {
      this.outTicks = this.inTicks = TickCalculation.fromDuration(var1);
      this.inValue = this.outValue = var2;
   }

   public String toString() {
      return "NumberTangentInterpolator [inValue=" + this.inValue + ", inDuration=" + TickCalculation.toDuration(this.inTicks) + ", outValue=" + this.outValue + ", outDuration=" + TickCalculation.toDuration(this.outTicks) + "]";
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 59 * var1 + (int)(Double.doubleToLongBits(this.inValue) ^ Double.doubleToLongBits(this.inValue) >>> 32);
      var1 = 59 * var1 + (int)(Double.doubleToLongBits(this.outValue) ^ Double.doubleToLongBits(this.outValue) >>> 32);
      var1 = 59 * var1 + (int)(this.inTicks ^ this.inTicks >>> 32);
      var1 = 59 * var1 + (int)(this.outTicks ^ this.outTicks >>> 32);
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         NumberTangentInterpolator var2 = (NumberTangentInterpolator)var1;
         if (Double.doubleToLongBits(this.inValue) != Double.doubleToLongBits(var2.inValue)) {
            return false;
         } else if (Double.doubleToLongBits(this.outValue) != Double.doubleToLongBits(var2.outValue)) {
            return false;
         } else if (this.inTicks != var2.inTicks) {
            return false;
         } else {
            return this.outTicks == var2.outTicks;
         }
      }
   }

   protected double curve(double var1) {
      return var1;
   }
}
