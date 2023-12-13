package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractSortedSetMultimap extends AbstractSetMultimap implements SortedSetMultimap {
   private static final long serialVersionUID = 430848587173315748L;

   protected AbstractSortedSetMultimap(Map var1) {
      super(var1);
   }

   abstract SortedSet createCollection();

   SortedSet createUnmodifiableEmptyCollection() {
      Comparator var1 = this.valueComparator();
      return (SortedSet)(var1 == null ? Collections.unmodifiableSortedSet(this.createCollection()) : ImmutableSortedSet.emptySet(this.valueComparator()));
   }

   public SortedSet get(@Nullable Object var1) {
      return (SortedSet)super.get(var1);
   }

   public SortedSet removeAll(@Nullable Object var1) {
      return (SortedSet)super.removeAll(var1);
   }

   public SortedSet replaceValues(@Nullable Object var1, Iterable var2) {
      return (SortedSet)super.replaceValues(var1, var2);
   }

   public Map asMap() {
      return super.asMap();
   }

   public Collection values() {
      return super.values();
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

   Set createUnmodifiableEmptyCollection() {
      return this.createUnmodifiableEmptyCollection();
   }

   Set createCollection() {
      return this.createCollection();
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

   Collection createCollection() {
      return this.createCollection();
   }

   Collection createUnmodifiableEmptyCollection() {
      return this.createUnmodifiableEmptyCollection();
   }
}
