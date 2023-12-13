package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.SortedSet;

@GwtCompatible
final class SortedIterables {
   private SortedIterables() {
   }

   public static boolean hasSameComparator(Comparator var0, Iterable var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Comparator var2;
      if (var1 instanceof SortedSet) {
         var2 = comparator((SortedSet)var1);
      } else {
         if (!(var1 instanceof SortedIterable)) {
            return false;
         }

         var2 = ((SortedIterable)var1).comparator();
      }

      return var0.equals(var2);
   }

   public static Comparator comparator(SortedSet var0) {
      Object var1 = var0.comparator();
      if (var1 == null) {
         var1 = Ordering.natural();
      }

      return (Comparator)var1;
   }
}
