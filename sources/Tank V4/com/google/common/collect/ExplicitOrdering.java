package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class ExplicitOrdering extends Ordering implements Serializable {
   final ImmutableMap rankMap;
   private static final long serialVersionUID = 0L;

   ExplicitOrdering(List var1) {
      this(buildRankMap(var1));
   }

   ExplicitOrdering(ImmutableMap var1) {
      this.rankMap = var1;
   }

   public int compare(Object var1, Object var2) {
      return this.rank(var1) - this.rank(var2);
   }

   private int rank(Object var1) {
      Integer var2 = (Integer)this.rankMap.get(var1);
      if (var2 == null) {
         throw new Ordering.IncomparableValueException(var1);
      } else {
         return var2;
      }
   }

   private static ImmutableMap buildRankMap(List var0) {
      ImmutableMap.Builder var1 = ImmutableMap.builder();
      int var2 = 0;
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var1.put(var4, var2++);
      }

      return var1.build();
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof ExplicitOrdering) {
         ExplicitOrdering var2 = (ExplicitOrdering)var1;
         return this.rankMap.equals(var2.rankMap);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.rankMap.hashCode();
   }

   public String toString() {
      return "Ordering.explicit(" + this.rankMap.keySet() + ")";
   }
}
