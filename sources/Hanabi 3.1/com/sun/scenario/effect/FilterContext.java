package com.sun.scenario.effect;

public class FilterContext {
   private Object referent;

   protected FilterContext(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Referent must be non-null");
      } else {
         this.referent = var1;
      }
   }

   public final Object getReferent() {
      return this.referent;
   }

   public int hashCode() {
      return this.referent.hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof FilterContext)) {
         return false;
      } else {
         FilterContext var2 = (FilterContext)var1;
         return this.referent.equals(var2.referent);
      }
   }
}
