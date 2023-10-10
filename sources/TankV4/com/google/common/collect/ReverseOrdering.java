package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class ReverseOrdering extends Ordering implements Serializable {
   final Ordering forwardOrder;
   private static final long serialVersionUID = 0L;

   ReverseOrdering(Ordering var1) {
      this.forwardOrder = (Ordering)Preconditions.checkNotNull(var1);
   }

   public int compare(Object var1, Object var2) {
      return this.forwardOrder.compare(var2, var1);
   }

   public Ordering reverse() {
      return this.forwardOrder;
   }

   public Object min(Object var1, Object var2) {
      return this.forwardOrder.max(var1, var2);
   }

   public Object min(Object var1, Object var2, Object var3, Object... var4) {
      return this.forwardOrder.max(var1, var2, var3, var4);
   }

   public Object min(Iterator var1) {
      return this.forwardOrder.max(var1);
   }

   public Object min(Iterable var1) {
      return this.forwardOrder.max(var1);
   }

   public Object max(Object var1, Object var2) {
      return this.forwardOrder.min(var1, var2);
   }

   public Object max(Object var1, Object var2, Object var3, Object... var4) {
      return this.forwardOrder.min(var1, var2, var3, var4);
   }

   public Object max(Iterator var1) {
      return this.forwardOrder.min(var1);
   }

   public Object max(Iterable var1) {
      return this.forwardOrder.min(var1);
   }

   public int hashCode() {
      return -this.forwardOrder.hashCode();
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof ReverseOrdering) {
         ReverseOrdering var2 = (ReverseOrdering)var1;
         return this.forwardOrder.equals(var2.forwardOrder);
      } else {
         return false;
      }
   }

   public String toString() {
      return this.forwardOrder + ".reverse()";
   }
}
