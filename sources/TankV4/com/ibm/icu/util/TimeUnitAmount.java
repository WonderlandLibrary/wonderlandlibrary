package com.ibm.icu.util;

public class TimeUnitAmount extends Measure {
   public TimeUnitAmount(Number var1, TimeUnit var2) {
      super(var1, var2);
   }

   public TimeUnitAmount(double var1, TimeUnit var3) {
      super(new Double(var1), var3);
   }

   public TimeUnit getTimeUnit() {
      return (TimeUnit)this.getUnit();
   }
}
