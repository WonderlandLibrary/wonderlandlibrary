package org.apache.commons.lang3.builder;

final class IDKey {
   private final Object value;
   private final int id;

   public IDKey(Object var1) {
      this.id = System.identityHashCode(var1);
      this.value = var1;
   }

   public int hashCode() {
      return this.id;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof IDKey)) {
         return false;
      } else {
         IDKey var2 = (IDKey)var1;
         if (this.id != var2.id) {
            return false;
         } else {
            return this.value == var2.value;
         }
      }
   }
}
