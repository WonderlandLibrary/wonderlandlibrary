package com.ibm.icu.util;

public abstract class Measure {
  private Number number;
  
  private MeasureUnit unit;
  
  protected Measure(Number number, MeasureUnit unit) {
    if (number == null || unit == null)
      throw new NullPointerException(); 
    this.number = number;
    this.unit = unit;
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj == this)
      return true; 
    try {
      Measure m = (Measure)obj;
      return (this.unit.equals(m.unit) && numbersEqual(this.number, m.number));
    } catch (ClassCastException e) {
      return false;
    } 
  }
  
  private static boolean numbersEqual(Number a, Number b) {
    if (a.equals(b))
      return true; 
    if (a.doubleValue() == b.doubleValue())
      return true; 
    return false;
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\Measure.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */