package com.ibm.icu.util;

public class CurrencyAmount extends Measure {
  public CurrencyAmount(Number number, Currency currency) {
    super(number, currency);
  }
  
  public CurrencyAmount(double number, Currency currency) {
    super(new Double(number), currency);
  }
  
  public Currency getCurrency() {
    return (Currency)getUnit();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\CurrencyAmount.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */