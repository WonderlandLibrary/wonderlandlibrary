package org.apache.commons.lang3.mutable;

import java.io.Serializable;

public class MutableBoolean implements Mutable, Serializable, Comparable {
   private static final long serialVersionUID = -4830728138360036487L;
   private boolean value;

   public MutableBoolean() {
   }

   public MutableBoolean(boolean var1) {
      this.value = var1;
   }

   public MutableBoolean(Boolean var1) {
      this.value = var1;
   }

   public Boolean getValue() {
      return this.value;
   }

   public void setValue(boolean var1) {
      this.value = var1;
   }

   public void setFalse() {
      this.value = false;
   }

   public void setTrue() {
      this.value = true;
   }

   public void setValue(Boolean var1) {
      this.value = var1;
   }

   public boolean isTrue() {
      return this.value;
   }

   public boolean isFalse() {
      return !this.value;
   }

   public boolean booleanValue() {
      return this.value;
   }

   public Boolean toBoolean() {
      return this.booleanValue();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof MutableBoolean) {
         return this.value == ((MutableBoolean)var1).booleanValue();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
   }

   public int compareTo(MutableBoolean var1) {
      boolean var2 = var1.value;
      return this.value == var2 ? 0 : (this.value ? 1 : -1);
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public void setValue(Object var1) {
      this.setValue((Boolean)var1);
   }

   public Object getValue() {
      return this.getValue();
   }

   public int compareTo(Object var1) {
      return this.compareTo((MutableBoolean)var1);
   }
}
