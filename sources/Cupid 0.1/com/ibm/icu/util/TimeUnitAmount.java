package com.ibm.icu.util;

public class TimeUnitAmount extends Measure {
  public TimeUnitAmount(Number number, TimeUnit unit) {
    super(number, unit);
  }
  
  public TimeUnitAmount(double number, TimeUnit unit) {
    super(new Double(number), unit);
  }
  
  public TimeUnit getTimeUnit() {
    return (TimeUnit)getUnit();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\TimeUnitAmount.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */