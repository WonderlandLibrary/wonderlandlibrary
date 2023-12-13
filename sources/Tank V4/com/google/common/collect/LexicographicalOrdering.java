package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class LexicographicalOrdering extends Ordering implements Serializable {
   final Ordering elementOrder;
   private static final long serialVersionUID = 0L;

   LexicographicalOrdering(Ordering var1) {
      this.elementOrder = var1;
   }

   public int compare(Iterable var1, Iterable var2) {
      Iterator var3 = var1.iterator();
      Iterator var4 = var2.iterator();

      int var5;
      do {
         if (!var3.hasNext()) {
            if (var4.hasNext()) {
               return -1;
            }

            return 0;
         }

         if (!var4.hasNext()) {
            return 1;
         }

         var5 = this.elementOrder.compare(var3.next(), var4.next());
      } while(var5 == 0);

      return var5;
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof LexicographicalOrdering) {
         LexicographicalOrdering var2 = (LexicographicalOrdering)var1;
         return this.elementOrder.equals(var2.elementOrder);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.elementOrder.hashCode() ^ 2075626741;
   }

   public String toString() {
      return this.elementOrder + ".lexicographical()";
   }

   public int compare(Object var1, Object var2) {
      return this.compare((Iterable)var1, (Iterable)var2);
   }
}
