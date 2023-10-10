package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.ObjectUtils;

public class ConstantInitializer implements ConcurrentInitializer {
   private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";
   private final Object object;

   public ConstantInitializer(Object var1) {
      this.object = var1;
   }

   public final Object getObject() {
      return this.object;
   }

   public Object get() throws ConcurrentException {
      return this.getObject();
   }

   public int hashCode() {
      return this.getObject() != null ? this.getObject().hashCode() : 0;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ConstantInitializer)) {
         return false;
      } else {
         ConstantInitializer var2 = (ConstantInitializer)var1;
         return ObjectUtils.equals(this.getObject(), var2.getObject());
      }
   }

   public String toString() {
      return String.format("ConstantInitializer@%d [ object = %s ]", System.identityHashCode(this), String.valueOf(this.getObject()));
   }
}
