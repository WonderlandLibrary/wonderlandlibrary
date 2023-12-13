package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
class RegularImmutableMultiset extends ImmutableMultiset {
   private final transient ImmutableMap map;
   private final transient int size;

   RegularImmutableMultiset(ImmutableMap var1, int var2) {
      this.map = var1;
      this.size = var2;
   }

   boolean isPartialView() {
      return this.map.isPartialView();
   }

   public int count(@Nullable Object var1) {
      Integer var2 = (Integer)this.map.get(var1);
      return var2 == null ? 0 : var2;
   }

   public int size() {
      return this.size;
   }

   public boolean contains(@Nullable Object var1) {
      return this.map.containsKey(var1);
   }

   public ImmutableSet elementSet() {
      return this.map.keySet();
   }

   Multiset.Entry getEntry(int var1) {
      java.util.Map.Entry var2 = (java.util.Map.Entry)this.map.entrySet().asList().get(var1);
      return Multisets.immutableEntry(var2.getKey(), (Integer)var2.getValue());
   }

   public int hashCode() {
      return this.map.hashCode();
   }

   public Set elementSet() {
      return this.elementSet();
   }
}
