package org.apache.commons.lang3.mutable;

public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {
  private static final long serialVersionUID = 62986528375L;
  
  private long value;
  
  public MutableLong() {}
  
  public MutableLong(long value) {
    this.value = value;
  }
  
  public MutableLong(Number value) {
    this.value = value.longValue();
  }
  
  public MutableLong(String value) throws NumberFormatException {
    this.value = Long.parseLong(value);
  }
  
  public Long getValue() {
    return Long.valueOf(this.value);
  }
  
  public void setValue(long value) {
    this.value = value;
  }
  
  public void setValue(Number value) {
    this.value = value.longValue();
  }
  
  public void increment() {
    this.value++;
  }
  
  public void decrement() {
    this.value--;
  }
  
  public void add(long operand) {
    this.value += operand;
  }
  
  public void add(Number operand) {
    this.value += operand.longValue();
  }
  
  public void subtract(long operand) {
    this.value -= operand;
  }
  
  public void subtract(Number operand) {
    this.value -= operand.longValue();
  }
  
  public int intValue() {
    return (int)this.value;
  }
  
  public long longValue() {
    return this.value;
  }
  
  public float floatValue() {
    return (float)this.value;
  }
  
  public double doubleValue() {
    return this.value;
  }
  
  public Long toLong() {
    return Long.valueOf(longValue());
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof MutableLong)
      return (this.value == ((MutableLong)obj).longValue()); 
    return false;
  }
  
  public int hashCode() {
    return (int)(this.value ^ this.value >>> 32L);
  }
  
  public int compareTo(MutableLong other) {
    long anotherVal = other.value;
    return (this.value < anotherVal) ? -1 : ((this.value == anotherVal) ? 0 : 1);
  }
  
  public String toString() {
    return String.valueOf(this.value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\mutable\MutableLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */