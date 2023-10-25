package org.apache.commons.lang3.mutable;

public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {
  private static final long serialVersionUID = -2135791679L;
  
  private short value;
  
  public MutableShort() {}
  
  public MutableShort(short value) {
    this.value = value;
  }
  
  public MutableShort(Number value) {
    this.value = value.shortValue();
  }
  
  public MutableShort(String value) throws NumberFormatException {
    this.value = Short.parseShort(value);
  }
  
  public Short getValue() {
    return Short.valueOf(this.value);
  }
  
  public void setValue(short value) {
    this.value = value;
  }
  
  public void setValue(Number value) {
    this.value = value.shortValue();
  }
  
  public void increment() {
    this.value = (short)(this.value + 1);
  }
  
  public void decrement() {
    this.value = (short)(this.value - 1);
  }
  
  public void add(short operand) {
    this.value = (short)(this.value + operand);
  }
  
  public void add(Number operand) {
    this.value = (short)(this.value + operand.shortValue());
  }
  
  public void subtract(short operand) {
    this.value = (short)(this.value - operand);
  }
  
  public void subtract(Number operand) {
    this.value = (short)(this.value - operand.shortValue());
  }
  
  public short shortValue() {
    return this.value;
  }
  
  public int intValue() {
    return this.value;
  }
  
  public long longValue() {
    return this.value;
  }
  
  public float floatValue() {
    return this.value;
  }
  
  public double doubleValue() {
    return this.value;
  }
  
  public Short toShort() {
    return Short.valueOf(shortValue());
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof MutableShort)
      return (this.value == ((MutableShort)obj).shortValue()); 
    return false;
  }
  
  public int hashCode() {
    return this.value;
  }
  
  public int compareTo(MutableShort other) {
    short anotherVal = other.value;
    return (this.value < anotherVal) ? -1 : ((this.value == anotherVal) ? 0 : 1);
  }
  
  public String toString() {
    return String.valueOf(this.value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\mutable\MutableShort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */