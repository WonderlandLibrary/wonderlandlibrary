package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class EmptyImmutableBiMap extends ImmutableBiMap {
   static final EmptyImmutableBiMap INSTANCE = new EmptyImmutableBiMap();

   private EmptyImmutableBiMap() {
   }

   public ImmutableBiMap inverse() {
      return this;
   }

   public int size() {
      return 0;
   }

   public boolean isEmpty() {
      return true;
   }

   public Object get(@Nullable Object var1) {
      return null;
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

   public ImmutableSet keySet() {
      return ImmutableSet.of();
   }

   boolean isPartialView() {
      return false;
   }

   Object readResolve() {
      return INSTANCE;
   }

   public BiMap inverse() {
      return this.inverse();
   }

   public Set entrySet() {
      return this.entrySet();
   }

   public Set keySet() {
      return this.keySet();
   }
}
