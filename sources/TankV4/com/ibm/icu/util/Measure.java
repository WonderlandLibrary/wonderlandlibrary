package com.ibm.icu.util;

public abstract class Measure {
   private Number number;
   private MeasureUnit unit;

   protected Measure(Number var1, MeasureUnit var2) {
      if (var1 != null && var2 != null) {
         this.number = var1;
         this.unit = var2;
      } else {
         throw new NullPointerException();
      }
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else {
         try {
            Measure var2 = (Measure)var1;
            Number var10000;
            if (this.unit.equals(var2.unit)) {
               var10000 = this.number;
               if (var2.number != false) {
                  boolean var10001 = true;
                  return (boolean)var10000;
               }
            }

            var10000 = false;
            return (boolean)var10000;
         } catch (ClassCastException var3) {
            return false;
         }
      }
   }

   public int hashCode() {
      return this.number.hashCode() ^ this.unit.hashCode();
   }

   public String toString() {
      return this.number.toString() + ' ' + this.unit.toString();
   }

   public Number getNumber() {
      return this.number;
   }

   public MeasureUnit getUnit() {
      return this.unit;
   }
}
