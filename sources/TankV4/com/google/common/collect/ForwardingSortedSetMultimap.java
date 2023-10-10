package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSortedSetMultimap extends ForwardingSetMultimap implements SortedSetMultimap {
   protected ForwardingSortedSetMultimap() {
   }

   protected abstract SortedSetMultimap delegate();

   public SortedSet get(@Nullable Object var1) {
      return this.delegate().get(var1);
   }

   public SortedSet removeAll(@Nullable Object var1) {
      return this.delegate().removeAll(var1);
   }

   public SortedSet replaceValues(Object var1, Iterable var2) {
      return this.delegate().replaceValues(var1, var2);
   }

   public Comparator valueComparator() {
      return this.delegate().valueComparator();
   }

   public Set replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public Set removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Set get(Object var1) {
      return this.get(var1);
   }

   protected SetMultimap delegate() {
      return this.delegate();
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   protected Multimap delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
