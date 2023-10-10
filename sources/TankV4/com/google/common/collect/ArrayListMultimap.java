package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class ArrayListMultimap extends AbstractListMultimap {
   private static final int DEFAULT_VALUES_PER_KEY = 3;
   @VisibleForTesting
   transient int expectedValuesPerKey;
   @GwtIncompatible("Not needed in emulated source.")
   private static final long serialVersionUID = 0L;

   public static ArrayListMultimap create() {
      return new ArrayListMultimap();
   }

   public static ArrayListMultimap create(int var0, int var1) {
      return new ArrayListMultimap(var0, var1);
   }

   public static ArrayListMultimap create(Multimap var0) {
      return new ArrayListMultimap(var0);
   }

   private ArrayListMultimap() {
      super(new HashMap());
      this.expectedValuesPerKey = 3;
   }

   private ArrayListMultimap(int var1, int var2) {
      super(Maps.newHashMapWithExpectedSize(var1));
      CollectPreconditions.checkNonnegative(var2, "expectedValuesPerKey");
      this.expectedValuesPerKey = var2;
   }

   private ArrayListMultimap(Multimap var1) {
      this(var1.keySet().size(), var1 instanceof ArrayListMultimap ? ((ArrayListMultimap)var1).expectedValuesPerKey : 3);
      this.putAll(var1);
   }

   List createCollection() {
      return new ArrayList(this.expectedValuesPerKey);
   }

   public void trimToSize() {
      Iterator var1 = this.backingMap().values().iterator();

      while(var1.hasNext()) {
         Collection var2 = (Collection)var1.next();
         ArrayList var3 = (ArrayList)var2;
         var3.trimToSize();
      }

   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeInt(this.expectedValuesPerKey);
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
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

   public Map asMap() {
      return super.asMap();
   }

   public boolean put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   public List replaceValues(Object var1, Iterable var2) {
      return super.replaceValues(var1, var2);
   }

   public List removeAll(Object var1) {
      return super.removeAll(var1);
   }

   public List get(Object var1) {
      return super.get(var1);
   }

   public Collection entries() {
      return super.entries();
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
