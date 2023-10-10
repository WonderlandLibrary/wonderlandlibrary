package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class EmptyImmutableSortedMap extends ImmutableSortedMap {
   private final transient ImmutableSortedSet keySet;

   EmptyImmutableSortedMap(Comparator var1) {
      this.keySet = ImmutableSortedSet.emptySet(var1);
   }

   EmptyImmutableSortedMap(Comparator var1, ImmutableSortedMap var2) {
      super(var2);
      this.keySet = ImmutableSortedSet.emptySet(var1);
   }

   public Object get(@Nullable Object var1) {
      return null;
   }

   public ImmutableSortedSet keySet() {
      return this.keySet;
   }

   public int size() {
      return 0;
   }

   public boolean isEmpty() {
      return true;
   }

   public ImmutableCollection values() {
      return ImmutableList.of();
   }

   public String toString() {
      return "{}";
   }

   boolean isPartialView() {
      return false;
   }

   public ImmutableSet entrySet() {
      return ImmutableSet.of();
   }

   ImmutableSet createEntrySet() {
      throw new AssertionError("should never be called");
   }

   public ImmutableSetMultimap asMultimap() {
      return ImmutableSetMultimap.of();
   }

   public ImmutableSortedMap headMap(Object var1, boolean var2) {
      Preconditions.checkNotNull(var1);
      return this;
   }

   public ImmutableSortedMap tailMap(Object var1, boolean var2) {
      Preconditions.checkNotNull(var1);
      return this;
   }

   ImmutableSortedMap createDescendingMap() {
      return new EmptyImmutableSortedMap(Ordering.from(this.comparator()).reverse(), this);
   }

   public NavigableMap tailMap(Object var1, boolean var2) {
      return this.tailMap(var1, var2);
   }

   public NavigableMap headMap(Object var1, boolean var2) {
      return this.headMap(var1, var2);
   }

   public Set entrySet() {
      return this.entrySet();
   }

   public Collection values() {
      return this.values();
   }

   public Set keySet() {
      return this.keySet();
   }

   public ImmutableSet keySet() {
      return this.keySet();
   }
}
