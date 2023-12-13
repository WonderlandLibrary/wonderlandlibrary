package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
final class SingletonImmutableBiMap extends ImmutableBiMap {
   final transient Object singleKey;
   final transient Object singleValue;
   transient ImmutableBiMap inverse;

   SingletonImmutableBiMap(Object var1, Object var2) {
      CollectPreconditions.checkEntryNotNull(var1, var2);
      this.singleKey = var1;
      this.singleValue = var2;
   }

   private SingletonImmutableBiMap(Object var1, Object var2, ImmutableBiMap var3) {
      this.singleKey = var1;
      this.singleValue = var2;
      this.inverse = var3;
   }

   SingletonImmutableBiMap(Entry var1) {
      this(var1.getKey(), var1.getValue());
   }

   public Object get(@Nullable Object var1) {
      return this.singleKey.equals(var1) ? this.singleValue : null;
   }

   public int size() {
      return 1;
   }

   public boolean containsKey(@Nullable Object var1) {
      return this.singleKey.equals(var1);
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.singleValue.equals(var1);
   }

   boolean isPartialView() {
      return false;
   }

   ImmutableSet createEntrySet() {
      return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
   }

   ImmutableSet createKeySet() {
      return ImmutableSet.of(this.singleKey);
   }

   public ImmutableBiMap inverse() {
      ImmutableBiMap var1 = this.inverse;
      return var1 == null ? (this.inverse = new SingletonImmutableBiMap(this.singleValue, this.singleKey, this)) : var1;
   }

   public BiMap inverse() {
      return this.inverse();
   }
}
