package com.sun.javafx.css;

import javafx.scene.text.Font;

public final class Size {
   private final double value;
   private final SizeUnits units;

   public Size(double var1, SizeUnits var3) {
      this.value = var1;
      this.units = var3 != null ? var3 : SizeUnits.PX;
   }

   public double getValue() {
      return this.value;
   }

   public SizeUnits getUnits() {
      return this.units;
   }

   public boolean isAbsolute() {
      return this.units.isAbsolute();
   }

   public double points(Font var1) {
      return this.points(1.0, var1);
   }

   public double points(double var1, Font var3) {
      return this.units.points(this.value, var1, var3);
   }

   public double pixels(double var1, Font var3) {
      return this.units.pixels(this.value, var1, var3);
   }

   public double pixels(Font var1) {
      return this.pixels(1.0, var1);
   }

   public double pixels(double var1) {
      return this.pixels(var1, (Font)null);
   }

   public double pixels() {
      return this.pixels(1.0, (Font)null);
   }

   public String toString() {
      return Double.toString(this.value) + this.units.toString();
   }

   public int hashCode() {
      long var1 = 17L;
      var1 = 37L * var1 + Double.doubleToLongBits(this.value);
      var1 = 37L * var1 + (long)this.units.hashCode();
      return (int)(var1 ^ var1 >> 32);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && var1.getClass() == this.getClass()) {
         Size var2 = (Size)var1;
         if (this.units != var2.units) {
            return false;
         } else if (this.value == var2.value) {
            return true;
         } else {
            label42: {
               if (this.value > 0.0) {
                  if (var2.value > 0.0) {
                     break label42;
                  }
               } else if (var2.value < 0.0) {
                  break label42;
               }

               return false;
            }

            double var10000;
            if (this.value > 0.0) {
               var10000 = this.value;
            } else {
               var10000 = -this.value;
            }

            if (var2.value > 0.0) {
               var10000 = var2.value;
            } else {
               var10000 = -var2.value;
            }

            double var7 = this.value - var2.value;
            return !(var7 < -1.0E-6) && !(1.0E-6 < var7);
         }
      } else {
         return false;
      }
   }
}
