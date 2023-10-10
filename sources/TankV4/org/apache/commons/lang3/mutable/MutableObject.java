package org.apache.commons.lang3.mutable;

import java.io.Serializable;

public class MutableObject implements Mutable, Serializable {
   private static final long serialVersionUID = 86241875189L;
   private Object value;

   public MutableObject() {
   }

   public MutableObject(Object var1) {
      this.value = var1;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object var1) {
      this.value = var1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else if (this.getClass() == var1.getClass()) {
         MutableObject var2 = (MutableObject)var1;
         return this.value.equals(var2.value);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.value == null ? 0 : this.value.hashCode();
   }

   public String toString() {
      return this.value == null ? "null" : this.value.toString();
   }
}
