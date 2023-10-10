package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
class FilteredKeyMultimap extends AbstractMultimap implements FilteredMultimap {
   final Multimap unfiltered;
   final Predicate keyPredicate;

   FilteredKeyMultimap(Multimap var1, Predicate var2) {
      this.unfiltered = (Multimap)Preconditions.checkNotNull(var1);
      this.keyPredicate = (Predicate)Preconditions.checkNotNull(var2);
   }

   public Multimap unfiltered() {
      return this.unfiltered;
   }

   public Predicate entryPredicate() {
      return Maps.keyPredicateOnEntries(this.keyPredicate);
   }

   public int size() {
      int var1 = 0;

      Collection var3;
      for(Iterator var2 = this.asMap().values().iterator(); var2.hasNext(); var1 += var3.size()) {
         var3 = (Collection)var2.next();
      }

      return var1;
   }

   public Collection removeAll(Object var1) {
      return var1 != false ? this.unfiltered.removeAll(var1) : this.unmodifiableEmptyCollection();
   }

   Collection unmodifiableEmptyCollection() {
      return (Collection)(this.unfiltered instanceof SetMultimap ? ImmutableSet.of() : ImmutableList.of());
   }

   public void clear() {
      this.keySet().clear();
   }

   Set createKeySet() {
      return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
   }

   public Collection get(Object var1) {
      if (this.keyPredicate.apply(var1)) {
         return this.unfiltered.get(var1);
      } else {
         return (Collection)(this.unfiltered instanceof SetMultimap ? new FilteredKeyMultimap.AddRejectingSet(var1) : new FilteredKeyMultimap.AddRejectingList(var1));
      }
   }

   Iterator entryIterator() {
      throw new AssertionError("should never be called");
   }

   Collection createEntries() {
      return new FilteredKeyMultimap.Entries(this);
   }

   Collection createValues() {
      return new FilteredMultimapValues(this);
   }

   Map createAsMap() {
      return Maps.filterKeys(this.unfiltered.asMap(), this.keyPredicate);
   }

   Multiset createKeys() {
      return Multisets.filter(this.unfiltered.keys(), this.keyPredicate);
   }

   class Entries extends ForwardingCollection {
      final FilteredKeyMultimap this$0;

      Entries(FilteredKeyMultimap var1) {
         this.this$0 = var1;
      }

      protected Collection delegate() {
         return Collections2.filter(this.this$0.unfiltered.entries(), this.this$0.entryPredicate());
      }

      public boolean remove(@Nullable Object var1) {
         if (var1 instanceof Entry) {
            Entry var2 = (Entry)var1;
            if (this.this$0.unfiltered.containsKey(var2.getKey()) && this.this$0.keyPredicate.apply(var2.getKey())) {
               return this.this$0.unfiltered.remove(var2.getKey(), var2.getValue());
            }
         }

         return false;
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   static class AddRejectingList extends ForwardingList {
      final Object key;

      AddRejectingList(Object var1) {
         this.key = var1;
      }

      public boolean add(Object var1) {
         this.add(0, var1);
         return true;
      }

      public boolean addAll(Collection var1) {
         this.addAll(0, var1);
         return true;
      }

      public void add(int var1, Object var2) {
         Preconditions.checkPositionIndex(var1, 0);
         throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
      }

      public boolean addAll(int var1, Collection var2) {
         Preconditions.checkNotNull(var2);
         Preconditions.checkPositionIndex(var1, 0);
         throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
      }

      protected List delegate() {
         return Collections.emptyList();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }

   static class AddRejectingSet extends ForwardingSet {
      final Object key;

      AddRejectingSet(Object var1) {
         this.key = var1;
      }

      public boolean add(Object var1) {
         throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
      }

      public boolean addAll(Collection var1) {
         Preconditions.checkNotNull(var1);
         throw new IllegalArgumentException("Key does not satisfy predicate: " + this.key);
      }

      protected Set delegate() {
         return Collections.emptySet();
      }

      protected Collection delegate() {
         return this.delegate();
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
