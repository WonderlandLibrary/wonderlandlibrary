package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class HashMultimap extends AbstractSetMultimap {
   private static final int DEFAULT_VALUES_PER_KEY = 2;
   @VisibleForTesting
   transient int expectedValuesPerKey = 2;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static HashMultimap create() {
      return new HashMultimap();
   }

   public static HashMultimap create(int var0, int var1) {
      return new HashMultimap(var0, var1);
   }

   public static HashMultimap create(Multimap var0) {
      return new HashMultimap(var0);
   }

   private HashMultimap() {
      super(new HashMap());
   }

   private HashMultimap(int var1, int var2) {
      super(Maps.newHashMapWithExpectedSize(var1));
      Preconditions.checkArgument(var2 >= 0);
      this.expectedValuesPerKey = var2;
   }

   private HashMultimap(Multimap var1) {
      super(Maps.newHashMapWithExpectedSize(var1.keySet().size()));
      this.putAll(var1);
   }

   Set createCollection() {
      return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.expectedValuesPerKey);
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.expectedValuesPerKey = var1.readInt();
      int var2 = Serialization.readCount(var1);
      HashMap var3 = Maps.newHashMapWithExpectedSize(var2);
      this.setMap(var3);
      Serialization.populateMultimap(this, var1, var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   public Map asMap() {
      return super.asMap();
   }

   public Set replaceValues(Object var1, Iterable var2) {
      return super.replaceValues(var1, var2);
   }

   public Set removeAll(Object var1) {
      return super.removeAll(var1);
   }

   public Set entries() {
      return super.entries();
   }

   public Set get(Object var1) {
      return super.get(var1);
   }

   public Collection values() {
      return super.values();
   }

   public void clear() {
      super.clear();
   }

   public boolean containsKey(Object var1) {
      return super.containsKey(var1);
   }

   public int size() {
      return super.size();
   }

   Collection createCollection() {
      return this.createCollection();
   }

   public String toString() {
      return super.toString();
   }

   public int hashCode() {
      return super.hashCode();
   }

   public Multiset keys() {
      return super.keys();
   }

   public Set keySet() {
      return super.keySet();
   }

   public boolean putAll(Multimap var1) {
      return super.putAll(var1);
   }

   public boolean putAll(Object var1, Iterable var2) {
      return super.putAll(var1, var2);
   }

   public boolean remove(Object var1, Object var2) {
      return super.remove(var1, var2);
   }

   public boolean containsEntry(Object var1, Object var2) {
      return super.containsEntry(var1, var2);
   }

   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }
}
