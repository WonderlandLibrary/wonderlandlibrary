package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
final class FilteredKeySetMultimap extends FilteredKeyMultimap implements FilteredSetMultimap {
   FilteredKeySetMultimap(SetMultimap var1, Predicate var2) {
      super(var1, var2);
   }

   public SetMultimap unfiltered() {
      return (SetMultimap)this.unfiltered;
   }

   public Set get(Object var1) {
      return (Set)super.get(var1);
   }

   public Set removeAll(Object var1) {
      return (Set)super.removeAll(var1);
   }

   public Set replaceValues(Object var1, Iterable var2) {
      return (Set)super.replaceValues(var1, var2);
   }

   public Set entries() {
      return (Set)super.entries();
   }

   Set createEntries() {
      return new FilteredKeySetMultimap.EntrySet(this);
   }

   Collection createEntries() {
      return this.createEntries();
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Multimap unfiltered() {
      return this.unfiltered();
   }

   public Collection entries() {
      return this.entries();
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   class EntrySet extends FilteredKeyMultimap.Entries implements Set {
      final FilteredKeySetMultimap this$0;

      EntrySet(FilteredKeySetMultimap var1) {
         super();
         this.this$0 = var1;
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }
   }
}
