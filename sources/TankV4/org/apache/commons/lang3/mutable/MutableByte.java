package org.apache.commons.lang3.mutable;

public class MutableByte extends Number implements Comparable, Mutable {
   private static final long serialVersionUID = -1585823265L;
   private byte value;

   public MutableByte() {
   }

   public MutableByte(byte var1) {
      this.value = var1;
   }

   public MutableByte(Number var1) {
      this.value = var1.byteValue();
   }

   public MutableByte(String var1) throws NumberFormatException {
      this.value = Byte.parseByte(var1);
   }

   public Byte getValue() {
      return this.value;
   }

   public void setValue(byte var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.byteValue();
   }

   public void increment() {
      ++this.value;
   }

   public void decrement() {
      --this.value;
   }

   public void add(byte var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.byteValue();
   }

   public void subtract(byte var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.byteValue();
   }

   public byte byteValue() {
      return this.value;
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

   public Byte toByte() {
      return this.byteValue();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof MutableByte) {
         return this.value == ((MutableByte)var1).byteValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value;
   }

   public int compareTo(MutableByte var1) {
      byte var2 = var1.value;
      return this.value < var2 ? -1 : (this.value == var2 ? 0 : 1);
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public int compareTo(Object var1) {
      return this.compareTo((MutableByte)var1);
   }

   public void setValue(Object var1) {
      this.setValue((Number)var1);
   }

   public Object getValue() {
      return this.getValue();
   }
}
