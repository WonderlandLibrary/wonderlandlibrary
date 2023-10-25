package org.apache.commons.lang3.mutable;

public class MutableDouble extends Number implements Comparable<MutableDouble>, Mutable<Number> {
  private static final long serialVersionUID = 1587163916L;
  
  private double value;
  
  public MutableDouble() {}
  
  public MutableDouble(double value) {
    this.value = value;
  }
  
  public MutableDouble(Number value) {
    this.value = value.doubleValue();
  }
  
  public MutableDouble(String value) throws NumberFormatException {
    this.value = Double.parseDouble(value);
  }
  
  public Double getValue() {
    return Double.valueOf(this.value);
  }
  
  public void setValue(double value) {
    this.value = value;
  }
  
  public void setValue(Number value) {
    this.value = value.doubleValue();
  }
  
  public boolean isNaN() {
    return Double.isNaN(this.value);
  }
  
  public boolean isInfinite() {
    return Double.isInfinite(this.value);
  }
  
  public void increment() {
    this.value++;
  }
  
  public void decrement() {
    this.value--;
  }
  
  public void add(double operand) {
    this.value += operand;
  }
  
  public void add(Number operand) {
    this.value += operand.doubleValue();
  }
  
  public void subtract(double operand) {
    this.value -= operand;
  }
  
  public void subtract(Number operand) {
    this.value -= operand.doubleValue();
  }
  
  public int intValue() {
    return (int)this.value;
  }
  
  public long longValue() {
    return (long)this.value;
  }
  
  public float floatValue() {
    return (float)this.value;
  }
  
  public double doubleValue() {
    return this.value;
  }
  
  public Double toDouble() {
    return Double.valueOf(doubleValue());
  }
  
  public boolean equals(Object obj) {
    return (obj instanceof MutableDouble && Double.doubleToLongBits(((MutableDouble)obj).value) == Double.doubleToLongBits(this.value));
  }
  
  public int hashCode() {
    long bits = Double.doubleToLongBits(this.value);
    return (int)(bits ^ bits >>> 32L);
  }
  
  public int compareTo(MutableDouble other) {
    double anotherVal = other.value;
    return Double.compare(this.value, anotherVal);
  }
  
  public String toString() {
    return String.valueOf(this.value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\mutable\MutableDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */