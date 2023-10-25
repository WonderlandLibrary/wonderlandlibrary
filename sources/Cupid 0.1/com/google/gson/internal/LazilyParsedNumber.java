package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;

public final class LazilyParsedNumber extends Number {
  private final String value;
  
  public LazilyParsedNumber(String value) {
    this.value = value;
  }
  
  public int intValue() {
    try {
      return Integer.parseInt(this.value);
    } catch (NumberFormatException e) {
      try {
        return (int)Long.parseLong(this.value);
      } catch (NumberFormatException nfe) {
        return (new BigDecimal(this.value)).intValue();
      } 
    } 
  }
  
  public long longValue() {
    try {
      return Long.parseLong(this.value);
    } catch (NumberFormatException e) {
      return (new BigDecimal(this.value)).longValue();
    } 
  }
  
  public float floatValue() {
    return Float.parseFloat(this.value);
  }
  
  public double doubleValue() {
    return Double.parseDouble(this.value);
  }
  
  public String toString() {
    return this.value;
  }
  
  private Object writeReplace() throws ObjectStreamException {
    return new BigDecimal(this.value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\LazilyParsedNumber.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */