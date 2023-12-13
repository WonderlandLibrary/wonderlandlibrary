package org.apache.commons.lang3.mutable;

public class MutableInt extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = 512176391864L;
   private int value;

   public MutableInt() {
   }

   public MutableInt(int var1) {
      this.value = var1;
   }

   public MutableInt(Number var1) {
      this.value = var1.intValue();
   }

   public MutableInt(String var1) throws NumberFormatException {
      this.value = Integer.parseInt(var1);
   }

   public Integer getValue() {
      return this.value;
   }

   public void setValue(int var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.intValue();
   }

   public void increment() {
      ++this.value;
   }

   public void decrement() {
      --this.value;
   }

   public void add(int var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.intValue();
   }

   public void subtract(int var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.intValue();
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public Integer toInteger() {
      return this.intValue();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof MutableInt) {
         return this.value == ((MutableInt)var1).intValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value;
   }

   public int compareTo(MutableInt var1) {
      int var2 = var1.value;
      return this.value < var2 ? -1 : (this.value == var2 ? 0 : 1);
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public int compareTo(Object var1) {
      return this.compareTo((MutableInt)var1);
   }

   public void setValue(Object var1) {
      this.setValue((Number)var1);
   }

   public Object getValue() {
      return this.getValue();
   }
}
