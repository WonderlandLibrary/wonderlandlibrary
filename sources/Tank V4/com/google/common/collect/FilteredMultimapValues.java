package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
final class FilteredMultimapValues extends AbstractCollection {
   private final FilteredMultimap multimap;

   FilteredMultimapValues(FilteredMultimap var1) {
      this.multimap = (FilteredMultimap)Preconditions.checkNotNull(var1);
   }

   public Iterator iterator() {
      return Maps.valueIterator(this.multimap.entries().iterator());
   }

   public boolean contains(@Nullable Object var1) {
      return this.multimap.containsValue(var1);
   }

   public int size() {
      return this.multimap.size();
   }

   public boolean remove(@Nullable Object var1) {
      Predicate var2 = this.multimap.entryPredicate();
      Iterator var3 = this.multimap.unfiltered().entries().iterator();

      Entry var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (Entry)var3.next();
      } while(!var2.apply(var4) || !Objects.equal(var4.getValue(), var1));

      var3.remove();
      return true;
   }

   public boolean removeAll(Collection var1) {
      return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(var1))));
   }

   public boolean retainAll(Collection var1) {
      return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(var1)))));
   }

   public void clear() {
      this.multimap.clear();
   }
}
