package org.apache.commons.lang3.mutable;

public class MutableFloat extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = 5787169186L;
   private float value;

   public MutableFloat() {
   }

   public MutableFloat(float var1) {
      this.value = var1;
   }

   public MutableFloat(Number var1) {
      this.value = var1.floatValue();
   }

   public MutableFloat(String var1) throws NumberFormatException {
      this.value = Float.parseFloat(var1);
   }

   public Float getValue() {
      return this.value;
   }

   public void setValue(float var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.floatValue();
   }

   public boolean isNaN() {
      return Float.isNaN(this.value);
   }

   public boolean isInfinite() {
      return Float.isInfinite(this.value);
   }

   public void increment() {
      ++this.value;
   }

   public void decrement() {
      --this.value;
   }

   public void add(float var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.floatValue();
   }

   public void subtract(float var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.floatValue();
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public float floatValue() {
      return this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public Float toFloat() {
      return this.floatValue();
   }

   public boolean equals(Object var1) {
      return var1 instanceof MutableFloat && Float.floatToIntBits(((MutableFloat)var1).value) == Float.floatToIntBits(this.value);
   }

   public int hashCode() {
      return Float.floatToIntBits(this.value);
   }

   public int compareTo(MutableFloat var1) {
      float var2 = var1.value;
      return Float.compare(this.value, var2);
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public int compareTo(Object var1) {
      return this.compareTo((MutableFloat)var1);
   }

   public void setValue(Object var1) {
      this.setValue((Number)var1);
   }

   public Object getValue() {
      return this.getValue();
   }
}
