package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
final class AllEqualOrdering extends Ordering implements Serializable {
   static final AllEqualOrdering INSTANCE = new AllEqualOrdering();
   private static final long serialVersionUID = 0L;

   public int compare(@Nullable Object var1, @Nullable Object var2) {
      return 0;
   }

   public List sortedCopy(Iterable var1) {
      return Lists.newArrayList(var1);
   }

   public ImmutableList immutableSortedCopy(Iterable var1) {
      return ImmutableList.copyOf(var1);
   }

   public Ordering reverse() {
      return this;
   }

   private Object readResolve() {
      return INSTANCE;
   }

   public String toString() {
      return "Ordering.allEqual()";
   }
}
