package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractSetMultimap extends AbstractMapBasedMultimap implements SetMultimap {
   private static final long serialVersionUID = 7431625294878419160L;

   protected AbstractSetMultimap(Map var1) {
      super(var1);
   }

   abstract Set createCollection();

   Set createUnmodifiableEmptyCollection() {
      return ImmutableSet.of();
   }

   public Set get(@Nullable Object var1) {
      return (Set)super.get(var1);
   }

   public Set entries() {
      return (Set)super.entries();
   }

   public Set removeAll(@Nullable Object var1) {
      return (Set)super.removeAll(var1);
   }

   public Set replaceValues(@Nullable Object var1, Iterable var2) {
      return (Set)super.replaceValues(var1, var2);
   }

   public Map asMap() {
      return super.asMap();
   }

   public boolean put(@Nullable Object var1, @Nullable Object var2) {
      return super.put(var1, var2);
   }

   public boolean equals(@Nullable Object var1) {
      return super.equals(var1);
   }

   public Collection entries() {
      return this.entries();
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
