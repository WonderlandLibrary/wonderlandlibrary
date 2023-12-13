package com.ibm.icu.util;

public class CurrencyAmount extends Measure {
   public CurrencyAmount(Number var1, Currency var2) {
      super(var1, var2);
   }

   public CurrencyAmount(double var1, Currency var3) {
      super(new Double(var1), var3);
   }

   public Currency getCurrency() {
      return (Currency)this.getUnit();
   }
}
