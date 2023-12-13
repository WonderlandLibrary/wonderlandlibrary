package org.apache.commons.lang3.mutable;

public class MutableDouble extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = 1587163916L;
   private double value;

   public MutableDouble() {
   }

   public MutableDouble(double var1) {
      this.value = var1;
   }

   public MutableDouble(Number var1) {
      this.value = var1.doubleValue();
   }

   public MutableDouble(String var1) throws NumberFormatException {
      this.value = Double.parseDouble(var1);
   }

   public Double getValue() {
      return this.value;
   }

   public void setValue(double var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.doubleValue();
   }

   public boolean isNaN() {
      return Double.isNaN(this.value);
   }

   public boolean isInfinite() {
      return Double.isInfinite(this.value);
   }

   public void increment() {
      ++this.value;
   }

   public void decrement() {
      --this.value;
   }

   public void add(double var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.doubleValue();
   }

   public void subtract(double var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.doubleValue();
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
      return this.doubleValue();
   }

   public boolean equals(Object var1) {
      return var1 instanceof MutableDouble && Double.doubleToLongBits(((MutableDouble)var1).value) == Double.doubleToLongBits(this.value);
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.value);
      return (int)(var1 ^ var1 >>> 32);
   }

   public int compareTo(MutableDouble var1) {
      double var2 = var1.value;
      return Double.compare(this.value, var2);
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public int compareTo(Object var1) {
      return this.compareTo((MutableDouble)var1);
   }

   public void setValue(Object var1) {
      this.setValue((Number)var1);
   }

   public Object getValue() {
      return this.getValue();
   }
}
